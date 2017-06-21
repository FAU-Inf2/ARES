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
 *
 * <p>Combines the specifiers and modifiers with the declaration
 * 
 */
public class BastDeclaration extends AbstractBastInternalDecl {
  public static final int TAG = TagConstants.BAST_DECLARATION;

  public LinkedList<AbstractBastSpecifier> modifiers = null;
  public LinkedList<AbstractBastSpecifier> specifierList = null;
  public LinkedList<AbstractBastDeclarator> declaratorList = null;

  /**
   * todo.
   * If the declaratorList is empty, decl must not be null
   * 
   *
   * @param tokens the tokens
   * @param specifierList the specifier list
   * @param decl todo
   * @param declaratorList todo
   */
  public BastDeclaration(TokenAndHistory[] tokens, LinkedList<AbstractBastSpecifier> specifierList,
      AbstractBastDeclarator decl, LinkedList<AbstractBastExternalDecl> declaratorList) {
    super(tokens);
    this.specifierList = specifierList;
    fieldMap.put(BastFieldConstants.DECLARATION_SPECIFIER, new BastField(specifierList));

    this.declaratorList = new LinkedList<AbstractBastDeclarator>();
    this.declaratorList.add(decl);
    fieldMap.put(BastFieldConstants.DECLARATION_DECLARATORS, new BastField(this.declaratorList));

    assert (declaratorList == null);
  }

  /**
   * Instantiates a new bast declaration.
   *
   * @param tokens the tokens
   * @param specifierList the specifier list
   * @param declaratorList the declarator list
   */
  public BastDeclaration(TokenAndHistory[] tokens, LinkedList<AbstractBastSpecifier> specifierList,
      LinkedList<AbstractBastDeclarator> declaratorList) {
    super(tokens);
    this.specifierList = specifierList;
    fieldMap.put(BastFieldConstants.DECLARATION_SPECIFIER, new BastField(specifierList));
    this.declaratorList = declaratorList;
    fieldMap.put(BastFieldConstants.DECLARATION_DECLARATORS, new BastField(declaratorList));
  }

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
      case DECLARATION_SPECIFIER:
        this.specifierList = (LinkedList<AbstractBastSpecifier>) fieldValue.getListField();
        break;
      case DECLARATION_DECLARATORS:
        this.declaratorList = (LinkedList<AbstractBastDeclarator>) fieldValue.getListField();
        break;
      case DECLARATION_MODIFIERS:
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
    fieldMap.put(BastFieldConstants.DECLARATION_MODIFIERS, new BastField(modifiers));

  }

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    String tmp = "";
    if (specifierList != null) {
      for (AbstractBastSpecifier spec : specifierList) {
        tmp += spec.toString();
      }
    }
    if (declaratorList != null) {
      for (AbstractBastDeclarator decl : declaratorList) {
        tmp += decl.toString();
      }
    }
    return tmp;
  }

}
