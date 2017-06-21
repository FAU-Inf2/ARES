package de.fau.cs.inf2.cas.ares.pipeline;

import de.fau.cs.inf2.cas.ares.io.AresSearchTime;

import de.fau.cs.inf2.cas.common.io.ReadableEncodedGroup;
import de.fau.cs.inf2.cas.common.vcs.base.VcsRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class LaseEvaluationGroupParameters {
  public ReadableEncodedGroup group;
  public HashMap<String, HashMap<String, Date>> dateMap;
  public HashMap<String, VcsRepository> repoMap;
  public File tmpDir;
  public boolean allPatterns;
  public AresSearchTime searchTime;
  public ArrayList<String> filteredFiles;
  
  /**
   * Instantiates a new lase evaluation group parameters.
   *
   * @param group the group
   * @param dateMap the date map
   * @param repoMap the repo map
   * @param tmpDir the tmp dir
   * @param allPatterns the all patterns
   * @param searchTime the search time
   */
  public LaseEvaluationGroupParameters(ReadableEncodedGroup group,
      HashMap<String, HashMap<String, Date>> dateMap, HashMap<String, VcsRepository> repoMap,
      File tmpDir, boolean allPatterns, AresSearchTime searchTime) {
    this.group = group;
    this.dateMap = dateMap;
    this.repoMap = repoMap;
    this.tmpDir = tmpDir;
    this.allPatterns = allPatterns;
    this.searchTime = searchTime;
  }
  
  public void setFilteredFiles(ArrayList<String> filteredFiles) {
    this.filteredFiles = filteredFiles;
  }
}