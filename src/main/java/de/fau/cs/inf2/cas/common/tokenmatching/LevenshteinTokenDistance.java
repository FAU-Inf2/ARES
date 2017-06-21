package de.fau.cs.inf2.cas.common.tokenmatching;

import de.fau.cs.inf2.cas.ares.bast.general.AresWrapper;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.visitors.CollectNodesVisitor;

import java.util.ArrayList;
import java.util.Objects;

public class LevenshteinTokenDistance {

  /**
   * Match.
   *
   * @param anode the a
   * @param bnode the b
   * @return the double
   */
  public static double match(AbstractBastNode anode, AbstractBastNode bnode) {
    if (anode == null && bnode == null) {
      return 1.0;
    }
    CollectNodesVisitor cnv = new CollectNodesVisitor();
    if (anode != null) {
      anode.accept(cnv);
    }
    ArrayList<AbstractBastNode> listA = cnv.nodes;
    cnv = new CollectNodesVisitor();
    bnode.accept(cnv);
    ArrayList<AbstractBastNode> listB = cnv.nodes;
    AbstractBastNode[] arrayA = listA.toArray(new AbstractBastNode[listA.size()]);
    AbstractBastNode[] arrayB = listB.toArray(new AbstractBastNode[listB.size()]);
    return matchArrays(arrayA, arrayB);
  }

  /**
   * Match arrays.
   *
   * @param arrayA the array a
   * @param arrayB the array b
   * @return the double
   */
  public static double matchArrays(AbstractBastNode[] arrayA, AbstractBastNode[] arrayB) {
    int maxLen = Math.max(arrayA.length, arrayB.length);
    int delta = levenshteinDistance(arrayA, arrayB);
    double rdouble = (double) (maxLen - delta) / maxLen;
    return rdouble;
  }

  /**
   * Levenshtein distance.
   *
   * @param anodes the s
   * @param bnodes the t
   * @return the int
   */
  // Source: http://en.wikipedia.org/wiki/Levenshtein_distance
  public static int levenshteinDistance(AbstractBastNode[] anodes, AbstractBastNode[] bnodes) {
    if (anodes == bnodes) {
      return 0;
    }
    if (anodes.length == 0) {
      return bnodes.length;
    }
    if (bnodes.length == 0) {
      return anodes.length;
    }

    int[] v0 = new int[bnodes.length + 1];
    int[] v1 = new int[bnodes.length + 1];

    for (int i = 0; i < v0.length; i++) {
      v0[i] = i;
    }

    for (int i = 0; i < anodes.length; i++) {
      v1[0] = i + 1;
      for (int j = 0; j < bnodes.length; j++) {
        int cost = (anodes[i].getTag() == bnodes[j].getTag())
            && Objects.equals(AresWrapper.staticGetValue(anodes[i]),
              AresWrapper.staticGetValue(bnodes[j])) ? 0 : 1;
        v1[j + 1] = Math.min(v1[j] + 1, Math.min(v0[j + 1] + 1, v0[j] + cost));
      }
      for (int j = 0; j < v0.length; j++) {
        v0[j] = v1[j];
      }
    }

    return v1[bnodes.length];
  }
}
