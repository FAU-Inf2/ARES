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
public class BastBlock extends AbstractBastInternalDecl {

  public static final int TAG = TagConstants.BAST_BLOCK;
  public LinkedList<AbstractBastStatement> statements = null;
  public boolean isStatic = false;
  @SuppressWarnings("unused")
  private LinkedList<AbstractBastSpecifier> modifiers = null;

  /**
   * Instantiates a new bast block.
   *
   * @param tokens the tokens
   * @param statements the statements
   */
  public BastBlock(TokenAndHistory[] tokens, LinkedList<AbstractBastStatement> statements) {
    super(tokens);
    if (statements == null) {
      statements = new LinkedList<>();
    }
    this.statements = statements;
    fieldMap.put(BastFieldConstants.BLOCK_STATEMENT, new BastField(statements));
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
  @SuppressWarnings("unchecked")
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case BLOCK_STATEMENT:

        this.statements = (LinkedList<AbstractBastStatement>) fieldValue.getListField();
        assert (statements != null);
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
    fieldMap.put(BastFieldConstants.BLOCK_MODIFIERS, new BastField(modifiers));

  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    String val = "{";
    if (statements != null && this.statements.size() > 0) {
      val += this.statements.get(0).toString();
    }
    if (statements != null && this.statements.size() > 1) {
      val += " ... ";
      val += this.statements.get(this.statements.size() - 1).toString();
    }
    val += " }";
    return val;
  }

}
