package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

/**
 * todo. 
 */
public class BastCharConst extends AbstractBastConstant {

  public static final int TAG = TagConstants.BAST_CHAR_CONST;
  public char value = 0;
  public String text = null;

  /**
   * Instantiates a new bast char const.
   *
   * @param tokens the tokens
   * @param value the value
   */
  public BastCharConst(TokenAndHistory[] tokens, String value) {
    super(tokens);
    if (value.startsWith("'")) {
      value = value.substring(1);
    }
    if (value.endsWith("'")) {
      value = value.substring(0, value.length() - 1);
    }
    this.text = value;
    this.value = value.toCharArray()[0];
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
    return "'" + String.valueOf(value) + "'";
  }

}
