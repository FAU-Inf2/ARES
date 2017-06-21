package de.fau.cs.inf2.cas.common.bast.modification.visitor;

import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.modification.ModificationInformation;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.HashMap;

public class FindModificationsVisitor extends AresDefaultFieldVisitor {
  public HashMap<AbstractBastNode, ModificationInformation> modMap = null;

  /**
   * Instantiates a new find modifications visitor.
   *
   * @param modMap the mod map
   */
  public FindModificationsVisitor(HashMap<AbstractBastNode, ModificationInformation> modMap) {
    this.modMap = modMap;
  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    ModificationInformation modInfo = modMap.get(node);
    if (modInfo == null) {
      modMap.put(node, new ModificationInformation());
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
    ModificationInformation modInfo = modMap.get(node);
    if (node.fieldMap.get(constant) != null) {
      if (node.fieldMap.get(constant).isList()) {
        int counter = 0;
        if (node.fieldMap.get(constant).getListField() != null) {
          for (AbstractBastNode expr : node.fieldMap.get(constant).getListField()) {
            setVariables(constant, node, counter++);
            expr.accept(this);
            ModificationInformation childInfo = modMap.get(expr);

            if (childInfo.nodeModified || childInfo.childModified) {
              modInfo.childModified = true;
              Integer lower = modInfo.fieldLowerBorder.get(constant);
              if (lower == null) {
                modInfo.fieldLowerBorder.put(constant, counter - 1);
                modInfo.fieldUpperBorder.put(constant, counter);
              } else {
                modInfo.fieldLowerBorder.put(constant, Math.min(lower, counter - 1));
                Integer upper = modInfo.fieldUpperBorder.get(constant);
                modInfo.fieldUpperBorder.put(constant, Math.max(upper, counter));
              }
            }
          }
        }
      } else if (node.fieldMap.get(constant).getField() != null) {
        setVariables(constant, node, -1);
        node.fieldMap.get(constant).getField().accept(this);
        ModificationInformation childInfo = modMap.get(node.fieldMap.get(constant).getField());
        if (childInfo != null && (childInfo.nodeModified || childInfo.childModified)) {
          modInfo.childModified = true;
          modInfo.fieldLowerBorder.put(constant, 0);
          modInfo.fieldUpperBorder.put(constant, 0);
        }
      }
    }
  }

}
