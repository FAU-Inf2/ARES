package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeQualifier;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing a deletion of a final modifier.
 *
 */
public final class FinalModifierDeleteOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new final modifier delete operation.
   *
   * @param op the op
   */
  public FinalModifierDeleteOperation(final BastEditOperation op) {
    super(EditOperationType.FINAL_DELETE, op);
    assert FinalModifierDeleteOperation.describes(op);

  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final BastEditOperation op) {
    final AbstractBastNode node = op.getOldOrInsertedNode();

    return (op.getType() == EditOperationType.DELETE && node.getTag() == BastTypeQualifier.TAG)
        && (((BastTypeQualifier) node).type == BastTypeQualifier.TYPE_FINAL);
  }
}
