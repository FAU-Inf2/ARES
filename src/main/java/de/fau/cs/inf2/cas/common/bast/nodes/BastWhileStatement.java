package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


public class BastWhileStatement extends AbstractBastStatement {

  public static final int TAG = TagConstants.BAST_WHILE_STATEMENT;
  public static final int TYPE_WHILE = 0;
  public static final int TYPE_DO_WHILE = 1;
  public int type = TYPE_WHILE;

  public AbstractBastExpr expression = null;
  public AbstractBastStatement statement = null;

  /**
   * Instantiates a new bast while statement.
   *
   * @param tokens the tokens
   * @param expression the expression
   * @param statement the statement
   * @param type the type
   */
  public BastWhileStatement(TokenAndHistory[] tokens, AbstractBastExpr expression,
      AbstractBastStatement statement, int type) {
    super(tokens);
    this.expression = expression;
    fieldMap.put(BastFieldConstants.WHILE_EXPRESSION, new BastField(expression));
    this.statement = statement;
    fieldMap.put(BastFieldConstants.WHILE_STATEMENT, new BastField(statement));
    this.type = type;
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
      case WHILE_EXPRESSION:
        this.expression = (AbstractBastExpr) fieldValue.getField();
        break;
      case WHILE_STATEMENT:
        this.statement = (AbstractBastStatement) fieldValue.getField();
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
    String string = null;
    if (type == TYPE_WHILE) {
      string = "while (" + this.expression + ")" + this.statement;

    } else {
      string = "do " + this.statement + "while (" + this.expression + ")";
    }
    return string;
  }

}
