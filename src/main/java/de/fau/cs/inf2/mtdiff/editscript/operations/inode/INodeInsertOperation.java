package de.fau.cs.inf2.mtdiff.editscript.operations.inode;

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An {@see INodeEditOperation} representing an insertion.
 *
 */
public final class INodeInsertOperation extends INodeEditOperation {
  private INode parentNode;
  private INode insertedNode;
  private NodeIndex index;

  /**
   * Instantiates a new i node insert operation.
   */
  public INodeInsertOperation() {
    super(EditOperationType.INSERT);
  }

  /**
   * Instantiates a new i node insert operation.
   *
   * @param parentNode the parent node
   * @param insertedNode the inserted node
   * @param index the index
   */
  public INodeInsertOperation(final INode parentNode, final INode insertedNode,
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
  public INode getNewOrChangedNode() {
    return (INode) insertedNode;
  }

  
  /**
   * Gets the unchanged or new parent node.
   *
   * @return the unchanged or new parent node
   */
  @Override
  public INode getUnchangedOrNewParentNode() {
    return (INode) parentNode;
  }

  
  /**
   * Gets the unchanged or old parent node.
   *
   * @return the unchanged or old parent node
   */
  @Override
  public INode getUnchangedOrOldParentNode() {
    return (INode) parentNode;
  }

  
  /**
   * Gets the old or inserted node.
   *
   * @return the old or inserted node
   */
  @Override
  public INode getOldOrInsertedNode() {
    return (INode) insertedNode;
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
