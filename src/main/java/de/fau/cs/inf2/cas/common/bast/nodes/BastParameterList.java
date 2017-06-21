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
 * <p>List of parameters. An extra node type is necessary
 *  because in c the list can be open (end with
 * ...)
 * 

 */
public class BastParameterList extends AbstractBastNode {

  public static final int TAG = TagConstants.BAST_PARAMETER_TYPE_LIST;
  public boolean open = false;
  public LinkedList<BastParameter> parameters = null;

  /**
   * Instantiates a new bast parameter list.
   *
   * @param tokens the tokens
   * @param parameters the parameters
   * @param open the open
   */
  public BastParameterList(TokenAndHistory[] tokens, LinkedList<BastParameter> parameters,
      boolean open) {
    super(tokens);
    this.open = open;
    this.parameters = parameters;
    fieldMap.put(BastFieldConstants.PARAMETER_TYPE_LIST_PARAMETERS, new BastField(parameters));
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
      case PARAMETER_TYPE_LIST_PARAMETERS:
        this.parameters = (LinkedList<BastParameter>) fieldValue.getListField();
        break;
      default:
        assert (false);

    }
  }

}
