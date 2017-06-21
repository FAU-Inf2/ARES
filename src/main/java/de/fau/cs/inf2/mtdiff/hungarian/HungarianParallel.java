package de.fau.cs.inf2.mtdiff.hungarian;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;



/**
 * An implementation of the so-called Hungarian Algorithm (Kuhn-Munkres algorithm) for the linear
 * assignment problem. It is based on http://csclab.murraystate.edu/bob.pilgrim/445/munkres.html
 *
 */
public final class HungarianParallel {
  private static final double EPSILON = 1e-12;



  private static final class Pair {
    public int row;
    public int col;

    public Pair(final int row, final int col) {
      this.row = row;
      this.col = col;
    }
  }



  private HungarianParallel() {}



  /**
   * todo.
   * Find a minimizing solution for the linear assignment problem specified by the matrix.
   * 
   * @param matrix the matrix
   * @return An array with an element for each row containing the assigned column of the matrix.
   */
  public static int[] assign(final double[][] matrix, int numThreads) {
    final double[][] matrixNxN = makeNxN(matrix);
    subtractSmallest(matrixNxN);
    ExecutorService service = Executors.newCachedThreadPool();
    final byte[][] mask = new byte[matrixNxN.length][matrixNxN.length];
    findZeros(matrixNxN, mask, service, numThreads);
    service.shutdown();
    return readResult(mask);
  }



  private static double[][] makeNxN(final double[][] matrix) {
    assert matrix.length > 0;
    boolean quadratic = true;
    int max = matrix.length;
    for (int i = 0; i < matrix.length; i++) {
      if (matrix[i].length != matrix.length) {
        quadratic = false;
        max = Math.max(max, matrix[i].length);
      }

    }
    if (quadratic) {
      return matrix;
    }

    final double[][] result = new double[max][max];
    for (int i = 0; i < max; ++i) {
      for (int j = 0; j < max; ++j) {
        if ((i >= matrix.length) || (j >= matrix[i].length)) {
          result[i][j] = 0;
        } else {
          result[i][j] = matrix[i][j];
        }
      }
    }
    return result;
  }



  private static void subtractSmallest(final double[][] matrix) {
    final int n = matrix.length;
    for (int i = 0; i < n; ++i) {
      double minValue = matrix[i][0];
      for (int j = 1; j < n; ++j) {
        minValue = Math.min(minValue, matrix[i][j]);
      }
      for (int j = 0; j < n; ++j) {
        matrix[i][j] = (matrix[i][j] - minValue);
      }
    }
  }



  private static void findZeros(final double[][] matrix, final byte[][] mask,
      ExecutorService service, int numThreads) {
    final int n = matrix.length;
    final boolean[] rowCover = new boolean[n];
    final boolean[] colCover = new boolean[n];

    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        if ((matrix[i][j] < EPSILON) && !rowCover[i] && !colCover[j]) {
          mask[i][j] = (byte) 1;
          rowCover[i] = true;
          colCover[j] = true;
        }
      }
    }
    Arrays.fill(rowCover, false);
    Arrays.fill(colCover, false);

    coverColumns(matrix, mask, rowCover, colCover, service, numThreads);
  }



  private static void coverColumns(final double[][] matrix, final byte[][] mask,
      final boolean[] rowCover, final boolean[] colCover, ExecutorService service, int numThreads) {
    final int n = matrix.length;

    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        if (mask[i][j] == 1) {
          colCover[j] = true;
        }
      }
    }

    int colCount = 0;
    for (int j = 0; j < n; ++j) {
      if (colCover[j]) {
        colCount += 1;
      }
    }
    if (colCount < n) {
      findNonCovered(matrix, mask, rowCover, colCover, service, numThreads);
    }
  }



  private static void findNonCovered(final double[][] matrix, final byte[][] mask,
      final boolean[] rowCover, final boolean[] colCover, ExecutorService service, int numThreads) {
    int row = -1;
    int col = -1;
    boolean done = false;
    do {
      final Pair zero = findAZero(matrix, mask, rowCover, colCover, service, numThreads);
      row = zero.row;
      col = zero.col;

      if (row == -1) {
        addSmallest(matrix, mask, rowCover, colCover);
      } else {
        mask[row][col] = (byte) 2;
        int starInRow = findStarInRow(mask, row);
        if (starInRow >= 0) {
          rowCover[row] = true;
          colCover[starInRow] = false;
        } else {
          done = true;
          alternatingSeries(matrix, mask, rowCover, colCover, row, col, service, numThreads);
        }
      }
    }
    while (!done);
  }



  private static void alternatingSeries(final double[][] matrix, final byte[][] mask,
      final boolean[] rowCover, final boolean[] colCover, final int pathRow0, final int pathCol0,
      ExecutorService service, int numThreads) {
    final ArrayList<Pair> path = new ArrayList<>();
    int pathCount = 1;
    path.add(new Pair(pathRow0, pathCol0));

    boolean done = false;
    do {
      int col = path.get(pathCount - 1).col;
      int row = findStarInCol(mask, col);
      if (row > -1) {
        pathCount += 1;
        path.add(new Pair(row, col));
      } else {
        done = true;
      }

      if (!done) {
        row = path.get(pathCount - 1).row;
        col = findPrimeInRow(mask, row);
        pathCount += 1;
        path.add(new Pair(row, col));
      }
    }
    while (!done);

    augmentPath(mask, pathCount, path);
    clearCovers(rowCover, colCover);
    erasePrimes(mask);
    coverColumns(matrix, mask, rowCover, colCover, service, numThreads);
  }



  private static void addSmallest(final double[][] matrix, final byte[][] mask,
      final boolean[] rowCover, final boolean[] colCover) {
    final int n = matrix.length;

    final double minVal = findSmallest(matrix, rowCover, colCover);

    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        if (rowCover[i] && colCover[j]) {
          matrix[i][j] = (matrix[i][j] + minVal);
        }
        if (!rowCover[i] && !colCover[j]) {
          matrix[i][j] = (matrix[i][j] - minVal);
        }
      }
    }
  }



  private static int[] readResult(final byte[][] mask) {
    final int n = mask.length;

    final int[] result = new int[n];
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        if (mask[i][j] == 1) {
          result[i] = j;
          break;
        }
      }
    }

    return result;
  }

  private static class HungarianRunnable implements Runnable {
    private boolean[] rowCover;
    private boolean[] colCover;
    private AtomicBoolean done;
    private AtomicInteger row;
    private AtomicInteger col;
    private int numRows;
    private final double[][] matrix;
    private AtomicInteger workCounter;
    private final int start;
    private final int end;

    @SuppressWarnings("PMD.ExcessiveParameterList")
    HungarianRunnable(final double[][] matrix, final boolean[] rowCover, final boolean[] colCover,
        AtomicBoolean done, AtomicInteger row, AtomicInteger col, final int numRows,
        AtomicInteger workCounter, int start, int end) {
      this.rowCover = rowCover;
      this.colCover = colCover;
      this.done = done;
      this.row = row;
      this.col = col;
      this.numRows = numRows;
      this.matrix = matrix;
      this.workCounter = workCounter;
      this.start = start;
      this.end = end;
    }

    @Override
    public void run() {
      int rowPos = 0;
      do {
        for (int j = start; j < end; ++j) {
          if ((matrix[rowPos][j] < EPSILON) && !rowCover[rowPos] && !colCover[j]) {
            synchronized (matrix) {
              if (row.get() == -1) {
                row.set(rowPos);
                col.set(j);
              } else if (row.get() > rowPos) {
                row.set(rowPos);
                col.set(j);
              } else if (row.get() == rowPos && col.get() > j) {
                row.set(rowPos);
                col.set(j);
              }
            }
            done.set(true);
            break;
          }
        }
        rowPos += 1;
        if (rowPos >= numRows) {
          break;
        }
      }
      while (!done.get());
      workCounter.decrementAndGet();
    }

  }

  private static Pair findAZero(final double[][] matrix, final byte[][] mask,
      final boolean[] rowCover, final boolean[] colCover, ExecutorService service, int numThreads) {
    final int numRows = matrix.length;
    if (numRows > 500) {
      AtomicBoolean done = new AtomicBoolean(false);
      AtomicInteger row = new AtomicInteger(-1);
      AtomicInteger col = new AtomicInteger(-1);
      AtomicInteger workCounter = new AtomicInteger(0);
      int start = 0;
      int step = numRows / numThreads > 1 ? numRows / numThreads : 1;
      for (int i = 0; i < numThreads - 1; i++) {
        if (start + step >= numRows) {
          break;
        }
        HungarianRunnable runnable = new HungarianRunnable(matrix, rowCover, colCover, done, row,
            col, numRows, workCounter, start, start + step);
        workCounter.incrementAndGet();
        service.execute(runnable);
        start += step;

      }
      HungarianRunnable runnable = new HungarianRunnable(matrix, rowCover, colCover, done, row, col,
          numRows, workCounter, start, numRows);
      workCounter.incrementAndGet();
      service.execute(runnable);
      while (workCounter.get() > 0) {
        Thread.yield();
      }
      return new Pair(row.get(), col.get());
    } else {
      int rowPos = 0;
      int row = -1;
      int col = -1;
      boolean done = false;
      do {
        for (int j = 0; (j < numRows) && !done; ++j) {
          if ((matrix[rowPos][j] < EPSILON) && !rowCover[rowPos] && !colCover[j]) {
            row = rowPos;
            col = j;
            done = true;
          }
        }
        rowPos += 1;
        if (rowPos >= numRows) {
          done = true;
        }
      }
      while (!done);

      return new Pair(row, col);
    }
  }



  private static int findXInRow(final byte[][] mask, final int row, final int xtoFind) {
    final int n = mask.length;

    int col = -1;
    for (int j = 0; j < n; ++j) {
      if (mask[row][j] == xtoFind) {
        col = j;
      }
    }
    return col;
  }



  private static int findStarInRow(final byte[][] mask, final int row) {
    return findXInRow(mask, row, 1);
  }



  private static int findStarInCol(final byte[][] mask, final int col) {
    final int n = mask.length;

    int row = -1;
    for (int i = 0; i < n; ++i) {
      if (mask[i][col] == 1) {
        row = i;
      }
    }
    return row;
  }



  private static int findPrimeInRow(final byte[][] mask, final int row) {
    return findXInRow(mask, row, 2);
  }



  private static void augmentPath(final byte[][] mask, final int pathCount,
      final ArrayList<Pair> path) {
    for (int p = 0; p < pathCount; ++p) {
      final Pair pathElem = path.get(p);
      if (mask[pathElem.row][pathElem.col] == 1) {
        mask[pathElem.row][pathElem.col] = (byte) 0;
      } else {
        mask[pathElem.row][pathElem.col] = (byte) 1;
      }
    }
  }



  private static void clearCovers(final boolean[] rowCover, final boolean[] colCover) {
    for (int k = 0; k < rowCover.length; ++k) {
      rowCover[k] = false;
      colCover[k] = false;
    }
  }



  private static void erasePrimes(final byte[][] mask) {
    final int n = mask.length;

    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        if (mask[i][j] == 2) {
          mask[i][j] = (byte) 0;
        }
      }
    }
  }



  private static double findSmallest(final double[][] matrix, final boolean[] rowCover,
      final boolean[] colCover) {
    final int n = matrix.length;

    double minValue = Double.MAX_VALUE;
    for (int i = 0; i < n; ++i) {
      for (int j = 0; j < n; ++j) {
        if (!rowCover[i] && !colCover[j]) {
          minValue = Math.min(minValue, matrix[i][j]);
        }
      }
    }
    return minValue;
  }
}

