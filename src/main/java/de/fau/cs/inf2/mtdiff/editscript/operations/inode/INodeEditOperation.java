package de.fau.cs.inf2.mtdiff.editscript.operations.inode;

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * The abstract base class for any tree edit operation (e.g. node insertion).
 *
 */
public abstract class INodeEditOperation extends EditOperation {


  protected INodeEditOperation(EditOperationType type) {
    super(type);
  }

  /**
   * Gets the new or changed node.
   *
   * @return the new or changed node
   */
  public abstract INode getNewOrChangedNode();

  /**
   * Gets the unchanged or new parent node.
   *
   * @return the unchanged or new parent node
   */
  public abstract INode getUnchangedOrNewParentNode();

  /**
   * Gets the unchanged or old parent node.
   *
   * @return the unchanged or old parent node
   */
  public abstract INode getUnchangedOrOldParentNode();

  /**
   * Gets the old or inserted node.
   *
   * @return the old or inserted node
   */
  public abstract INode getOldOrInsertedNode();

  /**
   * Gets the new or changed index.
   *
   * @return the new or changed index
   */
  public abstract NodeIndex getNewOrChangedIndex();

  /**
   * Gets the old or changed index.
   *
   * @return the old or changed index
   */
  public abstract NodeIndex getOldOrChangedIndex();

}
