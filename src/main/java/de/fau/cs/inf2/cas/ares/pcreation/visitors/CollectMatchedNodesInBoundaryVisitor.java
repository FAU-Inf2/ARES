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

package de.fau.cs.inf2.cas.ares.pcreation.visitors;

import de.fau.cs.inf2.cas.ares.pcreation.MatchBoundary;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.modification.ModificationInformation;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.visitors.DefaultFieldVisitor;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;

import java.util.HashMap;

public class CollectMatchedNodesInBoundaryVisitor extends DefaultFieldVisitor {
  public HashMap<AbstractBastNode, ModificationInformation> modMap = new HashMap<>();
  private MatchBoundary boundary = null;
  private ExtendedDiffResult exDiffBb = null;
  private ExtendedDiffResult exDiffBA1 = null;
  private ExtendedDiffResult exDiffBA2 = null;

  /**
   * Instantiates a new collect matched nodes in boundary visitor.
   *
   * @param boundary the boundary
   * @param exDiffBb the ex diff bb
   * @param exDiffBA1 the ex diff b a1
   * @param exDiffBA2 the ex diff b a2
   */
  public CollectMatchedNodesInBoundaryVisitor(MatchBoundary boundary, ExtendedDiffResult exDiffBb,
      ExtendedDiffResult exDiffBA1, ExtendedDiffResult exDiffBA2) {
    this.boundary = boundary;
    this.exDiffBb = exDiffBb;
    this.exDiffBA1 = exDiffBA1;
    this.exDiffBA2 = exDiffBA2;
  }

  private boolean isLeaf(AbstractBastNode node) {
    switch (node.getTag()) {
      case BastIntConst.TAG:
      case BastNameIdent.TAG:
        return true;
      default:
        return false;
    }
  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    if (!isLeaf(node) && found) {

      if (exDiffBb.firstToSecondMap.containsKey(node)
          || exDiffBb.secondToFirstMap.containsKey(node)) {
        ModificationInformation modInfo = new ModificationInformation();
        modInfo.nodeModified = true;
        AbstractBastNode node1 = exDiffBA1.firstToSecondMap.get(node);
        if (node1 != null) {
          modMap.put(node1, modInfo);
        }
        modInfo = new ModificationInformation();
        modInfo.nodeModified = true;
        AbstractBastNode node2 = exDiffBA2.firstToSecondMap.get(node);
        if (node2 != null) {
          modMap.put(node2, modInfo);
        }
      }
    }
  }

  
  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {

  }

  private boolean found = false;

  
  /**
   * Standard visit.
   *
   * @param constant the constant
   * @param node the node
   */
  @Override
  public void standardVisit(BastFieldConstants constant, AbstractBastNode node) {
    boolean inside = false;
    if (node.fieldMap.get(constant) != null) {
      if (node.fieldMap.get(constant).isList()) {
        int counter = 0;
        if (node.fieldMap.get(constant).getListField() != null) {
          for (AbstractBastNode expr : node.fieldMap.get(constant).getListField()) {
            setVariables(constant, node, counter++);

            if (node == boundary.getNode2() && constant == boundary.field2) {
              found = true;
              inside = true;
            }
            expr.accept(this);
            if (inside) {
              found = false;
            }
          }
        }
      } else if (node.fieldMap.get(constant).getField() != null) {
        setVariables(constant, node, -1);
        if (node == boundary.getNode2() && constant == boundary.field2) {
          found = true;
          inside = true;
        }
        node.fieldMap.get(constant).getField().accept(this);
        if (inside) {
          found = false;
        }
      }
    }
  }

}
