package de.fau.cs.inf2.cas.common.util.string;

public interface IStringSimilarityMeasure {
  
  /**
   * todo.
   * 
   * <p>Calculate the similarity between two strings.
   * 
   *
   * @param s1 the s1
   * @param s2 the s2
   * @return A value between 0.0 and 1.0, where the former one indicates that the given strings are
   *         not similar at all, whereas the latter one indicates that the two strings are equal.
   */
  public float similarity(String s1, String s2);
}
