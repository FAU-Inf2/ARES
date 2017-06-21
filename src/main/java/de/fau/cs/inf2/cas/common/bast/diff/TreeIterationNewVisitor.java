package de.fau.cs.inf2.cas.common.bast.diff;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.visitors.DefaultFieldVisitor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * Helper class for {@see TreeIterator}.
 * 
 */
final class TreeIterationNewVisitor extends DefaultFieldVisitor {
  private final IterationOrder iterationOrder;
  private final Deque<AbstractBastNode> nodeList = new ArrayDeque<>();

  TreeIterationNewVisitor(final IterationOrder order) {
    iterationOrder = order;
  }

  private void addToList(final List<AbstractBastNode> list, final AbstractBastNode node) {
    if (node != null) {
      list.add(node);
    }
  }

  private void addToList(final List<AbstractBastNode> list,
      final List<? extends AbstractBastNode> nodes) {
    if (nodes != null) {
      for (final AbstractBastNode node : nodes) {
        addToList(list, node);
      }
    }
  }

  private void doIteration(final AbstractBastNode node, final List<AbstractBastNode> children) {
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

      for (final AbstractBastNode child : children) {
        child.accept(this);
      }
    }

    if (iterationOrder == IterationOrder.DEPTH_FIRST_POST_ORDER) {
      nodeList.addLast(node);
    }
  }

  /**
   * todo.
   *
   *<p>Get an ordered queue of nodes according to the traversal
   * order specified by the constructor of
   * this class.
   * 
   * @param root The node where the traversal is to begin
   * @return An ordered queue of nodes
   */
  Deque<AbstractBastNode> getNodes(final AbstractBastNode root) {
    if (iterationOrder == IterationOrder.BREADTH_FIRST) {
      nodeList.addLast(root);
    }

    root.accept(this);

    return nodeList;
  }

  private List<AbstractBastNode> nodeChldrn = null;

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    nodeChldrn = new ArrayList<>();

  }

  
  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {
    doIteration(node, nodeChldrn);

  }

  
  /**
   * Standard visit.
   *
   * @param constant the constant
   * @param node the node
   */
  @Override
  public void standardVisit(BastFieldConstants constant, AbstractBastNode node) {
    if (node.fieldMap.get(constant) != null) {
      if (node.fieldMap.get(constant).isList()) {
        if (node.fieldMap.get(constant).getListField() != null) {
          addToList(nodeChldrn, node.fieldMap.get(constant).getListField());
        }
      } else if (node.fieldMap.get(constant).getField() != null) {
        addToList(nodeChldrn, node.fieldMap.get(constant).getField());
      }
    }

  }
}
