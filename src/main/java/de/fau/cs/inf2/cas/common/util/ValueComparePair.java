package de.fau.cs.inf2.cas.common.util;

/**
 * A {@see ComparePair} with an additional value field. However, the value field is not considered
 * when comparing two instances of this class. Thus two instances of ValueComparePair with the same
 * elements but different values would be considered equal and would return the same hash code.
 *
 */
public class ValueComparePair<T, V> extends ComparePair<T> {
  private final V value;

  /**
   * Instantiates a new value compare pair.
   *
   * @param oldElement the old element
   * @param newElement the new element
   * @param value the value
   */
  public ValueComparePair(final T oldElement, final T newElement, final V value) {
    super(oldElement, newElement);
    this.value = value;
  }

  /**
   * todo.
   * 
   * <p>Returns the value associated with this pair.
   *
   * @return the value
   */
  public final V getValue() {
    return value;
  }

  /**
   * todo.
   * 
   * <p>Returns an equivalent {@see ComparePair} without the associated value.
   *
   * @return the compare pair
   */
  public final ComparePair<T> dropValue() {
    return new ComparePair<T>(getOldElement(), getNewElement());
  }

  
  /**
   * Equals.
   *
   * @param obj the obj
   * @return true, if successful
   */
  public final boolean equals(final Object obj) {
    return super.equals(obj);
  }

  
  /**
   * Hash code.
   *
   * @return the int
   */
  public final int hashCode() {
    return super.hashCode();
  }
}
