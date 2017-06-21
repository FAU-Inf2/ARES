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

package de.fau.cs.inf2.mtdiff.editscript.operations.inode;

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An {@see INodeEditOperation} representing an insertion.
 *
 */
public final class INodeInsertOperation extends INodeEditOperation {
  private INode parentNode;
  private INode insertedNode;
  private NodeIndex index;

  /**
   * Instantiates a new i node insert operation.
   */
  public INodeInsertOperation() {
    super(EditOperationType.INSERT);
  }

  /**
   * Instantiates a new i node insert operation.
   *
   * @param parentNode the parent node
   * @param insertedNode the inserted node
   * @param index the index
   */
  public INodeInsertOperation(final INode parentNode, final INode insertedNode,
      final NodeIndex index) {
    super(EditOperationType.INSERT);
    this.parentNode = parentNode;
    this.insertedNode = insertedNode;
    this.index = index;
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    return String.format("Inserted %s (node %s) in %s (index %s, node %s)", insertedNode.toString(),
        insertedNode.getClass().getName(), parentNode.toString(), index.toString(),
        parentNode.getClass().getName());
  }

  
  /**
   * Gets the new or changed node.
   *
   * @return the new or changed node
   */
  @Override
  public INode getNewOrChangedNode() {
    return (INode) insertedNode;
  }

  
  /**
   * Gets the unchanged or new parent node.
   *
   * @return the unchanged or new parent node
   */
  @Override
  public INode getUnchangedOrNewParentNode() {
    return (INode) parentNode;
  }

  
  /**
   * Gets the unchanged or old parent node.
   *
   * @return the unchanged or old parent node
   */
  @Override
  public INode getUnchangedOrOldParentNode() {
    return (INode) parentNode;
  }

  
  /**
   * Gets the old or inserted node.
   *
   * @return the old or inserted node
   */
  @Override
  public INode getOldOrInsertedNode() {
    return (INode) insertedNode;
  }

  
  /**
   * Gets the new or changed index.
   *
   * @return the new or changed index
   */
  @Override
  public NodeIndex getNewOrChangedIndex() {
    return index;
  }

  
  /**
   * Gets the old or changed index.
   *
   * @return the old or changed index
   */
  @Override
  public NodeIndex getOldOrChangedIndex() {
    return index;
  }
}
