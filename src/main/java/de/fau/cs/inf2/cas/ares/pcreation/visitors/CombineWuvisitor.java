package de.fau.cs.inf2.cas.ares.pcreation.visitors;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.BlockChanges;
import de.fau.cs.inf2.cas.ares.pcreation.CombinationHelper;
import de.fau.cs.inf2.cas.ares.pcreation.MatchBoundary;
import de.fau.cs.inf2.cas.ares.pcreation.PatternGenerator;
import de.fau.cs.inf2.cas.ares.pcreation.ReplaceMap;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAsgnExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class CombineWuvisitor extends AresDefaultFieldVisitor {

  public ReplaceMap replaceMap;
  private ConnectWildcardAndUseVisitor cwuv;

  private boolean onlyEmptyExpr = false;
  private final int tag;
  private ExtendedDiffResult extDiff;

  /**
   * Instantiates a new combine wuvisitor.
   *
   * @param replaceMap the replace map
   * @param boundary the boundary
   * @param cwuv the cwuv
   * @param onlyEmptyExpr the only empty expr
   * @param tagPar the tag
   * @param containsAresBlock the contains ARES block
   * @param changeMap the change map
   */
  public CombineWuvisitor(ReplaceMap replaceMap, MatchBoundary boundary,
      ConnectWildcardAndUseVisitor cwuv, boolean onlyEmptyExpr, int tagPar,
      boolean containsAresBlock, HashMap<AbstractBastNode, BlockChanges> changeMap,
      ExtendedDiffResult extDiff) {
    this.replaceMap = replaceMap;
    this.cwuv = cwuv;
    this.onlyEmptyExpr = onlyEmptyExpr;
    this.tag = tagPar;
    this.extDiff = extDiff;
  }


  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {

  }


  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {

  }

  private BastField removeCalls(LinkedList<? extends AbstractBastNode> nodes) {
    int containsWildcard = 0;
    for (AbstractBastNode expr : nodes) {
      if (expr.getTag() == tag) {
        containsWildcard++;
      }
    }
    if (containsWildcard > 1) {
      LinkedList<AbstractBastNode> newNodes = new LinkedList<>();
      newNodes.addAll(nodes);
      boolean globalChange = false;
      boolean changed = true;
      int index = -1;
      while (changed || index < newNodes.size()) {
        changed = false;

        index++;

        boolean useSelected = false;
        for (int i = index; i < newNodes.size(); i++) {
          if (newNodes.get(i).getTag() == BastCall.TAG) {
            index = i;
            useSelected = true;
            break;
          }
        }
        if (!useSelected) {
          break;
        }
        BastCall call = (BastCall) newNodes.get(index);
        LinkedList<AbstractBastNode> wildcardStmts = new LinkedList<AbstractBastNode>();
        for (int j = index - 1; j >= 0; j--) {
          if (WildcardAccessHelper.isExprWildcard(nodes.get(j))) {
            wildcardStmts.add((AbstractBastNode) nodes.get(j));
          } else {
            break;
          }
        }
        if (wildcardStmts.size() == 0) {
          continue;
        }
        boolean function = false;
        boolean[] arguments = new boolean[call.arguments.size()];
        for (AbstractBastNode stmt : wildcardStmts) {
          if (WildcardAccessHelper.isEqual(WildcardAccessHelper.getExpr(stmt), call.function)) {
            function = true;
          }
        }
        for (int i = 0; i < arguments.length; i++) {
          for (AbstractBastNode stmt : wildcardStmts) {
            if (WildcardAccessHelper.isEqual(WildcardAccessHelper.getExpr(stmt),
                call.arguments.get(i))) {
              arguments[i] = true;
            } else if (call.arguments.get(i).getTag() == BastIntConst.TAG) {
              arguments[i] = true;
            }
          }
        }
        boolean isCovered = true;
        if (arguments.length > 0) {
          for (int i = 0; i < arguments.length; i++) {
            isCovered &= arguments[i];
          }
        }
        isCovered &= function;
        if (!isCovered) {
          continue;
        }
        StringBuffer bufferTmp = PatternGenerator.extractIndentation(call);
        AbstractBastNode annotation = null;
        if (tag == AresUseStmt.TAG) {
          annotation = CombinationHelper.createStmtUse(bufferTmp);
        } else {
          annotation = CombinationHelper.createWildcard1(bufferTmp);
        }
        replaceMap.put(call, annotation, false);

        newNodes.add(index, annotation);
        newNodes.remove(call);

        for (AbstractBastNode stmt : wildcardStmts) {
          replaceMap = updateMaps(cwuv, tag, replaceMap, stmt, annotation);
          newNodes.remove(stmt);
        }
        changed = true;
        globalChange = true;
      }
      if (globalChange) {
        return new BastField(newNodes);
      } else {
        return null;
      }

    }
    return null;
  }

  private BastField removeUselessExprPatterns(LinkedList<? extends AbstractBastNode> nodes) {
    int containsWildcard = 0;
    for (AbstractBastNode expr : nodes) {
      if (expr.getTag() == tag) {
        containsWildcard++;
      }
    }
    if (containsWildcard > 1) {
      LinkedList<AbstractBastNode> newNodes = new LinkedList<>();
      newNodes.addAll(nodes);
      boolean globalChange = false;
      boolean changed = true;
      int index = -1;
      while (changed || index < newNodes.size()) {
        changed = false;

        index++;

        boolean wildcardSelected = false;
        for (int i = index; i < newNodes.size(); i++) {
          if (newNodes.get(i).getTag() == tag) {
            if (WildcardAccessHelper.isWildcard(newNodes.get(i))) {
              index = i;
              wildcardSelected = true;
              break;
            }
          }
        }
        if (!wildcardSelected) {
          break;
        }
        boolean remove = false;
        int keepIndex = -1;
        for (int j = index + 1; j < newNodes.size(); j++) {
          if (newNodes.get(j).getTag() == tag) {
            if (WildcardAccessHelper.isExprWildcard(newNodes.get(index))) {
              if (WildcardAccessHelper.isExprWildcard(newNodes.get(j))) {
                AbstractBastNode node1 = WildcardAccessHelper.getExpr(newNodes.get(index));
                AbstractBastNode node2 = WildcardAccessHelper.getExpr(newNodes.get(j));
                if (node1.getTag() == BastIdentDeclarator.TAG) {
                  if (node2.getTag() == BastAsgnExpr.TAG) {
                    AbstractBastNode tmpExpr = ((BastIdentDeclarator) node2).expression;
                    if (tmpExpr != null && tmpExpr.getTag() == BastExprInitializer.TAG) {
                      tmpExpr = ((BastExprInitializer) tmpExpr).init;
                    }
                    node1 = new BastAsgnExpr(null, ((BastIdentDeclarator) node1).identifier,
                        (AbstractBastExpr) tmpExpr);
                  }
                }
                if (node2.getTag() == BastIdentDeclarator.TAG) {
                  if (node1.getTag() == BastAsgnExpr.TAG) {
                    AbstractBastNode tmpExpr = ((BastIdentDeclarator) node2).expression;
                    if (tmpExpr != null && tmpExpr.getTag() == BastExprInitializer.TAG) {
                      tmpExpr = ((BastExprInitializer) tmpExpr).init;
                    }
                    node2 = new BastAsgnExpr(null, ((BastIdentDeclarator) node2).identifier,
                        (AbstractBastExpr) tmpExpr);
                  }
                }
                if (node1.getTag() == BastExprInitializer.TAG) {
                  node1 = ((BastExprInitializer) node1).init;
                }
                if (node2.getTag() == BastExprInitializer.TAG) {
                  node2 = ((BastExprInitializer) node2).init;
                }

                if (WildcardAccessHelper.isEqual(node1, node2) && WildcardAccessHelper
                    .getOccurence(newNodes.get(index)).value == WildcardAccessHelper
                        .getOccurence(newNodes.get(j)).value) {
                  if (WildcardAccessHelper.getName(newNodes.get(j)) != null
                      || WildcardAccessHelper.getName(newNodes.get(index)) == null) {

                    remove = true;
                    keepIndex = j;
                    break;
                  } else {
                    WildcardAccessHelper.setName(newNodes.get(j),
                        WildcardAccessHelper.getName(newNodes.get(index)));
                    WildcardAccessHelper.setName(newNodes.get(index), null);
                    remove = true;
                    keepIndex = j;
                    break;
                  }
                } else {
                  if (newNodes.size() > j + 1 && newNodes.get(j + 1).getTag() == BastIf.TAG
                      && replaceMap.get(((BastIf) newNodes.get(j + 1)).condition) == newNodes
                          .get(j)) {

                    remove = true;
                    keepIndex = j;
                    break;

                  } else {
                    AbstractBastNode indexStmt = null;
                    for (int k = j + 1; k < newNodes.size(); k++) {
                      if (!WildcardAccessHelper.isWildcard(newNodes.get(k))) {
                        indexStmt = newNodes.get(k);
                        break;
                      }
                    }
                    if (WildcardAccessHelper.isPart(indexStmt, newNodes.get(index),
                        newNodes.get(j))) {
                      remove = true;
                      keepIndex = index;
                      index = j;
                      break;
                    } else if (WildcardAccessHelper.isPart(indexStmt, newNodes.get(j),
                        newNodes.get(index))) {
                      remove = true;
                      keepIndex = j;
                      break;
                    } else {
                      continue;
                    }
                  }

                }
              }
            } else {
              if (WildcardAccessHelper.isExprWildcard(newNodes.get(j))) {
                break;
              } else {

                keepIndex = j;
                if (keepIndex < newNodes.size() && index < newNodes.size()
                    && WildcardAccessHelper.getName(newNodes.get(keepIndex)) != null
                    && WildcardAccessHelper.getName(newNodes.get(index)) != null) {
                  break;
                }
                boolean skipMerge = false;
                AbstractBastNode indexNode = newNodes.get(index);
                AbstractBastNode keepIndexNode = newNodes.get(keepIndex);

                skipMerge = checkForUnallowedMerge(indexNode, keepIndexNode);
                if (skipMerge) {
                  continue;
                }
                remove = true;
                replaceMap = updateMaps(cwuv, tag, replaceMap, indexNode, keepIndexNode);
                if (WildcardAccessHelper.getName(indexNode) != null
                    && WildcardAccessHelper.getName(keepIndexNode) == null) {
                  AbstractBastNode tmp = indexNode;
                  indexNode = keepIndexNode;
                  keepIndexNode = tmp;
                  int tmpIndex = index;
                  index = keepIndex;
                  keepIndex = tmpIndex;
                }
                break;
              }
            }
          } else {
            break;
          }
        }
        if (remove) {
          replaceMap =
              updateMaps(cwuv, tag, replaceMap, newNodes.get(index), newNodes.get(keepIndex));
          newNodes.remove(index);

          changed = true;
          globalChange = true;
        }

      }
      if (globalChange) {
        return new BastField(newNodes);
      } else {
        return null;
      }

    }
    return null;
  }


  private boolean checkForUnallowedMerge(AbstractBastNode indexNode,
      AbstractBastNode keepIndexNode) {
    if (indexNode.getTag() == AresWildcard.TAG) {
      return false;
    }
    boolean skipMerge = false;
    List<Entry<AbstractBastNode, AbstractBastNode>> indexEntryList = new LinkedList<>();
    List<Entry<AbstractBastNode, AbstractBastNode>> keepEntryList = new LinkedList<>();

    for (Entry<AbstractBastNode, AbstractBastNode> entry : replaceMap.entrySet()) {
      if (entry.getValue() == indexNode) {
        indexEntryList.add(entry);
      }
      if (entry.getValue() == keepIndexNode) {
        keepEntryList.add(entry);
      }
    }

    if (indexEntryList.size() == 1 && keepEntryList.size() == 1) {
      Entry<AbstractBastNode, AbstractBastNode> indexEntry = indexEntryList.get(0);
      Entry<AbstractBastNode, AbstractBastNode> keepEntry = keepEntryList.get(0);
      if (extDiff != null) {
        BastEditOperation indexOp = extDiff.editMapOld.get(indexEntry.getKey());
        if (indexOp == null) {
          indexOp = extDiff.editMapNew.get(indexEntry.getKey());
        }
        BastEditOperation keepindexOp = extDiff.editMapOld.get(keepEntry.getKey());
        if (keepindexOp == null) {
          keepindexOp = extDiff.editMapNew.get(keepEntry.getKey());
        }
        if (indexOp != null && keepindexOp != null) {
          if (keepindexOp.getType() == EditOperationType.MOVE) {
            if (indexOp.getType() == EditOperationType.DELETE) {
              if (extDiff.firstToSecondMap
                  .get(keepindexOp.getUnchangedOrOldParentNode()) != keepindexOp
                      .getUnchangedOrNewParentNode()) {
                BastFieldConstants childrenListNumber = keepindexOp
                            .getNewOrChangedIndex().childrenListNumber;
                if (childrenListNumber != BastFieldConstants.FOR_STMT_INIT_DECL) {
                  skipMerge = true;
                }
              }
            }
          }
        }
      }
    }
    return skipMerge;
  }

  private HashSet<AbstractBastNode> ignoreSet = new HashSet<>();

  /**
   * Uses to ignore.
   *
   * @param ignoreSet the ignore set
   */
  public void usesToIgnore(HashSet<AbstractBastNode> ignoreSet) {
    this.ignoreSet.addAll(ignoreSet);
  }

  private BastField combineEmptyExprPatterns(LinkedList<? extends AbstractBastNode> nodes) {
    int containsWildcard = 0;
    for (AbstractBastNode expr : nodes) {
      if (expr.getTag() == tag) {
        containsWildcard++;
      }
    }
    if (containsWildcard > 1) {
      LinkedList<AbstractBastNode> newNodes = new LinkedList<>();
      newNodes.addAll(nodes);
      boolean globalChange = false;
      boolean changed = true;
      int index = -1;
      while (changed || index < newNodes.size()) {
        changed = false;
        index++;
        boolean wildcardSelected = false;
        for (int i = index; i < newNodes.size(); i++) {
          if (newNodes.get(i).getTag() == tag) {
            AresPatternClause pattern = (AresPatternClause) ((AresUseStmt) newNodes.get(i))
                .getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField();
            if (pattern == null || pattern.expr != null) {
              index = i;
              wildcardSelected = true;
              break;
            }
          }
        }
        if (!wildcardSelected) {
          break;
        }
        boolean remove = false;
        int keepIndex = -1;
        for (int j = index + 1; j < newNodes.size(); j++) {
          if (newNodes.get(j).getTag() == tag) {
            AresPatternClause pattern = (AresPatternClause) ((AresUseStmt) newNodes.get(j))
                .getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField();
            AresPatternClause patternIndex = (AresPatternClause) ((AresUseStmt) newNodes.get(index))
                .getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField();

            if (pattern != null) {
              if (patternIndex != null && patternIndex.expr != null) {

                if (WildcardAccessHelper.getName(((AresUseStmt) newNodes.get(index))) == null
                    && patternIndex.ident == null) {

                  remove = true;
                  keepIndex = j;
                } else {
                  continue;
                }
              } else {
                continue;
              }
            } else {
              if (patternIndex != null) {
                continue;
              } else {
                if (ignoreSet.contains(newNodes.get(index))
                    || ignoreSet.contains(newNodes.get(j))) {
                  continue;
                } else {
                  if (!(WildcardAccessHelper.getName(newNodes.get(index)) != null
                      && WildcardAccessHelper.getName(newNodes.get(j)) != null)) {
                    remove = true;
                    keepIndex = j;
                  } else {
                    continue;
                  }

                }
              }
            }
          } else {
            break;
          }
        }
        if (remove) {
          boolean skipMerge = checkForUnallowedMerge(newNodes.get(index), newNodes.get(keepIndex));
          if (skipMerge) {
            continue;
          }
          replaceMap =
              updateMaps(cwuv, tag, replaceMap, newNodes.get(index), newNodes.get(keepIndex));
          newNodes.remove(index);

          changed = true;
          globalChange = true;
        }

      }
      if (globalChange) {
        return new BastField(newNodes);
      } else {
        return null;
      }

    }
    return null;
  }

  private BastField combineWildcards(LinkedList<? extends AbstractBastNode> nodes) {
    int containsWildcard = 0;
    for (AbstractBastNode expr : nodes) {
      if (expr.getTag() == tag) {
        containsWildcard++;
      }
    }
    if (containsWildcard > 1) {
      LinkedList<AbstractBastNode> newNodes = new LinkedList<>();
      AbstractBastNode lastNode = nodes.get(0);
      for (int i = 1; i < nodes.size(); i++) {
        AbstractBastNode nextNode = nodes.get(i);
        if (lastNode.getTag() == tag && nextNode.getTag() == tag) {
          if (tag == AresUseStmt.TAG
              || WildcardAccessHelper.getName(lastNode) == null
                  && WildcardAccessHelper.getName(nextNode) == null
              || (WildcardAccessHelper.getName(lastNode) != null
                  && WildcardAccessHelper.getName(nextNode) != null
                  && WildcardAccessHelper.getName(lastNode).name
                      .equals(WildcardAccessHelper.getName(nextNode).name))) {
            if (!WildcardAccessHelper.isExprWildcard(lastNode)
                && !WildcardAccessHelper.isExprWildcard(nextNode)
                && !(WildcardAccessHelper.getName(nextNode) != null
                    && WildcardAccessHelper.getName(lastNode) != null)) {
              boolean skipMerge = checkForUnallowedMerge(lastNode, nextNode);
              if (skipMerge) {
                newNodes.add(lastNode);
                lastNode = nextNode;
              } else {
                nextNode.info.tokens[0] = lastNode.info.tokens[0];
                replaceMap = updateMaps(cwuv, tag, replaceMap, lastNode, nextNode);

                lastNode = nextNode;

                continue;
              }
            } else {
              if (WildcardAccessHelper.isExprWildcard(lastNode)) {
                if (WildcardAccessHelper.isExprWildcard(nextNode)) {
                  if (WildcardAccessHelper.isEqual(lastNode, nextNode)
                      && WildcardAccessHelper.getOccurence(lastNode).value == WildcardAccessHelper
                          .getOccurence(nextNode).value) {
                    nextNode.info.tokens[0] = lastNode.info.tokens[0];
                    replaceMap = updateMaps(cwuv, tag, replaceMap, lastNode, nextNode);
                    lastNode = nextNode;
                  } else {
                    newNodes.add(lastNode);
                    lastNode = nextNode;
                  }

                } else {

                  replaceMap = updateMaps(cwuv, tag, replaceMap, lastNode, nextNode);

                  lastNode = nextNode;

                }

              } else if (nextNode.getTag() == AresWildcard.TAG
                  && ((AresWildcard) nextNode).plugin.ident.name.equals("stmt")
                  && !(WildcardAccessHelper.getName(nextNode) != null
                      && WildcardAccessHelper.getName(lastNode) != null)) {
                nextNode.info.tokens[0] = lastNode.info.tokens[0];
                ReplaceMap tmpreplaceMap = replaceMap;
                replaceMap = new ReplaceMap();
                for (Entry<AbstractBastNode, AbstractBastNode> entry : tmpreplaceMap.entrySet()) {
                  if (entry.getValue() == lastNode) {
                    replaceMap.put(entry.getKey(), nextNode,
                        tmpreplaceMap.belongsToExpr(entry.getKey()));

                  } else {
                    replaceMap.put(entry.getKey(), entry.getValue(),
                        tmpreplaceMap.belongsToExpr(entry.getKey()));
                  }
                }
                if (WildcardAccessHelper.getName(lastNode) != null) {
                  if (WildcardAccessHelper.getName(nextNode) != null) {
                    cwuv.nodeReplacements.put(lastNode, nextNode);
                    newNodes.add(lastNode);
                    lastNode = nextNode;
                    continue;
                  } else {
                    WildcardAccessHelper.setName(nextNode, WildcardAccessHelper.getName(lastNode));
                  }
                }
                HashMap<String, AresWildcard> replaceConnectMap = new HashMap<>();
                for (Entry<String, AresWildcard> entry : cwuv.wildcards.entrySet()) {
                  if (entry.getValue() == lastNode) {
                    replaceConnectMap.put(entry.getKey(), (AresWildcard) nextNode);
                  } else {
                    replaceConnectMap.put(entry.getKey(), entry.getValue());
                  }
                }
                cwuv.wildcards = replaceConnectMap;

                lastNode = nextNode;

              } else {
                if (tag == AresUseStmt.TAG && !WildcardAccessHelper.isExprWildcard(lastNode)
                    && !WildcardAccessHelper.isExprWildcard(nextNode)
                    && WildcardAccessHelper.getName(lastNode) != null) {
                  boolean replace = false;
                  for (Entry<AbstractBastNode, AbstractBastNode> entry : cwuv.nodeReplacements
                      .entrySet()) {
                    if (WildcardAccessHelper.getName(entry.getKey()) != null
                        && WildcardAccessHelper.getName(lastNode).name
                            .equals(WildcardAccessHelper.getName(entry.getKey()).name)) {
                      replace = true;
                      break;
                    }
                  }
                  if (replace) {
                    nextNode.info.tokens[0] = lastNode.info.tokens[0];
                    replaceMap = updateMaps(cwuv, tag, replaceMap, lastNode, nextNode);
                  } else {
                    newNodes.add(lastNode);

                  }
                  lastNode = nextNode;
                } else {
                  newNodes.add(lastNode);
                  lastNode = nextNode;
                }
              }
            }

          } else {
            if (((AresWildcard) nextNode).plugin.ident.name.equals("stmt")) {
              nextNode.info.tokens[0] = lastNode.info.tokens[0];
              replaceMap = updateMaps(cwuv, tag, replaceMap, lastNode, nextNode);

              lastNode = nextNode;
            } else {
              newNodes.add(lastNode);
              lastNode = nextNode;
            }
          }
        } else {
          newNodes.add(lastNode);
          lastNode = nextNode;
        }
      }
      newNodes.add(lastNode);
      return new BastField(newNodes);
    }
    return null;
  }

  /**
   * Update maps.
   *
   * @param cwuv the cwuv
   * @param tag the tag
   * @param replaceMap the replace map
   * @param lastNode the last node
   * @param nextNode the next node
   * @return the replace map
   */
  public static ReplaceMap updateMaps(ConnectWildcardAndUseVisitor cwuv, int tag,
      ReplaceMap replaceMap, AbstractBastNode lastNode, AbstractBastNode nextNode) {
    ReplaceMap tmpreplaceMap = replaceMap;
    replaceMap = new ReplaceMap();
    for (Entry<AbstractBastNode, AbstractBastNode> entry : tmpreplaceMap.entrySet()) {
      if (entry.getValue() == lastNode) {
        if (nextNode != null) {
          replaceMap.put(entry.getKey(), nextNode, tmpreplaceMap.belongsToExpr(entry.getKey()));
        }
      } else {
        replaceMap.put(entry.getKey(), entry.getValue(),
            tmpreplaceMap.belongsToExpr(entry.getKey()));
      }
    }

    if (tag == AresWildcard.TAG) {
      HashMap<String, AresWildcard> replaceConnectMap = new HashMap<>();
      for (Entry<String, AresWildcard> entry : cwuv.wildcards.entrySet()) {
        if (entry.getValue() == lastNode) {
          replaceConnectMap.put(entry.getKey(), (AresWildcard) nextNode);
        } else {
          replaceConnectMap.put(entry.getKey(), entry.getValue());
        }
      }
      cwuv.wildcards = replaceConnectMap;
    } else {
      HashMap<String, List<AresUseStmt>> replaceConnectMap = new HashMap<>();
      List<AresUseStmt> list = null;

      for (Entry<String, List<AresUseStmt>> entry : cwuv.useStmts.entrySet()) {
        if (entry.getValue().contains(nextNode)) {
          list = entry.getValue();
        }
      }
      if (list == null) {
        list = new ArrayList<AresUseStmt>();
      }
      for (Entry<String, List<AresUseStmt>> entry : cwuv.useStmts.entrySet()) {
        if (entry.getValue().contains(lastNode)) {
          replaceConnectMap.put(entry.getKey(), list);
          assert (list != null);
        } else {
          replaceConnectMap.put(entry.getKey(), entry.getValue());
          assert (entry.getValue() != null);
        }
      }
      cwuv.useStmts = replaceConnectMap;
    }
    HashMap<AbstractBastNode, AbstractBastNode> tmpMap = cwuv.nodeReplacements;
    cwuv.nodeReplacements = new HashMap<AbstractBastNode, AbstractBastNode>();
    for (Entry<AbstractBastNode, AbstractBastNode> pair : tmpMap.entrySet()) {
      if (pair.getValue() == lastNode) {
        cwuv.nodeReplacements.put(pair.getKey(), nextNode);
      } else {
        cwuv.nodeReplacements.put(pair.getKey(), pair.getValue());
      }
    }
    cwuv.nodeReplacements.put(lastNode, nextNode);

    return replaceMap;
  }


  /**
   * Standard visit.
   *
   * @param constant the constant
   * @param node the node
   */
  @Override
  public void standardVisit(BastFieldConstants constant, AbstractBastNode node) {
    if (node.fieldMap.get(constant) != null) {
      if (node.fieldMap.get(constant).isList()) {
        int counter = 0;
        if (node.fieldMap.get(constant).getListField() != null) {
          if (node.fieldMap.get(constant).getListField().size() > 1) {
            fieldId = constant;
            globalParent = node;
            boolean changed = true;
            PatternGenerator.print(node);
            while (changed) {
              changed = false;
              BastField field = null;
              if (onlyEmptyExpr) {
                field = combineEmptyExprPatterns(node.fieldMap.get(constant).getListField());
                if (field != null) {
                  if (field.getListField().size() != node.fieldMap.get(constant).getListField()
                      .size()) {
                    changed = true;
                  }
                  node.replaceField(constant, field);
                  continue;
                }
              } else {
                field = removeUselessExprPatterns(node.fieldMap.get(constant).getListField());
                if (field != null) {
                  if (field.getListField().size() != node.fieldMap.get(constant).getListField()
                      .size()) {
                    changed = true;
                  }
                  node.replaceField(constant, field);
                  PatternGenerator.print(node);
                  continue;
                }
                field = removeCalls(node.fieldMap.get(constant).getListField());
                if (field != null) {
                  if (field.getListField().size() != node.fieldMap.get(constant).getListField()
                      .size()) {
                    changed = true;
                  }
                  node.replaceField(constant, field);
                  PatternGenerator.print(node);
                  continue;
                }
                field = combineWildcards(node.fieldMap.get(constant).getListField());
                if (field != null) {
                  if (field.getListField().size() != node.fieldMap.get(constant).getListField()
                      .size()) {
                    changed = true;
                  }
                  node.replaceField(constant, field);
                  PatternGenerator.print(node);
                  continue;
                }
              }
            }

          }
          for (AbstractBastNode expr : node.fieldMap.get(constant).getListField()) {
            setVariables(constant, node, counter++);
            expr.accept(this);
          }
        }
      } else if (node.fieldMap.get(constant).getField() != null) {
        setVariables(constant, node, -1);
        node.fieldMap.get(constant).getField().accept(this);
      }
    }

  }

}
