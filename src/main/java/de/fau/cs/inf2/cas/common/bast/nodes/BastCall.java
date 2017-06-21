package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;

/**
 * todo. 
 */
public class BastCall extends AbstractBastExpr {

  public static final int TAG = TagConstants.BAST_CALL;
  public AbstractBastExpr function = null;
  public LinkedList<AbstractBastExpr> arguments = null;

  /**
   * Instantiates a new bast call.
   *
   * @param tokens the tokens
   * @param function the function
   * @param arguments the arguments
   */
  public BastCall(TokenAndHistory[] tokens, AbstractBastExpr function,
      LinkedList<AbstractBastExpr> arguments) {
    super(tokens);
    this.function = function;
    this.arguments = arguments;
    fieldMap.put(BastFieldConstants.DIRECT_CALL_ARGUMENTS, new BastField(arguments));
    fieldMap.put(BastFieldConstants.DIRECT_CALL_FUNCTION, new BastField(function));
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
  @SuppressWarnings("unchecked")
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case DIRECT_CALL_ARGUMENTS:
        this.arguments = (LinkedList<AbstractBastExpr>) fieldValue.getListField();
        break;
      case DIRECT_CALL_FUNCTION:
        this.function = (AbstractBastExpr) fieldValue.getField();
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
    return function.toString() + "(" + (this.arguments != null ? this.arguments.size() : 0) + ")";
  }

}
