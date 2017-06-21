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

package de.fau.cs.inf2.cas.common.util;

final class Token {
  static enum TokenType {
    TOKEN_PACKAGE, TOKEN_IDENTIFIER, TOKEN_DOT, TOKEN_SEMIcOLON, TOKEN_UNKNOWN, TOKEN_EOF
  }

  private final TokenType tokenType;
  private final String string;

  /**
   * Instantiates a new token.
   *
   * @param tokenType the token type
   * @param string the string
   */
  public Token(final TokenType tokenType, final String string) {
    this.tokenType = tokenType;
    this.string = string;
  }

  /**
   * Gets the token type.
   *
   * @return the token type
   */
  public final TokenType getTokenType() {
    return this.tokenType;
  }

  /**
   * Gets the string.
   *
   * @return the string
   */
  public final String getString() {
    return this.string;
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public final String toString() {
    return String.format("(%s::%s)", this.tokenType, this.string);
  }
}
