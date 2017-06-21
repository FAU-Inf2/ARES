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
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeQualifier;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.DeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;

/**
 * An edit operation describing a change which increases the accessibility of a member of a class.
 *
 */
public final class IncreasingAccessibilityOperation extends AdvancedEditOperation {

  /**
   * todo.
   * 
   * <p>Create a new IncreasingAccessibilityOperation, where a single
   * type qualifier has been inserted.
   * This may be used in cases when a class member had package
   * local access and was modified to have
   * public access.
   *
   * @param op the op
   */
  public IncreasingAccessibilityOperation(final BastEditOperation op) {
    super(EditOperationType.INCREASING_ACCESS, op);
  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final InsertOperation op) {
    final AbstractBastNode node = op.getOldOrInsertedNode();

    return (node.getTag() == BastTypeQualifier.TAG) && isPublic((BastTypeQualifier) node);
  }

  /**
   * Describes.
   *
   * @param delOp the del op
   * @param insOp the ins op
   * @return true, if successful
   */
  public static boolean describes(final DeleteOperation delOp, final InsertOperation insOp) {
    final AbstractBastNode deleted = delOp.getOldOrInsertedNode();
    final AbstractBastNode inserted = insOp.getOldOrInsertedNode();

    return (deleted.getTag() == BastTypeQualifier.TAG)
        && (inserted.getTag() == BastTypeQualifier.TAG)
        && (isPrivate((BastTypeQualifier) deleted) || isProtected((BastTypeQualifier) deleted))
        && (isProtected((BastTypeQualifier) inserted) || isPublic((BastTypeQualifier) inserted))
        && (((BastTypeQualifier) deleted).type > ((BastTypeQualifier) inserted).type);
  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final DeleteOperation op) {
    final AbstractBastNode node = op.getOldOrInsertedNode();

    return (node.getTag() == BastTypeQualifier.TAG)
        && (isPrivate((BastTypeQualifier) node) || isProtected((BastTypeQualifier) node));
  }

  private static boolean isPrivate(final BastTypeQualifier qual) {
    return qual.type == BastTypeQualifier.TYPE_PRIVATE;
  }

  private static boolean isProtected(final BastTypeQualifier qual) {
    return qual.type == BastTypeQualifier.TYPE_PROTECTED;
  }

  private static boolean isPublic(final BastTypeQualifier qual) {
    return qual.type == BastTypeQualifier.TYPE_PUBLIC;
  }

}
