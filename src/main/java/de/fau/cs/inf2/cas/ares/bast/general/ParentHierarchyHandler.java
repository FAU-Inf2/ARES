package de.fau.cs.inf2.cas.ares.bast.general;

import de.fau.cs.inf2.cas.ares.bast.visitors.RetrieveParentInformationMapVisitor;

import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformation;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class ParentHierarchyHandler {
  
  /**
   * Gets the parent hierarchy.
   *
   * @param node the node
   * @return the parent hierarchy
   */
  public static Map<AbstractBastNode, NodeParentInformationHierarchy> getParentHierarchy(
      AbstractBastNode node) {
    RetrieveParentInformationMapVisitor rpimv = new RetrieveParentInformationMapVisitor();
    node.accept(rpimv);
    Map<AbstractBastNode, NodeParentInformation> parentInfo = rpimv.parentInformation;
    Map<AbstractBastNode, NodeParentInformationHierarchy> parentHierarchyInfo = new HashMap<>();
    for (Entry<AbstractBastNode, NodeParentInformation> es : parentInfo.entrySet()) {
      LinkedList<NodeParentInformation> list = new LinkedList<>();
      NodeParentInformation tmp = es.getValue();
      while (tmp != null) {
        list.add(tmp);
        tmp = parentInfo.get(tmp.parent);
      }
      parentHierarchyInfo.put(es.getKey(), new NodeParentInformationHierarchy(es.getKey(), list));
    }
    return parentHierarchyInfo;
  }

}
