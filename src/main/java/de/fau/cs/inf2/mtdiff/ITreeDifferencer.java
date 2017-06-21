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
