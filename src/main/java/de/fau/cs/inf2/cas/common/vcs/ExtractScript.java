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

package de.fau.cs.inf2.cas.common.vcs;

import de.fau.cs.inf2.cas.common.bast.nodes.BastParameterList;

public class ExtractScript {

  public final String repUri;
  public final String commitOriginal;
  public final String commitModified;
  public final String pathOriginal;
  public final String pathModified;
  public final String methodNameOriginal;
  public final String methodNameModified;
  public final String innerClassOriginal;
  public final String innerClassModified;
  final BastParameterList parametersOriginal;
  final BastParameterList parametersModified;
  final String rawParametersOriginal;
  final String rawParametersModified;
  final boolean inGroup;

  /**
   * Instantiates a new extract script.
   *
   */
  @SuppressWarnings("PMD.ExcessiveParameterList")
  public ExtractScript(String repUri, String commitOriginal, String commitModified,
      String pathOriginal, String pathModified, String innerClassOriginal,
      String innerClassModified, String methodNameOriginal, String methodNameModified,
      BastParameterList parametersOriginal, BastParameterList parametersModified, boolean inGroup) {
    this(repUri, commitOriginal, commitModified, pathOriginal, pathModified, innerClassOriginal,
        innerClassModified, methodNameOriginal, methodNameModified, parametersOriginal,
        parametersModified, inGroup, null, null);
  }

  @SuppressWarnings("PMD.ExcessiveParameterList")
  ExtractScript(String repUri, String commitOriginal, String commitModified, String pathOriginal,
      String pathModified, String innerClassOriginal, String innerClassModified,
      String methodNameOriginal, String methodNameModified, BastParameterList parametersOriginal,
      BastParameterList parametersModified, boolean inGroup, String rawParametersOriginal,
      String rawParametersModified) {
    this.repUri = repUri;
    this.commitOriginal = commitOriginal;
    this.commitModified = commitModified;
    this.pathOriginal = pathOriginal;
    this.pathModified = pathModified;
    this.methodNameOriginal = methodNameOriginal;
    this.methodNameModified = methodNameModified;
    this.parametersOriginal = parametersOriginal;
    this.parametersModified = parametersModified;
    this.inGroup = inGroup;
    this.innerClassOriginal = innerClassOriginal;
    this.innerClassModified = innerClassModified;
    this.rawParametersOriginal = rawParametersOriginal;
    this.rawParametersModified = rawParametersModified;
  }

}
