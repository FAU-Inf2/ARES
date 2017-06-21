package de.fau.cs.inf2.cas.ares.bast.nodes;

import de.fau.cs.inf2.cas.ares.bast.visitors.IAresBastVisitor;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

/**
 * todo.
 *
 * <p>Placeholder for expr Ast nodes that are removed form the tree
 * 
 */
public class AresPlaceholder extends AbstractBastExpr {

  private static final int TAG = TagConstants.ARES_PLACEHOLDER;
  
  
  
  

  /**
   * Instantiates a new ARES placeholder.
   *
   * @param tokens the tokens
   * @param type the type
   */
  public AresPlaceholder(TokenAndHistory[] tokens, int type) {
    super(tokens);
  }

  
  /**
   * Accept.
   *
   * @param visitor the visitor
   */
  public void accept(IAresBastVisitor visitor) {
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
  public String toString() {
    return "";
  }

}
