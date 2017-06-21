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

package de.fau.cs.inf2.mtdiff;

import de.fau.cs.inf2.cas.common.bast.diff.LabelConfiguration;
import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.ComparePair;
import de.fau.cs.inf2.cas.common.util.string.NGramCalculator;

import de.fau.cs.inf2.mtdiff.intern.InnerMatcherMtDiffRunnable;
import de.fau.cs.inf2.mtdiff.intern.LMatcher;
import de.fau.cs.inf2.mtdiff.intern.LeafSimResults;
import de.fau.cs.inf2.mtdiff.intern.LeafSimilarityRunnable;
import de.fau.cs.inf2.mtdiff.intern.MatchingCandidate;
import de.fau.cs.inf2.mtdiff.intern.PairComparator;
import de.fau.cs.inf2.mtdiff.intern.PostOrderSetGenerator;
import de.fau.cs.inf2.mtdiff.intern.SimilarLeafExaminationRunnable;
import de.fau.cs.inf2.mtdiff.intern.StringDifferenceRunnable;
import de.fau.cs.inf2.mtdiff.intern.TagComparator;
import de.fau.cs.inf2.mtdiff.optimizations.CrossMoveMatcherThetaF;
import de.fau.cs.inf2.mtdiff.optimizations.IdenticalSubtreeMatcherThetaA;
import de.fau.cs.inf2.mtdiff.optimizations.InnerNodesMatcherThetaD;
import de.fau.cs.inf2.mtdiff.optimizations.LcsOptMatcherThetaB;
import de.fau.cs.inf2.mtdiff.optimizations.LeafMoveMatcherThetaE;
import de.fau.cs.inf2.mtdiff.optimizations.UnmappedLeavesMatcherThetaC;
import de.fau.cs.inf2.mtdiff.parameters.ComputeLeafSimilarityParameter;
import de.fau.cs.inf2.mtdiff.parameters.ComputeTiedLeafSimilaritiesParameter;
import de.fau.cs.inf2.mtdiff.parameters.LeafSimilarityRunnableParameter;
import de.fau.cs.inf2.mtdiff.parameters.MatchInnerNodesParameter;
import de.fau.cs.inf2.mtdiff.parameters.MatchLeavesParameter;
import de.fau.cs.inf2.mtdiff.parameters.SimilarLeafExaminationRunnableParameter;
import de.fau.cs.inf2.mtdiff.similarity.InnerNodeMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class TreeMatcher implements ITreeMatcher {

  public static final int SIMILIARITY_CACHE_SIZE = 100000;
  private static final int HASH_MAP_SIZE = 50000;

  private INode tree1;
  private INode tree2;
  private LabelConfiguration labelConfiguration;

  private ExecutorService executorService;
  private HashSet<ComparePair<INode>> finalResultSet;
  private AtomicLong similarityEntries = new AtomicLong(0);
  private ITreeDifferencer treeDifferencer;
  private TreeMatcherConfiguration configuration;
  private boolean verbose;
  private int numThreads;

  /**
   * Instantiates a new tree matcher.
   *
   * @param configuration the configuration
   */
  public TreeMatcher(TreeMatcherConfiguration configuration) {

    this(null, configuration);
  }

  private TreeMatcher(ITreeDifferencer treeDifferencer, TreeMatcherConfiguration configuration) {
    this.treeDifferencer = treeDifferencer;
    this.configuration = configuration;
  }

  /**
   * Inits ares.
   *
   * @param executorService the executor service
   * @param leafThreshold the leaf threshold
   */
  @Override
  public void initAres(ExecutorService executorService, double leafThreshold, int numThreads) {
    this.executorService = executorService;
    this.numThreads = numThreads;
  }


  /**
   * Inits the.
   *
   * @param tree1 the tree1
   * @param tree2 the tree2
   * @param labelConfiguration the label configuration
   */
  @Override
  public void init(INode tree1, INode tree2, LabelConfiguration labelConfiguration) {
    this.tree1 = tree1;
    this.tree2 = tree2;
    this.labelConfiguration = labelConfiguration;

  }


  /**
   * Gets the matching pairs.
   *
   * @return the matching pairs
   */
  @Override
  public Set<ComparePair<INode>> getMatchingPairs() {
    return finalResultSet;
  }

  /**
   * Gets the nodes in order.
   *
   * @param tree the tree
   * @return the nodes in order
   */
  public static IdentityHashMap<INode, Integer> getNodesInOrder(INode tree) {
    IdentityHashMap<INode, Integer> nodes = new IdentityHashMap<>();
    ConcurrentLinkedQueue<INode> workList = new ConcurrentLinkedQueue<INode>();
    workList.add(tree);
    int counter = 0;
    while (!workList.isEmpty()) {
      INode node = (INode) workList.remove();
      nodes.put(node, counter);
      counter++;
      for (INode child : node.getChildrenWrapped()) {
        workList.add(child);
      }
    }
    return nodes;
  }

  private ArrayList<INode> getUnmachedNodesInOrder(INode tree, Set<INode> unorderedNodes) {
    ArrayList<INode> nodes = new ArrayList<>();
    ConcurrentLinkedQueue<INode> workList = new ConcurrentLinkedQueue<INode>();
    workList.add(tree);
    while (!workList.isEmpty()) {
      INode node = (INode) workList.remove();
      if (unorderedNodes.contains(node)) {
        nodes.add(node);
        unorderedNodes.remove(node);
      }
      for (INode child : node.getChildrenWrapped()) {
        workList.add(child);
      }
    }
    return nodes;
  }

  private static int numberOfNodesWithTag(INode tree, int tag) {
    ConcurrentLinkedQueue<INode> workList = new ConcurrentLinkedQueue<INode>();
    workList.add(tree);
    int counter = 0;
    while (!workList.isEmpty()) {
      INode node = (INode) workList.remove();
      if (node.getTypeWrapped() == tag) {
        counter++;
      }
      for (INode child : node.getChildrenWrapped()) {
        workList.add(child);
      }
    }
    return counter;
  }

  private static void genStringHashes(Set<INode> leaves1, Set<INode> leaves2,
      ConcurrentHashMap<String, Float> stringSimCache, ExecutorService executioner,
      LabelConfiguration labelConfiguration, boolean verbose) {
    for (int tag : labelConfiguration.labelsForStringCompare) {
      ArrayList<INode> subLeaves1 = new ArrayList<INode>();
      ArrayList<INode> subLeaves2 = new ArrayList<INode>();
      Iterator<INode> it = leaves1.iterator();
      while (it.hasNext()) {
        INode node = it.next();
        if (node.getTypeWrapped() == tag) {
          subLeaves1.add(node);
        }
      }
      it = leaves2.iterator();
      while (it.hasNext()) {
        INode node = it.next();
        if (node.getTypeWrapped() == tag) {
          subLeaves2.add(node);
        }
      }
      long stringCount = 0;
      INode[] nodes = subLeaves1.toArray(new INode[subLeaves1.size()]);
      int start = 0;
      int step = Math.max(1, nodes.length / 10);
      AtomicInteger stringRunnableCounter = new AtomicInteger();
      while (start + step < nodes.length) {
        if (executioner != null) {
          executioner.execute(new StringDifferenceRunnable(stringSimCache, nodes, subLeaves2,
              stringCount, start, start + step, stringRunnableCounter, verbose));
        } else {
          StringDifferenceRunnable myStringRunnable = new StringDifferenceRunnable(stringSimCache,
              nodes, subLeaves2, stringCount, start, start + step, stringRunnableCounter, verbose);
          myStringRunnable.run();
        }
        start += step;
      }
      StringDifferenceRunnable myStringRunnable = new StringDifferenceRunnable(stringSimCache,
          nodes, subLeaves2, stringCount, start, nodes.length, stringRunnableCounter, verbose);
      myStringRunnable.run();
      while (stringRunnableCounter.get() > 0) {
        Thread.yield();
      }
    }
  }

  /**
   * Gets the hash.
   *
   * @param node the node
   * @param quickFind the quick find
   * @param stringMap the string map
   * @return the hash
   */
  @SuppressWarnings({ "checkstyle:AvoidEscapedUnicodeCharacters" })
  public static String getHash(INode node, IdentityHashMap<INode, Integer> quickFind,
      IdentityHashMap<INode, String> stringMap) {
    String tmp = node.getTypeWrapped() + node.getLabel();
    for (INode child : node.getChildrenWrapped()) {
      tmp += getHash(child, quickFind, stringMap);
    }
    tmp += "\u2620";
    quickFind.put(node, tmp.hashCode());
    stringMap.put(node, tmp);
    return tmp;
  }


  /**
   * Compute matching pairs.
   *
   * @throws Exception the exception
   */
  public void computeMatchingPairs() throws Exception {
    final LMatcher leafMatcher = new LMatcher(labelConfiguration, configuration.leafThreshold);
    IdentityHashMap<INode, Integer> orderedList1 = getNodesInOrder(tree1);
    IdentityHashMap<INode, Integer> orderedList2 = getNodesInOrder(tree2);
    final TreeSet<MatchingCandidate<INode>> matchedLeaves =
        new TreeSet<>(new PairComparator<INode>(orderedList1, orderedList2));
    final IdentityHashMap<INode, ComparePair<INode>> resultMap = new IdentityHashMap<>();
    Set<INode> leaves1 = null;
    PostOrderSetGenerator setGeneratorNew = new PostOrderSetGenerator();
    int countClasses1 = numberOfNodesWithTag(tree1, labelConfiguration.classLabel);
    int countClasses2 = numberOfNodesWithTag(tree2, labelConfiguration.classLabel);
    boolean onlyOneClassPair = false;
    if (countClasses1 == 1 && countClasses2 == 1) {
      onlyOneClassPair = true;
    }
    setGeneratorNew.createSetsForNode(tree1);
    leaves1 = setGeneratorNew.getSetOfLeaves();
    final Set<INode> unmatchedNodes1 = setGeneratorNew.getSetOfNodes();

    final IdentityHashMap<INode, ArrayList<INode>> leavesMap1 = setGeneratorNew.getLeaveMap();
    final IdentityHashMap<INode, ArrayList<INode>> directChildrenMap1 =
        setGeneratorNew.getDirectChildrenMap();
    setGeneratorNew.createSetsForNode(tree2);
    Set<INode> leaves2 = setGeneratorNew.getSetOfLeaves();
    final Set<INode> unmatchedNodes2 = setGeneratorNew.getSetOfNodes();

    final IdentityHashMap<INode, ArrayList<INode>> leavesMap2 = setGeneratorNew.getLeaveMap();
    final IdentityHashMap<INode, ArrayList<INode>> directChildrenMap2 =
        setGeneratorNew.getDirectChildrenMap();
    final Set<Integer> nodeTags = new HashSet<>();
    HashMap<Integer, Integer> tagSizeMap = new HashMap<>();
    for (final INode firstNode : leaves1) {
      nodeTags.add(firstNode.getTypeWrapped());
      if (tagSizeMap.get(firstNode.getTypeWrapped()) == null) {
        tagSizeMap.put(firstNode.getTypeWrapped(), 1);
      } else {
        tagSizeMap.put(firstNode.getTypeWrapped(), tagSizeMap.get(firstNode.getTypeWrapped()) + 1);
      }
    }
    for (final INode secondNode : leaves2) {
      nodeTags.add(secondNode.getTypeWrapped());
      if (tagSizeMap.get(secondNode.getTypeWrapped()) == null) {
        tagSizeMap.put(secondNode.getTypeWrapped(), 1);
      } else {
        tagSizeMap.put(secondNode.getTypeWrapped(),
            tagSizeMap.get(secondNode.getTypeWrapped()) + 1);
      }
    }
    List<Map.Entry<Integer, Integer>> list = new LinkedList<>(tagSizeMap.entrySet());
    Collections.sort(list, new TagComparator(labelConfiguration));
    ConcurrentHashMap<String, Float> stringSimCache = new ConcurrentHashMap<>(HASH_MAP_SIZE);
    genStringHashes(leaves1, leaves2, stringSimCache, executorService, labelConfiguration, verbose);

    ArrayList<INode> leaves1tmp = new ArrayList<INode>();
    ArrayList<INode> leaves2tmp = new ArrayList<INode>();
    leaves1tmp.addAll(leaves1);
    leaves2tmp.addAll(leaves2);

    NGramCalculator stringSim = new NGramCalculator(2, 1000, 10000);
    HashSet<INode> skipList = new HashSet<>();

    IdenticalSubtreeMatcherThetaA.newUnchangedMatching(labelConfiguration, resultMap,
        unmatchedNodes1, unmatchedNodes2, stringSim, skipList, tree1, tree2);
    leaves1 = null;
    leaves2 = null;
    IdentityHashMap<INode, INode> parents1 = getParents(tree1);
    IdentityHashMap<INode, INode> parents2 = getParents(tree2);
    IdentityHashMap<INode, Integer> quickFind = new IdentityHashMap<>();
    IdentityHashMap<INode, String> stringMap = new IdentityHashMap<>();
    getHash(tree1, quickFind, stringMap);
    getHash(tree2, quickFind, stringMap);

    matchLeaves(new MatchLeavesParameter(orderedList1, orderedList2, matchedLeaves, resultMap,
        unmatchedNodes1, unmatchedNodes2, onlyOneClassPair, list, stringSimCache, leaves1tmp,
        leaves2tmp, skipList, leafMatcher, parents1, parents2, leavesMap1, leavesMap2,
        directChildrenMap1, directChildrenMap2, null, stringMap, verbose));
    HashSet<ComparePair<INode>> resultSet = new HashSet<>();

    resultSet.addAll(resultMap.values());

    ArrayList<INode> unmatchedNodesOrdered1 = null;
    ArrayList<INode> unmatchedNodesOrdered2 = null;
    unmatchedNodesOrdered1 = getUnmachedNodesInOrder(tree1, unmatchedNodes1);
    unmatchedNodesOrdered2 = getUnmachedNodesInOrder(tree2, unmatchedNodes2);
    assert (unmatchedNodes1.size() == 0);
    assert (unmatchedNodes2.size() == 0);
    matchInnerNodes(new MatchInnerNodesParameter(orderedList1, orderedList2, resultMap,
        unmatchedNodesOrdered1, unmatchedNodesOrdered2, leavesMap1, directChildrenMap1, leavesMap2,
        directChildrenMap2, skipList, resultSet));
    unmatchedNodes1.clear();
    unmatchedNodes2.clear();
    unmatchedNodes1.addAll(unmatchedNodesOrdered1);
    unmatchedNodes2.addAll(unmatchedNodesOrdered2);
    runPostOptimizations(resultMap, unmatchedNodes1, unmatchedNodes2, parents1, parents2);
    resultSet = new HashSet<>();
    resultSet.addAll(resultMap.values());
    resultMap.clear();
    finalResultSet = resultSet;
  }

  private void runPostOptimizations(IdentityHashMap<INode, ComparePair<INode>> resultMap,
      Set<INode> unmatchedNodes1, Set<INode> unmatchedNodes2,
      IdentityHashMap<INode, INode> parents1, IdentityHashMap<INode, INode> parents2) {
    LcsOptMatcherThetaB.advancedLcsMatching(resultMap, unmatchedNodes1, unmatchedNodes2, parents1,
        parents2, tree1, tree2);
    MappingWrapper mappings = new MappingWrapper(resultMap);
    UnmappedLeavesMatcherThetaC.thetaC(unmatchedNodes1, unmatchedNodes2, parents1, parents2,
        mappings);
    InnerNodesMatcherThetaD.thetaD(parents1, parents2, mappings);
    LeafMoveMatcherThetaE.thetaE(parents1, parents2, mappings);
    CrossMoveMatcherThetaF.thetaF(tree1, tree2, resultMap, unmatchedNodes1, unmatchedNodes2,
        parents1, parents2);
  }

  /**
   * Gets the parents.
   *
   * @param tree the tree
   * @return the parents
   */
  public static IdentityHashMap<INode, INode> getParents(INode tree) {
    IdentityHashMap<INode, INode> parentMap = new IdentityHashMap<>();
    LinkedList<INode> workList = new LinkedList<>();
    workList.add(tree);
    while (!workList.isEmpty()) {
      INode node = workList.removeFirst();
      for (INode child : node.getChildrenWrapped()) {
        parentMap.put(child, node);
        workList.add(child);
      }
    }
    return parentMap;
  }

  private void matchLeaves(MatchLeavesParameter parameterObject)
      throws Exception, InterruptedException, ExecutionException {
    ConcurrentHashMap<INode, ConcurrentHashMap<INode, Float>> similarityCache =
        new ConcurrentHashMap<>(SIMILIARITY_CACHE_SIZE / 1000);
    HashMap<Integer, ArrayList<INode>> nodeMap1 = new HashMap<>();
    HashMap<Integer, ArrayList<INode>> nodeMap2 = new HashMap<>();
    createLeavesMap(parameterObject.leaves1tmp, parameterObject.leaves2tmp, nodeMap1, nodeMap2);
    for (Map.Entry<Integer, Integer> tagEntry : parameterObject.list) {
      int tag = tagEntry.getKey();
      final ConcurrentHashMap<INode, ArrayList<MatchingCandidate<INode>>> leafCandidateMap =
          new ConcurrentHashMap<>();
      ArrayList<INode> subLeaves1 = nodeMap1.get(tag);
      ArrayList<INode> subLeaves2 = nodeMap2.get(tag);
      if (subLeaves1 == null) {
        subLeaves1 = new ArrayList<>();
      }
      if (subLeaves2 == null) {
        subLeaves2 = new ArrayList<>();
      }

      INode[] nodes = subLeaves1.toArray(new INode[subLeaves1.size()]);
      subLeaves1.clear();
      AtomicInteger leafSimCounter = new AtomicInteger(0);
      LinkedList<Future<LeafSimResults<INode>>> leafSimResults =
          computeLeafSimilarity(new ComputeLeafSimilarityParameter(parameterObject.orderedList1,
              parameterObject.orderedList2, parameterObject.stringSimCache,
              parameterObject.skipList, parameterObject.leafMatcher, parameterObject.renames,
              parameterObject.verbose, subLeaves2, nodes, leafSimCounter));
      createCandidateMaps(parameterObject.matchedLeaves, parameterObject.verbose, leafCandidateMap,
          leafSimResults);
      while (leafSimCounter.get() > 0) {
        assert (false);
        Thread.yield();
      }
      leafSimResults.clear();
      subLeaves2.clear();
      nodes = null;
      Map<INode, ArrayList<MatchingCandidate<INode>>> tieCandidates = new HashMap<>();
      examineCandidateMaps(parameterObject.matchedLeaves, leafCandidateMap, tieCandidates);
      leafCandidateMap.clear();
      leafSimResults = null;

      @SuppressWarnings("unchecked")
      Map.Entry<INode, ArrayList<MatchingCandidate<INode>>>[] array =
          (Entry<INode, ArrayList<MatchingCandidate<INode>>>[]) tieCandidates.entrySet()
              .toArray(new Map.Entry[tieCandidates.size()]);

      final LinkedList<MatchingCandidate<INode>> candidateList = new LinkedList<>();
      computeSimilarities(parameterObject, similarityCache, leafSimCounter, array, candidateList);
    }
    for (Entry<INode, ConcurrentHashMap<INode, Float>> e : similarityCache.entrySet()) {
      e.getValue().clear();
    }
    similarityCache.clear();
    similarityEntries.set(0);
  }

  private void computeSimilarities(MatchLeavesParameter parameterObject,
      ConcurrentHashMap<INode, ConcurrentHashMap<INode, Float>> similarityCache,
      AtomicInteger leafSimCounter, Map.Entry<INode, ArrayList<MatchingCandidate<INode>>>[] array,
      final LinkedList<MatchingCandidate<INode>> candidateList)
      throws InterruptedException, ExecutionException {
    final long time = System.currentTimeMillis();

    IdentityHashMap<INode, LinkedHashSet<INode>> nodeMappings = new IdentityHashMap<>();
    IdentityHashMap<INode, HashSet<MatchingCandidate<INode>>> nodeMCs = new IdentityHashMap<>();

    aggregateTieEntries(array, nodeMappings, nodeMCs);
    array = null;
    ArrayList<ArrayList<INode>> oldNodeArray = new ArrayList<>();
    ArrayList<ArrayList<INode>> newNodeArray = new ArrayList<>();
    ArrayList<HashSet<MatchingCandidate<INode>>> mcList = new ArrayList<>();

    for (Map.Entry<INode, LinkedHashSet<INode>> entry : nodeMappings.entrySet()) {
      if (entry.getValue().size() > 0) {
        ArrayList<INode> listOld = new ArrayList<>();
        ArrayList<INode> listNew = new ArrayList<>();
        for (INode node : entry.getValue()) {
          if (parameterObject.orderedList1.containsKey(node)) {
            listOld.add(node);
          } else {
            listNew.add(node);
          }
        }
        mcList.add(nodeMCs.get(entry.getKey()));
        oldNodeArray.add(listOld);
        newNodeArray.add(listNew);
        entry.getValue().clear();
      }
    }
    nodeMappings.clear();
    nodeMappings = null;
    AtomicInteger treeDiffCounter = new AtomicInteger(0);
    LinkedList<Future<Set<MatchingCandidate<INode>>>> diffResultList = new LinkedList<>();
    computeTiedLeafSimilarities(new ComputeTiedLeafSimilaritiesParameter(
        parameterObject.orderedList1, parameterObject.orderedList2, parameterObject.matchedLeaves,
        parameterObject.resultMap, parameterObject.unmatchedNodes1, parameterObject.unmatchedNodes2,
        parameterObject.onlyOneClassPair, parameterObject.stringSimCache,
        parameterObject.leafMatcher, parameterObject.parents1, parameterObject.parents2,
        parameterObject.leavesMap1, parameterObject.leavesMap2, parameterObject.directChildrenMap1,
        parameterObject.directChildrenMap2, parameterObject.stringMap, parameterObject.verbose,
        similarityCache, candidateList, oldNodeArray, newNodeArray, mcList, treeDiffCounter,
        diffResultList));
    waitForTiedResults(parameterObject.matchedLeaves, parameterObject.verbose, leafSimCounter,
        candidateList, nodeMCs, oldNodeArray, newNodeArray, mcList, diffResultList);

    long end = System.currentTimeMillis() - time;
    if (treeDifferencer != null) {
      treeDifferencer.getextendedCdTime().addAndGet(end > 0 ? end : 0);
    }


    createLeafPairs(parameterObject.orderedList1, parameterObject.orderedList2,
        parameterObject.matchedLeaves, parameterObject.resultMap, parameterObject.unmatchedNodes1,
        parameterObject.unmatchedNodes2, candidateList);
  }

  private void waitForTiedResults(final TreeSet<MatchingCandidate<INode>> matchedLeaves,
      boolean verbose, AtomicInteger leafSimCounter,
      LinkedList<MatchingCandidate<INode>> candidateList,
      IdentityHashMap<INode, HashSet<MatchingCandidate<INode>>> nodeMCs,
      ArrayList<ArrayList<INode>> oldNodeArray, ArrayList<ArrayList<INode>> newNodeArray,
      ArrayList<HashSet<MatchingCandidate<INode>>> mcList,
      LinkedList<Future<Set<MatchingCandidate<INode>>>> diffResultList)
      throws InterruptedException, ExecutionException {
    for (int i = 0; i < oldNodeArray.size(); i++) {
      HashSet<MatchingCandidate<INode>> subList = mcList.get(i);
      matchedLeaves.removeAll(subList);
    }
    candidateList.addAll(matchedLeaves);
    matchedLeaves.clear();
    for (Future<Set<MatchingCandidate<INode>>> fu : diffResultList) {
      Set<MatchingCandidate<INode>> diffResult;
      try {
        diffResult = fu.get();
        candidateList.addAll(diffResult);
        diffResult.clear();
      } catch (InterruptedException e) {
        e.printStackTrace();
        throw e;
      } catch (ExecutionException e) {
        e.printStackTrace();
        throw e;
      }

    }

    while (leafSimCounter.get() > 0) {
      assert (false);
      Thread.yield();
    }
    for (int j = 0; j < oldNodeArray.size(); j++) {
      newNodeArray.get(j).clear();
      oldNodeArray.get(j).clear();
      mcList.get(j).clear();
    }
    for (Entry<INode, HashSet<MatchingCandidate<INode>>> e : nodeMCs.entrySet()) {
      if (e.getValue() != null) {
        e.getValue().clear();
      }
    }
  }

  private void createLeafPairs(IdentityHashMap<INode, Integer> orderedList1,
      IdentityHashMap<INode, Integer> orderedList2,
      final TreeSet<MatchingCandidate<INode>> matchedLeaves,
      IdentityHashMap<INode, ComparePair<INode>> resultMap, Set<INode> unmatchedNodes1,
      Set<INode> unmatchedNodes2, LinkedList<MatchingCandidate<INode>> candidateList) {
    Collections.sort(candidateList, new PairComparator<INode>(orderedList1, orderedList2));
    while (!candidateList.isEmpty()) {
      final MatchingCandidate<INode> pair = candidateList.pollFirst();
      if (pair.getOldElement() == null) {
        assert (false);
      }
      if (!unmatchedNodes1.contains(pair.getOldElement())) {
        continue;
      }
      if (!unmatchedNodes2.contains(pair.getNewElement())) {
        continue;
      }

      unmatchedNodes1.remove(pair.getOldElement());
      unmatchedNodes2.remove(pair.getNewElement());
      ComparePair<INode> tmp = resultMap.get(pair.getOldElement());
      assert (tmp == null);
      resultMap.put(pair.getOldElement(), pair);
    }
    matchedLeaves.clear();
  }

  private void computeTiedLeafSimilarities(ComputeTiedLeafSimilaritiesParameter parameterObject)
      throws InterruptedException, ExecutionException {
    for (int i = 0; i < parameterObject.oldNodeArray.size(); i++) {
      ArrayList<INode> newNodeList = parameterObject.newNodeArray.get(i);
      ArrayList<INode> oldNodeList = parameterObject.oldNodeArray.get(i);
      HashSet<MatchingCandidate<INode>> subList = parameterObject.mcList.get(i);
      SimilarLeafExaminationRunnable treeDiffRunnable =
          new SimilarLeafExaminationRunnable(new SimilarLeafExaminationRunnableParameter(
              oldNodeList, newNodeList, subList, parameterObject.treeDiffCounter,
              parameterObject.stringSimCache, parameterObject.onlyOneClassPair,
              parameterObject.orderedList1, parameterObject.orderedList2, parameterObject.resultMap,
              parameterObject.similarityCache, similarityEntries, parameterObject.parents1,
              parameterObject.parents2, parameterObject.leavesMap1, parameterObject.leavesMap2,
              labelConfiguration, parameterObject.leafMatcher, parameterObject.directChildrenMap1,
              parameterObject.directChildrenMap2, tree1, tree2, configuration.weightSimilarity,
              configuration.weightPosition, parameterObject.stringMap, parameterObject.verbose,
              numThreads, configuration));
      if (oldNodeList.size() * newNodeList.size() > 10000000) {
        handleLargeMemoryLeafSimilarity(parameterObject, i, newNodeList, oldNodeList, subList,
            treeDiffRunnable);
      } else {
        if (executorService != null) {
          parameterObject.diffResultList.add(executorService.submit(treeDiffRunnable));
        } else {
          FutureTask<Set<MatchingCandidate<INode>>> task = new FutureTask<>(treeDiffRunnable);
          task.run();
          parameterObject.candidateList.addAll(task.get());
          task.get().clear();
        }
      }

    }
  }

  private void handleLargeMemoryLeafSimilarity(ComputeTiedLeafSimilaritiesParameter parameterObject,
      int end, ArrayList<INode> newNodeList, ArrayList<INode> oldNodeList,
      HashSet<MatchingCandidate<INode>> subList, SimilarLeafExaminationRunnable treeDiffRunnable)
      throws InterruptedException, ExecutionException {
    while (parameterObject.treeDiffCounter.get() > 1) {
      Thread.yield();
    }
    for (int j = 0; j < end; j++) {
      HashSet<MatchingCandidate<INode>> subListX = parameterObject.mcList.get(j);
      parameterObject.matchedLeaves.removeAll(subListX);
    }
    for (int j = 0; j < end; j++) {
      parameterObject.newNodeArray.get(j).clear();
      parameterObject.oldNodeArray.get(j).clear();
      parameterObject.mcList.get(j).clear();
    }
    parameterObject.similarityCache.clear();
    similarityEntries.set(SIMILIARITY_CACHE_SIZE);
    parameterObject.matchedLeaves.removeAll(subList);
    if (oldNodeList.size() * newNodeList.size() < 12000000) {

      FutureTask<Set<MatchingCandidate<INode>>> task = new FutureTask<>(treeDiffRunnable);
      task.run();
      parameterObject.diffResultList.add(task);
    } else {
      LinkedList<MatchingCandidate<INode>> tmpCandidates = new LinkedList<>();
      tmpCandidates.addAll(subList);
      subList.clear();
      Collections.sort(tmpCandidates,
          new PairComparator<INode>(parameterObject.orderedList1, parameterObject.orderedList2));
      while (!tmpCandidates.isEmpty()) {
        final MatchingCandidate<INode> pair = tmpCandidates.pollLast();
        if (pair.getOldElement() == null) {
          assert (false);
        }
        if (!parameterObject.unmatchedNodes1.contains(pair.getOldElement())) {
          continue;
        }
        if (!parameterObject.unmatchedNodes2.contains(pair.getNewElement())) {
          continue;
        }

        parameterObject.unmatchedNodes1.remove(pair.getOldElement());
        parameterObject.unmatchedNodes2.remove(pair.getNewElement());

        ComparePair<INode> tmp = parameterObject.resultMap.get(pair.getOldElement());
        assert (tmp == null);
        parameterObject.resultMap.put(pair.getOldElement(), pair);
      }
      tmpCandidates.clear();
      for (Future<Set<MatchingCandidate<INode>>> fu : parameterObject.diffResultList) {
        Set<MatchingCandidate<INode>> diffResult;
        try {
          diffResult = fu.get();
          parameterObject.candidateList.addAll(diffResult);
          diffResult.clear();
        } catch (InterruptedException e) {
          e.printStackTrace();
          throw e;
        } catch (ExecutionException e) {
          e.printStackTrace();
          throw e;
        }

      }
      createComparePair(parameterObject);
    }
    parameterObject.newNodeArray.get(end).clear();
    parameterObject.oldNodeArray.get(end).clear();
    similarityEntries.set(0);
  }

  private void createComparePair(ComputeTiedLeafSimilaritiesParameter parameterObject) {
    Collections.sort(parameterObject.candidateList,
        new PairComparator<INode>(parameterObject.orderedList1, parameterObject.orderedList2));
    while (!parameterObject.candidateList.isEmpty()) {
      final MatchingCandidate<INode> pair = parameterObject.candidateList.pollLast();
      if (pair.getOldElement() == null) {
        assert (false);
      }
      if (!parameterObject.unmatchedNodes1.contains(pair.getOldElement())) {
        continue;
      }
      if (!parameterObject.unmatchedNodes2.contains(pair.getNewElement())) {
        continue;
      }

      parameterObject.unmatchedNodes1.remove(pair.getOldElement());
      parameterObject.unmatchedNodes2.remove(pair.getNewElement());

      ComparePair<INode> tmp = parameterObject.resultMap.get(pair.getOldElement());
      assert (tmp == null);
      parameterObject.resultMap.put(pair.getOldElement(), pair);
    }
    parameterObject.treeDiffCounter.decrementAndGet();
    if (treeDifferencer != null) {
      treeDifferencer.getlargeLeafDiff().incrementAndGet();
    }
  }

  private void aggregateTieEntries(Map.Entry<INode, ArrayList<MatchingCandidate<INode>>>[] array,
      IdentityHashMap<INode, LinkedHashSet<INode>> nodeMappings,
      IdentityHashMap<INode, HashSet<MatchingCandidate<INode>>> nodeMCs) {
    for (Entry<INode, ArrayList<MatchingCandidate<INode>>> tieEntries : array) {
      for (MatchingCandidate<INode> mc : tieEntries.getValue()) {
        if (!nodeMappings.containsKey(mc.getOldElement())
            && !(nodeMappings.containsKey(mc.getNewElement()))) {
          LinkedHashSet<INode> tmp = new LinkedHashSet<>();
          nodeMappings.put(mc.getOldElement(), tmp);
          nodeMappings.put(mc.getNewElement(), tmp);
          tmp.add(mc.getNewElement());
          tmp.add(mc.getOldElement());
          HashSet<MatchingCandidate<INode>> canList = new HashSet<>();
          nodeMCs.put(mc.getOldElement(), canList);
          nodeMCs.put(mc.getNewElement(), canList);
          canList.add(mc);
        } else if (nodeMappings.containsKey(mc.getOldElement())
            && !nodeMappings.containsKey(mc.getNewElement())) {
          LinkedHashSet<INode> tmp = nodeMappings.get(mc.getOldElement());
          assert (tmp != null);
          tmp.add(mc.getNewElement());
          nodeMappings.put(mc.getNewElement(), tmp);
          assert (nodeMCs.get(mc.getOldElement()) != null);
          nodeMCs.get(mc.getOldElement()).add(mc);
          nodeMCs.put(mc.getNewElement(), nodeMCs.get(mc.getOldElement()));
        } else if (!nodeMappings.containsKey(mc.getOldElement())
            && nodeMappings.containsKey(mc.getNewElement())) {
          LinkedHashSet<INode> tmp = nodeMappings.get(mc.getNewElement());
          assert (tmp != null);
          tmp.add(mc.getOldElement());
          nodeMappings.put(mc.getOldElement(), tmp);
          assert (nodeMCs.get(mc.getNewElement()) != null);
          nodeMCs.get(mc.getNewElement()).add(mc);
          nodeMCs.put(mc.getOldElement(), nodeMCs.get(mc.getNewElement()));
        } else {
          LinkedHashSet<INode> tmpOld = nodeMappings.get(mc.getOldElement());
          LinkedHashSet<INode> tmpNew = nodeMappings.get(mc.getNewElement());
          assert (tmpOld != null && tmpNew != null);
          if (tmpOld != tmpNew) {
            tmpOld.addAll(tmpNew);

            for (INode node : tmpNew) {
              nodeMappings.put(node, tmpOld);
            }
            assert (nodeMCs.get(mc.getOldElement()) != null);
            HashSet<MatchingCandidate<INode>> oldList = nodeMCs.get(mc.getOldElement());
            nodeMCs.get(mc.getOldElement()).addAll(nodeMCs.get(mc.getNewElement()));
            for (INode node : tmpNew) {
              nodeMCs.put(node, oldList);
            }
            nodeMCs.get(mc.getOldElement()).add(mc);
          } else {
            assert (nodeMCs.get(mc.getOldElement()) == nodeMCs.get(mc.getNewElement()));
            nodeMCs.get(mc.getOldElement()).add(mc);
          }
        }
      }
      if (tieEntries.getValue() != null) {
        tieEntries.getValue().clear();
      }

    }
  }

  private void examineCandidateMaps(final TreeSet<MatchingCandidate<INode>> matchedLeaves,
      final ConcurrentHashMap<INode, ArrayList<MatchingCandidate<INode>>> leafCandidateMap,
      Map<INode, ArrayList<MatchingCandidate<INode>>> tieCandidates) {
    for (final Map.Entry<INode, ArrayList<MatchingCandidate<INode>>> entry : leafCandidateMap
        .entrySet()) {

      Iterator<MatchingCandidate<INode>> cit = entry.getValue().iterator();
      float maxSim2 = Float.MIN_VALUE;
      while (cit.hasNext()) {
        final MatchingCandidate<INode> next = cit.next();
        if (next.getValue() > maxSim2) {
          maxSim2 = next.getValue();
        }
      }
      cit = entry.getValue().iterator();
      while (cit.hasNext()) {
        final MatchingCandidate<INode> next = cit.next();
        if (next.getValue() < maxSim2) {
          cit.remove();
          matchedLeaves.remove(next);
        }
      }

      if (entry.getValue().size() > 1) {
        tieCandidates.put(entry.getKey(), entry.getValue());
      } else {
        entry.getValue().clear();
      }

    }
  }

  private void createCandidateMaps(final TreeSet<MatchingCandidate<INode>> matchedLeaves,
      boolean verbose,
      final ConcurrentHashMap<INode, ArrayList<MatchingCandidate<INode>>> leafCandidateMap,
      LinkedList<Future<LeafSimResults<INode>>> leafSimResults) throws Exception {
    for (Future<LeafSimResults<INode>> fu : leafSimResults) {

      LeafSimResults<INode> leafSimRes;
      try {

        leafSimRes = fu.get();
        matchedLeaves.addAll(leafSimRes.submatchedLeaves);
        leafSimRes.submatchedLeaves.clear();
        final Set<Entry<INode, ArrayList<MatchingCandidate<INode>>>> entrySet =
            leafSimRes.subleafCandidateMap.entrySet();
        for (final Map.Entry<INode, ArrayList<MatchingCandidate<INode>>> entry : entrySet) {
          ArrayList<MatchingCandidate<INode>> myList = new ArrayList<>();
          leafCandidateMap.putIfAbsent(entry.getKey(), myList);
          leafCandidateMap.get(entry.getKey()).addAll(entry.getValue());
        }
        leafSimRes.subleafCandidateMap.clear();
        leafSimRes.submatchedLeaves.clear();

      } catch (InterruptedException | ExecutionException e) {
        e.printStackTrace();
        throw e;
      }

    }
  }

  private LinkedList<Future<LeafSimResults<INode>>> computeLeafSimilarity(
      ComputeLeafSimilarityParameter parameterObject) {
    int start = 0;
    int step = Math.max(parameterObject.nodes.length / 16, 1);
    LinkedList<Future<LeafSimResults<INode>>> leafSimResults = new LinkedList<>();
    while (start + step < parameterObject.nodes.length) {
      LeafSimilarityRunnable runnable =
          new LeafSimilarityRunnable(new LeafSimilarityRunnableParameter(
              parameterObject.stringSimCache, parameterObject.nodes, parameterObject.subLeaves2,
              parameterObject.leafSimCounter, start, start + step, parameterObject.orderedList1,
              parameterObject.orderedList2, parameterObject.skipList, parameterObject.leafMatcher,
              labelConfiguration, parameterObject.renames, parameterObject.verbose));
      if (executorService != null) {
        leafSimResults.add(executorService.submit(runnable));
      } else {
        FutureTask<LeafSimResults<INode>> task = new FutureTask<>(runnable);
        task.run();
        leafSimResults.add(task);
      }
      start += step;
    }
    LeafSimilarityRunnable runnable = new LeafSimilarityRunnable(
        new LeafSimilarityRunnableParameter(parameterObject.stringSimCache, parameterObject.nodes,
            parameterObject.subLeaves2, parameterObject.leafSimCounter, start,
            parameterObject.nodes.length, parameterObject.orderedList1,
            parameterObject.orderedList2, parameterObject.skipList, parameterObject.leafMatcher,
            labelConfiguration, parameterObject.renames, parameterObject.verbose));
    if (executorService != null) {
      leafSimResults.add(executorService.submit(runnable));
    } else {
      FutureTask<LeafSimResults<INode>> task = new FutureTask<>(runnable);
      task.run();
      leafSimResults.add(task);
    }
    return leafSimResults;
  }

  private void createLeavesMap(ArrayList<INode> leaves1tmp, ArrayList<INode> leaves2tmp,
      HashMap<Integer, ArrayList<INode>> nodeMap1, HashMap<Integer, ArrayList<INode>> nodeMap2) {
    Iterator<INode> it = leaves1tmp.iterator();
    while (it.hasNext()) {
      INode node = it.next();
      ArrayList<INode> subLeaves1 = nodeMap1.get(node.getTypeWrapped());
      if (subLeaves1 == null) {
        subLeaves1 = new ArrayList<>();
        nodeMap1.put(node.getTypeWrapped(), subLeaves1);
      }
      subLeaves1.add(node);
    }
    it = leaves2tmp.iterator();
    while (it.hasNext()) {
      INode node = it.next();
      ArrayList<INode> subLeaves2 = nodeMap2.get(node.getTypeWrapped());
      if (subLeaves2 == null) {
        subLeaves2 = new ArrayList<>();
        nodeMap2.put(node.getTypeWrapped(), subLeaves2);
      }
      subLeaves2.add(node);
    }
    leaves1tmp.clear();
    leaves2tmp.clear();
  }

  private void matchInnerNodes(MatchInnerNodesParameter parameterObject)
      throws InterruptedException, ExecutionException {
    InnerNodeMatcher innerMatcher2 = new InnerNodeMatcher(configuration, labelConfiguration,
        parameterObject.leavesMap1, parameterObject.leavesMap2, parameterObject.directChildrenMap1,
        parameterObject.directChildrenMap2, parameterObject.resultSet);
    if (parameterObject.unmatchedNodesOrdered1 == null) {
      return;
    }
    final long time = System.currentTimeMillis();

    AtomicInteger innerMatchCounter = new AtomicInteger(0);
    int start = 0;
    int step = Math.max(parameterObject.unmatchedNodesOrdered1.size() / 16, 1);
    LinkedList<Future<ArrayList<MatchingCandidate<INode>>>> results = new LinkedList<>();
    while (start + step < parameterObject.unmatchedNodesOrdered1.size()) {
      InnerMatcherMtDiffRunnable runnable = new InnerMatcherMtDiffRunnable(
          parameterObject.unmatchedNodesOrdered1, parameterObject.unmatchedNodesOrdered2,
          innerMatchCounter, start, start + step, innerMatcher2, configuration.weightValue,
          configuration.weightChildren, configuration.innerNodeThreshold);
      if (executorService != null) {
        results.add(executorService.submit(runnable));
      } else {
        FutureTask<ArrayList<MatchingCandidate<INode>>> task = new FutureTask<>(runnable);
        task.run();
        results.add(task);
      }
      start += step;
    }
    InnerMatcherMtDiffRunnable runnable = new InnerMatcherMtDiffRunnable(
        parameterObject.unmatchedNodesOrdered1, parameterObject.unmatchedNodesOrdered2,
        innerMatchCounter, start, parameterObject.unmatchedNodesOrdered1.size(), innerMatcher2,
        configuration.weightValue, configuration.weightChildren, configuration.innerNodeThreshold);
    if (executorService != null) {
      results.add(executorService.submit(runnable));
    } else {
      FutureTask<ArrayList<MatchingCandidate<INode>>> task = new FutureTask<>(runnable);
      task.run();
      results.add(task);
    }
    LinkedList<MatchingCandidate<INode>> candidateList = new LinkedList<>();
    for (Future<ArrayList<MatchingCandidate<INode>>> fu : results) {
      ArrayList<MatchingCandidate<INode>> candidates;
      try {
        candidates = fu.get();
        candidateList.addAll(candidates);
        candidates.clear();
      } catch (InterruptedException e) {
        e.printStackTrace();
        throw e;
      } catch (ExecutionException e) {
        e.printStackTrace();
        throw e;
      }

    }

    while (innerMatchCounter.get() > 0) {
      assert (false);
      Thread.yield();
    }
    Collections.sort(candidateList,
        new PairComparator<INode>(parameterObject.orderedList1, parameterObject.orderedList2));
    while ((parameterObject.unmatchedNodesOrdered1 != null
        && parameterObject.unmatchedNodesOrdered2 != null) && candidateList.size() > 0) {
      MatchingCandidate<INode> can = candidateList.pollLast();
      if (parameterObject.unmatchedNodesOrdered1.contains(can.getOldElement())
          && parameterObject.unmatchedNodesOrdered2.contains(can.getNewElement())) {
        parameterObject.unmatchedNodesOrdered1.remove(can.getOldElement());
        parameterObject.unmatchedNodesOrdered2.remove(can.getNewElement());
        if (!parameterObject.skipList.contains(can.getOldElement())
            && !parameterObject.skipList.contains(can.getNewElement())) {
          parameterObject.resultMap.put(can.getOldElement(),
              new ComparePair<INode>(can.getOldElement(), can.getNewElement()));
        }
      }
    }
    final long newtime = System.currentTimeMillis() - time;
    if (treeDifferencer != null) {
      treeDifferencer.getextendedInnerMatchingTime().addAndGet(newtime > 0 ? newtime : 0);
    }
  }

  /**
   * Gets the node stream.
   *
   * @param root the root
   * @return the node stream
   */
  public static List<INode> getNodeStream(INode root) {
    LinkedList<INode> nodes = new LinkedList<>();
    LinkedList<INode> workList = new LinkedList<>();
    workList.add(root);
    while (!workList.isEmpty()) {
      INode node = workList.removeFirst();
      nodes.add(node);
      for (int i = node.getChildrenWrapped().size() - 1; i >= 0; i--) {
        workList.addFirst(node.getChildrenWrapped().get(i));
      }
    }
    return nodes;
  }

}
