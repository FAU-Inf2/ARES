package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo.
 *
 * <p>Used for nested declarations TODO: Look for an easier way to do that, to avoid this class
 * 

 */
public class BastRegularDeclarator extends AbstractBastDeclarator {

  public static final int TAG = TagConstants.BAST_REGULAR_DECLARATOR;

  public AbstractBastDeclarator declaration = null;
  public AbstractBastExpr expression = null;
  public BastPointer pointer = null;
  public AbstractBastInitializer init = null;

  /**
   * Instantiates a new bast regular declarator.
   *
   * @param tokens the tokens
   * @param declaration the declaration
   * @param expression the expression
   */
  public BastRegularDeclarator(TokenAndHistory[] tokens, AbstractBastDeclarator declaration,
      AbstractBastExpr expression) {
    super(tokens);
    this.declaration = declaration;
    fieldMap.put(BastFieldConstants.REGULAR_DECLARATOR_DECLARATION, new BastField(declaration));
    this.expression = expression;
    fieldMap.put(BastFieldConstants.REGULAR_DECLARATOR_EXPRESSION, new BastField(expression));
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
    this.init = init;
    fieldMap.put(BastFieldConstants.REGULAR_DECLARATOR_INIT, new BastField(init));

  }

  
  /**
   * Sets the pointer.
   *
   * @param pointer the new pointer
   */
  @Override
  public void setPointer(BastPointer pointer) {
    this.pointer = pointer;
    fieldMap.put(BastFieldConstants.REGULAR_DECLARATOR_POINTER, new BastField(pointer));

  }

}
