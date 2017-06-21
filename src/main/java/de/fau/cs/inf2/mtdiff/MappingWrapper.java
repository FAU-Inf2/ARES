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

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.ComparePair;

import java.util.Collection;
import java.util.IdentityHashMap;

public class MappingWrapper {
  private IdentityHashMap<INode, INode> mapFirst2Second = new IdentityHashMap<>();
  private IdentityHashMap<INode, INode> mapSecond2First = new IdentityHashMap<>();
  private IdentityHashMap<INode, ComparePair<INode>> resultMap;

  /**
   * Instantiates a new mapping wrapper.
   *
   * @param resultMap the result map
   */
  public MappingWrapper(IdentityHashMap<INode, ComparePair<INode>> resultMap) {
    for (ComparePair<INode> value : resultMap.values()) {
      mapFirst2Second.put(value.getOldElement(), value.getNewElement());
      mapSecond2First.put(value.getNewElement(), value.getOldElement());
    }
    this.resultMap = resultMap;
  }

  /**
   * Gets the dst.
   *
   * @param src the src
   * @return the dst
   */
  public INode getDst(INode src) {
    return mapFirst2Second.get(src);
  }

  /**
   * Gets the src.
   *
   * @param dst the dst
   * @return the src
   */
  public INode getSrc(INode dst) {
    return mapSecond2First.get(dst);
  }

  /**
   * Adds the mapping.
   *
   * @param src the src
   * @param dst the dst
   */
  public void addMapping(INode src, INode dst) {
    assert (mapSecond2First.get(dst) == src || mapSecond2First.get(dst) == null);
    resultMap.put(src, new ComparePair<INode>(src, dst));
    mapFirst2Second.put(src, dst);
    mapSecond2First.put(dst, src);
  }

  /**
   * As set.
   *
   * @return the collection
   */
  public Collection<ComparePair<INode>> asSet() {
    return resultMap.values();
  }

  /**
   * Unlink.
   *
   * @param first the first
   * @param second the second
   */
  public void unlink(INode first, INode second) {
    resultMap.remove(first);
    mapFirst2Second.remove(first);
    mapSecond2First.remove(second);

  }

  /**
   * Checks for src.
   *
   * @param oldElement the old element
   * @return true, if successful
   */
  public boolean hasSrc(INode oldElement) {

    return mapFirst2Second.get(oldElement) != null;
  }

  /**
   * Checks for dst.
   *
   * @param newElement the new element
   * @return true, if successful
   */
  public boolean hasDst(INode newElement) {

    return mapSecond2First.get(newElement) != null;
  }
}
