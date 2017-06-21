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

package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An advanced edit operation, i.e. an edit operation that is a more practical representation of a
 * change than the raw tree edit operations.
 *
 */
public abstract class AdvancedEditOperation extends BastEditOperation {
  final BastEditOperation operation;

  AdvancedEditOperation(final EditOperationType type, BastEditOperation operation) {
    super(type);
    this.operation = operation;
  }

  /**
   * Gets the basic operation.
   *
   * @return the basic operation
   */
  public BastEditOperation getBasicOperation() {
    return this.operation;
  }

  
  /**
   * Gets the new or changed node.
   *
   * @return the new or changed node
   */
  public AbstractBastNode getNewOrChangedNode() {
    return operation.getNewOrChangedNode();
  }

  
  /**
   * Gets the unchanged or new parent node.
   *
   * @return the unchanged or new parent node
   */
  public AbstractBastNode getUnchangedOrNewParentNode() {
    return operation.getUnchangedOrNewParentNode();
  }

  
  /**
   * Gets the unchanged or old parent node.
   *
   * @return the unchanged or old parent node
   */
  public AbstractBastNode getUnchangedOrOldParentNode() {
    return operation.getUnchangedOrOldParentNode();
  }

  
  /**
   * Gets the old or inserted node.
   *
   * @return the old or inserted node
   */
  public AbstractBastNode getOldOrInsertedNode() {
    return operation.getOldOrInsertedNode();
  }

  
  /**
   * Gets the new or changed index.
   *
   * @return the new or changed index
   */
  public NodeIndex getNewOrChangedIndex() {
    return operation.getNewOrChangedIndex();
  }

  
  /**
   * Gets the old or changed index.
   *
   * @return the old or changed index
   */
  public NodeIndex getOldOrChangedIndex() {
    return operation.getOldOrChangedIndex();
  }

}
