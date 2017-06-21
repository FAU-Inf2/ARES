package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

/**
 * todo. 
 */
public abstract class AbstractBastIdentifier extends AbstractBastExpr {

  AbstractBastIdentifier(TokenAndHistory[] tokens) {
    super(tokens);
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  public abstract String getName();

}
