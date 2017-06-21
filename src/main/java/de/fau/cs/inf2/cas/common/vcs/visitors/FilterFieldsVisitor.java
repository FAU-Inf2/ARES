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

package de.fau.cs.inf2.cas.common.vcs.visitors;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastInternalDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.visitors.DefaultFieldVisitor;

import java.util.Iterator;
import java.util.LinkedList;

public class FilterFieldsVisitor extends DefaultFieldVisitor {

  /**
   * Instantiates a new filter fields visitor.
   */
  public FilterFieldsVisitor() {}

  private boolean remove = false;

  
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
          LinkedList<AbstractBastNode> nodesToRemove = null;
          for (AbstractBastNode expr : node.fieldMap.get(constant).getListField()) {
            setVariables(constant, node, counter++);
            expr.accept(this);
            if (remove) {
              if (nodesToRemove == null) {
                nodesToRemove = new LinkedList<>();
              }
              nodesToRemove.add(expr);
              remove = false;
            }
          }
          if (nodesToRemove != null) {
            if (constant.equals(BastFieldConstants.CLASS_DECL_DECLARATIONS)) {
              LinkedList<AbstractBastNode> newNodes = new LinkedList<>();
              newNodes.addAll(node.fieldMap.get(constant).getListField());
              newNodes.removeAll(nodesToRemove);
              node.replaceField(constant, new BastField(newNodes));
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
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastClassDecl node) {
    @SuppressWarnings("unchecked")
    LinkedList<AbstractBastInternalDecl> declarations = (LinkedList<AbstractBastInternalDecl>) node
        .getField(BastFieldConstants.CLASS_DECL_DECLARATIONS).getListField();
    if (declarations != null) {
      Iterator<AbstractBastInternalDecl> it = declarations.iterator();
      while (it.hasNext()) {
        AbstractBastInternalDecl decl = it.next();
        if (decl.getTag() != BastFunction.TAG) {
          it.remove();
        }
      }
      node.replaceField(BastFieldConstants.CLASS_DECL_DECLARATIONS, new BastField(declarations));
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
