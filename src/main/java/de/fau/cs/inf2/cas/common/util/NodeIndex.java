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

package de.fau.cs.inf2.cas.common.util;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An index of a child node in respect to the children of the parent node. It is similar to an index
 * used on an array, however, a node may have more than one list of children.
 *
 */
public final class NodeIndex {
  private static final Pattern PATTERN = Pattern.compile("\\(([0-9]+)/([0-9]+)\\)");

  public BastFieldConstants childrenListNumber;
  public int childrenListIndex;

  /**
   * Instantiates a new node index.
   *
   * @param listNumber the list number
   * @param listIndex the list index
   */
  public NodeIndex(final BastFieldConstants listNumber, final int listIndex) {
    assert (listNumber != null);
    this.childrenListNumber = listNumber;
    this.childrenListIndex = listIndex;
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    return String.format("(%s-%d/%d)", childrenListNumber.name, childrenListNumber.id,
        childrenListIndex);
  }

  /**
   * From string.
   *
   * @param string the s
   * @return the node index
   */
  public static NodeIndex fromString(final String string) {
    final Matcher matcher = PATTERN.matcher(string);

    if (matcher.matches()) {
      try {
        BastFieldConstants tfc = BastFieldConstants.getConstant(Integer.parseInt(matcher.group(1)));
        assert (tfc != null);
        NodeIndex ni = new NodeIndex(tfc, Integer.parseInt(matcher.group(2)));
        return ni;
      } catch (NumberFormatException e) {
        throw new IllegalArgumentException(String.format("%s is not a NodeIndex", string));
      }
    } else {
      throw new IllegalArgumentException(String.format("%s is not a NodeIndex", string));
    }
  }

  /**
   * To string xml.
   *
   * @return the string
   */
  public String toStringXml() {
    return String.format("(%d/%d)", childrenListNumber.id, childrenListIndex);
  }
}
