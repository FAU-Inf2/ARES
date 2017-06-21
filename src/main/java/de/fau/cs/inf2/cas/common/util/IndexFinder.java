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
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import java.util.List;

/**
 * A class providing methods to retrieve the index of a child node in respect to its parent.
 *
 */
public final class IndexFinder {
  
  /**
   * todo.
   * 
   * <p>Retrieves the correct index of a node in respect to its parent.
   * 
   * @param node The node, whose index is retrieved
   *
   * @param parent The parent node
   * @param childrenGetter The ChildrenRetriever instance used to look up children
   * @return The index of node in respect to the children lists
   */
  public static NodeIndex getIndex(final AbstractBastNode node, final AbstractBastNode parent,
      final ChildrenRetriever childrenGetter) {
    final List<ChildrenList> children = childrenGetter.getChildren(parent);

    if (children == null) {
      return null;
    }
    for (final ChildrenList list : children) {
      int listIndex = 0;

      for (final AbstractBastNode child : list.list) {
        if (node == child) {
          return new NodeIndex(list.fieldConstant, listIndex);
        }
        listIndex++;
      }

    }

    assert false;
    return null;
  }

  /**
   * Gets the index.
   *
   * @param node the node
   * @param parent the parent
   * @return the index
   */
  public static NodeIndex getIndex(final INode node, final INode parent) {
    final List<INode> children = parent.getChildrenWrapped();

    if (children == null) {
      return null;
    }
    int index = children.indexOf(node);
    if (index != -1) {
      return new NodeIndex(BastFieldConstants.INODE_FIELD_ID, index);
    }

    assert false;
    return null;
  }
}
