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
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;

import java.util.ArrayList;
import java.util.HashMap;

public class GetMethodsVisitor extends DefaultFieldVisitor {

  public HashMap<String, ArrayList<BastFunction>> functionMap =
      new HashMap<String, ArrayList<BastFunction>>();
  public HashMap<Integer, BastFunction> functionIdMap = new HashMap<Integer, BastFunction>();
  public HashMap<BastFunction, Integer> idMap2Function = new HashMap<BastFunction, Integer>();

  private int id = 0;

  /**
   * Instantiates a new gets the methods visitor.
   */
  public GetMethodsVisitor() {

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
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastFunction node) {
    functionIdMap.put(id, node);
    idMap2Function.put(node, id);
    id++;
    if (((BastFunction) node).name == null) {
      return;
    }
    ArrayList<BastFunction> functions = functionMap.get(((BastFunction) node).name);
    if (functions == null) {
      functions = new ArrayList<BastFunction>();
      functionMap.put(((BastFunction) node).name, functions);
    }
    functions.add(node);
    super.visit(node);
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
