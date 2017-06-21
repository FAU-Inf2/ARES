package de.fau.cs.inf2.mtdiff.parameters;

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.mtdiff.intern.LMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ComputeLeafSimilarityParameter {
  public IdentityHashMap<INode, Integer> orderedList1;
  public IdentityHashMap<INode, Integer> orderedList2;
  public ConcurrentHashMap<String, Float> stringSimCache;
  public HashSet<INode> skipList;
  public LMatcher leafMatcher;
  public HashMap<String, String> renames;
  public boolean verbose;
  public ArrayList<INode> subLeaves2;
  public INode[] nodes;
  public AtomicInteger leafSimCounter;

  /**
   * Instantiates a new compute leaf similarity parameter.
   *
   * @param orderedList1 the ordered list 1
   * @param orderedList2 the ordered list 2
   * @param stringSimCache the string sim cache
   * @param skipList the skip list
   * @param leafMatcher the leaf matcher
   * @param renames the renames
   * @param verbose the verbose
   * @param subLeaves2 the sub leaves 2
   * @param nodes the nodes
   * @param leafSimCounter the leaf sim counter
   */
  @SuppressWarnings("PMD.ExcessiveParameterList")
  public ComputeLeafSimilarityParameter(IdentityHashMap<INode, Integer> orderedList1,
      IdentityHashMap<INode, Integer> orderedList2, ConcurrentHashMap<String, Float> stringSimCache,
      HashSet<INode> skipList, LMatcher leafMatcher, HashMap<String, String> renames,
      boolean verbose, ArrayList<INode> subLeaves2, INode[] nodes, AtomicInteger leafSimCounter) {
    this.orderedList1 = orderedList1;
    this.orderedList2 = orderedList2;
    this.stringSimCache = stringSimCache;
    this.skipList = skipList;
    this.leafMatcher = leafMatcher;
    this.renames = renames;
    this.verbose = verbose;
    this.subLeaves2 = subLeaves2;
    this.nodes = nodes;
    this.leafSimCounter = leafSimCounter;
  }
}