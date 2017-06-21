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
import de.fau.cs.inf2.cas.common.bast.type.BastType;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;
import java.util.List;


public class BastEnumSpec extends AbstractBastSpecifierQualifier {

  public static final int TAG = TagConstants.BAST_ENUM_SPEC;

  public BastNameIdent name = null;
  public List<BastEnumMember> members = null;
  public LinkedList<BastType> interfaces = null;
  public LinkedList<AbstractBastInternalDecl> declarations = null;

  /**
   * Instantiates a new bast enum spec.
   *
   * @param tokens the tokens
   * @param name the name
   * @param interfaces the interfaces
   * @param members the members
   * @param declarations the declarations
   */
  public BastEnumSpec(TokenAndHistory[] tokens, BastNameIdent name, LinkedList<BastType> interfaces,
      LinkedList<BastEnumMember> members, LinkedList<AbstractBastInternalDecl> declarations) {
    super(tokens);
    this.name = name;
    fieldMap.put(BastFieldConstants.ENUM_SPEC_NAME, new BastField(name));
    this.members = members;
    fieldMap.put(BastFieldConstants.ENUM_SPEC_MEMBERS, new BastField(members));
    this.interfaces = interfaces;
    fieldMap.put(BastFieldConstants.ENUM_SPEC_INTERFACES, new BastField(interfaces));
    this.declarations = declarations;
    fieldMap.put(BastFieldConstants.ENUM_SPEC_DECLARATIONS, new BastField(declarations));
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
  @SuppressWarnings("unchecked")
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case ENUM_SPEC_NAME:
        this.name = (BastNameIdent) fieldValue.getField();
        break;
      case ENUM_SPEC_MEMBERS:
        this.members = (List<BastEnumMember>) fieldValue.getListField();
        break;
      case ENUM_SPEC_INTERFACES:
        this.interfaces = (LinkedList<BastType>) fieldValue.getListField();
        break;
      case ENUM_SPEC_DECLARATIONS:
        this.declarations = (LinkedList<AbstractBastInternalDecl>) fieldValue.getListField();
        break;
      default:
        assert (false);

    }
  }
}
