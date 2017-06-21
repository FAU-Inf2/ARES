package de.fau.cs.inf2.cas.common.bast.general;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;

import java.util.LinkedList;
import java.util.Map;

public class NodeParentInformationHierarchy {
  public LinkedList<NodeParentInformation> list = new LinkedList<>();
  private Boolean insideMethod = null;
  private AbstractBastNode node = null;

  public NodeParentInformationHierarchy(AbstractBastNode node,
      LinkedList<NodeParentInformation> list) {
    this.list = list;
    this.node = node;
  }

  /**
   * Gets the node.
   *
   * @return the node
   */
  public AbstractBastNode getNode() {
    return node;
  }

  /**
   * Inside method.
   *
   * @return true, if successful
   */
  public boolean insideMethod() {
    if (insideMethod == null) {
      insideMethod = false;
      for (NodeParentInformation np : list) {
        if (np.fieldConstant == BastFieldConstants.FUNCTION_BLOCK_STATEMENTS) {
          insideMethod = true;
          break;
        }
      }
    }
    return insideMethod;
  }

  /**
   * Shared parent hierarchy.
   *
   * @param info1 the info1
   * @param info2 the info2
   * @param completeInformationMap the complete information map
   * @return the node parent information hierarchy
   */
  public static NodeParentInformationHierarchy sharedParentHierarchy(
      NodeParentInformationHierarchy info1, NodeParentInformationHierarchy info2,
      Map<AbstractBastNode, NodeParentInformationHierarchy> completeInformationMap) {
    if (info1 == null) {
      return info2;
    }
    if (info2 == null) {
      return info1;
    }
    if (info1.node == info2.node) {
      return info1;
    }
    LinkedList<AbstractBastNode> tmpInfoNodes1 = new LinkedList<>();
    LinkedList<AbstractBastNode> tmpInfoNodes2 = new LinkedList<>();
    for (NodeParentInformation np : info1.list) {
      tmpInfoNodes1.add(np.parent);
    }
    tmpInfoNodes1.addFirst(info1.node);
    for (NodeParentInformation np : info2.list) {
      tmpInfoNodes2.add(np.parent);
    }
    tmpInfoNodes2.addFirst(info2.node);

    java.util.Collections.reverse(tmpInfoNodes1);
    java.util.Collections.reverse(tmpInfoNodes2);
    AbstractBastNode node = null;
    for (int i = 0; i < tmpInfoNodes1.size(); i++) {
      if (i >= tmpInfoNodes2.size()) {
        break;
      }
      if (tmpInfoNodes1.get(i) == tmpInfoNodes2.get(i)) {
        node = tmpInfoNodes1.get(i);
      }
    }

    if (node.getTag() == BastFunction.TAG || completeInformationMap.get(node) == null) {
      assert (completeInformationMap.get(node) != null);
    }
    return completeInformationMap.get(node);
  }
}
