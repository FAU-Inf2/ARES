package de.fau.cs.inf2.cas.ares.recommendation.visitors;

import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.HashMap;

public class CountNodesVisitor extends AresDefaultFieldVisitor {
  private HashMap<Integer, Integer> nodeNumber = new HashMap<Integer, Integer>();
  private int counter = 0;
  

  /**
   * Instantiates a new count nodes visitor.
   */
  public CountNodesVisitor() {

  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    nodeNumber.put(node.nodeId, counter++);
  }

  
  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {
    // nothing to do

  }

}
