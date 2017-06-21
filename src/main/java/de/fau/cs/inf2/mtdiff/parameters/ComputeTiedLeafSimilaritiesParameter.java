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

import de.fau.cs.inf2.mtdiff.intern.LMatcher;
import de.fau.cs.inf2.mtdiff.intern.MatchingCandidate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressWarnings("PMD.TooManyFields")
public class ComputeTiedLeafSimilaritiesParameter {
  public IdentityHashMap<INode, Integer> orderedList1;
  public IdentityHashMap<INode, Integer> orderedList2;
  public TreeSet<MatchingCandidate<INode>> matchedLeaves;
  public IdentityHashMap<INode, ComparePair<INode>> resultMap;
  public Set<INode> unmatchedNodes1;
  public Set<INode> unmatchedNodes2;
  public boolean onlyOneClassPair;
  public ConcurrentHashMap<String, Float> stringSimCache;
  public LMatcher leafMatcher;
  public Map<INode, INode> parents1;
  public Map<INode, INode> parents2;
  public Map<INode, ArrayList<INode>> leavesMap1;
  public Map<INode, ArrayList<INode>> leavesMap2;
  public Map<INode, ArrayList<INode>> directChildrenMap1;
  public Map<INode, ArrayList<INode>> directChildrenMap2;
  public IdentityHashMap<INode, String> stringMap;
  public boolean verbose;
  public ConcurrentHashMap<INode, ConcurrentHashMap<INode, Float>> similarityCache;
  public LinkedList<MatchingCandidate<INode>> candidateList;
  public ArrayList<ArrayList<INode>> oldNodeArray;
  public ArrayList<ArrayList<INode>> newNodeArray;
  public ArrayList<HashSet<MatchingCandidate<INode>>> mcList;
  public AtomicInteger treeDiffCounter;
  public LinkedList<Future<Set<MatchingCandidate<INode>>>> diffResultList;

  /**
   * Instantiates a new compute tied leaf similarities parameter.
   *
   * @param orderedList1 the ordered list 1
   * @param orderedList2 the ordered list 2
   * @param matchedLeaves the matched leaves
   * @param resultMap the result map
   * @param unmatchedNodes1 the unmatched nodes 1
   * @param unmatchedNodes2 the unmatched nodes 2
   * @param onlyOneClassPair the only one class pair
   * @param stringSimCache the string sim cache
   * @param leafMatcher the leaf matcher
   * @param parents1 the parents 1
   * @param parents2 the parents 2
   * @param leavesMap1 the leaves map 1
   * @param leavesMap2 the leaves map 2
   * @param directChildrenMap1 the direct children map 1
   * @param directChildrenMap2 the direct children map 2
   * @param stringMap the string map
   * @param verbose the verbose
   * @param similarityCache the similarity cache
   * @param candidateList the candidate list
   * @param oldNodeArray the old node array
   * @param newNodeArray the new node array
   * @param mcList the mc list
   * @param treeDiffCounter the tree diff counter
   * @param diffResultList the diff result list
   */
  @SuppressWarnings("PMD.ExcessiveParameterList")
  public ComputeTiedLeafSimilaritiesParameter(IdentityHashMap<INode, Integer> orderedList1,
      IdentityHashMap<INode, Integer> orderedList2, TreeSet<MatchingCandidate<INode>> matchedLeaves,
      IdentityHashMap<INode, ComparePair<INode>> resultMap, Set<INode> unmatchedNodes1,
      Set<INode> unmatchedNodes2, boolean onlyOneClassPair,
      ConcurrentHashMap<String, Float> stringSimCache, LMatcher leafMatcher,
      Map<INode, INode> parents1, Map<INode, INode> parents2,
      Map<INode, ArrayList<INode>> leavesMap1, Map<INode, ArrayList<INode>> leavesMap2,
      Map<INode, ArrayList<INode>> directChildrenMap1,
      Map<INode, ArrayList<INode>> directChildrenMap2, IdentityHashMap<INode, String> stringMap,
      boolean verbose, ConcurrentHashMap<INode, ConcurrentHashMap<INode, Float>> similarityCache,
      LinkedList<MatchingCandidate<INode>> candidateList, ArrayList<ArrayList<INode>> oldNodeArray,
      ArrayList<ArrayList<INode>> newNodeArray, ArrayList<HashSet<MatchingCandidate<INode>>> mcList,
      AtomicInteger treeDiffCounter,
      LinkedList<Future<Set<MatchingCandidate<INode>>>> diffResultList) {
    this.orderedList1 = orderedList1;
    this.orderedList2 = orderedList2;
    this.matchedLeaves = matchedLeaves;
    this.resultMap = resultMap;
    this.unmatchedNodes1 = unmatchedNodes1;
    this.unmatchedNodes2 = unmatchedNodes2;
    this.onlyOneClassPair = onlyOneClassPair;
    this.stringSimCache = stringSimCache;
    this.leafMatcher = leafMatcher;
    this.parents1 = parents1;
    this.parents2 = parents2;
    this.leavesMap1 = leavesMap1;
    this.leavesMap2 = leavesMap2;
    this.directChildrenMap1 = directChildrenMap1;
    this.directChildrenMap2 = directChildrenMap2;
    this.stringMap = stringMap;
    this.verbose = verbose;
    this.similarityCache = similarityCache;
    this.candidateList = candidateList;
    this.oldNodeArray = oldNodeArray;
    this.newNodeArray = newNodeArray;
    this.mcList = mcList;
    this.treeDiffCounter = treeDiffCounter;
    this.diffResultList = diffResultList;
  }
}