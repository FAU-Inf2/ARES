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

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * A convienence class for retrieving the children of a node of an abstract syntax tree.
 *
 */
public class ChildrenRetriever {

  /**
   * todo.
   * 
   * <p>Retrieve a list of all children of a given node. Each type of children is contained in a
   * separate list.
   *
   * @param node the node
   * @return the children
   */
  public List<ChildrenList> getChildren(final AbstractBastNode node) {
    if (node == null) {
      return null;
    }
    synchronized (node) {
      Collection<BastFieldConstants> constants = node.fieldMap.keySet();
      ArrayList<Integer> keys = new ArrayList<>();
      for (BastFieldConstants tfc : constants) {
        keys.add(tfc.id);
      }
      Collections.sort(keys);
      List<ChildrenList> list = new ArrayList<ChildrenList>();
      for (Integer key : keys) {
        BastField field = node.fieldMap.get(BastFieldConstants.getConstant(key));
        assert (field != null);
        if (field.isList()) {
          ArrayList<AbstractBastNode> tmpList = new ArrayList<>();
          ChildrenList tmp = new ChildrenList(tmpList, BastFieldConstants.getConstant(key));
          if (field.getListField() == null || field.getListField().isEmpty()) {
            final List<AbstractBastNode> emptyList = Collections.emptyList();
            final ChildrenList empty =
                new ChildrenList(emptyList, BastFieldConstants.getConstant(key));
            list.add(empty);
          } else {
            tmpList.addAll(field.getListField());
            list.add(tmp);
          }
        } else {
          ArrayList<AbstractBastNode> tmpList = new ArrayList<>();
          ChildrenList tmp = new ChildrenList(tmpList, BastFieldConstants.getConstant(key));
          if (field.getField() == null) {
            final List<AbstractBastNode> emptyList = Collections.emptyList();
            final ChildrenList empty =
                new ChildrenList(emptyList, BastFieldConstants.getConstant(key));
            list.add(empty);
          } else {
            tmpList.add(field.getField());
            list.add(tmp);
          }
        }
      }
      return list;
    }
  }
}
