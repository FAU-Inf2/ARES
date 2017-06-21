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

import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.pcreation.PatternGenerator;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.ExecutionRunType;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformation;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.DeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ChoiceAnnotationChanges extends AbstractFilterRule {

  public ChoiceAnnotationChanges() {
    super(FilterRuleType.CHOICE_ANNOTATION_CHANGES);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return choiceChanges(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond, parentFirst,
        parentSecond, exDiffBA1, exDiffBA2, runType);
  }

  private static List<BastEditOperation> choiceChanges(List<BastEditOperation> editScript,
      ExtendedDiffResult exDiffCurrent,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond,
      NodeParentInformationHierarchy parentFirst, NodeParentInformationHierarchy parentSecond,
      ExtendedDiffResult exDiffBA1, ExtendedDiffResult exDiffBA2, ExecutionRunType runType) {
    if (runType == ExecutionRunType.ORIGINAL_RUN) {
      return editScript;
    }
    List<BastEditOperation> workList = new ArrayList<>();
    for (BastEditOperation ep : editScript) {
      switch (ep.getType()) {
        case INCREASING_ACCESS:
          workList.add(ep);
          continue;
        default:
          if (ep.getOldOrInsertedNode().getTag() == AresChoiceStmt.TAG) {
            continue;
          }
      }
      NodeParentInformationHierarchy npi = PatternGenerator.getParentHierarchy(ep, hierarchyFirst,
          hierarchySecond, exDiffBA1.firstToSecondMap);
      if (npi == null) {
        npi = PatternGenerator.getNewParentHierarchy(ep, hierarchySecond);
        if (npi == null) {
          workList.add(ep);
        } else {
          boolean inChoice = false;
          for (NodeParentInformation np : npi.list) {
            assert (np != null);
            assert (parentSecond != null);
            if (np.parent.getTag() == AresChoiceStmt.TAG) {
              inChoice = true;
              break;
            }
          }
          if (!inChoice) {
            workList.add(ep);
          }
        }
        continue;
      } else {
        boolean inChoice = false;
        boolean insertChoice = false;
        for (NodeParentInformation np : npi.list) {
          assert (np != null);
          assert (parentSecond != null);
          if (np.parent.getTag() == AresChoiceStmt.TAG) {
            inChoice = true;
            break;
          }
        }
        if (ep.getType() == EditOperationType.MOVE && !inChoice) {
          npi = PatternGenerator.getNewParentHierarchy(ep, hierarchySecond);
          if (npi == null) {
            workList.add(ep);
          } else {
            inChoice = false;
            for (NodeParentInformation np : npi.list) {
              assert (np != null);
              assert (parentSecond != null);
              if (np.parent.getTag() == AresChoiceStmt.TAG) {
                inChoice = true;
                break;
              }
            }
            if (!inChoice) {
              workList.add(ep);
              continue;
            }
          }
          if (inChoice) {
            insertChoice = true;
            inChoice = false;
          }
        }
        if ((ep.getType() == EditOperationType.MOVE || ep.getType() == EditOperationType.DELETE)
            && !inChoice) {
          npi = hierarchyFirst.get(ep.getOldOrInsertedNode());
          if (npi != null) {
            outer: for (int i = 0; i < npi.list.size(); i++) {
              NodeParentInformation np = npi.list.get(i);
              if (!inChoice && exDiffCurrent.firstToSecondMap.get(np.parent) != null) {
                AbstractBastNode partner = exDiffCurrent.firstToSecondMap.get(np.parent);
                NodeParentInformationHierarchy npiPartner = hierarchySecond.get(partner);
                if (npiPartner != null) {
                  for (int j = 0; j < npiPartner.list.size(); j++) {
                    NodeParentInformation npPartner = npiPartner.list.get(j);
                    if (npPartner.parent.getTag() == AresChoiceStmt.TAG) {
                      if (WildcardAccessHelper.isPart(npPartner.parent,
                          ep.getOldOrInsertedNode())) {
                        inChoice = true;

                        break;
                      } else {
                        if (ep.getType() == EditOperationType.DELETE && npi.list.size() > i + 1
                            && npiPartner.list.size() > j + 1
                            && ep.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG) {
                          AbstractBastNode possiblePartner =
                              exDiffCurrent.firstToSecondMap.get(npi.list.get(i + 1).parent);
                          if (possiblePartner != null && possiblePartner
                              .getTag() != npiPartner.list.get(j + 1).parent.getTag()) {
                            break outer;
                          }
                        }
                        inChoice = true;
                        break;
                      }
                    }
                  }
                }
              }
            }
          }
        }
        if (!inChoice) {
          if (insertChoice) {
            DeleteOperation delOp = new DeleteOperation(ep.getUnchangedOrOldParentNode(),
                ep.getOldOrInsertedNode(), ep.getOldOrChangedIndex());
            workList.add(delOp);
          } else {
            workList.add(ep);
          }
        }
      }
    }
    return workList;
  }



  public static FilterRule getInstance() {
    return new ChoiceAnnotationChanges();
  }
}