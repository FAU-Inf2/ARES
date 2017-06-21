package de.fau.cs.inf2.cas.common.bast.type;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

/**
 * todo.
 *
 * <p>Abstract general type class
 * 

 */
public abstract class BastType extends AbstractBastExpr {

  public static final int IN = 0;
  
  

  /**
   * Compatible types.
   *
   * @param firstType the a
   * @param secondType the b
   * @return true, if successful
   */
  public static boolean compatibleTypes(BastType firstType, BastType secondType) {
    int tag1 = firstType.getTag();
    int tag2 = secondType.getTag();
    switch (tag1) {
      case TagConstants.TYPE_BOOL:
        if (tag2 == TagConstants.TYPE_BOOL || tag2 == TagConstants.TYPE_GENERAL_BOOLEAN
            || tag2 == TagConstants.TYPE_GENERAL_INT) {
          return true;
        }
        break;
      case TagConstants.TYPE_BYTE:
        if (tag2 == TagConstants.TYPE_BYTE || tag2 == TagConstants.TYPE_GENERAL_INT) {
          return true;
        }
        break;
      case TagConstants.TYPE_CHAR:
        if (tag2 == TagConstants.TYPE_CHAR || tag2 == TagConstants.TYPE_GENERAL_INT) {
          return true;
        }
        break;
      case TagConstants.TYPE_WORD:
        if (tag2 == TagConstants.TYPE_WORD || tag2 == TagConstants.TYPE_GENERAL_INT) {
          return true;
        }
        break;
      case TagConstants.TYPE_DWORD:
        if (tag2 == TagConstants.TYPE_DWORD || tag2 == TagConstants.TYPE_GENERAL_INT) {
          return true;
        }
        break;
      case TagConstants.TYPE_QWORD:
        if (tag2 == TagConstants.TYPE_QWORD || tag2 == TagConstants.TYPE_GENERAL_INT) {
          return true;
        }
        break;
      case TagConstants.TYPE_INT:
        if (tag2 == TagConstants.TYPE_INT || tag2 == TagConstants.TYPE_GENERAL_INT) {
          return true;
        }
        break;
      case TagConstants.TYPE_DINT:
        if (tag2 == TagConstants.TYPE_DINT || tag2 == TagConstants.TYPE_GENERAL_INT) {
          return true;
        }
        break;
      case TagConstants.TYPE_QINT:
        if (tag2 == TagConstants.TYPE_QINT || tag2 == TagConstants.TYPE_GENERAL_INT) {
          return true;
        }
        break;
      case TagConstants.TYPE_NONE:
        return false;
      case TagConstants.TYPE_VOID:
        return false;
      case TagConstants.TYPE_UNKNOWN:
        return false;
      case TagConstants.TYPE_FLOAT:
        if (tag2 == TagConstants.TYPE_FLOAT) {
          return true;
        }
        break;
      case TagConstants.TYPE_STRING:
        if (tag2 == TagConstants.TYPE_STRING) {
          return true;
        }
        break;
      case TagConstants.TYPE_ARRAY:
        if (secondType.getTag() == TagConstants.TYPE_ARRAY) {
          if (((BastArrayType) secondType).type.getTag() 
              == ((BastArrayType) firstType).type.getTag()) {
            if (((BastArrayType) secondType).dimensions.size() 
                == ((BastArrayType) firstType).dimensions.size()) {
              return true;
            }
          }
        }
        break;
      /*
       * case TagConstants.TYPE_STRUCT: if (b.getTag() == TagConstants.TYPE_STRUCT){ if
       * (((BastStructType)a).structName.equals(((BastStructType)b).structName )){ return true; } }
       * break;
       */
      case TagConstants.TYPE_POINTER:
        if (secondType.getTag() == TagConstants.TYPE_POINTER) {
          return compatibleTypes(((BastPointerType) firstType).type,
              ((BastPointerType) secondType).type);
        }
        break;
      case TagConstants.TYPE_GENERAL_INT:
        switch (tag2) {
          case TagConstants.TYPE_BOOL:
          case TagConstants.TYPE_BYTE:
          case TagConstants.TYPE_CHAR:
          case TagConstants.TYPE_WORD:
          case TagConstants.TYPE_DWORD:
          case TagConstants.TYPE_QWORD:
          case TagConstants.TYPE_INT:
          case TagConstants.TYPE_DINT:
          case TagConstants.TYPE_QINT:
          case TagConstants.TYPE_GENERAL_BOOLEAN:
            return true;
          default:
            break;
        }
        break;
      case TagConstants.TYPE_GENERAL_BOOLEAN:
        if (tag2 == TagConstants.TYPE_BOOL || tag2 == TagConstants.TYPE_GENERAL_BOOLEAN) {
          return true;
        }
        break;
      default:
    }

    return false;
  }
  
  public int endLine = -1;

  public int endColumn = -1;

  /**
   * Instantiates a new bast type.
   *
   * @param tokens the tokens
   */
  public BastType(TokenAndHistory[] tokens) {
    super(tokens);
  }

}
