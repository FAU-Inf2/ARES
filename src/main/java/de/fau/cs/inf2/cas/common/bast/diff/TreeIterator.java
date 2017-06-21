package de.fau.cs.inf2.cas.common.bast.diff;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.Deque;
import java.util.Iterator;

/**
 * An iterator implementation for abstract syntax trees. Provides three different traversal methods:
 * Breadth-first, pre-order and post-order traversal.
 * 
 */
public final class TreeIterator implements Iterator<AbstractBastNode> {
  private final Deque<AbstractBastNode> nodes;

  /**
   * todo.
   * 
   * <p>Create a new iterator using the given traversal order.
   * 
   * @param order The order, in which the tree should be traversed.
   *
   * @param rootNode The node where the traversal should start
   */
  public TreeIterator(final IterationOrder order, final AbstractBastNode rootNode) {
    final TreeIterationNewVisitor visitor = new TreeIterationNewVisitor(order);
    nodes = visitor.getNodes(rootNode);
  }

  
  /**
   * Checks for next.
   *
   * @return true, if successful
   */
  public boolean hasNext() {
    return !nodes.isEmpty();
  }

  
  /**
   * Next.
   *
   * @return the abstract bast node
   */
  public AbstractBastNode next() {
    return nodes.pollFirst();
  }

  
  /**
   * Removes the.
   */
  public void remove() {
    throw new UnsupportedOperationException();
  }
}
