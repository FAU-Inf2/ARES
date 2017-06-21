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

import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.LinkedList;

public class AddUseVisitor extends AresDefaultFieldVisitor {
  private AbstractBastNode oldUse = null;
  private AbstractBastNode newUse = null;
  private boolean done = false;
  private boolean addBefore = false;
  
  /**
   * Instantiates a new adds the use visitor.
   *
   * @param oldUse the old use
   * @param newUse the new use
   */
  public AddUseVisitor(AbstractBastNode oldUse, AbstractBastNode newUse,
      boolean addBefore) {
    this.oldUse = oldUse;
    this.newUse = newUse;
    this.addBefore = addBefore;
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
        int ivar = 0;
        if (node.fieldMap.get(constant).getListField() != null) {
          if (!done && node.fieldMap.get(constant).getListField().contains(oldUse)) {
            if (constant == BastFieldConstants.SWITCH_CASE_GROUPS) {
              done = true;
              return;
            }
            LinkedList<AbstractBastNode> nodeList = new LinkedList<>();
            for (AbstractBastNode expr : node.fieldMap.get(constant).getListField()) {
              if (expr == oldUse && addBefore) {
                nodeList.add(newUse);
              }
              nodeList.add(expr);
              if (expr == oldUse && !addBefore) {
                nodeList.add(newUse);
              }
            }
            BastField field = new BastField(nodeList);
            node.replaceField(constant, field);
            done = true;
          } else {
            for (AbstractBastNode expr : node.fieldMap.get(constant).getListField()) {
              setVariables(constant, node, ivar++);
              expr.accept(this);
            }
          }
        }
      } else if (node.fieldMap.get(constant).getField() != null) {
        setVariables(constant, node, -1);
        node.fieldMap.get(constant).getField().accept(this);
      }
    }

  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {

  }

  
  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {

  }
}
