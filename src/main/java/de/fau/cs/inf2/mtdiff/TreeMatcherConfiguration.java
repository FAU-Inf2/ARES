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

package de.fau.cs.inf2.mtdiff;

public class TreeMatcherConfiguration {
  final double leafThreshold;
  final double weightSimilarity;
  final double weightPosition;
  final double weightValue;
  final double weightChildren;
  final double innerNodeThreshold;
  public final double innerNodeNonEqualSimilarity;

  /**
   * Instantiates a new tree matcher configuration.
   *
   * @param leafThreshold the leaf threshold
   * @param weightSimilarity the weight similarity
   * @param weightPosition the weight position
   * @param weightValue the weight value
   * @param weightChildren the weight children
   * @param innerNodeThreshold the inner node threshold
   * @param innerNodeNonEqualSimilarity the inner node non equal similarity
   */
  public TreeMatcherConfiguration(double leafThreshold, double weightSimilarity,
      double weightPosition, double weightValue, double weightChildren, double innerNodeThreshold,
      double innerNodeNonEqualSimilarity) {
    this.leafThreshold = leafThreshold;
    this.weightSimilarity = weightSimilarity;
    this.weightPosition = weightPosition;
    this.weightValue = weightValue;
    this.weightChildren = weightChildren;
    this.innerNodeThreshold = innerNodeThreshold;
    this.innerNodeNonEqualSimilarity = innerNodeNonEqualSimilarity;
  }
  
}
