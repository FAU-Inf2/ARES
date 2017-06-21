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

package de.fau.cs.inf2.cas.common.util.string;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public final class NGramCalculator implements IStringSimilarityMeasure {
  private final int ngram;
  private final int subMapSize;

  /**
   * Instantiates a new n gram calculator.
   *
   * @param ngram the n
   */
  public NGramCalculator(final int ngram) {
    this.ngram = ngram;
    cache = new ConcurrentHashMap<>(10000);
    this.subMapSize = 3500200;
  }

  /**
   * Instantiates a new n gram calculator.
   *
   * @param npar the n
   * @param cacheSize the cache size
   * @param subMapSize the sub map size
   */
  public NGramCalculator(final int npar, int cacheSize, int subMapSize) {
    this.ngram = npar;
    cache = new ConcurrentHashMap<>(cacheSize);
    this.subMapSize = subMapSize;
  }

  private ConcurrentHashMap<String, ConcurrentHashMap<String, Float>> cache;

  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(final String first, final String second) {
    if (first.equals(second)) {
      return 1.0f;
    } else if (first.equals("") || second.equals("")) {
      return 0.0f;
    } else {
      ConcurrentHashMap<String, Float> map2 = cache.get(first);
      if (map2 != null) {
        Float result = map2.get(second);
        if (result != null) {
          return result;
        }
      } else {
        map2 = new ConcurrentHashMap<>();
        cache.put(first, map2);
      }

      final int m = Math.min(ngram, Math.min(first.length(), second.length()));
      final HashSet<String> firstNGrams = calculateNGrams(first, m);
      final HashSet<String> secondNGrams = calculateNGrams(second, m);
      final int firstSize = firstNGrams.size();
      final int secondSize = secondNGrams.size();
      firstNGrams.retainAll(secondNGrams);

      float result = 2.0f * firstNGrams.size() / (firstSize + secondSize);
      if (map2.size() < subMapSize) {
        map2.put(second, result);
      }

      return result;
    }
  }

  private HashSet<String> calculateNGrams(final String string, final int npar) {
    final HashSet<String> result = new HashSet<String>();

    for (int i = 0; i < string.length() - npar + 1; ++i) {
      result.add(string.substring(i, i + npar));
    }

    return result;
  }

  /**
   * Clear.
   */
  public void clear() {
    for (Entry<String, ConcurrentHashMap<String, Float>> e : cache.entrySet()) {
      if (e.getValue() != null) {
        e.getValue().clear();
      }
    }
    cache.clear();
  }
}
