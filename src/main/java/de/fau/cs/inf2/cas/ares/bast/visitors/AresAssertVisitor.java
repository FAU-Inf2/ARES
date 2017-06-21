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
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.visitors.AssertVisitor;


class AresAssertVisitor extends AssertVisitor implements IAresBastVisitor {

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresPatternClause node) {
    if (node.occurrence != null) {
      node.occurrence.accept(this);
    }
    if (node.expr != null) {
      node.expr.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresPluginClause node) {
    if (node.ident != null) {
      node.ident.accept(this);
    }
    if (node.exprList != null) {
      for (AbstractBastExpr expr : node.exprList) {
        expr.accept(this);
      }
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresUseStmt node) {
    AbstractBastNode pattern = node.getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField();

    if (pattern != null) {
      pattern.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresWildcard node) {
    if (node.pattern != null) {
      node.pattern.accept(this);
    }
    if (node.plugin != null) {
      node.plugin.accept(this);
    }
    if (node.statements != null) {
      for (AbstractBastStatement stmt : node.statements) {
        stmt.accept(this);
      }
    }
    assert (false);
  }
  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresBlock node) {
    if (node.identifiers != null) {
      for (AbstractBastExpr expr : node.identifiers) {
        expr.accept(this);
      }
    }
    if (node.block != null) {
      node.block.accept(this);
    }
    assert (false);
  }
  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresPlaceholder node) {
    assert (false);
  }
  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresChoiceStmt node) {
    if (node.choiceBlock != null) {
      node.choiceBlock.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresCaseStmt node) {
    if (node.block != null) {
      node.block.accept(this);
    }
    assert (false);
  }

}
