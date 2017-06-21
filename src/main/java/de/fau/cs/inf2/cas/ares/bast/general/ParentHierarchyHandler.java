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

package de.fau.cs.inf2.cas.ares.bast.general;

import de.fau.cs.inf2.cas.ares.bast.visitors.RetrieveParentInformationMapVisitor;

import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformation;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class ParentHierarchyHandler {
  
  /**
   * Gets the parent hierarchy.
   *
   * @param node the node
   * @return the parent hierarchy
   */
  public static Map<AbstractBastNode, NodeParentInformationHierarchy> getParentHierarchy(
      AbstractBastNode node) {
    RetrieveParentInformationMapVisitor rpimv = new RetrieveParentInformationMapVisitor();
    node.accept(rpimv);
    Map<AbstractBastNode, NodeParentInformation> parentInfo = rpimv.parentInformation;
    Map<AbstractBastNode, NodeParentInformationHierarchy> parentHierarchyInfo = new HashMap<>();
    for (Entry<AbstractBastNode, NodeParentInformation> es : parentInfo.entrySet()) {
      LinkedList<NodeParentInformation> list = new LinkedList<>();
      NodeParentInformation tmp = es.getValue();
      while (tmp != null) {
        list.add(tmp);
        tmp = parentInfo.get(tmp.parent);
      }
      parentHierarchyInfo.put(es.getKey(), new NodeParentInformationHierarchy(es.getKey(), list));
    }
    return parentHierarchyInfo;
  }

}
