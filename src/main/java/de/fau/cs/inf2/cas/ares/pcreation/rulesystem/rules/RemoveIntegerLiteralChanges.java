package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RemoveIntegerLiteralChanges extends AbstractFilterRule {

  public RemoveIntegerLiteralChanges() {
    super(FilterRuleType.REMOVE_INTEGER_LITERAL_CHANGES);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    worklist = integerMoves(worklist);
    return integerDeletes(worklist, exDiffCurrent, delInsertMap);

  }

  private static List<BastEditOperation> integerMoves(List<BastEditOperation> editList) {
    ArrayList<BastEditOperation> workList = new ArrayList<>();

    for (BastEditOperation ep : editList) {
      switch (ep.getType()) {
        case MOVE:
          if (ep.getOldOrInsertedNode().getTag() == BastIntConst.TAG) {
            continue;
          }
          workList.add(ep);
          break;
        default:
          workList.add(ep);
      }
    }

    return workList;
  }



  public static FilterRule getInstance() {
    return new RemoveIntegerLiteralChanges();
  }

  private static List<BastEditOperation> integerDeletes(List<BastEditOperation> editList,
      ExtendedDiffResult extDiff, HashMap<AbstractBastNode, AbstractBastNode> delInsertMap) {
    ArrayList<BastEditOperation> workList = new ArrayList<>();
    ArrayList<BastEditOperation> toRemove = new ArrayList<>();

    for (BastEditOperation ep : editList) {
      switch (ep.getType()) {
        case DELETE:
          if (ep.getUnchangedOrOldParentNode().getTag() == BastCall.TAG) {
            if (ep.getOldOrInsertedNode().getTag() == BastIntConst.TAG) {
              if (extDiff.firstToSecondMap.get(ep.getUnchangedOrOldParentNode()) != null) {
                if (((BastCall) ep.getUnchangedOrOldParentNode()).arguments
                    .size() == ((BastCall) extDiff.firstToSecondMap
                        .get(ep.getUnchangedOrOldParentNode())).arguments.size()) {
                  AbstractBastNode partner =
                      extDiff.firstToSecondMap.get(ep.getUnchangedOrOldParentNode());
                  AbstractBastNode insertNode =
                      WildcardAccessHelper.getNodeToIndex(partner, ep.getOldOrChangedIndex());
                  if (insertNode != null && insertNode.getTag() == BastIntConst.TAG) {
                    if (extDiff.editMapNew.get(insertNode) != null) {
                      toRemove.add(extDiff.editMapNew.get(insertNode));
                    }
                  }
                  continue;
                } else {
                  BastCall partner =
                      (BastCall) extDiff.firstToSecondMap.get(ep.getUnchangedOrOldParentNode());
                  if (ep.getOldOrChangedIndex().childrenListIndex < partner.arguments.size()
                      && partner.arguments.get(ep.getOldOrChangedIndex().childrenListIndex)
                          .getTag() == BastIntConst.TAG) {
                    continue;
                  }
                }
              } else {
                if (delInsertMap.get(ep.getUnchangedOrOldParentNode()) != null) {
                  BastCall partner =
                      (BastCall) delInsertMap.get(ep.getUnchangedOrOldParentNode());
                  if (ep.getOldOrChangedIndex().childrenListIndex < partner.arguments.size()
                      && partner.arguments.get(ep.getOldOrChangedIndex().childrenListIndex)
                          .getTag() == BastIntConst.TAG) {
                    continue;
                  }
                }
              }

            }
          }

          workList.add(ep);
          break;
        case INSERT:
          if (ep.getUnchangedOrNewParentNode().getTag() == BastCall.TAG) {
            if (ep.getNewOrChangedNode().getTag() == BastIntConst.TAG) {
              if (extDiff.secondToFirstMap.get(ep.getUnchangedOrNewParentNode()) != null) {
                if (((BastCall) ep.getUnchangedOrNewParentNode()).arguments
                    .size() == ((BastCall) extDiff.secondToFirstMap
                        .get(ep.getUnchangedOrNewParentNode())).arguments.size()) {
                  AbstractBastNode partner =
                      extDiff.secondToFirstMap.get(ep.getUnchangedOrNewParentNode());
                  AbstractBastNode insertNode =
                      WildcardAccessHelper.getNodeToIndex(partner, ep.getNewOrChangedIndex());
                  if (insertNode != null && insertNode.getTag() == BastIntConst.TAG) {
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
    workList.removeAll(toRemove);
    return workList;
  }
}