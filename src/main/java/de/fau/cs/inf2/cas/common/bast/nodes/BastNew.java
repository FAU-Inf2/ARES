package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.type.BastType;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;


public class BastNew extends AbstractBastExpr {

  public static final int TAG = TagConstants.BAST_NEW;
  private static final int TYPE_CLASS = 0;
  public static final int TYPE_ARRAY = 1;

  public LinkedList<BastType> typeArguments = null;
  public BastType type = null;
  public LinkedList<AbstractBastExpr> parameters = null;
  public LinkedList<AbstractBastInternalDecl> declarations = null;

  public int newType = TYPE_CLASS;

  /**
   * Instantiates a new bast new.
   *
   * @param tokens the tokens
   * @param typeArguments the type arguments
   * @param type the type
   * @param parameters the parameters
   * @param declarations the declarations
   */
  public BastNew(TokenAndHistory[] tokens, LinkedList<BastType> typeArguments, BastType type,
      LinkedList<AbstractBastExpr> parameters, LinkedList<AbstractBastInternalDecl> declarations) {
    super(tokens);
    this.typeArguments = typeArguments;
    fieldMap.put(BastFieldConstants.NEW_CLASS_TYPE_ARGUMENTS, new BastField(typeArguments));
    this.type = type;
    fieldMap.put(BastFieldConstants.NEW_CLASS_TYPE, new BastField(type));
    this.parameters = parameters;
    fieldMap.put(BastFieldConstants.NEW_CLASS_PARAMETERS, new BastField(parameters));
    this.declarations = declarations;
    fieldMap.put(BastFieldConstants.NEW_CLASS_DECLARATIONS, new BastField(declarations));

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
      case NEW_CLASS_TYPE_ARGUMENTS:
        this.typeArguments = (LinkedList<BastType>) fieldValue.getListField();
        break;
      case NEW_CLASS_TYPE:
        this.type = (BastType) fieldValue.getField();
        break;
      case NEW_CLASS_PARAMETERS:
        this.parameters = (LinkedList<AbstractBastExpr>) fieldValue.getListField();
        break;
      case NEW_CLASS_DECLARATIONS:
        this.declarations = (LinkedList<AbstractBastInternalDecl>) fieldValue.getListField();
        break;
      default:
        assert (false);
    }
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("new ");
    if (type != null) {
      builder.append(type);
    }
    builder.append("(");
    if (parameters != null) {
      boolean comma = false;
      for (AbstractBastExpr expr : parameters) {
        if (comma) {
          builder.append(", ");
        } else {
          comma = true;
        }
        builder.append(expr);
      }
    }
    builder.append(")");
    return builder.toString();
  }

}
