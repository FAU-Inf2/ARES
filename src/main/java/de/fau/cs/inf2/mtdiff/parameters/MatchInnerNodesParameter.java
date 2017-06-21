package de.fau.cs.inf2.mtdiff.parameters;

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.ComparePair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;

public class MatchInnerNodesParameter {
  public IdentityHashMap<INode, Integer> orderedList1;
  public IdentityHashMap<INode, Integer> orderedList2;
  public IdentityHashMap<INode, ComparePair<INode>> resultMap;
  public ArrayList<INode> unmatchedNodesOrdered1;
  public ArrayList<INode> unmatchedNodesOrdered2;
  public IdentityHashMap<INode, ArrayList<INode>> leavesMap1;
  public IdentityHashMap<INode, ArrayList<INode>> directChildrenMap1;
  public IdentityHashMap<INode, ArrayList<INode>> leavesMap2;
  public IdentityHashMap<INode, ArrayList<INode>> directChildrenMap2;
  public HashSet<INode> skipList;
  public HashSet<ComparePair<INode>> resultSet;

  /**
   * Instantiates a new match inner nodes parameter.
   *
   * @param orderedList1 the ordered list 1
   * @param orderedList2 the ordered list 2
   * @param resultMap the result map
   * @param unmatchedNodesOrdered1 the unmatched nodes ordered 1
   * @param unmatchedNodesOrdered2 the unmatched nodes ordered 2
   * @param leavesMap1 the leaves map 1
   * @param directChildrenMap1 the direct children map 1
   * @param leavesMap2 the leaves map 2
   * @param directChildrenMap2 the direct children map 2
   * @param skipList the skip list
   * @param resultSet the result set
   */
  @SuppressWarnings("PMD.ExcessiveParameterList")
  public MatchInnerNodesParameter(IdentityHashMap<INode, Integer> orderedList1,
      IdentityHashMap<INode, Integer> orderedList2,
      IdentityHashMap<INode, ComparePair<INode>> resultMap, ArrayList<INode> unmatchedNodesOrdered1,
      ArrayList<INode> unmatchedNodesOrdered2, IdentityHashMap<INode, ArrayList<INode>> leavesMap1,
      IdentityHashMap<INode, ArrayList<INode>> directChildrenMap1,
      IdentityHashMap<INode, ArrayList<INode>> leavesMap2,
      IdentityHashMap<INode, ArrayList<INode>> directChildrenMap2, HashSet<INode> skipList,
      HashSet<ComparePair<INode>> resultSet) {
    this.orderedList1 = orderedList1;
    this.orderedList2 = orderedList2;
    this.resultMap = resultMap;
    this.unmatchedNodesOrdered1 = unmatchedNodesOrdered1;
    this.unmatchedNodesOrdered2 = unmatchedNodesOrdered2;
    this.leavesMap1 = leavesMap1;
    this.directChildrenMap1 = directChildrenMap1;
    this.leavesMap2 = leavesMap2;
    this.directChildrenMap2 = directChildrenMap2;
    this.skipList = skipList;
    this.resultSet = resultSet;
  }
}