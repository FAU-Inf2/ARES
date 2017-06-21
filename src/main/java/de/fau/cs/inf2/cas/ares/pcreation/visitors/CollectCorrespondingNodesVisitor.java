package de.fau.cs.inf2.cas.ares.pcreation.visitors;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.visitors.DefaultFieldVisitor;

import java.util.ArrayList;
import java.util.Map;

public class CollectCorrespondingNodesVisitor extends DefaultFieldVisitor {
  public ArrayList<AbstractBastNode> nodes = new ArrayList<>();
  private Map<AbstractBastNode, AbstractBastNode> nodeMap;

  /**
   * Instantiates a new collect corresponding nodes visitor.
   *
   * @param nodeMap the node map
   */
  public CollectCorrespondingNodesVisitor(Map<AbstractBastNode, AbstractBastNode> nodeMap) {
    this.nodeMap = nodeMap;
  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    AbstractBastNode nodeTmp = nodeMap.get(node);
    if (nodeTmp != null) {
      nodes.add(nodeTmp);
    }
    nodes.add(node);

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
