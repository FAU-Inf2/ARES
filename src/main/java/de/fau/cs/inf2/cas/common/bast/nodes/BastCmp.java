package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

/**
 * todo. 
 */
public class BastCmp extends AbstractBaseBinaryExpr {

  public static final int TAG = TagConstants.BAST_CMP;
  public static final int EQUAL = 0;
  public static final int NOT_EQUAL = 1;
  public static final int LESS = 2;
  public static final int GREATER = 3;
  public static final int LESS_EQUAL = 4;
  public static final int GREATER_EQUAL = 5;

  public int operation = -1;

  /**
   * Instantiates a new bast cmp.
   *
   * @param tokens the tokens
   * @param left the left
   * @param right the right
   * @param operation the operation
   */
  public BastCmp(TokenAndHistory[] tokens, AbstractBastExpr left, AbstractBastExpr right,
      int operation) {
    super(tokens, left, right);
    fieldMap.put(BastFieldConstants.CMP_EXPR_LEFT, new BastField(left));
    fieldMap.put(BastFieldConstants.CMP_EXPR_RIGHT, new BastField(right));
    this.operation = operation;
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
    return 6;
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
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case CMP_EXPR_LEFT:
        this.left = (AbstractBastExpr) fieldValue.getField();
        break;
      case CMP_EXPR_RIGHT:
        this.right = (AbstractBastExpr) fieldValue.getField();
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
    buffer.append(left.toString());
    switch (operation) {
      case BastCmp.EQUAL:
        buffer.append(" == ");
        break;
      case BastCmp.NOT_EQUAL:
        buffer.append(" != ");
        break;
      case BastCmp.LESS:
        buffer.append(" < ");
        break;
      case BastCmp.LESS_EQUAL:
        buffer.append(" <= ");
        break;
      case BastCmp.GREATER:
        buffer.append(" > ");
        break;
      case BastCmp.GREATER_EQUAL:
        buffer.append(" >= ");
        break;
      default:
        break;
    }
    buffer.append(right.toString());
    return buffer.toString();
  }

}
