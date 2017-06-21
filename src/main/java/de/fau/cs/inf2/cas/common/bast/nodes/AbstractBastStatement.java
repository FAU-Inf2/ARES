package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

/**
 * todo. 
 */
public abstract class AbstractBastStatement extends AbstractBastNode {

  /**
   * Instantiates a new abstract bast statement.
   *
   * @param tokens the tokens
   */
  AbstractBastStatement(TokenAndHistory[] tokens) {
    super(tokens);
  }

}
