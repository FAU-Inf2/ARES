package de.fau.cs.inf2.mtdiff.intern;

import de.fau.cs.inf2.cas.common.util.ValueComparePair;

import java.util.concurrent.atomic.AtomicLong;

/**
 * A class representing a matching candidate.
 */
public class MatchingCandidate<T> extends ValueComparePair<T, Float>
    implements Comparable<MatchingCandidate<T>> {
  private static AtomicLong counter = new AtomicLong();

  private final long id;

  /**
   * Instantiates a new matching candidate.
   *
   * @param oldElement the old element
   * @param newElement the new element
   * @param value the value
   */
  public MatchingCandidate(final T oldElement, final T newElement, final Float value) {
    super(oldElement, newElement, value);

    id = counter.incrementAndGet();
  }

  /**
   * Gets the id.
   *
   * @return the id
   */
  public long getId() {
    return id;
  }

  
  /**
   * Compare to.
   *
   * @param object the o
   * @return the int
   */
  @Override
  public int compareTo(MatchingCandidate<T> object) {
    return Float.compare(object.getValue(), this.getValue());
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return "(" + getOldElement().toString() + ", " + getNewElement().toString() + ")";
  }
}
