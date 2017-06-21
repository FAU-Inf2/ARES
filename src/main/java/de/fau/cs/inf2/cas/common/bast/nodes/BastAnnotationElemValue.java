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
 * <p>Annotation item initialization, can be an open list
 * 
 */
public class BastAnnotationElemValue extends AbstractBastSpecifier {

  public static final int TAG = TagConstants.BAST_ANNOTATION_ELEM_VALUE;
  public AbstractBastExpr qualifiedName = null;
  public LinkedList<AbstractBastExpr> initList = null;
  @SuppressWarnings("unused")
  private boolean withComma = false;

  /**
   * Instantiates a new bast annotation elem value.
   *
   * @param tokens the tokens
   * @param qualifiedName the qualified name
   * @param initList the init list
   * @param withComma the with comma
   */
  public BastAnnotationElemValue(TokenAndHistory[] tokens, AbstractBastExpr qualifiedName,
      LinkedList<AbstractBastExpr> initList, boolean withComma) {
    super(tokens);
    this.qualifiedName = qualifiedName;
    fieldMap.put(BastFieldConstants.ANNOTATION_ELEM_VALUE_QUALIFIED_NAME,
        new BastField(qualifiedName));
    this.initList = initList;
    fieldMap.put(BastFieldConstants.ANNOTATION_ELEM_VALUE_INITLIST, new BastField(initList));
    this.withComma = withComma;
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
      case ANNOTATION_ELEM_VALUE_QUALIFIED_NAME:
        this.qualifiedName = (AbstractBastExpr) fieldValue.getField();
        break;
      case ANNOTATION_ELEM_VALUE_INITLIST:
        this.initList = (LinkedList<AbstractBastExpr>) fieldValue.getListField();
        break;
      default:
        assert (false);

    }
  }

}
