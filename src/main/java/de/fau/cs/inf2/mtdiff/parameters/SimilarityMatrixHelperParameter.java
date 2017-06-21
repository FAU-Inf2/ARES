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

package de.fau.cs.inf2.mtdiff.parameters;

import de.fau.cs.inf2.cas.common.bast.diff.LabelConfiguration;
import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.ComparePair;
import de.fau.cs.inf2.cas.common.util.string.NGramCalculator;

import de.fau.cs.inf2.mtdiff.TreeMatcherConfiguration;
import de.fau.cs.inf2.mtdiff.intern.LMatcher;
import de.fau.cs.inf2.mtdiff.intern.MatchingCandidate;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("PMD.TooManyFields")
public class SimilarityMatrixHelperParameter {
  public boolean[][] aggregationFinisheds;
  public INode[] firstAggregations;
  public INode[] secondAggregations;
  public ConcurrentHashMap<INode, MatchingCandidate<INode>> currentResultMap;
  public AtomicBoolean changed;
  public ArrayList<INode> oldNodes;
  public ArrayList<INode> newNodes;
  public IdentityHashMap<INode, ComparePair<INode>> resultMap;
  public NGramCalculator stringSim;
  public ConcurrentHashMap<String, Float> stringSimCache;
  public boolean onlyOneClassPair;
  public double[][] similarityScores;
  public ConcurrentSkipListSet<MatchingCandidate<INode>> initialList;
  public ConcurrentHashMap<INode, ConcurrentHashMap<INode, MatchingCandidate<INode>>> candidateMap;
  public AtomicIntegerArray foundMaxArray;
  public ConcurrentHashMap<INode, ConcurrentHashMap<INode, Float>> similarityCache;
  public AtomicLong similarityEntries;
  public Map<INode, INode> parents1;
  public Map<INode, INode> parents2;
  public Map<INode, ArrayList<INode>> leavesMap1;
  public Map<INode, ArrayList<INode>> leavesMap2;
  public LabelConfiguration labelConfiguration;
  public LMatcher leafMatcher;
  public Map<INode, ArrayList<INode>> directChildrenMap1;
  public Map<INode, ArrayList<INode>> directChildrenMap2;
  public INode root1;
  public INode root2;
  public double weightSimilarity;
  public double weightPosition;
  public IdentityHashMap<INode, String> stringMap;
  public boolean verbose;
  public int numThreads;
  public TreeMatcherConfiguration configuration;

  /**
   * Instantiates a new similarity matrix helper parameter.
   *
   * @param aggregationFinisheds the aggregation finisheds
   * @param firstAggregations the first aggregations
   * @param secondAggregations the second aggregations
   * @param currentResultMap the current result map
   * @param changed the changed
   * @param oldNodes the old nodes
   * @param newNodes the new nodes
   * @param resultMap the result map
   * @param stringSim the string sim
   * @param stringSimCache the string sim cache
   * @param onlyOneClassPair the only one class pair
   * @param similarityScores the similarity scores
   * @param initialList the initial list
   * @param candidateMap the candidate map
   * @param foundMaxArray the found max array
   * @param similarityCache the similarity cache
   * @param similarityEntries the similarity entries
   * @param parents1 the parents 1
   * @param parents2 the parents 2
   * @param leavesMap1 the leaves map 1
   * @param leavesMap2 the leaves map 2
   * @param labelConfiguration the label configuration
   * @param leafMatcher the leaf matcher
   * @param directChildrenMap1 the direct children map 1
   * @param directChildrenMap2 the direct children map 2
   * @param root1 the root 1
   * @param root2 the root 2
   * @param weightSimilarity the weight similarity
   * @param weightPosition the weight position
   * @param stringMap the string map
   * @param verbose the verbose
   * @param numThreads the num threads
   * @param configuration the configuration
   */
  @SuppressWarnings("PMD.ExcessiveParameterList")
  public SimilarityMatrixHelperParameter(boolean[][] aggregationFinisheds,
      INode[] firstAggregations, INode[] secondAggregations,
      ConcurrentHashMap<INode, MatchingCandidate<INode>> currentResultMap, AtomicBoolean changed,
      ArrayList<INode> oldNodes, ArrayList<INode> newNodes,
      IdentityHashMap<INode, ComparePair<INode>> resultMap, NGramCalculator stringSim,
      ConcurrentHashMap<String, Float> stringSimCache, boolean onlyOneClassPair,
      double[][] similarityScores, ConcurrentSkipListSet<MatchingCandidate<INode>> initialList,
      ConcurrentHashMap<INode, ConcurrentHashMap<INode, MatchingCandidate<INode>>> candidateMap,
      AtomicIntegerArray foundMaxArray,
      ConcurrentHashMap<INode, ConcurrentHashMap<INode, Float>> similarityCache,
      AtomicLong similarityEntries, Map<INode, INode> parents1, Map<INode, INode> parents2,
      Map<INode, ArrayList<INode>> leavesMap1, Map<INode, ArrayList<INode>> leavesMap2,
      LabelConfiguration labelConfiguration, LMatcher leafMatcher,
      Map<INode, ArrayList<INode>> directChildrenMap1,
      Map<INode, ArrayList<INode>> directChildrenMap2, INode root1, INode root2,
      double weightSimilarity, double weightPosition, IdentityHashMap<INode, String> stringMap,
      boolean verbose, int numThreads, TreeMatcherConfiguration configuration) {
    this.aggregationFinisheds = aggregationFinisheds;
    this.firstAggregations = firstAggregations;
    this.secondAggregations = secondAggregations;
    this.currentResultMap = currentResultMap;
    this.changed = changed;
    this.oldNodes = oldNodes;
    this.newNodes = newNodes;
    this.resultMap = resultMap;
    this.stringSim = stringSim;
    this.stringSimCache = stringSimCache;
    this.onlyOneClassPair = onlyOneClassPair;
    this.similarityScores = similarityScores;
    this.initialList = initialList;
    this.candidateMap = candidateMap;
    this.foundMaxArray = foundMaxArray;
    this.similarityCache = similarityCache;
    this.similarityEntries = similarityEntries;
    this.parents1 = parents1;
    this.parents2 = parents2;
    this.leavesMap1 = leavesMap1;
    this.leavesMap2 = leavesMap2;
    this.labelConfiguration = labelConfiguration;
    this.leafMatcher = leafMatcher;
    this.directChildrenMap1 = directChildrenMap1;
    this.directChildrenMap2 = directChildrenMap2;
    this.root1 = root1;
    this.root2 = root2;
    this.weightSimilarity = weightSimilarity;
    this.weightPosition = weightPosition;
    this.stringMap = stringMap;
    this.verbose = verbose;
    this.numThreads = numThreads;
    this.configuration = configuration;
  }
}