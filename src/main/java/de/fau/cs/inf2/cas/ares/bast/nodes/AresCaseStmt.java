package de.fau.cs.inf2.cas.ares.bast.nodes;

import de.fau.cs.inf2.cas.ares.bast.visitors.IAresBastVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

public class AresCaseStmt extends AbstractAresExpr {

  public static final int TAG = TagConstants.ARES_CASE;

  public BastBlock block;

  /**
   * Instantiates a new ARES case stmt.
   *
   * @param tokens the tokens
   * @param block the block
   */
  public AresCaseStmt(TokenAndHistory[] tokens, BastBlock block) {
    super(tokens);
    this.block = block;
    fieldMap.put(BastFieldConstants.ARES_CASE_STMT_BLOCK, new BastField(block));
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
      case ARES_CASE_STMT_BLOCK:
        this.block = (BastBlock) fieldValue.getField();
        break;
      default:
        assert (false);
    }
  }

}
