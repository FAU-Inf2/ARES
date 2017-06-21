package de.fau.cs.inf2.cas.common.bast.visitors;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAsgnExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.type.BastBasicType;

import java.util.LinkedList;

public class FindPatternStartsVisitor extends AresDefaultFieldVisitor {
  public LinkedList<AbstractBastNode> starts = new LinkedList<AbstractBastNode>();
  public boolean exactName = false;
  protected AbstractBastNode nodeToFind = null;
  protected AresWildcard wildcard = null;

  
  /**
   * Standard visit.
   *
   * @param constant the constant
   * @param node the node
   */
  public void standardVisit(BastFieldConstants constant, AbstractBastNode node) {
    if (visit == false) {
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

  /**
   * Instantiates a new find pattern starts visitor.
   *
   * @param node the node
   * @param exactName the exact name
   */
  public FindPatternStartsVisitor(LinkedList<AbstractBastNode> node, boolean exactName) {
    this.exactName = exactName;
    switch (node.getFirst().getTag()) {

      case AresWildcard.TAG:
        FindNonAresNodeVisitor visitor = new FindNonAresNodeVisitor();
        node.getFirst().accept(visitor);
        nodeToFind = visitor.foundNode;
        break;
      case BastAsgnExpr.TAG:
      case BastNameIdent.TAG:
      case BastCall.TAG:
        this.nodeToFind = node.getFirst();
        break;
      default:
        this.nodeToFind = node.getFirst();

    }

  }

  /**
   * Instantiates a new find pattern starts visitor.
   *
   * @param node the node
   * @param exactName the exact name
   */
  public FindPatternStartsVisitor(AbstractBastNode node, boolean exactName) {
    this.exactName = exactName;

    this.nodeToFind = node;
    switch (node.getTag()) {

      case AresWildcard.TAG:
        FindNonAresNodeVisitor visitor = new FindNonAresNodeVisitor();
        node.accept(visitor);
        nodeToFind = visitor.foundNode;
        wildcard = (AresWildcard) node;
        break;
      case BastAsgnExpr.TAG:
      case BastNameIdent.TAG:
      case BastCall.TAG:
      case BastIntConst.TAG:
        this.nodeToFind = node;
        break;
      default:
        this.nodeToFind = node;

    }
  }

  private boolean visit = true;

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    if (nodeToFind == null) {
      return;
    } else if (node.getTag() == nodeToFind.getTag()) {
      if (node.getTag() == BastBasicType.TAG) {
        if (((BastBasicType) node).getTypeTag2() == ((BastBasicType) node).getTypeTag2()) {
          starts.add(node);
        }
      } else if (node.getTag() == BastNameIdent.TAG) {
        if (exactName) {
          if (((BastNameIdent) nodeToFind).name.equals(((BastNameIdent) node).name)) {
            starts.add(node);
          }
        } else {
          starts.add(node);
        }
      } else {

        starts.add(node);
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
    visit = true;

  }
}
