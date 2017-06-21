package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

/**
 * todo. 
 */
public abstract class AbstractBastUnaryExpr extends AbstractBastExpr {

  public AbstractBastExpr operand;

  AbstractBastUnaryExpr(TokenAndHistory[] tokens, AbstractBastExpr operand) {
    super(tokens);
    this.operand = operand;
  }

}
