package de.fau.cs.inf2.cas.ares.recommendation.visitors;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresPlaceholder;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastLineComment;

import java.util.LinkedList;

public class IsomorphLinearizationVisitor extends AresDefaultFieldVisitor {
  public LinkedList<AbstractBastNode> list = new LinkedList<AbstractBastNode>();

  private void addNode(AbstractBastNode node) {
    list.add(node);
  }

  /**
   * Gets the node list.
   *
   * @return the node list
   */
  public LinkedList<AbstractBastNode> getNodeList() {
    return list;
  }

  private boolean addNode = true;

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresWildcard node) {
    addNode(node);
    if (node.statements != null) {
      for (AbstractBastStatement stmt : node.statements) {
        stmt.accept(this);
      }
    }
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
  @Override
  public void visit(BastLineComment node) {
  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    if (addNode) {
      addNode(node);
    } else {
      addNode = true;
    }

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
