package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * Function header, combines the parameter list with the function name, modifier etc.
 * 
 */
public class BastFunctionParameterDeclarator extends AbstractBastDeclarator {

  public static final int TAG = TagConstants.BAST_FUNCTION_PARAMETER_DECL;
  public BastParameterList parameters = null;
  public AbstractBastExpr function = null;

  public BastPointer pointer = null;

  /**
   * Instantiates a new bast function parameter declarator.
   *
   * @param tokens the tokens
   * @param parameters the parameters
   * @param function the function
   */
  public BastFunctionParameterDeclarator(TokenAndHistory[] tokens, BastParameterList parameters,
      AbstractBastExpr function) {
    super(tokens);
    this.parameters = parameters;
    fieldMap.put(BastFieldConstants.FUNCTION_PARAMETER_DECLARATOR_PARAMETERS,
        new BastField(parameters));
    this.function = function;
    fieldMap.put(BastFieldConstants.FUNCTION_PARAMETER_DECLARATOR_FUNCTION,
        new BastField(function));
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
    this.pointer = pointer;
    fieldMap.put(BastFieldConstants.FUNCTION_PARAMETER_DECLARATOR_POINTER, new BastField(pointer));

  }
  
  /**
   * Replace field.
   *
   * @param field the field
   * @param fieldValue the field value
   */
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case FUNCTION_PARAMETER_DECLARATOR_PARAMETERS:
        this.parameters = (BastParameterList) fieldValue.getField();
        break;
      case FUNCTION_PARAMETER_DECLARATOR_FUNCTION:
        this.function = (AbstractBastExpr) fieldValue.getField();
        break;
      case FUNCTION_PARAMETER_DECLARATOR_POINTER:
        this.pointer = (BastPointer) fieldValue.getField();
        break;
      default:
        assert (false);

    }
  }

  
}
