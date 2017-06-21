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

import de.fau.cs.inf2.cas.ares.bast.visitors.NodeStreamVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondOr;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransformOrCondition extends AbstractFilterRule {

  public TransformOrCondition() {
    super(FilterRuleType.TRANSFORM_OR_CONDITION);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return handleOrCondition(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);
  }

  private static List<BastEditOperation> handleOrCondition(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    List<BastEditOperation> toRemove = new LinkedList<>();
    List<BastEditOperation> toAdd = new LinkedList<>();
    for (BastEditOperation ep : workList) {
      if (ep.getType() == EditOperationType.INSERT) {
        if (ep.getOldOrChangedIndex().childrenListNumber == BastFieldConstants.IF_CONDITION) {
          if (ep.getOldOrInsertedNode().getTag() == BastCondOr.TAG) {
            AbstractBastNode partner =
                exDiffAa.secondToFirstMap.get(ep.getUnchangedOrOldParentNode());
            if (partner != null) {
              NodeStreamVisitor firstTokens = new NodeStreamVisitor(ep.getOldOrInsertedNode());
              ep.getOldOrInsertedNode().accept(firstTokens);
              AbstractBastNode partnerCondition = ep.getUnchangedOrOldParentNode()
                  .getField(BastFieldConstants.IF_CONDITION).getField();
              NodeStreamVisitor secondTokens = new NodeStreamVisitor(partnerCondition);
              partnerCondition.accept(secondTokens);
              if (firstTokens.nodes.size() == secondTokens.nodes.size()) {
                toRemove.add(ep);
                for (AbstractBastNode node : firstTokens.nodes) {
                  BastEditOperation epTmp = exDiffAa.editMapNew.get(node);
                  if (epTmp != null) {
                    toRemove.add(epTmp);
                  }
                }
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
    return new TransformOrCondition();
  }
}