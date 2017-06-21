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
 *
 * <p>Node for add and sub
 * 
 */
public class BastAdditiveExpr extends AbstractBaseBinaryExpr {

  public static final int TAG = TagConstants.BAST_ADDITIVE_EXPR;
  public static final int TYPE_ADD = 0;
  public static final int TYPE_SUB = 1;
  public int type = TYPE_ADD;

  /**
   * Instantiates a new bast additive expr.
   *
   * @param tokens the tokens
   * @param left the left
   * @param right the right
   * @param type the type
   */
  public BastAdditiveExpr(TokenAndHistory[] tokens, AbstractBastExpr left, AbstractBastExpr right,
      int type) {
    super(tokens, left, right);
    fieldMap.put(BastFieldConstants.ADDITIVE_EXPR_LEFT, new BastField(left));
    fieldMap.put(BastFieldConstants.ADDITIVE_EXPR_RIGHT, new BastField(right));
    this.type = type;
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
    return 4;
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
      case ADDITIVE_EXPR_LEFT:
        this.left = (AbstractBastExpr) fieldValue.getField();
        break;
      case ADDITIVE_EXPR_RIGHT:
        this.right = (AbstractBastExpr) fieldValue.getField();
        break;
      default:
        assert (false);

    }
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    return "(" + left + (type == TYPE_ADD ? " + " : " - ") + right + ")";
  }

}
