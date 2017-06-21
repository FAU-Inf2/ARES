package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;


/**
 * todo.
 *

 */
public class BastParameter extends AbstractBastExternalDecl {
  public static final int TAG = TagConstants.BAST_PARAMETER;
  public String name = null;
  public boolean isEllipsis = false;

  public LinkedList<AbstractBastSpecifier> specifiers;
  public AbstractBastDeclarator declarator;

  /**
   * Instantiates a new bast parameter.
   *
   * @param tokens the tokens
   * @param specifiers the specifiers
   * @param declarator the declarator
   */
  public BastParameter(TokenAndHistory[] tokens, LinkedList<AbstractBastSpecifier> specifiers,
      AbstractBastDeclarator declarator) {
    super(tokens);
    this.specifiers = specifiers;
    fieldMap.put(BastFieldConstants.PARAMETER_SPECIFIERS, new BastField(specifiers));
    this.declarator = declarator;
    fieldMap.put(BastFieldConstants.PARAMETER_DECLARATOR, new BastField(declarator));
    if (declarator != null && declarator.getTag() == BastIdentDeclarator.TAG) {
      BastIdentDeclarator tmp = (BastIdentDeclarator) declarator;
      name = tmp.identifier.getName();
    }

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
    assert (false);
    return 0;
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
      case PARAMETER_SPECIFIERS:
        this.specifiers = (LinkedList<AbstractBastSpecifier>) fieldValue.getListField();
        break;
      case PARAMETER_DECLARATOR:
        this.declarator = (AbstractBastDeclarator) fieldValue.getField();
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
    assert (false);
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    StringBuilder tmp = new StringBuilder();
    for (AbstractBastSpecifier spec : specifiers) {
      tmp.append(spec.toString());
      tmp.append(" ");
    }

    tmp.append(declarator.toString());

    return tmp.toString();
  }
}
