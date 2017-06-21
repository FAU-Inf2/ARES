package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;
/**
 * todo. 
 */

public class BastAssertStmt extends AbstractBastStatement {

  public static final int TAG = TagConstants.BAST_ASSERT;

  public AbstractBastExpr firstAssert = null;
  public AbstractBastExpr secondAssert = null;

  /**
   * Instantiates a new bast assert stmt.
   *
   * @param tokens the tokens
   * @param firstAssert the first assert
   * @param secondAssert the second assert
   */
  public BastAssertStmt(TokenAndHistory[] tokens, AbstractBastExpr firstAssert,
      AbstractBastExpr secondAssert) {
    super(tokens);
    this.firstAssert = firstAssert;
    fieldMap.put(BastFieldConstants.ASSERT_STMT_FIRST_ASSERT, new BastField(firstAssert));
    this.secondAssert = secondAssert;
    fieldMap.put(BastFieldConstants.ASSERT_STMT_SECOND_ASSERT, new BastField(secondAssert));
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

}
