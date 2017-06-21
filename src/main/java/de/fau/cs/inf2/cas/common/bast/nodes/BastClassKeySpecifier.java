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
 * Simple wrapper representing the type of class definition.
 * 
 */
public class BastClassKeySpecifier extends AbstractBastSpecifierQualifier {

  public static final int TAG = TagConstants.BAST_CLASS_KEY_SPECIFIER;

  public static final int STRUCT = 0;

  public static final int ENUM = 1;
  public static final int UNION = 2;
  public static final int CLASS = 3;
  public int type = -1;

  /**
   * Instantiates a new bast class key specifier.
   *
   * @param tokens the tokens
   */
  public BastClassKeySpecifier(TokenAndHistory[] tokens) {
    super(tokens);
  }

  /**
   * Instantiates a new bast class key specifier.
   *
   * @param tokens the tokens
   * @param type the type
   */
  public BastClassKeySpecifier(TokenAndHistory[] tokens, int type) {
    super(tokens);
    this.type = type;
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
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    switch (type) {
      case STRUCT:
        return "struct";
      case ENUM:
        return "enum";
      case UNION:
        return "union";
      case CLASS:
        return "class";
      default:
        assert false;
        return "";
    }
  }

}
