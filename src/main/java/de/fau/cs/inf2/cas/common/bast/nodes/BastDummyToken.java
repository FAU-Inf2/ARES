package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

/**
 * This class is only used as a temporary placeholder during Bast generation in CParser.
 */
public class BastDummyToken extends AbstractBastStatement {

  public static final int TAG = TagConstants.BAST_DUMMY_TOKEN;
  public static final int ELSE = 1;
  public static final int DO = 2;
  public static final int OPENING_CURLY = 3;
  public int type;

  public BastDummyToken(int type) {
    super(null);
    this.type = type;
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
    switch (type) {
      case ELSE:
        return "else";
      case DO:
        return "do";
      case OPENING_CURLY:
        return "{";
      default:
        assert false;
        return "";
    }
  }
}

