package de.fau.cs.inf2.mtdiff.intern;

import de.fau.cs.inf2.cas.common.bast.diff.LabelConfiguration;
import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.ComparePair;
import de.fau.cs.inf2.cas.common.util.string.NGramCalculator;

import de.fau.cs.inf2.mtdiff.TreeMatcherConfiguration;
import de.fau.cs.inf2.mtdiff.parameters.SimilarLeafExaminationRunnableParameter;
import de.fau.cs.inf2.mtdiff.parameters.SimilarityMatrixHelperParameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("PMD.TooManyFields")
public class SimilarLeafExaminationRunnable implements Callable<Set<MatchingCandidate<INode>>> {

  private ArrayList<INode> oldNodes;
  private ArrayList<INode> newNodes;
  private AtomicInteger count;
  private ConcurrentSkipListSet<MatchingCandidate<INode>> initialList;
  private HashSet<MatchingCandidate<INode>> initialListOld;

  private ConcurrentHashMap<INode, ConcurrentHashMap<INode,
      MatchingCandidate<INode>>> candidateMap =
      new ConcurrentHashMap<>();
  private ConcurrentHashMap<INode, ConcurrentHashMap<INode, Float>> similarityCache;
  private ConcurrentHashMap<String, Float> stringSimCache;
  private boolean onlyOneClassPair;
  private IdentityHashMap<INode, Integer> orderedListOld;
  private IdentityHashMap<INode, Integer> orderedListNew;
  private IdentityHashMap<INode, ComparePair<INode>> resultMap;
  private NGramCalculator stringSim = new NGramCalculator(2, 10, 10);
  private boolean verbose;
  private int identifierTag;
  private AtomicLong similarityEntries;
  private LMatcher leafMatcher;
  private LabelConfiguration labelConfiguration;
  private Map<INode, ArrayList<INode>> leavesMap1 = null;
  private Map<INode, ArrayList<INode>> leavesMap2 = null;
  private Map<INode, ArrayList<INode>> directChildrenMap1 = null;
  private Map<INode, ArrayList<INode>> directChildrenMap2 = null;
  private Map<INode, INode> parents1;
  private Map<INode, INode> parents2;
  private INode root1;
  private INode root2;
  private double weightSimilarity;
  private double weightPosition;
  private IdentityHashMap<INode, String> stringMap;
  private int numThreads;
  private TreeMatcherConfiguration configuration;

  /**
   * Instantiates a new similar leaf examination runnable.
   * @param parameterObject TODO
   */
  public SimilarLeafExaminationRunnable(SimilarLeafExaminationRunnableParameter parameterObject) {
    super();
    this.oldNodes = parameterObject.oldNodes;
    this.newNodes = parameterObject.newNodes;
    this.initialListOld = parameterObject.initialListOld;
    this.count = parameterObject.count;
    this.similarityCache = parameterObject.similarityCache;
    this.stringSimCache = parameterObject.stringSimCache;
    this.onlyOneClassPair = parameterObject.onlyOneClassPair;
    this.orderedListOld = parameterObject.orderedListOld;
    this.orderedListNew = parameterObject.orderedListNew;
    this.resultMap = parameterObject.resultMap;
    parameterObject.count.incrementAndGet();
    this.verbose = parameterObject.verbose;
    this.similarityEntries = parameterObject.similarityEntries;
    this.labelConfiguration = parameterObject.labelConfiguration;
    this.leafMatcher = parameterObject.leafMatcher;
    this.leavesMap1 = parameterObject.leavesMap1;
    this.leavesMap2 = parameterObject.leavesMap2;
    this.parents1 = parameterObject.parents1;
    this.parents2 = parameterObject.parents2;
    this.directChildrenMap1 = parameterObject.directChildrenMap1;
    this.directChildrenMap2 = parameterObject.directChildrenMap2;
    this.root1 = parameterObject.root1;
    this.root2 = parameterObject.root2;
    this.weightSimilarity = parameterObject.weightSimilarity;
    this.weightPosition = parameterObject.weightPosition;
    this.stringMap = parameterObject.stringMap;
    this.numThreads = parameterObject.numThreads;
    this.configuration = parameterObject.configuration;
  }


  /**
   * Call.
   *
   * @return the sets the
   * @throws Exception the exception
   */
  @Override
  public Set<MatchingCandidate<INode>> call() throws Exception {
    try {
      initialList =
          new ConcurrentSkipListSet<>(new PairComparator<INode>(orderedListOld, orderedListNew));
      initialList.addAll(initialListOld);
      BreadthFirstComparator<INode> compOld = new BreadthFirstComparator<INode>(orderedListOld);
      Collections.sort(oldNodes, compOld);
      compOld = null;
      BreadthFirstComparator<INode> compNew = new BreadthFirstComparator<INode>(orderedListNew);
      Collections.sort(newNodes, compNew);
      compNew = null;

      oldNodes.size();
      newNodes.size();
      final ConcurrentSkipListSet<MatchingCandidate<INode>> resultList =
          new ConcurrentSkipListSet<>(new PairComparator<INode>(orderedListOld, orderedListNew));
      final ConcurrentHashMap<INode, MatchingCandidate<INode>> currentResultMap =
          new ConcurrentHashMap<>();
      AtomicBoolean[] doneOld = new AtomicBoolean[oldNodes.size()];
      for (int i = 0; i < oldNodes.size(); i++) {
        doneOld[i] = new AtomicBoolean();
      }
      boolean[][] aggregationFinished = new boolean[oldNodes.size()][newNodes.size()];
      double[][] similarityScores = new double[oldNodes.size()][newNodes.size()];
      INode[] firstAggregations = new INode[oldNodes.size()];
      INode[] secondAggregations = new INode[newNodes.size()];
      prepareDataStructures(aggregationFinished, similarityScores, firstAggregations,
          secondAggregations);
      AtomicBoolean changed = new AtomicBoolean(true);
      AtomicIntegerArray foundMaxArray = new AtomicIntegerArray(oldNodes.size());
      new AtomicInteger();
      new SimilarityMatrixHelper(new SimilarityMatrixHelperParameter(aggregationFinished,
          firstAggregations, secondAggregations, currentResultMap, changed, oldNodes, newNodes,
          resultMap, stringSim, stringSimCache, onlyOneClassPair, similarityScores, initialList,
          candidateMap, foundMaxArray, similarityCache, similarityEntries, parents1, parents2,
          leavesMap1, leavesMap2, labelConfiguration, leafMatcher, directChildrenMap1,
          directChildrenMap2, root1, root2, weightSimilarity, weightPosition, stringMap, verbose,
          numThreads, configuration)).call();

      resultList.addAll(initialList);
      count.decrementAndGet();
      stringSim.clear();
      initialList.clear();
      for (Entry<INode, ConcurrentHashMap<INode, MatchingCandidate<INode>>> entry : candidateMap
          .entrySet()) {
        if (entry.getValue() != null) {
          entry.getValue().clear();
        }
      }
      candidateMap.clear();
      firstAggregations = null;
      secondAggregations = null;
      similarityScores = null;
      return resultList;
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }


  private void prepareDataStructures(boolean[][] aggregationFinished, double[][] similarityScores,
      INode[] firstAggregations, INode[] secondAggregations) {
    for (MatchingCandidate<INode> mc : initialList) {

      ConcurrentHashMap<INode, MatchingCandidate<INode>> tmp =
          candidateMap.get(mc.getOldElement());
      if (tmp == null) {
        tmp = new ConcurrentHashMap<>();
        ConcurrentHashMap<INode, MatchingCandidate<INode>> tmp2 =
            candidateMap.putIfAbsent(mc.getOldElement(), tmp);
        if (tmp2 != null) {
          tmp = tmp2;
        }

      }
      tmp.put(mc.getNewElement(), mc);
    }
    for (int i = 0; i < oldNodes.size(); i++) {
      firstAggregations[i] = oldNodes.get(i);
    }
    for (int j = 0; j < newNodes.size(); j++) {
      secondAggregations[j] = newNodes.get(j);
    }
    for (int i = 0; i < oldNodes.size(); i++) {
      INode oldNode = oldNodes.get(i);
      ConcurrentHashMap<INode, MatchingCandidate<INode>> tmp = candidateMap.get(oldNode);
      if (tmp != null) {
        for (int j = 0; j < newNodes.size(); j++) {
          INode newNode = newNodes.get(j);
          MatchingCandidate<INode> mc = tmp.get(newNode);
          if (mc == null) {
            aggregationFinished[i][j] = true;
            similarityScores[i][j] = Float.MIN_VALUE;
          } else {
            aggregationFinished[i][j] = false;
            similarityScores[i][j] = mc.getValue();
          }
        }
      } else {
        for (int j = 0; j < newNodes.size(); j++) {
          aggregationFinished[i][j] = true;
          similarityScores[i][j] = Float.MIN_VALUE;
        }
      }
    }
  }
}
