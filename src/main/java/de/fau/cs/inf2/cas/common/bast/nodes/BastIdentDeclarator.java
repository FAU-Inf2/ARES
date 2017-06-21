package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo.
 *
 * <p>Combines an identifier with its declaration details
 *  Necessary for nested declarations TODO: See
 * if it's possible to do that easier
 * 
 */
public class BastIdentDeclarator extends AbstractBastDeclarator {

  public static final int TAG = TagConstants.BAST_IDENT_DECLARATOR;
  public AbstractBastIdentifier identifier = null;
  public AbstractBastExpr expression = null;
  public AbstractBastExpr declarator = null;

  public BastPointer pointer = null;

  /**
   * Instantiates a new bast ident declarator.
   *
   * @param tokens the tokens
   * @param identifier the identifier
   * @param expression the expression
   * @param arrayDeclarator the array declarator
   */
  public BastIdentDeclarator(TokenAndHistory[] tokens, AbstractBastIdentifier identifier,
      AbstractBastExpr expression, AbstractBastExpr arrayDeclarator) {
    super(tokens);
    this.identifier = identifier;
    fieldMap.put(BastFieldConstants.IDENT_DECLARATOR_IDENTIFIER, new BastField(identifier));
    this.expression = expression;
    fieldMap.put(BastFieldConstants.IDENT_DECLARATOR_EXPRESSION, new BastField(expression));
    this.declarator = arrayDeclarator;
    fieldMap.put(BastFieldConstants.IDENT_DECLARATOR_ARRAY_DECLARATOR,
        new BastField(arrayDeclarator));

  }

  /**
   * Instantiates a new bast ident declarator.
   *
   * @param tokens the tokens
   * @param ident the ident
   */
  public BastIdentDeclarator(TokenAndHistory[] tokens, BastNameIdent ident) {
    this(tokens, ident, null, null);
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
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case IDENT_DECLARATOR_EXPRESSION:
        this.expression = (AbstractBastExpr) fieldValue.getField();
        break;
      case IDENT_DECLARATOR_ARRAY_DECLARATOR:
        this.declarator = (AbstractBastExpr) fieldValue.getField();
        break;
      case IDENT_DECLARATOR_IDENTIFIER:
        this.identifier = (AbstractBastIdentifier) fieldValue.getField();
        break;
      case IDENT_DECLARATOR_POINTER:
        this.pointer = (BastPointer) fieldValue.getField();
        break;
      default:
        assert (false);

    }
  }

  
  /**
   * Sets the initializer.
   *
   * @param init the new initializer
   */
  @Override
  public void setInitializer(AbstractBastInitializer init) {
    this.expression = init;
    fieldMap.put(BastFieldConstants.IDENT_DECLARATOR_EXPRESSION, new BastField(expression));

  }

  
  /**
   * Sets the pointer.
   *
   * @param pointer the new pointer
   */
  @Override
  public void setPointer(BastPointer pointer) {
    this.pointer = pointer;
    fieldMap.put(BastFieldConstants.IDENT_DECLARATOR_POINTER, new BastField(pointer));

  }

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    String tmp = "";
    if (pointer != null) {
      tmp += pointer.toString() + " ";
    }
    tmp += identifier.getName() + " ";
    if (expression != null) {
      tmp += " + " + expression.toString();
    }
    return tmp;
  }

}
