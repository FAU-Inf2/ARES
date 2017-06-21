package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.UpdateOperation;

/**
 * An edit operation describing a renamed class definition.
 *
 */
public final class ClassRenamingOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new class renaming operation.
   *
   * @param op the op
   */
  public ClassRenamingOperation(BastEditOperation op) {
    super(EditOperationType.CLASS_RENAME, op);

  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final UpdateOperation op) {
    return (op.getOldOrInsertedNode().getTag() == BastNameIdent.TAG)
        && (op.getNewOrChangedNode().getTag() == BastNameIdent.TAG);
  }

}
