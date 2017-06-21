package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.BastParameter;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameterList;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing a reordering of the parameters of a method.
 *
 */
public final class ParameterReorderingOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new parameter reordering operation.
   *
   * @param op the op
   */
  public ParameterReorderingOperation(final BastEditOperation op) {
    super(EditOperationType.PARAMETER_REORDERING, op);
    assert ParameterReorderingOperation.describes(op);
  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final BastEditOperation op) {
    return (op.getType() == EditOperationType.ALIGN
        && op.getOldOrInsertedNode().getTag() == BastParameter.TAG)
        && (op.getNewOrChangedNode().getTag() == BastParameter.TAG)
        && (op.getUnchangedOrOldParentNode().getTag() == BastParameterList.TAG);
  }

}
