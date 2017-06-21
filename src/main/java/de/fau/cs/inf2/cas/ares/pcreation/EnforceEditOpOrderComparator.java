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

package de.fau.cs.inf2.cas.ares.pcreation;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.Comparator;
import java.util.HashMap;

class EnforceEditOpOrderComparator implements Comparator<BastEditOperation> {
  private HashMap<EditOperationType, Integer> orderMap = new HashMap<>();

  /**
   * Instantiates a new enforce edit op order comparator.
   */
  public EnforceEditOpOrderComparator() {
    for (EditOperationType e : EditOperationType.values()) {
      switch (e) {
        case INSERT:
        case STATEMENT_INSERT:
          orderMap.put(e, 2);
          break;
        case DELETE:
        case STATEMENT_DELETE:
          orderMap.put(e, 3);
          break;
        case MOVE:
        case STATEMENT_REORDERING:
          orderMap.put(e, 1);
          break;
        case ALIGN:
          orderMap.put(e, 4);
          break;
        default:
          orderMap.put(e, 0);
      }
    }
  }

  
  /**
   * Compare.
   *
   * @param o1 the o1
   * @param o2 the o2
   * @return the int
   */
  @Override
  public int compare(BastEditOperation o1, BastEditOperation o2) {
    int valO1 = orderMap.get(o1.getType());
    int valO2 = orderMap.get(o2.getType());
    if (valO1 != valO2) {
      return Integer.compare(valO1, valO2);
    }
    if (valO1 == 0) {
      return 0;
    }
    if (o1.getUnchangedOrOldParentNode() != null && o2.getUnchangedOrOldParentNode() != null
        && o1.getUnchangedOrOldParentNode() != o2.getUnchangedOrOldParentNode()) {
      return Integer.compare(o1.getUnchangedOrOldParentNode().nodeId,
          o2.getUnchangedOrOldParentNode().nodeId);
    }
    if (o1.getUnchangedOrNewParentNode() != null && o2.getUnchangedOrNewParentNode() != null
        && o1.getUnchangedOrNewParentNode() != o2.getUnchangedOrNewParentNode()) {
      return Integer.compare(o1.getUnchangedOrNewParentNode().nodeId,
          o2.getUnchangedOrNewParentNode().nodeId);
    }
    if (o1.getOldOrChangedIndex() != null && o2.getOldOrChangedIndex() != null
        && o1.getOldOrChangedIndex().childrenListNumber.id != o2
            .getOldOrChangedIndex().childrenListNumber.id) {
      return Integer.compare(o1.getOldOrChangedIndex().childrenListNumber.id,
          o2.getOldOrChangedIndex().childrenListNumber.id);
    }
    if (o1.getOldOrChangedIndex() != null && o2.getOldOrChangedIndex() != null && o1
        .getOldOrChangedIndex().childrenListIndex != o2.getOldOrChangedIndex().childrenListIndex) {
      return Integer.compare(o1.getOldOrChangedIndex().childrenListIndex,
          o2.getOldOrChangedIndex().childrenListIndex);
    }
    if (o1.getNewOrChangedIndex() != null && o2.getNewOrChangedIndex() != null
        && o1.getNewOrChangedIndex().childrenListNumber.id != o2
            .getNewOrChangedIndex().childrenListNumber.id) {
      return Integer.compare(o1.getNewOrChangedIndex().childrenListNumber.id,
          o2.getNewOrChangedIndex().childrenListNumber.id);
    }
    if (o1.getNewOrChangedIndex() != null && o2.getNewOrChangedIndex() != null && o1
        .getNewOrChangedIndex().childrenListIndex != o2.getNewOrChangedIndex().childrenListIndex) {
      return Integer.compare(o1.getNewOrChangedIndex().childrenListIndex,
          o2.getNewOrChangedIndex().childrenListIndex);
    }
    return 0;
  }

}
