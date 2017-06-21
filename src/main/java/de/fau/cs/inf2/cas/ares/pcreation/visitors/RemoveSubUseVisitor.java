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
import de.fau.cs.inf2.cas.ares.pcreation.PatternGenerator;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.visitors.DefaultFieldVisitor;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Stack;

public class RemoveSubUseVisitor extends DefaultFieldVisitor {

  private Stack<AbstractBastNode> affectedStmtStack = new Stack<AbstractBastNode>();
  private LinkedList<AbstractBastNode> exprStmts = new LinkedList<AbstractBastNode>();


  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    if (exprStmts.contains(node)) {
      affectedStmtStack.push(node);
    }

  }


  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {
    if (exprStmts.contains(node)) {
      affectedStmtStack.pop();
    }

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresUseStmt node) {
    if (WildcardAccessHelper.getName(node) == null
        || WildcardAccessHelper.getName(node).name.isEmpty()) {
      uses.add(node);
    }
  }

  private HashSet<AbstractBastNode> uses = new HashSet<>();


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
          if (node.fieldMap.get(constant).getListField().size() > 1) {
            fieldId = constant;
            globalParent = node;
            boolean changed = true;
            PatternGenerator.print(node);
            findExprUses(node.fieldMap.get(constant).getListField());
            while (changed) {
              changed = false;
              BastField field = null;

              field = removeSubUse(node.fieldMap.get(constant).getListField());
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

  private BastField removeSubUse(LinkedList<? extends AbstractBastNode> listField) {
    if (!affectedStmtStack.isEmpty()) {
      LinkedList<AbstractBastNode> toRemove = new LinkedList<AbstractBastNode>();
      for (AbstractBastNode n : listField) {
        if (WildcardAccessHelper.isExprWildcard(n)) {
          if (WildcardAccessHelper.getName(n) == null) {
            toRemove.add(n);
          }
        }
      }
      if (!toRemove.isEmpty()) {
        LinkedList<AbstractBastNode> copy = new LinkedList<AbstractBastNode>();
        copy.addAll(listField);
        copy.removeAll(toRemove);
        return new BastField(copy);
      }
    }
    return null;
  }

  private void findExprUses(LinkedList<? extends AbstractBastNode> listField) {
    boolean affectedStmt = false;
    for (AbstractBastNode n : listField) {
      if (!WildcardAccessHelper.isExprWildcard(n)) {
        if (affectedStmt) {
          exprStmts.add(n);
        }
        affectedStmt = false;
      } else {
        if (!affectedStmt) {
          if (WildcardAccessHelper.getName(n) == null) {
            affectedStmt = true;
          }
        }

      }
    }

  }
}
