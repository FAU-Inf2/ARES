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

