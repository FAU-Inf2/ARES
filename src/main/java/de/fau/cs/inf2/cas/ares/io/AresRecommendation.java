package de.fau.cs.inf2.cas.ares.io;

public class AresRecommendation {

  public String methodBlock;
  public double accuracyTokens;
  public double accuracyCharacters;

  /**
   * Instantiates a new ares recommendation.
   *
   * @param methodBlock the method block
   * @param accuracyTokens the accuracy tokens
   * @param accuracyCharacters the accuracy characters
   */
  public AresRecommendation(String methodBlock,
      double accuracyTokens, double accuracyCharacters) {
    super();
    this.methodBlock = methodBlock;
    this.accuracyTokens = accuracyTokens;
    this.accuracyCharacters = accuracyCharacters;
  }
  
}
