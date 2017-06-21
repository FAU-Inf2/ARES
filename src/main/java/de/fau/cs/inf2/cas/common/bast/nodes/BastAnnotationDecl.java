package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;

/**
 * todo.
 *
 * <p>To define new annotations
 * 
 */
public class BastAnnotationDecl extends AbstractBastInternalDecl {

  public static final int TAG = TagConstants.BAST_ANNOTATION_DECL;

  public BastNameIdent name = null;
  public LinkedList<AbstractBastExternalDecl> declarations = null;
  public LinkedList<AbstractBastSpecifier> modifiers = null;

  /**
   * Instantiates a new bast annotation decl.
   *
   * @param tokens the tokens
   * @param name the name
   * @param declarations the declarations
   */
  public BastAnnotationDecl(TokenAndHistory[] tokens, BastNameIdent name,
      LinkedList<AbstractBastExternalDecl> declarations) {
    super(tokens);
    this.name = name;
    fieldMap.put(BastFieldConstants.ANNOTATION_DECL_NAME, new BastField(name));
    this.declarations = declarations;
    fieldMap.put(BastFieldConstants.ANNOTATION_DECL_DECLARATIONS, new BastField(declarations));
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
      case ANNOTATION_DECL_NAME:
        this.name = (BastNameIdent) fieldValue.getField();
        break;
      case ANNOTATION_DECL_DECLARATIONS:
        this.declarations = (LinkedList<AbstractBastExternalDecl>) fieldValue.getListField();
        break;
      case ANNOTATION_DECL_MODIFIERS:
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
    fieldMap.put(BastFieldConstants.ANNOTATION_DECL_MODIFIERS, new BastField(modifiers));
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    return "AnnotationDecl[" + name + "] = " + declarations;
  }
}
