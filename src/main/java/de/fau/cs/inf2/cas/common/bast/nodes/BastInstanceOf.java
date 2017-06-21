package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.type.BastType;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo. 
 */
public class BastInstanceOf extends AbstractBastExpr {

  public static final int TAG = TagConstants.BAST_INSTANCE_OF;
  public AbstractBastExpr expr = null;
  public BastType type = null;

  /**
   * Instantiates a new bast instance of.
   *
   * @param tokens the tokens
   * @param expr the expr
   * @param type the type
   */
  public BastInstanceOf(TokenAndHistory[] tokens, AbstractBastExpr expr, BastType type) {
    super(tokens);
    this.expr = expr;
    fieldMap.put(BastFieldConstants.INSTANCE_OF_EXPR, new BastField(expr));
    this.type = type;
    fieldMap.put(BastFieldConstants.INSTANCE_OF_TYPE, new BastField(type));
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
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case INSTANCE_OF_EXPR:
        this.expr = (AbstractBastExpr) fieldValue.getField();
        break;
      case INSTANCE_OF_TYPE:
        this.type = (BastType) fieldValue.getField();
        break;
      default:
        assert (false);

    }
  }

}
