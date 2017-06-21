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

import de.fau.cs.inf2.cas.common.util.Token.TokenType;

import java.util.Iterator;

public final class PackageParser {
  private final MiniLexer lexer;

  /**
   * Instantiates a new package parser.
   *
   * @param lexer the lexer
   */
  public PackageParser(final MiniLexer lexer) {
    this.lexer = lexer;
  }

  /**
   * Parses the package.
   *
   * @return the string
   */
  public final String parsePackage() {
    /*
     * PACKAGE := TOKEN_PACKAGE TOKEN_IDENTIFIER (TOKEN_DOT TOKEN_IDENTIFIER)* TOKEN_SEMIcOLON
     */

    final Iterator<Token> tokenIterator = this.lexer.iterator();

    final Token firstToken = tokenIterator.next();
    {
      if (firstToken.getTokenType() != Token.TokenType.TOKEN_PACKAGE) {
        return "";
      }
    }

    final StringBuilder builder = new StringBuilder();

    final Token firstPackagePart = tokenIterator.next();
    {
      if (firstPackagePart.getTokenType() != Token.TokenType.TOKEN_IDENTIFIER) {
        return null;
      }
    }
    builder.append(firstPackagePart.getString());

    Token nextToken;
    while ((nextToken = tokenIterator.next()).getTokenType() == Token.TokenType.TOKEN_DOT) {
      final Token packagePart = tokenIterator.next();
      if (packagePart.getTokenType() != Token.TokenType.TOKEN_IDENTIFIER) {
        return null;
      }

      builder.append(".");
      builder.append(packagePart.getString());
    }

    if (nextToken.getTokenType() != Token.TokenType.TOKEN_SEMIcOLON) {
      return null;
    }

    return builder.toString();
  }
}
