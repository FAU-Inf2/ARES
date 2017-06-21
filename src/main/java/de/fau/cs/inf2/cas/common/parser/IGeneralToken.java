package de.fau.cs.inf2.cas.common.parser;

public interface IGeneralToken extends Cloneable {
  
  /**
   * Clone.
   *
   * @return the i general token
   */
  public IGeneralToken clone();

  /**
   * Gets the code id.
   *
   * @return the code id
   */
  public int getCodeId();

  /**
   * Gets the pos.
   *
   * @return the pos
   */
  public TokenPosition getPos();

  /**
   * Gets the tag.
   *
   * @return the tag
   */
  public int getTag();

  /**
   * Gets the text.
   *
   * @return the text
   */
  public String getText();
}
