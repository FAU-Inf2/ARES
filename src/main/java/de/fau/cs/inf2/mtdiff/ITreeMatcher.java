package de.fau.cs.inf2.mtdiff;

import de.fau.cs.inf2.cas.common.bast.diff.LabelConfiguration;
import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.ComparePair;

import java.util.Set;
import java.util.concurrent.ExecutorService;

public interface ITreeMatcher {
  
  /**
   * Compute matching pairs.
   *
   * @throws Exception the exception
   */
  public void computeMatchingPairs() throws Exception;

  /**
   * Gets the matching pairs.
   *
   * @return the matching pairs
   */
  public Set<ComparePair<INode>> getMatchingPairs();

  void init(INode tree1, INode tree2, LabelConfiguration labelConfiguration);

  void initAres(ExecutorService executorService, double leafThreshold, int numThreads);
}
