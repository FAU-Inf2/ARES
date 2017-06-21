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
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastIdentifier;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeArgument;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;


public class BastClassType extends BastType {

  public static final int TAG = TagConstants.TYPE_CLASS;

  public AbstractBastIdentifier name = null;
  public LinkedList<BastTypeArgument> typeArgumentList = null;
  public BastType subClass = null;

  /**
   * Instantiates a new bast class type.
   *
   * @param tokens the tokens
   * @param name the name
   * @param param the param
   * @param subClass the sub class
   */
  public BastClassType(TokenAndHistory[] tokens, AbstractBastIdentifier name,
      LinkedList<BastTypeArgument> param, BastType subClass) {
    super(tokens);
    this.name = name;
    fieldMap.put(BastFieldConstants.CLASS_TYPE_NAME, new BastField(name));
    this.typeArgumentList = param;
    fieldMap.put(BastFieldConstants.CLASS_TYPE_TYPE_ARGUMENT, new BastField(param));
    this.subClass = subClass;
    fieldMap.put(BastFieldConstants.CLASS_TYPE_SUB_CLASS, new BastField(subClass));

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
      case CLASS_TYPE_NAME:
        this.name = (AbstractBastIdentifier) fieldValue.getField();
        break;
      case CLASS_TYPE_SUB_CLASS:
        this.subClass = (BastType) fieldValue.getField();
        break;
      case CLASS_TYPE_TYPE_ARGUMENT:
        this.typeArgumentList = (LinkedList<BastTypeArgument>) fieldValue.getListField();
        for (AbstractBastNode n : this.typeArgumentList) {
          if (!(n instanceof BastTypeArgument || n instanceof BastNameIdent)) {
            assert (false);
          }
        }
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
    StringBuilder sb = new StringBuilder();
    sb.append(this.name.getName());
    if (typeArgumentList != null) {
      sb.append("<");
      for (AbstractBastNode arg : this.typeArgumentList) {
        sb.append(arg.toString());
      }
      sb.append(">");
    }
    return sb.toString();
  }

}
