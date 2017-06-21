package de.fau.cs.inf2.mtdiff.editscript.operations;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

/**
 * The abstract base class for any tree edit operation (e.g. node insertion).
 *
 */
public abstract class BastEditOperation extends EditOperation {


  protected BastEditOperation(EditOperationType type) {
    super(type);
  }

  /**
   * Gets the new or changed node.
   *
   * @return the new or changed node
   */
  public abstract AbstractBastNode getNewOrChangedNode();

  /**
   * Gets the unchanged or new parent node.
   *
   * @return the unchanged or new parent node
   */
  public abstract AbstractBastNode getUnchangedOrNewParentNode();

  /**
   * Gets the unchanged or old parent node.
   *
   * @return the unchanged or old parent node
   */
  public abstract AbstractBastNode getUnchangedOrOldParentNode();

  /**
   * Gets the old or inserted node.
   *
   * @return the old or inserted node
   */
  public abstract AbstractBastNode getOldOrInsertedNode();

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
