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

package de.fau.cs.inf2.cas.ares.bast.visitors;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresCaseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPlaceholder;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPluginClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractAresStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.visitors.DefaultFieldVisitor;

public abstract class AresDefaultFieldVisitor 
    extends DefaultFieldVisitor implements IAresBastVisitor {

  /**
   * Begin visit.
   *
   * @param node the node
   */
  public abstract void beginVisit(AbstractBastNode node);
  
  /**
   * End visit.
   *
   * @param node the node
   */
  public abstract void endVisit(AbstractBastNode node);
  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AbstractAresStatement node) {
    switch (node.getTag()) {
      case AresBlock.TAG:
        visit((AresBlock)node);
        break;
      case AresCaseStmt.TAG:
        visit((AresCaseStmt)node);
        break;
      case AresChoiceStmt.TAG:
        visit((AresChoiceStmt)node);
        break;
      case AresUseStmt.TAG:
        visit((AresUseStmt)node);
        break;
      case AresWildcard.TAG:
        visit((AresWildcard)node);
        break;
      case AresPatternClause.TAG:
        visit((AresPatternClause)node);
        break;
      case AresPluginClause.TAG:
        visit((AresPluginClause)node);
        break;
      default:
        assert (false);
    }
  }
  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresBlock node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ARES_BLOCK_NUMBER, node);
    standardVisit(BastFieldConstants.ARES_BLOCK_IDENTIFIERS, node);
    standardVisit(BastFieldConstants.ARES_BLOCK_BLOCK, node);
    endVisit(node);
  }

  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresCaseStmt node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ARES_CASE_STMT_BLOCK, node);
    endVisit(node);
  }

  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresChoiceStmt node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ARES_CHOICE_STMT_BLOCK, node);
    endVisit(node);
  }


  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresPatternClause node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ARES_PATTERN_CLAUSE_OCCURENCE, node);
    standardVisit(BastFieldConstants.ARES_PATTERN_CLAUSE_EXPR, node);
    standardVisit(BastFieldConstants.ARES_PATTERN_CLAUSE_IDENT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresPlaceholder node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresPluginClause node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ARES_PLUGIN_CLAUSE_IDENT, node);
    standardVisit(BastFieldConstants.ARES_PLUGIN_CLAUSE_EXPR_LIST, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresUseStmt node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ARES_USE_STMT_IDENT, node);
    standardVisit(BastFieldConstants.ARES_USE_STMT_PATTERN, node);
    standardVisit(BastFieldConstants.ARES_USE_STMT_STATEMENT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresWildcard node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ARES_WILDCARD_PATTERN, node);
    standardVisit(BastFieldConstants.ARES_WILDCARD_PLUGIN, node);
    standardVisit(BastFieldConstants.ARES_WILDCARD_DECLARE, node);
    standardVisit(BastFieldConstants.ARES_WILDCARD_STATEMENTS, node);
    endVisit(node);
  }

}
