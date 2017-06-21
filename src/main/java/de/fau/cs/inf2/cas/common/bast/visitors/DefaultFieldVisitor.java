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

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
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
import de.fau.cs.inf2.cas.common.bast.type.BastStructOrUnionSpecifierType;
import de.fau.cs.inf2.cas.common.bast.type.BastTypeName;

public abstract class DefaultFieldVisitor extends AssertVisitor {

  public BastFieldConstants fieldId = BastFieldConstants.INVALID_FIELD_ID;
  public int listId = -1;
  public AbstractBastNode globalParent;

  /**
   * Begin visit.
   *
   * @param node the node
   */
  public abstract void beginVisit(AbstractBastNode node);

  /**
   * End visit.
   *
   * @param node the node
   */
  public abstract void endVisit(AbstractBastNode node);

  /**
   * Sets the variables.
   *
   * @param constant the constant
   * @param node the node
   * @param id the i
   */
  public void setVariables(BastFieldConstants constant, AbstractBastNode node, int id) {
    globalParent = node;
    listId = id;
    fieldId = constant;
  }

  /**
   * Standard visit.
   *
   * @param constant the constant
   * @param node the node
   */
  public void standardVisit(BastFieldConstants constant, AbstractBastNode node) {
    if (node.fieldMap.get(constant) != null) {
      if (node.fieldMap.get(constant).isList()) {
        int counter = 0;
        if (node.fieldMap.get(constant).getListField() != null) {
          for (AbstractBastNode expr : node.fieldMap.get(constant).getListField()) {
            setVariables(constant, node, counter++);
            if (expr != null) {
              expr.accept(this);
            } else {
              assert (false);
            }
          }
        }
      } else if (node.fieldMap.get(constant).getField() != null) {
        setVariables(constant, node, -1);
        node.fieldMap.get(constant).getField().accept(this);
      }
    }
  }

  

 

  




  


  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastBlock node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.BLOCK_MODIFIERS, node);
    standardVisit(BastFieldConstants.BLOCK_STATEMENT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastBreak node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.BREAK_NAME, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastLabelStmt node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.LABEL_STMT_IDENT, node);
    standardVisit(BastFieldConstants.LABEL_STMT_STMT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastGoto node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.GOTO_LABEL, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAssertStmt node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ASSERT_STMT_FIRST_ASSERT, node);
    standardVisit(BastFieldConstants.ASSERT_STMT_SECOND_ASSERT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastSwitch node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.SWITCH_CONDITION, node);
    standardVisit(BastFieldConstants.SWITCH_CASE_GROUPS, node);
    endVisit(node);
  }



  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastSwitchCaseGroup node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.SWITCH_CASE_GROUP_LABELS, node);
    standardVisit(BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastSynchronizedBlock node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.SYNCHRONIZED_BLOCK_BLOCK, node);
    standardVisit(BastFieldConstants.SYNCHRONIZED_BLOCK_EXPR, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCase node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.CASE_CONSTANT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastDefault node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastStructDecl node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.STRUCT_DECL_SPECIFIER_QUALIFIER, node);
    standardVisit(BastFieldConstants.STRUCT_DECL_DECLARATORS, node);
    standardVisit(BastFieldConstants.STRUCT_DECL_MEMBER, node);
    endVisit(node);
  }


  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastIf node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.IF_CONDITION, node);
    standardVisit(BastFieldConstants.IF_IF_PART, node);
    standardVisit(BastFieldConstants.IF_ELSE_PART, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastWhileStatement node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.WHILE_EXPRESSION, node);
    standardVisit(BastFieldConstants.WHILE_STATEMENT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCondAnd node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.COND_AND_LEFT, node);
    standardVisit(BastFieldConstants.COND_AND_RIGHT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCondOr node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.COND_OR_LEFT, node);
    standardVisit(BastFieldConstants.COND_OR_RIGHT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastContinue node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.CONTINUE_NAME, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastRealConst node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastMultiExpr node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.MULTI_EXPR_LEFT, node);
    standardVisit(BastFieldConstants.MULTI_EXPR_RIGHT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAsgnExpr node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ASGN_EXPR_LEFT, node);
    standardVisit(BastFieldConstants.ASGN_EXPR_RIGHT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastReturn node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.RETURN_VALUE, node);
    endVisit(node);
  }

  


  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastPackage node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.PACKAGE_ANNOTATIONS, node);
    standardVisit(BastFieldConstants.PACKAGE_NAME, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastParameter node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.PARAMETER_DECLARATOR, node);
    standardVisit(BastFieldConstants.PARAMETER_SPECIFIERS, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastFunctionParameterDeclarator node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.FUNCTION_PARAMETER_DECLARATOR_FUNCTION, node);
    standardVisit(BastFieldConstants.FUNCTION_PARAMETER_DECLARATOR_PARAMETERS, node);
    standardVisit(BastFieldConstants.FUNCTION_PARAMETER_DECLARATOR_POINTER, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastParameterList node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.PARAMETER_TYPE_LIST_PARAMETERS, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastFunction node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.FUNCTION_BLOCK_MODIFIERS, node);
    standardVisit(BastFieldConstants.FUNCTION_BLOCK_DECL, node);
    standardVisit(BastFieldConstants.FUNCTION_BLOCK_SPECIFIER_LIST, node);
    standardVisit(BastFieldConstants.FUNCTION_BLOCK_DECL_LIST, node);
    standardVisit(BastFieldConstants.FUNCTION_BLOCK_PARAMETERS, node);
    standardVisit(BastFieldConstants.FUNCTION_BLOCK_LOCAL_DECLARATIONS, node);
    standardVisit(BastFieldConstants.FUNCTION_BLOCK_STATEMENTS, node);
    standardVisit(BastFieldConstants.FUNCTION_BLOCK_EXCEPTIONS, node);
    standardVisit(BastFieldConstants.FUNCTION_BLOCK_NAME_IDENT, node);
    standardVisit(BastFieldConstants.FUNCTION_BLOCK_RETURN_TYPE, node);
    standardVisit(BastFieldConstants.FUNCTION_BLOCK_TYPE_PARAMTER, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastPointer node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.POINTER_POINTER, node);
    standardVisit(BastFieldConstants.POINTER_QUALIFIERS, node);
    endVisit(node);
  }

  




  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastStorageClassSpecifier node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastEmptyDeclaration node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastEmptyStmt node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastExprInitializer node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.EXPR_INITIALIZER_INIT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCastExpr node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.CAST_EXPR_OPERAND, node);
    standardVisit(BastFieldConstants.CAST_EXPR_TYPE, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCatchClause node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.CATCH_CLAUSE_DECL, node);
    standardVisit(BastFieldConstants.CATCH_CLAUSE_BLOCK, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastNew node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.NEW_CLASS_TYPE, node);
    standardVisit(BastFieldConstants.NEW_CLASS_TYPE_ARGUMENTS, node);
    standardVisit(BastFieldConstants.NEW_CLASS_PARAMETERS, node);
    standardVisit(BastFieldConstants.NEW_CLASS_DECLARATIONS, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastIdentDeclarator node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.IDENT_DECLARATOR_IDENTIFIER, node);
    standardVisit(BastFieldConstants.IDENT_DECLARATOR_ARRAY_DECLARATOR, node);
    standardVisit(BastFieldConstants.IDENT_DECLARATOR_EXPRESSION, node);
    standardVisit(BastFieldConstants.IDENT_DECLARATOR_POINTER, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastImportDeclaration node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.IMPORT_NAME, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastListInitializer node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.LIST_INITIALIZER_INIT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastArrayDeclarator node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ARRAY_DECLARATOR_INDEX, node);
    standardVisit(BastFieldConstants.ARRAY_DECLARATOR_SOURCE, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastFunctionIdentDeclarator node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.FUNCTION_IDENT_DECLARATOR_FUNCTION, node);
    standardVisit(BastFieldConstants.FUNCTION_IDENT_DECLARATOR_PARAMTERS, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastDeclaration node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.DECLARATION_MODIFIERS, node);
    standardVisit(BastFieldConstants.DECLARATION_SPECIFIER, node);
    standardVisit(BastFieldConstants.DECLARATION_DECLARATORS, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastForStmt node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.FOR_STMT_INIT, node);
    standardVisit(BastFieldConstants.FOR_STMT_INIT_DECL, node);
    standardVisit(BastFieldConstants.FOR_STMT_CONDITION, node);
    standardVisit(BastFieldConstants.FOR_STMT_INCREMENT, node);
    standardVisit(BastFieldConstants.FOR_STMT_STATEMENT, node);
    standardVisit(BastFieldConstants.FOR_STMT_LIST_STMT, node);
    standardVisit(BastFieldConstants.FOR_STMT_LIST_DECL, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastArrayRef node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ARRAY_REF_REF, node);
    standardVisit(BastFieldConstants.ARRAY_REF_INDEX_LIST, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastExprList node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.EXPR_LIST_LIST, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAdditiveExpr node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ADDITIVE_EXPR_LEFT, node);
    standardVisit(BastFieldConstants.ADDITIVE_EXPR_RIGHT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastShift node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.SHIFT_LEFT, node);
    standardVisit(BastFieldConstants.SHIFT_RIGHT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCall node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.DIRECT_CALL_FUNCTION, node);
    standardVisit(BastFieldConstants.DIRECT_CALL_ARGUMENTS, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCondExpr node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.COND_EXPR_CONDITION, node);
    standardVisit(BastFieldConstants.COND_EXPR_TRUE_PART, node);
    standardVisit(BastFieldConstants.COND_EXPR_FALSE_PART, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastStringConst node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastClassConst node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAccess node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ACCESS_TARGET, node);
    standardVisit(BastFieldConstants.ACCESS_MEMBER, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastOr node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.OR_LEFT, node);
    standardVisit(BastFieldConstants.OR_RIGHT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastClassDecl node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.CLASS_DECL_NAME, node);
    standardVisit(BastFieldConstants.CLASS_DECL_MODIFIERS, node);
    standardVisit(BastFieldConstants.CLASS_DECL_TYPE_PARAMETERS, node);
    standardVisit(BastFieldConstants.CLASS_DECL_EXTENDED_CLASS, node);
    standardVisit(BastFieldConstants.CLASS_DECL_INTERFACES, node);
    standardVisit(BastFieldConstants.CLASS_DECL_DECLARATIONS, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastUnaryExpr node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.UNARY_EXPR_OPERAND, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastRegularDeclarator node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.REGULAR_DECLARATOR_DECLARATION, node);
    standardVisit(BastFieldConstants.REGULAR_DECLARATOR_EXPRESSION, node);
    standardVisit(BastFieldConstants.REGULAR_DECLARATOR_POINTER, node);
    standardVisit(BastFieldConstants.REGULAR_DECLARATOR_INIT, node);
    standardVisit(BastFieldConstants.UNARY_EXPR_OPERAND, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTypeArgument node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.TYPE_ARGUMENT_TYPE, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTypeName node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.TYPE_NAME_DECLARATOR, node);
    standardVisit(BastFieldConstants.TYPE_NAME_SPECIFIERS, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTypeParameter node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.TYPE_PARAMETER_LIST, node);
    standardVisit(BastFieldConstants.TYPE_PARAMETER_NAME, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastIncrExpr node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.INCR_EXPR_OPERAND, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastInstanceOf node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.INSTANCE_OF_EXPR, node);
    standardVisit(BastFieldConstants.INSTANCE_OF_TYPE, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastDecrExpr node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.DECR_EXPR_OPERAND, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCmp node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.CMP_EXPR_LEFT, node);
    standardVisit(BastFieldConstants.CMP_EXPR_RIGHT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnd node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.AND_LEFT, node);
    standardVisit(BastFieldConstants.AND_RIGHT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastXor node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.XOR_LEFT, node);
    standardVisit(BastFieldConstants.XOR_RIGHT, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnnotation node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ANNOTATION_EXPRLIST, node);
    standardVisit(BastFieldConstants.ANNOTATION_NAME, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnnotationDecl node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ANNOTATION_DECL_DECLARATIONS, node);
    standardVisit(BastFieldConstants.ANNOTATION_DECL_MODIFIERS, node);
    standardVisit(BastFieldConstants.ANNOTATION_DECL_NAME, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnnotationElemValue node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ANNOTATION_ELEM_VALUE_INITLIST, node);
    standardVisit(BastFieldConstants.ANNOTATION_ELEM_VALUE_QUALIFIED_NAME, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnnotationMethod node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ANNOTATION_METHOD_DECLARATOR, node);
    standardVisit(BastFieldConstants.ANNOTATION_METHOD_DEFAULT_VALUE, node);
    standardVisit(BastFieldConstants.ANNOTATION_METHOD_MODIFIERS, node);
    standardVisit(BastFieldConstants.ANNOTATION_METHOD_TYPE, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastThrowStmt node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.THROW_EXCEPTION, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTryStmt node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.TRY_BLOCK, node);
    standardVisit(BastFieldConstants.TRY_CATCHES, node);
    standardVisit(BastFieldConstants.TRY_FINALLY_BLOCK, node);
    standardVisit(BastFieldConstants.TRY_RESOURCES, node);
    endVisit(node);

  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastEnumSpec node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ENUM_SPEC_DECLARATIONS, node);
    standardVisit(BastFieldConstants.ENUM_SPEC_INTERFACES, node);
    standardVisit(BastFieldConstants.ENUM_SPEC_MEMBERS, node);
    standardVisit(BastFieldConstants.ENUM_SPEC_NAME, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastEnumMember node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ENUM_MEMBER_ANNOTATIONS, node);
    standardVisit(BastFieldConstants.ENUM_MEMBER_IDENTIFIER, node);
    standardVisit(BastFieldConstants.ENUM_MEMBER_CLASS_BODIES, node);
    standardVisit(BastFieldConstants.ENUM_MEMBER_INIT, node);
    standardVisit(BastFieldConstants.ENUM_MEMBER_INIT_ARGUMENTS, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastEnumDecl node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ENUM_DECL_ENUMERATOR, node);
    standardVisit(BastFieldConstants.ENUM_DECL_MODIFIERS, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCharConst node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTypeSpecifier node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.TYPE_SPECIFIER_TYPE, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastStructOrUnionSpecifierType node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.STRUCT_OR_UNION_SPECIFIER_TYPE_NAME, node);
    standardVisit(BastFieldConstants.STRUCT_OR_UNION_SPECIFIER_TYPE_STRUCT, node);
    standardVisit(BastFieldConstants.STRUCT_OR_UNION_SPECIFIER_TYPE_IDENTIFIER, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastArrayType node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.ARRAY_TYPE_TYPE, node);
    standardVisit(BastFieldConstants.ARRAY_TYPE_DIMENSIONS, node);
    standardVisit(BastFieldConstants.ARRAY_TYPE_INITIALIZER, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastClassType node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.CLASS_TYPE_NAME, node);
    standardVisit(BastFieldConstants.CLASS_TYPE_TYPE_ARGUMENT, node);
    standardVisit(BastFieldConstants.CLASS_TYPE_SUB_CLASS, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastProgram node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.PROGRAM_COMMENTS, node);
    standardVisit(BastFieldConstants.PROGRAM_FUNCTION_BLOCKS, node);
    standardVisit(BastFieldConstants.PROGRAM_PACKAGE, node);
    standardVisit(BastFieldConstants.PROGRAM_IMPORTS, node);
    standardVisit(BastFieldConstants.PROGRAM_SYMTAB, node);
    standardVisit(BastFieldConstants.PROGRAM_ANNOTATIONS, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastIncludeStmt node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.INCLUDE_STMT_NAME_PART, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTypeQualifier node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastNameIdent node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTemplateSpecifier node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.TEMPLATE_SPECIFIER_TARGET, node);
    standardVisit(BastFieldConstants.TEMPLATE_SPECIFIER_TYPE_ARGUMENTS, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastThis node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastSuper node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastNullConst node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastBasicType node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastIntConst node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastInterfaceDecl node) {
    beginVisit(node);
    standardVisit(BastFieldConstants.INTERFACE_DECL_DECLARATIONS, node);
    standardVisit(BastFieldConstants.INTERFACE_DECL_INTERFACES, node);
    standardVisit(BastFieldConstants.INTERFACE_DECL_MODIFIERS, node);
    standardVisit(BastFieldConstants.INTERFACE_DECL_NAME, node);
    standardVisit(BastFieldConstants.INTERFACE_DECL_TYPE_PARAMETERS, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastBoolConst node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastClassKeySpecifier node) {
    beginVisit(node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastExprWater node) {
    beginVisit(node);
    endVisit(node);
  }

}
