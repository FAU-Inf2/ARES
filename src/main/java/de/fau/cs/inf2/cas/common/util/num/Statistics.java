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

import java.util.List;

/**
 * The Class Statistics.
 */
public class Statistics {
  public static final Double NANO = 1000000000d;

  /**
   * Creates the upper quartile.
   *
   * @param values the values
   * @return the double
   */
  public static Double getUpperQuartile(List<Long> values) {
    Double upperQuartile;
    if (values.size() % 2 == 0) {
      upperQuartile = ((double) values.size() / 2) % 2 == 0
          ? ((double) values.get(values.size() / 2 / 2 + values.size() / 2)
              + values.get(values.size() / 2 / 2 + 1 + values.size() / 2)) / 2
          : (double) values.get(values.size() / 2 / 2 + 1 + values.size() / 2);
    } else {
      upperQuartile = ((double) (values.size() - 1) / 2) % 2 == 0
          ? ((double) values.get((values.size() - 1) / 2 / 2 + values.size() / 2 + 1)
              + values.get((values.size() - 1) / 2 / 2 + 1 + values.size() / 2 + 1)) / 2
          : (double) values.get((values.size() - 1) / 2 / 2 + 1 + values.size() / 2 + 1);
    }
    return upperQuartile;
  }

  /**
   * Creates the lower quartile.
   *
   * @param values the values
   * @return the double
   */
  public static Double getLowerQuartile(List<Long> values) {
    Double lowerQuartile;
    if (values.size() % 2 == 0) {
      lowerQuartile = ((double) values.size() / 2) % 2 == 0
          ? ((double) values.get(values.size() / 2 / 2) + values.get(values.size() / 2 / 2 + 1)) / 2
          : (double) values.get(values.size() / 2 / 2 + 1);

    } else {
      lowerQuartile = ((double) (values.size() - 1) / 2) % 2 == 0
          ? ((double) values.get((values.size() - 1) / 2 / 2)
              + values.get((values.size() - 1) / 2 / 2 + 1)) / 2
          : (double) values.get((values.size() - 1) / 2 / 2 + 1);

    }
    return lowerQuartile;
  }

  /**
   * Creates the median.
   *
   * @param values the values
   * @return the double
   */
  public static Double getMedian(List<Long> values) {
    Double median = values.size() % 2 == 0
        ? ((double) values.get(values.size() / 2) + values.get(values.size() / 2 + 1)) / 2
        : (double) values.get(values.size() / 2 + 1);
    return median;
  }

  /**
   * Creates the mean.
   *
   * @param values the values
   * @return the double
   */
  public static Double getMean(List<Long> values) {
    long sum = 0;
    for (Long value : values) {
      sum += value;
    }
    final Double mean = (((double)sum) / values.size());
    return mean;
  }
}
