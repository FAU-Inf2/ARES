package de.fau.cs.inf2.cas.common.util.num;

import java.math.BigInteger;

public interface IIntegerSimilarityMeasure {
  
  /**
   * Similarity.
   *
   * @param i1 the i1
   * @param i2 the i2
   * @return the float
   */
  public float similarity(BigInteger i1, BigInteger i2);
}
