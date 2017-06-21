package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo.
 *

 */
public class BastShift extends AbstractBaseBinaryExpr {

  public static final int TAG = TagConstants.BAST_SHIFT;
  public static final int TYPE_SAR = 0;
  public static final int TYPE_SLR = 1;
  public static final int TYPE_SLL = 2;
  public int type;

  /**
   * Instantiates a new bast shift.
   *
   * @param tokens the tokens
   * @param left the left
   * @param right the right
   * @param type the type
   */
  public BastShift(TokenAndHistory[] tokens, AbstractBastExpr left, AbstractBastExpr right,
      int type) {
    super(tokens, left, right);
    this.type = type;
    fieldMap.put(BastFieldConstants.SHIFT_LEFT, new BastField(left));
    fieldMap.put(BastFieldConstants.SHIFT_RIGHT, new BastField(right));
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
    return 5;
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
    switch (type) {
      case TYPE_SAR:
        return "(" + left + " >> " + right + ")";
      case TYPE_SLL:
        return "(" + left + " << " + right + ")";
      case TYPE_SLR:
        return "(" + left + " >>> " + right + ")";
      default:
        assert false;
        return "";
    }
  }

}
