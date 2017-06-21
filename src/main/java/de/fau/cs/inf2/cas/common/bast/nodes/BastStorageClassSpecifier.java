package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;


/**
 * todo.
 *
 * <p>Represents the c and java variable specifiers like register, extern, static, typedef and auto
 * 

 */
public class BastStorageClassSpecifier extends AbstractBastSpecifier {

  public static final int TAG = TagConstants.BAST_STORAGE_CLASS_SPECIFIER;

  public static final int AUTO_SPECIFIER = 0;
  public static final int REGISTER_SPECIFIER = 1;
  private static final int EXTERN_SPECIFIER = 2;
  private static final int STATIC_SPECIFIER = 3;
  public static final int TYPEDEF_SPECIFIER = 4;
  public int type = -1;

  /**
   * Instantiates a new bast storage class specifier.
   *
   * @param tokens the tokens
   * @param type the type
   */
  public BastStorageClassSpecifier(TokenAndHistory[] tokens, int type) {
    super(tokens);
    this.type = type;
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
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    switch (type) {
      case AUTO_SPECIFIER:
        return "auto";
      case REGISTER_SPECIFIER:
        return "register";
      case EXTERN_SPECIFIER:
        return "extern";
      case STATIC_SPECIFIER:
        return "static";
      case TYPEDEF_SPECIFIER:
        return "typedef";
      default:
        assert false;
        return "";
    }
  }

}
