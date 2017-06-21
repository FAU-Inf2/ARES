package de.fau.cs.inf2.mtdiff.optimizations;

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.mtdiff.MappingWrapper;

import java.util.IdentityHashMap;
import java.util.Set;

public class UnmappedLeavesMatcherThetaC {
  
  /**
   * Theta C.
   *
   * @param unmatchedNodes1 the unmatched nodes 1
   * @param unmatchedNodes2 the unmatched nodes 2
   * @param parents1 the parents 1
   * @param parents2 the parents 2
   * @param mappings the mappings
   */
  public static void thetaC(Set<INode> unmatchedNodes1, Set<INode> unmatchedNodes2,
      IdentityHashMap<INode, INode> parents1, IdentityHashMap<INode, INode> parents2,
      MappingWrapper mappings) {
    for (INode node : unmatchedNodes1) {
      examineUnmatchedNodes(parents1, mappings, node);
    }
    for (INode node : unmatchedNodes2) {
      if (mappings.hasSrc(node)) {
        continue;
      }
      examineUnmatchedNodesInModifiedTree(unmatchedNodes2, parents2, mappings, node);
    }
  }

  private static void examineUnmatchedNodesInModifiedTree(Set<INode> unmatchedNodes2,
      IdentityHashMap<INode, INode> parents2, MappingWrapper mappings, INode node) {
    if (node.getChildrenWrapped().size() == 0) {
      INode parent = parents2.get(node);
      if (mappings.getSrc(parent) != null) {
        INode partner = mappings.getSrc(parent);
        int pos = parent.getChildrenWrapped().indexOf(node);
        if (pos < partner.getChildrenWrapped().size()) {
          INode child = partner.getChildrenWrapped().get(pos);
          if (child.getTypeWrapped() == node.getTypeWrapped()) {
            if (child.getLabel().equals(node.getLabel())) {
              INode tree = mappings.getDst(child);
              if (tree != null) {
                if (!tree.getLabel().equals(node.getLabel())) {
                  mappings.unlink(child, tree);
                  mappings.addMapping(child, node);
                }
              } else {
                mappings.addMapping(child, node);
              }
            } else {
              INode childPartner = mappings.getDst(child);
              if (childPartner != null) {
                if (mappings.getSrc(parents2.get(childPartner)) == null) {
                  if (!childPartner.getLabel().equals(child.getLabel())) {
                    mappings.unlink(child, childPartner);
                    mappings.addMapping(child, node);
                  }
                }
              } else {
                mappings.addMapping(child, node);

              }
            }
          } else {
            if (child.getChildrenWrapped().size() == 1) {
              child = child.getChildrenWrapped().get(0);
              if (child.getTypeWrapped() == node.getTypeWrapped()
                  && child.getLabel().equals(node.getLabel())) {
                INode childPartner = mappings.getDst(child);
                if (childPartner != null) {
                  if (!childPartner.getLabel().equals(node.getLabel())) {
                    mappings.unlink(child, childPartner);
                    mappings.addMapping(child, node);
                  } else if (mappings.getSrc(parents2.get(childPartner)) == null) {
                    mappings.unlink(childPartner, child);
                    mappings.addMapping(node, child);
                  }
                }
              }
            } else {
              for (int i = 0; i < partner.getChildrenWrapped().size(); i++) {
                INode possibleMatch = partner.getChildrenWrapped().get(i);
                if (possibleMatch.getTypeWrapped() == node.getTypeWrapped()
                    && possibleMatch.getLabel().equals(node.getLabel())) {
                  INode possibleMatchDst = mappings.getDst(possibleMatch);
                  if (possibleMatchDst == null) {
                    mappings.addMapping(possibleMatch, node);
                    break;
                  } else {
                    if (!possibleMatchDst.getLabel().equals(possibleMatch.getLabel())) {
                      mappings.unlink(possibleMatch, possibleMatchDst);
                      mappings.addMapping(possibleMatch, node);
                      break;
                    }
                  }
                }
              }
            }
          }
        }
      } else if (unmatchedNodes2.contains(parent)) {
        INode oldParent = parent;
        parent = parents2.get(parent);
        if (mappings.getSrc(parent) != null) {
          INode partner = mappings.getSrc(parent);
          int pos = parent.getChildrenWrapped().indexOf(oldParent);
          if (pos < partner.getChildrenWrapped().size()) {
            INode child = partner.getChildrenWrapped().get(pos);
            if (child.getTypeWrapped() == node.getTypeWrapped()
                && child.getLabel().equals(node.getLabel())) {
              INode tree = mappings.getDst(child);
              if (tree != null) {
                if (!tree.getLabel().equals(node.getLabel())) {
                  mappings.unlink(child, tree);
                  mappings.addMapping(child, node);
                }
              } else {
                mappings.addMapping(child, node);
              }
            }
          }
        }
      }
    }
  }

  private static void examineUnmatchedNodes(IdentityHashMap<INode, INode> parents1,
      MappingWrapper mappings, INode node) {
    if (node.getChildrenWrapped().size() == 0) {

      INode parent = parents1.get(node);
      if (mappings.getDst(parent) != null) {
        INode partner = mappings.getDst(parent);
        int pos = parent.getChildrenWrapped().indexOf(node);
        if (pos < partner.getChildrenWrapped().size()) {
          INode child = partner.getChildrenWrapped().get(pos);
          if (child.getTypeWrapped() == node.getTypeWrapped()) {
            if (child.getLabel().equals(node.getLabel())) {
              INode childPartner = mappings.getSrc(child);
              if (childPartner != null) {
                if (!childPartner.getLabel().equals(node.getLabel())) {
                  mappings.unlink(childPartner, child);
                  mappings.addMapping(node, child);
                }
              } else {
                mappings.addMapping(node, child);

              }
            } else {
              INode childPartner = mappings.getSrc(child);
              if (childPartner != null) {
                if (mappings.getDst(parents1.get(childPartner)) == null) {
                  if (!childPartner.getLabel().equals(child.getLabel())) {
                    mappings.unlink(childPartner, child);
                    mappings.addMapping(node, child);
                  }
                }
              } else {
                mappings.addMapping(node, child);

              }
            }
          } else {
            if (child.getChildrenWrapped().size() == 1) {
              child = child.getChildrenWrapped().get(0);
              if (child.getTypeWrapped() == node.getTypeWrapped()
                  && child.getLabel().equals(node.getLabel())) {
                INode childPartner = mappings.getSrc(child);
                if (childPartner != null) {
                  if (!childPartner.getLabel().equals(node.getLabel())) {
                    mappings.unlink(childPartner, child);
                    mappings.addMapping(node, child);
                  } else if (mappings.getDst(parents1.get(childPartner)) == null) {
                    mappings.unlink(childPartner, child);
                    mappings.addMapping(node, child);
                  }
                }
              }
            } else {
              for (int i = 0; i < partner.getChildrenWrapped().size(); i++) {
                INode possibleMatch = partner.getChildrenWrapped().get(i);
                if (possibleMatch.getTypeWrapped() == node.getTypeWrapped()
                    && possibleMatch.getLabel().equals(node.getLabel())) {
                  INode possibleMatchSrc = mappings.getSrc(possibleMatch);
                  if (possibleMatchSrc == null) {
                    mappings.addMapping(node, possibleMatch);
                    break;
                  } else {
                    if (!possibleMatchSrc.getLabel().equals(possibleMatch.getLabel())) {
                      mappings.unlink(possibleMatchSrc, possibleMatch);
                      mappings.addMapping(node, possibleMatch);
                      break;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
  }
}
