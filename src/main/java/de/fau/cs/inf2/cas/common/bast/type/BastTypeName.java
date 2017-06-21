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

package de.fau.cs.inf2.cas.common.bast.type;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastSpecifierQualifier;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;


/**
 * todo. 

 */
public class BastTypeName extends BastType {
  public static final int TAG = TagConstants.TYPE_TYPE_NAME;
  public LinkedList<AbstractBastSpecifierQualifier> specifiers = null;
  public AbstractBastExpr declarator = null;

  /**
   * Instantiates a new bast type name.
   *
   * @param tokens the tokens
   * @param specifiers the specifiers
   * @param declarator the declarator
   */
  public BastTypeName(TokenAndHistory[] tokens,
      LinkedList<AbstractBastSpecifierQualifier> specifiers, AbstractBastExpr declarator) {
    super(tokens);
    this.specifiers = specifiers;
    fieldMap.put(BastFieldConstants.TYPE_NAME_SPECIFIERS, new BastField(specifiers));
    this.declarator = declarator;
    fieldMap.put(BastFieldConstants.TYPE_NAME_DECLARATOR, new BastField(declarator));
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
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    for (AbstractBastSpecifierQualifier spec : specifiers) {
      str.append(spec.toString());
      str.append(" ");
    }

    str.append(declarator.toString());

    return str.toString();
  }
}
