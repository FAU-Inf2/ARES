package de.fau.cs.inf2.cas.common.parser;

import de.fau.cs.inf2.cas.common.parser.odin.JavaToken;

public class SyntaxError extends RuntimeException {
  /**
   * todo.
   */
  private static final long serialVersionUID = -7390098486387094810L;

  final TokenPosition pos;
  public final String msg;

  /**
   * Instantiates a new syntax error.
   *
   * @param msg the msg
   * @param pos the pos
   */
  public SyntaxError(String msg, IGeneralToken pos) {
    if (pos != null && pos.getPos() != null) {
      this.pos = new TokenPosition(pos.getPos().col, pos.getPos().line, -1, -1);
    } else {
      this.pos = null;
    }
    if (pos != null && pos.getTag() == JavaToken.TAG) {
      this.msg = msg + " " + ((JavaToken)pos).type.name;
    } else {
      this.msg = msg;
    }
    
  }

  /**
   * Instantiates a new syntax error.
   *
   * @param msg the msg
   * @param pos the pos
   */
  public SyntaxError(String msg, TokenPosition pos) {
    this.pos = pos;
    this.msg = msg;
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    if (pos != null) {
      return pos.line + ":" + pos.col + ": Syntax Error: " + msg;
    } else {
      return ": Syntax Error: " + msg;
    }
  }
}
