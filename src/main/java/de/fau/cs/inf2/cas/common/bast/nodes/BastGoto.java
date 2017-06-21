package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo. 
 */
public class BastGoto extends AbstractBastStatement {

  public static final int TAG = TagConstants.BAST_GOTO;
  public BastNameIdent label = null;

  /**
   * Instantiates a new bast goto.
   *
   * @param tokens the tokens
   * @param label the label
   */
  public BastGoto(TokenAndHistory[] tokens, BastNameIdent label) {
    super(tokens);
    this.label = label;
    fieldMap.put(BastFieldConstants.GOTO_LABEL, new BastField(label));
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

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    return "goto " + label.toString() + ";";
  }

}
