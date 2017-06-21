package de.fau.cs.inf2.cas.common.parser;

public class ParserError extends SyntaxError {

  /**
   * todo.
   *
   */
  private static final long serialVersionUID = -2467316934146435028L;

  /**
   * Instantiates a new parser error.
   *
   * @param msg the msg
   * @param tok the tok
   */
  public ParserError(String msg, IGeneralToken tok) {
    super(msg, tok);
    assert (tok != null);
  }

}
