package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo. 

 */
public class BastThrowStmt extends AbstractBastStatement {

  public static final int TAG = TagConstants.BAST_THROW;

  public AbstractBastExpr exception = null;

  /**
   * Instantiates a new bast throw stmt.
   *
   * @param tokens the tokens
   * @param exception the exception
   */
  public BastThrowStmt(TokenAndHistory[] tokens, AbstractBastExpr exception) {
    super(tokens);
    this.exception = exception;
    fieldMap.put(BastFieldConstants.THROW_EXCEPTION, new BastField(exception));

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
      case THROW_EXCEPTION:
        this.exception = (AbstractBastExpr) fieldValue.getField();
        break;
      default:
        assert (false);

    }
  }

}
