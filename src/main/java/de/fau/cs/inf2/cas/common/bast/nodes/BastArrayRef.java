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
 * <p>Read/Write access of an array element
 * 
 */
public class BastArrayRef extends AbstractBastExpr {

  public static final int TAG = TagConstants.BAST_ARRAY_REF;
  public AbstractBastExpr arrayRef = null;
  public LinkedList<AbstractBastExpr> indices = null;

  /**
   * Instantiates a new bast array ref.
   *
   * @param tokens the tokens
   * @param arrayRef the array ref
   * @param indices the indices
   */
  public BastArrayRef(TokenAndHistory[] tokens, AbstractBastExpr arrayRef,
      LinkedList<AbstractBastExpr> indices) {
    super(tokens);
    this.arrayRef = arrayRef;
    fieldMap.put(BastFieldConstants.ARRAY_REF_REF, new BastField(arrayRef));
    this.indices = indices;
    fieldMap.put(BastFieldConstants.ARRAY_REF_INDEX_LIST, new BastField(indices));
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
    return 2;
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
      case ARRAY_REF_REF:
        this.arrayRef = (AbstractBastExpr) fieldValue.getField();
        break;
      case ARRAY_REF_INDEX_LIST:
        this.indices = (LinkedList<AbstractBastExpr>) fieldValue.getListField();
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
    StringBuffer buffer = new StringBuffer();
    if (arrayRef != null) {
      buffer.append(arrayRef.toString());
    }
    buffer.append("[");
    if (indices != null) {
      boolean comma = false;
      for (AbstractBastExpr expr : indices) {
        if (comma == true) {
          buffer.append(",");
        }
        if (comma == false) {
          comma = true;
        }
        buffer.append(expr.toString());
      }
    }
    buffer.append("]");
    return buffer.toString();
  }

}
