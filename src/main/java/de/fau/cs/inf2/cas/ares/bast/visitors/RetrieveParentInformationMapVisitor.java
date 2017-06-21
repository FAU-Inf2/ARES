package de.fau.cs.inf2.cas.ares.bast.visitors;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformation;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.HashMap;
import java.util.Map;

public class RetrieveParentInformationMapVisitor extends AresDefaultFieldVisitor {

  public Map<AbstractBastNode, NodeParentInformation> parentInformation =
      new HashMap<AbstractBastNode, NodeParentInformation>();

  /**
   * Instantiates a new retrieve parent information map visitor.
   */
  public RetrieveParentInformationMapVisitor() {

  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {

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
    boolean insideMethod = false;
    if (constant == BastFieldConstants.FUNCTION_BLOCK_STATEMENTS) {
      insideMethod = true;
    }
    if (node.fieldMap.get(constant) != null) {
      if (node.fieldMap.get(constant).isList()) {
        int counter = 0;
        if (node.fieldMap.get(constant).getListField() != null) {
          for (AbstractBastNode expr : node.fieldMap.get(constant).getListField()) {
            setVariables(constant, node, counter++);
            if (expr != null) {
              expr.accept(this);
            }
            parentInformation.put(expr,
                new NodeParentInformation(node, constant, counter - 1, insideMethod));
          }
        }
      } else if (node.fieldMap.get(constant).getField() != null) {
        setVariables(constant, node, -1);
        node.fieldMap.get(constant).getField().accept(this);
        assert (node.fieldMap.get(constant).getField() != null);
        parentInformation.put(node.fieldMap.get(constant).getField(),
            new NodeParentInformation(node, constant, listId, insideMethod));
      }
    }
  }

}
