package de.fau.cs.inf2.cas.common.parser.odin;

import de.fau.cs.inf2.cas.common.parser.SyntaxError;

public class TokenChecker {
  static void aresTokenNotExpected(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type == BasicJavaToken.ARES_TOKEN) {
      throw new SyntaxError("//# not expected.", ((JavaToken) nextToken.token));
    }
  }

  static void assertNotExpected(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type == BasicJavaToken.ASSERT) {
      throw new SyntaxError("assert not allowed here!", ((JavaToken) nextToken.token));
    }
  }

  static void enumNotExpected(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type == BasicJavaToken.ENUM) {
      throw new SyntaxError("Enum used as variable name!", ((JavaToken) nextToken.token));
    }
  }

  static void expectAssert(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.ASSERT) {
      throw new SyntaxError("ASSERT expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectAt(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.AT) {
      throw new SyntaxError("'@' expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectBreak(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.BREAK) {
      throw new SyntaxError("BREAK expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectCatch(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.CATCH) {
      throw new SyntaxError("CATCH expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectClass(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.CLASS) {
      throw new SyntaxError("CLASS expected.", ((JavaToken) nextToken.token));
    }
  }

  /**
   * Expect colon.
   *
   * @param nextToken the next token
   */
  public static void expectColon(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.COLON) {
      throw new SyntaxError("':' expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectContinue(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.CONTINUE) {
      throw new SyntaxError("CONTINUE expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectDo(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.DO) {
      throw new SyntaxError("DO expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectEnum(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.ENUM) {
      throw new SyntaxError("ENUM expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectEqual(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.EQUAL) {
      throw new SyntaxError("'=' expected.", ((JavaToken) nextToken.token));
    }
  }

  /**
   * Expect equal equal.
   *
   * @param nextToken the next token
   */
  public static void expectEqualEqual(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.EQUAL_EQUAL) {
      throw new SyntaxError("'==' expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectFor(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.FOR) {
      throw new SyntaxError("FOR expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectGreater(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.GREATER) {
      throw new SyntaxError("'>' expected.", ((JavaToken) nextToken.token));
    }
  }


  static void expectIdentifier(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.IDENTIFIER) {
      throw new SyntaxError("Identifier expected.", nextToken.token);
    }
  }

  static void expectIf(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.IF) {
      throw new SyntaxError("IF expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectImport(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.IMPORT) {
      throw new SyntaxError("IMPORT expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectInterface(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.INTERFACE) {
      throw new SyntaxError("INTERFACE expected.", ((JavaToken) nextToken.token));
    }
  }

  /**
   * Expect left brace.
   *
   * @param nextToken the next token
   */
  public static void expectLeftBrace(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.LBRACE) {
      throw new SyntaxError("'{' expected.", ((JavaToken) nextToken.token));
    }
  }

  /**
   * Expect left parenthesis.
   *
   * @param nextToken the next token
   */
  public static void expectLeftParenthesis(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.LPAREN) {
      throw new SyntaxError("'(' expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectLess(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.LESS) {
      throw new SyntaxError("'<' expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectMultiply(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.MULTIPLY) {
      throw new SyntaxError("'*' expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectNew(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.NEW) {
      throw new SyntaxError("NEW expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectPackage(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.PACKAGE) {
      throw new SyntaxError("PACKAGE expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectPoint(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.POINT) {
      throw new SyntaxError("'.' expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectRichtBracket(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.RBRACKET) {
      throw new SyntaxError("']' expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectRightBrace(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.RBRACE) {
      throw new SyntaxError("'}' expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectRightBracket(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.RBRACKET) {
      throw new SyntaxError("']' expected.", ((JavaToken) nextToken.token));
    }
  }

  /**
   * Expect right parenthesis.
   *
   * @param nextToken the next token
   */
  public static void expectRightParenthesis(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.RPAREN) {
      throw new SyntaxError("')' expected.", ((JavaToken) nextToken.token));
    }
  }

  /**
   * Expect semicolon.
   *
   * @param nextToken the next token
   */
  public static void expectSemicolon(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.SEMICOLON) {
      throw new SyntaxError("';' expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectSwitch(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.SWITCH) {
      throw new SyntaxError("SWITCH expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectSynchronized(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.SYNCHRONIZED) {
      throw new SyntaxError("SYNCHRONIZED expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectTry(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.TRY) {
      throw new SyntaxError("TRY expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectWhile(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.WHILE) {
      throw new SyntaxError("WHILE expected.", ((JavaToken) nextToken.token));
    }
  }

  static boolean isAnd(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.AND;
  }

  /**
   * Checks if is ares token.
   *
   * @param nextToken the next token
   * @return true, if is ares token
   */
  public static boolean isAresToken(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.ARES_TOKEN;
  }


  static boolean isAt(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.AT;
  }

  static boolean isCase(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.CASE;
  }

  static boolean isCatch(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.CATCH;
  }

  static boolean isClass(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.CLASS;
  }

  static boolean isColon(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.COLON;
  }

  public static boolean isComma(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.COMMA;
  }

  static boolean isDefault(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.DEFAULT;
  }

  static boolean isElse(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.ELSE;
  }


  static boolean isEof(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.EOF;
  }

  static boolean isEqual(TokenAndHistory nextnextToken) {
    return ((JavaToken) nextnextToken.token).type == BasicJavaToken.EQUAL;
  }
  
  /**
   * Checks if is equal equal.
   *
   * @param nextnextToken the nextnext token
   * @return true, if is equal equal
   */
  public static boolean isEqualEqual(TokenAndHistory nextnextToken) {
    return ((JavaToken) nextnextToken.token).type == BasicJavaToken.EQUAL_EQUAL;
  }

  static boolean isExpressionToken(TokenAndHistory token) {
    switch (((JavaToken) token.token).type) {
      case OR_OR:
      case AND_AND:
      case OR:
      case XOR:
      case AND:
      case EQUAL_EQUAL:
      case NOT_EQUAL:
      case INSTANCEOF:
      case LESS:
      case LESS_EQUAL:
      case GREATER:
      case GREATER_EQUAL:
      case SAR:
      case SLR:
      case SLL:
      case PLUS:
      case MINUS:
      case MULTIPLY:
      case DIV:
      case REMAINDER:
        return true;
      default:
        return false;
    }
  }

  static boolean isExtends(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.EXTENDS;
  }

  static boolean isFinal(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.FINAL;
  }

  static boolean isFinally(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.FINALLY;
  }

  static boolean isGreater(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.GREATER;
  }

  /**
   * Checks if is identifier.
   *
   * @param nextToken the next token
   * @return true, if is identifier
   */
  public static boolean isIdentifier(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.IDENTIFIER;
  }

  static boolean isImplements(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.IMPLEMENTS;
  }

  static boolean isImport(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.IMPORT;
  }

  static boolean isInstanceOf(TokenAndHistory currentTokenAndHistory) {
    return ((JavaToken) currentTokenAndHistory.token).type == BasicJavaToken.INSTANCEOF;
  }

  /**
   * Checks if is integer literal.
   *
   * @param nextnextToken the nextnext token
   * @return true, if is integer literal
   */
  public static boolean isIntegerLiteral(TokenAndHistory nextnextToken) {
    return ((JavaToken) nextnextToken.token).type == BasicJavaToken.INTEGER_LITERAL;
  }

  static boolean isInterface(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.INTERFACE;
  }

  /**
   * Checks if is left brace.
   *
   * @param nextToken the next token
   * @return true, if is left brace
   */
  public static boolean isLeftBrace(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.LBRACE;
  }

  static boolean isLeftBracket(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.LBRACKET;
  }

  /**
   * Checks if is left parenthesis.
   *
   * @param nextToken the next token
   * @return true, if is left parenthesis
   */
  public static boolean isLeftParenthesis(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.LPAREN;
  }

  static boolean isLess(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.LESS;
  }

  static boolean isMinusMinus(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.MINUS_MINUS;
  }

  /**
   * Checks if is not ares token.
   *
   * @param nextToken the next token
   * @return true, if is not ares token
   */
  public static boolean isNotAresToken(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type != BasicJavaToken.ARES_TOKEN;
  }

  /**
   * Checks if is not case.
   *
   * @param nextToken the next token
   * @return true, if is not case
   */
  public static boolean isNotCase(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type != BasicJavaToken.CASE;
  }

  static boolean isNotComma(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type != BasicJavaToken.COMMA;
  }

  static boolean isNotDefault(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type != BasicJavaToken.DEFAULT;
  }

  /**
   * Checks if is not equal equal.
   *
   * @param nextToken the next token
   * @return true, if is not equal equal
   */
  public static boolean isNotEqualEqual(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type != BasicJavaToken.EQUAL_EQUAL;
  }

  static boolean isNotIdentifier(TokenAndHistory token) {
    return ((JavaToken) token.token).type != BasicJavaToken.IDENTIFIER;
  }

  static boolean isNotInterface(TokenAndHistory nextnextToken) {
    return ((JavaToken) nextnextToken.token).type != BasicJavaToken.INTERFACE;
  }

  /**
   * Checks if is not left parenthesis.
   *
   * @param nextNextToken the next next token
   * @return true, if is not left parenthesis
   */
  public static boolean isNotLeftParenthesis(TokenAndHistory nextNextToken) {
    return ((JavaToken) nextNextToken.token).type != BasicJavaToken.LPAREN;
  }

  static boolean isNotRightBrace(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type != BasicJavaToken.RBRACE;
  }

  static boolean isNotRightBracket(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type != BasicJavaToken.RBRACKET;
  }

  /**
   * Checks if is not right parenthesis.
   *
   * @param nextToken the next token
   * @return true, if is not right parenthesis
   */
  public static boolean isNotRightParenthesis(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type != BasicJavaToken.RPAREN;
  }

  static boolean isNotSemicolon(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type != BasicJavaToken.SEMICOLON;
  }

  static boolean isOr(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.OR;
  }

  static boolean isPackage(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.PACKAGE;
  }

  static boolean isPlusPlus(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.PLUS_PLUS;
  }

  static boolean isPoint(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.POINT;
  }

  static boolean isQuestionmark(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.QUESTION;
  }

  static boolean isRightBrace(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.RBRACE;
  }

  static boolean isRightBracket(TokenAndHistory nextnextToken) {
    return ((JavaToken) nextnextToken.token).type == BasicJavaToken.RBRACKET;
  }

  static boolean isRightParenthesis(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.RPAREN;
  }

  /**
   * Checks if is semicolon.
   *
   * @param nextToken the next token
   * @return true, if is semicolon
   */
  public static boolean isSemicolon(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.SEMICOLON;
  }

  static boolean isStatic(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.STATIC;
  }

  static boolean isSuper(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.SUPER;
  }

  static boolean isThrows(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.THROWS;
  }

  static void lessNotExpected(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type == BasicJavaToken.LESS) {
      throw new SyntaxError("'<' not expected.", ((JavaToken) nextToken.token));
    }
  }

  /**
   * Right brace not expected.
   *
   * @param nextToken the next token
   */
  public static void rightBraceNotExpected(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type == BasicJavaToken.RBRACE) {
      throw new SyntaxError("Unfinished statement. Semicolon expected.",
          ((JavaToken) nextToken.token));
    }
  }
}
