package de.fau.cs.inf2.cas.ares.io;

import de.fau.cs.inf2.cas.common.io.ReadableEncodedScript;

import java.util.ArrayList;
import java.util.List;

public class RecommendationResult {
  public String name;
  public List<ReadableEncodedScript> allMembers;
  public List<ReadableEncodedScript> inputs;
  public List<AresRecommendationSet> recommendationSets;
  public int foundMembers;
  public int numberOfRecommendationSets;
  public double precision;
  public double recall;
  public double accuracyTokensMin;
  public double accuracyCharactersMin;
  public double accuracyTokensMax;
  public double accuracyCharactersMax;
  public String patternOriginal;
  public String patternModified;
  public long patternCreationTimeInNanoSec;
  public long patternUseTimeInNanoSec;
  
  /**
   * Instantiates a new ares recommendation result.
   *
   * @param name the name
   * @param allMembers the all members
   * @param inputs the inputs
   * @param recommendationSets the recommendation sets
   * @param foundMembers the found members
   * @param numberOfRecommendationSets the number of recommendation sets
   * @param precision the precision
   * @param recall the recall
   * @param accuracyTokensMin the accuracy tokens
   * @param accuracyCharactersMin the accuracy characters
   * @param accuracyTokensMax the accuracy tokens
   * @param accuracyCharactersMax the accuracy characters
   */
  public RecommendationResult(String name, 
      List<ReadableEncodedScript> allMembers,
      List<ReadableEncodedScript> inputs, List<AresRecommendationSet> recommendationSets,
      int foundMembers, int numberOfRecommendationSets, double precision, double recall,
      double accuracyTokensMin, double accuracyCharactersMin, 
      double accuracyTokensMax, double accuracyCharactersMax, 
      String patternOriginal,
      String patternModified,
      long patternCreationTimeInNanoSec,
      long patternUseTimeInNanoSec) {
    super();
    this.name = name;
    this.allMembers = allMembers;
    this.inputs = inputs;
    this.recommendationSets = recommendationSets;
    this.foundMembers = foundMembers;
    this.numberOfRecommendationSets = numberOfRecommendationSets;
    this.precision = precision;
    this.recall = recall;
    this.accuracyTokensMin = accuracyTokensMin;
    this.accuracyCharactersMin = accuracyCharactersMin;
    this.accuracyTokensMax = accuracyTokensMax;
    this.accuracyCharactersMax = accuracyCharactersMax;
    this.patternOriginal = patternOriginal;
    this.patternModified = patternModified;
    this.patternCreationTimeInNanoSec = patternCreationTimeInNanoSec;
    this.patternUseTimeInNanoSec = patternUseTimeInNanoSec;
  }
}

