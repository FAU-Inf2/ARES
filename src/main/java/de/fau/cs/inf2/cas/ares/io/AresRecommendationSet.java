package de.fau.cs.inf2.cas.ares.io;

import java.util.List;

public class AresRecommendationSet {

  public String repository;
  public String commitId;
  public String fileName;
  public int methodNumber = -1;
  public List<AresRecommendation> recommendations;
  public String memberId;
  
  /**
   * Instantiates a new ares recommendation set.
   *
   * @param repository the repository
   * @param commitId the commit id
   * @param fileName the file name
   * @param methodNumber the method number
   * @param recommendations the recommendations
   */
  public AresRecommendationSet(String repository, String commitId, String fileName,
      int methodNumber, List<AresRecommendation> recommendations, String memberId) {
    super();
    this.repository = repository;
    this.commitId = commitId;
    this.fileName = fileName;
    this.methodNumber = methodNumber;
    this.recommendations = recommendations;
    this.memberId = memberId;
  }
  
}
