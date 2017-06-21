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

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing the reordering of statements.
 *
 */
public final class StatementReorderingOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new statement reordering operation.
   *
   * @param op the op
   */
  public StatementReorderingOperation(final BastEditOperation op) {
    super(EditOperationType.STATEMENT_REORDERING, op);
    assert StatementReorderingOperation.describes(op);
  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final BastEditOperation op) {
    boolean oldPart =
        op.getOldOrChangedIndex().childrenListNumber == BastFieldConstants.BLOCK_STATEMENT
            || op.getOldOrChangedIndex().childrenListNumber == BastFieldConstants.IF_IF_PART
            || op.getOldOrChangedIndex().childrenListNumber == BastFieldConstants.IF_ELSE_PART
            || op.getOldOrChangedIndex().childrenListNumber == BastFieldConstants.WHILE_STATEMENT
        || op.getOldOrChangedIndex().childrenListNumber == BastFieldConstants.FOR_STMT_STATEMENT
        || op
            .getOldOrChangedIndex().childrenListNumber 
            == BastFieldConstants.ARES_USE_STMT_STATEMENT
        || op
            .getOldOrChangedIndex().childrenListNumber 
            == BastFieldConstants.ARES_WILDCARD_STATEMENTS
        || op
            .getOldOrChangedIndex().childrenListNumber 
            == BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS;
    boolean newPart =
        op.getNewOrChangedIndex().childrenListNumber == BastFieldConstants.BLOCK_STATEMENT
            || op.getNewOrChangedIndex().childrenListNumber == BastFieldConstants.IF_IF_PART
            || op.getNewOrChangedIndex().childrenListNumber == BastFieldConstants.IF_ELSE_PART
            || op.getNewOrChangedIndex().childrenListNumber == BastFieldConstants.WHILE_STATEMENT
        || op.getNewOrChangedIndex().childrenListNumber == BastFieldConstants.FOR_STMT_STATEMENT
        || op
            .getNewOrChangedIndex().childrenListNumber 
            == BastFieldConstants.ARES_USE_STMT_STATEMENT
        || op
            .getNewOrChangedIndex().childrenListNumber 
            == BastFieldConstants.ARES_WILDCARD_STATEMENTS
        || op
            .getNewOrChangedIndex().childrenListNumber 
            == BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS;
    return op.getType() == EditOperationType.ALIGN && oldPart && newPart;
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return "StatementReorderingOperation [operation=" + operation + "]";
  }
}
