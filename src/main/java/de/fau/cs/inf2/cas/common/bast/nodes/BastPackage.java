package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;


/**
 * todo. 

 */
public class BastPackage extends AbstractBastNode {

  public static final int TAG = TagConstants.BAST_PACKAGE;

  public LinkedList<BastAnnotation> annotations = null;
  public AbstractBastExpr name = null;

  /**
   * Instantiates a new bast package.
   *
   * @param tokens the tokens
   * @param name the name
   * @param annotations the annotations
   */
  public BastPackage(TokenAndHistory[] tokens, AbstractBastExpr name,
      LinkedList<BastAnnotation> annotations) {
    super(tokens);
    this.name = name;
    fieldMap.put(BastFieldConstants.PACKAGE_NAME, new BastField(name));
    this.annotations = annotations;
    fieldMap.put(BastFieldConstants.PACKAGE_ANNOTATIONS, new BastField(annotations));
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
      case PACKAGE_NAME:
        this.name = (AbstractBastExpr) fieldValue.getField();
        break;
      case PACKAGE_ANNOTATIONS:
        this.annotations = (LinkedList<BastAnnotation>) fieldValue.getListField();
        break;
      default:
        assert (false);

    }
  }

}
