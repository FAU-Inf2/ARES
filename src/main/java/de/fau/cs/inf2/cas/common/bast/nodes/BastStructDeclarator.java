package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo.
 *
 * <p>Declarator of a C struct
 * 

 */
public class BastStructDeclarator extends AbstractBastDeclarator {

  public static final int TAG = TagConstants.BAST_STRUCT_DECLARATOR;
  public AbstractBastDeclarator decl = null;
  public AbstractBastExpr constant = null;
  public AbstractBastInitializer init = null;
  public BastPointer pointer = null;

  /**
   * todo.
   *
   * @param tokens the tokens
   * @param decl todo
   * @param constant The struct can be initialized with a constant
   */
  @Deprecated
  public BastStructDeclarator(TokenAndHistory[] tokens, AbstractBastDeclarator decl,
      AbstractBastExpr constant) {
    super(tokens);
    this.decl = decl;
    this.constant = constant;
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
   * Sets the initializer.
   *
   * @param init the new initializer
   */
  @Override
  public void setInitializer(AbstractBastInitializer init) {
    this.init = init;

  }

  
  /**
   * Sets the pointer.
   *
   * @param pointer the new pointer
   */
  @Override
  public void setPointer(BastPointer pointer) {
    this.pointer = pointer;

  }

}
