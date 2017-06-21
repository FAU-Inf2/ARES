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
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeSpecifier;
import de.fau.cs.inf2.cas.common.bast.type.BastBasicType;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.DeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class TransformDeclarationChildrenChanges extends AbstractFilterRule {

  public TransformDeclarationChildrenChanges() {
    super(FilterRuleType.TRANSFORM_DECLARATION_CHILDREN_CHANGES);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return declarationChildren(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);
  }

  private static List<BastEditOperation> declarationChildren(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    List<BastEditOperation> toRemove = new LinkedList<>();
    List<BastEditOperation> toAdd = new LinkedList<>();
    List<AbstractBastNode> done = new LinkedList<>();
    for (BastEditOperation ep : workList) {
      if (ep.getType() == EditOperationType.ALIGN || ep.getType() == EditOperationType.MOVE
          || ep.getType() == EditOperationType.DELETE) {
        NodeParentInformationHierarchy npi = null;
        if (ep.getUnchangedOrOldParentNode().getTag() == BastExprInitializer.TAG) {
          npi = hierarchyFirst.get(ep.getOldOrInsertedNode());
        }
        if (ep.getUnchangedOrOldParentNode().getTag() == BastIdentDeclarator.TAG
            && ep.getOldOrInsertedNode().getTag() == BastExprInitializer.TAG) {
          if (((BastExprInitializer) ep.getOldOrInsertedNode()).init != null) {
            npi = hierarchyFirst.get(((BastExprInitializer) ep.getOldOrInsertedNode()).init);
          }
        }
        if (ep.getUnchangedOrOldParentNode().getTag() == BastDeclaration.TAG) {
          if (ep.getOldOrInsertedNode().getTag() == BastIdentDeclarator.TAG) {
            if (((BastIdentDeclarator) ep.getOldOrInsertedNode()) != null
                && ((BastIdentDeclarator) ep.getOldOrInsertedNode()).expression
                    .getTag() == BastExprInitializer.TAG) {
              if (((BastExprInitializer) ((BastIdentDeclarator) ep
                  .getOldOrInsertedNode()).expression).init != null) {
                npi = hierarchyFirst.get(((BastExprInitializer) ((BastIdentDeclarator) ep
                    .getOldOrInsertedNode()).expression).init);
              }
            }
          }
        }
        if (npi != null && npi.list.size() > 3) {
          if (npi.list.get(1).parent.getTag() == BastIdentDeclarator.TAG
              && npi.list.get(2).parent.getTag() == BastDeclaration.TAG) {
            BastIdentDeclarator identDecl = (BastIdentDeclarator) npi.list.get(1).parent;
            if (exDiffAa.editMapOld.get(identDecl.identifier) != null) {
              BastDeclaration decl = (BastDeclaration) npi.list.get(2).parent;
              BastDeclaration partner = (BastDeclaration) exDiffAa.firstToSecondMap.get(decl);
              if (done.contains(decl)) {
                continue;
              }
              if (partner != null) {
                if (partner.declaratorList.size() == decl.declaratorList.size()) {
                  if (partner.declaratorList.getFirst().getTag() == BastIdentDeclarator.TAG) {
                    BastIdentDeclarator partnerIdentDecl =
                        (BastIdentDeclarator) partner.declaratorList.getFirst();
                    if (!WildcardAccessHelper.isEqual(identDecl.identifier,
                        partnerIdentDecl.identifier)) {
                      if (decl.specifierList.size() == 1
                          && ((BastTypeSpecifier) decl.specifierList.get(0)).type
                              .getTag() == BastBasicType.TAG) {
                        toRemove.add(ep);
                        DeleteOperation delOp = new DeleteOperation(npi.list.get(3).parent,
                            npi.list.get(2).parent,
                            new NodeIndex(npi.list.get(3).fieldConstant, npi.list.get(3).listId));
                        toAdd.add(delOp);

                      }
                    }
                  }
                }

              }
            }
          }
        }
      }
      if (ep.getType() == EditOperationType.ALIGN || ep.getType() == EditOperationType.MOVE
          || ep.getType() == EditOperationType.INSERT) {
        NodeParentInformationHierarchy npi = null;
        if (ep.getUnchangedOrNewParentNode().getTag() == BastExprInitializer.TAG) {
          npi = hierarchySecond.get(ep.getNewOrChangedNode());
        }
        if (ep.getUnchangedOrNewParentNode().getTag() == BastIdentDeclarator.TAG
            && ep.getNewOrChangedNode().getTag() == BastExprInitializer.TAG) {
          if (((BastExprInitializer) ep.getNewOrChangedNode()).init != null) {
            npi = hierarchySecond.get(((BastExprInitializer) ep.getNewOrChangedNode()).init);
          }
        }
        if (ep.getUnchangedOrNewParentNode().getTag() == BastDeclaration.TAG) {
          if (ep.getNewOrChangedNode().getTag() == BastIdentDeclarator.TAG) {
            if (((BastIdentDeclarator) ep.getNewOrChangedNode()) != null
                && ((BastIdentDeclarator) ep.getNewOrChangedNode()).expression != null
                && ((BastIdentDeclarator) ep.getNewOrChangedNode()).expression
                    .getTag() == BastExprInitializer.TAG) {
              if (((BastExprInitializer) ((BastIdentDeclarator) ep
                  .getNewOrChangedNode()).expression).init != null) {

                npi = hierarchySecond.get(((BastExprInitializer) ((BastIdentDeclarator) ep
                    .getNewOrChangedNode()).expression).init);
              }
            }
          }
        }
        if (npi != null && npi.list.size() > 3) {
          if (npi.list.get(1).parent.getTag() == BastIdentDeclarator.TAG
              && npi.list.get(2).parent.getTag() == BastDeclaration.TAG) {
            BastIdentDeclarator identDecl = (BastIdentDeclarator) npi.list.get(1).parent;
            if (exDiffAa.editMapNew.get(identDecl.identifier) != null) {
              BastDeclaration decl = (BastDeclaration) npi.list.get(2).parent;
              BastDeclaration partner = (BastDeclaration) exDiffAa.secondToFirstMap.get(decl);
              if (done.contains(decl)) {
                continue;
              }
              if (partner != null) {
                if (partner.declaratorList.size() == decl.declaratorList.size()) {
                  if (partner.declaratorList.getFirst().getTag() == BastIdentDeclarator.TAG) {
                    BastIdentDeclarator partnerIdentDecl =
                        (BastIdentDeclarator) partner.declaratorList.getFirst();
                    if (!WildcardAccessHelper.isEqual(identDecl.identifier,
                        partnerIdentDecl.identifier)) {
                      if (decl.specifierList.size() == 1
                          && ((BastTypeSpecifier) decl.specifierList.get(0)).type
                              .getTag() == BastBasicType.TAG) {
                        toRemove.add(ep);
                        InsertOperation insOp = new InsertOperation(npi.list.get(3).parent,
                            npi.list.get(2).parent,
                            new NodeIndex(npi.list.get(3).fieldConstant, npi.list.get(3).listId));
                        toAdd.add(insOp);

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
    workList.removeAll(toRemove);
    workList.addAll(toAdd);
    return workList;
  }



  public static FilterRule getInstance() {
    return new TransformDeclarationChildrenChanges();
  }
}