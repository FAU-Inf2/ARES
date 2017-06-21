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

package de.fau.cs.inf2.mtdiff.hungarian;



final class InMemoryDoubleMatrix extends DoubleMatrix {
  private final double[][] data;


  @SuppressWarnings("ucd")
  InMemoryDoubleMatrix(final int rows, final int cols) {
    super(rows, cols);
    data = new double[rows][cols];
  }



  
  /**
   * {@inheritDoc}.
   */
  @Override
  public double get(final int row, final int col) {
    return data[row][col];
  }



  
  /**
   * {@inheritDoc}.
   */
  @Override
  public void set(final int row, final int col, final double val) {
    data[row][col] = val;
  }



  
  /**
   * {@inheritDoc}.
   */
  @Override
  public void finish() {}
}

