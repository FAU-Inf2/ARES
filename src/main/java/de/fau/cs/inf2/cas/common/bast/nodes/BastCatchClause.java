package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

/**
 * todo. 
 */
public class BastCatchClause extends AbstractBastExpr {

  public static final int TAG = TagConstants.BAST_CATCH;
  public BastDeclaration decl = null;
  public AbstractBastStatement block = null;

  /**
   * Instantiates a new bast catch clause.
   *
   * @param tokens the tokens
   * @param decl the decl
   * @param block the block
   */
  public BastCatchClause(TokenAndHistory[] tokens, BastDeclaration decl,
      AbstractBastStatement block) {
    super(tokens);
    this.block = block;
    fieldMap.put(BastFieldConstants.CATCH_CLAUSE_BLOCK, new BastField(block));
    this.decl = decl;
    fieldMap.put(BastFieldConstants.CATCH_CLAUSE_DECL, new BastField(decl));
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
      case CATCH_CLAUSE_BLOCK:
        this.block = (AbstractBastStatement) fieldValue.getField();
        break;
      case CATCH_CLAUSE_DECL:
        this.decl = (BastDeclaration) fieldValue.getField();
        break;
      default:
        assert (false);

    }
  }

}
