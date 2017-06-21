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

package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.math.BigInteger;


/**
 * todo.
 *
 */

public class BastIntConst extends AbstractBastConstant {

  public static final int TAG = TagConstants.BAST_INT_CONST;
  public boolean fitsInLong = true;
  public boolean isLongValue = false;

  @SuppressWarnings("unused")
  private boolean unsigned = false;

  public long value;

  public BigInteger bigValue = null;
  
  /**
   * Instantiates a new bast int const.
   *
   * @param tokens the tokens
   * @param value the value
   */
  public BastIntConst(TokenAndHistory[] tokens, long value) {
    super(tokens);
    this.value = value;
  }
  
  /**
   * Instantiates a new bast int const.
   *
   * @param tokens the tokens
   * @param value the value
   */
  public BastIntConst(TokenAndHistory[] tokens, String value) {
    super(tokens);
    value = value.replace("_", "");
    if (value.endsWith("L") || value.endsWith("l")) {
      isLongValue = true;
      value = value.substring(0, value.length() - 1);
    }
    if (value.endsWith("u") || value.endsWith("U")) {
      unsigned = true;
      value = value.substring(0, value.length() - 1);
    }
    if (value.startsWith("L#")) {
      value = value.substring(2);
      isLongValue = true;
    }
    try {
      this.value = Long.valueOf(value);
    } catch (NumberFormatException e) {
      fitsInLong = false;
      try {
        if (value.startsWith("0x") || value.startsWith("0X")) {
          try {
            if (isLongValue || (value.length() > 10)) {
              this.value = Long.parseUnsignedLong(value.substring(2), 16);
            } else {
              this.value = Integer.parseUnsignedInt(value.substring(2), 16);
            }
            fitsInLong = true;
          } catch (NumberFormatException e3) {
            bigValue = new BigInteger(value.substring(2), 16);
          }
        } else if (value.startsWith("W#16#") || value.startsWith("B#16#")) {
          bigValue = new BigInteger(value.substring(5), 16);
        } else if (value.startsWith("2#")) {
          bigValue = new BigInteger(value.substring(2), 2);
        } else if (value.startsWith("DW#16#")) {
          bigValue = new BigInteger(value.substring(6), 16);
        } else if (value.startsWith("C#")) {
          bigValue = new BigInteger(value.substring(2));
        } else {
          bigValue = new BigInteger(value);
        }
      } catch (NumberFormatException e2) {
        System.err.println("value: " + value);
        System.err.println("Line: " + line + " Column: " + column);
        e.printStackTrace();
      }

    }
  }

  
  /**
   * Accept.
   *
   * @param visitor the visitor
   */
  public void accept(IBastVisitor visitor) {
    visitor.visit(this);

  }

  
  /**
   * Gets the priority.
   *
   * @return the priority
   */
  public int getPriority() {
    return 0;
  }

  /**
   * Gets the tag.
   *
   * @return the tag
   */
  @Override
  public int getTag() {
    return TAG;
  }

  
  /**
   * Replace field.
   *
   * @param field the field
   * @param fieldValue the field value
   */
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    assert (false);
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    if (fitsInLong == false) {
      return bigValue.toString();
    } else {
      return String.valueOf(value);
    }
  }

}
