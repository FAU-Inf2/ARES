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
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.visitors.DefaultFieldVisitor;

import java.util.LinkedList;

public class RemoveObsoleteUseVisitor extends DefaultFieldVisitor {
  private ConnectWildcardAndUseVisitor cwuv;

  /**
   * Instantiates a new removes the obsolete use visitor.
   *
   * @param cwuv the cwuv
   */
  public RemoveObsoleteUseVisitor(ConnectWildcardAndUseVisitor cwuv) {
    this.cwuv = cwuv;
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
          if (node.fieldMap.get(constant).getListField().size() > 1) {
            globalParent = node;
            fieldId = constant;
            boolean changed = true;
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
    for (AbstractBastNode expr : listField) {
      if (expr.getTag() == AresUseStmt.TAG) {
        containsUses++;
      }
    }
    if (containsUses > 0) {
      LinkedList<AbstractBastNode> toRemove = new LinkedList<AbstractBastNode>();
      for (AbstractBastNode expr : listField) {
        if (expr.getTag() == AresUseStmt.TAG) {
          BastNameIdent name = WildcardAccessHelper.getName(expr);
          if (name != null) {
            for (AresWildcard w : cwuv.removedNamedWildcards) {
              if (WildcardAccessHelper.getName(w) != null
                  && name.name.equals(WildcardAccessHelper.getName(w).name)) {
                int pos = listField.indexOf(expr);
                boolean middle = false;
                for (int i = listField.size() - 1; i >= pos; i--) {
                  if (!WildcardAccessHelper.isWildcard(expr)) {
                    middle = true;
                  }
                }
                if (!middle) {
                  toRemove.add(expr);
                  break;
                } else {
                  WildcardAccessHelper.setName(expr, null);
                }
              }
            }
          }
        }
      }
      if (toRemove.size() == 0) {
        return null;
      } else {
        LinkedList<AbstractBastNode> newNodes = new LinkedList<>();
        newNodes.addAll(listField);
        newNodes.removeAll(toRemove);
        return new BastField(newNodes);
      }
    }
    return null;
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
