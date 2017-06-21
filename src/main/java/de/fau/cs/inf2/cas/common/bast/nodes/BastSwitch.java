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
public class BastSwitch extends AbstractBastStatement {

  public static final int TAG = TagConstants.BAST_SWITCH;
  public AbstractBastExpr condition = null;
  public LinkedList<AbstractBastStatement> switchGroups = null;

  /**
   * Instantiates a new bast switch.
   *
   * @param tokens the tokens
   * @param condition the condition
   * @param switchGroups the switch groups
   * @param emptyGroup the empty group
   */
  public BastSwitch(TokenAndHistory[] tokens, AbstractBastExpr condition,
      LinkedList<AbstractBastStatement> switchGroups, AbstractBastStatement emptyGroup) {
    super(tokens);
    this.condition = condition;
    fieldMap.put(BastFieldConstants.SWITCH_CONDITION, new BastField(condition));
    this.switchGroups = switchGroups;
    if (switchGroups == null) {
      this.switchGroups = new LinkedList<AbstractBastStatement>();
    }
    fieldMap.put(BastFieldConstants.SWITCH_CASE_GROUPS, new BastField(switchGroups));
    if (emptyGroup != null) {
      this.switchGroups.add(emptyGroup);
    }
  }



  
  /**
   * Accept.
   *
   * @param visitor the visitor
   */
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
      case SWITCH_CONDITION:
        this.condition = (AbstractBastExpr) fieldValue.getField();
        break;
      case SWITCH_CASE_GROUPS:
        this.switchGroups = (LinkedList<AbstractBastStatement>) fieldValue.getListField();
        break;
      default:
        assert (false);
    }
  }

}
