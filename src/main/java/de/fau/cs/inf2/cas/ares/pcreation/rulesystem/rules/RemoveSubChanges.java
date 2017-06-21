package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
import de.fau.cs.inf2.cas.ares.pcreation.MatchBoundary;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.CollectCorrespondingNodesVisitor;

import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformation;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastListInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitchCaseGroup;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.DeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.MoveOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RemoveSubChanges extends AbstractFilterRule {

  public RemoveSubChanges() {
    super(FilterRuleType.REMOVE_SUB_CHANGES);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    worklist = standardSubChanges(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond,
        delInsertMap, alignList, matchBoundary);
    worklist = handleInserts(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond,
        matchBoundary, delInsertMap);
    worklist = standardSubChangesPartTwo(worklist, exDiffCurrent, matchBoundary);
    worklist = handleDuplicateDeletes(worklist, exDiffCurrent, delInsertMap);
    worklist = handleMovements(worklist, exDiffCurrent);
    worklist = handleCallDeletes(worklist, exDiffCurrent, hierarchyFirst);
    return handleSpecificCallChanges(worklist, exDiffCurrent, hierarchyFirst);



  }

  private static List<BastEditOperation> standardSubChanges(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffBb,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond,
      HashMap<AbstractBastNode, AbstractBastNode> delInsertMap, List<BastEditOperation> alignList,
      MatchBoundary boundary) {
    List<AbstractBastNode> insertedNodes = new ArrayList<>();
    List<AbstractBastNode> deletedNodes = new ArrayList<>();
    List<AbstractBastNode> insertedExprInit = new ArrayList<>();
    List<AbstractBastNode> movedNodes = new ArrayList<>();
    for (BastEditOperation ep : workList) {
      switch (ep.getType()) {

        case MOVE:

          deletedNodes.add((AbstractBastNode) (ep).getOldOrInsertedNode());
          insertedNodes.add((AbstractBastNode) (ep).getNewOrChangedNode());
          movedNodes.add(ep.getOldOrInsertedNode());
          movedNodes.add(ep.getNewOrChangedNode());
          break;
        case ALIGN:
        case UPDATE:
          break;
        case INSERT:
          if (ep.getOldOrInsertedNode().getTag() != BastExprInitializer.TAG
              && ep.getOldOrInsertedNode().getTag() != BastDeclaration.TAG) {
            insertedNodes.add((AbstractBastNode) ((InsertOperation) ep).getOldOrInsertedNode());
          } else {
            insertedExprInit.add(ep.getOldOrInsertedNode());

          }
          break;

        case DELETE:
          deletedNodes.add((AbstractBastNode) ((DeleteOperation) ep).getOldOrInsertedNode());
          break;

        default:
      }
    }

    List<AbstractBastNode> insertedNodesToAdd = new ArrayList<>();
    for (AbstractBastNode node : insertedExprInit) {
      NodeParentInformationHierarchy npi = hierarchySecond.get(node);
      if (npi == null || npi.list.size() < 2) {
        insertedNodes.add(node);
      } else {
        if (insertedNodes.contains(npi.list.get(1).parent)) {
          insertedNodes.add(node);
          if (!insertedNodes.contains(npi.list.get(0).parent)) {
            insertedNodes.add(npi.list.get(0).parent);
          }
        } else if (npi.list.get(0).parent.getTag() == BastListInitializer.TAG
            && insertedNodes.contains(npi.list.get(2).parent)) {
          insertedNodes.add(node);
          if (!insertedNodes.contains(npi.list.get(0).parent)) {
            insertedNodes.add(npi.list.get(0).parent);
          }
          if (!insertedNodes.contains(npi.list.get(1).parent)) {
            insertedNodes.add(npi.list.get(1).parent);
          }
        } else {
          insertedNodesToAdd.add(node);
          insertedNodes.add(node);
        }
      }
    }
    List<BastEditOperation> workListTmp = workList;
    workList = new ArrayList<>();
    for (BastEditOperation ep : workListTmp) {
      switch (ep.getType()) {

        case MOVE:
          if (ep.getUnchangedOrNewParentNode().getTag() == BastExprInitializer.TAG) {
            NodeParentInformationHierarchy npi =
                hierarchySecond.get(ep.getUnchangedOrNewParentNode());
            if (npi != null && npi.list != null && npi.list.size() > 1) {
              if (insertedNodes.contains(npi.list.get(1).parent)) {
                break;
              }
            }
          }
          if (!insertedNodes.contains(((MoveOperation) ep).getUnchangedOrNewParentNode())) {
            workList.add(ep);
          } else {
            if (!deletedNodes.contains(ep.getUnchangedOrOldParentNode())
                && ep.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG
                && ep.getUnchangedOrNewParentNode().getTag() != BastBlock.TAG) {
              AbstractBastNode partner =
                  exDiffBb.secondToFirstMap.get(ep.getUnchangedOrNewParentNode());
              boolean found = false;
              if (partner != null) {
                if (WildcardAccessHelper.isPart(partner, ep.getOldOrInsertedNode())) {
                  found = true;
                }
              }
              if (!found) {
                DeleteOperation delOp = new DeleteOperation(ep.getUnchangedOrOldParentNode(),
                    ep.getOldOrInsertedNode(), ep.getOldOrChangedIndex());
                workList.add(delOp);
                continue;
              }
            }
            if (ep.getUnchangedOrNewParentNode().getTag() == BastBlock.TAG) {
              NodeParentInformationHierarchy npi2 =
                  hierarchySecond.get(ep.getUnchangedOrOldParentNode());

              if (npi2 != null) {
                if (npi2.list.size() > 0 && npi2.list.get(0) != null) {
                  if (insertedNodes.contains(npi2.list.get(0).parent)) {
                    if (exDiffBb.secondToFirstMap.get(ep.getUnchangedOrNewParentNode()) == boundary
                        .getNode1() && ep.getUnchangedOrNewParentNode() == boundary.getNode2()) {
                      break;
                    }

                  }
                }
              }
              workList.add(ep);
              break;
            } else if (!deletedNodes.contains(ep.getUnchangedOrOldParentNode())
                && ep.getUnchangedOrNewParentNode().getTag() != AresPatternClause.TAG) {
              DeleteOperation delOp = new DeleteOperation(ep.getUnchangedOrOldParentNode(),
                  ep.getOldOrInsertedNode(), ep.getOldOrChangedIndex());
              workList.add(delOp);
              continue;
            }
          }
          break;
        case ALIGN:
        case UPDATE:
          workList.add(ep);
          break;
        case INSERT:
          if (ep.getUnchangedOrNewParentNode().getTag() == BastIf.TAG) {
            if (insertedNodes.contains(ep.getUnchangedOrNewParentNode())) {
              break;
            }
          } else if (ep.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG) {
            NodeParentInformationHierarchy npi2 =
                hierarchySecond.get(ep.getUnchangedOrOldParentNode());

            if (npi2 != null) {
              if (npi2.list.size() > 0 && npi2.list.get(0) != null) {
                if (insertedNodes.contains(npi2.list.get(0).parent)) {
                  if (exDiffBb.secondToFirstMap.get(ep.getUnchangedOrNewParentNode()) == boundary
                      .getNode1() && ep.getUnchangedOrNewParentNode() == boundary.getNode2()) {
                    break;
                  } else {
                    boolean found = false;
                    for (BastEditOperation epInner : workListTmp) {
                      if (epInner.getNewOrChangedNode() == npi2.list.get(0).parent
                          && epInner.getType() == EditOperationType.INSERT) {
                        found = true;
                        break;
                      }
                    }
                    if (found) {
                      break;
                    }
                  }

                }
              }
            }
            workList.add(ep);
            break;
          }
          if (insertedNodesToAdd.contains(ep.getOldOrInsertedNode())) {
            workList.add(ep);
            break;
          }
          if (ep.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG) {
            NodeParentInformationHierarchy npi2 =
                hierarchySecond.get(ep.getUnchangedOrOldParentNode());

            if (npi2 != null) {
              if (npi2.list.size() > 0 && npi2.list.get(0) != null) {
                if (insertedNodes.contains(npi2.list.get(0).parent)) {
                  if (movedNodes.contains(npi2.list.get(0).parent)) {
                    if (!insertedNodes.contains(npi2.list.get(1).parent)) {
                      workList.add(ep);
                      break;
                    }
                  }
                  break;
                } else {
                  workList.add(ep);
                }
              }
            }
          } else {

            if (!insertedNodes.contains(ep.getUnchangedOrOldParentNode())) {
              workList.add(ep);
            } else {
              NodeParentInformationHierarchy npi2 = hierarchySecond.get(ep.getOldOrInsertedNode());
              if (movedNodes.contains(npi2.list.get(0).parent)) {
                if (!insertedNodes.contains(npi2.list.get(1).parent)) {
                  workList.add(ep);
                }
              }
            }
          }

          break;
        case DELETE:
          if (!deletedNodes.contains(((DeleteOperation) ep).getUnchangedOrOldParentNode())
              || ((DeleteOperation) ep).getUnchangedOrOldParentNode()
                  .getTag() == BastSwitchCaseGroup.TAG
              || ((DeleteOperation) ep).getUnchangedOrOldParentNode().getTag() == BastBlock.TAG) {
            if (delInsertMap.containsKey(ep.getUnchangedOrOldParentNode())) {
              AbstractBastNode parent =
                  hierarchyFirst.get(ep.getUnchangedOrOldParentNode()).list.get(0).parent;
              if (deletedNodes.contains((parent))
                  || (parent.getTag() == BastBlock.TAG && parent != boundary.getNode1())) {
                continue;
              } else {
                if (ep.getUnchangedOrOldParentNode().getTag() == BastExprInitializer.TAG) {
                  parent = hierarchyFirst.get(ep.getUnchangedOrOldParentNode()).list.get(0).parent;
                  if (parent.getTag() == BastListInitializer.TAG) {
                    continue;
                  }
                }
                workList.add(ep);
              }
            } else {
              workList.add(ep);
            }
          }
          break;
        default:
      }
    }
    return workList;
  }

  private static List<BastEditOperation> handleInserts(List<BastEditOperation> editList,
      ExtendedDiffResult exDiffBb, Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy2, MatchBoundary boundary,
      HashMap<AbstractBastNode, AbstractBastNode> delInsertMap) {
    ArrayList<BastEditOperation> workList = new ArrayList<>();
    for (BastEditOperation ep : editList) {
      switch (ep.getType()) {
        case INSERT:
          if (hierarchy2.get(ep.getUnchangedOrNewParentNode()) != null
              && hierarchy2.get(ep.getUnchangedOrNewParentNode()).list.get(0).parent
                  .getTag() == AresBlock.TAG) {
            if (boundary.getNode2() == ep.getUnchangedOrNewParentNode()) {
              if (hierarchy1.get(boundary.getNode1()) != null
                  && hierarchy1.get(boundary.getNode1()).list.get(0).parent
                      .getTag() == BastFunction.TAG) {
                if (boundary.getNode1().getField(boundary.field1) != null
                    && boundary.getNode1().getField(boundary.field1).getListField() != null
                    && boundary.getNode1().getField(boundary.field1).getListField()
                        .size() > ep.getNewOrChangedIndex().childrenListIndex) {
                  if (boundary.getNode1().getField(boundary.field1).getListField()
                      .get(ep.getNewOrChangedIndex().childrenListIndex)
                      .getTag() == ep.getOldOrInsertedNode().getTag()) {
                    if (ep.getOldOrInsertedNode().getTag() == BastCall.TAG) {
                      if (((BastCall) boundary.getNode1().getField(boundary.field1).getListField()
                          .get(ep.getNewOrChangedIndex().childrenListIndex)).function
                              .getTag() == ((BastCall) ep.getOldOrInsertedNode()).function
                                  .getTag()) {
                        BastCall call1 = ((BastCall) boundary.getNode1().getField(boundary.field1)
                            .getListField().get(ep.getNewOrChangedIndex().childrenListIndex));
                        BastCall call2 = ((BastCall) ep.getOldOrInsertedNode());
                        if (call1.arguments.size() == call2.arguments.size()) {
                          if (call1.arguments.size() == 0) {
                            continue;
                          }
                          if (call1.arguments.get(0).getTag() == call2.arguments.get(0).getTag()) {
                            workList.add(ep);

                            continue;
                          } else {
                            workList.add(ep);
                            continue;
                          }
                        } else {
                          continue;
                        }
                      } else {
                        workList.add(ep);
                        break;
                      }
                    }
                    workList.add(ep);
                    continue;
                  }
                }
              }
            }

          }
          workList.add(ep);
          break;
        case DELETE:
          if (hierarchy2.get(ep.getUnchangedOrNewParentNode()) != null
              && hierarchy2.get(ep.getUnchangedOrNewParentNode()).list.get(0).parent
                  .getTag() == AresBlock.TAG) {
            if (boundary.getNode2() == ep.getUnchangedOrNewParentNode()) {
              if (hierarchy1.get(boundary.getNode1()) != null
                  && hierarchy1.get(boundary.getNode1()).list.get(0).parent
                      .getTag() == BastFunction.TAG) {
                if (boundary.getNode1().getField(boundary.field1).getListField()
                    .size() > ep.getNewOrChangedIndex().childrenListIndex) {
                  if (boundary.getNode1().getField(boundary.field1).getListField()
                      .get(ep.getNewOrChangedIndex().childrenListIndex)
                      .getTag() == ep.getOldOrInsertedNode().getTag()) {
                    continue;
                  }
                }
              }
            }

          }
          workList.add(ep);
          break;
        default:
          workList.add(ep);
      }
    }
    return workList;
  }

  private static List<BastEditOperation> standardSubChangesPartTwo(
      List<BastEditOperation> editScript, ExtendedDiffResult exDiff, MatchBoundary boundary) {
    List<BastEditOperation> workList = new ArrayList<BastEditOperation>();
    List<AbstractBastNode> obsoleteParents = new ArrayList<AbstractBastNode>();
    CollectCorrespondingNodesVisitor nodesVisitor = null;
    for (BastEditOperation ep : editScript) {
      switch (ep.getType()) {
        case INSERT:
          if (ep.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG) {
            nodesVisitor = new CollectCorrespondingNodesVisitor(exDiff.secondToFirstMap);
            ((InsertOperation) ep).getOldOrInsertedNode().accept(nodesVisitor);
            obsoleteParents.addAll(nodesVisitor.nodes);
          }
          break;
        case DELETE:
          if (ep.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG) {

            nodesVisitor = new CollectCorrespondingNodesVisitor(exDiff.secondToFirstMap);
            ((DeleteOperation) ep).getOldOrInsertedNode().accept(nodesVisitor);
            obsoleteParents.addAll(nodesVisitor.nodes);
          }
          break;
        default:
          continue;
      }
    }
    boolean changed = true;
    workList.addAll(editScript);
    while (changed == true) {
      changed = false;
      Iterator<BastEditOperation> it = workList.iterator();
      while (it.hasNext()) {
        BastEditOperation ep = it.next();
        switch (ep.getType()) {
          case DELETE:
            if (obsoleteParents.contains(((DeleteOperation) ep).getUnchangedOrOldParentNode())
                && ep.getUnchangedOrOldParentNode().getTag() != BastExprInitializer.TAG) {
              it.remove();
              changed = true;

              obsoleteParents.add((AbstractBastNode) ((DeleteOperation) ep).getOldOrInsertedNode());
            }
            break;
          case MOVE:
            if (((MoveOperation) ep).getUnchangedOrNewParentNode().getTag() != BastBlock.TAG
                && ep.getUnchangedOrOldParentNode() != boundary.getNode1()) {
              if (obsoleteParents.contains(((MoveOperation) ep).getUnchangedOrNewParentNode())) {
                it.remove();
                changed = true;
              }
            } else {
              if (boundary.getNode1() == ep.getUnchangedOrOldParentNode()
                  && boundary.getNode2() == ep.getUnchangedOrNewParentNode()) {
                if (ep.getOldOrChangedIndex().childrenListIndex == ep
                    .getNewOrChangedIndex().childrenListIndex) {
                  it.remove();
                  changed = true;
                }
              }
            }
            break;
          case INSERT:
            break;
          default:
        }
      }
    }

    return workList;
  }

  private static List<BastEditOperation> handleDuplicateDeletes(List<BastEditOperation> editList,
      ExtendedDiffResult exDiff, Map<AbstractBastNode, AbstractBastNode> delInsertMap) {
    ArrayList<BastEditOperation> workList = new ArrayList<>();
    ArrayList<BastEditOperation> toRemove = new ArrayList<>();

    for (BastEditOperation ep : editList) {
      if (ep.getType() == EditOperationType.INSERT) {
        if (delInsertMap.containsKey(ep.getUnchangedOrOldParentNode())) {
          AbstractBastNode partner = delInsertMap.get(ep.getUnchangedOrOldParentNode());
          if (partner != null && partner.getTag() == ep.getUnchangedOrOldParentNode().getTag()) {
            AbstractBastNode partnerNode = null;
            if (partner.getField(ep.getOldOrChangedIndex().childrenListNumber) != null) {
              if (partner.getField(ep.getOldOrChangedIndex().childrenListNumber).isList()) {
                if (partner.getField(ep.getOldOrChangedIndex().childrenListNumber)
                    .getListField() != null
                    && partner.getField(ep.getOldOrChangedIndex().childrenListNumber).getListField()
                        .size() > ep.getOldOrChangedIndex().childrenListIndex) {
                  partnerNode = partner.getField(ep.getOldOrChangedIndex().childrenListNumber)
                      .getListField().get(ep.getOldOrChangedIndex().childrenListIndex);
                }
              } else {
                partnerNode =
                    partner.getField(ep.getOldOrChangedIndex().childrenListNumber).getField();

              }
              if (partnerNode != null) {
                BastEditOperation delOp = exDiff.editMapOld.get(partnerNode);
                if (delOp != null && editList.contains(delOp)) {
                  toRemove.add(delOp);
                  delInsertMap.put(ep.getOldOrInsertedNode(), partnerNode);
                  delInsertMap.put(partnerNode, ep.getOldOrInsertedNode());

                }
              }
            }
          }
        }
      }
    }
    workList.addAll(editList);
    workList.removeAll(toRemove);
    return workList;
  }

  private static List<BastEditOperation> handleMovements(List<BastEditOperation> workList,
      ExtendedDiffResult extDiff) {
    LinkedList<BastEditOperation> toRemove = new LinkedList<BastEditOperation>();
    for (BastEditOperation ep : workList) {
      if (ep.getType() == EditOperationType.MOVE) {
        if (extDiff.editMapOld.get(ep.getUnchangedOrOldParentNode()) != null
            && (extDiff.editMapOld.get(ep.getUnchangedOrOldParentNode())
                .getType() == EditOperationType.DELETE)
            && workList.contains(extDiff.editMapOld.get(ep.getUnchangedOrOldParentNode()))) {
          if (extDiff.editMapOld.get(ep.getUnchangedOrNewParentNode()) != null
              && (extDiff.editMapOld.get(ep.getUnchangedOrNewParentNode())
                  .getType() == EditOperationType.INSERT)
              && workList.contains(extDiff.editMapOld.get(ep.getUnchangedOrNewParentNode()))) {
            toRemove.add(ep);
          }
        }
      }
    }
    for (BastEditOperation ep : workList) {
      if (ep.getType() == EditOperationType.MOVE) {
        if (ep.getOldOrInsertedNode().getTag() == BastBlock.TAG) {
          for (AbstractBastNode stmt : ((BastBlock) ep.getOldOrInsertedNode()).statements) {
            if (stmt.getTag() == BastIf.TAG
                && ep.getUnchangedOrNewParentNode().getTag() == BastIf.TAG) {
              if (WildcardAccessHelper.isEqual(((BastIf) stmt).condition,
                  ((BastIf) ep.getUnchangedOrNewParentNode()).condition)) {
                toRemove.add(ep);
                break;
              }
            }
          }
        }
      }
    }
    workList.removeAll(toRemove);
    return workList;
  }


  private static List<BastEditOperation> handleCallDeletes(List<BastEditOperation> workList,
      ExtendedDiffResult extDiff,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst) {
    LinkedList<BastEditOperation> toRemove = new LinkedList<BastEditOperation>();

    for (BastEditOperation ep : workList) {
      if (ep.getType() == EditOperationType.MOVE) {
        if (ep.getOldOrInsertedNode().getTag() == BastCall.TAG
            && ep.getUnchangedOrOldParentNode().getTag() == BastCall.TAG
            && ep.getUnchangedOrNewParentNode().getTag() != BastCall.TAG) {
          if (WildcardAccessHelper.isEqual(((BastCall) ep.getUnchangedOrOldParentNode()).function,
              ((BastCall) ep.getNewOrChangedNode()).function)) {
            BastEditOperation epDel = extDiff.editMapOld.get(ep.getUnchangedOrOldParentNode());
            if (epDel != null) {
              toRemove.add(ep);
              toRemove.add(epDel);
              continue;
            }
          }

        }

      }
    }
    workList.removeAll(toRemove);
    return workList;
  }

  public static FilterRule getInstance() {
    return new RemoveSubChanges();
  }

  private static List<BastEditOperation> handleSpecificCallChanges(List<BastEditOperation> workList,
      ExtendedDiffResult extDiff,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst) {
    LinkedList<BastEditOperation> toRemove = new LinkedList<BastEditOperation>();

    for (BastEditOperation ep : workList) {
      if (ep.getType() == EditOperationType.DELETE) {
        if (extDiff.editMapOld.get(ep.getUnchangedOrOldParentNode()) == null
            && (extDiff.firstToSecondMap.get(ep.getUnchangedOrOldParentNode()) != null)) {
          NodeParentInformationHierarchy npi = hierarchyFirst.get(ep.getUnchangedOrOldParentNode());
          if (npi.list.size() > 1) {
            for (int i = 1; i < npi.list.size(); i++) {
              NodeParentInformation np = npi.list.get(i);
              for (BastEditOperation epTmp : workList) {
                if (epTmp.getType() == EditOperationType.DELETE
                    || epTmp.getType() == EditOperationType.DELETE
                    || epTmp.getType() == EditOperationType.MOVE) {
                  if (epTmp.getOldOrInsertedNode() == np.parent) {
                    toRemove.add(ep);
                  }
                }
              }
            }
          }
        }

      }
    }
    workList.removeAll(toRemove);
    return workList;
  }
}
