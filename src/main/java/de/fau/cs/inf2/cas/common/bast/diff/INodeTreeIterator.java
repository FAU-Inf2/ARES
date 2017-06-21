package de.fau.cs.inf2.cas.common.bast.diff;

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import java.util.Deque;
import java.util.Iterator;


public final class INodeTreeIterator implements Iterator<INode> {

  private final Deque<INode> nodes;

  /**
   * todo.
   * 
   * <p>Create a new iterator using the given traversal order.
   * 
   * @param order The order, in which the tree should be traversed.
   *
   * @param rootNode The node where the traversal should start
   */
  public INodeTreeIterator(final IterationOrder order, final INode rootNode) {
    final INodeTreeVisitor visitor = new INodeTreeVisitor(order);
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
   * @return the i node
   */
  public INode next() {
    return nodes.pollFirst();
  }

  
  /**
   * Removes the.
   */
  public void remove() {
    throw new UnsupportedOperationException();
  }
}
