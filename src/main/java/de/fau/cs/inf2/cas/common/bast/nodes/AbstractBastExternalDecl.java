package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;

/**
 * todo. 
 */
public abstract class AbstractBastExternalDecl extends AbstractBastStatement {

  AbstractBastExternalDecl(TokenAndHistory[] tokens) {
    super(tokens);
  }

  /**
   * Sets the modifiers.
   *
   * @param modifiers the new modifiers
   */
  public abstract void setModifiers(LinkedList<AbstractBastSpecifier> modifiers);

}
