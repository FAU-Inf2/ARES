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

package de.fau.cs.inf2.cas.ares.bast.nodes;

import de.fau.cs.inf2.cas.ares.bast.visitors.IAresBastVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;

/**
 * todo.
 *
 * <p>ARES statement subclause to specify the place of insertad statements
 * 
 */
public class AresPluginClause extends AbstractAresClause {

  public static final int TAG = TagConstants.ARES_PLUGIN;

  public BastNameIdent ident = null;
  public LinkedList<AbstractBastExpr> exprList = null;
  public AresPatternClause pattern = null;

  /**
   * Instantiates a new ARES plugin clause.
   *
   * @param tokens the tokens
   * @param ident the ident
   * @param exprList the expr list
   */
  public AresPluginClause(TokenAndHistory[] tokens, BastNameIdent ident,
      LinkedList<AbstractBastExpr> exprList) {
    super(tokens);
    this.ident = ident;
    fieldMap.put(BastFieldConstants.ARES_PLUGIN_CLAUSE_IDENT, new BastField(ident));
    this.exprList = exprList;
    fieldMap.put(BastFieldConstants.ARES_PLUGIN_CLAUSE_EXPR_LIST, new BastField(exprList));
  }

  
  /**
   * Accept.
   *
   * @param visitor the visitor
   */
  public void accept(IAresBastVisitor visitor) {
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
  @SuppressWarnings("unchecked")
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case ARES_PLUGIN_CLAUSE_IDENT:
        this.ident = (BastNameIdent) fieldValue.getField();
        break;
      case ARES_PLUGIN_CLAUSE_EXPR_LIST:
        this.exprList = (LinkedList<AbstractBastExpr>) fieldValue.getListField();
        break;
      default:
        assert (false);
    }
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    if (exprList != null && exprList.size() > 0 && exprList.get(0) != null) {
      return this.ident + "(" + exprList.get(0) + ")";
    } else {
      return this.ident + "(" + ")";
    }
  }

}
