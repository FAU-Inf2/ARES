package de.fau.cs.inf2.cas.ares.io;

import java.util.List;

public class AresTimeMeasurementFile {

  public List<AresSearchTime> searchTwoFilesForLaseTime;
  public List<AresSearchTime> searchTwoFilesTime;
  public List<AresSearchTime> searchAllTime;
  public List<AresCreatePatternTime> createTwoFilesTime;
  public List<AresCreatePatternTime> createAllTime;

  /**
   * Instantiates a new ares time measurement file.
   *
   * @param searchTwoFilesTime the search two files time
   * @param searchAllTime the search all time
   * @param createTwoFilesTime the create two files time
   * @param createAllTime the create all time
   */
  public AresTimeMeasurementFile(List<AresSearchTime> searchTwoFilesForLaseTime,
      List<AresSearchTime> searchTwoFilesTime,
      List<AresSearchTime> searchAllTime, List<AresCreatePatternTime> createTwoFilesTime,
      List<AresCreatePatternTime> createAllTime) {
    super();
    this.searchTwoFilesForLaseTime = searchTwoFilesForLaseTime;
    this.searchTwoFilesTime = searchTwoFilesTime;
    this.searchAllTime = searchAllTime;
    this.createTwoFilesTime = createTwoFilesTime;
    this.createAllTime = createAllTime;
  }
  
}
