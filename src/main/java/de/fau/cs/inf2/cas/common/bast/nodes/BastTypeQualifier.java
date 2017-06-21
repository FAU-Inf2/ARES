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

 */
public class BastTypeQualifier extends AbstractBastSpecifierQualifier {

  public static final int TAG = TagConstants.BAST_TYPE_QUALIFIER;

  public static final int CONST_QUALIFIER = 0;
  public static final int VOLATILE_QUALIFIER = 1;
  public static final int TYPE_PUBLIC = 2;
  public static final int TYPE_PROTECTED = 3;
  public static final int TYPE_PRIVATE = 4;
  public static final int TYPE_STATIC = 5;
  public static final int TYPE_ABSTRACT = 6;
  public static final int TYPE_FINAL = 7;
  public static final int TYPE_NATIVE = 8;
  public static final int TYPE_SYNCHRONIZED = 9;
  public static final int TYPE_TRANSIENT = 10;
  public static final int TYPE_VOLATILE = 11;
  public static final int TYPE_STRICTFP = 12;
  private static final int TYPE_SHARED = 13;
  private static final int TYPE_INLINE = 14;
  public int type = -1;

  /**
   * Instantiates a new bast type qualifier.
   *
   * @param tokens the tokens
   * @param type the type
   */
  public BastTypeQualifier(TokenAndHistory[] tokens, int type) {
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
      case CONST_QUALIFIER:
        return "const";
      case VOLATILE_QUALIFIER:
        return "volatile";
      case TYPE_PUBLIC:
        return "public";
      case TYPE_PROTECTED:
        return "protected";
      case TYPE_PRIVATE:
        return "private";
      case TYPE_STATIC:
        return "static";
      case TYPE_ABSTRACT:
        return "abstract";
      case TYPE_FINAL:
        return "final";
      case TYPE_NATIVE:
        return "native";
      case TYPE_SYNCHRONIZED:
        return "synchronized";
      case TYPE_TRANSIENT:
        return "transient";
      case TYPE_VOLATILE:
        return "volatile";
      case TYPE_STRICTFP:
        return "strictfp";
      case TYPE_SHARED:
        return "shared";
      case TYPE_INLINE:
        return "inline";
      default:
        break;
    }
    return "";
  }

}
