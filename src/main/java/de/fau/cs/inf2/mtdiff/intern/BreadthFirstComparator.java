package de.fau.cs.inf2.mtdiff.intern;

import java.util.Comparator;
import java.util.Map;

class BreadthFirstComparator<T> implements Comparator<T> {
  private Map<T, Integer> order;

  /**
   * Instantiates a new breadth first comparator.
   *
   * @param order the order
   */
  BreadthFirstComparator(Map<T, Integer> order) {
    this.order = order;
  }

  
  /**
   * Compare.
   *
   * @param o1 the o1
   * @param o2 the o2
   * @return the int
   */
  @Override
  public int compare(T o1, T o2) {
    int index1 = order.get(o1);
    int index2 = order.get(o2);
    return Integer.compare(index1, index2);
  }

}
