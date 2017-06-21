package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo.
 *
 * <p>To initialize variables at their declaration. TODO: Perhaps you can get rid of this node later
 * 
 */
public class BastExprInitializer extends AbstractBastInitializer {

  public static final int TAG = TagConstants.BAST_EXPR_INITIALIZER;
  public AbstractBastExpr init = null;

  /**
   * Instantiates a new bast expr initializer.
   *
   * @param tokens the tokens
   * @param init the init
   */
  public BastExprInitializer(TokenAndHistory[] tokens, AbstractBastExpr init) {
    super(tokens);
    this.init = init;
    fieldMap.put(BastFieldConstants.EXPR_INITIALIZER_INIT, new BastField(init));
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
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case EXPR_INITIALIZER_INIT:
        this.init = (AbstractBastExpr) fieldValue.getField();
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
    return init.toString();
  }

}
