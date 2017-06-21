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
