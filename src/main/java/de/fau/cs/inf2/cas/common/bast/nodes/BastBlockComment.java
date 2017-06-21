package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;
/**
 * todo. 
 */

public class BastBlockComment extends AbstractBastComment {

  public static final int TAG = TagConstants.BAST_BLOCK_COMMENT;

  /**
   * Instantiates a new bast block comment.
   *
   * @param tokens the tokens
   */
  @SuppressWarnings("ucd")
  public BastBlockComment(TokenAndHistory[] tokens) {
    super(tokens);
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

}
