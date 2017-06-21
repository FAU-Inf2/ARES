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
 * <p>Represents structs of C
 * 

 */
public class BastStructDecl extends AbstractBastExternalDecl {

  public static final int TAG = TagConstants.BAST_STRUCT_DECL;
  public String name = null;
  public LinkedList<BastStructMember> members = null;
  @SuppressWarnings("unused")
  private LinkedList<AbstractBastSpecifierQualifier> specifierQualifier;
  @SuppressWarnings("unused")
  private LinkedList<BastStructDeclarator> declarators;

  /**
   * todo.
   * Struct in C with Token and history
   * 
   * @param tokens the tokens
   * @param specifierQualifier the specifier qualifier
   * @param declarators todo
   */
  @Deprecated
  public BastStructDecl(TokenAndHistory[] tokens,
      LinkedList<AbstractBastSpecifierQualifier> specifierQualifier,
      LinkedList<BastStructDeclarator> declarators) {
    super(tokens);
    this.specifierQualifier = specifierQualifier;
    fieldMap.put(BastFieldConstants.STRUCT_DECL_SPECIFIER_QUALIFIER,
        new BastField(specifierQualifier));
    this.declarators = declarators;
    fieldMap.put(BastFieldConstants.STRUCT_DECL_DECLARATORS, new BastField(declarators));
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
   * Sets the modifiers.
   *
   * @param modifiers the new modifiers
   */
  @Override
  public void setModifiers(LinkedList<AbstractBastSpecifier> modifiers) {
    assert (false);
  }
}
