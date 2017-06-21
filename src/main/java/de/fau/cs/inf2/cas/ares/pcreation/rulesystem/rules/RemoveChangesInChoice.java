package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.pcreation.CombinationHelper;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.ExecutionRunType;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformation;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RemoveChangesInChoice extends AbstractFilterRule {

  public RemoveChangesInChoice() {
    super(FilterRuleType.REMOVE_CHANGES_IN_CHOICE);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    worklist = nodeChangesInChoice(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);
    return handleChoiceDeletes(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond, runType);

  }

  private static List<BastEditOperation> nodeChangesInChoice(List<BastEditOperation> editList,
      ExtendedDiffResult extDiff,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    ArrayList<BastEditOperation> workList = new ArrayList<>();

    for (BastEditOperation ep : editList) {
      switch (ep.getType()) {
        case MOVE:
          NodeParentInformationHierarchy npi = hierarchySecond.get(ep.getNewOrChangedNode());
          boolean inChoice = false;
          for (NodeParentInformation np : npi.list) {
            if (np.parent.getTag() == AresChoiceStmt.TAG) {
              inChoice = true;
              break;
            }
          }
          npi = hierarchyFirst.get(ep.getOldOrInsertedNode());
          for (NodeParentInformation np : npi.list) {
            if (np.parent.getTag() == AresChoiceStmt.TAG) {
              inChoice = true;
              break;
            }
          }
          npi = hierarchySecond.get(ep.getUnchangedOrNewParentNode());
          for (NodeParentInformation np : npi.list) {
            if (np.parent.getTag() == AresChoiceStmt.TAG) {
              inChoice = true;
              break;
            }
          }
          boolean linkedOldPart = false;
          AbstractBastNode linkedOldParent =
              extDiff.firstToSecondMap.get(ep.getUnchangedOrOldParentNode());
          if (linkedOldParent != null) {
            npi = hierarchySecond.get(linkedOldParent);
            for (NodeParentInformation np : npi.list) {
              if (np.parent.getTag() == AresChoiceStmt.TAG) {
                inChoice = true;
                linkedOldPart = true;
                break;
              }
            }
          }
          if (!inChoice) {
            workList.add(ep);
          } else if (linkedOldPart) {
            InsertOperation op = new InsertOperation(ep.getUnchangedOrNewParentNode(),
                ep.getNewOrChangedNode(), ep.getNewOrChangedIndex());
            workList.add(op);

          }
          break;
        case INSERT:

          npi = hierarchySecond.get((ep.getUnchangedOrOldParentNode()));
          inChoice = false;
          if (npi != null) {
            for (NodeParentInformation np : npi.list) {
              if (np.parent.getTag() == AresChoiceStmt.TAG) {
                inChoice = true;
                break;
              }
            }
          }
          if (!inChoice) {
            workList.add(ep);
          }
          break;
        case DELETE:
          npi = hierarchySecond
              .get(extDiff.firstToSecondMap.get((ep.getUnchangedOrOldParentNode())));
          inChoice = false;
          if (npi != null) {
            for (int i = 0; i < npi.list.size(); i++) {
              NodeParentInformation np = npi.list.get(i);
              if (np.parent.getTag() == AresChoiceStmt.TAG) {
                if (WildcardAccessHelper.isPart(np.parent, ep.getOldOrInsertedNode())) {
                  inChoice = true;

                  break;
                } else {
                  NodeParentInformationHierarchy npiOld =
                      hierarchyFirst.get(ep.getUnchangedOrOldParentNode());
                  if (npiOld.list.size() > 1) {
                    AbstractBastNode alternative =
                        extDiff.firstToSecondMap.get(npiOld.list.get(0).parent);
                    if (ep.getType() == EditOperationType.DELETE && npi.list.size() > i + 1
                        && ep.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG) {

                      if (alternative != null
                          && alternative.getTag() != npi.list.get(i + 1).parent.getTag()) {
                        break;
                      }
                    }
                  }
                  inChoice = true;
                  break;
                }
              }
            }
          }
          if (!inChoice) {
            workList.add(ep);
          }
          break;
        default:
          workList.add(ep);
      }
    }

    return workList;
  }

  private static List<BastEditOperation> handleChoiceDeletes(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond,
      ExecutionRunType runType) {
    if (runType == ExecutionRunType.ORIGINAL_RUN) {
      return workList;
    }
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
          for (AbstractBastNode stmt : stmts) {
            if (stmt.getTag() == AresChoiceStmt.TAG) {
              if (WildcardAccessHelper.isPart(stmt, ep.getOldOrInsertedNode())) {
                toRemove.add(ep);
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
    return new RemoveChangesInChoice();
  }
}