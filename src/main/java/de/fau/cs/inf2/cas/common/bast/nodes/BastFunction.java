package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.type.BastType;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;


/**
 * todo.
 *
 * <p>Complete function, with specifier, declarator and function block
 * 
 */
@SuppressWarnings("unused")
public class BastFunction extends AbstractBastInternalDecl {

  public static final int TAG = TagConstants.BAST_FUNCTION;

  public BastType returnType = null;
  public String name = null;

  private AbstractBastIdentifier nameIdent = null;

  public LinkedList<BastParameter> parameters = null;
  public LinkedList<AbstractBastStatement> statements = null;

  public LinkedList<AbstractBastSpecifier> specifierList = null;
  public LinkedList<AbstractBastExternalDecl> declList = null;
  public AbstractBastDeclarator decl = null;

  public LinkedList<AbstractBastExpr> exceptions = null;
  public LinkedList<BastTypeParameter> typeParameters = null;
  public LinkedList<AbstractBastSpecifier> modifiers = null;

  public boolean containsInsert = false;

  /**
   * todo.
   * Java constructor
   * 
   * @param specifierList todo
   * @param decl todo
   * @param declList todo
   * @param block todo
   * @param typeParameters todo
   * @param returnType todo
   * @param exceptions todo
   */
  public BastFunction(TokenAndHistory[] tokens, LinkedList<AbstractBastSpecifier> specifierList,
      AbstractBastDeclarator decl, LinkedList<AbstractBastExternalDecl> declList, BastBlock block,
      LinkedList<BastTypeParameter> typeParameters, BastType returnType,
      LinkedList<AbstractBastExpr> exceptions) {
    super(tokens);
    this.specifierList = specifierList;
    fieldMap.put(BastFieldConstants.FUNCTION_BLOCK_SPECIFIER_LIST, new BastField(specifierList));

    this.typeParameters = typeParameters;
    fieldMap.put(BastFieldConstants.FUNCTION_BLOCK_TYPE_PARAMTER, new BastField(typeParameters));

    this.exceptions = exceptions;
    fieldMap.put(BastFieldConstants.FUNCTION_BLOCK_EXCEPTIONS, new BastField(exceptions));

    this.returnType = returnType;
    fieldMap.put(BastFieldConstants.FUNCTION_BLOCK_RETURN_TYPE, new BastField(returnType));

    this.declList = declList;
    fieldMap.put(BastFieldConstants.FUNCTION_BLOCK_DECL_LIST, new BastField(declList));

    this.decl = decl;
    fieldMap.put(BastFieldConstants.FUNCTION_BLOCK_DECL, new BastField(decl));

    if (decl != null) {
      if (decl.getTag() == BastIdentDeclarator.TAG) {
        this.name = ((BastIdentDeclarator) decl).identifier.getName();
      }
    }
    this.statements = new LinkedList<AbstractBastStatement>();
    fieldMap.put(BastFieldConstants.FUNCTION_BLOCK_STATEMENTS, new BastField(statements));

    if (block != null) {
      this.statements.add(block);
    }
  }

  
  /**
   * Accept.
   *
   * @param visitor the visitor
   */
  public void accept(IBastVisitor visitor) {
    visitor.visit(this);
  }

  
  /**
   * Gets the tag.
   *
   * @return the tag
   */
  @Override
  public int getTag() {
    return TAG;
  }

  
  /**
   * Replace field.
   *
   * @param field the field
   * @param fieldValue the field value
   */
  @SuppressWarnings("unchecked")
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case FUNCTION_BLOCK_RETURN_TYPE:
        this.returnType = (BastType) fieldValue.getField();
        break;
      case FUNCTION_BLOCK_NAME_IDENT:
        this.nameIdent = (AbstractBastIdentifier) fieldValue.getField();
        break;
      case FUNCTION_BLOCK_PARAMETERS:
        this.parameters = (LinkedList<BastParameter>) fieldValue.getListField();
        break;
      case FUNCTION_BLOCK_STATEMENTS:
        this.statements = (LinkedList<AbstractBastStatement>) fieldValue.getListField();
        break;
      case FUNCTION_BLOCK_MODIFIERS:
        this.modifiers = (LinkedList<AbstractBastSpecifier>) fieldValue.getListField();
        break;
      case FUNCTION_BLOCK_SPECIFIER_LIST:
        this.specifierList = (LinkedList<AbstractBastSpecifier>) fieldValue.getListField();
        break;
      case FUNCTION_BLOCK_TYPE_PARAMTER:
        this.typeParameters = (LinkedList<BastTypeParameter>) fieldValue.getListField();
        break;
      case FUNCTION_BLOCK_EXCEPTIONS:
        this.exceptions = (LinkedList<AbstractBastExpr>) fieldValue.getListField();
        break;
      case FUNCTION_BLOCK_DECL_LIST:
        this.declList = (LinkedList<AbstractBastExternalDecl>) fieldValue.getListField();
        break;
      case FUNCTION_BLOCK_DECL:
        this.decl = (AbstractBastDeclarator) fieldValue.getField();
        break;
      default:
        assert (false);

    }
  }

  
  /**
   * Sets the modifiers.
   *
   * @param modifiers the new modifiers
   */
  @Override
  public void setModifiers(LinkedList<AbstractBastSpecifier> modifiers) {
    this.modifiers = modifiers;
    fieldMap.put(BastFieldConstants.FUNCTION_BLOCK_MODIFIERS, new BastField(modifiers));

  }
}
