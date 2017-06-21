package de.fau.cs.inf2.cas.ares.pcreation;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

public class InsertResult {
  public ReplaceMap replaceMap;

  InsertResult(AbstractBastNode node, ReplaceMap replaceMap) {
    this.replaceMap = replaceMap;
  }
}
