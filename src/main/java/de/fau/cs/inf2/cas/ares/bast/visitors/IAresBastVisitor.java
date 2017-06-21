package de.fau.cs.inf2.cas.ares.bast.visitors;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresCaseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPlaceholder;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPluginClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;

import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

public interface IAresBastVisitor extends IBastVisitor {

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresBlock node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresCaseStmt node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresChoiceStmt node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresPatternClause node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresPlaceholder node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresPluginClause node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresUseStmt node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresWildcard node);

}
