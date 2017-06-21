package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * This class wraps around intializers on a module level without having an internal structure See
 * joern's CoarseSimpleDecl.g4
 * 
 */
public class BastExprWater extends AbstractBastExpr {
  public static final int TAG = TagConstants.BAST_EXPR_WATER;
  public String content;

  /**
   * Instantiates a new bast expr water.
   *
   * @param tokens the tokens
   * @param content the content
   */
  public BastExprWater(TokenAndHistory[] tokens, String content) {
    super(tokens);

    this.content = content;
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
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return content;
  }
}
