package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo.
 *
 * <p>Common identifier class
 * 

 */
public class BastNameIdent extends AbstractBastIdentifier {

  public static final int TAG = TagConstants.BAST_NAME_IDENT;
  public String name = null;

  /**
   * Instantiates a new bast name ident.
   *
   * @param tokens the tokens
   * @param name the name
   */
  public BastNameIdent(TokenAndHistory[] tokens, String name) {
    super(tokens);
    this.name = name;
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
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
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
    return name;
  }

}
