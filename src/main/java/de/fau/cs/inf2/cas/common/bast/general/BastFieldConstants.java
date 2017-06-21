package de.fau.cs.inf2.cas.common.bast.general;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

public enum BastFieldConstants {

  ACCESS_TARGET("ACCESS_TARGET", TagConstants.BAST_ACCESS, 10 * TagConstants.BAST_ACCESS + 0),
  ACCESS_MEMBER("ACCESS_MEMBER", TagConstants.BAST_ACCESS, 10 * TagConstants.BAST_ACCESS + 1),
  ADDITIVE_EXPR_LEFT(
      "ADDITIVE_EXPR_LEFT",
      TagConstants.BAST_ADDITIVE_EXPR,
      10 * TagConstants.BAST_ADDITIVE_EXPR + 0),
  ADDITIVE_EXPR_RIGHT(
      "ADDITIVE_EXPR_RIGHT",
      TagConstants.BAST_ADDITIVE_EXPR,
      10 * TagConstants.BAST_ADDITIVE_EXPR + 1),
  AND_LEFT("AND_LEFT", TagConstants.BAST_AND, 10 * TagConstants.BAST_AND + 0),
  AND_RIGHT("AND_RIGHT", TagConstants.BAST_AND, 10 * TagConstants.BAST_AND + 1),
  ANNOTATION_DECL_NAME(
      "ANNOTATION_DECL_NAME",
      TagConstants.BAST_ANNOTATION_DECL,
      10 * TagConstants.BAST_ANNOTATION_DECL + 0),
  ANNOTATION_DECL_DECLARATIONS(
      "ANNOTATION_DECL_DECLARATIONS",
      TagConstants.BAST_ANNOTATION_DECL,
      10 * TagConstants.BAST_ANNOTATION_DECL + 1,
      true),
  ANNOTATION_DECL_MODIFIERS(
      "ANNOTATION_DECL_MODIFIERS",
      TagConstants.BAST_ANNOTATION_DECL,
      10 * TagConstants.BAST_ANNOTATION_DECL + 2,
      true),
  ANNOTATION_ELEM_VALUE_INITLIST(
      "ANNOTATION_ELEM_VALUE_INITLIST",
      TagConstants.BAST_ANNOTATION_ELEM_VALUE,
      10 * TagConstants.BAST_ANNOTATION_ELEM_VALUE + 0,
      true),
  ANNOTATION_ELEM_VALUE_QUALIFIED_NAME(
      "ANNOTATION_ELEM_VALUE_QUALIFIED_NAME",
      TagConstants.BAST_ANNOTATION_ELEM_VALUE,
      10 * TagConstants.BAST_ANNOTATION_ELEM_VALUE + 1),
  ANNOTATION_METHOD_DECLARATOR(
      "ANNOTATION_METHOD_DECLARATOR",
      TagConstants.BAST_ANNOTATION_METHOD,
      10 * TagConstants.BAST_ANNOTATION_METHOD + 0),
  ANNOTATION_METHOD_TYPE(
      "ANNOTATION_METHOD_TYPE",
      TagConstants.BAST_ANNOTATION_METHOD,
      10 * TagConstants.BAST_ANNOTATION_METHOD + 1),
  ANNOTATION_METHOD_DEFAULT_VALUE(
      "ANNOTATION_METHOD_DEFAULT_VALUE",
      TagConstants.BAST_ANNOTATION_METHOD,
      10 * TagConstants.BAST_ANNOTATION_METHOD + 2),
  ANNOTATION_METHOD_MODIFIERS(
      "ANNOTATION_METHOD_MODIFIERS",
      TagConstants.BAST_ANNOTATION_METHOD,
      10 * TagConstants.BAST_ANNOTATION_METHOD + 3,
      true),
  ANNOTATION_NAME(
      "ANNOTATION_NAME",
      TagConstants.BAST_ANNOTATION,
      10 * TagConstants.BAST_ANNOTATION + 0),
  ANNOTATION_EXPRLIST(
      "ANNOTATION_EXPRLIST",
      TagConstants.BAST_ANNOTATION,
      10 * TagConstants.BAST_ANNOTATION + 1,
      true),
  ARRAY_DECLARATOR_INDEX(
      "ARRAY_DECLARATOR_INDEX",
      TagConstants.BAST_ARRAY_DECLARATOR,
      10 * TagConstants.BAST_ARRAY_DECLARATOR + 0),
  ARRAY_DECLARATOR_SOURCE(
      "ARRAY_DECLARATOR_INDEX",
      TagConstants.BAST_ARRAY_DECLARATOR,
      10 * TagConstants.BAST_ARRAY_DECLARATOR + 1),
  ARRAY_REF_REF("ARRAY_REF_REF", TagConstants.BAST_ARRAY_REF, 10 * TagConstants.BAST_ARRAY_REF + 0),
  ARRAY_REF_INDEX_LIST(
      "ARRAY_REF_INDEX_LIST",
      TagConstants.BAST_ARRAY_REF,
      10 * TagConstants.BAST_ARRAY_REF + 1,
      true),
  ARRAY_TYPE_DIMENSIONS(
      "ARRAY_TYPE_EXPR",
      TagConstants.BAST_ARRAY_TYPE,
      10 * TagConstants.BAST_ARRAY_TYPE + 0,
      true),
  ARRAY_TYPE_TYPE(
      "ARRAY_TYPE_TYPE",
      TagConstants.BAST_ARRAY_TYPE,
      10 * TagConstants.BAST_ARRAY_TYPE + 1),
  ARRAY_TYPE_INITIALIZER(
      "ARRAY_TYPE_INITIALIZER",
      TagConstants.BAST_ARRAY_TYPE,
      10 * TagConstants.BAST_ARRAY_TYPE + 2),
  ASGN_EXPR_LEFT(
      "ASGN_EXPR_LEFT",
      TagConstants.BAST_ASGN_EXPR,
      10 * TagConstants.BAST_ASGN_EXPR + 0),
  ASGN_EXPR_RIGHT(
      "ASGN_EXPR_RIGHT",
      TagConstants.BAST_ASGN_EXPR,
      10 * TagConstants.BAST_ASGN_EXPR + 1),
  ASSERT_STMT_FIRST_ASSERT(
      "ASSERT_STMT_FIRST_ASSERT",
      TagConstants.BAST_ASSERT,
      10 * TagConstants.BAST_ASSERT + 0),
  ASSERT_STMT_SECOND_ASSERT(
      "ASSERT_STMT_SECOND_ASSERT",
      TagConstants.BAST_ASSERT,
      10 * TagConstants.BAST_ASSERT + 1),
  BLOCK_STATEMENT(
      "BLOCK_STATEMENT",
      TagConstants.BAST_BLOCK,
      10 * TagConstants.BAST_BLOCK + 0,
      true),
  BLOCK_MODIFIERS(
      "BLOCK_MODIFIERS",
      TagConstants.BAST_BLOCK,
      10 * TagConstants.BAST_BLOCK + 1,
      true),
  BREAK_NAME("BREAK_NAME", TagConstants.BAST_BREAK, 10 * TagConstants.BAST_BREAK + 0),
  CASE_CONSTANT("CASE_CONSTANT", TagConstants.BAST_CASE, 10 * TagConstants.BAST_CASE + 0),
  CAST_EXPR_TYPE(
      "CAST_EXPR_TYPE",
      TagConstants.BAST_CAST_EXPR,
      10 * TagConstants.BAST_CAST_EXPR + 0),
  CAST_EXPR_OPERAND(
      "CAst_EXPR_TYPE",
      TagConstants.BAST_CAST_EXPR,
      10 * TagConstants.BAST_CAST_EXPR + 1),
  CATCH_CLAUSE_BLOCK(
      "CATCH_CLAUSE_BLOCK",
      TagConstants.BAST_CATCH,
      10 * TagConstants.BAST_CATCH + 0),
  CATCH_CLAUSE_DECL("CATCH_CLAUSE_DECL", TagConstants.BAST_CATCH, 10 * TagConstants.BAST_CATCH + 1),
  CLASS_DECL_NAME(
      "CLASS_DECL_NAME",
      TagConstants.BAST_CLASS_DECL,
      10 * TagConstants.BAST_CLASS_DECL + 0),
  CLASS_DECL_MODIFIERS(
      "CLASS_DECL_MODIFIERS",
      TagConstants.BAST_CLASS_DECL,
      10 * TagConstants.BAST_CLASS_DECL + 1,
      true),
  CLASS_DECL_TYPE_PARAMETERS(
      "CLASS_DECL_TYPE_PARAMETERS",
      TagConstants.BAST_CLASS_DECL,
      10 * TagConstants.BAST_CLASS_DECL + 2,
      true),
  CLASS_DECL_EXTENDED_CLASS(
      "CLASS_DECL_EXTENDED_CLASS",
      TagConstants.BAST_CLASS_DECL,
      10 * TagConstants.BAST_CLASS_DECL + 3),
  CLASS_DECL_INTERFACES(
      "CLASS_DECL_INTERFACES",
      TagConstants.BAST_CLASS_DECL,
      10 * TagConstants.BAST_CLASS_DECL + 4,
      true),
  CLASS_DECL_DECLARATIONS(
      "CLASS_DECL_DECLARATIONS",
      TagConstants.BAST_CLASS_DECL,
      10 * TagConstants.BAST_CLASS_DECL + 5,
      true),
  CLASS_TYPE_NAME("CLASS_TYPE_NAME", TagConstants.TYPE_CLASS, 10 * TagConstants.TYPE_CLASS + 0),
  CLASS_TYPE_TYPE_ARGUMENT(
      "CLASS_TYPE_TYPE_ARGUMENT",
      TagConstants.TYPE_CLASS,
      10 * TagConstants.TYPE_CLASS + 1, true),
  CLASS_TYPE_SUB_CLASS(
      "CLASS_TYPE_SUB_CLASS",
      TagConstants.TYPE_CLASS,
      10 * TagConstants.TYPE_CLASS + 2),
  CMP_EXPR_LEFT("CMP_EXPR_LEFT", TagConstants.BAST_CMP, 10 * TagConstants.BAST_CMP + 0),
  CMP_EXPR_RIGHT("CMP_EXPR_RIGHT", TagConstants.BAST_CMP, 10 * TagConstants.BAST_CMP + 1),
  COND_AND_LEFT("COND_AND_LEFT", TagConstants.BAST_COND_AND, 10 * TagConstants.BAST_COND_AND + 0),
  COND_AND_RIGHT("COND_AND_RIGHT", TagConstants.BAST_COND_AND, 10 * TagConstants.BAST_COND_AND + 1),
  COND_OR_LEFT("COND_OR_LEFT", TagConstants.BAST_COND_OR, 10 * TagConstants.BAST_COND_OR + 0),
  COND_OR_RIGHT("COND_OR_RIGHT", TagConstants.BAST_COND_OR, 10 * TagConstants.BAST_COND_OR + 1),
  COND_EXPR_CONDITION(
      "COND_EXPR_CONDITION",
      TagConstants.BAST_COND_EXPR,
      10 * TagConstants.BAST_COND_EXPR + 0),
  COND_EXPR_TRUE_PART(
      "COND_EXPR_TRUE_PART",
      TagConstants.BAST_COND_EXPR,
      10 * TagConstants.BAST_COND_EXPR + 1),
  COND_EXPR_FALSE_PART(
      "COND_EXPR_FALSE_PART",
      TagConstants.BAST_COND_EXPR,
      10 * TagConstants.BAST_COND_EXPR + 2),
  CONTINUE_NAME("CONTINUE_NAME", TagConstants.BAST_CONTINUE, 10 * TagConstants.BAST_CONTINUE + 0),
  DECLARATION_SPECIFIER(
      "DECLARATION_SPECIFIER",
      TagConstants.BAST_DECLARATION,
      10 * TagConstants.BAST_DECLARATION + 0,
      true),
  DECLARATION_DECLARATORS(
      "DECLARATION_DECLARATORS",
      TagConstants.BAST_DECLARATION,
      10 * TagConstants.BAST_DECLARATION + 1,
      true),
  DECLARATION_MODIFIERS(
      "DECLARATION_MODIFIERS",
      TagConstants.BAST_DECLARATION,
      10 * TagConstants.BAST_DECLARATION + 2,
      true),
  DECR_EXPR_OPERAND(
      "DECR_EXPR_OPERAND",
      TagConstants.BAST_DECR_EXPR,
      10 * TagConstants.BAST_DECR_EXPR + 0),
  DIRECT_CALL_FUNCTION(
      "DIRECT_CALL_FUNCTION",
      TagConstants.BAST_CALL,
      10 * TagConstants.BAST_CALL + 0),
  DIRECT_CALL_ARGUMENTS(
      "DIRECT_CALL_ARGUMENTS",
      TagConstants.BAST_CALL,
      10 * TagConstants.BAST_CALL + 1,
      true),
  ENUM_DECL_ENUMERATOR(
      "ENUM_DECL_ENUMERATOR",
      TagConstants.BAST_ENUM_DECL,
      10 * TagConstants.BAST_ENUM_DECL + 0),
  ENUM_DECL_MODIFIERS(
      "ENUM_DECL_MODIFIERS",
      TagConstants.BAST_ENUM_DECL,
      10 * TagConstants.BAST_ENUM_DECL + 1,
      true),
  ENUM_MEMBER_IDENTIFIER(
      "ENUM_MEMBER_IDENTIFIER",
      TagConstants.BAST_ENUM_MEMBER,
      10 * TagConstants.BAST_ENUM_MEMBER + 0),
  ENUM_MEMBER_INIT(
      "ENUM_MEMBER_INIT",
      TagConstants.BAST_ENUM_MEMBER,
      10 * TagConstants.BAST_ENUM_MEMBER + 1),
  ENUM_MEMBER_ANNOTATIONS(
      "ENUM_MEMBER_ANNOTATIONS",
      TagConstants.BAST_ENUM_MEMBER,
      10 * TagConstants.BAST_ENUM_MEMBER + 2,
      true),
  ENUM_MEMBER_INIT_ARGUMENTS(
      "ENUM_MEMBER_INIT_ARGUMENTS",
      TagConstants.BAST_ENUM_MEMBER,
      10 * TagConstants.BAST_ENUM_MEMBER + 3,
      true),
  ENUM_MEMBER_CLASS_BODIES(
      "ENUM_MEMBER_CLASS_BODIES",
      TagConstants.BAST_ENUM_MEMBER,
      10 * TagConstants.BAST_ENUM_MEMBER + 4,
      true),
  ENUM_SPEC_NAME(
      "ENUM_SPEC_NAME",
      TagConstants.BAST_ENUM_SPEC,
      10 * TagConstants.BAST_ENUM_SPEC + 0),
  ENUM_SPEC_MEMBERS(
      "ENUM_SPEC_MEMBERS",
      TagConstants.BAST_ENUM_SPEC,
      10 * TagConstants.BAST_ENUM_SPEC + 1,
      true),
  ENUM_SPEC_INTERFACES(
      "ENUM_SPEC_INTERFACES",
      TagConstants.BAST_ENUM_SPEC,
      10 * TagConstants.BAST_ENUM_SPEC + 2,
      true),
  ENUM_SPEC_DECLARATIONS(
      "ENUM_SPEC_DECLARATIONS",
      TagConstants.BAST_ENUM_SPEC,
      10 * TagConstants.BAST_ENUM_SPEC + 3,
      true),
  EXPR_INITIALIZER_INIT(
      "EXPR_INITIALIZER_INIT",
      TagConstants.BAST_EXPR_INITIALIZER,
      10 * TagConstants.BAST_EXPR_INITIALIZER + 0),
  EXPR_LIST_LIST(
      "EXPR_LIST_LIST",
      TagConstants.BAST_EXPR_LIST,
      10 * TagConstants.BAST_EXPR_LIST + 0,
      true),
  FOR_STMT_INIT("FOR_STMT_INIT", TagConstants.BAST_FOR_STMT, 10 * TagConstants.BAST_FOR_STMT + 0),
  FOR_STMT_CONDITION(
      "FOR_STMT_CONDITION",
      TagConstants.BAST_FOR_STMT,
      10 * TagConstants.BAST_FOR_STMT + 1),
  FOR_STMT_INCREMENT(
      "FOR_STMT_INCREMENT",
      TagConstants.BAST_FOR_STMT,
      10 * TagConstants.BAST_FOR_STMT + 2),
  FOR_STMT_STATEMENT(
      "FOR_STMT_STATEMENT",
      TagConstants.BAST_FOR_STMT,
      10 * TagConstants.BAST_FOR_STMT + 3),
  FOR_STMT_INIT_DECL(
      "FOR_STMT_INIT_DECL",
      TagConstants.BAST_FOR_STMT,
      10 * TagConstants.BAST_FOR_STMT + 4),
  FOR_STMT_LIST_STMT(
      "FOR_STMT_LIST_STMT",
      TagConstants.BAST_FOR_STMT,
      10 * TagConstants.BAST_FOR_STMT + 5),
  FOR_STMT_LIST_DECL(
      "FOR_STMT_LIST_DECL",
      TagConstants.BAST_FOR_STMT,
      10 * TagConstants.BAST_FOR_STMT + 6),
  FUNCTION_BLOCK_PARAMETERS(
      "FUNCTION_BLOCK_PARAMETERS",
      TagConstants.BAST_FUNCTION,
      10 * TagConstants.BAST_FUNCTION + 0,
      true),
  FUNCTION_BLOCK_STATEMENTS(
      "FUNCTION_BLOCK_STATEMENTS",
      TagConstants.BAST_FUNCTION,
      10 * TagConstants.BAST_FUNCTION + 1,
      true),
  FUNCTION_BLOCK_LOCAL_DECLARATIONS(
      "FUNCTION_BLOCK_LOCAL_DECLARATIONS",
      TagConstants.BAST_FUNCTION,
      10 * TagConstants.BAST_FUNCTION + 2,
      true),
  FUNCTION_BLOCK_SPECIFIER_LIST(
      "FUNCTION_BLOCK_SPECIFIER_LIST",
      TagConstants.BAST_FUNCTION,
      10 * TagConstants.BAST_FUNCTION + 3,
      true),
  FUNCTION_BLOCK_DECL_LIST(
      "FUNCTION_BLOCK_DECL_LIST",
      TagConstants.BAST_FUNCTION,
      10 * TagConstants.BAST_FUNCTION + 4,
      true),
  FUNCTION_BLOCK_DECL(
      "FUNCTION_BLOCK_DECL",
      TagConstants.BAST_FUNCTION,
      10 * TagConstants.BAST_FUNCTION + 5),
  FUNCTION_BLOCK_MODIFIERS(
      "FUNCTION_BLOCK_MODIFIERS",
      TagConstants.BAST_FUNCTION,
      10 * TagConstants.BAST_FUNCTION + 6,
      true),
  FUNCTION_BLOCK_RETURN_TYPE(
      "FUNCTION_BLOCK_RETURN_TYPE",
      TagConstants.BAST_FUNCTION,
      10 * TagConstants.BAST_FUNCTION + 7),
  FUNCTION_BLOCK_NAME_IDENT(
      "FUNCTION_BLOCK_NAME_IDENT",
      TagConstants.BAST_FUNCTION,
      10 * TagConstants.BAST_FUNCTION + 8),
  FUNCTION_BLOCK_TYPE_PARAMTER(
      "FUNCTION_BLOCK_TYPE_PARAMTER",
      TagConstants.BAST_FUNCTION,
      10 * TagConstants.BAST_FUNCTION + 16),
  FUNCTION_BLOCK_EXCEPTIONS(
      "FUNCTION_BLOCK_EXCEPTIONS",
      TagConstants.BAST_FUNCTION,
      10 * TagConstants.BAST_FUNCTION + 17,
      true),

  FUNCTION_IDENT_DECLARATOR_FUNCTION(
      "FUNCTION_IDENT_DECLARATOR_FUNCTION",
      TagConstants.BAST_FUNCTION_IDENT_DECL,
      10 * TagConstants.BAST_FUNCTION_IDENT_DECL + 0),
  FUNCTION_IDENT_DECLARATOR_PARAMTERS(
      "FUNCTION_IDENT_DECLARATOR_PARAMTERS",
      TagConstants.BAST_FUNCTION_IDENT_DECL,
      10 * TagConstants.BAST_FUNCTION_IDENT_DECL + 1,
      true),
  FUNCTION_PARAMETER_DECLARATOR_FUNCTION(
      "FUNCTION_PARAMETER_DECLARATOR_FUNCTION",
      TagConstants.BAST_FUNCTION_PARAMETER_DECL,
      10 * TagConstants.BAST_FUNCTION_PARAMETER_DECL + 0),
  FUNCTION_PARAMETER_DECLARATOR_PARAMETERS(
      "FUNCTION_PARAMETER_DECLARATOR_PARAMETERS",
      TagConstants.BAST_FUNCTION_PARAMETER_DECL,
      10 * TagConstants.BAST_FUNCTION_PARAMETER_DECL + 1),
  FUNCTION_PARAMETER_DECLARATOR_POINTER(
      "FUNCTION_PARAMETER_DECLARATOR_POINTER",
      TagConstants.BAST_FUNCTION_PARAMETER_DECL,
      10 * TagConstants.BAST_FUNCTION_PARAMETER_DECL + 2),

  GOTO_LABEL("GOTO_LABEL", TagConstants.BAST_GOTO, 10 * TagConstants.BAST_GOTO + 0),
  IDENT_DECLARATOR_IDENTIFIER(
      "IDENT_DECLARATOR_IDENTIFIER",
      TagConstants.BAST_IDENT_DECLARATOR,
      10 * TagConstants.BAST_IDENT_DECLARATOR + 0),
  IDENT_DECLARATOR_EXPRESSION(
      "IDENT_DECLARATOR_EXPRESSION",
      TagConstants.BAST_IDENT_DECLARATOR,
      10 * TagConstants.BAST_IDENT_DECLARATOR + 1),
  IDENT_DECLARATOR_POINTER(
      "IDENT_DECLARATOR_POINTER",
      TagConstants.BAST_IDENT_DECLARATOR,
      10 * TagConstants.BAST_IDENT_DECLARATOR + 2),
  IDENT_DECLARATOR_ARRAY_DECLARATOR(
      "IDENT_DECLARATOR_ARRAY_DECLARATOR",
      TagConstants.BAST_IDENT_DECLARATOR,
      10 * TagConstants.BAST_IDENT_DECLARATOR + 3),
  IF_CONDITION("IF_CONDITION", TagConstants.BAST_IF, 10 * TagConstants.BAST_IF + 0),
  IF_IF_PART("IF_IF_PART", TagConstants.BAST_IF, 10 * TagConstants.BAST_IF + 1),
  IF_ELSE_PART("IF_ELSE_PART", TagConstants.BAST_IF, 10 * TagConstants.BAST_IF + 2),
  IMPORT_NAME("IMPORT_NAME", TagConstants.BAST_IMPORT_DECL, 10 * TagConstants.BAST_IMPORT_DECL + 0),
  INCLUDE_STMT_NAME_PART(
      "INCLUDE_STMT_NAME_PART",
      TagConstants.BAST_INCLUDE,
      10 * TagConstants.BAST_INCLUDE + 0),
  INCR_EXPR_OPERAND(
      "INCR_EXPR_OPERAND",
      TagConstants.BAST_INCR_EXPR,
      10 * TagConstants.BAST_INCR_EXPR + 0),
  INSTANCE_OF_EXPR(
      "INSTANCE_OF_EXPR",
      TagConstants.BAST_INSTANCE_OF,
      10 * TagConstants.BAST_INSTANCE_OF + 0),
  INSTANCE_OF_TYPE(
      "INSTANCE_OF_TYPE",
      TagConstants.BAST_INSTANCE_OF,
      10 * TagConstants.BAST_INSTANCE_OF + 1),
  INTERFACE_DECL_NAME(
      "INTERFACE_DECL_NAME",
      TagConstants.BAST_INTERFACE,
      10 * TagConstants.BAST_INTERFACE + 0),
  INTERFACE_DECL_TYPE_PARAMETERS(
      "INTERFACE_DECL_TYPE_PARAMETERS",
      TagConstants.BAST_INTERFACE,
      10 * TagConstants.BAST_INTERFACE + 1,
      true),
  INTERFACE_DECL_INTERFACES(
      "INTERFACE_DECL_INTERFACES",
      TagConstants.BAST_INTERFACE,
      10 * TagConstants.BAST_INTERFACE + 2,
      true),
  INTERFACE_DECL_DECLARATIONS(
      "INTERFACE_DECL_DECLARATIONS",
      TagConstants.BAST_INTERFACE,
      10 * TagConstants.BAST_INTERFACE + 3,
      true),
  INTERFACE_DECL_MODIFIERS(
      "INTERFACE_DECL_MODIFIERS",
      TagConstants.BAST_INTERFACE,
      10 * TagConstants.BAST_INTERFACE + 4,
      true),
  LABEL_STMT_STMT(
      "LABEL_STMT_STMT",
      TagConstants.BAST_LABEL_STMT,
      10 * TagConstants.BAST_LABEL_STMT + 0),
  LABEL_STMT_IDENT(
      "LABEL_STMT_IDENT",
      TagConstants.BAST_LABEL_STMT,
      10 * TagConstants.BAST_LABEL_STMT + 1),
  ARES_BLOCK_NUMBER(
      "ARES_BLOCK_STATEMENTS",
      TagConstants.ARES_BLOCK,
      10 * TagConstants.ARES_BLOCK + 0),
  ARES_BLOCK_BLOCK(
      "ARES_BLOCK_BLOCK",
      TagConstants.ARES_BLOCK,
      10 * TagConstants.ARES_BLOCK + 1),
  ARES_BLOCK_IDENTIFIERS(
      "ARES_BLOCK_IDENTIFIER",
      TagConstants.ARES_BLOCK,
      10 * TagConstants.ARES_BLOCK + 2,
      true),
  ARES_PATTERN_CLAUSE_OCCURENCE(
      "ARES_PATTERN_CLAUSE_OCCURENCE",
      TagConstants.ARES_PATTERN,
      10 * TagConstants.ARES_PATTERN + 0),
  ARES_PATTERN_CLAUSE_EXPR(
      "ARES_PATTERN_CLAUSE_EXPR",
      TagConstants.ARES_PATTERN,
      10 * TagConstants.ARES_PATTERN + 1),
  ARES_PATTERN_CLAUSE_IDENT(
      "ARES_PATTERN_CLAUSE_IDENT",
      TagConstants.ARES_PATTERN,
      10 * TagConstants.ARES_PATTERN + 2),
  ARES_PLUGIN_CLAUSE_IDENT(
      "ARES_PLUGIN_CLAUSE_IDENT",
      TagConstants.ARES_PLUGIN,
      10 * TagConstants.ARES_PLUGIN + 0),
  ARES_PLUGIN_CLAUSE_EXPR_LIST(
      "ARES_PLUGIN_CLAUSE_EXPR_LIST",
      TagConstants.ARES_PLUGIN,
      10 * TagConstants.ARES_PLUGIN + 1,
      true),
  ARES_USE_STMT_IDENT(
      "ARES_USE_STMT_IDENT",
      TagConstants.ARES_USE,
      10 * TagConstants.ARES_USE + 0),
  ARES_USE_STMT_PATTERN(
      "ARES_USE_STMT_PATTERN",
      TagConstants.ARES_USE,
      10 * TagConstants.ARES_USE + 1),
  ARES_USE_STMT_STATEMENT(
      "ARES_USE_STMT_STATEMENT",
      TagConstants.ARES_USE,
      10 * TagConstants.ARES_USE + 2,
      true),
  ARES_WILDCARD_PATTERN(
      "ARES_WILDCARD_PATTERN",
      TagConstants.ARES_WILDCARD,
      10 * TagConstants.ARES_WILDCARD + 0),
  ARES_WILDCARD_PLUGIN(
      "ARES_WILDCARD_PLUGIN",
      TagConstants.ARES_WILDCARD,
      10 * TagConstants.ARES_WILDCARD + 1),
  ARES_WILDCARD_DECLARE(
      "ARES_WILDCARD_DECLARE",
      TagConstants.ARES_WILDCARD,
      10 * TagConstants.ARES_WILDCARD + 2),
  ARES_WILDCARD_STATEMENTS(
      "ARES_WILDCARD_STATEMENTS",
      TagConstants.ARES_WILDCARD,
      10 * TagConstants.ARES_WILDCARD + 3,
      true),
  ARES_CHOICE_STMT_BLOCK(
      "ARES_CHOICE_STMT_CHOICES",
      TagConstants.ARES_CHOICE,
      10 * TagConstants.ARES_CHOICE + 0,
      true),
  ARES_CASE_STMT_BLOCK(
      "ARES_YIELD_STMT_STATEMENTS",
      TagConstants.ARES_CASE,
      10 * TagConstants.ARES_CASE + 0,
      true),

  LIST_INITIALIZER_INIT(
      "LIST_INITIALIZER_INIT",
      TagConstants.BAST_LIST_INITIALIZER,
      10 * TagConstants.BAST_LIST_INITIALIZER + 0, true),
  MULTI_EXPR_LEFT(
      "MULTI_EXPR_LEFT",
      TagConstants.BAST_MULTI_EXPR,
      10 * TagConstants.BAST_MULTI_EXPR + 0),
  MULTI_EXPR_RIGHT(
      "MULTI_EXPR_RIGHT",
      TagConstants.BAST_MULTI_EXPR,
      10 * TagConstants.BAST_MULTI_EXPR + 1),
  NEW_CLASS_TYPE("NEW_CLASS_TYPE", TagConstants.BAST_NEW, 10 * TagConstants.BAST_NEW + 0),
  NEW_CLASS_TYPE_ARGUMENTS(
      "NEW_CLASS_TYPE_ARGUMENTS",
      TagConstants.BAST_NEW,
      10 * TagConstants.BAST_NEW + 1,
      true),
  NEW_CLASS_PARAMETERS(
      "NEW_CLASS_PARAMETERS",
      TagConstants.BAST_NEW,
      10 * TagConstants.BAST_NEW + 2,
      true),
  NEW_CLASS_DECLARATIONS(
      "NEW_CLASS_DECLARATIONS",
      TagConstants.BAST_NEW,
      10 * TagConstants.BAST_NEW + 3,
      true),
  OR_LEFT("OR_LEFT", TagConstants.BAST_OR, 10 * TagConstants.BAST_OR + 0),
  OR_RIGHT("OR_RIGHT", TagConstants.BAST_OR, 10 * TagConstants.BAST_OR + 1),
  PACKAGE_ANNOTATIONS(
      "PACKAGE_ANNOTATIONS",
      TagConstants.BAST_PACKAGE,
      10 * TagConstants.BAST_PACKAGE + 0,
      true),
  PACKAGE_NAME("PACKAGE_NAME", TagConstants.BAST_PACKAGE, 10 * TagConstants.BAST_PACKAGE + 1),
  PARAMETER_DECLARATOR(
      "PARAMETER_DECLARATOR",
      TagConstants.BAST_PARAMETER,
      10 * TagConstants.BAST_PARAMETER + 0),
  PARAMETER_SPECIFIERS(
      "PARAMETER_SPECIFIERS",
      TagConstants.BAST_PARAMETER,
      10 * TagConstants.BAST_PARAMETER + 1,
      true),
  PARAMETER_TYPE_LIST_PARAMETERS(
      "PARAMETER_TYPE_LIST_PARAMETERS",
      TagConstants.BAST_PARAMETER,
      10 * TagConstants.BAST_PARAMETER + 2,
      true),
  POINTER_POINTER("POINTER_POINTER", TagConstants.BAST_POINTER, 10 * TagConstants.BAST_POINTER + 0),
  POINTER_QUALIFIERS(
      "POINTER_QUALIFIERS",
      TagConstants.BAST_POINTER,
      10 * TagConstants.BAST_POINTER + 1,
      true),
  PROGRAM_COMMENTS(
      "PROGRAM_COMMENTS",
      TagConstants.BAST_PROGRAM,
      10 * TagConstants.BAST_PROGRAM + 0,
      true),
  PROGRAM_FUNCTION_BLOCKS(
      "PROGRAM_FUNCTION_BLOCKS",
      TagConstants.BAST_PROGRAM,
      10 * TagConstants.BAST_PROGRAM + 2,
      true),
  PROGRAM_SYMTAB("PROGRAM_SYMTAB", TagConstants.BAST_PROGRAM, 10 * TagConstants.BAST_PROGRAM + 3),
  PROGRAM_PACKAGE("PROGRAM_PACKAGE", TagConstants.BAST_PROGRAM, 10 * TagConstants.BAST_PROGRAM + 4),
  PROGRAM_IMPORTS(
      "PROGRAM_IMPORTS",
      TagConstants.BAST_PROGRAM,
      10 * TagConstants.BAST_PROGRAM + 5,
      true),
  PROGRAM_ANNOTATIONS(
      "PROGRAM_ANNOTATIONS",
      TagConstants.BAST_PROGRAM,
      10 * TagConstants.BAST_PROGRAM + 6,
      true),

  REGULAR_DECLARATOR_DECLARATION(
      "REGULAR_DECLARATOR_DECLARATION",
      TagConstants.BAST_REGULAR_DECLARATOR,
      10 * TagConstants.BAST_REGULAR_DECLARATOR + 0),

  REGULAR_DECLARATOR_EXPRESSION(
      "REGULAR_DECLARATOR_EXPRESSION",
      TagConstants.BAST_REGULAR_DECLARATOR,
      10 * TagConstants.BAST_REGULAR_DECLARATOR + 1),
  REGULAR_DECLARATOR_POINTER(
      "REGULAR_DECLARATOR_POINTER",
      TagConstants.BAST_REGULAR_DECLARATOR,
      10 * TagConstants.BAST_REGULAR_DECLARATOR + 2),
  REGULAR_DECLARATOR_INIT(
      "REGULAR_DECLARATOR_INIT",
      TagConstants.BAST_REGULAR_DECLARATOR,
      10 * TagConstants.BAST_REGULAR_DECLARATOR + 3),
  RETURN_VALUE("RETURN_VALUE", TagConstants.BAST_RETURN, 10 * TagConstants.BAST_RETURN + 0),
  SHIFT_LEFT("SHIFT_LEFT", TagConstants.BAST_SHIFT, 10 * TagConstants.BAST_SHIFT + 0),
  SHIFT_RIGHT("SHIFT_RIGHT", TagConstants.BAST_SHIFT, 10 * TagConstants.BAST_SHIFT + 1),
  STRUCT_DECL_MEMBER(
      "STRUCT_DECL_MEMBER",
      TagConstants.BAST_STRUCT_DECL,
      10 * TagConstants.BAST_STRUCT_DECL + 0),
  STRUCT_DECL_SPECIFIER_QUALIFIER(
      "STRUCT_DECL_SPECIFIER_QUALIFIER",
      TagConstants.BAST_STRUCT_DECL,
      10 * TagConstants.BAST_STRUCT_DECL + 1),
  STRUCT_DECL_DECLARATORS(
      "STRUCT_DECL_DECLARATORS",
      TagConstants.BAST_STRUCT_DECL,
      10 * TagConstants.BAST_STRUCT_DECL + 2,
      true),
  STRUCT_OR_UNION_SPECIFIER_TYPE_NAME(
      "STRUCT_OR_UNION_SPECIFIER_TYPE_NAME",
      TagConstants.TYPE_BAST_STRUCT_OR_UNION,
      10 * TagConstants.TYPE_BAST_STRUCT_OR_UNION + 0),
  STRUCT_OR_UNION_SPECIFIER_TYPE_STRUCT(
      "STRUCT_OR_UNION_SPECIFIER_TYPE_STRUCT",
      TagConstants.TYPE_BAST_STRUCT_OR_UNION,
      10 * TagConstants.TYPE_BAST_STRUCT_OR_UNION + 1),
  STRUCT_OR_UNION_SPECIFIER_TYPE_IDENTIFIER(
      "STRUCT_OR_UNION_SPECIFIER_TYPE_IDENTIFIER",
      TagConstants.TYPE_BAST_STRUCT_OR_UNION,
      10 * TagConstants.TYPE_BAST_STRUCT_OR_UNION + 2),
  SWITCH_CONDITION("SWITCH_CONDITION", TagConstants.BAST_SWITCH, 10 * TagConstants.BAST_SWITCH + 0),
  SWITCH_CASE_GROUPS(
      "SWITCH_CASE_GROUPS",
      TagConstants.BAST_SWITCH,
      10 * TagConstants.BAST_SWITCH + 1,
      true),
  SWITCH_CASE_GROUP_LABELS(
      "SWITCH_CASE_GROUP_LABELS",
      TagConstants.BAST_SWITCH_CASE_GROUP,
      10 * TagConstants.BAST_SWITCH_CASE_GROUP + 0,
      true),
  SWITCH_CASE_GROUP_STATEMENTS(
      "SWITCH_CASE_GROUP_STATEMENTS",
      TagConstants.BAST_SWITCH_CASE_GROUP,
      10 * TagConstants.BAST_SWITCH_CASE_GROUP + 1,
      true),
  SYNCHRONIZED_BLOCK_EXPR(
      "SYNCHRONIZED_BLOCK_EXPR",
      TagConstants.BAST_SYNCHRONIZED_BLOCK,
      10 * TagConstants.BAST_SYNCHRONIZED_BLOCK + 0),
  SYNCHRONIZED_BLOCK_BLOCK(
      "SYNCHRONIZED_BLOCK_BLOCK",
      TagConstants.BAST_SYNCHRONIZED_BLOCK,
      10 * TagConstants.BAST_SYNCHRONIZED_BLOCK + 1),
  TEMPLATE_SPECIFIER_TARGET(
      "TEMPLATE_SPECIFIER_TARGET",
      TagConstants.BAST_TEMPLATE,
      10 * TagConstants.BAST_TEMPLATE + 0),
  TEMPLATE_SPECIFIER_TYPE_ARGUMENTS(
      "TEMPLATE_SPECIFIER_TYPE_ARGUMENTS",
      TagConstants.BAST_TEMPLATE,
      10 * TagConstants.BAST_TEMPLATE + 1,
      true),
  THROW_EXCEPTION("THROW_EXCEPTION", TagConstants.BAST_THROW, 10 * TagConstants.BAST_THROW + 0),
  TRY_BLOCK("TRY_BLOCK", TagConstants.BAST_TRY, 10 * TagConstants.BAST_TRY + 0),
  TRY_CATCHES("TRY_CATCHES", TagConstants.BAST_TRY, 10 * TagConstants.BAST_TRY + 1, true),
  TRY_FINALLY_BLOCK("TRY_FINALLY_BLOCK", TagConstants.BAST_TRY, 10 * TagConstants.BAST_TRY + 2),
  TRY_RESOURCES("TRY_RESOURCES", TagConstants.BAST_TRY, 10 * TagConstants.BAST_TRY + 3, true),
  TYPE_ARGUMENT_TYPE(
      "TYPE_ARGUMENT_TYPE",
      TagConstants.BAST_TYPE_ARGUMENT,
      10 * TagConstants.BAST_TYPE_ARGUMENT + 0),
  TYPE_NAME_DECLARATOR(
      "TYPE_NAME_DECLARATOR",
      TagConstants.TYPE_TYPE_NAME,
      10 * TagConstants.TYPE_TYPE_NAME + 0),
  TYPE_NAME_SPECIFIERS(
      "TYPE_NAME_SPECIFIERS",
      TagConstants.TYPE_TYPE_NAME,
      10 * TagConstants.TYPE_TYPE_NAME + 1,
      true),
  TYPE_PARAMETER_NAME(
      "TYPE_PARAMETER_NAME",
      TagConstants.BAST_TYPE_PARAMETER,
      10 * TagConstants.BAST_TYPE_PARAMETER + 0),
  TYPE_PARAMETER_LIST(
      "TYPE_PARAMETER_LIST",
      TagConstants.BAST_TYPE_PARAMETER,
      10 * TagConstants.BAST_TYPE_PARAMETER + 1,
      true),
  TYPE_SPECIFIER_TYPE(
      "TYPE_SPECIFIER_TYPE",
      TagConstants.BAST_TYPE_SPECIFIER,
      10 * TagConstants.BAST_TYPE_SPECIFIER + 0),
  UNARY_EXPR_OPERAND(
      "UNARY_EXPR_OPERAND",
      TagConstants.BAST_UNARY_EXPR,
      10 * TagConstants.BAST_UNARY_EXPR + 0),
  WHILE_EXPRESSION(
      "WHILE_EXPRESSION",
      TagConstants.BAST_WHILE_STATEMENT,
      10 * TagConstants.BAST_WHILE_STATEMENT + 0),
  WHILE_STATEMENT(
      "WHILE_STATEMENT",
      TagConstants.BAST_WHILE_STATEMENT,
      10 * TagConstants.BAST_WHILE_STATEMENT + 1),
  XOR_LEFT("XOR_LEFT", TagConstants.BAST_XOR, 10 * TagConstants.BAST_XOR + 0),
  XOR_RIGHT("XOR_RIGHT", TagConstants.BAST_XOR, 10 * TagConstants.BAST_XOR + 1),
  INVALID_FIELD_ID("INVALID_FIELD_ID", -1, -1),
  INODE_FIELD_ID("INODE_FIELD_ID", -2, -2),
  NEXT_NAME_IDENT(
      "NESTED_NAMESPACE_IDENT",
      TagConstants.BAST_NAMESPACE_IDENT,
      10 * TagConstants.BAST_NAMESPACE_IDENT + 0);



  private static HashMap<Integer, BastFieldConstants> map = new HashMap<>();
  /**
   * Gets the constant.
   *
   * @param id the id
   * @return the constant
   */
  public static synchronized BastFieldConstants getConstant(int id) {
    if (map.isEmpty()) {
      for (BastFieldConstants fc : EnumSet.allOf(BastFieldConstants.class)) {
        map.put(fc.id, fc);
      }
    }
    return map.get(id);
  }
  
  public final String name;
  public final int id;

  public final int sanityCheck;

  public final boolean isList;

  private BastFieldConstants(String name, int sanityCheck, int id) {
    this(name, sanityCheck, id, false);
  }

  private static HashMap<Integer, List<BastFieldConstants>> tagMap = new HashMap<>();
  
  /**
   * Gets the field constants.
   *
   * @param tag the tag
   * @return the list
   */
  public static synchronized List<BastFieldConstants> getFieldMap(int tag) {
    if (tagMap.isEmpty()) {
      for (BastFieldConstants fc : EnumSet.allOf(BastFieldConstants.class)) {
        List<BastFieldConstants> tmpList = tagMap.get(fc.sanityCheck);
        if (tmpList == null) {
          tmpList = new ArrayList<>(5);
          tagMap.put(fc.sanityCheck, tmpList);
        }
        tmpList.add(fc);
      }
    }
    List<BastFieldConstants> staticList = tagMap.get(tag);
    if (staticList == null) {
      return null;
    }
    List<BastFieldConstants> newList = new ArrayList<>(staticList);
    return newList;
  }
  
  
  private BastFieldConstants(String name, int sanityCheck, int id, boolean isList) {
    this.name = name;
    this.id = id;
    this.sanityCheck = sanityCheck;
    this.isList = isList;
  }
}
