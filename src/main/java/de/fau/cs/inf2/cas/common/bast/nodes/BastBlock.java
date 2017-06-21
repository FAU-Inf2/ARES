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

/**
 * todo. 
 */
public class BastBlock extends AbstractBastInternalDecl {

  public static final int TAG = TagConstants.BAST_BLOCK;
  public LinkedList<AbstractBastStatement> statements = null;
  public boolean isStatic = false;
  @SuppressWarnings("unused")
  private LinkedList<AbstractBastSpecifier> modifiers = null;

  /**
   * Instantiates a new bast block.
   *
   * @param tokens the tokens
   * @param statements the statements
   */
  public BastBlock(TokenAndHistory[] tokens, LinkedList<AbstractBastStatement> statements) {
    super(tokens);
    if (statements == null) {
      statements = new LinkedList<>();
    }
    this.statements = statements;
    fieldMap.put(BastFieldConstants.BLOCK_STATEMENT, new BastField(statements));
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
  @SuppressWarnings("unchecked")
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case BLOCK_STATEMENT:

        this.statements = (LinkedList<AbstractBastStatement>) fieldValue.getListField();
        assert (statements != null);
        break;
      default:
        assert (false);
    }
  }

  
  /**
   * Sets the modifiers.
   *
   * @param modifiers the new modifiers
   */
  @Override
  public void setModifiers(LinkedList<AbstractBastSpecifier> modifiers) {
    this.modifiers = modifiers;
    fieldMap.put(BastFieldConstants.BLOCK_MODIFIERS, new BastField(modifiers));

  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    String val = "{";
    if (statements != null && this.statements.size() > 0) {
      val += this.statements.get(0).toString();
    }
    if (statements != null && this.statements.size() > 1) {
      val += " ... ";
      val += this.statements.get(this.statements.size() - 1).toString();
    }
    val += " }";
    return val;
  }

}
