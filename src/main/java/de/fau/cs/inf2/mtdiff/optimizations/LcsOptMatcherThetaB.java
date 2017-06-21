/*
 * Copyright (c) 2017 Programming Systems Group, CS Department, FAU
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */

package de.fau.cs.inf2.mtdiff.optimizations;

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.ComparePair;

import de.fau.cs.inf2.mtdiff.MappingWrapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class LcsOptMatcherThetaB {

  private static final long LIMIT = 1024L * 1024 * 1024 * 4;


  /**
   * Advanced lcs matching.
   *
   * @param resultMap the result map
   * @param unmatchedNodes1 the unmatched nodes 1
   * @param unmatchedNodes2 the unmatched nodes 2
   * @param parents1 the parents 1
   * @param parents2 the parents 2
   * @param src the src
   * @param dst the dst
   */
  public static void advancedLcsMatching(IdentityHashMap<INode, ComparePair<INode>> resultMap,
      Set<INode> unmatchedNodes1, Set<INode> unmatchedNodes2,
      IdentityHashMap<INode, INode> parents1, IdentityHashMap<INode, INode> parents2, INode src,
      INode dst) {
    MappingWrapper mappings = new MappingWrapper(resultMap);
    if (unmatchedNodes1.size() > 0 && unmatchedNodes2.size() > 0) {
      ArrayList<INode> workList = new ArrayList<>();
      getUnmatchedNodeListInPostOrder(src, workList, unmatchedNodes1);
      HashSet<INode> checkedParent = new HashSet<>();
      for (INode node : workList) {

        if (!unmatchedNodes1.contains(node)) {
          continue;
        }
        INode parent = parents1.get(node);
        if (parent == null) {

          continue;
        }

        INode partner = null;
        if (parent == src) {
          partner = dst;
        } else {
          partner = mappings.getDst(parent);
        }

        while (parent != null && partner == null) {
          parent = parents1.get(parent);
          partner = mappings.getDst(parent);
        }
        if (parent != null && partner != null) {
          if (checkedParent.contains(parent)) {
            continue;
          }
          checkedParent.add(parent);
          ArrayList<INode> list1 = new ArrayList<>();
          ArrayList<INode> list2 = new ArrayList<>();
          getNodeListInPostOrder(parent, list1);
          getNodeListInPostOrder(partner, list2);
          List<ComparePair<INode>> lcsMatch =
              lcs(list1, list2, unmatchedNodes1, unmatchedNodes2, mappings);

          for (ComparePair<INode> match : lcsMatch) {
            if (!mappings.hasDst(match.getNewElement())
                && !mappings.hasSrc(match.getOldElement())) {
              mappings.addMapping(match.getOldElement(), match.getNewElement());
              unmatchedNodes1.remove(match.getOldElement());
              unmatchedNodes2.remove(match.getNewElement());
            }
          }
        }
      }
    }
  }

  private static void getUnmatchedNodeListInPostOrder(INode tree, ArrayList<INode> nodes,
      Set<INode> unmatchedNodes1) {
    if (tree != null) {
      for (INode child : tree.getChildrenWrapped()) {
        getNodeListInPostOrder(child, nodes);
      }
      if (unmatchedNodes1.contains(tree)) {
        nodes.add(tree);
      }
    }
  }

  private static void getNodeListInPostOrder(INode tree, ArrayList<INode> nodes) {
    if (tree != null) {
      for (INode child : tree.getChildrenWrapped()) {
        getNodeListInPostOrder(child, nodes);
      }
      nodes.add(tree);
    }
  }

  private static List<ComparePair<INode>> lcs(ArrayList<INode> list1, ArrayList<INode> list2,
      Set<INode> unmatchedNodes1, Set<INode> unmatchedNodes2, MappingWrapper mappings) {
    if ((long) list1.size() * list2.size() * 4 > LIMIT) {
      return new LinkedList<>();
    }
    int[][] matrix = new int[list1.size() + 1][list2.size() + 1];
    for (int i = 1; i < list1.size() + 1; i++) {
      for (int j = 1; j < list2.size() + 1; j++) {
        if (testCondition(list1.get(i - 1), list2.get(j - 1), unmatchedNodes1, unmatchedNodes2,
            mappings)) {
          matrix[i][j] = matrix[i - 1][j - 1] + 1;
        } else {
          matrix[i][j] = Math.max(matrix[i][j - 1], matrix[i - 1][j]);
        }
      }
    }
    LinkedList<ComparePair<INode>> resultList = new LinkedList<>();
    backtrack(list1, list2, resultList, matrix, list1.size(), list2.size(), unmatchedNodes1,
        unmatchedNodes2, mappings);
    return resultList;
  }



  private static void backtrack(ArrayList<INode> list1, ArrayList<INode> list2,
      LinkedList<ComparePair<INode>> resultList, int[][] matrix, int firstIndex, int secondIndex,
      Set<INode> unmatchedNodes1, Set<INode> unmatchedNodes2, MappingWrapper mappings) {
    assert (firstIndex >= 0);
    assert (secondIndex >= 0);
    while (firstIndex > 0 && secondIndex > 0) {
      if (testCondition(list1.get(firstIndex - 1), list2.get(secondIndex - 1), unmatchedNodes1,
          unmatchedNodes2, mappings)) {
        if (!mappings.hasSrc(list1.get(firstIndex - 1))) {
          resultList
              .add(new ComparePair<INode>(list1.get(firstIndex - 1), list2.get(secondIndex - 1)));
        }
      }
      if (matrix[firstIndex][secondIndex - 1] > matrix[firstIndex - 1][secondIndex]) {
        secondIndex--;
      } else {
        firstIndex--;
      }
    }
  }

  /**
   * Test condition.
   *
   * @param node1 the node1
   * @param node2 the node2
   * @param unmatchedNodes1 the unmatched nodes1
   * @param unmatchedNodes2 the unmatched nodes2
   * @param mappings the mappings
   * @return true, if successful
   */
  public static boolean testCondition(INode node1, INode node2, Set<INode> unmatchedNodes1,
      Set<INode> unmatchedNodes2, MappingWrapper mappings) {
    if (node1.getTypeWrapped() != node2.getTypeWrapped()) {
      return false;
    }
    if (mappings.hasSrc(node1) && mappings.getDst(node1) == node2) {
      return true;
    }
    if (unmatchedNodes1.contains(node1) && unmatchedNodes2.contains(node2)) {
      return true;
    }
    return false;
  }
}
