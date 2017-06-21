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
import de.fau.cs.inf2.cas.ares.pcreation.Filter;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformation;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAccess;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastForStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTryStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeSpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastWhileStatement;
import de.fau.cs.inf2.cas.common.bast.type.BastBasicType;
import de.fau.cs.inf2.cas.common.bast.visitors.CollectNodesVisitor;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CombineSubchangesOfStatements extends AbstractFilterRule {

  public CombineSubchangesOfStatements() {
    super(FilterRuleType.COMBINE_SUBCHANGES_OF_STATEMENTS);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return handleStatementPairs(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond,
        delInsertMap);
  }

  private static List<BastEditOperation> handleStatementPairs(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond,
      HashMap<AbstractBastNode, AbstractBastNode> delInsertMap) {
    HashSet<BastEditOperation> editSet = new HashSet<>(workList);
    LinkedList<BastEditOperation> toAdd = new LinkedList<>();
    LinkedList<BastEditOperation> toRemove = new LinkedList<>();
    for (Entry<AbstractBastNode, AbstractBastNode> entry : exDiffAa.firstToSecondMap.entrySet()) {
      if (entry.getKey().getTag() == BastBlock.TAG || entry.getKey().getTag() == BastFunction.TAG
          || entry.getKey().getTag() == BastProgram.TAG || entry.getKey().getTag() == BastIf.TAG
          || entry.getKey().getTag() == BastCondExpr.TAG
          || entry.getKey().getTag() == BastTryStmt.TAG
          || entry.getKey().getTag() == BastForStmt.TAG
          || entry.getKey().getTag() == BastWhileStatement.TAG) {
        continue;
      }
      boolean insideChoice = false;
      NodeParentInformationHierarchy npiChoiceTest = hierarchySecond.get(entry.getValue());
      for (NodeParentInformation np : npiChoiceTest.list) {
        if (np.parent.getTag() == AresChoiceStmt.TAG) {
          insideChoice = true;
          break;
        }
      }
      if (insideChoice) {
        continue;
      }
      if ((exDiffAa.editMapOld.get(entry.getKey()) == null
          && exDiffAa.editMapNew.get(entry.getValue()) == null)
          || (exDiffAa.editMapNew.get(entry.getValue()).getType() == EditOperationType.MOVE
              && exDiffAa.editMapNew.get(entry.getValue()).getOldOrInsertedNode() == entry
                  .getKey())) {
        if (!WildcardAccessHelper.isEqual(entry.getKey(), entry.getValue(), true)) {
          List<BastFieldConstants> constants =
              BastFieldConstants.getFieldMap(entry.getKey().getTag());
          boolean examine = true;
          if (constants == null) {
            continue;
          }
          for (BastFieldConstants constant : constants) {
            BastField fieldKey = entry.getKey().getField(constant);
            BastField fieldValue = entry.getValue().getField(constant);
            if (fieldKey == null || fieldValue == null) {
              continue;
            }
            if (constant.isList) {
              List<? extends AbstractBastNode> listKey = fieldKey.getListField();
              List<? extends AbstractBastNode> listValue = fieldValue.getListField();
              if (listKey == null || listValue == null || listKey.size() == 0
                  || listValue.size() == 0) {
                continue;
              }
              int upperBound = Math.min(listKey.size(), listValue.size());
              for (int i = 0; i < upperBound; i++) {
                AbstractBastNode nodeKey = listKey.get(i);
                AbstractBastNode nodeValue = listValue.get(i);
                if (nodeKey == null || nodeValue == null) {
                  continue;
                }
                if (WildcardAccessHelper.isEqual(nodeKey, nodeValue)) {
                  if (nodeKey.getTag() == BastTypeSpecifier.TAG) {
                    if (((BastTypeSpecifier) nodeKey).type.getTag() == BastBasicType.TAG) {
                      if (entry.getKey().getTag() == BastDeclaration.TAG) {
                        if (exDiffAa.firstToSecondMap
                            .get(((BastDeclaration) entry.getKey()).declaratorList
                                .getFirst()) == ((BastDeclaration) entry
                                    .getValue()).declaratorList.getFirst()) {
                          examine = false;
                          break;
                        } else {
                          continue;
                        }
                      }
                    }
                  }
                  examine = false;
                  break;
                } else if (delInsertMap.get(nodeKey) == nodeValue) {
                  examine = Filter.checkDelInsertMapping(delInsertMap, examine, nodeKey, nodeValue);
                }
              }
              if (examine == false) {
                break;
              }
            } else {
              AbstractBastNode nodeKey = fieldKey.getField();
              AbstractBastNode nodeValue = fieldValue.getField();
              if (nodeKey == null || nodeValue == null) {
                continue;
              }
              if (WildcardAccessHelper.isEqual(nodeKey, nodeValue)) {
                examine = false;
                break;
              } else if (delInsertMap.get(nodeKey) == nodeValue) {
                examine = Filter.checkDelInsertMapping(delInsertMap, examine, nodeKey, nodeValue);
                if (entry.getKey().getTag() == BastCall.TAG) {
                  if (((BastCall) entry.getKey()).arguments
                      .size() != ((BastCall) entry.getValue()).arguments.size()) {
                    examine = true;
                    break;
                  }
                }
                break;
              } else if (nodeKey.getTag() == BastAccess.TAG
                  && nodeKey.getTag() == nodeValue.getTag()) {
                BastAccess nodeKeyAccess = ((BastAccess) nodeKey);
                BastAccess nodeValueAccess = ((BastAccess) nodeValue);
                if (WildcardAccessHelper.isEqual(nodeKeyAccess.member, nodeValueAccess.member)) {
                  if (delInsertMap.get(nodeKeyAccess.target) == nodeValueAccess.target) {
                    examine = false;
                    break;
                  }
                } else if (WildcardAccessHelper.isEqual(nodeKeyAccess.target,
                    nodeValueAccess.target)) {
                  if (delInsertMap.get(nodeKeyAccess.member) == nodeValueAccess.member) {
                    if (constant == BastFieldConstants.ASGN_EXPR_LEFT) {
                      examine = false;
                      break;
                    }
                  }
                }
                continue;
              }


            }
          }
          if (examine) {
            if (constants.size() > 1) {
              NodeParentInformationHierarchy hierarchyKey = hierarchyFirst.get(entry.getKey());
              NodeParentInformationHierarchy hierarchyValue =
                  hierarchySecond.get(entry.getValue());
              if (hierarchyKey != null && hierarchyValue != null) {
                boolean examineKey = true;
                boolean examineValue = true;
                for (NodeParentInformation npi : hierarchyKey.list) {
                  AbstractBastNode parent = npi.parent;
                  BastEditOperation operation = exDiffAa.editMapOld.get(parent);
                  if (operation != null && editSet.contains(operation)) {
                    examineKey = false;
                    break;
                  }
                }
                for (NodeParentInformation npi : hierarchyValue.list) {
                  AbstractBastNode parent = npi.parent;
                  BastEditOperation operation = exDiffAa.editMapNew.get(parent);
                  if (operation != null && editSet.contains(operation)) {
                    examineValue = false;
                    break;
                  }
                }
                if (examineKey || examineValue) {
                  if (hierarchyValue.list.size() > 0) {
                    InsertOperation ip = new InsertOperation(hierarchyValue.list.get(0).parent,
                        entry.getValue(), new NodeIndex(hierarchyValue.list.get(0).fieldConstant,
                            hierarchyValue.list.get(0).listId));
                    toAdd.add(ip);
                    CollectNodesVisitor nodeVisitor = new CollectNodesVisitor();
                    entry.getKey().accept(nodeVisitor);
                    for (AbstractBastNode node : nodeVisitor.nodes) {
                      if (node != entry.getKey()) {
                        BastEditOperation operation = exDiffAa.editMapOld.get(node);
                        if (operation != null && editSet.contains(operation)) {
                          if (operation.getUnchangedOrNewParentNode().getTag() != BastBlock.TAG
                              || operation.getUnchangedOrOldParentNode()
                                  .getTag() == BastBlock.TAG) {
                            toRemove.add(operation);
                          }
                        }
                      }
                    }
                    nodeVisitor = new CollectNodesVisitor();
                    entry.getValue().accept(nodeVisitor);
                    for (AbstractBastNode node : nodeVisitor.nodes) {
                      if (node != entry.getValue()) {
                        BastEditOperation operation = exDiffAa.editMapNew.get(node);
                        if (operation != null && editSet.contains(operation)) {
                          if (operation.getUnchangedOrNewParentNode().getTag() != BastBlock.TAG
                              || operation.getUnchangedOrOldParentNode()
                                  .getTag() == BastBlock.TAG) {
                            toRemove.add(operation);
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
    workList.removeAll(toRemove);
    workList.addAll(toAdd);
    return workList;
  }



  public static FilterRule getInstance() {
    return new CombineSubchangesOfStatements();
  }

}