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
 * <p>List of expressions
 * 
 */
public class BastExprList extends AbstractBastExpr {
  public static final int TAG = TagConstants.BAST_EXPR_LIST;
  public LinkedList<AbstractBastExpr> list = null;

  /**
   * Instantiates a new bast expr list.
   *
   * @param tokens the tokens
   * @param list the list
   */
  public BastExprList(TokenAndHistory[] tokens, LinkedList<AbstractBastExpr> list) {
    super(tokens);
    this.list = list;
    fieldMap.put(BastFieldConstants.EXPR_LIST_LIST, new BastField(list));
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
      case EXPR_LIST_LIST:
        this.list = (LinkedList<AbstractBastExpr>) fieldValue.getListField();
        break;
      default:
        assert (false);

    }
  }

}
