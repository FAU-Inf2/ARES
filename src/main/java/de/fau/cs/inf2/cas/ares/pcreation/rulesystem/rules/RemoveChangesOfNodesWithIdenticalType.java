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

import de.fau.cs.inf2.cas.ares.pcreation.MatchBoundary;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAccess;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.visitors.CollectNodesVisitor;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.AdvancedEditOperation;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RemoveChangesOfNodesWithIdenticalType extends AbstractFilterRule {

  public RemoveChangesOfNodesWithIdenticalType() {
    super(FilterRuleType.REMOVE_CHANGES_OF_NODES_WITH_IDENTICAL_TYPE);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    worklist = removeChangesForNodePairs(worklist, exDiffCurrent, delInsertMap, hierarchyFirst,
        hierarchySecond, matchBoundary);
    worklist = removeChangesForDeleteInsertPairs(worklist, delInsertMap);
    return removeCallChanges(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);

  }

  private static List<BastEditOperation> removeChangesForNodePairs(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffCurrent, HashMap<AbstractBastNode, AbstractBastNode> delInsertMap,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond,
      MatchBoundary boundary) {
    List<BastEditOperation> toRemoveSecond = new LinkedList<BastEditOperation>();
    List<BastEditOperation> toRemoveFirst = new LinkedList<BastEditOperation>();
    for (BastEditOperation epFirst : workList) {
      if (toRemoveFirst.contains(epFirst)) {
        continue;
      }
      AbstractBastNode parent = null;
      if (epFirst.getType() != EditOperationType.UPDATE
          && epFirst.getUnchangedOrOldParentNode() != null
          && epFirst.getUnchangedOrOldParentNode().nodeId == 232) {
        exDiffCurrent.secondToFirstMap.get(epFirst.getUnchangedOrOldParentNode());
      }
      switch (epFirst.getType()) {
        case ALIGN:
        case DELETE:
        case MOVE:
          parent = epFirst.getUnchangedOrOldParentNode();
          break;
        default:
          break;
      }
      if (parent == null) {
        continue;
      }
      AbstractBastNode partnerParent = null;
      if (exDiffCurrent.firstToSecondMap.get(parent) != null) {
        partnerParent = exDiffCurrent.firstToSecondMap.get(parent);
      } else if (parent == boundary.getNode1()) {
        partnerParent = boundary.getNode2();
      } else {
        switch (epFirst.getType()) {
          case ALIGN:
            parent = epFirst.getUnchangedOrNewParentNode();
            break;
          default:
            break;
        }
      }
      if (partnerParent == null) {
        continue;
      }
      for (BastEditOperation epSecond : workList) {
        if (epFirst == epSecond) {
          continue;
        }
        if (toRemoveSecond.contains(epSecond) || toRemoveFirst.contains(epFirst)) {
          continue;
        }
        switch (epSecond.getType()) {
          case INSERT:
          case ALIGN:
          case MOVE:
            if (epSecond.getUnchangedOrNewParentNode() == partnerParent) {
              if (epFirst.getOldOrInsertedNode().getTag() == epSecond.getNewOrChangedNode()
                  .getTag()) {
                if (epFirst.getOldOrInsertedNode().getTag() == BastNameIdent.TAG
                    || hierarchyFirst.get(epFirst.getUnchangedOrOldParentNode()) == null
                    || hierarchySecond.get(epFirst.getUnchangedOrNewParentNode()) == null
                    || hierarchyFirst.get(epFirst.getUnchangedOrOldParentNode()).list.get(0).parent
                        .getTag() == hierarchySecond.get(epFirst.getUnchangedOrNewParentNode()).list
                            .get(0).parent.getTag()) {
                  if (epFirst.getOldOrChangedIndex().childrenListNumber == epSecond
                      .getNewOrChangedIndex().childrenListNumber) {
                    if (epFirst.getOldOrChangedIndex().childrenListIndex == epSecond
                        .getNewOrChangedIndex().childrenListIndex) {
                      if (WildcardAccessHelper.isEqual(epFirst.getOldOrInsertedNode(),
                          epSecond.getNewOrChangedNode(), true)) {
                        ;
                        hierarchyFirst.get(epSecond.getUnchangedOrOldParentNode());
                        hierarchySecond.get(epFirst.getUnchangedOrNewParentNode());
                        hierarchySecond.get(epSecond.getUnchangedOrNewParentNode());

                        delInsertMap.put(epFirst.getOldOrInsertedNode(),
                            epSecond.getNewOrChangedNode());
                        delInsertMap.put(epSecond.getNewOrChangedNode(),
                            epFirst.getOldOrInsertedNode());
                        toRemoveFirst.add(epFirst);
                        toRemoveSecond.add(epSecond);
                        CollectNodesVisitor visitor = new CollectNodesVisitor();
                        epFirst.getOldOrInsertedNode().accept(visitor);
                        for (AbstractBastNode node : visitor.nodes) {
                          BastEditOperation subOp = exDiffCurrent.editMapOld.get(node);
                          if (subOp instanceof AdvancedEditOperation) {
                            subOp = ((AdvancedEditOperation) subOp).getBasicOperation();
                          }
                          if (subOp != null && subOp.getType() == EditOperationType.DELETE) {
                            toRemoveFirst.add(subOp);
                          }
                        }
                        continue;
                      }

                    } else {
                      if (WildcardAccessHelper.isEqual(epFirst.getOldOrInsertedNode(),
                          epSecond.getNewOrChangedNode(), true)) {
                        int diff = 0;
                        for (BastEditOperation epThird : workList) {
                          if (epThird.getUnchangedOrNewParentNode() == partnerParent) {
                            if (epFirst.getOldOrChangedIndex().childrenListNumber == epThird
                                .getNewOrChangedIndex().childrenListNumber) {
                              switch (epThird.getType()) {
                                case INSERT:
                                  if (epThird.getNewOrChangedIndex().childrenListIndex < epSecond
                                      .getNewOrChangedIndex().childrenListIndex) {
                                    diff--;
                                  }
                                  break;
                                default:
                              }
                            }
                          } else if (epThird.getUnchangedOrOldParentNode() == parent) {
                            if (epFirst.getOldOrChangedIndex().childrenListNumber == epThird
                                .getNewOrChangedIndex().childrenListNumber) {
                              switch (epThird.getType()) {
                                case DELETE:
                                  if (epThird.getOldOrChangedIndex().childrenListIndex < epFirst
                                      .getOldOrChangedIndex().childrenListIndex) {
                                    diff++;
                                  }
                                  break;
                                default:
                              }
                            }
                          }
                        }
                        if (epFirst.getOldOrChangedIndex().childrenListIndex == epSecond
                            .getNewOrChangedIndex().childrenListIndex + diff) {
                          continue;
                        }

                      }
                    }
                  }
                }
              }
            }
            break;
          default:
            break;

        }
      }
    }
    workList.removeAll(toRemoveFirst);
    workList.removeAll(toRemoveSecond);
    return workList;
  }

  private static List<BastEditOperation> removeChangesForDeleteInsertPairs(
      List<BastEditOperation> workList,
      HashMap<AbstractBastNode, AbstractBastNode> deleteInsertMap) {
    boolean changed = true;
    LinkedList<BastEditOperation> toRemove = new LinkedList<BastEditOperation>();
    while (changed) {
      changed = false;
      for (BastEditOperation ep : workList) {
        if (!toRemove.contains(ep)) {
          if (ep.getType() == EditOperationType.INSERT) {
            if (deleteInsertMap.get(ep.getUnchangedOrOldParentNode()) != null) {
              AbstractBastNode otherParent = deleteInsertMap.get(ep.getUnchangedOrOldParentNode());
              for (BastEditOperation epOther : workList) {
                if (!toRemove.contains(epOther)) {
                  if (epOther.getType() == EditOperationType.DELETE) {
                    if (epOther.getUnchangedOrOldParentNode() == otherParent) {
                      if (epOther.getOldOrInsertedNode().getTag() == ep.getOldOrInsertedNode()
                          .getTag()
                          && ep.getUnchangedOrOldParentNode().getTag() != BastDeclaration.TAG
                          && ep.getUnchangedOrOldParentNode().getTag() != BastNameIdent.TAG) {
                        BastFieldConstants childrenListNumber =
                            ep.getOldOrChangedIndex().childrenListNumber;
                        BastFieldConstants childrenListNumber2 =
                            epOther.getOldOrChangedIndex().childrenListNumber;
                        if (childrenListNumber2 == childrenListNumber) {
                          if (epOther.getOldOrChangedIndex().childrenListIndex == ep
                              .getOldOrChangedIndex().childrenListIndex) {
                            if (epOther.getOldOrInsertedNode().getTag() == BastCall.TAG) {
                              if (((BastCall) epOther.getOldOrInsertedNode()).function
                                  .getTag() != ((BastCall) ep.getOldOrInsertedNode()).function
                                      .getTag()) {
                                break;

                              } else {
                                boolean test = examineCall(workList, epOther);
                                if (!test) {
                                  toRemove.add(ep);
                                  toRemove.addFirst(epOther);
                                  deleteInsertMap.put(ep.getOldOrInsertedNode(),
                                      epOther.getOldOrInsertedNode());
                                  deleteInsertMap.put(epOther.getOldOrInsertedNode(),
                                      ep.getOldOrInsertedNode());
                                  break;
                                } else {
                                  break;
                                }
                              }
                            } else {
                              if (ep.getUnchangedOrOldParentNode().getTag() == BastCall.TAG
                                  && childrenListNumber == BastFieldConstants.DIRECT_CALL_FUNCTION
                                  && ep.getOldOrInsertedNode().getTag() == BastAccess.TAG) {
                                if (WildcardAccessHelper.isEqual(ep.getOldOrInsertedNode(),
                                    epOther.getOldOrInsertedNode())) {
                                  toRemove.add(ep);
                                  toRemove.addFirst(epOther);
                                  deleteInsertMap.put(ep.getOldOrInsertedNode(),
                                      epOther.getOldOrInsertedNode());
                                  deleteInsertMap.put(epOther.getOldOrInsertedNode(),
                                      ep.getOldOrInsertedNode());
                                } else {
                                  break;
                                }
                              }
                              toRemove.add(ep);
                              toRemove.addFirst(epOther);
                              deleteInsertMap.put(ep.getOldOrInsertedNode(),
                                  epOther.getOldOrInsertedNode());
                              deleteInsertMap.put(epOther.getOldOrInsertedNode(),
                                  ep.getOldOrInsertedNode());
                              break;

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
      if (!toRemove.isEmpty()) {
        workList.removeAll(toRemove);
        changed = true;
        toRemove.clear();
      }
    }
    return workList;
  }

  private static boolean examineCall(List<BastEditOperation> exprDel, BastEditOperation delOp) {
    BastCall call = (BastCall) delOp.getOldOrInsertedNode();
    boolean[] arguments = new boolean[call.arguments.size()];
    boolean function = false;
    for (BastEditOperation ep : exprDel) {
      if (ep.getType() == EditOperationType.DELETE) {
        for (int i = 0; i < arguments.length; i++) {
          if (call.arguments.get(i) == ep.getOldOrInsertedNode()) {
            arguments[i] = true;
          }
        }
        if (ep.getOldOrInsertedNode() == call.function) {
          function = true;
        }
      }
    }
    boolean test = function;
    for (int i = 0; i < arguments.length; i++) {
      test &= arguments[i];
    }
    return test;
  }

  private static List<BastEditOperation> removeCallChanges(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    LinkedList<BastEditOperation> toRemove = new LinkedList<>();
    for (BastEditOperation ep : workList) {
      if (ep.getType() != EditOperationType.UPDATE) {
        if (ep.getType() == EditOperationType.DELETE) {
          if (toRemove.contains(ep)) {
            continue;
          }
          for (BastEditOperation ep2 : workList) {
            if (toRemove.contains(ep2)) {
              continue;
            }
            if (ep2.getType() == EditOperationType.INSERT) {
              if (ep.getUnchangedOrOldParentNode().getTag() == ep2.getUnchangedOrOldParentNode()
                  .getTag()) {
                if (ep.getOldOrChangedIndex().childrenListNumber.isList
                    && ep2.getOldOrChangedIndex().childrenListNumber.isList) {
                  AbstractBastNode parent = ep.getUnchangedOrOldParentNode();
                  AbstractBastNode parentPartner = exDiffAa.firstToSecondMap.get(parent);
                  if (parentPartner == null) {
                    parentPartner = exDiffAa.secondToFirstMap.get(parent);
                  }
                  if (parentPartner != null) {
                    if (ep2.getUnchangedOrOldParentNode() == parentPartner) {
                      @SuppressWarnings("unchecked")
                      LinkedList<AbstractBastNode> listDel = (LinkedList<AbstractBastNode>) ep
                          .getUnchangedOrOldParentNode()
                          .getField(ep.getOldOrChangedIndex().childrenListNumber).getListField();
                      @SuppressWarnings("unchecked")
                      LinkedList<AbstractBastNode> listIns = (LinkedList<AbstractBastNode>) ep2
                          .getUnchangedOrOldParentNode()
                          .getField(ep.getOldOrChangedIndex().childrenListNumber).getListField();
                      if (listDel != null && listIns != null) {
                        if (listDel.size() > ep.getOldOrChangedIndex().childrenListIndex + 1
                            && listIns.size() > ep2.getOldOrChangedIndex().childrenListIndex + 1) {
                          AbstractBastNode possiblePartner = exDiffAa.firstToSecondMap
                              .get(listDel.get(ep.getOldOrChangedIndex().childrenListIndex + 1));
                          if (possiblePartner == null) {
                            possiblePartner = exDiffAa.secondToFirstMap
                                .get(listDel.get(ep.getOldOrChangedIndex().childrenListIndex + 1));
                          }
                          if (possiblePartner != null) {
                            if (possiblePartner == listIns
                                .get(ep2.getOldOrChangedIndex().childrenListIndex + 1)) {
                              if (WildcardAccessHelper.isEqual(ep.getOldOrInsertedNode(),
                                  ep2.getOldOrInsertedNode())) {
                                toRemove.add(ep);
                                toRemove.add(ep2);
                              } else {
                                if (ep.getUnchangedOrOldParentNode().getTag() != BastBlock.TAG) {
                                  toRemove.add(ep);
                                }
                              }
                              break;
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
    }
    workList.removeAll(toRemove);
    return workList;
  }

  public static FilterRule getInstance() {
    return new RemoveChangesOfNodesWithIdenticalType();
  }
}
