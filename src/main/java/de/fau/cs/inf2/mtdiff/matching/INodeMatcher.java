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

package de.fau.cs.inf2.mtdiff.matching;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;

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
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunctionIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunctionParameterDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastGoto;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastImportDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIncludeStmt;
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
import de.fau.cs.inf2.cas.common.bast.nodes.BastPointer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.nodes.BastRealConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastRegularDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastReturn;
import de.fau.cs.inf2.cas.common.bast.nodes.BastShift;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStorageClassSpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStringConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStructDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStructDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStructMember;
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
import de.fau.cs.inf2.cas.common.bast.type.BastPointerType;
import de.fau.cs.inf2.cas.common.bast.type.BastStructOrUnionSpecifierType;
import de.fau.cs.inf2.cas.common.bast.type.BastTypeName;

@SuppressWarnings("PMD.ExcessivePublicCount")
interface INodeMatcher {
  
  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAnd first, BastAnd second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastArrayRef first, BastArrayRef second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAsgnExpr first, BastAsgnExpr second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastBlock first, BastBlock second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCase first, BastCase second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCastExpr first, BastCastExpr second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCharConst first, BastCharConst second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCmp first, BastCmp second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCondAnd first, BastCondAnd second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCondExpr first, BastCondExpr second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCondOr first, BastCondOr second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCall first, BastCall second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastFunction first, BastFunction second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastIf first, BastIf second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastIntConst first, BastIntConst second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastGoto first, BastGoto second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastNameIdent first, BastNameIdent second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastMultiExpr first, BastMultiExpr second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastOr first, BastOr second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastParameter first, BastParameter second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastProgram first, BastProgram second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastRealConst first, BastRealConst second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastReturn first, BastReturn second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastShift first, BastShift second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastStringConst first, BastStringConst second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastStructDecl first, BastStructDecl second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastStructMember first, BastStructMember second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAccess first, BastAccess second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastSwitch first, BastSwitch second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastXor first, BastXor second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastDeclaration first, BastDeclaration second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastTypeSpecifier first, BastTypeSpecifier second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastIdentDeclarator first, BastIdentDeclarator second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastExprList first, BastExprList second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastForStmt first, BastForStmt second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastIncrExpr first, BastIncrExpr second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastExprInitializer first, BastExprInitializer second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastPointer first, BastPointer second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastFunctionIdentDeclarator first, BastFunctionIdentDeclarator second,
      float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastFunctionParameterDeclarator first,
      BastFunctionParameterDeclarator second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastParameterList first, BastParameterList second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastArrayDeclarator first, BastArrayDeclarator second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastRegularDeclarator first, BastRegularDeclarator second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastLabelStmt first, BastLabelStmt second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastDecrExpr first, BastDecrExpr second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastWhileStatement first, BastWhileStatement second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastContinue first, BastContinue second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastBreak first, BastBreak second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastEnumMember first, BastEnumMember second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastUnaryExpr first, BastUnaryExpr second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastBoolConst first, BastBoolConst second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastNullConst first, BastNullConst second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAnnotation first, BastAnnotation second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAnnotationDecl first, BastAnnotationDecl second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAnnotationElemValue first, BastAnnotationElemValue second,
      float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAnnotationMethod first, BastAnnotationMethod second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastPackage first, BastPackage second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastImportDeclaration first, BastImportDeclaration second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastSynchronizedBlock first, BastSynchronizedBlock second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastTryStmt first, BastTryStmt second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastInterfaceDecl first, BastInterfaceDecl second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastThrowStmt first, BastThrowStmt second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAssertStmt first, BastAssertStmt second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastThis first, BastThis second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastSuper first, BastSuper second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastCatchClause first, BastCatchClause second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastInstanceOf first, BastInstanceOf second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastEnumDecl first, BastEnumDecl second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastAdditiveExpr first, BastAdditiveExpr second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastTemplateSpecifier first, BastTemplateSpecifier second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastDefault first, BastDefault second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastSwitchCaseGroup first, BastSwitchCaseGroup second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastEmptyStmt first, BastEmptyStmt second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastArrayType first, BastArrayType second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastBasicType first, BastBasicType second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastEnumSpec first, BastEnumSpec second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastPointerType first, BastPointerType second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastStructOrUnionSpecifierType first, BastStructOrUnionSpecifierType second,
      float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastTypeName first, BastTypeName second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastClassDecl first, BastClassDecl second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastTypeQualifier first, BastTypeQualifier second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastClassType first, BastClassType second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastNew first, BastNew second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastIncludeStmt first, BastIncludeStmt second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastStorageClassSpecifier first, BastStorageClassSpecifier second,
      float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastStructDeclarator first, BastStructDeclarator second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastListInitializer first, BastListInitializer second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastTypeParameter first, BastTypeParameter second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastEmptyDeclaration first, BastEmptyDeclaration second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastClassConst first, BastClassConst second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(BastTypeArgument first, BastTypeArgument second, float similarity);

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(AresBlock first, AresBlock second, float similarity);
}
