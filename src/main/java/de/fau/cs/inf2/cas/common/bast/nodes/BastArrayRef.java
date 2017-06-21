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
 * <p>Read/Write access of an array element
 * 
 */
public class BastArrayRef extends AbstractBastExpr {

  public static final int TAG = TagConstants.BAST_ARRAY_REF;
  public AbstractBastExpr arrayRef = null;
  public LinkedList<AbstractBastExpr> indices = null;

  /**
   * Instantiates a new bast array ref.
   *
   * @param tokens the tokens
   * @param arrayRef the array ref
   * @param indices the indices
   */
  public BastArrayRef(TokenAndHistory[] tokens, AbstractBastExpr arrayRef,
      LinkedList<AbstractBastExpr> indices) {
    super(tokens);
    this.arrayRef = arrayRef;
    fieldMap.put(BastFieldConstants.ARRAY_REF_REF, new BastField(arrayRef));
    this.indices = indices;
    fieldMap.put(BastFieldConstants.ARRAY_REF_INDEX_LIST, new BastField(indices));
  }

  
  /**
   * Accept.
   *
   * @param visitor the visitor
   */
  public void accept(IBastVisitor visitor) {
    visitor.visit(this);
  }

  
  /**
   * Gets the priority.
   *
   * @return the priority
   */
  public int getPriority() {
    return 2;
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
      case ARRAY_REF_REF:
        this.arrayRef = (AbstractBastExpr) fieldValue.getField();
        break;
      case ARRAY_REF_INDEX_LIST:
        this.indices = (LinkedList<AbstractBastExpr>) fieldValue.getListField();
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
    if (arrayRef != null) {
      buffer.append(arrayRef.toString());
    }
    buffer.append("[");
    if (indices != null) {
      boolean comma = false;
      for (AbstractBastExpr expr : indices) {
        if (comma == true) {
          buffer.append(",");
        }
        if (comma == false) {
          comma = true;
        }
        buffer.append(expr.toString());
      }
    }
    buffer.append("]");
    return buffer.toString();
  }

}
