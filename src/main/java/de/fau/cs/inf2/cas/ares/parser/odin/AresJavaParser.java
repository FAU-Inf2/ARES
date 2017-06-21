package de.fau.cs.inf2.cas.ares.parser.odin;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresCaseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPluginClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastConstant;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastSpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.type.BastType;

import de.fau.cs.inf2.cas.common.parser.AresExtension;
import de.fau.cs.inf2.cas.common.parser.IParser;
import de.fau.cs.inf2.cas.common.parser.SyntaxError;
import de.fau.cs.inf2.cas.common.parser.odin.BasicJavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.FileData;
import de.fau.cs.inf2.cas.common.parser.odin.JavaLexer;
import de.fau.cs.inf2.cas.common.parser.odin.JavaParser;
import de.fau.cs.inf2.cas.common.parser.odin.JavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.ListToken;
import de.fau.cs.inf2.cas.common.parser.odin.ParseResult;
import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;
import de.fau.cs.inf2.cas.common.parser.odin.TokenChecker;

import java.io.File;
import java.util.LinkedList;

public class AresJavaParser extends JavaParser {
  private static <T> LinkedList<T> add(LinkedList<T> list, T obj) {
    if (obj != null) {
      if (list == null) {
        list = new LinkedList<T>();
      }
      list.add(obj);
    }
    return list;
  }

  /**
   * Gets the single instance of JavaParser.
   *
   * @return single instance of JavaParser
   */
  public static IParser getInstance() {
    return new AresJavaParser();
  }

  /**
   * Instantiates a new java parser.
   *
   * @param activateAresTokens the activate ARES tokens
   */
  private AresJavaParser() {
    super();
  }


  private ParseResult<AbstractBastStatement> aresBlock(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    AresTokenChecker.expectAresToken(nextToken);
    currentTokenAndHistory = nextToken;
    final TokenAndHistory[] tokens = new TokenAndHistory[12];
    nextToken = convertAresToken(lexer.nextToken(data, currentTokenAndHistory));
    AresTokenChecker.expectMatch(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[0] = currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectLeftParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[2] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    nextToken = convertAresToken(lexer.nextToken(data, currentTokenAndHistory));
    AresTokenChecker.expectOriginalOrModified(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[3] = currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<AbstractBastConstant> value = null;
    LinkedList<AbstractBastExpr> identifiers = null;

    if (TokenChecker.isComma(nextToken)) {
      TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
      if (TokenChecker.isIntegerLiteral(nextnextToken)) {
        currentTokenAndHistory = nextToken;
        tokens[4] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        value = literal(lexer, data, currentTokenAndHistory);
        currentTokenAndHistory = value.currentTokenAndHistory;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
      }
      if (TokenChecker.isComma(nextToken)) {
        currentTokenAndHistory = nextToken;
        tokens[5] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        TokenChecker.expectLeftParenthesis(nextToken);
        currentTokenAndHistory = nextToken;
        tokens[6] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        ParseResult<LinkedList<AbstractBastExpr>> identifiersResult = null;
        identifiersResult = arguments(lexer, data, currentTokenAndHistory);
        tokens[7] = new TokenAndHistory(new ListToken(identifiersResult.additionalTokens));
        currentTokenAndHistory = identifiersResult.currentTokenAndHistory;
        identifiers = identifiersResult.value;
        TokenChecker.expectRightParenthesis(currentTokenAndHistory);
        tokens[8] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
      }
    }
    TokenChecker.expectRightParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[9] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectLeftBrace(nextToken);
    ParseResult<BastBlock> block = block(lexer, data, currentTokenAndHistory, true);
    currentTokenAndHistory = block.currentTokenAndHistory;
    if (value != null) {
      return new ParseResult<AbstractBastStatement>(
          new AresBlock(tokens, (BastIntConst) value.value, block.value, identifiers),
          currentTokenAndHistory);
    } else {
      return new ParseResult<AbstractBastStatement>(
          new AresBlock(tokens, null, block.value, identifiers), currentTokenAndHistory);
    }
  }



  private ParseResult<AbstractBastStatement> aresCaseStmt(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isNotAresToken(nextToken)) {
      return new ParseResult<AbstractBastStatement>(null, null);
    }
    currentTokenAndHistory = nextToken;
    final TokenAndHistory[] tokens = new TokenAndHistory[5];

    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isNotCase(nextToken)) {
      return new ParseResult<AbstractBastStatement>(null, null);
    }
    currentTokenAndHistory = nextToken;
    tokens[0] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectLeftBrace(nextToken);
    ParseResult<BastBlock> block = block(lexer, data, currentTokenAndHistory, true);
    currentTokenAndHistory = block.currentTokenAndHistory;
    return new ParseResult<AbstractBastStatement>(new AresCaseStmt(tokens, block.value),
        currentTokenAndHistory);
  }



  private ParseResult<AbstractBastStatement> aresChoiceStmt(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    AresTokenChecker.expectAresToken(nextToken);
    currentTokenAndHistory = nextToken;
    TokenAndHistory[] tokens = new TokenAndHistory[7];
    tokens[0] = currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);

    nextToken = convertAresToken(lexer.nextToken(data, currentTokenAndHistory));
    AresTokenChecker.expectChoice(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[0] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isLeftParenthesis(nextToken)) {
      currentTokenAndHistory = nextToken;
      tokens[2] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      TokenChecker.expectRightParenthesis(nextToken);
      currentTokenAndHistory = nextToken;
      tokens[3] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    TokenChecker.expectLeftBrace(nextToken);
    ParseResult<BastBlock> block = block(lexer, data, currentTokenAndHistory, true);
    currentTokenAndHistory = block.currentTokenAndHistory;
    return new ParseResult<AbstractBastStatement>(new AresChoiceStmt(tokens, block.value),
        currentTokenAndHistory);
  }



  private ParseResult<AresPatternClause> aresPatternClause(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory[] tokens = new TokenAndHistory[5];
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = convertAresToken(lexer.nextToken(data, currentTokenAndHistory));
    if (nextToken != null && AresTokenChecker.isNotPattern(nextToken)
        && TokenChecker.isNotLeftParenthesis(nextToken)) {
      throw new SyntaxError("Pattern or '(' expected.", ((JavaToken) nextToken.token));
    }
    if (nextToken != null && AresTokenChecker.isPattern(nextToken)) {
      currentTokenAndHistory = nextToken;
      tokens[0] = currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    if (nextToken == null) {
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    TokenChecker.expectLeftParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[1] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    nextToken = lexer.nextToken(data, currentTokenAndHistory);

    ParseResult<AbstractBastExpr> expr1 =
        expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
    ParseResult<AbstractBastExpr> expr2 = null;
    ParseResult<AbstractBastExpr> expr3 = null;
    ParseResult<AbstractBastStatement> expr4 = null;
    ParseResult<BastType> expr5 = null;
    ParseResult<AbstractBastSpecifier> expr6 = null;
    if (expr1 == null) {
      assert (false);
      return new ParseResult<AresPatternClause>(null, null);
    }
    currentTokenAndHistory = expr1.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    AbstractBastNode exprValue = null;
    if (TokenChecker.isComma(nextToken)) {
      currentTokenAndHistory = nextToken;
      tokens[2] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      expr2 = expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
      if (expr2.value == null || expr2.currentTokenAndHistory == null) {
        try {
          expr4 = switchLabel(lexer, data, currentTokenAndHistory);
          currentTokenAndHistory = expr4.currentTokenAndHistory;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
          exprValue = expr4.value;
        } catch (SyntaxError e) {
          expr5 = type(lexer, data, currentTokenAndHistory);
          if (expr5.value == null) {
            expr6 = modifier(lexer, data, currentTokenAndHistory, true);
            currentTokenAndHistory = expr6.currentTokenAndHistory;
            nextToken = lexer.nextToken(data, currentTokenAndHistory);
            exprValue = expr6.value;
          } else {
            currentTokenAndHistory = expr5.currentTokenAndHistory;
            nextToken = lexer.nextToken(data, currentTokenAndHistory);
            exprValue = expr5.value;
          }
        }

      } else {
        nextToken = lexer.nextToken(data, expr2.currentTokenAndHistory);
        if (TokenChecker.isIdentifier(nextToken)) {

          ParseResult<BastDeclaration> tmp =
              localVariableDeclarationStatement(lexer, data, currentTokenAndHistory, false);
          exprValue = tmp.value;
          currentTokenAndHistory = tmp.currentTokenAndHistory;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);

        } else {

          exprValue = expr2.value;
          currentTokenAndHistory = expr2.currentTokenAndHistory;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
        }
      }
      if (TokenChecker.isComma(nextToken)) {
        currentTokenAndHistory = nextToken;
        tokens[3] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        if (TokenChecker.isNotRightParenthesis(nextToken)) {

          expr3 = expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);

          currentTokenAndHistory = expr3.currentTokenAndHistory;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
        }

      }
    }
    TokenChecker.expectRightParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[4] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    return createPatternClause(tokens, currentTokenAndHistory, expr1, expr2, expr3, exprValue);
  }

  private ParseResult<AresPatternClause> createPatternClause(TokenAndHistory[] tokens,
      TokenAndHistory currentTokenAndHistory, ParseResult<AbstractBastExpr> expr1,
      ParseResult<AbstractBastExpr> expr2, ParseResult<AbstractBastExpr> expr3,
      AbstractBastNode exprValue) {
    AresPatternClause clause = null;
    if (expr3 != null) {
      if (expr1.value.getTag() == BastIntConst.TAG) {
        clause = new AresPatternClause(tokens, (BastIntConst) expr1.value, exprValue,
            (BastNameIdent) expr3.value);
      } else {
        clause = new AresPatternClause(tokens, (BastNameIdent) expr1.value, exprValue,
            (BastIntConst) expr3.value);
      }

    } else if (expr2 != null) {
      clause = new AresPatternClause(tokens, (BastIntConst) expr1.value, exprValue, null);
    } else {
      assert (expr1 != null);
      if (expr1.value.getTag() == BastNameIdent.TAG) {
        clause = new AresPatternClause(tokens, null, null, (BastNameIdent) expr1.value);
      } else {
        clause = new AresPatternClause(tokens, (BastIntConst) null, expr1.value, null);
      }
    }
    return new ParseResult<AresPatternClause>(clause, currentTokenAndHistory);
  }



  private ParseResult<AresPluginClause> aresPlugin(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory[] tokens = new TokenAndHistory[4];
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = convertAresToken(lexer.nextToken(data, currentTokenAndHistory));
    if (nextToken != null && AresTokenChecker.isNotPlugin(nextToken)) {
      throw new SyntaxError("Plugin expected.", ((JavaToken) nextToken.token));
    }
    if (nextToken != null && AresTokenChecker.isPlugin(nextToken)) {
      currentTokenAndHistory = nextToken;
      tokens[0] = currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      TokenChecker.expectColon(nextToken);
      currentTokenAndHistory = nextToken;
      tokens[1] = currentTokenAndHistory;
    }
    ParseResult<BastNameIdent> ident = identifier(lexer, data, currentTokenAndHistory);
    assert (ident != null);
    currentTokenAndHistory = ident.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    LinkedList<AbstractBastExpr> list = null;

    if (TokenChecker.isLeftParenthesis(nextToken)) {
      TokenAndHistory nextNextToken = convertAresToken(lexer.nextToken(data, nextToken));
      if (nextNextToken != null && AresTokenChecker.isPattern(nextNextToken)) {
        currentTokenAndHistory = nextToken;
        tokens[2] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
      }
      ParseResult<AbstractBastExpr> expr = null;
      ParseResult<AresPatternClause> pattern = null;
      if (TokenChecker.isNotRightParenthesis(nextToken)) {
        if (TokenChecker.isIdentifier(nextToken) || TokenChecker.isLeftParenthesis(nextToken)) {
          pattern = aresPatternClause(lexer, data, currentTokenAndHistory);
          list = add(list, pattern.value);
          currentTokenAndHistory = pattern.currentTokenAndHistory;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
        } else {
          expr = expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
          currentTokenAndHistory = expr.currentTokenAndHistory;
          list = add(list, expr.value);
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
        }
        while (TokenChecker.isComma(nextToken)) {
          if (AresTokenChecker.isPattern(nextToken)) {
            pattern = aresPatternClause(lexer, data, currentTokenAndHistory);
            list = add(list, expr.value);
            currentTokenAndHistory = expr.currentTokenAndHistory;
            nextToken = lexer.nextToken(data, currentTokenAndHistory);
          } else {
            currentTokenAndHistory = nextToken;
            expr = expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
            list = add(list, expr.value);
            currentTokenAndHistory = expr.currentTokenAndHistory;
            nextToken = lexer.nextToken(data, currentTokenAndHistory);
          }
        }
      }
      if (tokens[2] != null && TokenChecker.isNotRightParenthesis(nextToken)) {
        throw new SyntaxError("')' expected.", ((JavaToken) nextToken.token));
      }
      if (tokens[2] != null) {
        currentTokenAndHistory = nextToken;
        tokens[3] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      }
    }
    AresPluginClause clause = new AresPluginClause(tokens, ident.value, list);
    return new ParseResult<AresPluginClause>(clause, currentTokenAndHistory);
  }



  /**
   * Ares statement.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<AbstractBastStatement> aresStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    AresTokenChecker.expectAresToken(nextToken);
    TokenAndHistory nextnextToken = convertAresToken(lexer.nextToken(data, nextToken));

    switch (((JavaToken) nextnextToken.token).type) {
      case MATCH:
        return aresBlock(lexer, data, inputTokenAndHistory);
      case WILDCARD:
        return aresWildcard(lexer, data, inputTokenAndHistory);
      case CHOICE:
        return aresChoiceStmt(lexer, data, inputTokenAndHistory);
      case USE:
        return aresUse(lexer, data, inputTokenAndHistory);
      case CASE:
        return aresCaseStmt(lexer, data, inputTokenAndHistory);
      default:
        throw new SyntaxError("ARES token expected.", ((JavaToken) nextToken.token));
    }
  }

  private ParseResult<AbstractBastStatement> aresUse(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    AresTokenChecker.expectAresToken(nextToken);
    currentTokenAndHistory = nextToken;
    TokenAndHistory[] tokens = new TokenAndHistory[9];
    tokens[0] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    nextToken = lexer.nextToken(data, currentTokenAndHistory);

    nextToken = convertAresToken(lexer.nextToken(data, currentTokenAndHistory));
    AresTokenChecker.expectUse(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[1] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<BastNameIdent> ident = null;
    AresPatternClause clause = null;

    if (TokenChecker.isLeftParenthesis(nextToken)) {
      TokenAndHistory nextNextToken = lexer.nextToken(data, nextToken);
      TokenAndHistory nextNextNextToken = lexer.nextToken(data, nextNextToken);
      ParseResult<AresPatternClause> clauseRes = null;
  
      if (TokenChecker.isEqualEqual(nextNextNextToken)) {
        currentTokenAndHistory = nextToken;
        tokens[2] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        if (TokenChecker.isNotEqualEqual(nextToken)
            && TokenChecker.isNotRightParenthesis(nextToken)) {
          ident = identifier(lexer, data, currentTokenAndHistory);
          currentTokenAndHistory = ident.currentTokenAndHistory;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
        }
        if (TokenChecker.isNotRightParenthesis(nextToken)) {
          TokenChecker.expectEqualEqual(nextToken);
          currentTokenAndHistory = nextToken;
          tokens[3] = currentTokenAndHistory;
          tokens[4] = null;
          clauseRes = aresPatternClause(lexer, data, currentTokenAndHistory);
          currentTokenAndHistory = clauseRes.currentTokenAndHistory;
          clause = clauseRes.value;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
        }
        TokenChecker.expectRightParenthesis(nextToken);
        currentTokenAndHistory = nextToken;
        tokens[5] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
      } else {
        clauseRes = aresPatternClause(lexer, data, currentTokenAndHistory);
        currentTokenAndHistory = clauseRes.currentTokenAndHistory;
        clause = clauseRes.value;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
      }
    }
    LinkedList<AbstractBastStatement> stmts = null;
    if (TokenChecker.isSemicolon(nextToken)) {
      currentTokenAndHistory = nextToken;
      tokens[6] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    } 
    BastNameIdent identValue = null;
    if (ident != null) {
      identValue = ident.value;
    }
    return new ParseResult<AbstractBastStatement>(
        new AresUseStmt(tokens, identValue, clause, stmts), currentTokenAndHistory);
  }



  private ParseResult<AbstractBastStatement> aresWildcard(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    AresTokenChecker.expectAresToken(nextToken);
    currentTokenAndHistory = nextToken;
    TokenAndHistory[] tokens = new TokenAndHistory[4];
    tokens[0] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    nextToken = convertAresToken(lexer.nextToken(data, currentTokenAndHistory));
    AresTokenChecker.expectWildcard(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[1] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    ParseResult<AresPluginClause> plugin = null;
    ParseResult<LinkedList<AbstractBastStatement>> stmtsRes = null;
    nextToken = convertAresToken(lexer.nextToken(data, currentTokenAndHistory));
    if (nextToken == null || AresTokenChecker.isPlugin(nextToken)) {
      plugin = aresPlugin(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = plugin.currentTokenAndHistory;
    } else {
      throw new SyntaxError("ARES TOKEN not expected!", ((JavaToken) nextToken.token));
    }
    LinkedList<AbstractBastStatement> stmts = null;

    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isSemicolon(nextToken)) {
      currentTokenAndHistory = nextToken;
      tokens[2] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    } else if (TokenChecker.isLeftBrace(nextToken)) {
      stmtsRes = blockStatementList(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = stmtsRes.currentTokenAndHistory;
      stmts = stmtsRes.value;
    }

    return new ParseResult<AbstractBastStatement>(new AresWildcard(tokens, plugin.value, stmts),
        currentTokenAndHistory);
  }



  /**
   * Statement.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<AbstractBastStatement> statement(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isAresToken(nextToken)) {
      return aresStatement(lexer, data, currentTokenAndHistory);
    }
    return innerStatement(lexer, data, inputTokenAndHistory);
  }



  @Override
  protected ParseResult<LinkedList<AbstractBastStatement>> switchBlockStatementGroups(
      final JavaLexer lexer, final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // SwitchBlockStatementGroups:
    // { SwitchBlockStatementGroup }


    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<AbstractBastStatement> switchGroup = null;
    if (TokenChecker.isAresToken(nextToken)) {
      switchGroup = aresStatement(lexer, data, inputTokenAndHistory);
    } else {
      switchGroup = switchBlockStatementGroup(lexer, data, currentTokenAndHistory);
    }
    if (switchGroup.value == null) {
      return new ParseResult<LinkedList<AbstractBastStatement>>(null, currentTokenAndHistory);
    }
    currentTokenAndHistory = switchGroup.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    LinkedList<AbstractBastStatement> list = null;
    list = add(list, switchGroup.value);

    while (((JavaToken) nextToken.token).type != BasicJavaToken.RBRACE) {
      if (TokenChecker.isAresToken(nextToken)) {
        switchGroup = aresStatement(lexer, data, inputTokenAndHistory);
      } else {
        switchGroup = switchBlockStatementGroup(lexer, data, currentTokenAndHistory);
      }
      currentTokenAndHistory = switchGroup.currentTokenAndHistory;
      if (currentTokenAndHistory == null) {
        throw new SyntaxError("Unexpected token.", ((JavaToken) nextToken.token));
      }
      list = add(list, switchGroup.value);
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    return new ParseResult<LinkedList<AbstractBastStatement>>(list, currentTokenAndHistory);

  }

  /**
   * Parses the.
   *
   * @param data the data
   * @return the bast program
   */
  @Override
  public BastProgram parse(byte[] data) {
    JavaLexer lexer = new JavaLexer(AresExtension.WITH_ARES_EXTENSIONS);
    return parse(data, lexer);
  }

  /**
   * Parses the.
   *
   * @param file the file
   * @return the bast program
   */
  public BastProgram parse(File file) {
    JavaLexer lexer = new JavaLexer(AresExtension.WITH_ARES_EXTENSIONS);
    return parse(file, lexer);

  }
}
