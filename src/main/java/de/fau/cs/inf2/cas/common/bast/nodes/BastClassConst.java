package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

public class BastClassConst extends AbstractBastExpr {

  public static final int TAG = TagConstants.BAST_CLASS_CONST;

  /**
   * Instantiates a new bast class const.
   *
   * @param tokens the tokens
   */
  public BastClassConst(TokenAndHistory[] tokens) {
    super(tokens);
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
