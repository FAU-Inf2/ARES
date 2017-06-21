package de.fau.cs.inf2.cas.ares.pcreation.visitors;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.LinkedList;

public class RemoveLostWildcardNamesVisitor extends AresDefaultFieldVisitor {
  private LinkedList<String> wildcardNames = new LinkedList<String>();

  /**
   * Instantiates a new removes the lost wildcard names visitor.
   */
  public RemoveLostWildcardNamesVisitor() {}

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresWildcard node) {
    if (WildcardAccessHelper.getName(node) != null) {
      wildcardNames.add(WildcardAccessHelper.getName(node).name);
    }
    super.visit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresUseStmt node) {
    if (WildcardAccessHelper.getName(node) != null) {
      if (!wildcardNames.contains(WildcardAccessHelper.getName(node).name)) {
        WildcardAccessHelper.setName(node, null);
      }

    }
    super.visit(node);
  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {

  }

  
  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {

  }
}
