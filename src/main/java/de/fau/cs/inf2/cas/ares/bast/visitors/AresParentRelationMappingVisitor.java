package de.fau.cs.inf2.cas.ares.bast.visitors;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;

/**
 * A visitor creating a map of parental relationships. In other words, this visitor creates a map,
 * with which it is possible to find the parent of any node contained in the visited tree.
 * 
 */
public class AresParentRelationMappingVisitor extends AresDefaultFieldVisitor {
  private Map<AbstractBastNode, AbstractBastNode> parentsMap;
  private ArrayDeque<AbstractBastNode> parentStack = new ArrayDeque<>();

  /**
   * todo.
   * 
   * <p>Return a map of parental relationships
   *
   * @param root the root
   * @return the map of parent relationships
   */
  public Map<AbstractBastNode, AbstractBastNode> getMapOfParentRelationships(
      final AbstractBastNode root) {
    parentsMap = new HashMap<>();
    parentStack.clear();
    if (root == null) {
      assert (false);
    }
    root.accept(this);

    return parentsMap;
  }

  private void addParent(AbstractBastNode node) {
    if (!parentStack.isEmpty()) {
      parentsMap.put(node, parentStack.peekFirst());
    }
  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    addParent(node);
    parentStack.push(node);

  }

  
  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {
    parentStack.pop();

  }

}
