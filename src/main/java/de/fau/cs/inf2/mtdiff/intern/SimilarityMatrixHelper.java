/*
 * Copyright (c) 2017 Programming Systems Group, CS Department, FAU
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package de.fau.cs.inf2.mtdiff.intern;

import de.fau.cs.inf2.cas.common.bast.diff.LabelConfiguration;
import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.ComparePair;
import de.fau.cs.inf2.cas.common.util.string.NGramCalculator;

import de.fau.cs.inf2.mtdiff.TreeMatcher;
import de.fau.cs.inf2.mtdiff.TreeMatcherConfiguration;
import de.fau.cs.inf2.mtdiff.hungarian.DoubleMatrix;
import de.fau.cs.inf2.mtdiff.hungarian.HungarianParallel;
import de.fau.cs.inf2.mtdiff.parameters.SimilarityMatrixHelperParameter;
import de.fau.cs.inf2.mtdiff.parameters.UpdateSimilarityRowParameter;
import de.fau.cs.inf2.mtdiff.similarity.InnerNodeMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@SuppressWarnings("PMD.TooManyFields")
class SimilarityMatrixHelper {

  private boolean[][] aggregationFinished;
  private INode[] firstAggregations;
  private INode[] secondAggregations;
  private ConcurrentHashMap<INode, MatchingCandidate<INode>> currentResultMap;
  private AtomicBoolean changed;
  private ConcurrentHashMap<INode, ConcurrentHashMap<INode, Float>> similarityCache;
  private AtomicLong similarityEntries;
  private boolean verbose;
  private Map<INode, INode> parents1;
  private Map<INode, INode> parents2;
  private Map<INode, ArrayList<INode>> leavesMap1 = null;
  private Map<INode, ArrayList<INode>> leavesMap2 = null;
  private Map<INode, ArrayList<INode>> directChildrenMap1 = null;
  private Map<INode, ArrayList<INode>> directChildrenMap2 = null;
  private ConcurrentHashMap<INode, ConcurrentHashMap<INode, MatchingCandidate<INode>>> candidateMap;

  private LabelConfiguration labelConfiguration;
  private static final int NODE_AGGREGATION_LABEL = -1000;
  private LMatcher leafMatcher;
  private IdentityHashMap<INode, String> stringMap;
  private int numThreads;
  private TreeMatcherConfiguration configuration;

  private ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Float>> hashbasedCache;
  private IdentityHashMap<INode, Integer> quickFindHashMap;

  /**
   * Instantiates a new restructured tree diff helper runnable.
   * 
   * @param parameterObject TODO
   */
  SimilarityMatrixHelper(SimilarityMatrixHelperParameter parameterObject) {

    this.aggregationFinished = parameterObject.aggregationFinisheds;
    this.firstAggregations = parameterObject.firstAggregations;
    this.secondAggregations = parameterObject.secondAggregations;
    this.currentResultMap = parameterObject.currentResultMap;
    this.changed = parameterObject.changed;
    this.newNodes = parameterObject.newNodes;
    this.oldNodes = parameterObject.oldNodes;
    this.similarityEntries = parameterObject.similarityEntries;
    this.resultMap = parameterObject.resultMap;
    this.stringSim = parameterObject.stringSim;
    this.stringSimCache = parameterObject.stringSimCache;
    this.onlyOneClassPair = parameterObject.onlyOneClassPair;
    this.similarityScores = parameterObject.similarityScores;
    this.initialList = parameterObject.initialList;
    this.similarityCache = parameterObject.similarityCache;
    this.verbose = parameterObject.verbose;
    this.parents1 = parameterObject.parents1;
    this.parents2 = parameterObject.parents2;
    this.leavesMap1 = parameterObject.leavesMap1;
    this.leavesMap2 = parameterObject.leavesMap2;
    this.labelConfiguration = parameterObject.labelConfiguration;
    this.leafMatcher = parameterObject.leafMatcher;
    this.directChildrenMap1 = parameterObject.directChildrenMap1;
    this.directChildrenMap2 = parameterObject.directChildrenMap2;
    this.candidateMap = parameterObject.candidateMap;
    this.weightSimilarity = parameterObject.weightSimilarity;
    this.weightPosition = parameterObject.weightPosition;
    this.stringMap = parameterObject.stringMap;
    this.numThreads = parameterObject.numThreads;
    this.configuration = parameterObject.configuration;
    this.hashbasedCache = parameterObject.hashbasedCache;
    this.quickFindHashMap = parameterObject.quickFindHashMap;
  }

  private ArrayList<INode> oldNodes;
  private ArrayList<INode> newNodes;
  private IdentityHashMap<INode, ComparePair<INode>> resultMap;
  private NGramCalculator stringSim;
  private ConcurrentHashMap<String, Float> stringSimCache;
  private boolean onlyOneClassPair;
  private double[][] similarityScores;
  private ConcurrentSkipListSet<MatchingCandidate<INode>> initialList;
  private double weightSimilarity;
  private double weightPosition;


  /**
   * Call.
   *
   * @return the boolean
   */
  Boolean call() {
    try {
      while (changed.get()) {
        changed.set(false);
        HashSet<Integer> tmpSet = new HashSet<>();
        double minValue = Double.MAX_VALUE;
        double maxValue = Double.MIN_VALUE;
        for (int i = 0; i < oldNodes.size(); i++) {
          for (int j = 0; j < newNodes.size(); j++) {
            int value = (int) (similarityScores[i][j] * 1000);
            if (tmpSet.contains(value)) {
              minValue = Math.min(minValue, similarityScores[i][j]);
            } else {
              tmpSet.add(value);
            }
            maxValue = Math.max(maxValue, similarityScores[i][j]);
          }
        }
        for (int i = 0; i < oldNodes.size(); i++) {
          for (int j = 0; j < newNodes.size(); j++) {
            if (Math.abs(similarityScores[i][j] - maxValue) < 0.00001) {
              similarityScores[i][j] += 1.0;
            } else if (minValue == Double.MAX_VALUE) {
              aggregationFinished[i][j] = true;
            }
          }
        }
        createAggregations();
        if (oldNodes.size() * newNodes.size() > 5000 && oldNodes.size() > numThreads) {
          updateSimilarityRowParallel();
        } else {
          updateSimilarityRowSequential();
        }
      }
      computePairs();
    } catch (Exception e) {
      e.printStackTrace();
      throw new InternalError();
    }
    return true;
  }

  private void computePairs() {
    DoubleMatrix matrix = DoubleMatrix.unsafeNewMatrix(Math.max(oldNodes.size(), newNodes.size()),
        Math.max(oldNodes.size(), newNodes.size()));
    int maxDiff = Integer.MIN_VALUE;
    double[][] posDiff = new double[oldNodes.size()][newNodes.size()];
    computeRankScores(maxDiff, posDiff);

    List<INode> skipList = new LinkedList<>();

    addPositionValues(posDiff);



    double[][] similarityScoresTransformed = performTransformation(similarityScores);

    double[][] posDiffTransformed = performTransformation(posDiff);
    int maxDim = Math.max(oldNodes.size(), newNodes.size());
    double[][] matrixPar = new double[maxDim][maxDim];
    for (int i = 0; i < maxDim; i++) {
      for (int j = 0; j < maxDim; j++) {
        matrixPar[i][j] = 0;
      }
    }
    for (int i = 0; i < oldNodes.size(); i++) {
      for (int j = 0; j < newNodes.size(); j++) {

        double val = ((1000 - similarityScoresTransformed[i][j]) * weightSimilarity)
            + posDiffTransformed[i][j] * weightPosition;
        double val2 = ((similarityScoresTransformed[i][j]) * weightSimilarity)
            + (1000 - posDiffTransformed[i][j]) * weightPosition;

        matrix.set(i, j, val2);
        matrixPar[i][j] = val;
      }
    }


    int[] res = null;
    if (Math.abs(oldNodes.size() - newNodes.size()) > 2500
        || oldNodes.size() > 2000 && newNodes.size() > 2000) {
      res = new int[matrixPar.length];
      HashSet<Integer> alreadyDone = new HashSet<>();
      for (int i = 0; i < oldNodes.size(); i++) {
        double min = Double.MAX_VALUE;
        int index = -1;
        for (int j = 0; j < newNodes.size(); j++) {
          if (matrixPar[i][j] < min && !alreadyDone.contains(j)) {
            min = matrixPar[i][j];
            index = j;
            if (min == 0) {
              break;
            }
          }
        }
        if (min != Double.MAX_VALUE) {
          res[i] = index;
          alreadyDone.add(index);
        }
      }
    } else {
      res = HungarianParallel.assign(matrixPar, numThreads);

    }

    initialList.clear();

    for (int i = 0; i < oldNodes.size(); i++) {
      if (res[i] < newNodes.size()) {
        if (!(skipList.contains(oldNodes.get(i)) || skipList.contains(newNodes.get(res[i])))) {
          if (candidateMap.get(oldNodes.get(i)).get(newNodes.get(res[i])) != null) {
            initialList.add(candidateMap.get(oldNodes.get(i)).get(newNodes.get(res[i])));
          } else {
            initialList
                .add(new MatchingCandidate<INode>(oldNodes.get(i), newNodes.get(res[i]), 0.0f));
          }
        }

      }
    }
  }

  private void addPositionValues(double[][] posDiff) {
    for (int i = 0; i < oldNodes.size(); i++) {
      double maxsim = 0;
      double minDiff = Integer.MAX_VALUE;
      for (int j = 0; j < newNodes.size(); j++) {

        if (similarityScores[i][j] - maxsim > 0.001) {
          maxsim = similarityScores[i][j];
          minDiff = posDiff[i][j];
        } else if (similarityScores[i][j] - maxsim < 0.001) {
          if (minDiff > posDiff[i][j]) {
            minDiff = posDiff[i][j];
          }
        }
      }

    }
  }

  private void computeRankScores(int maxDiff, double[][] posDiff) {
    ArrayList<INode> oldNodesSorted = new ArrayList<>(oldNodes);
    Collections.sort(oldNodesSorted, new IdComparator());
    HashMap<INode, Integer> oldRankMap = new HashMap<>();
    int rank = 0;
    for (INode node : oldNodesSorted) {
      oldRankMap.put(node, rank);
      rank++;
    }
    ArrayList<INode> newNodesSorted = new ArrayList<>(newNodes);
    Collections.sort(newNodesSorted, new IdComparator());
    HashMap<INode, Integer> newRankMap = new HashMap<>();
    rank = 0;
    for (INode node : newNodesSorted) {
      newRankMap.put(node, rank);
      rank++;
    }
    for (int i = 0; i < oldNodes.size(); i++) {
      for (int j = 0; j < newNodes.size(); j++) {
        int val = Math.abs(oldRankMap.get(oldNodes.get(i)) - newRankMap.get(newNodes.get(j)));

        posDiff[i][j] = val;
        if (val > maxDiff) {
          maxDiff = val;
        }
      }
    }
  }

  private void createAggregations() {
    if (oldNodes.size() > 500) {
      ExecutorService innerService = Executors.newFixedThreadPool(numThreads);
      AtomicInteger workerCounter = new AtomicInteger(0);
      int start = 0;
      int step = oldNodes.size() / numThreads > 0 ? oldNodes.size() / numThreads : 1;
      for (int i = 0; i < numThreads - 1; i++) {
        if (start + step > oldNodes.size()) {
          break;
        }
        AggregationRunnable runnable = new AggregationRunnable(start, start + step, workerCounter);
        workerCounter.incrementAndGet();
        innerService.execute(runnable);
        start += step;
      }
      AggregationRunnable runnable = new AggregationRunnable(start, oldNodes.size(), workerCounter);
      workerCounter.incrementAndGet();
      innerService.execute(runnable);
      while (workerCounter.get() > 0) {
        Thread.yield();
      }
      innerService.shutdown();
    } else {
      for (int i = 0; i < oldNodes.size(); i++) {
        if (firstAggregations[i] != null) {
          NodeAggregation tmp =
              createAggregation((INode) firstAggregations[i], parents1, stringMap);
          if (tmp == null) {
            for (int j = 0; j < newNodes.size(); j++) {
              if (!aggregationFinished[i][j]) {
                aggregationFinished[i][j] = true;
              }
            }
          }
          firstAggregations[i] = tmp;
        }
      }
    }

    for (int j = 0; j < newNodes.size(); j++) {
      if (secondAggregations[j] != null) {
        NodeAggregation tmp = createAggregation((INode) secondAggregations[j], parents2, stringMap);
        if (tmp == null) {
          for (int i = 0; i < oldNodes.size(); i++) {
            if (!aggregationFinished[i][j]) {
              aggregationFinished[i][j] = true;
            }
          }
        }
        secondAggregations[j] = tmp;
      }
    }
  }

  private void updateSimilarityRowSequential() {
    if (oldNodes.size() > 0) {
      updateSimilarityRow(new UpdateSimilarityRowParameter(aggregationFinished, similarityScores,
          firstAggregations, secondAggregations, currentResultMap, changed, 0, newNodes,
          onlyOneClassPair, resultMap, stringSim, stringSimCache, similarityCache,
          similarityEntries, stringMap, verbose, hashbasedCache, quickFindHashMap));
    }
    for (int i = 1; i < oldNodes.size(); i++) {
      updateSimilarityRow(new UpdateSimilarityRowParameter(aggregationFinished, similarityScores,
          firstAggregations, secondAggregations, currentResultMap, changed, i, newNodes,
          onlyOneClassPair, resultMap, stringSim, stringSimCache, similarityCache,
          similarityEntries, stringMap, verbose, hashbasedCache, quickFindHashMap));
    }
  }

  private void updateSimilarityRowParallel() {
    ExecutorService innerService = Executors.newFixedThreadPool(numThreads);
    AtomicInteger workerCounter = new AtomicInteger(0);
    int start = 0;
    int step = oldNodes.size() / numThreads > 0 ? oldNodes.size() / numThreads : 1;
    for (int i = 0; i < numThreads - 1; i++) {
      if (start + step > oldNodes.size()) {
        break;
      }
      SimilarityRowUpdater runnable = new SimilarityRowUpdater(start, start + step, workerCounter);
      workerCounter.incrementAndGet();
      innerService.execute(runnable);
      start += step;
    }
    SimilarityRowUpdater runnable = new SimilarityRowUpdater(start, oldNodes.size(), workerCounter);
    workerCounter.incrementAndGet();
    innerService.execute(runnable);
    while (workerCounter.get() > 0) {
      Thread.yield();
    }
    innerService.shutdown();
  }

  private static double[][] performTransformation(double[][] original) {
    double[][] similarityValues = new double[original.length][];
    for (int i = 0; i < original.length; i++) {
      similarityValues[i] = Arrays.copyOf(original[i], original[i].length);
    }

    double min = Double.MAX_VALUE;
    double max = Double.MIN_VALUE;

    final int size = similarityValues.length;
    for (int row = 0; row < size; ++row) {
      for (int column = 0; column < similarityValues[row].length; ++column) {
        final double value = similarityValues[row][column];

        if (value < min) {
          min = value;
        }

        if (value > max) {
          max = value;
        }
      }
    }

    for (int row = 0; row < size; ++row) {
      for (int column = 0; column < similarityValues[row].length; ++column) {
        final double value = similarityValues[row][column];

        if (max != min) {
          final double newSimilarityValue = (value - min) / (max - min) * 1000;
          similarityValues[row][column] = newSimilarityValue;
        } else {
          similarityValues[row][column] = 1000;
        }

      }
    }

    return similarityValues;
  }

  /**
   * todo. Creates a {@see diff.tree.matching.NodeAggregation} that consists of the specified node,
   * it's parent and all of it's siblings.
   */
  private NodeAggregation createAggregation(final INode node, final Map<INode, INode> parentsMap,
      IdentityHashMap<INode, String> stringMap) {
    if (node.getTypeWrapped() == NODE_AGGREGATION_LABEL) {
      final NodeAggregation aggregation = (NodeAggregation) node;
      final INode parent = parentsMap.get(aggregation.getAssociatedTree());

      if (parent == null) {
        return null;
      }

      aggregation.setAssociatedTree(parent, stringMap);
      return aggregation;
    } else {
      final INode parent = parentsMap.get(node);
      if (parent == null) {
        return null;
      }

      return new NodeAggregation(parent, stringMap);
    }
  }

  private void updateSimilarityRow(UpdateSimilarityRowParameter parameterObject) {
    {

      for (int j = 0; j < parameterObject.newNodes.size(); j++) {
        if (!parameterObject.aggregationFinisheds[parameterObject.oldNodeIndex][j]) {
          if (parameterObject.firstAggregations[parameterObject.oldNodeIndex] != null
              && parameterObject.firstAggregations[parameterObject.oldNodeIndex]
                  .getTypeWrapped() != NodeAggregation.TAG) {
            parameterObject.firstAggregations[parameterObject.oldNodeIndex] =
                new NodeAggregation(parameterObject.firstAggregations[parameterObject.oldNodeIndex],
                    parameterObject.stringMap);
          }
          if (parameterObject.secondAggregations[j] != null
              && parameterObject.secondAggregations[j].getTypeWrapped() != NodeAggregation.TAG) {
            parameterObject.secondAggregations[j] = new NodeAggregation(
                parameterObject.secondAggregations[j], parameterObject.stringMap);
          }
          final NodeAggregation firstAggregation =
              (NodeAggregation) parameterObject.firstAggregations[parameterObject.oldNodeIndex];
          final NodeAggregation secondAggregation =
              (NodeAggregation) parameterObject.secondAggregations[j];
          if (firstAggregation == null || firstAggregation.getAssociatedTree() == null
              || secondAggregation == null || secondAggregation.getAssociatedTree() == null) {
            parameterObject.aggregationFinisheds[parameterObject.oldNodeIndex][j] = true;
          } else {
            float similarity = Float.MIN_VALUE;
            ConcurrentHashMap<INode, Float> map =
                parameterObject.similarityCache.get(firstAggregation.getAssociatedTree());
            if (firstAggregation.getHash() == secondAggregation.getHash()) {
              similarity = 1.0f;
            } else if (firstAggregation.getAssociatedTree().getTypeWrapped() != secondAggregation
                .getAssociatedTree().getTypeWrapped()) {
              similarity = 0.0f;

            } else {
              final Integer firstHash =
                  parameterObject.quickFindHashMap.get(firstAggregation.getAssociatedTree());
              ConcurrentHashMap<Integer, Float> singleMap = new ConcurrentHashMap<>();
              hashbasedCache.putIfAbsent(firstHash, singleMap);
              singleMap = hashbasedCache.get(firstHash);
              final Integer secondHash =
                  parameterObject.quickFindHashMap.get(secondAggregation.getAssociatedTree());
              if (singleMap.containsKey(secondHash)) {
                similarity = (float) (singleMap.get(secondHash));
              } else if (leavesMap1.get(firstAggregation.getAssociatedTree()).size()
                  * leavesMap2.get(secondAggregation.getAssociatedTree()).size() > 10000) {

                if (map != null) {
                  Float value = map.get(secondAggregation.getAssociatedTree());
                  if (value != null) {
                    similarity = value;
                  } else {
                    similarity = simpleSimilarity(firstAggregation.getAssociatedTree(),
                        secondAggregation.getAssociatedTree(), parameterObject.stringSimCache,
                        parameterObject.onlyOneClassPair, parameterObject.resultMap,
                        parameterObject.stringSim, parameterObject.currentResultMap,
                        parameterObject.verbose);
                    singleMap.put(secondHash, similarity);

                    if (parameterObject.similarityEntries
                        .get() < TreeMatcher.SIMILIARITY_CACHE_SIZE) {
                      map.put(secondAggregation.getAssociatedTree(), similarity);
                      parameterObject.similarityEntries.incrementAndGet();
                    }
                  }
                } else {
                  ConcurrentHashMap<INode, Float> tmp = new ConcurrentHashMap<>();
                  similarity = simpleSimilarity(firstAggregation.getAssociatedTree(),
                      secondAggregation.getAssociatedTree(), parameterObject.stringSimCache,
                      parameterObject.onlyOneClassPair, parameterObject.resultMap,
                      parameterObject.stringSim, parameterObject.currentResultMap,
                      parameterObject.verbose);
                  singleMap.put(secondHash, similarity);

                  if (parameterObject.similarityEntries
                      .get() < TreeMatcher.SIMILIARITY_CACHE_SIZE) {
                    parameterObject.similarityCache.put(firstAggregation.getAssociatedTree(), tmp);
                    tmp.put(secondAggregation.getAssociatedTree(), similarity);
                    parameterObject.similarityEntries.incrementAndGet();
                  }
                }
              } else {
                similarity = simpleSimilarity(firstAggregation.getAssociatedTree(),
                    secondAggregation.getAssociatedTree(), parameterObject.stringSimCache,
                    parameterObject.onlyOneClassPair, parameterObject.resultMap,
                    parameterObject.stringSim, parameterObject.currentResultMap,
                    parameterObject.verbose);
                // singleMap.put(secondHash, similarity);

              }

            }
            parameterObject.changed.set(true);
            parameterObject.similarityScores[parameterObject.oldNodeIndex][j] += similarity;
          }

        }
      }
    }
    return;
  }

  /**
   * A similarity calculation based on a simplified ChangeDistiller matching.
   */
  private float simpleSimilarity(final INode tree1, final INode tree2,
      ConcurrentHashMap<String, Float> stringSimCache, boolean onlyOneClassPair,
      IdentityHashMap<INode, ComparePair<INode>> resultMap, NGramCalculator stringSim,
      ConcurrentHashMap<INode, MatchingCandidate<INode>> currentMatchings, boolean verbose) {

    float similarity = 0.0f;
    if (tree1.getTypeWrapped() == labelConfiguration.rootLabel
        && tree2.getTypeWrapped() == labelConfiguration.rootLabel) {
      return 1.0f;
    } else if (tree1.getTypeWrapped() == labelConfiguration.rootLabel
        || tree2.getTypeWrapped() == labelConfiguration.rootLabel) {
      return 0.0f;
    }
    final HashSet<ComparePair<INode>> currentResultSet = new HashSet<>();
    final LinkedList<MatchingCandidate<INode>> matchedLeaves = new LinkedList<>();

    Set<INode> leaves1 = null;
    Set<INode> unmatchedNodes1 = null;
    PostOrderSetGenerator setGeneratorNew = new PostOrderSetGenerator();

    setGeneratorNew.createSetsForNode(tree1);
    leaves1 = setGeneratorNew.getSetOfLeaves();
    unmatchedNodes1 = setGeneratorNew.getSetOfNodes();

    setGeneratorNew.createSetsForNode(tree2);
    Set<INode> leaves2 = setGeneratorNew.getSetOfLeaves();
    final Set<INode> unmatchedNodes2 = setGeneratorNew.getSetOfNodes();

    if (tree1.getTypeWrapped() == labelConfiguration.classLabel
        && tree2.getTypeWrapped() == labelConfiguration.classLabel) {
      if (onlyOneClassPair) {
        return 1.0f;
      }
      String sc1 = tree1.getLabel();
      String sc2 = tree2.getLabel();

      float csim = stringSim.similarity(sc1, sc2);
      if (csim > 0.6) {
        if (leaves1.size() > 3000 && leaves2.size() > 3000) {
          float diffCount = Math.abs(leaves1.size() - leaves2.size())
              / (float) Math.max(leaves1.size(), leaves2.size());
          if (diffCount < 0.2) {
            return (float) csim + (1 - diffCount) / (float) 2;
          }
        }
      }
    } else if (tree1.getTypeWrapped() == labelConfiguration.classLabel
        || tree2.getTypeWrapped() == labelConfiguration.classLabel) {
      return 0.0f;
    }
    final int nodeCount = Math.max(unmatchedNodes1.size(), unmatchedNodes2.size());
    final Set<Integer> nodeTags = new HashSet<>();
    for (final INode firstNode : leaves1) {
      nodeTags.add(firstNode.getTypeWrapped());
    }
    for (final INode secondNode : leaves2) {
      nodeTags.add(secondNode.getTypeWrapped());
    }
    ArrayList<INode> leaves1tmp = new ArrayList<INode>();
    ArrayList<INode> leaves2tmp = new ArrayList<INode>();
    leaves1tmp.addAll(leaves1);
    leaves2tmp.addAll(leaves2);
    createPairs(resultMap, currentMatchings, currentResultSet, leaves1tmp, leaves2tmp);
    for (int tag : nodeTags) {

      computeSimilarityForTag(tree1, tree2, stringSimCache, stringSim, verbose, matchedLeaves,
          leaves1tmp, leaves2tmp, tag);

    }

    IdentityHashMap<INode, Integer> orderedList1 = TreeMatcher.getNodesInOrder(tree1);
    IdentityHashMap<INode, Integer> orderedList2 = TreeMatcher.getNodesInOrder(tree2);
    Collections.sort(matchedLeaves, new PairComparator<INode>(orderedList1, orderedList2));

    similarity = addToResultset(similarity, currentResultSet, matchedLeaves, unmatchedNodes1,
        unmatchedNodes2);
    similarity =
        computeInnerNodeSimilarity(similarity, currentResultSet, unmatchedNodes1, unmatchedNodes2);
    matchedLeaves.clear();
    unmatchedNodes1.clear();
    unmatchedNodes2.clear();
    currentResultSet.clear();
    leaves1tmp.clear();
    leaves2tmp.clear();
    return similarity / (float) nodeCount;
  }

  private float addToResultset(float similarity, HashSet<ComparePair<INode>> currentResultSet,
      final LinkedList<MatchingCandidate<INode>> matchedLeaves, Set<INode> unmatchedNodes1,
      final Set<INode> unmatchedNodes2) {
    while (!matchedLeaves.isEmpty()) {
      final MatchingCandidate<INode> pair = matchedLeaves.pollLast();

      if (!unmatchedNodes1.remove(pair.getOldElement())) {
        continue;
      }
      if (!unmatchedNodes2.remove(pair.getNewElement())) {
        continue;
      }

      currentResultSet.add(pair.dropValue());
      similarity += pair.getValue();
    }
    return similarity;
  }

  private void createPairs(IdentityHashMap<INode, ComparePair<INode>> resultMap,
      ConcurrentHashMap<INode, MatchingCandidate<INode>> currentMatchings,
      HashSet<ComparePair<INode>> currentResultSet, ArrayList<INode> leaves1tmp,
      ArrayList<INode> leaves2tmp) {
    if (currentMatchings.size() > 0 || resultMap.size() > 0) {
      Iterator<INode> nodeIt = leaves1tmp.iterator();
      while (nodeIt.hasNext()) {
        INode next = nodeIt.next();
        ComparePair<INode> pair = resultMap.get(next);
        if (pair != null) {
          nodeIt.remove();
          if (leaves2tmp.contains(pair.getNewElement())) {
            currentResultSet.add(pair);
            leaves2tmp.remove(pair.getNewElement());
          }
        } else {
          MatchingCandidate<INode> mc = currentMatchings.get(next);
          if (mc != null) {
            currentResultSet.add(mc.dropValue());
            nodeIt.remove();
            leaves2tmp.remove(mc.getNewElement());
          }
        }

      }
    }
  }

  private float computeInnerNodeSimilarity(float similarity,
      HashSet<ComparePair<INode>> currentResultSet, Set<INode> unmatchedNodes1,
      final Set<INode> unmatchedNodes2) {
    InnerNodeMatcher nodeMatcher = new InnerNodeMatcher(configuration, labelConfiguration,
        leavesMap1, leavesMap2, directChildrenMap1, directChildrenMap2, currentResultSet);
    for (final INode firstNode : unmatchedNodes1) {
      final Iterator<INode> iterator = unmatchedNodes2.iterator();
      if (firstNode.getChildrenWrapped() == null || firstNode.getChildrenWrapped().size() == 0) {
        continue;
      }
      while (iterator.hasNext()) {
        final INode secondNode = iterator.next();
        if (firstNode.getTypeWrapped() != secondNode.getTypeWrapped()) {
          continue;
        }
        float sim = 0.0f;
        sim = nodeMatcher.similarity(firstNode, secondNode);

        if (nodeMatcher.match(firstNode, secondNode, sim)) {
          similarity += sim;

          iterator.remove();
          break;
        }
      }
    }
    return similarity;
  }

  private void computeSimilarityForTag(final INode tree1, final INode tree2,
      ConcurrentHashMap<String, Float> stringSimCache, NGramCalculator stringSim, boolean verbose,
      final LinkedList<MatchingCandidate<INode>> matchedLeaves, ArrayList<INode> leaves1tmp,
      ArrayList<INode> leaves2tmp, int tag) {
    ArrayList<INode> subLeaves1 = new ArrayList<INode>();
    ArrayList<INode> subLeaves2 = new ArrayList<INode>();
    Iterator<INode> it = leaves1tmp.iterator();
    while (it.hasNext()) {
      INode node = it.next();
      if (node.getTypeWrapped() == tag) {
        subLeaves1.add(node);
        it.remove();
      }
    }
    it = leaves2tmp.iterator();
    while (it.hasNext()) {
      INode node = it.next();
      if (node.getTypeWrapped() == tag) {
        subLeaves2.add(node);
        it.remove();
      }
    }

    long count = 0;
    for (final INode firstNode : subLeaves1) {
      float maxSim = Float.MIN_VALUE;
      ArrayList<MatchingCandidate<INode>> canlist = new ArrayList<>();
      for (final INode secondNode : subLeaves2) {
        float sim = Float.MIN_VALUE;
        if (labelConfiguration.labelsForStringCompare.contains(tag)) {
          if (firstNode.getLabel() == null || secondNode.getLabel() == null) {
            sim = 0.0f;
          } else if (firstNode.getLabel().equals(secondNode.getLabel())) {
            sim = 1.0f;
          } else {

            if (stringSimCache.get(firstNode.getLabel() + "@@" + secondNode.getLabel()) != null) {
              sim = stringSimCache.get(firstNode.getLabel() + "@@" + secondNode.getLabel());
            } else if (stringSimCache
                .get(secondNode.getLabel() + "@@" + firstNode.getLabel()) != null) {
              sim = stringSimCache.get(secondNode.getLabel() + "@@" + firstNode.getLabel());
            } else {
              sim = stringSim.similarity(firstNode.getLabel(), secondNode.getLabel());
            }
          }
        } else if (tag == labelConfiguration.basicTypeLabel
            || tag == labelConfiguration.qualifierLabel) {
          if (firstNode.getLabel().equals(secondNode.getLabel())) {
            sim = 1.0f;
          } else {
            sim = 0.0f;
          }
        } else {

          sim = leafMatcher.leavesSimilarity(firstNode, secondNode);

        }
        if (sim >= maxSim && sim > 0.0f) {
          if (sim > maxSim && !canlist.isEmpty()) {
            canlist.clear();
          }
          if (leafMatcher.match(firstNode, secondNode, sim)) {
            final MatchingCandidate<INode> candidate =
                new MatchingCandidate<INode>(firstNode, secondNode, sim);
            if (sim > maxSim) {
              maxSim = sim;
            }
            canlist.add(candidate);
          }
          count++;
        }
      }
      matchedLeaves.addAll(canlist);
    }
  }

  private class SimilarityRowUpdater implements Runnable {
    private final int start;
    private final int end;
    private AtomicInteger workerCounter;

    SimilarityRowUpdater(int start, int end, AtomicInteger workerCounter) {
      this.start = start;
      this.end = end;
      this.workerCounter = workerCounter;
    }

    @Override
    public void run() {
      for (int i = start; i < end; i++) {
        updateSimilarityRow(new UpdateSimilarityRowParameter(aggregationFinished, similarityScores,
            firstAggregations, secondAggregations, currentResultMap, changed, i, newNodes,
            onlyOneClassPair, resultMap, stringSim, stringSimCache, similarityCache,
            similarityEntries, stringMap, verbose, hashbasedCache, quickFindHashMap));
      }
      workerCounter.decrementAndGet();
    }

  }

  private class AggregationRunnable implements Runnable {
    private final int start;
    private final int end;
    private AtomicInteger workerCounter;

    AggregationRunnable(int start, int end, AtomicInteger workerCounter) {
      this.start = start;
      this.end = end;
      this.workerCounter = workerCounter;
    }

    @Override
    public void run() {
      for (int i = start; i < end; i++) {
        if (firstAggregations[i] != null) {
          NodeAggregation tmp =
              createAggregation((INode) firstAggregations[i], parents1, stringMap);
          if (tmp == null) {
            for (int j = 0; j < newNodes.size(); j++) {
              if (!aggregationFinished[i][j]) {
                aggregationFinished[i][j] = true;
              }
            }
          }
          firstAggregations[i] = tmp;
        }
      }
      workerCounter.decrementAndGet();
    }

  }

  private class IdComparator implements Comparator<INode> {

    /**
     * Compare.
     *
     * @param o1 the o1
     * @param o2 the o2
     * @return the int
     */
    @Override
    public int compare(INode o1, INode o2) {
      return Integer.compare(o1.getId(), o2.getId());
    }

  }

}
