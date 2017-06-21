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
import de.fau.cs.inf2.cas.common.bast.type.BastArrayType;
import de.fau.cs.inf2.cas.common.bast.type.BastBasicType;
import de.fau.cs.inf2.cas.common.bast.type.BastClassType;
import de.fau.cs.inf2.cas.common.bast.type.BastTypeName;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing an update of the type of a variable declaration.
 *
 */
public final class VariableTypeUpdateOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new variable type update operation.
   *
   * @param op the op
   */
  public VariableTypeUpdateOperation(final BastEditOperation op) {
    super(EditOperationType.VARIABLE_TYPE_UPDATE, op);
    assert VariableTypeUpdateOperation.describes(op);
  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final BastEditOperation op) {
    return op.getType() == EditOperationType.UPDATE
        && isType(op.getOldOrInsertedNode())
        && isType(op.getNewOrChangedNode());
  }

  private static boolean isType(final AbstractBastNode node) {
    switch (node.getTag()) {
      case BastArrayType.TAG:
      case BastBasicType.TAG:
      case BastClassType.TAG:
      case BastTypeName.TAG:
        return true;

      default:
        return false;
    }
  }
  
}
