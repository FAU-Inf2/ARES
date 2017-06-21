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