package de.fau.cs.inf2.cas.ares.bast.nodes;

import de.fau.cs.inf2.cas.ares.bast.visitors.IAresBastVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;

/**
 * todo.
 *
 * <p>ARES statement subclause to specify the place of insertad statements
 * 
 */
public class AresPluginClause extends AbstractAresClause {

  public static final int TAG = TagConstants.ARES_PLUGIN;

  public BastNameIdent ident = null;
  public LinkedList<AbstractBastExpr> exprList = null;
  public AresPatternClause pattern = null;

  /**
   * Instantiates a new ARES plugin clause.
   *
   * @param tokens the tokens
   * @param ident the ident
   * @param exprList the expr list
   */
  public AresPluginClause(TokenAndHistory[] tokens, BastNameIdent ident,
      LinkedList<AbstractBastExpr> exprList) {
    super(tokens);
    this.ident = ident;
    fieldMap.put(BastFieldConstants.ARES_PLUGIN_CLAUSE_IDENT, new BastField(ident));
    this.exprList = exprList;
    fieldMap.put(BastFieldConstants.ARES_PLUGIN_CLAUSE_EXPR_LIST, new BastField(exprList));
  }

  
  /**
   * Accept.
   *
   * @param visitor the visitor
   */
  public void accept(IAresBastVisitor visitor) {
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
      case ARES_PLUGIN_CLAUSE_IDENT:
        this.ident = (BastNameIdent) fieldValue.getField();
        break;
      case ARES_PLUGIN_CLAUSE_EXPR_LIST:
        this.exprList = (LinkedList<AbstractBastExpr>) fieldValue.getListField();
        break;
      default:
        assert (false);
    }
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    if (exprList != null && exprList.size() > 0 && exprList.get(0) != null) {
      return this.ident + "(" + exprList.get(0) + ")";
    } else {
      return this.ident + "(" + ")";
    }
  }

}
