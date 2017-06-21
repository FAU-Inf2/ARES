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
 */
public class BastInterfaceDecl extends AbstractBastInternalDecl {

  public static final int TAG = TagConstants.BAST_INTERFACE;
  public BastNameIdent name = null;
  public LinkedList<BastTypeParameter> typeParameters = null;
  public LinkedList<BastType> interfaces = null;
  public LinkedList<AbstractBastInternalDecl> declarations = null;
  public LinkedList<AbstractBastSpecifier> modifiers = null;

  /**
   * Instantiates a new bast interface decl.
   *
   * @param tokens the tokens
   * @param name the name
   * @param typeParameters the type parameters
   * @param interfaces the interfaces
   * @param declarations the declarations
   */
  public BastInterfaceDecl(TokenAndHistory[] tokens, BastNameIdent name,
      LinkedList<BastTypeParameter> typeParameters, LinkedList<BastType> interfaces,
      LinkedList<AbstractBastInternalDecl> declarations) {
    super(tokens);
    this.name = name;
    fieldMap.put(BastFieldConstants.INTERFACE_DECL_NAME, new BastField(name));
    this.typeParameters = typeParameters;
    fieldMap.put(BastFieldConstants.INTERFACE_DECL_TYPE_PARAMETERS, new BastField(typeParameters));
    this.interfaces = interfaces;
    fieldMap.put(BastFieldConstants.INTERFACE_DECL_INTERFACES, new BastField(interfaces));
    this.declarations = declarations;
    fieldMap.put(BastFieldConstants.INTERFACE_DECL_DECLARATIONS, new BastField(declarations));

  }

  
  /**
   * Accept.
   *
   * @param visitor the visitor
   */
  @Override
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
      case INTERFACE_DECL_NAME:
        this.name = (BastNameIdent) fieldValue.getField();
        break;
      case INTERFACE_DECL_TYPE_PARAMETERS:
        this.typeParameters = (LinkedList<BastTypeParameter>) fieldValue.getListField();
        break;
      case INTERFACE_DECL_INTERFACES:
        this.interfaces = (LinkedList<BastType>) fieldValue.getListField();
        break;
      case INTERFACE_DECL_DECLARATIONS:
        this.declarations = (LinkedList<AbstractBastInternalDecl>) fieldValue.getListField();
        break;
      case INTERFACE_DECL_MODIFIERS:
        this.modifiers = (LinkedList<AbstractBastSpecifier>) fieldValue.getListField();
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
    fieldMap.put(BastFieldConstants.INTERFACE_DECL_MODIFIERS, new BastField(modifiers));

  }

}
