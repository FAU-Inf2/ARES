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

package de.fau.cs.inf2.cas.common.bast.visitors;

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
public class ParentRelationMappingVisitor extends DefaultFieldVisitor {
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
