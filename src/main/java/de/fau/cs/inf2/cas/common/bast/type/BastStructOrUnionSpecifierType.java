package de.fau.cs.inf2.cas.common.bast.type;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastSpecifierQualifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStructDecl;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;


/**
 * todo.
 *
 * <p>Represents a C struct or union type, including the name
 * 

 */
public class BastStructOrUnionSpecifierType extends AbstractBastSpecifierQualifier {
  public static final int TAG = TagConstants.TYPE_BAST_STRUCT_OR_UNION;
  public static final int STRUCT = 0;
  public static final int UNION = 1;
  public LinkedList<BastStructDecl> list = null;
  public BastNameIdent name = null;
  public BastNameIdent identifier = null;

  /**
   * Instantiates a new bast struct or union specifier type.
   *
   * @param tokens the tokens
   * @param tag the tag
   * @param name the name
   * @param list the list
   * @param identifier the identifier
   */
  public BastStructOrUnionSpecifierType(TokenAndHistory[] tokens, int tag, BastNameIdent name,
      LinkedList<BastStructDecl> list, BastNameIdent identifier) {
    super(tokens);
    this.name = name;
    fieldMap.put(BastFieldConstants.STRUCT_OR_UNION_SPECIFIER_TYPE_NAME, new BastField(name));
    this.list = list;
    fieldMap.put(BastFieldConstants.STRUCT_OR_UNION_SPECIFIER_TYPE_STRUCT, new BastField(list));
    this.identifier = identifier;
    fieldMap.put(BastFieldConstants.STRUCT_OR_UNION_SPECIFIER_TYPE_IDENTIFIER,
        new BastField(identifier));
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
}
