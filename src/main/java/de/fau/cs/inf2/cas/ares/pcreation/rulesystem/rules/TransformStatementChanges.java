package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.pcreation.MatchBoundary;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransformStatementChanges extends AbstractFilterRule {

  public TransformStatementChanges() {
    super(FilterRuleType.TRANSFORM_STATEMENT_CHANGES);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return unaryNodes(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond, matchBoundary,
        exDiffBA1, exDiffBA2);
  }

  private static List<BastEditOperation> unaryNodes(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond,
      MatchBoundary boundary, ExtendedDiffResult exDiffBA1, ExtendedDiffResult exDiffBA2) {
    List<BastEditOperation> toRemove = new LinkedList<>();
    List<BastEditOperation> toAdd = new LinkedList<>();

    outer: for (BastEditOperation editOp : workList) {
      if (editOp.getType() == EditOperationType.INSERT) {
        List<BastFieldConstants> constants =
            BastFieldConstants.getFieldMap(editOp.getOldOrInsertedNode().getTag());
        if (constants != null && constants.size() == 1 && !constants.get(0).isList) {
          NodeParentInformationHierarchy npi = hierarchySecond.get(editOp.getOldOrInsertedNode());
          if (npi != null && npi.list != null && npi.list.size() > 0) {
            AbstractBastNode parent = npi.list.get(0).parent;
            AbstractBastNode partner = exDiffAa.secondToFirstMap.get(parent);
            if (partner == null && parent.getTag() == BastBlock.TAG) {
              for (BastEditOperation editOpOther : workList) {
                if (editOpOther.getType() == EditOperationType.DELETE) {
                  if (editOp.getNewOrChangedNode().getTag() == editOpOther.getOldOrInsertedNode()
                      .getTag()) {
                    if (editOpOther.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG) {
                      if (editOp.getNewOrChangedIndex().childrenListIndex == editOpOther
                          .getOldOrChangedIndex().childrenListIndex) {
                        if (npi.list.get(1).parent.getTag() == AresBlock.TAG) {
                          NodeParentInformationHierarchy npiOther =
                              hierarchyFirst.get(editOpOther.getOldOrInsertedNode());
                          if (npiOther.list.size() > 1
                              && npiOther.list.get(1).parent.getTag() == BastFunction.TAG) {
                            toRemove.add(editOp);
                            toRemove.add(editOpOther);
                            if (!WildcardAccessHelper.isEqual(editOp.getOldOrInsertedNode(),
                                editOpOther.getOldOrInsertedNode())) {
                              InsertOperation subPart = new InsertOperation(
                                  editOp.getOldOrInsertedNode(), editOp.getOldOrInsertedNode()
                                      .getField(constants.get(0)).getField(),
                                  new NodeIndex(constants.get(0), -1));
                              toAdd.add(subPart);
                            }
                            continue outer;
                          }
                        }
                      }
                    }
                  }

                }
              }

            }
            if (partner != null && partner.getTag() == BastBlock.TAG) {
              @SuppressWarnings("unchecked")
              List<AbstractBastStatement> stmts = (List<AbstractBastStatement>) partner
                  .getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
              List<AbstractBastNode> newList = new LinkedList<>();
              int pos = -1;
              AbstractBastNode partnerStmt = null;
              for (AbstractBastNode stmt : stmts) {
                BastEditOperation operation = exDiffAa.editMapOld.get(stmt);
                if (operation == null || !workList.contains(operation)) {
                  newList.add(stmt);
                }
                if (stmt.getTag() == editOp.getOldOrInsertedNode().getTag()) {
                  pos = newList.size();
                  partnerStmt = stmt;
                  break;
                }
              }
              if (pos != -1 && pos == npi.list.get(0).listId) {
                toRemove.add(editOp);
                if (!WildcardAccessHelper.isEqual(editOp.getOldOrInsertedNode(), partnerStmt)) {
                  InsertOperation subPart = new InsertOperation(editOp.getOldOrInsertedNode(),
                      editOp.getOldOrInsertedNode().getField(constants.get(0)).getField(),
                      new NodeIndex(constants.get(0), -1));
                  toAdd.add(subPart);
                }
                BastEditOperation partnerOp = exDiffAa.editMapOld.get(partnerStmt);
                if (partnerOp != null && workList.contains(partnerOp)) {
                  if (partnerOp.getType() == EditOperationType.MOVE) {
                    InsertOperation insertOp =
                        new InsertOperation(partnerOp.getUnchangedOrNewParentNode(),
                            partnerOp.getNewOrChangedNode(), partnerOp.getNewOrChangedIndex());
                    toAdd.add(insertOp);
                  }
                  toRemove.add(partnerOp);
                }
              }
            } else {
              if (npi.list.get(0).listId > 0) {
                AbstractBastNode nodeAbove = WildcardAccessHelper.getNodeToIndex(parent,
                    npi.list.get(0).fieldConstant, npi.list.get(0).listId - 1);
                if (WildcardAccessHelper.isExprWildcard(nodeAbove)) {
                  AbstractBastNode wildcardExpr = WildcardAccessHelper.getExpr(nodeAbove);
                  if (wildcardExpr != null
                      && WildcardAccessHelper.isEqual(wildcardExpr, WildcardAccessHelper
                          .getNodeToIndex(editOp.getNewOrChangedNode(), constants.get(0), -1))) {
                    toRemove.add(editOp);
                  }
                }
              }
            }
          }
        }
      }

    }
    workList.addAll(toAdd);
    workList.removeAll(toRemove);
    return workList;
  }



  public static FilterRule getInstance() {
    return new TransformStatementChanges();
  }
}