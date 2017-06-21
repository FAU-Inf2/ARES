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

import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.pcreation.CombinationHelper;
import de.fau.cs.inf2.cas.ares.pcreation.MatchBoundary;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitchCaseGroup;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RemoveChangesWithIdenticalPartners extends AbstractFilterRule {

  public RemoveChangesWithIdenticalPartners() {
    super(FilterRuleType.REMOVE_CHANGES_WITH_IDENTICAL_PARTNERS);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return handleIdenticalPartners(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond,
        matchBoundary);
  }

  private static List<BastEditOperation> handleIdenticalPartners(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond,
      MatchBoundary boundary) {
    List<BastEditOperation> toRemove = new LinkedList<>();
    List<BastEditOperation> toAdd = new LinkedList<>();
    for (BastEditOperation ep : workList) {
      if (ep.getType() == EditOperationType.MOVE) {
        if (ep.getOldOrChangedIndex().childrenListNumber.isList) {
          LinkedList<? extends AbstractBastNode> list = ep.getUnchangedOrOldParentNode()
              .getField(ep.getOldOrChangedIndex().childrenListNumber).getListField();
          int offset = 0;
          for (int i = 0; i < Math.min(list.size(),
              ep.getOldOrChangedIndex().childrenListIndex); i++) {
            BastEditOperation oldOp = exDiffAa.editMapOld.get(list.get(i));
            if (oldOp != null && oldOp.getType() == EditOperationType.MOVE) {
              offset++;
            }
          }
          if (ep.getOldOrChangedIndex().childrenListIndex - offset >= 0
              && ep.getOldOrChangedIndex().childrenListIndex
                  - offset == ep.getNewOrChangedIndex().childrenListIndex) {
            if (WildcardAccessHelper.isEqual(ep.getOldOrInsertedNode(),
                ep.getNewOrChangedNode())) {
              NodeParentInformationHierarchy npiOld =
                  hierarchyFirst.get(ep.getUnchangedOrOldParentNode());
              NodeParentInformationHierarchy npiNew =
                  hierarchySecond.get(ep.getUnchangedOrNewParentNode());
              if (npiOld != null && npiNew != null && npiOld.list.size() > 0
                  && npiNew.list.size() > 0) {
                if (npiOld.list.get(0).parent.getTag() == npiNew.list.get(0).parent.getTag()) {
                  toRemove.add(ep);
                  continue;
                }
              }
            }
          }
          if (ep.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG) {
            AbstractBastNode block = (BastBlock) (ep).getUnchangedOrOldParentNode();
            block = (BastBlock) exDiffAa.firstToSecondMap
                .get(((BastBlock) (ep).getUnchangedOrOldParentNode()));
            block = CombinationHelper.optimizeBlockMapping((ep).getUnchangedOrOldParentNode(),
                block, exDiffAa.firstToSecondMap, exDiffAa, hierarchyFirst, hierarchySecond);

            if (block != null) {
              if (block.getTag() == AresBlock.TAG) {
                block = ((AresBlock) block).block;
              }
              if (WildcardAccessHelper.isEqual(ep.getOldOrInsertedNode(),
                  ep.getNewOrChangedNode())) {
                int possiblePartner = 0;
                for (AbstractBastStatement stmt : ((BastBlock) block).statements) {
                  if (WildcardAccessHelper.isEqual(ep.getNewOrChangedNode(), stmt)) {
                    possiblePartner++;
                  }
                }

                if (possiblePartner == 1) {
                  if (boundary.getNode2() != ep.getUnchangedOrNewParentNode()) {
                    InsertOperation insOp = new InsertOperation(ep.getUnchangedOrNewParentNode(),
                        ep.getNewOrChangedNode(), ep.getNewOrChangedIndex());
                    toAdd.add(insOp);
                    toRemove.add(ep);
                  }
                  continue;
                }
              }
            }
          }
        }
        AbstractBastNode partner =
            exDiffAa.secondToFirstMap.get(ep.getUnchangedOrNewParentNode());
        if (partner != null) {
          if (partner.getField(ep.getNewOrChangedIndex().childrenListNumber) != null) {
            if (partner.getField(ep.getNewOrChangedIndex().childrenListNumber).isList()) {
              if (partner.getField(ep.getNewOrChangedIndex().childrenListNumber).getListField()
                  .size() > ep.getNewOrChangedIndex().childrenListIndex) {
                AbstractBastNode partnerChild =
                    partner.getField(ep.getNewOrChangedIndex().childrenListNumber).getListField()
                        .get(ep.getNewOrChangedIndex().childrenListIndex);
                if (partnerChild.getTag() == ep.getNewOrChangedNode().getTag()) {
                  if (WildcardAccessHelper.isEqual(partnerChild, ep.getNewOrChangedNode(),
                      true)) {
                    toRemove.add(ep);
                    if (exDiffAa.editMapOld.get(partnerChild) != null) {
                      toRemove.add(exDiffAa.editMapOld.get(partnerChild));
                    }
                  } else {
                    partner = exDiffAa.firstToSecondMap.get(ep.getUnchangedOrOldParentNode());
                    if (partner != null) {
                      if (!ep.getOldOrChangedIndex().childrenListNumber.isList) {
                        AbstractBastNode oldPart = partner
                            .getField(ep.getOldOrChangedIndex().childrenListNumber).getField();
                        if (oldPart != null && WildcardAccessHelper.isWildcard(oldPart)) {
                          InsertOperation insOp =
                              new InsertOperation(ep.getUnchangedOrNewParentNode(),
                                  ep.getNewOrChangedNode(), ep.getNewOrChangedIndex());
                          toAdd.add(insOp);
                          toRemove.add(ep);
                        }
                      }
                    }
                  }
                }
              }
              continue;
            } else {
              AbstractBastNode partnerChild =
                  partner.getField(ep.getNewOrChangedIndex().childrenListNumber).getField();
              if (WildcardAccessHelper.isEqual(ep.getNewOrChangedNode(), partnerChild)) {
                toRemove.add(ep);
              } else if (partnerChild != null && partnerChild.getTag() == BastBlock.TAG) {
                LinkedList<? extends AbstractBastNode> items =
                    partnerChild.getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
                if (items.size() == 1) {
                  if (WildcardAccessHelper.isEqual(items.getFirst(), ep.getNewOrChangedNode())) {
                    toRemove.add(ep);
                  }
                }
              }
            }
          }
        }
        partner = exDiffAa.firstToSecondMap.get(ep.getUnchangedOrOldParentNode());
        if (partner != null) {
          if (partner.getField(ep.getOldOrChangedIndex().childrenListNumber) != null) {
            if (partner.getField(ep.getOldOrChangedIndex().childrenListNumber).isList()) {
              continue;
            } else {
              AbstractBastNode partnerChild =
                  partner.getField(ep.getOldOrChangedIndex().childrenListNumber).getField();
              if (WildcardAccessHelper.isEqual(ep.getOldOrInsertedNode(), partnerChild)) {
                toRemove.add(ep);
              } else if (ep.getOldOrInsertedNode().getTag() == BastBlock.TAG) {
                LinkedList<? extends AbstractBastNode> items = ep.getOldOrInsertedNode()
                    .getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
                if (items.size() == 1) {
                  if (WildcardAccessHelper.isEqual(items.getFirst(), partnerChild)) {
                    toRemove.add(ep);
                  }
                }
              }
            }
          }
        }
      } else if (ep.getType() == EditOperationType.DELETE) {
        NodeParentInformationHierarchy npi = hierarchyFirst.get(ep.getOldOrInsertedNode());
        if (npi != null && npi.list.size() > 1) {
          AbstractBastNode parent = npi.list.get(1).parent;
          AbstractBastNode partner = exDiffAa.firstToSecondMap.get(parent);
          if (partner != null && ep.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG) {
            AbstractBastNode block = (BastBlock) (ep).getUnchangedOrOldParentNode();
            block = (BastBlock) exDiffAa.firstToSecondMap
                .get(((BastBlock) (ep).getUnchangedOrOldParentNode()));
            block = CombinationHelper.optimizeBlockMapping((ep).getUnchangedOrOldParentNode(),
                block, exDiffAa.firstToSecondMap, exDiffAa, hierarchyFirst, hierarchySecond);
            NodeParentInformationHierarchy npiNew = hierarchySecond.get(block);
            if (npiNew != null && npiNew.list.size() > 0) {
              if (partner.getTag() != npiNew.list.get(0).parent.getTag()) {
                if (partner.getTag() != BastFunction.TAG
                    || npiNew.list.get(0).parent.getTag() != AresBlock.TAG) {
                  if (block.getTag() != AresBlock.TAG) {
                    toRemove.add(ep);
                  } else {
                    AresBlock tmpBlock = ((AresBlock) block);
                    LinkedList<? extends AbstractBastNode> nodes = tmpBlock.block
                        .getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
                    if (nodes.size() > ep.getOldOrChangedIndex().childrenListIndex) {
                      if (nodes.get(ep.getOldOrChangedIndex().childrenListIndex)
                          .getTag() == AresChoiceStmt.TAG) {
                        toRemove.add(ep);
                      }
                    }
                    for (AbstractBastNode node : nodes) {
                      if (node.getTag() == AresChoiceStmt.TAG) {
                        AresChoiceStmt choice = ((AresChoiceStmt) node);
                        if (choice.choiceBlock != null
                            && ((BastBlock) choice.choiceBlock).statements.size() > 0) {
                          for (AbstractBastNode choiceCase : choice.choiceBlock
                              .getField(BastFieldConstants.BLOCK_STATEMENT).getListField()) {
                            if (WildcardAccessHelper.isPart(choiceCase, ep.getOldOrInsertedNode(),
                                true)) {
                              toRemove.add(ep);
                            }
                          }
                        }
                      }
                    }
                  }
                } else if (npiNew.list.get(0).parent.getTag() == AresBlock.TAG) {
                  AresBlock tmpBlock = ((AresBlock) npiNew.list.get(0).parent);
                  LinkedList<? extends AbstractBastNode> nodes =
                      tmpBlock.block.getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
                  if (nodes.size() > ep.getOldOrChangedIndex().childrenListIndex) {
                    if (nodes.get(ep.getOldOrChangedIndex().childrenListIndex)
                        .getTag() == AresChoiceStmt.TAG) {
                      toRemove.add(ep);
                    }
                  }
                }
              } else {
                for (int i = 0; i < npiNew.list.size(); i++) {
                  if (npiNew.list.get(i).parent.getTag() == AresChoiceStmt.TAG) {
                    toRemove.add(ep);
                  }
                }
              }
            }
          } else {
            if (partner == null && ep.getUnchangedOrOldParentNode().getTag() != BastBlock.TAG
                && ep.getUnchangedOrOldParentNode().getTag() != BastSwitchCaseGroup.TAG) {
              int block = 0;
              int nonBlock = 0;
              int secondPartnerPos = 0;
              AbstractBastNode firstPartner = null;
              int firstPartnerPos = 0;
              AbstractBastNode secondPartner = null;

              for (int i = 0; i < npi.list.size(); i++) {
                if (firstPartner == null
                    && exDiffAa.firstToSecondMap.get(npi.list.get(i).parent) != null) {
                  firstPartner = exDiffAa.firstToSecondMap.get(npi.list.get(i).parent);
                  firstPartnerPos = i;
                }
                if (npi.list.get(i).parent.getTag() == BastBlock.TAG) {
                  block++;
                } else if (block > 0 && npi.list.get(i).parent.getTag() != BastBlock.TAG) {
                  nonBlock++;
                }
                if (nonBlock == 2) {
                  if (exDiffAa.firstToSecondMap.get(npi.list.get(i).parent) != null) {
                    secondPartner = exDiffAa.firstToSecondMap.get(npi.list.get(i).parent);
                    secondPartnerPos = i;
                  }
                  break;
                }
              }
              if (secondPartner != null && firstPartner != null) {
                NodeParentInformationHierarchy npiNew = hierarchySecond.get(firstPartner);
                if (npiNew != null) {
                  if (npiNew.list.size() > secondPartnerPos - firstPartnerPos) {
                    if (npiNew.list
                        .get(secondPartnerPos - firstPartnerPos).parent != secondPartner) {
                      toRemove.add(ep);
                    }
                  }
                }
              }
            }
          }
        }
      } else if (ep.getType() == EditOperationType.INSERT) {
        NodeParentInformationHierarchy npi = hierarchySecond.get(ep.getNewOrChangedNode());
        if (npi != null && npi.list.size() > 1) {
          AbstractBastNode grandParent = npi.list.get(1).parent;
          AbstractBastNode partner = exDiffAa.secondToFirstMap.get(grandParent);
          if (partner != null) {
            if (!npi.list.get(1).fieldConstant.isList) {
              AbstractBastNode parentPartner =
                  partner.fieldMap.get(npi.list.get(1).fieldConstant).getField();
              if (parentPartner != null) {
                if (parentPartner.getTag() == BastBlock.TAG
                    && ep.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG) {
                  LinkedList<? extends AbstractBastNode> partnerStmts = parentPartner.fieldMap
                      .get(BastFieldConstants.BLOCK_STATEMENT).getListField();
                  int itemsFound = 0;
                  int pos = 0;
                  for (int i = 0; i < partnerStmts.size(); i++) {
                    if (partnerStmts.get(i).getTag() == ep.getNewOrChangedNode().getTag()) {
                      switch (partnerStmts.get(i).getTag()) {
                        case BastIf.TAG: {
                          AbstractBastNode partnerCondition = partnerStmts.get(i)
                              .getField(BastFieldConstants.IF_CONDITION).getField();
                          AbstractBastNode editCondition = ep.getNewOrChangedNode()
                              .getField(BastFieldConstants.IF_CONDITION).getField();
                          if (WildcardAccessHelper.isEqual(partnerCondition, editCondition)) {
                            itemsFound++;
                            pos = i;
                          }
                          break;
                        }
                        case BastDeclaration.TAG:
                        case BastCall.TAG: {

                          if (WildcardAccessHelper.isEqual(partnerStmts.get(i),
                              ep.getNewOrChangedNode(), true)) {
                            if (i == ep.getNewOrChangedIndex().childrenListIndex) {
                              itemsFound++;
                              pos = i;
                            }
                          }
                          break;
                        }
                        default:
                          break;
                      }
                    }
                  }
                  if (itemsFound == 1) {
                    toRemove.add(ep);
                    switch (ep.getNewOrChangedNode().getTag()) {
                      case BastIf.TAG:
                        AbstractBastNode partnerCondition = partnerStmts.get(pos)
                            .getField(BastFieldConstants.IF_CONDITION).getField();
                        AbstractBastNode editCondition = ep.getNewOrChangedNode()
                            .getField(BastFieldConstants.IF_CONDITION).getField();
                        for (BastEditOperation innerOp : workList) {
                          if (innerOp.getType() == EditOperationType.DELETE) {
                            NodeParentInformationHierarchy npitmp =
                                hierarchyFirst.get(innerOp.getOldOrInsertedNode());
                            if (npitmp != null) {
                              for (int i = 0; i < npitmp.list.size(); i++) {
                                if (npitmp.list.get(i).parent == partnerCondition) {
                                  toRemove.add(innerOp);
                                  break;
                                }
                              }
                            }
                          }
                          if (innerOp.getUnchangedOrOldParentNode() == partnerCondition
                              || innerOp.getUnchangedOrNewParentNode() == editCondition) {
                            toRemove.add(innerOp);
                          }
                        }
                        break;
                      default:
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
    workList.removeAll(toRemove);
    workList.addAll(toAdd);
    return workList;
  }



  public static FilterRule getInstance() {
    return new RemoveChangesWithIdenticalPartners();
  }
}