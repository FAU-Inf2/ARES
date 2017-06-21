package de.fau.cs.inf2.cas.ares.recommendation.visitors;

import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprInitializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Stack;

public class GetChildrenMapVisitor extends AresDefaultFieldVisitor {
  private HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> children = new HashMap<>();

  private LinkedList<AbstractBastNode> parentNodeStack = new LinkedList<>();

  Stack<Integer> sizeStack = new Stack<>();

  /**
   * todo.
   * 
   * <p>Return a map of node to direct children by this visitor
   *
   * @return the children map
   */
  @SuppressWarnings("ucd")
  public HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> getChildrenMap() {
    return children;
  }
  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    ArrayList<AbstractBastNode> tmp = new ArrayList<>();
    parentNodeStack.push(node);
    tmp = new ArrayList<>();
    children.put(node, tmp);
  }

  
  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {
    for (AbstractBastNode parentNode : parentNodeStack) {
      if (parentNode != node) {
        ArrayList<AbstractBastNode> list = children.get(parentNode);
        assert (list != null);
        list.add(node);
      }
    }
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
            if (expr != null) {
              expr.accept(this);
            }
          }
        }
      } else if (node.fieldMap.get(constant).getField() != null) {
        setVariables(constant, node, -1);
        node.fieldMap.get(constant).getField().accept(this);
      }
    }
  }
}
