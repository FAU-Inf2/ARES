package de.fau.cs.inf2.cas.common.bast.diff;

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;


final class INodeTreeVisitor {
  private final IterationOrder iterationOrder;
  private final Deque<INode> nodeList = new ArrayDeque<>();

  INodeTreeVisitor(final IterationOrder order) {
    iterationOrder = order;
  }

  private void addToList(final List<INode> list, final INode node) {
    if (node != null) {
      list.add(node);
    }
  }

  private void addToList(final List<INode> list, final List<? extends INode> nodes) {
    if (nodes != null) {
      for (final INode node : nodes) {
        addToList(list, node);
      }
    }
  }

  private void doIteration(final INode node, final List<INode> children) {
    if (iterationOrder == IterationOrder.DEPTH_FIRST_PRE_ORDER) {
      nodeList.addLast(node);
    }

    if (children != null) {
      if (iterationOrder == IterationOrder.BREADTH_FIRST) {
        try {
          nodeList.addAll(children);
        } catch (Exception e) {
          System.out.println(node.getClass().getCanonicalName());
          throw e;
        }
      }

      for (final INode child : children) {
        visit(child);
      }
    }

    if (iterationOrder == IterationOrder.DEPTH_FIRST_POST_ORDER) {
      nodeList.addLast(node);
    }
  }

  /**
   * todo.
   *
   *<p>Get an ordered queue of nodes according to the traversal order
   * specified by the constructor of
   * this class.
   * 
   * @param root The node where the traversal is to begin
   * @return An ordered queue of nodes
   */
  Deque<INode> getNodes(final INode root) {
    if (iterationOrder == IterationOrder.BREADTH_FIRST) {
      nodeList.addLast(root);
    }
    visit(root);

    return nodeList;
  }

  private void visit(INode node) {
    List<INode> nodeChldrn = new ArrayList<>();
    addToList(nodeChldrn, node.getChildrenWrapped());
    doIteration(node, nodeChldrn);


  }

}
