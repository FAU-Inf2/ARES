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

public class TokenPosition implements IPositioned, Cloneable {
  /**
   * Enclose.
   *
   * @param left the _l
   * @param right the _r
   * @return the token position
   */
  public static TokenPosition enclose(IPositioned left, IPositioned right) {
    if (left == null && right == null) {
      return null;
    } else if (right == null) {
      return left.getPos();
    } else if (left == null) {
      return right.getPos();
    }

    TokenPosition leftLocal = left.getPos();
    TokenPosition rightLocal = right.getPos();
    assert (validatePositions(leftLocal, rightLocal)) : "broken TokenPosition instances";
    int last = Math.max(leftLocal.last, rightLocal.last);

    if (leftLocal.first <= rightLocal.first) {
      return leftLocal.moveLast(last);
    }
    return rightLocal.moveLast(last);
  }
  
  private static boolean validatePositions(TokenPosition left, TokenPosition right) {
    if (left.first < right.first) {
      return left.line < right.line || left.line == right.line && left.col < right.col;
    }
    if (left.first > right.first) {
      return left.line > right.line || left.line == right.line && left.col > right.col;
    }
    return left.line == right.line && left.col == right.col;
  }
  
  final int line;
  final int col;

  private final int first;

  
  private final int last;

  
  TokenPosition(int line, int col, int first, int last) {
    this.line = line;
    this.col = col;
    this.first = first;
    this.last = last;
  }

  /**
   * Clone.
   *
   * @return the token position
   */
  public TokenPosition clone() {
    return new TokenPosition(line, col, first, last);
  }

  /**
   * Gets the length.
   *
   * @return the length
   */
  public int getLength() {
    return last - first + 1;
  }

  /**
   * Gets the pos.
   *
   * @return the pos
   */
  public TokenPosition getPos() {
    return this;
  }

  private TokenPosition moveLast(int last) {
    return new TokenPosition(line, col, first, last);
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return line + ":" + col + " [" + first + "-" + last + "]";
  }

}
