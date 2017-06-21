package de.fau.cs.inf2.cas.ares.bast.nodes;

import de.fau.cs.inf2.cas.ares.bast.visitors.IAresBastVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

/**
 * todo.
 *
 * <p>ARES statement subclause to specify the place of insertad statements
 * 
 */
public class AresPatternClause extends AbstractAresClause {

  public static final int TAG = TagConstants.ARES_PATTERN;

  public BastIntConst occurrence = null;
  public AbstractBastNode expr = null;
  public BastNameIdent ident = null;

  /**
   * Instantiates a new ARES pattern clause.
   *
   * @param tokens the tokens
   * @param occurrence the occurrence
   * @param expr the expr
   */
  public AresPatternClause(TokenAndHistory[] tokens, BastIntConst occurrence,
      AbstractBastExpr expr) {
    super(tokens);
    this.occurrence = occurrence;
    fieldMap.put(BastFieldConstants.ARES_PATTERN_CLAUSE_OCCURENCE, new BastField(occurrence));
    this.expr = expr;
    fieldMap.put(BastFieldConstants.ARES_PATTERN_CLAUSE_EXPR, new BastField(expr));

  }

  /**
   * Instantiates a new ARES pattern clause.
   *
   * @param tokens the tokens
   * @param occurrence the occurrence
   * @param expr the expr
   * @param ident the ident
   */
  public AresPatternClause(TokenAndHistory[] tokens, BastIntConst occurrence,
      AbstractBastNode expr, BastNameIdent ident) {
    super(tokens);
    this.occurrence = occurrence;
    fieldMap.put(BastFieldConstants.ARES_PATTERN_CLAUSE_OCCURENCE, new BastField(occurrence));
    this.expr = expr;
    fieldMap.put(BastFieldConstants.ARES_PATTERN_CLAUSE_EXPR, new BastField(expr));
    this.ident = ident;
    fieldMap.put(BastFieldConstants.ARES_PATTERN_CLAUSE_IDENT, new BastField(ident));

  }

  /**
   * Instantiates a new ARES pattern clause.
   *
   * @param tokens the tokens
   * @param occurrence the occurrence
   * @param expr the expr
   * @param ident the ident
   */
  public AresPatternClause(TokenAndHistory[] tokens, BastNameIdent ident ,
      AbstractBastNode expr, BastIntConst occurrence) {
    super(tokens);
    this.occurrence = occurrence;
    fieldMap.put(BastFieldConstants.ARES_PATTERN_CLAUSE_OCCURENCE, new BastField(occurrence));
    this.expr = expr;
    fieldMap.put(BastFieldConstants.ARES_PATTERN_CLAUSE_EXPR, new BastField(expr));
    this.ident = ident;
    fieldMap.put(BastFieldConstants.ARES_PATTERN_CLAUSE_IDENT, new BastField(ident));

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
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case ARES_PATTERN_CLAUSE_OCCURENCE:
        this.occurrence = (BastIntConst) fieldValue.getField();
        break;
      case ARES_PATTERN_CLAUSE_EXPR:
        this.expr = (AbstractBastExpr) fieldValue.getField();
        break;
      case ARES_PATTERN_CLAUSE_IDENT:
        this.ident = (BastNameIdent) fieldValue.getField();
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
    String tmp = "";
    if (occurrence != null) {
      tmp += occurrence + ", ";
    }
    if (expr != null) {
      tmp += expr + ", ";
    }
    if (ident != null) {
      tmp += ident + ", ";
    }
    return tmp;
  }

}
