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

package de.fau.cs.inf2.cas.ares.pcreation.help;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;

import java.util.ArrayList;

public class MovementInformation {
  private int[] oldMap;
  public ArrayList<BastEditOperation> delList = new ArrayList<>();
  public ArrayList<BastEditOperation> insList = new ArrayList<>();

  /**
   * Instantiates a new movement information.
   *
   * @param oldNode the old node
   * @param oldSize the old size
   * @param newNode the new node
   * @param newSize the new size
   */
  public MovementInformation(AbstractBastNode oldNode, int oldSize, AbstractBastNode newNode,
      int newSize) {
    oldMap = new int[oldSize];
    for (int i = 0; i < oldSize; i++) {
      oldMap[i] = i;
    }

  }

  /**
   * Update map.
   *
   * @param begin the begin
   * @param end the end
   * @param diff the diff
   */
  public void updateMap(int begin, int end, int diff) {
    int[] tmp = oldMap;

    int tmpBegin = (begin == -1) ? 0 : begin;

    int tmpEnd = (end == -1) ? tmp.length : end;
    for (int i = tmpBegin; i < tmpEnd; i++) {
      tmp[i] = tmp[i] + diff;
    }

  }

}
