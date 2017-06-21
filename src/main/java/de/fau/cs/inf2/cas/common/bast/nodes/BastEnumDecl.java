package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;


public class BastEnumDecl extends AbstractBastInternalDecl {

  public static final int TAG = TagConstants.BAST_ENUM_DECL;
  public BastEnumSpec enumerator = null;
  public LinkedList<AbstractBastSpecifier> modifiers = null;

  /**
   * Instantiates a new bast enum decl.
   *
   * @param tokens the tokens
   * @param enumerator the enumerator
   */
  public BastEnumDecl(TokenAndHistory[] tokens, BastEnumSpec enumerator) {
    super(tokens);
    this.enumerator = enumerator;
    fieldMap.put(BastFieldConstants.ENUM_DECL_ENUMERATOR, new BastField(enumerator));
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
  @SuppressWarnings("unchecked")
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case ENUM_DECL_ENUMERATOR:
        this.enumerator = (BastEnumSpec) fieldValue.getField();
        break;
      case ENUM_DECL_MODIFIERS:
        this.modifiers = (LinkedList<AbstractBastSpecifier>) fieldValue.getListField();
        break;
      default:
        assert (false);

    }
  }

  
  /**
   * Sets the modifiers.
   *
   * @param modifiers the new modifiers
   */
  @Override
  public void setModifiers(LinkedList<AbstractBastSpecifier> modifiers) {
    this.modifiers = modifiers;
    fieldMap.put(BastFieldConstants.ENUM_DECL_MODIFIERS, new BastField(modifiers));
  }

}
