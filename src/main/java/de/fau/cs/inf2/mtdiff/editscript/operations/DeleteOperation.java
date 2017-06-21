package de.fau.cs.inf2.mtdiff.editscript.operations;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

/**
 * An {@see BastEditOperation} representing a deletion.
 *
 */
public final class DeleteOperation extends BastEditOperation {
  private AbstractBastNode parentNode;
  private AbstractBastNode deletedNode;
  private NodeIndex index;

  /**
   * Instantiates a new delete operation.
   */
  public DeleteOperation() {
    super(EditOperationType.DELETE);
  }

  /**
   * Instantiates a new delete operation.
   *
   * @param parentNode the parent node
   * @param deletedNode the deleted node
   * @param index the index
   */
  public DeleteOperation(final AbstractBastNode parentNode, final AbstractBastNode deletedNode,
      final NodeIndex index) {
    super(EditOperationType.DELETE);
    this.parentNode = parentNode;
    this.deletedNode = deletedNode;
    this.index = index;
  }
  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    return String.format("Deleted %s (node %s) in %s (index %s, node %s)", deletedNode.toString(),
        deletedNode.getClass().getName(), parentNode.toString(), index.toString(),
        parentNode.getClass().getName());
  }

  
  /**
   * Gets the new or changed node.
   *
   * @return the new or changed node
   */
  @Override
  public AbstractBastNode getNewOrChangedNode() {
    return (AbstractBastNode) deletedNode;
  }

  
  /**
   * Gets the unchanged or new parent node.
   *
   * @return the unchanged or new parent node
   */
  @Override
  public AbstractBastNode getUnchangedOrNewParentNode() {
    return (AbstractBastNode) parentNode;
  }

  
  /**
   * Gets the unchanged or old parent node.
   *
   * @return the unchanged or old parent node
   */
  @Override
  public AbstractBastNode getUnchangedOrOldParentNode() {
    return (AbstractBastNode) parentNode;
  }

  
  /**
   * Gets the old or inserted node.
   *
   * @return the old or inserted node
   */
  @Override
  public AbstractBastNode getOldOrInsertedNode() {
    return (AbstractBastNode) deletedNode;
  }

  
  /**
   * Gets the new or changed index.
   *
   * @return the new or changed index
   */
  @Override
  public NodeIndex getNewOrChangedIndex() {
    return index;
  }

  
  /**
   * Gets the old or changed index.
   *
   * @return the old or changed index
   */
  @Override
  public NodeIndex getOldOrChangedIndex() {
    return index;
  }
}
