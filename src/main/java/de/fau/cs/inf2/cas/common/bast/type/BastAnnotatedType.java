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

package de.fau.cs.inf2.cas.common.bast.type;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAnnotation;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;

/**
 * An annotated type.
 */
public class BastAnnotatedType extends BastType {

  public static final int TAG = TagConstants.BAST_ANNOTATED_TYPE;
  private static final int TYPE_TAG = TagConstants.TYPE_ANNOTATED;
  public LinkedList<BastAnnotation> annotations;
  public BastType type = null;



  /**
   * Instantiates a new annotated type.
   *
   * @param tokens the tokens
   * @param annotations the annotations
   * @param type the type
   */
  public BastAnnotatedType(final TokenAndHistory[] tokens,
      final LinkedList<BastAnnotation> annotations, final BastType type) {
    super(tokens);
    this.annotations = annotations;
    fieldMap.put(BastFieldConstants.ANNOTATED_TYPE_ANNOTATIONS, new BastField(annotations));
    this.type = type;
    fieldMap.put(BastFieldConstants.ANNOTATED_TYPE_TYPE, new BastField(type));
  }



  /**
   * Accepts this annotated type.
   *
   * @param visitor the visitor
   */
  @Override
  public void accept(final IBastVisitor visitor) {
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



  public int getTypeTag() {
    return TYPE_TAG;
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
      case ANNOTATED_TYPE_TYPE:
        this.type = (BastType) fieldValue.getField();
        break;
      case ANNOTATED_TYPE_ANNOTATIONS:
        this.annotations = (LinkedList<BastAnnotation>) fieldValue.getListField();
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
  public String toString() {
    StringBuilder resultBuilder = new StringBuilder();
    for (final BastAnnotation annotation : this.annotations) {
      resultBuilder.append(annotation).append(' ');
    }
    resultBuilder.append(this.type);
    return resultBuilder.toString();
  }
}

