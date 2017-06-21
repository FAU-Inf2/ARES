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
import de.fau.cs.inf2.cas.ares.pcreation.MatchBoundary;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.DeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AddDeleteForChangeRootStatements extends AbstractFilterRule {

  public AddDeleteForChangeRootStatements() {
    super(FilterRuleType.ADD_DELETE_FOR_CHANGE_ROOT_STATEMENTS);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return addDeleteForChangeRootStatements(worklist, exDiffCurrent, hierarchyFirst,
        hierarchySecond, matchBoundary);
  }

  @SuppressWarnings("unchecked")
  private static List<BastEditOperation> addDeleteForChangeRootStatements(
      List<BastEditOperation> workList, ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond,
      MatchBoundary matchBoundary) {
    final List<BastEditOperation> toRemove = new LinkedList<>();
    final List<BastEditOperation> toAdd = new LinkedList<>();
    LinkedList<AbstractBastNode> oldNodes = null;
    LinkedList<AbstractBastNode> newNodes = null;
    if (matchBoundary.getNode2().getTag() == BastBlock.TAG) {
      newNodes = (LinkedList<AbstractBastNode>) matchBoundary.getNode2()
          .getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
    } else if (matchBoundary.getNode2().getTag() == AresBlock.TAG) {
      newNodes = (LinkedList<AbstractBastNode>) ((AresBlock) matchBoundary.getNode2()).block
          .getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
    } else if (matchBoundary.getNode2().getTag() == BastFunction.TAG) {
      newNodes = (LinkedList<AbstractBastNode>) ((BastBlock) ((BastFunction) matchBoundary
          .getNode2()).statements.getFirst()).getField(BastFieldConstants.BLOCK_STATEMENT)
              .getListField();
      if (newNodes.size() == 1) {
        if (newNodes.getFirst().getTag() == AresBlock.TAG) {
          newNodes = (LinkedList<AbstractBastNode>) ((AresBlock) newNodes.getFirst()).block
              .getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
        }
      }
    }
    if (matchBoundary.getNode1().getTag() == BastBlock.TAG) {
      oldNodes = (LinkedList<AbstractBastNode>) matchBoundary.getNode1()
          .getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
    } else if (matchBoundary.getNode1().getTag() == BastFunction.TAG) {
      oldNodes = (LinkedList<AbstractBastNode>) ((BastBlock) ((BastFunction) matchBoundary
          .getNode1()).statements.getFirst()).getField(BastFieldConstants.BLOCK_STATEMENT)
              .getListField();
    }
    for (BastEditOperation editOp : workList) {
      if (editOp.getType() == EditOperationType.MOVE) {
        NodeParentInformationHierarchy npi = hierarchySecond.get(editOp.getNewOrChangedNode());
        NodeParentInformationHierarchy npiOld = hierarchyFirst.get(editOp.getOldOrInsertedNode());

        if (npi != null && npiOld != null && oldNodes != null && newNodes != null
            && npi.list.size() > 0 && npi.list.get(0).parent == matchBoundary.getNode2()
            && npiOld.list.size() > 1
            && !(npiOld.list.get(0).parent == matchBoundary.getNode1()
                || (npiOld.list.get(1).parent == matchBoundary.getNode1()
                    && matchBoundary.getNode1().getTag() == BastFunction.TAG))) {
          for (int i = npi.list.get(0).listId - 1; i >= 0; i--) {
            if (exDiffAa.editMapNew.get(newNodes.get(i)) == null) {
              AbstractBastNode partner = exDiffAa.secondToFirstMap.get(newNodes.get(i));
              if (partner != null) {
                int index = oldNodes.indexOf(partner);
                int offset = npi.list.get(0).listId - i;
                if (index != -1 && index + offset < oldNodes.size()) {
                  if (WildcardAccessHelper.isEqual(oldNodes.get(index + offset),
                      editOp.getNewOrChangedNode())) {
                    DeleteOperation delOp =
                        new DeleteOperation(editOp.getUnchangedOrOldParentNode(),
                            editOp.getOldOrInsertedNode(), editOp.getOldOrChangedIndex());
                    toAdd.add(delOp);
                    toRemove.add(editOp);
                    for (BastEditOperation tmpOp : workList) {
                      if (tmpOp.getOldOrInsertedNode() == oldNodes.get(index + offset)) {
                        toRemove.add(tmpOp);
                      }
                    }
                    break;
                  }
                }
              }
            }
          }
        }
      } else if (editOp.getType() == EditOperationType.ALIGN) {
        NodeParentInformationHierarchy npi = hierarchySecond.get(editOp.getNewOrChangedNode());
        NodeParentInformationHierarchy npiOld = hierarchyFirst.get(editOp.getOldOrInsertedNode());

        if (npi != null && npiOld != null && oldNodes != null && newNodes != null
            && npi.list.size() > 0 && npi.list.get(0).parent == matchBoundary.getNode2()
            && npiOld.list.size() > 1
            && (npiOld.list.get(0).parent == matchBoundary.getNode1()
                || (npiOld.list.get(1).parent == matchBoundary.getNode1()
                    && matchBoundary.getNode1().getTag() == BastFunction.TAG))) {
          boolean insideAresBlock = false;
          for (int i = 0; i < npi.list.size(); i++) {
            if (npi.list.get(i).parent.getTag() == AresBlock.TAG) {
              insideAresBlock = true;
              break;
            }
          }
          if (editOp.getOldOrChangedIndex().childrenListIndex > newNodes.size()
              && insideAresBlock) {
            toRemove.add(editOp);
            DeleteOperation delOp = new DeleteOperation(editOp.getUnchangedOrOldParentNode(),
                editOp.getOldOrInsertedNode(), editOp.getOldOrChangedIndex());
            toAdd.add(delOp);
          }
        }
      }
    }
    workList.removeAll(toRemove);
    workList.addAll(toAdd);
    return workList;
  }



  public static FilterRule getInstance() {
    return new AddDeleteForChangeRootStatements();
  }
}
