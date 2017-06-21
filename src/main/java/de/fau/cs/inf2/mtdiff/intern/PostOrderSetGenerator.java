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

package de.fau.cs.inf2.mtdiff.intern;

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

public class PostOrderSetGenerator {
  private Set<INode> nodeSet;
  private Set<INode> leafSet;

  private IdentityHashMap<INode, ArrayList<INode>> leaves;
  private IdentityHashMap<INode, ArrayList<INode>> directChildren;

  private LinkedList<INode> parentNodeStack;

  /**
   * todo.
   * 
   * <p>Return a map of node to leaves by this visitor
   *
   * @return the leave map
   */
  public IdentityHashMap<INode, ArrayList<INode>> getLeaveMap() {
    return leaves;
  }

  /**
   * todo.
   * 
   * <p>Return a map of node to direct children by this visitor
   *
   * @return the direct children map
   */
  public IdentityHashMap<INode, ArrayList<INode>> getDirectChildrenMap() {
    return directChildren;
  }

  /**
   * todo.
   * 
   * <p>Return a list of all nodes visited by this visitor
   *
   * @return the sets the of nodes
   */
  public Set<INode> getSetOfNodes() {
    return nodeSet;
  }

  /**
   * todo.
   * 
   * <p>Return a list of all leaves visited by this visitor
   *
   * @return the sets the of leaves
   */
  public Set<INode> getSetOfLeaves() {
    return leafSet;
  }

  /**
   * todo.
   * 
   * <p>Generates sets for a given root node.
   *
   * @param root the root
   */
  public void createSetsForNode(final INode root) {
    nodeSet = new LinkedHashSet<INode>();
    leafSet = new LinkedHashSet<INode>();
    leaves = new IdentityHashMap<>();
    directChildren = new IdentityHashMap<>();
    parentNodeStack = new LinkedList<>();
    visit(root);
  }

  private Stack<Integer> sizeStack = new Stack<>();

  private void visit(INode node) {
    sizeStack.push(nodeSet.size());
    ArrayList<INode> tmp = new ArrayList<>();
    leaves.put(node, tmp);
    parentNodeStack.push(node);
    tmp = new ArrayList<>();
    directChildren.put(node, tmp);
    tmp.addAll(node.getChildrenWrapped());
    for (INode child : node.getChildrenWrapped()) {
      visit(child);
    }
    int size = sizeStack.pop();
    if (size == nodeSet.size()) {
      leafSet.add(node);
      for (INode parentNode : parentNodeStack) {
        if (parentNode != node) {
          ArrayList<INode> list = leaves.get(parentNode);
          assert (list != null);
          list.add(node);
        }
      }
    }
    nodeSet.add(node);
    parentNodeStack.pop();
  }
}
