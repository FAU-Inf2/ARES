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

package de.fau.cs.inf2.cas.ares.bast.general;

import de.fau.cs.inf2.cas.ares.bast.diff.AresTreeIterator;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresParentRelationMappingVisitor;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresPostOrderSetGenerationNewVisitor;

import de.fau.cs.inf2.cas.common.bast.diff.IConfiguration;
import de.fau.cs.inf2.cas.common.bast.diff.IterationOrder;
import de.fau.cs.inf2.cas.common.bast.diff.LabelConfiguration;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAccess;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAdditiveExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastArrayDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAsgnExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBoolConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCharConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCmp;
import de.fau.cs.inf2.cas.common.bast.nodes.BastForStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastImportDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastLabelStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastListInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastMultiExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNew;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameter;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameterList;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.nodes.BastRealConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastShift;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStorageClassSpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStringConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStructDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStructMember;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeQualifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastUnaryExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastWhileStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.INode;
import de.fau.cs.inf2.cas.common.bast.type.BastArrayType;
import de.fau.cs.inf2.cas.common.bast.type.BastBasicType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class AresConfiguration implements IConfiguration {
  
  /**
   * todo.
   *
   *<p>Mapping to retrieve the parents of any node from the first tree.
   */
  private Map<AbstractBastNode, AbstractBastNode> parents1;

  /**
   * todo.
   *
   *<p>Mapping to retrieve the parents of any node from the second tree.
   */
  private Map<AbstractBastNode, AbstractBastNode> parents2;
  
  private final LabelConfiguration labelConfiguration;
  
  public LabelConfiguration getLabelConfiguration() {
    return labelConfiguration;
  }
  
  private INode wrappedTree1;
  private INode wrappedTree2;
  
  private HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> leavesMap1;

  private HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> leavesMap2;
  HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> childrenMap1 = null;
  HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> childrenMap2 = null;
  
  /**
   * Inits the wrapper.
   *
   * @param tree1 the tree1
   * @param tree2 the tree2
   */
  private void initWrapper(final AbstractBastNode tree1, final AbstractBastNode tree2) {
    final HashMap<AbstractBastNode, INode> wrapperMap1 = new HashMap<>();
    final HashMap<AbstractBastNode, INode> wrapperMap2 = new HashMap<>();
    AresPostOrderSetGenerationNewVisitor setGenerator = new AresPostOrderSetGenerationNewVisitor();
    setGenerator.createSetsForNode(tree1);
    childrenMap1 = setGenerator.getDirectChildrenMap();
    leavesMap1 = setGenerator.getLeaveMap();

    wrappedTree1 = new AresWrapper(tree1, wrapperMap1, childrenMap1);
    setGenerator = new AresPostOrderSetGenerationNewVisitor();
    setGenerator.createSetsForNode(tree2);
    childrenMap2 = 
        setGenerator.getDirectChildrenMap();
    leavesMap2 = setGenerator.getLeaveMap();

    wrappedTree2 = new AresWrapper(tree2, wrapperMap2, childrenMap2);
  }
  
  private AbstractBastNode tree1;
  private AbstractBastNode tree2;
  private final double leafThreshold;
  private final double weightSimilarity;
  private final double weightPosition;
  private final double weightInnerValueSim;
  private final double weightInnerChildrenSim;
  private final double innerNodeThreshold;
  private final double innerNodeNonEqualSimilarity;
  private final int numThreads;
  
  /**
   * Instantiates a new bast configuration.
   *
   * @param tree1 the tree1
   * @param tree2 the tree2
   * @param leafThreshold the leaf threshold
   * @param weightSimilarity the weight similarity
   * @param weightPosition the weight position
   * @param weightInnerValueSim the weight inner value sim
   * @param weightInnerChildrenSim the weight inner children sim
   * @param innerNodeThreshold the inner node threshold
   * @param numThreads the num threads
   */
  public AresConfiguration(final AbstractBastNode tree1, final AbstractBastNode tree2,
      double leafThreshold, double weightSimilarity, double weightPosition,
      double weightInnerValueSim, double weightInnerChildrenSim,
      double innerNodeThreshold, double innerNodeNonEqualSimilarity, int numThreads) {
    this.leafThreshold = leafThreshold;
    this.weightSimilarity = weightSimilarity;
    this.weightPosition = weightPosition;
    this.weightInnerValueSim = weightInnerValueSim;
    this.weightInnerChildrenSim = weightInnerChildrenSim;
    this.innerNodeThreshold = innerNodeThreshold;
    this.innerNodeNonEqualSimilarity = innerNodeNonEqualSimilarity;
    this.numThreads = numThreads;
    
    final AresParentRelationMappingVisitor parentMapper = new AresParentRelationMappingVisitor();
    parents1 = parentMapper.getMapOfParentRelationships(tree1);
    parents2 = parentMapper.getMapOfParentRelationships(tree2);
    HashSet<Integer> labelsForValueCompare = new HashSet<Integer>();

    labelsForValueCompare.addAll(Arrays.asList(new Integer[] { BastCharConst.TAG, BastUnaryExpr.TAG,
        BastBasicType.TAG, BastTypeQualifier.TAG, BastStorageClassSpecifier.TAG, BastAsgnExpr.TAG,
        BastBlock.TAG, BastCastExpr.TAG, BastCmp.TAG, BastMultiExpr.TAG, BastShift.TAG,
        BastAccess.TAG, BastForStmt.TAG, BastParameterList.TAG, BastArrayDeclarator.TAG,
        BastWhileStatement.TAG, BastUnaryExpr.TAG, BastImportDeclaration.TAG, BastAdditiveExpr.TAG,
        BastArrayType.TAG, BastNew.TAG, BastStorageClassSpecifier.TAG, BastListInitializer.TAG}));
    HashSet<Integer> labelsForRealCompare = new HashSet<Integer>();
    labelsForRealCompare.addAll(Arrays.asList(new Integer[] { BastRealConst.TAG}));

    HashSet<Integer> labelsForIntCompare = new HashSet<Integer>();
    labelsForIntCompare.addAll(Arrays.asList(new Integer[] { BastIntConst.TAG}));

    HashSet<Integer> labelsForStringCompare = new HashSet<Integer>();
    labelsForStringCompare.addAll(
        Arrays.asList(new Integer[] { BastNameIdent.TAG, BastParameter.TAG, BastFunction.TAG,
            BastStructDecl.TAG, BastStructMember.TAG, BastLabelStmt.TAG, BastStringConst.TAG}));

    HashSet<Integer> labelsForBoolCompare = new HashSet<Integer>();
    labelsForBoolCompare.addAll(Arrays.asList(new Integer[] { BastBoolConst.TAG}));

    labelConfiguration =
        new LabelConfiguration(BastNameIdent.TAG, BastProgram.TAG, BastClassDecl.TAG,
            BastBasicType.TAG, BastTypeQualifier.TAG, labelsForValueCompare, labelsForRealCompare,
            labelsForIntCompare, labelsForStringCompare, labelsForBoolCompare);
    initWrapper(tree1, tree2);
    this.tree1 = tree1;
    this.tree2 = tree2;
  }

  @Override
  public Map<AbstractBastNode, AbstractBastNode> getParents1() {
    return parents1;
  }

  @Override
  public Map<AbstractBastNode, AbstractBastNode> getParents2() {
    return parents2;
  }

  @Override
  public AbstractBastNode getTree1() {
    return tree1;
  }

  @Override
  public AbstractBastNode getTree2() {
    return tree2;
  }

  @Override
  public INode getWrappedTree1() {
    return wrappedTree1;
  }

  @Override
  public INode getWrappedTree2() {
    return wrappedTree2;
  }

  @Override
  public double getLeafThreshold() {
    return leafThreshold;
  }

  @Override
  public double getWeightSimilarity() {
    return weightSimilarity;
  }

  @Override
  public double getWeightPosition() {
    return weightPosition;
  }

  @Override
  public double getWeightInnerValueSim() {
    return weightInnerValueSim;
  }

  @Override
  public double getWeightInnerChildrenSim() {
    return weightInnerChildrenSim;
  }

  @Override
  public double getInnerNodeThreshold() {
    return innerNodeThreshold;
  }

  @Override
  public int getNumThreads() {
    return numThreads;
  }

  @Override
  public AresTreeIterator getTreeIterator(IterationOrder order, AbstractBastNode tree) {
    return new AresTreeIterator(order, tree);
  }

  @Override
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getLeavesMap1() {
    return leavesMap1;
  }

  @Override
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getDirectChildrenMap1() {
    return childrenMap1;
  }

  @Override
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getLeavesMap2() {
    return leavesMap2;
  }

  @Override
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getDirectChildrenMap2() {
    return childrenMap2;
  }

  @Override
  public double getInnerNodeNonEqualSimilarity() {
    return innerNodeNonEqualSimilarity;
  }
  
}
