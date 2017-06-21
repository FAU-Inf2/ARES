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
import de.fau.cs.inf2.cas.common.bast.type.BastType;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

/**
 * todo. 
 */
public class BastCastExpr extends AbstractBastUnaryExpr {
  public static final int TAG = TagConstants.BAST_CAST_EXPR;

  private static final int DEFAULT_CAST = 0;
  public static final int RND_CAST = 1;
  public static final int RND_NEG_CAST = 2;
  public static final int RND_POS_CAST = 3;
  public static final int TRUNC_CAST = 4;
  public int type = DEFAULT_CAST;

  public BastType castType = null;

  /**
   * Instantiates a new bast cast expr.
   *
   * @param tokens the tokens
   * @param operand the operand
   * @param castType the cast type
   */
  public BastCastExpr(TokenAndHistory[] tokens, AbstractBastExpr operand, BastType castType) {
    super(tokens, operand);
    this.castType = castType;
    fieldMap.put(BastFieldConstants.CAST_EXPR_OPERAND, new BastField(operand));
    fieldMap.put(BastFieldConstants.CAST_EXPR_TYPE, new BastField(castType));
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
      case CAST_EXPR_OPERAND:
        this.operand = (AbstractBastExpr) fieldValue.getField();
        break;
      case CAST_EXPR_TYPE:
        this.castType = (BastType) fieldValue.getField();
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
    StringBuffer buffer = new StringBuffer();
    if (castType != null) {
      buffer.append("(");
      buffer.append(castType);
      buffer.append(")");
    } else {
      switch (type) {
        case BastCastExpr.RND_CAST:
          buffer.append("CAST_RND(");
          break;
        case BastCastExpr.RND_NEG_CAST:
          buffer.append("CAST_RNDNEG(");
          break;
        case BastCastExpr.RND_POS_CAST:
          buffer.append("CAST_RNDPOS(");
          break;
        case BastCastExpr.TRUNC_CAST:
          buffer.append("CAST_TRUNC(");
          break;
        default:
          assert (false);
      }
      buffer.append(operand.toString());
      buffer.append(")");
    }
    return buffer.toString();
  }

}
