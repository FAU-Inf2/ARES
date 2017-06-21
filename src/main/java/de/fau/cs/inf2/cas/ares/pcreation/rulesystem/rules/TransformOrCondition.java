package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.bast.visitors.NodeStreamVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondOr;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransformOrCondition extends AbstractFilterRule {

  public TransformOrCondition() {
    super(FilterRuleType.TRANSFORM_OR_CONDITION);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return handleOrCondition(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);
  }

  private static List<BastEditOperation> handleOrCondition(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    List<BastEditOperation> toRemove = new LinkedList<>();
    List<BastEditOperation> toAdd = new LinkedList<>();
    for (BastEditOperation ep : workList) {
      if (ep.getType() == EditOperationType.INSERT) {
        if (ep.getOldOrChangedIndex().childrenListNumber == BastFieldConstants.IF_CONDITION) {
          if (ep.getOldOrInsertedNode().getTag() == BastCondOr.TAG) {
            AbstractBastNode partner =
                exDiffAa.secondToFirstMap.get(ep.getUnchangedOrOldParentNode());
            if (partner != null) {
              NodeStreamVisitor firstTokens = new NodeStreamVisitor(ep.getOldOrInsertedNode());
              ep.getOldOrInsertedNode().accept(firstTokens);
              AbstractBastNode partnerCondition = ep.getUnchangedOrOldParentNode()
                  .getField(BastFieldConstants.IF_CONDITION).getField();
              NodeStreamVisitor secondTokens = new NodeStreamVisitor(partnerCondition);
              partnerCondition.accept(secondTokens);
              if (firstTokens.nodes.size() == secondTokens.nodes.size()) {
                toRemove.add(ep);
                for (AbstractBastNode node : firstTokens.nodes) {
                  BastEditOperation epTmp = exDiffAa.editMapNew.get(node);
                  if (epTmp != null) {
                    toRemove.add(epTmp);
                  }
                }
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
    return new TransformOrCondition();
  }
}