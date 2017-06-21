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

package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;


/**
 * todo. 
 */
public class BastForStmt extends AbstractBastStatement {

  public static final int TAG = TagConstants.BAST_FOR_STMT;
  public static final int TYPE_STANDARD = 0;
  public static final int TYPE_SPECIAL = 1;
  public static final int TYPE_NONCSTYLE = 2;

  public AbstractBastExpr init = null;
  public AbstractBastExpr condition;
  public AbstractBastExpr increment;
  public AbstractBastStatement statement;
  public BastDeclaration initDecl;
  public AbstractBastExpr listStmt = null;
  public int type = TYPE_STANDARD;
  

  /**
   * todo.
   * for loop without declaration
   * 
   * @param tokens the tokens
   * @param init todo
   * @param condition todo
   * @param increment todo
   * @param statement todo
   */
  public BastForStmt(TokenAndHistory[] tokens, AbstractBastExpr init, AbstractBastExpr condition,
      AbstractBastExpr increment, AbstractBastStatement statement) {
    super(tokens);
    this.init = init;
    fieldMap.put(BastFieldConstants.FOR_STMT_INIT, new BastField(init));
    this.condition = condition;
    fieldMap.put(BastFieldConstants.FOR_STMT_CONDITION, new BastField(condition));
    this.increment = increment;
    fieldMap.put(BastFieldConstants.FOR_STMT_INCREMENT, new BastField(increment));
    this.statement = statement;
    fieldMap.put(BastFieldConstants.FOR_STMT_STATEMENT, new BastField(statement));
  }

  /**
   * todo.
   * for loop with declaration for (int i = 0; ;)
   * 
   * @param tokens the tokens
   * @param initDecl todo
   * @param condition todo
   * @param increment todo
   * @param statement todo
   */
  public BastForStmt(TokenAndHistory[] tokens, BastDeclaration initDecl, AbstractBastExpr condition,
      AbstractBastExpr increment, AbstractBastStatement statement) {
    super(tokens);
    this.initDecl = initDecl;
    fieldMap.put(BastFieldConstants.FOR_STMT_INIT_DECL, new BastField(initDecl));

    this.condition = condition;
    fieldMap.put(BastFieldConstants.FOR_STMT_CONDITION, new BastField(condition));

    this.increment = increment;
    fieldMap.put(BastFieldConstants.FOR_STMT_INCREMENT, new BastField(increment));

    this.statement = statement;
    fieldMap.put(BastFieldConstants.FOR_STMT_STATEMENT, new BastField(statement));

  }

  /**
   * Instantiates a new bast for stmt.
   *
   * @param tokens the tokens
   * @param initDecl the init decl
   * @param listStmt the list stmt
   * @param statement the statement
   */
  public BastForStmt(TokenAndHistory[] tokens, BastDeclaration initDecl, AbstractBastExpr listStmt,
      AbstractBastStatement statement) {
    super(tokens);
    this.initDecl = initDecl;
    fieldMap.put(BastFieldConstants.FOR_STMT_INIT_DECL, new BastField(initDecl));

    this.listStmt = listStmt;
    fieldMap.put(BastFieldConstants.FOR_STMT_LIST_STMT, new BastField(listStmt));

    this.statement = statement;
    fieldMap.put(BastFieldConstants.FOR_STMT_STATEMENT, new BastField(statement));

    this.type = TYPE_SPECIAL;
  }

  
  /**
   * Accept.
   *
   * @param visitor the visitor
   */
  @Override
  public void accept(IBastVisitor visitor) {
    visitor.visit(this);

  }

  
  /**
   * Gets the tag.
   *
   * @return the tag
   */
  @Override
  public int getTag() {
    return TAG;
  }

  /**
   * Replace field.
   *
   * @param field the field
   * @param fieldValue the field value
   */
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case FOR_STMT_INIT:
        this.init = (AbstractBastExpr) fieldValue.getField();
        break;
      case FOR_STMT_CONDITION:
        this.condition = (AbstractBastExpr) fieldValue.getField();
        break;
      case FOR_STMT_INCREMENT:
        this.increment = (AbstractBastExpr) fieldValue.getField();
        break;
      case FOR_STMT_STATEMENT:
        this.statement = (AbstractBastStatement) fieldValue.getField();
        break;
      case FOR_STMT_INIT_DECL:
        this.initDecl = (BastDeclaration) fieldValue.getField();
        break;
      case FOR_STMT_LIST_STMT:
        this.listStmt = (AbstractBastExpr) fieldValue.getField();
        break;
      default:
        assert (false);

    }
  }

  
  /**
   * Sets the modifiers.
   *
   * @param modifiers the new modifiers
   */
  public void setModifiers(LinkedList<AbstractBastSpecifier> modifiers) {
    assert (false);
  }

}
