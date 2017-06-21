package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.type.BastType;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo.
 * 

 */
public class BastStructMember extends AbstractBastExpr {

  public static final int TAG = TagConstants.BAST_STRUCT_MEMBER;
  public BastType type;
  public String name;
  public BastIntConst position;

  /**
   * todo.
   * 
   * @param name name of the struct entry
   *
   * @param tokens the tokens
   * @param type type of the entry
   * @param position bit position of the entry
   */
  @Deprecated
  public BastStructMember(TokenAndHistory[] tokens, String name, BastType type,
      BastIntConst position) {
    super(tokens);
    this.type = type;
    this.name = name;
    this.position = position;
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
    assert (false);
    return -1;
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
