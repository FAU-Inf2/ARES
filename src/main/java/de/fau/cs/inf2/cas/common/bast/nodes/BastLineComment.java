package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo. 
 */
public class BastLineComment extends AbstractBastComment {

  public static final int TAG = TagConstants.BAST_LINE_COMMENT;

  /**
   * Instantiates a new bast line comment.
   *
   * @param tokens the tokens
   * @param endLine the end line
   * @param endColumn the end column
   */
  @SuppressWarnings("ucd")
  public BastLineComment(TokenAndHistory[] tokens, int endLine, int endColumn) {
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
