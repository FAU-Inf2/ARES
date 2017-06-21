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

package de.fau.cs.inf2.cas.common.bast.visitors;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractAresStatement;
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
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlockComment;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBoolConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBreak;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCase;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCatchClause;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCharConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassKeySpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCmp;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondAnd;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondOr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastContinue;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDecrExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDefault;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDummyToken;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEmptyDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEmptyStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEnumDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEnumMember;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEnumSpec;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprList;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprWater;
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
import de.fau.cs.inf2.cas.common.bast.nodes.BastLineComment;
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
public interface IBastVisitor extends IAstVisitor {

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AbstractAresStatement node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastAccess node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastAdditiveExpr node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastAnd node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastAnnotation node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastAnnotationDecl node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastAnnotationElemValue node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastAnnotationMethod node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastArrayDeclarator node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastArrayRef node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastArrayType node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastAsgnExpr node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastAssertStmt node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastBasicType node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastBlock node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastBlockComment node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastBoolConst node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastBreak node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastCall node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastCase node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastCastExpr node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastCatchClause node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastCharConst node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastClassConst node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastClassDecl node);

  /**
   * Visit.
   *
   * @param bastClassKeySpecifier the bast class key specifier
   */
  public void visit(BastClassKeySpecifier bastClassKeySpecifier);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastClassType node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastCmp node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastCondAnd node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastCondExpr node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastCondOr node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastContinue node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastDeclaration node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastDecrExpr node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastDefault node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastDummyToken node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastEmptyDeclaration node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastEmptyStmt node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastEnumDecl node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastEnumMember node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastEnumSpec node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastExprInitializer node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastExprList node);

  /**
   * Visit. Only needed for CParser.
   *
   * @param node the node
   */
  public void visit(BastExprWater node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastForStmt node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastFunction node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastFunctionIdentDeclarator node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastFunctionParameterDeclarator node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastGoto node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastIdentDeclarator node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastIf node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastImportDeclaration node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastIncludeStmt node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastIncrExpr node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastInstanceOf node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastIntConst node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastInterfaceDecl node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastLabelStmt node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastLineComment node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastListInitializer node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastMultiExpr node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastNameIdent node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastNew node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastNullConst node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastOr node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastPackage node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastParameter node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastParameterList node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastPointer node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastPointerType node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastProgram node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastRealConst node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastRegularDeclarator node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastReturn node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastShift node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastStorageClassSpecifier node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastStringConst node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastStructDecl node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastStructDeclarator node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastStructMember node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastStructOrUnionSpecifierType node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastSuper node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastSwitch node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastSwitchCaseGroup node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastSynchronizedBlock node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastTemplateSpecifier node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastThis node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastThrowStmt node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastTryStmt node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastTypeArgument node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastTypeName node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastTypeParameter node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastTypeQualifier node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastTypeSpecifier node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastUnaryExpr node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastWhileStatement node);

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastXor node);
}
