package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.BastParameter;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameterList;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing a deletion of a parameter.
 *
 */
public final class ParameterDeleteOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new parameter delete operation.
   *
   * @param op the op
   */
  public ParameterDeleteOperation(final BastEditOperation op) {
    super(EditOperationType.PARAMETER_DELETE, op);
    assert ParameterDeleteOperation.describes(op);
  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final BastEditOperation op) {
    return (op.getType() == EditOperationType.DELETE
        && op.getOldOrInsertedNode().getTag() == BastParameter.TAG)
        && (op.getUnchangedOrOldParentNode().getTag() == BastParameterList.TAG);
  }

}
