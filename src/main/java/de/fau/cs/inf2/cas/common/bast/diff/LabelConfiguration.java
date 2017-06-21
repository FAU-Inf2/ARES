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

package de.fau.cs.inf2.cas.common.bast.diff;

import java.util.HashSet;

public class LabelConfiguration {
  public final int identifierLabel;
  public final int rootLabel;
  public final int classLabel;
  public final int basicTypeLabel;
  public final int qualifierLabel;

  public HashSet<Integer> labelsForValueCompare;
  public HashSet<Integer> labelsForRealCompare;
  public HashSet<Integer> labelsForIntCompare;
  public HashSet<Integer> labelsForStringCompare;
  public HashSet<Integer> labelsForBoolCompare;

  /**
   * Instantiates a new label configuration.
   *
   * @param labelsForValueCompare the labels for value compare
   */
  public LabelConfiguration(HashSet<Integer> labelsForValueCompare) {
    this(-1, -1, -1, -1, -1, labelsForValueCompare, new HashSet<Integer>(), new HashSet<Integer>(),
        new HashSet<Integer>(), new HashSet<Integer>());

  }

  /**
   * Instantiates a new label configuration.
   *
   * @param identifierLabel the identifier label
   * @param rootLabel the root label
   * @param classLabel the class label
   * @param basicTypeLabel the basic type label
   * @param qualifierLabel the qualifier label
   * @param labelsForValueCompare the labels for value compare
   * @param labelsForRealCompare the labels for real compare
   * @param labelsForIntCompare the labels for int compare
   * @param labelsForStringCompare the labels for string compare
   * @param labelsForBoolCompare the labels for bool compare
   */
  public LabelConfiguration(int identifierLabel, int rootLabel, int classLabel, int basicTypeLabel,
      int qualifierLabel, HashSet<Integer> labelsForValueCompare,
      HashSet<Integer> labelsForRealCompare, HashSet<Integer> labelsForIntCompare,
      HashSet<Integer> labelsForStringCompare, HashSet<Integer> labelsForBoolCompare) {
    this.identifierLabel = identifierLabel;
    this.rootLabel = rootLabel;
    this.classLabel = classLabel;
    this.labelsForValueCompare = labelsForValueCompare;
    this.labelsForBoolCompare = labelsForBoolCompare;
    this.labelsForRealCompare = labelsForRealCompare;
    this.labelsForIntCompare = labelsForIntCompare;
    this.labelsForStringCompare = labelsForStringCompare;
    this.labelsForBoolCompare = labelsForBoolCompare;
    this.basicTypeLabel = basicTypeLabel;
    this.qualifierLabel = qualifierLabel;
  }
}
