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