package de.fau.cs.inf2.cas.common.io.extern;

public class LaseTime {

  public long createPattern;
  public long searchPattern;
  
  /**
   * Instantiates a new lase time.
   *
   * @param createPattern the create pattern
   * @param searchPattern the search pattern
   */
  public LaseTime(long createPattern, long searchPattern) {
    super();
    this.createPattern = createPattern;
    this.searchPattern = searchPattern;
  }
  
}
