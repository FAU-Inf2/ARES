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

public class BastAssertStmt extends AbstractBastStatement {

  public static final int TAG = TagConstants.BAST_ASSERT;

  public AbstractBastExpr firstAssert = null;
  public AbstractBastExpr secondAssert = null;

  /**
   * Instantiates a new bast assert stmt.
   *
   * @param tokens the tokens
   * @param firstAssert the first assert
   * @param secondAssert the second assert
   */
  public BastAssertStmt(TokenAndHistory[] tokens, AbstractBastExpr firstAssert,
      AbstractBastExpr secondAssert) {
    super(tokens);
    this.firstAssert = firstAssert;
    fieldMap.put(BastFieldConstants.ASSERT_STMT_FIRST_ASSERT, new BastField(firstAssert));
    this.secondAssert = secondAssert;
    fieldMap.put(BastFieldConstants.ASSERT_STMT_SECOND_ASSERT, new BastField(secondAssert));
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
  
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case ASSERT_STMT_FIRST_ASSERT:
        this.firstAssert = (AbstractBastExpr) fieldValue.getField();
      case ASSERT_STMT_SECOND_ASSERT:
        this.secondAssert = (AbstractBastExpr) fieldValue.getField();
        break;
      default:
        assert (false);

    }
  }

}
