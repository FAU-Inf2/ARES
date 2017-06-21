package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo.
 *
 * <p>Label/Statement combination
 * 
 */
public class BastLabelStmt extends AbstractBastStatement {

  public static final int TAG = TagConstants.BAST_LABEL_STMT;

  public String name = null;
  public BastNameIdent ident = null;
  public AbstractBastStatement stmt = null;

  /**
   * Instantiates a new bast label stmt.
   *
   * @param tokens the tokens
   * @param name the name
   * @param stmt the stmt
   */
  public BastLabelStmt(TokenAndHistory[] tokens, AbstractBastExpr name,
      AbstractBastStatement stmt) {
    super(tokens);
    this.ident = (BastNameIdent) name;
    fieldMap.put(BastFieldConstants.LABEL_STMT_IDENT, new BastField(ident));
    this.stmt = stmt;
    fieldMap.put(BastFieldConstants.LABEL_STMT_STMT, new BastField(stmt));
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
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case LABEL_STMT_IDENT:
        this.ident = (BastNameIdent) fieldValue.getField();
        break;
      case LABEL_STMT_STMT:
        this.stmt = (AbstractBastStatement) fieldValue.getField();
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
  public String toString() {
    return ident + ":";
  }
}
