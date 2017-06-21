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

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractAresStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExternalDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastInternalDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
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

import de.fau.cs.inf2.cas.common.parser.IGeneralToken;
import de.fau.cs.inf2.cas.common.parser.odin.JavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.ListToken;
import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Stack;

public class EnlightenedJavaPrinter implements IPrettyPrinter, IBastVisitor {

  public StringBuffer buffer = new StringBuffer();

  /**
   * Instantiates a new enlightened java printer.
   */
  public EnlightenedJavaPrinter() {

  }


  protected void addAllTokens(AbstractBastNode node, boolean before) {
    if (node.info == null) {
      return;
    }
    if (before) {
      for (TokenAndHistory token : node.info.tokensBefore) {
        addTokenData(token);
      }
    } else {
      for (TokenAndHistory token : node.info.tokensAfter) {
        addTokenData(token);
      }
    }
  }


  /**
   * Adds the token data.
   *
   * @param node the node
   * @param field the field
   */
  public void addTokenData(AbstractBastNode node, int field) {
    if (node.info.tokens == null || node.info.tokens.length <= field
        || node.info.tokens[field] == null) {
      return;
    }

    TokenAndHistory tokenAndHistory = node.info.tokens[field];
    addTokenData(tokenAndHistory);
  }

  protected void addTokenData(AbstractBastNode node, int field, int index) {
    if (node.info.tokens == null || node.info.tokens.length <= field
        || node.info.tokens[field] == null) {
      return;
    }
    TokenAndHistory tokenAndHistory = node.info.tokens[field];

    if (tokenAndHistory.token.getTag() == ListToken.TAG && index >= 0) {
      if (index < ((ListToken) tokenAndHistory.token).tokenList.size()) {
        addTokenData(((ListToken) tokenAndHistory.token).tokenList.get(index));
      }
    }
  }


  private void addTokenData(TokenAndHistory tokenAndHistory) {
    if (tokenAndHistory.prevTokens != null) {
      for (IGeneralToken token : tokenAndHistory.prevTokens) {
        buffer.append(((JavaToken) token).whiteSpace.toString());
        buffer.append(((JavaToken) token).data.toString());
      }
    }
    buffer.append(((JavaToken) tokenAndHistory.token).whiteSpace.toString());
    buffer.append(((JavaToken) tokenAndHistory.token).data.toString());
    if (tokenAndHistory.followingTokens != null) {
      for (IGeneralToken token : tokenAndHistory.followingTokens) {
        buffer.append(((JavaToken) token).whiteSpace.toString());
        buffer.append(((JavaToken) token).data.toString());
      }
    }
  }


  /**
   * Gets the buffer.
   *
   * @return the buffer
   */
  public StringBuffer getBuffer() {
    return buffer;
  }


  /**
   * Prints the.
   *
   * @param file the file
   */
  public void print(File file) {
    print(file, false);
  }

  private void print(File file, boolean append) {
    try {
      Writer output = new BufferedWriter(new FileWriter(file, append));
      try {
        output.write(this.buffer.toString());
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        output.close();
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AbstractAresStatement node) {
    assert false;
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAccess node) {
    addAllTokens(node, true);
    if (node.target != null) {
      node.target.accept(this);
    }
    addTokenData(node, 0);
    if (node.member != null) {
      node.member.accept(this);
    }
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAdditiveExpr node) {
    addAllTokens(node, true);
    if (node.left != null && node.left.getTag() == BastAdditiveExpr.TAG) {
      AbstractBastExpr tmp = node;
      Stack<BastAdditiveExpr> stack = new Stack<BastAdditiveExpr>();
      while (tmp.getTag() == BastAdditiveExpr.TAG) {
        stack.push((BastAdditiveExpr) tmp);
        tmp = ((BastAdditiveExpr) tmp).left;
      }
      tmp.accept(this);
      while (!stack.isEmpty()) {
        tmp = stack.pop();
        addTokenData(tmp, 0);
        ((BastAdditiveExpr) tmp).right.accept(this);
      }
    } else {
      if (node.left != null) {
        node.left.accept(this);
      }
      addTokenData(node, 0);
      switch (node.type) {
        case BastAdditiveExpr.TYPE_ADD:
          break;
        case BastAdditiveExpr.TYPE_SUB:
          break;
        default:
          break;
      }
      if (node.right != null) {
        node.right.accept(this);
      }
    }
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnd node) {
    addAllTokens(node, true);
    node.left.accept(this);
    addTokenData(node, 0);
    node.right.accept(this);
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnnotation node) {
    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.exprList != null) {
      for (AbstractBastExpr elem : node.exprList) {
        elem.accept(this);
      }
    }
    addTokenData(node, 1);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnnotationDecl node) {
    if (node.modifiers != null) {
      for (AbstractBastSpecifier modifier : node.modifiers) {
        modifier.accept(this);
      }
    }
    addTokenData(node, 0);
    addTokenData(node, 1);
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.declarations != null) {
      for (AbstractBastExternalDecl elem : node.declarations) {
        elem.accept(this);
      }
    }
    addTokenData(node, 2);
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
    addTokenData(node, 0);
    if (node.initList != null) {
      for (AbstractBastExpr elem : node.initList) {
        elem.accept(this);
      }
    }
    addTokenData(node, 1);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnnotationMethod node) {
    addAllTokens(node, true);
    if (node.modifiers != null) {
      for (AbstractBastSpecifier modifier : node.modifiers) {
        modifier.accept(this);
      }
    }
    if (node.type != null) {
      node.type.accept(this);
    }
    if (node.declarator != null) {
      node.declarator.accept(this);
    }
    addTokenData(node, 0);
    addTokenData(node, 1);
    if (node.defaultValue != null) {
      node.defaultValue.accept(this);
    }
    addAllTokens(node, false);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastArrayDeclarator node) {
    if (node.source != null) {
      node.source.accept(this);
    }
    addTokenData(node, 0);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastArrayRef node) {
    addAllTokens(node, true);
    if (node.arrayRef != null) {
      node.arrayRef.accept(this);
    }
    if (node.indices != null) {
      int count = 0;
      for (AbstractBastExpr expr : node.indices) {
        addTokenData(node, 0, count);
        count++;
        if (expr != null) {
          expr.accept(this);
        }
        addTokenData(node, 0, count);
        count++;
      }
    }
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastArrayType node) {
    addTokenData(node, 0);
    node.type.accept(this);

    int count = 0;
    if (node.dimensions != null) {
      for (AbstractBastExpr expr : node.dimensions) {
        addTokenData(node, 1, count);
        count++;
        expr.accept(this);
        addTokenData(node, 1, count);
        count++;
      }
    }
    int start = 0;
    if (node.dimensions != null) {
      start = node.dimensions.size();
    }

    for (int i = start; i < node.dimensionNumber; i++) {
      addTokenData(node, 1, count);
      count++;
      addTokenData(node, 1, count);
      count++;
    }

    if (node.initializer != null) {
      node.initializer.accept(this);
    }
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAsgnExpr node) {
    addAllTokens(node, true);
    if (node.left != null) {
      node.left.accept(this);
    }
    switch (node.operation) {
      case BastAsgnExpr.EXPR_TYPE_PLUS_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_DIVIDE_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_MINUS_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_SLL_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_SLR_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_SAR_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_REMAINDER_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_AND_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_OR_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_XOR_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_MULTIPLY_EQUAL:
        addTokenData(node, 0);
        break;
      default:
        assert (false);
    }
    if (node.right != null) {
      node.right.accept(this);
    }
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAssertStmt node) {
    addAllTokens(node, true);
    addTokenData(node, 0);
    if (node.firstAssert != null) {
      node.firstAssert.accept(this);
    }
    if (node.secondAssert != null) {
      addTokenData(node, 1);
      node.secondAssert.accept(this);
    }
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastBasicType node) {
    addAllTokens(node, true);
    addTokenData(node, 0);
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastBlock node) {
    addTokenData(node, 0);
    addTokenData(node, 1);
    if (node.statements != null) {
      for (AbstractBastStatement stmt : node.statements) {
        if (stmt != null) {
          stmt.accept(this);
        }
      }
    }

    addTokenData(node, 2);
    addTokenData(node, 3);
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
  public void visit(BastBoolConst node) {
    addAllTokens(node, true);
    addTokenData(node, 0);
    addAllTokens(node, false);
  }

  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastBreak node) {
    addAllTokens(node, true);
    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCall node) {
    addAllTokens(node, true);
    if (node.function != null) {
      node.function.accept(this);
    }
    addTokenData(node, 0);
    if (node.arguments != null) {
      int count = -1;
      for (AbstractBastNode expr : node.arguments) {
        addTokenData(node, 1, count);
        count++;
        if (expr != null) {
          expr.accept(this);
        } else {
          assert (false);
        }
      }
    }
    addTokenData(node, 2);
    addAllTokens(node, false);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCase node) {
    addTokenData(node, 0);
    node.caseConstant.accept(this);
    addTokenData(node, 1);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCastExpr node) {
    addAllTokens(node, true);
    if (node.castType != null) {
      addTokenData(node, 0);
      node.castType.accept(this);
      addTokenData(node, 1);
      if (node.operand != null) {
        node.operand.accept(this);
      }
    }
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCatchClause node) {
    addTokenData(node, 0);
    addTokenData(node, 1);

    if (node.decl != null) {
      node.decl.accept(this);
    }
    addTokenData(node, 2);

    if (node.block != null) {
      node.block.accept(this);
    }
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCharConst node) {
    addTokenData(node, 0);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastClassConst node) {
    addTokenData(node, 0);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastClassDecl node) {
    addAllTokens(node, true);
    if (node.modifiers != null) {
      for (AbstractBastSpecifier modifier : node.modifiers) {
        modifier.accept(this);
      }
    }
    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.typeParameters != null) {
      for (BastTypeParameter typeParameter : node.typeParameters) {
        typeParameter.accept(this);
      }
    }
    if (node.extendedClass != null) {
      addTokenData(node, 1);
      node.extendedClass.accept(this);
    }
    if (node.interfaces != null) {
      addTokenData(node, 2);
      for (BastType inter : node.interfaces) {
        inter.accept(this);
      }
    }
    addTokenData(node, 3);

    if (node.declarations != null) {
      for (AbstractBastInternalDecl decl : node.declarations) {
        decl.accept(this);
      }
    }
    addTokenData(node, 4);
    addAllTokens(node, false);

  }


  /**
   * Visit.
   *
   * @param bastClassKeySpecifier the bast class key specifier
   */
  @Override
  public void visit(BastClassKeySpecifier bastClassKeySpecifier) {
    assert false; // This node is only used in C
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastClassType node) {
    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.typeArgumentList != null) {
      for (AbstractBastNode arg : node.typeArgumentList) {
        arg.accept(this);
      }
    }
    if (node.subClass != null) {
      node.subClass.accept(this);
    }
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCmp node) {
    addAllTokens(node, true);
    if (node.left != null) {
      node.left.accept(this);
    }
    addTokenData(node, 0);
    if (node.right != null) {
      node.right.accept(this);
    }
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCondAnd node) {
    addAllTokens(node, true);
    node.left.accept(this);
    addTokenData(node, 0);
    node.right.accept(this);
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCondExpr node) {
    addAllTokens(node, true);
    node.condition.accept(this);
    addTokenData(node, 0);
    node.truePart.accept(this);
    addTokenData(node, 1);
    node.falsePart.accept(this);
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCondOr node) {
    addAllTokens(node, true);
    node.left.accept(this);
    addTokenData(node, 0);
    node.right.accept(this);
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastContinue node) {
    addAllTokens(node, true);
    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastDeclaration node) {
    addAllTokens(node, true);
    if (node.modifiers != null) {
      for (AbstractBastSpecifier spec : node.modifiers) {
        spec.accept(this);
      }
    }

    if (node.specifierList != null) {
      for (AbstractBastNode spec : node.specifierList) {
        if (spec != null) {
          spec.accept(this);
        }
      }
    }
    if (node.declaratorList != null) {
      int count = -1;

      for (AbstractBastNode decl : node.declaratorList) {
        if (decl != null) {
          addTokenData(node, 0, count);
          count++;
          decl.accept(this);
        }
      }
    }
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastDecrExpr node) {
    addAllTokens(node, true);
    if (node.operand != null) {
      node.operand.accept(this);
    }
    addTokenData(node, 0);
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastDefault node) {
    addTokenData(node, 0);
    addTokenData(node, 1);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastDummyToken node) {
    assert false; // This node is only used in C
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastEmptyDeclaration node) {
    addAllTokens(node, true);
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastEmptyStmt node) {
    addAllTokens(node, true);
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastEnumDecl node) {
    addAllTokens(node, true);
    if (node.modifiers != null) {
      for (AbstractBastSpecifier modifier : node.modifiers) {
        modifier.accept(this);
      }
    }

    if (node.enumerator != null) {
      node.enumerator.accept(this);
    }
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastEnumMember node) {
    addAllTokens(node, true);
    if (node.annotations != null) {
      for (BastAnnotation annotation : node.annotations) {
        annotation.accept(this);
      }
    }
    if (node.identifier != null) {
      node.identifier.accept(this);
    }
    if (node.init != null) {
      node.accept(this);
    }
    addTokenData(node, 0);
    if (node.initArguments != null) {
      int count = -1;
      for (AbstractBastExpr expr : node.initArguments) {
        addTokenData(node, 1, count);
        count++;
        expr.accept(this);
      }
    }
    addTokenData(node, 2);
    addTokenData(node, 3);
    if (node.classBodies != null) {
      for (AbstractBastInternalDecl decl : node.classBodies) {
        decl.accept(this);
      }
    }
    addTokenData(node, 4);
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastEnumSpec node) {
    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }

    if (node.interfaces != null) {
      addTokenData(node, 1);
      for (BastType type : node.interfaces) {
        type.accept(this);
      }
    }
    addTokenData(node, 2);

    if (node.members != null) {
      for (BastEnumMember member : node.members) {
        member.accept(this);
      }
    }
    addTokenData(node, 3);
    if (node.declarations != null) {
      for (AbstractBastInternalDecl decl : node.declarations) {
        decl.accept(this);
      }
    }
    addTokenData(node, 4);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastExprInitializer node) {
    addTokenData(node, 0);
    if (node.init != null) {
      node.init.accept(this);
    }
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastExprList node) {
    if (node.list != null) {
      int count = node.list.size() - 1;
      for (AbstractBastExpr expr : node.list) {
        expr.accept(this);
      }
    }
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastExprWater node) {
    assert false; // This node is only used in C
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastForStmt node) {
    addTokenData(node, 0);
    addTokenData(node, 1);

    if (node.init != null) {
      node.init.accept(this);
    } else {
      if (node.initDecl != null) {
        node.initDecl.accept(this);
      }
    }
    addTokenData(node, 2);
    if (node.listStmt != null) {
      node.listStmt.accept(this);
    }
    addTokenData(node, 3);
    if (node.condition != null) {
      node.condition.accept(this);
    }
    addTokenData(node, 4);

    if (node.increment != null) {
      node.increment.accept(this);
    }
    addTokenData(node, 5);
    if (node.statement != null) {
      node.statement.accept(this);
    }
    addTokenData(node, 6);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastFunction node) {
    addAllTokens(node, true);
    if (node.modifiers != null) {
      for (AbstractBastSpecifier spec : node.modifiers) {
        spec.accept(this);
      }
    }

    if (node.typeParameters != null) {
      for (BastTypeParameter param : node.typeParameters) {
        param.accept(this);
      }
    }

    if (node.specifierList != null) {
      for (AbstractBastSpecifier spec : node.specifierList) {
        spec.accept(this);
      }
    }
    if (node.returnType != null) {
      node.returnType.accept(this);
    }

    if (node.decl != null) {
      node.decl.accept(this);
    }
    if (node.exceptions != null) {
      addTokenData(node, 0);
      for (AbstractBastExpr expr : node.exceptions) {
        expr.accept(this);
      }
    }
    if (node.statements != null) {
      for (AbstractBastStatement expr : node.statements) {
        expr.accept(this);
      }
    }
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastFunctionIdentDeclarator node) {
    if (node.function != null) {
      node.function.accept(this);
    }
    if (node.parameters != null) {
      for (BastNameIdent par : node.parameters) {
        par.accept(this);
      }
    }
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
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastGoto node) {
    addTokenData(node, 0);
    node.label.accept(this);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public synchronized void visit(BastIdentDeclarator node) {
    if (node.pointer != null) {
      node.pointer.accept(this);
    }
    if (node.identifier != null) {
      node.identifier.accept(this);
    }
    if (node.declarator != null) {
      node.declarator.accept(this);
    }
    if (node.expression != null) {
      if (node.expression.getTag() == BastFunctionParameterDeclarator.TAG
          || node.expression.getTag() == BastFunctionIdentDeclarator.TAG) {
        node.expression.accept(this);
      } else {
        if (node.info.tokens != null) {
          addTokenData(node, node.info.tokens.length - 1);
        }
        node.expression.accept(this);
      }
    }
  }



  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastIf node) {
    addTokenData(node, 0);
    addTokenData(node, 1);

    if (node.condition != null) {
      node.condition.accept(this);
    }
    addTokenData(node, 2);
    if (node.ifPart != null && node.ifPart.getTag() == TagConstants.BAST_BLOCK) {
      node.ifPart.accept(this);
    } else {
      if (node.ifPart != null) {
        node.ifPart.accept(this);
      }
    }

    if (node.elsePart != null) {
      addTokenData(node, 3);
      node.elsePart.accept(this);
    }
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastImportDeclaration node) {
    addAllTokens(node, true);
    addTokenData(node, 0);
    addTokenData(node, 1);
    if (node.name != null) {
      node.name.accept(this);
    }
    addTokenData(node, 2);
    addAllTokens(node, false);
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
  public void visit(BastIncrExpr node) {
    addAllTokens(node, true);
    if (node.operand != null) {
      node.operand.accept(this);
    }
    addTokenData(node, 0);
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastInstanceOf node) {
    addAllTokens(node, true);
    if (node.expr != null) {
      node.expr.accept(this);
    }
    addTokenData(node, 0);
    if (node.type != null) {
      node.type.accept(this);
    }
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastIntConst node) {
    addAllTokens(node, true);
    addTokenData(node, 0);
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastInterfaceDecl node) {
    if (node.modifiers != null) {
      for (AbstractBastSpecifier modifier : node.modifiers) {
        modifier.accept(this);
      }
    }
    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.typeParameters != null) {
      for (BastTypeParameter elem : node.typeParameters) {
        elem.accept(this);
      }
    }
    if (node.interfaces != null) {
      addTokenData(node, 1);
      for (BastType elem : node.interfaces) {
        elem.accept(this);
      }
    }
    addTokenData(node, 2);

    if (node.declarations != null) {
      for (AbstractBastInternalDecl elem : node.declarations) {
        elem.accept(this);
      }
    }
    addTokenData(node, 3);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastLabelStmt node) {
    if (node.ident != null) {
      node.ident.accept(this);
    }
    addTokenData(node, 0);
    if (node.stmt != null) {
      node.stmt.accept(this);
    }

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
  @Override
  public void visit(BastListInitializer node) {
    addTokenData(node, 0);

    if (node.list != null) {
      int count = -1;
      for (AbstractBastNode init : node.list) {
        addTokenData(node, 1, count);
        count++;
        init.accept(this);
      }
    }
    addTokenData(node, 2);
    addTokenData(node, 3);
    addTokenData(node, 4);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastMultiExpr node) {
    addAllTokens(node, true);

    node.left.accept(this);
    addTokenData(node, 0);
    if (node.right != null) {
      node.right.accept(this);
    }
    addAllTokens(node, false);
  }

  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastNameIdent node) {
    addAllTokens(node, true);
    addTokenData(node, 0);
    addAllTokens(node, false);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastNew node) {
    addAllTokens(node, true);
    addTokenData(node, 0);
    if (node.typeArguments != null) {
      int count = 0;
      for (BastType typeArgument : node.typeArguments) {
        count++;
        typeArgument.accept(this);
      }
    }
    if (node.type != null) {
      node.type.accept(this);
    }

    addTokenData(node, 1);

    if (node.parameters != null) {
      int count = -1;
      for (AbstractBastExpr parameter : node.parameters) {
        addTokenData(node, 2, count);
        count++;
        parameter.accept(this);
      }

    }
    addTokenData(node, 3);
    addTokenData(node, 4);
    if (node.declarations != null) {
      for (AbstractBastInternalDecl decl : node.declarations) {
        decl.accept(this);
      }
    }

    addTokenData(node, 5);
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastNullConst node) {
    addTokenData(node, 0);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastOr node) {
    addAllTokens(node, true);
    node.left.accept(this);
    addTokenData(node, 0);
    node.right.accept(this);
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastPackage node) {
    addAllTokens(node, true);
    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.annotations != null) {
      for (BastAnnotation elem : node.annotations) {
        elem.accept(this);
      }
    }
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastParameter node) {
    if (node.specifiers != null) {
      int counter = 0;
      for (AbstractBastNode spec : node.specifiers) {
        spec.accept(this);
        counter++;
      }
    }
    addTokenData(node, 0);
    if (node.declarator != null) {
      node.declarator.accept(this);
    }
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastParameterList node) {
    addTokenData(node, 0);
    if (node.parameters != null) {
      int count = -1;
      for (AbstractBastNode par : node.parameters) {
        addTokenData(node, 1, count);
        count++;
        par.accept(this);
      }
    }
    addTokenData(node, 2);
    addTokenData(node, 3);

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
    buffer.append("*");
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastPointerType node) {
    assert (false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastProgram node) {
    if (node.annotations != null) {
      for (BastAnnotation expr : node.annotations) {
        expr.accept(this);
      }
    }
    if (node.packageName != null) {
      node.packageName.accept(this);
    }
    if (node.imports != null) {
      for (BastImportDeclaration expr : node.imports) {
        expr.accept(this);
      }
    }

    if (node.functionBlocks != null) {
      for (AbstractBastExternalDecl expr : node.functionBlocks) {
        expr.accept(this);
      }
    }

    addTokenData(node, 0);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastRealConst node) {
    addAllTokens(node, true);
    addTokenData(node, 0);
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastRegularDeclarator node) {
    if (node.pointer != null) {
      node.pointer.accept(this);
    }
    if (node.declaration != null) {
      node.declaration.accept(this);
    }
    if (node.expression != null) {
      node.expression.accept(this);
    }
    if (node.init != null) {
      node.init.accept(this);
    }

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastReturn node) {
    addAllTokens(node, true);
    addTokenData(node, 0);
    if (node.returnValue != null) {
      node.returnValue.accept(this);
    }
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastShift node) {
    addAllTokens(node, true);
    node.left.accept(this);
    addTokenData(node, 0);
    addTokenData(node, 1);
    addTokenData(node, 2);
    node.right.accept(this);
    addAllTokens(node, false);
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
  public void visit(BastStringConst node) {
    addAllTokens(node, true);
    addTokenData(node, 0);
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastStructDecl node) {
    buffer.append("structdecl ");
    buffer.append(node.name);
    buffer.append(" {");
    if (node.members != null) {
      for (BastStructMember expr : node.members) {
        expr.accept(this);
      }
    }
    buffer.append("};");
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastStructDeclarator node) {
    assert (false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastStructMember node) {
    buffer.append(node.name);
    buffer.append(" : ");
    buffer.append(node.type);
    buffer.append(" @ ");
    node.position.accept(this);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastStructOrUnionSpecifierType node) {
    assert (false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastSuper node) {
    addTokenData(node, 0);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastSwitch node) {
    addTokenData(node, 0);
    addTokenData(node, 1);
    node.condition.accept(this);
    addTokenData(node, 2);
    addTokenData(node, 3);
    if (node.switchGroups != null) {
      for (AbstractBastStatement group : node.switchGroups) {
        group.accept(this);
      }
    }
    addTokenData(node, 4);
    addTokenData(node, 5);
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
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastSynchronizedBlock node) {
    addTokenData(node, 0);
    addTokenData(node, 1);
    if (node.expr != null) {
      node.expr.accept(this);
    }
    addTokenData(node, 2);
    if (node.block != null) {
      node.block.accept(this);
    }

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTemplateSpecifier node) {

    if (node.typeArguments != null) {
      for (BastTypeArgument decl : node.typeArguments) {
        decl.accept(this);
      }
    }

    if (node.target != null) {
      node.target.accept(this);
    }
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastThis node) {
    addTokenData(node, 0);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastThrowStmt node) {
    addAllTokens(node, true);
    addTokenData(node, 0);
    if (node.exception != null) {
      node.exception.accept(this);
    }
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTryStmt node) {
    addTokenData(node, 0);
    if (node.resources != null) {
      for (BastDeclaration decl : node.resources) {
        decl.accept(this);
      }
    }
    addTokenData(node, 1);
    if (node.block != null) {
      node.block.accept(this);
    }
    if (node.catches != null) {
      for (AbstractBastNode elem : node.catches) {
        elem.accept(this);
      }
    }
    addTokenData(node, 2);
    if (node.finallyBlock != null) {
      node.finallyBlock.accept(this);
    }
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTypeArgument node) {
    addTokenData(node, 0);
    addTokenData(node, 1);
    addTokenData(node, 2);
    if (node.type != null) {
      node.type.accept(this);
    }
    addTokenData(node, 3);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastTypeName node) {
    if (node.specifiers != null) {
      for (AbstractBastSpecifier spec : node.specifiers) {
        spec.accept(this);
      }
    }
    if (node.declarator != null) {
      node.declarator.accept(this);
    }
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTypeParameter node) {
    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }
    addTokenData(node, 1);
    if (node.list != null) {
      for (BastType decl : node.list) {
        decl.accept(this);
      }
    }
    addTokenData(node, 2);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastTypeQualifier node) {
    addTokenData(node, 0);
    switch (node.type) {
      case BastTypeQualifier.CONST_QUALIFIER:
        break;
      case BastTypeQualifier.VOLATILE_QUALIFIER:
        break;
      case BastTypeQualifier.TYPE_PUBLIC:
        break;
      case BastTypeQualifier.TYPE_PROTECTED:
        break;
      case BastTypeQualifier.TYPE_PRIVATE:
        break;
      case BastTypeQualifier.TYPE_STATIC:
        break;
      case BastTypeQualifier.TYPE_ABSTRACT:
        break;
      case BastTypeQualifier.TYPE_FINAL:
        break;
      case BastTypeQualifier.TYPE_NATIVE:
        break;
      case BastTypeQualifier.TYPE_SYNCHRONIZED:
        break;
      case BastTypeQualifier.TYPE_TRANSIENT:
        break;
      case BastTypeQualifier.TYPE_VOLATILE:
        break;
      case BastTypeQualifier.TYPE_STRICTFP:
        break;
      default:
        assert (false);

    }
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTypeSpecifier node) {
    addTokenData(node, 0);
    if (node.type != null) {
      node.type.accept(this);
    }
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastUnaryExpr node) {
    addAllTokens(node, true);
    addTokenData(node, 0);
    if (node.operand != null) {
      node.operand.accept(this);
    }
    addAllTokens(node, false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastWhileStatement node) {
    addAllTokens(node, true);
    addTokenData(node, 0);
    if (node.type == BastWhileStatement.TYPE_WHILE) {
      addTokenData(node, 1);
      if (node.expression != null) {
        node.expression.accept(this);
      }
      addTokenData(node, 2);
      if (node.statement != null) {
        node.statement.accept(this);
      }
    } else {
      if (node.statement != null) {
        node.statement.accept(this);
      }
      addTokenData(node, 1);
      addTokenData(node, 2);
      if (node.expression != null) {
        node.expression.accept(this);
      }
      addTokenData(node, 3);
    }
    addAllTokens(node, false);
  }

  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastXor node) {
    addAllTokens(node, true);
    node.left.accept(this);
    addTokenData(node, 0);
    node.right.accept(this);
    addAllTokens(node, false);
  }



}
