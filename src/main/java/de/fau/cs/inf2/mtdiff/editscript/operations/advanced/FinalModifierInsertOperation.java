package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeQualifier;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing an insertion of a final modifier.
 *
 */
public final class FinalModifierInsertOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new final modifier insert operation.
   *
   * @param op the op
   */
  public FinalModifierInsertOperation(final BastEditOperation op) {
    super(EditOperationType.FINAL_INSERT, op);
    assert FinalModifierInsertOperation.describes(op);
  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final BastEditOperation op) {
    final AbstractBastNode node = op.getOldOrInsertedNode();

    return (op.getType() == EditOperationType.INSERT && node.getTag() == BastTypeQualifier.TAG)
        && (((BastTypeQualifier) node).type == BastTypeQualifier.TYPE_FINAL);
  }

}
