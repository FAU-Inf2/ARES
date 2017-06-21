package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;
/**
 * todo.
 *
 * <p>An assignment what else?
 * 
 */

public class BastAsgnExpr extends AbstractBaseBinaryExpr {
  public static final int TAG = TagConstants.BAST_ASGN_EXPR;
  public static final int EXPR_TYPE_EQUAL = 0;
  public static final int EXPR_TYPE_MULTIPLY_EQUAL = 1;
  public static final int EXPR_TYPE_DIVIDE_EQUAL = 2;
  public static final int EXPR_TYPE_REMAINDER_EQUAL = 3;
  public static final int EXPR_TYPE_PLUS_EQUAL = 4;
  public static final int EXPR_TYPE_MINUS_EQUAL = 5;
  public static final int EXPR_TYPE_SLL_EQUAL = 6;
  public static final int EXPR_TYPE_SLR_EQUAL = 7;
  public static final int EXPR_TYPE_AND_EQUAL = 8;
  public static final int EXPR_TYPE_XOR_EQUAL = 9;
  public static final int EXPR_TYPE_OR_EQUAL = 10;
  public static final int EXPR_TYPE_SAR_EQUAL = 11;

  public int operation = EXPR_TYPE_EQUAL;

  /**
   * Instantiates a new bast asgn expr.
   *
   * @param tokens the tokens
   * @param left the left
   * @param right the right
   */
  public BastAsgnExpr(TokenAndHistory[] tokens, AbstractBastExpr left, AbstractBastExpr right) {
    super(tokens, left, right);
    fieldMap.put(BastFieldConstants.ASGN_EXPR_LEFT, new BastField(left));
    fieldMap.put(BastFieldConstants.ASGN_EXPR_RIGHT, new BastField(right));
  }

  /**
   * Instantiates a new bast asgn expr.
   *
   * @param tokens the tokens
   * @param left the left
   * @param right the right
   * @param operation the operation
   */
  public BastAsgnExpr(TokenAndHistory[] tokens, AbstractBastExpr left, AbstractBastExpr right,
      int operation) {
    super(tokens, left, right);
    this.operation = operation;
    fieldMap.put(BastFieldConstants.ASGN_EXPR_LEFT, new BastField(left));
    fieldMap.put(BastFieldConstants.ASGN_EXPR_RIGHT, new BastField(right));
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
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case ASGN_EXPR_LEFT:
        this.left = (AbstractBastExpr) fieldValue.getField();
        break;
      case ASGN_EXPR_RIGHT:
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
    String op = null;
    switch (operation) {
      case EXPR_TYPE_EQUAL:
        op = "=";
        break;
      case EXPR_TYPE_MULTIPLY_EQUAL:
        op = "*=";
        break;
      case EXPR_TYPE_DIVIDE_EQUAL:
        op = "/=";
        break;
      case EXPR_TYPE_REMAINDER_EQUAL:
        op = "%=";
        break;
      case EXPR_TYPE_PLUS_EQUAL:
        op = "+=";
        break;
      case EXPR_TYPE_MINUS_EQUAL:
        op = "-=";
        break;
      case EXPR_TYPE_SLL_EQUAL:
        op = "<<<=";
        break;
      case EXPR_TYPE_SLR_EQUAL:
        op = "<<=";
        break;
      case EXPR_TYPE_AND_EQUAL:
        op = "&=";
        break;
      case EXPR_TYPE_XOR_EQUAL:
        op = "^=";
        break;
      case EXPR_TYPE_OR_EQUAL:
        op = "|=";
        break;
      case EXPR_TYPE_SAR_EQUAL:
        op = ">>=";
        break;
      default:
        break;
    }
    return super.left + " " + op + " " + super.right;
  }
}
