package de.fau.cs.inf2.mtdiff.editscript.operations;

import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.ClassRenamingOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.DecreasingAccessibilityOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.FieldDeclarationDeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.FieldDeclarationInsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.FieldRenamingOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.FieldTypeUpdateOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.FinalModifierDeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.FinalModifierInsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.IncreasingAccessibilityOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.MethodDeclarationDeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.MethodDeclarationInsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.MethodRenamingOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.ParameterDeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.ParameterInsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.ParameterRenamingOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.ParameterReorderingOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.ParameterTypeUpdateOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.ParentClassDeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.ParentClassInsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.ReturnTypeUpdateOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.StatementDeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.StatementInsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.StatementParentChangeOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.StatementReorderingOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.StatementUpdateOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.VariableDeclarationDeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.VariableDeclarationInsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.VariableRenamingOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.VariableTypeUpdateOperation;

import java.util.HashMap;
import java.util.Map;

/**
 * The type of an {@see BastEditOperation}. Used for more efficient type checks.
 *
 */
public enum EditOperationType {

  INSERT(0, StringTags.TAG_INSERT_NAME, InsertOperation.class), 
  DELETE(1, StringTags.TAG_DELETE_NAME, DeleteOperation.class), 
  MOVE(2, StringTags.TAG_MOVE_NAME, MoveOperation.class), 
  ALIGN(3, StringTags.TAG_ALIGN_NAME, AlignOperation.class), 
  UPDATE(4, StringTags.TAG_UPDATE_NAME, UpdateOperation.class),
  
  STATEMENT_REORDERING(5, StringTags.TAG_STMT_REORDER_NAME, StatementReorderingOperation.class),
  STATEMENT_PARENT_CHANGE(
      6,
      StringTags.TAG_STMT_PARENT_CHANGE_NAME,
      StatementParentChangeOperation.class),
  STATEMENT_INSERT(7, StringTags.TAG_STMT_INSERT_NAME, StatementInsertOperation.class),
  STATEMENT_DELETE(8, StringTags.TAG_STMT_DELETE_NAME, StatementDeleteOperation.class),
  STATEMENT_UPDATE(9, StringTags.TAG_STMT_UPDATE_NAME, StatementUpdateOperation.class),
  INCREASING_ACCESS(
      10,
      StringTags.TAG_INCREASING_ACCESS_NAME,
      IncreasingAccessibilityOperation.class),
  DECREASING_ACCESS(
      11,
      StringTags.TAG_DECREASING_ACCESS_NAME,
      DecreasingAccessibilityOperation.class),
  FINAL_INSERT(12, StringTags.TAG_FINAL_INSERT_NAME, FinalModifierInsertOperation.class),
  FINAL_DELETE(13, StringTags.TAG_FINAL_DELETE_NAME, FinalModifierDeleteOperation.class),
  RETURN_TYPE_UPDATE(14, StringTags.TAG_RETURN_TYPE_UPDATE_NAME, ReturnTypeUpdateOperation.class),
  METHOD_DELETE(15, StringTags.TAG_METHOD_DELETE_NAME, MethodDeclarationDeleteOperation.class),
  METHOD_INSERT(16, StringTags.TAG_METHOD_INSERT_NAME, MethodDeclarationInsertOperation.class),
  METHOD_RENAME(17, StringTags.TAG_METHOD_RENAME_NAME, MethodRenamingOperation.class),
  PARAMETER_DELETE(18, StringTags.TAG_PARAMETER_DELETE_NAME, ParameterDeleteOperation.class),
  PARAMETER_INSERT(19, StringTags.TAG_PARAMETER_INSERT_NAME, ParameterInsertOperation.class),
  PARAMETER_RENAME(20, StringTags.TAG_PARAMETER_RENAME_NAME, ParameterRenamingOperation.class),
  PARAMETER_REORDERING(
      21,
      StringTags.TAG_PARAMETER_REORDER_NAME,
      ParameterReorderingOperation.class),
  PARAMETER_TYPE_UPDATE(
      22,
      StringTags.TAG_PARAMETER_TYPE_UPD_NAME,
      ParameterTypeUpdateOperation.class),
  CLASS_RENAME(23, StringTags.TAG_CLASS_RENAME_NAME, ClassRenamingOperation.class),
  PARENT_CLASS_INSERT(23, StringTags.TAG_PARENTCLASS_INSERT_NAME, ParentClassInsertOperation.class),
  PARENT_CLASS_DELETE(24, StringTags.TAG_PARENTCLASS_DELETE_NAME, ParentClassDeleteOperation.class),
  VARIABLE_INSERT(25, StringTags.TAG_VAR_INSERT_NAME, VariableDeclarationInsertOperation.class),
  VARIABLE_DELETE(26, StringTags.TAG_VAR_DELETE_NAME, VariableDeclarationDeleteOperation.class),
  VARIABLE_RENAME(27, StringTags.TAG_VAR_RENAME_NAME, VariableRenamingOperation.class),
  VARIABLE_TYPE_UPDATE(28, StringTags.TAG_VAR_TYPE_UPDATE_NAME, VariableTypeUpdateOperation.class),
  FIELD_INSERT(29, StringTags.TAG_FIELD_INSERT_NAME, FieldDeclarationInsertOperation.class),
  FIELD_DELETE(30, StringTags.TAG_FIELD_DELETE_NAME, FieldDeclarationDeleteOperation.class),
  FIELD_RENAME(31, StringTags.TAG_FIELD_RENAME_NAME, FieldRenamingOperation.class),
  FIELD_TYPE_UPDATE(32, StringTags.TAG_FIELD_TYPE_UPDATE_NAME, FieldTypeUpdateOperation.class),

  ;

  public final int id;
  public final String niceName;
  public final Class<?> classMember;

  private EditOperationType(int id, String niceName, Class<?> classMember) {
    this.id = id;
    this.niceName = niceName;
    this.classMember = classMember;
  }

  private static Map<String, EditOperationType> niceNameMap =
      new HashMap<String, EditOperationType>();

  static {
    for (EditOperationType t : EditOperationType.values()) {
      niceNameMap.put(t.niceName, t);
    }
  }

}
