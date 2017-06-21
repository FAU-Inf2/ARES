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

package de.fau.cs.inf2.cas.ares.recommendation.extension;

import de.fau.cs.inf2.cas.ares.bast.diff.AresTreeIterator;
import de.fau.cs.inf2.cas.common.bast.diff.IterationOrder;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;

import java.util.Comparator;
import java.util.HashMap;

public class InsertComparator implements Comparator<BastEditOperation> {

  HashMap<AbstractBastNode, Integer> map = new HashMap<>();

  /**
   * Instantiates a new insert comparator.
   *
   * @param root the root
   */
  public InsertComparator(AbstractBastNode root) {
    AresTreeIterator iterator = new AresTreeIterator(IterationOrder.BREADTH_FIRST, root);
    int pos = 0;
    while (iterator.hasNext()) {
      AbstractBastNode node = iterator.next();
      map.put(node, pos);
      pos++;
    }
  }

  @Override
  public int compare(BastEditOperation arg0, BastEditOperation arg1) {
    if (map.get(arg0.getNewOrChangedNode()) == null) {
      assert (false);
    }
    int posArg0 = map.get(arg0.getNewOrChangedNode());
    int posArg1 = map.get(arg1.getNewOrChangedNode());
    return Integer.compare(posArg0, posArg1);
  }

}