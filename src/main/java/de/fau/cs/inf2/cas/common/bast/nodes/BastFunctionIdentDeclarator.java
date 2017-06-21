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
 * <p>Function header in c, if the parameter list contains no types but only identifiers (old c)
 * 
 */
public class BastFunctionIdentDeclarator extends AbstractBastDeclarator {

  public static final int TAG = TagConstants.BAST_FUNCTION_IDENT_DECL;
  public LinkedList<BastNameIdent> parameters = null;

  public AbstractBastExpr function = null;

  /**
   * Instantiates a new bast function ident declarator.
   *
   * @param tokens the tokens
   * @param parameters the parameters
   * @param function the function
   */
  public BastFunctionIdentDeclarator(TokenAndHistory[] tokens, LinkedList<BastNameIdent> parameters,
      AbstractBastExpr function) {
    super(tokens);
    this.parameters = parameters;
    fieldMap.put(BastFieldConstants.FUNCTION_IDENT_DECLARATOR_PARAMTERS, new BastField(parameters));
    this.function = function;
    fieldMap.put(BastFieldConstants.FUNCTION_IDENT_DECLARATOR_FUNCTION, new BastField(function));
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
   * Sets the initializer.
   *
   * @param init the new initializer
   */
  @Override
  public void setInitializer(AbstractBastInitializer init) {
    assert (false);

  }

  
  /**
   * Sets the pointer.
   *
   * @param pointer the new pointer
   */
  @Override
  public void setPointer(BastPointer pointer) {
    assert (false);

  }
}
