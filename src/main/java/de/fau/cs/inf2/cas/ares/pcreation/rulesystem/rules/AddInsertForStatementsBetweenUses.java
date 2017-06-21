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
import de.fau.cs.inf2.cas.ares.pcreation.Filter;
import de.fau.cs.inf2.cas.ares.pcreation.MatchBoundary;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.ExecutionRunType;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AddInsertForStatementsBetweenUses extends AbstractFilterRule {

  public AddInsertForStatementsBetweenUses() {
    super(FilterRuleType.ADD_INSERT_FOR_STATEMENTS_BETWEEN_USES);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return statementsBetweenUses(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond,
        matchBoundary, runType, exDiffBA2);
  }

  @SuppressWarnings("unchecked")
  private static List<BastEditOperation> statementsBetweenUses(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond,
      MatchBoundary matchBoundary, ExecutionRunType type, ExtendedDiffResult exDiffBA2) {
    if (type == ExecutionRunType.ORIGINAL_RUN) {
      return workList;
    }
    List<BastEditOperation> toAdd = new LinkedList<>();
    for (BastEditOperation ep : exDiffBA2.editScript) {
      if (ep.getType() == EditOperationType.INSERT) {
        if (ep.getUnchangedOrNewParentNode().getTag() == BastBlock.TAG) {
          BastEditOperation bastEditOperation = exDiffAa.editMapNew.get(ep.getNewOrChangedNode());
          if (bastEditOperation == null || (bastEditOperation.getType() == EditOperationType.MOVE
              && WildcardAccessHelper.isEqual(bastEditOperation.getOldOrInsertedNode(),
                  bastEditOperation.getNewOrChangedNode()))) {

            NodeParentInformationHierarchy npi = hierarchySecond.get(ep.getNewOrChangedNode());
            if (npi.list.size() > 0) {
              LinkedList<AbstractBastNode> list =
                  (LinkedList<AbstractBastNode>) npi.list.get(0).parent
                      .getField(npi.list.get(0).fieldConstant).getListField();
              if (ep.getNewOrChangedIndex().childrenListIndex > 0
                  && ep.getNewOrChangedIndex().childrenListIndex < list.size() - 1) {
                AbstractBastNode above = list.get(ep.getNewOrChangedIndex().childrenListIndex - 1);
                boolean aboveCovered = false;
                boolean belowCovered = false;
                if (above.getTag() == AresUseStmt.TAG) {
                  aboveCovered = true;
                } else {
                  for (BastEditOperation epList : workList) {
                    if (epList.getNewOrChangedNode() == above) {
                      aboveCovered = true;
                    }
                  }
                }
                if (aboveCovered) {
                  AbstractBastNode below =
                      list.get(ep.getNewOrChangedIndex().childrenListIndex + 1);
                  if (below.getTag() == AresUseStmt.TAG) {
                    belowCovered = true;
                  } else {
                    for (BastEditOperation epList : workList) {
                      if (epList.getNewOrChangedNode() == below) {
                        belowCovered = true;
                        break;
                      }
                    }
                  }
                }
                if (aboveCovered && belowCovered) {
                  InsertOperation insertOp =
                      new InsertOperation(npi.list.get(0).parent, ep.getNewOrChangedNode(),
                          new NodeIndex(npi.list.get(0).fieldConstant, npi.list.get(0).listId));
                  toAdd.add(insertOp);
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
    return new AddInsertForStatementsBetweenUses();
  }
}
