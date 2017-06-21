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
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastSpecifierQualifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStructDecl;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;


/**
 * todo.
 *
 * <p>Represents a C struct or union type, including the name
 * 

 */
public class BastStructOrUnionSpecifierType extends AbstractBastSpecifierQualifier {
  public static final int TAG = TagConstants.TYPE_BAST_STRUCT_OR_UNION;
  public static final int STRUCT = 0;
  public static final int UNION = 1;
  public LinkedList<BastStructDecl> list = null;
  public BastNameIdent name = null;
  public BastNameIdent identifier = null;

  /**
   * Instantiates a new bast struct or union specifier type.
   *
   * @param tokens the tokens
   * @param tag the tag
   * @param name the name
   * @param list the list
   * @param identifier the identifier
   */
  public BastStructOrUnionSpecifierType(TokenAndHistory[] tokens, int tag, BastNameIdent name,
      LinkedList<BastStructDecl> list, BastNameIdent identifier) {
    super(tokens);
    this.name = name;
    fieldMap.put(BastFieldConstants.STRUCT_OR_UNION_SPECIFIER_TYPE_NAME, new BastField(name));
    this.list = list;
    fieldMap.put(BastFieldConstants.STRUCT_OR_UNION_SPECIFIER_TYPE_STRUCT, new BastField(list));
    this.identifier = identifier;
    fieldMap.put(BastFieldConstants.STRUCT_OR_UNION_SPECIFIER_TYPE_IDENTIFIER,
        new BastField(identifier));
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
}
