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


public class BastWhileStatement extends AbstractBastStatement {

  public static final int TAG = TagConstants.BAST_WHILE_STATEMENT;
  public static final int TYPE_WHILE = 0;
  public static final int TYPE_DO_WHILE = 1;
  public int type = TYPE_WHILE;

  public AbstractBastExpr expression = null;
  public AbstractBastStatement statement = null;

  /**
   * Instantiates a new bast while statement.
   *
   * @param tokens the tokens
   * @param expression the expression
   * @param statement the statement
   * @param type the type
   */
  public BastWhileStatement(TokenAndHistory[] tokens, AbstractBastExpr expression,
      AbstractBastStatement statement, int type) {
    super(tokens);
    this.expression = expression;
    fieldMap.put(BastFieldConstants.WHILE_EXPRESSION, new BastField(expression));
    this.statement = statement;
    fieldMap.put(BastFieldConstants.WHILE_STATEMENT, new BastField(statement));
    this.type = type;
  }

  
  /**
   * Accept.
   *
   * @param visitor the visitor
   */
  @Override
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
      case WHILE_EXPRESSION:
        this.expression = (AbstractBastExpr) fieldValue.getField();
        break;
      case WHILE_STATEMENT:
        this.statement = (AbstractBastStatement) fieldValue.getField();
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
    String string = null;
    if (type == TYPE_WHILE) {
      string = "while (" + this.expression + ")" + this.statement;

    } else {
      string = "do " + this.statement + "while (" + this.expression + ")";
    }
    return string;
  }

}
