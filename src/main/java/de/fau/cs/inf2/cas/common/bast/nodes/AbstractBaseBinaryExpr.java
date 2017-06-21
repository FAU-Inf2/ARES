package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

public abstract class AbstractBaseBinaryExpr extends AbstractBastExpr {

  public AbstractBastExpr left;
  public AbstractBastExpr right;

  AbstractBaseBinaryExpr(TokenAndHistory[] tokens, AbstractBastExpr left, AbstractBastExpr right) {
    super(tokens);
    this.left = left;
    this.right = right;
  }

}
