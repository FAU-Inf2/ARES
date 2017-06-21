package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresCaseStmt;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.ExecutionRunType;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.ArrayList;
import java.util.List;

public class CaseAnnotationChanges extends AbstractFilterRule {

  public CaseAnnotationChanges() {
    super(FilterRuleType.CASE_ANNOTATION_CHANGES);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return caseChanges(worklist, runType);
  }

  private static List<BastEditOperation> caseChanges(List<BastEditOperation> workList,
      ExecutionRunType runType) {
    if (runType == ExecutionRunType.ORIGINAL_RUN) {
      return workList;
    }
    List<BastEditOperation> toRemove = new ArrayList<>();
    for (BastEditOperation ep : workList) {
      if (ep.getOldOrInsertedNode().getTag() == AresCaseStmt.TAG) {
        toRemove.add(ep);
        continue;
      }
      if (ep.getType() != EditOperationType.UPDATE

          && ep.getUnchangedOrNewParentNode() != null
          && ep.getUnchangedOrNewParentNode().getTag() == AresCaseStmt.TAG) {
        toRemove.add(ep);

        continue;

      }
    }
    workList.removeAll(toRemove);
    return workList;
  }



  public static FilterRule getInstance() {
    return new CaseAnnotationChanges();
  }
}
