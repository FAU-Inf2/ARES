package de.fau.cs.inf2.cas.common.bast.general;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

public class NodeParentInformation {
  public final BastFieldConstants fieldConstant;
  public final int listId;
  public final AbstractBastNode parent;

  /**
   * Instantiates a new node parent information.
   *
   * @param parent the parent
   * @param fieldConstant the field constant
   * @param listId the list id
   * @param insideMethod the inside method
   */
  public NodeParentInformation(AbstractBastNode parent, BastFieldConstants fieldConstant,
      int listId, boolean insideMethod) {
    this.parent = parent;
    this.fieldConstant = fieldConstant;
    this.listId = listId;
  }
}
