package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.BastClassDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing the insertion of a method declaration.
 *
 */
public final class MethodDeclarationInsertOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new method declaration insert operation.
   *
   * @param op the op
   */
  public MethodDeclarationInsertOperation(final BastEditOperation op) {
    super(EditOperationType.METHOD_INSERT, op);
    assert MethodDeclarationInsertOperation.describes(op);
  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final BastEditOperation op) {
    return (op.getType() == EditOperationType.INSERT
        && op.getOldOrInsertedNode().getTag() == BastFunction.TAG)
        && (op.getUnchangedOrOldParentNode().getTag() == BastClassDecl.TAG);
  }

}
