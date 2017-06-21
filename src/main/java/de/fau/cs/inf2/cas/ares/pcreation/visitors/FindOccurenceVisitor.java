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

import de.fau.cs.inf2.cas.ares.bast.general.ParserFactory;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.CreateJavaNodeHelper;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.visitors.IPrettyPrinter;

import java.util.LinkedList;
import java.util.regex.Pattern;

public class FindOccurenceVisitor extends AresDefaultFieldVisitor {

  private String toFind = null;
  private AbstractBastNode nodeToFind;
  public int count = 0;
  private AbstractBastNode startNode = null;

  /**
   * Instantiates a new find occurence visitor.
   *
   * @param nodeToFind the node to find
   * @param clone the clone
   */
  public FindOccurenceVisitor(AbstractBastNode nodeToFind, AbstractBastNode clone) {
    IPrettyPrinter printer = ParserFactory.getPrettyPrinter();
    clone.accept(printer);
    toFind = printer.getBuffer().toString();
    this.nodeToFind = nodeToFind;
  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    if (startNode == null) {
      startNode = node;
    }
    if (node == nodeToFind) {
      if (globalParent.getField(fieldId).isList()) {
        BastField field = globalParent.getField(fieldId);
        LinkedList<AbstractBastExpr> nodeList = new LinkedList<>();
        for (AbstractBastNode node2 : field.getListField()) {
          if (node2 == nodeToFind) {
            nodeList.add(CreateJavaNodeHelper.createBastNameIdent("#ARES#"));
          } else {
            nodeList.add((AbstractBastExpr) node2);
          }
        }
        BastField tmpField = new BastField(nodeList);
        globalParent.replaceField(fieldId, tmpField);
        IPrettyPrinter printer = ParserFactory.getPrettyPrinter();
        startNode.accept(printer);
        String[] tmp = printer.getBuffer().toString().split("#ARES#");
        assert (tmp.length > 1);
        count = tmp[0].split(Pattern.quote(toFind), -1).length;
        globalParent.replaceField(fieldId, field);
      } else {
        final BastField field = globalParent.getField(fieldId);
        BastField tmpField = null;
        if (fieldId == BastFieldConstants.TYPE_SPECIFIER_TYPE
            || fieldId == BastFieldConstants.ARRAY_TYPE_TYPE
            || fieldId == BastFieldConstants.TYPE_ARGUMENT_TYPE
            || fieldId == BastFieldConstants.NEW_CLASS_TYPE) {
          tmpField = new BastField(CreateJavaNodeHelper.createBastClassType("#ARES#"));
        } else {
          tmpField = new BastField(CreateJavaNodeHelper.createBastNameIdent("#ARES#"));
        }
        globalParent.replaceField(fieldId, tmpField);
        IPrettyPrinter printer = ParserFactory.getAresPrettyPrinter();
        startNode.accept(printer);
        String[] tmp = printer.getBuffer().toString().split("#ARES#");
        assert (tmp.length > 1);
        count = tmp[0].split(Pattern.quote(toFind), -1).length;
        globalParent.replaceField(fieldId, field);

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

}
