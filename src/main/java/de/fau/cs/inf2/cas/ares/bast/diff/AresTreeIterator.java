/*
 * Copyright (c) 2017 Programming Systems Group, CS Department, FAU
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */

package de.fau.cs.inf2.cas.ares.bast.diff;

import de.fau.cs.inf2.cas.common.bast.diff.IterationOrder;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.Deque;
import java.util.Iterator;

/**
 * An iterator implementation for abstract syntax trees. Provides three different traversal methods:
 * Breadth-first, pre-order and post-order traversal.
 * 
 */
public final class AresTreeIterator implements Iterator<AbstractBastNode> {

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
  public AresTreeIterator(final IterationOrder order, final AbstractBastNode rootNode) {
    final AresTreeIterationNewVisitor visitor = new AresTreeIterationNewVisitor(order);
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
