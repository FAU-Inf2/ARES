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
 * <p>Initializes an array with a list. Can be open on the right (eg. {5,};)
 * 

 */
public class BastListInitializer extends AbstractBastInitializer {

  public static final int TAG = TagConstants.BAST_LIST_INITIALIZER;

  public LinkedList<AbstractBastInitializer> list = null;
  public boolean open = false;

  /**
   * Instantiates a new bast list initializer.
   *
   * @param tokens the tokens
   * @param list the list
   * @param open the open
   */
  public BastListInitializer(TokenAndHistory[] tokens, LinkedList<AbstractBastInitializer> list,
      boolean open) {
    super(tokens);
    this.list = list;
    fieldMap.put(BastFieldConstants.LIST_INITIALIZER_INIT, new BastField(list));
    this.open = open;
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
      case LIST_INITIALIZER_INIT:
        this.list = (LinkedList<AbstractBastInitializer>) fieldValue.getListField();
        break;
      default:
        assert (false);

    }
  }
  
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    if (list != null) {
      if (list.size() > 0) {
        builder.append(list.getFirst());
      }
      if (list.size() > 1) {
        builder.append(" ... ");
        builder.append(list.getLast());
      }
    }
    builder.append("}");
    return builder.toString();
  }
}
