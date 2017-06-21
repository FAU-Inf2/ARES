package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


public class BastNamespaceIdent extends BastNameIdent {
  private static final int TAG = TagConstants.BAST_NAMESPACE_IDENT;
  private BastNameIdent next = null;

  /**
   * Instantiates a new bast namespace ident.
   *
   * @param tokens the tokens
   * @param name the name
   * @param next the next
   */
  public BastNamespaceIdent(TokenAndHistory[] tokens, String name, BastNameIdent next) {
    super(tokens, name);
    this.next = next;
    fieldMap.put(BastFieldConstants.NEXT_NAME_IDENT, new BastField(next));
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
    return name + "::" + next;
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
    return getName();
  }
}
