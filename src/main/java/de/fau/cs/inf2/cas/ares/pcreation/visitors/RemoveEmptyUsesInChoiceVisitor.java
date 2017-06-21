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

import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.PatternGenerator;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.LinkedList;
import java.util.Stack;

public class RemoveEmptyUsesInChoiceVisitor extends AresDefaultFieldVisitor {
  

  /**
   * Instantiates a new removes the empty uses in choice visitor.
   */
  public RemoveEmptyUsesInChoiceVisitor() {}

  
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
          if (node.fieldMap.get(constant).getListField().size() > 1 && !choices.isEmpty()) {
            fieldId = constant;
            globalParent = node;
            boolean changed = true;
            PatternGenerator.print(node);
            while (changed) {
              changed = false;
              BastField field = null;

              field = removeUses(node.fieldMap.get(constant).getListField());
              if (field != null) {
                if (field.getListField().size() != node.fieldMap.get(constant).getListField()
                    .size()) {
                  changed = true;
                }
                node.replaceField(constant, field);
                PatternGenerator.print(node);
                continue;
              }

            }
          }
          for (AbstractBastNode expr : node.fieldMap.get(constant).getListField()) {
            setVariables(constant, node, counter++);
            expr.accept(this);
          }
        }

      } else if (node.fieldMap.get(constant).getField() != null) {
        setVariables(constant, node, -1);
        node.fieldMap.get(constant).getField().accept(this);
      }
    }
  }

  private BastField removeUses(LinkedList<? extends AbstractBastNode> listField) {
    int containsUses = 0;
    LinkedList<AbstractBastNode> newNodes = new LinkedList<>();
    boolean changed = false;
    for (AbstractBastNode expr : listField) {
      if (expr.getTag() == AresUseStmt.TAG) {
        containsUses++;
      }
    }
    if (containsUses > 0) {
      for (int i = 0; i < listField.size(); i++) {
        if (listField.get(i).getTag() == AresUseStmt.TAG) {
          changed = true;
          continue;
        }
        newNodes.add(listField.get(i));
      }
    }
    if (changed) {
      return new BastField(newNodes);
    }
    return null;
  }

  private Stack<AresChoiceStmt> choices = new Stack<>();

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresChoiceStmt node) {
    choices.push(node);
    super.visit(node);
    choices.pop();
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
