package de.fau.cs.inf2.cas.common.bast.diff;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresCaseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
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

import java.math.BigInteger;

/**
 * A convienence class for retrieving the values of nodes of an abstract syntax tree.
 *
 */
public final class ValueRetriever {

  /**
   * todo.
   * 
   * <p>Retrieves the value of the specified node.
   *
   * @param node the node
   * @return the value
   */
  public ValueContainer getValue(final AbstractBastNode node) {
    ValueContainer result = ValueContainer.NULL_VALUE;
    switch (node.getTag()) {
      case AresUseStmt.TAG:
      case AresCaseStmt.TAG:
      case BastAnd.TAG:
      case BastArrayRef.TAG:
      case BastBlockComment.TAG:
      case BastCase.TAG:
      case BastCondAnd.TAG:
      case BastCondExpr.TAG:
      case BastCondOr.TAG:
      case BastCall.TAG:
      case BastFunction.TAG:
      case BastIf.TAG:
      case BastGoto.TAG:
      case BastLineComment.TAG:
      case BastOr.TAG:
      case BastProgram.TAG:
      case BastReturn.TAG:
      case BastSwitch.TAG:
      case BastXor.TAG:
      case BastDeclaration.TAG:
      case BastTypeSpecifier.TAG:
      case BastIdentDeclarator.TAG:
      case BastExprList.TAG:
      case BastIncrExpr.TAG:
      case BastExprInitializer.TAG:
      case BastPointer.TAG:
      case BastFunctionIdentDeclarator.TAG:
      case BastFunctionParameterDeclarator.TAG:
      case BastRegularDeclarator.TAG:
      case BastLabelStmt.TAG:
      case BastDecrExpr.TAG:
      case BastContinue.TAG:
      case BastBreak.TAG:
      case BastEnumMember.TAG:
      case BastNullConst.TAG:
      case BastAnnotation.TAG:
      case BastAnnotationDecl.TAG:
      case BastAnnotationElemValue.TAG:
      case BastAnnotationMethod.TAG:
      case BastPackage.TAG:
      case BastSynchronizedBlock.TAG:
      case BastTryStmt.TAG:
      case BastInterfaceDecl.TAG:
      case BastThrowStmt.TAG:
      case BastAssertStmt.TAG:
      case BastThis.TAG:
      case BastSuper.TAG:
      case BastCatchClause.TAG:
      case BastInstanceOf.TAG:
      case BastEnumDecl.TAG:
      case BastTemplateSpecifier.TAG:
      case BastDefault.TAG:
      case BastSwitchCaseGroup.TAG:
      case BastEmptyStmt.TAG:
      case BastEnumSpec.TAG:
      case BastPointerType.TAG:
      case BastStructOrUnionSpecifierType.TAG:
      case BastTypeName.TAG:
      case BastClassDecl.TAG:
      case BastClassType.TAG:
      case BastIncludeStmt.TAG:
      case BastStructDeclarator.TAG:
      case BastTypeParameter.TAG:
      case BastEmptyDeclaration.TAG:
      case BastClassConst.TAG:
      case AresBlock.TAG:
      case AresPatternClause.TAG:
      case AresChoiceStmt.TAG:
      case AresWildcard.TAG:
        result = ValueContainer.NULL_VALUE;
        break;
      case BastAsgnExpr.TAG:
        result = new ValueContainer(BigInteger.valueOf(((BastAsgnExpr) node).operation));
        break;
      case BastBlock.TAG:
        result = new ValueContainer(((BastBlock) node).isStatic);
        break;
      case BastCastExpr.TAG:
        result = new ValueContainer(BigInteger.valueOf(((BastCastExpr) node).type));
        break;
      case BastCharConst.TAG:
        result = new ValueContainer(BigInteger.valueOf((int) ((BastCharConst) node).value));
        break;
      case BastCmp.TAG:
        result = new ValueContainer(BigInteger.valueOf(((BastCmp) node).operation));
        break;
      case BastTypeArgument.TAG:
        result = new ValueContainer(BigInteger.valueOf(((BastTypeArgument) node).extendsType));
        break;
      case BastIntConst.TAG:
        BigInteger bigValue;

        if (((BastIntConst) node).fitsInLong) {
          bigValue = BigInteger.valueOf(((BastIntConst) node).value);
        } else {
          bigValue = ((BastIntConst) node).bigValue;
        }

        result = new ValueContainer(bigValue);
        break;
      case BastNameIdent.TAG:
        result = new ValueContainer(((BastNameIdent) node).name);
        break;

      case BastMultiExpr.TAG:
        result = new ValueContainer(BigInteger.valueOf(((BastMultiExpr) node).type.to_int()));
        break;
      case BastParameter.TAG:
        result = new ValueContainer(((BastParameter) node).name);
        break;
      case BastRealConst.TAG:
        if (((BastRealConst) node).fitsInDouble) {
          result = new ValueContainer(((BastRealConst) node).value);
        } else {
          result = new ValueContainer(((BastRealConst) node).actualValue);
        }
        break;
      case BastShift.TAG:
        result = new ValueContainer(((BastShift) node).type);
        break;
      case BastStringConst.TAG:
        result = new ValueContainer(((BastStringConst) node).value);
        break;
      case BastStructDecl.TAG:
        result = new ValueContainer(((BastStructDecl) node).name);
        break;
      case BastAccess.TAG:
        result = new ValueContainer(BigInteger.valueOf(((BastAccess) node).type));
        break;
      case BastForStmt.TAG:
        result = new ValueContainer(BigInteger.valueOf(((BastForStmt) node).type));
        break;
      case BastParameterList.TAG:
        result = new ValueContainer(((BastParameterList) node).open);
        break;
      case BastArrayDeclarator.TAG:
        result = new ValueContainer(BigInteger.valueOf(((BastArrayDeclarator) node).dimensions));
        break;
      case BastWhileStatement.TAG:
        result = new ValueContainer(BigInteger.valueOf(((BastWhileStatement) node).type));
        break;
      case BastUnaryExpr.TAG:
        result = new ValueContainer(BigInteger.valueOf(((BastUnaryExpr) node).type));
        break;
      case BastBoolConst.TAG:
        result = new ValueContainer(((BastBoolConst) node).value);
        break;
      case BastImportDeclaration.TAG:
        result = new ValueContainer(
            ((BastImportDeclaration) node).isStatic && ((BastImportDeclaration) node).isPackage);
        break;
      case BastAdditiveExpr.TAG:
        result = new ValueContainer(BigInteger.valueOf(((BastAdditiveExpr) node).type));
        break;
      case BastArrayType.TAG:
        result = new ValueContainer(BigInteger.valueOf(((BastArrayType) node).dimensionNumber));
        break;
      case BastBasicType.TAG:
        result = new ValueContainer(BigInteger.valueOf(((BastBasicType) node).typeTag));
        break;
      case BastTypeQualifier.TAG:
        result = new ValueContainer(BigInteger.valueOf(((BastTypeQualifier) node).type));
        break;
      case BastNew.TAG:
        result = new ValueContainer(BigInteger.valueOf(((BastNew) node).newType));
        break;
      case BastStorageClassSpecifier.TAG:
        result = new ValueContainer(BigInteger.valueOf(((BastStorageClassSpecifier) node).type));
        break;
      case BastListInitializer.TAG:
        result = new ValueContainer(((BastListInitializer) node).open);
        break;
      case BastClassKeySpecifier.TAG:
        result = new ValueContainer(((BastClassKeySpecifier) node).type);
        break;
      case BastExprWater.TAG:
        result = new ValueContainer(((BastExprWater) node).content);
        break;
      default:
        System.err.print(node.getClass());
        assert (false);
        break;
    }
    return result;

  }
}
