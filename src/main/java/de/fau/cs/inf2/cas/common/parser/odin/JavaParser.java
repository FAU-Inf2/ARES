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

package de.fau.cs.inf2.cas.common.parser.odin;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastConstant;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExternalDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastInternalDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastSpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAccess;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAdditiveExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAnd;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAnnotation;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAnnotationDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAnnotationElemValue;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAnnotationMethod;
import de.fau.cs.inf2.cas.common.bast.nodes.BastArrayDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastArrayRef;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAsgnExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAssertStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBoolConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBreak;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCase;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCatchClause;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCharConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCmp;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondAnd;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondOr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastContinue;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDecrExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDefault;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEmptyDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEmptyStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEnumDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEnumMember;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEnumSpec;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprList;
import de.fau.cs.inf2.cas.common.bast.nodes.BastForStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunctionParameterDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastImportDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIncrExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastInstanceOf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastInterfaceDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastLabelStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastListInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastMultiExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNew;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNullConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastOr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastPackage;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameter;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameterList;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.nodes.BastRealConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastReturn;
import de.fau.cs.inf2.cas.common.bast.nodes.BastShift;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStringConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSuper;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitch;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitchCaseGroup;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSynchronizedBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTemplateSpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastThis;
import de.fau.cs.inf2.cas.common.bast.nodes.BastThrowStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTryStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeArgument;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeParameter;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeQualifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeSpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastUnaryExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastWhileStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastXor;
import de.fau.cs.inf2.cas.common.bast.type.BastArrayType;
import de.fau.cs.inf2.cas.common.bast.type.BastBasicType;
import de.fau.cs.inf2.cas.common.bast.type.BastClassType;
import de.fau.cs.inf2.cas.common.bast.type.BastType;

import de.fau.cs.inf2.cas.common.parser.AresExtension;
import de.fau.cs.inf2.cas.common.parser.IGeneralToken;
import de.fau.cs.inf2.cas.common.parser.IParser;
import de.fau.cs.inf2.cas.common.parser.SyntaxError;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;

public class JavaParser implements IParser {

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
    return new JavaParser();
  }

  /**
   * Instantiates a new java parser.
   *
   */
  protected JavaParser() {

  }



  private ParseResult<BastAnnotation> annotation(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // Annotation:
    // @ QualifiedIdentifier [ ( [AnnotationElement] ) ]
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectAt(nextToken);
    currentTokenAndHistory = nextToken;
    final TokenAndHistory[] tokens = { currentTokenAndHistory, null };
    ParseResult<AbstractBastExpr> nameRes = qualifiedName(lexer, data, currentTokenAndHistory);
    assert (nameRes.value != null);
    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<LinkedList<AbstractBastExpr>> initListRes =
        new ParseResult<LinkedList<AbstractBastExpr>>(null, null);
    LinkedList<AbstractBastExpr> initList = null;
    if (TokenChecker.isLeftParenthesis(nextToken)) {
      currentTokenAndHistory = nextToken;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      if (TokenChecker.isRightParenthesis(nextToken)) {
        currentTokenAndHistory = nextToken;
      } else {
        initListRes = annotationElement(lexer, data, currentTokenAndHistory);
        initList = initListRes.value;
        currentTokenAndHistory = initListRes.currentTokenAndHistory;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        TokenChecker.expectRightParenthesis(nextToken);
        currentTokenAndHistory = nextToken;
      }
    }
    BastAnnotation annotation = new BastAnnotation(tokens, nameRes.value, initList);
    ParseResult<BastAnnotation> result =
        new ParseResult<BastAnnotation>(annotation, currentTokenAndHistory);
    return result;
  }



  private ParseResult<LinkedList<AbstractBastExpr>> annotationElement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // AnnotationElement:
    // ElementValuePairs
    // ElementValue
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
    ParseResult<LinkedList<AbstractBastExpr>> listRes =
        new ParseResult<LinkedList<AbstractBastExpr>>(null, null);
    LinkedList<AbstractBastExpr> list = null;
    if (TokenChecker.isIdentifier(nextToken) && TokenChecker.isEqual(nextnextToken)) {
      listRes = elementValuePairList(lexer, data, currentTokenAndHistory);
      return listRes;
    }
    ParseResult<AbstractBastExpr> elemRes = elementValue(lexer, data, currentTokenAndHistory);
    AbstractBastExpr elem = elemRes.value;
    list = add(list, elem);
    return new ParseResult<LinkedList<AbstractBastExpr>>(list, elemRes.currentTokenAndHistory);

  }



  private ParseResult<LinkedList<BastAnnotation>> annotationList(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // Annotations:
    // Annotation [Annotations]
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    LinkedList<BastAnnotation> list = null;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectAt(nextToken);
    TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
    while (TokenChecker.isAt(nextToken) && TokenChecker.isNotInterface(nextnextToken)) {
      ParseResult<BastAnnotation> annotation = annotation(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = annotation.currentTokenAndHistory;
      list = add(list, annotation.value);
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      nextnextToken = lexer.nextToken(data, nextToken);
    }

    return new ParseResult<LinkedList<BastAnnotation>>(list, currentTokenAndHistory);
  }



  private ParseResult<AbstractBastInternalDecl> annotationMethodOrConstantRest(
      final JavaLexer lexer, final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // AnnotationMethodOrConstantRest:
    // AnnotationMethodRest
    // ConstantDeclaratorsRest
    // AnnotationMethodRest:
    // ( ) [[]] [default ElementValue]
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<BastType> typeRes = type(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = typeRes.currentTokenAndHistory;
    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokens = new TokenAndHistory[2];
    switch (((JavaToken) nextToken.token).type) {
      case LPAREN:
        currentTokenAndHistory = nextToken;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        TokenChecker.expectRightParenthesis(nextToken);
        currentTokenAndHistory = nextToken;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
        int count = 0;
        while (TokenChecker.isLeftBracket(nextToken)
            && TokenChecker.isRightBracket(nextnextToken)) {
          count++;
          currentTokenAndHistory = nextnextToken;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
          nextnextToken = lexer.nextToken(data, nextToken);
        }
        BastIdentDeclarator identDecl = null;
        if (count != 0) {
          TokenAndHistory[] tokenstmp = new TokenAndHistory[1];
          tokenstmp[0] = currentTokenAndHistory;
          currentTokenAndHistory = currentTokenAndHistory.setFlushed();
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
          identDecl = new BastIdentDeclarator(null, nameRes.value, null,
              new BastArrayDeclarator(tokenstmp, null, null, count));
        } else {
          identDecl = new BastIdentDeclarator((TokenAndHistory[]) null, nameRes.value, null, null);
          tokens[0] = currentTokenAndHistory;
          currentTokenAndHistory = currentTokenAndHistory.setFlushed();
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
        }
        ParseResult<AbstractBastExpr> valueRes = new ParseResult<AbstractBastExpr>(null, null);
        AbstractBastExpr value = null;
        if (TokenChecker.isDefault(nextToken)) {
          currentTokenAndHistory = nextToken;
          valueRes = elementValue(lexer, data, currentTokenAndHistory);
          value = valueRes.value;
          tokens[1] = currentTokenAndHistory;
          currentTokenAndHistory = valueRes.currentTokenAndHistory;

        }
        BastAnnotationMethod method =
            new BastAnnotationMethod(tokens, identDecl, typeRes.value, value);
        return new ParseResult<AbstractBastInternalDecl>(method, currentTokenAndHistory);
      case EQUAL:
      case LBRACKET:
        LinkedList<AbstractBastSpecifier> specifierList = new LinkedList<AbstractBastSpecifier>();
        BastTypeSpecifier typeSpec = new BastTypeSpecifier(null, typeRes.value);
        specifierList.add(typeSpec);
        ParseResult<LinkedList<AbstractBastDeclarator>> declaratorListRes =
            constantDeclaratorsRest(lexer, data, currentTokenAndHistory, nameRes.value);
        LinkedList<AbstractBastDeclarator> declaratorList = declaratorListRes.value;
        AbstractBastInternalDecl decl = new BastDeclaration(null, specifierList, declaratorList);
        return new ParseResult<AbstractBastInternalDecl>(decl,
            declaratorListRes.currentTokenAndHistory);
      // ConstantDeclaratorsRest
      default:
        throw new SyntaxError("Token not expected.", ((JavaToken) nextToken.token));
    }
  }



  private ParseResult<LinkedList<AbstractBastExternalDecl>> annotationTypeBody(
      final JavaLexer lexer, final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<LinkedList<AbstractBastExternalDecl>> list =
        new ParseResult<LinkedList<AbstractBastExternalDecl>>(null, null);
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isLeftBrace(nextToken)) {
      currentTokenAndHistory = nextToken;
      list = annotationTypeBodyList(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = list.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      if (TokenChecker.isRightBrace(nextToken)) {
        currentTokenAndHistory = nextToken;
        return new ParseResult<LinkedList<AbstractBastExternalDecl>>(list.value,
            currentTokenAndHistory);
      }
    }
    return new ParseResult<LinkedList<AbstractBastExternalDecl>>(null, currentTokenAndHistory);

  }



  private ParseResult<LinkedList<AbstractBastExternalDecl>> annotationTypeBodyList(
      final JavaLexer lexer, final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // AnnotationTypeBody:
    // { [AnnotationTypeElementDeclarations] }
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    LinkedList<AbstractBastExternalDecl> list = null;
    ParseResult<AbstractBastInternalDecl> annotationBody =
        annotationTypeElementDeclaration(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = annotationBody.currentTokenAndHistory;
    while (annotationBody.value != null) {
      list = add(list, annotationBody.value);
      annotationBody = annotationTypeElementDeclaration(lexer, data, currentTokenAndHistory);
      if (annotationBody.value != null) {
        currentTokenAndHistory = annotationBody.currentTokenAndHistory;
      }
    }
    return new ParseResult<LinkedList<AbstractBastExternalDecl>>(list, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastInternalDecl> annotationTypeDeclaration(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // @ interface Identifier AnnotationTypeBody

    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokens = new TokenAndHistory[3];
    TokenChecker.expectAt(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[0] = currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectInterface(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[1] = currentTokenAndHistory;
    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);

    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    ParseResult<LinkedList<AbstractBastExternalDecl>> declarationsResult =
        annotationTypeBody(lexer, data, currentTokenAndHistory);
    if (declarationsResult.currentTokenAndHistory == null) {
      return new ParseResult<AbstractBastInternalDecl>(null, null);
    }
    currentTokenAndHistory = declarationsResult.currentTokenAndHistory;
    tokens[2] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    LinkedList<AbstractBastExternalDecl> declarations = declarationsResult.value;
    BastAnnotationDecl decl = new BastAnnotationDecl(tokens, nameRes.value, declarations);
    return new ParseResult<AbstractBastInternalDecl>(decl, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastInternalDecl> annotationTypeElementDeclaration(
      final JavaLexer lexer, final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // AnnotationTypeElementDeclaration:
    // {Modifier} AnnotationTypeElementRest
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<LinkedList<AbstractBastSpecifier>> modifiers =
        modifierList(lexer, data, currentTokenAndHistory, false);
    currentTokenAndHistory = modifiers.currentTokenAndHistory;
    ParseResult<AbstractBastInternalDecl> member =
        annotationTypeElementRest(lexer, data, currentTokenAndHistory);
    if (modifiers.value != null && !modifiers.value.isEmpty()) {
      member.value.setModifiers(modifiers.value);
    }
    return member;
  }

  private ParseResult<AbstractBastInternalDecl> annotationTypeElementRest(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // AnnotationTypeElementRest:
    // Type Identifier AnnotationMethodOrConstantRest ;
    // ClassDeclaration
    // InterfaceDeclaration
    // EnumDeclaration
    // AnnotationTypeDeclaration
    // ;
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<AbstractBastInternalDecl> declRes =
        new ParseResult<AbstractBastInternalDecl>(null, null);
    AbstractBastInternalDecl decl = null;
    switch (((JavaToken) nextToken.token).type) {
      case INT:
      case BYTE:
      case SHORT:
      case BOOLEAN:
      case LONG:
      case FLOAT:
      case DOUBLE:
      case CHAR:
      case VOID:
        declRes = annotationMethodOrConstantRest(lexer, data, currentTokenAndHistory);
        decl = declRes.value;
        currentTokenAndHistory = declRes.currentTokenAndHistory;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        TokenChecker.expectSemicolon(nextToken);
        currentTokenAndHistory = nextToken;
        decl.info.tokensAfter.add(currentTokenAndHistory);
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        return new ParseResult<AbstractBastInternalDecl>(decl, currentTokenAndHistory);
      case IDENTIFIER:
        declRes = annotationMethodOrConstantRest(lexer, data, currentTokenAndHistory);
        currentTokenAndHistory = declRes.currentTokenAndHistory;
        decl = declRes.value;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        TokenChecker.expectSemicolon(nextToken);
        currentTokenAndHistory = nextToken;
        decl.info.tokensAfter.add(currentTokenAndHistory);
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        return new ParseResult<AbstractBastInternalDecl>(decl, currentTokenAndHistory);
      case ENUM:
        declRes = enumDeclaration(lexer, data, currentTokenAndHistory);
        return declRes;
      case CLASS:
        declRes = classDeclaration(lexer, data, currentTokenAndHistory);
        return declRes;
      case INTERFACE:
        declRes = interfaceDeclaration(lexer, data, currentTokenAndHistory);
        return declRes;
      case RBRACE:
        return new ParseResult<AbstractBastInternalDecl>(null, currentTokenAndHistory);
      case SEMICOLON:
        currentTokenAndHistory = nextToken;
        BastEmptyDeclaration emptyDecl = new BastEmptyDeclaration(null);
        emptyDecl.info.tokensAfter.add(currentTokenAndHistory);
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        return new ParseResult<AbstractBastInternalDecl>(emptyDecl, currentTokenAndHistory);
      case AT:
        declRes = annotationTypeDeclaration(lexer, data, inputTokenAndHistory);
        return declRes;
      default:
        throw new SyntaxError("Token not expected.", ((JavaToken) nextToken.token));
    }
  }

  protected ParseResult<LinkedList<AbstractBastExpr>> arguments(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // Arguments:
    // ( [Expression { , Expression }] )
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    LinkedList<AbstractBastExpr> list = new LinkedList<AbstractBastExpr>();
    ArrayList<TokenAndHistory> additionalTokens = new ArrayList<>();
    if (TokenChecker.isRightParenthesis(nextToken)) {
      currentTokenAndHistory = nextToken;
      return new ParseResult<LinkedList<AbstractBastExpr>>(list, currentTokenAndHistory);
    } else {
      ParseResult<AbstractBastExpr> exprRes =
          expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = exprRes.currentTokenAndHistory;
      if (exprRes.value != null) {
        list = add(list, exprRes.value);
      }
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      while (TokenChecker.isComma(nextToken)) {
        currentTokenAndHistory = nextToken;
        additionalTokens.add(currentTokenAndHistory);
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        exprRes = expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
        if (exprRes.value != null) {
          list = add(list, exprRes.value);
          currentTokenAndHistory = exprRes.currentTokenAndHistory;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
        } else {
          throw new SyntaxError("Expression expected!", ((JavaToken) nextToken.token));
        }
      }
      if (TokenChecker.isRightParenthesis(nextToken)) {
        currentTokenAndHistory = nextToken;
        return new ParseResult<LinkedList<AbstractBastExpr>>(list, currentTokenAndHistory,
            additionalTokens);
      } else {
        return new ParseResult<LinkedList<AbstractBastExpr>>(null, null);
      }
    }
  }



  /**
   * Array initializer.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<BastListInitializer> arrayInitializer(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // ArrayInitializer:
    // { [VariableInitializer {, VariableInitializer} [,]] }
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    LinkedList<AbstractBastInitializer> list = new LinkedList<AbstractBastInitializer>();
    TokenAndHistory[] tokens = new TokenAndHistory[5];
    boolean openList = false;
    ArrayList<TokenAndHistory> additionalTokens = new ArrayList<>();
    TokenChecker.expectLeftBrace(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[0] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    nextToken = lexer.nextToken(data, currentTokenAndHistory);

    while (TokenChecker.isNotRightBrace(nextToken)) {
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      ParseResult<AbstractBastInitializer> init =
          new ParseResult<AbstractBastInitializer>(null, null);
      if (TokenChecker.isNotComma(nextToken)) {
        init = variableInitializer(lexer, data, currentTokenAndHistory);
        currentTokenAndHistory = init.currentTokenAndHistory;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        assert (init.value != null);
      }
      list = add(list, init.value);
      if (TokenChecker.isComma(nextToken)) {
        currentTokenAndHistory = nextToken;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        if (TokenChecker.isRightBrace(nextToken)) {
          openList = true;
          tokens[2] = currentTokenAndHistory;
          currentTokenAndHistory = currentTokenAndHistory.setFlushed();
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
        } else {
          additionalTokens.add(currentTokenAndHistory);
          currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        }
      } else {
        TokenChecker.expectRightBrace(nextToken);
      }
    }
    TokenChecker.expectRightBrace(nextToken);
    tokens[1] = new TokenAndHistory(new ListToken(additionalTokens));

    currentTokenAndHistory = nextToken;
    tokens[3] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    return new ParseResult<BastListInitializer>(new BastListInitializer(tokens, list, openList),
        currentTokenAndHistory);

  }



  protected ParseResult<AbstractBastStatement> assertStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectAssert(nextToken);
    TokenAndHistory[] tokens = new TokenAndHistory[2];

    currentTokenAndHistory = nextToken;
    tokens[0] = currentTokenAndHistory;
    ParseResult<AbstractBastExpr> firstAssert =
        expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
    ParseResult<AbstractBastExpr> secondAssert = new ParseResult<AbstractBastExpr>(null, null);
    currentTokenAndHistory = firstAssert.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isColon(nextToken)) {
      currentTokenAndHistory = nextToken;
      tokens[1] = currentTokenAndHistory;
      secondAssert = expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = secondAssert.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    TokenChecker.expectSemicolon(nextToken);

    BastAssertStmt stmt = new BastAssertStmt(tokens, firstAssert.value, secondAssert.value);
    currentTokenAndHistory = nextToken;
    stmt.info.tokensAfter.add(currentTokenAndHistory);
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    return new ParseResult<AbstractBastStatement>(stmt, currentTokenAndHistory);
  }



  private ParseResult<AbstractBastExpr> basicTypeClass(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // BasicType {[]} .class
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    BastType type = null;
    TokenAndHistory[] tokens = { nextToken };
    switch (((JavaToken) nextToken.token).type) {
      case BYTE:
        type = new BastBasicType(tokens, TagConstants.TYPE_BYTE);
        break;
      case SHORT:
        type = new BastBasicType(tokens, TagConstants.TYPE_SHORT);
        break;
      case CHAR:
        type = new BastBasicType(tokens, TagConstants.TYPE_CHAR);
        break;
      case INT:
        type = new BastBasicType(tokens, TagConstants.TYPE_INT);
        break;
      case LONG:
        type = new BastBasicType(tokens, TagConstants.TYPE_LONG);
        break;
      case FLOAT:
        type = new BastBasicType(tokens, TagConstants.TYPE_FLOAT);
        break;
      case DOUBLE:
        type = new BastBasicType(tokens, TagConstants.TYPE_DOUBLE);
        break;
      case BOOLEAN:
        type = new BastBasicType(tokens, TagConstants.TYPE_BOOL);
        break;
      default:
        throw new SyntaxError("Basic type expected.", ((JavaToken) nextToken.token));

    }
    currentTokenAndHistory = nextToken;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    int count = 0;
    ArrayList<TokenAndHistory> additionalTokens = new ArrayList<>();
    while (TokenChecker.isLeftBracket(nextToken)) {
      currentTokenAndHistory = nextToken;
      additionalTokens.add(currentTokenAndHistory);
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      TokenChecker.expectRichtBracket(nextToken);
      currentTokenAndHistory = nextToken;
      additionalTokens.add(currentTokenAndHistory);
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      count++;
    }

    if (count != 0) {
      TokenAndHistory[] tokenstmp = new TokenAndHistory[2];
      tokenstmp[1] = new TokenAndHistory(new ListToken(additionalTokens));
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      type = new BastArrayType(tokenstmp, type, null, count);
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    if (TokenChecker.isPoint(nextToken)) {
      currentTokenAndHistory = nextToken;
      TokenAndHistory[] tokenstmp = new TokenAndHistory[2];
      tokenstmp[0] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      TokenChecker.expectClass(nextToken);
      currentTokenAndHistory = nextToken;
      tokens = new TokenAndHistory[2];
      tokens[0] = currentTokenAndHistory;
      BastClassConst classNode = new BastClassConst(tokens);
      assert (type != null);
      return new ParseResult<AbstractBastExpr>(new BastAccess(tokenstmp, type, classNode),
          currentTokenAndHistory);
    } else {
      return new ParseResult<AbstractBastExpr>(null, null);
    }
  }



  private ParseResult<BastType> basicTypeWithoutBrackets(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // Java 7:
    // BasicType
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    int typeConstant = -1;
    TokenAndHistory[] tokens = null;
    switch (((JavaToken) nextToken.token).type) {
      case BYTE:
        currentTokenAndHistory = nextToken;
        typeConstant = TagConstants.TYPE_BYTE;
        break;
      case SHORT:
        currentTokenAndHistory = nextToken;
        typeConstant = TagConstants.TYPE_SHORT;
        break;
      case CHAR:
        currentTokenAndHistory = nextToken;
        typeConstant = TagConstants.TYPE_CHAR;
        break;
      case INT:
        currentTokenAndHistory = nextToken;
        typeConstant = TagConstants.TYPE_INT;
        break;
      case LONG:
        currentTokenAndHistory = nextToken;
        typeConstant = TagConstants.TYPE_LONG;
        break;
      case FLOAT:
        currentTokenAndHistory = nextToken;
        typeConstant = TagConstants.TYPE_FLOAT;
        break;
      case DOUBLE:
        currentTokenAndHistory = nextToken;
        typeConstant = TagConstants.TYPE_DOUBLE;
        break;
      case BOOLEAN:
        currentTokenAndHistory = nextToken;
        typeConstant = TagConstants.TYPE_BOOL;
        break;
      default:
        throw new SyntaxError("Basic type expected.", ((JavaToken) nextToken.token));
    }

    if (typeConstant != -1) {
      tokens = new TokenAndHistory[1];
      tokens[0] = currentTokenAndHistory;
      BastType type = new BastBasicType(tokens, typeConstant);
      return new ParseResult<BastType>(type, currentTokenAndHistory);
    }
    return new ParseResult<BastType>(null, null);
  }

  private ParseResult<BastProgram> bastProgram(final JavaLexer lexer, final FileData data) {
    // [[Annotations] package QualifiedIdentifier ; ] {ImportDeclaration}
    // {TypeDeclaration}
    TokenAndHistory currentTokenAndHistory = null;
    ParseResult<LinkedList<BastAnnotation>> annotationList =
        new ParseResult<LinkedList<BastAnnotation>>(null, null);
    ParseResult<LinkedList<BastImportDeclaration>> bastImportDecl =
        new ParseResult<LinkedList<BastImportDeclaration>>(null, null);
    ParseResult<BastPackage> packageDecl = new ParseResult<BastPackage>(null, null);
    TokenAndHistory recoveryTokenAndHistory = currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isAt(nextToken)) {
      annotationList = annotationList(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = annotationList.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      if (TokenChecker.isPackage(nextToken)) {
        currentTokenAndHistory = annotationList.currentTokenAndHistory;
      } else {
        currentTokenAndHistory = recoveryTokenAndHistory;
        annotationList = new ParseResult<LinkedList<BastAnnotation>>(null, null);
      }
    }
    if (TokenChecker.isPackage(nextToken)) {
      packageDecl = packageMethod(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = packageDecl.currentTokenAndHistory;
    }
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isImport(nextToken)) {
      bastImportDecl = importDeclarationList(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = bastImportDecl.currentTokenAndHistory;
    }

    final ParseResult<LinkedList<AbstractBastExternalDecl>> typeDeclarationList =
        typeDeclarationList(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = typeDeclarationList.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    assert TokenChecker.isEof(nextToken);
    TokenAndHistory[] tokens = { nextToken };
    return new ParseResult<BastProgram>(new BastProgram(tokens, typeDeclarationList.value, null,
        packageDecl.value, bastImportDecl.value, annotationList.value), nextToken);

  }


  /**
   * Block.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @param acceptAresToken the accept ares token
   * @return the parses the result
   */
  public ParseResult<BastBlock> block(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory, boolean acceptAresToken) {
    // Block:
    // { BlockStatements }
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokens = { null, null, null };
    TokenChecker.expectLeftBrace(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[1] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    ParseResult<LinkedList<AbstractBastStatement>> statements =
        new ParseResult<LinkedList<AbstractBastStatement>>(null, null);
    statements = blockStatementList(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = statements.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (acceptAresToken && TokenChecker.isAresToken(nextToken)) {
      currentTokenAndHistory = nextToken;
      currentTokenAndHistory.setFlushed();
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      TokenChecker.expectRightBrace(nextToken);
      currentTokenAndHistory = nextToken;
      tokens[2] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      return new ParseResult<BastBlock>(new BastBlock(tokens, statements.value),
          currentTokenAndHistory);
    } else if (TokenChecker.isRightBrace(nextToken)) {
      currentTokenAndHistory = nextToken;
      tokens[2] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      return new ParseResult<BastBlock>(new BastBlock(tokens, statements.value),
          currentTokenAndHistory);
    } else {
      throw new SyntaxError("Block left unfinished.", ((JavaToken) nextToken.token));
    }
  }



  private ParseResult<AbstractBastStatement> blockStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // LocalVariableDeclarationStatement
    // ClassOrInterfaceDeclaration
    // [Identifier :] Statement
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<LinkedList<AbstractBastSpecifier>> modifiers =
        new ParseResult<LinkedList<AbstractBastSpecifier>>(null, null);
    ParseResult<BastDeclaration> decl = new ParseResult<BastDeclaration>(null, null);
    switch (((JavaToken) nextToken.token).type) {
      case INT:
      case DOUBLE:
      case SHORT:
      case LONG:
      case CHAR:
      case BYTE:
      case BOOLEAN:
      case FLOAT:
      case FINAL:
      case STATIC:
      case ABSTRACT:
        modifiers = modifierList(lexer, data, currentTokenAndHistory, false);
        currentTokenAndHistory = modifiers.currentTokenAndHistory;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        return innerClassOrVariable(lexer, data, currentTokenAndHistory, nextToken, modifiers);
      case IDENTIFIER:
        modifiers = modifierList(lexer, data, currentTokenAndHistory, false);
        currentTokenAndHistory = modifiers.currentTokenAndHistory;
        decl = localVariableDeclarationStatement(lexer, data, currentTokenAndHistory);
        if (decl.value != null) {
          return new ParseResult<AbstractBastStatement>(decl.value, decl.currentTokenAndHistory);
        }
        ParseResult<AbstractBastStatement> stmt = statement(lexer, data, currentTokenAndHistory);
        if (stmt.value != null) {
          return stmt;
        }
        break;

      case AT:
        modifiers = modifierList(lexer, data, currentTokenAndHistory, false);
        currentTokenAndHistory = modifiers.currentTokenAndHistory;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        return atInBlock(lexer, data, currentTokenAndHistory, nextToken, modifiers);
      case CLASS:
      case INTERFACE:
      case ENUM:
        modifiers = modifierList(lexer, data, currentTokenAndHistory, false);
        currentTokenAndHistory = modifiers.currentTokenAndHistory;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        return enumClassOrInterfaceInBlock(lexer, data, currentTokenAndHistory, modifiers);
      case RETURN:
      case SWITCH:
      case FOR:
      case ASSERT:
      case TRY:
      case ELSE:
      case THIS:
      case BREAK:
      case LBRACE:
      case IF:
      case LPAREN:
      case SUPER:
      case WHILE:
      case NEW:
      case THROW:
      case DO:
      case CONTINUE:
      case SYNCHRONIZED:
      case PLUS_PLUS:
      case MINUS_MINUS:
      case SEMICOLON:
      case LESS:
      case NULL:
        return statement(lexer, data, currentTokenAndHistory);
      case ARES_TOKEN:
        return aresTokenInBlock(lexer, data, currentTokenAndHistory, nextToken);
      case DEFAULT:
      case CATCH:
      case CASE:
      case FINALLY:
      case COLON:
      case EOF:
        return new ParseResult<AbstractBastStatement>(null, currentTokenAndHistory);
      case RPAREN:
      case RBRACE:
        return new ParseResult<AbstractBastStatement>(null, currentTokenAndHistory);
      case PUBLIC:
        throw new SyntaxError("Keyword is not allowed here.", ((JavaToken) nextToken.token));
      default:
        throw new SyntaxError("Token not expected.", ((JavaToken) nextToken.token));
    }
    return new ParseResult<AbstractBastStatement>(null, null);
  }

  private ParseResult<AbstractBastStatement> atInBlock(final JavaLexer lexer, final FileData data,
      TokenAndHistory currentTokenAndHistory, TokenAndHistory nextToken,
      ParseResult<LinkedList<AbstractBastSpecifier>> modifiers) {
    ParseResult<BastDeclaration> decl;
    switch (((JavaToken) nextToken.token).type) {
      case CLASS:
      case INTERFACE:
      case ENUM:
        return enumClassOrInterfaceInBlock(lexer, data, currentTokenAndHistory, modifiers);
      default:
        decl = localVariableDeclarationStatement(lexer, data, currentTokenAndHistory);
        if (decl.value != null) {
          decl.value.setModifiers(modifiers.value);
        }
        return new ParseResult<AbstractBastStatement>(decl.value, decl.currentTokenAndHistory);
    }
  }

  private ParseResult<AbstractBastStatement> enumClassOrInterfaceInBlock(final JavaLexer lexer,
      final FileData data, TokenAndHistory currentTokenAndHistory,
      ParseResult<LinkedList<AbstractBastSpecifier>> modifiers) {
    ParseResult<AbstractBastExternalDecl> exDecl =
        classOrInterfaceDeclaration(lexer, data, currentTokenAndHistory);
    if (exDecl != null) {
      exDecl.value.setModifiers(modifiers.value);
    }
    return new ParseResult<AbstractBastStatement>(exDecl.value, exDecl.currentTokenAndHistory);
  }

  private ParseResult<AbstractBastStatement> aresTokenInBlock(final JavaLexer lexer,
      final FileData data, TokenAndHistory currentTokenAndHistory, TokenAndHistory nextToken) {
    nextToken = lexer.nextToken(data, nextToken);
    if (TokenChecker.isRightBrace(nextToken)) {
      return new ParseResult<AbstractBastStatement>(null, currentTokenAndHistory);
    } else {
      return statement(lexer, data, currentTokenAndHistory);
    }
  }

  private ParseResult<AbstractBastStatement> innerClassOrVariable(final JavaLexer lexer,
      final FileData data, TokenAndHistory currentTokenAndHistory, TokenAndHistory nextToken,
      ParseResult<LinkedList<AbstractBastSpecifier>> modifiers) {
    ParseResult<BastDeclaration> decl;
    if (TokenChecker.isClass(nextToken)) {
      return enumClassOrInterfaceInBlock(lexer, data, currentTokenAndHistory, modifiers);
    } else {
      decl = localVariableDeclarationStatement(lexer, data, currentTokenAndHistory);
      if (decl.value != null) {
        decl.value.setModifiers(modifiers.value);
      }
      return new ParseResult<AbstractBastStatement>(decl.value, decl.currentTokenAndHistory);
    }
  }



  protected ParseResult<LinkedList<AbstractBastStatement>> blockStatementList(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // BlockStatements:
    // { BlockStatement }
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    LinkedList<AbstractBastStatement> list = null;
    ParseResult<AbstractBastStatement> stmt = blockStatement(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = stmt.currentTokenAndHistory;
    if (currentTokenAndHistory == null) {
      return new ParseResult<LinkedList<AbstractBastStatement>>(null, null);
    }
    while (stmt.value != null) {
      list = add(list, stmt.value);
      stmt = blockStatement(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = stmt.currentTokenAndHistory;
    }
    return new ParseResult<LinkedList<AbstractBastStatement>>(list, currentTokenAndHistory);
  }

  protected ParseResult<AbstractBastStatement> breakStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectBreak(nextToken);
    currentTokenAndHistory = nextToken;
    final TokenAndHistory[] tokens = { currentTokenAndHistory };
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<BastNameIdent> nameRes = new ParseResult<BastNameIdent>(null, null);

    if (TokenChecker.isNotSemicolon(nextToken)) {
      nameRes = identifier(lexer, data, currentTokenAndHistory);

      currentTokenAndHistory = nameRes.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    TokenChecker.expectSemicolon(nextToken);
    BastBreak breakStmt = new BastBreak(tokens, nameRes.value);
    currentTokenAndHistory = nextToken;
    breakStmt.info.tokensAfter.add(currentTokenAndHistory);
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    return new ParseResult<AbstractBastStatement>(breakStmt, currentTokenAndHistory);

  }



  /**
   * Catch clause.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<BastCatchClause> catchClause(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // CatchClause:
    // catch ( {VariableModifier} CatchType Identifier ) Block
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokens = new TokenAndHistory[4];
    ArrayList<TokenAndHistory> additionalTokens = new ArrayList<>();
    TokenChecker.expectCatch(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[0] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectLeftParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[1] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<LinkedList<AbstractBastSpecifier>> modifiers =
        modifierList(lexer, data, currentTokenAndHistory, true);
    currentTokenAndHistory = modifiers.currentTokenAndHistory;
    LinkedList<AbstractBastSpecifier> declSpecList = new LinkedList<AbstractBastSpecifier>();
    final LinkedList<AbstractBastDeclarator> initDeclList =
        new LinkedList<AbstractBastDeclarator>();
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectIdentifier(nextToken);
    ParseResult<BastClassType> type = classType(lexer, data, currentTokenAndHistory);
    BastTypeSpecifier specifier = new BastTypeSpecifier(null, type.value);
    declSpecList.add(specifier);
    currentTokenAndHistory = type.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);

    while (TokenChecker.isOr(nextToken)) {
      currentTokenAndHistory = nextToken;
      type = classType(lexer, data, currentTokenAndHistory);
      additionalTokens.add(currentTokenAndHistory);
      specifier = new BastTypeSpecifier(null, type.value);
      specifier.info.tokens = new TokenAndHistory[1];
      specifier.info.tokens[0] = currentTokenAndHistory;

      declSpecList.add(specifier);
      currentTokenAndHistory = type.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);

    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    BastIdentDeclarator ident = new BastIdentDeclarator(null, nameRes.value, null, null);
    initDeclList.add(ident);
    BastDeclaration decl = new BastDeclaration(null, declSpecList, initDeclList);
    if (modifiers.value != null) {
      decl.setModifiers(modifiers.value);
    }
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectRightParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[2] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    ParseResult<AbstractBastStatement> block = blockStatement(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = block.currentTokenAndHistory;
    BastCatchClause clause = new BastCatchClause(tokens, decl, block.value);
    return new ParseResult<BastCatchClause>(clause, currentTokenAndHistory);
  }



  private ParseResult<LinkedList<BastCatchClause>> catches(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // Catches:
    // CatchClause { CatchClause }
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    LinkedList<BastCatchClause> list = null;
    ParseResult<BastCatchClause> catchClause = catchClause(lexer, data, currentTokenAndHistory);
    list = add(list, catchClause.value);
    currentTokenAndHistory = catchClause.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    while (TokenChecker.isCatch(nextToken)) {

      catchClause = catchClause(lexer, data, currentTokenAndHistory);
      list = add(list, catchClause.value);
      currentTokenAndHistory = catchClause.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    return new ParseResult<LinkedList<BastCatchClause>>(list, currentTokenAndHistory);
  }



  private ParseResult<LinkedList<AbstractBastInternalDecl>> classBody(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<LinkedList<AbstractBastInternalDecl>> list =
        new ParseResult<LinkedList<AbstractBastInternalDecl>>(null, null);
    TokenAndHistory nextToken = null;
    list = classBodyDeclarationList(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = list.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isRightBrace(nextToken)) {
      currentTokenAndHistory = nextToken;
      return new ParseResult<LinkedList<AbstractBastInternalDecl>>(list.value,
          currentTokenAndHistory);
    }
    return new ParseResult<LinkedList<AbstractBastInternalDecl>>(null, null);

  }

  /**
   * Class body declaration.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<AbstractBastInternalDecl> classBodyDeclaration(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // ClassBodyDeclaration:
    // ;
    // [static] Block
    // {Modifier} MemberDecl
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    boolean isStatic = false;
    switch (((JavaToken) nextToken.token).type) {
      case SEMICOLON:
        BastEmptyDeclaration emptyDecl = new BastEmptyDeclaration(null);
        currentTokenAndHistory = nextToken;
        emptyDecl.info.tokensAfter.add(currentTokenAndHistory);
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        return new ParseResult<AbstractBastInternalDecl>(emptyDecl, currentTokenAndHistory);
      case STATIC:
        TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
        if (TokenChecker.isLeftBrace(nextnextToken)) {
          currentTokenAndHistory = nextToken;
          ParseResult<BastBlock> block = block(lexer, data, currentTokenAndHistory, false);
          assert (block.value.info.tokens[0] == null);
          block.value.info.tokens[0] = currentTokenAndHistory;
          block.value.isStatic = true;
          return new ParseResult<AbstractBastInternalDecl>(block.value,
              block.currentTokenAndHistory);
        } else {
          ParseResult<LinkedList<AbstractBastSpecifier>> modifiers =
              modifierList(lexer, data, currentTokenAndHistory, false);
          currentTokenAndHistory = modifiers.currentTokenAndHistory;
          ParseResult<AbstractBastInternalDecl> member =
              memberDecl(lexer, data, currentTokenAndHistory);
          if (modifiers.value != null && !modifiers.value.isEmpty()) {
            member.value.setModifiers(modifiers.value);
          }
          return member;
        }
      case LBRACE:
        ParseResult<BastBlock> block = block(lexer, data, currentTokenAndHistory, false);
        block.value.isStatic = isStatic;
        return new ParseResult<AbstractBastInternalDecl>(block.value, block.currentTokenAndHistory);
      default:
        ParseResult<LinkedList<AbstractBastSpecifier>> modifiers =
            modifierList(lexer, data, currentTokenAndHistory, false);
        currentTokenAndHistory = modifiers.currentTokenAndHistory;
        ParseResult<AbstractBastInternalDecl> member =
            memberDecl(lexer, data, currentTokenAndHistory);
        if (modifiers.value != null && !modifiers.value.isEmpty() && member.value != null) {
          member.value.setModifiers(modifiers.value);
        }
        return member;
    }
  }

  private ParseResult<LinkedList<AbstractBastInternalDecl>> classBodyDeclarationList(
      final JavaLexer lexer, final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    LinkedList<AbstractBastInternalDecl> list = null;
    ParseResult<AbstractBastInternalDecl> classBody =
        classBodyDeclaration(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = classBody.currentTokenAndHistory;
    while (classBody.value != null) {
      list = add(list, classBody.value);
      classBody = classBodyDeclaration(lexer, data, currentTokenAndHistory);
      if (classBody.value != null) {
        currentTokenAndHistory = classBody.currentTokenAndHistory;
      }
    }
    return new ParseResult<LinkedList<AbstractBastInternalDecl>>(list, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastInternalDecl> classDeclaration(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // ClassDeclaration:
    // NormalClassDeclaration
    // EnumDeclaration
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    switch (((JavaToken) nextToken.token).type) {
      case CLASS:
        return normalClassDeclaration(lexer, data, currentTokenAndHistory, nextToken);
      case ENUM:
        return enumDeclaration(lexer, data, currentTokenAndHistory);
      default:
        throw new SyntaxError("Token not expected.", ((JavaToken) nextToken.token));
    }
  }

  private ParseResult<AbstractBastExternalDecl> classOrInterfaceDeclaration(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // {Modifier} (ClassDeclaration | InterfaceDeclaration)
    // LinkedList<AbstractBastSpecifier> modifiers =
    // ModifierList(currentTokenAndHistory, false);
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<LinkedList<AbstractBastSpecifier>> modifiers =
        modifierList(lexer, data, currentTokenAndHistory, false);
    currentTokenAndHistory = modifiers.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<AbstractBastInternalDecl> decl = null;
    switch (((JavaToken) nextToken.token).type) {
      case CLASS:
      case ENUM:
        decl = classDeclaration(lexer, data, currentTokenAndHistory);
        if (decl.value == null) {
          return new ParseResult<AbstractBastExternalDecl>(null, null);
        }
        decl.value.setModifiers(modifiers.value);
        return new ParseResult<AbstractBastExternalDecl>(decl.value, decl.currentTokenAndHistory);
      case INTERFACE:
      case AT:
        decl = interfaceDeclaration(lexer, data, currentTokenAndHistory);
        if (decl.value == null) {
          return new ParseResult<AbstractBastExternalDecl>(null, null);
        }
        decl.value.setModifiers(modifiers.value);
        return new ParseResult<AbstractBastExternalDecl>(decl.value, decl.currentTokenAndHistory);
      case EOF:
        return new ParseResult<AbstractBastExternalDecl>(null, currentTokenAndHistory);
      case INT:
      case BOOLEAN:
      case LONG:
      case DOUBLE:
      case BYTE:
      case CHAR:
      case FLOAT:
      case SHORT:
      case IDENTIFIER:
      case DO:
      case VOID:
      case RBRACE:
      case IF:
      case SWITCH:
      case LBRACE:
        throw new SyntaxError("Class or interface expected!", ((JavaToken) nextToken.token));
      default:
        throw new SyntaxError("Class or interface expected.", ((JavaToken) nextToken.token));
    }
  }

  /**
   * Class type.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<BastClassType> classType(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // Identifier [TypeArguments]{ . Identifier [TypeArguments]} {[]}
    return classType(lexer, data, inputTokenAndHistory, false);
  }

  /**
   * Class type.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<BastClassType> classType(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory, final boolean withDiamond) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);

    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    ParseResult<LinkedList<BastTypeArgument>> typeParameters =
        new ParseResult<LinkedList<BastTypeArgument>>(null, null);
    BastClassType type = null;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isLess(nextToken)) {
      typeParameters = typeArgumentList(lexer, data, currentTokenAndHistory, true, withDiamond);
      currentTokenAndHistory = typeParameters.currentTokenAndHistory;
    }
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    type = new BastClassType(null, nameRes.value, typeParameters.value, null);
    TokenAndHistory nextNextToken = lexer.nextToken(data, nextToken);
    if (TokenChecker.isPoint(nextToken) && TokenChecker.isIdentifier(nextNextToken)) {
      currentTokenAndHistory = nextToken;
      ParseResult<BastClassType> tmpType =
          classType(lexer, data, currentTokenAndHistory, withDiamond);
      currentTokenAndHistory = tmpType.currentTokenAndHistory;
      type = new BastClassType(null, nameRes.value, typeParameters.value, tmpType.value);
    }
    return new ParseResult<BastClassType>(type, currentTokenAndHistory);
  }



  private ParseResult<AbstractBastExpr> condOr(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<AbstractBastExpr> left = prefixExpression3(lexer, data, currentTokenAndHistory);
    ArrayDeque<AbstractBastExpr> stack = new ArrayDeque<AbstractBastExpr>();
    ArrayDeque<TokenAndHistory> tokenStack = new ArrayDeque<TokenAndHistory>();
    ArrayDeque<Integer> tokenCount = new ArrayDeque<Integer>();
    if (left.value == null) {
      return new ParseResult<AbstractBastExpr>(null, null);
    }
    stack.push(left.value);
    currentTokenAndHistory = left.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
    if (TokenChecker.isExpressionToken(nextToken)) {
      currentTokenAndHistory =
          addExpressionToken(lexer, data, tokenStack, tokenCount, nextToken, nextnextToken);
    } else {
      return left;
    }
    ParseResult<AbstractBastExpr> right = new ParseResult<AbstractBastExpr>(null, null);
    right = rightInCondOr(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = right.currentTokenAndHistory;
    if (right.value == null) {
      return new ParseResult<AbstractBastExpr>(null, null);
    }
    stack.push(right.value);
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    nextnextToken = lexer.nextToken(data, nextToken);
    TokenAndHistory next3Token = null;
    while (TokenChecker.isExpressionToken(nextToken)) {
      BasicJavaToken bjt = ((JavaToken) nextToken.token).type;
      int tokenCounter = 1;
      if (TokenChecker.isLess(nextToken) && TokenChecker.isLess(nextnextToken)) {
        tokenCounter = 2;
        bjt = BasicJavaToken.SLL;
      } else if (TokenChecker.isGreater(nextToken) && TokenChecker.isGreater(nextnextToken)) {
        next3Token = lexer.nextToken(data, nextnextToken);
        if (TokenChecker.isGreater(next3Token)) {
          bjt = BasicJavaToken.SLR;
          tokenCounter = 3;
        } else {
          tokenCounter = 2;
          bjt = BasicJavaToken.SAR;
        }
      }
      while (!tokenStack.isEmpty()
          && higherOrEqualPriority(getPeekToken(tokenStack, tokenCount), bjt)) {

        stackMergeIteration(stack, tokenStack, tokenCount);

      }

      tokenStack.push(nextToken);
      if (tokenCounter == 2) {
        tokenStack.push(nextnextToken);
        currentTokenAndHistory = nextnextToken;
      } else if (tokenCounter == 3) {
        tokenStack.push(nextnextToken);
        tokenStack.push(next3Token);
        currentTokenAndHistory = next3Token;
      } else {
        currentTokenAndHistory = nextToken;

      }
      tokenCount.push(tokenCounter);
      right = rightInCondOr(lexer, data, currentTokenAndHistory);
      if (right != null) {
        stack.push(right.value);
      } else {
        throw new SyntaxError("Expression expected.", ((JavaToken) nextToken.token));
      }
      currentTokenAndHistory = right.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      nextnextToken = lexer.nextToken(data, nextToken);
    }

    while (stack.size() > 1) {
      stackMergeIteration(stack, tokenStack, tokenCount);
    }
    return new ParseResult<AbstractBastExpr>(stack.pop(), currentTokenAndHistory);
  }
  
  private BasicJavaToken getPeekToken(ArrayDeque<TokenAndHistory> tokenStack,
      ArrayDeque<Integer> tokenCount) {
    if (tokenCount.peek() == 1) {
      return ((JavaToken) tokenStack.peek().token).type;
    }
    TokenAndHistory second = tokenStack.pop();
    TokenAndHistory first = tokenStack.peek();
    tokenStack.push(second);
    if (TokenChecker.isLess(first) && TokenChecker.isLess(second)) {
      return BasicJavaToken.SLL;
    } else {
      assert (TokenChecker.isGreater(first) && TokenChecker.isGreater(second));
      if (tokenCount.peek() == 3) {
        return BasicJavaToken.SLR;
      } else {
        return BasicJavaToken.SAR;
      }
    }
  }
  
  private ParseResult<AbstractBastExpr> rightInCondOr(final JavaLexer lexer, final FileData data,
      TokenAndHistory currentTokenAndHistory) {
    ParseResult<AbstractBastExpr> right;
    if (TokenChecker.isInstanceOf(currentTokenAndHistory)) {
      ParseResult<BastType> tmp = type(lexer, data, currentTokenAndHistory);
      right = new ParseResult<AbstractBastExpr>(tmp.value, tmp.currentTokenAndHistory);
    } else {
      right = prefixExpression3(lexer, data, currentTokenAndHistory);
    }
    return right;
  }

  private TokenAndHistory addExpressionToken(final JavaLexer lexer, final FileData data,
      ArrayDeque<TokenAndHistory> tokenStack, ArrayDeque<Integer> tokenCount,
      TokenAndHistory nextToken,
      TokenAndHistory nextnextToken) {
    TokenAndHistory currentTokenAndHistory;
    tokenStack.push(nextToken);
    currentTokenAndHistory = nextToken;
    if (TokenChecker.isLess(nextToken) && TokenChecker.isLess(nextnextToken)) {
      tokenStack.push(nextnextToken);
      currentTokenAndHistory = nextnextToken;
      tokenCount.push(2);
    } else if (TokenChecker.isGreater(nextToken) && TokenChecker.isGreater(nextnextToken)) {
      TokenAndHistory next3Token = lexer.nextToken(data, nextnextToken);
      if (TokenChecker.isGreater(next3Token)) {
        tokenStack.push(nextnextToken);
        tokenStack.push(next3Token);
        currentTokenAndHistory = next3Token;
        tokenCount.push(3);
      } else {
        tokenStack.push(nextnextToken);
        currentTokenAndHistory = nextnextToken;
        tokenCount.push(2);
      }
    } else {
      tokenCount.push(1);
    }
    return currentTokenAndHistory;
  }

  private void stackMergeIteration(ArrayDeque<AbstractBastExpr> stack,
      ArrayDeque<TokenAndHistory> tokenStack,
      ArrayDeque<Integer> tokenCount) {
    AbstractBastExpr leftExpr;
    AbstractBastExpr rightExpr;
    rightExpr = stack.pop();
    leftExpr = stack.pop();
    TokenAndHistory tmp = tokenStack.pop();
    TokenAndHistory tmp2 = null;
    TokenAndHistory tmp3 = null;
    if (tokenCount.peek() == 3) {
      tmp2 = tokenStack.pop();
      tmp3 = tokenStack.pop();
    } else if (tokenCount.peek() == 2) {
      tmp2 = tokenStack.pop();
    }
    tokenCount.pop();

    AbstractBastExpr merge = mergeStack(leftExpr, rightExpr, tmp, tmp2, tmp3, tokenStack);
    stack.push(merge);
  }



  private ParseResult<AbstractBastDeclarator> constantDeclarator(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // ConstantDeclarator:
    // Identifier ConstantDeclaratorRest
    // ConstantDeclaratorRest:
    // {[]} = VariableInitializer
    // VariableInitializer:
    // ArrayInitializer
    // Expression
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    BastIdentDeclarator identDecl = null;
    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);

    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    if (nameRes.value == null) {
      return new ParseResult<AbstractBastDeclarator>(null, null);
    }
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    int count = 0;
    if (TokenChecker.isLeftBracket(nextToken)) {
      TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
      while (TokenChecker.isLeftBracket(nextToken) && TokenChecker.isRightBracket(nextnextToken)) {
        currentTokenAndHistory = nextnextToken;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        nextnextToken = lexer.nextToken(data, nextToken);
        count++;
      }
    }
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    TokenAndHistory[] tokenstmp = new TokenAndHistory[1];
    if (count != 0) {
      tokenstmp[0] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    if (TokenChecker.isEqual(nextToken)) {
      tokens[0] = nextToken;
    }

    if (count != 0) {
      identDecl = new BastIdentDeclarator(tokens, nameRes.value, null,
          new BastArrayDeclarator(tokens, null, null, count));
    } else {
      identDecl = new BastIdentDeclarator(tokens, nameRes.value, null, null);
    }
    ParseResult<AbstractBastInitializer> init =
        new ParseResult<AbstractBastInitializer>(null, null);
    if (TokenChecker.isEqual(nextToken)) {
      currentTokenAndHistory = nextToken;
      init = variableInitializer(lexer, data, currentTokenAndHistory);
      identDecl.setInitializer(init.value);
      currentTokenAndHistory = init.currentTokenAndHistory;

    }
    return new ParseResult<AbstractBastDeclarator>(identDecl, currentTokenAndHistory);
  }

  private ParseResult<LinkedList<AbstractBastDeclarator>> constantDeclaratorsRest(
      final JavaLexer lexer, final FileData data, final TokenAndHistory inputTokenAndHistory,
      BastNameIdent name) {
    // ConstantDeclaratorsRest:
    // ConstantDeclaratorRest { , ConstantDeclarator }
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    AbstractBastDeclarator stmt = null;
    int count = 0;
    if (TokenChecker.isLeftBracket(nextToken)) {
      TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
      while (TokenChecker.isLeftBracket(nextToken) && TokenChecker.isRightBracket(nextnextToken)) {
        currentTokenAndHistory = nextnextToken;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        nextnextToken = lexer.nextToken(data, nextToken);
        count++;
      }
    }
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    TokenAndHistory[] tokenstmp = new TokenAndHistory[1];
    if (count != 0) {
      tokenstmp[0] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    if (TokenChecker.isEqual(nextToken)) {
      tokens[0] = nextToken;
    }

    if (count != 0) {
      stmt = new BastIdentDeclarator(tokens, name, null,
          new BastArrayDeclarator(tokens, null, null, count));
    } else {
      stmt = new BastIdentDeclarator(tokens, name, null, null);
    }
    ParseResult<AbstractBastInitializer> init =
        new ParseResult<AbstractBastInitializer>(null, null);
    if (TokenChecker.isEqual(nextToken)) {
      currentTokenAndHistory = nextToken;
      init = variableInitializer(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = init.currentTokenAndHistory;
      stmt.setInitializer(init.value);

    }
    LinkedList<AbstractBastDeclarator> list = null;
    list = add(list, stmt);
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<AbstractBastDeclarator> stmtRes =
        new ParseResult<AbstractBastDeclarator>(null, null);
    while (TokenChecker.isComma(nextToken)) {
      currentTokenAndHistory = nextToken;
      stmtRes = constantDeclarator(lexer, data, currentTokenAndHistory);
      if (stmt != null) {
        list = add(list, stmtRes.value);
      }
      currentTokenAndHistory = stmtRes.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    return new ParseResult<LinkedList<AbstractBastDeclarator>>(list, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastInternalDecl> constructorDeclaratorRest(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory, BastNameIdent name,
      LinkedList<BastTypeParameter> typeParameters) {
    // ConstructorDeclaratorRest:
    // FormalParameters [throws QualifiedIdentifierList] MethodBody
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<LinkedList<AbstractBastExpr>> exceptions =
        new ParseResult<LinkedList<AbstractBastExpr>>(null, null);
    ParseResult<BastParameterList> paramList =
        formalParameters(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = paramList.currentTokenAndHistory;
    BastFunctionParameterDeclarator decl =
        new BastFunctionParameterDeclarator(null, paramList.value, null);
    BastIdentDeclarator ident = new BastIdentDeclarator(null, name, null, decl);
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    if (TokenChecker.isThrows(nextToken)) {
      currentTokenAndHistory = nextToken;
      tokens[0] = currentTokenAndHistory;
      exceptions = qualifiedIdentifierList(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = exceptions.currentTokenAndHistory;
    }
    ParseResult<BastBlock> methodBody = block(lexer, data, currentTokenAndHistory, false);
    currentTokenAndHistory = methodBody.currentTokenAndHistory;
    BastFunction function = new BastFunction(tokens, null, ident, null, methodBody.value,
        typeParameters, null, exceptions.value);
    return new ParseResult<AbstractBastInternalDecl>(function, methodBody.currentTokenAndHistory);
  }



  protected ParseResult<AbstractBastStatement> continueStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectContinue(nextToken);
    currentTokenAndHistory = nextToken;
    final TokenAndHistory[] tokens = { currentTokenAndHistory };
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<BastNameIdent> nameRes = new ParseResult<BastNameIdent>(null, null);
    if (TokenChecker.isNotSemicolon(nextToken)) {
      nameRes = identifier(lexer, data, currentTokenAndHistory);

      currentTokenAndHistory = nameRes.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    TokenChecker.expectSemicolon(nextToken);
    BastContinue continueStmt = new BastContinue(tokens, nameRes.value);
    currentTokenAndHistory = nextToken;
    continueStmt.info.tokensAfter.add(currentTokenAndHistory);
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    return new ParseResult<AbstractBastStatement>(continueStmt, currentTokenAndHistory);

  }

  protected TokenAndHistory convertAresToken(TokenAndHistory token) {
    if (TokenChecker.isNotIdentifier(token)) {
      return token;
    }
    switch (((JavaToken) token.token).data.toString()) {
      case "match":
        ((JavaToken) token.token).type = BasicJavaToken.MATCH;
        break;
      case "wildcard":
        ((JavaToken) token.token).type = BasicJavaToken.WILDCARD;
        break;
      case "use":
        ((JavaToken) token.token).type = BasicJavaToken.USE;
        break;
      case "pattern":
        ((JavaToken) token.token).type = BasicJavaToken.PATTERN;
        break;
      case "plugin":
        ((JavaToken) token.token).type = BasicJavaToken.PLUGIN;
        break;
      case "original":
        ((JavaToken) token.token).type = BasicJavaToken.ORIGINAL;
        break;
      case "modified":
        ((JavaToken) token.token).type = BasicJavaToken.MODIFIED;
        break;
      case "transformed":
        ((JavaToken) token.token).type = BasicJavaToken.MODIFIED;
        break;
      case "choice":
        ((JavaToken) token.token).type = BasicJavaToken.CHOICE;
        break;
      default:
        return null;
    }
    return token;

  }



  private ParseResult<BastType> createdName(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // CreatedName:
    // Identifier [TypeArgumentsOrDiamond] {. Identifier
    // [TypeArgumentsOrDiamond]}
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<BastNameIdent> nameRes = new ParseResult<BastNameIdent>(null, null);
    BastClassType type = null;
    ParseResult<LinkedList<BastTypeArgument>> typeParameters =
        new ParseResult<LinkedList<BastTypeArgument>>(null, null);
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    switch (((JavaToken) nextToken.token).type) {
      case IDENTIFIER:

        nameRes = identifier(lexer, data, currentTokenAndHistory);

        currentTokenAndHistory = nameRes.currentTokenAndHistory;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        if (TokenChecker.isLess(nextToken)) {
          typeParameters = typeArgumentList(lexer, data, currentTokenAndHistory, true, true);
          currentTokenAndHistory = typeParameters.currentTokenAndHistory;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
        }

        type = new BastClassType(null, nameRes.value, typeParameters.value, null);
        TokenAndHistory nextNextToken = lexer.nextToken(data, nextToken);
        if (TokenChecker.isPoint(nextToken) && TokenChecker.isIdentifier(nextNextToken)) {
          currentTokenAndHistory = nextToken;
          ParseResult<BastClassType> tmpType = classType(lexer, data, currentTokenAndHistory, true);
          currentTokenAndHistory = tmpType.currentTokenAndHistory;
          type = new BastClassType(null, nameRes.value, typeParameters.value, tmpType.value);
        }
        return new ParseResult<BastType>(type, currentTokenAndHistory);
      case DOUBLE:
      case INT:
      case BYTE:
      case LONG:
      case SHORT:
      case CHAR:
      case BOOLEAN:
      case FLOAT:
        ParseResult<BastType> tmpType =
            basicTypeWithoutBrackets(lexer, data, currentTokenAndHistory);
        return tmpType;
      case RBRACE:
        throw new SyntaxError("Incomplete statement.", ((JavaToken) nextToken.token));
      default:
        throw new SyntaxError("Incomplete statement.", ((JavaToken) nextToken.token));
    }
  }

  /**
   * Creator.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<AbstractBastExpr> creator(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // Creator:
    // [NonWildcardTypeArguments] CreatedName ( ArrayCreatorRest |
    // ClassCreatorRest )
    // ArrayCreatorRest:
    // [ ( ] {[]} ArrayInitializer | Expression ] {[ Expression ]} {[]} )
    // ClassCreatorRest:
    // Arguments [ClassBody]
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectNew(nextToken);
    TokenAndHistory[] tokens = { nextToken, null, null, null, null, null };
    currentTokenAndHistory = nextToken;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<LinkedList<BastType>> typeArguments =
        new ParseResult<LinkedList<BastType>>(null, null);
    if (TokenChecker.isLess(nextToken)) {
      typeArguments = typeArgumentAsTypeList(lexer, data, currentTokenAndHistory, false);
      currentTokenAndHistory = typeArguments.currentTokenAndHistory;
    }
    ParseResult<BastType> typeRes = createdName(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = typeRes.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    BastType type2 = typeRes.value;
    if (TokenChecker.isLeftBracket(nextToken)) {
      return leftBracketInCreator(lexer, data, currentTokenAndHistory, nextToken, tokens,
          typeArguments, typeRes);
    } else {
      return noLeftBracketInCreator(lexer, data, currentTokenAndHistory, tokens, typeArguments,
          type2);
    }
  }

  private ParseResult<AbstractBastExpr> noLeftBracketInCreator(final JavaLexer lexer,
      final FileData data, TokenAndHistory currentTokenAndHistory, TokenAndHistory[] tokens,
      ParseResult<LinkedList<BastType>> typeArguments, BastType type2) {
    ParseResult<LinkedList<AbstractBastInternalDecl>> declarations =
        new ParseResult<LinkedList<AbstractBastInternalDecl>>(null, null);
    TokenAndHistory nextToken;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectLeftParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[1] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    ParseResult<LinkedList<AbstractBastExpr>> parameters;
    parameters = arguments(lexer, data, currentTokenAndHistory);
    tokens[2] = new TokenAndHistory(new ListToken(parameters.additionalTokens));
    currentTokenAndHistory = parameters.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    tokens[3] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    if (TokenChecker.isLeftBrace(nextToken)) {
      currentTokenAndHistory = lexer.nextToken(data, currentTokenAndHistory);
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      tokens[4] = currentTokenAndHistory;
      declarations = classBody(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = declarations.currentTokenAndHistory;
      tokens[5] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    }
    BastNew newNode =
        new BastNew(tokens, typeArguments.value, type2, parameters.value, declarations.value);
    return new ParseResult<AbstractBastExpr>(newNode, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastExpr> leftBracketInCreator(final JavaLexer lexer,
      final FileData data, TokenAndHistory currentTokenAndHistory, TokenAndHistory nextToken,
      TokenAndHistory[] tokens, ParseResult<LinkedList<BastType>> typeArguments,
      ParseResult<BastType> typeRes) {
    ParseResult<LinkedList<AbstractBastExpr>> parameters =
        new ParseResult<LinkedList<AbstractBastExpr>>(null, null);
    ParseResult<LinkedList<AbstractBastInternalDecl>> declarations =
        new ParseResult<LinkedList<AbstractBastInternalDecl>>(null, null);
    TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
    LinkedList<AbstractBastExpr> arrayList = null;
    ArrayList<TokenAndHistory> additionalTokens = new ArrayList<>();
    ParseResult<AbstractBastExpr> arrayExpr;
    BastType type2;
    if (TokenChecker.isRightBracket(nextnextToken)) {
      int count = 0;
      while (TokenChecker.isLeftBracket(nextToken) && TokenChecker.isRightBracket(nextnextToken)) {
        additionalTokens.add(nextToken);
        currentTokenAndHistory = nextToken.setFlushed();
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        additionalTokens.add(nextToken);
        currentTokenAndHistory = nextToken.setFlushed();
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        nextnextToken = lexer.nextToken(data, nextToken);
        count++;
      }
      ParseResult<BastListInitializer> arrayInit =
          arrayInitializer(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = arrayInit.currentTokenAndHistory;
      TokenAndHistory[] tokenstmp = new TokenAndHistory[2];
      tokenstmp[1] = new TokenAndHistory(new ListToken(additionalTokens));
      type2 = new BastArrayType(tokenstmp, typeRes.value, null, count, arrayInit.value);
    } else {
      int count = 0;
      while (TokenChecker.isLeftBracket(nextToken)) {
        additionalTokens.add(nextToken);
        currentTokenAndHistory = nextToken.setFlushed();
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        if (TokenChecker.isNotRightBracket(nextToken)) {
          arrayExpr = expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
          currentTokenAndHistory = arrayExpr.currentTokenAndHistory;
          arrayList = add(arrayList, arrayExpr.value);
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
        }
        TokenChecker.expectRichtBracket(nextToken);
        additionalTokens.add(nextToken);
        currentTokenAndHistory = nextToken.setFlushed();
        count++;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
      }
      TokenAndHistory[] tokenstmp = new TokenAndHistory[2];
      tokenstmp[1] = new TokenAndHistory(new ListToken(additionalTokens));
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      type2 = new BastArrayType(tokenstmp, typeRes.value, arrayList, count);
    }
    BastNew newNode =
        new BastNew(tokens, typeArguments.value, type2, parameters.value, declarations.value);
    return new ParseResult<AbstractBastExpr>(newNode, currentTokenAndHistory);
  }



  protected ParseResult<AbstractBastStatement> doWhileStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // do Statement while ParExpression ;
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokens = new TokenAndHistory[4];
    TokenChecker.expectDo(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[0] = currentTokenAndHistory;
    ParseResult<AbstractBastStatement> stmt = statement(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = stmt.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectWhile(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[1] = currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectLeftParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[2] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    ParseResult<AbstractBastExpr> condition =
        parExpression(lexer, data, currentTokenAndHistory, false);
    currentTokenAndHistory = condition.currentTokenAndHistory;
    tokens[3] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectSemicolon(nextToken);
    BastWhileStatement whileStmt = new BastWhileStatement(tokens, condition.value, stmt.value,
        BastWhileStatement.TYPE_DO_WHILE);
    currentTokenAndHistory = nextToken;
    whileStmt.info.tokensAfter.add(currentTokenAndHistory);
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    return new ParseResult<AbstractBastStatement>(whileStmt, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastExpr> elementValue(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // ElementValue:
    // Annotation
    // Expression1
    // ElementValueArrayInitializer
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    switch (((JavaToken) nextToken.token).type) {
      case AT:
        ParseResult<BastAnnotation> result = annotation(lexer, data, currentTokenAndHistory);
        return new ParseResult<AbstractBastExpr>(result.value, result.currentTokenAndHistory);
      case LBRACE:
        return elementValueArrayInitializer(lexer, data, currentTokenAndHistory);
      default:
        return expression1bastCondExpr(lexer, data, currentTokenAndHistory);
    }
  }

  private ParseResult<AbstractBastExpr> elementValueArrayInitializer(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // ElementValueArrayInitializer:
    // { [ElementValues] [,] }
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<LinkedList<AbstractBastExpr>> initListRes =
        new ParseResult<LinkedList<AbstractBastExpr>>(null, null);
    LinkedList<AbstractBastExpr> initList = null;
    TokenChecker.expectLeftBrace(nextToken);
    currentTokenAndHistory = nextToken;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    boolean withComma = false;
    if (TokenChecker.isComma(nextToken)) {
      withComma = true;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    } else {
      initListRes = elementValueList(lexer, data, currentTokenAndHistory);
      initList = initListRes.value;
      currentTokenAndHistory = initListRes.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      if (TokenChecker.isComma(nextToken)) {
        withComma = true;
        currentTokenAndHistory = nextToken;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
      }
    }
    TokenChecker.expectRightBrace(nextToken);
    currentTokenAndHistory = nextToken;
    TokenAndHistory[] tokens = { null, null };
    BastAnnotationElemValue value = new BastAnnotationElemValue(tokens, null, initList, withComma);
    return new ParseResult<AbstractBastExpr>(value, currentTokenAndHistory);
  }

  private ParseResult<LinkedList<AbstractBastExpr>> elementValueList(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isRightBrace(nextToken)) {
      return new ParseResult<LinkedList<AbstractBastExpr>>(null, currentTokenAndHistory);
    }
    ParseResult<AbstractBastExpr> elemRes = elementValue(lexer, data, currentTokenAndHistory);
    AbstractBastExpr elem = elemRes.value;
    currentTokenAndHistory = elemRes.currentTokenAndHistory;
    assert (elem != null);
    LinkedList<AbstractBastExpr> list = null;
    list = add(list, elem);
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
    while (TokenChecker.isComma(nextToken) && TokenChecker.isNotRightBrace(nextnextToken)) {
      currentTokenAndHistory = nextToken;
      elemRes = elementValue(lexer, data, currentTokenAndHistory);
      elem = elemRes.value;
      list = add(list, elem);
      currentTokenAndHistory = elemRes.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      nextnextToken = lexer.nextToken(data, nextToken);
    }
    return new ParseResult<LinkedList<AbstractBastExpr>>(list, currentTokenAndHistory);
  }

  private ParseResult<BastAnnotationElemValue> elementValuePair(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // ElementValuePair:
    // Identifier = ElementValue
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);

    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectEqual(nextToken);
    currentTokenAndHistory = nextToken;
    TokenAndHistory[] tokens = { currentTokenAndHistory, null };
    LinkedList<AbstractBastExpr> initList = null;
    ParseResult<AbstractBastExpr> exprResult = elementValue(lexer, data, currentTokenAndHistory);
    AbstractBastExpr expr = exprResult.value;
    initList = add(initList, expr);
    BastAnnotationElemValue elem =
        new BastAnnotationElemValue(tokens, nameRes.value, initList, false);
    return new ParseResult<BastAnnotationElemValue>(elem, exprResult.currentTokenAndHistory);

  }

  private ParseResult<LinkedList<AbstractBastExpr>> elementValuePairList(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // ElementValuePairs:
    // ElementValuePair { , ElementValuePair }
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    LinkedList<AbstractBastExpr> list = null;
    ParseResult<BastAnnotationElemValue> pairRes =
        elementValuePair(lexer, data, currentTokenAndHistory);
    BastAnnotationElemValue pair = pairRes.value;
    list = add(list, pair);
    currentTokenAndHistory = pairRes.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
    while (TokenChecker.isComma(nextToken) && TokenChecker.isNotRightParenthesis(nextnextToken)) {
      currentTokenAndHistory = nextToken;
      pairRes = elementValuePair(lexer, data, currentTokenAndHistory);
      pair = pairRes.value;
      list = add(list, pair);
      currentTokenAndHistory = pairRes.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      nextnextToken = lexer.nextToken(data, nextToken);
    }
    return new ParseResult<LinkedList<AbstractBastExpr>>(list, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastInternalDecl> enumDeclaration(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // EnumDeclaration:
    // enum Identifier [implements TypeList] EnumBody
    // EnumBody:
    // { [EnumConstants] [,] [EnumBodyDeclarations] }

    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectEnum(nextToken);
    currentTokenAndHistory = nextToken;
    TokenAndHistory[] tokens = new TokenAndHistory[5];

    tokens[0] = currentTokenAndHistory;
    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);

    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<LinkedList<BastType>> typeList = new ParseResult<LinkedList<BastType>>(null, null);
    ParseResult<LinkedList<AbstractBastInternalDecl>> declarations =
        new ParseResult<LinkedList<AbstractBastInternalDecl>>(null, null);
    if (TokenChecker.isImplements(nextToken)) {
      currentTokenAndHistory = nextToken;
      typeList = typeList(lexer, data, currentTokenAndHistory);
      tokens[1] = currentTokenAndHistory;
      currentTokenAndHistory = typeList.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    TokenChecker.expectLeftBrace(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[2] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<LinkedList<BastEnumMember>> members =
        new ParseResult<LinkedList<BastEnumMember>>(null, null);
    if (TokenChecker.isNotRightBrace(nextToken)) {
      if (TokenChecker.isNotSemicolon(nextToken)) {
        members = enumMembers(lexer, data, currentTokenAndHistory);
        if (members.currentTokenAndHistory != null) {
          currentTokenAndHistory = members.currentTokenAndHistory;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
          if (TokenChecker.isComma(nextToken)) {
            currentTokenAndHistory = nextToken;
            nextToken = lexer.nextToken(data, currentTokenAndHistory);
          }
        }
      }
      if (TokenChecker.isSemicolon(nextToken) || members.currentTokenAndHistory == null) {
        currentTokenAndHistory = nextToken;
        if (TokenChecker.isSemicolon(currentTokenAndHistory) && !currentTokenAndHistory.flushed) {
          tokens[3] = currentTokenAndHistory;
          currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        }
        declarations = classBodyDeclarationList(lexer, data, currentTokenAndHistory);
        currentTokenAndHistory = declarations.currentTokenAndHistory;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
      }
    }
    TokenChecker.expectRightBrace(nextToken);

    currentTokenAndHistory = nextToken;
    tokens[4] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    BastEnumSpec enumerator =
        new BastEnumSpec(tokens, nameRes.value, typeList.value, members.value, declarations.value);
    BastEnumDecl decl = new BastEnumDecl(null, enumerator);
    return new ParseResult<AbstractBastInternalDecl>(decl, currentTokenAndHistory);
  }



  private ParseResult<BastEnumMember> enumMember(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // EnumConstant:
    // [Annotations] Identifier [Arguments] [ClassBody]
    TokenAndHistory[] tokens = new TokenAndHistory[5];
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);

    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    if (nameRes.value == null) {
      return new ParseResult<BastEnumMember>(null, null);
    }
    assert (nameRes.value != null);
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<LinkedList<AbstractBastExpr>> arguments =
        new ParseResult<LinkedList<AbstractBastExpr>>(null, null);
    if (TokenChecker.isLeftParenthesis(nextToken)) {
      currentTokenAndHistory = nextToken;
      tokens[0] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      arguments = arguments(lexer, data, currentTokenAndHistory);
      tokens[1] = new TokenAndHistory(new ListToken(arguments.additionalTokens));

      if (arguments.value == null) {
        return new ParseResult<BastEnumMember>(null, null);
      }
      currentTokenAndHistory = arguments.currentTokenAndHistory;
      tokens[2] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      nextToken = lexer.nextToken(data, currentTokenAndHistory);

    }
    ParseResult<LinkedList<AbstractBastInternalDecl>> declarations =
        new ParseResult<LinkedList<AbstractBastInternalDecl>>(null, null);
    if (TokenChecker.isLeftBrace(nextToken)) {
      currentTokenAndHistory = nextToken;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      tokens[3] = currentTokenAndHistory;
      declarations = classBody(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = declarations.currentTokenAndHistory;
      tokens[4] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    }
    BastEnumMember member =
        new BastEnumMember(tokens, null, nameRes.value, arguments.value, declarations.value);
    return new ParseResult<BastEnumMember>(member, currentTokenAndHistory);
  }

  private ParseResult<LinkedList<BastEnumMember>> enumMembers(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // EnumConstants:
    // EnumConstant
    // EnumConstants , EnumConstant
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    LinkedList<BastEnumMember> list = null;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isComma(nextToken)) {
      return new ParseResult<LinkedList<BastEnumMember>>(null, currentTokenAndHistory);
    }
    ParseResult<BastEnumMember> member = enumMember(lexer, data, currentTokenAndHistory);
    if (member.value != null) {
      list = add(list, member.value);
      currentTokenAndHistory = member.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }

    TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
    while (TokenChecker.isComma(nextToken) && TokenChecker.isIdentifier(nextnextToken)) {
      currentTokenAndHistory = nextToken;
      member = enumMember(lexer, data, currentTokenAndHistory);
      list = add(list, member.value);
      currentTokenAndHistory = member.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      nextnextToken = lexer.nextToken(data, nextToken);

    }
    if (list == null || list.isEmpty()) {
      return new ParseResult<LinkedList<BastEnumMember>>(null, null);
    } else {
      return new ParseResult<LinkedList<BastEnumMember>>(list, currentTokenAndHistory);
    }
  }

  private ParseResult<AbstractBastExpr> explicitGenericInvocation(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory, AbstractBastExpr left,
      TokenAndHistory[] pointToken) {
    // ExplicitGenericInvocation:
    // NonWildcardTypeArguments ExplicitGenericInvocationSuffix
    // ExplicitGenericInvocationSuffix:
    // super SuperSuffix
    // Identifier Arguments
    // SuperSuffix:
    // Arguments
    // . Identifier [Arguments]
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<LinkedList<BastTypeArgument>> typeArguments =
        typeArgumentList(lexer, data, currentTokenAndHistory, false, false);
    currentTokenAndHistory = typeArguments.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);

    switch (((JavaToken) nextToken.token).type) {
      case SUPER: {
        ParseResult<LinkedList<AbstractBastExpr>> arguments =
            new ParseResult<LinkedList<AbstractBastExpr>>(null, null);
        AbstractBastExpr expr = null;
        ParseResult<BastNameIdent> ident = new ParseResult<BastNameIdent>(null, null);
        TokenAndHistory[] tokens = { nextToken };
        BastSuper superNode = new BastSuper(tokens);
        assert (left != null);
        expr = new BastTemplateSpecifier(null, superNode, typeArguments.value);
        expr = new BastAccess(pointToken, left, expr);
        currentTokenAndHistory = nextToken;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        if (TokenChecker.isPoint(nextToken)) {
          currentTokenAndHistory = nextToken;
          TokenAndHistory[] tokenstmp = new TokenAndHistory[2];
          tokenstmp[0] = currentTokenAndHistory;
          currentTokenAndHistory = currentTokenAndHistory.setFlushed();
          ident = identifier(lexer, data, currentTokenAndHistory);
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
          assert (ident.value != null);

          expr = new BastAccess(tokenstmp, ident.value, expr);
          if (TokenChecker.isLeftParenthesis(nextToken)) {
            tokens = new TokenAndHistory[3];

            currentTokenAndHistory = nextToken;
            tokens[0] = currentTokenAndHistory;
            currentTokenAndHistory = currentTokenAndHistory.setFlushed();
            arguments = arguments(lexer, data, currentTokenAndHistory);
            tokens[1] = new TokenAndHistory(new ListToken(arguments.additionalTokens));

            currentTokenAndHistory = arguments.currentTokenAndHistory;
            tokens[2] = currentTokenAndHistory;
            currentTokenAndHistory = currentTokenAndHistory.setFlushed();
            expr = new BastCall(tokens, expr, arguments.value);
          }
        } else {
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
          tokens = new TokenAndHistory[3];
          currentTokenAndHistory = nextToken;
          tokens[0] = currentTokenAndHistory;
          currentTokenAndHistory = currentTokenAndHistory.setFlushed();
          arguments = arguments(lexer, data, currentTokenAndHistory);
          tokens[1] = new TokenAndHistory(new ListToken(arguments.additionalTokens));
          currentTokenAndHistory = arguments.currentTokenAndHistory;
          tokens[2] = currentTokenAndHistory;
          currentTokenAndHistory = currentTokenAndHistory.setFlushed();
          expr = new BastCall(tokens, expr, arguments.value);
        }
        return new ParseResult<AbstractBastExpr>(expr, currentTokenAndHistory);
      }
      case IDENTIFIER: {
        AbstractBastExpr expr = null;
        ParseResult<BastNameIdent> ident = new ParseResult<BastNameIdent>(null, null);
        ident = identifier(lexer, data, currentTokenAndHistory);
        currentTokenAndHistory = ident.currentTokenAndHistory;
        expr = new BastTemplateSpecifier(null, ident.value, typeArguments.value);

        expr = new BastAccess(pointToken, left, expr);
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        TokenAndHistory[] tokens = new TokenAndHistory[3];
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        ParseResult<LinkedList<AbstractBastExpr>> arguments =
            arguments(lexer, data, currentTokenAndHistory);
        tokens[1] = new TokenAndHistory(new ListToken(arguments.additionalTokens));

        currentTokenAndHistory = arguments.currentTokenAndHistory;
        tokens[2] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        expr = new BastCall(tokens, expr, arguments.value);
        return new ParseResult<AbstractBastExpr>(expr, currentTokenAndHistory);
      }
      default:
        throw new SyntaxError("SUPER or IDENTIFIER expected.", ((JavaToken) nextToken.token));

    }
  }

  /**
   * Expression1bast cond expr.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<AbstractBastExpr> expression1bastCondExpr(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // Expression1:
    // Expression2 [Expression1Rest]
    // Expression1Rest:
    // ? Expression : Expression1
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<AbstractBastExpr> left = condOr(lexer, data, currentTokenAndHistory);
    if (left.value == null) {
      return new ParseResult<AbstractBastExpr>(null, null);
    }
    currentTokenAndHistory = left.currentTokenAndHistory;
    TokenAndHistory[] tokens = new TokenAndHistory[2];
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isQuestionmark(nextToken)) {
      currentTokenAndHistory = nextToken;
      tokens[0] = currentTokenAndHistory;
      ParseResult<AbstractBastExpr> truePart =
          expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = truePart.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      TokenChecker.expectColon(nextToken);
      currentTokenAndHistory = nextToken;
      tokens[1] = currentTokenAndHistory;
      ParseResult<AbstractBastExpr> falsePart =
          expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = falsePart.currentTokenAndHistory;
      return new ParseResult<AbstractBastExpr>(
          new BastCondExpr(tokens, left.value, truePart.value, falsePart.value),
          currentTokenAndHistory);
    } else {
      return left;
    }
  }


  /**
   * Expressionbast asgn expr.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<AbstractBastExpr> expressionbastAsgnExpr(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // Expression:
    // Expression1 [AssignmentOperator Expression1]]
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<AbstractBastExpr> left =
        expression1bastCondExpr(lexer, data, currentTokenAndHistory);
    if (left.value == null) {
      return new ParseResult<AbstractBastExpr>(null, null);
    }
    currentTokenAndHistory = left.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    int operation = -1;
    switch (((JavaToken) nextToken.token).type) {
      case EQUAL:
        operation = BastAsgnExpr.EXPR_TYPE_EQUAL;
        break;
      case MULTIPLY_EQUAL:
        operation = BastAsgnExpr.EXPR_TYPE_MULTIPLY_EQUAL;
        break;
      case DIVIDE_EQUAL:
        operation = BastAsgnExpr.EXPR_TYPE_DIVIDE_EQUAL;
        break;
      case REMAINDER_EQUAL:
        operation = BastAsgnExpr.EXPR_TYPE_REMAINDER_EQUAL;
        break;
      case PLUS_EQUAL:
        operation = BastAsgnExpr.EXPR_TYPE_PLUS_EQUAL;
        break;
      case MINUS_EQUAL:
        operation = BastAsgnExpr.EXPR_TYPE_MINUS_EQUAL;
        break;
      case SLL_EQUAL:
        operation = BastAsgnExpr.EXPR_TYPE_SLL_EQUAL;
        break;
      case SLR_EQUAL:
        operation = BastAsgnExpr.EXPR_TYPE_SLR_EQUAL;
        break;
      case SAR_EQUAL:
        operation = BastAsgnExpr.EXPR_TYPE_SAR_EQUAL;
        break;
      case AND_EQUAL:
        operation = BastAsgnExpr.EXPR_TYPE_AND_EQUAL;
        break;
      case XOR_EQUAL:
        operation = BastAsgnExpr.EXPR_TYPE_XOR_EQUAL;
        break;
      case OR_EQUAL:
        operation = BastAsgnExpr.EXPR_TYPE_OR_EQUAL;
        break;
      default:
        // Do nothing
    }
    if (operation != -1) {
      currentTokenAndHistory = nextToken;
      TokenAndHistory[] tokens = { currentTokenAndHistory };
      ParseResult<AbstractBastExpr> right =
          expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = right.currentTokenAndHistory;
      BastAsgnExpr asgn = new BastAsgnExpr(tokens, left.value, right.value, operation);
      return new ParseResult<AbstractBastExpr>(asgn, currentTokenAndHistory);
    }

    return left;
  }

  private ParseResult<LinkedList<AbstractBastExpr>> expressionList(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    LinkedList<AbstractBastExpr> list = null;
    ParseResult<AbstractBastExpr> expr =
        expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = expr.currentTokenAndHistory;
    list = add(list, expr.value);
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    while (TokenChecker.isComma(nextToken)) {
      currentTokenAndHistory = nextToken;
      expr = expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
      list = add(list, expr.value);
      currentTokenAndHistory = expr.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    return new ParseResult<LinkedList<AbstractBastExpr>>(list, currentTokenAndHistory);

  }

  /**
   * Field declarator rest.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<AbstractBastInternalDecl> fieldDeclaratorRest(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // Changed to fit Java 1.7 20120301
    // FieldDeclaratorsRest:
    // VariableDeclaratorRest { , VariableDeclarator }
    // VariableDeclarator:
    // Identifier VariableDeclaratorRest
    // VariableDeclaratorRest:
    // {[]} [ = VariableInitializer]
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<BastType> typeRes = type(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = typeRes.currentTokenAndHistory;
    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    LinkedList<AbstractBastSpecifier> declSpecList = new LinkedList<AbstractBastSpecifier>();
    LinkedList<AbstractBastDeclarator> initDeclList = new LinkedList<AbstractBastDeclarator>();
    BastTypeSpecifier specifier = new BastTypeSpecifier(null, typeRes.value);
    BastIdentDeclarator identDecl = null;
    declSpecList.add(specifier);
    boolean comma = true;
    while (comma) {
      int count = 0;
      TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
      if (TokenChecker.isLeftBracket(nextToken)) {
        TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
        while (TokenChecker.isLeftBracket(nextToken)
            && TokenChecker.isRightBracket(nextnextToken)) {
          currentTokenAndHistory = nextnextToken;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
          nextnextToken = lexer.nextToken(data, nextToken);
          count++;
        }
      }
      TokenAndHistory[] tokens = new TokenAndHistory[1];
      TokenAndHistory[] tokenstmp = new TokenAndHistory[1];
      if (count != 0) {
        tokenstmp[0] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
      }
      if (TokenChecker.isEqual(nextToken)) {
        tokens[0] = nextToken;
      }
      if (count != 0) {
        identDecl = new BastIdentDeclarator(tokens, nameRes.value, null,
            new BastArrayDeclarator(tokenstmp, null, null, count));
      } else {
        identDecl = new BastIdentDeclarator(tokens, nameRes.value, null, null);
      }
      ParseResult<AbstractBastInitializer> init =
          new ParseResult<AbstractBastInitializer>(null, null);
      if (TokenChecker.isEqual(nextToken)) {
        currentTokenAndHistory = nextToken;
        init = variableInitializer(lexer, data, currentTokenAndHistory);
        currentTokenAndHistory = init.currentTokenAndHistory;
        identDecl.setInitializer(init.value);

      }
      initDeclList.add(identDecl);
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      if (TokenChecker.isComma(nextToken)) {
        currentTokenAndHistory = nextToken;
        comma = true;
        nameRes = identifier(lexer, data, currentTokenAndHistory);

        currentTokenAndHistory = nameRes.currentTokenAndHistory;
      } else {
        comma = false;
      }

    }
    BastDeclaration decl = new BastDeclaration(null, declSpecList, initDeclList);
    return new ParseResult<AbstractBastInternalDecl>(decl, currentTokenAndHistory);
  }

  /**
   * Formal parameters.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<BastParameterList> formalParameters(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // FormalParameters:
    // ( [FormalParameterDecls] )
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokens = new TokenAndHistory[4];
    ArrayList<TokenAndHistory> additionalTokens = new ArrayList<>();
    if (TokenChecker.isLeftParenthesis(nextToken)) {
      currentTokenAndHistory = nextToken;
      tokens[0] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      nextToken = lexer.nextToken(data, currentTokenAndHistory);

      if (TokenChecker.isRightParenthesis(nextToken)) {
        currentTokenAndHistory = nextToken;
        tokens[2] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        BastParameterList parameterList = new BastParameterList(tokens, null, false);
        return new ParseResult<BastParameterList>(parameterList, currentTokenAndHistory);
      }
      ParseResult<BastParameter> parameter = parameter(lexer, data, currentTokenAndHistory);
      if (parameter.value == null) {
        return new ParseResult<BastParameterList>(null, null);
      }
      currentTokenAndHistory = parameter.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      LinkedList<BastParameter> list = null;
      if (parameter.value.isEllipsis == true) {
        list = add(list, parameter.value);
      } else {
        while (parameter.value != null) {
          currentTokenAndHistory = nextToken;
          list = add(list, parameter.value);
          if (TokenChecker.isComma(currentTokenAndHistory)) {
            additionalTokens.add(currentTokenAndHistory);
            currentTokenAndHistory = currentTokenAndHistory.setFlushed();
            parameter = parameter(lexer, data, currentTokenAndHistory);
            currentTokenAndHistory = parameter.currentTokenAndHistory;
            nextToken = lexer.nextToken(data, currentTokenAndHistory);
          } else {
            parameter = new ParseResult<BastParameter>(null, null);
          }
        }
      }
      tokens[1] = new TokenAndHistory(new ListToken(additionalTokens));
      TokenChecker.expectRightParenthesis(nextToken);
      currentTokenAndHistory = nextToken;
      tokens[2] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      boolean isEllipsis = list.getLast().isEllipsis;
      BastParameterList parameterList = new BastParameterList(tokens, list, isEllipsis);
      return new ParseResult<BastParameterList>(parameterList, currentTokenAndHistory);
    }

    return new ParseResult<BastParameterList>(null, null);
  }

  protected ParseResult<AbstractBastStatement> forStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // for ( ForControl ) Statement
    // ForControl:
    // ForVarControl
    // ForInit ; [Expression] ; [ForUpdate]
    //
    // ForVarControl:
    // {VariableModifier} Type VariableDeclaratorId ForVarControlRest
    // ForVarControlRest:
    // ForVariableDeclaratorsRest ; [Expression] ; [ForUpdate]
    // : Expression
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory[] tokens = new TokenAndHistory[6];
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<BastDeclaration> initDecl = new ParseResult<BastDeclaration>(null, null);
    ParseResult<AbstractBastExpr> listStmt = new ParseResult<AbstractBastExpr>(null, null);
    AbstractBastExpr init = null;
    currentTokenAndHistory = parseForBeginning(lexer, data, tokens, nextToken);
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
    switch (((JavaToken) nextToken.token).type) {
      case INT:
      case AT:
      case LONG:
      case FINAL:
      case CHAR:
      case BYTE:
      case DOUBLE:
      case SHORT:
      case BOOLEAN:
      case FLOAT:
      case IDENTIFIER:
      case PLUS_PLUS:
      case THIS:
      case SUPER:
        boolean withType = true;
        if (TokenChecker.isEqual(nextnextToken)) {
          withType = false;
        }
        TokenAndHistory backup = currentTokenAndHistory;
        ParseResult<LinkedList<AbstractBastSpecifier>> modifiers =
            modifierList(lexer, data, currentTokenAndHistory, true);
        currentTokenAndHistory = modifiers.currentTokenAndHistory;
        ParseResult<BastType> typeRes = new ParseResult<BastType>(null, null);
        if (withType) {
          typeRes = type(lexer, data, currentTokenAndHistory);
          currentTokenAndHistory = typeRes.currentTokenAndHistory;
        }
        ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);
        if (nameRes.value == null) {
          currentTokenAndHistory = backup;
          ParseResult<AbstractBastExpr> initResult =
              expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
          currentTokenAndHistory = initResult.currentTokenAndHistory;
          init = initResult.value;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
        } else {
          initDecl = forVarControlHead(lexer, data, currentTokenAndHistory, withType,
              modifiers.value, typeRes.value);
          currentTokenAndHistory = initDecl.currentTokenAndHistory;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
          if (TokenChecker.isColon(nextToken)) {
            currentTokenAndHistory = nextToken;
            tokens[2] = currentTokenAndHistory;
            currentTokenAndHistory = currentTokenAndHistory.setFlushed();
            listStmt = expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);

            currentTokenAndHistory = listStmt.currentTokenAndHistory;
          } else {
            // ForVariableDeclaratorsRest:
            // [ = VariableInitializer ] { , VariableDeclarator }
            nextToken = lexer.nextToken(data, currentTokenAndHistory);
            currentTokenAndHistory =
                parseDeclarationsInFor(lexer, data, currentTokenAndHistory, nextToken, initDecl);
          }

          nextToken = lexer.nextToken(data, currentTokenAndHistory);
        }
        break;
      case SEMICOLON:
        break;
      default:
        throw new SyntaxError("Token not expected.", ((JavaToken) nextToken.token));
    }

    return createForStmt(lexer, data, tokens, nextToken, initDecl, listStmt, init);
  }

  private TokenAndHistory parseDeclarationsInFor(final JavaLexer lexer, final FileData data,
      TokenAndHistory currentTokenAndHistory, TokenAndHistory nextToken,
      ParseResult<BastDeclaration> initDecl) {
    ParseResult<AbstractBastInitializer> initializer =
        new ParseResult<AbstractBastInitializer>(null, null);
    AbstractBastDeclarator declaration = initDecl.value.declaratorList.getFirst();
    ParseResult<AbstractBastDeclarator> declarator =
        new ParseResult<AbstractBastDeclarator>(null, null);
    if (TokenChecker.isEqual(nextToken)) {
      currentTokenAndHistory = nextToken;
      initializer = parseInitializerInFor(lexer, data, currentTokenAndHistory, declaration);
      currentTokenAndHistory = initializer.currentTokenAndHistory;
    }

    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    while (TokenChecker.isComma(nextToken)) {
      currentTokenAndHistory = nextToken;
      declarator = variableDeclarator(lexer, data, currentTokenAndHistory);
      assert (declaration != null);
      initDecl.value.declaratorList.add(declarator.value);
      currentTokenAndHistory = declarator.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    return currentTokenAndHistory;
  }

  private TokenAndHistory parseForBeginning(final JavaLexer lexer, final FileData data,
      TokenAndHistory[] tokens, TokenAndHistory nextToken) {
    TokenAndHistory currentTokenAndHistory;
    TokenChecker.expectFor(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[0] = currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectLeftParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[1] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    return currentTokenAndHistory;
  }

  private ParseResult<AbstractBastInitializer> parseInitializerInFor(final JavaLexer lexer,
      final FileData data, TokenAndHistory currentTokenAndHistory,
      AbstractBastDeclarator declaration) {
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
    ParseResult<AbstractBastInitializer> initializer =
        variableInitializer(lexer, data, currentTokenAndHistory);
    declaration.setInitializer(initializer.value);
    return initializer;
  }

  private ParseResult<AbstractBastStatement> createForStmt(final JavaLexer lexer,
      final FileData data, TokenAndHistory[] tokens, TokenAndHistory nextToken,
      ParseResult<BastDeclaration> initDecl, ParseResult<AbstractBastExpr> listStmt,
      AbstractBastExpr init) {
    AbstractBastExpr increment = null;
    ParseResult<AbstractBastExpr> condition = new ParseResult<AbstractBastExpr>(null, null);
    TokenAndHistory currentTokenAndHistory;
    BastForStmt forStmt;
    if (listStmt.value == null) {
      TokenChecker.expectSemicolon(nextToken);
      currentTokenAndHistory = nextToken;
      tokens[3] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      if (TokenChecker.isNotSemicolon(nextToken)) {
        condition = expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
        currentTokenAndHistory = condition.currentTokenAndHistory;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
      }

      TokenChecker.expectSemicolon(nextToken);
      currentTokenAndHistory = nextToken;
      tokens[4] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      if (TokenChecker.isNotRightParenthesis(nextToken)) {
        ParseResult<LinkedList<AbstractBastExpr>> elist =
            expressionList(lexer, data, currentTokenAndHistory);
        increment = new BastExprList(null, elist.value);
        currentTokenAndHistory = elist.currentTokenAndHistory;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
      }
    }
    TokenChecker.expectRightParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[5] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    final ParseResult<AbstractBastStatement> statement =
        statement(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = statement.currentTokenAndHistory;

    if (listStmt.value != null) {
      forStmt = new BastForStmt(tokens, initDecl.value, listStmt.value, statement.value);
    } else {
      if (initDecl.value != null) {
        forStmt =
            new BastForStmt(tokens, initDecl.value, condition.value, increment, statement.value);
      } else {
        forStmt = new BastForStmt(tokens, init, condition.value, increment, statement.value);
      }
    }

    return new ParseResult<AbstractBastStatement>(forStmt, currentTokenAndHistory);
  }

  /**
   * For var control head.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @param withType the with type
   * @param modifiers the modifiers
   * @param type the type
   * @return the parses the result
   */
  public ParseResult<BastDeclaration> forVarControlHead(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory, boolean withType,
      LinkedList<AbstractBastSpecifier> modifiers, BastType type) {
    // ForVarControl:
    // {VariableModifier} Type VariableDeclaratorId ForVarControlRest
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;

    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);

    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    LinkedList<AbstractBastSpecifier> declSpecList = new LinkedList<AbstractBastSpecifier>();
    LinkedList<AbstractBastDeclarator> initDeclList = new LinkedList<AbstractBastDeclarator>();
    if (withType) {
      BastTypeSpecifier specifier = new BastTypeSpecifier(null, type);
      declSpecList.add(specifier);
    }

    BastIdentDeclarator identDecl =
        new BastIdentDeclarator((TokenAndHistory[]) null, nameRes.value, null, null);
    initDeclList.add(identDecl);
    BastDeclaration member = new BastDeclaration(null, declSpecList, initDeclList);
    if (modifiers != null && !modifiers.isEmpty()) {
      member.setModifiers(modifiers);
    }
    return new ParseResult<BastDeclaration>(member, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastInternalDecl> genericMethodOrConstructorDecl(
      final JavaLexer lexer, final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // TypeParameters GenericMethodOrConstructorRest
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<LinkedList<BastTypeParameter>> typeParameters =
        typeParameters(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = typeParameters.currentTokenAndHistory;
    return genericMethodOrConstructorRest(lexer, data, currentTokenAndHistory,
        typeParameters.value);
  }

  private ParseResult<AbstractBastInternalDecl> genericMethodOrConstructorRest(
      final JavaLexer lexer, final FileData data, final TokenAndHistory inputTokenAndHistory,
      LinkedList<BastTypeParameter> typeParameters) {
    // GenericMethodOrConstructorRest:
    // (Type | void) Identifier MethodDeclaratorRest
    // Identifier ConstructorDeclaratorRest
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<BastNameIdent> nameRes = new ParseResult<BastNameIdent>(null, null);
    switch (((JavaToken) nextToken.token).type) {
      case VOID:
        return methodOrFieldRest(lexer, data, currentTokenAndHistory, typeParameters);
      case BYTE:
      case SHORT:
      case CHAR:
      case INT:
      case LONG:
      case FLOAT:
      case DOUBLE:
      case BOOLEAN:
        return methodDeclaratorRest(lexer, data, currentTokenAndHistory, true, typeParameters);
      case IDENTIFIER:
        TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
        switch (((JavaToken) nextnextToken.token).type) {
          case POINT:
          case LESS:
          case LBRACKET:
            return methodDeclaratorRest(lexer, data, inputTokenAndHistory, true, typeParameters);
          case IDENTIFIER:
            return methodDeclaratorRest(lexer, data, currentTokenAndHistory, true, typeParameters);
          case LPAREN:
            nameRes = identifier(lexer, data, currentTokenAndHistory);
            currentTokenAndHistory = nameRes.currentTokenAndHistory;
            assert (nameRes.value != null);
            return constructorDeclaratorRest(lexer, data, currentTokenAndHistory, nameRes.value,
                typeParameters);
          default:
            throw new SyntaxError("Token not expected.", ((JavaToken) nextToken.token));

        }
      default:
        throw new SyntaxError("Token not expected.", ((JavaToken) nextToken.token));
    }
  }

  private boolean higherOrEqualPriority(BasicJavaToken left, BasicJavaToken right) {
    switch (left) {
      case OR_OR:
        return priorityOrOr(right);
      case AND_AND:
        return priorityAndAnd(right);
      case OR:
        return priorityOr(right);
      case XOR:
        return priorityXor(right);
      case AND:
        return priorityAnd(right);
      case EQUAL_EQUAL:
      case NOT_EQUAL:
        return priorityEqual(right);
      case INSTANCEOF:
        return priorityInstanceOf(right);
      case LESS:
      case LESS_EQUAL:
      case GREATER:
      case GREATER_EQUAL:
        return priorityLessGreater(right);
      case SAR:
      case SLR:
      case SLL:
        return priorityShift(right);
      case PLUS:
      case MINUS:
        return priorityPlusMinus(right);
      case MULTIPLY:
      case DIV:
      case REMAINDER:
        return true;
      default:
        throw new SyntaxError("Invalid token.", (IGeneralToken) null);

    }
  }

  private boolean priorityOrOr(BasicJavaToken right) {
    if (right == BasicJavaToken.OR_OR) {
      return true;
    }
    return false;
  }

  private boolean priorityAndAnd(BasicJavaToken right) {
    switch (right) {
      case OR_OR:
      case AND_AND:
        return true;
      default:
        return false;
    }
  }

  private boolean priorityOr(BasicJavaToken right) {
    switch (right) {
      case OR_OR:
      case AND_AND:
      case OR:
        return true;
      default:
        return false;
    }
  }

  private boolean priorityXor(BasicJavaToken right) {
    switch (right) {
      case OR_OR:
      case AND_AND:
      case OR:
      case XOR:
        return true;
      default:
        return false;
    }
  }

  private boolean priorityAnd(BasicJavaToken right) {
    switch (right) {
      case OR_OR:
      case AND_AND:
      case OR:
      case XOR:
      case AND:
        return true;
      default:
        return false;
    }
  }

  private boolean priorityEqual(BasicJavaToken right) {
    switch (right) {
      case OR_OR:
      case AND_AND:
      case OR:
      case XOR:
      case AND:
      case EQUAL_EQUAL:
      case NOT_EQUAL:
        return true;
      default:
        return false;
    }
  }

  private boolean priorityInstanceOf(BasicJavaToken right) {
    switch (right) {
      case OR_OR:
      case AND_AND:
      case OR:
      case XOR:
      case AND:
      case EQUAL_EQUAL:
      case NOT_EQUAL:
      case INSTANCEOF:
        return true;
      default:
        return false;
    }
  }

  private boolean priorityLessGreater(BasicJavaToken right) {
    switch (right) {
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
        return true;
      default:
        return false;
    }
  }

  private boolean priorityShift(BasicJavaToken right) {
    switch (right) {
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
        return true;
      default:
        return false;
    }
  }

  private boolean priorityPlusMinus(BasicJavaToken right) {
    switch (right) {
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
        return true;
      default:
        return false;
    }
  }

  /**
   * Identifier.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<BastNameIdent> identifier(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.enumNotExpected(nextToken);

    if (TokenChecker.isIdentifier(nextToken)) {
      currentTokenAndHistory = nextToken;
      TokenAndHistory[] tokens = { nextToken, null };
      return new ParseResult<BastNameIdent>(
          new BastNameIdent(tokens, String.valueOf(((JavaToken) nextToken.token).data)),
          currentTokenAndHistory);
    }
    return new ParseResult<BastNameIdent>(null, null);
  }

  private ParseResult<AbstractBastExpr> identifierSuffix(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // IdentifierSuffix:
    // [ ( ] {[]} . class | Expression ])
    // Arguments
    // . ( class | ExplicitGenericInvocation | this | super Arguments | new
    // [NonWildcardTypeArguments] InnerCreator )
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<AbstractBastExpr> name = qualifiedName(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = name.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<BastType> typeRes = new ParseResult<BastType>(null, null);
    TokenAndHistory nextNextToken = lexer.nextToken(data, nextToken);
    switch (((JavaToken) nextToken.token).type) {
      case LBRACKET: {
        TokenAndHistory nextnextToken = nextNextToken;
        if (TokenChecker.isRightBracket(nextnextToken)) {
          currentTokenAndHistory = inputTokenAndHistory;
          typeRes = type(lexer, data, currentTokenAndHistory);
          currentTokenAndHistory = typeRes.currentTokenAndHistory;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
          if (TokenChecker.isPoint(nextToken)) {
            currentTokenAndHistory = nextToken;
            TokenAndHistory[] tokenstmp = new TokenAndHistory[2];
            tokenstmp[0] = currentTokenAndHistory;
            currentTokenAndHistory = currentTokenAndHistory.setFlushed();
            nextToken = lexer.nextToken(data, currentTokenAndHistory);
            TokenChecker.expectClass(nextToken);

            currentTokenAndHistory = nextToken;
            TokenAndHistory[] tokens2 = { currentTokenAndHistory };
            BastClassConst classNode = new BastClassConst(tokens2);
            assert (typeRes.value != null);
            return new ParseResult<AbstractBastExpr>(
                new BastAccess(tokenstmp, typeRes.value, classNode), currentTokenAndHistory);

          } else {
            return new ParseResult<AbstractBastExpr>(null, null);
          }
        } else {
          return name;
        }
      }
      case LPAREN: {
        return parseLeftParenthesisSuffix(lexer, data, name, nextToken);
      }
      case POINT: {
        return parsePointInSuffix(lexer, data, name, nextToken, nextNextToken);
      }
      case SEMICOLON:
        return name;
      case EQUAL:
      case LESS:

        return name;
      case RPAREN:
        return name;
      default:
        return name;
    }
  }

  private ParseResult<AbstractBastExpr> parseLeftParenthesisSuffix(final JavaLexer lexer,
      final FileData data, ParseResult<AbstractBastExpr> name, TokenAndHistory nextToken) {
    TokenAndHistory currentTokenAndHistory;
    TokenAndHistory[] tokens;
    tokens = new TokenAndHistory[3];
    currentTokenAndHistory = nextToken;
    tokens[0] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    ParseResult<LinkedList<AbstractBastExpr>> arguments =
        arguments(lexer, data, currentTokenAndHistory);
    if (arguments.currentTokenAndHistory == null) {
      return new ParseResult<AbstractBastExpr>(null, null);
    }
    tokens[1] = new TokenAndHistory(new ListToken(arguments.additionalTokens));
    currentTokenAndHistory = arguments.currentTokenAndHistory;
    tokens[2] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    return new ParseResult<AbstractBastExpr>(new BastCall(tokens, name.value, arguments.value),
        currentTokenAndHistory);
  }

  private ParseResult<AbstractBastExpr> parsePointInSuffix(final JavaLexer lexer,
      final FileData data, ParseResult<AbstractBastExpr> name, TokenAndHistory nextToken,
      TokenAndHistory nextNextToken) {
    TokenAndHistory currentTokenAndHistory;
    ParseResult<LinkedList<AbstractBastExpr>> arguments;
    AbstractBastExpr expr;
    TokenAndHistory[] tokens;
    currentTokenAndHistory = nextToken;
    TokenAndHistory[] tokenstmp = new TokenAndHistory[1];
    tokenstmp[0] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    AbstractBastExpr member = null;
    switch (((JavaToken) nextToken.token).type) {
      case CLASS: {
        currentTokenAndHistory = nextToken;
        tokens = new TokenAndHistory[1];
        tokens[0] = currentTokenAndHistory;
        member = new BastClassConst(tokens);
        assert (name.value != null);
        return new ParseResult<AbstractBastExpr>(new BastAccess(tokenstmp, name.value, member),
            currentTokenAndHistory);
      }
      case LESS:
        return explicitGenericInvocation(lexer, data, currentTokenAndHistory, name.value,
            tokenstmp);
      case THIS: {
        currentTokenAndHistory = nextToken;
        tokens = new TokenAndHistory[1];
        tokens[0] = currentTokenAndHistory;
        member = new BastThis(tokens);
        assert (name.value != null);
        return new ParseResult<AbstractBastExpr>(new BastAccess(tokenstmp, name.value, member),
            currentTokenAndHistory);
      }
      case SUPER: {
        if (TokenChecker.isNotLeftParenthesis(nextNextToken)) {
          return name;
        }
        currentTokenAndHistory = nextToken;
        tokens = new TokenAndHistory[2];
        tokens[0] = currentTokenAndHistory;
        member = new BastSuper(tokens);
        assert (name.value != null);
        expr = new BastAccess(tokenstmp, name.value, member);
        TokenAndHistory[] tokensTmp = new TokenAndHistory[3];
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        currentTokenAndHistory = nextToken;
        tokensTmp[0] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();

        arguments = arguments(lexer, data, currentTokenAndHistory);
        tokensTmp[1] = new TokenAndHistory(new ListToken(arguments.additionalTokens));

        currentTokenAndHistory = arguments.currentTokenAndHistory;
        return new ParseResult<AbstractBastExpr>(new BastCall(tokensTmp, expr, arguments.value),
            currentTokenAndHistory);
      }
      case NEW:
        return innerCreator(lexer, data, currentTokenAndHistory, name.value, tokenstmp);
      default:
        throw new SyntaxError("Token not expected.", ((JavaToken) nextToken.token));
    }
  }



  /**
   * If statement.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<AbstractBastStatement> ifStatement(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // if ParExpression Statement [else Statement]
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokens = new TokenAndHistory[4];
    TokenChecker.expectIf(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[0] = currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectLeftParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[1] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    ParseResult<AbstractBastExpr> condition =
        expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = condition.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectRightParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[2] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    ParseResult<AbstractBastStatement> ifPart = statement(lexer, data, currentTokenAndHistory);
    ParseResult<AbstractBastStatement> elsePart =
        new ParseResult<AbstractBastStatement>(null, null);
    assert (ifPart.value != null);
    currentTokenAndHistory = ifPart.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isElse(nextToken)) {
      currentTokenAndHistory = nextToken;
      elsePart = statement(lexer, data, currentTokenAndHistory);
      tokens[3] = currentTokenAndHistory;
      currentTokenAndHistory = elsePart.currentTokenAndHistory;
    }
    BastIf ifStmt = new BastIf(tokens, condition.value, ifPart.value, elsePart.value);
    return new ParseResult<AbstractBastStatement>(ifStmt, currentTokenAndHistory);
  }



  private ParseResult<BastImportDeclaration> importDeclaration(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // import [ static] Identifier { . Identifier } [ . * ] ;
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory[] tokens = new TokenAndHistory[3];
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectImport(nextToken);

    currentTokenAndHistory = nextToken;
    tokens[0] = nextToken;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    boolean isStatic = false;
    if (TokenChecker.isStatic(nextToken)) {
      isStatic = true;
      currentTokenAndHistory = nextToken;
      tokens[1] = currentTokenAndHistory;
    }
    final ParseResult<AbstractBastExpr> name = qualifiedName(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = name.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    boolean isPackage = false;
    if (TokenChecker.isPoint(nextToken)) {
      currentTokenAndHistory = nextToken;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      TokenChecker.expectMultiply(nextToken);
      currentTokenAndHistory = nextToken;
      tokens[2] = currentTokenAndHistory;
      isPackage = true;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);

    }
    TokenChecker.expectSemicolon(nextToken);
    BastImportDeclaration importDecl =
        new BastImportDeclaration(tokens, name.value, isStatic, isPackage);
    currentTokenAndHistory = nextToken;
    importDecl.info.tokensAfter.add(currentTokenAndHistory);
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    return new ParseResult<BastImportDeclaration>(importDecl, currentTokenAndHistory);
  }



  private ParseResult<LinkedList<BastImportDeclaration>> importDeclarationList(
      final JavaLexer lexer, final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectImport(nextToken);
    LinkedList<BastImportDeclaration> list = null;
    ParseResult<BastImportDeclaration> importDecl =
        importDeclaration(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = importDecl.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    list = add(list, importDecl.value);
    while (TokenChecker.isImport(nextToken)) {
      importDecl = importDeclaration(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = importDecl.currentTokenAndHistory;
      list = add(list, importDecl.value);
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    return new ParseResult<LinkedList<BastImportDeclaration>>(list, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastExpr> innerCreator(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory, AbstractBastExpr name,
      TokenAndHistory[] pointToken) {

    // new [NonWildcardTypeArguments] InnerCreator
    // InnerCreator:
    // Identifier [NonWildcardTypeArgumentsOrDiamond] ClassCreatorRest
    // ClassCreatorRest:
    // Arguments [ClassBody]

    ParseResult<LinkedList<AbstractBastInternalDecl>> declarations =
        new ParseResult<LinkedList<AbstractBastInternalDecl>>(null, null);
    ParseResult<LinkedList<BastType>> typeArguments =
        new ParseResult<LinkedList<BastType>>(null, null);
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectNew(nextToken);
    currentTokenAndHistory = nextToken;
    final TokenAndHistory[] tokens = { currentTokenAndHistory, null, null, null, null, null };
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isLess(nextToken)) {

      typeArguments = typeArgumentAsTypeList(lexer, data, currentTokenAndHistory, false);
      currentTokenAndHistory = typeArguments.currentTokenAndHistory;
    }

    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);

    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<LinkedList<BastTypeArgument>> nonWildcardtypeArguments =
        new ParseResult<LinkedList<BastTypeArgument>>(null, null);
    if (TokenChecker.isLess(nextToken)) {
      nonWildcardtypeArguments = typeArgumentList(lexer, data, currentTokenAndHistory, false, true);
      currentTokenAndHistory = nonWildcardtypeArguments.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    TokenChecker.expectLeftParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[1] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    ParseResult<LinkedList<AbstractBastExpr>> parameters =
        arguments(lexer, data, currentTokenAndHistory);
    tokens[2] = new TokenAndHistory(new ListToken(parameters.additionalTokens));
    currentTokenAndHistory = parameters.currentTokenAndHistory;
    tokens[3] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isLeftBrace(nextToken)) {
      currentTokenAndHistory = nextToken;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      tokens[4] = currentTokenAndHistory;
      declarations = classBody(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = declarations.currentTokenAndHistory;
      tokens[5] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    }
    BastClassType type =
        new BastClassType(null, nameRes.value, nonWildcardtypeArguments.value, null);
    BastNew newNode =
        new BastNew(tokens, typeArguments.value, type, parameters.value, declarations.value);
    if (name != null) {
      assert (name != null);
      BastAccess access = new BastAccess(pointToken, name, newNode);
      return new ParseResult<AbstractBastExpr>(access, currentTokenAndHistory);
    }
    return new ParseResult<AbstractBastExpr>(newNode, currentTokenAndHistory);
  }

  private ParseResult<LinkedList<AbstractBastInternalDecl>> interfaceBody(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // { { InterfaceBodyDeclaration } }
    //
    // InterfaceBodyDeclaration:
    // ;
    // {Modifier} InterfaceMemberDecl
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<LinkedList<AbstractBastInternalDecl>> list =
        new ParseResult<LinkedList<AbstractBastInternalDecl>>(null, null);
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    list = interfaceBodyDeclarationList(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = list.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectRightBrace(nextToken);
    currentTokenAndHistory = nextToken;
    return new ParseResult<LinkedList<AbstractBastInternalDecl>>(list.value,
        currentTokenAndHistory);
  }

  private ParseResult<AbstractBastInternalDecl> interfaceBodyDeclaration(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // InterfaceBodyDeclaration:
    // ;
    // {Modifier} InterfaceMemberDecl
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    switch (((JavaToken) nextToken.token).type) {
      case SEMICOLON:
        BastEmptyDeclaration emptyDecl = new BastEmptyDeclaration(null);
        currentTokenAndHistory = nextToken;
        emptyDecl.info.tokensAfter.add(currentTokenAndHistory);
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        return new ParseResult<AbstractBastInternalDecl>(emptyDecl, currentTokenAndHistory);
      default:
        ParseResult<LinkedList<AbstractBastSpecifier>> modifiers =
            modifierList(lexer, data, currentTokenAndHistory, false);
        currentTokenAndHistory = modifiers.currentTokenAndHistory;
        ParseResult<AbstractBastInternalDecl> member =
            interfaceMemberDecl(lexer, data, currentTokenAndHistory);
        if (modifiers.value != null && !modifiers.value.isEmpty()) {
          member.value.setModifiers(modifiers.value);
        }
        return member;
    }
  }

  private ParseResult<LinkedList<AbstractBastInternalDecl>> interfaceBodyDeclarationList(
      final JavaLexer lexer, final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    LinkedList<AbstractBastInternalDecl> list = null;
    ParseResult<AbstractBastInternalDecl> interfaceBody =
        interfaceBodyDeclaration(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = interfaceBody.currentTokenAndHistory;
    while (interfaceBody.value != null) {
      list = add(list, interfaceBody.value);
      interfaceBody = interfaceBodyDeclaration(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = interfaceBody.currentTokenAndHistory;
    }
    return new ParseResult<LinkedList<AbstractBastInternalDecl>>(list, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastInternalDecl> interfaceDeclaration(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // InterfaceDeclaration:
    // NormalInterfaceDeclaration
    // AnnotationTypeDeclaration
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    switch (((JavaToken) nextToken.token).type) {
      case INTERFACE:
        return normalInterfaceDeclaration(lexer, data, currentTokenAndHistory);

      case AT:
        return annotationTypeDeclaration(lexer, data, currentTokenAndHistory);

      default:
        throw new SyntaxError("INTERFACE expected.", ((JavaToken) nextToken.token));

    }
  }

  private ParseResult<AbstractBastInternalDecl> interfaceGenericMethodDecl(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // InterfaceGenericMethodDecl:
    // TypeParameters (Type | void) Identifier InterfaceMethodDeclaratorRest
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<LinkedList<BastTypeParameter>> typeParameters =
        typeParameters(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = typeParameters.currentTokenAndHistory;
    switch (((JavaToken) nextToken.token).type) {
      case VOID:
        return interfaceMethodDeclaratorRest(lexer, data, currentTokenAndHistory, false,
            typeParameters.value);
      default:
        return interfaceMethodDeclaratorRest(lexer, data, currentTokenAndHistory, false,
            typeParameters.value);
    }
  }

  private ParseResult<AbstractBastInternalDecl> interfaceMemberDecl(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // InterfaceMemberDecl:
    // InterfaceMethodOrFieldDecl
    // void Identifier VoidInterfaceMethodDeclaratorRest
    // InterfaceGenericMethodDecl
    // ClassDeclaration
    // InterfaceDeclaration
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    switch (((JavaToken) nextToken.token).type) {
      case LESS:
        return interfaceGenericMethodDecl(lexer, data, currentTokenAndHistory);
      case VOID:
        return methodDeclaratorRest(lexer, data, currentTokenAndHistory, false, null);
      case AT:
      case INTERFACE:
        return interfaceDeclaration(lexer, data, currentTokenAndHistory);
      case CLASS:
      case ENUM:
        return classDeclaration(lexer, data, currentTokenAndHistory);
      case BYTE:
      case SHORT:
      case CHAR:
      case INT:
      case LONG:
      case FLOAT:
      case DOUBLE:
      case BOOLEAN:
        return interfaceMethodOrFieldDecl(lexer, data, currentTokenAndHistory);
      case IDENTIFIER:
        TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
        switch (((JavaToken) nextnextToken.token).type) {
          case POINT:
          case LESS:
            return interfaceMethodOrFieldDecl(lexer, data, currentTokenAndHistory);
          case IDENTIFIER:
            return interfaceMethodOrFieldDecl(lexer, data, currentTokenAndHistory);
          case LBRACKET:
            return interfaceMethodOrFieldDecl(lexer, data, currentTokenAndHistory);
          default:
            throw new SyntaxError("Identifier expected.", ((JavaToken) nextToken.token));
        }
      case RBRACE:
        return new ParseResult<AbstractBastInternalDecl>(null, currentTokenAndHistory);
      default:
        throw new SyntaxError("Member declaration expected.", ((JavaToken) nextToken.token));
    }

  }

  private ParseResult<AbstractBastInternalDecl> interfaceMethodDeclaratorRest(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory, boolean withBrackets,
      LinkedList<BastTypeParameter> typeParameters) {
    // InterfaceMethodDeclaratorRest:
    // FormalParameters {[]} [throws QualifiedIdentifierList] ;
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<BastType> typeRes = type(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = typeRes.currentTokenAndHistory;
    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    ParseResult<LinkedList<AbstractBastExpr>> exceptions =
        new ParseResult<LinkedList<AbstractBastExpr>>(null, null);
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    ParseResult<BastParameterList> paramList =
        formalParameters(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = paramList.currentTokenAndHistory;

    BastFunctionParameterDeclarator decl =
        new BastFunctionParameterDeclarator(null, paramList.value, null);
    BastIdentDeclarator identDecl = null;
    int count = 0;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (withBrackets) {
      if (TokenChecker.isLeftBracket(nextToken)) {
        TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
        while (TokenChecker.isLeftBracket(nextToken)
            && TokenChecker.isRightBracket(nextnextToken)) {
          currentTokenAndHistory = nextnextToken;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
          nextnextToken = lexer.nextToken(data, nextToken);
          count++;
        }
      }
    }
    if (count != 0) {
      TokenAndHistory[] tokenstmp = new TokenAndHistory[1];
      tokenstmp[0] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      identDecl = new BastIdentDeclarator(tokens, nameRes.value, null,
          new BastArrayDeclarator(tokenstmp, null, decl, count));
    } else {
      identDecl = new BastIdentDeclarator(tokens, nameRes.value, null, decl);
    }
    if (TokenChecker.isThrows(nextToken)) {
      currentTokenAndHistory = nextToken;
      tokens[0] = currentTokenAndHistory;
      exceptions = qualifiedIdentifierList(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = exceptions.currentTokenAndHistory;
    }
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    BastBlock methodBody = null;
    TokenChecker.expectSemicolon(nextToken);
    currentTokenAndHistory = nextToken;
    BastFunction function = new BastFunction(tokens, null, identDecl, null, methodBody,
        typeParameters, typeRes.value, exceptions.value);
    function.info.tokensAfter.add(currentTokenAndHistory);
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();

    return new ParseResult<AbstractBastInternalDecl>(function, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastInternalDecl> interfaceMethodOrFieldDecl(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // InterfaceMethodOrFieldDecl:
    // Type Identifier InterfaceMethodOrFieldRest
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    return interfaceMethodOrFieldRest(lexer, data, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastInternalDecl> interfaceMethodOrFieldRest(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // Changed to fit Java 1.7 20120301
    // InterfaceMethodOrFieldRest:
    // ConstantDeclaratorsRest ;
    // InterfaceMethodDeclaratorRest
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;

    ParseResult<BastType> typeRes = type(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = typeRes.currentTokenAndHistory;
    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = nameRes.currentTokenAndHistory;

    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    switch (((JavaToken) nextToken.token).type) {
      case LPAREN:
        return interfaceMethodDeclaratorRest(lexer, data, inputTokenAndHistory, true, null);
      case EQUAL:
        ParseResult<AbstractBastInternalDecl> tmp =
            fieldDeclaratorRest(lexer, data, inputTokenAndHistory);
        currentTokenAndHistory = tmp.currentTokenAndHistory;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        TokenChecker.expectSemicolon(nextToken);
        currentTokenAndHistory = nextToken;
        tmp.value.info.tokensAfter.add(currentTokenAndHistory);
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        return new ParseResult<AbstractBastInternalDecl>(tmp.value, currentTokenAndHistory);
      default:
        throw new SyntaxError("'(' or '=' expected.", ((JavaToken) nextToken.token));
    }
  }



  protected ParseResult<AbstractBastStatement> labeledStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);

    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    assert (nameRes.value != null);
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);

    TokenChecker.expectColon(nextToken);
    currentTokenAndHistory = nextToken;
    TokenAndHistory[] tokens = { currentTokenAndHistory };
    ParseResult<AbstractBastStatement> stmt = statement(lexer, data, currentTokenAndHistory);
    BastLabelStmt labelStmt = new BastLabelStmt(tokens, nameRes.value, stmt.value);
    return new ParseResult<AbstractBastStatement>(labelStmt, stmt.currentTokenAndHistory);

  }

  /**
   * Literal.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<AbstractBastConstant> literal(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    switch (((JavaToken) nextToken.token).type) {
      case STRING_LITERAL:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastConstant>(
            new BastStringConst(tokens, ((JavaToken) nextToken.token).data.toString()),
            currentTokenAndHistory);
      case INTEGER_LITERAL:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastConstant>(
            new BastIntConst(tokens, ((JavaToken) nextToken.token).data.toString()),
            currentTokenAndHistory);
      case CHAR_LITERAL:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastConstant>(
            new BastCharConst(tokens, ((JavaToken) nextToken.token).data.toString()),
            currentTokenAndHistory);
      case REAL_LITERAL:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastConstant>(
            new BastRealConst(tokens, ((JavaToken) nextToken.token).data.toString()),
            currentTokenAndHistory);
      case TRUE:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastConstant>(new BastBoolConst(tokens, true),
            currentTokenAndHistory);
      case FALSE:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastConstant>(new BastBoolConst(tokens, false),
            currentTokenAndHistory);
      case NULL:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastConstant>(new BastNullConst(tokens),
            currentTokenAndHistory);
      default:
        throw new SyntaxError("LITERAL expected.", ((JavaToken) nextToken.token));
    }
  }

  /**
   * Local variable declaration statement.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<BastDeclaration> localVariableDeclarationStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    return localVariableDeclarationStatement(lexer, data, inputTokenAndHistory, true);
  }

  protected ParseResult<BastDeclaration> localVariableDeclarationStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory, boolean semicolon) {
    // LocalVariableDeclarationStatement:
    // { VariableModifier } Type VariableDeclarators ;
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<LinkedList<AbstractBastSpecifier>> modifiers =
        modifierList(lexer, data, currentTokenAndHistory, false);
    // = ModifierList(currentTokenAndHistory, false);
    currentTokenAndHistory = modifiers.currentTokenAndHistory;
    ParseResult<BastType> typeRes = type(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = typeRes.currentTokenAndHistory;
    if (typeRes.value == null) {
      return new ParseResult<BastDeclaration>(null, null);
    }
    LinkedList<AbstractBastSpecifier> declSpecList = new LinkedList<AbstractBastSpecifier>();

    BastTypeSpecifier specifier = new BastTypeSpecifier(null, typeRes.value);
    declSpecList.add(specifier);
    // = BastIdentDeclarator
    ParseResult<LinkedList<AbstractBastDeclarator>> initDeclList =
        variableDeclarators(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = initDeclList.currentTokenAndHistory;
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    tokens[0] = new TokenAndHistory(new ListToken(initDeclList.additionalTokens));
    if (initDeclList.value != null && initDeclList.value.size() != 0) {
      BastDeclaration decl = new BastDeclaration(tokens, declSpecList, initDeclList.value);
      decl.setModifiers(modifiers.value);
      TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
      if (TokenChecker.isSemicolon(nextToken) && semicolon) {
        currentTokenAndHistory = nextToken;
        decl.info.tokensAfter.add(currentTokenAndHistory);
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        return new ParseResult<BastDeclaration>(decl, currentTokenAndHistory);
      } else if (TokenChecker.isEof(nextToken) || !semicolon) {
        return new ParseResult<BastDeclaration>(decl, currentTokenAndHistory);
      }

    }
    return new ParseResult<BastDeclaration>(null, null);

  }

  /**
   * Member decl.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<AbstractBastInternalDecl> memberDecl(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // MemberDecl:
    // GenericMethodOrConstructorDecl
    // MethodOrFieldDecl
    // void Identifier VoidMethodDeclaratorRest
    // Identifier ConstructorDeclaratorRest
    // InterfaceDeclaration
    // ClassDeclaration
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<BastNameIdent> nameRes = new ParseResult<BastNameIdent>(null, null);
    switch (((JavaToken) nextToken.token).type) {
      case LESS:
        return genericMethodOrConstructorDecl(lexer, data, currentTokenAndHistory);
      case VOID:
        return methodDeclaratorRest(lexer, data, currentTokenAndHistory, false, null);
      case AT:
        nextToken = lexer.nextToken(data, nextToken);
        if (TokenChecker.isInterface(nextToken)) {
          return interfaceDeclaration(lexer, data, currentTokenAndHistory);
        }
        return methodOrFieldDecl(lexer, data, currentTokenAndHistory);
      case INTERFACE:
        return interfaceDeclaration(lexer, data, currentTokenAndHistory);
      case CLASS:
      case ENUM:
        return classDeclaration(lexer, data, currentTokenAndHistory);
      case BYTE:
      case SHORT:
      case CHAR:
      case INT:
      case LONG:
      case FLOAT:
      case DOUBLE:
      case BOOLEAN:

        return methodOrFieldDecl(lexer, data, currentTokenAndHistory);
      case IDENTIFIER:
        TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
        switch (((JavaToken) nextnextToken.token).type) {
          case POINT:
          case LESS:
            return methodOrFieldDecl(lexer, data, currentTokenAndHistory);
          case IDENTIFIER:
            return methodOrFieldDecl(lexer, data, currentTokenAndHistory);
          case LBRACKET:
            return methodOrFieldDecl(lexer, data, currentTokenAndHistory);
          case LPAREN:
            nameRes = identifier(lexer, data, currentTokenAndHistory);

            currentTokenAndHistory = nameRes.currentTokenAndHistory;
            assert (nameRes.value != null);
            return constructorDeclaratorRest(lexer, data, currentTokenAndHistory, nameRes.value,
                null);
          case RBRACE:
            throw new SyntaxError("Unfinished statement.", ((JavaToken) nextnextToken.token));
          default:
            throw new SyntaxError("Keyword not allowed here.", ((JavaToken) nextToken.token));
        }
      case IF:
      case EOF:
        throw new SyntaxError("Unfinished statement.", ((JavaToken) nextToken.token));
      case RBRACE:
        return new ParseResult<AbstractBastInternalDecl>(null, currentTokenAndHistory);
      default:
        throw new SyntaxError("Keyword not allowed here.", ((JavaToken) nextToken.token));
    }
  }



  private AbstractBastExpr mergeStack(AbstractBastExpr left, AbstractBastExpr right,
      final TokenAndHistory inputTokenAndHistory, final TokenAndHistory inputTokenAndHistory2,
      final TokenAndHistory inputTokenAndHistory3, ArrayDeque<TokenAndHistory> tokenStack) {
    TokenAndHistory[] tokens = null;
    if (inputTokenAndHistory2 == null) {
      tokens = new TokenAndHistory[1];
      tokens[0] = inputTokenAndHistory;
    } else if (inputTokenAndHistory3 == null) {
      tokens = new TokenAndHistory[2];
      tokens[0] = inputTokenAndHistory2;
      tokens[1] = inputTokenAndHistory;
    } else {
      tokens = new TokenAndHistory[3];
      tokens[0] = inputTokenAndHistory3;
      tokens[1] = inputTokenAndHistory2;
      tokens[2] = inputTokenAndHistory;
    }
    switch (((JavaToken) inputTokenAndHistory.token).type) {
      case OR_OR:
        return new BastCondOr(tokens, left, right);
      case AND_AND:
        return new BastCondAnd(tokens, left, right);
      case OR:
        return new BastOr(tokens, left, right);
      case XOR:
        return new BastXor(tokens, left, right);
      case AND:
        return new BastAnd(tokens, left, right);
      case EQUAL_EQUAL:
        return new BastCmp(tokens, left, right, BastCmp.EQUAL);
      case NOT_EQUAL:
        return new BastCmp(tokens, left, right, BastCmp.NOT_EQUAL);
      case INSTANCEOF:
        return new BastInstanceOf(tokens, left, (BastType) right);
      case LESS:
        if (inputTokenAndHistory2 == null) {
          return new BastCmp(tokens, left, right, BastCmp.LESS);
        } else {
          return new BastShift(tokens, left, right, BastShift.TYPE_SLL);
        }
      case LESS_EQUAL:
        return new BastCmp(tokens, left, right, BastCmp.LESS_EQUAL);
      case GREATER:
        if (inputTokenAndHistory3 != null) {
          return new BastShift(tokens, left, right, BastShift.TYPE_SAR);
        } else if (inputTokenAndHistory2 != null) {
          return new BastShift(tokens, left, right, BastShift.TYPE_SLR);
        } else {
          return new BastCmp(tokens, left, right, BastCmp.GREATER);
        }
      case GREATER_EQUAL:
        return new BastCmp(tokens, left, right, BastCmp.GREATER_EQUAL);
      case SAR:
        return new BastShift(tokens, left, right, BastShift.TYPE_SAR);
      case SLR:
        return new BastShift(tokens, left, right, BastShift.TYPE_SLR);
      case PLUS:
        return new BastAdditiveExpr(tokens, left, right, BastAdditiveExpr.TYPE_ADD);
      case MINUS:
        return new BastAdditiveExpr(tokens, left, right, BastAdditiveExpr.TYPE_SUB);
      case MULTIPLY:
        return new BastMultiExpr(tokens, left, right, BastMultiExpr.Types.TYPE_MUL);
      case DIV:
        return new BastMultiExpr(tokens, left, right, BastMultiExpr.Types.TYPE_DIV);
      case REMAINDER:
        return new BastMultiExpr(tokens, left, right, BastMultiExpr.Types.TYPE_MOD);
      default:
        throw new SyntaxError("Invalid token.", ((JavaToken) inputTokenAndHistory.token));
    }
  }

  private ParseResult<AbstractBastInternalDecl> methodDeclaratorRest(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory, boolean withBrackets,
      LinkedList<BastTypeParameter> typeParameters) {
    // MethodDeclaratorRest:
    // FormalParameters {[]} [throws QualifiedIdentifierList] ( MethodBody |
    // ;
    // )

    // ConstructorDeclaratorRest:
    // FormalParameters [throws QualifiedIdentifierList] MethodBody
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<BastType> typeRes = type(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = typeRes.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.assertNotExpected(nextToken);
    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    ParseResult<LinkedList<AbstractBastExpr>> exceptions =
        new ParseResult<LinkedList<AbstractBastExpr>>(null, null);
    ParseResult<BastParameterList> paramList =
        formalParameters(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = paramList.currentTokenAndHistory;
    BastFunctionParameterDeclarator decl =
        new BastFunctionParameterDeclarator(null, paramList.value, null);
    BastIdentDeclarator identDecl = null;
    int count = 0;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    if (withBrackets) {
      if (TokenChecker.isLeftBracket(nextToken)) {
        TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
        while (TokenChecker.isLeftBracket(nextToken)
            && TokenChecker.isRightBracket(nextnextToken)) {
          currentTokenAndHistory = nextnextToken;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
          nextnextToken = lexer.nextToken(data, nextToken);
          count++;
        }
      }
    }
    if (count != 0) {
      TokenAndHistory[] tokenstmp = new TokenAndHistory[1];
      tokenstmp[0] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      identDecl = new BastIdentDeclarator(tokens, nameRes.value, null,
          new BastArrayDeclarator(tokenstmp, null, decl, count));
    } else {
      identDecl = new BastIdentDeclarator(tokens, nameRes.value, null, decl);
    }
    if (nameRes.value == null) {
      return new ParseResult<AbstractBastInternalDecl>(null, null);
    }
    if (TokenChecker.isThrows(nextToken)) {
      currentTokenAndHistory = nextToken;
      exceptions = qualifiedIdentifierList(lexer, data, currentTokenAndHistory);
      tokens[0] = currentTokenAndHistory;
      currentTokenAndHistory = exceptions.currentTokenAndHistory;

    }
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<BastBlock> methodBody = new ParseResult<BastBlock>(null, null);
    switch (((JavaToken) nextToken.token).type) {
      case LBRACE:
        methodBody = block(lexer, data, currentTokenAndHistory, false);
        currentTokenAndHistory = methodBody.currentTokenAndHistory;
        if (methodBody.currentTokenAndHistory == null) {
          return new ParseResult<AbstractBastInternalDecl>(null, null);
        }
        break;
      case SEMICOLON:
        currentTokenAndHistory = nextToken;
        break;
      default:
        throw new SyntaxError("Semicolon expected", nextToken.token);

    }
    if (currentTokenAndHistory == null) {
      return new ParseResult<AbstractBastInternalDecl>(null, null);

    }

    BastFunction function = new BastFunction(tokens, null, identDecl, null, methodBody.value,
        typeParameters, typeRes.value, exceptions.value);
    if (TokenChecker.isSemicolon(currentTokenAndHistory)) {
      function.info.tokensAfter.add(currentTokenAndHistory);
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    }
    return new ParseResult<AbstractBastInternalDecl>(function, currentTokenAndHistory);
  }

  /**
   * Method or field decl.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<AbstractBastInternalDecl> methodOrFieldDecl(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // MethodOrFieldDecl:
    // Type Identifier MethodOrFieldRest
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    return methodOrFieldRest(lexer, data, currentTokenAndHistory, null);
  }

  private ParseResult<AbstractBastInternalDecl> methodOrFieldRest(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory,
      LinkedList<BastTypeParameter> typeParameters) {
    // Changed to fit Java 1.7 20120301
    // MethodOrFieldRest:
    // FieldDeclaratorsRest ;
    // MethodDeclaratorRest
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<BastType> typeRes = type(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = typeRes.currentTokenAndHistory;
    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isLeftParenthesis(nextToken)) {
      return methodDeclaratorRest(lexer, data, inputTokenAndHistory, true, typeParameters);
    } else {
      ParseResult<AbstractBastInternalDecl> tmp =
          fieldDeclaratorRest(lexer, data, inputTokenAndHistory);
      currentTokenAndHistory = tmp.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      TokenChecker.expectSemicolon(nextToken);
      currentTokenAndHistory = nextToken;
      tmp.value.info.tokensAfter.add(currentTokenAndHistory);
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      return new ParseResult<AbstractBastInternalDecl>(tmp.value, currentTokenAndHistory);
    }
  }

  protected ParseResult<AbstractBastSpecifier> modifier(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory, boolean variableModifier) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    if (variableModifier) {
      switch (((JavaToken) nextToken.token).type) {
        case AT:
          nextToken = lexer.nextToken(data, nextToken);
          if (TokenChecker.isNotInterface(nextToken)) {
            ParseResult<BastAnnotation> result = annotation(lexer, data, currentTokenAndHistory);
            return new ParseResult<AbstractBastSpecifier>(result.value,
                result.currentTokenAndHistory);
          } else {
            return new ParseResult<AbstractBastSpecifier>(null, null);
          }
        case FINAL:
          currentTokenAndHistory = nextToken;
          tokens[0] = currentTokenAndHistory;
          return new ParseResult<AbstractBastSpecifier>(
              new BastTypeQualifier(tokens, BastTypeQualifier.TYPE_FINAL), currentTokenAndHistory);
        default:
          // Do nothing
      }
      return new ParseResult<AbstractBastSpecifier>(null, currentTokenAndHistory);
    }
    switch (((JavaToken) nextToken.token).type) {
      case AT:
        nextToken = lexer.nextToken(data, nextToken);
        if (TokenChecker.isNotInterface(nextToken)) {
          ParseResult<BastAnnotation> result = annotation(lexer, data, currentTokenAndHistory);
          return new ParseResult<AbstractBastSpecifier>(result.value,
              result.currentTokenAndHistory);
        } else {
          return new ParseResult<AbstractBastSpecifier>(null, inputTokenAndHistory);
        }
      case PUBLIC:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastSpecifier>(
            new BastTypeQualifier(tokens, BastTypeQualifier.TYPE_PUBLIC), currentTokenAndHistory);
      case PROTECTED:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastSpecifier>(
            new BastTypeQualifier(tokens, BastTypeQualifier.TYPE_PROTECTED),
            currentTokenAndHistory);
      case PRIVATE:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastSpecifier>(
            new BastTypeQualifier(tokens, BastTypeQualifier.TYPE_PRIVATE), currentTokenAndHistory);
      case STATIC:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastSpecifier>(
            new BastTypeQualifier(tokens, BastTypeQualifier.TYPE_STATIC), currentTokenAndHistory);
      case ABSTRACT:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastSpecifier>(
            new BastTypeQualifier(tokens, BastTypeQualifier.TYPE_ABSTRACT), currentTokenAndHistory);
      case FINAL:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastSpecifier>(
            new BastTypeQualifier(tokens, BastTypeQualifier.TYPE_FINAL), currentTokenAndHistory);
      case NATIVE:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastSpecifier>(
            new BastTypeQualifier(tokens, BastTypeQualifier.TYPE_NATIVE), currentTokenAndHistory);
      case SYNCHRONIZED:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastSpecifier>(
            new BastTypeQualifier(tokens, BastTypeQualifier.TYPE_SYNCHRONIZED),
            currentTokenAndHistory);
      case TRANSIENT:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastSpecifier>(
            new BastTypeQualifier(tokens, BastTypeQualifier.TYPE_TRANSIENT),
            currentTokenAndHistory);
      case VOLATILE:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastSpecifier>(
            new BastTypeQualifier(tokens, BastTypeQualifier.TYPE_VOLATILE), currentTokenAndHistory);
      case STRIcTFP:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<AbstractBastSpecifier>(
            new BastTypeQualifier(tokens, BastTypeQualifier.TYPE_STRICTFP), currentTokenAndHistory);
      default:
        // Do nothing
    }
    return new ParseResult<AbstractBastSpecifier>(null, currentTokenAndHistory);
  }

  /**
   * Modifier list.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @param variableModifier the variable modifier
   * @return the parses the result
   */
  public ParseResult<LinkedList<AbstractBastSpecifier>> modifierList(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory, boolean variableModifier) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    LinkedList<AbstractBastSpecifier> list = null;
    ParseResult<AbstractBastSpecifier> modifier =
        modifier(lexer, data, currentTokenAndHistory, variableModifier);
    currentTokenAndHistory = modifier.currentTokenAndHistory;
    while (modifier.value != null) {
      list = add(list, modifier.value);
      modifier = modifier(lexer, data, currentTokenAndHistory, variableModifier);
      currentTokenAndHistory = modifier.currentTokenAndHistory;
    }
    return new ParseResult<LinkedList<AbstractBastSpecifier>>(list, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastInternalDecl> normalClassDeclaration(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory, TokenAndHistory nextToken) {
    // NormalClassDeclaration:
    // class Identifier [TypeParameters] [extends Type] [implements
    // TypeList]
    // ClassBody
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<BastNameIdent> nameRes = new ParseResult<BastNameIdent>(null, null);

    ParseResult<LinkedList<BastTypeParameter>> typeParameters =
        new ParseResult<LinkedList<BastTypeParameter>>(null, null);
    ParseResult<BastType> extendedClass = new ParseResult<BastType>(null, null);
    ParseResult<LinkedList<BastType>> interfaces =
        new ParseResult<LinkedList<BastType>>(null, null);
    TokenAndHistory[] tokens = new TokenAndHistory[5];
    tokens[0] = nextToken;
    currentTokenAndHistory = nextToken;
    nameRes = identifier(lexer, data, currentTokenAndHistory);

    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    if (nameRes.value == null) {
      throw new SyntaxError("Identifier expected.", ((JavaToken) nextToken.token));
    }
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isLess(nextToken)) {
      typeParameters = typeParameters(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = typeParameters.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    if (TokenChecker.isExtends(nextToken)) {
      currentTokenAndHistory = nextToken;
      extendedClass = type(lexer, data, currentTokenAndHistory);
      tokens[1] = currentTokenAndHistory;
      currentTokenAndHistory = extendedClass.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    if (TokenChecker.isImplements(nextToken)) {
      currentTokenAndHistory = nextToken;
      interfaces = typeList(lexer, data, currentTokenAndHistory);
      tokens[2] = currentTokenAndHistory;
      currentTokenAndHistory = interfaces.currentTokenAndHistory;
    }
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    currentTokenAndHistory = nextToken;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    tokens[3] = currentTokenAndHistory;
    final ParseResult<LinkedList<AbstractBastInternalDecl>> classBody =
        classBody(lexer, data, currentTokenAndHistory);
    if (classBody.currentTokenAndHistory == null) {
      return new ParseResult<AbstractBastInternalDecl>(null, null);
    }
    currentTokenAndHistory = classBody.currentTokenAndHistory;
    if (!currentTokenAndHistory.flushed && TokenChecker.isRightBrace(currentTokenAndHistory)) {
      tokens[4] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    }
    return new ParseResult<AbstractBastInternalDecl>(new BastClassDecl(tokens, nameRes.value,
        typeParameters.value, extendedClass.value, interfaces.value, classBody.value),
        currentTokenAndHistory);
  }



  private ParseResult<AbstractBastInternalDecl> normalInterfaceDeclaration(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // interface Identifier [ TypeParameters] [extends TypeList]
    // InterfaceBody
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<BastNameIdent> nameRes = new ParseResult<BastNameIdent>(null, null);

    ParseResult<LinkedList<BastTypeParameter>> typeParameters =
        new ParseResult<LinkedList<BastTypeParameter>>(null, null);
    ParseResult<LinkedList<BastType>> interfaces =
        new ParseResult<LinkedList<BastType>>(null, null);
    TokenAndHistory[] tokens = new TokenAndHistory[4];
    tokens[0] = nextToken;
    currentTokenAndHistory = nextToken;
    nameRes = identifier(lexer, data, currentTokenAndHistory);

    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isLess(nextToken)) {
      typeParameters = typeParameters(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = typeParameters.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    if (TokenChecker.isExtends(nextToken)) {
      currentTokenAndHistory = nextToken;
      interfaces = typeList(lexer, data, currentTokenAndHistory);
      tokens[1] = currentTokenAndHistory;
      currentTokenAndHistory = interfaces.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    TokenChecker.expectLeftBrace(nextToken);
    currentTokenAndHistory = lexer.nextToken(data, currentTokenAndHistory);
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    tokens[2] = currentTokenAndHistory;
    ParseResult<LinkedList<AbstractBastInternalDecl>> interfaceBody =
        new ParseResult<LinkedList<AbstractBastInternalDecl>>(null, null);
    interfaceBody = interfaceBody(lexer, data, currentTokenAndHistory);
    if (interfaceBody == null) {
      return new ParseResult<AbstractBastInternalDecl>(null, null);
    }
    currentTokenAndHistory = interfaceBody.currentTokenAndHistory;
    tokens[3] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    return new ParseResult<AbstractBastInternalDecl>(new BastInterfaceDecl(tokens, nameRes.value,
        typeParameters.value, interfaces.value, interfaceBody.value), currentTokenAndHistory);
  }

  private ParseResult<BastPackage> packageMethod(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectPackage(nextToken);
    currentTokenAndHistory = nextToken;
    final TokenAndHistory[] tokens = { currentTokenAndHistory };
    ParseResult<AbstractBastExpr> name = qualifiedName(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = name.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectSemicolon(nextToken);
    BastPackage bastPackage = new BastPackage(tokens, name.value, null);
    currentTokenAndHistory = nextToken;
    bastPackage.info.tokensAfter.add(currentTokenAndHistory);
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    return new ParseResult<BastPackage>(bastPackage, currentTokenAndHistory);
  }

  /**
   * Parameter.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<BastParameter> parameter(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // FormalParameterDecls:
    // [final] [Annotations] Type FormalParameterDeclsRest]
    // FormalParameterDeclsRest:
    // VariableDeclaratorId [ , FormalParameterDecls]
    // ... VariableDeclaratorId
    // VariableDeclaratorId:
    // Identifier {[]}
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    BastTypeQualifier qualifier = null;
    ParseResult<BastAnnotation> annotation = new ParseResult<BastAnnotation>(null, null);

    ParseResult<BastNameIdent> nameRes = new ParseResult<BastNameIdent>(null, null);
    boolean isEllipsis = false;
    int count = 0;
    LinkedList<AbstractBastSpecifier> specifiers = new LinkedList<AbstractBastSpecifier>();
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    if (TokenChecker.isAt(nextToken)) {
      annotation = annotation(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = annotation.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      specifiers.add(annotation.value);
    }
    if (TokenChecker.isFinal(nextToken)) {
      tokens[0] = nextToken;
      qualifier = new BastTypeQualifier(tokens, BastTypeQualifier.TYPE_FINAL);
      currentTokenAndHistory = nextToken;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      specifiers.add(qualifier);
      tokens = new TokenAndHistory[1];
    }
    if (TokenChecker.isAt(nextToken)) {
      annotation = annotation(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = annotation.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      specifiers.add(annotation.value);
    }
    final ParseResult<BastType> typeRes = type(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = typeRes.currentTokenAndHistory;
    BastTypeSpecifier typeSpecifier = new BastTypeSpecifier(null, typeRes.value);
    specifiers.add(typeSpecifier);
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    switch (((JavaToken) nextToken.token).type) {
      case IDENTIFIER:
        nameRes = identifier(lexer, data, currentTokenAndHistory);
        currentTokenAndHistory = nameRes.currentTokenAndHistory;
        assert (nameRes.value != null);
        break;
      case ELLIPSIS:
        tokens[0] = nextToken;
        isEllipsis = true;
        currentTokenAndHistory = nextToken;
        nameRes = identifier(lexer, data, currentTokenAndHistory);
        currentTokenAndHistory = nameRes.currentTokenAndHistory;
        assert (nameRes.value != null);
        break;
      default:
        throw new SyntaxError("Identifier or '...' expected.", ((JavaToken) nextToken.token));

    }
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
    while (TokenChecker.isLeftBracket(nextToken) && TokenChecker.isRightBracket(nextnextToken)) {
      currentTokenAndHistory = nextnextToken;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      nextnextToken = lexer.nextToken(data, nextToken);
      count++;
    }

    AbstractBastDeclarator declarator = null;
    if (count != 0) {
      TokenAndHistory[] tokenstmp = new TokenAndHistory[1];
      tokenstmp[0] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      declarator = new BastIdentDeclarator(null, nameRes.value, null,
          new BastArrayDeclarator(tokenstmp, null, null, count));
    } else {
      declarator = new BastIdentDeclarator(null, nameRes.value, null, null);
    }
    if (nameRes.value == null) {
      return new ParseResult<BastParameter>(null, null);
    }
    BastParameter param = new BastParameter(tokens, specifiers, declarator);
    param.isEllipsis = isEllipsis;
    return new ParseResult<BastParameter>(param, currentTokenAndHistory);
  }



  private ParseResult<AbstractBastExpr> parExpression(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory, boolean lookForLParen) {
    // ParExpression:
    // ( Expression )
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (lookForLParen) {
      TokenChecker.expectLeftParenthesis(nextToken);
      currentTokenAndHistory = nextToken;
    }
    ParseResult<AbstractBastExpr> expr =
        expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = expr.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectRightParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    return new ParseResult<AbstractBastExpr>(expr.value, currentTokenAndHistory);

  }

  /**
   * Parses the.
   *
   * @param data the data
   * @return the bast program
   */
  public BastProgram parse(byte[] data) {
    JavaLexer lexer = new JavaLexer(AresExtension.NO_EXTENSIONS);
    return parse(data, lexer);
  }

  protected BastProgram parse(byte[] fileData, JavaLexer lexer) {
    FileData data = new FileData(fileData);
    try {
      BastProgram program = bastProgram(lexer, data).value;
      return program;
    } catch (SyntaxError e) {
      throw e;
    } catch (AssertionError e) {
      throw e;
    }

  }

  /**
   * Parses the.
   *
   * @param file the file
   * @return the bast program
   */
  public BastProgram parse(File file) {
    JavaLexer lexer = new JavaLexer(AresExtension.NO_EXTENSIONS);
    return parse(file, lexer);

  }

  protected BastProgram parse(File file, JavaLexer lexer) {
    if (!Files.exists(file.toPath(), LinkOption.NOFOLLOW_LINKS)) {
      return null;
    }
    FileData data = new FileData(file);
    try {
      BastProgram program = bastProgram(lexer, data).value;
      return program;
    } catch (SyntaxError e) {
      throw e;
    } catch (AssertionError e) {
      throw e;
    }

  }

  private ParseResult<AbstractBastExpr> prefixExpression3(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // Expression3:
    // PrefixOp Expression3
    // ( Expression | Type ) Expression3
    // Primary {Selector} {PostfixOp}
    TokenAndHistory currentTokenAndHistoryTmp = inputTokenAndHistory;
    TokenAndHistory nextTokenTmp = lexer.nextToken(data, currentTokenAndHistoryTmp);
    TokenAndHistory nextnextToken = null;
    switch (((JavaToken) nextTokenTmp.token).type) {
      case PLUS_PLUS:
      case MINUS_MINUS:
      case NOT:
      case TWIDDLE:
      case PLUS:
        nextnextToken = lexer.nextToken(data, nextTokenTmp);
        return parsePlusInPrefix(lexer, data, currentTokenAndHistoryTmp, nextnextToken);
      case MINUS:
        nextnextToken = lexer.nextToken(data, nextTokenTmp);
        return parseMinusInPrefix(lexer, data, currentTokenAndHistoryTmp, nextnextToken);
      case LPAREN: {
        return parseLeftParenthesisInPrefix(lexer, data, inputTokenAndHistory,
            currentTokenAndHistoryTmp);
      }
      default: {
        return parseDefaultInPrefix(lexer, data, inputTokenAndHistory, currentTokenAndHistoryTmp);
      }
    }


  }

  private ParseResult<AbstractBastExpr> parseLeftParenthesisInPrefix(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory,
      TokenAndHistory currentTokenAndHistoryTmp) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<AbstractBastExpr> expr = new ParseResult<AbstractBastExpr>(null, null);
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistoryTmp);

    currentTokenAndHistory = nextToken;
    final TokenAndHistory tmpBefore = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory recoveryTokenAndHistory = currentTokenAndHistory;
    currentTokenAndHistory = recoveryTokenAndHistory;
    ParseResult<BastType> typeRes = type(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = typeRes.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (typeRes.value == null || TokenChecker.isNotRightParenthesis(nextToken)) {
      currentTokenAndHistory = recoveryTokenAndHistory;
      typeRes = new ParseResult<BastType>(null, currentTokenAndHistory);
      expr = expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
      if (expr.value == null) {
        return new ParseResult<AbstractBastExpr>(null, null);
      }
      currentTokenAndHistory = expr.currentTokenAndHistory;
    }
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isNotRightParenthesis(nextToken)) {
      return new ParseResult<AbstractBastExpr>(null, null);
    }
    currentTokenAndHistory = nextToken;
    TokenAndHistory tmpAfter = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    handlePrefixTokens(expr, nextToken, tmpBefore, typeRes, tmpAfter);
    return parseDifferentOperandsInPrefix(lexer, data, inputTokenAndHistory, currentTokenAndHistory,
        expr, tmpBefore, recoveryTokenAndHistory, typeRes, tmpAfter);
  }

  private ParseResult<AbstractBastExpr> parseDifferentOperandsInPrefix(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory,
      TokenAndHistory currentTokenAndHistory, ParseResult<AbstractBastExpr> expr,
      final TokenAndHistory tmpBefore, TokenAndHistory recoveryTokenAndHistory,
      ParseResult<BastType> typeRes, TokenAndHistory tmpAfter) {
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);

    switch (((JavaToken) nextToken.token).type) {
      case AND:
      case OR:
      case RPAREN:
      case COLON:
      case DIV:
      case QUESTION:
      case XOR:
      case SEMICOLON:
      case MULTIPLY:
      case LESS:
      case REMAINDER:
      case GREATER_EQUAL:
      case NOT_EQUAL:
      case EQUAL:
      case OR_OR:
      case AND_AND:
      case GREATER:
      case EQUAL_EQUAL:
      case LESS_EQUAL:
      case COMMA:
      case PLUS_EQUAL:
      case MINUS_EQUAL:
      case MULTIPLY_EQUAL:
      case XOR_EQUAL:
      case RBRACKET:
      case INSTANCEOF:
      case RBRACE:
      case LBRACE:
        return parseLeftBraceInOperand(lexer, data, currentTokenAndHistory, expr,
            recoveryTokenAndHistory, tmpBefore, tmpAfter);
      case POINT:
        return parsePointInOperand(lexer, data, inputTokenAndHistory, currentTokenAndHistory, expr,
            nextToken, typeRes);

      case PLUS:
      case MINUS:
        if (typeRes.value == null || typeRes.value.getTag() == BastClassType.TAG) {
          if (expr.value != null) {
            return new ParseResult<AbstractBastExpr>(expr.value, currentTokenAndHistory);
          }
          currentTokenAndHistory = recoveryTokenAndHistory;
          expr = expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
          currentTokenAndHistory = expr.currentTokenAndHistory;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
          TokenChecker.expectRightParenthesis(nextToken);
          currentTokenAndHistory = nextToken;
          return new ParseResult<AbstractBastExpr>(expr.value, currentTokenAndHistory);
        }
        break;
      case INTEGER_LITERAL:
      case REAL_LITERAL:
      case STRING_LITERAL:
      case IDENTIFIER:
      case LPAREN:
      case THIS:
      case LBRACKET:
      case NEW:
      case SUPER:
      case PLUS_PLUS:
      case MINUS_MINUS:
      case NULL:
      case CHAR_LITERAL:
      case TWIDDLE:
        break;
      case EOF:
        return expr;
      default:
        throw new SyntaxError("Token not expected.", ((JavaToken) nextToken.token));
    }
    return parseOperandInPrefix(lexer, data, inputTokenAndHistory, currentTokenAndHistory, expr,
        nextToken, typeRes);
  }

  private void handlePrefixTokens(ParseResult<AbstractBastExpr> expr, TokenAndHistory nextToken,
      TokenAndHistory tmpBefore, ParseResult<BastType> typeRes, TokenAndHistory tmpAfter) {
    if (expr != null && expr.value != null) {
      if (expr.value.info != null) {
        expr.value.info.tokensBefore.addFirst(tmpBefore);
      } else {
        throw new SyntaxError("Invalid expression.", ((JavaToken) nextToken.token));
      }
      if (expr.value.info != null) {
        expr.value.info.tokensAfter.addFirst(tmpAfter);
      } else {
        throw new SyntaxError("Invalid expression.", ((JavaToken) nextToken.token));
      }
    } else if (typeRes != null && typeRes.value != null) {
      if (typeRes.value.info != null) {
        typeRes.value.info.tokensBefore.addFirst(tmpBefore);
      } else {
        throw new SyntaxError("Invalied expression.", ((JavaToken) nextToken.token));
      }
      if (typeRes.value.info != null) {
        typeRes.value.info.tokensAfter.addLast(tmpAfter);
      } else {
        throw new SyntaxError("Invalid expression.", ((JavaToken) nextToken.token));
      }
    }
  }

  private ParseResult<AbstractBastExpr> parseLeftBraceInOperand(final JavaLexer lexer,
      final FileData data, TokenAndHistory currentTokenAndHistory,
      ParseResult<AbstractBastExpr> expr, TokenAndHistory recoveryTokenAndHistory,
      TokenAndHistory tmpBefore, TokenAndHistory tmpAfter) {
    if (expr.value != null) {
      return new ParseResult<AbstractBastExpr>(expr.value, currentTokenAndHistory);
    }
    currentTokenAndHistory = recoveryTokenAndHistory;
    expr = expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = expr.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectRightParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    if (expr.value.info != null) {
      expr.value.info.tokensBefore.addFirst(tmpBefore);
    } else {
      throw new SyntaxError("Invalid expression.", ((JavaToken) nextToken.token));
    }
    if (expr.value.info != null) {
      expr.value.info.tokensAfter.addFirst(tmpAfter);
    } else {
      throw new SyntaxError("Invalid expression.", ((JavaToken) nextToken.token));
    }
    return new ParseResult<AbstractBastExpr>(expr.value, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastExpr> parsePointInOperand(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory,
      TokenAndHistory currentTokenAndHistory, ParseResult<AbstractBastExpr> expr,
      TokenAndHistory nextToken, ParseResult<BastType> typeRes) {
    while (TokenChecker.isPoint(nextToken) || TokenChecker.isLeftBracket(nextToken)) {
      if (expr.value == null) {
        if (typeRes.value.getTag() == BastClassType.TAG) {
          for (int i = typeRes.value.info.tokensBefore.size() - 1; i >= 0; i--) {
            ((BastClassType) typeRes.value).name.info.tokensBefore
                .add(typeRes.value.info.tokensBefore.get(i));
          }
          ((BastClassType) typeRes.value).name.info.tokensAfter
              .addAll(typeRes.value.info.tokensAfter);
          expr =
              selector(lexer, data, currentTokenAndHistory, ((BastClassType) typeRes.value).name);
        } else {
          expr = selector(lexer, data, currentTokenAndHistory, typeRes.value);
        }
      } else {
        expr = selector(lexer, data, currentTokenAndHistory, expr.value);
      }
      currentTokenAndHistory = expr.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    while (TokenChecker.isPlusPlus(nextToken) || TokenChecker.isMinusMinus(nextToken)) {
      TokenAndHistory[] tokens = new TokenAndHistory[1];
      tokens[0] = nextToken;
      currentTokenAndHistory = nextToken;
      if (TokenChecker.isPlusPlus(nextToken)) {
        expr = new ParseResult<AbstractBastExpr>(new BastIncrExpr(tokens, expr.value),
            currentTokenAndHistory);
      } else {
        expr = new ParseResult<AbstractBastExpr>(new BastDecrExpr(tokens, expr.value),
            currentTokenAndHistory);
      }
      currentTokenAndHistory = nextToken;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }

    return expr;
  }

  private ParseResult<AbstractBastExpr> parseOperandInPrefix(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory,
      TokenAndHistory currentTokenAndHistory, ParseResult<AbstractBastExpr> expr,
      TokenAndHistory nextToken, ParseResult<BastType> typeRes) {
    ParseResult<AbstractBastExpr> operand = prefixExpression3(lexer, data, currentTokenAndHistory);
    if (operand.value == null) {
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      switch (((JavaToken) nextToken.token).type) {
        case POINT:
        case LBRACKET:
          while (TokenChecker.isPoint(nextToken) || TokenChecker.isLeftBracket(nextToken)) {
            expr = selector(lexer, data, currentTokenAndHistory, expr.value);
            currentTokenAndHistory = expr.currentTokenAndHistory;
            nextToken = lexer.nextToken(data, currentTokenAndHistory);
          }
          break;
        case SEMICOLON:
        case COLON:
        case PLUS:
        case RPAREN:
          break;
        case PLUS_PLUS:
        case MINUS_MINUS:
          currentTokenAndHistory = inputTokenAndHistory;
          expr = primary(lexer, data, currentTokenAndHistory);
          if (expr.value == null) {
            return new ParseResult<AbstractBastExpr>(null, null);
          }
          currentTokenAndHistory = expr.currentTokenAndHistory;
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
          while (TokenChecker.isPoint(nextToken) || TokenChecker.isLeftBracket(nextToken)) {
            expr = selector(lexer, data, currentTokenAndHistory, expr.value);
            currentTokenAndHistory = expr.currentTokenAndHistory;
            nextToken = lexer.nextToken(data, currentTokenAndHistory);
          }
          while (TokenChecker.isPlusPlus(nextToken) || TokenChecker.isMinusMinus(nextToken)) {
            TokenAndHistory[] tokens = new TokenAndHistory[2];
            tokens[0] = nextToken;
            if (TokenChecker.isPlusPlus(nextToken)) {

              expr = new ParseResult<AbstractBastExpr>(new BastIncrExpr(tokens, expr.value),
                  currentTokenAndHistory);
            } else {
              expr = new ParseResult<AbstractBastExpr>(new BastDecrExpr(tokens, expr.value),
                  currentTokenAndHistory);
            }
            currentTokenAndHistory = nextToken;
            nextToken = lexer.nextToken(data, currentTokenAndHistory);
          }

          break;
        default:
          throw new SyntaxError("Token not expected.", ((JavaToken) nextToken.token));

      }

      return new ParseResult<AbstractBastExpr>(expr.value, currentTokenAndHistory);
    } else {
      if (typeRes.value != null) {
        currentTokenAndHistory = operand.currentTokenAndHistory;
        TokenAndHistory[] tokens = new TokenAndHistory[2];
        tokens[0] = typeRes.value.info.tokensBefore.getFirst();
        tokens[1] = typeRes.value.info.tokensAfter.getLast();
        BastCastExpr castExpr = new BastCastExpr(tokens, operand.value, typeRes.value);
        typeRes.value.info.tokensBefore.removeFirst();
        typeRes.value.info.tokensAfter.removeLast();
        return new ParseResult<AbstractBastExpr>(castExpr, currentTokenAndHistory);
      } else {
        throw new SyntaxError("Invalid expression.", ((JavaToken) nextToken.token));
      }
    }
  }

  private ParseResult<AbstractBastExpr> parseDefaultInPrefix(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory,
      TokenAndHistory currentTokenAndHistoryTmp) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<AbstractBastExpr> expr = new ParseResult<AbstractBastExpr>(null, null);
    expr = primary(lexer, data, currentTokenAndHistory);
    if (expr.value == null) {
      return new ParseResult<AbstractBastExpr>(null, null);
    }
    currentTokenAndHistory = expr.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    while (TokenChecker.isPoint(nextToken) || TokenChecker.isLeftBracket(nextToken)) {
      expr = selector(lexer, data, currentTokenAndHistory, expr.value);
      currentTokenAndHistory = expr.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    while (TokenChecker.isPlusPlus(nextToken) || TokenChecker.isMinusMinus(nextToken)) {
      TokenAndHistory[] tokens = new TokenAndHistory[2];
      tokens[0] = nextToken;
      if (TokenChecker.isPlusPlus(nextToken)) {
        expr = new ParseResult<AbstractBastExpr>(new BastIncrExpr(tokens, expr.value),
            currentTokenAndHistory);
      } else {
        expr = new ParseResult<AbstractBastExpr>(new BastDecrExpr(tokens, expr.value),
            currentTokenAndHistory);
      }
      currentTokenAndHistory = nextToken;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    return new ParseResult<AbstractBastExpr>(expr.value, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastExpr> parseMinusInPrefix(final JavaLexer lexer,
      final FileData data, TokenAndHistory currentTokenAndHistory, TokenAndHistory nextnextToken) {
    switch (((JavaToken) nextnextToken.token).type) {
      case REAL_LITERAL:
        return prefixOp(lexer, data, currentTokenAndHistory);
      case INTEGER_LITERAL:
        return prefixOp(lexer, data, currentTokenAndHistory);
      case IDENTIFIER:
        return prefixOp(lexer, data, currentTokenAndHistory);
      case NEW:
        return prefixOp(lexer, data, currentTokenAndHistory);
      case LPAREN:
        return prefixOp(lexer, data, currentTokenAndHistory);
      case STRING_LITERAL:
        return prefixOp(lexer, data, currentTokenAndHistory);
      case THIS:
        return prefixOp(lexer, data, currentTokenAndHistory);
      case SUPER:
        return prefixOp(lexer, data, currentTokenAndHistory);
      default:
        return new ParseResult<AbstractBastExpr>(null, null);
    }
  }

  private ParseResult<AbstractBastExpr> parsePlusInPrefix(final JavaLexer lexer,
      final FileData data, TokenAndHistory currentTokenAndHistory, TokenAndHistory nextnextToken) {
    switch (((JavaToken) nextnextToken.token).type) {
      case INTEGER_LITERAL:
      case REAL_LITERAL:
      case IDENTIFIER:
      case NEW:
      case LPAREN:
      case STRING_LITERAL:
      case THIS:
      case SUPER:
      case TWIDDLE:
      case TRUE:
      case FALSE:
        return prefixOp(lexer, data, currentTokenAndHistory);
      default:
        return new ParseResult<AbstractBastExpr>(null, null);
    }
  }



  private ParseResult<AbstractBastExpr> prefixOp(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    int type = -1;
    ParseResult<AbstractBastExpr> expr = new ParseResult<AbstractBastExpr>(null, null);
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    switch (((JavaToken) nextToken.token).type) {
      case PLUS_PLUS:
        type = BastUnaryExpr.TYPE_INCR;
        break;
      case MINUS_MINUS:
        type = BastUnaryExpr.TYPE_DECR;
        break;
      case NOT:
        type = BastUnaryExpr.TYPE_NOT;
        break;
      case TWIDDLE:
        type = BastUnaryExpr.TYPE_INVERSE;
        break;
      case PLUS:
        type = BastUnaryExpr.TYPE_PLUS;
        break;
      case MINUS:
        type = BastUnaryExpr.TYPE_NEG;
        break;
      default:
        // Do nothing
    }

    if (type != -1) {
      currentTokenAndHistory = nextToken;
    }
    expr = prefixExpression3(lexer, data, currentTokenAndHistory);
    TokenAndHistory[] tokens = { currentTokenAndHistory };
    return new ParseResult<AbstractBastExpr>(new BastUnaryExpr(tokens, expr.value, type),
        expr.currentTokenAndHistory);

  }

  private ParseResult<AbstractBastExpr> primary(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // Primary:
    // ParExpression
    // NonWildcardTypeArguments (ExplicitGenericInvocationSuffix | this
    // Arguments)
    // this [Arguments]
    // super SuperSuffix
    // Literal
    // new Creator
    // Identifier { . Identifier }[ IdentifierSuffix]
    // BasicType {[]} .class
    // void.class
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    AbstractBastExpr expr = null;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    ParseResult<LinkedList<AbstractBastExpr>> arguments =
        new ParseResult<LinkedList<AbstractBastExpr>>(null, null);
    switch (((JavaToken) nextToken.token).type) {
      case LPAREN:
        return parExpression(lexer, data, currentTokenAndHistory, true);
      case LESS:
        return parseLessInPrimary(lexer, data, currentTokenAndHistory);
      case THIS:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        expr = new BastThis(tokens);
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        if (TokenChecker.isLeftParenthesis(nextToken)) {
          TokenAndHistory[] tokenstmp = new TokenAndHistory[3];
          currentTokenAndHistory = nextToken;
          tokenstmp[0] = currentTokenAndHistory;
          currentTokenAndHistory = currentTokenAndHistory.setFlushed();
          arguments = arguments(lexer, data, currentTokenAndHistory);
          tokenstmp[1] = new TokenAndHistory(new ListToken(arguments.additionalTokens));

          currentTokenAndHistory = arguments.currentTokenAndHistory;

          tokenstmp[2] = currentTokenAndHistory;
          currentTokenAndHistory = currentTokenAndHistory.setFlushed();
          expr = new BastCall(tokenstmp, expr, arguments.value);
        }
        return new ParseResult<AbstractBastExpr>(expr, currentTokenAndHistory);
      case SUPER:
        return parseSuperInPrimary(lexer, data, currentTokenAndHistory);
      case STRING_LITERAL:
      case INTEGER_LITERAL:
      case CHAR_LITERAL:
      case REAL_LITERAL:
      case TRUE:
      case FALSE:
      case NULL:
        ParseResult<AbstractBastConstant> literal = literal(lexer, data, currentTokenAndHistory);
        return new ParseResult<AbstractBastExpr>(literal.value, literal.currentTokenAndHistory);
      case NEW:
        return creator(lexer, data, currentTokenAndHistory);
      case IDENTIFIER:
        return identifierSuffix(lexer, data, currentTokenAndHistory);
      case BYTE:
      case SHORT:
      case CHAR:
      case INT:
      case LONG:
      case FLOAT:
      case DOUBLE:
      case BOOLEAN:
        return basicTypeClass(lexer, data, currentTokenAndHistory);
      case VOID:
        currentTokenAndHistory = nextToken;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        TokenAndHistory[] tokens4 = { currentTokenAndHistory };
        final BastType type = new BastBasicType(tokens4, TagConstants.TYPE_VOID);
        TokenChecker.expectPoint(nextToken);
        currentTokenAndHistory = nextToken;
        TokenAndHistory[] tokenstmpAccess = new TokenAndHistory[2];
        tokenstmpAccess[0] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        TokenChecker.expectClass(nextToken);
        currentTokenAndHistory = nextToken;
        TokenAndHistory[] tokens3 = { currentTokenAndHistory };
        BastClassConst classNode = new BastClassConst(tokens3);
        assert (type != null);
        return new ParseResult<AbstractBastExpr>(new BastAccess(tokenstmpAccess, type, classNode),
            currentTokenAndHistory);

      default:
        // Do nothing
    }
    return new ParseResult<AbstractBastExpr>(null, null);
  }

  private ParseResult<AbstractBastExpr> parseSuperInPrimary(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    AbstractBastExpr expr = null;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    currentTokenAndHistory = nextToken;
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    ParseResult<LinkedList<AbstractBastExpr>> arguments =
        new ParseResult<LinkedList<AbstractBastExpr>>(null, null);
    tokens[0] = currentTokenAndHistory;
    expr = new BastSuper(tokens);

    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isLeftParenthesis(nextToken)) {
      TokenAndHistory[] tokenstmp = new TokenAndHistory[3];
      currentTokenAndHistory = nextToken;
      tokenstmp[0] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      arguments = arguments(lexer, data, currentTokenAndHistory);
      tokenstmp[1] = new TokenAndHistory(new ListToken(arguments.additionalTokens));

      currentTokenAndHistory = arguments.currentTokenAndHistory;
      tokenstmp[2] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      expr = new BastCall(tokenstmp, expr, arguments.value);
    } else if (TokenChecker.isPoint(nextToken)) {
      currentTokenAndHistory = nextToken;
      TokenAndHistory[] tokenstmpAccess = new TokenAndHistory[2];
      tokenstmpAccess[0] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      ParseResult<BastNameIdent> ident = identifier(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = ident.currentTokenAndHistory;
      assert (expr != null);
      expr = new BastAccess(tokenstmpAccess, expr, ident.value);
      if (ident.currentTokenAndHistory != null) {
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      }
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      if (TokenChecker.isLeftParenthesis(nextToken)) {
        TokenAndHistory[] tokenstmp = new TokenAndHistory[3];
        currentTokenAndHistory = nextToken;
        tokenstmp[0] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        arguments = arguments(lexer, data, currentTokenAndHistory);
        tokenstmp[1] = new TokenAndHistory(new ListToken(arguments.additionalTokens));

        currentTokenAndHistory = arguments.currentTokenAndHistory;
        tokenstmp[2] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        expr = new BastCall(tokenstmp, expr, arguments.value);

      }
    }
    return new ParseResult<AbstractBastExpr>(expr, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastExpr> parseLessInPrimary(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    AbstractBastExpr expr = null;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<LinkedList<BastTypeArgument>> typeArguments =
        typeArgumentList(lexer, data, currentTokenAndHistory, false, false);
    currentTokenAndHistory = typeArguments.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    ParseResult<BastNameIdent> ident = new ParseResult<BastNameIdent>(null, null);
    ParseResult<LinkedList<AbstractBastExpr>> arguments =
        new ParseResult<LinkedList<AbstractBastExpr>>(null, null);
    switch (((JavaToken) nextToken.token).type) {
      case SUPER:
        tokens[0] = nextToken;
        BastSuper superNode = new BastSuper(tokens);
        expr = new BastTemplateSpecifier(null, superNode, typeArguments.value);
        currentTokenAndHistory = nextToken;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        if (TokenChecker.isPoint(nextToken)) {
          currentTokenAndHistory = nextToken;
          TokenAndHistory[] tokenstmpAccess = new TokenAndHistory[2];
          tokenstmpAccess[0] = currentTokenAndHistory;
          currentTokenAndHistory = currentTokenAndHistory.setFlushed();
          ident = identifier(lexer, data, currentTokenAndHistory);
          nextToken = lexer.nextToken(data, currentTokenAndHistory);
          assert (ident.value != null);
          expr = new BastAccess(tokenstmpAccess, ident.value, expr);
          if (TokenChecker.isLeftParenthesis(nextToken)) {
            TokenAndHistory[] tokenstmp = new TokenAndHistory[3];
            currentTokenAndHistory = nextToken;
            tokenstmp[0] = currentTokenAndHistory;
            currentTokenAndHistory = currentTokenAndHistory.setFlushed();
            arguments = arguments(lexer, data, currentTokenAndHistory);
            tokenstmp[1] = new TokenAndHistory(new ListToken(arguments.additionalTokens));

            currentTokenAndHistory = arguments.currentTokenAndHistory;

            tokenstmp[2] = currentTokenAndHistory;
            currentTokenAndHistory = currentTokenAndHistory.setFlushed();
            expr = new BastCall(tokenstmp, expr, arguments.value);

          }
        } else {
          TokenAndHistory[] tokenstmp = new TokenAndHistory[3];

          currentTokenAndHistory = nextToken;
          tokenstmp[0] = currentTokenAndHistory;
          currentTokenAndHistory = currentTokenAndHistory.setFlushed();
          arguments = arguments(lexer, data, currentTokenAndHistory);
          tokenstmp[1] = new TokenAndHistory(new ListToken(arguments.additionalTokens));

          currentTokenAndHistory = arguments.currentTokenAndHistory;
          tokenstmp[2] = currentTokenAndHistory;
          currentTokenAndHistory = currentTokenAndHistory.setFlushed();
          expr = new BastCall(tokenstmp, expr, arguments.value);

        }
        return new ParseResult<AbstractBastExpr>(expr, currentTokenAndHistory);
      case THIS:
        tokens[0] = nextToken;
        BastThis thisNode = new BastThis(tokens);
        expr = new BastTemplateSpecifier(null, thisNode, typeArguments.value);
        currentTokenAndHistory = nextToken;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        TokenChecker.expectLeftParenthesis(nextToken);
        TokenAndHistory[] tokenstmp = new TokenAndHistory[3];
        currentTokenAndHistory = nextToken;
        tokenstmp[0] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        arguments = arguments(lexer, data, currentTokenAndHistory);
        tokenstmp[1] = new TokenAndHistory(new ListToken(arguments.additionalTokens));
        currentTokenAndHistory = arguments.currentTokenAndHistory;
        tokenstmp[2] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        expr = new BastCall(tokenstmp, expr, arguments.value);
        return new ParseResult<AbstractBastExpr>(expr, currentTokenAndHistory);
      default:
        throw new SyntaxError("SUPER or THIS expected.", ((JavaToken) nextToken.token));

    }
  }


  private ParseResult<LinkedList<AbstractBastExpr>> qualifiedIdentifierList(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // QualifiedIdentifierList:
    // QualifiedIdentifier { , QualifiedIdentifier}
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    LinkedList<AbstractBastExpr> list = null;
    ParseResult<AbstractBastExpr> qualifiedIdent =
        qualifiedName(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = qualifiedIdent.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    list = add(list, qualifiedIdent.value);
    while (TokenChecker.isComma(nextToken)) {
      currentTokenAndHistory = nextToken;
      qualifiedIdent = qualifiedName(lexer, data, currentTokenAndHistory);
      list = add(list, qualifiedIdent.value);
      currentTokenAndHistory = qualifiedIdent.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    return new ParseResult<LinkedList<AbstractBastExpr>>(list, currentTokenAndHistory);

  }

  /**
   * Qualified name.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<AbstractBastExpr> qualifiedName(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<BastNameIdent> nameIdentRes = new ParseResult<BastNameIdent>(null, null);
    AbstractBastExpr expr = null;
    nameIdentRes = identifier(lexer, data, currentTokenAndHistory);
    expr = nameIdentRes.value;
    currentTokenAndHistory = nameIdentRes.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokenstmp = null;

    TokenAndHistory nextNextToken = lexer.nextToken(data, nextToken);
    while (TokenChecker.isPoint(nextToken) && TokenChecker.isIdentifier(nextNextToken)) {
      currentTokenAndHistory = nextToken;
      tokenstmp = new TokenAndHistory[2];
      tokenstmp[0] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      nameIdentRes = identifier(lexer, data, currentTokenAndHistory);
      assert (expr != null);
      expr = new BastAccess(tokenstmp, expr, nameIdentRes.value);
      currentTokenAndHistory = nameIdentRes.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      nextNextToken = lexer.nextToken(data, nextToken);

    }
    return new ParseResult<AbstractBastExpr>(expr, currentTokenAndHistory);
  }

  private ParseResult<BastDeclaration> resource(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // Resource:
    // {VariableModifier} ReferenceType VariableDeclaratorId = Expression
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<LinkedList<AbstractBastSpecifier>> modifiers =
        modifierList(lexer, data, currentTokenAndHistory, true);
    currentTokenAndHistory = modifiers.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectIdentifier(nextToken);
    ParseResult<BastType> typeRes = type(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = typeRes.currentTokenAndHistory;
    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);

    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectEqual(nextToken);
    currentTokenAndHistory = nextToken;
    TokenAndHistory[] tokens = { currentTokenAndHistory };

    ParseResult<AbstractBastExpr> expr =
        expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
    BastTypeSpecifier typeSpec = new BastTypeSpecifier(null, typeRes.value);
    BastIdentDeclarator declarator = new BastIdentDeclarator(tokens, nameRes.value);
    LinkedList<AbstractBastSpecifier> specifierList = new LinkedList<AbstractBastSpecifier>();
    specifierList.add(typeSpec);
    LinkedList<AbstractBastDeclarator> declaratorList = new LinkedList<AbstractBastDeclarator>();
    declarator.setInitializer(new BastExprInitializer(null, expr.value));
    declaratorList.add(declarator);
    BastDeclaration decl = new BastDeclaration(null, specifierList, declaratorList);
    if (modifiers.value != null) {
      decl.setModifiers(modifiers.value);
    }
    return new ParseResult<BastDeclaration>(decl, expr.currentTokenAndHistory);
  }

  private ParseResult<LinkedList<BastDeclaration>> resourceList(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // Resources:
    // Resource { ; Resource }
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    LinkedList<BastDeclaration> list = null;
    ParseResult<BastDeclaration> resource = resource(lexer, data, currentTokenAndHistory);
    list = add(list, resource.value);
    currentTokenAndHistory = resource.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
    while (TokenChecker.isSemicolon(nextToken) && TokenChecker.isNotLeftParenthesis(nextnextToken)
        && TokenChecker.isNotRightParenthesis(nextnextToken)) {
      currentTokenAndHistory = nextToken;
      resource.value.info.tokensAfter.add(currentTokenAndHistory);
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      resource = resource(lexer, data, currentTokenAndHistory);
      list = add(list, resource.value);
      currentTokenAndHistory = resource.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      nextnextToken = lexer.nextToken(data, nextToken);
    }
    return new ParseResult<LinkedList<BastDeclaration>>(list, currentTokenAndHistory);
  }

  private ParseResult<LinkedList<BastDeclaration>> resourceSpecification(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // ( Resources [;] )
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectLeftParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<LinkedList<BastDeclaration>> list =
        resourceList(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = list.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isSemicolon(nextToken)) {
      currentTokenAndHistory = nextToken;
      list.value.getLast().info.tokensAfter.add(currentTokenAndHistory);
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    TokenChecker.expectRightParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    return new ParseResult<LinkedList<BastDeclaration>>(list.value, currentTokenAndHistory);

  }

  private ParseResult<AbstractBastExpr> selector(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory, AbstractBastExpr left) {
    // Selector: Selector:
    // . Identifier [Arguments]
    // . ExplicitGenericInvocation
    // . this
    // . super SuperSuffix
    // . new [NonWildcardTypeArguments] InnerCreator
    // [ Expression ]
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<BastNameIdent> ident = new ParseResult<BastNameIdent>(null, null);
    ParseResult<LinkedList<AbstractBastExpr>> arguments =
        new ParseResult<LinkedList<AbstractBastExpr>>(null, null);
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    switch (((JavaToken) nextToken.token).type) {
      case POINT:
        currentTokenAndHistory = nextToken;
        TokenAndHistory[] tokenstmp = new TokenAndHistory[2];
        tokenstmp[0] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        switch (((JavaToken) nextToken.token).type) {
          case IDENTIFIER:
            ident = identifier(lexer, data, currentTokenAndHistory);
            currentTokenAndHistory = ident.currentTokenAndHistory;
            nextToken = lexer.nextToken(data, currentTokenAndHistory);
            assert (left != null);
            BastAccess access = new BastAccess(tokenstmp, left, ident.value);
            if (TokenChecker.isLeftParenthesis(nextToken)) {
              tokens = new TokenAndHistory[3];
              currentTokenAndHistory = nextToken;
              tokens[0] = currentTokenAndHistory;
              currentTokenAndHistory = currentTokenAndHistory.setFlushed();
              arguments = arguments(lexer, data, currentTokenAndHistory);
              tokens[1] = new TokenAndHistory(new ListToken(arguments.additionalTokens));

              currentTokenAndHistory = arguments.currentTokenAndHistory;
              tokens[2] = currentTokenAndHistory;
              currentTokenAndHistory = currentTokenAndHistory.setFlushed();
              return new ParseResult<AbstractBastExpr>(
                  new BastCall(tokens, access, arguments.value), currentTokenAndHistory);
            } else {
              return new ParseResult<AbstractBastExpr>(access, currentTokenAndHistory);
            }
          case LESS:
            ParseResult<AbstractBastExpr> genericAccess =
                explicitGenericInvocation(lexer, data, currentTokenAndHistory, left, tokenstmp);
            return genericAccess;
          case THIS:
            currentTokenAndHistory = nextToken;
            tokens = new TokenAndHistory[1];
            tokens[0] = currentTokenAndHistory;
            BastThis thisNode = new BastThis(tokens);
            assert (left != null);
            return new ParseResult<AbstractBastExpr>(new BastAccess(tokenstmp, left, thisNode),
                currentTokenAndHistory);
          case SUPER:
            return parseSuperInSelector(lexer, data, currentTokenAndHistory, left, tokenstmp);
          case NEW:
            return innerCreator(lexer, data, currentTokenAndHistory, left, tokenstmp);
          default:
            throw new SyntaxError("Token not expected.", ((JavaToken) nextToken.token));
        }
      case LBRACKET:
        currentTokenAndHistory = nextToken;
        ArrayList<TokenAndHistory> additionalTokens = new ArrayList<>();
        additionalTokens.add(currentTokenAndHistory);
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        ParseResult<AbstractBastExpr> exprRes =
            expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
        assert (exprRes.value != null);
        currentTokenAndHistory = exprRes.currentTokenAndHistory;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        TokenChecker.expectRightBracket(nextToken);
        currentTokenAndHistory = nextToken;
        additionalTokens.add(currentTokenAndHistory);
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        if (left.getTag() == TagConstants.BAST_ARRAY_REF) {
          ((BastArrayRef) left).indices.add(exprRes.value);
          ((ListToken) left.info.tokens[0].token).tokenList.addAll(additionalTokens);
          return new ParseResult<AbstractBastExpr>(left, currentTokenAndHistory);
        } else {
          TokenAndHistory[] tokensTmp = new TokenAndHistory[1];
          tokensTmp[0] = new TokenAndHistory(new ListToken(additionalTokens));
          LinkedList<AbstractBastExpr> indices = new LinkedList<AbstractBastExpr>();
          indices.add(exprRes.value);
          return new ParseResult<AbstractBastExpr>(new BastArrayRef(tokensTmp, left, indices),
              currentTokenAndHistory);
        }
      default:
        throw new SyntaxError("'.' or '[' expected.", ((JavaToken) nextToken.token));
    }
  }

  private ParseResult<AbstractBastExpr> parseSuperInSelector(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory, AbstractBastExpr left,
      TokenAndHistory[] tokenstmp) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    ParseResult<LinkedList<AbstractBastExpr>> arguments =
        new ParseResult<LinkedList<AbstractBastExpr>>(null, null);
    currentTokenAndHistory = nextToken;
    tokens[0] = currentTokenAndHistory;
    BastSuper superNode = new BastSuper(tokens);
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    AbstractBastExpr expr = null;
    ParseResult<BastNameIdent> ident = new ParseResult<BastNameIdent>(null, null);
    switch (((JavaToken) nextToken.token).type) {
      case LPAREN:
        tokens = new TokenAndHistory[3];
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        arguments = arguments(lexer, data, currentTokenAndHistory);
        tokens[1] = new TokenAndHistory(new ListToken(arguments.additionalTokens));

        currentTokenAndHistory = arguments.currentTokenAndHistory;
        tokens[2] = currentTokenAndHistory;
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        BastCall call = new BastCall(tokens, superNode, arguments.value);
        assert (left != null);
        expr = new BastAccess(tokenstmp, left, call);
        return new ParseResult<AbstractBastExpr>(expr, currentTokenAndHistory);
      case IDENTIFIER:
        ident = identifier(lexer, data, currentTokenAndHistory);
        currentTokenAndHistory = ident.currentTokenAndHistory;
        assert (expr != null);
        expr = new BastAccess(tokenstmp, expr, ident.value);
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        if (TokenChecker.isLeftParenthesis(nextToken)) {
          tokens = new TokenAndHistory[3];
          currentTokenAndHistory = nextToken;
          tokens[0] = currentTokenAndHistory;
          currentTokenAndHistory = currentTokenAndHistory.setFlushed();
          arguments = arguments(lexer, data, currentTokenAndHistory);
          tokens[1] = new TokenAndHistory(new ListToken(arguments.additionalTokens));

          tokens[2] = currentTokenAndHistory;
          currentTokenAndHistory = currentTokenAndHistory.setFlushed();
          return new ParseResult<AbstractBastExpr>(new BastCall(tokens, expr, arguments.value),
              currentTokenAndHistory);
        }
        return new ParseResult<AbstractBastExpr>(expr, currentTokenAndHistory);
      case POINT:
        expr = new BastAccess(tokenstmp, left, superNode);
        return selector(lexer, data, currentTokenAndHistory, expr);
      default:
        throw new SyntaxError("Token not expected.", ((JavaToken) nextToken.token));

    }
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
      throw new SyntaxError("//# not expected.", ((JavaToken) nextToken.token));
    }
    return innerStatement(lexer, data, inputTokenAndHistory);
  }


  /**
   * Statement.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<AbstractBastStatement> innerStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // Statement:
    // Block
    // ;
    // Identifier : Statement
    // StatementExpression ;
    // if ParExpression Statement [else Statement]
    // assert Expression [: Expression] ;
    // switch ParExpression { SwitchBlockStatementGroups }
    // while ParExpression Statement
    // do Statement while ParExpression ;
    // for ( ForControl ) Statement
    // break [Identifier] ;
    // continue [Identifier] ;
    // return [Expression] ;
    // throw Expression ;
    // synchronized ParExpression Block
    // try Block ( Catches | [Catches] Finally )
    // try ResourceSpecification Block [Catches] [Finally]
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<AbstractBastExpr> exprStmt = new ParseResult<AbstractBastExpr>(null, null);
    switch (((JavaToken) nextToken.token).type) {
      case LBRACE:
        // Block
        ParseResult<BastBlock> blockRes = block(lexer, data, currentTokenAndHistory, false);
        return new ParseResult<AbstractBastStatement>(blockRes.value,
            blockRes.currentTokenAndHistory);
      case SEMICOLON:
        currentTokenAndHistory = nextToken;
        BastEmptyStmt emptyStmt = new BastEmptyStmt(null);
        emptyStmt.info.tokensAfter.add(currentTokenAndHistory);
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        return new ParseResult<AbstractBastStatement>(emptyStmt, currentTokenAndHistory);
      case IDENTIFIER:
        return parseIdentifierInStatement(lexer, data, currentTokenAndHistory);
      case LPAREN:
      case LESS:
        return parseStatementExprInStatement(lexer, data, currentTokenAndHistory);
      case THIS:
      case SUPER:
      case NEW:
      case PLUS_PLUS:
      case MINUS_MINUS:
      case NULL:
        // StatementExpression:
        // Expression
        exprStmt = expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
        currentTokenAndHistory = exprStmt.currentTokenAndHistory;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        TokenChecker.expectSemicolon(nextToken);
        currentTokenAndHistory = nextToken;
        exprStmt.value.info.tokensAfter.add(currentTokenAndHistory);
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        return new ParseResult<AbstractBastStatement>(exprStmt.value, currentTokenAndHistory);
      case IF:
        // if ParExpression Statement [else Statement]
        return ifStatement(lexer, data, currentTokenAndHistory);
      case ASSERT:
        // assert Expression [: Expression] ;
        return assertStatement(lexer, data, currentTokenAndHistory);
      case SWITCH:
        // switch ParExpression { SwitchBlockStatementGroups }
        return switchStatement(lexer, data, currentTokenAndHistory);
      case WHILE:
        // while ParExpression Statement
        return whileStatement(lexer, data, currentTokenAndHistory);
      case DO:
        // do Statement while ParExpression ;
        return doWhileStatement(lexer, data, currentTokenAndHistory);
      case FOR:
        // for ( ForControl ) Statement
        return forStatement(lexer, data, currentTokenAndHistory);
      case BREAK:
        // break [Identifier] ;
        return breakStatement(lexer, data, currentTokenAndHistory);
      case CONTINUE:
        // continue [Identifier] ;
        return continueStatement(lexer, data, currentTokenAndHistory);
      case RETURN:
        // return [Expression] ;
        return parseReturnInStatement(lexer, data, nextToken);
      case THROW:
        // throw Expression ;
        return parseThrowInStatement(lexer, data, nextToken);
      case SYNCHRONIZED:
        // synchronized ParExpression Block
        return synchronizedStatement(lexer, data, currentTokenAndHistory);
      case TRY:
        // try Block ( Catches | [Catches] Finally )
        // try ResourceSpecification Block [Catches] [Finally]
        return tryStatement(lexer, data, currentTokenAndHistory);
      default:
        throw new SyntaxError("Token not expected.", ((JavaToken) nextToken.token));
    }
  }

  private ParseResult<AbstractBastStatement> parseStatementExprInStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // StatementExpression:
    // Expression
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<AbstractBastExpr> exprStmt =
        expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = exprStmt.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectSemicolon(nextToken);
    currentTokenAndHistory = nextToken;
    exprStmt.value.info.tokensAfter.add(currentTokenAndHistory);
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    return new ParseResult<AbstractBastStatement>(exprStmt.value, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastStatement> parseIdentifierInStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // Identifier : Statement
    // StatementExpression:
    // Expression
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
    ParseResult<AbstractBastExpr> exprStmt = new ParseResult<AbstractBastExpr>(null, null);
    if (TokenChecker.isColon(nextnextToken)) {
      ParseResult<AbstractBastStatement> stmt =
          labeledStatement(lexer, data, currentTokenAndHistory);
      return stmt;
    }
    exprStmt = expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = exprStmt.currentTokenAndHistory;
    if (currentTokenAndHistory == null) {
      throw new SyntaxError("Unfinished statement. Semicolon expected.",
          ((JavaToken) nextToken.token));
    }
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isNotSemicolon(nextToken)) {
      TokenChecker.rightBraceNotExpected(nextToken);
      throw new SyntaxError("';' expected.", ((JavaToken) nextToken.token));
    }
    currentTokenAndHistory = nextToken;
    exprStmt.value.info.tokensAfter.add(currentTokenAndHistory);
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    return new ParseResult<AbstractBastStatement>(exprStmt.value, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastStatement> parseReturnInStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    final TokenAndHistory[] tokens = { currentTokenAndHistory };
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<AbstractBastExpr> exprStmt = new ParseResult<AbstractBastExpr>(null, null);
    if (TokenChecker.isNotSemicolon(nextToken)) {
      exprStmt = expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
      assert (exprStmt.value != null);
      currentTokenAndHistory = exprStmt.currentTokenAndHistory;
    }
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectSemicolon(nextToken);
    BastReturn returnStmt = new BastReturn(tokens, exprStmt.value);
    currentTokenAndHistory = nextToken;
    returnStmt.info.tokensAfter.add(currentTokenAndHistory);
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    return new ParseResult<AbstractBastStatement>(returnStmt, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastStatement> parseThrowInStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    final TokenAndHistory[] throwTokens = { currentTokenAndHistory };
    ParseResult<AbstractBastExpr> exprStmt =
        expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = exprStmt.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectSemicolon(nextToken);
    BastThrowStmt throwStmt = new BastThrowStmt(throwTokens, exprStmt.value);
    currentTokenAndHistory = nextToken;
    throwStmt.info.tokensAfter.add(currentTokenAndHistory);
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    return new ParseResult<AbstractBastStatement>(throwStmt, currentTokenAndHistory);
  }

  /**
   * Switch block statement group.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<AbstractBastStatement> switchBlockStatementGroup(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // SwitchBlockStatementGroup:
    // SwitchLabels BlockStatements
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    ParseResult<LinkedList<AbstractBastStatement>> labels =
        switchLabels(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = labels.currentTokenAndHistory;
    if (labels.value == null) {
      return new ParseResult<AbstractBastStatement>(null, null);
    }
    ParseResult<LinkedList<AbstractBastStatement>> stmts =
        blockStatementList(lexer, data, currentTokenAndHistory);
    BastSwitchCaseGroup group = new BastSwitchCaseGroup(null, labels.value, stmts.value);
    return new ParseResult<AbstractBastStatement>(group, stmts.currentTokenAndHistory);
  }

  protected ParseResult<LinkedList<AbstractBastStatement>> switchBlockStatementGroups(
      final JavaLexer lexer, final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // SwitchBlockStatementGroups:
    // { SwitchBlockStatementGroup }


    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<AbstractBastStatement> switchGroup = null;
    TokenChecker.aresTokenNotExpected(nextToken);

    switchGroup = switchBlockStatementGroup(lexer, data, currentTokenAndHistory);

    if (switchGroup.value == null) {
      return new ParseResult<LinkedList<AbstractBastStatement>>(null, currentTokenAndHistory);
    }
    currentTokenAndHistory = switchGroup.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    LinkedList<AbstractBastStatement> list = null;
    list = add(list, switchGroup.value);

    while (TokenChecker.isNotRightBrace(nextToken)) {
      TokenChecker.aresTokenNotExpected(nextToken);
      switchGroup = switchBlockStatementGroup(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = switchGroup.currentTokenAndHistory;
      list = add(list, switchGroup.value);
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    return new ParseResult<LinkedList<AbstractBastStatement>>(list, currentTokenAndHistory);

  }

  /**
   * Switch label.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<AbstractBastStatement> switchLabel(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // SwitchLabel:
    // case Expression :
    // case EnumConstantName :
    // default :
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory[] tokens = new TokenAndHistory[2];
    ParseResult<AbstractBastExpr> expr = new ParseResult<AbstractBastExpr>(null, null);
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    switch (((JavaToken) nextToken.token).type) {
      case CASE:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        expr = expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
        currentTokenAndHistory = expr.currentTokenAndHistory;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        TokenChecker.expectColon(nextToken);
        currentTokenAndHistory = nextToken;
        tokens[1] = currentTokenAndHistory;
        return new ParseResult<AbstractBastStatement>(new BastCase(tokens, expr.value),
            currentTokenAndHistory);
      case DEFAULT:
        currentTokenAndHistory = nextToken;
        tokens[0] = currentTokenAndHistory;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        TokenChecker.expectColon(nextToken);
        currentTokenAndHistory = nextToken;
        tokens[1] = currentTokenAndHistory;
        return new ParseResult<AbstractBastStatement>(new BastDefault(tokens),
            currentTokenAndHistory);
      default:
        throw new SyntaxError("CASE expected.", nextToken.token.getPos());
    }
  }

  private ParseResult<LinkedList<AbstractBastStatement>> switchLabels(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // SwitchLabels:
    // SwitchLabel { SwitchLabel }
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isNotCase(nextToken) && TokenChecker.isNotDefault(nextToken)) {
      return new ParseResult<LinkedList<AbstractBastStatement>>(null, null);
    }
    ParseResult<AbstractBastStatement> switchLabel =
        switchLabel(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = switchLabel.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    LinkedList<AbstractBastStatement> list = null;
    list = add(list, switchLabel.value);
    while (TokenChecker.isCase(nextToken) || TokenChecker.isDefault(nextToken)) {
      switchLabel = switchLabel(lexer, data, currentTokenAndHistory);
      list = add(list, switchLabel.value);
      currentTokenAndHistory = switchLabel.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }

    return new ParseResult<LinkedList<AbstractBastStatement>>(list, currentTokenAndHistory);
  }



  protected ParseResult<AbstractBastStatement> switchStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // switch ParExpression { SwitchBlockStatementGroups }
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);

    TokenChecker.expectSwitch(nextToken);
    currentTokenAndHistory = nextToken;
    final TokenAndHistory[] tokens = { currentTokenAndHistory, null, null, null, null, null };
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectLeftParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[1] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    ParseResult<AbstractBastExpr> condition =
        parExpression(lexer, data, currentTokenAndHistory, false);
    currentTokenAndHistory = condition.currentTokenAndHistory;
    tokens[2] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    assert (condition.value != null);
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectLeftBrace(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[3] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    ParseResult<LinkedList<AbstractBastStatement>> switchGroups =
        switchBlockStatementGroups(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = switchGroups.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectRightBrace(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[4] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    BastSwitch switchStmt = new BastSwitch(tokens, condition.value, switchGroups.value, null);
    return new ParseResult<AbstractBastStatement>(switchStmt, currentTokenAndHistory);
  }

  protected ParseResult<AbstractBastStatement> synchronizedStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // synchronized ParExpression Block
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectSynchronized(nextToken);
    currentTokenAndHistory = nextToken;
    final TokenAndHistory[] tokens = { currentTokenAndHistory, null, null };
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectLeftParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[1] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    ParseResult<AbstractBastExpr> expr = parExpression(lexer, data, currentTokenAndHistory, false);
    currentTokenAndHistory = expr.currentTokenAndHistory;
    tokens[2] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    ParseResult<BastBlock> block = block(lexer, data, currentTokenAndHistory, false);
    BastSynchronizedBlock sync = new BastSynchronizedBlock(tokens, expr.value, block.value);
    return new ParseResult<AbstractBastStatement>(sync, block.currentTokenAndHistory);
  }

  protected ParseResult<AbstractBastStatement> tryStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // try Block ( Catches | [Catches] Finally )
    // try ResourceSpecification Block [Catches] [Finally]
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectTry(nextToken);
    currentTokenAndHistory = nextToken;
    TokenAndHistory[] tokens = new TokenAndHistory[3];
    tokens[0] = currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<LinkedList<BastDeclaration>> resources =
        new ParseResult<LinkedList<BastDeclaration>>(null, null);
    if (TokenChecker.isLeftParenthesis(nextToken)) {
      // try ResourceSpecification Block [Catches] [Finally]
      resources = resourceSpecification(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = resources.currentTokenAndHistory;
      tokens[1] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    }
    ParseResult<BastBlock> block = block(lexer, data, currentTokenAndHistory, false);
    currentTokenAndHistory = block.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<LinkedList<BastCatchClause>> catches =
        new ParseResult<LinkedList<BastCatchClause>>(null, null);
    if (TokenChecker.isCatch(nextToken)) {
      catches = catches(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = catches.currentTokenAndHistory;
    }

    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    ParseResult<BastBlock> finallyBlock = new ParseResult<BastBlock>(null, null);
    if (TokenChecker.isFinally(nextToken)) {
      currentTokenAndHistory = nextToken;
      finallyBlock = block(lexer, data, currentTokenAndHistory, false);
      assert (finallyBlock.value != null);
      tokens[2] = currentTokenAndHistory;
      currentTokenAndHistory = finallyBlock.currentTokenAndHistory;
    }

    BastTryStmt stmt =
        new BastTryStmt(tokens, block.value, catches.value, finallyBlock.value, resources.value);
    return new ParseResult<AbstractBastStatement>(stmt, currentTokenAndHistory);
  }



  /**
   * Type.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<BastType> type(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // Java 7:
    // BasicType {[]}
    // ReferenceType {[]}

    // Identifier [TypeArguments]{ . Identifier [TypeArguments]} {[]}
    // BasicType
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    int typeConstant = -1;
    TokenAndHistory[] tokens = null;
    switch (((JavaToken) nextToken.token).type) {
      case BYTE:
        currentTokenAndHistory = nextToken;
        typeConstant = TagConstants.TYPE_BYTE;
        break;
      case SHORT:
        currentTokenAndHistory = nextToken;
        typeConstant = TagConstants.TYPE_SHORT;
        break;
      case CHAR:
        currentTokenAndHistory = nextToken;
        typeConstant = TagConstants.TYPE_CHAR;
        break;
      case INT:
        currentTokenAndHistory = nextToken;
        typeConstant = TagConstants.TYPE_INT;
        break;
      case LONG:
        currentTokenAndHistory = nextToken;
        typeConstant = TagConstants.TYPE_LONG;
        break;
      case FLOAT:
        currentTokenAndHistory = nextToken;
        typeConstant = TagConstants.TYPE_FLOAT;
        break;
      case DOUBLE:
        currentTokenAndHistory = nextToken;
        typeConstant = TagConstants.TYPE_DOUBLE;
        break;
      case BOOLEAN:
        currentTokenAndHistory = nextToken;
        typeConstant = TagConstants.TYPE_BOOL;
        break;
      case VOID:
        currentTokenAndHistory = nextToken;
        tokens = new TokenAndHistory[1];
        tokens[0] = currentTokenAndHistory;
        return new ParseResult<BastType>(new BastBasicType(tokens, TagConstants.TYPE_VOID),
            currentTokenAndHistory);
      case IDENTIFIER:
        return parseClassType(lexer, data, inputTokenAndHistory);
      default:
        // Do nothing
    }

    if (typeConstant != -1) {
      tokens = new TokenAndHistory[1];
      tokens[0] = currentTokenAndHistory;
      BastType type = new BastBasicType(tokens, typeConstant);
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
      int count = 0;
      ArrayList<TokenAndHistory> additionalTokens = new ArrayList<>();
      while (TokenChecker.isLeftBracket(nextToken) && TokenChecker.isRightBracket(nextnextToken)) {
        additionalTokens.add(nextToken);
        currentTokenAndHistory = nextToken.setFlushed();
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        additionalTokens.add(nextToken);
        currentTokenAndHistory = nextToken.setFlushed();
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        nextnextToken = lexer.nextToken(data, nextToken);
        count++;
      }
      if (count != 0) {
        tokens = new TokenAndHistory[2];
        tokens[1] = new TokenAndHistory(new ListToken(additionalTokens));
        currentTokenAndHistory = currentTokenAndHistory.setFlushed();
        type = new BastArrayType(tokens, type, null, count);
      }
      return new ParseResult<BastType>(type, currentTokenAndHistory);
    }
    return new ParseResult<BastType>(null, null);
  }

  private ParseResult<BastType> parseClassType(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;

    ParseResult<BastClassType> type = classType(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = type.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
    int count = 0;
    ArrayList<TokenAndHistory> additionalTokens = new ArrayList<>();
    while (TokenChecker.isLeftBracket(nextToken) && TokenChecker.isRightBracket(nextnextToken)) {
      additionalTokens.add(nextToken);
      currentTokenAndHistory = nextToken.setFlushed();
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      additionalTokens.add(nextToken);
      currentTokenAndHistory = nextToken.setFlushed();
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      nextnextToken = lexer.nextToken(data, nextToken);
      count++;

    }
    if (count != 0) {
      TokenAndHistory[] tokenstmp = new TokenAndHistory[2];
      tokenstmp[1] = new TokenAndHistory(new ListToken(additionalTokens));
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      return new ParseResult<BastType>(new BastArrayType(tokenstmp, type.value, null, count),
          currentTokenAndHistory);
    } else {
      return new ParseResult<BastType>(type.value, currentTokenAndHistory);
    }
  }

  /**
   * Type argument.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @param wildcard the wildcard
   * @return the parses the result
   */
  public ParseResult<BastTypeArgument> typeArgument(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory, boolean wildcard) {
    // TypeArgument:
    // Type
    // ? [( extends |super ) Type]
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;

    int extendsType = -1;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokens = new TokenAndHistory[4];
    ParseResult<BastType> typeRes = new ParseResult<BastType>(null, null);
    if (TokenChecker.isQuestionmark(nextToken) && wildcard == true) {
      currentTokenAndHistory = nextToken;
      tokens[1] = currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      if (TokenChecker.isExtends(nextToken)) {
        extendsType = BastTypeArgument.EXTENDS_TYPE;
      } else if (TokenChecker.isSuper(nextToken)) {
        extendsType = BastTypeArgument.SUPER_TYPE;
      }
      if (extendsType != -1) {
        currentTokenAndHistory = nextToken;
        typeRes = type(lexer, data, currentTokenAndHistory);
        tokens[2] = currentTokenAndHistory;
        currentTokenAndHistory = typeRes.currentTokenAndHistory;

      } else {
        extendsType = BastTypeArgument.QUESTION_TYPE;
      }
    } else {
      typeRes = type(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = typeRes.currentTokenAndHistory;
    }

    return new ParseResult<BastTypeArgument>(
        new BastTypeArgument(tokens, extendsType, typeRes.value), currentTokenAndHistory);
  }



  private ParseResult<LinkedList<BastType>> typeArgumentAsTypeList(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory, boolean wildcard) {
    // TypeArguments:
    // < TypeArgument {, TypeArgument} >
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectLess(nextToken);
    currentTokenAndHistory = nextToken;
    final TokenAndHistory backupToken = currentTokenAndHistory;
    LinkedList<BastType> list = null;

    ParseResult<BastTypeArgument> typeParameter =
        typeArgument(lexer, data, currentTokenAndHistory, wildcard);
    currentTokenAndHistory = typeParameter.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    list = add(list, typeParameter.value);
    typeParameter.value.info.tokens[0] = backupToken;
    while (TokenChecker.isComma(nextToken)) {
      currentTokenAndHistory = nextToken;
      typeParameter = typeArgument(lexer, data, currentTokenAndHistory, wildcard);
      list = add(list, typeParameter.value);
      currentTokenAndHistory = typeParameter.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }

    TokenChecker.expectGreater(nextToken);
    currentTokenAndHistory = nextToken;
    typeParameter.value.info.tokens[3] = currentTokenAndHistory;

    return new ParseResult<LinkedList<BastType>>(list, currentTokenAndHistory);
  }



  private ParseResult<LinkedList<BastTypeArgument>> typeArgumentList(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory, boolean wildcard,
      boolean allowDiamond) {
    // >TypeArguments:
    // < TypeArgument {, TypeArgument} >
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectLess(nextToken);
    currentTokenAndHistory = nextToken;
    final TokenAndHistory backupToken = currentTokenAndHistory;
    LinkedList<BastTypeArgument> list = null;
    ParseResult<BastTypeArgument> typeParameter = new ParseResult<BastTypeArgument>(null, null);

    if (allowDiamond) {
      TokenAndHistory[] tokens = new TokenAndHistory[4];
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
      if (TokenChecker.isGreater(nextToken)) {
        tokens[0] = currentTokenAndHistory;
        tokens[3] = nextToken;
        typeParameter = new ParseResult<BastTypeArgument>(new BastTypeArgument(tokens, -1, null),
            currentTokenAndHistory);
        list = add(list, typeParameter.value);
        currentTokenAndHistory = nextToken;
        return new ParseResult<LinkedList<BastTypeArgument>>(list, currentTokenAndHistory);
      }
    }
    typeParameter = typeArgument(lexer, data, currentTokenAndHistory, wildcard);
    currentTokenAndHistory = typeParameter.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    list = add(list, typeParameter.value);
    typeParameter.value.info.tokens[0] = backupToken;
    while (TokenChecker.isComma(nextToken)) {
      currentTokenAndHistory = nextToken;

      typeParameter = typeArgument(lexer, data, currentTokenAndHistory, wildcard);
      list = add(list, typeParameter.value);
      currentTokenAndHistory = typeParameter.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    switch (((JavaToken) nextToken.token).type) {
      case GREATER:
        currentTokenAndHistory = nextToken;
        typeParameter.value.info.tokens[3] = currentTokenAndHistory;
        return new ParseResult<LinkedList<BastTypeArgument>>(list, currentTokenAndHistory);
      case RBRACE:
        throw new SyntaxError("Unfinished statement.", ((JavaToken) nextToken.token));
      default:
        return new ParseResult<LinkedList<BastTypeArgument>>(null, null);
    }
  }

  private ParseResult<LinkedList<BastType>> typeBound(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // Bound:
    // Type {& Type}
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    LinkedList<BastType> list = null;
    ParseResult<BastType> typeRes = type(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = typeRes.currentTokenAndHistory;
    list = add(list, typeRes.value);
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    while (TokenChecker.isAnd(nextToken)) {
      currentTokenAndHistory = nextToken;
      TokenAndHistory tmp = nextToken;
      typeRes = type(lexer, data, currentTokenAndHistory);
      if (typeRes.value.info.tokens == null) {
        typeRes.value.info.tokens = new TokenAndHistory[1];
      }
      typeRes.value.info.tokens[0] = tmp;
      currentTokenAndHistory = typeRes.currentTokenAndHistory;
      list = add(list, typeRes.value);
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    return new ParseResult<LinkedList<BastType>>(list, currentTokenAndHistory);
  }



  private ParseResult<AbstractBastExternalDecl> typeDeclaration(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // TypeDeclaration:
    // ClassOrInterfaceDeclaration
    // ;
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isSemicolon(nextToken)) {
      currentTokenAndHistory = nextToken;
      BastEmptyDeclaration emptyDecl = new BastEmptyDeclaration(null);
      emptyDecl.info.tokensAfter.add(currentTokenAndHistory);
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      return new ParseResult<AbstractBastExternalDecl>(emptyDecl, currentTokenAndHistory);
    }
    return classOrInterfaceDeclaration(lexer, data, currentTokenAndHistory);
  }

  private ParseResult<LinkedList<AbstractBastExternalDecl>> typeDeclarationList(
      final JavaLexer lexer, final FileData data, final TokenAndHistory inputTokenAndHistory) {
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    LinkedList<AbstractBastExternalDecl> list = null;
    ParseResult<AbstractBastExternalDecl> typeDeclaration =
        typeDeclaration(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = typeDeclaration.currentTokenAndHistory;
    while (typeDeclaration.value != null) {
      list = add(list, typeDeclaration.value);
      typeDeclaration = typeDeclaration(lexer, data, currentTokenAndHistory);
      if (typeDeclaration.value != null) {
        currentTokenAndHistory = typeDeclaration.currentTokenAndHistory;
      }
    }
    return new ParseResult<LinkedList<AbstractBastExternalDecl>>(list, currentTokenAndHistory);
  }

  private ParseResult<LinkedList<BastType>> typeList(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // TypeList:
    // Type , Type
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.lessNotExpected(nextToken);

    ParseResult<BastType> typeRes = type(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = typeRes.currentTokenAndHistory;
    if (typeRes.value == null) {
      throw new SyntaxError("Invalid type.", ((JavaToken) nextToken.token));
    }
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    LinkedList<BastType> list = null;
    list = add(list, typeRes.value);
    while (TokenChecker.isComma(nextToken)) {
      currentTokenAndHistory = nextToken;
      typeRes = type(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = typeRes.currentTokenAndHistory;
      list = add(list, typeRes.value);
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    return new ParseResult<LinkedList<BastType>>(list, currentTokenAndHistory);
  }

  private ParseResult<BastTypeParameter> typeParameter(final JavaLexer lexer, final FileData data,
      final TokenAndHistory inputTokenAndHistory) {
    // TypeParameter:
    // Identifier [extends Bound]
    // Bound:
    // Type {& Type}
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory[] tokens = new TokenAndHistory[3];
    ParseResult<LinkedList<BastType>> list = new ParseResult<LinkedList<BastType>>(null, null);
    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);

    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isExtends(nextToken)) {
      currentTokenAndHistory = nextToken;
      tokens[1] = currentTokenAndHistory;
      list = typeBound(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = list.currentTokenAndHistory;
    }

    return new ParseResult<BastTypeParameter>(
        new BastTypeParameter(tokens, nameRes.value, list.value), currentTokenAndHistory);
  }

  private ParseResult<LinkedList<BastTypeParameter>> typeParameters(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // TypeParameters:
    // < TypeParameter {, TypeParameter} >
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectLess(nextToken);
    currentTokenAndHistory = nextToken;
    TokenAndHistory backupToken = currentTokenAndHistory;
    ParseResult<BastTypeParameter> typeParameter =
        typeParameter(lexer, data, currentTokenAndHistory);
    typeParameter.value.info.tokens[0] = backupToken;
    currentTokenAndHistory = typeParameter.currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    LinkedList<BastTypeParameter> list = null;
    list = add(list, typeParameter.value);
    while (TokenChecker.isComma(nextToken)) {
      currentTokenAndHistory = nextToken;
      typeParameter = typeParameter(lexer, data, currentTokenAndHistory);
      list = add(list, typeParameter.value);
      currentTokenAndHistory = typeParameter.currentTokenAndHistory;
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    TokenChecker.expectGreater(nextToken);
    currentTokenAndHistory = nextToken;
    typeParameter.value.info.tokens[2] = currentTokenAndHistory;

    return new ParseResult<LinkedList<BastTypeParameter>>(list, currentTokenAndHistory);
  }

  private ParseResult<AbstractBastDeclarator> variableDeclarator(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // VariableDeclarator:
    // Identifier VariableDeclaratorRest
    // VariableDeclaratorRest:
    // {[]} [ = VariableInitializer ]
    // VariableInitializer:
    // ArrayInitializer
    // Expression
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    BastIdentDeclarator identDecl = null;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.enumNotExpected(nextToken);
    ParseResult<BastNameIdent> nameRes = identifier(lexer, data, currentTokenAndHistory);

    currentTokenAndHistory = nameRes.currentTokenAndHistory;
    if (nameRes.value == null) {
      return new ParseResult<AbstractBastDeclarator>(null, null);
    }
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    int count = 0;
    if (TokenChecker.isLeftBracket(nextToken)) {
      TokenAndHistory nextnextToken = lexer.nextToken(data, nextToken);
      while (TokenChecker.isLeftBracket(nextToken) && TokenChecker.isRightBracket(nextnextToken)) {
        currentTokenAndHistory = nextnextToken;
        nextToken = lexer.nextToken(data, currentTokenAndHistory);
        nextnextToken = lexer.nextToken(data, nextToken);
        count++;
      }
    }
    TokenAndHistory[] tokens = new TokenAndHistory[1];
    TokenAndHistory[] tokenstmp = new TokenAndHistory[1];
    if (count != 0) {
      tokenstmp[0] = currentTokenAndHistory;
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    if (TokenChecker.isEqual(nextToken)) {
      tokens[0] = nextToken;
    }

    if (count != 0) {

      identDecl = new BastIdentDeclarator(tokens, nameRes.value, null,
          new BastArrayDeclarator(tokenstmp, null, null, count));
    } else {
      identDecl = new BastIdentDeclarator(tokens, nameRes.value, null, null);
    }
    ParseResult<AbstractBastInitializer> init =
        new ParseResult<AbstractBastInitializer>(null, null);
    if (TokenChecker.isEqual(nextToken)) {
      currentTokenAndHistory = nextToken;
      init = variableInitializer(lexer, data, currentTokenAndHistory);
      identDecl.setInitializer(init.value);
      currentTokenAndHistory = init.currentTokenAndHistory;
    }
    return new ParseResult<AbstractBastDeclarator>(identDecl, currentTokenAndHistory);
  }

  private ParseResult<LinkedList<AbstractBastDeclarator>> variableDeclarators(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // VariableDeclarators:
    // VariableDeclarator { , VariableDeclarator }
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    LinkedList<AbstractBastDeclarator> list = null;
    ParseResult<AbstractBastDeclarator> stmt =
        variableDeclarator(lexer, data, currentTokenAndHistory);
    currentTokenAndHistory = stmt.currentTokenAndHistory;
    if (stmt.value != null) {
      list = add(list, stmt.value);
    } else {
      return new ParseResult<LinkedList<AbstractBastDeclarator>>(list, currentTokenAndHistory);
    }
    LinkedList<TokenAndHistory> additionalTokens = new LinkedList<>();
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    while (TokenChecker.isComma(nextToken)) {
      currentTokenAndHistory = nextToken;
      additionalTokens.add(currentTokenAndHistory);
      currentTokenAndHistory = currentTokenAndHistory.setFlushed();
      stmt = variableDeclarator(lexer, data, currentTokenAndHistory);
      if (stmt.value != null) {
        list = add(list, stmt.value);
      }
      currentTokenAndHistory = stmt.currentTokenAndHistory;

      nextToken = lexer.nextToken(data, currentTokenAndHistory);
    }
    return new ParseResult<LinkedList<AbstractBastDeclarator>>(list, currentTokenAndHistory,
        additionalTokens);

  }

  /**
   * Variable initializer.
   *
   * @param lexer the lexer
   * @param data the data
   * @param inputTokenAndHistory the input token and history
   * @return the parses the result
   */
  public ParseResult<AbstractBastInitializer> variableInitializer(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // VariableInitializer:
    // ArrayInitializer
    // Expression
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    if (TokenChecker.isLeftBrace(nextToken)) {
      ParseResult<BastListInitializer> tmp = arrayInitializer(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = tmp.currentTokenAndHistory;
      return new ParseResult<AbstractBastInitializer>(tmp.value, tmp.currentTokenAndHistory);
    } else {
      ParseResult<AbstractBastExpr> expr =
          expressionbastAsgnExpr(lexer, data, currentTokenAndHistory);
      currentTokenAndHistory = expr.currentTokenAndHistory;
      return new ParseResult<AbstractBastInitializer>(new BastExprInitializer(null, expr.value),
          expr.currentTokenAndHistory);
    }
  }

  protected ParseResult<AbstractBastStatement> whileStatement(final JavaLexer lexer,
      final FileData data, final TokenAndHistory inputTokenAndHistory) {
    // while ParExpression Statement
    TokenAndHistory currentTokenAndHistory = inputTokenAndHistory;
    TokenAndHistory nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenAndHistory[] tokens = new TokenAndHistory[4];
    TokenChecker.expectWhile(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[0] = currentTokenAndHistory;
    nextToken = lexer.nextToken(data, currentTokenAndHistory);
    TokenChecker.expectLeftParenthesis(nextToken);
    currentTokenAndHistory = nextToken;
    tokens[1] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    ParseResult<AbstractBastExpr> condition =
        parExpression(lexer, data, currentTokenAndHistory, false);
    currentTokenAndHistory = condition.currentTokenAndHistory;
    tokens[2] = currentTokenAndHistory;
    currentTokenAndHistory = currentTokenAndHistory.setFlushed();
    ParseResult<AbstractBastStatement> ifPart = statement(lexer, data, currentTokenAndHistory);
    BastWhileStatement whileStmt = new BastWhileStatement(tokens, condition.value, ifPart.value,
        BastWhileStatement.TYPE_WHILE);
    return new ParseResult<AbstractBastStatement>(whileStmt, ifPart.currentTokenAndHistory);
  }

}
