package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.type.BastType;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo.. 

 */
public class BastTypeSpecifier extends AbstractBastSpecifierQualifier {

  public static final int TAG = TagConstants.BAST_TYPE_SPECIFIER;

  public BastType type = null;

  /**
   * Instantiates a new bast type specifier.
   *
   * @param tokens the tokens
   * @param type the type
   */
  public BastTypeSpecifier(TokenAndHistory[] tokens, BastType type) {
    super(tokens);
    if (type != null) {
      this.line = type.line;
      this.column = type.column;
    }
    this.type = type;
    fieldMap.put(BastFieldConstants.TYPE_SPECIFIER_TYPE, new BastField(type));
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
      case TYPE_SPECIFIER_TYPE:
        this.type = (BastType) fieldValue.getField();
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
    return type.toString();
  }
}
