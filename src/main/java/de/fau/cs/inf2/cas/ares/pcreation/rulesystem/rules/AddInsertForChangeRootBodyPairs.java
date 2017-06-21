package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.pcreation.MatchBoundary;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AddInsertForChangeRootBodyPairs extends AbstractFilterRule {

  public AddInsertForChangeRootBodyPairs() {
    super(FilterRuleType.ADD_INSERT_FOR_CHANGE_ROOT_BODY_PAIRS);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return handleChangeRootBydoPairs(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond,
        matchBoundary);
  }

  private static List<BastEditOperation> handleChangeRootBydoPairs(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond,
      MatchBoundary matchBoundary) {
    List<BastEditOperation> toRemove = new LinkedList<>();
    List<BastEditOperation> toAdd = new LinkedList<>();
    NodeParentInformationHierarchy npiTmp = hierarchySecond.get(matchBoundary.getNode2());
    AresBlock block = null;
    if (matchBoundary.getNode2().getTag() == AresBlock.TAG) {
      block = (AresBlock) matchBoundary.getNode2();
    } else {
      for (int i = 0; i < npiTmp.list.size(); i++) {
        if (npiTmp.list.get(i).parent.getTag() == AresBlock.TAG) {
          block = (AresBlock) npiTmp.list.get(i).parent;
          break;
        }
      }
    }
    if (block != null) {
      AbstractBastNode partner = exDiffAa.secondToFirstMap.get(block.block);
      if (partner != null) {
        NodeParentInformationHierarchy npi = hierarchyFirst.get(partner);
        if (npi.list.size() > 0) {
          AbstractBastNode parent = npi.list.get(0).parent;
          if (parent.getTag() != BastFunction.TAG) {
            LinkedList<? extends AbstractBastNode> nodes =
                partner.getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
            if (nodes != null) {
              for (AbstractBastNode node : nodes) {
                if (exDiffAa.editMapOld.get(node) == null) {
                  AbstractBastNode toInsertNode = exDiffAa.firstToSecondMap.get(node);
                  NodeParentInformationHierarchy npi2 = hierarchySecond.get(toInsertNode);
                  if (npi2.list.size() > 0) {
                    InsertOperation insOp = new InsertOperation(npi2.list.get(0).parent,
                        toInsertNode,
                        new NodeIndex(npi2.list.get(0).fieldConstant, npi2.list.get(0).listId));
                    toAdd.add(insOp);
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
    return new AddInsertForChangeRootBodyPairs();
  }
}