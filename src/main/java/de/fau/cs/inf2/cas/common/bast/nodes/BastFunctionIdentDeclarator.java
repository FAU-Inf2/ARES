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
 * <p>Function header in c, if the parameter list contains no types but only identifiers (old c)
 * 
 */
public class BastFunctionIdentDeclarator extends AbstractBastDeclarator {

  public static final int TAG = TagConstants.BAST_FUNCTION_IDENT_DECL;
  public LinkedList<BastNameIdent> parameters = null;

  public AbstractBastExpr function = null;

  /**
   * Instantiates a new bast function ident declarator.
   *
   * @param tokens the tokens
   * @param parameters the parameters
   * @param function the function
   */
  public BastFunctionIdentDeclarator(TokenAndHistory[] tokens, LinkedList<BastNameIdent> parameters,
      AbstractBastExpr function) {
    super(tokens);
    this.parameters = parameters;
    fieldMap.put(BastFieldConstants.FUNCTION_IDENT_DECLARATOR_PARAMTERS, new BastField(parameters));
    this.function = function;
    fieldMap.put(BastFieldConstants.FUNCTION_IDENT_DECLARATOR_FUNCTION, new BastField(function));
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
}
