package de.fau.cs.inf2.cas.common.bast.general;

/**
 * todo.
 *
 * <p>All global tags used in the Ast nodes.
 * 
 */
public final class TagConstants {

  private static final int BAST_START = 0;
  private static final int CONSTRAINT_START = 2500;
  private static final int ARES_START = 1000;
  private static final int TYPE_START = 800;
  private static final int TOKEN_START = 4000;

  public static final int BAST_PROGRAM = BAST_START + 1;

  public static final int BAST_AND = BAST_START + 8;

  public static final int BAST_CAST_EXPR = BAST_START + 16;
  public static final int BAST_CMP = BAST_START + 17;
  public static final int BAST_COND_AND = BAST_START + BAST_START + 18;

  public static final int BAST_COND_OR = BAST_START + BAST_START + 20;

  public static final int BAST_CALL = BAST_START + 23;

  public static final int BAST_IF = BAST_START + 27;

  public static final int BAST_INT_CONST = BAST_START + 29;
  public static final int BAST_GOTO = BAST_START + 30;

  public static final int BAST_MULTI_EXPR = BAST_START + 39;

  public static final int BAST_OR = BAST_START + 41;
  public static final int BAST_PARAMETER = BAST_START + 42;

  public static final int BAST_REAL_CONST = BAST_START + 44;

  public static final int BAST_RETURN = BAST_START + 47;
  public static final int BAST_SHIFT = BAST_START + 48;

  public static final int BAST_STRING_CONST = BAST_START + 54;

  public static final int BAST_SWITCH = BAST_START + 57;

  public static final int BAST_BLOCK = BAST_START + 61;
  public static final int BAST_ACCESS = BAST_START + 63;

  public static final int BAST_STRUCT_MEMBER = BAST_START + 64;
  public static final int BAST_STRUCT_DECL = BAST_START + 65;
  public static final int BAST_CHAR_CONST = BAST_START + 66;
  public static final int BAST_IDENT_DECLARATOR = BAST_START + 67;
  public static final int BAST_TYPE_SPECIFIER = BAST_START + 68;
  public static final int BAST_EXPR_LIST = BAST_START + 69;
  public static final int BAST_NAME_IDENT = BAST_START + 70;
  public static final int BAST_DECLARATION = BAST_START + 71;
  public static final int BAST_FUNCTION_IDENT_DECL = BAST_START + 72;
  public static final int BAST_FUNCTION_PARAMETER_DECL = BAST_START + 73;
  public static final int BAST_PARAMETER_TYPE_LIST = BAST_START + 74;
  public static final int BAST_ARRAY_DECLARATOR = BAST_START + 75;

  public static final int BAST_ARRAY_REF = BAST_START + 76;
  public static final int BAST_ASGN_EXPR = BAST_START + 77;

  public static final int BAST_BLOCK_COMMENT = BAST_START + 79;
  public static final int BAST_BREAK = BAST_START + 80;
  public static final int BAST_CASE = BAST_START + 81;
  public static final int BAST_COND_EXPR = BAST_START + 82;
  public static final int BAST_CONTINUE = BAST_START + 83;
  public static final int BAST_DECR_EXPR = BAST_START + 84;

  public static final int BAST_ENUM_MEMBER = BAST_START + 86;
  public static final int BAST_EXPR_INITIALIZER = BAST_START + 87;
  public static final int BAST_FOR_STMT = BAST_START + 88;
  public static final int BAST_FUNCTION = BAST_START + 89;
  public static final int BAST_INCR_EXPR = BAST_START + 90;

  public static final int BAST_LABEL_STMT = BAST_START + 93;
  public static final int BAST_LINE_COMMENT = BAST_START + 94;
  public static final int BAST_LIST_INITIALIZER = BAST_START + 95;
  public static final int BAST_LOCAL_DECL = BAST_START + 96;

  public static final int BAST_POINTER = BAST_START + 102;

  public static final int BAST_REGULAR_DECLARATOR = BAST_START + 104;

  public static final int BAST_STORAGE_CLASS_SPECIFIER = BAST_START + 106;
  public static final int BAST_STRUCT_DECLARATOR = BAST_START + 107;
  public static final int BAST_TYPE_QUALIFIER = BAST_START + 108;
  public static final int BAST_WHILE_STATEMENT = BAST_START + 109;
  public static final int BAST_XOR = BAST_START + 110;

  public static final int BAST_CLASS_DECL = BAST_START + 111;
  public static final int BAST_NEW = BAST_START + 112;
  public static final int BAST_INCLUDE = BAST_START + 113;
  public static final int BAST_BASIC_TYPE = BAST_START + 114;
  public static final int BAST_ARRAY_TYPE = BAST_START + 115;
  public static final int BAST_ENUM_SPEC = BAST_START + 117;
  public static final int BAST_UNARY_EXPR = BAST_START + 118;
  public static final int BAST_BOOL_CONST = BAST_START + 119;
  public static final int BAST_NULL_CONST = BAST_START + 120;
  public static final int BAST_ANNOTATION = BAST_START + 121;
  public static final int BAST_ANNOTATION_DECL = BAST_START + 122;
  public static final int BAST_ANNOTATION_METHOD = BAST_START + 123;
  public static final int BAST_ANNOTATION_ELEM_VALUE = BAST_START + 124;
  public static final int BAST_PACKAGE = BAST_START + 125;
  public static final int BAST_IMPORT_DECL = BAST_START + 126;
  public static final int BAST_SYNCHRONIZED_BLOCK = BAST_START + 127;
  public static final int BAST_TRY = BAST_START + 128;
  public static final int BAST_INTERFACE = BAST_START + 129;
  public static final int BAST_THROW = BAST_START + 130;
  public static final int BAST_ASSERT = BAST_START + 131;
  public static final int BAST_THIS = BAST_START + 132;
  public static final int BAST_SUPER = BAST_START + 133;
  public static final int BAST_CATCH = BAST_START + 134;
  public static final int BAST_INSTANCE_OF = BAST_START + 135;
  public static final int BAST_ENUM_DECL = BAST_START + 136;
  public static final int BAST_ADDITIVE_EXPR = BAST_START + 137;
  public static final int BAST_TEMPLATE = BAST_START + 138;
  public static final int BAST_TYPE_PARAMETER = BAST_START + 139;
  public static final int BAST_DEFAULT = BAST_START + 140;
  public static final int BAST_SWITCH_CASE_GROUP = BAST_START + 141;
  public static final int BAST_EMPTY_STMT = BAST_START + 142;
  public static final int BAST_EMPTY_DECLARATION = BAST_START + 143;
  public static final int BAST_CLASS_CONST = BAST_START + 144;
  public static final int BAST_TYPE_ARGUMENT = BAST_START + 145;
  static final int BAST_NETWORK = BAST_START + 148;
  public static final int BAST_NAMESPACE_IDENT = BAST_START + 150;

  public static final int BAST_EXPR_WATER = BAST_START + 151;
  public static final int BAST_CLASS_KEY_SPECIFIER = BAST_START + 152;
  public static final int BAST_DUMMY_TOKEN = BAST_START + 153;

  public static final int ARES_BLOCK = ARES_START + 1;
  static final int ARES_PARALLEL = ARES_START + 3;
  public static final int ARES_PLACEHOLDER = ARES_START + 9;
  public static final int ARES_WILDCARD = ARES_START + 15;
  public static final int ARES_PLUGIN = ARES_START + 16;
  public static final int ARES_PATTERN = ARES_START + 18;
  public static final int ARES_USE = ARES_START + 19;
  static final int ARES_POS_FUNCTION = ARES_START + 21;
  public static final int ARES_CHOICE = ARES_START + 22;
  public static final int ARES_CASE = ARES_START + 23;

  public static final int CONSTRAINT_PLUGIN = CONSTRAINT_START + 3;

  public static final int TYPE_VOID = TagConstants.TYPE_START + 1;
  public static final int TYPE_BOOL = TagConstants.TYPE_START + 2;
  public static final int TYPE_BYTE = TagConstants.TYPE_START + 3;
  public static final int TYPE_CHAR = TagConstants.TYPE_START + 4;
  public static final int TYPE_WORD = TagConstants.TYPE_START + 5;
  public static final int TYPE_DWORD = TagConstants.TYPE_START + 6;
  public static final int TYPE_QWORD = TagConstants.TYPE_START + 7;
  public static final int TYPE_INT = TagConstants.TYPE_START + 8;
  public static final int TYPE_DINT = TagConstants.TYPE_START + 9;
  public static final int TYPE_QINT = TagConstants.TYPE_START + 10;
  public static final int TYPE_FLOAT = TagConstants.TYPE_START + 11;
  public static final int TYPE_STRING = TagConstants.TYPE_START + 12;
  public static final int TYPE_ARRAY = TagConstants.TYPE_START + 13;
  public static final int TYPE_POINTER = TagConstants.TYPE_START + 15;
  @SuppressWarnings("ucd")
  public static final int TYPE_GENERAL_INT = TagConstants.TYPE_START + 16;
  public static final int TYPE_GENERAL_BOOLEAN = TagConstants.TYPE_START + 17;
  public static final int TYPE_UNKNOWN = TagConstants.TYPE_START + 18;

  public static final int TYPE_SHORT = TagConstants.TYPE_START + 19;
  public static final int TYPE_SIGNED = TagConstants.TYPE_START + 20;
  public static final int TYPE_UNSIGNED = TagConstants.TYPE_START + 21;
  public static final int TYPE_DOUBLE = TagConstants.TYPE_START + 22;
  public static final int TYPE_LONG = TagConstants.TYPE_START + 23;

  public static final int TYPE_NONE = TagConstants.TYPE_START + 26;
  public static final int TYPE_TYPE_NAME = TagConstants.TYPE_START + 27;
  public static final int TYPE_CLASS = TagConstants.TYPE_START + 28;
  public static final int TYPE_BAST_STRUCT_OR_UNION = TagConstants.TYPE_START + 29;

  public static final int JAVA_TOKEN = TOKEN_START + 2;
  public static final int LIST_TOKEN = TOKEN_START + 3;
  public static final int C_TOKEN = TOKEN_START + 4;

}
