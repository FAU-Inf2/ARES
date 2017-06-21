package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.MoveOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatchAnnotationChanges extends AbstractFilterRule {

  public MatchAnnotationChanges() {
    super(FilterRuleType.MATCH_ANNOTATION_CHANGES);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return matchChanges(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);
  }

  private static List<BastEditOperation> matchChanges(List<BastEditOperation> editScript,
      ExtendedDiffResult extDiff,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    List<BastEditOperation> workList = new ArrayList<BastEditOperation>();
    List<InsertOperation> aresList = new ArrayList<>();
    for (int i = 0; i < editScript.size(); i++) {
      switch (editScript.get(i).getType()) {
        case MOVE:
          if (((MoveOperation) editScript.get(i)).getUnchangedOrNewParentNode()
              .getTag() == AresBlock.TAG
              || ((MoveOperation) editScript.get(i)).getUnchangedOrNewParentNode()
                  .getTag() == AresPatternClause.TAG) {
            workList.add(editScript.get(i));
            continue;
          } else {
            workList.add(editScript.get(i));
          }
          break;
        case INSERT:
          if (((InsertOperation) editScript.get(i)).getOldOrInsertedNode()
              .getTag() == AresBlock.TAG) {
            aresList.add((InsertOperation) editScript.get(i));
            continue;
          } else if (((InsertOperation) editScript.get(i)).getUnchangedOrOldParentNode()
              .getTag() == AresBlock.TAG) {
            BastFieldConstants childrenListNumber =
                ((InsertOperation) editScript.get(i)).getOldOrChangedIndex().childrenListNumber;
            if (childrenListNumber == BastFieldConstants.ARES_BLOCK_NUMBER) {
              continue;
            } else {
              workList.add(editScript.get(i));
            }
          } else {
            workList.add(editScript.get(i));
          }
          break;
        default:
          workList.add(editScript.get(i));
      }
    }
    editScript = workList;
    workList = new ArrayList<BastEditOperation>();

    for (BastEditOperation ep : editScript) {
      switch (ep.getType()) {
        case DELETE:
          workList.add(ep);
          break;
        default:
          workList.add(ep);
      }
    }
    return workList;
  }



  public static FilterRule getInstance() {
    return new MatchAnnotationChanges();
  }
}