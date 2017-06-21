package de.fau.cs.inf2.cas.common.util;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.List;

public class ChildrenList {
  public final List<AbstractBastNode> list;
  public final BastFieldConstants fieldConstant;

  ChildrenList(List<AbstractBastNode> emptyList, BastFieldConstants fieldConstant) {
    this.list = emptyList;
    this.fieldConstant = fieldConstant;
  }
}
