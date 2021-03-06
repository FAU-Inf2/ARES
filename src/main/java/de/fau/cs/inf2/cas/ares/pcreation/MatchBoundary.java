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

package de.fau.cs.inf2.cas.ares.pcreation;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.CreateJavaNodeHelper;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

public class MatchBoundary {

  MatchBoundary(MatchBoundary boundary) {
    this.node1 = boundary.node1;
    this.node2 = boundary.node2;
    this.copyNode1 = CreateJavaNodeHelper.cloneTree(node1);
    this.copyNode2 = CreateJavaNodeHelper.cloneTree(node2);
    this.field1 = boundary.field1;
    this.field2 = boundary.field2;
    if (boundary.node1.getField(boundary.field1) == null) {
      assert (false);
    }
  }
  
  MatchBoundary(AbstractBastNode nodeBefore1, AbstractBastNode nodeBefore2,
      BastFieldConstants field1, BastFieldConstants field2, boolean update) {
    this.node1 = nodeBefore1;
    this.node2 = nodeBefore2;
    this.copyNode1 = CreateJavaNodeHelper.cloneTree(node1);
    this.copyNode2 = CreateJavaNodeHelper.cloneTree(node2);
    this.field1 = field1;
    this.field2 = field2;
    if (node1.getField(field1) == null) {
      assert (false);
    }
    this.update = update;
  }
  
  public boolean update = false;
  private AbstractBastNode copyNode1;
  AbstractBastNode copyNode2;
  private AbstractBastNode node1;
  private AbstractBastNode node2;
  public BastFieldConstants field1;
  public BastFieldConstants field2;
  
  public void setNode1(AbstractBastNode node1) {
    this.node1 = node1;
    copyNode1 = CreateJavaNodeHelper.cloneTree(node1);
  }
  
  public void setNode2(AbstractBastNode node2) {
    this.node2 = node2;
    copyNode2 = CreateJavaNodeHelper.cloneTree(node2);
  }
  
  public AbstractBastNode getNode1() {
    return node1;
  }
  
  public AbstractBastNode getNode2() {
    return node2;
  }
  
  public AbstractBastNode getCopyNode1() {
    return copyNode1;
  }
  
  public AbstractBastNode getCopyNode2() {
    return copyNode2;
  }
}
