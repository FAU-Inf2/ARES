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
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastComment;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExternalDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastInternalDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastSpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastSpecifierQualifier;
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
import de.fau.cs.inf2.cas.common.bast.type.BastType;
import de.fau.cs.inf2.cas.common.bast.type.BastTypeName;

public class AssertVisitor implements IBastVisitor {

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTypeArgument node) {
    if (node.type != null) {
      node.type.accept(this);
    }
    assert (false);
  }


  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastListInitializer node) {
    if (node.list != null) {
      for (AbstractBastInitializer init : node.list) {
        init.accept(this);
      }
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAdditiveExpr node) {
    node.left.accept(this);
    node.right.accept(this);
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastUnaryExpr node) {
    node.operand.accept(this);

    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnd node) {
    node.left.accept(this);
    node.right.accept(this);
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastArrayDeclarator node) {
    if (node.index != null) {
      node.index.accept(this);
    }
    if (node.source != null) {
      node.source.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastArrayRef node) {
    node.arrayRef.accept(this);
    if (node.indices != null) {
      for (AbstractBastExpr expr : node.indices) {
        expr.accept(this);
      }
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAsgnExpr node) {
    node.left.accept(this);
    node.right.accept(this);
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastBlock node) {
    if (node.statements != null) {
      for (AbstractBastStatement expr : node.statements) {
        expr.accept(this);
      }
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastBlockComment node) {
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCase node) {
    node.caseConstant.accept(this);
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCastExpr node) {
    node.operand.accept(this);
    if (node.castType != null) {
      node.castType.accept(this);
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCharConst node) {
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCmp node) {
    node.left.accept(this);
    node.right.accept(this);
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCondAnd node) {
    node.left.accept(this);
    node.right.accept(this);
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCondExpr node) {
    node.condition.accept(this);
    node.truePart.accept(this);
    node.falsePart.accept(this);
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCondOr node) {
    node.left.accept(this);
    node.right.accept(this);
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastDeclaration node) {

    if (node.modifiers != null) {
      for (AbstractBastSpecifier spec : node.modifiers) {
        spec.accept(this);
      }
    }

    if (node.specifierList != null) {
      for (AbstractBastSpecifier spec : node.specifierList) {
        spec.accept(this);
      }
    }
    if (node.declaratorList != null) {
      for (AbstractBastDeclarator decl : node.declaratorList) {
        decl.accept(this);
      }
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastDecrExpr node) {
    if (node.operand != null) {
      node.operand.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCall node) {
    node.function.accept(this);
    if (node.arguments != null) {
      for (AbstractBastExpr expr : node.arguments) {
        expr.accept(this);
      }
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastExprInitializer node) {
    if (node.init != null) {
      node.init.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastExprList node) {
    if (node.list != null) {
      for (AbstractBastExpr expr : node.list) {
        expr.accept(this);
      }
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastFunction node) {

    if (node.modifiers != null) {
      for (AbstractBastSpecifier spec : node.modifiers) {
        spec.accept(this);
      }
    }

    if (node.decl != null) {
      node.decl.accept(this);
    }

    if (node.specifierList != null) {
      for (AbstractBastSpecifier spec : node.specifierList) {
        spec.accept(this);
      }

    }

    if (node.declList != null) {
      for (AbstractBastExternalDecl spec : node.declList) {
        spec.accept(this);
      }

    }

    if (node.parameters != null) {
      for (BastParameter expr : node.parameters) {
        expr.accept(this);
      }
    }

    if (node.statements != null) {
      for (AbstractBastStatement expr : node.statements) {
        expr.accept(this);
      }
    }

    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastFunctionIdentDeclarator node) {
    if (node.function != null) {
      node.function.accept(this);
    }
    if (node.parameters != null) {
      for (BastNameIdent par : node.parameters) {
        par.accept(this);
      }
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastFunctionParameterDeclarator node) {
    if (node.function != null) {
      node.function.accept(this);
    }
    if (node.parameters != null) {
      node.parameters.accept(this);
    }
    if (node.pointer != null) {
      node.pointer.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastIdentDeclarator node) {
    if (node.identifier != null) {
      node.identifier.accept(this);
    }
    if (node.expression != null) {
      node.expression.accept(this);
    }
    if (node.pointer != null) {
      node.pointer.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastIf node) {
    node.condition.accept(this);
    node.ifPart.accept(this);
    if (node.elsePart != null) {
      node.elsePart.accept(this);
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastIncrExpr node) {
    if (node.operand != null) {
      node.operand.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastIntConst node) {
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastGoto node) {
    node.label.accept(this);
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastLineComment node) {
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastEmptyStmt node) {
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastEmptyDeclaration node) {
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastMultiExpr node) {
    node.left.accept(this);
    node.right.accept(this);
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastNameIdent node) {
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastOr node) {
    node.left.accept(this);
    node.right.accept(this);
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastParameter node) {
    if (node.declarator != null) {
      node.declarator.accept(this);
    }
    if (node.specifiers != null) {
      for (AbstractBastSpecifier spec : node.specifiers) {
        spec.accept(this);
      }
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastParameterList node) {
    if (node.parameters != null) {
      for (BastParameter par : node.parameters) {
        par.accept(this);
      }
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastPointer node) {
    if (node.pointer != null) {
      node.pointer.accept(this);
    }
    if (node.list != null) {
      for (AbstractBastSpecifier qual : node.list) {
        qual.accept(this);
      }
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastProgram node) {
    if (node.comments != null) {
      for (AbstractBastComment expr : node.comments) {
        expr.accept(this);
      }
    }
    if (node.functionBlocks != null) {
      for (AbstractBastExternalDecl expr : node.functionBlocks) {
        expr.accept(this);
      }
    }
    if (node.imports != null) {
      for (BastImportDeclaration expr : node.imports) {
        expr.accept(this);
      }
    }
    if (node.annotations != null) {
      for (BastAnnotation expr : node.annotations) {
        expr.accept(this);
      }
    }
    if (node.packageName != null) {
      node.packageName.accept(this);
    }

    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastRealConst node) {
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastRegularDeclarator node) {
    if (node.declaration != null) {
      node.declaration.accept(this);
    }
    if (node.expression != null) {
      node.expression.accept(this);
    }
    if (node.pointer != null) {
      node.pointer.accept(this);
    }
    if (node.init != null) {
      node.init.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastReturn node) {
    if (node.returnValue != null) {
      node.returnValue.accept(this);
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastShift node) {
    node.left.accept(this);
    node.right.accept(this);
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastStringConst node) {
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastStructDecl node) {
    if (node.members != null) {
      for (BastStructMember expr : node.members) {
        expr.accept(this);
      }
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastStructMember node) {
    node.position.accept(this);
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAccess node) {
    node.target.accept(this);
    node.member.accept(this);
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastSwitch node) {
    node.condition.accept(this);
    if (node.switchGroups != null) {
      for (AbstractBastStatement group : node.switchGroups) {
        group.accept(this);
      }
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTypeSpecifier node) {
    if (node.type != null) {
      node.type.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastXor node) {
    node.left.accept(this);
    node.right.accept(this);
    assert (false);

  }




  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastArrayType node) {
    if (node.dimensions != null) {
      for (AbstractBastExpr expr : node.dimensions) {
        expr.accept(this);
      }
    }
    if (node.type != null) {
      node.type.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastBasicType node) {
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastPointerType node) {
    if (node.type != null) {
      node.type.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastStructOrUnionSpecifierType node) {
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.list != null) {
      for (BastStructDecl decl : node.list) {
        decl.accept(this);
      }
    }
    if (node.identifier != null) {
      node.identifier.accept(this);
    }
    assert (false);
  }

  /*
   * <p>public void visit(BastStructType node){ assert (false); }
   */

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastTypeName node) {
    if (node.declarator != null) {
      node.declarator.accept(this);
    }
    if (node.specifiers != null) {
      for (AbstractBastSpecifierQualifier spec : node.specifiers) {
        spec.accept(this);
      }
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastClassDecl node) {
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.modifiers != null) {
      for (AbstractBastSpecifier modifier : node.modifiers) {
        modifier.accept(this);
      }
    }
    if (node.typeParameters != null) {
      for (BastTypeParameter typeParameter : node.typeParameters) {
        typeParameter.accept(this);
      }
    }
    if (node.extendedClass != null) {
      node.extendedClass.accept(this);
    }
    if (node.interfaces != null) {
      for (BastType inter : node.interfaces) {
        inter.accept(this);
      }
    }
    if (node.declarations != null) {
      for (AbstractBastInternalDecl decl : node.declarations) {
        decl.accept(this);
      }
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastTypeQualifier node) {
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastStructDeclarator node) {
    if (node.init != null) {
      node.init.accept(this);
    }
    if (node.decl != null) {
      node.decl.accept(this);
    }
    if (node.constant != null) {
      node.constant.accept(this);
    }
    if (node.pointer != null) {
      node.pointer.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastClassType node) {
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.typeArgumentList != null) {
      for (BastTypeArgument arg : node.typeArgumentList) {
        arg.accept(this);
      }
    }
    if (node.subClass != null) {
      node.subClass.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastIncludeStmt node) {
    if (node.list != null) {
      for (BastNameIdent name : node.list) {
        name.accept(this);
      }
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastStorageClassSpecifier node) {
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastLabelStmt node) {
    if (node.stmt != null) {
      node.stmt.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastWhileStatement node) {
    if (node.expression != null) {
      node.expression.accept(this);
    }
    if (node.statement != null) {
      node.statement.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastContinue node) {
    if (node.name != null) {
      node.name.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastBreak node) {
    if (node.name != null) {
      node.name.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastBoolConst node) {
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastNullConst node) {
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastClassConst node) {
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnnotation node) {
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.exprList != null) {
      for (AbstractBastExpr elem : node.exprList) {
        elem.accept(this);
      }
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnnotationDecl node) {
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.declarations != null) {
      for (AbstractBastExternalDecl elem : node.declarations) {
        elem.accept(this);
      }
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnnotationElemValue node) {
    if (node.qualifiedName != null) {
      node.qualifiedName.accept(this);
    }
    if (node.initList != null) {
      for (AbstractBastExpr elem : node.initList) {
        elem.accept(this);
      }
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnnotationMethod node) {
    if (node.type != null) {
      node.type.accept(this);
    }

    if (node.defaultValue != null) {
      node.defaultValue.accept(this);
    }

    if (node.declarator != null) {
      node.declarator.accept(this);
    }

    if (node.modifiers != null) {
      for (AbstractBastSpecifier elem : node.modifiers) {
        elem.accept(this);
      }
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastPackage node) {
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.annotations != null) {
      for (BastAnnotation elem : node.annotations) {
        elem.accept(this);
      }
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastImportDeclaration node) {
    if (node.name != null) {
      node.name.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastSynchronizedBlock node) {
    if (node.expr != null) {
      node.expr.accept(this);
    }
    if (node.block != null) {
      node.block.accept(this);
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTryStmt node) {
    if (node.block != null) {
      node.block.accept(this);
    }
    if (node.catches != null) {
      for (BastCatchClause elem : node.catches) {
        elem.accept(this);
      }
    }
    if (node.finallyBlock != null) {
      node.finallyBlock.accept(this);
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastInterfaceDecl node) {
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.typeParameters != null) {
      for (BastTypeParameter elem : node.typeParameters) {
        elem.accept(this);
      }
    }
    if (node.interfaces != null) {
      for (BastType elem : node.interfaces) {
        elem.accept(this);
      }
    }
    if (node.declarations != null) {
      for (AbstractBastInternalDecl elem : node.declarations) {
        elem.accept(this);
      }
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastThrowStmt node) {
    if (node.exception != null) {
      node.exception.accept(this);
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAssertStmt node) {
    if (node.firstAssert != null) {
      node.firstAssert.accept(this);
    }
    if (node.secondAssert != null) {
      node.secondAssert.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastThis node) {
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastDefault node) {
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastSwitchCaseGroup node) {
    if (node.labels != null) {
      for (AbstractBastStatement expr : node.labels) {
        expr.accept(this);
      }
    }
    if (node.stmts != null) {
      for (AbstractBastStatement stmt : node.stmts) {
        stmt.accept(this);
      }
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastSuper node) {
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCatchClause node) {
    if (node.decl != null) {
      node.decl.accept(this);
    }
    if (node.block != null) {
      node.block.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastInstanceOf node) {
    if (node.expr != null) {
      node.expr.accept(this);
    }
    if (node.type != null) {
      node.type.accept(this);
    }
    assert (false);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastEnumDecl node) {
    if (node.enumerator != null) {
      node.enumerator.accept(this);
    }
    if (node.modifiers != null) {
      for (AbstractBastSpecifier spec : node.modifiers) {
        spec.accept(this);
      }
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastEnumSpec node) {
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.members != null) {
      for (BastEnumMember name : node.members) {
        name.accept(this);
      }
    }
    if (node.interfaces != null) {
      for (BastType name : node.interfaces) {
        name.accept(this);
      }
    }
    if (node.declarations != null) {
      for (AbstractBastInternalDecl name : node.declarations) {
        name.accept(this);
      }
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastEnumMember node) {
    if (node.init != null) {
      node.init.accept(this);
    }
    if (node.identifier != null) {
      node.identifier.accept(this);
    }
    if (node.annotations != null) {
      for (BastAnnotation name : node.annotations) {
        name.accept(this);
      }
    }
    if (node.initArguments != null) {
      for (AbstractBastExpr name : node.initArguments) {
        name.accept(this);
      }
    }
    if (node.classBodies != null) {
      for (AbstractBastInternalDecl name : node.classBodies) {
        name.accept(this);
      }
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastForStmt node) {
    if (node.init != null) {
      node.init.accept(this);
    } else {
      if (node.initDecl != null) {
        node.initDecl.accept(this);
      }
    }
    if (node.condition != null) {
      node.condition.accept(this);
    }
    if (node.increment != null) {
      node.increment.accept(this);
    }
    if (node.listStmt != null) {
      node.listStmt.accept(this);
    }
    if (node.statement != null) {
      node.statement.accept(this);
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastNew node) {
    if (node.type != null) {
      node.type.accept(this);
    }
    if (node.typeArguments != null) {
      for (BastType typeArgument : node.typeArguments) {
        typeArgument.accept(this);
      }
    }

    if (node.parameters != null) {
      for (AbstractBastExpr parameter : node.parameters) {
        parameter.accept(this);
      }
    }
    if (node.declarations != null) {
      for (AbstractBastInternalDecl decl : node.declarations) {
        decl.accept(this);
      }
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTemplateSpecifier node) {
    if (node.target != null) {
      node.target.accept(this);
    }
    if (node.typeArguments != null) {
      for (BastTypeArgument decl : node.typeArguments) {
        decl.accept(this);
      }
    }
    assert (false);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTypeParameter node) {
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.list != null) {
      for (BastType decl : node.list) {
        decl.accept(this);
      }
    }
    assert (false);
  }

  


  

  
  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastExprWater node) {
    assert false;
  }

  
  /**
   * Visit.
   *
   * @param bastClassKeySpecifier the bast class key specifier
   */
  @Override
  public void visit(BastClassKeySpecifier bastClassKeySpecifier) {
    assert false;
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastDummyToken node) {
    assert false;
  }

  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AbstractAresStatement node) {
    
  }

}
