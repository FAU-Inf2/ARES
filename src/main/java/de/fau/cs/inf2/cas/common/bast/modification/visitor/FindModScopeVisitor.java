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

package de.fau.cs.inf2.cas.common.bast.modification.visitor;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.modification.ModificationInformation;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;

import java.util.HashMap;
import java.util.Map.Entry;

public class FindModScopeVisitor extends AresDefaultFieldVisitor {
  private HashMap<AbstractBastNode, ModificationInformation> modMap;

  /**
   * Instantiates a new find mod scope visitor.
   *
   * @param modMap the mod map
   */
  public FindModScopeVisitor(HashMap<AbstractBastNode, ModificationInformation> modMap) {
    this.modMap = modMap;
  }

  private boolean found = false;
  private boolean insideMethod = false;

  public AbstractBastNode topNode;
  public BastFieldConstants field;
  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    if (insideMethod && topNode == null) {
      ModificationInformation modInfo = modMap.get(node);
      if (modInfo != null) {
        if (modInfo.nodeModified || modInfo.fieldLowerBorder.size() > 1) {
          topNode = node;
          found = true;
          if (modInfo.fieldLowerBorder.size() == 1) {
            Entry<BastFieldConstants, Integer> lowerEntry =
                modInfo.fieldLowerBorder.entrySet().iterator().next();
            field = lowerEntry.getKey();
          } else {
            topNode = globalParent;
            field = fieldId;
          }
        } else if (modInfo.fieldLowerBorder.size() == 1) {
          Entry<BastFieldConstants, Integer> lowerEntry =
              modInfo.fieldLowerBorder.entrySet().iterator().next();
          Entry<BastFieldConstants, Integer> upperEntry =
              modInfo.fieldUpperBorder.entrySet().iterator().next();
          if (Math.abs(lowerEntry.getValue() - upperEntry.getValue()) > 0) {
            topNode = node;
            field = lowerEntry.getKey();
            found = true;
          }
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

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastFunction node) {
    beginVisit(node);
    insideMethod = true;
    standardVisit(BastFieldConstants.FUNCTION_BLOCK_STATEMENTS, node);
    endVisit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresBlock node) {
    topNode = globalParent;
    found = true;
    field = fieldId;
  }

  
  /**
   * Standard visit.
   *
   * @param constant the constant
   * @param node the node
   */
  @Override
  public void standardVisit(BastFieldConstants constant, AbstractBastNode node) {
    if (found) {
      return;
    }
    if (node.fieldMap.get(constant) != null) {
      if (node.fieldMap.get(constant).isList()) {
        int counter = 0;
        if (node.fieldMap.get(constant).getListField() != null) {
          for (AbstractBastNode expr : node.fieldMap.get(constant).getListField()) {
            setVariables(constant, node, counter++);
            expr.accept(this);
            if (found) {
              return;
            }
          }
        }
      } else if (node.fieldMap.get(constant).getField() != null) {
        setVariables(constant, node, -1);
        node.fieldMap.get(constant).getField().accept(this);
      }
    }
  }

}
