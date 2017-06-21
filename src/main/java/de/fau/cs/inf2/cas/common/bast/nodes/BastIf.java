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
public class BastIf extends AbstractBastStatement {

  public static final int TAG = TagConstants.BAST_IF;
  public AbstractBastExpr condition = null;
  public AbstractBastStatement ifPart = null;
  public AbstractBastStatement elsePart = null;

  /**
   * Instantiates a new bast if.
   *
   * @param tokens the tokens
   * @param condition the condition
   * @param ifPart the if part
   * @param elsePart the else part
   */
  public BastIf(TokenAndHistory[] tokens, AbstractBastExpr condition, AbstractBastStatement ifPart,
      AbstractBastStatement elsePart) {
    super(tokens);
    this.condition = condition;
    fieldMap.put(BastFieldConstants.IF_CONDITION, new BastField(condition));
    this.ifPart = ifPart;
    fieldMap.put(BastFieldConstants.IF_IF_PART, new BastField(ifPart));
    this.elsePart = elsePart;
    fieldMap.put(BastFieldConstants.IF_ELSE_PART, new BastField(elsePart));
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
      case IF_CONDITION:
        this.condition = (AbstractBastExpr) fieldValue.getField();
        break;
      case IF_IF_PART:
        this.ifPart = (AbstractBastStatement) fieldValue.getField();
        break;
      case IF_ELSE_PART:
        this.elsePart = (AbstractBastStatement) fieldValue.getField();
        break;
      default:
        assert (false);
    }
  }

  /**
   * Sets the else.
   *
   * @param elif the new else
   */
  public void setElse(AbstractBastStatement elif) {
    assert elsePart == null;
    elsePart = elif;
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("BastIf (");
    buffer.append(condition.toString());
    buffer.append(") \n");
    if (this.ifPart != null) {
      buffer.append(this.ifPart);
    }
    if (this.elsePart != null) {
      buffer.append(this.elsePart);
    }
    return buffer.toString();

  }

}
