package de.fau.cs.inf2.cas.common.bast.type;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;



/**
 * todo.
 *

 */
public class BastPointerType extends BastType {
  public static final int TAG = TagConstants.TYPE_POINTER;

  private static final int TYPE_TAG = TagConstants.TYPE_POINTER;

  public BastType type = null;

  /**
   * Instantiates a new bast pointer type.
   *
   * @param tokens the tokens
   * @param type the type
   */
  @SuppressWarnings("ucd")
  public BastPointerType(TokenAndHistory[] tokens, BastType type) {
    super(tokens);
    this.type = type;
  }

  /**
   * Accept.
   *
   * @param visitor the visitor
   */
  @Override
  public void accept(IBastVisitor visitor) {
    assert (false);

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
   * Gets the type tag.
   *
   * @return the type tag
   */
  public int getTypeTag() {
    return TYPE_TAG;
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    return "ptr to " + type;
  }
}
