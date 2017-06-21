package de.fau.cs.inf2.cas.common.util.num;

import java.math.BigInteger;

public final class GaussianIntSimilarityMeasure implements IIntegerSimilarityMeasure {

  private final float sigma;

  /**
   * Instantiates a new gaussian int similarity measure.
   *
   * @param sigma the sigma
   */
  public GaussianIntSimilarityMeasure(final float sigma) {
    this.sigma = sigma;
  }

  /**
   * Similarity.
   *
   * @param i1 the i1
   * @param i2 the i2
   * @return the float
   */
  public float similarity(final long i1, final long i2) {
    return similarity(BigInteger.valueOf(i1), BigInteger.valueOf(i2));
  }

  
  /**
   * Similarity.
   *
   * @param i1 the i1
   * @param i2 the i2
   * @return the float
   */
  public float similarity(final BigInteger i1, final BigInteger i2) {
    final double x = i1.subtract(i2).doubleValue() / sigma;

    return (float) Math.exp(-0.5 * x * x);
  }
}
