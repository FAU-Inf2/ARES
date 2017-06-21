package de.fau.cs.inf2.cas.ares.pcreation;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresCaseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

class WildcardPlacementComputation {

  private static int handleInsert(BlockChanges change, BastEditOperation ep,
      AbstractBastNode parentNode, BastFieldConstants fieldConstant, int alternativeIndex) {
    if (!parentNode.getField(fieldConstant).isList()) {
      return 0;
    }
    LinkedList<? extends AbstractBastNode> list = parentNode.getField(fieldConstant).getListField();
    int pos = 0;
    int index = -1;
    if (alternativeIndex != -1) {
      index = alternativeIndex;
      return alternativeIndex;
    } else {
      index = ep.getOldOrChangedIndex().childrenListIndex;
    }
    for (int i = 0; i < list.size(); i++) {
      AbstractBastNode stmt = list.get(i);
      if (stmt.getTag() == AresUseStmt.TAG || stmt.getTag() == AresWildcard.TAG
          || stmt.getTag() == AresChoiceStmt.TAG) {
        if (i < index + pos) {
          if ((change.isExpressionWildcard(stmt) && !change.isInitialWildcard(stmt))
              || change.isAdditionalWildcard(stmt)) {
            pos++;
          }
        } else if (i == index + pos) {
          if ((change.isExpressionWildcard(stmt) && !change.isInitialWildcard(stmt))
              || change.isAdditionalWildcard(stmt)) {
            pos++;
          }
        }
      }
    }
    return pos + index;
  }

  private static int handleDelete(BlockChanges change, BastEditOperation ep,
      AbstractBastNode parentNode, BastFieldConstants fieldConstant, int alternativeIndex,
      ExtendedDiffResult extDiff, ArrayList<BastEditOperation> workList, MatchBoundary boundary) {
    LinkedList<? extends AbstractBastNode> list = parentNode.getField(fieldConstant).getListField();
    if (list == null) {
      return -1;
    }
    int pos = 0;
    int index = -1;
    if (alternativeIndex != -1) {
      index = alternativeIndex;
      return alternativeIndex;
    } else {
      index = ep.getOldOrChangedIndex().childrenListIndex;
    }
    for (int i = 0; i < list.size(); i++) {
      AbstractBastNode stmt = list.get(i);
      if (i < index + pos && stmt.getTag() == AresChoiceStmt.TAG) {
        pos++;
        continue;
      }
      if (stmt.getTag() == AresUseStmt.TAG || stmt.getTag() == AresWildcard.TAG
          || stmt.getTag() == AresChoiceStmt.TAG) {
        if (i < index + pos) {

          if (change.isInsertWildcard(stmt)) {
            if (!change.hasCorrespondingDelete(stmt, ep, workList, extDiff, boundary)) {
              change.hasCorrespondingDelete(stmt, ep, workList, extDiff, boundary);
            } else {
              pos++;
            }
          } else if (stmt.getTag() == AresChoiceStmt.TAG) {
            if ((((AresChoiceStmt) stmt).choiceBlock.statements.size()) > 0
                && ((AresCaseStmt) ((AresChoiceStmt) stmt).choiceBlock.statements
                    .get(0)).block.statements.size() > 1) {
              pos -= (((AresCaseStmt) ((AresChoiceStmt) stmt).choiceBlock.statements
                  .get(0)).block.statements.size()) - 1;
            }
          }
        } else if (i == index + pos) {

          if ((change.isAdditionalWildcard(stmt) && !change.isDeleteWildcard(stmt))
          ) {
            pos++;
          } else if (change.isExpressionWildcard(stmt)) {
            int stIndex = -1;
            for (int j = i + 1; j < list.size(); j++) {
              if (!WildcardAccessHelper.isWildcard(list.get(j))) {
                stIndex = j;
                break;
              }
            }
            if (stIndex != -1) {
              if (extDiff.secondToFirstMap.get(list.get(stIndex)) == null) {
                boolean inserted = false;
                BastEditOperation epDiff = extDiff.editMapOld.get(list.get(stIndex));
                if (epDiff != null) {
                  inserted = true;

                }
                if (!inserted) {
                  pos += (stIndex - i);
                }
              } else {
                boolean moved = false;
                BastEditOperation epDiff = extDiff.editMapOld.get(list.get(stIndex));
                if (epDiff != null && (epDiff.getType() == EditOperationType.MOVE
                    || epDiff.getType() == EditOperationType.STATEMENT_PARENT_CHANGE)) {
                  moved = true;
                }
                if (moved) {
                  pos += (stIndex - i);
                  break;
                }
              }
            }
          }
        }
      } else {
        BastEditOperation epDiff = extDiff.editMapNew.get(stmt);
        if (epDiff != null && epDiff.getType() == EditOperationType.STATEMENT_PARENT_CHANGE) {
          if (epDiff.getOldOrChangedIndex().childrenListIndex > epDiff
              .getNewOrChangedIndex().childrenListIndex) {
            if (ep.getOldOrChangedIndex().childrenListIndex < epDiff
                .getOldOrChangedIndex().childrenListIndex) {
              if (ep.getOldOrChangedIndex().childrenListIndex > epDiff
                  .getNewOrChangedIndex().childrenListIndex) {
                if (i < ep.getOldOrChangedIndex().childrenListIndex) {
                  return i;
                }
              }
            }
          }
        }
      }
    }
    pos = checkMinMaxPosition(index, pos, ep, parentNode, fieldConstant, extDiff, workList);
    if (pos + index > list.size()) {
      return list.size();
    } else {
      return pos + index;
    }
  }

  private static int handleMove(BlockChanges change, BastEditOperation ep,
      AbstractBastNode parentNode, BastFieldConstants fieldConstant, boolean newIndex,
      int alternativeIndex, ExtendedDiffResult exDiff, ArrayList<BastEditOperation> workList) {
    LinkedList<? extends AbstractBastNode> list = parentNode.getField(fieldConstant).getListField();
    int pos = 0;
    int index = -1;
    if (alternativeIndex != -1) {
      index = alternativeIndex;
      return alternativeIndex;
    } else {
      index = ep.getOldOrChangedIndex().childrenListIndex;
      if (newIndex) {
        index = ep.getNewOrChangedIndex().childrenListIndex;
      }
    }
    for (int i = 0; i < list.size(); i++) {
      AbstractBastNode stmt = list.get(i);
      if (stmt.getTag() == AresUseStmt.TAG || stmt.getTag() == AresWildcard.TAG
          || stmt.getTag() == AresChoiceStmt.TAG) {
        if (i < index + pos) {
          if ((change.isExpressionWildcard(stmt)
              && (!newIndex || !change.isInitialWildcard(stmt)))) {
            if (change.isExpressionWildcard(stmt) && change.isInitialWildcard(stmt)) {
              if (!newIndex) {
                pos++;
              }
            }
          } else if (change.isAdditionalWildcard(stmt)) {
            pos++;
          } else if (change.isExpressionWildcard(stmt) && (change.isInitialWildcard(stmt))
              && i > ep.getNewOrChangedIndex().childrenListIndex) {
            pos++;
          }
        } else if (i == index + pos) {
          if ((change.isExpressionWildcard(stmt) && (!newIndex && !change.isInitialWildcard(stmt)))
              || change.isAdditionalWildcard(stmt)) {
            pos++;
          } else if (change.isExpressionWildcard(stmt)
              && (!newIndex && change.isInitialWildcard(stmt))
              && i > ep.getNewOrChangedIndex().childrenListIndex) {
            pos++;
          }
        }
      }
    }
    if (!newIndex) {
      pos = checkMinMaxPosition(index, pos, ep, parentNode, fieldConstant, exDiff, workList);
    }
    if (pos + index > list.size()) {
      return list.size();
    } else {
      return pos + index;
    }
  }

  /**
   * Check min max position.
   *
   * @param index the index
   * @param pos the pos
   * @param ep the ep
   * @param parentNode the parent node
   * @param fieldConstant the field constant
   * @param exDiff the ex diff
   * @param workList the work list
   * @return the int
   */
  private static int checkMinMaxPosition(int index, int pos, BastEditOperation ep,
      AbstractBastNode parentNode, BastFieldConstants fieldConstant, ExtendedDiffResult exDiff,
      ArrayList<BastEditOperation> workList) {
    if (ep.getType() == EditOperationType.MOVE || ep.getType() == EditOperationType.DELETE
        || ep.getType() == EditOperationType.ALIGN) {
      if (ep.getUnchangedOrOldParentNode()
          .getField(ep.getOldOrChangedIndex().childrenListNumber) != null
          && ep.getUnchangedOrOldParentNode().getField(ep.getOldOrChangedIndex().childrenListNumber)
          .isList()) {
        LinkedList<? extends AbstractBastNode> stmtsOld = ep.getUnchangedOrOldParentNode()
            .getField(ep.getOldOrChangedIndex().childrenListNumber).getListField();
        LinkedList<? extends AbstractBastNode> stmtNew =
            parentNode.getField(fieldConstant).getListField();
        int posMin = Integer.MIN_VALUE;
        int posMax = Integer.MAX_VALUE;
        int offset = 0;
        
        for (int i = 0; i < stmtsOld.size(); i++) {
          AbstractBastNode node = stmtsOld.get(i);
          if (node != ep.getOldOrInsertedNode()) {
            if (exDiff.firstToSecondMap.get(node) != null) {
              if (stmtNew.contains(exDiff.firstToSecondMap.get(node))) {
                boolean oldNotInList = true;
                boolean newNotInList = true;
                for (BastEditOperation epWork : workList) {
                  if (epWork.getOldOrInsertedNode() == node) {
                    oldNotInList = false;
                  } else if (epWork.getOldOrInsertedNode() == exDiff.firstToSecondMap.get(node)) {
                    newNotInList = false;
                  }
                  if (epWork.getNewOrChangedNode() == node) {
                    oldNotInList = false;
                  } else if (epWork.getNewOrChangedNode() == exDiff.firstToSecondMap.get(node)) {
                    newNotInList = false;
                  }
                }
                if (exDiff.editMapOld.get(node) == null
                    || oldNotInList
                        && exDiff.editMapNew.get(exDiff.firstToSecondMap.get(node)) == null
                    || newNotInList) {
                  if (i < ep.getOldOrChangedIndex().childrenListIndex) {
                    if (ep.getType() == EditOperationType.DELETE) {
                      posMin = Math.max(posMin, stmtNew.indexOf(
                          exDiff.firstToSecondMap.get(node)) + 1);
                    } else {
                      posMin = Math.max(posMin, stmtNew.indexOf(
                          exDiff.firstToSecondMap.get(node)) + 1 - offset);
                    }
                  } else if (i > ep.getOldOrChangedIndex().childrenListIndex) {
                    boolean identicalStatements = false;
                    for (int j = 0; j < i; j++) {
                      if (WildcardAccessHelper.isEqual(stmtsOld.get(j), node)) {
                        identicalStatements = true;
                        break;
                      }
                    }
                    if (!identicalStatements) {
                      posMax = Math.min(posMax, stmtNew.indexOf(exDiff.firstToSecondMap.get(node)));
                    }
                  }
                }
              } else {
                if (exDiff.editMapOld.get(node) != null) {
                  if (exDiff.editMapOld.get(node).getType() == EditOperationType.MOVE
                      && exDiff.editMapOld.get(node).getOldOrChangedIndex().childrenListIndex 
                      < ep.getOldOrChangedIndex().childrenListIndex) {
                    offset++;
                    for (int j = 0; j < stmtNew.size(); j++) {
                      if (stmtNew.get(j).getTag() == AresChoiceStmt.TAG) {
                        if (WildcardAccessHelper.isPart(stmtNew.get(j),exDiff
                            .editMapOld.get(node).getNewOrChangedNode())) {
                          if (i <= ep.getOldOrChangedIndex().childrenListIndex) {
                            posMin = Math.max(posMin, j + 1);
                          } else if (i >= ep.getOldOrChangedIndex().childrenListIndex) {
                            posMax = Math.min(posMax, j + 1);
                          }
                        }
                      }
                    }
                  }
                }
              }
            } else {
              for (int j = 0; j < stmtNew.size(); j++) {
                if (WildcardAccessHelper.isEqual(stmtsOld.get(i), stmtNew.get(j))) {
                  if (i <= ep.getOldOrChangedIndex().childrenListIndex) {
                    posMin = Math.max(posMin, j + 1);
                  } else if (i >= ep.getOldOrChangedIndex().childrenListIndex) {
                    posMax = Math.min(posMax, j + 1);
                  }
                }
              }
            }
          } else {
            if (stmtNew.size() > i && i - offset >= 0) {
              if (WildcardAccessHelper.isEqual(stmtsOld.get(i), stmtNew.get(i - offset))) {
                posMax = i - offset;
              } else if (stmtsOld.size() == i + 1) {
                for (int j = i - offset; j < stmtNew.size(); j++) {
                  if (WildcardAccessHelper.isEqual(stmtsOld.get(i), stmtNew.get(j))) {
                    if (i <= ep.getOldOrChangedIndex().childrenListIndex) {
                      posMin = Math.max(posMin, j);
                      if (j == stmtNew.size() - 1) {
                        posMax = Math.min(posMax, j);
                      }
                    } else if (i >= ep.getOldOrChangedIndex().childrenListIndex) {
                      posMax = Math.min(posMax, j);
                    }
                  }
                }
              
              }
            }
          }
        }
        int epIndex = stmtsOld.indexOf(ep.getOldOrInsertedNode());
        if (epIndex != -1 && epIndex > 0) {
          AbstractBastNode tmp = null;
          for (int i = epIndex - 1; i >= 0; i--) {
            tmp = stmtsOld.get(i);
            if (exDiff.editMapOld.get(tmp) != null
                && exDiff.editMapOld.get(tmp).getType() == EditOperationType.ALIGN) {
              tmp = null;
            } else {
              break;
            }
          }
          if (tmp != null) {
            AbstractBastNode partner = exDiff.firstToSecondMap.get(tmp);
            if (partner != null) {
              int tmpPos = stmtNew.indexOf(partner);
              if (tmpPos > -1) {
                posMin = Math.max(posMin, tmpPos + 1);
              }
            }
          }
        }
        if (posMin < posMax) {
          if (index + pos < posMin) {
            pos = posMin - index;
          }
          if (index + pos > posMax) {
            pos = posMax - index;
          }
        }
        if (posMin == posMax) {
          if (index + pos > posMax) {
            pos = posMax - index;
          } else if (index + pos < posMin) {
            pos = posMin - index;
          }
        }
      }
    }
    return pos;
  }

  private static int handleReorder(BlockChanges change, BastEditOperation ep,
      AbstractBastNode parentNode, BastFieldConstants fieldConstant, boolean newIndex,
      int alternativeIndex) {
    LinkedList<? extends AbstractBastNode> list = parentNode.getField(fieldConstant).getListField();
    int pos = 0;
    int index = -1;
    if (alternativeIndex != -1) {
      index = alternativeIndex;
    } else {
      index = ep.getOldOrChangedIndex().childrenListIndex;
      if (newIndex) {
        index = ep.getNewOrChangedIndex().childrenListIndex;
      }
    }
    for (int i = 0; i < list.size(); i++) {
      AbstractBastNode stmt = list.get(i);
      if (stmt.getTag() == AresUseStmt.TAG || stmt.getTag() == AresWildcard.TAG
          || stmt.getTag() == AresChoiceStmt.TAG) {
        if (i < index + pos) {
          if (change.isExpressionWildcard(stmt) || change.isAdditionalWildcard(stmt)) {
            if (!change.isMoveWildcard(stmt)) {
              pos++;
            }
          }
        } else if (pos == 0 && i == index) {
          if (change.isExpressionWildcard(stmt) || change.isAdditionalWildcard(stmt)) {
            pos++;
          }
        }

      }
    }
    return pos + index;
  }

  static int getPosition(BastEditOperation ep, AbstractBastNode parentNode,
      BastFieldConstants fieldConstant, HashMap<AbstractBastNode, BlockChanges> changeMap,
      boolean newIndex, ExtendedDiffResult extDiff, ArrayList<BastEditOperation> workList,
      MatchBoundary boundary) {
    return getPosition(ep, parentNode, fieldConstant, changeMap, newIndex, -1, extDiff, workList,
        boundary);
  }

  static int getPosition(BastEditOperation ep, AbstractBastNode parentNode,
      BastFieldConstants fieldConstant, HashMap<AbstractBastNode, BlockChanges> changeMap,
      boolean newIndex, int index, ExtendedDiffResult extDiff,
      ArrayList<BastEditOperation> workList, MatchBoundary boundary) {
    BlockChanges change = changeMap.get(parentNode);
    if (change == null) {
      change = new BlockChanges(parentNode, fieldConstant);
      changeMap.put(parentNode, change);
    }
    switch (ep.getType()) {
      case INSERT:
      case STATEMENT_INSERT:
        return handleInsert(change, ep, parentNode, fieldConstant, index);
      case MOVE:
      case STATEMENT_PARENT_CHANGE:
        return handleMove(change, ep, parentNode, fieldConstant, newIndex, index, extDiff,
            workList);
      case ALIGN:
      case STATEMENT_REORDERING:
        if (newIndex) {
          return handleReorder(change, ep, parentNode, fieldConstant, newIndex, index);
        } else {
          return handleMove(change, ep, parentNode, fieldConstant, newIndex, index, extDiff,
              workList);

        }
      case DELETE:
      case STATEMENT_DELETE:
        return handleDelete(change, ep, parentNode, fieldConstant, index, extDiff, workList, 
            boundary);
      default:
        assert (false);
        break;

    }
    return -1;
  }

  static void updateExpressionWildcard(AbstractBastNode parentNode,
      BastFieldConstants fieldConstant, AbstractBastNode node, AbstractBastNode wildcard,
      HashMap<AbstractBastNode, BlockChanges> changeMap, MatchBoundary boundary, int pos) {
    BlockChanges change = changeMap.get(parentNode);
    if (change == null) {
      change = new BlockChanges(parentNode, fieldConstant);
      changeMap.put(parentNode, change);
    }
    change.updateExpressionWildcard(node, wildcard);

  }

  static void updateReplacedStatement(AbstractBastNode parentNode, BastFieldConstants fieldConstant,
      AbstractBastNode node, AbstractBastNode wildcard, BastEditOperation ep,
      HashMap<AbstractBastNode, BlockChanges> changeMap, ReplaceMap replaceMap) {
    BlockChanges change = changeMap.get(parentNode);
    if (change == null) {
      change = new BlockChanges(parentNode, fieldConstant);
      changeMap.put(parentNode, change);
    }
    change.updateReplacedStatement(node, wildcard, ep);
    if (WildcardAccessHelper.isWildcard(node)) {
      LinkedList<AbstractBastNode> nodesToReplace = new LinkedList<>();
      for (Entry<AbstractBastNode, AbstractBastNode> r : replaceMap.entrySet()) {
        if (r.getValue() == node) {
          nodesToReplace.add(r.getKey());
        }
      }
      for (AbstractBastNode n : nodesToReplace) {
        replaceMap.put(n, wildcard, replaceMap.belongsToExpr(n));
      }
    }
  }

  static void additionalWildcards(AbstractBastNode parentNode, BastFieldConstants fieldConstant,
      AbstractBastNode wildcard, BastEditOperation ep,
      HashMap<AbstractBastNode, BlockChanges> changeMap, boolean delInsWildcard,
      MatchBoundary boundary, int pos) {
    BlockChanges change = changeMap.get(parentNode);
    if (change == null) {
      change = new BlockChanges(parentNode, fieldConstant);
      changeMap.put(parentNode, change);
    }
    change.additionalWildcards(wildcard, ep, delInsWildcard);

  }

}
