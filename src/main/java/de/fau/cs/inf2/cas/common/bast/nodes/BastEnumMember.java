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
 * <p>Part of an enumeration definition
 * 
 */
public class BastEnumMember extends AbstractBastExpr {

  public static final int TAG = TagConstants.BAST_ENUM_MEMBER;

  public BastNameIdent identifier = null;
  public AbstractBastExpr init = null;

  public LinkedList<BastAnnotation> annotations = null;
  public LinkedList<AbstractBastExpr> initArguments = null;
  public LinkedList<AbstractBastInternalDecl> classBodies = null;

  /**
   * todo.
   * Member can have arguments or a class body
   * 
   * @param annotations todo
   * @param identifier todo
   * @param initArguments todo
   * @param classBodies todo
   */
  public BastEnumMember(TokenAndHistory[] tokens, LinkedList<BastAnnotation> annotations,
      BastNameIdent identifier, LinkedList<AbstractBastExpr> initArguments,
      LinkedList<AbstractBastInternalDecl> classBodies) {
    super(tokens);
    this.annotations = annotations;
    fieldMap.put(BastFieldConstants.ENUM_MEMBER_ANNOTATIONS, new BastField(annotations));
    this.identifier = identifier;
    fieldMap.put(BastFieldConstants.ENUM_MEMBER_IDENTIFIER, new BastField(identifier));
    this.initArguments = initArguments;
    fieldMap.put(BastFieldConstants.ENUM_MEMBER_INIT_ARGUMENTS, new BastField(initArguments));
    this.classBodies = classBodies;
    fieldMap.put(BastFieldConstants.ENUM_MEMBER_CLASS_BODIES, new BastField(classBodies));
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
      case ENUM_MEMBER_ANNOTATIONS:
        this.annotations = (LinkedList<BastAnnotation>) fieldValue.getListField();
        break;
      case ENUM_MEMBER_IDENTIFIER:
        this.identifier = (BastNameIdent) fieldValue.getField();
        break;
      case ENUM_MEMBER_INIT_ARGUMENTS:
        this.initArguments = (LinkedList<AbstractBastExpr>) fieldValue.getListField();
        break;
      case ENUM_MEMBER_CLASS_BODIES:
        this.classBodies = (LinkedList<AbstractBastInternalDecl>) fieldValue.getListField();
        break;
      default:
        assert (false);

    }
  }

}
