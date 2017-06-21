package de.fau.cs.inf2.cas.ares.pcreation.visitors;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.ArrayList;

public class FindNodesFromTagWithoutChoiceVisitor extends AresDefaultFieldVisitor {

  private int tag = -1;

  public ArrayList<AbstractBastNode> nodes = new ArrayList<>();

  /**
   * Instantiates a new find nodes from tag visitor.
   *
   * @param tag the tag
   */
  public FindNodesFromTagWithoutChoiceVisitor(int tag) {
    this.tag = tag;
  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    if (node.getTag() == tag) {
      nodes.add(node);
    }
  }

  @Override
  public void visit(AresChoiceStmt node) {
    
  }
  
  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {}

}
