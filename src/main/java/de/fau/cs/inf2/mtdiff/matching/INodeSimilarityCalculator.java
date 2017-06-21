package de.fau.cs.inf2.mtdiff.matching;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresCaseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPluginClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;

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
interface INodeSimilarityCalculator {
  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastAnd first, BastAnd second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastArrayRef first, BastArrayRef second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastAsgnExpr first, BastAsgnExpr second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastBlock first, BastBlock second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastCase first, BastCase second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastCastExpr first, BastCastExpr second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastCharConst first, BastCharConst second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastCmp first, BastCmp second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastCondAnd first, BastCondAnd second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastCondExpr first, BastCondExpr second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastCondOr first, BastCondOr second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastCall first, BastCall second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastFunction first, BastFunction second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastIf first, BastIf second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastIntConst first, BastIntConst second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastGoto first, BastGoto second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastNameIdent first, BastNameIdent second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastMultiExpr first, BastMultiExpr second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastOr first, BastOr second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastParameter first, BastParameter second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastProgram first, BastProgram second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastRealConst first, BastRealConst second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastReturn first, BastReturn second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastShift first, BastShift second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastStringConst first, BastStringConst second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastStructDecl first, BastStructDecl second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastStructMember first, BastStructMember second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastAccess first, BastAccess second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastSwitch first, BastSwitch second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastXor first, BastXor second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastDeclaration first, BastDeclaration second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastTypeSpecifier first, BastTypeSpecifier second)
      throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastIdentDeclarator first, BastIdentDeclarator second)
      throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastExprList first, BastExprList second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastForStmt first, BastForStmt second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastIncrExpr first, BastIncrExpr second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastExprInitializer first, BastExprInitializer second)
      throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastPointer first, BastPointer second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastFunctionIdentDeclarator first, BastFunctionIdentDeclarator second)
      throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastFunctionParameterDeclarator first,
      BastFunctionParameterDeclarator second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastParameterList first, BastParameterList second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastArrayDeclarator first, BastArrayDeclarator second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastRegularDeclarator first, BastRegularDeclarator second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastLabelStmt first, BastLabelStmt second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastDecrExpr first, BastDecrExpr second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastWhileStatement first, BastWhileStatement second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastContinue first, BastContinue second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastBreak first, BastBreak second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastEnumMember first, BastEnumMember second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastUnaryExpr first, BastUnaryExpr second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastBoolConst first, BastBoolConst second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastNullConst first, BastNullConst second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastAnnotation first, BastAnnotation second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastAnnotationDecl first, BastAnnotationDecl second)
      throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastAnnotationElemValue first, BastAnnotationElemValue second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastAnnotationMethod first, BastAnnotationMethod second)
      throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastPackage first, BastPackage second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastImportDeclaration first, BastImportDeclaration second)
      throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastSynchronizedBlock first, BastSynchronizedBlock second)
      throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastTryStmt first, BastTryStmt second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastInterfaceDecl first, BastInterfaceDecl second)
      throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastThrowStmt first, BastThrowStmt second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastAssertStmt first, BastAssertStmt second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastThis first, BastThis second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastSuper first, BastSuper second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastCatchClause first, BastCatchClause second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastInstanceOf first, BastInstanceOf second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastEnumDecl first, BastEnumDecl second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastAdditiveExpr first, BastAdditiveExpr second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastTemplateSpecifier first, BastTemplateSpecifier second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastDefault first, BastDefault second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastSwitchCaseGroup first, BastSwitchCaseGroup second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastEmptyStmt first, BastEmptyStmt second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastArrayType first, BastArrayType second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastBasicType first, BastBasicType second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastEnumSpec first, BastEnumSpec second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastPointerType first, BastPointerType second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastStructOrUnionSpecifierType first,
      BastStructOrUnionSpecifierType second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastTypeName first, BastTypeName second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastClassDecl first, BastClassDecl second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastTypeQualifier first, BastTypeQualifier second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastClassType first, BastClassType second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastNew first, BastNew second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(BastIncludeStmt first, BastIncludeStmt second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastStorageClassSpecifier first, BastStorageClassSpecifier second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastStructDeclarator first, BastStructDeclarator second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastListInitializer first, BastListInitializer second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastTypeParameter first, BastTypeParameter second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastEmptyDeclaration first, BastEmptyDeclaration second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastClassConst first, BastClassConst second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastTypeArgument first, BastTypeArgument second);

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(AresBlock first, AresBlock second) throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(AresPatternClause first, AresPatternClause second)
      throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(AresWildcard first, AresWildcard second)
      throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(AresChoiceStmt first, AresChoiceStmt second)
      throws NotALeafException;
  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(AresCaseStmt first, AresCaseStmt second)
      throws NotALeafException;
  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(AresPluginClause first, AresPluginClause second)
      throws NotALeafException;

  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   * @throws NotALeafException the not a leaf exception
   */
  public float similarity(AresUseStmt first, AresUseStmt second) throws NotALeafException;
}
