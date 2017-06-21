package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeQualifier;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.DeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;

/**
 * An edit operation describing a change which decreases the accessibility of a member of a class.
 *
 */
public final class DecreasingAccessibilityOperation extends AdvancedEditOperation {

  /**
   * todo.
   * 
   * <p>Create a new DecreasingAccessibilityOperation,
   *  where a single type qualifier has been inserted.
   * This may be used in cases when a class member had package local access and was modified to have
   * private or protected access.
   *
   * @param op the op
   */
  public DecreasingAccessibilityOperation(final BastEditOperation op) {
    super(EditOperationType.DECREASING_ACCESS, op);
  }


  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final InsertOperation op) {
    final AbstractBastNode node = op.getOldOrInsertedNode();

    return (node.getTag() == BastTypeQualifier.TAG)
        && (isPrivate((BastTypeQualifier) node) || isProtected((BastTypeQualifier) node));
  }

  /**
   * Describes.
   *
   * @param delOp the del op
   * @param insOp the ins op
   * @return true, if successful
   */
  public static boolean describes(final DeleteOperation delOp, final InsertOperation insOp) {
    final AbstractBastNode deleted = delOp.getOldOrInsertedNode();
    final AbstractBastNode inserted = insOp.getOldOrInsertedNode();

    return (deleted.getTag() == BastTypeQualifier.TAG)
        && (inserted.getTag() == BastTypeQualifier.TAG)
        && (isProtected((BastTypeQualifier) deleted) || isPublic((BastTypeQualifier) deleted))
        && (isPrivate((BastTypeQualifier) inserted) || isProtected((BastTypeQualifier) inserted))
        && (((BastTypeQualifier) deleted).type < ((BastTypeQualifier) inserted).type);
  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final DeleteOperation op) {
    final AbstractBastNode node = op.getOldOrInsertedNode();

    return (node.getTag() == BastTypeQualifier.TAG) && isPublic((BastTypeQualifier) node);
  }

  private static boolean isPrivate(final BastTypeQualifier qual) {
    return qual.type == BastTypeQualifier.TYPE_PRIVATE;
  }

  private static boolean isProtected(final BastTypeQualifier qual) {
    return qual.type == BastTypeQualifier.TYPE_PROTECTED;
  }

  private static boolean isPublic(final BastTypeQualifier qual) {
    return qual.type == BastTypeQualifier.TYPE_PUBLIC;
  }

}
