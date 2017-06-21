package de.fau.cs.inf2.cas.ares.io;

public class AresCreatePatternTime {
  
  public String name;
  public int iteration;
  public int numberOfInputs;
  public long completeCreation;
  public long treeDifferencing;
  public long determineOrder;
  
  public AresCreatePatternTime(String name) {
    this.name = name;
  }
  
  /**
   * Instantiates a new ares create pattern time.
   *
   * @param name the name
   * @param iteration the iteration
   * @param numberOfInputs the number of inputs
   * @param treeDifferencing the tree differencing
   */
  public AresCreatePatternTime(String name, int iteration, int numberOfInputs,
      long completeCreation,
      long treeDifferencing, long determineOrder) {
    super();
    this.name = name;
    this.iteration = iteration;
    this.numberOfInputs = numberOfInputs;
    this.completeCreation = completeCreation;
    this.treeDifferencing = treeDifferencing;
    this.determineOrder = determineOrder;
  }
  
}
