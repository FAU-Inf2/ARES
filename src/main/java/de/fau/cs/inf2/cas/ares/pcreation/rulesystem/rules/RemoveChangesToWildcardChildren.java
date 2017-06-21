package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RemoveChangesToWildcardChildren extends AbstractFilterRule {

  public RemoveChangesToWildcardChildren() {
    super(FilterRuleType.REMOVE_CHANGES_TO_WILDCARD_CHILDREN);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return removeWildcardChanges(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);
  }

  private static List<BastEditOperation> removeWildcardChanges(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    List<BastEditOperation> toRemove = new LinkedList<>();
    List<BastEditOperation> toAdd = new LinkedList<>();
    for (BastEditOperation ep : workList) {
      if (ep.getType() == EditOperationType.DELETE) {
        if (!ep.getOldOrChangedIndex().childrenListNumber.isList) {
          AbstractBastNode partner =
              exDiffAa.firstToSecondMap.get(ep.getUnchangedOrOldParentNode());
          if (partner != null
              && partner.getField(ep.getOldOrChangedIndex().childrenListNumber) != null) {
            AbstractBastNode node =
                partner.getField(ep.getOldOrChangedIndex().childrenListNumber).getField();
            if (node != null
                && (node.getTag() == AresWildcard.TAG || node.getTag() == AresUseStmt.TAG)) {
              toRemove.add(ep);
            }
          }
        }
      }
    }
    workList.removeAll(toRemove);
    workList.addAll(toAdd);
    return workList;
  }



  public static FilterRule getInstance() {
    return new RemoveChangesToWildcardChildren();
  }
}