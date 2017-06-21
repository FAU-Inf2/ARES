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

package de.fau.cs.inf2.cas.common.bast.general;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;

public class BastInfo {

  public TokenAndHistory[] tokens;
  public LinkedList<TokenAndHistory> tokensBefore = new LinkedList<>();
  public LinkedList<TokenAndHistory> tokensAfter = new LinkedList<>();

  /**
   * Instantiates a new bast info.
   *
   * @param tokens the tokens
   */
  public BastInfo(TokenAndHistory[] tokens) {
    this.tokens = tokens;
  }

  /**
   * Gets the token clone.
   *
   * @return the token clone
   */
  public TokenAndHistory[] getTokenClone() {
    if (tokens == null) {
      return null;
    }
    TokenAndHistory[] clonedTokens = new TokenAndHistory[tokens.length];
    for (int i = 0; i < tokens.length; i++) {
      if (tokens[i] != null) {
        clonedTokens[i] = tokens[i].clone();
      } else {
        clonedTokens[i] = null;
      }
    }
    return clonedTokens;
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    String tmp = "";
    if (tokens != null) {
      for (int i = 0; i < tokens.length; i++) {
        if (tokens[i] != null) {
          tmp += tokens[i].toString();
        }
      }
    }
    return tmp;
  }
}
