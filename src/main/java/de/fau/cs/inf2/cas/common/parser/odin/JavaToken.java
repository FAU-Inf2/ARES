package de.fau.cs.inf2.cas.common.parser.odin;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;

import de.fau.cs.inf2.cas.common.parser.IGeneralToken;
import de.fau.cs.inf2.cas.common.parser.TokenPosition;

public class JavaToken implements Cloneable, IGeneralToken {
  public static final int TAG = TagConstants.JAVA_TOKEN;

  int endPos = -1;
  public int endLine = -1;
  boolean insideComment = false;
  public StringBuilder whiteSpace = new StringBuilder();
  public StringBuilder data = new StringBuilder();
  public BasicJavaToken type = null;

  /**
   * Instantiates a new java token.
   */
  public JavaToken() {

  }

  /**
   * Instantiates a new java token.
   *
   * @param type the type
   * @param data the data
   */
  public JavaToken(BasicJavaToken type, String data) {
    this.type = type;
    this.data.append(data);
  }

  
  /**
   * Clone.
   *
   * @return the java token
   */
  @Override
  public JavaToken clone() {

    JavaToken clonedToken = new JavaToken(type, data.toString());
    clonedToken.whiteSpace.append(this.whiteSpace.toString());
    clonedToken.endPos = this.endPos;
    clonedToken.endLine = this.endLine;
    clonedToken.insideComment = this.insideComment;
    return clonedToken;
  }

  
  /**
   * Gets the code id.
   *
   * @return the code id
   */
  @Override
  public int getCodeId() {
    return type.id;
  }

  
  /**
   * Gets the pos.
   *
   * @return the pos
   */
  @Override
  public TokenPosition getPos() {
    return null;
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
   * Gets the text.
   *
   * @return the text
   */
  @Override
  public String getText() {
    return whiteSpace.toString() + data.toString();
  }
}
