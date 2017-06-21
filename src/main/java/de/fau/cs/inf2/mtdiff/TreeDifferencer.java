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

package de.fau.cs.inf2.mtdiff;

import de.fau.cs.inf2.cas.common.bast.diff.IConfiguration;
import de.fau.cs.inf2.cas.common.bast.diff.INodeTreeIterator;
import de.fau.cs.inf2.cas.common.bast.diff.IterationOrder;
import de.fau.cs.inf2.cas.common.bast.diff.LabelConfiguration;
import de.fau.cs.inf2.cas.common.bast.diff.ValueRetriever;
import de.fau.cs.inf2.cas.common.bast.general.INodeWrapper;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.ChildrenList;
import de.fau.cs.inf2.cas.common.util.ChildrenRetriever;
import de.fau.cs.inf2.cas.common.util.ComparePair;
import de.fau.cs.inf2.cas.common.util.IndexFinder;
import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.editscript.ChildrenAligner;
import de.fau.cs.inf2.mtdiff.editscript.ChildrenAlignerINode2;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.DeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.MoveOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.UpdateOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.inode.INodeDeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.inode.INodeEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.inode.INodeExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.inode.INodeInsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.inode.INodeMoveOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.inode.INodeUpdateOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A class providing methods to compute the edit distance between two annotated abstract syntax
 * trees.
 * 
 */
public final class TreeDifferencer implements ITreeDifferencer {
  // Based on "Change Distilling: Tree Differencing for Fine-Grained Source
  // Code Change Detection" by Fluri et al

  private final double leafThreshold;
  private AtomicLong extendedCdTime = new AtomicLong(0);
  private AtomicLong extendedInnerMatchingTime = new AtomicLong(0);
  private AtomicLong largeLeafDiff = new AtomicLong(0);
  private ExecutorService executioner;

  /**
   * Instantiates a new tree differencer.
   *
   * @param leafThreshold the leaf threshold
   */
  public TreeDifferencer(double leafThreshold, int numThreads) {
    this(Executors.newFixedThreadPool(numThreads), leafThreshold);
  }

  /**
   * Instantiates a new tree differencer.
   *
   * @param executorService the executor service
   * @param leafThreshold the leaf threshold
   */
  public TreeDifferencer(ExecutorService executorService, double leafThreshold) {
    this.executioner = executorService;
    this.leafThreshold = leafThreshold;
  }


  /**
   * Gets the single instance of TreeDifferencer.
   *
   * @return single instance of TreeDifferencer
   */
  public ITreeDifferencer getInstance() {
    return new TreeDifferencer(executioner, leafThreshold);
  }


  /**
   * Shutdown.
   */
  public void shutdown() {
    executioner.shutdownNow();
  }

  public static class TreeDiffTimeoutException extends RuntimeException {

    /**
     * todo.
     */
    private static final long serialVersionUID = -5236979612425887682L;

  }



  /**
   * A class providing an easy and standardized way to retrieve the children of any tree node.
   */
  private final ChildrenRetriever childrenGetter = new ChildrenRetriever();

  private HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> leavesMap1 = null;
  private HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> leavesMap2 = null;
  private HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> directChildrenMap1 = null;
  private HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> directChildrenMap2 = null;

  /**
   * Computes the edit script. Includes the necessary operations to turn the annotated abstract
   * syntax tree tree1 into tree2.
   * 
   * @return A {@see List} of {@see BastEditOperation}s which would turn tree1 into tree2
   * @throws Exception todo
   */
  public ExtendedDiffResult computeDifference(String filename, IConfiguration configuration)
      throws Exception {
    final Set<ComparePair<INode>> matchingNodes = computeNodePairs(configuration);
    Set<ComparePair<AbstractBastNode>> resultNodes = new HashSet<>();
    for (ComparePair<INode> pair : matchingNodes) {
      resultNodes
          .add(new ComparePair<AbstractBastNode>(((INodeWrapper) pair.getOldElement()).getNode(),
              ((INodeWrapper) pair.getNewElement()).getNode()));
    }

    leavesMap1 = configuration.getLeavesMap1();
    directChildrenMap1 = configuration.getDirectChildrenMap1();
    leavesMap2 = configuration.getLeavesMap2();
    directChildrenMap2 = configuration.getDirectChildrenMap2();

    return createEditScript(filename, configuration.getTree1(), configuration.getTree2(),
        resultNodes, configuration.getParents1(), configuration.getParents2(), childrenGetter,
        configuration.getTreeIterator(IterationOrder.BREADTH_FIRST, configuration.getTree2()),
        configuration.getTreeIterator(IterationOrder.DEPTH_FIRST_POST_ORDER,
            configuration.getTree1()));
  }

  /**
   * Compute node pairs.
   *
   * @return the sets the
   * @throws Exception the exception
   */
  public Set<ComparePair<INode>> computeNodePairs(IConfiguration configuration) throws Exception {
    ITreeMatcher matcher = new TreeMatcher(new TreeMatcherConfiguration(
        configuration.getLeafThreshold(), configuration.getWeightSimilarity(),
        configuration.getWeightPosition(), configuration.getWeightInnerValueSim(),
        configuration.getWeightInnerChildrenSim(), configuration.getInnerNodeThreshold(),
        configuration.getInnerNodeNonEqualSimilarity()));
    matcher.initAres(executioner, leafThreshold, configuration.getNumThreads());

    final LabelConfiguration labelConfiguration = configuration.getLabelConfiguration();


    matcher.init(configuration.getWrappedTree1(), configuration.getWrappedTree2(),
        labelConfiguration);
    matcher.computeMatchingPairs();

    final Set<ComparePair<INode>> matchingNodes = matcher.getMatchingPairs();
    matchingNodes.add(
        new ComparePair<INode>(configuration.getWrappedTree1(), configuration.getWrappedTree2()));
    return matchingNodes;
  }

  /**
   * Creates the edit operations.
   *
   * @param tree1 the tree1
   * @param tree2 the tree2
   * @param matchingNodes the matching nodes
   * @return the i node extended diff result
   */
  public static INodeExtendedDiffResult createEditOperations(final INode tree1, final INode tree2,
      final Set<ComparePair<INode>> matchingNodes) {
    IdentityHashMap<INode, INode> parents1 = TreeMatcher.getParents(tree1);
    IdentityHashMap<INode, INode> parents2 = TreeMatcher.getParents(tree2);
    final List<INodeEditOperation> editScript = new LinkedList<>();

    // Create mappings for each matched node of tree1 and tree2 which map
    // any
    // matched node to its partner of the other tree
    final Map<INode, INode> partnersInTree2 = new HashMap<>();
    final Map<INode, INode> partnersInTree1 = new HashMap<>();
    for (final ComparePair<INode> matchedPair : matchingNodes) {
      partnersInTree2.put((INode) matchedPair.getOldElement(), (INode) matchedPair.getNewElement());
      partnersInTree1.put((INode) matchedPair.getNewElement(), (INode) matchedPair.getOldElement());
    }

    final ChildrenAlignerINode2 childrenAligner = new ChildrenAlignerINode2(partnersInTree2);

    // First do a breadth-first traversal of tree2 to find update, insert,
    // move and align operations
    INodeTreeIterator iterator =
        new INodeTreeIterator(IterationOrder.BREADTH_FIRST, tree2);
    while (iterator.hasNext()) {
      final INode node = iterator.next();
      final INode partner = (INode) partnersInTree1.get(node);
      if (partner != null) {
        // node and partner have been matched
        if (node.getId() != partner.getId()) {
          assert (node.getId() == partner.getId());
        }

        final INode nodeParent = parents2.get(node);
        final INode partnerParent = parents1.get(partner);
        // Update phase:
        // Check, whether values differ
        if (!node.getLabel().equals(partner.getLabel()) && nodeParent != null) {
          if (partnerParent == partnersInTree1.get(nodeParent)
              || partnersInTree1.get(nodeParent) == null) {
            editScript.add(new INodeUpdateOperation(partnerParent, nodeParent, partner, node,
                IndexFinder.getIndex(partner, partnerParent),
                IndexFinder.getIndex(node, nodeParent)));
          } else if (partnerParent == null) {
            assert (false);
          }
        }

        // Move phase:
        if (nodeParent != null && partnerParent != null && nodeParent != null) {
          if (partnersInTree1.get(nodeParent) != partnerParent) {
            editScript.add(new INodeMoveOperation(partnerParent, nodeParent, partner, node,
                IndexFinder.getIndex(partner, partnerParent),
                IndexFinder.getIndex(node, nodeParent)));
          } else {
            NodeIndex partnerIndex = IndexFinder.getIndex(partner, partnerParent);
            NodeIndex nodeIndex = IndexFinder.getIndex(node, nodeParent);
            if (partnerIndex != null && nodeIndex != null
                && partnerIndex.childrenListNumber != nodeIndex.childrenListNumber) {
              editScript.add(new INodeMoveOperation(partnerParent, nodeParent, partner, node,
                  IndexFinder.getIndex(partner, partnerParent),
                  IndexFinder.getIndex(node, nodeParent)));
            }

          }
        }

        // Align phase:
        final List<INode> nodeChildren = node.getChildrenWrapped();
        final List<INode> partnerChildren = partner.getChildrenWrapped();

        if ((nodeChildren != null) && (partnerChildren != null) && partner != null) {
          childrenAligner.alignChildren(partner, partnerChildren, nodeChildren, editScript);
        }
      } else {
        // Insert phase:
        final INode parent = parents2.get(node);
        if (!parents2.containsKey(node)) {
          assert (false);
        }
        editScript.add(new INodeInsertOperation(parent, node, IndexFinder.getIndex(node, parent)));
      }
    }

    // Now do a post-order traversal of tree1 to find delete operations
    iterator = new INodeTreeIterator(IterationOrder.DEPTH_FIRST_POST_ORDER, tree1);
    while (iterator.hasNext()) {
      final INode node = iterator.next();
      final INode partner = partnersInTree2.get(node);

      if (partner == null) {
        // Delete phase:
        final INode parent = parents1.get(node);
        assert parent != null;
        editScript.add(new INodeDeleteOperation(parent, node, IndexFinder.getIndex(node, parent)));
      }
    }

    return new INodeExtendedDiffResult(editScript, partnersInTree1, partnersInTree2);
  }

  /**
   * Creates the edit script.
   *
   * @param tree1 the tree1
   * @param tree2 the tree2
   * @param matchingNodes the matching nodes
   * @param parents1 the parents1
   * @param parents2 the parents2
   * @param childrenGetter the children getter
   * @return the extended diff result
   */
  public static ExtendedDiffResult createEditScript(String filename, final AbstractBastNode tree1,
      final AbstractBastNode tree2, final Set<ComparePair<AbstractBastNode>> matchingNodes,
      Map<AbstractBastNode, AbstractBastNode> parents1,
      Map<AbstractBastNode, AbstractBastNode> parents2, ChildrenRetriever childrenGetter,
      Iterator<AbstractBastNode> iteratorBreathFirst2, Iterator<AbstractBastNode> depthPostOrder1) {

    final List<BastEditOperation> editScript = new LinkedList<>();

    // Create mappings for each matched node of tree1 and tree2 which map
    // any
    // matched node to its partner of the other tree
    final Map<AbstractBastNode, AbstractBastNode> partnersInTree2 = new HashMap<>();
    final Map<AbstractBastNode, AbstractBastNode> partnersInTree1 = new HashMap<>();
    for (final ComparePair<AbstractBastNode> matchedPair : matchingNodes) {
      partnersInTree2.put((AbstractBastNode) matchedPair.getOldElement(),
          (AbstractBastNode) matchedPair.getNewElement());
      partnersInTree1.put((AbstractBastNode) matchedPair.getNewElement(),
          (AbstractBastNode) matchedPair.getOldElement());
    }

    // Provide easy access to values and children
    final ValueRetriever valueGetter = new ValueRetriever();
    final ChildrenAligner childrenAligner = new ChildrenAligner(partnersInTree2);

    // First do a breadth-first traversal of tree2 to find update, insert,
    // move and align operations
    while (iteratorBreathFirst2.hasNext()) {
      final AbstractBastNode node = iteratorBreathFirst2.next();
      final AbstractBastNode partner = (AbstractBastNode) partnersInTree1.get(node);
      if (partner != null) {
        // node and partner have been matched
        if (node.getTag() != partner.getTag()) {
          assert (node.getTag() == partner.getTag());
        }

        final AbstractBastNode nodeParent = parents2.get(node);
        final AbstractBastNode partnerParent = parents1.get(partner);
        // Update phase:
        // Check, whether values differ
        addUpdates(childrenGetter, editScript, partnersInTree1, valueGetter, node, partner,
            nodeParent, partnerParent);

        // Move phase:
        // Look for nodes which parents have not been matched

        addMoves(childrenGetter, editScript, partnersInTree1, node, partner, nodeParent,
            partnerParent);

        // Align phase:
        // Look for nodes whose children have been rearranged
        final List<ChildrenList> nodeChildren = childrenGetter.getChildren(node);
        final List<ChildrenList> partnerChildren = childrenGetter.getChildren(partner);

        if ((nodeChildren != null) && (partnerChildren != null) && partner != null) {
          childrenAligner.alignChildren(partner, partnerChildren, nodeChildren, editScript);
        }
      } else {
        // node is unmatched, does not have a partner

        // Insert phase:
        addInsert(parents2, childrenGetter, editScript, node);
      }
    }

    // Now do a post-order traversal of tree1 to find delete operations
    addDeletes(parents1, childrenGetter, depthPostOrder1, editScript, partnersInTree2);

    return new ExtendedDiffResult(filename, editScript, partnersInTree1, partnersInTree2);
  }

  private static void addUpdates(ChildrenRetriever childrenGetter,
      final List<BastEditOperation> editScript,
      final Map<AbstractBastNode, AbstractBastNode> partnersInTree1,
      final ValueRetriever valueGetter, final AbstractBastNode node, final AbstractBastNode partner,
      final AbstractBastNode nodeParent, final AbstractBastNode partnerParent) {
    if (!valueGetter.getValue(node).equals(valueGetter.getValue(partner))
        && nodeParent != null) {
      if (partnerParent == null || partnerParent == partnersInTree1.get(nodeParent)
          || partnersInTree1.get(nodeParent) == null) {
        editScript.add(new UpdateOperation(partnerParent, nodeParent, partner, node,
            IndexFinder.getIndex(partner, partnerParent, childrenGetter),
            IndexFinder.getIndex(node, nodeParent, childrenGetter)));
      } else {
        editScript.add(new UpdateOperation(partnerParent, nodeParent, partner, node,
            IndexFinder.getIndex(partner, partnerParent, childrenGetter),
            IndexFinder.getIndex(node, nodeParent, childrenGetter)));
      }
    }
  }

  private static void addMoves(ChildrenRetriever childrenGetter,
      final List<BastEditOperation> editScript,
      final Map<AbstractBastNode, AbstractBastNode> partnersInTree1, final AbstractBastNode node,
      final AbstractBastNode partner, final AbstractBastNode nodeParent,
      final AbstractBastNode partnerParent) {
    if (nodeParent != null && partnerParent != null && nodeParent != null) {
      if (partnersInTree1.get(nodeParent) != partnerParent) {
        // Generate move operation
        editScript.add(new MoveOperation(partnerParent, nodeParent, partner, node,
            IndexFinder.getIndex(partner, partnerParent, childrenGetter),
            IndexFinder.getIndex(node, nodeParent, childrenGetter)));
      } else {
        NodeIndex partnerIndex = IndexFinder.getIndex(partner, partnerParent, childrenGetter);
        NodeIndex nodeIndex = IndexFinder.getIndex(node, nodeParent, childrenGetter);
        if (partnerIndex != null && nodeIndex != null
            && partnerIndex.childrenListNumber != nodeIndex.childrenListNumber) {
          // Generate move operation
          editScript.add(new MoveOperation(partnerParent, nodeParent, partner, node,
              IndexFinder.getIndex(partner, partnerParent, childrenGetter),
              IndexFinder.getIndex(node, nodeParent, childrenGetter)));
        }

      }
    }
  }

  private static void addDeletes(Map<AbstractBastNode, AbstractBastNode> parents1,
      ChildrenRetriever childrenGetter, Iterator<AbstractBastNode> depthPostOrder1,
      final List<BastEditOperation> editScript,
      final Map<AbstractBastNode, AbstractBastNode> partnersInTree2) {
    while (depthPostOrder1.hasNext()) {
      final AbstractBastNode node = depthPostOrder1.next();
      final AbstractBastNode partner = partnersInTree2.get(node);

      if (partner == null) {
        // Delete phase:
        final AbstractBastNode parent = parents1.get(node);
        assert parent != null;
        editScript.add(
            new DeleteOperation(parent, node, IndexFinder.getIndex(node, parent, childrenGetter)));
      }
    }
  }

  private static void addInsert(Map<AbstractBastNode, AbstractBastNode> parents2,
      ChildrenRetriever childrenGetter, final List<BastEditOperation> editScript,
      final AbstractBastNode node) {
    final AbstractBastNode parent = parents2.get(node);
    if (!parents2.containsKey(node)) {
      assert (false);
    }
    editScript.add(
        new InsertOperation(parent, node, IndexFinder.getIndex(node, parent, childrenGetter)));
  }

  @Override
  public AtomicLong getextendedCdTime() {
    return extendedCdTime;
  }


  /**
   * Gets the extended inner matching time.
   *
   * @return the extended inner matching time
   */
  @Override
  public AtomicLong getextendedInnerMatchingTime() {
    return extendedInnerMatchingTime;
  }


  /**
   * Gets the large leaf diff.
   *
   * @return the large leaf diff
   */
  @Override
  public AtomicLong getlargeLeafDiff() {
    return largeLeafDiff;
  }


  /**
   * Gets the leaves map1.
   *
   * @return the leaves map1
   */
  @Override
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getLeavesMap1() {
    return leavesMap1;
  }


  /**
   * Gets the leaves map2.
   *
   * @return the leaves map2
   */
  @Override
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getLeavesMap2() {
    return leavesMap2;
  }


  /**
   * Gets the direct children map1.
   *
   * @return the direct children map1
   */
  @Override
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getdirectChildrenMap1() {
    return directChildrenMap1;
  }


  /**
   * Gets the direct children map2.
   *
   * @return the direct children map2
   */
  @Override
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getdirectChildrenMap2() {
    return directChildrenMap2;
  }


  /**
   * Sets the timeout.
   *
   * @param value the new timeout
   */
  @Override
  public void setTimeout(long value) {

  }
}
