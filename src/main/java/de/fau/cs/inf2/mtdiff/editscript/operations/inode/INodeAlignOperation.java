package de.fau.cs.inf2.mtdiff.editscript.operations.inode;

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An {@see INodeEditOperation} representing an alignment.
 *
 */
public final class INodeAlignOperation extends INodeEditOperation {
  private INode oldParent;
  private INode newParent;
  private INode oldNode;
  private INode newNode;
  private NodeIndex oldIndex;
  private NodeIndex newIndex;

  /**
   * Instantiates a new i node align operation.
   */
  public INodeAlignOperation() {
    super(EditOperationType.ALIGN);
  }

  /**
   * Instantiates a new i node align operation.
   *
   * @param oldParent the old parent
   * @param newParent the new parent
   * @param oldNode the old node
   * @param newNode the new node
   * @param oldIndex the old index
   * @param newIndex the new index
   */
  public INodeAlignOperation(final INode oldParent, final INode newParent, final INode oldNode,
      final INode newNode, final NodeIndex oldIndex, final NodeIndex newIndex) {
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
  public INode getNewOrChangedNode() {
    return (INode) newNode;
  }

  
  /**
   * Gets the unchanged or new parent node.
   *
   * @return the unchanged or new parent node
   */
  @Override
  public INode getUnchangedOrNewParentNode() {
    return (INode) newParent;
  }

  
  /**
   * Gets the unchanged or old parent node.
   *
   * @return the unchanged or old parent node
   */
  @Override
  public INode getUnchangedOrOldParentNode() {
    return (INode) oldParent;
  }

  
  /**
   * Gets the old or inserted node.
   *
   * @return the old or inserted node
   */
  @Override
  public INode getOldOrInsertedNode() {
    return (INode) oldNode;
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
