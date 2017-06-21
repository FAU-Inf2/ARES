package de.fau.cs.inf2.cas.common.bast.visitors;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprInitializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;

public class PostOrderSetGenerationNewVisitor extends DefaultFieldVisitor {
  private Set<AbstractBastNode> nodeSet;
  private Set<AbstractBastNode> leafSet;

  private HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> leaves;
  private HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> directChildren;

  private LinkedList<AbstractBastNode> parentNodeStack;

  /**
   * todo.
   * 
   * <p>Return a map of node to leaves by this visitor
   *
   * @return the leave map
   */
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getLeaveMap() {
    return leaves;
  }

  /**
   * todo.
   * 
   * <p>Return a map of node to direct children by this visitor
   *
   * @return the direct children map
   */
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getDirectChildrenMap() {
    return directChildren;
  }

  /**
   * todo.
   * 
   * <p>Return a list of all nodes visited by this visitor
   *
   * @return the sets the of nodes
   */
  public Set<AbstractBastNode> getSetOfNodes() {
    return nodeSet;
  }

  /**
   * todo.
   * 
   * <p>Return a list of all leaves visited by this visitor
   *
   * @return the sets the of leaves
   */
  public Set<AbstractBastNode> getSetOfLeaves() {
    return leafSet;
  }

  /**
   * todo.
   * 
   * <p>Generates sets for a given root node.
   *
   * @param root the root
   */
  public void createSetsForNode(final AbstractBastNode root) {
    nodeSet = new LinkedHashSet<AbstractBastNode>();
    leafSet = new LinkedHashSet<AbstractBastNode>();
    leaves = new HashMap<>();
    directChildren = new HashMap<>();
    parentNodeStack = new LinkedList<>();
    root.accept(this);
  }

  private Stack<Integer> sizeStack = new Stack<>();

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    sizeStack.push(nodeSet.size());
    ArrayList<AbstractBastNode> tmp = new ArrayList<>();
    leaves.put(node, tmp);
    parentNodeStack.push(node);
    tmp = new ArrayList<>();
    directChildren.put(node, tmp);
  }

  
  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {
    int size = sizeStack.pop();
    if (size == nodeSet.size()) {
      leafSet.add(node);
      for (AbstractBastNode parentNode : parentNodeStack) {
        if (parentNode != node) {
          ArrayList<AbstractBastNode> list = leaves.get(parentNode);
          assert (list != null);
          list.add(node);
        }
      }
    }
    nodeSet.add(node);
    parentNodeStack.pop();
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
        int counter = 0;
        if (node.fieldMap.get(constant).getListField() != null) {
          for (AbstractBastNode expr : node.fieldMap.get(constant).getListField()) {
            setVariables(constant, node, counter++);
            directChildren.get(node).add(expr);
            expr.accept(this);
          }
        }
      } else if (node.fieldMap.get(constant).getField() != null) {
        setVariables(constant, node, -1);
        directChildren.get(node).add(node.fieldMap.get(constant).getField());
        node.fieldMap.get(constant).getField().accept(this);
      }
    }
  }
}
