/*
 * Copyright (c) 2017 Programming Systems Group, CS Department, FAU
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */

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