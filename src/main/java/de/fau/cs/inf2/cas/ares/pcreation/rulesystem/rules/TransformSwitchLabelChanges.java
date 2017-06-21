package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCase;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitchCaseGroup;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransformSwitchLabelChanges extends AbstractFilterRule {

  public TransformSwitchLabelChanges() {
    super(FilterRuleType.TRANSFORM_SWITCH_LABEL_CHANGES);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return switchLabels(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);
  }


  private static List<BastEditOperation> switchLabels(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    List<BastEditOperation> toRemove = new LinkedList<>();
    for (BastEditOperation editOp : workList) {
      if (editOp.getType() == EditOperationType.MOVE) {
        if (editOp.getOldOrInsertedNode().getTag() == BastCase.TAG) {
          toRemove.add(editOp);
          continue;
        }
      } else if (editOp.getType() == EditOperationType.DELETE) {
        if (editOp.getOldOrInsertedNode().getTag() == BastSwitchCaseGroup.TAG) {
          toRemove.add(editOp);
          continue;
        }
      }

    }
    workList.removeAll(toRemove);
    return workList;
  }



  public static FilterRule getInstance() {
    return new TransformSwitchLabelChanges();
  }
}