package de.fau.cs.inf2.cas.common.parser;

public class LexicalError extends SyntaxError {

  /**
   * todo.
   */
  private static final long serialVersionUID = -7433286813757889777L;

  public LexicalError(String msg, TokenPosition pos) {
    super(msg, pos);
  }

  public String toString() {
    return pos.line + ":" + pos.col + ": Lexical Error: " + msg;
  }
}
