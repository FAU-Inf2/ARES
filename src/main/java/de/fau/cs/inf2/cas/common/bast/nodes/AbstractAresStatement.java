package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

public abstract class AbstractAresStatement extends AbstractBastStatement {

  /**
   * Instantiates a new abstract ARES statement.
   *
   * @param tokens the tokens
   */
  public AbstractAresStatement(TokenAndHistory[] tokens) {
    super(tokens);
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

}
