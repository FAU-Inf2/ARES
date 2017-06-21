package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

/**
 * todo. 
 */
public class BastCase extends AbstractBastExpr {

  public static final int TAG = TagConstants.BAST_CASE;
  public AbstractBastExpr caseConstant = null;

  /**
   * Instantiates a new bast case.
   *
   * @param tokens the tokens
   * @param caseConstant the case constant
   */
  public BastCase(TokenAndHistory[] tokens, AbstractBastExpr caseConstant) {
    super(tokens);
    this.caseConstant = caseConstant;
    fieldMap.put(BastFieldConstants.CASE_CONSTANT, new BastField(caseConstant));
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
      case CASE_CONSTANT:
        this.caseConstant = (AbstractBastExpr) fieldValue.getField();
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
    return "case " + caseConstant.toString() + ":";
  }

}
