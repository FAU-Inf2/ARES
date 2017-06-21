package de.fau.cs.inf2.cas.ares.recommendation.visitors;

import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;
import de.fau.cs.inf2.cas.ares.bast.visitors.NodeStreamVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.ArrayList;
import java.util.LinkedList;

;

public class FindPatternOccurrence extends AresDefaultFieldVisitor {

  private ArrayList<AbstractBastNode> list = null;
  private int listPosition = 0;
  private final int occurenceToFind;
  private int currentOccurence = 0;
  private boolean finished = false;
  public LinkedList<AbstractBastNode> matches = new LinkedList<>();

  /**
   * Instantiates a new find pattern occurrence.
   *
   * @param expr the expr
   * @param occurenceToFind the occurence to find
   */
  public FindPatternOccurrence(AbstractBastNode expr, int occurenceToFind) {
    NodeStreamVisitor programTokenVisitor = new NodeStreamVisitor(expr);
    expr.accept(programTokenVisitor);

    list = programTokenVisitor.nodes;
    this.occurenceToFind = occurenceToFind;
  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    if (finished) {
      return;
    }
    if (list.size() <= listPosition) {
      finished = true;
      matches.clear();
      return;
    }
    if (node.getTag() == list.get(listPosition).getTag()) {
      matches.add(node);
      listPosition++;
      if (list.size() == listPosition) {
        currentOccurence++;
        if (currentOccurence == occurenceToFind) {
          finished = true;
        } else {
          matches.clear();
        }
      }
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
   * Standard visit.
   *
   * @param constant the constant
   * @param node the node
   */
  @Override
  public void standardVisit(BastFieldConstants constant, AbstractBastNode node) {
    if (finished) {
      return;
    }
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
    }
  }

}
