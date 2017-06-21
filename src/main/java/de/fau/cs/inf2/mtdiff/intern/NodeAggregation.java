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

import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * A class representing a subtree, whose nodes have been linked together. It is used by
 * {@see OptimalTreeMatcher.tree.TreeMatcher}
 * for the implementation of the best-match strategy.
 *
 */
class NodeAggregation implements INode {

  static final int TAG = -1000;

  private INode subTree;

  /**
   * Instantiates a new node aggregation.
   *
   * @param subTree the sub tree
   * @param stringMap the string map
   */
  NodeAggregation(final INode subTree, IdentityHashMap<INode, String> stringMap) {
    setAssociatedTree(subTree, stringMap);
  }

  /**
   * Gets the associated tree.
   *
   * @return the associated tree
   */
  public INode getAssociatedTree() {
    return subTree;
  }

  /**
   * Sets the associated tree.
   *
   * @param subTree the sub tree
   * @param stringMap the string map
   */
  void setAssociatedTree(final INode subTree, IdentityHashMap<INode, String> stringMap) {

    if (subTree != null) {
      this.subTree = subTree;
      StringBuilder builder = new StringBuilder();
      LinkedList<INode> workList = new LinkedList<>();
      workList.add(subTree);
      while (!workList.isEmpty()) {
        INode node = workList.removeFirst();
        builder.append(node.getTypeWrapped() + node.getLabel());
        workList.addAll(node.getChildrenWrapped());
      }
      hash = builder.toString().hashCode();
    } else {
      throw new NullPointerException("The associated tree must not be null");
    }
  }

  private int hash = Integer.MIN_VALUE;

  /**
   * Gets the hash.
   *
   * @return the hash
   */
  public int getHash() {
    return hash;
  }

  /**
   * Gets the tag.
   *
   * @return the tag
   */
  public int getTag() {
    return TAG;
  }

  
  /**
   * Gets the type wrapped.
   *
   * @return the type wrapped
   */
  @Override
  public int getTypeWrapped() {
    return TAG;
  }

  
  /**
   * Gets the children wrapped.
   *
   * @return the children wrapped
   */
  @Override
  public List<INode> getChildrenWrapped() {
    return null;
  }

  
  /**
   * Gets the label.
   *
   * @return the label
   */
  @Override
  public String getLabel() {
    return null;
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return subTree.toString();
  }

  
  /**
   * Gets the id.
   *
   * @return the id
   */
  @Override
  public int getId() {
    return -1;
  }

  
  /**
   * Checks if is leaf.
   *
   * @return true, if is leaf
   */
  @Override
  public boolean isLeaf() {
    return false;
  }
}
