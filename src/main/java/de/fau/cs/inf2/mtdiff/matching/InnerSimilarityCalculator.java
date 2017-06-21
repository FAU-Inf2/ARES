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

import de.fau.cs.inf2.cas.common.util.string.IStringSimilarityMeasure;
import de.fau.cs.inf2.cas.common.util.string.NGramCalculator;


@SuppressWarnings("PMD.ExcessivePublicCount")
public class InnerSimilarityCalculator implements INodeSimilarityCalculator {
  private IStringSimilarityMeasure stringSim = new NGramCalculator(2, 10, 10);

  private static final float SIMILARITY_VALUE_MISMATCH = 0.2f;

  private float notAnInnerNode() {
    return 0.0f;
  }

  private float emptyNode() {
    return 1.0f;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastAnd first, BastAnd second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastArrayRef first, BastArrayRef second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastAsgnExpr first, BastAsgnExpr second) {
    if (first.operation == second.operation) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastBlock first, BastBlock second) {
    if (first.isStatic == second.isStatic) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastCase first, BastCase second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastCastExpr first, BastCastExpr second) {
    if (first.type == second.type) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastCharConst first, BastCharConst second) {
    return notAnInnerNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastCmp first, BastCmp second) {
    if (first.operation == second.operation) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastCondAnd first, BastCondAnd second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastCondExpr first, BastCondExpr second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastCondOr first, BastCondOr second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastCall first, BastCall second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(AresPatternClause first, AresPatternClause second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastFunction first, BastFunction second) {
    if ((first.name == null) || (second.name == null)) {
      return emptyNode();
    }
    return stringSim.similarity(first.name, second.name);
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastIf first, BastIf second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastIntConst first, BastIntConst second) {
    return notAnInnerNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastGoto first, BastGoto second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastNameIdent first, BastNameIdent second) {
    return notAnInnerNode();
  }
  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastMultiExpr first, BastMultiExpr second) {
    if (first.type == second.type) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastOr first, BastOr second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastParameter first, BastParameter second) {
    if ((first.name != null) && (second.name != null)) {
      return stringSim.similarity(first.name, second.name);
    }
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastProgram first, BastProgram second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastRealConst first, BastRealConst second) {
    return notAnInnerNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastReturn first, BastReturn second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastShift first, BastShift second) {
    if (first.type == second.type) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastStringConst first, BastStringConst second) {
    return notAnInnerNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastStructDecl first, BastStructDecl second) {
    if ((first.name != null) && (second.name != null)) {
      return stringSim.similarity(first.name, second.name);
    }
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastStructMember first, BastStructMember second) {
    return stringSim.similarity(first.name, second.name);
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastAccess first, BastAccess second) {
    if (first.type == second.type) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastSwitch first, BastSwitch second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastXor first, BastXor second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastDeclaration first, BastDeclaration second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastTypeSpecifier first, BastTypeSpecifier second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastIdentDeclarator first, BastIdentDeclarator second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastExprList first, BastExprList second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastForStmt first, BastForStmt second) {
    if (first.type == second.type) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastIncrExpr first, BastIncrExpr second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastExprInitializer first, BastExprInitializer second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastPointer first, BastPointer second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastFunctionIdentDeclarator first, BastFunctionIdentDeclarator second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastFunctionParameterDeclarator first,
      BastFunctionParameterDeclarator second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastParameterList first, BastParameterList second) {
    if (first.open == second.open) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastArrayDeclarator first, BastArrayDeclarator second) {
    if (first.dimensions == second.dimensions) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastRegularDeclarator first, BastRegularDeclarator second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastLabelStmt first, BastLabelStmt second) {
    if ((first.name != null) && (second.name != null)) {
      return stringSim.similarity(first.name, second.name);
    }
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastDecrExpr first, BastDecrExpr second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastWhileStatement first, BastWhileStatement second) {
    if (first.type == second.type) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastContinue first, BastContinue second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastBreak first, BastBreak second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastEnumMember first, BastEnumMember second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastUnaryExpr first, BastUnaryExpr second) {
    if (first.type == second.type) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastBoolConst first, BastBoolConst second) {
    return notAnInnerNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastNullConst first, BastNullConst second) {
    return notAnInnerNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastAnnotation first, BastAnnotation second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastAnnotationDecl first, BastAnnotationDecl second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastAnnotationElemValue first, BastAnnotationElemValue second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastAnnotationMethod first, BastAnnotationMethod second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastPackage first, BastPackage second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastImportDeclaration first, BastImportDeclaration second) {
    if ((first.isStatic == second.isStatic) && (first.isPackage == second.isPackage)) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastSynchronizedBlock first, BastSynchronizedBlock second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastTryStmt first, BastTryStmt second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastInterfaceDecl first, BastInterfaceDecl second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastThrowStmt first, BastThrowStmt second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastAssertStmt first, BastAssertStmt second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastThis first, BastThis second) {
    return notAnInnerNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastSuper first, BastSuper second) {
    return notAnInnerNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastCatchClause first, BastCatchClause second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastInstanceOf first, BastInstanceOf second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastEnumDecl first, BastEnumDecl second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastAdditiveExpr first, BastAdditiveExpr second) {
    if (first.type == second.type) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastTemplateSpecifier first, BastTemplateSpecifier second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastDefault first, BastDefault second) {
    return notAnInnerNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastSwitchCaseGroup first, BastSwitchCaseGroup second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastEmptyStmt first, BastEmptyStmt second) {
    return notAnInnerNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastArrayType first, BastArrayType second) {
    if (first.dimensionNumber == second.dimensionNumber) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastBasicType first, BastBasicType second) {
    return notAnInnerNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastEnumSpec first, BastEnumSpec second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastPointerType first, BastPointerType second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastStructOrUnionSpecifierType first,
      BastStructOrUnionSpecifierType second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastTypeName first, BastTypeName second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastClassDecl first, BastClassDecl second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastTypeQualifier first, BastTypeQualifier second) {
    return notAnInnerNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastClassType first, BastClassType second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastNew first, BastNew second) {
    if (first.newType == second.newType) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastIncludeStmt first, BastIncludeStmt second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastStorageClassSpecifier first, BastStorageClassSpecifier second) {
    if (first.type == second.type) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastStructDeclarator first, BastStructDeclarator second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastListInitializer first, BastListInitializer second) {
    if (first.open == second.open) {
      return 1.0f;
    }
    return SIMILARITY_VALUE_MISMATCH;
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastTypeParameter first, BastTypeParameter second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastEmptyDeclaration first, BastEmptyDeclaration second) {
    return notAnInnerNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastClassConst first, BastClassConst second) {
    return notAnInnerNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(BastTypeArgument first, BastTypeArgument second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(AresBlock first, AresBlock second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  @Override
  public float similarity(AresWildcard first, AresWildcard second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  @Override
  public float similarity(AresPluginClause first, AresPluginClause second) {
    return emptyNode();
  }

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  @Override
  public float similarity(AresUseStmt first, AresUseStmt second) {
    return emptyNode();
  }
  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  @Override
  public float similarity(AresChoiceStmt first, AresChoiceStmt second) {
    return emptyNode();
  }
  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  @Override
  public float similarity(AresCaseStmt first, AresCaseStmt second) {
    return emptyNode();
  }

}
