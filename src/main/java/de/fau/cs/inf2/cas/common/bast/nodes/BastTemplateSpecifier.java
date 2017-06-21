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
 * <p>Java template parameter
 * 

 */
public class BastTemplateSpecifier extends AbstractBastExpr {

  public static final int TAG = TagConstants.BAST_TEMPLATE;

  public AbstractBastExpr target = null;
  public LinkedList<BastTypeArgument> typeArguments = null;

  /**
   * Instantiates a new bast template specifier.
   *
   * @param tokens the tokens
   * @param target the target
   * @param typeArguments the type arguments
   */
  public BastTemplateSpecifier(TokenAndHistory[] tokens, AbstractBastExpr target,
      LinkedList<BastTypeArgument> typeArguments) {
    super(tokens);
    this.target = target;
    fieldMap.put(BastFieldConstants.TEMPLATE_SPECIFIER_TARGET, new BastField(target));
    this.typeArguments = typeArguments;
    fieldMap.put(BastFieldConstants.TEMPLATE_SPECIFIER_TYPE_ARGUMENTS,
        new BastField(typeArguments));
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
      case TEMPLATE_SPECIFIER_TARGET:
        this.target = (AbstractBastExpr) fieldValue.getField();
        break;
      case TEMPLATE_SPECIFIER_TYPE_ARGUMENTS:
        this.typeArguments = (LinkedList<BastTypeArgument>) fieldValue.getListField();
        break;
      default:
        assert (false);

    }
  }

}
