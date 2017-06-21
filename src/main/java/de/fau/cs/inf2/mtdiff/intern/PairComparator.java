package de.fau.cs.inf2.mtdiff.intern;

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import java.util.Comparator;
import java.util.Map;

/**
 * Comparator used for leaf matching to enforce an ordering on matched pairs.
 */
public class PairComparator<T extends INode> implements Comparator<MatchingCandidate<T>> {

  private Map<T, Integer> order1 = null;
  private Map<T, Integer> order2 = null;

  /**
   * Instantiates a new pair comparator.
   *
   * @param order1 the order 1
   * @param order2 the order 2
   */
  public PairComparator(Map<T, Integer> order1, Map<T, Integer> order2) {
    this.order1 = order1;
    this.order2 = order2;
  }

  
  /**
   * Compare.
   *
   * @param o1 the o1
   * @param o2 the o2
   * @return the int
   */
  public int compare(final MatchingCandidate<T> o1, final MatchingCandidate<T> o2) {
    final int valComparisonResult = o1.getValue().compareTo(o2.getValue());
    if (valComparisonResult != 0) {
      return valComparisonResult;
    }

    int tag1 = o1.getOldElement().getTypeWrapped();
    int tag2 = o2.getOldElement().getTypeWrapped();
    if (tag1 != tag2) {
      return Integer.compare(tag1, tag2);
    }

    int index1 = order1.get(o1.getOldElement());
    int index2 = order1.get(o2.getOldElement());
    order2.get(o1.getNewElement());
    order2.get(o2.getNewElement());

    int index = Long.compare(index1, index2);
    if (index != 0) {
      return index;
    }

    index1 = order2.get(o1.getNewElement());
    index2 = order2.get(o2.getNewElement());
    index = Long.compare(index1, index2);
    if (index == 0) {
      return Long.compare(o2.getId(), o1.getId());

    }
    assert (index != 0);
    return index;
  }
}
