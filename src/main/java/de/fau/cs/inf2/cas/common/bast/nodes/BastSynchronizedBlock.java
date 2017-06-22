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
public class BastSynchronizedBlock extends AbstractBastStatement {

  public static final int TAG = TagConstants.BAST_SYNCHRONIZED_BLOCK;

  public AbstractBastExpr expr = null;
  public BastBlock block = null;

  /**
   * Instantiates a new bast synchronized block.
   *
   * @param tokens the tokens
   * @param expr the expr
   * @param block the block
   */
  public BastSynchronizedBlock(TokenAndHistory[] tokens, AbstractBastExpr expr, BastBlock block) {
    super(tokens);
    this.expr = expr;
    fieldMap.put(BastFieldConstants.SYNCHRONIZED_BLOCK_EXPR, new BastField(expr));
    this.block = block;
    fieldMap.put(BastFieldConstants.SYNCHRONIZED_BLOCK_BLOCK, new BastField(block));
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
      case SYNCHRONIZED_BLOCK_EXPR:
        this.expr = (AbstractBastExpr) fieldValue.getField();
        break;
      case SYNCHRONIZED_BLOCK_BLOCK:
        this.block = (BastBlock) fieldValue.getField();
        break;
      default:
        assert (false);

    }
  }

}