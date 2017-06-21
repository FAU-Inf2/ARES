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
import de.fau.cs.inf2.cas.common.bast.visitors.StrippedJavaPrinter;

import java.util.HashMap;

@SuppressWarnings("ucd")
public class AresStrippedJavaPrinter extends StrippedJavaPrinter implements IAresBastVisitor {

  /**
   * Instantiates a new stripped java printer.
   *
   * @param startLines the start lines
   * @param endLines the end lines
   * @param startPos the start pos
   */
  public AresStrippedJavaPrinter(HashMap<AbstractBastNode, Integer> startLines,
      HashMap<AbstractBastNode, Integer> endLines, HashMap<AbstractBastNode, Integer> startPos) {
    super(startLines, endLines, startPos);
  }

  /**
   * Instantiates a new stripped java printer.
   */
  public AresStrippedJavaPrinter() {

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
  public void visit(AresPatternClause node) {
    setStartLine(node);

    addTokenData(node, 0);
    addTokenData(node, 1);
    if (node.occurrence != null) {
      node.occurrence.accept(this);
    }
    addTokenData(node, 2);
    if (node.expr != null) {
      node.expr.accept(this);
    }
    addTokenData(node, 3);
    if (node.ident != null) {
      node.ident.accept(this);
    }
    addTokenData(node, 4);
    setEndLine(node);

  }
  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresPluginClause node) {
    setStartLine(node);

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
    setEndLine(node);

  }

  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresWildcard node) {
    setStartLine(node);

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
    setEndLine(node);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresBlock node) {
    setStartLine(node);

    addTokenData(node, 0);
    addTokenData(node, 1);
    addTokenData(node, 2);
    addTokenData(node, 3);
    if (node.numberNode != null) {
      addTokenData(node, 4);
      node.numberNode.accept(this);

    }
    addTokenData(node, 5);
    if (node.block != null) {
      node.block.accept(this);
    }
    setEndLine(node);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresUseStmt node) {
    setStartLine(node);

    addTokenData(node, 0);
    addTokenData(node, 1);
    addTokenData(node, 2);
    AbstractBastNode pattern = node.getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField();
    if (pattern != null) {
      addTokenData(node, 3);
      pattern.accept(this);
    }
    addTokenData(node, 4);

    addTokenData(node, 5);
    setEndLine(node);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresChoiceStmt node) {
    setStartLine(node);

    addTokenData(node, 0);
    addTokenData(node, 1);
    addTokenData(node, 2);
    addTokenData(node, 3);
    addTokenData(node, 4);

    if (node.choiceBlock != null) {
      node.choiceBlock.accept(this);
    }
    addTokenData(node, 5);
    setEndLine(node);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresCaseStmt node) {
    setStartLine(node);

    addTokenData(node, 0);
    addTokenData(node, 1);
    addTokenData(node, 2);

    if (node.block != null) {
      node.block.accept(this);
    }
    addTokenData(node, 3);
    setEndLine(node);

  }

}
