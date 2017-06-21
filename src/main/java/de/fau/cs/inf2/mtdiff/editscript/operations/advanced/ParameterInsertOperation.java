package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.BastParameter;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameterList;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing an insertion of a parameter.
 *
 */
public final class ParameterInsertOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new parameter insert operation.
   *
   * @param op the op
   */
  public ParameterInsertOperation(final BastEditOperation op) {
    super(EditOperationType.PARAMETER_INSERT, op);
    assert ParameterInsertOperation.describes(op);
  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final BastEditOperation op) {
    return (op.getType() == EditOperationType.INSERT
        && op.getOldOrInsertedNode().getTag() == BastParameter.TAG)
        && (op.getUnchangedOrOldParentNode().getTag() == BastParameterList.TAG);
  }

}
