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
 * A method reference expression.
 */
public class BastMethodReference extends AbstractBastExpr {

  public static final int TAG = TagConstants.BAST_METHOD_REFERENCE;
  public Kind referenceKind;
  public AbstractBastExpr target;
  public LinkedList<BastTypeArgument> typeArguments;
  public BastNameIdent name;



  /**
   * The kind of method the method reference refers to.
   */
  public enum Kind {
    METHOD,
    CONSTRUCTOR;
  }



  /**
   * Instantiates a new method reference expression to a given method.
   *
   * @param tokens the tokens
   * @param target the target of the method reference
   * @param name the name of the method
   */
  public BastMethodReference(final TokenAndHistory[] tokens, final AbstractBastExpr target,
      final BastNameIdent name) {
    this(tokens, target, null, name);
  }



  /**
   * Instantiates a new method reference expression to a given method.
   *
   * @param tokens the tokens
   * @param target the target of the method reference
   * @param typeArguments the type arguments
   * @param name the name of the method
   */
  public BastMethodReference(final TokenAndHistory[] tokens, final AbstractBastExpr target,
      final LinkedList<BastTypeArgument> typeArguments, final BastNameIdent name) {
    super(tokens);
    this.referenceKind = Kind.METHOD;
    this.target = target;
    this.typeArguments = typeArguments;
    this.name = name;
    fieldMap.put(BastFieldConstants.METHOD_REFERENCE_TARGET, new BastField(target));
    fieldMap.put(BastFieldConstants.METHOD_REFERENCE_TYPEARGS, new BastField(typeArguments));
    fieldMap.put(BastFieldConstants.METHOD_REFERENCE_NAME, new BastField(name));
  }



  /**
   * Instantiates a new method reference expression to a constructor.
   *
   * @param tokens the tokens
   * @param target the target of the method reference
   */
  public BastMethodReference(final TokenAndHistory[] tokens, final AbstractBastExpr target) {
    this(tokens, target, (LinkedList<BastTypeArgument>) null);
  }



  /**
   * Instantiates a new method reference expression to a constructor.
   *
   * @param tokens the tokens
   * @param target the target of the method reference
   * @param typeArguments the type arguments
   */
  public BastMethodReference(final TokenAndHistory[] tokens, final AbstractBastExpr target,
      final LinkedList<BastTypeArgument> typeArguments) {
    super(tokens);
    this.referenceKind = Kind.CONSTRUCTOR;
    this.target = target;
    this.typeArguments = typeArguments;
    this.name = null;
    fieldMap.put(BastFieldConstants.METHOD_REFERENCE_TARGET, new BastField(target));
    fieldMap.put(BastFieldConstants.METHOD_REFERENCE_TYPEARGS, new BastField(typeArguments));
  }



  /**
   * Accept the given visitor.
   *
   * @param visitor the visitor to accept
   */
  public void accept(final IBastVisitor visitor) {
    visitor.visit(this);
  }



  /**
   * Gets the priority.
   *
   * @return the priority
   */
  public int getPriority() {
    return -1;
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
  public void replaceField(final BastFieldConstants field, final BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case METHOD_REFERENCE_TARGET:
        this.target = (AbstractBastExpr) fieldValue.getField();
        break;
      case METHOD_REFERENCE_TYPEARGS:
        this.typeArguments = (LinkedList<BastTypeArgument>) fieldValue.getListField();
        break;
      case METHOD_REFERENCE_NAME:
        this.name = (BastNameIdent) fieldValue.getField();
        break;
      default:
        assert false;
    }
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    final StringBuilder resultBuilder = new StringBuilder()
        .append(this.target)
        .append("::");

    if (this.typeArguments != null) {
      boolean first = true;

      resultBuilder.append('<');
      for (final BastTypeArgument typeArg : this.typeArguments) {
        if (!first) {
          resultBuilder.append(", ");
        }
        first = false;

        resultBuilder.append(typeArg);
      }
      resultBuilder.append('>');
    }

    if (this.referenceKind == Kind.METHOD) {
      resultBuilder.append(this.name);
    } else {
      resultBuilder.append("new");
    }

    return resultBuilder.toString();
  }
}

