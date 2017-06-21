package de.fau.cs.inf2.mtdiff.parameters;

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.ComparePair;

import de.fau.cs.inf2.mtdiff.intern.LMatcher;
import de.fau.cs.inf2.mtdiff.intern.MatchingCandidate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("PMD.TooManyFields")
public class MatchLeavesParameter {
  public IdentityHashMap<INode, Integer> orderedList1;
  public IdentityHashMap<INode, Integer> orderedList2;
  public TreeSet<MatchingCandidate<INode>> matchedLeaves;
  public IdentityHashMap<INode, ComparePair<INode>> resultMap;
  public Set<INode> unmatchedNodes1;
  public Set<INode> unmatchedNodes2;
  public boolean onlyOneClassPair;
  public List<Entry<Integer, Integer>> list;
  public ConcurrentHashMap<String, Float> stringSimCache;
  public ArrayList<INode> leaves1tmp;
  public ArrayList<INode> leaves2tmp;
  public HashSet<INode> skipList;
  public LMatcher leafMatcher;
  public Map<INode, INode> parents1;
  public Map<INode, INode> parents2;
  public Map<INode, ArrayList<INode>> leavesMap1;
  public Map<INode, ArrayList<INode>> leavesMap2;
  public Map<INode, ArrayList<INode>> directChildrenMap1;
  public Map<INode, ArrayList<INode>> directChildrenMap2;
  public HashMap<String, String> renames;
  public IdentityHashMap<INode, String> stringMap;
  public boolean verbose;

  /**
   * Instantiates a new match leaves parameter.
   *
   * @param orderedList1 the ordered list 1
   * @param orderedList2 the ordered list 2
   * @param matchedLeaves the matched leaves
   * @param resultMap the result map
   * @param unmatchedNodes1 the unmatched nodes 1
   * @param unmatchedNodes2 the unmatched nodes 2
   * @param onlyOneClassPair the only one class pair
   * @param list the list
   * @param stringSimCache the string sim cache
   * @param leaves1tmp the leaves 1 tmp
   * @param leaves2tmp the leaves 2 tmp
   * @param skipList the skip list
   * @param leafMatcher the leaf matcher
   * @param parents1 the parents 1
   * @param parents2 the parents 2
   * @param leavesMap1 the leaves map 1
   * @param leavesMap2 the leaves map 2
   * @param directChildrenMap1 the direct children map 1
   * @param directChildrenMap2 the direct children map 2
   * @param renames the renames
   * @param stringMap the string map
   * @param verbose the verbose
   */
  @SuppressWarnings("PMD.ExcessiveParameterList")
  public MatchLeavesParameter(IdentityHashMap<INode, Integer> orderedList1,
      IdentityHashMap<INode, Integer> orderedList2, TreeSet<MatchingCandidate<INode>> matchedLeaves,
      IdentityHashMap<INode, ComparePair<INode>> resultMap, Set<INode> unmatchedNodes1,
      Set<INode> unmatchedNodes2, boolean onlyOneClassPair, List<Entry<Integer, Integer>> list,
      ConcurrentHashMap<String, Float> stringSimCache, ArrayList<INode> leaves1tmp,
      ArrayList<INode> leaves2tmp, HashSet<INode> skipList, LMatcher leafMatcher,
      Map<INode, INode> parents1, Map<INode, INode> parents2,
      Map<INode, ArrayList<INode>> leavesMap1, Map<INode, ArrayList<INode>> leavesMap2,
      Map<INode, ArrayList<INode>> directChildrenMap1,
      Map<INode, ArrayList<INode>> directChildrenMap2, HashMap<String, String> renames,
      IdentityHashMap<INode, String> stringMap, boolean verbose) {
    this.orderedList1 = orderedList1;
    this.orderedList2 = orderedList2;
    this.matchedLeaves = matchedLeaves;
    this.resultMap = resultMap;
    this.unmatchedNodes1 = unmatchedNodes1;
    this.unmatchedNodes2 = unmatchedNodes2;
    this.onlyOneClassPair = onlyOneClassPair;
    this.list = list;
    this.stringSimCache = stringSimCache;
    this.leaves1tmp = leaves1tmp;
    this.leaves2tmp = leaves2tmp;
    this.skipList = skipList;
    this.leafMatcher = leafMatcher;
    this.parents1 = parents1;
    this.parents2 = parents2;
    this.leavesMap1 = leavesMap1;
    this.leavesMap2 = leavesMap2;
    this.directChildrenMap1 = directChildrenMap1;
    this.directChildrenMap2 = directChildrenMap2;
    this.renames = renames;
    this.stringMap = stringMap;
    this.verbose = verbose;
  }
}