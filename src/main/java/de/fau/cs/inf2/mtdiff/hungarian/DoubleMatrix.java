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


/**
 * An abstract class representing a matrix of type double.
 *
 * @see symbex.utils.ByteMatrix
 */
public abstract class DoubleMatrix {
  private final int rows;
  private final int cols;



  protected DoubleMatrix(final int rows, final int cols) {
    this.rows = rows;
    this.cols = cols;
  }



  /**
   * Returns the element in the specified row and column.
   *
   * @param row the row
   * @param col the column
   * @return the value at the specified position
   */
  public abstract double get(final int row, final int col);



  /**
   * Sets the element in the specified row and column.
   *
   * @param row the row
   * @param col the column
   * @param val the new value for the specified position
   */
  public abstract void set(final int row, final int col, final double val);



  /**
   * Releases the resources hold by this matrix.
   */
  public abstract void finish();



  /**
   * Returns the number of rows.
   *
   * @return the number of rows
   */
  private int numRows() {
    return rows;
  }



  /**
   * Returns the number of columns.
   *
   * @return the number of columns
   */
  private int numCols() {
    return cols;
  }


  /**
   * Creates a new matrix without checking whether it is too large.
   *
   * @param rows the number of rows
   * @param cols the number of columns
   * @return the new double matrix
   */
  public static DoubleMatrix unsafeNewMatrix(final int rows, final int cols) {
    return new InMemoryDoubleMatrix(rows, cols);
  }


  /**
   * Creates a new matrix.
   *
   * @param rows the number of rows
   * @param cols the number of columns
   * @return the new double matrix
   */
  private static DoubleMatrix newMatrix(final int rows, final int cols) {
    return new InMemoryDoubleMatrix(rows, cols);
  }



  /**
   * Creates a new matrix.
   *
   * @param matrix the matrix
   * @return the new double matrix
   */
  public static DoubleMatrix newMatrix(final double[][] matrix) {
    final DoubleMatrix result = newMatrix(matrix.length, matrix[0].length);
    for (int i = 0; i < matrix.length; ++i) {
      for (int j = 0; j < matrix[0].length; ++j) {
        result.set(i, j, matrix[i][j]);
      }
    }
    return result;
  }



  /**
   * Creates a new matrix.
   *
   * @param matrix the matrix
   * @return the new double matrix
   */
  public static DoubleMatrix newMatrix(final DoubleMatrix matrix) {
    final DoubleMatrix result = newMatrix(matrix.numRows(), matrix.numCols());
    for (int i = 0; i < matrix.numRows(); ++i) {
      for (int j = 0; j < matrix.numCols(); ++j) {
        result.set(i, j, matrix.get(i, j));
      }
    }
    return result;
  }
}

