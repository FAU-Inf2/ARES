package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.BastClassDecl;
import de.fau.cs.inf2.cas.common.bast.type.BastClassType;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing a deletion of a parent class.
 *
 */
public final class ParentClassDeleteOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new parent class delete operation.
   *
   * @param op the op
   */
  public ParentClassDeleteOperation(final BastEditOperation op) {
    super(EditOperationType.PARENT_CLASS_DELETE, op);
    assert ParentClassDeleteOperation.describes(op);
  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final BastEditOperation op) {
    return (op.getType() == EditOperationType.DELETE
        && op.getOldOrInsertedNode().getTag() == BastClassType.TAG)
        && (op.getUnchangedOrOldParentNode().getTag() == BastClassDecl.TAG);
  }

}
