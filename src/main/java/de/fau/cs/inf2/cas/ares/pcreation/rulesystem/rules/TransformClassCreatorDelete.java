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

import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNew;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransformClassCreatorDelete extends AbstractFilterRule {

  public TransformClassCreatorDelete() {
    super(FilterRuleType.TRANSFORM_CLASS_CREATOR_DELETE);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return classCreator(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);
  }

  private static List<BastEditOperation> classCreator(
      List<BastEditOperation> workList, ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    List<BastEditOperation> toRemove = new LinkedList<>();
    List<BastEditOperation> toAdd = new LinkedList<>();
    for (BastEditOperation ep : workList) {
      if (ep.getType() == EditOperationType.DELETE) {
        if (ep.getOldOrInsertedNode().getTag() == BastNew.TAG) {
          if (((BastNew) ep.getOldOrInsertedNode()).declarations != null
              && ((BastNew) ep.getOldOrInsertedNode()).declarations.size() == 1
              && ((BastNew) ep.getOldOrInsertedNode()).declarations.getFirst()
                  .getTag() == BastFunction.TAG) {
            AbstractBastNode partnerParent =
                exDiffAa.firstToSecondMap.get(ep.getUnchangedOrOldParentNode());
            if (partnerParent != null) {
              AbstractBastNode partner =
                  WildcardAccessHelper.getNodeToIndex(partnerParent, ep.getOldOrChangedIndex());
              if (exDiffAa.editMapNew.get(partner) != null) {
                toRemove.add(ep);
                toRemove.add(exDiffAa.editMapNew.get(partner));
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
    return new TransformClassCreatorDelete();
  }
}