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
