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
