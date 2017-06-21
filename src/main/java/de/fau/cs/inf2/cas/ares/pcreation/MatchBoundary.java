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
