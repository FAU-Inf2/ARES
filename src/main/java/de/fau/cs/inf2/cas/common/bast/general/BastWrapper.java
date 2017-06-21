package de.fau.cs.inf2.cas.common.bast.general;

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
import de.fau.cs.inf2.cas.common.bast.nodes.INode;
import de.fau.cs.inf2.cas.common.bast.type.BastArrayType;
import de.fau.cs.inf2.cas.common.bast.type.BastBasicType;
import de.fau.cs.inf2.cas.common.bast.type.BastClassType;
import de.fau.cs.inf2.cas.common.bast.type.BastPointerType;
import de.fau.cs.inf2.cas.common.bast.type.BastStructOrUnionSpecifierType;
import de.fau.cs.inf2.cas.common.bast.type.BastTypeName;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class BastWrapper implements INodeWrapper {
  private final AbstractBastNode node;
  private List<INode> nodeDirectChildren = new LinkedList<INode>();

  /**
   * Instantiates a new i node wrapper.
   *
   * @param node the node
   */
  public BastWrapper(AbstractBastNode node) {
    this.node = node;
  }

  /**
   * Instantiates a new i node wrapper.
   *
   * @param node the node
   * @param wrapperMap the wrapper map
   * @param childrenMap the children map
   */
  BastWrapper(AbstractBastNode node, HashMap<AbstractBastNode, INode> wrapperMap,
      HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> childrenMap) {
    this.node = node;
    if (wrapperMap != null && childrenMap != null) {
      List<AbstractBastNode> children = childrenMap.get(node);
      if (children != null) {
        for (AbstractBastNode child : children) {
          INode childNode = wrapperMap.get(child);
          if (childNode == null) {
            childNode = new BastWrapper((AbstractBastNode) child, wrapperMap, childrenMap);
            wrapperMap.put((AbstractBastNode) child, childNode);
          }
          nodeDirectChildren.add(childNode);
        }
      }
    }
  }

  /**
   * Gets the node.
   *
   * @return the node
   */
  public AbstractBastNode getNode() {
    assert (node != null);
    return node;
  }

  
  /**
   * Gets the id.
   *
   * @return the id
   */
  public int getId() {
    return node.nodeId;
  }

  
  /**
   * Gets the type wrapped.
   *
   * @return the type wrapped
   */
  public int getTypeWrapped() {
    return node.getTag();
  }

  
  /**
   * Gets the children wrapped.
   *
   * @return the children wrapped
   */
  public List<INode> getChildrenWrapped() {
    return nodeDirectChildren;
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return node.toString();
  }

  
  /**
   * Gets the label.
   *
   * @return the label
   */
  @Override
  public String getLabel() {
    return getValue(node);
  }

  /**
   * Gets the value.
   *
   * @param node the node
   * @return the value
   */
  public String getValue(AbstractBastNode node) {
    switch (node.getTag()) {
      case BastNameIdent.TAG:
        return ((BastNameIdent) node).name;
      case BastTypeQualifier.TAG:
        return String.valueOf(((BastTypeQualifier) node).type);
      case BastFunction.TAG:
        return ((BastFunction) node).name;
      case BastBlock.TAG:
        return String.valueOf(((BastBlock) node).isStatic);
      case BastForStmt.TAG:
        return String.valueOf(((BastForStmt) node).type);
      case BastParameterList.TAG:
        return String.valueOf(((BastParameterList) node).open);
      case BastParameter.TAG:
        return ((BastParameter) node).name;
      case BastStringConst.TAG:
        return ((BastStringConst) node).value;
      case BastBoolConst.TAG:
        return String.valueOf(((BastBoolConst) node).value);
      case BastAsgnExpr.TAG:
        return String.valueOf(((BastAsgnExpr) node).operation);
      case BastIntConst.TAG:
        if (((BastIntConst) node).fitsInLong) {
          return BigInteger.valueOf(((BastIntConst) node).value).toString();
        } else {
          return ((BastIntConst) node).bigValue.toString();
        }
      case BastRealConst.TAG:
        if (((BastRealConst) node).fitsInDouble) {
          return String.valueOf(((BastRealConst) node).value).toString();
        } else {
          return ((BastRealConst) node).actualValue;
        }
      case BastNew.TAG:
        return String.valueOf(((BastNew) node).newType);
      case BastArrayType.TAG:
        return String.valueOf(((BastArrayType) node).dimensionNumber);
      case BastAccess.TAG:
        return String.valueOf(((BastAccess) node).type);
      case BastBasicType.TAG:
        return String.valueOf(((BastBasicType) node).typeTag);
      case BastUnaryExpr.TAG:
        return String.valueOf(((BastUnaryExpr) node).type);
      case BastListInitializer.TAG:
        return String.valueOf(((BastListInitializer) node).open);
      case BastCastExpr.TAG:
        return String.valueOf(((BastCastExpr) node).type);
      case BastMultiExpr.TAG:
        return String.valueOf(((BastMultiExpr) node).type);
      case BastAdditiveExpr.TAG:
        return String.valueOf(((BastAdditiveExpr) node).type);
      case BastCmp.TAG:
        return String.valueOf(((BastCmp) node).operation);
      case BastWhileStatement.TAG:
        return String.valueOf(((BastWhileStatement) node).type);
      case BastCharConst.TAG:
        return String.valueOf(((BastCharConst) node).value);
      case BastShift.TAG:
        return String.valueOf(((BastShift) node).type);
      case BastStructDecl.TAG:
        return ((BastStructDecl) node).name;
      case BastStructMember.TAG:
        return ((BastStructMember) node).name;
      case BastArrayDeclarator.TAG:
        return String.valueOf(((BastArrayDeclarator) node).dimensions);
      case BastLabelStmt.TAG:
        return ((BastLabelStmt) node).name;
      case BastImportDeclaration.TAG:
        return String.valueOf(((BastImportDeclaration) node).isStatic)
            + String.valueOf(((BastImportDeclaration) node).isPackage);
      case BastStorageClassSpecifier.TAG:
        return String.valueOf(((BastStorageClassSpecifier) node).type);

      case BastAnd.TAG:
      case BastIf.TAG:
      case BastInstanceOf.TAG:
      case BastDeclaration.TAG:
      case BastClassDecl.TAG:
      case BastTypeParameter.TAG:
      case BastClassType.TAG:
      case BastIdentDeclarator.TAG:
      case BastCall.TAG:
      case BastArrayRef.TAG:
      case BastAnnotation.TAG:
      case BastTypeSpecifier.TAG:
      case BastTypeArgument.TAG:
      case BastExprInitializer.TAG:
      case BastProgram.TAG:
      case BastFunctionParameterDeclarator.TAG:
      case BastIncrExpr.TAG:
      case BastExprList.TAG:
      case BastDecrExpr.TAG:
      case BastCase.TAG:
      case BastCondAnd.TAG:
      case BastCondExpr.TAG:
      case BastCondOr.TAG:
      case BastGoto.TAG:
      case BastOr.TAG:
      case BastReturn.TAG:
      case BastSwitch.TAG:
      case BastXor.TAG:
      case BastPointer.TAG:
      case BastFunctionIdentDeclarator.TAG:
      case BastRegularDeclarator.TAG:
      case BastContinue.TAG:
      case BastBreak.TAG:
      case BastEnumMember.TAG:
      case BastNullConst.TAG:
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
      case BastEnumDecl.TAG:
      case BastTemplateSpecifier.TAG:
      case BastDefault.TAG:
      case BastSwitchCaseGroup.TAG:
      case BastEmptyStmt.TAG:
      case BastEnumSpec.TAG:
      case BastPointerType.TAG:
      case BastStructOrUnionSpecifierType.TAG:
      case BastTypeName.TAG:
      case BastIncludeStmt.TAG:
      case BastStructDeclarator.TAG:
      case BastEmptyDeclaration.TAG:
      case BastClassConst.TAG:
        return "";
      default:
        System.out.println(node.getClass());
        assert (false);
    }
    return null;
  }

  
  /**
   * Checks if is leaf.
   *
   * @return true, if is leaf
   */
  @Override
  public boolean isLeaf() {
    if (nodeDirectChildren == null || nodeDirectChildren.size() == 0) {
      return true;
    }
    return false;
  }

}
