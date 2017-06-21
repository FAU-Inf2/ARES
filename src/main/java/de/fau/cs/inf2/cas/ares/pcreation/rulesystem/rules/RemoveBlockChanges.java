package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.pcreation.CombinationHelper;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RemoveBlockChanges extends AbstractFilterRule {

  public RemoveBlockChanges() {
    super(FilterRuleType.REMOVE_BLOCK_CHANGES);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    worklist = standardBlockChanges(worklist, exDiffCurrent);
    return handleBlockDeletes(worklist, exDiffCurrent, hierarchyFirst,
        hierarchySecond);
  }

  private static List<BastEditOperation> standardBlockChanges(List<BastEditOperation> editScript,
      ExtendedDiffResult extDiff) {
    List<BastEditOperation> workList = new ArrayList<BastEditOperation>();
    for (BastEditOperation ep : editScript) {
      switch (ep.getType()) {
        case METHOD_INSERT:
        case INCREASING_ACCESS:
        case DECREASING_ACCESS:
        case PARENT_CLASS_INSERT:
        case CLASS_RENAME:
        case FIELD_DELETE:
        case FIELD_INSERT:
        case FIELD_RENAME:
        case FIELD_TYPE_UPDATE:
        case METHOD_DELETE:
        case METHOD_RENAME:
        case PARAMETER_DELETE:
        case PARAMETER_INSERT:
        case PARAMETER_RENAME:
        case PARAMETER_REORDERING:
        case PARAMETER_TYPE_UPDATE:
        case PARENT_CLASS_DELETE:
        case RETURN_TYPE_UPDATE:
          break;
        case MOVE:
        case ALIGN:
        case UPDATE:
          workList.add(ep);
          break;
        case INSERT:
          if ((ep).getOldOrInsertedNode().getTag() == BastBlock.TAG) {
            break;
          }
          workList.add(ep);
          break;
        case DELETE:
          if ((ep).getOldOrInsertedNode().getTag() == BastBlock.TAG) {
            if (ep.getOldOrChangedIndex().childrenListNumber != BastFieldConstants.IF_IF_PART) {
              break;
            }
            if (extDiff.firstToSecondMap.get(ep.getUnchangedOrOldParentNode()) == null) {
              break;
            }
            BastField field = extDiff.firstToSecondMap.get(ep.getUnchangedOrOldParentNode())
                .getField(BastFieldConstants.IF_IF_PART);
            if (field == null) {
              break;
            }
            if (field.getField().getTag() == BastBlock.TAG) {
              break;
            }
          }
          workList.add(ep);
          break;

        default:
          assert (false);
      }
    }
    List<BastEditOperation> workListTmp = new ArrayList<BastEditOperation>();
    for (BastEditOperation ep : workList) {
      switch (ep.getType()) {

        case INSERT:
          if (ep.getUnchangedOrOldParentNode().getTag() == AresBlock.TAG) {
            break;
          }
          workListTmp.add(ep);
          break;
        case DELETE:
          if (ep.getUnchangedOrOldParentNode().getTag() == AresBlock.TAG) {
            break;
          }
          workListTmp.add(ep);
          break;
        case MOVE:
          if (ep.getUnchangedOrOldParentNode().getTag() == AresBlock.TAG
              || ep.getUnchangedOrNewParentNode().getTag() == AresBlock.TAG) {
            break;
          }
          if (ep.getOldOrInsertedNode().getTag() == BastBlock.TAG) {
            AbstractBastNode partner =
                extDiff.firstToSecondMap.get(ep.getUnchangedOrOldParentNode());
            if (partner != null) {
              if (partner.getField(ep.getOldOrChangedIndex().childrenListNumber) != null
                  && !partner.getField(ep.getOldOrChangedIndex().childrenListNumber).isList()
                  && partner.getField(ep.getOldOrChangedIndex().childrenListNumber)
                      .getField() != null
                  && partner.getField(ep.getOldOrChangedIndex().childrenListNumber).getField()
                      .getTag() != BastBlock.TAG) {
                workListTmp.add(ep);
                break;
              }
            }
            break;
          }
          workListTmp.add(ep);
          break;
        default:
          workListTmp.add(ep);
      }
    }
    return workListTmp;
  }

  private static List<BastEditOperation> handleBlockDeletes(
      List<BastEditOperation> workList, ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    List<BastEditOperation> toRemove = new LinkedList<>();
    List<BastEditOperation> toAdd = new LinkedList<>();
    for (BastEditOperation ep : workList) {

      if (ep.getType() == EditOperationType.DELETE
          && ep.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG) {
        AbstractBastNode block = (BastBlock) (ep).getUnchangedOrOldParentNode();
        block = (BastBlock) exDiffAa.firstToSecondMap
            .get(((BastBlock) (ep).getUnchangedOrOldParentNode()));
        block = CombinationHelper.optimizeBlockMapping((ep).getUnchangedOrOldParentNode(), block,
            exDiffAa.firstToSecondMap, exDiffAa, hierarchyFirst, hierarchySecond);
        if (block != null && block.getTag() == BastBlock.TAG) {
          LinkedList<? extends AbstractBastNode> stmts =
              block.getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
          if (stmts.size() > ep.getOldOrChangedIndex().childrenListIndex) {
            AbstractBastNode stmt = stmts.get(ep.getOldOrChangedIndex().childrenListIndex);
            if (WildcardAccessHelper.isEqual(stmt, ep.getOldOrInsertedNode())) {
              if (exDiffAa.secondToFirstMap.get(stmt) != null
                  && exDiffAa.editMapNew.get(stmt) == null) {
                toRemove.add(ep);
              }
            }
          } else if (stmts.size() > 0 && stmts.getFirst().getTag() == AresBlock.TAG) {
            toRemove.add(ep);
          }
        }
      }
    }
    workList.removeAll(toRemove);
    workList.addAll(toAdd);
    return workList;
  }
  

  public static FilterRule getInstance() {
    return new RemoveBlockChanges();
  }
}