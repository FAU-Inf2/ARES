package de.fau.cs.inf2.mtdiff.optimizations;

import de.fau.cs.inf2.cas.common.bast.diff.LabelConfiguration;
import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.ComparePair;
import de.fau.cs.inf2.cas.common.util.string.NGramCalculator;

import de.fau.cs.inf2.mtdiff.TreeMatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class IdenticalSubtreeMatcherThetaA {
  
  /**
   * New unchanged matching.
   *
   * @param labelConfiguration the label configuration
   * @param resultMap the result map
   * @param unmatchedNodes1 the unmatched nodes 1
   * @param unmatchedNodes2 the unmatched nodes 2
   * @param stringSim the string sim
   * @param skipList the skip list
   * @param tree1 the tree 1
   * @param tree2 the tree 2
   */
  public static void newUnchangedMatching(LabelConfiguration labelConfiguration,
      IdentityHashMap<INode, ComparePair<INode>> resultMap, Set<INode> unmatchedNodes1,
      Set<INode> unmatchedNodes2, NGramCalculator stringSim, HashSet<INode> skipList,
      INode tree1, INode tree2) {
    IdentityHashMap<INode, Integer> quickFind = new IdentityHashMap<>();
    IdentityHashMap<INode, String> stringMap = new IdentityHashMap<>();
    TreeMatcher.getHash(tree1, quickFind, stringMap);
    TreeMatcher.getHash(tree2, quickFind, stringMap);
    HashMap<String, LinkedList<INode>> nodeMapOld = new HashMap<>();
    List<INode> streamOld = TreeMatcher.getNodeStream(tree1);
    List<INode> streamNew = TreeMatcher.getNodeStream(tree2);
    for (INode node : streamOld) {
      String hashString = stringMap.get(node);
      LinkedList<INode> nodeList = nodeMapOld.get(hashString);
      if (nodeList == null) {
        nodeList = new LinkedList<>();
        nodeMapOld.put(hashString, nodeList);
      }
      nodeList.add(node);
    }
    HashMap<String, LinkedList<INode>> nodeMapNew = new HashMap<>();

    for (INode node : streamNew) {
      String hashString = stringMap.get(node);
      LinkedList<INode> nodeList = nodeMapNew.get(hashString);
      if (nodeList == null) {
        nodeList = new LinkedList<>();
        nodeMapNew.put(hashString, nodeList);
      }
      nodeList.add(node);
    }

    HashSet<ComparePair<INode>> pairs = new HashSet<>();
    LinkedList<INode> workList = new LinkedList<>();
    workList.add(tree1);
    while (!workList.isEmpty()) {
      INode node = workList.removeFirst();
      LinkedList<INode> oldList = nodeMapOld.get(stringMap.get(node));
      assert (oldList != null);
      LinkedList<INode> newList = nodeMapNew.get(stringMap.get(node));
      if (oldList.size() == 1 && newList != null && newList.size() == 1) {
        if (node.getChildrenWrapped().size() > 0) {
          pairs.add(new ComparePair<>(node, newList.getFirst()));
          stringMap.remove(node);
          oldList.remove(node);
          newList.removeFirst();
        }
      } else {
        workList.addAll(node.getChildrenWrapped());
      }
    }
    for (ComparePair<INode> pair : pairs) {
      List<INode> stream1 = TreeMatcher.getNodeStream(pair.getOldElement());
      List<INode> stream2 = TreeMatcher.getNodeStream(pair.getNewElement());
      stream1 = new ArrayList<>(stream1);
      stream2 = new ArrayList<>(stream2);
      assert (stream1.size() == stream2.size());
      for (int i = 0; i < stream1.size(); i++) {
        INode oldNode = stream1.get(i);
        INode newNode = stream2.get(i);
        assert (oldNode.getTypeWrapped() == newNode.getTypeWrapped());
        if (oldNode.getLabel() != null) {
          assert (oldNode.getLabel().equals(newNode.getLabel()));
        }
        assert (resultMap.get(oldNode) == null);
        resultMap.put(oldNode, new ComparePair<INode>(oldNode, newNode));
        skipList.add(oldNode);
        skipList.add(newNode);
        unmatchedNodes1.remove(oldNode);
        unmatchedNodes2.remove(newNode);
      }
    }
  }
}
