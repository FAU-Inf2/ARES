package de.fau.cs.inf2.cas.ares.pcreation.visitors;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import java.util.HashMap;
import java.util.HashSet;

public class CollectEmptyUsesVisitor extends AresDefaultFieldVisitor {
  public HashSet<AresUseStmt> uses = new HashSet<>();
  public HashMap<AresUseStmt, NodeIndex> usePosition = new HashMap<>();
  public HashMap<AresUseStmt, AbstractBastNode> useParents = new HashMap<>();

  
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
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresChoiceStmt node) {
    super.visit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresUseStmt node) {
    if (WildcardAccessHelper.getName(node) == null) {
      uses.add(node);
      usePosition.put(node, new NodeIndex(fieldId, listId));
      useParents.put(node, globalParent);

    }
  }

}
