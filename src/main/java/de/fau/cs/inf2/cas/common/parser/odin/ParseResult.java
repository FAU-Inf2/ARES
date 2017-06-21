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

package de.fau.cs.inf2.cas.common.parser.odin;

import java.util.List;

public class ParseResult<T> {
  public final T value;
  public final TokenAndHistory currentTokenAndHistory;
  public final List<TokenAndHistory> additionalTokens;

  /**
   * Instantiates a new parses the result.
   *
   * @param value the value
   * @param currentTokenAndHistory the current token and history
   */
  public ParseResult(T value, TokenAndHistory currentTokenAndHistory) {
    this.value = value;
    this.currentTokenAndHistory = currentTokenAndHistory;
    this.additionalTokens = null;
  }

  ParseResult(T value, TokenAndHistory currentTokenAndHistory,
      List<TokenAndHistory> additionalTokens) {
    this.value = value;
    this.currentTokenAndHistory = currentTokenAndHistory;
    this.additionalTokens = additionalTokens;
  }
}
