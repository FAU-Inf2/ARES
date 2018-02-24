package de.fau.cs.inf2.cas.ares.io;

import de.fau.cs.inf2.cthree.data.Cluster;

import java.util.List;

public class AresOnCthreeResult {
  public Cluster cluster;
  public RecommendationResult recommendationResult;
  public ClusterCloneType cloneType;
  public List<Double> unchangedAccuracyTokens;
  public List<Double> unchangedAccuracyCharacters;

  /**
   * Instantiates a new ares on cthree result.
   *
   */
  public AresOnCthreeResult(Cluster cluster,
      RecommendationResult recommendationResult, ClusterCloneType cloneType,
      List<Double> unchangedAccuracyTokens, List<Double> unchangedAccuracyCharacters) {
    super();
    this.cluster = cluster;
    this.recommendationResult = recommendationResult;
    this.cloneType = cloneType;
    this.unchangedAccuracyTokens = unchangedAccuracyTokens;
    this.unchangedAccuracyCharacters = unchangedAccuracyCharacters;
  }
}
