package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.pcreation.MatchBoundary;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.AdvancedEditOperation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ChangeRootMovements extends AbstractFilterRule {

  public ChangeRootMovements() {
    super(FilterRuleType.CHANGE_ROOT_MOVEMENTS);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return handleMovesInsideChangeRoots(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond,
        matchBoundary);
  }

  private static List<BastEditOperation> handleMovesInsideChangeRoots(
      List<BastEditOperation> workList, ExtendedDiffResult exDiffCurrent,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond,
      MatchBoundary matchBoundary) {
    List<BastEditOperation> toRemove = new LinkedList<>();
    final List<BastEditOperation> toAdd = new LinkedList<>();

    AbstractBastNode boundary1 = matchBoundary.getNode1();
    BastFieldConstants field = matchBoundary.field1;
    AbstractBastNode boundary2 = matchBoundary.getNode2();
    if (boundary1.getTag() == BastFunction.TAG) {
      boundary1 = WildcardAccessHelper.getNodeToIndex(boundary1,
          BastFieldConstants.FUNCTION_BLOCK_STATEMENTS, 0);
      field = BastFieldConstants.BLOCK_STATEMENT;
    }
    LinkedList<? extends AbstractBastNode> stmts1 = boundary1.getField(field).getListField();
    LinkedList<? extends AbstractBastNode> stmts2 =
        boundary2.getField(matchBoundary.field2).getListField();
    LinkedList<BastEditOperation> editOperations = new LinkedList<>();
    for (AbstractBastNode stmt : stmts1) {
      BastEditOperation tmp = exDiffCurrent.editMapOld.get(stmt);
      if (tmp != null) {
        if (tmp instanceof AdvancedEditOperation) {
          tmp = ((AdvancedEditOperation) tmp).getBasicOperation();
        }
        if (tmp.getType() != EditOperationType.MOVE && tmp.getType() != EditOperationType.ALIGN) {
          editOperations.add(tmp);
        } else {
          if (tmp.getUnchangedOrNewParentNode() != boundary2) {
            editOperations.add(tmp);
          }
        }
      }
    }
    for (AbstractBastNode stmt : stmts2) {
      BastEditOperation tmp = exDiffCurrent.editMapNew.get(stmt);
      if (tmp != null) {
        if (tmp instanceof AdvancedEditOperation) {
          tmp = ((AdvancedEditOperation) tmp).getBasicOperation();
        }
        editOperations.add(tmp);
      }
    }
    for (BastEditOperation ep : workList) {
      if (ep.getType() != EditOperationType.MOVE && ep.getType() != EditOperationType.ALIGN) {
        continue;
      }
      if (ep.getUnchangedOrOldParentNode() != boundary1) {
        continue;
      }
      if (ep.getUnchangedOrNewParentNode() != boundary2) {
        continue;
      }
      int offset = 0;

      for (int i = 0; i < stmts2.size(); i++) {
        BastEditOperation tmp = exDiffCurrent.editMapNew.get(stmts2.get(i));
        if (tmp == null) {
          offset++;
        }
      }


      for (BastEditOperation epInner : editOperations) {
        if (epInner instanceof AdvancedEditOperation) {
          epInner = ((AdvancedEditOperation) epInner).getBasicOperation();
        }
        if (epInner == ep) {

          continue;
        }
        if (epInner.getType() == EditOperationType.INSERT) {
          if (epInner.getUnchangedOrNewParentNode() != boundary2) {
            continue;
          }
          if (epInner.getNewOrChangedIndex().childrenListIndex <= ep
              .getNewOrChangedIndex().childrenListIndex) {
            offset++;
          }
        } else if (epInner.getType() == EditOperationType.DELETE) {
          if (epInner.getUnchangedOrOldParentNode() != boundary1) {
            continue;
          }
          if (epInner.getOldOrChangedIndex().childrenListIndex <= ep
              .getOldOrChangedIndex().childrenListIndex) {
            offset--;
          }
        } else if (epInner.getType() == EditOperationType.MOVE
            || epInner.getType() == EditOperationType.ALIGN) {
          if (epInner.getUnchangedOrOldParentNode() == boundary1) {
            if (epInner.getOldOrChangedIndex().childrenListIndex <= ep
                .getOldOrChangedIndex().childrenListIndex) {
              offset--;
            }
          }
          if (epInner.getUnchangedOrNewParentNode() == boundary2) {
            if (epInner.getNewOrChangedIndex().childrenListIndex <= ep
                .getNewOrChangedIndex().childrenListIndex) {
              offset++;
            }
          }
        }
      }
      if (ep.getOldOrChangedIndex().childrenListIndex
          + offset == ep.getNewOrChangedIndex().childrenListIndex) {

        int index = ep.getNewOrChangedIndex().childrenListIndex;
        if (index >= 1) {
          int pos = 1;
          AbstractBastNode partnerTest = null;
          while (index + pos < stmts2.size() && partnerTest == null) {
            partnerTest = exDiffCurrent.secondToFirstMap.get(stmts2.get(index + pos));
            pos++;
          }
          if (partnerTest != null) {
            if (stmts1.indexOf(partnerTest) >= 0) {
              if (stmts1.indexOf(partnerTest) < ep.getOldOrChangedIndex().childrenListIndex) {
                continue;
              }
            }
          }
        }
        toRemove.add(ep);
      }

    }
    workList.removeAll(toRemove);
    workList.addAll(toAdd);

    return workList;
  }



  public static FilterRule getInstance() {
    return new ChangeRootMovements();
  }
}
