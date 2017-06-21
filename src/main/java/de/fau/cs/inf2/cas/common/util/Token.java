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
