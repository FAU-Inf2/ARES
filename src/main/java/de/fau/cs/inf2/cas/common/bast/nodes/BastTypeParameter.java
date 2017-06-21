package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.type.BastType;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;


/**
 * todo. 

 */
public class BastTypeParameter extends AbstractBastNode {

  public static final int TAG = TagConstants.BAST_TYPE_PARAMETER;

  public BastNameIdent name = null;
  public LinkedList<BastType> list = null;

  /**
   * Instantiates a new bast type parameter.
   *
   * @param tokens the tokens
   * @param name the name
   * @param list the list
   */
  public BastTypeParameter(TokenAndHistory[] tokens, BastNameIdent name,
      LinkedList<BastType> list) {
    super(tokens);
    this.name = name;
    fieldMap.put(BastFieldConstants.TYPE_PARAMETER_NAME, new BastField(name));
    this.list = list;
    fieldMap.put(BastFieldConstants.TYPE_PARAMETER_LIST, new BastField(list));

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
      case TYPE_PARAMETER_NAME:
        this.name = (BastNameIdent) fieldValue.getField();
        break;
      case TYPE_PARAMETER_LIST:
        this.list = (LinkedList<BastType>) fieldValue.getListField();
        break;
      default:
        assert (false);

    }
  }

}
