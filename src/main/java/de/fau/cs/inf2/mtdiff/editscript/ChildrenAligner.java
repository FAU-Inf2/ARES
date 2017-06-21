package de.fau.cs.inf2.mtdiff.editscript;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.cas.common.util.ChildrenList;
import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.editscript.operations.AlignOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A class which checks whether the children of two nodes are in the same order. If not,
 * {@see diff.tree.editscript.operations.INodeAlignOperation}s are generated to align the children
 * of the given nodes.
 *
 */
public class ChildrenAligner {
  private final Map<AbstractBastNode, AbstractBastNode> partners;

  /**
   * todo.
   * 
   * @param partners Contains a mapping from the nodes of the first tree to the nodes of the second
   *        tree.
   *
   */
  public ChildrenAligner(final Map<AbstractBastNode, AbstractBastNode> partners) {
    this.partners = partners;
  }

  private boolean nodesEqual(final AbstractBastNode first, final AbstractBastNode second) {

    return partners.get(first) == second;
  }

 

  /**
   * Longest common subsequence.
   *
   * @param firstChildren the first children
   * @param secondChildren the second children
   * @return the list
   */
  public List<AbstractBastNode> longestCommonSubsequence(final List<AbstractBastNode> firstChildren,
      final List<AbstractBastNode> secondChildren) {
    final int[][] matrix = new int[firstChildren.size() + 1][secondChildren.size() + 1];

    int ivar = 0;
    int jvar = 0;
    for (final AbstractBastNode x : firstChildren) {
      jvar = 0;
      for (final AbstractBastNode y : secondChildren) {
        if (nodesEqual(x, y)) {
          matrix[ivar + 1][jvar + 1] = matrix[ivar][jvar] + 1;
        } else {
          matrix[ivar + 1][jvar + 1] = Math.max(matrix[ivar][jvar + 1], matrix[ivar + 1][jvar]);
        }
        jvar++;
      }
      ivar++;
    }

    final ArrayList<AbstractBastNode> result =
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
   * Check whether the children are in the same order. If not, generate appropriate
   * {@see diff.tree.editscript.operations.INodeAlignOperation}s to align them.
   * 
   * @param node The node whose children are examined. This node should belong to the first tree in
   *        order to retain consistency.
   *
   * @param firstChildren The children of the first (i.e. older) tree
   * @param secondChildren The children of the second (i.e. newer) tree
   * @param editScript The list, to which the generated edit operations are written
   */
  public void alignChildren(final AbstractBastNode node, final List<ChildrenList> firstChildren,
      final List<ChildrenList> secondChildren, final List<BastEditOperation> editScript) {

    final Iterator<ChildrenList> firstIt = firstChildren.iterator();
    final Iterator<ChildrenList> secondIt = secondChildren.iterator();

    while (firstIt.hasNext() && secondIt.hasNext()) {
      ChildrenList firstCl = firstIt.next();
      ChildrenList secondCl = secondIt.next();

      final List<AbstractBastNode> firstList = firstCl.list;
      final List<AbstractBastNode> secondList = secondCl.list;
      final List<AbstractBastNode> lcs = longestCommonSubsequence(firstList, secondList);

      int oldIndex = 0;
      HashSet<AbstractBastNode> set = new HashSet<>(lcs);
      for (final AbstractBastNode firstNode : firstList) {
        if (!set.contains(firstNode)) {
          final int index = getPartnerIndex(firstNode, secondList);

          if (index >= 0) {
            assert (partners.get(firstNode) != null && partners.get(node) != null);
            editScript.add(
                new AlignOperation((AbstractBastNode) node, (AbstractBastNode) partners.get(node),
                    (AbstractBastNode) firstNode, (AbstractBastNode) partners.get(firstNode),
                    new NodeIndex(firstCl.fieldConstant, oldIndex),
                    new NodeIndex(firstCl.fieldConstant, index)));
          }
        }

        oldIndex++;
      }
    }
  }

  private int getPartnerIndex(final AbstractBastNode firstNode,
      final List<AbstractBastNode> secondList) {
    final AbstractBastNode secondNode = partners.get(firstNode);

    if (secondNode != null) {
      int index = 0;
      for (final AbstractBastNode node : secondList) {
        if (node == secondNode) {
          return index;
        }
        index++;
      }
    }
    return -1;
  }
}
