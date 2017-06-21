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

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
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

/**
 * An Adapter class for a {@see NodeMatcher}, which simplifies handling of the different abstract
 * syntax tree classes.
 *
 */
public class MatchingAdapter {

  private INodeMatcher matcher = null;

  /**
   * todo.
   * 
   * <p>Create a new MatchingAdapter.
   * 
   * @param matcher the matcher
   */
  public MatchingAdapter(final INodeMatcher matcher) {
    this.matcher = matcher;

  }

  /**
   * todo.
   * Determine whether two nodes match by using the {@see NodeMatcher} passed to this instance of
   * this class.
   * 
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return <code>true</code> if the two given nodes match
   */
  @SuppressWarnings("PMD.ExcessiveMethodLength")
  public boolean match(final AbstractBastNode first,
      final AbstractBastNode second, final float similarity) {
    if (first.getTag() == second.getTag()) {
      boolean result = false;

      if (matcher instanceof InnerMatcher) {
        result = ((InnerMatcher) matcher).match((AbstractBastNode) first,
            (AbstractBastNode) second, similarity);
        return result;
      } else {
        switch (first.getTag()) {
          case BastAnd.TAG:
            result = matcher.match((BastAnd) first, (BastAnd) second, similarity);
            break;
          case BastArrayRef.TAG:
            result = matcher.match((BastArrayRef) first, (BastArrayRef) second, similarity);
            break;
          case BastAsgnExpr.TAG:
            result = matcher.match((BastAsgnExpr) first, (BastAsgnExpr) second, similarity);
            break;
          case BastBlock.TAG:
            result = matcher.match((BastBlock) first, (BastBlock) second, similarity);
            break;
          case BastCase.TAG:
            result = matcher.match((BastCase) first, (BastCase) second, similarity);
            break;
          case BastCastExpr.TAG:
            result = matcher.match((BastCastExpr) first, (BastCastExpr) second, similarity);
            break;
          case BastCharConst.TAG:
            result = matcher.match((BastCharConst) first, (BastCharConst) second, similarity);
            break;
          case BastCmp.TAG:
            result = matcher.match((BastCmp) first, (BastCmp) second, similarity);
            break;
          case BastCondAnd.TAG:
            result = matcher.match((BastCondAnd) first, (BastCondAnd) second, similarity);
            break;
          case BastCondExpr.TAG:
            result = matcher.match((BastCondExpr) first, (BastCondExpr) second, similarity);
            break;
          case BastCondOr.TAG:
            result = matcher.match((BastCondOr) first, (BastCondOr) second, similarity);
            break;
          case BastCall.TAG:
            result = matcher.match((BastCall) first, (BastCall) second, similarity);
            break;
          case BastFunction.TAG:
            result = matcher.match((BastFunction) first, (BastFunction) second, similarity);
            break;
          case BastIf.TAG:
            result = matcher.match((BastIf) first, (BastIf) second, similarity);
            break;
          case BastIntConst.TAG:
            result = matcher.match((BastIntConst) first, (BastIntConst) second, similarity);
            break;
          case BastGoto.TAG:
            result = matcher.match((BastGoto) first, (BastGoto) second, similarity);
            break;
          case BastNameIdent.TAG:
            result = matcher.match((BastNameIdent) first, (BastNameIdent) second, similarity);
            break;
          case BastMultiExpr.TAG:
            result = matcher.match((BastMultiExpr) first, (BastMultiExpr) second, similarity);
            break;
          case BastOr.TAG:
            result = matcher.match((BastOr) first, (BastOr) second, similarity);
            break;
          case BastParameter.TAG:
            result = matcher.match((BastParameter) first, (BastParameter) second, similarity);
            break;
          case BastProgram.TAG:
            result = matcher.match((BastProgram) first, (BastProgram) second, similarity);
            break;
          case BastRealConst.TAG:
            result = matcher.match((BastRealConst) first, (BastRealConst) second, similarity);
            break;
          case BastReturn.TAG:
            result = matcher.match((BastReturn) first, (BastReturn) second, similarity);
            break;
          case BastShift.TAG:
            result = matcher.match((BastShift) first, (BastShift) second, similarity);
            break;
          case BastStringConst.TAG:
            result = matcher.match((BastStringConst) first, (BastStringConst) second, similarity);
            break;
          case BastStructDecl.TAG:
            result = matcher.match((BastStructDecl) first, (BastStructDecl) second, similarity);
            break;
          case BastAccess.TAG:
            result = matcher.match((BastAccess) first, (BastAccess) second, similarity);
            break;
          case BastSwitch.TAG:
            result = matcher.match((BastSwitch) first, (BastSwitch) second, similarity);
            break;
          case BastXor.TAG:
            result = matcher.match((BastXor) first, (BastXor) second, similarity);
            break;
          case BastDeclaration.TAG:
            result = matcher.match((BastDeclaration) first, (BastDeclaration) second, similarity);
            break;
          case BastTypeSpecifier.TAG:
            result =
                matcher.match((BastTypeSpecifier) first, (BastTypeSpecifier) second, similarity);
            break;
          case BastIdentDeclarator.TAG:
            result = matcher.match((BastIdentDeclarator) first, (BastIdentDeclarator) second,
                similarity);
            break;
          case BastExprList.TAG:
            result = matcher.match((BastExprList) first, (BastExprList) second, similarity);
            break;
          case BastForStmt.TAG:
            result = matcher.match((BastForStmt) first, (BastForStmt) second, similarity);
            break;
          case BastIncrExpr.TAG:
            result = matcher.match((BastIncrExpr) first, (BastIncrExpr) second, similarity);
            break;
          case BastExprInitializer.TAG:
            result = matcher.match((BastExprInitializer) first, (BastExprInitializer) second,
                similarity);
            break;
          case BastPointer.TAG:
            result = matcher.match((BastPointer) first, (BastPointer) second, similarity);
            break;
          case BastFunctionIdentDeclarator.TAG:
            result = matcher.match((BastFunctionIdentDeclarator) first,
                (BastFunctionIdentDeclarator) second, similarity);
            break;
          case BastFunctionParameterDeclarator.TAG:
            result = matcher.match((BastFunctionParameterDeclarator) first,
                (BastFunctionParameterDeclarator) second, similarity);
            break;
          case BastParameterList.TAG:
            result =
                matcher.match((BastParameterList) first, (BastParameterList) second, similarity);
            break;
          case BastArrayDeclarator.TAG:
            result = matcher.match((BastArrayDeclarator) first, (BastArrayDeclarator) second,
                similarity);
            break;
          case BastRegularDeclarator.TAG:
            result = matcher.match((BastRegularDeclarator) first, (BastRegularDeclarator) second,
                similarity);
            break;
          case BastLabelStmt.TAG:
            result = matcher.match((BastLabelStmt) first, (BastLabelStmt) second, similarity);
            break;
          case BastDecrExpr.TAG:
            result = matcher.match((BastDecrExpr) first, (BastDecrExpr) second, similarity);
            break;
          case BastWhileStatement.TAG:
            result =
                matcher.match((BastWhileStatement) first, (BastWhileStatement) second, similarity);
            break;
          case BastContinue.TAG:
            result = matcher.match((BastContinue) first, (BastContinue) second, similarity);
            break;
          case BastBreak.TAG:
            result = matcher.match((BastBreak) first, (BastBreak) second, similarity);
            break;
          case BastEnumMember.TAG:
            result = matcher.match((BastEnumMember) first, (BastEnumMember) second, similarity);
            break;
          case BastUnaryExpr.TAG:
            result = matcher.match((BastUnaryExpr) first, (BastUnaryExpr) second, similarity);
            break;
          case BastBoolConst.TAG:
            result = matcher.match((BastBoolConst) first, (BastBoolConst) second, similarity);
            break;
          case BastNullConst.TAG:
            result = matcher.match((BastNullConst) first, (BastNullConst) second, similarity);
            break;
          case BastAnnotation.TAG:
            result = matcher.match((BastAnnotation) first, (BastAnnotation) second, similarity);
            break;
          case BastAnnotationDecl.TAG:
            result =
                matcher.match((BastAnnotationDecl) first, (BastAnnotationDecl) second, similarity);
            break;
          case BastAnnotationElemValue.TAG:
            result = matcher.match((BastAnnotationElemValue) first,
                (BastAnnotationElemValue) second, similarity);
            break;
          case BastAnnotationMethod.TAG:
            result = matcher.match((BastAnnotationMethod) first, (BastAnnotationMethod) second,
                similarity);
            break;
          case BastPackage.TAG:
            result = matcher.match((BastPackage) first, (BastPackage) second, similarity);
            break;
          case BastImportDeclaration.TAG:
            result = matcher.match((BastImportDeclaration) first, (BastImportDeclaration) second,
                similarity);
            break;
          case BastSynchronizedBlock.TAG:
            result = matcher.match((BastSynchronizedBlock) first, (BastSynchronizedBlock) second,
                similarity);
            break;
          case BastTryStmt.TAG:
            result = matcher.match((BastTryStmt) first, (BastTryStmt) second, similarity);
            break;
          case BastInterfaceDecl.TAG:
            result =
                matcher.match((BastInterfaceDecl) first, (BastInterfaceDecl) second, similarity);
            break;
          case BastThrowStmt.TAG:
            result = matcher.match((BastThrowStmt) first, (BastThrowStmt) second, similarity);
            break;
          case BastAssertStmt.TAG:
            result = matcher.match((BastAssertStmt) first, (BastAssertStmt) second, similarity);
            break;
          case BastThis.TAG:
            result = matcher.match((BastThis) first, (BastThis) second, similarity);
            break;
          case BastSuper.TAG:
            result = matcher.match((BastSuper) first, (BastSuper) second, similarity);
            break;
          case BastCatchClause.TAG:
            result = matcher.match((BastCatchClause) first, (BastCatchClause) second, similarity);
            break;
          case BastInstanceOf.TAG:
            result = matcher.match((BastInstanceOf) first, (BastInstanceOf) second, similarity);
            break;
          case BastEnumDecl.TAG:
            result = matcher.match((BastEnumDecl) first, (BastEnumDecl) second, similarity);
            break;
          case BastAdditiveExpr.TAG:
            result = matcher.match((BastAdditiveExpr) first, (BastAdditiveExpr) second, similarity);
            break;
          case BastTemplateSpecifier.TAG:
            result = matcher.match((BastTemplateSpecifier) first, (BastTemplateSpecifier) second,
                similarity);
            break;
          case BastDefault.TAG:
            result = matcher.match((BastDefault) first, (BastDefault) second, similarity);
            break;
          case BastSwitchCaseGroup.TAG:
            result = matcher.match((BastSwitchCaseGroup) first, (BastSwitchCaseGroup) second,
                similarity);
            break;
          case BastEmptyStmt.TAG:
            result = matcher.match((BastEmptyStmt) first, (BastEmptyStmt) second, similarity);
            break;
          case BastArrayType.TAG:
            result = matcher.match((BastArrayType) first, (BastArrayType) second, similarity);
            break;
          case BastBasicType.TAG:
            result = matcher.match((BastBasicType) first, (BastBasicType) second, similarity);
            break;
          case BastEnumSpec.TAG:
            result = matcher.match((BastEnumSpec) first, (BastEnumSpec) second, similarity);
            break;
          case BastPointerType.TAG:
            result = matcher.match((BastPointerType) first, (BastPointerType) second, similarity);
            break;
          case BastStructOrUnionSpecifierType.TAG:
            result = matcher.match((BastStructOrUnionSpecifierType) first,
                (BastStructOrUnionSpecifierType) second, similarity);
            break;
          case BastTypeName.TAG:
            result = matcher.match((BastTypeName) first, (BastTypeName) second, similarity);
            break;
          case BastClassDecl.TAG:
            result = matcher.match((BastClassDecl) first, (BastClassDecl) second, similarity);
            break;
          case BastTypeQualifier.TAG:
            result =
                matcher.match((BastTypeQualifier) first, (BastTypeQualifier) second, similarity);
            break;
          case BastClassType.TAG:
            result = matcher.match((BastClassType) first, (BastClassType) second, similarity);
            break;
          case BastNew.TAG:
            result = matcher.match((BastNew) first, (BastNew) second, similarity);
            break;
          case BastIncludeStmt.TAG:
            result = matcher.match((BastIncludeStmt) first, (BastIncludeStmt) second, similarity);
            break;
          case BastStorageClassSpecifier.TAG:
            result = matcher.match((BastStorageClassSpecifier) first,
                (BastStorageClassSpecifier) second, similarity);
            break;
          case BastStructDeclarator.TAG:
            result = matcher.match((BastStructDeclarator) first, (BastStructDeclarator) second,
                similarity);
            break;
          case BastListInitializer.TAG:
            result = matcher.match((BastListInitializer) first, (BastListInitializer) second,
                similarity);
            break;
          case BastTypeParameter.TAG:
            result =
                matcher.match((BastTypeParameter) first, (BastTypeParameter) second, similarity);
            break;
          case BastEmptyDeclaration.TAG:
            result = matcher.match((BastEmptyDeclaration) first, (BastEmptyDeclaration) second,
                similarity);
            break;
          case BastClassConst.TAG:
            result = matcher.match((BastClassConst) first, (BastClassConst) second, similarity);
            break;
          case BastTypeArgument.TAG:
            result = matcher.match((BastTypeArgument) first, (BastTypeArgument) second, similarity);
            break;
          case AresBlock.TAG:
            result = matcher.match((AresBlock) first, (AresBlock) second, similarity);
            break;
          default:
            assert (false);
            break;
        }
        return result;
      }

    }
    return false;
  }
}
