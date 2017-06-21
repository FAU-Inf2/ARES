package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo.
 *
 * 

 */
public class BastRealConst extends AbstractBastConstant {

  public static final int TAG = TagConstants.BAST_REAL_CONST;
  public double value;
  public String actualValue = null;
  public boolean fitsInDouble = false;

  /**
   * todo.
   * Used to clone a constant.
   * 
   * @param tokens the tokens
   * @param value todo
   */
  public BastRealConst(TokenAndHistory[] tokens, double value) {
    super(tokens);
    this.value = value;
    actualValue = String.valueOf(value);
    fitsInDouble = true;
  }

  /**
   * Instantiates a new bast real const.
   *
   * @param tokens the tokens
   * @param value the value
   */
  public BastRealConst(TokenAndHistory[] tokens, String value) {
    super(tokens);

    int xvar = value.lastIndexOf(".d");
    if (xvar > 0) {
      value = value.replace(".d", ".e");
    }

    if (value.endsWith("d0")) {
      value = value.substring(0, value.length() - 1);
    } else if (value.endsWith("D0")) {
      value = value.substring(0, value.length() - 1);
    }
    this.value = Double.valueOf(value);
    actualValue = value;
    fitsInDouble = true;
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
   * Gets the priority.
   *
   * @return the priority
   */
  public int getPriority() {
    return 0;
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
   * To string.
   *
   * @return the string
   */
  public String toString() {
    return String.valueOf(value);
  }

}
