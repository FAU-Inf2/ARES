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
    return match(anode, bnode, true);
  }
  

  /**

   * Match.

   *

   * @param anode the a

   * @param bnode the b

   * @return the double

   */

  public static double match(AbstractBastNode anode, AbstractBastNode bnode, boolean exactMatch) {

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

    return matchArrays(arrayA, arrayB, exactMatch);

  }


  /**
   * Match arrays.
   *
   * @param arrayA the array a
   * @param arrayB the array b
   * @return the double
   */
  public static double matchArrays(AbstractBastNode[] arrayA, AbstractBastNode[] arrayB) {
    return matchArrays(arrayA, arrayB, true);
  }
  

  /**

   * Match arrays.

   *

   * @param arrayA the array a

   * @param arrayB the array b

   * @return the double

   */

  public static double matchArrays(AbstractBastNode[] arrayA,
      AbstractBastNode[] arrayB, boolean exactMatch) {

    int maxLen = Math.max(arrayA.length, arrayB.length);

    int delta = levenshteinDistance(arrayA, arrayB, exactMatch);

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
    return levenshteinDistance(anodes, bnodes, true);
  }
  

  /**

   * Levenshtein distance.

   *

   * @param anodes the s

   * @param bnodes the t

   * @return the int

   */

  // Source: http://en.wikipedia.org/wiki/Levenshtein_distance

  public static int levenshteinDistance(AbstractBastNode[] anodes,
      AbstractBastNode[] bnodes, boolean exactMatch) {

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

            && (!exactMatch || Objects.equals(AresWrapper.staticGetValue(anodes[i]),

              AresWrapper.staticGetValue(bnodes[j]))) ? 0 : 1;

        v1[j + 1] = Math.min(v1[j] + 1, Math.min(v0[j + 1] + 1, v0[j] + cost));

      }

      for (int j = 0; j < v0.length; j++) {

        v0[j] = v1[j];

      }

    }



    return v1[bnodes.length];

  }

}
