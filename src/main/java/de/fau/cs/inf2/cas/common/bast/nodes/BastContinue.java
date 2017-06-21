package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

/**
 * todo. 
 */
public class BastContinue extends AbstractBastStatement {

  public static final int TAG = TagConstants.BAST_CONTINUE;

  public BastNameIdent name = null;

  /**
   * Instantiates a new bast continue.
   *
   * @param tokens the tokens
   * @param name the name
   */
  public BastContinue(TokenAndHistory[] tokens, BastNameIdent name) {
    super(tokens);
    this.name = name;
    fieldMap.put(BastFieldConstants.CONTINUE_NAME, new BastField(name));
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
      case CONTINUE_NAME:
        this.name = (BastNameIdent) fieldValue.getField();
        break;
      default:
        assert (false);

    }
  }
}
