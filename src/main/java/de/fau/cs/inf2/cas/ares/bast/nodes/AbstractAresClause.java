package de.fau.cs.inf2.cas.ares.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

abstract class AbstractAresClause extends AbstractBastExpr {

  AbstractAresClause(TokenAndHistory[] tokens) {
    super(tokens);
  }

}
