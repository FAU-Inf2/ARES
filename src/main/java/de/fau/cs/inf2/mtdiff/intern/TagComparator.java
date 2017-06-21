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

package de.fau.cs.inf2.mtdiff.intern;

import de.fau.cs.inf2.cas.common.bast.diff.LabelConfiguration;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

public class TagComparator implements Comparator<Map.Entry<Integer, Integer>> {

  private LabelConfiguration labelConfiguration;

  /**
   * Instantiates a new tag comparator.
   *
   * @param labelConfiguration the label configuration
   */
  public TagComparator(LabelConfiguration labelConfiguration) {
    this.labelConfiguration = labelConfiguration;
  }

  
  /**
   * Compare.
   *
   * @param o1 the o1
   * @param o2 the o2
   * @return the int
   */
  @Override
  public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
    if (((Map.Entry<Integer, Integer>) (o1)).getKey() == labelConfiguration.identifierLabel) {
      return Integer.MIN_VALUE;
    } else {
      if (((Map.Entry<Integer, Integer>) (o2)).getKey() == labelConfiguration.identifierLabel) {
        return Integer.MAX_VALUE;
      } else {
        return (((o1)).getValue()).compareTo(((o2)).getValue());
      }
    }
  }
}
