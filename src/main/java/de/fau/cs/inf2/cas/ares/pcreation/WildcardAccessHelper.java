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
import de.fau.cs.inf2.cas.ares.bast.visitors.NodeStreamVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.CreateJavaNodeHelper;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.visitors.FindFieldVisitor;
import de.fau.cs.inf2.cas.common.bast.visitors.FindPatternStartsVisitor;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.matching.LeavesSimilarityCalculator;
import de.fau.cs.inf2.mtdiff.matching.NotALeafException;
import de.fau.cs.inf2.mtdiff.matching.SimilarityAdapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class WildcardAccessHelper {

  private static boolean compareTokenStream(ArrayList<AbstractBastNode> wildExprTokens,
      ArrayList<AbstractBastNode> partTokens, boolean testEqual, boolean ignoreSimilarity) {
    if (testEqual) {
      if (wildExprTokens.size() != partTokens.size()) {
        return false;
      }
    }
    SimilarityAdapter leavesCalc = new SimilarityAdapter(new LeavesSimilarityCalculator());
    for (int i = 0; i < partTokens.size(); i++) {
      if (wildExprTokens.size() > i
          && partTokens.get(i).getTag() == wildExprTokens.get(i).getTag()) {
        switch (partTokens.get(i).getTag()) {
          case TagConstants.BAST_ACCESS:
            continue;
          default:
            if (!ignoreSimilarity) {
              float val = 0.0f;
              try {
                val = leavesCalc.similarity(partTokens.get(i), wildExprTokens.get(i));
              } catch (NotALeafException e) {
                continue;
              }
              if (val != 1.0f) {
                return false;
              }

            } else {
              continue;
            }

        }
      } else {
        return false;
      }
    }
    return true;
  }

  /**
   * Gets the expr.
   *
   * @param node the node
   * @return the expr
   */
  public static AbstractBastNode getExpr(AbstractBastNode node) {
    if (!isExprWildcard(node)) {
      return null;
    }
    if (node.getTag() == AresWildcard.TAG) {
      return (((AresPatternClause) ((AresWildcard) node).plugin.exprList.get(0))).expr;

    } else {
      return ((AresPatternClause) ((AresUseStmt) node)
          .getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField()).expr;
    }

  }

  static AbstractBastNode getExpressionNode(AbstractBastNode wildcard, AbstractBastNode ast) {
    if (!isExprWildcard(wildcard)) {
      return null;
    }
    FindFieldVisitor ffv = new FindFieldVisitor(wildcard);
    ast.accept(ffv);
    if (ffv.found) {
      @SuppressWarnings("unchecked")
      List<AbstractBastNode> nodes =
          (List<AbstractBastNode>) ffv.foundParent.getField(ffv.foundConstant).getListField();
      for (int i = ffv.foundListId + 1; i < nodes.size(); i++) {
        if (!isExprWildcard(nodes.get(i))) {
          return nodes.get(i);
        }
      }
    }
    return null;
  }


  /**
   * Checks for equal name.
   *
   * @param first the first
   * @param second the second
   * @return true, if successful
   */
  public static boolean isNameEqual(AbstractBastNode first, AbstractBastNode second) {
    BastNameIdent nameFirst = getName(first);
    BastNameIdent nameSecond = getName(second);
    if (nameFirst == null || nameSecond == null) {
      return false;
    }
    return nameFirst.name.equals(nameSecond.name);
  }

  /**
   * Gets the name.
   *
   * @param node the node
   * @return the name
   */
  public static BastNameIdent getName(AbstractBastNode node) {
    if (node.getTag() == AresWildcard.TAG) {
      if (((AresWildcard) node).plugin != null && ((AresWildcard) node).plugin.exprList != null
          && ((AresWildcard) node).plugin.exprList.size() > 0
          && ((AresWildcard) node).plugin.exprList.get(0).getTag() == AresPatternClause.TAG
          && (((AresPatternClause) ((AresWildcard) node).plugin.exprList.get(0)).ident != null)) {
        return ((AresPatternClause) ((AresWildcard) node).plugin.exprList.get(0)).ident;

      }
    } else if (node.getTag() == AresUseStmt.TAG) {
      AresPatternClause pattern = (AresPatternClause) ((AresUseStmt) node)
          .getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField();
      if (pattern != null) {
        if (pattern.ident != null) {
          return pattern.ident;
        }
      }
    }
    return null;
  }

  /**
   * Gets the node to index.
   *
   * @param node the node
   * @param childrenListNumber the children list number
   * @param childrenListIndex the children list index
   * @return the node to index
   */
  public static AbstractBastNode getNodeToIndex(AbstractBastNode node,
      BastFieldConstants childrenListNumber, int childrenListIndex) {
    if (node == null) {
      return null;
    }
    if (node.getField(childrenListNumber) != null) {
      if (node.getField(childrenListNumber).isList()) {
        if (node.getField(childrenListNumber).getListField() != null
            && node.getField(childrenListNumber).getListField().size() > childrenListIndex) {
          return node.getField(childrenListNumber).getListField().get(childrenListIndex);
        }
      } else {
        return node.getField(childrenListNumber).getField();
      }
    }
    return null;
  }

  /**
   * Gets the node to index.
   *
   * @param node the node
   * @param index the index
   * @return the node to index
   */
  public static AbstractBastNode getNodeToIndex(AbstractBastNode node, NodeIndex index) {
    if (index == null) {
      return null;
    }
    return getNodeToIndex(node, index.childrenListNumber, index.childrenListIndex);
  }

  /**
   * Gets the occurence.
   *
   * @param node the node
   * @return the occurence
   */
  public static BastIntConst getOccurence(AbstractBastNode node) {
    if (!isExprWildcard(node)) {
      return null;
    }
    if (node.getTag() == AresWildcard.TAG) {
      return (((AresPatternClause) ((AresWildcard) node).plugin.exprList.get(0))).occurrence;

    } else {
      AresPatternClause pattern = (AresPatternClause) ((AresUseStmt) node)
          .getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField();
      return pattern.occurrence;
    }

  }

  /**
   * Checks if is equal.
   *
   * @param first the first
   * @param second the second
   * @return true, if is equal
   */
  public static boolean isEqual(AbstractBastNode first, AbstractBastNode second) {
    return isEqual(first, second, false);
  }

  /**
   * Checks if is equal.
   *
   * @param first the first
   * @param second the second
   * @param ignoreSimilarity the ignore similarity
   * @return true, if is equal
   */
  public static boolean isEqual(AbstractBastNode first, AbstractBastNode second,
      boolean ignoreSimilarity) {
    if (first == null && second == null) {
      return true;
    }
    if (first == null) {
      return false;
    }
    if (second == null) {
      return false;
    }
    if (first.getTag() != second.getTag()) {
      return false;
    }

    NodeStreamVisitor firstTokens = new NodeStreamVisitor(first);
    first.accept(firstTokens);

    NodeStreamVisitor secondTokens = new NodeStreamVisitor(second);
    second.accept(secondTokens);
    return compareTokenStream(firstTokens.nodes, secondTokens.nodes, true, ignoreSimilarity);
  }

  /**
   * Checks if is equal.
   *
   * @param firstList the first list
   * @param secondList the second list
   * @return true, if is equal
   */
  public static boolean isEqual(List<AbstractBastNode> firstList,
      List<AbstractBastNode> secondList) {
    if (firstList == null && secondList == null) {
      return true;
    }
    if (firstList == null) {
      return false;
    }
    if (secondList == null) {
      return false;
    }
    if (firstList.size() != secondList.size()) {
      return false;
    }
    for (int i = 0; i < firstList.size(); i++) {
      if (!isEqual(firstList.get(i), secondList.get(i))) {
        return false;
      }
    }
    return true;

  }

  /**
   * Checks if is expr wildcard.
   *
   * @param node the node
   * @return true, if is expr wildcard
   */
  public static boolean isExprWildcard(AbstractBastNode node) {
    if (node.getTag() == AresWildcard.TAG) {
      if (((AresWildcard) node).plugin != null && ((AresWildcard) node).plugin.exprList != null
          && ((AresWildcard) node).plugin.exprList.size() > 0
          && ((AresWildcard) node).plugin.exprList.get(0).getTag() == AresPatternClause.TAG
          && (((AresPatternClause) ((AresWildcard) node).plugin.exprList.get(0)).expr != null)) {
        return true;

      }
    } else if (node.getTag() == AresUseStmt.TAG) {
      if (((AresUseStmt) node).getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField() != null
          && ((AresPatternClause) ((AresUseStmt) node)
              .getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField()).expr != null) {
        return true;
      }
    }
    return false;

  }

  /**
   * Checks if is part.
   *
   * @param stmt the stmt
   * @param part the part
   * @return true, if is part
   */
  public static boolean isPart(AbstractBastNode stmt, AbstractBastNode part) {
    return isPart(stmt, part, false);
  }

  /**
   * Checks if is part.
   *
   * @param stmt the stmt
   * @param wildcard the wildcard
   * @param partWildcard the part wildcard
   * @return true, if is part
   */
  public static boolean isPart(AbstractBastNode stmt, AbstractBastNode wildcard,
      AbstractBastNode partWildcard) {
    if (isWildcard(stmt)) {
      return false;
    }
    if (!isExprWildcard(wildcard)) {
      return false;
    }
    if (!isExprWildcard(partWildcard)) {
      return false;
    }
    if (WildcardAccessHelper.getOccurence(wildcard).value > WildcardAccessHelper
        .getOccurence(partWildcard).value) {
      return false;
    }
    LinkedList<AbstractBastNode> list = new LinkedList<AbstractBastNode>();
    list.add(WildcardAccessHelper.getExpr(partWildcard));
    FindPatternStartsVisitor starts = new FindPatternStartsVisitor(list, true);
    WildcardAccessHelper.getExpr(wildcard).accept(starts);
    int countFounds = 0;
    for (int i = 0; i < starts.starts.size(); i++) {
      NodeStreamVisitor wildExprTokens = new NodeStreamVisitor(starts.starts.get(i));
      WildcardAccessHelper.getExpr(wildcard).accept(wildExprTokens);
      NodeStreamVisitor partTokens =
          new NodeStreamVisitor(WildcardAccessHelper.getExpr(partWildcard));
      WildcardAccessHelper.getExpr(partWildcard).accept(partTokens);
      boolean containsPart =
          compareTokenStream(wildExprTokens.nodes, partTokens.nodes, false, false);
      if (containsPart) {
        countFounds++;
      }

    }
    if (countFounds > 0 && countFounds == WildcardAccessHelper.getOccurence(partWildcard).value) {
      return true;
    }
    return false;
  }

  /**
   * Checks if is part.
   *
   * @param stmt the stmt
   * @param part the part
   * @param ignoreSimilarity the ignore similarity
   * @return true, if is part
   */
  public static boolean isPart(AbstractBastNode stmt, AbstractBastNode part,
      boolean ignoreSimilarity) {
    LinkedList<AbstractBastNode> list = new LinkedList<AbstractBastNode>();
    if (part == null) {
      return false;
    }
    list.add(part);
    FindPatternStartsVisitor starts = new FindPatternStartsVisitor(list, true);
    stmt.accept(starts);
    int countFounds = 0;
    for (int i = 0; i < starts.starts.size(); i++) {
      NodeStreamVisitor wildExprTokens = new NodeStreamVisitor(starts.starts.get(i));
      stmt.accept(wildExprTokens);
      NodeStreamVisitor partTokens = new NodeStreamVisitor(part);
      part.accept(partTokens);
      boolean containsPart =
          compareTokenStream(wildExprTokens.nodes, partTokens.nodes, false, ignoreSimilarity);
      if (containsPart) {
        countFounds++;
      }

    }
    if (countFounds > 0) {
      return true;
    }
    return false;
  }



  /**
   * Checks if is wildcard.
   *
   * @param node the node
   * @return true, if is wildcard
   */
  public static boolean isWildcard(AbstractBastNode node) {
    if (node == null) {
      return false;
    }
    if (node.getTag() == AresWildcard.TAG || node.getTag() == AresUseStmt.TAG) {
      return true;
    }
    return false;
  }

  /**
   * Contains node with tag.
   *
   * @param node the node
   * @param tag the tag
   * @return true, if successful
   */
  public static boolean hasChildWithTag(AbstractBastNode node, int tag) {
    NodeStreamVisitor nodeTokens = new NodeStreamVisitor(node);
    node.accept(nodeTokens);
    for (AbstractBastNode nodeInList : nodeTokens.nodes) {
      if (nodeInList.getTag() == tag) {
        return true;
      }
    }
    return false;
  }

  static boolean isWildcardPart(AbstractBastNode wildcard, AbstractBastNode part) {
    if (!isExprWildcard(wildcard)) {
      return false;
    }
    LinkedList<AbstractBastNode> list = new LinkedList<AbstractBastNode>();
    list.add(part);
    FindPatternStartsVisitor starts = new FindPatternStartsVisitor(list, true);
    WildcardAccessHelper.getExpr(wildcard).accept(starts);
    int countFounds = 0;
    for (int i = 0; i < starts.starts.size(); i++) {
      NodeStreamVisitor wildExprTokens = new NodeStreamVisitor(starts.starts.get(i));
      WildcardAccessHelper.getExpr(wildcard).accept(wildExprTokens);
      NodeStreamVisitor partTokens = new NodeStreamVisitor(part);
      part.accept(partTokens);
      boolean containsPart =
          compareTokenStream(wildExprTokens.nodes, partTokens.nodes, false, false);
      if (containsPart) {
        countFounds++;
      }

    }
    if (countFounds > 0) {
      return true;
    }
    return false;
  }

  /**
   * Sets the name.
   *
   * @param wildcard the wildcard
   * @param node the node
   */
  public static void setName(AbstractBastNode wildcard, BastNameIdent node) {
    if (wildcard.getTag() == AresWildcard.TAG) {
      if (((AresWildcard) wildcard).plugin == null) {
        assert (false);
      }
      if (((AresWildcard) wildcard).plugin.exprList == null) {
        ((AresWildcard) wildcard).plugin.exprList = new LinkedList<AbstractBastExpr>();
      }
      if (((AresWildcard) wildcard).plugin.exprList.size() == 0) {
        ((AresWildcard) wildcard).plugin.exprList
            .add(CreateJavaNodeHelper.createAresPatternClause(null, null, null, node, false));
      } else {
        ((AresPatternClause) ((AresWildcard) wildcard).plugin.exprList.get(0)).ident = node;
      }

    } else if (wildcard.getTag() == AresUseStmt.TAG) {
      AresPatternClause pattern = (AresPatternClause) ((AresUseStmt) wildcard)
          .getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField();
      pattern.ident = node;
    }
  }
}
