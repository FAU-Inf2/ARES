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
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.LinkedList;

public class RemoveEnclosedChoices extends AresDefaultFieldVisitor {

  private ExtendedDiffResult extDiff = null;
  
  /**
   * Instantiates a new removes the duplicate choice visitor.
   *
   */
  public RemoveEnclosedChoices(ExtendedDiffResult extDiff) {
    this.extDiff = extDiff;
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
            boolean changed = true;
            while (changed) {
              changed = false;
              globalParent = node;
              fieldId = constant;
              BastField field = null;
              field = removeChoices(node.fieldMap.get(constant).getListField());
              if (field != null) {
                node.replaceField(constant, field);
                changed = true;
              }
            }
          }
          for (AbstractBastNode expr : node.fieldMap.get(constant).getListField()) {
            setVariables(constant, node, counter++);
            expr.accept(this);
          }
        }
      } else if (node.fieldMap.get(constant).getField() != null) {
        globalParent = node;
        listId = -1;
        fieldId = constant;
        node.fieldMap.get(constant).getField().accept(this);
      }
    }
  }

  private BastField removeChoices(LinkedList<? extends AbstractBastNode> listField) {
    int containsChoices = 0;
    int containsUses = 0;

    for (AbstractBastNode expr : listField) {
      if (expr.getTag() == AresChoiceStmt.TAG) {
        containsChoices++;
      }
      if (expr.getTag() == AresUseStmt.TAG) {
        containsUses++;
      }
    }
    if (containsChoices > 0 && containsUses > 1) {
      outer: for (int i = 0; i < listField.size(); i++) {
        if (listField.get(i).getTag() == AresUseStmt.TAG) {
          AbstractBastNode use1 = listField.get(i);

          for (int j = i + 1; j < listField.size(); j++) {
            if (listField.get(j).getTag() == AresUseStmt.TAG) {
              if (WildcardAccessHelper.getName(use1) != null 
                  && WildcardAccessHelper.getName(use1).name
                  .equals(WildcardAccessHelper.getName(listField.get(j)).name)) {
                LinkedList<AbstractBastNode> toRemove = new LinkedList<AbstractBastNode>();

                for (int k = i + 1; k <= j; k++) {
                  toRemove.add(listField.get(k));
                }
                if (i > 0) {
                  if (listField.get(i - 1).getTag() == AresChoiceStmt.TAG) {
                    toRemove.add(listField.get(i - 1));
                  }
                }
                LinkedList<AbstractBastNode> newNodes = new LinkedList<>();
                newNodes.addAll(listField);
                newNodes.removeAll(toRemove);
                return new BastField(newNodes);
              } else {
                continue outer;
              }
            } else if (listField.get(j).getTag() == AresChoiceStmt.TAG) {
              continue;
            } else {
              if (extDiff.editMapNew.get(listField.get(j)) != null
                  && extDiff.editMapNew.get(listField.get(j)).getType() 
                  == EditOperationType.INSERT) {
                continue;
              } else {
                continue outer;
              }
            }
          }


        }
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
