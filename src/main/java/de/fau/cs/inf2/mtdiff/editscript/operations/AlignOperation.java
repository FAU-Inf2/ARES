package de.fau.cs.inf2.mtdiff.editscript.operations;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

/**
 * An {@see BastEditOperation} representing an alignment.
 *
 */
public final class AlignOperation extends BastEditOperation {
  private AbstractBastNode oldParent;
  private AbstractBastNode newParent;
  private AbstractBastNode oldNode;
  private AbstractBastNode newNode;
  private NodeIndex oldIndex;
  private NodeIndex newIndex;

  /**
   * Instantiates a new align operation.
   */
  public AlignOperation() {
    super(EditOperationType.ALIGN);
  }

  /**
   * Instantiates a new align operation.
   *
   * @param oldParent the old parent
   * @param newParent the new parent
   * @param oldNode the old node
   * @param newNode the new node
   * @param oldIndex the old index
   * @param newIndex the new index
   */
  public AlignOperation(final AbstractBastNode oldParent, final AbstractBastNode newParent,
      final AbstractBastNode oldNode, final AbstractBastNode newNode, final NodeIndex oldIndex,
      final NodeIndex newIndex) {
    super(EditOperationType.ALIGN);
    this.oldParent = oldParent;
    this.newParent = newParent;
    this.oldNode = oldNode;
    this.newNode = newNode;
    assert (newNode != null);
    this.oldIndex = oldIndex;
    this.newIndex = newIndex;
  }
  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    return String.format("Aligned %s (node %s) in %s (node %s)", oldNode.toString(),
        oldNode.getClass().getName(), oldParent.toString(), oldParent.getClass().getName());
  }

  
  /**
   * Gets the new or changed node.
   *
   * @return the new or changed node
   */
  @Override
  public AbstractBastNode getNewOrChangedNode() {
    return (AbstractBastNode) newNode;
  }

  
  /**
   * Gets the unchanged or new parent node.
   *
   * @return the unchanged or new parent node
   */
  @Override
  public AbstractBastNode getUnchangedOrNewParentNode() {
    return (AbstractBastNode) newParent;
  }

  
  /**
   * Gets the unchanged or old parent node.
   *
   * @return the unchanged or old parent node
   */
  @Override
  public AbstractBastNode getUnchangedOrOldParentNode() {
    return (AbstractBastNode) oldParent;
  }

  
  /**
   * Gets the old or inserted node.
   *
   * @return the old or inserted node
   */
  @Override
  public AbstractBastNode getOldOrInsertedNode() {
    return (AbstractBastNode) oldNode;
  }

  
  /**
   * Gets the new or changed index.
   *
   * @return the new or changed index
   */
  @Override
  public NodeIndex getNewOrChangedIndex() {
    return newIndex;
  }

  
  /**
   * Gets the old or changed index.
   *
   * @return the old or changed index
   */
  @Override
  public NodeIndex getOldOrChangedIndex() {
    return oldIndex;
  }
}
