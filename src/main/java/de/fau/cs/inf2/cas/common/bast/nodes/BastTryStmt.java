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

import java.util.LinkedList;


public class BastTryStmt extends AbstractBastStatement {

  public static final int TAG = TagConstants.BAST_TRY;

  public BastBlock block = null;
  public LinkedList<BastCatchClause> catches = null;
  public BastBlock finallyBlock = null;
  public LinkedList<BastDeclaration> resources = null;

  /**
   * Instantiates a new bast try stmt.
   *
   * @param tokens the tokens
   * @param block the block
   * @param catches the catches
   * @param finallyBlock the finally block
   * @param resources the resources
   */
  public BastTryStmt(TokenAndHistory[] tokens, BastBlock block, LinkedList<BastCatchClause> catches,
      BastBlock finallyBlock, LinkedList<BastDeclaration> resources) {
    super(tokens);
    this.block = block;
    fieldMap.put(BastFieldConstants.TRY_BLOCK, new BastField(block));
    this.catches = catches;
    fieldMap.put(BastFieldConstants.TRY_CATCHES, new BastField(catches));
    this.finallyBlock = finallyBlock;
    fieldMap.put(BastFieldConstants.TRY_FINALLY_BLOCK, new BastField(finallyBlock));
    this.resources = resources;
    fieldMap.put(BastFieldConstants.TRY_RESOURCES, new BastField(resources));
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
  @SuppressWarnings("unchecked")
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case TRY_BLOCK:
        this.block = (BastBlock) fieldValue.getField();
        break;
      case TRY_CATCHES:
        this.catches = (LinkedList<BastCatchClause>) fieldValue.getListField();
        break;
      case TRY_FINALLY_BLOCK:
        this.finallyBlock = (BastBlock) fieldValue.getField();
        break;
      case TRY_RESOURCES:
        this.resources = (LinkedList<BastDeclaration>) fieldValue.getListField();
        break;
      default:
        assert (false);

    }
  }

}
