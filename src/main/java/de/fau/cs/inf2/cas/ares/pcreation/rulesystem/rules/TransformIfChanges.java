package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIf;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.DeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransformIfChanges extends AbstractFilterRule {

  public TransformIfChanges() {
    super(FilterRuleType.TRANSFORM_IF_CHANGES);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return handleIf(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);
  }

  private static List<BastEditOperation> handleIf(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffCurrent,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    LinkedList<BastIf> ifStmts = new LinkedList<>();
    LinkedList<BastIf> deleteIfStmts = new LinkedList<>();
    for (BastEditOperation ep : workList) {
      if (ep.getType() != EditOperationType.UPDATE) {
        if (ep.getUnchangedOrOldParentNode().getTag() == BastIf.TAG
            && ep.getUnchangedOrNewParentNode().getTag() == BastIf.TAG) {
          if (!ifStmts.contains(ep.getUnchangedOrOldParentNode())) {
            ifStmts.add((BastIf) ep.getUnchangedOrOldParentNode());
          }
          if (ep.getType() == EditOperationType.DELETE
              || ep.getType() == EditOperationType.MOVE) {
            if (!deleteIfStmts.contains(ep.getUnchangedOrOldParentNode())) {
              deleteIfStmts.add((BastIf) ep.getUnchangedOrOldParentNode());
            }
          }
          if (ep.getType() == EditOperationType.INSERT
              || ep.getType() == EditOperationType.MOVE) {
            if (!ifStmts.contains(ep.getUnchangedOrNewParentNode())) {
              ifStmts.add((BastIf) ep.getUnchangedOrNewParentNode());
            }
          }
        }
      }
    }
    LinkedList<BastEditOperation> toRemove = new LinkedList<>();
    LinkedList<BastEditOperation> toAdd = new LinkedList<>();

    for (BastIf ifStmt : ifStmts) {
      boolean condition = false;
      boolean body = false;
      boolean elsePart = false;
      LinkedList<AbstractBastStatement> ifStatements = null;
      int ifCount = 0;
      LinkedList<AbstractBastStatement> elseStatements = null;
      int elseCount = 0;
      AbstractBastNode ifBlock = null;
      AbstractBastNode elseBlock = null;
      if (ifStmt.ifPart.getTag() == BastBlock.TAG) {
        ifBlock = ifStmt.ifPart;
        ifStatements = ((BastBlock) ifStmt.ifPart).statements;
      }
      if (ifStmt.elsePart != null && ifStmt.elsePart.getTag() == BastBlock.TAG) {
        elseBlock = ifStmt.elsePart;
        elseStatements = ((BastBlock) ifStmt.elsePart).statements;
      }
      LinkedList<BastEditOperation> tmpToRemove = new LinkedList<>();
      if (ifStmt.elsePart == null) {
        elsePart = true;
      }
      for (BastEditOperation ep : workList) {
        if (ep.getType() != EditOperationType.UPDATE) {
          if (ep.getUnchangedOrOldParentNode() == ifStmt) {
            if (ep.getOldOrChangedIndex().childrenListNumber == BastFieldConstants.IF_CONDITION) {
              condition = true;
              tmpToRemove.add(ep);
              if (ep.getType() == EditOperationType.MOVE) {
                InsertOperation ip = new InsertOperation(ep.getUnchangedOrNewParentNode(),
                    ep.getNewOrChangedNode(), ep.getNewOrChangedIndex());
                toAdd.add(ip);
              }
            } else if (ep
                .getOldOrChangedIndex().childrenListNumber == BastFieldConstants.IF_IF_PART) {
              body = true;
              tmpToRemove.add(ep);

            } else if (ep
                .getOldOrChangedIndex().childrenListNumber == BastFieldConstants.IF_ELSE_PART) {
              elsePart = true;
              tmpToRemove.add(ep);

            }
          } else if (ep.getUnchangedOrNewParentNode() == ifStmt) {
            if (ep.getNewOrChangedIndex().childrenListNumber == BastFieldConstants.IF_CONDITION) {
              condition = true;
              tmpToRemove.add(ep);
            } else if (ep
                .getNewOrChangedIndex().childrenListNumber == BastFieldConstants.IF_IF_PART) {
              body = true;
              tmpToRemove.add(ep);

            } else if (ep
                .getNewOrChangedIndex().childrenListNumber == BastFieldConstants.IF_ELSE_PART) {
              elsePart = true;
              tmpToRemove.add(ep);

            }
          } else if (ifBlock != null && ep.getUnchangedOrOldParentNode() == ifBlock) {
            ifCount++;
            tmpToRemove.add(ep);
            if (ep.getType() == EditOperationType.MOVE
                || ep.getType() == EditOperationType.ALIGN) {
              InsertOperation insOp = new InsertOperation(ep.getUnchangedOrNewParentNode(),
                  ep.getNewOrChangedNode(), ep.getNewOrChangedIndex());
              toAdd.add(insOp);
            }
          } else if (ifBlock != null && ep.getUnchangedOrNewParentNode() == ifBlock) {
            ifCount++;
            tmpToRemove.add(ep);
            if (ep.getType() == EditOperationType.MOVE) {
              NodeParentInformationHierarchy npi =
                  hierarchyFirst.get(ep.getUnchangedOrOldParentNode());
              if (npi != null && npi.list.size() > 0) {
                if (npi.list.get(0).parent.getTag() != BastIf.TAG) {
                  if (ep.getOldOrChangedIndex().childrenListIndex == ep
                      .getNewOrChangedIndex().childrenListIndex) {
                    tmpToRemove.add(ep);
                    DeleteOperation delOp = new DeleteOperation(ep.getUnchangedOrOldParentNode(),
                        ep.getOldOrInsertedNode(), ep.getOldOrChangedIndex());
                    toAdd.add(delOp);
                  }
                }
              }
            }
          } else if (elseBlock != null && ep.getUnchangedOrOldParentNode() == elseBlock) {
            elseCount++;
            tmpToRemove.add(ep);
          } else if (ifBlock != null && ep.getUnchangedOrNewParentNode() == elseBlock) {
            elseCount++;
            tmpToRemove.add(ep);
          }
        }
      }
      if (ifStatements != null) {
        assert ifCount <= ifStatements.size();
        if (ifStatements.size() == ifCount) {
          body = true;
        }
      }
      if (elseStatements != null) {
        assert elseCount <= elseStatements.size();
        if (elseStatements.size() == elseCount) {
          elsePart = true;
        }
      }
      if (elsePart && ifStmt.elsePart != null && ifStmt.elsePart.getTag() == BastIf.TAG) {
        body = true;
        condition = true;
      }
      if (!condition) {
        BastEditOperation condOp = exDiffCurrent.editMapNew.get(ifStmt.condition);
        if (condOp != null && condOp.getType() == EditOperationType.MOVE) {
          if (!WildcardAccessHelper.isEqual(condOp.getOldOrInsertedNode(),
              condOp.getNewOrChangedNode())) {
            condition = true;
          }
        }
      }
      if (condition && body && elsePart) {
        toRemove.addAll(tmpToRemove);

        AbstractBastNode parentNode = null;
        NodeIndex index = null;
        if (hierarchySecond.get(ifStmt) != null) {
          parentNode = hierarchySecond.get(ifStmt).list.get(0).parent;

          index = new NodeIndex(hierarchySecond.get(ifStmt).list.get(0).fieldConstant,
              hierarchySecond.get(ifStmt).list.get(0).listId);

        } else {
          parentNode = hierarchyFirst.get(ifStmt).list.get(0).parent;
          index = new NodeIndex(hierarchyFirst.get(ifStmt).list.get(0).fieldConstant,
              hierarchyFirst.get(ifStmt).list.get(0).listId);

        }
        if (deleteIfStmts.contains(ifStmt)) {
          DeleteOperation ip = new DeleteOperation(parentNode, ifStmt, index);
          toAdd.add(ip);
        } else {
          InsertOperation ip = new InsertOperation(parentNode, ifStmt, index);
          toAdd.add(ip);
        }
      }
    }

    workList.removeAll(toRemove);
    workList.addAll(toAdd);
    return workList;
  }



  public static FilterRule getInstance() {
    return new TransformIfChanges();
  }
}