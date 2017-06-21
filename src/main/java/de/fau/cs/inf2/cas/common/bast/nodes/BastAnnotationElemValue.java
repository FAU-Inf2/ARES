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
 * <p>Annotation item initialization, can be an open list
 * 
 */
public class BastAnnotationElemValue extends AbstractBastSpecifier {

  public static final int TAG = TagConstants.BAST_ANNOTATION_ELEM_VALUE;
  public AbstractBastExpr qualifiedName = null;
  public LinkedList<AbstractBastExpr> initList = null;
  @SuppressWarnings("unused")
  private boolean withComma = false;

  /**
   * Instantiates a new bast annotation elem value.
   *
   * @param tokens the tokens
   * @param qualifiedName the qualified name
   * @param initList the init list
   * @param withComma the with comma
   */
  public BastAnnotationElemValue(TokenAndHistory[] tokens, AbstractBastExpr qualifiedName,
      LinkedList<AbstractBastExpr> initList, boolean withComma) {
    super(tokens);
    this.qualifiedName = qualifiedName;
    fieldMap.put(BastFieldConstants.ANNOTATION_ELEM_VALUE_QUALIFIED_NAME,
        new BastField(qualifiedName));
    this.initList = initList;
    fieldMap.put(BastFieldConstants.ANNOTATION_ELEM_VALUE_INITLIST, new BastField(initList));
    this.withComma = withComma;
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
      case ANNOTATION_ELEM_VALUE_QUALIFIED_NAME:
        this.qualifiedName = (AbstractBastExpr) fieldValue.getField();
        break;
      case ANNOTATION_ELEM_VALUE_INITLIST:
        this.initList = (LinkedList<AbstractBastExpr>) fieldValue.getListField();
        break;
      default:
        assert (false);

    }
  }

}
