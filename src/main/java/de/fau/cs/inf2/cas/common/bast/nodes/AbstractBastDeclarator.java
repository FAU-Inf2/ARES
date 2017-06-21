package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

/**
 * todo. 
 */
public abstract class AbstractBastDeclarator extends AbstractBastExpr {

  AbstractBastDeclarator(TokenAndHistory[] tokens) {
    super(tokens);
  }

  /**
   * Sets the initializer.
   *
   * @param init the new initializer
   */
  public abstract void setInitializer(AbstractBastInitializer init);

  

  /**
   * Sets the pointer.
   *
   * @param pointer the new pointer
   */
  public abstract void setPointer(BastPointer pointer);
}
