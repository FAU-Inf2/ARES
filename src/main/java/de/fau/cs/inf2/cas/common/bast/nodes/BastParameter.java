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
 *

 */
public class BastParameter extends AbstractBastExternalDecl {
  public static final int TAG = TagConstants.BAST_PARAMETER;
  public String name = null;
  public boolean isEllipsis = false;

  public LinkedList<AbstractBastSpecifier> specifiers;
  public AbstractBastDeclarator declarator;

  /**
   * Instantiates a new bast parameter.
   *
   * @param tokens the tokens
   * @param specifiers the specifiers
   * @param declarator the declarator
   */
  public BastParameter(TokenAndHistory[] tokens, LinkedList<AbstractBastSpecifier> specifiers,
      AbstractBastDeclarator declarator) {
    super(tokens);
    this.specifiers = specifiers;
    fieldMap.put(BastFieldConstants.PARAMETER_SPECIFIERS, new BastField(specifiers));
    this.declarator = declarator;
    fieldMap.put(BastFieldConstants.PARAMETER_DECLARATOR, new BastField(declarator));
    if (declarator != null && declarator.getTag() == BastIdentDeclarator.TAG) {
      BastIdentDeclarator tmp = (BastIdentDeclarator) declarator;
      name = tmp.identifier.getName();
    }

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
    assert (false);
    return 0;
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
      case PARAMETER_SPECIFIERS:
        this.specifiers = (LinkedList<AbstractBastSpecifier>) fieldValue.getListField();
        break;
      case PARAMETER_DECLARATOR:
        this.declarator = (AbstractBastDeclarator) fieldValue.getField();
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
    assert (false);
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    StringBuilder tmp = new StringBuilder();
    for (AbstractBastSpecifier spec : specifiers) {
      tmp.append(spec.toString());
      tmp.append(" ");
    }

    tmp.append(declarator.toString());

    return tmp.toString();
  }
}
