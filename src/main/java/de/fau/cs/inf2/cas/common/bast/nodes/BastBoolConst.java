package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;
/**
 * todo. 
 */

public class BastBoolConst extends AbstractBastConstant {

  public static final int TAG = TagConstants.BAST_BOOL_CONST;
  public boolean value = false;

  /**
   * Instantiates a new bast bool const.
   *
   * @param tokens the tokens
   * @param value the value
   */
  public BastBoolConst(TokenAndHistory[] tokens, boolean value) {
    super(tokens);
    this.value = value;
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
    return String.valueOf(value);
  }

}
