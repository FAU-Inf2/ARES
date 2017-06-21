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

import java.util.LinkedList;

/**
 * todo.
 *
 * <p>To define an annotation method
 * 
 */
public class BastAnnotationMethod extends AbstractBastInternalDecl {

  public static final int TAG = TagConstants.BAST_ANNOTATION_METHOD;
  public BastType type = null;
  public AbstractBastExpr defaultValue = null;
  public AbstractBastDeclarator declarator = null;
  @SuppressWarnings("ucd")
  public LinkedList<AbstractBastSpecifier> modifiers = null;

  
  /**
   * Instantiates a new bast annotation method.
   *
   * @param tokens the tokens
   * @param declarator the declarator
   * @param type the type
   * @param defaultValue the default value
   */
  public BastAnnotationMethod(TokenAndHistory[] tokens, BastIdentDeclarator declarator,
      BastType type, AbstractBastExpr defaultValue) {
    super(tokens);
    this.declarator = declarator;
    fieldMap.put(BastFieldConstants.ANNOTATION_METHOD_DECLARATOR, new BastField(declarator));
    this.type = type;
    fieldMap.put(BastFieldConstants.ANNOTATION_METHOD_TYPE, new BastField(type));
    this.defaultValue = defaultValue;
    fieldMap.put(BastFieldConstants.ANNOTATION_METHOD_DEFAULT_VALUE, new BastField(defaultValue));
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
      case ANNOTATION_METHOD_TYPE:
        this.type = (BastType) fieldValue.getField();
        break;
      case ANNOTATION_METHOD_DEFAULT_VALUE:
        this.defaultValue = (AbstractBastExpr) fieldValue.getField();
        break;
      case ANNOTATION_METHOD_DECLARATOR:
        this.declarator = (AbstractBastDeclarator) fieldValue.getField();
        break;
      case ANNOTATION_METHOD_MODIFIERS:
        this.modifiers = (LinkedList<AbstractBastSpecifier>) fieldValue.getListField();
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
    fieldMap.put(BastFieldConstants.ANNOTATION_METHOD_MODIFIERS, new BastField(modifiers));

  }

}
