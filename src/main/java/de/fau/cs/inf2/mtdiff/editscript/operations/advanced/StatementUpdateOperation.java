package de.fau.cs.inf2.mtdiff.editscript.operations.advanced;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

/**
 * An edit operation describing the update of a statement.
 *
 */
public final class StatementUpdateOperation extends AdvancedEditOperation {

  /**
   * Instantiates a new statement update operation.
   *
   * @param op the op
   */
  public StatementUpdateOperation(final BastEditOperation op) {
    super(EditOperationType.STATEMENT_UPDATE, op);
    assert StatementUpdateOperation.describes(op);
  }

  /**
   * Describes.
   *
   * @param op the op
   * @return true, if successful
   */
  public static boolean describes(final BastEditOperation op) {
    boolean oldPart =
        op.getOldOrChangedIndex().childrenListNumber == BastFieldConstants.BLOCK_STATEMENT
            || op.getOldOrChangedIndex().childrenListNumber == BastFieldConstants.IF_IF_PART
            || op.getOldOrChangedIndex().childrenListNumber == BastFieldConstants.IF_ELSE_PART
            || op.getOldOrChangedIndex().childrenListNumber == BastFieldConstants.WHILE_STATEMENT
        || op.getOldOrChangedIndex().childrenListNumber == BastFieldConstants.FOR_STMT_STATEMENT
        || op
            .getOldOrChangedIndex().childrenListNumber 
            == BastFieldConstants.ARES_USE_STMT_STATEMENT
        || op
            .getOldOrChangedIndex().childrenListNumber 
            == BastFieldConstants.ARES_WILDCARD_STATEMENTS
        || op
            .getOldOrChangedIndex().childrenListNumber 
            == BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS;
    boolean newPart =
        op.getNewOrChangedIndex().childrenListNumber == BastFieldConstants.BLOCK_STATEMENT
            || op.getNewOrChangedIndex().childrenListNumber == BastFieldConstants.IF_IF_PART
            || op.getNewOrChangedIndex().childrenListNumber == BastFieldConstants.IF_ELSE_PART
            || op.getNewOrChangedIndex().childrenListNumber == BastFieldConstants.WHILE_STATEMENT
        || op.getNewOrChangedIndex().childrenListNumber == BastFieldConstants.FOR_STMT_STATEMENT
        || op
            .getNewOrChangedIndex().childrenListNumber 
            == BastFieldConstants.ARES_USE_STMT_STATEMENT
        || op
            .getNewOrChangedIndex().childrenListNumber 
            == BastFieldConstants.ARES_WILDCARD_STATEMENTS
        || op
            .getNewOrChangedIndex().childrenListNumber 
            == BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS;
    return op.getType() == EditOperationType.UPDATE && oldPart && newPart;
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return "StatementUpdateOperation [operation=" + operation + "]";
  }
}
