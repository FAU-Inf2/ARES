package de.fau.cs.inf2.cas.common.io.extern;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.fau.cs.inf2.cas.common.io.CommitPairIdentifier;

import java.util.List;

public class LaseMapper {

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

    @SuppressWarnings("unused")
    MixInCommitPairIdentifier(@JsonProperty("id") String id, @JsonProperty("idx") int idx,
        @JsonProperty("repository") String repository, @JsonProperty("commitId1") String commitId1,
        @JsonProperty("commitId2") String commitId2, @JsonProperty("fileName") String fileName,
        @JsonProperty("methodNumber1") int methodNumber1,
        @JsonProperty("methodNumber2") int methodNumber2) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInLaseTime {
    @JsonProperty("createPattern")
    public long createPattern;

    @JsonProperty("searchPattern")
    public long searchPattern;

    @SuppressWarnings("unused")
    MixInLaseTime(@JsonProperty("createPattern") long createPattern,
        @JsonProperty("searchPattern") long searchPattern) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInLaseOutput {
    @JsonProperty("classname")
    public String classname;

    @JsonProperty("relativePath")
    public String relativePath;

    @JsonProperty("methodSignature")
    public String methodSignature;

    @JsonProperty("startPosition")
    public int startPosition;

    @JsonProperty("length")
    public int length;

    @JsonProperty("newMethodString")
    public String newMethodString;

    @SuppressWarnings("unused")
    MixInLaseOutput(@JsonProperty("classname") String classname,
        @JsonProperty("relativePath") String relativePath,
        @JsonProperty("methodSignature") String methodSignature,
        @JsonProperty("startPosition") int startPosition, @JsonProperty("length") int length,
        @JsonProperty("newMethodString") String newMethodString) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInLaseEvalOutput {
    @JsonProperty("laseOutput")
    public LaseOutput laseOutput;

    @JsonProperty("type")
    public LaseEvalOutputTypeEnum type;

    @JsonProperty("foundChange")
    public CommitPairIdentifier foundChange;

    @JsonProperty("tokenSim")
    public double tokenSim;

    @JsonProperty("stringSim")
    public double stringSim;



    @SuppressWarnings("unused")
    MixInLaseEvalOutput(@JsonProperty("laseOutput") LaseOutput laseOutput,
        @JsonProperty("type") LaseEvalOutputTypeEnum type,
        @JsonProperty("foundChange") CommitPairIdentifier foundChange,
        @JsonProperty("tokenSim") double tokenSim, @JsonProperty("stringSim") double stringSim) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInLaseOutputFile {
    @JsonProperty("recommendations")
    public List<LaseOutput> recommendations;

    @SuppressWarnings("unused")
    MixInLaseOutputFile(@JsonProperty("recommendations") List<LaseOutput> recommendations) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInLaseOutputEvalFile {

    @JsonProperty("cpis")
    public List<CommitPairIdentifier> cpis;
    @JsonProperty("outputEval")
    public List<LaseEvalOutput> outputEval;
    @JsonProperty("inputsFound")
    public int inputsFound;
    @JsonProperty("identicalChangesFound")
    public int identicalChangesFound;


    @SuppressWarnings("unused")
    MixInLaseOutputEvalFile(@JsonProperty("cpis") List<CommitPairIdentifier> cpis,
        @JsonProperty("outputEval") List<LaseEvalOutput> outputEval,
        @JsonProperty("inputsFound") int inputsFound,
        @JsonProperty("identicalChangesFound") int identicalChangesFound) {}
  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInLaseEvalOutputTypeEnum {
    @JsonProperty("type")
    public String type;

    @SuppressWarnings("unused")
    MixInLaseEvalOutputTypeEnum(@JsonProperty("type") String type) {}

  }

  @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY,
      property = "@class")
  private abstract static class MixInLaseFolders {
    @JsonProperty("folders")
    public String[][] folders;

    @JsonProperty("names")
    public String[] names;

    @SuppressWarnings("unused")
    MixInLaseFolders(@JsonProperty("folders") String[][] folders,
        @JsonProperty("names") String[] names) {}

  }

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
    mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    mapper.setVisibilityChecker(mapper.getVisibilityChecker().with(JsonAutoDetect.Visibility.NONE));
    mapper.setSerializationInclusion(Include.NON_NULL);

    addMixIns(mapper, LaseOutput.class, MixInLaseOutput.class);
    addMixIns(mapper, LaseOutputFile.class, MixInLaseOutputFile.class);
    addMixIns(mapper, LaseEvalOutput.class, MixInLaseEvalOutput.class);
    addMixIns(mapper, LaseOutputEvalFile.class, MixInLaseOutputEvalFile.class);
    addMixIns(mapper, LaseEvalOutputTypeEnum.class, MixInLaseEvalOutputTypeEnum.class);
    addMixIns(mapper, CommitPairIdentifier.class, MixInCommitPairIdentifier.class);
    addMixIns(mapper, LaseTime.class, MixInLaseTime.class);
    addMixIns(mapper, LaseFolders.class, MixInLaseFolders.class);

    return mapper;
  }
}
