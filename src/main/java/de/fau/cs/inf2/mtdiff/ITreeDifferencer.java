package de.fau.cs.inf2.mtdiff;

import de.fau.cs.inf2.cas.common.bast.diff.IConfiguration;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

public interface ITreeDifferencer {

  /**
   * Compute difference.
   *
   * @return the extended diff result
   * @throws Exception the exception
   */
  public ExtendedDiffResult computeDifference(String filename,
      final IConfiguration configuration) throws Exception;

  /**
   * Sets the timeout.
   *
   * @param value the new timeout
   */
  public void setTimeout(long value);

  /**
   * Gets the extended cd time.
   *
   * @return the extended cd time
   */
  public AtomicLong getextendedCdTime();

  /**
   * Gets the extended inner matching time.
   *
   * @return the extended inner matching time
   */
  public AtomicLong getextendedInnerMatchingTime();

  /**
   * Gets the large leaf diff.
   *
   * @return the large leaf diff
   */
  public AtomicLong getlargeLeafDiff();

  /**
   * Shutdown.
   */
  public void shutdown();

  /**
   * Gets the leaves map1.
   *
   * @return the leaves map1
   */
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getLeavesMap1();

  /**
   * Gets the leaves map2.
   *
   * @return the leaves map2
   */
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getLeavesMap2();

  /**
   * Gets the direct children map1.
   *
   * @return the direct children map1
   */
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getdirectChildrenMap1();

  /**
   * Gets the direct children map2.
   *
   * @return the direct children map2
   */
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getdirectChildrenMap2();

  /**
   * Gets the single instance of ITreeDifferencer.
   *
   * @return single instance of ITreeDifferencer
   */
  public ITreeDifferencer getInstance();

}
