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

package de.fau.cs.inf2.mtdiff.intern;

import de.fau.cs.inf2.cas.common.bast.diff.LabelConfiguration;
import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.num.GaussianFloatSimilarityMeasure;
import de.fau.cs.inf2.cas.common.util.num.GaussianIntSimilarityMeasure;
import de.fau.cs.inf2.cas.common.util.num.IFloatSimilarityMeasure;
import de.fau.cs.inf2.cas.common.util.num.IIntegerSimilarityMeasure;
import de.fau.cs.inf2.cas.common.util.string.IStringSimilarityMeasure;
import de.fau.cs.inf2.cas.common.util.string.NGramCalculator;

import java.math.BigInteger;

public class LMatcher {

  private IStringSimilarityMeasure stringSim = new NGramCalculator(2, 10, 10);
  private IIntegerSimilarityMeasure intSim = new GaussianIntSimilarityMeasure(1);
  private IFloatSimilarityMeasure floatSim = new GaussianFloatSimilarityMeasure(1);

  private LabelConfiguration labelConfiguration;

  private final double threshold;

  /**
   * Instantiates a new l matcher.
   *
   * @param labelConfiguration the label configuration
   * @param threshold the threshold
   */
  public LMatcher(LabelConfiguration labelConfiguration, double threshold) {
    this.labelConfiguration = labelConfiguration;
    this.threshold = threshold;
  }

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(INode first, INode second, float similarity) {
    return similarity >= threshold;
  }

  /**
   * Leaves similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float leavesSimilarity(INode first, INode second) {
    if (first.getTypeWrapped() != second.getTypeWrapped()) {
      return 0.0f;
    }
    if (first.getChildrenWrapped().size() != 0) {
      return 0.0f;
    }
    if (second.getChildrenWrapped().size() != 0) {
      return 0.0f;
    }
    if (labelConfiguration.labelsForValueCompare.contains(first.getTypeWrapped())) {
      if (first.getLabel().equals(second.getLabel())) {
        return 1.0f;
      }
      return 0.0f;
    } else if (labelConfiguration.labelsForRealCompare.contains(first.getTypeWrapped())) {
      Double firstDoubleValue = new Double(first.getLabel());
      Double secondDoubleValue = new Double(second.getLabel());
      return floatSim.similarity(firstDoubleValue, secondDoubleValue);
    } else if (labelConfiguration.labelsForIntCompare.contains(first.getTypeWrapped())) {
      try {
        BigInteger firstValue = new BigInteger(first.getLabel());
        BigInteger secondValue = new BigInteger(second.getLabel());
        if (firstValue.compareTo(secondValue) == 0) {
          return 1.0f;
        }
        return intSim.similarity(firstValue, secondValue);
      } catch (NumberFormatException e) {
        try {
          Double firstDoubleValue = new Double(first.getLabel());
          Double secondDoubleValue = new Double(second.getLabel());
          return floatSim.similarity(firstDoubleValue, secondDoubleValue);
        } catch (NullPointerException e2) {
          return 0.0f;
        }
      }
    } else if (labelConfiguration.labelsForStringCompare.contains(first.getTypeWrapped())) {
      return stringSim.similarity(first.getLabel(), second.getLabel());
    } else if (labelConfiguration.labelsForBoolCompare.contains(first.getTypeWrapped())) {
      if (first.getLabel().equals(second.getLabel())) {
        return 1.0f;
      }

      return 0.5f;
    } else {
      return 1.0f;
    }
  }
}
