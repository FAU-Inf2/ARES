package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.pcreation.MatchBoundary;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.help.MovementInformation;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.ExecutionRunType;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformation;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameter;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitchCaseGroup;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.AlignOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.MoveOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RemoveMovesObsoleteDueToWildcards extends AbstractFilterRule {

  public RemoveMovesObsoleteDueToWildcards() {
    super(FilterRuleType.REMOVE_MOVES_OBSOLETE_DUE_TO_WILDCARDS);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    worklist =
        handleStandardMoves(worklist, exDiffCurrent, delInsertMap, hierarchyFirst, hierarchySecond);
    worklist = alignments(worklist, exDiffCurrent, exDiffBA1, exDiffBA2, exDiffOther, alignList,
        matchBoundary, runType);
    return alignmentsPartTwo(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond,
        matchBoundary, delInsertMap, alignList);
  }

  private static List<BastEditOperation> handleStandardMoves(List<BastEditOperation> editList,
      ExtendedDiffResult exDiffCurrent, HashMap<AbstractBastNode, AbstractBastNode> delInsertMap,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    ArrayList<BastEditOperation> workList = new ArrayList<>();
    for (BastEditOperation ep : editList) {
      switch (ep.getType()) {
        case MOVE:
          if (ep.getUnchangedOrOldParentNode().getTag() == ep.getUnchangedOrNewParentNode()
              .getTag()) {
            BastFieldConstants childrenListNumber = ep.getOldOrChangedIndex().childrenListNumber;
            if (childrenListNumber == ep.getNewOrChangedIndex().childrenListNumber) {
              if (ep.getOldOrChangedIndex().childrenListIndex == ep
                  .getNewOrChangedIndex().childrenListIndex) {
                NodeParentInformationHierarchy npi =
                    hierarchyFirst.get(ep.getUnchangedOrOldParentNode());
                NodeParentInformationHierarchy npi2 =
                    hierarchySecond.get(ep.getUnchangedOrNewParentNode());

                if (npi.list.get(0).parent.getTag() == npi2.list.get(0).parent.getTag()) {
                  if ((npi.list.get(0).listId == npi2.list.get(0).listId
                      || ep.getOldOrInsertedNode().getTag() == BastBlock.TAG)
                      && WildcardAccessHelper.isEqual(ep.getOldOrInsertedNode(),
                          ep.getNewOrChangedNode())) {

                    delInsertMap.put(ep.getOldOrInsertedNode(), ep.getNewOrChangedNode());
                    delInsertMap.put(ep.getNewOrChangedNode(), ep.getOldOrInsertedNode());
                  } else {
                    if (childrenListNumber == BastFieldConstants.DIRECT_CALL_FUNCTION
                        && WildcardAccessHelper.isEqual(ep.getOldOrInsertedNode(),
                            ep.getNewOrChangedNode())) {
                      delInsertMap.put(ep.getOldOrInsertedNode(), ep.getNewOrChangedNode());
                      delInsertMap.put(ep.getNewOrChangedNode(), ep.getOldOrInsertedNode());
                    } else {
                      workList.add(ep);
                    }
                  }
                } else {
                  if (ep.getNewOrChangedNode().getTag() != BastNameIdent.TAG) {
                    workList.add(ep);
                  }
                }

                continue;
              }
            }
          } else if (ep.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG
              && ep.getUnchangedOrNewParentNode().getTag() == BastSwitchCaseGroup.TAG) {
            if (ep.getOldOrChangedIndex().childrenListIndex == ep
                .getNewOrChangedIndex().childrenListIndex) {
              NodeParentInformationHierarchy npi =
                  hierarchyFirst.get(ep.getUnchangedOrOldParentNode());
              if (npi.list.size() > 0
                  && npi.list.get(0).parent.getTag() == BastSwitchCaseGroup.TAG) {
                continue;
              }
            }
          }
          if (ep.getUnchangedOrOldParentNode().getTag() == BastParameter.TAG) {
            continue;
          }
          AbstractBastNode partner =
              exDiffCurrent.secondToFirstMap.get(ep.getUnchangedOrNewParentNode());
          AbstractBastNode partner2 =
              exDiffCurrent.secondToFirstMap.get(ep.getUnchangedOrOldParentNode());

          if (partner == null && partner2 == null
              && ep.getOldOrInsertedNode().getTag() == BastNameIdent.TAG) {
            // do nothing
          } else {
            if (ep.getNewOrChangedNode().getTag() == BastExprInitializer.TAG) {
              if (exDiffCurrent.editMapNew.containsKey(ep.getNewOrChangedNode()
                  .getField(BastFieldConstants.EXPR_INITIALIZER_INIT).getField())) {
                continue;
              }
            }
            workList.add(ep);
          }
          break;
        default:
          workList.add(ep);
      }
    }
    return workList;
  }

  /**
   * Alignments.
   *
   * @param operations the operations
   * @param extDiff the ext diff
   * @param extDiffBA1 the ext diff BA 1
   * @param extDiffBA2 the ext diff BA 2
   * @param extDiffOther the ext diff other
   * @param alignments the alignments
   * @param boundary the boundary
   * @param usePart the use part
   * @return the list
   */
  public static List<BastEditOperation> alignments(List<BastEditOperation> operations,
      ExtendedDiffResult extDiff, ExtendedDiffResult extDiffBA1, ExtendedDiffResult extDiffBA2,
      ExtendedDiffResult extDiffOther, List<BastEditOperation> alignments, MatchBoundary boundary,
      ExecutionRunType usePart) {
    HashMap<AbstractBastNode, MovementInformation> movementMap =
        new HashMap<AbstractBastNode, MovementInformation>();
    for (BastEditOperation ep : operations) {
      MovementInformation mi = null;
      MovementInformation miO = null;
      NodeIndex index = null;
      switch (ep.getType()) {
        case INSERT:
        case MOVE:
        case ALIGN:
          NodeIndex indexOld = ep.getOldOrChangedIndex();
          mi = movementMap.get(ep.getUnchangedOrNewParentNode());
          index = ep.getNewOrChangedIndex();
          AbstractBastNode parent = ep.getUnchangedOrNewParentNode();
          AbstractBastNode oldParent = extDiff.secondToFirstMap.get(parent);

          if (oldParent == null && boundary != null && parent == boundary.getNode2()) {
            oldParent = boundary.getNode1();
          }
          if (oldParent == null && boundary != null && parent == boundary.getNode1()) {
            oldParent = boundary.getNode2();
          }
          if (oldParent != null && oldParent.getTag() == BastBlock.TAG) {
            indexOld = new NodeIndex(BastFieldConstants.BLOCK_STATEMENT, 0);
          }
          if (oldParent != null) {
            miO = movementMap.get(ep.getUnchangedOrNewParentNode());
          }

          if (mi == null) {

            if (parent != null && parent.getField(index.childrenListNumber) != null
                && parent.getField(index.childrenListNumber).isList()
                && parent.getField(index.childrenListNumber).getListField() != null) {
              if (oldParent != null && miO == mi
                  && oldParent.getField(indexOld.childrenListNumber) != null
                  && oldParent.getField(indexOld.childrenListNumber).isList()
                  && oldParent.getField(indexOld.childrenListNumber).getListField() != null) {
                mi = new MovementInformation(oldParent,
                    oldParent.getField(indexOld.childrenListNumber).getListField().size(), parent,
                    parent.getField(index.childrenListNumber).getListField().size());
                movementMap.put(parent, mi);
                movementMap.put(oldParent, mi);
                miO = mi;
              } else {
                mi = new MovementInformation(null, 0, parent,
                    parent.getField(index.childrenListNumber).getListField().size());
                movementMap.put(parent, mi);
              }
            }
          }
          if (mi != null) {
            mi.insList.add(ep);
            mi.updateMap(index.childrenListIndex, -1, 1);
          }
          if (oldParent == null) {
            continue;
          }
          if (miO != null && miO != mi) {
            miO.insList.add(ep);
            miO.updateMap(index.childrenListIndex, -1, 1);
          }
          break;
        case DELETE:
          mi = movementMap.get(ep.getUnchangedOrOldParentNode());
          index = ep.getOldOrChangedIndex();
          if (mi == null) {
            oldParent = ep.getUnchangedOrOldParentNode();
            parent = extDiff.firstToSecondMap.get(oldParent);
            if (parent == null || oldParent == null) {
              continue;
            }
            if (oldParent.getField(index.childrenListNumber) == null
                || parent.getField(index.childrenListNumber) == null) {
              continue;
            }
            if (!(oldParent.getField(index.childrenListNumber).isList()
                && parent.getField(index.childrenListNumber).isList())) {
              continue;
            }
            if (oldParent.getField(index.childrenListNumber).getListField() == null
                || parent.getField(index.childrenListNumber).getListField() == null) {
              continue;
            }
            mi = new MovementInformation(oldParent,
                oldParent.getField(index.childrenListNumber).getListField().size(), parent,
                parent.getField(index.childrenListNumber).getListField().size());
            movementMap.put(parent, mi);
            movementMap.put(oldParent, mi);
          }
          mi.delList.add(ep);

          mi.updateMap(index.childrenListIndex, -1, -1);
          break;
        default:
          break;
      }
    }
    List<BastEditOperation> workList = new ArrayList<>();
    outer: for (BastEditOperation ep : operations) {
      switch (ep.getType()) {
        case ALIGN:
          MovementInformation miO = movementMap.get(ep.getUnchangedOrOldParentNode());
          MovementInformation miN =
              movementMap.get(extDiff.firstToSecondMap.get(ep.getUnchangedOrNewParentNode()));
          if (miO == null && miN == null) {
            workList.add(ep);
            break;
          }
          int index = ep.getOldOrChangedIndex().childrenListIndex;
          if (miO != null) {
            for (BastEditOperation editOperation : miO.delList) {
              if (editOperation.getOldOrChangedIndex().childrenListIndex < ep
                  .getOldOrChangedIndex().childrenListIndex) {
                if (editOperation.getType() != EditOperationType.ALIGN) {

                  index--;
                }
              }
            }
          }
          if (miN != null) {
            for (BastEditOperation editOperation : miN.insList) {
              if (editOperation.getOldOrChangedIndex().childrenListIndex < ep
                  .getNewOrChangedIndex().childrenListIndex) {
                if (editOperation.getType() != EditOperationType.ALIGN) {
                  index++;
                }
              }
            }
          }
          if (index == ep.getNewOrChangedIndex().childrenListIndex) {
            @SuppressWarnings("unchecked")
            LinkedList<AbstractBastNode> stmtsOld =
                (LinkedList<AbstractBastNode>) ep.getUnchangedOrOldParentNode()
                    .getField(ep.getOldOrChangedIndex().childrenListNumber).getListField();
            @SuppressWarnings("unchecked")
            LinkedList<AbstractBastNode> stmtsNew =
                (LinkedList<AbstractBastNode>) ep.getUnchangedOrNewParentNode()
                    .getField(ep.getNewOrChangedIndex().childrenListNumber).getListField();
            for (int i = 0; i < stmtsOld.size(); i++) {
              for (int j = 0; j < stmtsNew.size(); j++) {
                if (extDiff.firstToSecondMap.get(stmtsOld.get(i)) == stmtsNew.get(j)) {
                  if (i > ep.getOldOrChangedIndex().childrenListIndex
                      && j < ep.getOldOrChangedIndex().childrenListIndex) {
                    if (extDiff.editMapOld.get(stmtsOld.get(i)) == null
                        && extDiff.editMapNew.get(stmtsNew.get(j)) == null) {
                      workList.add(ep);
                      continue outer;
                    }
                  }
                }
              }
            }

            if (miO != null) {
              for (BastEditOperation editOperation : miO.insList) {
                if (editOperation.getOldOrChangedIndex().childrenListIndex < ep
                    .getOldOrChangedIndex().childrenListIndex) {
                  if (editOperation.getType() != EditOperationType.ALIGN) {

                    index++;
                  }
                }
              }
            }
            for (int i = index; i > ep.getNewOrChangedIndex().childrenListIndex; i--) {
              @SuppressWarnings("unchecked")
              LinkedList<AbstractBastNode> stmts =
                  (LinkedList<AbstractBastNode>) ep.getUnchangedOrNewParentNode()
                      .getField(ep.getNewOrChangedIndex().childrenListNumber).getListField();
              if (stmts.size() > i) {
                InsertOperation insertOp = new InsertOperation(ep.getUnchangedOrNewParentNode(),
                    stmts.get(i), new NodeIndex(ep.getNewOrChangedIndex().childrenListNumber, i));
                boolean add = true;
                for (BastEditOperation editOperation : miO.insList) {
                  if (editOperation.getOldOrChangedIndex().childrenListIndex == i) {
                    if (editOperation.getType() == EditOperationType.ALIGN) {
                      add = false;
                      break;
                    }
                  }
                }
                if (add) {
                  workList.add(insertOp);
                }
              }
            }
            alignments.add(ep);
            break;
          } else {
            boolean sourceCovered = false;
            boolean targetCovered = false;
            for (BastEditOperation editOperation : operations) {
              if (editOperation.getType() == ep.getType()) {
                if (editOperation.getOldOrInsertedNode().getTag() == ep.getOldOrInsertedNode()
                    .getTag()) {
                  if (editOperation.getUnchangedOrOldParentNode() == ep
                      .getUnchangedOrOldParentNode()) {
                    if (editOperation.getNewOrChangedIndex().childrenListIndex == ep
                        .getOldOrChangedIndex().childrenListIndex) {
                      sourceCovered = true;
                    } else if (editOperation.getOldOrChangedIndex().childrenListIndex == ep
                        .getNewOrChangedIndex().childrenListIndex) {
                      targetCovered = true;
                    }
                  }
                }
              }

            }
            if (sourceCovered && targetCovered) {
              alignments.add(ep);
              break;
            }
            LinkedList<BastEditOperation> aligns = new LinkedList<BastEditOperation>();
            aligns.add(ep);
            for (BastEditOperation epTmp : operations) {
              if (epTmp.getType() == EditOperationType.ALIGN && epTmp != ep) {
                if (WildcardAccessHelper.isEqual(epTmp.getOldOrInsertedNode(),
                    ep.getOldOrInsertedNode())) {
                  if (WildcardAccessHelper.isEqual(epTmp.getNewOrChangedNode(),
                      ep.getNewOrChangedNode())) {
                    aligns.add(epTmp);
                  }
                }
              }
            }
            LinkedList<Integer> starts = new LinkedList<Integer>();
            LinkedList<Integer> ends = new LinkedList<Integer>();
            HashSet<Integer> diffs = new HashSet<Integer>();

            HashSet<Integer> possibleEnds = new HashSet<Integer>();

            for (BastEditOperation align : aligns) {
              starts.add(align.getOldOrChangedIndex().childrenListIndex);
              ends.add(align.getNewOrChangedIndex().childrenListIndex);
            }
            for (int start : starts) {
              for (int end : ends) {
                int pos = 0;
                if (miO != null) {
                  for (BastEditOperation editOperation : miO.delList) {
                    if (editOperation.getOldOrChangedIndex().childrenListIndex < start) {
                      if (editOperation.getType() != EditOperationType.ALIGN
                          && editOperation.getType() != EditOperationType.MOVE) {

                        pos--;
                      }
                    }
                  }
                }
                if (miN != null) {
                  for (BastEditOperation editOperation : miN.insList) {
                    if (editOperation.getOldOrChangedIndex().childrenListIndex < end) {
                      if (editOperation.getType() != EditOperationType.ALIGN
                          && editOperation.getType() != EditOperationType.MOVE) {
                        pos++;
                      }
                    }
                  }
                }
                for (BastEditOperation epTmp : extDiff.editScript) {
                  if (epTmp.getType() == EditOperationType.MOVE) {
                    if (epTmp.getUnchangedOrOldParentNode() == ep.getUnchangedOrOldParentNode()) {
                      if (epTmp.getUnchangedOrNewParentNode() != ep.getUnchangedOrNewParentNode()) {
                        if (epTmp.getOldOrChangedIndex().childrenListIndex < start) {
                          pos--;
                        }
                      }
                    }
                  }
                }
                diffs.add(pos);
              }

            }
            for (int start : starts) {
              for (int diff : diffs) {
                possibleEnds.add(start + diff);
              }
            }

            if (possibleEnds.contains(ep.getNewOrChangedIndex().childrenListIndex)) {
              alignments.add(ep);
              break;

            }
            if (ep.getOldOrChangedIndex().childrenListIndex == ep
                .getNewOrChangedIndex().childrenListIndex) {
              int deletedNodes = 0;
              for (BastEditOperation editOperation : miO.delList) {
                if (editOperation.getOldOrChangedIndex().childrenListIndex < ep
                    .getOldOrChangedIndex().childrenListIndex) {
                  if (editOperation.getType() != EditOperationType.ALIGN
                      && editOperation.getType() != EditOperationType.MOVE) {
                    deletedNodes++;
                  }
                }
              }

              if (miN == null && miO.insList.size() == 1
                  && deletedNodes == ep.getOldOrChangedIndex().childrenListIndex - index
                  && usePart == ExecutionRunType.MODIFIED_RUN && extDiffBA2 != null) {
                if (extDiffBA2.secondToFirstMap.get(ep.getNewOrChangedNode()) != null) {
                  AbstractBastNode node = extDiffBA2.secondToFirstMap.get(ep.getNewOrChangedNode());
                  if (extDiffOther.secondToFirstMap.get(node) != null
                      && extDiffOther.editMapNew.get(node) == null) {
                    if (ep.getUnchangedOrOldParentNode()
                        .getField(ep.getOldOrChangedIndex().childrenListNumber).isList()) {
                      @SuppressWarnings("unchecked")
                      LinkedList<AbstractBastNode> nodesPartner = (LinkedList<AbstractBastNode>) ep
                          .getUnchangedOrNewParentNode()
                          .getField(ep.getNewOrChangedIndex().childrenListNumber).getListField();

                      for (int i = 0; i < ep.getNewOrChangedIndex().childrenListIndex; i++) {
                        AbstractBastNode partner =
                            extDiff.secondToFirstMap.get(nodesPartner.get(i));
                        if (extDiff.editMapNew.get(nodesPartner.get(i)) == null
                            && partner != null) {
                          @SuppressWarnings("unchecked")
                          LinkedList<AbstractBastNode> nodes =
                              (LinkedList<AbstractBastNode>) ep.getUnchangedOrOldParentNode()
                                  .getField(ep.getOldOrChangedIndex().childrenListNumber)
                                  .getListField();
                          int nodeIndex = -1;
                          for (int j = 0; j < nodes.size(); j++) {
                            if (nodes.get(j) == partner) {
                              nodeIndex = j;
                              break;
                            }
                          }
                          if (nodeIndex != -1) {
                            AlignOperation align =
                                new AlignOperation(ep.getUnchangedOrOldParentNode(),
                                    ep.getUnchangedOrNewParentNode(), partner, nodesPartner.get(i),
                                    new NodeIndex(ep.getOldOrChangedIndex().childrenListNumber,
                                        nodeIndex),
                                    new NodeIndex(ep.getNewOrChangedIndex().childrenListNumber, i));
                            workList.add(align);
                          }
                        }
                      }
                    }
                  }
                }
                break;
              }
            }

            if (ep.getOldOrChangedIndex().childrenListNumber.isList
                && ep.getNewOrChangedIndex().childrenListNumber.isList) {
              @SuppressWarnings("unchecked")
              List<AbstractBastNode> oldNodes =
                  (List<AbstractBastNode>) ep.getUnchangedOrOldParentNode()
                      .getField(ep.getNewOrChangedIndex().childrenListNumber).getListField();
              @SuppressWarnings("unchecked")
              List<AbstractBastNode> newNodes =
                  (List<AbstractBastNode>) ep.getUnchangedOrNewParentNode()
                      .getField(ep.getNewOrChangedIndex().childrenListNumber).getListField();
              if (oldNodes != null && newNodes != null) {
                if (ep.getOldOrChangedIndex().childrenListIndex < oldNodes.size()
                    && ep.getOldOrChangedIndex().childrenListIndex < newNodes.size()) {
                  if (ep.getNewOrChangedIndex().childrenListIndex < oldNodes.size()
                      && ep.getNewOrChangedIndex().childrenListIndex < newNodes.size()) {

                    if (WildcardAccessHelper.isEqual(
                        oldNodes.get(ep.getOldOrChangedIndex().childrenListIndex),
                        newNodes.get(ep.getOldOrChangedIndex().childrenListIndex))) {
                      if (WildcardAccessHelper.isEqual(
                          oldNodes.get(ep.getNewOrChangedIndex().childrenListIndex),
                          newNodes.get(ep.getNewOrChangedIndex().childrenListIndex))) {
                        break;
                      }
                    }
                  }
                }
              }
            }
            workList.add(ep);
            break;
          }
        default:
          workList.add(ep);
      }
    }
    return workList;

  }

  private static List<BastEditOperation> alignmentsPartTwo(List<BastEditOperation> editScript,
      ExtendedDiffResult exDiffBb,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond,
      MatchBoundary matchBoundary, Map<AbstractBastNode, AbstractBastNode> delInsertMap,
      ArrayList<BastEditOperation> alignList) {
    List<BastEditOperation> workList = new ArrayList<>();
    NodeParentInformationHierarchy npi = null;
    outer: for (BastEditOperation ep : editScript) {
      switch (ep.getType()) {
        case ALIGN:
          if (ep.getUnchangedOrOldParentNode().getTag() != BastBlock.TAG) {
            workList.add(ep);
            continue;
          }
          @SuppressWarnings("unchecked")
          LinkedList<AbstractBastNode> stmtsOld =
              (LinkedList<AbstractBastNode>) ep.getUnchangedOrOldParentNode()
                  .getField(ep.getOldOrChangedIndex().childrenListNumber).getListField();
          @SuppressWarnings("unchecked")
          LinkedList<AbstractBastNode> stmtsNew =
              (LinkedList<AbstractBastNode>) ep.getUnchangedOrNewParentNode()
                  .getField(ep.getNewOrChangedIndex().childrenListNumber).getListField();
          for (int i = 0; i < stmtsOld.size(); i++) {
            for (int j = 0; j < stmtsNew.size(); j++) {
              if (exDiffBb.firstToSecondMap.get(stmtsOld.get(i)) == stmtsNew.get(j)) {
                if (i > ep.getOldOrChangedIndex().childrenListIndex
                    && j < ep.getOldOrChangedIndex().childrenListIndex) {
                  if (exDiffBb.editMapOld.get(stmtsOld.get(i)) == null
                      && exDiffBb.editMapNew.get(stmtsNew.get(j)) == null) {
                    workList.add(ep);
                    continue outer;
                  }
                }
              }
            }
          }
          AlignOperation sro = (AlignOperation) ep;
          npi = hierarchyFirst.get(sro.getOldOrInsertedNode());
          for (NodeParentInformation np : npi.list) {
            assert (np != null);
            assert (matchBoundary.getNode2() != null);
            if (np.parent == matchBoundary.getNode2() || np.parent == matchBoundary.getNode1()) {
              if (np.fieldConstant == matchBoundary.field2
                  || np.fieldConstant == matchBoundary.field1) {
                if ((np.listId == 0 && ep.getNewOrChangedIndex().childrenListIndex != 1)) {
                  workList.add(ep);
                  break;
                } else {
                  BastEditOperation add = null;
                  for (BastEditOperation epAlign : alignList) {
                    if (epAlign.getUnchangedOrOldParentNode() == ep.getUnchangedOrOldParentNode()
                        && epAlign.getUnchangedOrNewParentNode() == ep
                            .getUnchangedOrNewParentNode()) {
                      if (epAlign.getOldOrChangedIndex().childrenListIndex == ep
                          .getNewOrChangedIndex().childrenListIndex) {
                        add = epAlign;
                        break;
                      } else if (epAlign.getNewOrChangedIndex().childrenListIndex == ep
                          .getOldOrChangedIndex().childrenListIndex) {
                        add = epAlign;
                        break;
                      }

                    }
                  }
                  if (add != null && !workList.contains(add) && !WildcardAccessHelper
                      .isEqual(add.getOldOrInsertedNode(), ep.getOldOrInsertedNode())) {
                    if (Math
                        .signum(add.getOldOrChangedIndex().childrenListIndex
                            - add.getNewOrChangedIndex().childrenListIndex)
                        - Math.signum(ep.getOldOrChangedIndex().childrenListIndex
                            - ep.getNewOrChangedIndex().childrenListIndex) != 0) {
                      workList.add(add);
                      break;
                    }
                  }
                  workList.add(ep);
                  break;
                }
              }
            }
          }
          break;
        case MOVE:
          if (ep.getUnchangedOrOldParentNode().getTag() != BastBlock.TAG) {
            workList.add(ep);
            continue;
          }
          MoveOperation spc = (MoveOperation) ep;
          AbstractBastNode parent =
              exDiffBb.firstToSecondMap.get(spc.getUnchangedOrOldParentNode());

          if (parent != null && (parent != spc.getUnchangedOrNewParentNode())) {
            npi = hierarchySecond.get(spc.getNewOrChangedNode());
            NodeParentInformationHierarchy npi2 = hierarchyFirst.get(spc.getOldOrInsertedNode());
            if (npi.list.size() > 1 && npi2.list.size() > 1) {
              if (npi.list.get(1).parent == exDiffBb.firstToSecondMap
                  .get(npi2.list.get(1).parent)) {
                break;
              }
              if (npi.list.get(1).parent.getTag() == AresBlock.TAG
                  && npi2.list.get(1).parent == matchBoundary.getNode1()
                  && spc.getUnchangedOrNewParentNode() == matchBoundary.getNode2()) {
                if (ep.getOldOrChangedIndex().childrenListIndex == ep
                    .getNewOrChangedIndex().childrenListIndex) {
                  break;
                } else {
                  int moveOffset = 0;
                  for (BastEditOperation eop : editScript) {
                    if (eop.getUnchangedOrOldParentNode() == ep.getUnchangedOrOldParentNode()) {
                      if (eop.getOldOrChangedIndex().childrenListNumber == ep
                          .getOldOrChangedIndex().childrenListNumber) {
                        if (eop.getType() == EditOperationType.DELETE) {
                          if (eop.getOldOrChangedIndex().childrenListIndex < ep
                              .getOldOrChangedIndex().childrenListIndex) {
                            moveOffset++;
                          }
                        } else if (eop.getType() == EditOperationType.ALIGN
                            || eop.getType() == EditOperationType.MOVE) {
                          if (eop.getUnchangedOrNewParentNode() != ep.getUnchangedOrNewParentNode()
                              || eop.getNewOrChangedIndex().childrenListNumber != ep
                                  .getNewOrChangedIndex().childrenListNumber) {
                            moveOffset++;
                            continue;
                          }
                          if (eop.getNewOrChangedIndex().childrenListIndex > ep
                              .getNewOrChangedIndex().childrenListIndex) {
                            moveOffset++;
                          }
                        }
                      }

                    }
                  }
                  if (moveOffset == ep.getOldOrChangedIndex().childrenListIndex
                      - ep.getNewOrChangedIndex().childrenListIndex) {
                    break;
                  }
                }
              }
              workList.add(ep);
              break;
            }
          }
          npi = hierarchyFirst.get(spc.getOldOrInsertedNode());
          NodeParentInformation np = npi.list.get(0);
          assert (np != null);
          assert (matchBoundary.getNode2() != null);
          if (spc.getUnchangedOrOldParentNode() == matchBoundary.getNode1()
              && spc.getUnchangedOrNewParentNode() == matchBoundary.getNode2()) {
            int insertCount = 0;
            for (BastEditOperation epTest : editScript) {
              if (epTest.getType() != EditOperationType.UPDATE) {
                if (epTest.getUnchangedOrOldParentNode() == matchBoundary.getNode2()
                    && (epTest.getType() == EditOperationType.INSERT)) {

                  insertCount++;
                }
              }
            }
            if (spc.getOldOrChangedIndex().childrenListIndex
                + insertCount == spc.getNewOrChangedIndex().childrenListIndex) {
              break;
            }
          }
          if (np.parent == matchBoundary.getNode2() || np.parent == matchBoundary.getNode1()) {
            if (np.fieldConstant == matchBoundary.field2
                || np.fieldConstant == matchBoundary.field1) {

              workList.add(ep);
              break;
            }
          }
          npi = hierarchySecond.get(spc.getNewOrChangedNode());
          np = npi.list.get(0);
          assert (np != null);
          assert (matchBoundary.getNode2() != null);
          if (np.parent == matchBoundary.getNode2() || np.parent == matchBoundary.getNode1()) {
            if (np.fieldConstant == matchBoundary.field2
                || np.fieldConstant == matchBoundary.field1) {
              workList.add(ep);
              break;
            }
          }
          for (BastEditOperation epAdd : exDiffBb.editScript) {
            if (epAdd.getType() == EditOperationType.INSERT) {
              if (epAdd.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG) {
                NodeParentInformationHierarchy npi2 =
                    hierarchySecond.get(epAdd.getUnchangedOrOldParentNode());
                if (npi2.list.get(0).parent == spc.getNewOrChangedNode()) {
                  if (!delInsertMap.containsKey(epAdd.getOldOrInsertedNode())) {
                    workList.add(epAdd);
                  }
                }
              }
            }

          }
          NodeParentInformationHierarchy npiNew = hierarchySecond.get(spc.getNewOrChangedNode());
          NodeParentInformationHierarchy npiOld = hierarchyFirst.get(spc.getOldOrInsertedNode());
          LinkedList<AbstractBastNode> parents = new LinkedList<AbstractBastNode>();
          for (int i = 0; i < npiOld.list.size(); i++) {
            parents.add(npiOld.list.get(i).parent);
          }
          int newIndex = -1;
          int oldIndex = -1;
          for (int i = 0; i < npiNew.list.size(); i++) {
            if (exDiffBb.secondToFirstMap.get(npiNew.list.get(i).parent) != null) {
              if (parents.contains(exDiffBb.secondToFirstMap.get(npiNew.list.get(i).parent))) {
                newIndex = i;
                oldIndex =
                    parents.indexOf(exDiffBb.secondToFirstMap.get(npiNew.list.get(i).parent));
                break;
              }
            }
          }
          if (newIndex == 0 && oldIndex != -1 && newIndex != oldIndex) {
            if (ep.getUnchangedOrOldParentNode() != matchBoundary.getNode1()
                && ep.getUnchangedOrNewParentNode() != matchBoundary.getNode2()) {
              workList.add(ep);

              break;
            }
          } else if (newIndex < oldIndex && oldIndex != -1 && newIndex != oldIndex) {
            if (ep.getUnchangedOrOldParentNode() != matchBoundary.getNode1()
                && ep.getUnchangedOrNewParentNode() != matchBoundary.getNode2()) {
              workList.add(ep);

              break;
            }
          } else {
            if (ep.getUnchangedOrOldParentNode() == matchBoundary.getNode1()
                && ep.getUnchangedOrNewParentNode() == matchBoundary.getNode2()) {
              for (AbstractBastNode stmt : matchBoundary.getNode1()
                  .getField(ep.getNewOrChangedIndex().childrenListNumber).getListField()) {
                if (exDiffBb.firstToSecondMap.get(stmt) != null) {
                  if (matchBoundary.getNode2()
                      .getField(ep.getNewOrChangedIndex().childrenListNumber).getListField()
                      .indexOf(exDiffBb.firstToSecondMap.get(stmt)) != -1) {
                    matchBoundary.getNode2().getField(ep.getNewOrChangedIndex().childrenListNumber)
                        .getListField().indexOf(exDiffBb.firstToSecondMap.get(stmt));
                    break;
                  }
                }
              }
              int moveOffset = 0;
              moveOffset = computeOffset(editScript, ep, moveOffset);
              if (ep.getOldOrChangedIndex().childrenListIndex != ep
                  .getNewOrChangedIndex().childrenListIndex + moveOffset) {
                workList.add(ep);
              }
              break;
            }
          }
          break;
        default:
          workList.add(ep);
      }
    }
    return workList;
  }

  static int computeOffset(List<BastEditOperation> editScript, BastEditOperation ep,
      int moveOffset) {
    for (BastEditOperation eop : editScript) {
      if (eop == ep) {
        continue;
      }
      if (ep.getType() == EditOperationType.UPDATE) {
        continue;
      }
      if (eop.getType() == EditOperationType.UPDATE) {
        continue;
      }
      if (ep.getType() != EditOperationType.UPDATE
          && !(ep.getUnchangedOrOldParentNode() == eop.getUnchangedOrOldParentNode()
              || ep.getType() != EditOperationType.UPDATE
                  && ep.getUnchangedOrNewParentNode() == eop.getUnchangedOrNewParentNode())) {
        continue;
      }
      if (eop.getType() == EditOperationType.MOVE) {

        if (eop.getUnchangedOrOldParentNode() == ep.getUnchangedOrOldParentNode()
            && eop.getUnchangedOrNewParentNode() == ep.getUnchangedOrNewParentNode()) {
          if (ep.getOldOrChangedIndex().childrenListIndex < ep
              .getNewOrChangedIndex().childrenListIndex) {
            if (eop.getOldOrChangedIndex().childrenListIndex > ep
                .getOldOrChangedIndex().childrenListIndex
                && eop.getNewOrChangedIndex().childrenListIndex < ep
                    .getNewOrChangedIndex().childrenListIndex) {
              moveOffset--;

            }
          } else if (ep.getOldOrChangedIndex().childrenListIndex > ep
              .getNewOrChangedIndex().childrenListIndex) {
            if (eop.getOldOrChangedIndex().childrenListIndex < ep
                .getOldOrChangedIndex().childrenListIndex
                && eop.getNewOrChangedIndex().childrenListIndex > ep
                    .getNewOrChangedIndex().childrenListIndex) {
              moveOffset++;
            }
          }
        }
      } else {
        if (eop.getType() == EditOperationType.INSERT) {
          if (ep.getOldOrChangedIndex().childrenListIndex < ep
              .getNewOrChangedIndex().childrenListIndex) {
            if (eop.getNewOrChangedIndex().childrenListIndex < ep
                .getNewOrChangedIndex().childrenListIndex) {
              moveOffset--;
            }
          } else if (ep.getOldOrChangedIndex().childrenListIndex > ep
              .getNewOrChangedIndex().childrenListIndex) {
            if (eop.getNewOrChangedIndex().childrenListIndex > ep
                .getNewOrChangedIndex().childrenListIndex) {
              moveOffset++;
            }

          }
          if (eop.getType() == EditOperationType.INSERT) {
            if (ep.getOldOrChangedIndex().childrenListIndex > ep
                .getNewOrChangedIndex().childrenListIndex) {
              if (eop.getNewOrChangedIndex().childrenListIndex > ep
                  .getNewOrChangedIndex().childrenListIndex) {
                moveOffset++;
              }

            }
          }
        }
      }
    }
    return moveOffset;
  }



  public static FilterRule getInstance() {
    return new RemoveMovesObsoleteDueToWildcards();
  }
}
