package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.type.BastType;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;
import java.util.List;


public class BastEnumSpec extends AbstractBastSpecifierQualifier {

  public static final int TAG = TagConstants.BAST_ENUM_SPEC;

  public BastNameIdent name = null;
  public List<BastEnumMember> members = null;
  public LinkedList<BastType> interfaces = null;
  public LinkedList<AbstractBastInternalDecl> declarations = null;

  /**
   * Instantiates a new bast enum spec.
   *
   * @param tokens the tokens
   * @param name the name
   * @param interfaces the interfaces
   * @param members the members
   * @param declarations the declarations
   */
  public BastEnumSpec(TokenAndHistory[] tokens, BastNameIdent name, LinkedList<BastType> interfaces,
      LinkedList<BastEnumMember> members, LinkedList<AbstractBastInternalDecl> declarations) {
    super(tokens);
    this.name = name;
    fieldMap.put(BastFieldConstants.ENUM_SPEC_NAME, new BastField(name));
    this.members = members;
    fieldMap.put(BastFieldConstants.ENUM_SPEC_MEMBERS, new BastField(members));
    this.interfaces = interfaces;
    fieldMap.put(BastFieldConstants.ENUM_SPEC_INTERFACES, new BastField(interfaces));
    this.declarations = declarations;
    fieldMap.put(BastFieldConstants.ENUM_SPEC_DECLARATIONS, new BastField(declarations));
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
      case ENUM_SPEC_NAME:
        this.name = (BastNameIdent) fieldValue.getField();
        break;
      case ENUM_SPEC_MEMBERS:
        this.members = (List<BastEnumMember>) fieldValue.getListField();
        break;
      case ENUM_SPEC_INTERFACES:
        this.interfaces = (LinkedList<BastType>) fieldValue.getListField();
        break;
      case ENUM_SPEC_DECLARATIONS:
        this.declarations = (LinkedList<AbstractBastInternalDecl>) fieldValue.getListField();
        break;
      default:
        assert (false);

    }
  }
}
