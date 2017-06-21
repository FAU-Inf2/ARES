package de.fau.cs.inf2.cas.common.bast.type;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastSpecifierQualifier;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;


/**
 * todo. 

 */
public class BastTypeName extends BastType {
  public static final int TAG = TagConstants.TYPE_TYPE_NAME;
  public LinkedList<AbstractBastSpecifierQualifier> specifiers = null;
  public AbstractBastExpr declarator = null;

  /**
   * Instantiates a new bast type name.
   *
   * @param tokens the tokens
   * @param specifiers the specifiers
   * @param declarator the declarator
   */
  public BastTypeName(TokenAndHistory[] tokens,
      LinkedList<AbstractBastSpecifierQualifier> specifiers, AbstractBastExpr declarator) {
    super(tokens);
    this.specifiers = specifiers;
    fieldMap.put(BastFieldConstants.TYPE_NAME_SPECIFIERS, new BastField(specifiers));
    this.declarator = declarator;
    fieldMap.put(BastFieldConstants.TYPE_NAME_DECLARATOR, new BastField(declarator));
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
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    StringBuilder str = new StringBuilder();
    for (AbstractBastSpecifierQualifier spec : specifiers) {
      str.append(spec.toString());
      str.append(" ");
    }

    str.append(declarator.toString());

    return str.toString();
  }
}
