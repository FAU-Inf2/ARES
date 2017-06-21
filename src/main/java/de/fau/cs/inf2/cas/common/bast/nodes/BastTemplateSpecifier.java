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
 * <p>Java template parameter
 * 

 */
public class BastTemplateSpecifier extends AbstractBastExpr {

  public static final int TAG = TagConstants.BAST_TEMPLATE;

  public AbstractBastExpr target = null;
  public LinkedList<BastTypeArgument> typeArguments = null;

  /**
   * Instantiates a new bast template specifier.
   *
   * @param tokens the tokens
   * @param target the target
   * @param typeArguments the type arguments
   */
  public BastTemplateSpecifier(TokenAndHistory[] tokens, AbstractBastExpr target,
      LinkedList<BastTypeArgument> typeArguments) {
    super(tokens);
    this.target = target;
    fieldMap.put(BastFieldConstants.TEMPLATE_SPECIFIER_TARGET, new BastField(target));
    this.typeArguments = typeArguments;
    fieldMap.put(BastFieldConstants.TEMPLATE_SPECIFIER_TYPE_ARGUMENTS,
        new BastField(typeArguments));
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
      case TEMPLATE_SPECIFIER_TARGET:
        this.target = (AbstractBastExpr) fieldValue.getField();
        break;
      case TEMPLATE_SPECIFIER_TYPE_ARGUMENTS:
        this.typeArguments = (LinkedList<BastTypeArgument>) fieldValue.getListField();
        break;
      default:
        assert (false);

    }
  }

}
