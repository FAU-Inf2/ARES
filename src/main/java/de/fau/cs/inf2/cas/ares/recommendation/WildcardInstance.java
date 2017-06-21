package de.fau.cs.inf2.cas.ares.recommendation;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.recommendation.plugin.PluginTester;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformation;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitchCaseGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class WildcardInstance {

  ArrayList<CompareNodesResult> triedResetNodes = new ArrayList<>();
  public ArrayList<AbstractBastNode> headNodes = new ArrayList<AbstractBastNode>();
  private HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> headNodesMap = new HashMap<>();
  private AbstractBastNode wildcardParent = null;
  public AresWildcard wildcard = null;
  public ArrayList<AbstractBastNode> nodes = new ArrayList<AbstractBastNode>();

  public int possibleWildcardEndTest = -1;

  private int pluginType = -1;
  private boolean full = false;
  private boolean violation = false;

  private boolean unfinished = false;

  WildcardInstance(AresWildcard wildcard, BastProgram program) {
    this.wildcard = wildcard;
    pluginType = PluginTester.getPluginId(wildcard.plugin.ident.name);
  }

  /**
   * Checks if is empty.
   *
   * @return true, if is empty
   */
  public boolean isEmpty() {
    return nodes.isEmpty();
  }

  /**
   * toString.
   */
  public String toString() {
    StringBuffer buffer = new StringBuffer();
    buffer.append("WildcardInstance ");
    buffer.append(wildcard.toString());
    buffer.append(" (");
    if (headNodes != null && headNodes.size() > 0) {
      buffer.append(headNodes.get(0).toString());
      if (headNodes.size() > 1) {
        buffer.append(" ... ");
        buffer.append(headNodes.get(headNodes.size() - 1).toString());
      }
    }
    buffer.append(")");
    return buffer.toString();
  }
  
  /**
   * Gets the nodes.
   *
   * @return the nodes
   */
  public ArrayList<AbstractBastNode> getNodes() {
    return nodes;
  }

  boolean acceptEmpty() {
    switch (pluginType) {
      case PluginTester.PLUGIN_ACCEPT_ALL_EXPR:
      case PluginTester.PLUGIN_ACCEPT_ALL_STMT:
        return true;
      default:
        assert (false);

    }
    return false;
  }

  void removeNode(AbstractBastNode node) {
    nodes.remove(node);
    headNodes.remove(node);
    headNodesMap.remove(node);
  }

  int acceptNode(AbstractBastNode node, AbstractBastNode templateToken,
      HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> childrenMap,
      Map<AbstractBastNode, NodeParentInformationHierarchy> programParents,
      Map<AbstractBastNode, NodeParentInformationHierarchy> templateParents,
      Map<AbstractBastNode, AbstractBastNode> assignmentMap) {
    ArrayList<AbstractBastNode> children = childrenMap.get(node);
    if (node.getTag() == BastSwitchCaseGroup.TAG) {
      NodeParentInformationHierarchy npi = programParents.get(node);
      if (npi != null && npi.list.get(0).parent == this.wildcardParent) {
        children = new ArrayList<>();
        children.add(node);
      }
    }
    NodeParentInformationHierarchy headNodeHierarchy = null;
    NodeParentInformationHierarchy nodeHierarchy = null;

    ArrayList<AbstractBastNode> childrenNodes = null;
    ArrayList<AbstractBastNode> childrenMapList = null;
    switch (pluginType) {
      case PluginTester.PLUGIN_ACCEPT_ALL_EXPR:
      case PluginTester.PLUGIN_ACCEPT_ALL_STMT:
        if (headNodes.isEmpty()) {
          
          if (templateToken.getTag() == AresWildcard.TAG
              && templateParents.get(templateToken).list.get(0).parent.getTag() != AresBlock.TAG
              && templateParents.get(templateToken).list.get(0).parent
                  .getTag() != AresWildcard.TAG) {
            wildcardParent = assignmentMap.get(templateParents.get(templateToken)
                .list.get(0).parent);
            if (templateParents.get(templateToken)
                .list.get(0).fieldConstant == BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS) {
              wildcardParent = assignmentMap.get(templateParents.get(templateToken)
                  .list.get(1).parent);
            }
            if (wildcardParent == null) {
              NodeParentInformationHierarchy npi = programParents.get(node);
              if (npi != null && npi.list.size() > 0 && npi.list.get(0).parent != null
                  && npi.list.get(0).parent.getTag() == BastBlock.TAG) {
                wildcardParent = npi.list.get(0).parent;
              }
            }
            NodeParentInformationHierarchy parentMap = programParents.get(node);
            boolean found = false;
            for (NodeParentInformation npi : parentMap.list) {
              if (npi.parent == wildcardParent) {
                found = true;
                break;
              }
            }
            if (!found) {
              return CompareNodesResult.WILDCARD_FAIL;

            }
          }
          headNodes.add(node);
          nodes.add(node);
          childrenNodes = new ArrayList<>();
          headNodesMap.put(node, childrenNodes);
          if (children == null || children.size() == 0) {
            return CompareNodesResult.WILDCARD_ACCEPT;
          } else {
            return CompareNodesResult.WILDCARD_UNFINISHED;
          }
        } else {
          for (AbstractBastNode head : headNodes) {
            childrenNodes = childrenMap.get(head);
            if (head.getTag() == BastSwitchCaseGroup.TAG) {
              childrenNodes = new ArrayList<>();
              childrenNodes.add(head);
            }
            if (childrenNodes.contains(node)) {
              childrenMapList = headNodesMap.get(head);
              if (childrenMapList != null) {
                childrenMapList.add(node);
                nodes.add(node);
                if (childrenMapList.size() == childrenNodes.size()) {
                  if (wildcard.fixedNodes != null) {
                    if (childrenNodes.size() < wildcard.fixedNodes.size()) {
                      return CompareNodesResult.WILDCARD_FAIL; 
                    }
                    for (AbstractBastNode fn : wildcard.fixedNodes) {
                      boolean found = false;
                      HashSet<AbstractBastNode> mappedNodes = new HashSet<>();
                      for (AbstractBastNode cn :childrenNodes) {
                        if (mappedNodes.contains(cn)) {
                          continue;
                        }
                        if (fn.getTag() == cn.getTag()) {
                          found = true;
                          mappedNodes.add(cn);
                        }
                      }
                      if (!found) {
                        return CompareNodesResult.WILDCARD_FAIL;
                      }
                    }
                  }
                  return CompareNodesResult.WILDCARD_ACCEPT;
                } else {
                  return CompareNodesResult.WILDCARD_UNFINISHED;
                }
              } else {
                return CompareNodesResult.WILDCARD_FAIL;
              }
            }
          }
          headNodeHierarchy = programParents.get(headNodes.get(0));
          nodeHierarchy = programParents.get(node);
          assert (headNodeHierarchy.list != null);
          assert (nodeHierarchy.list != null);
          assert (headNodeHierarchy.list.get(0) != null);
          assert (nodeHierarchy.list.get(0) != null);
          childrenNodes = childrenMap.get(headNodes.get(headNodes.size() - 1));
          childrenMapList = headNodesMap.get(headNodes.get(headNodes.size() - 1));
          if (headNodeHierarchy.list.get(0).parent != nodeHierarchy.list.get(0).parent
              && !(headNodeHierarchy.list
                  .get(1).parent == wildcardParent 
                  && headNodeHierarchy.list.get(0).parent.getTag() == BastSwitchCaseGroup.TAG
              && (headNodeHierarchy.list.get(1).parent == nodeHierarchy.list.get(0).parent
              || headNodeHierarchy.list.get(1).parent == nodeHierarchy.list.get(1).parent))) {
            if (childrenNodes != null && childrenMapList != null
                && childrenNodes.size() == childrenMapList.size()) {
              return CompareNodesResult.WILDCARD_NO_MATCH;
            } else {
              return CompareNodesResult.WILDCARD_FAIL;
            }
          } else {
            if (headNodeHierarchy.list.get(0).fieldConstant != nodeHierarchy.list
                .get(0).fieldConstant
                && !(headNodeHierarchy.list
                    .get(0).fieldConstant == BastFieldConstants.SWITCH_CASE_GROUP_LABELS
                    && headNodeHierarchy.list
                    .get(1).parent == wildcardParent
                    && (nodeHierarchy.list
                        .get(0).fieldConstant == BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS
                        || nodeHierarchy.list
                        .get(0).fieldConstant == BastFieldConstants.SWITCH_CASE_GROUPS
                        ))) {
              if (childrenNodes.size() == childrenMapList.size()) {
                return CompareNodesResult.WILDCARD_NO_MATCH;
              } else {
                return CompareNodesResult.WILDCARD_FAIL;
              }
            } else {
              if (pluginType == PluginTester.PLUGIN_ACCEPT_ALL_EXPR) {
                if (headNodes.get(0).getTag() == BastIdentDeclarator.TAG) {
                  if (node.getTag() == BastIdentDeclarator.TAG) {
                    return CompareNodesResult.WILDCARD_NO_MATCH;
                  }
                }
              }
              headNodes.add(node);
              nodes.add(node);
              childrenNodes = new ArrayList<>();
              headNodesMap.put(node, childrenNodes);
              if (children == null || children.size() == 0) {
                return CompareNodesResult.WILDCARD_ACCEPT;
              } else {
                return CompareNodesResult.WILDCARD_UNFINISHED;
              }
            }
          }
        }
      default:
        assert (false);

    }
    return CompareNodesResult.WILDCARD_FAIL;
  }

  /**
   * Checks if is full.
   *
   * @return true, if is full
   */
  public boolean isFull() {
    return full;
  }

  /**
   * Checks if is unfinished.
   *
   * @return true, if is unfinished
   */
  public boolean isUnfinished() {
    return unfinished;
  }

  /**
   * Checks if is accepted.
   *
   * @return true, if is accepted
   */
  public boolean isAccepted() {
    if (!isFull()) {
      return false;
    }
    if (violation) {
      return false;
    }
    if (!nodes.isEmpty()) {
      switch (pluginType) {
        case PluginTester.PLUGIN_IS_SHARED:
          break;
        default:
          break;
      }
    }
    return true;
  }

}
