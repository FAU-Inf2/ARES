package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

/**
 * todo. 

 */
public class BastNullConst extends AbstractBastConstant {

  public static final int TAG = TagConstants.BAST_NULL_CONST;
  

  /**
   * Instantiates a new bast null const.
   *
   * @param tokens the tokens
   */
  public BastNullConst(TokenAndHistory[] tokens) {
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
   * Gets the priority.
   *
   * @return the priority
   */
  public int getPriority() {
    return 0;
  }

  /**
   * Gets the tag.
   *
   * @return the tag
   */
  public int getTag() {
    return TAG;
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    return "null";
  }
}
