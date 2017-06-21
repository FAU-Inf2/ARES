package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.pcreation.Filter;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.ExecutionRunType;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;

import java.util.ArrayList;
import java.util.List;

public class RemoveWildcardOrUseChanges extends AbstractFilterRule {

  public RemoveWildcardOrUseChanges() {
    super(FilterRuleType.REMOVE_WILDCARD_OR_USE_CHANGES);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return wildcardOrUseChanges(worklist, runType);
  }

  private static List<BastEditOperation> wildcardOrUseChanges(List<BastEditOperation> editScript,
      ExecutionRunType runType) {
    List<BastEditOperation> workList = new ArrayList<>();
    for (BastEditOperation ep : editScript) {
      switch (ep.getType()) {
        case INSERT:
          InsertOperation ip = (InsertOperation) ep;
          if (runType == ExecutionRunType.ORIGINAL_RUN
              && ip.getOldOrInsertedNode().getTag() == AresWildcard.TAG
              || runType == ExecutionRunType.MODIFIED_RUN
                  && ip.getOldOrInsertedNode().getTag() == AresUseStmt.TAG) {
            continue;
          }
          workList.add(ep);
          break;
        default:
          workList.add(ep);
      }
    }
    return workList;
  }



  public static FilterRule getInstance() {
    return new RemoveWildcardOrUseChanges();
  }
}