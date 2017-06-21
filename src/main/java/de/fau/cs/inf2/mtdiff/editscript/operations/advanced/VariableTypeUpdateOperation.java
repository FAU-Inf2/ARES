package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.type.BastArrayType;
import de.fau.cs.inf2.cas.common.bast.type.BastBasicType;
import de.fau.cs.inf2.cas.common.bast.type.BastClassType;
import de.fau.cs.inf2.cas.common.bast.type.BastTypeName;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing an update of the type of a variable declaration.
 *
 */
public final class VariableTypeUpdateOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new variable type update operation.
   *
   * @param op the op
   */
  public VariableTypeUpdateOperation(final BastEditOperation op) {
    super(EditOperationType.VARIABLE_TYPE_UPDATE, op);
    assert VariableTypeUpdateOperation.describes(op);
  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final BastEditOperation op) {
    return op.getType() == EditOperationType.UPDATE
        && isType(op.getOldOrInsertedNode())
        && isType(op.getNewOrChangedNode());
  }

  private static boolean isType(final AbstractBastNode node) {
    switch (node.getTag()) {
      case BastArrayType.TAG:
      case BastBasicType.TAG:
      case BastClassType.TAG:
      case BastTypeName.TAG:
        return true;

      default:
        return false;
    }
  }
  
}
