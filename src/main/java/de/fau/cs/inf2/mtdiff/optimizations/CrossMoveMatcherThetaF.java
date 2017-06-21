package de.fau.cs.inf2.mtdiff.optimizations;

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.ComparePair;

import de.fau.cs.inf2.mtdiff.MappingWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.Set;

public class CrossMoveMatcherThetaF {
  
  /**
   * Theta F.
   *
   * @param tree1 the tree 1
   * @param tree2 the tree 2
   * @param resultMap the result map
   * @param unmatchedNodes1 the unmatched nodes 1
   * @param unmatchedNodes2 the unmatched nodes 2
   * @param parents1 the parents 1
   * @param parents2 the parents 2
   */
  public static void thetaF(INode tree1, INode tree2,
      IdentityHashMap<INode, ComparePair<INode>> resultMap, Set<INode> unmatchedNodes1,
      Set<INode> unmatchedNodes2, IdentityHashMap<INode, INode> parents1,
      IdentityHashMap<INode, INode> parents2) {
    MappingWrapper mappings = new MappingWrapper(resultMap);
    ArrayList<ComparePair<INode>> workList = new ArrayList<>(mappings.asSet());
    Collections.sort(workList, new BfsComparator(tree1, tree2));


    for (ComparePair<INode> pair : workList) {
      INode parentOld = parents1.get(pair.getOldElement());
      INode parentNew = parents2.get(pair.getNewElement());
      if (mappings.hasSrc(parentOld) && mappings.getDst(parentOld) != parentNew) {
        if (mappings.hasDst(parentNew) && mappings.getSrc(parentNew) != parentOld) {
          INode parentOldOther = mappings.getSrc(parentNew);
          INode parentNewOther = mappings.getDst(parentOld);
          if (parentOld.getTypeWrapped() == parentNewOther.getTypeWrapped()
              && parentNew.getTypeWrapped() == parentOldOther.getTypeWrapped()
              && parentOld.getLabel().equals(parentNewOther.getLabel())
              && parentNew.getLabel().equals(parentOldOther.getLabel())) {
            boolean done = false;
            for (INode childOldOther : parentOldOther.getChildrenWrapped()) {
              if (mappings.hasSrc(childOldOther)) {
                INode childNewOther = mappings.getDst(childOldOther);
                if (pair.getOldElement().getLabel().equals(childNewOther.getLabel())
                    && childOldOther.getLabel().equals(pair.getNewElement().getLabel())
                    || !(pair.getOldElement().getLabel().equals(pair.getNewElement().getLabel())
                        || childOldOther.getLabel().equals(childNewOther.getLabel()))) {
                  if (parents2.get(childNewOther) == parentNewOther) {
                    if (childOldOther.getTypeWrapped() == pair.getOldElement().getTypeWrapped()) {
                      mappings.unlink(pair.getOldElement(), pair.getNewElement());
                      mappings.unlink(childOldOther, childNewOther);
                      mappings.addMapping(pair.getOldElement(), childNewOther);
                      mappings.addMapping(childOldOther, pair.getNewElement());
                    }
                  }
                }
              }
            }
            if (!done) {
              for (INode childNewOther : parentNewOther.getChildrenWrapped()) {
                if (mappings.hasDst(childNewOther)) {
                  INode childOldOther = mappings.getSrc(childNewOther);
                  if (parents1.get(childOldOther) == parentOldOther) {
                    if (childNewOther.getTypeWrapped() == pair.getNewElement().getTypeWrapped()) {
                      if (pair.getOldElement().getLabel().equals(childNewOther.getLabel())
                          && childOldOther.getLabel().equals(pair.getNewElement().getLabel())
                          || !(pair.getOldElement().getLabel()
                              .equals(pair.getNewElement().getLabel())
                              || childOldOther.getLabel().equals(childNewOther.getLabel()))) {
                        mappings.unlink(pair.getOldElement(), pair.getNewElement());
                        mappings.unlink(childOldOther, childNewOther);
                        mappings.addMapping(childOldOther, pair.getNewElement());
                        mappings.addMapping(pair.getOldElement(), childNewOther);
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
  
  private static class BfsComparator implements Comparator<ComparePair<INode>> {

    private HashMap<Integer, Integer> positionSrc;
    private HashMap<Integer, Integer> positionDst;

    private HashMap<Integer, Integer> getHashSet(INode tree) {
      HashMap<Integer, Integer> map = new HashMap<>();
      ArrayList<INode> list = new ArrayList<>();
      LinkedList<INode> workList = new LinkedList<>();
      workList.add(tree);
      while (!workList.isEmpty()) {
        INode node = workList.removeFirst();
        list.add(node);
        workList.addAll(node.getChildrenWrapped());
      }
      for (int i = 0; i < list.size(); i++) {
        map.put(list.get(i).getId(), i);
      }
      return map;
    }

    public BfsComparator(INode src, INode dst) {
      positionSrc = getHashSet(src);
      positionDst = getHashSet(dst);
    }

    @Override
    public int compare(ComparePair<INode> o1, ComparePair<INode> o2) {
      if (o1.getOldElement().getId() != o2.getOldElement().getId()) {
        return Integer.compare(positionSrc.get(o1.getOldElement().getId()),
            positionSrc.get(o2.getOldElement().getId()));
      }
      return Integer.compare(positionDst.get(o1.getNewElement().getId()),
          positionDst.get(o2.getNewElement().getId()));
    }

  }
}
