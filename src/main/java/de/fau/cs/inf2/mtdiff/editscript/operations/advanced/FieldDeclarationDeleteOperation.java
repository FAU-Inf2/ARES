package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing a deletion of a field declaration.
 *
 */
public final class FieldDeclarationDeleteOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new field declaration delete operation.
   *
   * @param op the op
   */
  public FieldDeclarationDeleteOperation(final BastEditOperation op) {
    super(EditOperationType.FIELD_DELETE, op);
    assert FieldDeclarationDeleteOperation.describes(op);

  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final BastEditOperation op) {
    return (op.getType() == EditOperationType.DELETE
        && op.getOldOrInsertedNode().getTag() == BastIdentDeclarator.TAG)
        && (op.getUnchangedOrOldParentNode().getTag() == BastDeclaration.TAG);
  }

}
