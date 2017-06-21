package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

/**
 * todo. 
 */
public class BastDecrExpr extends AbstractBastUnaryExpr {

  public static final int TAG = TagConstants.BAST_DECR_EXPR;

  /**
   * Instantiates a new bast decr expr.
   *
   * @param tokens the tokens
   * @param operand the operand
   */
  public BastDecrExpr(TokenAndHistory[] tokens, AbstractBastExpr operand) {
    super(tokens, operand);
    fieldMap.put(BastFieldConstants.DECR_EXPR_OPERAND, new BastField(operand));
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
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return "--" + operand;
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
      case DECR_EXPR_OPERAND:
        this.operand = (AbstractBastExpr) fieldValue.getField();
        break;
      default:
        assert (false);

    }
  }
}
