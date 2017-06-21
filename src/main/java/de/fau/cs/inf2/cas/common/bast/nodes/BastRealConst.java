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

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo.
 *
 * 

 */
public class BastRealConst extends AbstractBastConstant {

  public static final int TAG = TagConstants.BAST_REAL_CONST;
  public double value;
  public String actualValue = null;
  public boolean fitsInDouble = false;

  /**
   * todo.
   * Used to clone a constant.
   * 
   * @param tokens the tokens
   * @param value todo
   */
  public BastRealConst(TokenAndHistory[] tokens, double value) {
    super(tokens);
    this.value = value;
    actualValue = String.valueOf(value);
    fitsInDouble = true;
  }

  /**
   * Instantiates a new bast real const.
   *
   * @param tokens the tokens
   * @param value the value
   */
  public BastRealConst(TokenAndHistory[] tokens, String value) {
    super(tokens);

    int xvar = value.lastIndexOf(".d");
    if (xvar > 0) {
      value = value.replace(".d", ".e");
    }

    if (value.endsWith("d0")) {
      value = value.substring(0, value.length() - 1);
    } else if (value.endsWith("D0")) {
      value = value.substring(0, value.length() - 1);
    }
    this.value = Double.valueOf(value);
    actualValue = value;
    fitsInDouble = true;
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
   * To string.
   *
   * @return the string
   */
  public String toString() {
    return String.valueOf(value);
  }

}
