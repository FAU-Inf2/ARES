package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing a renamed variable.
 *
 */
public final class VariableRenamingOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new variable renaming operation.
   *
   * @param op the op
   */
  public VariableRenamingOperation(final BastEditOperation op) {
    super(EditOperationType.VARIABLE_RENAME, op);
    assert VariableRenamingOperation.describes(op);
  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final BastEditOperation op) {
    return (op.getOldOrInsertedNode().getTag() == BastNameIdent.TAG)
        && (op.getNewOrChangedNode().getTag() == BastNameIdent.TAG);
  }

}
