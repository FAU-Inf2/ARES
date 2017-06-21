package de.fau.cs.inf2.cas.common.util.string;

import de.fau.cs.inf2.cas.ares.bast.general.ParserFactory;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.visitors.IPrettyPrinter;

public class LevenshteinDistance implements IStringMatcher {

  /**
   * The main method.
   *
   * @param args todo
   */
  public static void main(String[] args) {
    String astring = "abcdefg";
    String bstring = "abdefg_";
    LevenshteinDistance dist = new LevenshteinDistance();
    System.out.println(dist.match(astring, bstring));
  }

  
  /**
   * Match.
   *
   * @param astring the a
   * @param bstring the b
   * @return the double
   */
  @Override
  public double match(String astring, String bstring) {
    return levenshteinMatch(astring, bstring);
  }

  /**
   * Levenshtein match.
   *
   * @param anode the a node
   * @param bnode the b node
   * @return the double
   */
  public static double levenshteinMatch(AbstractBastNode anode, AbstractBastNode bnode) {
    IPrettyPrinter printer = ParserFactory.getAresPrettyPrinter();
    anode.accept(printer);
    String astring = printer.getBuffer().toString();
    printer = ParserFactory.getAresPrettyPrinter();
    bnode.accept(printer);
    String bstring = printer.getBuffer().toString();
    if (astring.isEmpty() && bstring.isEmpty()) {
      return 1.0;
    }

    int maxLen = Math.max(astring.length(), bstring.length());
    int delta = levenshteinDistance(astring, bstring);
    double rdouble = (double) (maxLen - delta) / maxLen;
    return rdouble;
  }

  /**
   * Levenshtein match.
   *
   * @param astring the a
   * @param bstring the b
   * @return the double
   */
  public static double levenshteinMatch(String astring, String bstring) {
    if (astring.isEmpty() && bstring.isEmpty()) {
      return 1.0;
    }

    int maxLen = Math.max(astring.length(), bstring.length());
    int delta = levenshteinDistance(astring, bstring);
    double rdouble = (double) (maxLen - delta) / maxLen;
    return rdouble;
  }

  private static int levenshteinDistance(String astring, String bstring) {
    final int sLen = astring.length();
    final int tLen = bstring.length();
    if ((sLen > 200) && (Math.abs(sLen - tLen) < 0.10 * Math.min(sLen, tLen))) {
      return levenshteinDistanceAlternative(astring, bstring);
    } else {
      return levenshteinDistanceDefault(astring, bstring);
    }
  }

  // Source: http://en.wikipedia.org/wiki/Levenshtein_distance
  private static int levenshteinDistanceDefault(String astring, String bstring) {
    if (astring == bstring) {
      return 0;
    }

    final int sLen = astring.length();
    final int tLen = bstring.length();
    if (sLen == 0) {
      return tLen;
    }
    if (tLen == 0) {
      return sLen;
    }
    int[] v0 = new int[tLen + 1];
    for (int i = 0; i < v0.length; i++) {
      {
        {
          v0[i] = i;
        }
      }
    }

    for (int i = 0; i < sLen; i++) {
      int last = i + 1;
      final char sc = astring.charAt(i);
      for (int j = 0; j < tLen; j++) {
        final int cost = (sc == bstring.charAt(j)) ? 0 : 1;
        final int cur = Math.min(Math.min(last, v0[j + 1]) + 1, v0[j] + cost);
        v0[j] = last;
        last = cur;
      }
      v0[tLen] = last;
    }

    return v0[tLen];
  }

  private static int levenshteinDistanceAlternative(String stringOne, String stringTwo) {
    final int sLen = stringOne.length();
    final int tLen = stringTwo.length();
    final int Max = sLen + tLen;
    int[] px = new int[Max + Max + 1];
    int[] pxx = new int[Max + Max + 1];

    int counter;
    for (counter = 0; (counter < sLen) && (counter < tLen) 
        && (stringOne.charAt(counter) == stringTwo.charAt(counter)); ++counter) {
      {
        {
          ;
        }
      }
    }

    if ((counter == sLen) && (counter == tLen)) {
      return 0;
    }

    px[Max] = pxx[Max] = counter;

    for (int d = 1; d <= Max; ++d) {
      for (int k = -d + 1; k < d; ++k) {
        final int k0 = Max + k;
        final int x = px[k0];
        final int y = x - k;

        if ((y < 0) || (y > tLen)) {
          continue;
        }
        if (y < tLen) {
          int xdown = x;
          int ydown = y + 1;
          while ((xdown < sLen) && (ydown < tLen) 
              && (stringOne.charAt(xdown) == stringTwo.charAt(ydown))) {
            xdown += 1;
            ydown += 1;
          }
          if ((xdown >= sLen) && (ydown >= tLen)) {
            return d;
          }

          pxx[k0 - 1] = Math.max(pxx[k0 - 1], xdown);
        }

        if (x < sLen) {
          int xright = x + 1;
          int yright = y;
          while ((xright < sLen) && (yright < tLen) 
              && (stringOne.charAt(xright) == stringTwo.charAt(yright))) {
            xright += 1;
            yright += 1;
          }
          if ((xright >= sLen) && (yright >= tLen)) {
            return d;
          }

          pxx[k0 + 1] = Math.max(pxx[k0 + 1], xright);
        }
        if ((x < sLen) && (y < tLen)) {
          int xdiag = x + 1;
          int ydiag = y + 1;
          while ((xdiag < sLen) && (ydiag < tLen) 
              && (stringOne.charAt(xdiag) == stringTwo.charAt(ydiag))) {
            xdiag += 1;
            ydiag += 1;
          }
          if ((xdiag >= sLen) && (ydiag >= tLen)) {
            return d;
          }
          pxx[k0] = Math.max(pxx[k0], xdiag);
        }
      }
      int[] temp = px;
      px = pxx;
      pxx = temp;
    }
    return Max + 1;
  }

}
