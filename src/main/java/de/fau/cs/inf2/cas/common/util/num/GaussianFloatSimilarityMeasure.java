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

package de.fau.cs.inf2.cas.common.util.num;

public final class GaussianFloatSimilarityMeasure implements IFloatSimilarityMeasure {

  private final float sigma;

  /**
   * Instantiates a new gaussian float similarity measure.
   *
   * @param sigma the sigma
   */
  public GaussianFloatSimilarityMeasure(final float sigma) {
    this.sigma = sigma;
  }

  /**
   * Similarity.
   *
   * @param f1 the f1
   * @param f2 the f2
   * @return the float
   */
  public float similarity(final float f1, final float f2) {
    final float x = (f1 - f2) / sigma;

    return (float) Math.exp(-0.5 * x * x);
  }

  
  /**
   * Similarity.
   *
   * @param f1 the f1
   * @param f2 the f2
   * @return the float
   */
  public float similarity(final double f1, final double f2) {
    final double x = (f1 - f2) / sigma;

    return (float) Math.exp(-0.5 * x * x);
  }
}