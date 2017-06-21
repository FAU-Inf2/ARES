package de.fau.cs.inf2.cas.common.bast.diff;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public interface IConfiguration {
  public Map<AbstractBastNode, AbstractBastNode> getParents1();

  public Map<AbstractBastNode, AbstractBastNode> getParents2();

  public AbstractBastNode getTree1();

  public AbstractBastNode getTree2();

  public INode getWrappedTree1();

  public INode getWrappedTree2();

  public double getLeafThreshold();

  public double getWeightSimilarity();

  public double getWeightPosition();

  public double getWeightInnerValueSim();

  public double getWeightInnerChildrenSim();

  public double getInnerNodeThreshold();
  
  public double getInnerNodeNonEqualSimilarity();

  public LabelConfiguration getLabelConfiguration();

  public int getNumThreads();

  public Iterator<AbstractBastNode> getTreeIterator(IterationOrder order, AbstractBastNode tree);

  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getLeavesMap1();

  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getDirectChildrenMap1();
  
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getLeavesMap2();

  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getDirectChildrenMap2();
}
