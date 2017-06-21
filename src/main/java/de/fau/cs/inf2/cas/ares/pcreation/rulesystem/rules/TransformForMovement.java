package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;
import de.fau.cs.inf2.cas.ares.recommendation.extension.ExtendedTemplateExtractor;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastForStmt;
import de.fau.cs.inf2.cas.common.bast.visitors.CollectNodesVisitor;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransformForMovement extends AbstractFilterRule {

  public TransformForMovement() {
    super(FilterRuleType.TRANSFORM_FOR_MOVEMENT);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return handleFor(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);
  }

  private static List<BastEditOperation> handleFor(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    LinkedList<BastEditOperation> toRemove = new LinkedList<>();
    LinkedList<BastEditOperation> toAdd = new LinkedList<>();
    for (BastEditOperation ep : workList) {
      if (ep.getType() == EditOperationType.MOVE) {
        if (ep.getOldOrInsertedNode().getTag() == BastForStmt.TAG) {
          if (exDiffAa.firstToSecondMap.get(ep.getUnchangedOrOldParentNode()) != ep
              .getUnchangedOrNewParentNode()) {
            AbstractBastNode newblock = ep.getNewOrChangedNode()
                .getField(BastFieldConstants.FOR_STMT_STATEMENT).getField();
            AbstractBastNode oldBlock = exDiffAa.secondToFirstMap.get(newblock);
            if (oldBlock != null) {
              NodeParentInformationHierarchy blockHierarchy = hierarchyFirst.get(oldBlock);
              if (blockHierarchy != null && blockHierarchy.list.size() > 1) {
                AbstractBastNode possibleFor = blockHierarchy.list.get(0).parent;
                if (possibleFor.getTag() == BastForStmt.TAG) {
                  AbstractBastNode partner = blockHierarchy.list.get(1).parent;
                  if (exDiffAa.firstToSecondMap.get(partner) == ep
                      .getUnchangedOrNewParentNode()) {
                    toRemove.add(ep);
                  }
                }

                CollectNodesVisitor nodesVisitor = new CollectNodesVisitor();
                possibleFor.accept(nodesVisitor);
                ep.getNewOrChangedNode().accept(nodesVisitor);
                HashSet<AbstractBastNode> nodes = new HashSet<>(nodesVisitor.nodes);
                for (BastEditOperation innerEp : workList) {
                  if (nodes.contains(innerEp.getOldOrInsertedNode())
                      || nodes.contains(innerEp.getNewOrChangedNode())) {
                    toRemove.add(innerEp);
                  }
                }
                nodesVisitor = new CollectNodesVisitor();
                ep.getOldOrInsertedNode().accept(nodesVisitor);
                for (BastEditOperation innerEp : workList) {
                  if (nodes.contains(innerEp.getOldOrInsertedNode())
                      && innerEp.getType() != EditOperationType.MOVE) {
                    toRemove.add(innerEp);
                  }
                }
                ExtendedDiffResult exDiffNew = ExtendedTemplateExtractor.pipeline(null,
                    possibleFor, ep.getNewOrChangedNode(), null);
                toAdd.addAll(exDiffNew.editScript);
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
    return new TransformForMovement();
  }
}