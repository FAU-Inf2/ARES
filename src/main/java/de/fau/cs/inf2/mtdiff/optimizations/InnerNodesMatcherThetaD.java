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
import java.util.Map.Entry;

public class InnerNodesMatcherThetaD {

  /**
   * Theta D.
   *
   * @param parents1 the parents 1
   * @param parents2 the parents 2
   * @param mappings the mappings
   */
  public static void thetaD(IdentityHashMap<INode, INode> parents1,
      IdentityHashMap<INode, INode> parents2, MappingWrapper mappings) {
    IdentityHashMap<INode, IdentityHashMap<INode, Integer>> parentCount = new IdentityHashMap<>();

    for (ComparePair<INode> pair : mappings.asSet()) {
      INode parent = parents1.get(pair.getOldElement());
      INode parentPartner = parents2.get(pair.getNewElement());
      if (parent != null && parentPartner != null) {
        IdentityHashMap<INode, Integer> countMap = parentCount.get(parent);
        if (countMap == null) {
          countMap = new IdentityHashMap<>();
          parentCount.put(parent, countMap);
        }
        Integer count = countMap.get(parentPartner);
        if (count == null) {
          count = new Integer(0);
        }
        countMap.put(parentPartner, count + 1);
      }
    }
    LinkedList<Entry<INode, IdentityHashMap<INode, Integer>>> list =
        new LinkedList<>(parentCount.entrySet());
    Collections.sort(list, new ChangeMapComparator());
    for (Entry<INode, IdentityHashMap<INode, Integer>> countEntry : list) {
      int max = Integer.MIN_VALUE;
      int maxCount = 0;
      INode maxNode = null;
      for (Entry<INode, Integer> newNodeEntry : countEntry.getValue().entrySet()) {
        if (newNodeEntry.getValue() > max) {
          max = newNodeEntry.getValue();
          maxCount = 1;
          maxNode = newNodeEntry.getKey();
        } else if (newNodeEntry.getValue() == max) {
          maxCount++;
        }
      }
      if (maxCount == 1) {
        if (mappings.getDst(countEntry.getKey()) != null && mappings.getSrc(maxNode) != null) {
          INode partner = mappings.getDst(countEntry.getKey());
          INode maxNodePartner = mappings.getSrc(maxNode);
          if (partner != maxNode) {
            if (max > countEntry.getKey().getChildrenWrapped().size() / 2
                || countEntry.getKey().getChildrenWrapped().size() == 1) {
              INode parentPartner = mappings.getDst(parents1.get(countEntry.getKey()));

              if (parentPartner != null && parentPartner == parents2.get(partner)) {
                continue;
              }
              if (allowedMatching(countEntry.getKey(), maxNodePartner, parents1)) {
                if (countEntry.getKey().getTypeWrapped() == maxNode.getTypeWrapped()) {
                  if (maxNodePartner != null) {
                    mappings.unlink(maxNodePartner, maxNode);
                  }
                  if (partner != null) {
                    mappings.unlink(countEntry.getKey(), partner);
                  }
                  mappings.addMapping(countEntry.getKey(), maxNode);
                }
                if (maxNodePartner != null) {
                  if (maxNodePartner.getTypeWrapped() == partner.getTypeWrapped()) {
                    mappings.addMapping(maxNodePartner, partner);
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  private static class ChangeMapComparator
      implements Comparator<Entry<INode, IdentityHashMap<INode, Integer>>> {

    @Override
    public int compare(Entry<INode, IdentityHashMap<INode, Integer>> o1,
        Entry<INode, IdentityHashMap<INode, Integer>> o2) {

      return Integer.compare(o1.getKey().getId(), o2.getKey().getId());
    }

  }

  private static boolean allowedMatching(INode key, INode maxNodePartner,
      IdentityHashMap<INode, INode> parents1) {
    while (key != null) {
      if (key == maxNodePartner) {
        return false;
      }
      key = parents1.get(key);
    }
    return true;
  }
}
