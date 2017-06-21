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

package de.fau.cs.inf2.cas.common.util;

/**
 * A container for a pair of values which imposes an ordering on the associated elements, i.e. one
 * of the elements is regarded as old whereas the other one is considered new.
 *
 */
public class ComparePair<T> {
  private T oldElement;
  private T newElement;

  /**
   * Instantiates a new compare pair.
   *
   * @param oldElement the old element
   * @param newElement the new element
   */
  public ComparePair(final T oldElement, final T newElement) {
    this.oldElement = oldElement;
    this.newElement = newElement;
  }

  /**
   * todo.
   * 
   * <p>Returns the old element.
   *
   * @return the old element
   */
  public final T getOldElement() {
    return oldElement;
  }

  /**
   * todo.
   * 
   * <p>Returns the new element.
   *
   * @return the new element
   */
  public final T getNewElement() {
    return newElement;
  }

  /**
   * todo.
   * 
   * <p>Sets the old element.
   *
   * @param oldElement the new old element
   */
  public final void setOldElement(final T oldElement) {
    this.oldElement = oldElement;
  }

  /**
   * todo.
   * 
   * <p>Sets the new element.
   *
   * @param newElement the new new element
   */
  public final void setNewElement(final T newElement) {
    this.newElement = newElement;
  }

  /**
   * Equals.
   *
   * @param obj the obj
   * @return true, if successful
   */
  public boolean equals(final ComparePair<T> obj) {
    return oldElement.equals(obj.getOldElement()) && newElement.equals(obj.getNewElement());
  }

  
  /**
   * Equals.
   *
   * @param obj the obj
   * @return true, if successful
   */
  @SuppressWarnings("unchecked")
  public boolean equals(final Object obj) {
    if (obj instanceof ComparePair) {
      return equals((ComparePair<T>) obj);
    }
    return false;
  }

  
  /**
   * Hash code.
   *
   * @return the int
   */
  public int hashCode() {
    return oldElement.hashCode() ^ newElement.hashCode();
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return oldElement.toString() + "<->" + newElement.toString();
  }

}
