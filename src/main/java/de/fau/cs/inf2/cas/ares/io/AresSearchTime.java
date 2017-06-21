package de.fau.cs.inf2.cas.ares.io;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AresSearchTime {
  
  public String name;
  public AtomicInteger iteration = new AtomicInteger(-1);
  public AtomicInteger numberOfInputs = new AtomicInteger(-1);
  public AtomicLong completeSearch = new AtomicLong(-1);
  public AtomicLong createRecommendation = new AtomicLong(-1);
  public AtomicLong parsing = new AtomicLong(-1);
  public AtomicLong treeDifferencing = new AtomicLong(-1);
  public AtomicLong numberOfJavaFiles = new AtomicLong(-1);
  
  /**
   * Instantiates a new ares seach time.
   *
   * @param name the name
   */
  public AresSearchTime(String name) {
    this.name = name;
  }
  
  /**
   * Instantiates a new ares seach time.
   *
   * @param name the name
   * @param iteration the iteration
   * @param numberOfInputs the number of inputs
   * @param completeSearch the complete search
   * @param parsing the parsing
   * @param treeDifferencing the tree differencing
   * @param numberOfJavaFiles the number of java files
   */
  public AresSearchTime(String name, int iteration, int numberOfInputs, long completeSearch,
      long createRecommendation,
      long parsing, long treeDifferencing, long numberOfJavaFiles) {
    super();
    this.name = name;
    this.iteration = new AtomicInteger(iteration);
    this.numberOfInputs = new AtomicInteger(numberOfInputs);
    this.completeSearch = new AtomicLong(completeSearch);
    this.createRecommendation = new AtomicLong(createRecommendation);

    this.parsing = new AtomicLong(parsing);
    this.treeDifferencing = new AtomicLong(treeDifferencing);
    this.numberOfJavaFiles = new AtomicLong(numberOfJavaFiles);
  }
  
  /**
   * Instantiates a new ares seach time.
   *
   * @param name the name
   * @param iteration the iteration
   * @param numberOfInputs the number of inputs
   * @param completeSearch the complete search
   * @param parsing the parsing
   * @param treeDifferencing the tree differencing
   * @param numberOfJavaFiles the number of java files
   */
  public AresSearchTime(String name, AtomicInteger iteration,
      AtomicInteger numberOfInputs, AtomicLong completeSearch,
      AtomicLong createRecommendation,
      AtomicLong parsing, AtomicLong treeDifferencing, AtomicLong numberOfJavaFiles) {
    super();
    this.name = name;
    this.iteration = iteration;
    this.numberOfInputs = numberOfInputs;
    this.completeSearch = completeSearch;
    this.createRecommendation = createRecommendation;
    this.parsing = parsing;
    this.treeDifferencing = treeDifferencing;
    this.numberOfJavaFiles = numberOfJavaFiles;
  }
  
}
