package de.fau.cs.inf2.cas.ares.bast.nodes;

import de.fau.cs.inf2.cas.ares.bast.visitors.IAresBastVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

public class AresChoiceStmt extends AbstractAresExpr {

  public static final int TAG = TagConstants.ARES_CHOICE;

  public BastBlock choiceBlock;

  /**
   * Instantiates a new ARES choice stmt.
   *
   * @param tokens the tokens
   * @param choiceBlock the choice block
   */
  public AresChoiceStmt(TokenAndHistory[] tokens, BastBlock choiceBlock) {
    super(tokens);
    this.choiceBlock = choiceBlock;
    fieldMap.put(BastFieldConstants.ARES_CHOICE_STMT_BLOCK, new BastField(choiceBlock));
  }

  
  /**
   * Accept.
   *
   * @param visitor the visitor
   */
  public void accept(IAresBastVisitor visitor) {
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
      case ARES_CHOICE_STMT_BLOCK:
        this.choiceBlock = (BastBlock) fieldValue.getField();
        break;
      default:
        assert (false);
    }
  }

}
