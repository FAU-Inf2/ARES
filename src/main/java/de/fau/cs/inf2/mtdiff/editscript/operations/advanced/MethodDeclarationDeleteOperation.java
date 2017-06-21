package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.BastClassDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing a deletion of a field declaration.
 *
 */
public final class MethodDeclarationDeleteOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new method declaration delete operation.
   *
   * @param op the op
   */
  public MethodDeclarationDeleteOperation(final BastEditOperation op) {
    super(EditOperationType.METHOD_DELETE, op);
    assert MethodDeclarationDeleteOperation.describes(op);
  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final BastEditOperation op) {
    return (op.getType() == EditOperationType.DELETE
        && op.getOldOrInsertedNode().getTag() == BastFunction.TAG)
        && (op.getUnchangedOrOldParentNode().getTag() == BastClassDecl.TAG);
  }

}
