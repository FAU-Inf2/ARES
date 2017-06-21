package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo. 

 */
public class BastSynchronizedBlock extends AbstractBastStatement {

  public static final int TAG = TagConstants.BAST_SYNCHRONIZED_BLOCK;

  public AbstractBastExpr expr = null;
  public BastBlock block = null;

  /**
   * Instantiates a new bast synchronized block.
   *
   * @param tokens the tokens
   * @param expr the expr
   * @param block the block
   */
  public BastSynchronizedBlock(TokenAndHistory[] tokens, AbstractBastExpr expr, BastBlock block) {
    super(tokens);
    this.expr = expr;
    fieldMap.put(BastFieldConstants.SYNCHRONIZED_BLOCK_EXPR, new BastField(expr));
    this.block = block;
    fieldMap.put(BastFieldConstants.SYNCHRONIZED_BLOCK_BLOCK, new BastField(block));
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
      case SYNCHRONIZED_BLOCK_EXPR:
        this.expr = (AbstractBastExpr) fieldValue.getField();
        break;
      case SYNCHRONIZED_BLOCK_BLOCK:
        this.block = (BastBlock) fieldValue.getField();
        break;
      default:
        assert (false);

    }
  }

}
