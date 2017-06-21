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

package de.fau.cs.inf2.cas.common.bast.diff;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

import java.math.BigInteger;


/**
 * A class containing a value whose type is not known at compile time. Currently, only values of the
 * type INT (which is represented by a BigInteger), REAL (which is represented by a Double), STRING
 * and BOOL are supported.
 *
 */
public final class ValueContainer {
  public static final int TYPE_NULL = 0;
  private static final int TYPE_INT = 1;
  private static final int TYPE_REAL = 2;
  private static final int TYPE_STRING = 3;
  private static final int TYPE_BOOL = 4;

  /**
   * todo.
   *
   *<p>An empty ValueContainer. It is used to represent nonexistant values (similar to a
   * <code>null</code> value).
   */
  public static final ValueContainer NULL_VALUE = new ValueContainer();

  private final Object value;
  private final int type;

  private ValueContainer() {
    value = null;
    type = TYPE_NULL;
  }

  /**
   * todo.
   *
   *<p>Create a ValueContainer representing a BigInteger.
   */
  ValueContainer(final BigInteger intValue) {
    assert intValue != null;
    value = intValue;
    type = TYPE_INT;
  }

  /**
   * todo.
   *
   *<p>Create a ValueContainer representing a double.
   */
  ValueContainer(final double doubleValue) {
    value = Double.valueOf(doubleValue);
    type = TYPE_REAL;
  }

  /**
   * todo.
   *
   *<p>Create a ValueContainer representing a String.
   */
  ValueContainer(final String stringValue) {
    assert stringValue != null;
    value = stringValue;
    type = TYPE_STRING;
  }

  ValueContainer(final boolean boolValue) {
    value = Boolean.valueOf(boolValue);
    type = TYPE_BOOL;
  }

  /**
   * todo.
   * 
   * <p>Get the type of the value which is associated with this ValueContainer.
   *
   * @return the type
   */
  public int getType() {
    return type;
  }

  /**
   * todo.
   * If this ValueContainer represents an INT object, the associated object is returned. Otherwise,
   * <code>null</code> is returned.
   * 
   * @return The associated BigInteger value, if any
   */
  public BigInteger getIntValue() {
    if (type == TYPE_INT) {
      return (BigInteger) value;
    }
    return null;
  }

  /**
   * todo.
   * If this ValueContainer represents a REAL object, the associated object is returned. Otherwise,
   * <code>null</code> is returned.
   * 
   * @return The associated Double value, if any
   */
  public Double getDoubleValue() {
    if (type == TYPE_REAL) {
      return (Double) value;
    }
    return null;
  }

  /**
   * todo.
   * If this ValueContainer represents a String object, the associated object is returned.
   * Otherwise, <code>null</code> is returned.
   * 
   * @return The associated String value, if any
   */
  public String getStringValue() {
    if (type == TYPE_STRING) {
      return (String) value;
    }
    return null;
  }

  /**
   * todo.
   * If this ValueContainer represents a Boolean object, the associated object is returned.
   * Otherwise, <code>null</code> is returned.
   * 
   * @return The associated Boolean value, if any
   */
  @SuppressFBWarnings(value = "NP_BOOLEAN_RETURN_NULL")
  public Boolean getBooleanValue() {
    if (type == TYPE_BOOL) {
      return (Boolean) value;
    }
    return null;
  }

  /**
   * Equals.
   *
   * @param obj the obj
   * @return true, if successful
   */
  public boolean equals(final ValueContainer obj) {
    if (type == obj.getType()) {
      switch (type) {
        case TYPE_NULL:
          return true;
        case TYPE_INT:
          return obj.getIntValue().equals((BigInteger) value);
        case TYPE_REAL:
          return obj.getDoubleValue().equals((Double) value);
        case TYPE_STRING:
          return obj.getStringValue().equals((String) value);
        case TYPE_BOOL:
          return obj.getBooleanValue().equals((Boolean) value);
        default:
          break;
      }
    }
    return false;
  }

  
  /**
   * Equals.
   *
   * @param obj the obj
   * @return true, if successful
   */
  public boolean equals(final Object obj) {
    if (obj instanceof ValueContainer) {
      return equals((ValueContainer) obj);
    }
    return super.equals(obj);
  }

  
  /**
   * Hash code.
   *
   * @return the int
   */
  public int hashCode() {
    switch (type) {
      case TYPE_INT:
        return getIntValue().hashCode();
      case TYPE_REAL:
        return getDoubleValue().hashCode();
      case TYPE_STRING:
        return getStringValue().hashCode();
      case TYPE_BOOL:
        return getBooleanValue().hashCode();
      default:
        break;
    }
    return 0;
  }
}
