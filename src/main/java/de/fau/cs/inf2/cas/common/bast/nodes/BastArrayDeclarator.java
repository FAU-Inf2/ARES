package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;
/**
 * todo.
 *
 * <p>To declare an Array (no reference! to access an array element).
 * 
 */

public class BastArrayDeclarator extends AbstractBastDeclarator {

  public static final int TAG = TagConstants.BAST_ARRAY_DECLARATOR;
  public AbstractBastExpr index = null;
  public AbstractBastExpr source = null;
  public int dimensions = -1;

  /**
   * Instantiates a new bast array declarator.
   *
   * @param tokens the tokens
   * @param index the index
   * @param source the source
   */
  public BastArrayDeclarator(TokenAndHistory[] tokens, AbstractBastExpr index,
      AbstractBastExpr source) {
    super(tokens);
    this.index = index;
    fieldMap.put(BastFieldConstants.ARRAY_DECLARATOR_INDEX, new BastField(index));
    this.source = source;
    fieldMap.put(BastFieldConstants.ARRAY_DECLARATOR_SOURCE, new BastField(source));

  }

  /**
   * todo.
   * In Java it is possible to know the number of dimensions in some cases.
   * 
   * @param tokens the tokens
   * @param index index size if specified
   * @param source can be an identifier or another array declaration
   * @param dimensions todo
   */
  public BastArrayDeclarator(TokenAndHistory[] tokens, AbstractBastExpr index,
      AbstractBastExpr source, int dimensions) {
    super(tokens);
    this.index = index;
    fieldMap.put(BastFieldConstants.ARRAY_DECLARATOR_INDEX, new BastField(index));
    this.source = source;
    fieldMap.put(BastFieldConstants.ARRAY_DECLARATOR_SOURCE, new BastField(source));
    this.dimensions = dimensions;
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
   * Sets the initializer.
   *
   * @param init the new initializer
   */
  @Override
  public void setInitializer(AbstractBastInitializer init) {
    assert (false);

  }

  
  /**
   * Sets the pointer.
   *
   * @param pointer the new pointer
   */
  @Override
  public void setPointer(BastPointer pointer) {
    assert (false);

  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    String tmp = "[";
    if (index != null) {
      tmp += index.toString();
    }
    tmp += "]";
    return tmp;
  }

}
