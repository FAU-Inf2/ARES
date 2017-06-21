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
 * <p>Represents a C Pointer. Can point to a second pointer and stores type information
 * (AbstractBastSpecifier)
 * 

 */
public class BastPointer extends AbstractBastExpr {

  public static final int TAG = TagConstants.BAST_POINTER;

  public LinkedList<AbstractBastSpecifier> list = null;
  public BastPointer pointer;

  /**
   * Instantiates a new bast pointer.
   *
   * @param tokens the tokens
   * @param list the list
   * @param pointer the pointer
   */
  public BastPointer(TokenAndHistory[] tokens, LinkedList<AbstractBastSpecifier> list,
      BastPointer pointer) {
    super(tokens);
    this.list = list;
    fieldMap.put(BastFieldConstants.POINTER_QUALIFIERS, new BastField(list));
    this.pointer = pointer;
    fieldMap.put(BastFieldConstants.POINTER_POINTER, new BastField(pointer));
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
