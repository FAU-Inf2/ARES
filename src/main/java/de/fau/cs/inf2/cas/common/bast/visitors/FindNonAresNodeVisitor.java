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

package de.fau.cs.inf2.cas.common.bast.visitors;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAsgnExpr;

class FindNonAresNodeVisitor extends AresDefaultFieldVisitor {

  /**
   * Instantiates a new find non ARES node visitor.
   */
  public FindNonAresNodeVisitor() {

  }

  AbstractBastNode foundNode = null;

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresWildcard node) {
    if (node.statements != null) {
      for (AbstractBastStatement stmt : node.statements) {
        stmt.accept(this);
      }
    }
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAsgnExpr node) {
    foundNode = node;
    return;
  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    if (foundNode == null) {
      foundNode = node;
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

}