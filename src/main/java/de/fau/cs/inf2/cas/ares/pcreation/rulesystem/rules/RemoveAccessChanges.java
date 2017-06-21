package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.pcreation.MatchBoundary;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAccess;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RemoveAccessChanges extends AbstractFilterRule {

  public RemoveAccessChanges() {
    super(FilterRuleType.REMOVE_ACCESS_CHANGES);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return handleAccess(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond, matchBoundary);
  }

  private static List<BastEditOperation> handleAccess(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond,
      MatchBoundary matchBoundary) {
    List<BastEditOperation> toRemove = new LinkedList<>();

    for (BastEditOperation editOp : workList) {
      if (editOp.getType() == EditOperationType.DELETE) {
        if (editOp.getOldOrInsertedNode().getTag() == BastAccess.TAG) {
          AbstractBastNode parentPartner =
              exDiffAa.firstToSecondMap.get(editOp.getUnchangedOrOldParentNode());
          if (parentPartner != null) {
            if (WildcardAccessHelper.getNodeToIndex(parentPartner,
                editOp.getOldOrChangedIndex()) != null
                && WildcardAccessHelper
                    .getNodeToIndex(parentPartner, editOp.getOldOrChangedIndex())
                    .getTag() == BastAccess.TAG) {
              toRemove.add(editOp);
            }
          }
        }
      }
    }
    workList.removeAll(toRemove);
    return workList;
  }



  public static FilterRule getInstance() {
    return new RemoveAccessChanges();
  }
}