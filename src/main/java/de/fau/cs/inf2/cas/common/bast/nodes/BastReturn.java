package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo. 

 */
public class BastReturn extends AbstractBastStatement {
  public static final int TAG = TagConstants.BAST_RETURN;
  public AbstractBastExpr returnValue = null;

  /**
   * Instantiates a new bast return.
   *
   * @param tokens the tokens
   * @param returnValue the return value
   */
  public BastReturn(TokenAndHistory[] tokens, AbstractBastExpr returnValue) {
    super(tokens);
    this.returnValue = returnValue;
    fieldMap.put(BastFieldConstants.RETURN_VALUE, new BastField(returnValue));
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
      case RETURN_VALUE:
        this.returnValue = (AbstractBastExpr) fieldValue.getField();
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
    StringBuilder tmp = new StringBuilder();

    tmp.append("return");

    if (returnValue != null) {
      tmp.append(" ");
      tmp.append(returnValue.toString());
    }

    return tmp.toString();
  }
}
