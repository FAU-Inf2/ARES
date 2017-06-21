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
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.visitors.EnlightenedJavaPrinter;

public class AresEnlightenedJavaPrinter 
    extends EnlightenedJavaPrinter implements IAresBastVisitor {

  /**
   * Instantiates a new enlightened java printer.
   */
  public AresEnlightenedJavaPrinter() {

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresPlaceholder node) {

  }
  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresPluginClause node) {
    addTokenData(node, 0);
    addTokenData(node, 1);
    if (node.ident != null) {
      node.ident.accept(this);
    }
    addTokenData(node, 2);
    if (node.exprList != null) {
      for (AbstractBastExpr expr : node.exprList) {
        expr.accept(this);
      }
    }
    addTokenData(node, 3);
  }

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresPatternClause node) {
    addTokenData(node, 0);
    addTokenData(node, 1);
    if (node.ident != null) {
      node.ident.accept(this);
    }
    
    addTokenData(node, 2);
    if (node.expr != null) {
      node.expr.accept(this);
    }
    addTokenData(node, 3);
    if (node.occurrence != null) {
      node.occurrence.accept(this);
    }
    addTokenData(node, 4);
  }
  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresWildcard node) {
    addTokenData(node, 0);
    addTokenData(node, 1);
    if (node.plugin != null) {
      node.plugin.accept(this);
    }
    if (node.statements != null) {
      for (AbstractBastStatement stmt : node.statements) {
        stmt.accept(this);
      }
    } else {
      addTokenData(node, 2);
    }
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresChoiceStmt node) {
    addTokenData(node, 0);
    addTokenData(node, 1);
    addTokenData(node, 2);
    addTokenData(node, 3);
    addTokenData(node, 4);

    if (node.choiceBlock != null) {
      node.choiceBlock.accept(this);
    }
    addTokenData(node, 5);
    addTokenData(node, 6);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresCaseStmt node) {
    addTokenData(node, 0);
    addTokenData(node, 1);

    if (node.block != null) {
      node.block.accept(this);
    }
    addTokenData(node, 2);
    addTokenData(node, 3);

  }

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
      default:
        assert (false);
    }
  }
  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresBlock node) {
    addAllTokens(node, true);
    addTokenData(node, 0);
    addTokenData(node, 1);
    addTokenData(node, 2);
    addTokenData(node, 3);
    if (node.numberNode != null) {
      addTokenData(node, 4);
      node.numberNode.accept(this);

    }
    if (node.identifiers != null) {
      addTokenData(node, 5);
      addTokenData(node, 6);
      int count = -1;
      for (AbstractBastStatement stmt : node.identifiers) {
        addTokenData(node, 7, count);
        count++;
        stmt.accept(this);
      }
      addTokenData(node, 8);
    }
    addTokenData(node, 9);

    node.block.accept(this);
    addAllTokens(node, false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresUseStmt node) {
    addTokenData(node, 0);
    addTokenData(node, 1);
    addTokenData(node, 2);
    if (node.getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField() != null) {
      addTokenData(node, 3);
      node.getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField().accept(this);
    }
    addTokenData(node, 4);

    addTokenData(node, 5);
    addTokenData(node, 6);
  }

}
