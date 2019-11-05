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

package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo. 

 */
public class BastXor extends AbstractBaseBinaryExpr {

  public static final int TAG = TagConstants.BAST_XOR;

  /**
   * Instantiates a new bast xor.
   *
   * @param tokens the tokens
   * @param left the left
   * @param right the right
   */
  public BastXor(TokenAndHistory[] tokens, AbstractBastExpr left, AbstractBastExpr right) {
    super(tokens, left, right);
    fieldMap.put(BastFieldConstants.XOR_LEFT, new BastField(left));
    fieldMap.put(BastFieldConstants.XOR_RIGHT, new BastField(right));
  }

  
  /**
   * Accept.
   *
   * @param visitor the visitor
   */
  public void accept(IBastVisitor visitor) {
    visitor.visit(this);

  }

  
  /**
   * Gets the priority.
   *
   * @return the priority
   */
  public int getPriority() {
    return 9;
  }

  /**
   * Gets the tag.
   *
   * @return the tag
   */
  @Override
  public int getTag() {
    return TAG;
  }

  
  /**
   * Replace field.
   *
   * @param field the field
   * @param fieldValue the field value
   */
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case XOR_LEFT:
        this.left = (AbstractBastExpr) fieldValue.getField();
        break;
      case XOR_RIGHT:
        this.right = (AbstractBastExpr) fieldValue.getField();
        break;
      default:
        assert (false);

    }
  }

}
