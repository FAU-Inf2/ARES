/*
 * Copyright (c) 2017 Programming Systems Group, CS Department, FAU
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */

package de.fau.cs.inf2.cas.ares;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.fau.cs.inf2.cas.ares.bast.general.ParserFactory;
import de.fau.cs.inf2.cas.ares.io.AresCreatePatternTime;
import de.fau.cs.inf2.cas.ares.io.AresMapper;
import de.fau.cs.inf2.cas.ares.io.AresMeasurement;
import de.fau.cs.inf2.cas.ares.io.AresRecommendationSet;
import de.fau.cs.inf2.cas.ares.io.AresSearchTime;
import de.fau.cs.inf2.cas.ares.io.RecommendationFile;
import de.fau.cs.inf2.cas.ares.io.RecommendationResult;
import de.fau.cs.inf2.cas.ares.pcreation.GeneralizeGroup;
import de.fau.cs.inf2.cas.ares.pcreation.GeneralizeGroup.ReferenceConfiguration;
import de.fau.cs.inf2.cas.ares.pipeline.LaseEvaluationGroupParameters;
import de.fau.cs.inf2.cas.ares.pipeline.SearchForCodeLocationsParameter;
import de.fau.cs.inf2.cas.ares.pipeline.SharedMethods;
import de.fau.cs.inf2.cas.ares.recommendation.AnalysePath;
import de.fau.cs.inf2.cas.ares.recommendation.ExtendedAresPattern;
import de.fau.cs.inf2.cas.ares.recommendation.Recommendation;
import de.fau.cs.inf2.cas.ares.recommendation.RecommendationCreator;
import de.fau.cs.inf2.cas.ares.recommendation.extension.ExtendedTemplateExtractor;
import de.fau.cs.inf2.cas.ares.recommendation.visitors.FindInitialPatternStartsVisitor;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.visitors.FindNodesFromTagVisitor;
import de.fau.cs.inf2.cas.common.bast.visitors.GetMethodsVisitor;

import de.fau.cs.inf2.cas.common.io.ChangeGroup;
import de.fau.cs.inf2.cas.common.io.EncodedScript;
import de.fau.cs.inf2.cas.common.io.ReadableEncodedGroup;
import de.fau.cs.inf2.cas.common.io.ReadableEncodedGroupFile;
import de.fau.cs.inf2.cas.common.io.ReadableEncodedScript;

import de.fau.cs.inf2.cas.common.parser.AresExtension;
import de.fau.cs.inf2.cas.common.parser.ParserType;
import de.fau.cs.inf2.cas.common.parser.SyntaxError;

import de.fau.cs.inf2.cas.common.util.FileUtils;
import de.fau.cs.inf2.cas.common.util.MiniLexer;
import de.fau.cs.inf2.cas.common.util.PackageParser;

import de.fau.cs.inf2.cas.common.vcs.ExtractScript;
import de.fau.cs.inf2.cas.common.vcs.ExtractUtilities;
import de.fau.cs.inf2.cas.common.vcs.base.NoSuchRepositoryException;
import de.fau.cs.inf2.cas.common.vcs.base.VcsRepository;
import de.fau.cs.inf2.cas.common.vcs.base.VcsRevision;
import de.fau.cs.inf2.cas.common.vcs.cache.DateCache;
import de.fau.cs.inf2.cas.common.vcs.cache.JsonVcsData;
import de.fau.cs.inf2.cas.common.vcs.git.GitLink;
import de.fau.cs.inf2.cas.common.vcs.git.GitRepository;
import de.fau.cs.inf2.cas.common.vcs.git.GitRevision;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@SuppressWarnings("ucd")
public class ExecuteLaseEvaluation {

  public static final String INPUT_DEFINITION = "data/lase_evaluation/input.json";
  public static final String LASE_DATASET_LASE_RESULTS =
      "data/lase_evaluation/lase_result.json";
  public static final String LASE_DATASET_ARES_TWO_INPUTS_RESULTS =
      "data/lase_evaluation/ares_two_inputs_result.json";
  public static final String LASE_DATASET_ARES_ALL_INPUTS_RESULTS =
      "data/lase_evaluation/ares_all_inputs_result.json";
  public static final int NUM_THREADS = 8;

  private static String[][] dataX() {
    return new String[][] {
        { "77644", "2219be33a5e53110d26153b9b761bd7a", "88261d164b5728e03fcc1bfe9b1ead72" },
        { "82429", "952c1a1303a96c20a49e162a53101d3c", "975d644a692378a3469a9d5db5c4341d" },
        { "114007", "a9e871b4750f719d769d864a81e24d39", "3d4c2e11159fb027786dd40d52e0e259" },
        { "139329_1", "d8393f7d77e8e39197011a14efa8e325", "435c6c2e8b18e8e12c86d4241d393f67" },
        { "142947_1", "01b5052a73c82f892dafc4981a6998fd", "a87bd6b44e911a0cea2c18434386e5a9" },
        { "91937", "a26d756e4fdba750a38ac1d9fb95d44f", "4d17d67894099caffe78f6bebdcf9240" },
        { "103863", "aa605ad23dee4307b62423c9392e1cea", "c24cd28b084a250eab29d0fbd70039ac" },
        { "129314", "37939f3f0c955e95b5854843ee114098", "98c36e3ada6a00150d74be2226fa7a67" },
        { "134091", "cef0db73a399b1914ad62667dddc2be0", "da3b2c35cd9351da521cad4d1d3f4d36" },
        { "139329_2", "2af9e2e868e257c15289f6176b715b56", "4b6aeb1b0fe44e4b3dacc78c097c968f" },
        { "139329_3", "f14ec2e4b59f07159d19f5016a9d474c", "2894064c7d97283212e104931b4af559" },
        { "142947_2", "6ada29d77953ab4812f42972f3cb8af5", "d39432f3016c04cbdf296feb1b2795fd" },
        { "76182", "9d2949a58f429f843db67380f2f36636", "f3f57decb8b2ec2821183980c420a424" },
        { "77194", "86877308add979cb916d7aeebaa60dfb", "7487c6222f62527d4afd13672d2a173b" },
        { "86079_1", "787a0317e1a4a252a839c28889a05ec8", "f191d30bdf175ec5a28508c6cbd91d9b" },
        { "95409", "f3f615434e60b7154ca3cf599ef2a4b0", "0011a80c539260a5b97625d60fa91fb0" },
        { "97981", "61213e1d33be6df09651589941b1e8eb", "f17871ff615dfb52a62f74dc95989707" },
        { "74139", "af971705d64cf24f9828a655c4e068dd", "b8457a67275bd61d2117848aa29d441c" },
        { "76391", "113c78ecbf4f7de1e9be9cb14918575a", "d633a5811af620ef9eb35aa8dceaeb1c" },
        { "89785", "dc726bdd228b406be08aea3385b4d297", "abd11a5e3885523702b6c6e494f5e58b" },
        { "79107", "60cd2c2d473c4149b06a0c920ca9e1a6", "c477c8769f1bc45dacb08ac61745a889" },
        { "86079_2", "994f370afa6a3db9325028573466b41a", "4f0d95964b4788e870d780c728cd8763" },
        { "95116", "2df091ca67c7ea781d124da39cc5e370", "5e3b2715b021a2217a88cfec0df91d75" },
        { "98198", "59bfb550d3377219b9363ff6be8faf41", "250098d8c1699e8fea54ff002bbc8b38" },


    };
  }

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    File laseDataFile = new File(LASE_DATASET_LASE_RESULTS);
    final ObjectMapper aresMapper = AresMapper.createJsonMapper();
    RecommendationFile laseData = null;

    try {
      laseData = aresMapper.readValue(laseDataFile, RecommendationFile.class);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }

    File tmpDir = new File("/tmp/ARES");
    if (args.length > 0 && args[0] != null) {
      tmpDir = new File(args[0]);
    }
    if (tmpDir.exists() && !tmpDir.isDirectory()) {
      System.out.format("Temporary path (%s) is no directory.", args[0]);
      System.exit(-1);
    } else if (!tmpDir.exists()) {
      boolean createSuccessful = tmpDir.mkdirs();
      if (!createSuccessful) {
        System.out.format("Could not create temporary directory (%s).", args[0]);
        System.exit(-1);
      }
    }

    File dateCache = null;
    dateCache = new File(tmpDir, "date.json");
    ReadableEncodedGroupFile inputDefinition = null;
    ObjectMapper mapper = AresMapper.createJsonMapper();
    HashMap<String, HashMap<String, Date>> dateMap = new HashMap<>();

    try {
      inputDefinition =
          mapper.readValue(new File(INPUT_DEFINITION), ReadableEncodedGroupFile.class);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }

    HashMap<String, VcsRepository> repoMap = new HashMap<>();
    createRepositories(tmpDir, inputDefinition, repoMap);
    try {
      dateMap = createCommitDateCache(dateCache, dateMap, inputDefinition, repoMap);
      executeAresWithTwoInputChanges(tmpDir, inputDefinition, dateMap, repoMap);

      RecommendationFile newResults = combineResultData(aresMapper, tmpDir, inputDefinition, false);
      executeAresWithAllInputChanges(tmpDir, inputDefinition, dateMap, repoMap);
      RecommendationFile allData = combineResultData(aresMapper, tmpDir, inputDefinition, true);

      SharedMethods.printTable(laseData, newResults, allData);
      System.out.println("\n############################################\n");
      System.out.println("Time LASE: ");
      SharedMethods.printTimeData(laseData.results);
      System.out.println();
      System.out.println("Time ARES - Two Input Changes: ");
      SharedMethods.printTimeData(newResults.results);
      System.out.println("Time ARES - All Input Changes: ");
      SharedMethods.printTimeData(allData.results);
      System.out.println("\n############################################\n");
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }

  private static void executeAresWithAllInputChanges(File tmpDir,
      ReadableEncodedGroupFile inputDefinition, HashMap<String, HashMap<String, Date>> dateMap,
      HashMap<String, VcsRepository> repoMap) {
    for (ReadableEncodedGroup group : inputDefinition.groups) {
      AresResCallable callable = new AresResCallable(new LaseEvaluationGroupParameters(group,
          dateMap, repoMap, tmpDir, true, new AresSearchTime(group.name)));
      try {
        callable.call();
      } catch (Exception e) {
        // do nothing
      }
    }
  }

  private static void executeAresWithTwoInputChanges(File tmpDir,
      ReadableEncodedGroupFile inputDefinition, HashMap<String, HashMap<String, Date>> dateMap,
      HashMap<String, VcsRepository> repoMap) {
    for (ReadableEncodedGroup group : inputDefinition.groups) {
      AresResCallable callable = new AresResCallable(new LaseEvaluationGroupParameters(group,
          dateMap, repoMap, tmpDir, false, new AresSearchTime(group.name)));
      try {
        callable.call();
      } catch (Exception e) {
        // do nothing
      }
    }
  }

  private static RecommendationFile combineResultData(final ObjectMapper aresMapper, File tmpDir,
      ReadableEncodedGroupFile inputDefinition, boolean allInputs)
      throws IOException, JsonParseException, JsonMappingException, JsonGenerationException {
    List<de.fau.cs.inf2.cas.ares.io.RecommendationResult> resultList = new LinkedList<>();
    for (ReadableEncodedGroup group : inputDefinition.groups) {
      String name = group.name;
      if (name.equals("74139")) {
        continue;
      }
      File resultFile = null;
      if (allInputs) {
        resultFile = new File(tmpDir, "/allInputs/" + name + "/ares_result.json");
      } else {
        resultFile = new File(tmpDir, "/twoInputs/" + name + "/ares_result.json");
      }
      de.fau.cs.inf2.cas.ares.io.RecommendationResult result =
          aresMapper.readValue(resultFile, de.fau.cs.inf2.cas.ares.io.RecommendationResult.class);
      if (result != null) {
        resultList.add(result);
      }
    }
    File outputFile = null;
    if (allInputs) {
      outputFile = new File(tmpDir, "/ares_all_inputs_result.json");
    } else {
      outputFile = new File(tmpDir, "/ares_two_inputs_result.json");
    }
    RecommendationFile file = new RecommendationFile(resultList);
    aresMapper.writeValue(outputFile, file);
    file = null;
    file = aresMapper.readValue(outputFile, RecommendationFile.class);
    assert (file != null);
    return file;
  }

  private static HashMap<String, HashMap<String, Date>> createCommitDateCache(File dateCache,
      HashMap<String, HashMap<String, Date>> dateMap, ReadableEncodedGroupFile groupFileUpdated,
      HashMap<String, VcsRepository> repoMap)
      throws IOException, JsonParseException, JsonMappingException, JsonGenerationException {
    DateCache cache = null;
    ObjectMapper dateMapper = JsonVcsData.createJsonMapper();
    if (dateCache != null && dateCache.exists()) {
      cache = dateMapper.readValue(dateCache, DateCache.class);
      if (cache != null) {
        dateMap = cache.getRepositoryMap();
      }
    }

    for (ReadableEncodedGroup group : groupFileUpdated.groups) {
      for (ReadableEncodedScript readableScript : group.scripts) {
        EncodedScript script = readableScript.script;
        HashMap<String, Date> repositoryDateMap = dateMap.get(script.getPair().repository);
        if (repositoryDateMap == null) {
          repositoryDateMap = new HashMap<>();
          dateMap.put(script.getPair().repository, repositoryDateMap);
        }
        Date commitDate = repositoryDateMap.get(script.getPair().commitId1);
        if (commitDate == null) {
          commitDate =
              repoMap.get(script.getPair().repository).getCommitDate(script.getPair().commitId1);
          if (commitDate == null) {
            commitDate = new Date();
          }
          repositoryDateMap.put(script.getPair().commitId1, commitDate);
        }
        commitDate = repositoryDateMap.get(script.getPair().commitId2);
        if (commitDate == null) {
          commitDate =
              repoMap.get(script.getPair().repository).getCommitDate(script.getPair().commitId2);
          if (commitDate == null) {
            commitDate = new Date();
          }
          repositoryDateMap.put(script.getPair().commitId2, commitDate);
        }
      }
    }
    if (dateCache != null && !dateCache.exists()) {
      DateCache cacheTmp = new DateCache(dateMap);
      dateMapper.writeValue(dateCache, cacheTmp);
    }
    return dateMap;
  }

  private static void createRepositories(File tmpDir, ReadableEncodedGroupFile groupFileUpdated,
      HashMap<String, VcsRepository> repoMap) {
    for (ReadableEncodedGroup readableGroup : groupFileUpdated.groups) {
      for (ReadableEncodedScript script : readableGroup.scripts) {
        if (!repoMap.containsKey(script.script.getPair().repository)) {
          VcsRepository repo;
          try {
            repo = GitLink.openRepository(tmpDir, 0, script.script.getPair().repository);
            repoMap.put(script.script.getPair().repository, repo);
          } catch (NoSuchRepositoryException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }



  private static AresCreatePatternTime createPattern(
      LaseEvaluationGroupParameters parameterObject) {
    if (parameterObject.group.name.equals("74139")) {
      return null;
    }
    extractScripts(parameterObject);
    AresCreatePatternTime aresCreatePatternTime = null;
    aresCreatePatternTime = new AresCreatePatternTime(parameterObject.group.name);
    aresCreatePatternTime.iteration = 0;
    if (parameterObject.allPatterns) {
      createPatternForAllInputFiles(parameterObject, aresCreatePatternTime);
    } else {
      createPatternForTwoInputFiles(parameterObject, aresCreatePatternTime);
    }
    return aresCreatePatternTime;
  }

  private static void createPatternForTwoInputFiles(LaseEvaluationGroupParameters parameterObject,
      AresCreatePatternTime aresCreatePatternTime) {
    final File path = getPatternPath(parameterObject);
    aresCreatePatternTime.numberOfInputs = 2;
    String folder = parameterObject.group.name;
    String id0 = null;
    String id1 = null;
    for (String[] data : dataX()) {
      if (data[0].equals(folder)) {
        id0 = data[1];
        id1 = data[2];
        break;
      }
    }
    if (id0 == null || id1 == null) {
      return;
    }


    int[] order = new int[2];
    int pos = 0;
    for (ReadableEncodedScript script : parameterObject.group.scripts) {
      if (script.script.getPair().id.equals(id0)) {
        order[0] = pos;
      } else if (script.script.getPair().id.equals(id1)) {
        order[1] = pos;
      }
      pos++;
    }
    long timeComplete = System.nanoTime();
    AresMeasurement measurement = new AresMeasurement();
    GeneralizeGroup test = new GeneralizeGroup(path.getAbsolutePath(), path.getAbsolutePath(),
        order, measurement, ReferenceConfiguration.WRITE_REFERENCE);
    test.run();
    timeComplete = System.nanoTime() - timeComplete;
    aresCreatePatternTime.completeCreation = timeComplete;
    for (long time : measurement.timeOrderCreation) {
      aresCreatePatternTime.determineOrder += time;
    }
    for (long time : measurement.timeTreeDifferencing) {
      aresCreatePatternTime.treeDifferencing += time;
    }
  }

  private static void createPatternForAllInputFiles(LaseEvaluationGroupParameters parameterObject,
      AresCreatePatternTime aresCreatePatternTime) {


    aresCreatePatternTime.numberOfInputs = parameterObject.group.scripts.size();

    final AresMeasurement aresMeasurement = new AresMeasurement();
    final long timeComplete = System.nanoTime();

    runAllInputPatternCreation(parameterObject, aresMeasurement);
    aresCreatePatternTime.completeCreation = System.nanoTime() - timeComplete;
    for (long time : aresMeasurement.timeOrderCreation) {
      aresCreatePatternTime.determineOrder += time;
    }
    for (long time : aresMeasurement.timeTreeDifferencing) {
      aresCreatePatternTime.treeDifferencing += time;
    }

  }

  private static void runAllInputPatternCreation(LaseEvaluationGroupParameters parameterObject,
      final AresMeasurement aresMeasurement) {
    final File path = getPatternPath(parameterObject);

    int[] patternsToUse = new int[parameterObject.group.scripts.size()];
    for (int i = 0; i < parameterObject.group.scripts.size(); i++) {
      patternsToUse[i] = i;
    }
    File[] filesOriginal = new File[patternsToUse.length];
    File[] filesModified = new File[patternsToUse.length];
    BastProgram[] progsOriginal;
    BastProgram[] progsModified;
    for (int j = 0; j < patternsToUse.length; j++) {
      filesOriginal[j] =
          new File(path.getAbsoluteFile() + "/Change_" + patternsToUse[j] + "_original.java");
      filesModified[j] =
          new File(path.getAbsoluteFile() + "/Change_" + patternsToUse[j] + "_modified.java");
    }
    progsOriginal = new BastProgram[filesOriginal.length];
    progsModified = new BastProgram[filesOriginal.length];
    for (int j = 0; j < filesOriginal.length; j++) {
      progsOriginal[j] =
          ParserFactory.getParserInstance(AresExtension.NO_EXTENSIONS).parse(filesOriginal[j]);
      progsModified[j] =
          ParserFactory.getParserInstance(AresExtension.NO_EXTENSIONS).parse(filesModified[j]);
    }


    int[] initialOrder = null;
    long timeOrder = System.nanoTime();
    ExecutorService executor = Executors.newSingleThreadExecutor();
    final ExecutorService innerExecutor = Executors.newSingleThreadExecutor();
    initialOrder = GeneralizeGroup.generateInitialPair(progsOriginal, progsModified, executor);
    aresMeasurement.timeOrderCreation.add(System.nanoTime() - timeOrder);
    AbstractBastNode initOriginal = progsOriginal[initialOrder[0]];
    AbstractBastNode initModified = progsModified[initialOrder[0]];
    File subDir = new File(path, "/0/");
    if (!subDir.exists()) {
      subDir.mkdirs();
    }
    BastProgram secondProgramOriginal = progsOriginal[initialOrder[1]];
    BastProgram secondProgramModified = progsModified[initialOrder[1]];
    GeneralizeGroup.executeGeneralization(executor, innerExecutor, initOriginal, initModified,
        subDir, secondProgramOriginal, secondProgramModified, aresMeasurement,
        ReferenceConfiguration.WRITE_REFERENCE);
    LinkedList<BastProgram> used = new LinkedList<>();
    used.add(progsOriginal[initialOrder[0]]);
    used.add(progsOriginal[initialOrder[1]]);
    for (int i = 1; i < progsOriginal.length - 1; i++) {
      try {
        initOriginal = ParserFactory.getParserInstance(AresExtension.WITH_ARES_EXTENSIONS)
            .parse(new File(path, "/" + (i - 1) + "/Pattern_original.java"));
        initModified = ParserFactory.getParserInstance(AresExtension.WITH_ARES_EXTENSIONS)
            .parse(new File(path, "/" + (i - 1) + "/Pattern_modified.java"));
        assert (initOriginal != null);
        assert (initModified != null);

        int next = -1;
        timeOrder = System.nanoTime();

        next = GeneralizeGroup.nextIndex(used, progsOriginal, progsModified,
            (BastProgram) initOriginal, (BastProgram) initModified, executor, innerExecutor);
        aresMeasurement.timeOrderCreation.add(System.nanoTime() - timeOrder);
        used.add(progsOriginal[next]);
        secondProgramOriginal = progsOriginal[next];
        secondProgramModified = progsModified[next];
        subDir = new File(path, "/" + i + "/");
        if (!subDir.exists()) {
          subDir.mkdirs();
        }
        if (initOriginal != null && initModified != null) {
          GeneralizeGroup.executeGeneralization(executor, innerExecutor, initOriginal, initModified,
              subDir, secondProgramOriginal, secondProgramModified, aresMeasurement,
              ReferenceConfiguration.WRITE_REFERENCE);
        }
      } catch (SyntaxError e) {
        e.printStackTrace();
        // do nothing
      }

    }
    executor.shutdown();
    innerExecutor.shutdown();
  }

  private static void extractScripts(LaseEvaluationGroupParameters parameterObject) {
    File path = getPatternPath(parameterObject);
    if (!path.exists()) {
      path.mkdirs();
    }
    int index = 0;
    for (ReadableEncodedScript script : parameterObject.group.scripts) {
      ExtractScript extScript = ExtractUtilities.getScript(parameterObject.repoMap,
          parameterObject.tmpDir, script.script.getPair());
      ExtractUtilities.extractScriptPair(parameterObject.repoMap, path, parameterObject.tmpDir,
          extScript, index);
      index++;
    }
  }

  static void createGroup(LaseEvaluationGroupParameters parameterObject) {
    try {
      if (parameterObject.group.name.equals("74139")) {
        return;
      }
      File outputSubFileDir = getPatternPath(parameterObject);

      final AresCreatePatternTime createtime = createPattern(parameterObject);

      if (!outputSubFileDir.exists()) {
        outputSubFileDir.mkdirs();
      }
      File outputSubFile = new File(outputSubFileDir, "/ares_result.json");

      final ObjectMapper aresMapper = AresMapper.createJsonMapper();
      if (checkOldResults(parameterObject, outputSubFile, aresMapper)) {
        return;
      }
      String oldestId = null;
      Date oldest = new Date();
      String repositoryName = null;

      for (ReadableEncodedScript script : parameterObject.group.scripts) {

        Date tmp = parameterObject.dateMap.get(script.script.getPair().repository)
            .get(script.script.getPair().commitId1);
        if (tmp != null) {
          if (oldest.compareTo(tmp) > 0) {
            oldest = tmp;
            oldestId = script.script.getPair().commitId1;
            repositoryName = script.script.getPair().repository;
          }
        }
      }

      VcsRepository repository = parameterObject.repoMap.get(repositoryName);
      VcsRevision revisionBefore = (VcsRevision) repository.searchCommit(oldestId);

      final File revDir = createRevisionFiles(new File(parameterObject.tmpDir + "/revisions/"),
          repository, revisionBefore);

      final List<RecommendationResult> results = new LinkedList<>();

      File patternPath = null;
      if (parameterObject.allPatterns) {
        patternPath =
            new File(outputSubFileDir, "" + (parameterObject.group.scripts.size() - 2) + "/");
      } else {
        patternPath = outputSubFileDir;
      }

      final AresMeasurement measurement = new AresMeasurement();


      HashMap<ReadableEncodedScript, String> methodSignatureMap = new HashMap<>();
      HashMap<ReadableEncodedScript, AbstractBastNode> methodBlockMap = new HashMap<>();
      HashSet<String> startStrings = new HashSet<>();
      findPatternStarts(parameterObject, repositoryName, revDir, methodSignatureMap, methodBlockMap,
          startStrings);

      if (oldestId == null) {
        return;
      }
      List<String> files = repository.getallFiles_cmdline(oldestId);
      String[] filesArray = files.toArray(new String[files.size()]);
      final long timeComplete = System.nanoTime();

      ArrayList<String> filteredFiles = getFilteredFileList(parameterObject, files, filesArray);
      parameterObject.setFilteredFiles(filteredFiles);
      ArrayList<ReadableEncodedScript> inputs = createInputs(parameterObject);
      HashSet<String> ids = new HashSet<>();

      for (ReadableEncodedScript e : parameterObject.group.scripts) {
        ids.add(e.script.getPair().id);
      }

      ChangeGroup changeGroup = new ChangeGroup(ids);

      SearchForCodeLocationsParameter parameterObject3 =
          new SearchForCodeLocationsParameter(oldestId, repositoryName, revDir, null, measurement,
              inputs, parameterObject.group.scripts,
              methodSignatureMap, methodBlockMap, filesArray, createtime.completeCreation,
              changeGroup, parameterObject.tmpDir);
      executeParallelSearch(parameterObject3, patternPath);
      System.out.println();
      parameterObject3.searchTime.completeSearch.set(System.nanoTime() - timeComplete);
      RecommendationResult result = SharedMethods.computeRecommendationResult(parameterObject3,
          parameterObject.group.name, parameterObject.group.scripts, patternPath);
      results.add(result);
      aresMapper.writeValue(outputSubFile, result);
      result = aresMapper.readValue(outputSubFile, RecommendationResult.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static boolean checkOldResults(LaseEvaluationGroupParameters parameterObject,
      File outputSubFile, final ObjectMapper aresMapper) {
    RecommendationResult oldResult = null;
    if (outputSubFile.exists()) {
      try {
        oldResult = aresMapper.readValue(outputSubFile, RecommendationResult.class);
      } catch (Exception e) {
        oldResult = null;
      }
    }
    if (oldResult != null) {
      if (parameterObject.allPatterns) {
        if (oldResult.foundMembers == oldResult.allMembers.size()) {
          return true;
        }
      } else {
        String id1 = null;
        String id2 = null;
        for (String[] array : ExecuteLaseEvaluation.dataX()) {
          if (array[0].equals(parameterObject.group.name)) {
            id1 = array[1];
            id2 = array[2];
          }
        }
        boolean id1found = false;
        boolean id2found = false;
        for (AresRecommendationSet set : oldResult.recommendationSets) {
          if (id1.equals(set.memberId)) {
            id1found = true;
          }
          if (id2.equals(set.memberId)) {
            id2found = true;
          }
          if (id1found && id2found) {
            return true;
          }
        }
      }
    }
    return false;
  }

  private static void executeParallelSearch(SearchForCodeLocationsParameter parameterObject2,
      File workDir) {
    ExecutorService outerExecutor = Executors.newFixedThreadPool(NUM_THREADS);
    LinkedList<Future<List<AresRecommendationSet>>> recommendationSetFutures = new LinkedList<>();

    for (int i = 0; i < NUM_THREADS; i++) {

      Callable<List<AresRecommendationSet>> callable =
          new AresSearchPatternCallable(parameterObject2, i, NUM_THREADS, workDir);
      Future<List<AresRecommendationSet>> future = outerExecutor.submit(callable);
      recommendationSetFutures.add(future);
    }
    for (Future<List<AresRecommendationSet>> future : recommendationSetFutures) {
      try {
        parameterObject2.recommendationSets.addAll(future.get());
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        e.printStackTrace();
      }
    }
    outerExecutor.shutdown();
  }

  private static ArrayList<ReadableEncodedScript> createInputs(
      LaseEvaluationGroupParameters parameterObject) {
    ArrayList<ReadableEncodedScript> inputs = new ArrayList<>(2);
    if (parameterObject.allPatterns) {
      for (ReadableEncodedScript script : parameterObject.group.scripts) {
        inputs.add(script);
      }
    } else {
      for (int i = 0; i < ExecuteLaseEvaluation.dataX().length; i++) {
        if (ExecuteLaseEvaluation.dataX()[i][0].equals(parameterObject.group.name)) {
          String id1 = ExecuteLaseEvaluation.dataX()[i][1];
          String id2 = ExecuteLaseEvaluation.dataX()[i][2];
          for (ReadableEncodedScript script : parameterObject.group.scripts) {
            if (script.script.getPair().id.equals(id1)) {
              inputs.add(script);
            } else if (script.script.getPair().id.equals(id2)) {
              inputs.add(script);
            }
          }
        }
      }
    }
    return inputs;
  }

  private static void findPatternStarts(LaseEvaluationGroupParameters parameterObject,
      String repositoryName, final File revDir,
      HashMap<ReadableEncodedScript, String> methodSignatureMap,
      HashMap<ReadableEncodedScript, AbstractBastNode> methodBlockMap,
      HashSet<String> startStrings) {
    for (ReadableEncodedScript script : parameterObject.group.scripts) {
      methodSignatureMap.put(script, script.methodOriginal);
      VcsRepository repositoryTmp = parameterObject.repoMap.get(repositoryName);
      VcsRevision revisionModified =
          (GitRevision) repositoryTmp.searchCommit(script.script.getPair().commitId2);
      byte[] modifiedBytes = ((GitRepository) repositoryTmp).getFileContents(revisionModified,
          script.script.getPair().fileName);
      BastProgram modifiedTmp = RecommendationCreator.getAst(modifiedBytes, ParserType.JAVA_PARSER,
          AresExtension.NO_EXTENSIONS);
      if (modifiedTmp == null) {
        assert (false);
        continue;
      }
      GetMethodsVisitor methodsVisitor = new GetMethodsVisitor();
      modifiedTmp.accept(methodsVisitor);

      BastFunction method = methodsVisitor.functionIdMap.get(script.script.getPair().methodNumber2);
      FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(BastBlock.TAG);
      method.accept(fnft);
      methodBlockMap.put(script, fnft.nodes.get(0));

      String startString = extractFileStart(script, revDir);

      startStrings.add(startString);

    }
  }

  private static File getPatternPath(LaseEvaluationGroupParameters parameterObject) {
    File outputSubFileDir = null;
    if (parameterObject.allPatterns) {
      outputSubFileDir =
          new File(parameterObject.tmpDir, "/allInputs/" + parameterObject.group.name + "/");
    } else {
      outputSubFileDir =
          new File(parameterObject.tmpDir, "/twoInputs/" + parameterObject.group.name + "/");
    }
    return outputSubFileDir;
  }

  /**
   * Extract file start.
   *
   * @param script the script
   * @param revDir the rev dir
   * @return the string
   */
  public static String extractFileStart(ReadableEncodedScript script, File revDir) {
    final String fileName = script.script.getPair().fileName;


    byte[] fileContent = null;
    try {
      fileContent = FileUtils.getFileContents(new File(revDir, fileName));
    } catch (IOException e) {
      e.printStackTrace();
    }

    if (fileContent == null) {
      return null;
    }

    String packageName = null;

    try {
      final BufferedReader reader =
          new BufferedReader(new InputStreamReader(new ByteArrayInputStream(fileContent)));

      final MiniLexer lexer = new MiniLexer(reader);
      final PackageParser parser = new PackageParser(lexer);

      packageName = parser.parsePackage();
    } catch (final Throwable throwable) {
      return null;
    }

    if (packageName == null) {
      return null;
    }

    final String packagePath = packageName.replaceAll("\\.", File.separator);
    final int indexOfPackagePath = fileName.indexOf(packagePath);

    if (indexOfPackagePath == -1) {
      return null;
    }

    String startString = fileName.substring(0, indexOfPackagePath);
    return startString;
  }

  private static ArrayList<String> getFilteredFileList(
      LaseEvaluationGroupParameters parameterObject, List<String> files, String[] filesArray) {
    ArrayList<String> filteredFiles = new ArrayList<>();
    for (int i = 0; i < files.size(); i++) {
      String file = filesArray[i];

      String extension = "";

      int index = file.lastIndexOf('.');
      if (index > 0) {
        extension = file.substring(index + 1);
      }
      if (!extension.equals("java")) {
        continue;
      }
      filteredFiles.add(filesArray[i]);
    }
    return filteredFiles;
  }

  private static class AresSearchPatternCallable implements Callable<List<AresRecommendationSet>> {

    /**
     * Instantiates a new ares search pattern callable.
     * 
     * @param parameterObject TODO
     */
    private AresSearchPatternCallable(SearchForCodeLocationsParameter parameterObject, int start,
        int step, File workDir) {
      super();
      this.parameterObject = parameterObject;
      this.start = start;
      this.step = step;
      this.workDir = workDir;
    }

    SearchForCodeLocationsParameter parameterObject;
    int start;
    int step;
    File workDir;

    @Override
    public List<AresRecommendationSet> call() throws Exception {
      return searchFile(parameterObject, start, step, workDir);
    }

  }

  static List<AresRecommendationSet> searchFile(SearchForCodeLocationsParameter parameterObjectX,
      int start, int step, File workDir) {
    final ExecutorService executor = Executors.newSingleThreadExecutor();
    final File patternOriginalFile = new File(workDir, "Pattern_original.java");

    final File patternModifiedFile = new File(workDir, "Pattern_modified.java");
    BastProgram originalProg = AnalysePath.analyse(patternOriginalFile, ParserType.JAVA_PARSER);
    BastProgram modifiedProg = AnalysePath.analyse(patternModifiedFile, ParserType.JAVA_PARSER);
    byte[] patternOriginalBytes = null;
    byte[] patternModifiedBytes = null;
    try {
      patternOriginalBytes = FileUtils.getFileContents(patternOriginalFile);
      patternModifiedBytes = FileUtils.getFileContents(patternModifiedFile);
    } catch (IOException e1) {
      e1.printStackTrace();
    }
    List<AresRecommendationSet> recommendationSets = new LinkedList<>();

    ExtendedAresPattern outerTemplate = ExtendedTemplateExtractor.extractTemplate(originalProg,
        modifiedProg, executor, parameterObjectX.measurement);
    outerTemplate.resolveWildcards();
    if (outerTemplate.originalAst.block.statements == null
        || outerTemplate.originalAst.block.statements.size() == 0) {
      return recommendationSets;
    }
    for (int i = start; i < parameterObjectX.filesArrays.length; i += step) {
      try {
        if (i % 100 == 0) {
          System.out.print(".");
        }
        String file = parameterObjectX.filesArrays[i];

        String extension = "";

        int index = file.lastIndexOf('.');
        if (index > 0) {
          extension = file.substring(index + 1);
        }
        if (!extension.equals("java")) {
          continue;
        }
        if (parameterObjectX.searchTime != null) {
          parameterObjectX.searchTime.numberOfJavaFiles.incrementAndGet();
        }
        long parsingTime = System.nanoTime();
        byte[] testBytes = FileUtils.getFileContents(new File(parameterObjectX.revDir, file));


        BastProgram testProgTmp = null;
        try {
          testProgTmp = AnalysePath.analyse(testBytes, ParserType.JAVA_PARSER, false,
              AresExtension.NO_EXTENSIONS);
        } catch (SyntaxError e) {
          // do nothing
        }
        if (testProgTmp == null) {
          continue;
        }
        if (parameterObjectX.searchTime != null) {
          parameterObjectX.searchTime.parsing.addAndGet(System.nanoTime() - parsingTime);
        }
        de.fau.cs.inf2.cas.ares.recommendation.visitors.CountNodesVisitor countVisitor =
            new de.fau.cs.inf2.cas.ares.recommendation.visitors.CountNodesVisitor();
        testProgTmp.accept(countVisitor);

        FindInitialPatternStartsVisitor starts = new FindInitialPatternStartsVisitor(
            outerTemplate.originalAst.block.statements, outerTemplate);
        testProgTmp.accept(starts);
        if (starts.starts.size() == 0) {
          continue;
        }
        ArrayList<ArrayList<Recommendation>> testProg = RecommendationCreator
            .createTransformedProgram(file, patternOriginalBytes, patternModifiedBytes, testBytes,
                false, executor, parameterObjectX.measurement, outerTemplate);
        if (testProg == null) {
          continue;
        }
        if (testProg != null && testProg.size() > 0) {
          originalProg = AnalysePath.analyse(patternOriginalFile, ParserType.JAVA_PARSER);
          modifiedProg = AnalysePath.analyse(patternModifiedFile, ParserType.JAVA_PARSER);
          outerTemplate = ExtendedTemplateExtractor.extractTemplate(originalProg, modifiedProg,
              executor, parameterObjectX.measurement);
          outerTemplate.resolveWildcards();
        }

        SharedMethods.evaluateRecommendation(parameterObjectX, file, testProg);
      } catch (SyntaxError ex) {
        // do nothing
      } catch (Throwable ex) {
        // do nothing
      }
    }
    executor.shutdown();
    return recommendationSets;
  }


  private static class AresResCallable implements Callable<Integer> {
    LaseEvaluationGroupParameters parameterObject = null;

    public AresResCallable(LaseEvaluationGroupParameters parameterObject) {
      super();
      this.parameterObject = parameterObject;
    }



    @Override
    public Integer call() throws Exception {
      createGroup(parameterObject);
      return 1;
    }

  }

  /**
   * Creates the revision files.
   *
   * @param tmpDir the tmp dir
   * @param repository the repository
   * @param revisionBefore the revision before
   * @return the file
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static File createRevisionFiles(File tmpDir, VcsRepository repository,
      VcsRevision revisionBefore) throws IOException {
    File revDir = new File(tmpDir.getAbsolutePath(), revisionBefore.getName());
    if (!revDir.exists()) {
      revDir.mkdirs();
      Runtime run = Runtime.getRuntime();
      Process proc =
          run.exec(
              new String[] { "/bin/zsh",
                  "-c", "git archive " + revisionBefore.getName() + " | (cd "
                      + revDir.getAbsolutePath() + " && tar -x)", },
              null, (repository.getWorkDirectory()));
      try {
        proc.waitFor();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    return revDir;
  }
}
