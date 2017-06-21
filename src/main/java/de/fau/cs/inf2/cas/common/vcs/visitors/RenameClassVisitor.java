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
import de.fau.cs.inf2.cas.common.bast.general.CreateJavaNodeHelper;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.visitors.DefaultFieldVisitor;

import java.util.LinkedList;

public class RenameClassVisitor extends DefaultFieldVisitor {

  private String oldName;
  private String newName;

  /**
   * Instantiates a new rename class visitor.
   *
   * @param oldName the old name
   * @param newName the new name
   */
  public RenameClassVisitor(String oldName, String newName) {
    this.oldName = oldName;
    this.newName = newName;
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastClassDecl node) {
    if (((BastNameIdent) node.getField(BastFieldConstants.CLASS_DECL_NAME).getField()).name
        .equals(oldName)) {
      BastNameIdent newNameIdent = CreateJavaNodeHelper.createBastNameIdent(" " + newName);
      node.replaceField(BastFieldConstants.CLASS_DECL_NAME, new BastField(newNameIdent));
      node.replaceField(BastFieldConstants.CLASS_DECL_MODIFIERS,
          new BastField(new LinkedList<AbstractBastNode>()));
      node.replaceField(BastFieldConstants.CLASS_DECL_TYPE_PARAMETERS,
          new BastField(new LinkedList<AbstractBastNode>()));
      node.replaceField(BastFieldConstants.CLASS_DECL_EXTENDED_CLASS,
          new BastField((AbstractBastNode) null));
      node.replaceField(BastFieldConstants.CLASS_DECL_INTERFACES,
          new BastField(new LinkedList<AbstractBastNode>()));
      for (int i = 0; i < node.info.tokens.length; i++) {
        if (i == 1 || i == 2) {
          node.info.tokens[i] = null;
        }
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
