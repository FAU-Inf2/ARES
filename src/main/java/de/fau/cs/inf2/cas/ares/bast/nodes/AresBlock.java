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

package de.fau.cs.inf2.cas.ares.bast.nodes;

import de.fau.cs.inf2.cas.ares.bast.visitors.IAresBastVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractAresStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;

public class AresBlock extends AbstractAresStatement {

  public static final int TAG = TagConstants.ARES_BLOCK;

  public BastIntConst numberNode = null;
  public long number = -1;
  public BastBlock block;
  public LinkedList<AbstractBastExpr> identifiers = new LinkedList<AbstractBastExpr>();

  /**
   * Instantiates a new ARES block.
   *
   * @param tokens the tokens
   * @param number the number
   * @param block the block
   * @param identifiers the identifiers
   */
  public AresBlock(TokenAndHistory[] tokens, BastIntConst number, BastBlock block,
      LinkedList<AbstractBastExpr> identifiers) {
    super(tokens);
    this.numberNode = number;
    fieldMap.put(BastFieldConstants.ARES_BLOCK_NUMBER, new BastField(number));
    fieldMap.put(BastFieldConstants.ARES_BLOCK_BLOCK, new BastField(block));
    fieldMap.put(BastFieldConstants.ARES_BLOCK_IDENTIFIERS, new BastField(identifiers));

    if (number != null) {
      this.number = number.value;
    }
    this.block = block;
    this.identifiers = identifiers;
  }

  
  /**
   * Accept.
   *
   * @param visitor the visitor
   */
  public void accept(IAresBastVisitor visitor) {
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
  @SuppressWarnings("unchecked")
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case ARES_BLOCK_BLOCK:
        this.block = (BastBlock) fieldValue.getField();
        break;
      case ARES_BLOCK_NUMBER:
        this.numberNode = (BastIntConst) fieldValue.getField();
        break;
      case ARES_BLOCK_IDENTIFIERS:
        this.identifiers = (LinkedList<AbstractBastExpr>) fieldValue.getListField();
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
    return "//# match (" + this.numberNode + ")" + this.block;
  }

}
