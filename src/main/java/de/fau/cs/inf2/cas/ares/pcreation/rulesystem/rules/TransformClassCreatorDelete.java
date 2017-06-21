package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNew;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransformClassCreatorDelete extends AbstractFilterRule {

  public TransformClassCreatorDelete() {
    super(FilterRuleType.TRANSFORM_CLASS_CREATOR_DELETE);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return classCreator(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);
  }

  private static List<BastEditOperation> classCreator(
      List<BastEditOperation> workList, ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    List<BastEditOperation> toRemove = new LinkedList<>();
    List<BastEditOperation> toAdd = new LinkedList<>();
    for (BastEditOperation ep : workList) {
      if (ep.getType() == EditOperationType.DELETE) {
        if (ep.getOldOrInsertedNode().getTag() == BastNew.TAG) {
          if (((BastNew) ep.getOldOrInsertedNode()).declarations != null
              && ((BastNew) ep.getOldOrInsertedNode()).declarations.size() == 1
              && ((BastNew) ep.getOldOrInsertedNode()).declarations.getFirst()
                  .getTag() == BastFunction.TAG) {
            AbstractBastNode partnerParent =
                exDiffAa.firstToSecondMap.get(ep.getUnchangedOrOldParentNode());
            if (partnerParent != null) {
              AbstractBastNode partner =
                  WildcardAccessHelper.getNodeToIndex(partnerParent, ep.getOldOrChangedIndex());
              if (exDiffAa.editMapNew.get(partner) != null) {
                toRemove.add(ep);
                toRemove.add(exDiffAa.editMapNew.get(partner));
              }
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
    return new TransformClassCreatorDelete();
  }
}