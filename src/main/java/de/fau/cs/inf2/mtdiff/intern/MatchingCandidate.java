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
