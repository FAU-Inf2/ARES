package de.fau.cs.inf2.cas.ares.bast.nodes;

import de.fau.cs.inf2.cas.ares.bast.visitors.IAresBastVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractAresStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;

public class AresBlock extends AbstractAresStatement {

  public static final int TAG = TagConstants.ARES_BLOCK;

  public BastIntConst numberNode = null;
  public long number = -1;
  public BastBlock block;
  public LinkedList<AbstractBastExpr> identifiers = new LinkedList<AbstractBastExpr>();

  /**
   * Instantiates a new ARES block.
   *
   * @param tokens the tokens
   * @param number the number
   * @param block the block
   * @param identifiers the identifiers
   */
  public AresBlock(TokenAndHistory[] tokens, BastIntConst number, BastBlock block,
      LinkedList<AbstractBastExpr> identifiers) {
    super(tokens);
    this.numberNode = number;
    fieldMap.put(BastFieldConstants.ARES_BLOCK_NUMBER, new BastField(number));
    fieldMap.put(BastFieldConstants.ARES_BLOCK_BLOCK, new BastField(block));
    fieldMap.put(BastFieldConstants.ARES_BLOCK_IDENTIFIERS, new BastField(identifiers));

    if (number != null) {
      this.number = number.value;
    }
    this.block = block;
    this.identifiers = identifiers;
  }

  
  /**
   * Accept.
   *
   * @param visitor the visitor
   */
  public void accept(IAresBastVisitor visitor) {
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
      case ARES_BLOCK_BLOCK:
        this.block = (BastBlock) fieldValue.getField();
        break;
      case ARES_BLOCK_NUMBER:
        this.numberNode = (BastIntConst) fieldValue.getField();
        break;
      case ARES_BLOCK_IDENTIFIERS:
        this.identifiers = (LinkedList<AbstractBastExpr>) fieldValue.getListField();
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
    return "//# match (" + this.numberNode + ")" + this.block;
  }

}
