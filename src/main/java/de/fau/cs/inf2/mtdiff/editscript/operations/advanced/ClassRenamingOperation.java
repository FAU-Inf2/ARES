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

import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.UpdateOperation;

/**
 * An edit operation describing a renamed class definition.
 *
 */
public final class ClassRenamingOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new class renaming operation.
   *
   * @param op the op
   */
  public ClassRenamingOperation(BastEditOperation op) {
    super(EditOperationType.CLASS_RENAME, op);

  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final UpdateOperation op) {
    return (op.getOldOrInsertedNode().getTag() == BastNameIdent.TAG)
        && (op.getNewOrChangedNode().getTag() == BastNameIdent.TAG);
  }

}
