package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

public abstract class AbstractBastExpr extends AbstractAresStatement {

  /**
   * Instantiates a new abstract bast expr.
   *
   * @param tokens the tokens
   */
  public AbstractBastExpr(TokenAndHistory[] tokens) {
    super(tokens);
  }
}
