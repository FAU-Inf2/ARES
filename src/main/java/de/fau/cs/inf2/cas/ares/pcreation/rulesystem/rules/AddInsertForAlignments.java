package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.pcreation.MatchBoundary;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class AddInsertForAlignments extends AbstractFilterRule {

  public AddInsertForAlignments() {
    super(FilterRuleType.ADD_INSERT_FOR_ALIGNMENTS);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return handleAlignment(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond,
        matchBoundary);
  }

  @SuppressWarnings("unchecked")
  private static List<BastEditOperation> handleAlignment(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond,
      MatchBoundary matchBoundary) {
    List<BastEditOperation> toAdd = new LinkedList<>();

    for (Entry<AbstractBastNode, AbstractBastNode> entry : exDiffAa.firstToSecondMap.entrySet()) {
      if (entry.getKey().getTag() == BastCall.TAG) {
        NodeParentInformationHierarchy npiSecond = hierarchySecond.get(entry.getValue());
        if (npiSecond != null && npiSecond.list.size() > 1) {
          if (npiSecond.list.get(1).parent.getTag() == AresBlock.TAG) {
            NodeParentInformationHierarchy npiFirst = hierarchyFirst.get(entry.getKey());
            if (npiFirst.list.get(1).parent.getTag() == BastFunction.TAG) {
              if (npiSecond.list.get(0).parent.getTag() == BastBlock.TAG
                  && npiFirst.list.get(0).parent.getTag() == BastBlock.TAG) {
                if (exDiffAa.editMapNew.get(entry.getValue()) == null) {
                  if (npiSecond.list.get(0).listId != npiFirst.list.get(0).listId) {
                    if (!WildcardAccessHelper.isEqual(entry.getKey(), entry.getValue())) {
                      LinkedList<AbstractBastNode> nodesSecond =
                          (LinkedList<AbstractBastNode>) npiSecond.list.get(0).parent
                              .getField(npiSecond.list.get(0).fieldConstant).getListField();
                      if (npiSecond.list.get(0).listId > 0) {
                        AbstractBastNode above =
                            nodesSecond.get(npiSecond.list.get(0).listId - 1);
                        if (exDiffAa.editMapNew.get(above) != null && exDiffAa.editMapNew
                            .get(above).getType() == EditOperationType.ALIGN) {
                          if (npiFirst.list.get(0).listId < exDiffAa.editMapNew.get(above)
                              .getOldOrChangedIndex().childrenListIndex
                              && npiSecond.list.get(0).listId > exDiffAa.editMapNew.get(above)
                                  .getOldOrChangedIndex().childrenListIndex) {
                            InsertOperation insertOp = new InsertOperation(
                                npiSecond.list.get(0).parent, entry.getValue(),
                                new NodeIndex(npiSecond.list.get(0).fieldConstant,
                                    npiSecond.list.get(0).listId));
                            toAdd.add(insertOp);
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    workList.addAll(toAdd);
    return workList;
  }



  public static FilterRule getInstance() {
    return new AddInsertForAlignments();
  }
}