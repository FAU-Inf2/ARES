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
 * <p>Java annotation, can have a list of item = value pairs e.g. (a = "1", b = "2",)
 * 
 */
public class BastAnnotation extends AbstractBastSpecifier {

  public static final int TAG = TagConstants.BAST_ANNOTATION;
  public AbstractBastExpr name = null;
  public LinkedList<AbstractBastExpr> exprList = null;

  /**
   * Instantiates a new bast annotation.
   *
   * @param tokens the tokens
   * @param name the name
   * @param exprList the expr list
   */
  public BastAnnotation(TokenAndHistory[] tokens, AbstractBastExpr name,
      LinkedList<AbstractBastExpr> exprList) {
    super(tokens);
    this.name = name;
    fieldMap.put(BastFieldConstants.ANNOTATION_NAME, new BastField(name));
    this.exprList = exprList;
    fieldMap.put(BastFieldConstants.ANNOTATION_EXPRLIST, new BastField(exprList));

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
      case ANNOTATION_NAME:
        this.name = (AbstractBastExpr) fieldValue.getField();
        break;
      case ANNOTATION_EXPRLIST:
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
    return name != null ? name.toString() : "";
  }

}
