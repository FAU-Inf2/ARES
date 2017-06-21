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

import de.fau.cs.inf2.cas.common.bast.diff.LabelConfiguration;
import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.mtdiff.intern.LMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class LeafSimilarityRunnableParameter {
  public ConcurrentHashMap<String, Float> stringSimCache;
  public INode[] subLeaves1s;
  public ArrayList<INode> subLeaves2;
  public AtomicInteger counter;
  public int start;
  public int end;
  public IdentityHashMap<INode, Integer> orderedList1;
  public IdentityHashMap<INode, Integer> orderedList2;
  public HashSet<INode> skipList;
  public LMatcher lmatcher;
  public LabelConfiguration labelConfiguration;
  public HashMap<String, String> renames;
  public boolean verbose;

  /**
   * Instantiates a new leaf similarity runnable parameter.
   *
   * @param stringSimCache the string sim cache
   * @param subLeaves1s the sub leaves 1 s
   * @param subLeaves2 the sub leaves 2
   * @param counter the counter
   * @param start the start
   * @param end the end
   * @param orderedList1 the ordered list 1
   * @param orderedList2 the ordered list 2
   * @param skipList the skip list
   * @param lmatcher the lmatcher
   * @param labelConfiguration the label configuration
   * @param renames the renames
   * @param verbose the verbose
   */
  @SuppressWarnings("PMD.ExcessiveParameterList")
  public LeafSimilarityRunnableParameter(ConcurrentHashMap<String, Float> stringSimCache,
      INode[] subLeaves1s, ArrayList<INode> subLeaves2, AtomicInteger counter, int start, int end,
      IdentityHashMap<INode, Integer> orderedList1, IdentityHashMap<INode, Integer> orderedList2,
      HashSet<INode> skipList, LMatcher lmatcher, LabelConfiguration labelConfiguration,
      HashMap<String, String> renames, boolean verbose) {
    this.stringSimCache = stringSimCache;
    this.subLeaves1s = subLeaves1s;
    this.subLeaves2 = subLeaves2;
    this.counter = counter;
    this.start = start;
    this.end = end;
    this.orderedList1 = orderedList1;
    this.orderedList2 = orderedList2;
    this.skipList = skipList;
    this.lmatcher = lmatcher;
    this.labelConfiguration = labelConfiguration;
    this.renames = renames;
    this.verbose = verbose;
  }
}