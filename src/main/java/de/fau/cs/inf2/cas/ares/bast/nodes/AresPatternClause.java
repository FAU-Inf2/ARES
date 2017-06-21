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
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

/**
 * todo.
 *
 * <p>ARES statement subclause to specify the place of insertad statements
 * 
 */
public class AresPatternClause extends AbstractAresClause {

  public static final int TAG = TagConstants.ARES_PATTERN;

  public BastIntConst occurrence = null;
  public AbstractBastNode expr = null;
  public BastNameIdent ident = null;

  /**
   * Instantiates a new ARES pattern clause.
   *
   * @param tokens the tokens
   * @param occurrence the occurrence
   * @param expr the expr
   */
  public AresPatternClause(TokenAndHistory[] tokens, BastIntConst occurrence,
      AbstractBastExpr expr) {
    super(tokens);
    this.occurrence = occurrence;
    fieldMap.put(BastFieldConstants.ARES_PATTERN_CLAUSE_OCCURENCE, new BastField(occurrence));
    this.expr = expr;
    fieldMap.put(BastFieldConstants.ARES_PATTERN_CLAUSE_EXPR, new BastField(expr));

  }

  /**
   * Instantiates a new ARES pattern clause.
   *
   * @param tokens the tokens
   * @param occurrence the occurrence
   * @param expr the expr
   * @param ident the ident
   */
  public AresPatternClause(TokenAndHistory[] tokens, BastIntConst occurrence,
      AbstractBastNode expr, BastNameIdent ident) {
    super(tokens);
    this.occurrence = occurrence;
    fieldMap.put(BastFieldConstants.ARES_PATTERN_CLAUSE_OCCURENCE, new BastField(occurrence));
    this.expr = expr;
    fieldMap.put(BastFieldConstants.ARES_PATTERN_CLAUSE_EXPR, new BastField(expr));
    this.ident = ident;
    fieldMap.put(BastFieldConstants.ARES_PATTERN_CLAUSE_IDENT, new BastField(ident));

  }

  /**
   * Instantiates a new ARES pattern clause.
   *
   * @param tokens the tokens
   * @param occurrence the occurrence
   * @param expr the expr
   * @param ident the ident
   */
  public AresPatternClause(TokenAndHistory[] tokens, BastNameIdent ident ,
      AbstractBastNode expr, BastIntConst occurrence) {
    super(tokens);
    this.occurrence = occurrence;
    fieldMap.put(BastFieldConstants.ARES_PATTERN_CLAUSE_OCCURENCE, new BastField(occurrence));
    this.expr = expr;
    fieldMap.put(BastFieldConstants.ARES_PATTERN_CLAUSE_EXPR, new BastField(expr));
    this.ident = ident;
    fieldMap.put(BastFieldConstants.ARES_PATTERN_CLAUSE_IDENT, new BastField(ident));

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
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case ARES_PATTERN_CLAUSE_OCCURENCE:
        this.occurrence = (BastIntConst) fieldValue.getField();
        break;
      case ARES_PATTERN_CLAUSE_EXPR:
        this.expr = (AbstractBastExpr) fieldValue.getField();
        break;
      case ARES_PATTERN_CLAUSE_IDENT:
        this.ident = (BastNameIdent) fieldValue.getField();
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
    String tmp = "";
    if (occurrence != null) {
      tmp += occurrence + ", ";
    }
    if (expr != null) {
      tmp += expr + ", ";
    }
    if (ident != null) {
      tmp += ident + ", ";
    }
    return tmp;
  }

}
