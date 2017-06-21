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


public class BastUnaryExpr extends AbstractBastUnaryExpr {

  public static final int TAG = TagConstants.BAST_UNARY_EXPR;
  public static final int TYPE_INCR = 0;
  public static final int TYPE_DECR = 1;
  public static final int TYPE_ADDR_OF = 2;
  public static final int TYPE_DREF = 3;
  public static final int TYPE_PLUS = 4;
  public static final int TYPE_NEG = 5;
  public static final int TYPE_INVERSE = 6;
  public static final int TYPE_NOT = 7;
  public static final int TYPE_SIZE_OF = 8;
  public int type = TYPE_INCR;

  /**
   * Instantiates a new bast unary expr.
   *
   * @param tokens the tokens
   * @param operand the operand
   * @param type the type
   */
  public BastUnaryExpr(TokenAndHistory[] tokens, AbstractBastExpr operand, int type) {
    super(tokens, operand);
    this.type = type;
    fieldMap.put(BastFieldConstants.UNARY_EXPR_OPERAND, new BastField(operand));
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
    return 2;
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
      case UNARY_EXPR_OPERAND:
        this.operand = (AbstractBastExpr) fieldValue.getField();
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
  @Override
  public String toString() {
    switch (type) {
      case TYPE_INCR:
        return operand + "++";
      case TYPE_DECR:
        return operand + "--";
      case TYPE_ADDR_OF:
        return "&" + operand;
      case TYPE_DREF:
        return "*" + operand;
      case TYPE_PLUS:
        return "+" + operand;
      case TYPE_NEG:
        return "-" + operand;
      case TYPE_INVERSE:
        return "~" + operand;
      case TYPE_NOT:
        return "!" + operand;
      case TYPE_SIZE_OF:
        return "sizeof " + operand;
      default:
        assert false;
    }
    return "";
  }

}
