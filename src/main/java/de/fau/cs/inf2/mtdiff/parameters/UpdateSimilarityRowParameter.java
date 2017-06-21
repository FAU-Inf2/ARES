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
      AtomicLong similarityEntries, IdentityHashMap<INode, String> stringMap, boolean verbose) {
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
  }
}