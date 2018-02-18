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

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.ComparePair;
import de.fau.cs.inf2.cas.common.util.string.NGramCalculator;

import de.fau.cs.inf2.mtdiff.intern.MatchingCandidate;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("PMD.TooManyFields")
public class UpdateSimilarityRowParameter {
  public boolean[][] aggregationFinisheds;
  public double[][] similarityScores;
  public INode[] firstAggregations;
  public INode[] secondAggregations;
  public ConcurrentHashMap<INode, MatchingCandidate<INode>> currentResultMap;
  public AtomicBoolean changed;
  public int oldNodeIndex;
  public ArrayList<INode> newNodes;
  public boolean onlyOneClassPair;
  public IdentityHashMap<INode, ComparePair<INode>> resultMap;
  public NGramCalculator stringSim;
  public ConcurrentHashMap<String, Float> stringSimCache;
  public ConcurrentHashMap<INode, ConcurrentHashMap<INode, Float>> similarityCache;
  public AtomicLong similarityEntries;
  public IdentityHashMap<INode, String> stringMap;
  public boolean verbose;
  public ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Float>> hashbasedCache;
  public IdentityHashMap<INode, Integer> quickFindHashMap;

  /**
   * Instantiates a new update similarity row parameter.
   *
   * @param aggregationFinisheds the aggregation finisheds
   * @param similarityScores the similarity scores
   * @param firstAggregations the first aggregations
   * @param secondAggregations the second aggregations
   * @param currentResultMap the current result map
   * @param changed the changed
   * @param oldNodeIndex the old node index
   * @param newNodes the new nodes
   * @param onlyOneClassPair the only one class pair
   * @param resultMap the result map
   * @param stringSim the string sim
   * @param stringSimCache the string sim cache
   * @param similarityCache the similarity cache
   * @param similarityEntries the similarity entries
   * @param stringMap the string map
   * @param verbose the verbose
   */
  @SuppressWarnings("PMD.ExcessiveParameterList")
  public UpdateSimilarityRowParameter(boolean[][] aggregationFinisheds, double[][] similarityScores,
      INode[] firstAggregations, INode[] secondAggregations,
      ConcurrentHashMap<INode, MatchingCandidate<INode>> currentResultMap, AtomicBoolean changed,
      int oldNodeIndex, ArrayList<INode> newNodes, boolean onlyOneClassPair,
      IdentityHashMap<INode, ComparePair<INode>> resultMap, NGramCalculator stringSim,
      ConcurrentHashMap<String, Float> stringSimCache,
      ConcurrentHashMap<INode, ConcurrentHashMap<INode, Float>> similarityCache,
      AtomicLong similarityEntries, IdentityHashMap<INode, String> stringMap, boolean verbose,
      ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Float>> hashbasedCache,
      IdentityHashMap<INode, Integer> quickFindHashMap) {
    this.aggregationFinisheds = aggregationFinisheds;
    this.similarityScores = similarityScores;
    this.firstAggregations = firstAggregations;
    this.secondAggregations = secondAggregations;
    this.currentResultMap = currentResultMap;
    this.changed = changed;
    this.oldNodeIndex = oldNodeIndex;
    this.newNodes = newNodes;
    this.onlyOneClassPair = onlyOneClassPair;
    this.resultMap = resultMap;
    this.stringSim = stringSim;
    this.stringSimCache = stringSimCache;
    this.similarityCache = similarityCache;
    this.similarityEntries = similarityEntries;
    this.stringMap = stringMap;
    this.verbose = verbose;
    this.hashbasedCache = hashbasedCache;
    this.quickFindHashMap = quickFindHashMap;
  }
}