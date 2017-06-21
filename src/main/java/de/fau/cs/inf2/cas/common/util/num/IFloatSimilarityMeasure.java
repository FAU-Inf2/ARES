package de.fau.cs.inf2.cas.common.util.num;

public interface IFloatSimilarityMeasure {
  
  /**
   * Similarity.
   *
   * @param f1 the f1
   * @param f2 the f2
   * @return the float
   */
  public float similarity(double f1, double f2);
}
