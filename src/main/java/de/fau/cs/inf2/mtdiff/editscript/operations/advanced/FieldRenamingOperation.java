package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing a renamed field.
 *
 */
public final class FieldRenamingOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new field renaming operation.
   *
   * @param op the op
   */
  public FieldRenamingOperation(final BastEditOperation op) {
    super(EditOperationType.FIELD_RENAME, op);
    assert FieldRenamingOperation.describes(op);
  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final BastEditOperation op) {
    return (op.getType() == EditOperationType.UPDATE
        && op.getOldOrInsertedNode().getTag() == BastNameIdent.TAG)
        && (op.getNewOrChangedNode().getTag() == BastNameIdent.TAG);
  }

}
