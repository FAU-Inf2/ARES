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

import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.ReplaceMap;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RestoreChoicePartsVisitor extends AresDefaultFieldVisitor {
  private ReplaceMap replaceMap;

  /**
   * Instantiates a new restore choice parts visitor.
   *
   * @param extDiff the ext diff
   * @param replaceMap the replace map
   */
  public RestoreChoicePartsVisitor(ExtendedDiffResult extDiff, ReplaceMap replaceMap) {
    this.replaceMap = replaceMap;
  }

  private Map<AbstractBastNode, AbstractBastNode> revertMap = new HashMap<>();

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    if (node.getTag() == AresUseStmt.TAG && !WildcardAccessHelper.isExprWildcard(node)) {

      for (Map.Entry<AbstractBastNode, AbstractBastNode> entry : replaceMap.entrySet()) {
        if (entry.getValue() == node) {
          revertMap.put(node, entry.getKey());
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
   * Standard visit.
   *
   * @param constant the constant
   * @param node the node
   */
  @Override
  public void standardVisit(BastFieldConstants constant, AbstractBastNode node) {
    if (node.fieldMap.get(constant) != null) {
      if (node.fieldMap.get(constant).isList()) {
        int counter = 0;
        if (node.fieldMap.get(constant).getListField() != null) {
          LinkedList<AbstractBastNode> tmpList = new LinkedList<>();
          boolean changed = false;
          for (AbstractBastNode expr : node.fieldMap.get(constant).getListField()) {
            setVariables(constant, node, counter++);
            expr.accept(this);
            if (revertMap.containsKey(expr)) {
              tmpList.add(revertMap.get(expr));
              changed = true;
            } else {
              tmpList.add(expr);
            }
          }
          if (changed) {
            node.replaceField(constant, new BastField(tmpList));
          }
        }
      } else if (node.fieldMap.get(constant).getField() != null) {
        setVariables(constant, node, -1);
        node.fieldMap.get(constant).getField().accept(this);
        if (revertMap.containsKey(node.fieldMap.get(constant).getField())) {
          node.replaceField(constant,
              new BastField(revertMap.get(node.fieldMap.get(constant).getField())));
        }
      }
    }

  }

}
