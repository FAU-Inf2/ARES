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
