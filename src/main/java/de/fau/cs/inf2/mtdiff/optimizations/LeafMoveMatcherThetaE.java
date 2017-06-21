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

import java.util.Collections;
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;

public class LeafMoveMatcherThetaE {
  
  private static class MappingComparator implements Comparator<ComparePair<INode>> {

    @Override
    public int compare(ComparePair<INode> o1, ComparePair<INode> o2) {
      if (o1.getOldElement().getId() != o2.getOldElement().getId()) {
        return Integer.compare(o1.getOldElement().getId(), o2.getOldElement().getId());
      }
      return Integer.compare(o1.getNewElement().getId(), o2.getNewElement().getId());
    }

  }
  
  /**
   * Theta E.
   *
   * @param parents1 the parents 1
   * @param parents2 the parents 2
   * @param mappings the mappings
   */
  public static void thetaE(IdentityHashMap<INode, INode> parents1,
      IdentityHashMap<INode, INode> parents2, MappingWrapper mappings) {
    LinkedList<ComparePair<INode>> workList = new LinkedList<>();
    LinkedList<ComparePair<INode>> workListTmp = null;
    LinkedList<ComparePair<INode>> changeMap = new LinkedList<>();

    for (ComparePair<INode> pair : mappings.asSet()) {
      if (pair.getOldElement().isLeaf() && pair.getNewElement().isLeaf()) {
        if (pair.getOldElement().getLabel() != null) {
          if (!pair.getOldElement().getLabel().equals(pair.getNewElement().getLabel())) {
            workList.add(pair);
          }
        }
      }

    }
    while (!workList.isEmpty()) {
      Collections.sort(workList, new MappingComparator());
      workListTmp = new LinkedList<>();
      for (ComparePair<INode> pair : workList) {
        INode firstParent = parents1.get(pair.getOldElement());
        if (!mappings.hasDst(firstParent)) {
          continue;
        }
        INode secondParent = mappings.getDst(parents1.get(pair.getOldElement()));
        reevaluateLeaves(firstParent, secondParent, pair, changeMap, mappings);
      }
      Collections.sort(changeMap, new MappingComparator());
      for (ComparePair<INode> entry : changeMap) {
        if (!mappings.hasSrc(entry.getOldElement()) && !mappings.hasDst(entry.getNewElement())) {
          mappings.addMapping(entry.getOldElement(), entry.getNewElement());
        }
        if (!entry.getOldElement().getLabel().equals(entry.getNewElement().getLabel())
            && entry.getOldElement().isLeaf() && entry.getNewElement().isLeaf()) {
          workListTmp.add(new ComparePair<INode>(entry.getOldElement(), entry.getNewElement()));
        }
      }
      changeMap.clear();
      workList = workListTmp;
    }

    workList = new LinkedList<>();
    workListTmp = null;

    for (ComparePair<INode> pair : mappings.asSet()) {
      if (pair.getOldElement().isLeaf() && pair.getNewElement().isLeaf()) {
        if (pair.getOldElement().getLabel() != null) {
          if (!pair.getOldElement().getLabel().equals(pair.getNewElement().getLabel())) {
            workList.add(pair);
          }
        }
      }

    }
    while (!workList.isEmpty()) {
      Collections.sort(workList, new MappingComparator());
      workListTmp = new LinkedList<>();
      for (ComparePair<INode> pair : workList) {
        INode firstParent = parents1.get(pair.getOldElement());
        INode secondParent = parents2.get(pair.getNewElement());
        reevaluateLeaves(firstParent, secondParent, pair, changeMap, mappings);
      }
      Collections.sort(changeMap, new MappingComparator());
      for (ComparePair<INode> entry : changeMap) {
        if (!mappings.hasSrc(entry.getOldElement()) && !mappings.hasDst(entry.getNewElement())) {
          mappings.addMapping(entry.getOldElement(), entry.getNewElement());
        }
        if (!entry.getOldElement().getLabel().equals(entry.getNewElement().getLabel())
            && entry.getOldElement().isLeaf() && entry.getNewElement().isLeaf()) {
          workListTmp.add(new ComparePair<INode>(entry.getOldElement(), entry.getNewElement()));
        }
      }
      changeMap.clear();
      workList = workListTmp;
    }
  }

  

  

  private static void reevaluateLeaves(INode firstParent, INode secondParent,
      ComparePair<INode> pair, List<ComparePair<INode>> changeMap, MappingWrapper mappings) {
    int count = 0;
    INode foundDstNode = null;
    INode foundPosDstNode = null;
    int pos = firstParent.getChildrenWrapped().indexOf(pair.getOldElement());
    for (int i = 0; i < secondParent.getChildrenWrapped().size(); i++) {
      INode child = secondParent.getChildrenWrapped().get(i);
      if (child.getTypeWrapped() == pair.getOldElement().getTypeWrapped()
          && child.getLabel().equals(pair.getOldElement().getLabel())) {
        count++;
        foundDstNode = child;
        if (i == pos) {
          foundPosDstNode = child;
        }
      }
    }
    ComparePair<INode> addedMappingKey = null;

    if ((count == 1 && foundDstNode != null) || foundPosDstNode != null) {
      if (count != 1 && foundPosDstNode != null) {
        foundDstNode = foundPosDstNode;
      }
      if (mappings.hasDst(foundDstNode)) {

        addedMappingKey = hasPartner(pair, changeMap, mappings, foundDstNode, addedMappingKey);
      } else {
        hasNoPartner(firstParent, pair, changeMap, mappings, foundDstNode);
      }
    }
    INode foundSrcNode = null;
    INode foundPosSrcNode = null;
    pos = secondParent.getChildrenWrapped().indexOf(pair.getNewElement());
    for (int i = 0; i < firstParent.getChildrenWrapped().size(); i++) {
      INode child = firstParent.getChildrenWrapped().get(i);
      if (child.getTypeWrapped() == pair.getNewElement().getTypeWrapped()
          && child.getLabel().equals(pair.getNewElement().getLabel())) {
        count++;
        foundSrcNode = child;
        if (i == pos) {
          foundPosSrcNode = child;
        }
      }
    }
    if ((count == 1 && foundSrcNode != null) || foundPosSrcNode != null) {
      if (count != 1 && foundPosSrcNode != null) {
        foundSrcNode = foundPosSrcNode;
      }
      if (addedMappingKey != null) {
        changeMap.remove(addedMappingKey);
      }
      if (mappings.hasSrc(foundSrcNode)) {
        INode foundDst = mappings.getSrc(foundSrcNode);
        if (foundDst != null && foundSrcNode != null
            && !foundDst.getLabel().equals(foundSrcNode.getLabel())) {

          mappings.unlink(pair.getOldElement(), pair.getNewElement());
          mappings.unlink(foundSrcNode, foundDst);
          changeMap.add(new ComparePair<INode>(foundSrcNode, pair.getNewElement()));
          if (addedMappingKey == null) {
            if (foundDst != pair.getNewElement() && foundSrcNode != pair.getOldElement()) {

              changeMap.add(new ComparePair<INode>(pair.getOldElement(), foundDst));
            }
          }
        }
      } else {
        mappings.unlink(pair.getOldElement(), pair.getNewElement());
        if (foundSrcNode.getLabel().equals(pair.getNewElement().getLabel())) {
          LinkedList<ComparePair<INode>> toRemove = new LinkedList<>();
          for (ComparePair<INode> mapPair : changeMap) {
            if (mapPair.getOldElement() == foundSrcNode) {
              if (!mapPair.getOldElement().getLabel().equals(mapPair.getNewElement().getLabel())) {
                toRemove.add(mapPair);
              }
            } else if (mapPair.getNewElement() == pair.getNewElement()) {
              if (!mapPair.getOldElement().getLabel().equals(mapPair.getNewElement().getLabel())) {
                toRemove.add(mapPair);
              }
            }
          }
          changeMap.removeAll(toRemove);
        }
        changeMap.add(new ComparePair<INode>(foundSrcNode, pair.getNewElement()));
        for (INode child : secondParent.getChildrenWrapped()) {
          if (child.isLeaf() && !mappings.hasSrc(child)
              && child.getTypeWrapped() == pair.getOldElement().getTypeWrapped()
              && child.getLabel().equals(pair.getOldElement().getLabel())) {
            mappings.addMapping(pair.getOldElement(), child);
            break;
          }
        }
      }
    }
  }





  private static void hasNoPartner(INode firstParent, ComparePair<INode> pair,
      List<ComparePair<INode>> changeMap, MappingWrapper mappings, INode foundDstNode) {
    mappings.unlink(pair.getOldElement(), pair.getNewElement());
    if (pair.getOldElement().getLabel().equals(foundDstNode.getLabel())) {
      LinkedList<ComparePair<INode>> toRemove = new LinkedList<>();
      for (ComparePair<INode> mapPair : changeMap) {
        if (mapPair.getOldElement() == pair.getOldElement()) {
          if (!mapPair.getOldElement().getLabel().equals(mapPair.getNewElement().getLabel())) {
            toRemove.add(mapPair);
          }
        } else if (mapPair.getNewElement() == foundDstNode) {
          if (!mapPair.getOldElement().getLabel().equals(mapPair.getNewElement().getLabel())) {
            toRemove.add(mapPair);
          }
        }
      }
      changeMap.removeAll(toRemove);
    }
    changeMap.add(new ComparePair<INode>(pair.getOldElement(), foundDstNode));
    for (INode child : firstParent.getChildrenWrapped()) {
      if (child.isLeaf() && !mappings.hasDst(child)
          && child.getTypeWrapped() == pair.getNewElement().getTypeWrapped()
          && child.getLabel().equals(pair.getNewElement().getLabel())) {
        mappings.addMapping(child, pair.getNewElement());
        break;
      }
    }
  }





  private static ComparePair<INode> hasPartner(ComparePair<INode> pair,
      List<ComparePair<INode>> changeMap, MappingWrapper mappings, INode foundDstNode,
      ComparePair<INode> addedMappingKey) {
    INode foundSrc = mappings.getSrc(foundDstNode);
    if (!foundSrc.getLabel().equals(foundDstNode.getLabel())) {

      mappings.unlink(pair.getOldElement(), pair.getNewElement());
      mappings.unlink(foundSrc, foundDstNode);
      changeMap.add(new ComparePair<INode>(pair.getOldElement(), foundDstNode));
      addedMappingKey = new ComparePair<INode>(foundSrc, foundDstNode);
      if (foundSrc != pair.getOldElement() && foundDstNode != pair.getNewElement()) {
        changeMap.add(new ComparePair<INode>(foundSrc, pair.getNewElement()));
      }
    }
    return addedMappingKey;
  }
}
