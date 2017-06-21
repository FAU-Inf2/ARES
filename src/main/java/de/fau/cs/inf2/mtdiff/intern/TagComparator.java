package de.fau.cs.inf2.mtdiff.intern;

import de.fau.cs.inf2.cas.common.bast.diff.LabelConfiguration;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

public class TagComparator implements Comparator<Map.Entry<Integer, Integer>> {

  private LabelConfiguration labelConfiguration;

  /**
   * Instantiates a new tag comparator.
   *
   * @param labelConfiguration the label configuration
   */
  public TagComparator(LabelConfiguration labelConfiguration) {
    this.labelConfiguration = labelConfiguration;
  }

  
  /**
   * Compare.
   *
   * @param o1 the o1
   * @param o2 the o2
   * @return the int
   */
  @Override
  public int compare(Entry<Integer, Integer> o1, Entry<Integer, Integer> o2) {
    if (((Map.Entry<Integer, Integer>) (o1)).getKey() == labelConfiguration.identifierLabel) {
      return Integer.MIN_VALUE;
    } else {
      if (((Map.Entry<Integer, Integer>) (o2)).getKey() == labelConfiguration.identifierLabel) {
        return Integer.MAX_VALUE;
      } else {
        return (((o1)).getValue()).compareTo(((o2)).getValue());
      }
    }
  }
}
