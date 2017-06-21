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
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;

public class AresUseStmt extends AbstractAresExpr {

  public static final int TAG = TagConstants.ARES_USE;

  private AresPatternClause pattern = null;

  /**
   * Instantiates a new ARES use stmt.
   *
   * @param tokens the tokens
   * @param ident the ident
   * @param pattern the pattern
   * @param statements the statements
   */
  public AresUseStmt(TokenAndHistory[] tokens, BastNameIdent ident,
      AresPatternClause pattern, LinkedList<AbstractBastStatement> statements) {
    super(tokens);
    this.pattern = pattern;
    fieldMap.put(BastFieldConstants.ARES_USE_STMT_PATTERN, new BastField(pattern));
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
      case ARES_USE_STMT_PATTERN:
        this.pattern = (AresPatternClause) fieldValue.getField();
        break;
      default:
        assert (false);
    }
  }
  
  public String toString() {
    return "//# use(" +  ")";
  }

}
