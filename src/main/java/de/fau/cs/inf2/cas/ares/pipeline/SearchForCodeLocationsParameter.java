package de.fau.cs.inf2.cas.ares.pipeline;

import de.fau.cs.inf2.cas.ares.io.AresMeasurement;
import de.fau.cs.inf2.cas.ares.io.AresRecommendationSet;
import de.fau.cs.inf2.cas.ares.io.AresSearchTime;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.cas.common.io.ChangeGroup;
import de.fau.cs.inf2.cas.common.io.ReadableEncodedScript;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;

@SuppressWarnings("PMD.TooManyFields")
public class SearchForCodeLocationsParameter {
  public AresSearchTime searchTime;
  public String oldestId;
  public String repositoryName;
  public File revDir;
  public ExecutorService executor;
  public AresMeasurement measurement;
  public List<ReadableEncodedScript> inputs;
  public List<ReadableEncodedScript> members;
  public HashMap<ReadableEncodedScript, String> methodSignatureMap;
  public HashMap<ReadableEncodedScript, AbstractBastNode> methodBlockMap;
  public String[] filesArrays;
  public List<AresRecommendationSet> recommendationSets;
  public HashSet<ReadableEncodedScript> foundInputs;
  public long createTime;
  public ChangeGroup group;
  public File workDir;


  /**
   * Instantiates a new search for code locations parameter.
   *
   * @param oldestId the oldest id
   * @param repositoryName the repository name
   * @param revDir the rev dir
   * @param executor the executor
   * @param measurement the measurement
   * @param inputs the inputs
   * @param methodSignatureMap the method signature map
   * @param methodBlockMap the method block map
   * @param filesArrays the files arrays
   * @param createTime the create time
   * @param group the group
   * @param workDir the work dir
   */
  @SuppressWarnings("PMD.ExcessiveParameterList")
  public SearchForCodeLocationsParameter(final String oldestId, final String repositoryName,
      final File revDir, final ExecutorService executor, final AresMeasurement measurement,
      final List<ReadableEncodedScript> inputs,
      final List<ReadableEncodedScript> members,
      final HashMap<ReadableEncodedScript, String> methodSignatureMap,
      final HashMap<ReadableEncodedScript, AbstractBastNode> methodBlockMap,
      final String[] filesArrays, final long createTime,
      final ChangeGroup group, final File workDir) {
    this.searchTime = new AresSearchTime(group.getHash());
    searchTime.numberOfInputs.set(group.getMembers().size());
    searchTime.iteration.set(0);
    this.oldestId = oldestId;
    this.repositoryName = repositoryName;
    this.revDir = revDir;
    this.executor = executor;
    this.measurement = measurement;
    this.inputs = inputs;
    this.members = members;
    this.methodSignatureMap = methodSignatureMap;
    this.methodBlockMap = methodBlockMap;
    this.filesArrays = filesArrays;
    this.recommendationSets = new LinkedList<>();
    this.foundInputs = new HashSet<>();
    this.createTime = createTime;
    this.group = group;
    this.workDir = workDir;
  }
}
