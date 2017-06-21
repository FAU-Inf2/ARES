package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo.
 *
 * <p>Multiplicative Expression
 * 

 */
public class BastMultiExpr extends AbstractBaseBinaryExpr {

  public enum Types {
    TYPE_MUL, TYPE_DIV, TYPE_MOD, TYPE_EXP;

    /**
     * To_int.
     *
     * @return the int
     */
    public int to_int() {
      return this.ordinal();
    }
  }

  public static final int TAG = TagConstants.BAST_MULTI_EXPR;

  public Types type = Types.TYPE_MUL;

  /**
   * Instantiates a new bast multi expr.
   *
   * @param tokens the tokens
   * @param left the left
   * @param right the right
   * @param type the type
   */
  public BastMultiExpr(TokenAndHistory[] tokens, AbstractBastExpr left, AbstractBastExpr right,
      Types type) {
    super(tokens, left, right);
    this.type = type;
    fieldMap.put(BastFieldConstants.MULTI_EXPR_LEFT, new BastField(left));
    fieldMap.put(BastFieldConstants.MULTI_EXPR_RIGHT, new BastField(right));
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
    return 3;
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
      case MULTI_EXPR_LEFT:
        this.left = (AbstractBastExpr) fieldValue.getField();
        break;
      case MULTI_EXPR_RIGHT:
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
    switch (type) {
      case TYPE_DIV:
        return "(" + this.left + " / " + this.right + ")";
      case TYPE_EXP:
        return "(" + this.left + " *exp* " + this.right + ")";
      case TYPE_MOD:
        return "(" + this.left + " % " + this.right + ")";
      case TYPE_MUL:
        return "(" + this.left + " * " + this.right + ")";
      default:
        assert false;
        return "";
    }
  }
}
