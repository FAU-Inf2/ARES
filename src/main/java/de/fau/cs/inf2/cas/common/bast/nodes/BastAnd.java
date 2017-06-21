package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

/**
 * todo. 
 */
public class BastAnd extends AbstractBaseBinaryExpr {

  public static final int TAG = TagConstants.BAST_AND;

  /**
   * Instantiates a new bast and.
   *
   * @param tokens the tokens
   * @param left the left
   * @param right the right
   */
  public BastAnd(TokenAndHistory[] tokens, AbstractBastExpr left, AbstractBastExpr right) {
    super(tokens, left, right);
    fieldMap.put(BastFieldConstants.AND_LEFT, new BastField(left));
    fieldMap.put(BastFieldConstants.AND_RIGHT, new BastField(right));
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
    return 8;
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
      case AND_LEFT:
        this.left = (AbstractBastExpr) fieldValue.getField();
        break;
      case AND_RIGHT:
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
  @Override
  public String toString() {
    return "(" + left + " && " + right + ")";
  }

}
