package de.fau.cs.inf2.cas.common.bast.type;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastListInitializer;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;

/**
 * todo.
 *
 * <p>General array type ;)
 * 

 */
public class BastArrayType extends BastType {

  public static final int TAG = TagConstants.BAST_ARRAY_TYPE;
  private static final int TYPE_TAG = TagConstants.TYPE_ARRAY;
  public LinkedList<AbstractBastExpr> dimensions = null;
  public BastType type = null;
  public BastListInitializer initializer = null;
  public int dimensionNumber = -1;

  /**
   * todo.
   * Constructor for Java array. If no dimension size is specified (e.g. int[4][5]), dimensionNumber
   * is used to specify the number of dimensions.
   * 
   * @param tokens the tokens
   * @param type the type
   * @param dimensions todo
   * @param dimensionNumber todo
   */
  public BastArrayType(TokenAndHistory[] tokens, BastType type,
      LinkedList<AbstractBastExpr> dimensions, int dimensionNumber) {
    super(tokens);
    this.dimensions = null;
    this.type = type;
    fieldMap.put(BastFieldConstants.ARRAY_TYPE_TYPE, new BastField(type));
    this.dimensions = dimensions;
    fieldMap.put(BastFieldConstants.ARRAY_TYPE_DIMENSIONS, new BastField(dimensions));

    this.dimensionNumber = dimensionNumber;
  }

  /**
   * todo.
   * Constructor for Java array. If no dimension size is specified (e.g. int[4][5]), dimensionNumber
   * is used to specify the number of dimensions.
   * 
   * @param tokens the tokens
   * @param type the type
   * @param dimensions todo
   * @param dimensionNumber todo
   * @param initializer the initializer
   */
  public BastArrayType(TokenAndHistory[] tokens, BastType type,
      LinkedList<AbstractBastExpr> dimensions, int dimensionNumber,
      BastListInitializer initializer) {
    super(tokens);
    this.dimensions = null;
    this.type = type;
    fieldMap.put(BastFieldConstants.ARRAY_TYPE_TYPE, new BastField(type));
    this.dimensions = dimensions;
    fieldMap.put(BastFieldConstants.ARRAY_TYPE_DIMENSIONS, new BastField(dimensions));
    this.dimensionNumber = dimensionNumber;
    this.initializer = initializer;
    fieldMap.put(BastFieldConstants.ARRAY_TYPE_INITIALIZER, new BastField(initializer));
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

  public int getTypeTag() {
    return TYPE_TAG;
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
      case ARRAY_TYPE_TYPE:
        this.type = (BastType) fieldValue.getField();
        break;
      case ARRAY_TYPE_DIMENSIONS:
        this.dimensions = (LinkedList<AbstractBastExpr>) fieldValue.getListField();
        break;
      case ARRAY_TYPE_INITIALIZER:
        this.initializer = (BastListInitializer) fieldValue.getField();
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
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("array [");
    if (dimensions != null) {
      boolean comma = false;
      for (AbstractBastExpr expr : dimensions) {
        if (comma == true) {
          buffer.append(", ");
        }
        if (comma == false) {
          comma = true;
        }
        buffer.append(expr);
      }
    }
    buffer.append("] of ");
    buffer.append(type);
    return buffer.toString();

  }

}
