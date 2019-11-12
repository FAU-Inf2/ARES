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
 * A lambda expression.
 */
public class BastLambdaExpr extends AbstractBastExpr {

  public static final int TAG = TagConstants.BAST_LAMBDA_EXPR;
  public LinkedList<BastParameter> parameters;
  public AbstractBastStatement body;



  /**
   * Instantiates a new lambda expression.
   *
   * @param tokens the tokens
   * @param parameters the formal parameter list
   * @param body the body
   */
  public BastLambdaExpr(final TokenAndHistory[] tokens, final LinkedList<BastParameter> parameters,
      final AbstractBastStatement body) {
    super(tokens);
    this.parameters = parameters;
    this.body = body;
    fieldMap.put(BastFieldConstants.LAMBDA_PARAMETERS, new BastField(parameters));
    fieldMap.put(BastFieldConstants.LAMBDA_BODY, new BastField(body));
  }



  /**
   * Accept the given visitor.
   *
   * @param visitor the visitor to accept
   */
  public void accept(final IBastVisitor visitor) {
    visitor.visit(this);
  }



  /**
   * Gets the priority.
   *
   * @return the priority
   */
  public int getPriority() {
    return -1;
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
  public void replaceField(final BastFieldConstants field, final BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case LAMBDA_PARAMETERS:
        this.parameters = (LinkedList<BastParameter>) fieldValue.getListField();
        break;
      case LAMBDA_BODY:
        this.body = (AbstractBastStatement) fieldValue.getField();
        break;
      default:
        assert false;
    }
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    final StringBuilder resultBuilder = new StringBuilder()
        .append('(');

    if (this.parameters != null) {
      boolean first = true;

      for (final BastParameter param : this.parameters) {
        if (!first) {
          resultBuilder.append(", ");
        }
        first = false;

        if (param.specifiers != null) {
          for (final AbstractBastSpecifier spec : param.specifiers) {
            resultBuilder.append(spec).append(' ');
          }
        }
        resultBuilder.append(param.declarator);
      }
    }

    resultBuilder.append(") -> ")
        .append(this.body);

    return resultBuilder.toString();
  }
}

