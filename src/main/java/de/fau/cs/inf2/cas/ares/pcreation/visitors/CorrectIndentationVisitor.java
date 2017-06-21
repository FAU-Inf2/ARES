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
import de.fau.cs.inf2.cas.ares.pcreation.PatternGenerator;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.CreateJavaNodeHelper;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;

import de.fau.cs.inf2.cas.common.parser.odin.JavaToken;


public class CorrectIndentationVisitor extends AresDefaultFieldVisitor {
  
  private final String indentToRemove;
  
  public CorrectIndentationVisitor(String indentToRemove) {
    this.indentToRemove = indentToRemove;
  }
  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    if (this.globalParent != null && this.globalParent.getTag() == BastBlock.TAG) {
      JavaToken leftToken = CreateJavaNodeHelper.findLeftJavaToken(node);
      adjustWhitespace(node, leftToken);
    }
  }

  private void adjustWhitespace(AbstractBastNode node, JavaToken leftToken) {
    StringBuffer buffer = PatternGenerator.extractIndentation(node);
    int index = buffer.lastIndexOf(indentToRemove);
    if (index != -1) {
      String replacement = buffer.substring(0, index);
      if (leftToken != null) {
        leftToken.whiteSpace.setLength(0);
        leftToken.whiteSpace.append(replacement);
      }
      leftToken = CreateJavaNodeHelper.findLeftJavaToken(node, true);
      if (leftToken != null) {
        leftToken.whiteSpace.setLength(0);
        leftToken.whiteSpace.append(replacement);
      }
    }
  }
  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastBlock node) {
    beginVisit(node);
    if (this.globalParent != null) {
      if (node.info.tokens[2] != null
          && node.info.tokens[2].token != null) {
        JavaToken tmp = (JavaToken)node.info.tokens[2].token;
        int index = tmp.whiteSpace.lastIndexOf(indentToRemove);
        if (index != -1) {
          String replacement = tmp.whiteSpace.substring(0, index);
          tmp.whiteSpace.setLength(0);
          tmp.whiteSpace.append(replacement);
        }
      }
    }
    standardVisit(BastFieldConstants.BLOCK_MODIFIERS, node);
    standardVisit(BastFieldConstants.BLOCK_STATEMENT, node);
    endVisit(node);
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
