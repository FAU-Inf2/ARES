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

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEnumDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEnumMember;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastInterfaceDecl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

public class GetInnerClassesVisitor extends DefaultFieldVisitor {

  private HashMap<BastFunction, ArrayList<String>> function2Classes =
      new HashMap<BastFunction, ArrayList<String>>();
  public HashMap<BastFunction, ArrayList<AbstractBastNode>> function2ClassNodes =
      new HashMap<BastFunction, ArrayList<AbstractBastNode>>();

  private LinkedList<String> classList = new LinkedList<>();
  private LinkedList<AbstractBastNode> classNodeList = new LinkedList<>();

  
  private static final boolean DEBUG = false;

  /**
   * Instantiates a new gets the inner classes visitor.
   */
  public GetInnerClassesVisitor() {
  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    if (node.getTag() == BastClassDecl.TAG) {
      classList.add(((BastClassDecl) node).name.name);
      classNodeList.add(node);
    } else if (node.getTag() == BastInterfaceDecl.TAG) {
      classList.add(((BastInterfaceDecl) node).name.name);
      classNodeList.add(node);
    } else if (node.getTag() == BastEnumDecl.TAG) {
      classList.add(((BastEnumDecl) node).enumerator.name.name);
      classNodeList.add(node);
    } else if (node.getTag() == BastEnumMember.TAG) {
      classList.add(((BastEnumMember) node).identifier.name);
      classNodeList.add(node);
    }
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastFunction node) {
    ArrayList<String> names = new ArrayList<>();
    ArrayList<AbstractBastNode> parentNodes = new ArrayList<>();
    parentNodes.addAll(classNodeList);
    names.addAll(classList);
    function2Classes.put(node, names);
    function2ClassNodes.put(node, parentNodes);
    super.visit(node);
  }

  
  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {
    if (node.getTag() == BastClassDecl.TAG) {
      classList.removeLast();
      classNodeList.removeLast();
    } else if (node.getTag() == BastInterfaceDecl.TAG) {
      classList.removeLast();
      classNodeList.removeLast();
    } else if (node.getTag() == BastEnumDecl.TAG) {
      classList.removeLast();
      classNodeList.removeLast();
    } else if (node.getTag() == BastEnumMember.TAG) {
      classList.removeLast();
      classNodeList.removeLast();
    }

  }

  /**
   * Gets the class string.
   *
   * @param node the node
   * @return the class string
   */
  public String getClassString(BastFunction node) {
    ArrayList<String> names = function2Classes.get(node);
    if (DEBUG && names == null) {
      System.err.println(node.name);
      for (Entry<BastFunction, ArrayList<String>> e : function2Classes.entrySet()) {
        System.err.print(e.getKey().name + ": ");
        for (String s : e.getValue()) {
          System.err.print(s + ", ");
        }
        System.err.println();
      }
    }
    if (names == null) {
      return null;
    }
    assert (names.size() >= 1);
    if (names.size() <= 1) {
      return null;
    }
    String string = "";
    for (int i = 1; i < names.size(); i++) {
      string += names.get(i);
      if (i != names.size() - 1) {
        string += ".";
      }
    }
    return string;
  }
}
