package de.fau.cs.inf2.cas.common.bast.visitors;

import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondOr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEmptyStmt;

import java.util.ArrayList;

public class NormalFormNodeStreamVisitor extends AresDefaultFieldVisitor {
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
  public NormalFormNodeStreamVisitor(AbstractBastNode nodeToFind) {
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
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCondOr node) {
    beginVisit(node);
    ArrayList<AbstractBastNode> otherBranches = new ArrayList<>();
    ArrayList<BastFieldConstants> otherConstants = new ArrayList<>();

    executeOr((BastCondOr)node, otherBranches, otherConstants);
    for (int i = 0; i < otherBranches.size(); i++) {
      setVariables(otherConstants.get(i), otherBranches.get(i), -1);
      otherBranches.get(i).accept(this);
    }
    endVisit(node);
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
            expr.accept(this);
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


  private void executeOr(BastCondOr node,
      ArrayList<AbstractBastNode> otherBranches, ArrayList<BastFieldConstants> otherConstants) {
    
    AbstractBastNode left = node.getField(BastFieldConstants.COND_OR_LEFT).getField();
    AbstractBastNode right = node.getField(BastFieldConstants.COND_OR_RIGHT).getField();

    if (left.getTag() == BastCondOr.TAG) {
      setVariables(BastFieldConstants.COND_OR_LEFT, left, -1);
      if (found) {
        nodes.add(left);
      }
      executeOr((BastCondOr) left, otherBranches, otherConstants);
    }
    if (right.getTag() == BastCondOr.TAG) {
      setVariables(BastFieldConstants.COND_OR_RIGHT, right, -1);
      if (found) {
        nodes.add(right);
      }
      executeOr((BastCondOr) right, otherBranches, otherConstants);
    }
    if (left.getTag() != BastCondOr.TAG) {
      otherBranches.add(left);
      otherConstants.add(BastFieldConstants.COND_OR_LEFT);
    }
    if (right.getTag() != BastCondOr.TAG) {
      otherBranches.add(right);
      otherConstants.add(BastFieldConstants.COND_OR_RIGHT);
    }
  }

}
