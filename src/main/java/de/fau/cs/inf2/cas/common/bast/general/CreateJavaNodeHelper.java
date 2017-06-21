package de.fau.cs.inf2.cas.common.bast.general;

import de.fau.cs.inf2.cas.ares.bast.general.ParserFactory;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresCaseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPluginClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.parser.odin.AresJavaParser;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAccess;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAdditiveExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAnd;
import de.fau.cs.inf2.cas.common.bast.nodes.BastArrayRef;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAsgnExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBoolConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBreak;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCatchClause;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCmp;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondAnd;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondOr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDecrExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprList;
import de.fau.cs.inf2.cas.common.bast.nodes.BastForStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIncrExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastInstanceOf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastListInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastMultiExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNew;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNullConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastOr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameter;
import de.fau.cs.inf2.cas.common.bast.nodes.BastRealConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastReturn;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStringConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSuper;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitch;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitchCaseGroup;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSynchronizedBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastThis;
import de.fau.cs.inf2.cas.common.bast.nodes.BastThrowStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTryStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeArgument;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeQualifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeSpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastUnaryExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastWhileStatement;
import de.fau.cs.inf2.cas.common.bast.type.BastArrayType;
import de.fau.cs.inf2.cas.common.bast.type.BastBasicType;
import de.fau.cs.inf2.cas.common.bast.type.BastClassType;
import de.fau.cs.inf2.cas.common.bast.type.BastType;
import de.fau.cs.inf2.cas.common.bast.visitors.IPrettyPrinter;

import de.fau.cs.inf2.cas.common.parser.AresExtension;
import de.fau.cs.inf2.cas.common.parser.IGeneralToken;
import de.fau.cs.inf2.cas.common.parser.odin.BasicJavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.FileData;
import de.fau.cs.inf2.cas.common.parser.odin.JavaLexer;
import de.fau.cs.inf2.cas.common.parser.odin.JavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.ParseResult;
import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;

public class CreateJavaNodeHelper {

  /**
   * Creates the bast class.
   *
   * @param name the name
   * @return the bast class decl
   */
  public static BastClassDecl createBastClass(String name) {
    return createBastClass(null, createBastNameIdent(name), null, null);
  }

  private static BastClassDecl createBastClass(TokenAndHistory structureToken, BastNameIdent name,
      LinkedList<BastTypeArgument> param, BastType subClass) {
    TokenAndHistory[] tokens = new TokenAndHistory[5];
    JavaToken token = new JavaToken(BasicJavaToken.CLASS, "class");
    tokens[0] = new TokenAndHistory(token);
    if (structureToken != null) {
      tokens[0].prevTokens.addAll(structureToken.prevTokens);
    }
    ((JavaToken) name.info.tokens[0].token).whiteSpace.append(" ");
    token = new JavaToken(BasicJavaToken.LBRACE, "{");
    token.whiteSpace.append(" \n");
    tokens[3] = new TokenAndHistory(token);
    token = new JavaToken(BasicJavaToken.RBRACE, "}");
    token.whiteSpace.append("\n");
    tokens[4] = new TokenAndHistory(token);

    BastClassDecl node = new BastClassDecl(tokens, name, null, null, null, null);
    return node;
  }

  /**
   * Creates the bast class type.
   *
   * @param name the name
   * @return the bast class type
   */
  public static BastClassType createBastClassType(String name) {
    return createBastClassType(null, createBastNameIdent(name), null, null);
  }

  private static BastClassType createBastClassType(TokenAndHistory structureToken,
      BastNameIdent name, LinkedList<BastTypeArgument> param, BastType subClass) {
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    if (structureToken != null) {
      tokens[0].prevTokens.addAll(structureToken.prevTokens);
    }
    BastClassType node = new BastClassType(tokens, name, null, null);
    return node;
  }

  /**
   * Creates the bast name ident.
   *
   * @param name the name
   * @return the bast name ident
   */
  public static BastNameIdent createBastNameIdent(String name) {
    return createBastNameIdent(null, name);
  }

  private static BastNameIdent createBastNameIdent(TokenAndHistory structureToken, String name) {
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    JavaToken token = new JavaToken(BasicJavaToken.STRING_LITERAL, name);
    tokens[0] = new TokenAndHistory(token);
    if (structureToken != null) {
      tokens[0].prevTokens.addAll(structureToken.prevTokens);
    }
    BastNameIdent node = new BastNameIdent(tokens, name);
    return node;
  }

  /**
   * Creates the bast int const.
   *
   * @param value the value
   * @return the bast int const
   */
  public static BastIntConst createBastIntConst(long value) {
    return createBastIntConst(null, value);
  }

  private static BastIntConst createBastIntConst(TokenAndHistory structureToken, long value) {
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    JavaToken token = new JavaToken(BasicJavaToken.INTEGER_LITERAL, String.valueOf(value));
    tokens[0] = new TokenAndHistory(token);
    if (structureToken != null) {
      tokens[0].prevTokens.addAll(structureToken.prevTokens);
    }
    BastIntConst node = new BastIntConst(tokens, value);
    return node;
  }

  /**
   * Creates the bast switch case group.
   *
   * @return the bast switch case group
   */
  public static BastSwitchCaseGroup createBastSwitchCaseGroup() {
    BastSwitchCaseGroup node =
        new BastSwitchCaseGroup(null, new LinkedList<>(), new LinkedList<>());
    return node;
  }

  private static JavaToken createToken(String prevText, BasicJavaToken type) {
    JavaToken token = new JavaToken(type, type.getName());
    if (prevText != null) {
      token.whiteSpace.append(prevText);
    }
    return token;
  }

  /**
   * Creates the token history.
   *
   * @param type the type
   * @return the token and history
   */
  public static TokenAndHistory createTokenHistory(BasicJavaToken type) {
    return createTokenHistory(null, null, type, false);
  }

  /**
   * Creates the token history.
   *
   * @param prevText the prev text
   * @param type the type
   * @return the token and history
   */
  public static TokenAndHistory createTokenHistory(String prevText, BasicJavaToken type) {
    return createTokenHistory(null, prevText, type, false);
  }

  private static TokenAndHistory createTokenHistory(TokenAndHistory structureToken,
      BasicJavaToken type, boolean removeAdditionalReturn) {
    return createTokenHistory(structureToken, null, type, removeAdditionalReturn);
  }

  private static TokenAndHistory createTokenHistory(TokenAndHistory structureToken,
      BasicJavaToken type) {
    return createTokenHistory(structureToken, null, type, false);
  }

  private static TokenAndHistory createTokenHistory(TokenAndHistory structureToken, String prevText,
      BasicJavaToken type, boolean removeAdditionalReturn) {
    TokenAndHistory token = new TokenAndHistory(createToken(prevText, type));
    if (structureToken != null) {
      for (IGeneralToken t : structureToken.prevTokens) {
        token.prevTokens.add(t.clone());
      }
      if (removeAdditionalReturn) {
        ((JavaToken) token.token).whiteSpace.append(
            removeAdditionalReturn(((JavaToken) structureToken.token).whiteSpace.toString()));
      } else {
        ((JavaToken) token.token).whiteSpace
            .append(((JavaToken) structureToken.token).whiteSpace.toString());
      }
    }
    return token;
  }

  /**
   * Creates the ARES plugin clause.
   *
   * @param structureToken the structure token
   * @param ident the ident
   * @param exprList the expr list
   * @return the ARES plugin clause
   */
  public static AresPluginClause createAresPluginClause(TokenAndHistory structureToken,
      BastNameIdent ident, LinkedList<AbstractBastExpr> exprList) {
    TokenAndHistory[] tokens = new TokenAndHistory[4];
    AresPluginClause clause = new AresPluginClause(tokens, ident, exprList);
    return clause;
  }

  /**
   * Creates the ARES pattern clause.
   *
   * @param structureToken the structure token
   * @param occurence the occurence
   * @param expr the expr
   * @param name the name
   * @return the ARES pattern clause
   */
  public static AresPatternClause createAresPatternClause(TokenAndHistory structureToken,
      BastIntConst occurence, AbstractBastNode expr, BastNameIdent name) {
    return createAresPatternClause(structureToken, occurence, expr, name, false);
  }

  /**
   * Creates the ARES pattern clause.
   *
   * @param structureToken the structure token
   * @param occurence the occurence
   * @param expr the expr
   * @param name the name
   * @param forUseClause the for use clause
   * @return the ARES pattern clause
   */
  public static AresPatternClause createAresPatternClause(TokenAndHistory structureToken,
      BastIntConst occurence, AbstractBastNode expr, BastNameIdent name, boolean forUseClause) {
    TokenAndHistory[] tokens = new TokenAndHistory[5];
    tokens[1] = createTokenHistory(structureToken, BasicJavaToken.LPAREN, false);

    if (occurence != null) {
      tokens[2] = createTokenHistory(structureToken, BasicJavaToken.COMMA);
      if (expr != null) {
        addWhiteSpace(expr, " ");
      }
      tokens[3] = createTokenHistory(structureToken, BasicJavaToken.COMMA);
      if (occurence != null) {
        addWhiteSpace(occurence, " ");
      }
    }

    tokens[4] = createTokenHistory(structureToken, BasicJavaToken.RPAREN);
    if (expr != null && expr.getTag() == BastAdditiveExpr.TAG) {
      IPrettyPrinter printer = ParserFactory.getPrettyPrinter();
      ((BastAdditiveExpr) expr).right.accept(printer);
      String tmp = printer.getBuffer().toString().trim();
      if (tmp.startsWith("(") && !tmp.endsWith(")")) {
        tokens[3].prevTokens.add(createTokenHistory(BasicJavaToken.RPAREN).token);
      }
    }
    AresPatternClause node = new AresPatternClause(tokens, name, expr, occurence);
    return node;
  }

  /**
   * Adds the return char.
   *
   * @param node the node
   */
  public static void addReturnChar(AbstractBastNode node) {
    if (node.getTag() == AresWildcard.TAG || node.getTag() == AresUseStmt.TAG) {
      if (!((JavaToken) node.info.tokens[0].token).whiteSpace.toString().contains("\n")) {
        ((JavaToken) node.info.tokens[0].token).whiteSpace.append("\n");
      }
    }
  }

  /**
   * Creates the ARES wildcard.
   *
   * @param whiteSpace the white space
   * @param structureToken the structure token
   * @param plugin the plugin
   * @param statements the statements
   * @return the ARES wildcard
   */
  public static AresWildcard createAresWildcard(String whiteSpace, TokenAndHistory structureToken,
      AresPluginClause plugin, LinkedList<AbstractBastStatement> statements) {
    TokenAndHistory[] tokens = new TokenAndHistory[3];
    tokens[0] = createTokenHistory(whiteSpace, BasicJavaToken.ARES_TOKEN);
    tokens[1] = createTokenHistory(" ", BasicJavaToken.WILDCARD);
    if (statements == null && !plugin.ident.name.equals("expr")) {
      tokens[2] = createTokenHistory(BasicJavaToken.SEMICOLON);
    }

    AresWildcard node = new AresWildcard(tokens, plugin, statements);
    return node;
  }

  /**
   * Adds white space to an AST node.
   *
   * @param node the AST node
   * @param whitespace the whitespace to add
   */
  public static void addWhiteSpace(AbstractBastNode node, String whitespace) {

    JavaToken javaToken = findLeftJavaToken(node);
    if (node == null || javaToken == null) {
      return;
    }
    javaToken.whiteSpace.insert(0, whitespace);
  }

  /**
   * Creates the ARES use.
   *
   * @param whiteSpace the white space
   * @param structureToken the structure token
   * @param name the name
   * @param pattern the pattern
   * @param statements the statements
   * @return the ARES use stmt
   */
  public static AresUseStmt createAresUse(String whiteSpace, TokenAndHistory structureToken,
      BastNameIdent name, AresPatternClause pattern, LinkedList<AbstractBastStatement> statements) {
    TokenAndHistory[] tokens = new TokenAndHistory[6];
    tokens[0] = createTokenHistory(whiteSpace, BasicJavaToken.ARES_TOKEN);
    tokens[1] = createTokenHistory(" ", BasicJavaToken.USE);
    if (pattern == null || pattern.expr == null) {
      tokens[5] = createTokenHistory(BasicJavaToken.SEMICOLON);
    }

    AresUseStmt node = new AresUseStmt(tokens, name, pattern, statements);
    return node;
  }

  private static String removeAdditionalReturn(String text) {
    return text.replace("\n\t\n\t", "\n\t");

  }

  /**
   * Creates the ARES case.
   *
   * @param whiteSpace the white space
   * @param structureToken the structure token
   * @param statements the statements
   * @return the ARES case stmt
   */
  public static AresCaseStmt createAresCase(String whiteSpace, TokenAndHistory structureToken,
      LinkedList<AbstractBastStatement> statements) {
    TokenAndHistory[] tokens = new TokenAndHistory[4];
    final TokenAndHistory[] blocktokens = new TokenAndHistory[4];
    String structureTokenString = "";
    if (structureToken != null) {
      structureTokenString =
          removeAdditionalReturn(((JavaToken) structureToken.token).whiteSpace.toString());
    }
    JavaToken expToken =
        new JavaToken(BasicJavaToken.ARES_TOKEN, structureTokenString + whiteSpace + "//#");
    tokens[0] = createTokenHistory(" ", BasicJavaToken.CASE);
    tokens[0].prevTokens.add(expToken);
    blocktokens[1] = createTokenHistory(" ", BasicJavaToken.LBRACE);
    blocktokens[2] = createTokenHistory(structureToken, BasicJavaToken.ARES_TOKEN, true);
    blocktokens[3] = createTokenHistory(" ", BasicJavaToken.RBRACE);

    BastBlock block = new BastBlock(blocktokens, statements);

    AresCaseStmt node = new AresCaseStmt(tokens, block);
    return node;
  }

  /**
   * Creates the ARES choice.
   *
   * @param whiteSpace the white space
   * @param structureToken the structure token
   * @param statements the statements
   * @return the ARES choice stmt
   */
  public static AresChoiceStmt createAresChoice(String whiteSpace, TokenAndHistory structureToken,
      LinkedList<AbstractBastStatement> statements) {
    TokenAndHistory[] tokens = new TokenAndHistory[7];
    final TokenAndHistory[] blocktokens = new TokenAndHistory[4];
    tokens[0] = createTokenHistory(structureToken, whiteSpace, BasicJavaToken.ARES_TOKEN, true);
    tokens[1] = createTokenHistory(" ", BasicJavaToken.CHOICE);
    blocktokens[0] = createTokenHistory(" ", BasicJavaToken.LBRACE);
    blocktokens[2] = createTokenHistory(structureToken, BasicJavaToken.ARES_TOKEN, true);
    blocktokens[3] = createTokenHistory(" ", BasicJavaToken.RBRACE);
    BastBlock block = new BastBlock(blocktokens, statements);

    AresChoiceStmt node = new AresChoiceStmt(tokens, block);
    return node;
  }

  /**
   * Creates the ARES block.
   *
   * @param structureToken the structure token
   * @param value the value
   * @param original the original
   * @param statements the statements
   * @param identifiers the identifiers
   * @return the ARES block
   */
  public static AresBlock createAresBlock(TokenAndHistory structureToken, int value,
      boolean original, LinkedList<AbstractBastStatement> statements,
      LinkedList<AbstractBastExpr> identifiers) {
    return createAresBlock(structureToken, createBastIntConst(value), original, statements,
        identifiers);
  }

  private static AresBlock createAresBlock(TokenAndHistory structureToken, BastIntConst value,
      boolean original, LinkedList<AbstractBastStatement> statements,
      LinkedList<AbstractBastExpr> identifiers) {
    TokenAndHistory[] tokens = new TokenAndHistory[10];
    tokens[0] = createTokenHistory(structureToken, BasicJavaToken.ARES_TOKEN);
    tokens[1] = createTokenHistory(" ", BasicJavaToken.MATCH);
    tokens[2] = createTokenHistory(" ", BasicJavaToken.LPAREN);
    if (original) {
      tokens[3] = createTokenHistory(BasicJavaToken.ORIGINAL);
    } else {
      tokens[3] = createTokenHistory(BasicJavaToken.MODIFIED);
    }
    tokens[8] = createTokenHistory(BasicJavaToken.RPAREN);
    tokens[9] = createTokenHistory(BasicJavaToken.RPAREN);

    assert (statements != null);
    TokenAndHistory[] blocktokens = new TokenAndHistory[4];
    blocktokens[0] = createTokenHistory(" ", BasicJavaToken.LBRACE);
    blocktokens[2] = createTokenHistory(BasicJavaToken.ARES_TOKEN);
    blocktokens[3] = createTokenHistory(" ", BasicJavaToken.RBRACE);
    BastBlock block = new BastBlock(blocktokens, statements);
    AresBlock node = new AresBlock(tokens, null, block, identifiers);
    return node;
  }

  /**
   * Creates the block.
   *
   * @param structureToken the structure token
   * @param statements the statements
   * @return the bast block
   */
  public static BastBlock createBlock(TokenAndHistory structureToken,
      LinkedList<AbstractBastStatement> statements) {
    TokenAndHistory[] tokens = new TokenAndHistory[4];
    tokens[0] = createTokenHistory(structureToken, BasicJavaToken.LBRACE);
    assert (statements != null);
    tokens[3] = createTokenHistory(" ", BasicJavaToken.RBRACE);
    BastBlock node = new BastBlock(tokens, statements);
    return node;

  }

  @SuppressWarnings("unused")
  private static String replaceLast(String string, String from, String to) {
    int lastIndex = string.lastIndexOf(from);
    if (lastIndex < 0) {
      return string;
    }
    String tail = string.substring(lastIndex).replaceFirst(from, to);
    return string.substring(0, lastIndex) + tail;
  }

  public static AbstractBastNode cloneTree(AbstractBastNode node) {
    return cloneTree(node, false);
  }

  /**
   * Clone tree.
   *
   * @param node the node
   * @return the abstract bast node
   */
  public static AbstractBastNode cloneTree(AbstractBastNode node, boolean verbose) {
    if (node == null) {
      return null;
    }
    if (node.getTag() == BastExprList.TAG) {
      if (((BastExprList) node).list.size() == 1) {
        node = ((BastExprList) node).list.get(0);
      }
    }
    IPrettyPrinter print = ParserFactory.getAresPrettyPrinter();
    if (print == null || node == null) {
      assert (false);
      return null;
    }
    node.accept(print);
    String tmp = print.getBuffer().toString();
    tmp = tmp.trim();
    byte[] bytes = tmp.toString().getBytes();
    FileData data = new FileData(bytes);
    AresJavaParser parser =
        (AresJavaParser) ParserFactory.getParserInstance(AresExtension.WITH_ARES_EXTENSIONS);
    AbstractBastNode clone = null;
    JavaLexer lexer = new JavaLexer(AresExtension.WITH_ARES_EXTENSIONS);
    switch (node.getTag()) {
      case TagConstants.BAST_BLOCK:
        clone = parser.block(lexer, data, (TokenAndHistory) null, true).value;
        break;
      case TagConstants.BAST_COND_AND:
      case TagConstants.BAST_COND_OR:
      case TagConstants.BAST_THIS:
      case TagConstants.BAST_CMP:
      case TagConstants.BAST_INSTANCE_OF:
      case TagConstants.BAST_CAST_EXPR:
      case TagConstants.BAST_ADDITIVE_EXPR:
      case TagConstants.BAST_UNARY_EXPR:
      case TagConstants.BAST_INCR_EXPR:
      case TagConstants.BAST_MULTI_EXPR:
      case TagConstants.BAST_DECR_EXPR:
      case TagConstants.BAST_SUPER:
      case TagConstants.BAST_COND_EXPR:
      case TagConstants.BAST_AND:
        clone = parser.expression1bastCondExpr(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_SWITCH_CASE_GROUP:
        clone = parser.switchBlockStatementGroup(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_EXPR_INITIALIZER:
        clone = parser.expression1bastCondExpr(lexer, data, (TokenAndHistory) null).value;
        if (clone == null) {
          assert (false);
        }
        if (clone.getTag() == BastIntConst.TAG) {
          clone = new BastExprInitializer(null, (AbstractBastExpr) clone);
        }
        break;
      case TagConstants.ARES_WILDCARD:
      case TagConstants.ARES_USE:
        clone = parser.aresStatement(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_ARRAY_TYPE:
        clone = parser.type(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_ARRAY_REF:
        clone = parser.expression1bastCondExpr(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_ACCESS:
        clone = parser.expression1bastCondExpr(lexer, data, (TokenAndHistory) null).value;
        if (clone == null) {
          clone = parser.qualifiedName(lexer, data, (TokenAndHistory) null).value;
        }
        break;
      case TagConstants.BAST_RETURN:
        clone = parser.statement(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_CALL:

        clone = parser.expression1bastCondExpr(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_BOOL_CONST:
      case TagConstants.BAST_INT_CONST:
      case TagConstants.BAST_REAL_CONST:
      case TagConstants.BAST_CHAR_CONST:
        clone = parser.literal(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_NAME_IDENT:
        clone = parser.identifier(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_STRING_CONST:
      case TagConstants.BAST_NULL_CONST:
        clone = parser.literal(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.TYPE_CLASS:
        clone = parser.classType(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_FUNCTION_PARAMETER_DECL:
        clone = parser.formalParameters(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_BASIC_TYPE:
        clone = parser.type(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_TYPE_SPECIFIER:
        clone = parser.fieldDeclaratorRest(lexer, data, (TokenAndHistory) null).value;
        clone = ((BastDeclaration) clone).specifierList.getFirst();
        break;
      case TagConstants.BAST_TYPE_QUALIFIER:
        clone = parser.modifierList(lexer, data, (TokenAndHistory) null, true).value.getFirst();
        break;
      case TagConstants.BAST_PARAMETER:
        clone = parser.parameter(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_NEW:
        clone = parser.creator(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_IF:
        clone = null;
        assert (false);
        break;
      case TagConstants.BAST_LIST_INITIALIZER:
        clone = parser.arrayInitializer(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_ASGN_EXPR:
        clone = parser.expressionbastAsgnExpr(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_CASE:
        clone = parser.switchLabel(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_TYPE_ARGUMENT:
        clone = parser.typeArgument(lexer, data, (TokenAndHistory) null, false).value;
        break;
      case TagConstants.BAST_FUNCTION:
        clone = parser.classBodyDeclaration(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_DECLARATION:
        clone = parser.localVariableDeclarationStatement(lexer, data, (TokenAndHistory) null).value;
        break;
      case TagConstants.BAST_IDENT_DECLARATOR:
        clone = parseIdentDeclarator(data, parser, lexer);
        break;
      case TagConstants.BAST_CATCH:
        clone = parser.catchClause(lexer, data, (TokenAndHistory) null).value;
        break;
      default:
        if (verbose) {
          System.err.println(node.getClass());
        }
        assert (false);
    }
    if (node != null) {
      if (clone == null) {
        assert (false);
      }
    }
    return clone;

  }

  /**
   * Parses the ident declarator.
   *
   * @param data the data
   * @param parser the parser
   * @param lexer the lexer
   * @return the abstract bast node
   */
  public static AbstractBastNode parseIdentDeclarator(FileData data, AresJavaParser parser,
      JavaLexer lexer) {
    AbstractBastNode clone;
    ParseResult<BastDeclaration> tmpRes =
        parser.forVarControlHead(lexer, data, (TokenAndHistory) null, false, null, null);
    TokenAndHistory currentTokenAndHistory = tmpRes.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<AbstractBastInitializer> initializer =
        new ParseResult<AbstractBastInitializer>(null, null);
    AbstractBastDeclarator declaration = tmpRes.value.declaratorList.getFirst();
    if (((JavaToken) nextToken.token).type == BasicJavaToken.EQUAL) {
      currentTokenAndHistory = nextToken;
      TokenAndHistory[] tokens2 = null;
      if (declaration.info.tokens == null) {
        tokens2 = new TokenAndHistory[1];
      } else {
        tokens2 = new TokenAndHistory[declaration.info.tokens.length + 1];
        for (int i = 0; i < declaration.info.tokens.length; i++) {
          tokens2[i] = declaration.info.tokens[i];
        }
      }

      tokens2[tokens2.length - 1] = currentTokenAndHistory;
      declaration.info.tokens = tokens2;
      initializer = parser.variableInitializer(lexer, data, currentTokenAndHistory);
      declaration.setInitializer(initializer.value);
      currentTokenAndHistory = initializer.currentTokenAndHistory;
    }
    clone = ((BastDeclaration) tmpRes.value).declaratorList.get(0);
    return clone;
  }

  /**
   * Find left java token.
   *
   * @param node the node
   * @return the java token
   */
  public static JavaToken findLeftJavaToken(AbstractBastNode node) {
    return findLeftJavaToken(node, false);
  }

  /**
   * Find left java token.
   *
   * @param node the node
   * @return the java token
   */
  public static JavaToken findLeftJavaToken(AbstractBastNode node, boolean ignoreBeforeToken) {
    boolean continueLoop = true;
    while (continueLoop) {
      if (node == null) {
        break;
      }
      if (!ignoreBeforeToken) {
        if (node.info.tokensBefore != null && node.info.tokensBefore.size() > 0) {
          break;
        }
      }
      switch (node.getTag()) {
        case BastCmp.TAG:
          node = ((BastCmp) node).left;
          break;
        case BastAdditiveExpr.TAG:
          node = ((BastAdditiveExpr) node).left;
          break;
        case BastMultiExpr.TAG:
          node = ((BastMultiExpr) node).left;
          break;
        case BastArrayRef.TAG:
          node = ((BastArrayRef) node).arrayRef;
          break;
        case BastAsgnExpr.TAG:
          node = ((BastAsgnExpr) node).left;
          break;
        case BastParameter.TAG:
          if (((BastParameter) node).specifiers != null
              && ((BastParameter) node).specifiers.size() > 0) {
            node = ((BastParameter) node).specifiers.getFirst();
            break;
          } else {
            node = ((BastParameter) node).declarator;
          }
          break;
        case BastArrayType.TAG:
          node = ((BastArrayType) node).type;
          break;
        case BastClassType.TAG:
          node = ((BastClassType) node).name;
          break;
        case BastCall.TAG:
          node = ((BastCall) node).function;
          break;
        case BastAccess.TAG:
          node = ((BastAccess) node).target;
          break;
        case BastExprInitializer.TAG:
          node = ((BastExprInitializer) node).init;
          break;
        case BastIdentDeclarator.TAG:
          node = ((BastIdentDeclarator) node).identifier;
          break;
        case BastInstanceOf.TAG:
          node = ((BastInstanceOf) node).expr;
          break;
        case BastCondAnd.TAG:
          node = ((BastCondAnd) node).left;
          break;
        case BastCondExpr.TAG:
          node = ((BastCondExpr) node).condition;
          break;
        case BastCondOr.TAG:
          node = ((BastCondOr) node).left;
          break;
        case BastAnd.TAG:
          node = ((BastAnd) node).left;
          break;
        case BastOr.TAG:
          node = ((BastOr) node).left;
          break;
        case BastUnaryExpr.TAG:
        case BastNameIdent.TAG:
        case BastIf.TAG:
        case BastBlock.TAG:
        case BastNullConst.TAG:
        case BastBasicType.TAG:
        case BastThis.TAG:
        case BastTypeQualifier.TAG:
        case BastIntConst.TAG:
        case BastStringConst.TAG:
        case BastRealConst.TAG:
        case BastWhileStatement.TAG:
        case BastNew.TAG:
        case BastBoolConst.TAG:
        case BastTryStmt.TAG:
        case BastCastExpr.TAG:
        case BastThrowStmt.TAG:
        case BastSuper.TAG:
        case BastForStmt.TAG:
        case BastCatchClause.TAG:
        case BastBreak.TAG:
        case BastSwitchCaseGroup.TAG:
        case BastSynchronizedBlock.TAG:
        case AresPatternClause.TAG:
        case BastReturn.TAG:
        case AresWildcard.TAG:
        case AresUseStmt.TAG:
        case AresChoiceStmt.TAG:
        case AresCaseStmt.TAG:
        case BastListInitializer.TAG:
        case AresBlock.TAG:
        case BastSwitch.TAG:
          continueLoop = false;
          break;
        case BastIncrExpr.TAG:
          node = ((BastIncrExpr) node).operand;
          break;
        case BastDecrExpr.TAG:
          node = ((BastDecrExpr) node).operand;
          break;
        case BastDeclaration.TAG:
          if (((BastDeclaration) node).modifiers == null
              || ((BastDeclaration) node).modifiers.size() == 0) {
            node = ((BastDeclaration) node).specifierList.getFirst();

          } else {
            node = ((BastDeclaration) node).modifiers.getFirst();
          }
          break;
        case BastTypeSpecifier.TAG:
          node = ((BastTypeSpecifier) node).type;
          break;

        default:
          assert (false);
          continueLoop = false;
          break;
      }
    }
    if (node != null && node.info != null) {
      if (!ignoreBeforeToken && node.info.tokensBefore != null && node.info.tokensBefore.size() > 0
          && node.info.tokensBefore.getFirst().token != null) {
        return (JavaToken) node.info.tokensBefore.getFirst().token;

      } else if (node.info.tokens != null && node.info.tokens.length > 0
          && node.info.tokens[0] != null) {
        if (!ignoreBeforeToken && node.info.tokens[0].prevTokens.size() > 0) {
          return (JavaToken) node.info.tokens[0].prevTokens.getFirst();
        }
        return (JavaToken) node.info.tokens[0].token;

      } else if (node.info.tokens != null && node.info.tokens.length > 0) {
        for (int i = 0; i < node.info.tokens.length; i++) {
          if (node.info.tokens[i] != null && node.info.tokens[i].token != null) {
            return (JavaToken) node.info.tokens[i].token;
          }
        }
      }

    }
    return null;
  }

}
