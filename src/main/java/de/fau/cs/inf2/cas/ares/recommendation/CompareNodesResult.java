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

package de.fau.cs.inf2.cas.ares.recommendation;

class CompareNodesResult {
  final int positionTemplate;
  static final int NEUTRAL = 0;
  static final int FAILED = 1;
  static final int SUCCESS = 2;
  static final int NO_WILDCARD = 3;
  static final int WILDCARD_ACCEPT = 4;
  static final int WILDCARD_FAIL = 5;
  static final int WILDCARD_UNFINISHED = 6;
  static final int WILDCARD_NO_MATCH = 7;

  CompareNodesResult(int positionTemplate, int positionProgram, int status, int wildcard) {
    this.positionTemplate = positionTemplate;
    this.positionProgram = positionProgram;
    this.status = status;
  }

  final int positionProgram;
  public final int status;

}
