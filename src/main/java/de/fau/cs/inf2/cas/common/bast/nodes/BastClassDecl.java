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

public class BastClassDecl extends AbstractBastInternalDecl {
  public static final int TAG = TagConstants.BAST_CLASS_DECL;

  public BastNameIdent name = null;
  public LinkedList<BastTypeParameter> typeParameters = null;
  public BastType extendedClass = null;
  public LinkedList<BastType> interfaces = null;
  public LinkedList<AbstractBastInternalDecl> declarations = null;
  public LinkedList<AbstractBastSpecifier> modifiers = null;

  /**
   * Instantiates a new bast class decl.
   *
   * @param tokens the tokens
   * @param name the name
   * @param typeParameters the type parameters
   * @param extendedClass the extended class
   * @param interfaces the interfaces
   * @param declarations the declarations
   */
  public BastClassDecl(TokenAndHistory[] tokens, BastNameIdent name,
      LinkedList<BastTypeParameter> typeParameters, BastType extendedClass,
      LinkedList<BastType> interfaces, LinkedList<AbstractBastInternalDecl> declarations) {
    super(tokens);
    this.name = name;
    fieldMap.put(BastFieldConstants.CLASS_DECL_NAME, new BastField(name));
    this.typeParameters = typeParameters;
    fieldMap.put(BastFieldConstants.CLASS_DECL_TYPE_PARAMETERS, new BastField(typeParameters));
    this.extendedClass = extendedClass;
    fieldMap.put(BastFieldConstants.CLASS_DECL_EXTENDED_CLASS, new BastField(extendedClass));
    this.interfaces = interfaces;
    fieldMap.put(BastFieldConstants.CLASS_DECL_INTERFACES, new BastField(interfaces));
    this.declarations = declarations;
    fieldMap.put(BastFieldConstants.CLASS_DECL_DECLARATIONS, new BastField(declarations));
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
      case CLASS_DECL_NAME:
        this.name = (BastNameIdent) fieldValue.getField();
        break;
      case CLASS_DECL_TYPE_PARAMETERS:
        this.typeParameters = (LinkedList<BastTypeParameter>) fieldValue.getListField();
        break;
      case CLASS_DECL_EXTENDED_CLASS:
        this.extendedClass = (BastType) fieldValue.getField();
        break;
      case CLASS_DECL_INTERFACES:
        this.interfaces = (LinkedList<BastType>) fieldValue.getListField();
        break;
      case CLASS_DECL_DECLARATIONS:
        this.declarations = (LinkedList<AbstractBastInternalDecl>) fieldValue.getListField();
        break;
      case CLASS_DECL_MODIFIERS:
        this.modifiers = (LinkedList<AbstractBastSpecifier>) fieldValue.getListField();
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
  @Override
  public void setModifiers(LinkedList<AbstractBastSpecifier> modifiers) {
    this.modifiers = modifiers;
    fieldMap.put(BastFieldConstants.CLASS_DECL_MODIFIERS, new BastField(modifiers));
  }

}
