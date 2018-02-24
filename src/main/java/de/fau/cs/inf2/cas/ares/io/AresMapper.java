/*
 * Copyright (c) 2017 Programming Systems Group, CS Department, FAU
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package de.fau.cs.inf2.cas.ares.io;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.fau.cs.inf2.cas.common.io.CommitPairIdentifier;
import de.fau.cs.inf2.cas.common.io.EditOperationCounts;
import de.fau.cs.inf2.cas.common.io.EncodedScript;
import de.fau.cs.inf2.cas.common.io.ReadableEncodedGroup;
import de.fau.cs.inf2.cas.common.io.ReadableEncodedGroupFile;
import de.fau.cs.inf2.cas.common.io.ReadableEncodedScript;
import de.fau.cs.inf2.cthree.data.Algorithm;
import de.fau.cs.inf2.cthree.data.Cluster;
import de.fau.cs.inf2.cthree.data.CodeChange;
import de.fau.cs.inf2.cthree.data.DataSet;

import java.util.List;


public class AresMapper {

  private static void addMixIns(ObjectMapper mapper, Class<?> targetClass, Class<?> mixInClass) {
    mapper.addMixIn(targetClass, mixInClass);
  }

  /**
   * Creates the json mapper.
   *
   * @return the object mapper
   */
  public static ObjectMapper createJsonMapper() {
    final ObjectMapper mapper;
    {
      mapper = new ObjectMapper();
      mapper.enable(SerializationFeature.INDENT_OUTPUT);
      mapper
          .setVisibilityChecker(mapper.getVisibilityChecker().with(JsonAutoDetect.Visibility.NONE));
      mapper.setSerializationInclusion(Include.NON_NULL);

      addMixIns(mapper, RecommendationResult.class, MixInRecommendationResult.class);
      addMixIns(mapper, RecommendationFile.class, MixInRecommendationFile.class);
      addMixIns(mapper, EncodedScript.class, MixInEncodedScript.class);
      addMixIns(mapper, ReadableEncodedScript.class, MixInReadableEncodedScript.class);
      addMixIns(mapper, CommitPairIdentifier.class, MixInCommitPairIdentifier.class);
      addMixIns(mapper, EditOperationCounts.class, MixInEditOperationCounts.class);
      addMixIns(mapper, AresRecommendationSet.class, MixInAresRecommendationSet.class);
      addMixIns(mapper, AresRecommendation.class, MixInAresRecommendation.class);
      addMixIns(mapper, ReadableEncodedGroupFile.class, MixInReadableEncodedGroupFile.class);
      addMixIns(mapper, ReadableEncodedGroup.class, MixInReadableEncodedGroup.class);
      addMixIns(mapper, AresCreatePatternTime.class, MixInAresCreatePatternTime.class);
      addMixIns(mapper, AresSearchTime.class, MixInAresSearchTime.class);
      addMixIns(mapper, AresTimeMeasurementFile.class, MixInAresTimeMeasurementFile.class);
      addMixIns(mapper, AresOnCthreeResultFile.class, MixInAresOnCthreeResultFile.class);
      addMixIns(mapper, AresOnCthreeResult.class, MixInAresOnCthreeResult.class);
      addMixIns(mapper, DataSet.class, MixInDataSet.class);
      addMixIns(mapper, Cluster.class, MixInCluster.class);
      addMixIns(mapper, CodeChange.class, MixInCodeChange.class);
    }

    return mapper;
  }



  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInRecommendationFile {

    @JsonProperty("results")
    List<RecommendationResult> results;

    MixInRecommendationFile(@JsonProperty("results") List<RecommendationResult> results) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInRecommendationResult {

    @JsonProperty("name")
    String name;

    @JsonProperty("allMembers")
    List<ReadableEncodedScript> allMembers;

    @JsonProperty("inputs")
    List<ReadableEncodedScript> inputs;

    @JsonProperty("recommendationSets")
    List<AresRecommendationSet> recommendationSets;

    @JsonProperty("foundMembers")
    int foundMembers;

    @JsonProperty("numberOfRecommendationSets")
    int numberOfRecommendationSets;

    @JsonProperty("precision")
    double precision;

    @JsonProperty("recall")
    double recall;

    @JsonProperty("accuracyTokens")
    double accuracyTokens;

    @JsonProperty("accuracyCharacters")
    double accuracyCharacters;

    @JsonProperty("accuracyTokensMin")
    double accuracyTokensMin;

    @JsonProperty("accuracyCharactersMin")
    double accuracyCharactersMin;

    @JsonProperty("accuracyTokensMax")
    double accuracyTokensMax;

    @JsonProperty("accuracyCharactersMax")
    double accuracyCharactersMax;

    @JsonProperty("patternOriginal")
    String patternOriginal;

    @JsonProperty("patternModified")
    String patternModified;

    @JsonProperty("patternCreationTimeInNanoSec")
    long patternCreationTimeInNanoSec;

    @JsonProperty("patternUseTimeInNanoSec")
    long patternUseTimeInNanoSec;

    MixInRecommendationResult(@JsonProperty("name") String name,
        @JsonProperty("allMembers") List<ReadableEncodedScript> allMembers,
        @JsonProperty("inputs") List<ReadableEncodedScript> inputs,
        @JsonProperty("recommendationSets") List<AresRecommendationSet> recommendationSets,
        @JsonProperty("foundMembers") int foundMembers,
        @JsonProperty("numberOfRecommendationSets") int numberOfRecommendationSets,
        @JsonProperty("precision") double precision, @JsonProperty("recall") double recall,
        @JsonProperty("accuracyTokensMin") double accuracyTokensMin,
        @JsonProperty("accuracyCharactersMin") double accuracyCharactersMin,
        @JsonProperty("accuracyTokens") double accuracyTokensMax,
        @JsonProperty("accuracyCharacters") double accuracyCharactersMax,
        @JsonProperty("patternOriginal") String patternOriginal,
        @JsonProperty("patternModified") String patternModified,
        @JsonProperty("patternCreationTimeInNanoSec") long patternCreationTimeInNanoSec,
        @JsonProperty("patternUseTimeInNanoSec") long patternUseTimeInNanoSec) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInEncodedScript {
    @JsonProperty("pair")
    private CommitPairIdentifier pair;

    @JsonProperty("pattern")
    private short[] pattern;

    @JsonProperty("operations")
    public EditOperationCounts operations;

    @JsonProperty("patternLength")
    public int patternLength;

    @JsonProperty("treeSize")
    public int treeSize;

    MixInEncodedScript(@JsonProperty("pair") CommitPairIdentifier pair,
        @JsonProperty("pattern") short[] pattern,

        @JsonProperty("operations") EditOperationCounts operations,

        @JsonProperty("patternLength") int patternLength,

        @JsonProperty("treeSize") int treeSize) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInCommitPairIdentifier {
    @JsonProperty("id")
    public String id;

    @JsonProperty("idx")
    public int idx;

    @JsonProperty("repository")
    public String repository;

    @JsonProperty("commitId1")
    public String commitId1;

    @JsonProperty("commitId2")
    public String commitId2;

    @JsonProperty("fileName")
    public String fileName;



    @JsonProperty("methodNumber1")
    public int methodNumber1;

    @JsonProperty("methodNumber2")
    public int methodNumber2;

    MixInCommitPairIdentifier(@JsonProperty("id") String id, @JsonProperty("idx") int idx,
        @JsonProperty("repository") String repository, @JsonProperty("commitId1") String commitId1,
        @JsonProperty("commitId2") String commitId2, @JsonProperty("fileName") String fileName,
        @JsonProperty("methodNumber1") int methodNumber1,
        @JsonProperty("methodNumber2") int methodNumber2) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInEditOperationCounts {

    @JsonProperty("all")
    public int all;

    @JsonProperty("moves")
    public int moves;

    @JsonProperty("inserts")
    public int inserts;

    @JsonProperty("deletes")
    public int deletes;

    @JsonProperty("updates")
    public int updates;

    @JsonProperty("aligns")
    public int aligns;

    MixInEditOperationCounts(@JsonProperty("all") int all, @JsonProperty("moves") int moves,
        @JsonProperty("inserts") int inserts, @JsonProperty("deletes") int deletes,
        @JsonProperty("updates") int updates, @JsonProperty("aligns") int aligns) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInReadableEncodedScript {

    @JsonProperty("methodOriginal")
    public String methodOriginal;

    @JsonProperty("methodModified")
    public String methodModified;

    @JsonProperty("diff")
    public String diff;

    @JsonProperty("commitMsg")
    public String commitMsg;

    @JsonProperty("script")
    public EncodedScript script;

    MixInReadableEncodedScript(@JsonProperty("methodOriginal") String methodOriginal,
        @JsonProperty("methodModified") String methodModified, @JsonProperty("diff") String diff,
        @JsonProperty("commitMsg") String commitMsg,
        @JsonProperty("script") EncodedScript script) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInAresRecommendationSet {
    @JsonProperty("repository")
    String repository;

    @JsonProperty("commitId")
    String commitId;

    @JsonProperty("fileName")
    String fileName;

    @JsonProperty("methodNumber")
    int methodNumber;

    @JsonProperty("recommendations")
    List<AresRecommendation> recommendations;

    @JsonProperty("memberId")
    String memberId;

    MixInAresRecommendationSet(@JsonProperty("repository") String repository,
        @JsonProperty("commitId") String commitId, @JsonProperty("fileName") String fileName,
        @JsonProperty("methodNumber") int methodNumber,
        @JsonProperty("recommendations") List<AresRecommendation> recommendations,
        @JsonProperty("memberId") String memberId) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInAresRecommendation {

    @JsonProperty("methodBlock")
    String methodBlock;

    @JsonProperty("accuracyTokens")
    double accuracyTokens;

    @JsonProperty("accuracyCharacters")
    double accuracyCharacters;

    MixInAresRecommendation(@JsonProperty("methodBlock") String methodBlock,
        @JsonProperty("accuracyTokens") double accuracyTokens,
        @JsonProperty("accuracyCharacters") double accuracyCharacters) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInReadableEncodedGroupFile {

    @JsonProperty("groups")
    List<ReadableEncodedGroup> groups;

    MixInReadableEncodedGroupFile(@JsonProperty("groups") List<ReadableEncodedGroup> groups) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInReadableEncodedGroup {

    @JsonProperty("name")
    public String name;

    @JsonProperty("scripts")
    public List<ReadableEncodedScript> scripts;

    MixInReadableEncodedGroup(@JsonProperty("name") String name,
        @JsonProperty("scripts") List<ReadableEncodedScript> scripts) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInAresTimeMeasurementFile {

    @JsonProperty("searchTwoFilesForLaseTime")
    List<AresSearchTime> searchTwoFilesForLaseTime;

    @JsonProperty("searchTwoFilesTime")
    List<AresSearchTime> searchTwoFilesTime;

    @JsonProperty("searchAllTime")
    List<AresSearchTime> searchAllTime;

    @JsonProperty("createTwoFilesTime")
    List<AresCreatePatternTime> createTwoFilesTime;

    @JsonProperty("createAllTime")
    List<AresCreatePatternTime> createAllTime;


    MixInAresTimeMeasurementFile(
        @JsonProperty("searchTwoFilesForLaseTime") List<AresSearchTime> searchTwoFilesForLaseTime,
        @JsonProperty("searchTwoFilesTime") List<AresSearchTime> searchTwoFilesTime,
        @JsonProperty("searchAllTime") List<AresSearchTime> searchAllTime,
        @JsonProperty("createTwoFilesTime") List<AresCreatePatternTime> createTwoFilesTime,
        @JsonProperty("createAllTime") List<AresCreatePatternTime> createAllTime) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInAresCreatePatternTime {

    @JsonProperty("name")
    String name;

    @JsonProperty("iteration")
    int iteration;

    @JsonProperty("numberOfInputs")
    int numberOfInputs;

    @JsonProperty("completeCreation")
    long completeCreation;

    @JsonProperty("treeDifferencing")
    long treeDifferencing;

    @JsonProperty("determineOrder")
    long determineOrder;

    MixInAresCreatePatternTime(@JsonProperty("name") String name,
        @JsonProperty("iteration") int iteration,
        @JsonProperty("numberOfInputs") int numberOfInputs,
        @JsonProperty("completeCreation") long completeCreation,
        @JsonProperty("treeDifferencing") long treeDifferencing,
        @JsonProperty("determineOrder") long determineOrder) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInAresSearchTime {

    @JsonProperty("name")
    String name;

    @JsonProperty("iteration")
    int iteration;

    @JsonProperty("numberOfInputs")
    int numberOfInputs;

    @JsonProperty("completeSearch")
    long completeSearch;

    @JsonProperty("createRecommendation")
    long createRecommendation;

    @JsonProperty("parsing")
    long parsing;

    @JsonProperty("treeDifferencing")
    long treeDifferencing;

    @JsonProperty("numberOfJavaFiles")
    long numberOfJavaFiles;


    MixInAresSearchTime(@JsonProperty("name") String name, @JsonProperty("iteration") int iteration,
        @JsonProperty("numberOfInputs") int numberOfInputs,
        @JsonProperty("completeSearch") long completeSearch,
        @JsonProperty("createRecommendation") long createRecommendation,
        @JsonProperty("parsing") long parsing,
        @JsonProperty("treeDifferencing") long treeDifferencing,
        @JsonProperty("numberOfJavaFiles") long numberOfJavaFiles) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInDataSet {
    @JsonProperty("clusters")
    private List<Cluster> clusters;

    @SuppressWarnings("unused")
    public MixInDataSet(@JsonProperty("clusters") final List<Cluster> clusters) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInCluster {
    @JsonProperty("id")
    private String id;

    @JsonProperty("detectedBy")
    private List<Algorithm> detectedBy;

    @JsonProperty("members")
    private CodeChange[] members;

    @SuppressWarnings("unused")
    public MixInCluster(@JsonProperty("id") final String id,
        @JsonProperty("detectedBy") final List<Algorithm> detectedBy,
        @JsonProperty("members") final CodeChange[] members) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInCodeChange {
    @JsonProperty("id")
    private String id;

    @JsonProperty("repository")
    private String repository;
    @JsonProperty("fileName")
    private String fileName;

    @JsonProperty("commitBeforeChange")
    private String commitBeforeChange;
    @JsonProperty("commitAfterChange")
    private String commitAfterChange;

    @JsonProperty("methodNumberBeforeChange")
    private int methodNumberBeforeChange;
    @JsonProperty("methodNumberAfterChange")
    private int methodNumberAfterChange;

    @JsonProperty("signatureBeforeChange")
    private String signatureBeforeChange;
    @JsonProperty("signatureAfterChange")
    private String signatureAfterChange;

    @JsonProperty("diff")
    private String[] diff;

    @SuppressWarnings("unused")
    public MixInCodeChange(@JsonProperty("id") final String id,
        @JsonProperty("repository") final String repository,
        @JsonProperty("fileName") final String fileName,
        @JsonProperty("commitBeforeChange") final String commitBeforeChange,
        @JsonProperty("commitAfterChange") final String commitAfterChange,
        @JsonProperty("methodNumberBeforeChange") final int methodNumberBeforeChange,
        @JsonProperty("methodNumberAfterChange") final int methodNumberAfterChange,
        @JsonProperty("signatureBeforeChange") final String signatureBeforeChange,
        @JsonProperty("signatureAfterChange") final String signatureAfterChange,
        @JsonProperty("diff") final String[] diff) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInAresOnCthreeResultFile {

    @JsonProperty("results")
    List<AresOnCthreeResult> results;

    MixInAresOnCthreeResultFile(@JsonProperty("results") List<AresOnCthreeResult> results) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInAresOnCthreeResult {

    @JsonProperty("cluster")
    public Cluster cluster;

    @JsonProperty("recommendationResult")
    public RecommendationResult recommendationResult;

    @JsonProperty("cloneType")
    public ClusterCloneType cloneType;

    @JsonProperty("unchangedAccuracyTokens")
    public List<Double> unchangedAccuracyTokens;

    @JsonProperty("unchangedAccuracyCharacters")
    public List<Double> unchangedAccuracyCharacters;


    @SuppressWarnings("unused")
    MixInAresOnCthreeResult(@JsonProperty("cluster") Cluster cluster,

        @JsonProperty("recommendationResult") RecommendationResult recommendationResult,

        @JsonProperty("cloneType") ClusterCloneType cloneType,

        @JsonProperty("unchangedAccuracyTokens") List<Double> unchangedAccuracyTokens,

        @JsonProperty("unchangedAccuracyCharacters") List<Double> unchangedAccuracyCharacters) {}
  }

}
