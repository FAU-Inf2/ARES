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

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.cas.common.util.ComparePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class InnerMatcherParameter {
  public float valueThreshold;
  public float subtreeThresholdLarge;
  public float subtreeThresholdSmall;
  public float subtreeThresholdValueMismatch;
  public int subtreeSizeThreshold;
  public Set<ComparePair<AbstractBastNode>> matchedNodes;
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> leavesMap1;
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> leavesMap2;
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> directChildren1;
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> directChildren2;

  /**
   * Instantiates a new inner matcher parameter.
   *
   * @param valueThreshold the value threshold
   * @param subtreeThresholdLarge the subtree threshold large
   * @param subtreeThresholdSmall the subtree threshold small
   * @param subtreeThresholdValueMismatch the subtree threshold value mismatch
   * @param subtreeSizeThreshold the subtree size threshold
   * @param matchedNodes the matched nodes
   * @param leavesMap1 the leaves map 1
   * @param leavesMap2 the leaves map 2
   * @param directChildren1 the direct children 1
   * @param directChildren2 the direct children 2
   */
  @SuppressWarnings("PMD.ExcessiveParameterList")
  public InnerMatcherParameter(float valueThreshold, float subtreeThresholdLarge,
      float subtreeThresholdSmall, float subtreeThresholdValueMismatch, int subtreeSizeThreshold,
      Set<ComparePair<AbstractBastNode>> matchedNodes,
      HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> leavesMap1,
      HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> leavesMap2,
      HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> directChildren1,
      HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> directChildren2) {
    this.valueThreshold = valueThreshold;
    this.subtreeThresholdLarge = subtreeThresholdLarge;
    this.subtreeThresholdSmall = subtreeThresholdSmall;
    this.subtreeThresholdValueMismatch = subtreeThresholdValueMismatch;
    this.subtreeSizeThreshold = subtreeSizeThreshold;
    this.matchedNodes = matchedNodes;
    this.leavesMap1 = leavesMap1;
    this.leavesMap2 = leavesMap2;
    this.directChildren1 = directChildren1;
    this.directChildren2 = directChildren2;
  }
}