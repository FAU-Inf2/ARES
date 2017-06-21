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
 * <p>(a == true)? a = false : a = true
 * 
 */
public class BastCondExpr extends AbstractBastExpr {

  public static final int TAG = TagConstants.BAST_COND_EXPR;

  public AbstractBastExpr condition = null;
  public AbstractBastExpr truePart = null;
  public AbstractBastExpr falsePart = null;

  /**
   * Instantiates a new bast cond expr.
   *
   * @param tokens the tokens
   * @param condition the condition
   * @param truePart the true part
   * @param falsePart the false part
   */
  public BastCondExpr(TokenAndHistory[] tokens, AbstractBastExpr condition,
      AbstractBastExpr truePart, AbstractBastExpr falsePart) {
    super(tokens);
    this.condition = condition;
    fieldMap.put(BastFieldConstants.COND_EXPR_CONDITION, new BastField(condition));
    this.truePart = truePart;
    fieldMap.put(BastFieldConstants.COND_EXPR_TRUE_PART, new BastField(truePart));
    this.falsePart = falsePart;
    fieldMap.put(BastFieldConstants.COND_EXPR_FALSE_PART, new BastField(falsePart));
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
    return -1;
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
      case COND_EXPR_CONDITION:
        this.condition = (AbstractBastExpr) fieldValue.getField();
        break;
      case COND_EXPR_TRUE_PART:
        this.truePart = (AbstractBastExpr) fieldValue.getField();
        break;
      case COND_EXPR_FALSE_PART:
        this.falsePart = (AbstractBastExpr) fieldValue.getField();
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
    StringBuilder tmp = new StringBuilder();

    tmp.append(condition);
    tmp.append(" ? ");
    tmp.append(truePart);
    tmp.append(" : ");
    tmp.append(falsePart);

    return tmp.toString();
  }
}
