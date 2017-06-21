package de.fau.cs.inf2.mtdiff.editscript.operations;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

/**
 * An {@see BastEditOperation} representing an insertion.
 *
 */
public final class InsertOperation extends BastEditOperation {
  private AbstractBastNode parentNode;
  private AbstractBastNode insertedNode;
  private NodeIndex index;

  /**
   * Instantiates a new insert operation.
   */
  public InsertOperation() {
    super(EditOperationType.INSERT);
  }

  /**
   * Instantiates a new insert operation.
   *
   * @param parentNode the parent node
   * @param insertedNode the inserted node
   * @param index the index
   */
  public InsertOperation(final AbstractBastNode parentNode, final AbstractBastNode insertedNode,
      final NodeIndex index) {
    super(EditOperationType.INSERT);
    this.parentNode = parentNode;
    this.insertedNode = insertedNode;
    this.index = index;
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    return String.format("Inserted %s (node %s) in %s (index %s, node %s)", insertedNode.toString(),
        insertedNode.getClass().getName(), parentNode.toString(), index.toString(),
        parentNode.getClass().getName());
  }

  
  /**
   * Gets the new or changed node.
   *
   * @return the new or changed node
   */
  @Override
  public AbstractBastNode getNewOrChangedNode() {
    return (AbstractBastNode) insertedNode;
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
    return (AbstractBastNode) insertedNode;
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
