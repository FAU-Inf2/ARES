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
public class BastSwitchCaseGroup extends AbstractBastStatement {

  public static final int TAG = TagConstants.BAST_SWITCH_CASE_GROUP;
  public LinkedList<AbstractBastStatement> labels = null;
  public LinkedList<AbstractBastStatement> stmts = null;

  /**
   * Instantiates a new bast switch case group.
   *
   * @param tokens the tokens
   * @param labels the labels
   * @param stmts the stmts
   */
  public BastSwitchCaseGroup(TokenAndHistory[] tokens, LinkedList<AbstractBastStatement> labels,
      LinkedList<AbstractBastStatement> stmts) {
    super(tokens);
    this.labels = labels;
    fieldMap.put(BastFieldConstants.SWITCH_CASE_GROUP_LABELS, new BastField(labels));
    this.stmts = stmts;
    fieldMap.put(BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS, new BastField(stmts));
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
      case SWITCH_CASE_GROUP_LABELS:
        this.labels = (LinkedList<AbstractBastStatement>) fieldValue.getListField();
        break;
      case SWITCH_CASE_GROUP_STATEMENTS:
        this.stmts = (LinkedList<AbstractBastStatement>) fieldValue.getListField();
        break;
      default:
        assert (false);
    }
  }

}
