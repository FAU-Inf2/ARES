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

package de.fau.cs.inf2.mtdiff.intern;

import de.fau.cs.inf2.cas.common.bast.diff.LabelConfiguration;
import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.string.NGramCalculator;

import de.fau.cs.inf2.mtdiff.parameters.LeafSimilarityRunnableParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.atomic.AtomicInteger;

public class LeafSimilarityRunnable implements Callable<LeafSimResults<INode>> {

  private ConcurrentHashMap<String, Float> stringSimCache;
  private INode[] subLeaves1;
  private ArrayList<INode> subLeaves2;
  private AtomicInteger counter = null;
  private int start;
  private int end;
  private IdentityHashMap<INode, Integer> orderedList1;
  private IdentityHashMap<INode, Integer> orderedList2;
  private NGramCalculator stringSim = new NGramCalculator(2, 1000, 10000);
  private HashSet<INode> skipList;
  private boolean verbose;
  private LMatcher lmatcher;
  private LabelConfiguration labelConfiguration;
  private HashMap<String, String> renames;

  /**
   * Instantiates a new leaf similarity runnable.
   *
   * @param parameterObject the parameter object
   */
  public LeafSimilarityRunnable(LeafSimilarityRunnableParameter parameterObject) {
    super();
    this.stringSimCache = parameterObject.stringSimCache;
    this.subLeaves1 = parameterObject.subLeaves1s;
    this.subLeaves2 = parameterObject.subLeaves2;
    this.counter = parameterObject.counter;
    this.start = parameterObject.start;
    this.end = parameterObject.end;
    parameterObject.counter.incrementAndGet();
    this.orderedList1 = parameterObject.orderedList1;
    this.orderedList2 = parameterObject.orderedList2;
    this.skipList = parameterObject.skipList;
    this.verbose = parameterObject.verbose;
    this.lmatcher = parameterObject.lmatcher;
    this.labelConfiguration = parameterObject.labelConfiguration;
    this.renames = parameterObject.renames;
  }


  /**
   * Call.
   *
   * @return the leaf sim results
   * @throws Exception the exception
   */
  @Override
  public LeafSimResults<INode> call() throws Exception {
    try {
      ConcurrentHashMap<INode, ArrayList<MatchingCandidate<INode>>> leafCandidateMap =
          new ConcurrentHashMap<>();
      ConcurrentSkipListSet<MatchingCandidate<INode>> matchedLeaves =
          new ConcurrentSkipListSet<>(new PairComparator<INode>(orderedList1, orderedList2));
      ConcurrentHashMap<String, INode> basicTypeCache = new ConcurrentHashMap<>();
      ConcurrentHashMap<String, INode> basicNameCache = new ConcurrentHashMap<>();
      ConcurrentHashMap<String, INode> basicTypeQualifierCache = new ConcurrentHashMap<>();

      for (int i = start; i < end; i++) {
        INode firstNode = subLeaves1[i];
        if (skipList.contains(firstNode)) {
          continue;
        }
        boolean nextIteration = false;
        nextIteration = lookForCachedValues(leafCandidateMap, matchedLeaves, basicTypeCache,
            basicNameCache, basicTypeQualifierCache, i, firstNode, nextIteration);
        if (nextIteration) {
          continue;
        }
        computeLeafSimilarities(matchedLeaves, stringSimCache, leafCandidateMap, subLeaves2,
            stringSim, firstNode, skipList, lmatcher);
        updateCaches(basicTypeCache, basicNameCache, basicTypeQualifierCache, firstNode);
      }
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

      }
      basicTypeCache.clear();
      basicNameCache.clear();
      basicTypeQualifierCache.clear();
      counter.decrementAndGet();
      stringSim.clear();
      return new LeafSimResults<INode>(leafCandidateMap, matchedLeaves);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

  }


  private boolean lookForCachedValues(
      ConcurrentHashMap<INode, ArrayList<MatchingCandidate<INode>>> leafCandidateMap,
      ConcurrentSkipListSet<MatchingCandidate<INode>> matchedLeaves,
      ConcurrentHashMap<String, INode> basicTypeCache,
      ConcurrentHashMap<String, INode> basicNameCache,
      ConcurrentHashMap<String, INode> basicTypeQualifierCache, int iteration, INode firstNode,
      boolean nextIteration) {
    if (firstNode.getTypeWrapped() == labelConfiguration.basicTypeLabel) {
      nextIteration = computeSimilarityForSpecificTypes(leafCandidateMap, matchedLeaves,
          basicTypeCache, iteration, firstNode, nextIteration);
    } else if (firstNode.getTypeWrapped() == labelConfiguration.identifierLabel) {
      nextIteration = computeSimilarityForSpecificTypes(leafCandidateMap, matchedLeaves,
          basicNameCache, iteration, firstNode, nextIteration);
    } else if (firstNode.getTypeWrapped() == labelConfiguration.qualifierLabel) {
      nextIteration = computeSimilarityForSpecificTypes(leafCandidateMap, matchedLeaves,
          basicTypeQualifierCache, iteration, firstNode, nextIteration);
    }
    return nextIteration;
  }


  private void updateCaches(ConcurrentHashMap<String, INode> basicTypeCache,
      ConcurrentHashMap<String, INode> basicNameCache,
      ConcurrentHashMap<String, INode> basicTypeQualifierCache, INode firstNode) {
    if (firstNode.getTypeWrapped() == labelConfiguration.basicTypeLabel) {
      basicTypeCache.putIfAbsent(firstNode.getLabel(), firstNode);
    } else if (firstNode.getTypeWrapped() == labelConfiguration.identifierLabel) {
      basicNameCache.putIfAbsent(firstNode.getLabel(), firstNode);
    } else if (firstNode.getTypeWrapped() == labelConfiguration.qualifierLabel) {
      basicTypeQualifierCache.putIfAbsent(firstNode.getLabel(), firstNode);
    }
  }


  private boolean computeSimilarityForSpecificTypes(
      ConcurrentHashMap<INode, ArrayList<MatchingCandidate<INode>>> leafCandidateMap,
      ConcurrentSkipListSet<MatchingCandidate<INode>> matchedLeaves,
      ConcurrentHashMap<String, INode> basicTypeCache, int iteration, INode firstNode,
      boolean nextIteration) {
    if (basicTypeCache.containsKey(firstNode.getLabel())) {
      INode oldNode = basicTypeCache.get(firstNode.getLabel());
      ArrayList<MatchingCandidate<INode>> oldList = leafCandidateMap.get(oldNode);
      ArrayList<MatchingCandidate<INode>> newList = new ArrayList<>();
      if (oldList != null) {
        for (MatchingCandidate<INode> mc : oldList) {
          MatchingCandidate<INode> newMc =
              new MatchingCandidate<INode>(firstNode, mc.getNewElement(), mc.getValue());
          newList.add(newMc);
          matchedLeaves.add(newMc);
          ArrayList<MatchingCandidate<INode>> newToOldList =
              leafCandidateMap.get(mc.getNewElement());
          newToOldList.add(newMc);
        }
        leafCandidateMap.put(firstNode, newList);
        nextIteration = true;
      }
    }
    return nextIteration;
  }

  private void computeLeafSimilarities(
      final ConcurrentSkipListSet<MatchingCandidate<INode>> matchedLeaves,
      ConcurrentHashMap<String, Float> stringSimCache,
      final ConcurrentHashMap<INode, ArrayList<MatchingCandidate<INode>>> leafCandidateMap,
      ArrayList<INode> subLeaves2, NGramCalculator stringSim, final INode firstNode,
      HashSet<INode> skipList, LMatcher lmatcher) {
    float maxSim = Float.MIN_VALUE;
    for (final INode secondNode : subLeaves2) {
      if (skipList.contains(secondNode)) {
        continue;
      }
      float sim = Float.MIN_VALUE;
      if (renames != null
          && labelConfiguration.labelsForStringCompare.contains(firstNode.getTypeWrapped())) {
        if (renames.get(firstNode.getLabel()) != null) {
          if (renames.get(firstNode.getLabel()).equals(secondNode.getLabel())) {
            sim = 1.0f;
          }
        }

      } else if (renames != null) {
        continue;
      }
      if (sim == Float.MIN_VALUE) {
        if (secondNode.getTypeWrapped() == labelConfiguration.identifierLabel) {
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
        } else if (secondNode.getTypeWrapped() == labelConfiguration.basicTypeLabel
            || secondNode.getTypeWrapped() == labelConfiguration.qualifierLabel) {
          if (firstNode.getLabel().equals(secondNode.getLabel())) {
            sim = 1.0f;
          } else {
            sim = 0.0f;
          }
        } else {
          sim = lmatcher.leavesSimilarity(firstNode, secondNode);

        }
      }
      if (sim >= maxSim && sim > 0.0f) {
        if (lmatcher.match(firstNode, secondNode, sim)) {
          final MatchingCandidate<INode> candidate =
              new MatchingCandidate<INode>(firstNode, secondNode, sim);
          if (sim > maxSim) {
            maxSim = sim;
          }
          matchedLeaves.add(candidate);
          insertCandidate(leafCandidateMap, firstNode, candidate, matchedLeaves);
          insertCandidate(leafCandidateMap, secondNode, candidate, matchedLeaves);
        }

      }

    }
  }

  /**
   * Inserts a {@see MatchingCandidate} into a specified mapping.
   * 
   * @param candidateMapping The mapping of a node to all possible candidates.
   * @param key The INode to be inserted as a key
   * @param candidate The candidate to be inserted as a value
   */
  private void insertCandidate(
      final Map<INode, ArrayList<MatchingCandidate<INode>>> candidateMapping, final INode key,
      final MatchingCandidate<INode> candidate,
      ConcurrentSkipListSet<MatchingCandidate<INode>> matchedLeaves) {
    synchronized (candidateMapping) {
      ArrayList<MatchingCandidate<INode>> candidateSet = candidateMapping.get(key);

      if (candidateSet == null) {
        candidateSet = new ArrayList<MatchingCandidate<INode>>(10);
        candidateMapping.put(key, candidateSet);
        candidateSet.add(candidate);
      } else {
        candidateSet.add(candidate);
        Iterator<MatchingCandidate<INode>> cit = candidateSet.iterator();
        float maxSim2 = Float.MIN_VALUE;
        while (cit.hasNext()) {
          final MatchingCandidate<INode> next = cit.next();
          if (next.getValue() > maxSim2) {
            maxSim2 = next.getValue();
          }
        }
        cit = candidateSet.iterator();
        while (cit.hasNext()) {
          final MatchingCandidate<INode> next = cit.next();
          if (next.getValue() < maxSim2) {
            cit.remove();
            matchedLeaves.remove(next);
          }
        }
      }
    }
  }

}
