/*
 * Copyright (c) 2017 Programming Systems Group, CS Department, FAU
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */

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
