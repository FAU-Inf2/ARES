package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;


/**
 * todo.
 *
 * <p>Temporary node for the c include statement until a preprocessor is implemented
 * 
 */
public class BastIncludeStmt extends AbstractBastExternalDecl {

  public static final int TAG = TagConstants.BAST_INCLUDE;
  public LinkedList<BastNameIdent> list = null;

  /**
   * Instantiates a new bast include stmt.
   *
   * @param tokens the tokens
   * @param list the list
   */
  @Deprecated
  public BastIncludeStmt(TokenAndHistory[] tokens, LinkedList<BastNameIdent> list) {
    super(tokens);
    this.list = list;
    fieldMap.put(BastFieldConstants.INCLUDE_STMT_NAME_PART, new BastField(list));
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
   * Sets the modifiers.
   *
   * @param modifiers the new modifiers
   */
  @Override
  public void setModifiers(LinkedList<AbstractBastSpecifier> modifiers) {
    assert (false);

  }

}
