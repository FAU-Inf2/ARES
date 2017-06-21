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
 *
 * <p>Access of an element from a c struct pointer (->)
 * 
 */
public class BastAccess extends AbstractBastExpr {
  public static final int TAG = TagConstants.BAST_ACCESS;
  public static final int TYPE_DEFAULT = 0;
  public static final int TYPE_STRUCT_POINTER = 1;
  public AbstractBastExpr member = null;
  public AbstractBastExpr target = null;
  public int type = TYPE_DEFAULT;

  /**
   * Instantiates a new bast access.
   *
   * @param tokens the tokens
   * @param target the target
   * @param member the member
   */
  public BastAccess(TokenAndHistory[] tokens, AbstractBastExpr target, AbstractBastExpr member) {
    super(tokens);
    this.member = member;
    fieldMap.put(BastFieldConstants.ACCESS_MEMBER, new BastField(member));
    this.target = target;
    fieldMap.put(BastFieldConstants.ACCESS_TARGET, new BastField(target));
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
    return 1;
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
      case ACCESS_MEMBER:
        this.member = (AbstractBastExpr) fieldValue.getField();
        break;
      case ACCESS_TARGET:
        this.target = (AbstractBastExpr) fieldValue.getField();
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
    buffer.append(target.toString());
    if (type == TYPE_DEFAULT) {
      buffer.append('.');
    } else if (type == TYPE_STRUCT_POINTER) {
      buffer.append("->");
    } else {
      assert false;
    }
    buffer.append(member.toString());
    return buffer.toString();
  }
}
