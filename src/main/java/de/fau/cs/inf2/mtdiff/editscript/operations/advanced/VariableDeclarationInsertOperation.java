package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing the insertion of a variable declaration.
 *
 */
public final class VariableDeclarationInsertOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new variable declaration insert operation.
   *
   * @param op the op
   */
  public VariableDeclarationInsertOperation(final BastEditOperation op) {
    super(EditOperationType.VARIABLE_INSERT, op);
    assert VariableDeclarationInsertOperation.describes(op);
  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final BastEditOperation op) {
    return (op.getType() == EditOperationType.INSERT
        && op.getOldOrInsertedNode().getTag() == BastIdentDeclarator.TAG)
        && (op.getUnchangedOrOldParentNode().getTag() == BastDeclaration.TAG);
  }

}
