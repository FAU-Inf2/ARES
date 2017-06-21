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

