package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;
/**
 * todo. 
 */

public class BastBreak extends AbstractBastStatement {

  public static final int TAG = TagConstants.BAST_BREAK;

  public BastNameIdent name = null;

  /**
   * Instantiates a new bast break.
   *
   * @param tokens the tokens
   * @param name the name
   */
  public BastBreak(TokenAndHistory[] tokens, BastNameIdent name) {
    super(tokens);
    this.name = name;
    fieldMap.put(BastFieldConstants.BREAK_NAME, new BastField(name));
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

}
