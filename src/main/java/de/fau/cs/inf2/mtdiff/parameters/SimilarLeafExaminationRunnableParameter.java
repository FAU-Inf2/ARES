package de.fau.cs.inf2.mtdiff.parameters;

import de.fau.cs.inf2.cas.common.bast.diff.LabelConfiguration;
import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.ComparePair;

import de.fau.cs.inf2.mtdiff.TreeMatcherConfiguration;
import de.fau.cs.inf2.mtdiff.intern.LMatcher;
import de.fau.cs.inf2.mtdiff.intern.MatchingCandidate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("PMD.TooManyFields")
public class SimilarLeafExaminationRunnableParameter {
  public ArrayList<INode> oldNodes;
  public ArrayList<INode> newNodes;
  public HashSet<MatchingCandidate<INode>> initialListOld;
  public AtomicInteger count;
  public ConcurrentHashMap<String, Float> stringSimCache;
  public boolean onlyOneClassPair;
  public IdentityHashMap<INode, Integer> orderedListOld;
  public IdentityHashMap<INode, Integer> orderedListNew;
  public IdentityHashMap<INode, ComparePair<INode>> resultMap;
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
   * Instantiates a new similar leaf examination runnable parameter.
   *
   * @param oldNodes the old nodes
   * @param newNodes the new nodes
   * @param initialListOld the initial list old
   * @param count the count
   * @param stringSimCache the string sim cache
   * @param onlyOneClassPair the only one class pair
   * @param orderedListOld the ordered list old
   * @param orderedListNew the ordered list new
   * @param resultMap the result map
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
  public SimilarLeafExaminationRunnableParameter(ArrayList<INode> oldNodes,
      ArrayList<INode> newNodes, HashSet<MatchingCandidate<INode>> initialListOld,
      AtomicInteger count, ConcurrentHashMap<String, Float> stringSimCache,
      boolean onlyOneClassPair, IdentityHashMap<INode, Integer> orderedListOld,
      IdentityHashMap<INode, Integer> orderedListNew,
      IdentityHashMap<INode, ComparePair<INode>> resultMap,
      ConcurrentHashMap<INode, ConcurrentHashMap<INode, Float>> similarityCache,
      AtomicLong similarityEntries, Map<INode, INode> parents1, Map<INode, INode> parents2,
      Map<INode, ArrayList<INode>> leavesMap1, Map<INode, ArrayList<INode>> leavesMap2,
      LabelConfiguration labelConfiguration, LMatcher leafMatcher,
      Map<INode, ArrayList<INode>> directChildrenMap1,
      Map<INode, ArrayList<INode>> directChildrenMap2, INode root1, INode root2,
      double weightSimilarity, double weightPosition, IdentityHashMap<INode, String> stringMap,
      boolean verbose, int numThreads, TreeMatcherConfiguration configuration) {
    this.oldNodes = oldNodes;
    this.newNodes = newNodes;
    this.initialListOld = initialListOld;
    this.count = count;
    this.stringSimCache = stringSimCache;
    this.onlyOneClassPair = onlyOneClassPair;
    this.orderedListOld = orderedListOld;
    this.orderedListNew = orderedListNew;
    this.resultMap = resultMap;
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