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

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.mtdiff.similarity.InnerNodeMatcher;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class InnerMatcherMtDiffRunnable implements Callable<ArrayList<MatchingCandidate<INode>>> {

  private AtomicInteger counter = null;
  private int end;
  private InnerNodeMatcher imatcher;
  private int start;
  private ArrayList<INode> unmatchedNodesOrdered1;
  private ArrayList<INode> unmatchedNodesOrdered2;
  private final double innerNodeThreshold;
  private final double weightValue;
  private final double weightChildren;
  
  /**
   * Instantiates a new inner matcher runnable.
   *
   * @param unmatchedNodesOrdered1 the unmatched nodes ordered1
   * @param unmatchedNodesOrdered2 the unmatched nodes ordered2
   * @param counter the counter
   * @param start the start
   * @param end the end
   * @param imatcher the imatcher
   */
  public InnerMatcherMtDiffRunnable(ArrayList<INode> unmatchedNodesOrdered1,
      ArrayList<INode> unmatchedNodesOrdered2, AtomicInteger counter, int start, int end,
      InnerNodeMatcher imatcher, double weightValue,
      double weightChildren, double innerNodeThreshold) {
    super();
    this.unmatchedNodesOrdered1 = unmatchedNodesOrdered1;
    this.unmatchedNodesOrdered2 = unmatchedNodesOrdered2;
    this.imatcher = imatcher;
    this.counter = counter;
    this.start = start;
    this.end = end;
    counter.incrementAndGet();
    this.weightValue = weightValue;
    this.weightChildren = weightChildren;
    this.innerNodeThreshold = innerNodeThreshold;
    
  }

  
  /**
   * Call.
   *
   * @return the array list
   * @throws Exception the exception
   */
  @Override
  public ArrayList<MatchingCandidate<INode>> call() throws Exception {
    try {
      ArrayList<MatchingCandidate<INode>> candidates = new ArrayList<>();
      for (int i = start; i < end; i++) {
        INode firstNode = unmatchedNodesOrdered1.get(i);
        ArrayList<MatchingCandidate<INode>> tmp =
            computeInnerMatchingCandidate(firstNode, unmatchedNodesOrdered2, imatcher);
        candidates.addAll(tmp);

      }
      counter.decrementAndGet();
      return candidates;

    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }

  }

  private ArrayList<MatchingCandidate<INode>> computeInnerMatchingCandidate(INode firstNode,
      ArrayList<INode> unmatchedNodesOrdered2, InnerNodeMatcher imatcher) {
    ArrayList<MatchingCandidate<INode>> list = new ArrayList<>();
    for (int j = 0; j < unmatchedNodesOrdered2.size(); j++) {
      final INode secondNode = unmatchedNodesOrdered2.get(j);
      if (firstNode.getTypeWrapped() == secondNode.getTypeWrapped()) {
        float labelSim = 0.0f;
        labelSim = imatcher.similarity(firstNode, secondNode);
        float childSim = imatcher.mtDiffSimilarity(firstNode, secondNode, labelSim);
        double res = (labelSim * weightValue + childSim * weightChildren) / 2;
        if (res >= innerNodeThreshold) {
          list.add(new MatchingCandidate<>(firstNode, secondNode, (float)res));
        }
      }
    }
    return list;
  }

}
