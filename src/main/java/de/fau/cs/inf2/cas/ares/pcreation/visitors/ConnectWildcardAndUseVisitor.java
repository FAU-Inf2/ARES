package de.fau.cs.inf2.cas.ares.pcreation.visitors;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.visitors.DefaultFieldVisitor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class ConnectWildcardAndUseVisitor extends DefaultFieldVisitor {

  public HashMap<String, AresWildcard> wildcards = new HashMap<>();
  public HashMap<String, List<AresUseStmt>> useStmts = new HashMap<>();
  List<AresWildcard> removedNamedWildcards = new LinkedList<AresWildcard>();

  public HashMap<AbstractBastNode, AbstractBastNode> nodeReplacements = new HashMap<>();

  public HashSet<String> names = new HashSet<>();

  /**
   * Instantiates a new connect wildcard and use visitor.
   */
  public ConnectWildcardAndUseVisitor() {
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
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresUseStmt node) {
    if (WildcardAccessHelper.getName(node) != null) {
      List<AresUseStmt> list = null;
      list = useStmts.get(WildcardAccessHelper.getName(node).name);
      if (list == null) {
        list = new ArrayList<AresUseStmt>();
        useStmts.put(WildcardAccessHelper.getName(node).name, list);
      }
      list.add(node);
      names.add(WildcardAccessHelper.getName(node).name);
    }
    super.visit(node);
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(AresWildcard node) {
    AbstractBastNode parent = globalParent;
    if (!parent.getField(fieldId).isList() && fieldId != BastFieldConstants.IF_IF_PART) {
      assert (parent.getField(fieldId).isList());
    }
    if (node.plugin.exprList != null) {
      if (((AresPatternClause) node.plugin.exprList.get(0)).ident != null) {
        wildcards.put(((AresPatternClause) node.plugin.exprList.get(0)).ident.name, node);
        names.add(((AresPatternClause) node.plugin.exprList.get(0)).ident.name);
      }

    }
    super.visit(node);
  }

}
