package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing the insertion of a field declaration.
 *
 */
public final class FieldDeclarationInsertOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new field declaration insert operation.
   *
   * @param op the op
   */
  public FieldDeclarationInsertOperation(final BastEditOperation op) {
    super(EditOperationType.FIELD_INSERT, op);
    assert FieldDeclarationInsertOperation.describes(op);
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
