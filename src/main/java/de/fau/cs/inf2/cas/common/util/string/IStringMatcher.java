package de.fau.cs.inf2.cas.common.util.string;

/**
 * Interface for classes that perform string matching.
 * 
 * @author Christoph Romstoeck
 */
interface IStringMatcher {

  /**
   * Calculates a value describing the similarity of the two strings.
   * 
   * @param astring The first string.
   * @param bstring The second string.
   * @return A value between zero (the strings do not match at all) and one (the strings are exactly
   *         equal).
   */
  public double match(String astring, String bstring);

}
