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

package de.fau.cs.inf2.cas.common.io.extern;

public class LaseOutput {
  public String classname;
  public String relativePath;
  public String methodSignature;
  public int startPosition;
  public int length;
  public String newMethodString;
  /**
   * Instantiates a new lase output.
   *
   * @param classname the classname
   * @param relativePath the relativePath
   * @param methodSignature the method signature
   * @param startPosition the start position
   * @param length the length
   * @param newMethodString the new method string
   */
  
  public LaseOutput(String classname, String relativePath, String methodSignature,
      int startPosition, int length, String newMethodString) {
    this.classname = classname;
    this.relativePath = relativePath;
    this.methodSignature = methodSignature;
    this.startPosition = startPosition;
    this.length = length;
    this.newMethodString = newMethodString;
  }

}
