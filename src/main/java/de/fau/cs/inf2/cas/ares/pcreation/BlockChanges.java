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

package de.fau.cs.inf2.cas.ares.pcreation;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;

public class BlockChanges {
  private LinkedList<AbstractBastNode> nodes = new LinkedList<>();
  private HashMap<AbstractBastNode, LinkedList<AbstractBastNode>> expressionWildcardMap =
      new HashMap<>();
  private HashSet<AbstractBastNode> expressionWildcardSet = new HashSet<>();

  private HashMap<AbstractBastNode, AbstractBastNode> replacedStatement = new HashMap<>();
  private HashMap<AbstractBastNode, BastEditOperation> wildcardEditOp = new HashMap<>();

  private HashSet<AbstractBastNode> initialWildcards = new HashSet<>();
  private HashSet<AbstractBastNode> additionalWildcards = new HashSet<>();
  private HashSet<AbstractBastNode> delInsWildcards = new HashSet<>();
  private HashMap<AbstractBastNode, AbstractBastNode> wildcardReplaceMap = new HashMap<>();

  BlockChanges(AbstractBastNode parentNode, BastFieldConstants constant) {
    if (parentNode == null || parentNode.getField(constant) == null) {
      assert (false);
    }
    if (parentNode.getField(constant).isList()) {
      if (parentNode.getField(constant).getListField() != null) {
        this.nodes.addAll(parentNode.getField(constant).getListField());
      }
    } else {
      this.nodes.add(parentNode.getField(constant).getField());
    }
    if (parentNode.getField(constant).isList()) {
      if (parentNode.getField(constant).getListField() != null) {
        for (AbstractBastNode node : parentNode.getField(constant).getListField()) {
          if (node.getTag() == AresWildcard.TAG || node.getTag() == AresUseStmt.TAG) {
            initialWildcards.add(node);
            if (node.getTag() == AresWildcard.TAG) {
              if (((AresWildcard) node).plugin.exprList != null
                  && ((AresWildcard) node).plugin.exprList.get(0) != null
                  && ((AresPatternClause) ((AresWildcard) node).plugin.exprList
                      .get(0)).expr != null) {
                expressionWildcardSet.add(node);
              }
            } else if (node.getTag() == AresUseStmt.TAG) {
              if (WildcardAccessHelper.getExpr(node) != null) {
                expressionWildcardSet.add(node);
              }
            }
          }
        }
      }
    }
  }

  void updateExpressionWildcard(AbstractBastNode node, AbstractBastNode wildcard) {
    assert (nodes.contains(node));
    assert (!expressionWildcardSet.contains(wildcard));

    LinkedList<AbstractBastNode> list = expressionWildcardMap.get(node);
    if (list == null) {
      list = new LinkedList<>();
      expressionWildcardMap.put(node, list);
    }
    list.add(wildcard);
    expressionWildcardSet.add(wildcard);
  }

  void updateReplacedStatement(AbstractBastNode node, AbstractBastNode wildcard,
      BastEditOperation ep) {
    replacedStatement.put(wildcard, node);
    wildcardEditOp.put(wildcard, ep);
    if (WildcardAccessHelper.isWildcard(node)) {
      wildcardReplaceMap.put(wildcard, node);
    }
  }

  void additionalWildcards(AbstractBastNode wildcard, BastEditOperation ep, boolean delInsW) {
    assert (!additionalWildcards.contains(wildcard));
    additionalWildcards.add(wildcard);
    wildcardEditOp.put(wildcard, ep);
    if (delInsW) {
      delInsWildcards.add(wildcard);
    }

  }

  boolean isExpressionWildcard(AbstractBastNode node) {
    if (expressionWildcardSet.contains(node)) {
      return true;
    }
    if (wildcardReplaceMap.get(node) != null
        && isExpressionWildcard(wildcardReplaceMap.get(node))) {
      return true;
    }
    return false;
  }

  boolean isAdditionalWildcard(AbstractBastNode node) {
    if (additionalWildcards.contains(node)) {
      return true;
    }
    if (wildcardReplaceMap.get(node) != null
        && isAdditionalWildcard(wildcardReplaceMap.get(node))) {
      return true;
    }
    return false;
  }

  boolean isDelInsWildcard(AbstractBastNode node) {
    if (delInsWildcards.contains(node)) {
      return true;
    }
    if (wildcardReplaceMap.get(node) != null && isDelInsWildcard(wildcardReplaceMap.get(node))) {
      return true;
    }
    return false;
  }

  boolean isInitialWildcard(AbstractBastNode node) {
    if (initialWildcards.contains(node)) {
      return true;
    }
    if (wildcardReplaceMap.get(node) != null && isInitialWildcard(wildcardReplaceMap.get(node))) {
      return true;
    }
    return false;
  }

  boolean isInsertWildcard(AbstractBastNode node) {
    if (replacedStatement.containsKey(node)
        && (wildcardEditOp.get(node).getType() == EditOperationType.INSERT
            || wildcardEditOp.get(node).getType() == EditOperationType.STATEMENT_INSERT)) {
      return true;
    }
    if (additionalWildcards.contains(node)
        && (wildcardEditOp.get(node).getType() == EditOperationType.INSERT
            || wildcardEditOp.get(node).getType() == EditOperationType.STATEMENT_INSERT)) {
      return true;
    }
    if (wildcardReplaceMap.get(node) != null && isInsertWildcard(wildcardReplaceMap.get(node))) {
      return true;
    }
    return false;
  }

  boolean isDeleteWildcard(AbstractBastNode node) {
    if (replacedStatement.containsKey(node)
        && (wildcardEditOp.get(node).getType() == EditOperationType.DELETE
            || wildcardEditOp.get(node).getType() == EditOperationType.STATEMENT_DELETE)) {
      return true;
    }
    if (additionalWildcards.contains(node)
        && (wildcardEditOp.get(node).getType() == EditOperationType.DELETE
            || wildcardEditOp.get(node).getType() == EditOperationType.STATEMENT_DELETE)) {
      return true;
    }
    if (wildcardReplaceMap.get(node) != null && isDeleteWildcard(wildcardReplaceMap.get(node))) {
      return true;
    }
    return false;
  }

  boolean isMoveWildcard(AbstractBastNode node) {
    if (replacedStatement.containsKey(node)
        && (wildcardEditOp.get(node).getType() == EditOperationType.MOVE
            || wildcardEditOp.get(node).getType() == EditOperationType.STATEMENT_REORDERING)) {
      return true;
    }
    if (wildcardReplaceMap.get(node) != null && isMoveWildcard(wildcardReplaceMap.get(node))) {
      return true;
    }
    return false;
  }

  boolean hasCorrespondingDelete(AbstractBastNode node, BastEditOperation epDel,
      ArrayList<BastEditOperation> workList, ExtendedDiffResult extDiff, MatchBoundary boundary) {
    if (!isInsertWildcard(node)) {
      return false;
    }

    BastEditOperation epIns = wildcardEditOp.get(node);
    if (WildcardAccessHelper.isEqual(epDel.getOldOrInsertedNode(), epIns.getOldOrInsertedNode())) {
      return true;
    }
    for (Entry<AbstractBastNode, BastEditOperation> ep : wildcardEditOp.entrySet()) {
      if (ep.getValue().getType() == EditOperationType.DELETE
          || ep.getValue().getType() == EditOperationType.STATEMENT_DELETE) {
        if (ep.getValue().getOldOrChangedIndex().childrenListIndex == epIns
            .getOldOrChangedIndex().childrenListIndex) {
          if (workList.contains(ep.getValue()) && workList.contains(epIns)) {
            return false;
          } else {
            return true;
          }
        }
      }
    }
    if (epDel.getType() == EditOperationType.DELETE
        || epDel.getType() == EditOperationType.STATEMENT_DELETE) {
      if (epDel.getOldOrChangedIndex().childrenListIndex == epIns
          .getOldOrChangedIndex().childrenListIndex) {
        return true;
      }
    }
    for (BastEditOperation epTmp : workList) {
      if (epTmp.getType() == EditOperationType.ALIGN
          || epTmp.getType() == EditOperationType.STATEMENT_REORDERING) {
        if (epDel.getUnchangedOrOldParentNode() == epTmp.getUnchangedOrOldParentNode()) {
          if (epDel.getOldOrChangedIndex().childrenListIndex == epTmp
              .getOldOrChangedIndex().childrenListIndex) {
            return true;
          }
        }
        if (epIns.getUnchangedOrOldParentNode() == extDiff.secondToFirstMap
            .get(epTmp.getUnchangedOrNewParentNode())
            || epIns.getUnchangedOrOldParentNode() == boundary.getNode2()
                && epTmp.getUnchangedOrOldParentNode() == boundary.getNode1()) {
          if (epIns.getOldOrChangedIndex().childrenListIndex == epTmp
              .getOldOrChangedIndex().childrenListIndex) {
            return true;
          }
        }
      }
    }
    return false;
  }
}
