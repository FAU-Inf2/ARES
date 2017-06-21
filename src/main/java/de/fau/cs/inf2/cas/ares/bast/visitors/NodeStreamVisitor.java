package de.fau.cs.inf2.cas.ares.bast.visitors;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEmptyStmt;

import java.util.ArrayList;

public class NodeStreamVisitor extends AresDefaultFieldVisitor {
  public ArrayList<AbstractBastNode> nodes = new ArrayList<>();
  private AbstractBastNode nodeToFind;
  private boolean found = false;
  private AbstractBastNode parentFound;
  private BastFieldConstants field;

  /**
   * Instantiates a new node stream visitor.
   *
   * @param nodeToFind the node to find
   */
  public NodeStreamVisitor(AbstractBastNode nodeToFind) {
    this.nodeToFind = nodeToFind;
  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    if (node == nodeToFind) {
      found = true;
      parentFound = globalParent;
      field = fieldId;
    }
    if (found) {
      nodes.add(node);
    }

  }

  
  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {

  }

  /**
   *  visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastEmptyStmt node) {

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
            } else {
              assert (false);
            }
          }
        }
      } else if (node.fieldMap.get(constant).getField() != null) {
        setVariables(constant, node, -1);
        node.fieldMap.get(constant).getField().accept(this);
      }
      if (found && node == parentFound && constant == field) {
        found = false;
      }
    }

  }

}
