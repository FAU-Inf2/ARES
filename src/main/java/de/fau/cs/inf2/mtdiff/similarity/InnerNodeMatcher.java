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

package de.fau.cs.inf2.mtdiff.similarity;

import de.fau.cs.inf2.cas.common.bast.diff.LabelConfiguration;
import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.ComparePair;
import de.fau.cs.inf2.cas.common.util.string.IStringSimilarityMeasure;
import de.fau.cs.inf2.cas.common.util.string.NGramCalculator;

import de.fau.cs.inf2.mtdiff.TreeMatcherConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class InnerNodeMatcher {

  private Map<INode, ArrayList<INode>> directChildrenMap1 = null;
  private Map<INode, ArrayList<INode>> directChildrenMap2 = null;
  private LabelConfiguration labelConfiguration;
  private TreeMatcherConfiguration configuration = null;
  private Map<INode, ArrayList<INode>> leavesMap1 = null;
  private Map<INode, ArrayList<INode>> leavesMap2 = null;

  private Set<ComparePair<INode>> matchedNodes;

  private IStringSimilarityMeasure stringSim = new NGramCalculator(2, 10, 10);

  private final int subtreeSizeThreshold = 4;
  private final float subtreeThresholdLarge = 0.6f;
  private final float subtreeThresholdSmall = 0.4f;
  private final float subtreeThresholdValueMismatch = 0.7f;
  private final float valueThreshold = 0.6f;

  /**
   * Instantiates a new inner node matcher.
   *
   * @param configuration the configuration
   * @param labelConfiguration the label configuration
   * @param leavesMap1 the leaves map 1
   * @param leavesMap2 the leaves map 2
   * @param directChildrenMap1 the direct children map 1
   * @param directChildrenMap2 the direct children map 2
   * @param matchedNodes the matched nodes
   */
  public InnerNodeMatcher(TreeMatcherConfiguration configuration,
      LabelConfiguration labelConfiguration, Map<INode, ArrayList<INode>> leavesMap1,
      Map<INode, ArrayList<INode>> leavesMap2, Map<INode, ArrayList<INode>> directChildrenMap1,
      Map<INode, ArrayList<INode>> directChildrenMap2, Set<ComparePair<INode>> matchedNodes) {
    this.labelConfiguration = labelConfiguration;
    this.leavesMap1 = leavesMap1;
    this.leavesMap2 = leavesMap2;
    this.directChildrenMap1 = directChildrenMap1;
    assert (directChildrenMap1 != null);
    this.directChildrenMap2 = directChildrenMap2;
    this.matchedNodes = matchedNodes;
    this.configuration = configuration;

  }

  private float childrenSimilarity(final List<INode> firstChildren,
      final List<INode> secondChildren, final List<INode> firstDirectChildren,
      final List<INode> secondDirectChildren) {
    int common = 0;
    assert (firstChildren != null);
    assert (secondChildren != null);
    final int max = Math.max(firstChildren.size(), secondChildren.size());
    int[] firstDcCount = new int[firstDirectChildren.size()];
    int[] secondDcCount = new int[secondDirectChildren.size()];
    @SuppressWarnings("unchecked")
    ArrayList<INode>[] firstDclists = new ArrayList[firstDirectChildren.size()];
    @SuppressWarnings("unchecked")
    ArrayList<INode>[] secondDclists = new ArrayList[secondDirectChildren.size()];
    for (int i = 0; i < firstDirectChildren.size(); i++) {
      firstDclists[i] = leavesMap1.get(firstDirectChildren.get(i));
      assert (firstDclists[i] != null);
    }
    for (int i = 0; i < secondDirectChildren.size(); i++) {
      secondDclists[i] = leavesMap2.get(secondDirectChildren.get(i));
      assert (secondDclists[i] != null);
    }
    if (max <= 0) {
      return 1.0f;
    }

    int posFirst = -1;
    outer: for (final INode firstNode : firstChildren) {
      if (common == matchedNodes.size()) {
        break outer;
      }
      posFirst++;
      int posSecond = -1;
      if (firstNode == null) {
        continue;
      }

      for (final INode secondNode : secondChildren) {
        posSecond++;
        if (secondNode == null) {
          continue;
        }

        final ComparePair<INode> pair = new ComparePair<>(firstNode, secondNode);

        if (matchedNodes.contains(pair)) {
          common++;
          int counter = 0;
          for (int i = 0; i < firstDclists.length; i++) {
            if (firstDclists[i].contains(firstNode)) {
              firstDcCount[i]++;
              break;
            } else if (counter == posFirst && firstDclists[i].isEmpty()) {
              firstDcCount[i]++;
              break;
            }
            counter += (firstDcCount[i] == 0 ? 1 : firstDcCount[i]);
          }
          counter = 0;
          for (int i = 0; i < secondDclists.length; i++) {
            if (secondDclists[i].contains(secondNode)) {
              secondDcCount[i]++;
              break;
            } else if (counter == posSecond && secondDclists[i].isEmpty()) {
              secondDcCount[i]++;
              break;
            }
            counter += (secondDcCount[i] == 0 ? 1 : secondDcCount[i]);
          }
          continue outer;
        }
      }
    }
    assert common <= max;
    float tmp = 0.0f;
    for (int i = 0; i < firstDclists.length; i++) {
      tmp += firstDcCount[i] / (float) (firstDclists[i].size() == 0 ? 1 : firstDclists[i].size());
    }
    for (int i = 0; i < secondDclists.length; i++) {
      tmp +=
          secondDcCount[i] / (float) (secondDclists[i].size() == 0 ? 1 : secondDclists[i].size());
    }
    tmp = tmp / (firstDclists.length + secondDclists.length);
    return tmp;
  }

  private boolean isMatch(final float childrenSimilarity, final float valueSimilarity,
      final int childrenCount) {
    if (valueSimilarity < valueThreshold) {
      return childrenSimilarity >= subtreeThresholdValueMismatch;
    }
    if (childrenCount <= subtreeSizeThreshold) {
      return childrenSimilarity >= subtreeThresholdSmall;
    }
    return childrenSimilarity >= subtreeThresholdLarge;
  }

  /**
   * Match.
   *
   * @param first the first
   * @param second the second
   * @param similarity the similarity
   * @return true, if successful
   */
  public boolean match(INode first, INode second, float similarity) {
    if (first.getChildrenWrapped() == null || first.getChildrenWrapped().size() == 0) {
      return false;
    }
    if (first.getTypeWrapped() != second.getTypeWrapped()) {
      return false;
    }
    final List<INode> firstChldrn = leavesMap1.get(first);
    final List<INode> secondChldrn = leavesMap2.get(second);
    final List<INode> firstDirectCh = directChildrenMap1.get(first);
    final List<INode> secondDirectCh = directChildrenMap2.get(second);
    if (firstChldrn == null) {
      assert (false);
    }
    if (secondChldrn == null) {
      assert (false);
    }
    final float childrenSim =
        childrenSimilarity(firstChldrn, secondChldrn, firstDirectCh, secondDirectCh);
    final int childrenCount = Math.max(firstChldrn.size(), secondChldrn.size());

    return isMatch(childrenSim, similarity, childrenCount);

  }

  /**
   * Mt diff similarity.
   *
   * @param first the first
   * @param second the second
   * @param labelSim the label sim
   * @return the float
   */
  public float mtDiffSimilarity(INode first, INode second, float labelSim) {
    float childSim = 0.0f;
    if (first.getChildrenWrapped() == null || first.getChildrenWrapped().size() == 0) {
      childSim = 0.0f;
    } else if (first.getTypeWrapped() != second.getTypeWrapped()) {
      childSim = 0.0f;
    } else {
      final List<INode> firstChldrn = leavesMap1.get(first);
      final List<INode> secondChldrn = leavesMap2.get(second);
      final List<INode> firstDirectCh = directChildrenMap1.get(first);
      final List<INode> secondDirectCh = directChildrenMap2.get(second);
      if (firstChldrn == null) {
        assert (false);
      }
      if (secondChldrn == null) {
        assert (false);
      }
      childSim = childrenSimilarity(firstChldrn, secondChldrn, firstDirectCh, secondDirectCh);
    }
    return childSim;

  }
  
  /**
   * Similarity.
   *
   * @param first the first
   * @param second the second
   * @return the float
   */
  public float similarity(INode first, INode second) {
    if (first.getTypeWrapped() != second.getTypeWrapped()) {
      return 0.0f;
    }
    if (first.getChildrenWrapped() == null || second.getChildrenWrapped() == null) {
      return 0.0f;
    }
    if (first.getChildrenWrapped().size() == 0 || second.getChildrenWrapped().size() == 0) {
      return 0.0f;
    }
    if (labelConfiguration.labelsForValueCompare.contains(first.getTypeWrapped())) {
      if (first.getLabel().equals(second.getLabel())) {
        return 1.0f;
      }
      return (float)configuration.innerNodeNonEqualSimilarity;

    } else if (labelConfiguration.labelsForStringCompare.contains(first.getTypeWrapped())) {
      if (first.getLabel() == null || second.getLabel() == null) {
        return 0.0f;
      }
      return stringSim.similarity(first.getLabel(), second.getLabel());

    } else {
      return 1.0f;
    }
  }

}
