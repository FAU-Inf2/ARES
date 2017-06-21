package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.type.BastType;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


public class BastTypeArgument extends BastType {

  public static final int TAG = TagConstants.BAST_TYPE_ARGUMENT;

  public static final int EXTENDS_TYPE = 0;
  public static final int SUPER_TYPE = 1;
  public static final int QUESTION_TYPE = 2;
  public BastType type = null;
  public int extendsType = -1;

  /**
   * Instantiates a new bast type argument.
   *
   * @param tokens the tokens
   * @param extendsType the extends type
   * @param type the type
   */
  public BastTypeArgument(TokenAndHistory[] tokens, int extendsType, BastType type) {
    super(tokens);
    this.type = type;
    fieldMap.put(BastFieldConstants.TYPE_ARGUMENT_TYPE, new BastField(type));
    this.extendsType = extendsType;
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
      case TYPE_ARGUMENT_TYPE:
        this.type = (BastType) fieldValue.getField();
        break;
      default:
        assert (false);

    }
  }

}
