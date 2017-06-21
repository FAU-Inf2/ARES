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
import de.fau.cs.inf2.cas.ares.bast.nodes.AresCaseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPluginClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;

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
 * An Adapter class for a {@see NodeSimilarityCalculator}, which simplifies handling of the
 * different abstract syntax tree classes.
 *
 */
public class SimilarityAdapter {

  private INodeSimilarityCalculator simCalc = null;

  /**
   * todo.
   * 
   * <p>Create a new SimilarityAdapter.
   * 
   * @param simCalc The {@see NodeSimilarityCalculator} which is used to calculate the similarity
   *        between the nodes passed to this adapter.
   *
   */
  public SimilarityAdapter(final INodeSimilarityCalculator simCalc) {
    this.simCalc = simCalc;
  }

  /**
   * todo.
   * Calculate the similarity between the given nodes using the similarity calculator passed to the
   * used instance of this class.
   * 
   *
   * @param first the first
   * @param second the second
   * @return The similarity between the two nodes, where 1.0f denotes maximal similarity.
   * @throws NotALeafException todo
   */
  @SuppressWarnings("PMD.ExcessiveMethodLength")
  public float similarity(final AbstractBastNode first,
      final AbstractBastNode second) throws NotALeafException {
    if (first.getTag() == second.getTag()) {
      float result = 0.0f;
      switch (first.getTag()) {
        case BastAnd.TAG:
          result = simCalc.similarity((BastAnd) first, (BastAnd) second);
          break;
        case BastArrayRef.TAG:
          result = simCalc.similarity((BastArrayRef) first, (BastArrayRef) second);
          break;
        case BastAsgnExpr.TAG:
          result = simCalc.similarity((BastAsgnExpr) first, (BastAsgnExpr) second);
          break;
        case BastBlock.TAG:
          result = simCalc.similarity((BastBlock) first, (BastBlock) second);
          break;
        case BastCase.TAG:
          result = simCalc.similarity((BastCase) first, (BastCase) second);
          break;
        case BastCastExpr.TAG:
          result = simCalc.similarity((BastCastExpr) first, (BastCastExpr) second);
          break;
        case BastCharConst.TAG:
          result = simCalc.similarity((BastCharConst) first, (BastCharConst) second);
          break;
        case BastCmp.TAG:
          result = simCalc.similarity((BastCmp) first, (BastCmp) second);
          break;
        case BastCondAnd.TAG:
          result = simCalc.similarity((BastCondAnd) first, (BastCondAnd) second);
          break;
        case BastCondExpr.TAG:
          result = simCalc.similarity((BastCondExpr) first, (BastCondExpr) second);
          break;
        case BastCondOr.TAG:
          result = simCalc.similarity((BastCondOr) first, (BastCondOr) second);
          break;
        case BastCall.TAG:
          result = simCalc.similarity((BastCall) first, (BastCall) second);
          break;
        case BastFunction.TAG:
          result = simCalc.similarity((BastFunction) first, (BastFunction) second);
          break;
        case BastIf.TAG:
          result = simCalc.similarity((BastIf) first, (BastIf) second);
          break;
        case BastIntConst.TAG:
          result = simCalc.similarity((BastIntConst) first, (BastIntConst) second);
          break;
        case BastGoto.TAG:
          result = simCalc.similarity((BastGoto) first, (BastGoto) second);
          break;
        case BastNameIdent.TAG:
          result = simCalc.similarity((BastNameIdent) first, (BastNameIdent) second);
          break;
        case BastMultiExpr.TAG:
          result = simCalc.similarity((BastMultiExpr) first, (BastMultiExpr) second);
          break;
        case BastOr.TAG:
          result = simCalc.similarity((BastOr) first, (BastOr) second);
          break;
        case BastParameter.TAG:
          result = simCalc.similarity((BastParameter) first, (BastParameter) second);
          break;
        case BastProgram.TAG:
          result = simCalc.similarity((BastProgram) first, (BastProgram) second);
          break;
        case BastRealConst.TAG:
          result = simCalc.similarity((BastRealConst) first, (BastRealConst) second);
          break;
        case BastReturn.TAG:
          result = simCalc.similarity((BastReturn) first, (BastReturn) second);
          break;
        case BastShift.TAG:
          result = simCalc.similarity((BastShift) first, (BastShift) second);
          break;
        case BastStringConst.TAG:
          result = simCalc.similarity((BastStringConst) first, (BastStringConst) second);
          break;
        case BastStructDecl.TAG:
          result = simCalc.similarity((BastStructDecl) first, (BastStructDecl) second);
          break;
        case BastAccess.TAG:
          result = simCalc.similarity((BastAccess) first, (BastAccess) second);
          break;
        case BastSwitch.TAG:
          result = simCalc.similarity((BastSwitch) first, (BastSwitch) second);
          break;
        case BastXor.TAG:
          result = simCalc.similarity((BastXor) first, (BastXor) second);
          break;
        case BastDeclaration.TAG:
          result = simCalc.similarity((BastDeclaration) first, (BastDeclaration) second);
          break;
        case BastTypeSpecifier.TAG:
          result = simCalc.similarity((BastTypeSpecifier) first, (BastTypeSpecifier) second);
          break;
        case BastIdentDeclarator.TAG:
          result = simCalc.similarity((BastIdentDeclarator) first, (BastIdentDeclarator) second);
          break;
        case BastExprList.TAG:
          result = simCalc.similarity((BastExprList) first, (BastExprList) second);
          break;
        case BastForStmt.TAG:
          result = simCalc.similarity((BastForStmt) first, (BastForStmt) second);
          break;
        case BastIncrExpr.TAG:
          result = simCalc.similarity((BastIncrExpr) first, (BastIncrExpr) second);
          break;
        case BastExprInitializer.TAG:
          result = simCalc.similarity((BastExprInitializer) first, (BastExprInitializer) second);
          break;
        case BastPointer.TAG:
          result = simCalc.similarity((BastPointer) first, (BastPointer) second);
          break;
        case BastFunctionIdentDeclarator.TAG:
          result = simCalc.similarity((BastFunctionIdentDeclarator) first,
              (BastFunctionIdentDeclarator) second);
          break;
        case BastFunctionParameterDeclarator.TAG:
          result = simCalc.similarity((BastFunctionParameterDeclarator) first,
              (BastFunctionParameterDeclarator) second);
          break;
        case BastParameterList.TAG:
          result = simCalc.similarity((BastParameterList) first, (BastParameterList) second);
          break;
        case BastArrayDeclarator.TAG:
          result = simCalc.similarity((BastArrayDeclarator) first, (BastArrayDeclarator) second);
          break;
        case BastRegularDeclarator.TAG:
          result =
              simCalc.similarity((BastRegularDeclarator) first, (BastRegularDeclarator) second);
          break;
        case BastLabelStmt.TAG:
          result = simCalc.similarity((BastLabelStmt) first, (BastLabelStmt) second);
          break;
        case BastDecrExpr.TAG:
          result = simCalc.similarity((BastDecrExpr) first, (BastDecrExpr) second);
          break;
        case BastWhileStatement.TAG:
          result = simCalc.similarity((BastWhileStatement) first, (BastWhileStatement) second);
          break;
        case BastContinue.TAG:
          result = simCalc.similarity((BastContinue) first, (BastContinue) second);
          break;
        case BastBreak.TAG:
          result = simCalc.similarity((BastBreak) first, (BastBreak) second);
          break;
        case BastEnumMember.TAG:
          result = simCalc.similarity((BastEnumMember) first, (BastEnumMember) second);
          break;
        case BastUnaryExpr.TAG:
          result = simCalc.similarity((BastUnaryExpr) first, (BastUnaryExpr) second);
          break;
        case BastBoolConst.TAG:
          result = simCalc.similarity((BastBoolConst) first, (BastBoolConst) second);
          break;
        case BastNullConst.TAG:
          result = simCalc.similarity((BastNullConst) first, (BastNullConst) second);
          break;
        case BastAnnotation.TAG:
          result = simCalc.similarity((BastAnnotation) first, (BastAnnotation) second);
          break;
        case BastAnnotationDecl.TAG:
          result = simCalc.similarity((BastAnnotationDecl) first, (BastAnnotationDecl) second);
          break;
        case BastAnnotationElemValue.TAG:
          result =
              simCalc.similarity((BastAnnotationElemValue) first, (BastAnnotationElemValue) second);
          break;
        case BastAnnotationMethod.TAG:
          result = simCalc.similarity((BastAnnotationMethod) first, (BastAnnotationMethod) second);
          break;
        case BastPackage.TAG:
          result = simCalc.similarity((BastPackage) first, (BastPackage) second);
          break;
        case BastImportDeclaration.TAG:
          result =
              simCalc.similarity((BastImportDeclaration) first, (BastImportDeclaration) second);
          break;
        case BastSynchronizedBlock.TAG:
          result =
              simCalc.similarity((BastSynchronizedBlock) first, (BastSynchronizedBlock) second);
          break;
        case BastTryStmt.TAG:
          result = simCalc.similarity((BastTryStmt) first, (BastTryStmt) second);
          break;
        case BastInterfaceDecl.TAG:
          result = simCalc.similarity((BastInterfaceDecl) first, (BastInterfaceDecl) second);
          break;
        case BastThrowStmt.TAG:
          result = simCalc.similarity((BastThrowStmt) first, (BastThrowStmt) second);
          break;
        case BastAssertStmt.TAG:
          result = simCalc.similarity((BastAssertStmt) first, (BastAssertStmt) second);
          break;
        case BastThis.TAG:
          result = simCalc.similarity((BastThis) first, (BastThis) second);
          break;
        case BastSuper.TAG:
          result = simCalc.similarity((BastSuper) first, (BastSuper) second);
          break;
        case BastCatchClause.TAG:
          result = simCalc.similarity((BastCatchClause) first, (BastCatchClause) second);
          break;
        case BastInstanceOf.TAG:
          result = simCalc.similarity((BastInstanceOf) first, (BastInstanceOf) second);
          break;
        case BastEnumDecl.TAG:
          result = simCalc.similarity((BastEnumDecl) first, (BastEnumDecl) second);
          break;
        case BastAdditiveExpr.TAG:
          result = simCalc.similarity((BastAdditiveExpr) first, (BastAdditiveExpr) second);
          break;
        case BastTemplateSpecifier.TAG:
          result =
              simCalc.similarity((BastTemplateSpecifier) first, (BastTemplateSpecifier) second);
          break;
        case BastDefault.TAG:
          result = simCalc.similarity((BastDefault) first, (BastDefault) second);
          break;
        case BastSwitchCaseGroup.TAG:
          result = simCalc.similarity((BastSwitchCaseGroup) first, (BastSwitchCaseGroup) second);
          break;
        case BastEmptyStmt.TAG:
          result = simCalc.similarity((BastEmptyStmt) first, (BastEmptyStmt) second);
          break;
        case BastArrayType.TAG:
          result = simCalc.similarity((BastArrayType) first, (BastArrayType) second);
          break;
        case BastBasicType.TAG:
          result = simCalc.similarity((BastBasicType) first, (BastBasicType) second);
          break;
        case BastEnumSpec.TAG:
          result = simCalc.similarity((BastEnumSpec) first, (BastEnumSpec) second);
          break;
        case BastPointerType.TAG:
          result = simCalc.similarity((BastPointerType) first, (BastPointerType) second);
          break;
        case BastStructOrUnionSpecifierType.TAG:
          result = simCalc.similarity((BastStructOrUnionSpecifierType) first,
              (BastStructOrUnionSpecifierType) second);
          break;
        case BastTypeName.TAG:
          result = simCalc.similarity((BastTypeName) first, (BastTypeName) second);
          break;
        case BastClassDecl.TAG:
          result = simCalc.similarity((BastClassDecl) first, (BastClassDecl) second);
          break;
        case BastTypeQualifier.TAG:
          result = simCalc.similarity((BastTypeQualifier) first, (BastTypeQualifier) second);
          break;
        case BastClassType.TAG:
          result = simCalc.similarity((BastClassType) first, (BastClassType) second);
          break;
        case BastNew.TAG:
          result = simCalc.similarity((BastNew) first, (BastNew) second);
          break;
        case BastIncludeStmt.TAG:
          result = simCalc.similarity((BastIncludeStmt) first, (BastIncludeStmt) second);
          break;
        case BastStorageClassSpecifier.TAG:
          result = simCalc.similarity((BastStorageClassSpecifier) first,
              (BastStorageClassSpecifier) second);
          break;
        case BastStructDeclarator.TAG:
          result = simCalc.similarity((BastStructDeclarator) first, (BastStructDeclarator) second);
          break;
        case BastListInitializer.TAG:
          result = simCalc.similarity((BastListInitializer) first, (BastListInitializer) second);
          break;
        case BastTypeParameter.TAG:
          result = simCalc.similarity((BastTypeParameter) first, (BastTypeParameter) second);
          break;
        case BastEmptyDeclaration.TAG:
          result = simCalc.similarity((BastEmptyDeclaration) first, (BastEmptyDeclaration) second);
          break;
        case BastClassConst.TAG:
          result = simCalc.similarity((BastClassConst) first, (BastClassConst) second);
          break;
        case BastTypeArgument.TAG:
          result = simCalc.similarity((BastTypeArgument) first, (BastTypeArgument) second);
          break;
        case AresBlock.TAG:
          result = simCalc.similarity((AresBlock) first, (AresBlock) second);
          break;
        case AresPatternClause.TAG:
          result =
              simCalc.similarity((AresPatternClause) first, (AresPatternClause) second);
          break;
        case AresWildcard.TAG:
          result = simCalc.similarity((AresWildcard) first, (AresWildcard) second);
          break;
        case AresPluginClause.TAG:
          result = simCalc.similarity((AresPluginClause) first, (AresPluginClause) second);
          break;
        case AresUseStmt.TAG:
          result = simCalc.similarity((AresUseStmt) first, (AresUseStmt) second);
          break;
        case AresChoiceStmt.TAG:
          result = simCalc.similarity((AresChoiceStmt) first, (AresChoiceStmt) second);
          break;
        case AresCaseStmt.TAG:
          result = simCalc.similarity((AresCaseStmt) first, (AresCaseStmt) second);
          break;
        default:
          assert (false);
          break;
      }
      return result;

    }
    return 0.0f;
  }
}
