package de.fau.cs.inf2.cas.ares.pcreation.visitors;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.ReplaceMap;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class RestoreChoicePartsVisitor extends AresDefaultFieldVisitor {
  private ReplaceMap replaceMap;

  /**
   * Instantiates a new restore choice parts visitor.
   *
   * @param extDiff the ext diff
   * @param replaceMap the replace map
   */
  public RestoreChoicePartsVisitor(ExtendedDiffResult extDiff, ReplaceMap replaceMap) {
    this.replaceMap = replaceMap;
  }

  private Map<AbstractBastNode, AbstractBastNode> revertMap = new HashMap<>();

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    if (node.getTag() == AresUseStmt.TAG && !WildcardAccessHelper.isExprWildcard(node)) {

      for (Map.Entry<AbstractBastNode, AbstractBastNode> entry : replaceMap.entrySet()) {
        if (entry.getValue() == node) {
          revertMap.put(node, entry.getKey());
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
    if (node.fieldMap.get(constant) != null) {
      if (node.fieldMap.get(constant).isList()) {
        int counter = 0;
        if (node.fieldMap.get(constant).getListField() != null) {
          LinkedList<AbstractBastNode> tmpList = new LinkedList<>();
          boolean changed = false;
          for (AbstractBastNode expr : node.fieldMap.get(constant).getListField()) {
            setVariables(constant, node, counter++);
            expr.accept(this);
            if (revertMap.containsKey(expr)) {
              tmpList.add(revertMap.get(expr));
              changed = true;
            } else {
              tmpList.add(expr);
            }
          }
          if (changed) {
            node.replaceField(constant, new BastField(tmpList));
          }
        }
      } else if (node.fieldMap.get(constant).getField() != null) {
        setVariables(constant, node, -1);
        node.fieldMap.get(constant).getField().accept(this);
        if (revertMap.containsKey(node.fieldMap.get(constant).getField())) {
          node.replaceField(constant,
              new BastField(revertMap.get(node.fieldMap.get(constant).getField())));
        }
      }
    }

  }

}
