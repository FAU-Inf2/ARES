package de.fau.cs.inf2.mtdiff.editscript;

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A class which checks whether the children of two nodes are in the same order. If not,
 * {@see diff.tree.editscript.operations.INodeAlignOperation}s are generated to align the children
 * of the given nodes.
 *
 */
public class ChildrenAlignerINode {
  // Based on a method suggested in "Change Detection in Hierarchically
  // Structured Information" by S. Chawathe et al, which uses the Longest
  // Common Subsequence.

  private final Map<INode, INode> partners;

  /**
   * todo.
   * 
   * @param partners Contains a mapping from the nodes of the first tree to the nodes of the second
   *        tree.
   *
   */
  public ChildrenAlignerINode(final Map<INode, INode> partners) {
    this.partners = partners;
  }

  private boolean nodesEqual(final INode first, final INode second) {
    return partners.get(first) == second;
  }

  private List<INode> longestCommonSubsequence(final List<INode> firstChildren,
      final List<INode> secondChildren) {
    final int[][] matrix = new int[firstChildren.size() + 1][secondChildren.size() + 1];

    int ivar = 0;
    int jvar = 0;
    for (final INode x : firstChildren) {
      jvar = 0;
      for (final INode y : secondChildren) {
        if (nodesEqual(x, y)) {
          matrix[ivar + 1][jvar + 1] = matrix[ivar][jvar] + 1;
        } else {
          matrix[ivar + 1][jvar + 1] = Math.max(matrix[ivar][jvar + 1], matrix[ivar + 1][jvar]);
        }
        jvar++;
      }
      ivar++;
    }

    final ArrayList<INode> result =
        new ArrayList<>(matrix[firstChildren.size()][secondChildren.size()]);
    for (ivar = 0; ivar < matrix[firstChildren.size()][secondChildren.size()]; ++ivar) {
      result.add(null);
    }

    ivar = firstChildren.size();
    jvar = secondChildren.size();
    while ((ivar > 0) && (jvar > 0)) {
      if (nodesEqual(firstChildren.get(ivar - 1), secondChildren.get(jvar - 1))) {
        result.set(matrix[ivar][jvar] - 1, firstChildren.get(ivar - 1));
        ivar--;
        jvar--;
      } else {
        if (matrix[ivar][jvar - 1] > matrix[ivar - 1][jvar]) {
          jvar--;
        } else {
          ivar--;
        }
      }
    }
    return result;
  }

  /**
   * Align children.
   *
   * @param node the node
   * @param firstChildren the first children
   * @param secondChildren the second children
   * @return the int
   */
  public int alignChildren(final INode node, final List<INode> firstChildren,
      final List<INode> secondChildren) {

    int count = 0;
    final List<INode> firstList = firstChildren;
    final List<INode> secondList = secondChildren;
    final List<INode> lcs = longestCommonSubsequence(firstList, secondList);

    int oldIndex = 0;
    int lcsPointer = 0;

    for (final INode firstNode : firstList) {
      if ((lcsPointer < lcs.size()) && (firstNode == lcs.get(lcsPointer))) {
        lcsPointer++;
      } else {
        final int index = getPartnerIndex(firstNode, secondList);

        if (index >= 0 && oldIndex != index) {
          assert (partners.get(firstNode) != null && partners.get(node) != null);
          count++;
        }
      }

      oldIndex++;
    }
    return count;
  }

  private int getPartnerIndex(final INode firstNode, final List<INode> secondList) {
    final INode secondNode = partners.get(firstNode);

    if (secondNode != null) {
      int index = 0;
      for (final INode node : secondList) {
        if (node == secondNode) {
          return index;
        }
        index++;
      }
    }
    return -1;
  }
}
