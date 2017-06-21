package de.fau.cs.inf2.cas.common.bast.visitors;

import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.ArrayList;

public class FindNodesFromTagVisitor extends AresDefaultFieldVisitor {

  private int tag = -1;

  public ArrayList<AbstractBastNode> nodes = new ArrayList<>();

  /**
   * Instantiates a new find nodes from tag visitor.
   *
   * @param tag the tag
   */
  public FindNodesFromTagVisitor(int tag) {
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

  
  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {}

}
