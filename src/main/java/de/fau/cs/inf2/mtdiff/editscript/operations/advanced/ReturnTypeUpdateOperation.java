package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.type.BastArrayType;
import de.fau.cs.inf2.cas.common.bast.type.BastBasicType;
import de.fau.cs.inf2.cas.common.bast.type.BastClassType;
import de.fau.cs.inf2.cas.common.bast.type.BastTypeName;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing an update of a return type of a method declaration.
 *
 */
public final class ReturnTypeUpdateOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new return type update operation.
   *
   * @param op the op
   */
  public ReturnTypeUpdateOperation(final BastEditOperation op) {
    super(EditOperationType.RETURN_TYPE_UPDATE, op);
    assert ReturnTypeUpdateOperation.describes(op);
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
