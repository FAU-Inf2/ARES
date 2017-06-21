package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


public class BastImportDeclaration extends AbstractBastNode {

  public static final int TAG = TagConstants.BAST_IMPORT_DECL;
  public AbstractBastExpr name = null;
  public boolean isStatic = false;
  public boolean isPackage = false;

  /**
   * Instantiates a new bast import declaration.
   *
   * @param tokens the tokens
   * @param name the name
   * @param isStatic the is static
   * @param isPackage the is package
   */
  public BastImportDeclaration(TokenAndHistory[] tokens, AbstractBastExpr name, boolean isStatic,
      boolean isPackage) {
    super(tokens);
    this.name = name;
    fieldMap.put(BastFieldConstants.IMPORT_NAME, new BastField(name));
    this.isStatic = isStatic;
    this.isPackage = isPackage;
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
      case IMPORT_NAME:
        this.name = (AbstractBastExpr) fieldValue.getField();
        break;
      default:
        assert (false);

    }
  }

}
