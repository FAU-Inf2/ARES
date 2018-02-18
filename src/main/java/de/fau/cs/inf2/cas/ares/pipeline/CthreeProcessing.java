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

package de.fau.cs.inf2.cas.ares.pipeline;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.fau.cs.inf2.cas.ares.bast.general.ParserFactory;
import de.fau.cs.inf2.cas.ares.io.AresCreatePatternTime;
import de.fau.cs.inf2.cas.ares.io.AresMapper;
import de.fau.cs.inf2.cas.ares.io.AresMeasurement;
import de.fau.cs.inf2.cas.ares.io.AresSearchTime;
import de.fau.cs.inf2.cas.ares.io.RecommendationResult;
import de.fau.cs.inf2.cas.ares.pcreation.GeneralizeGroup;
import de.fau.cs.inf2.cas.ares.pcreation.GeneralizeGroup.ReferenceConfiguration;
import de.fau.cs.inf2.cas.ares.pcreation.exception.GeneralizationException;
import de.fau.cs.inf2.cas.ares.recommendation.AnalysePath;
import de.fau.cs.inf2.cas.ares.recommendation.ExtendedAresPattern;
import de.fau.cs.inf2.cas.ares.recommendation.Recommendation;
import de.fau.cs.inf2.cas.ares.recommendation.RecommendationCreator;
import de.fau.cs.inf2.cas.ares.recommendation.extension.ExtendedTemplateExtractor;
import de.fau.cs.inf2.cas.ares.recommendation.visitors.FindInitialPatternStartsVisitor;
import de.fau.cs.inf2.cas.ares.recommendation.visitors.FindSpecialPatternStartsVisitor;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunctionParameterDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameterList;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.visitors.FindNodesFromTagVisitor;
import de.fau.cs.inf2.cas.common.bast.visitors.GetInnerClassesVisitor;
import de.fau.cs.inf2.cas.common.bast.visitors.GetMethodsVisitor;
import de.fau.cs.inf2.cas.common.diff.CreateDiffPatterns;
import de.fau.cs.inf2.cas.common.io.ChangeGroup;
import de.fau.cs.inf2.cas.common.io.CommitPairIdentifier;
import de.fau.cs.inf2.cas.common.io.EncodedScript;
import de.fau.cs.inf2.cas.common.io.ReadableEncodedScript;
import de.fau.cs.inf2.cas.common.parser.AresExtension;
import de.fau.cs.inf2.cas.common.parser.ParserType;
import de.fau.cs.inf2.cas.common.parser.SyntaxError;

import de.fau.cs.inf2.cas.common.util.FileUtils;

import de.fau.cs.inf2.cas.common.vcs.ExtractScript;
import de.fau.cs.inf2.cas.common.vcs.ExtractUtilities;
import de.fau.cs.inf2.cas.common.vcs.ExtractUtilities.SignaturePrinter;
import de.fau.cs.inf2.cas.common.vcs.base.NoSuchCommitException;
import de.fau.cs.inf2.cas.common.vcs.base.VcsRepository;
import de.fau.cs.inf2.cas.common.vcs.base.VcsRevision;
import de.fau.cs.inf2.cas.common.vcs.git.GitRepository;
import de.fau.cs.inf2.cas.common.vcs.git.GitRevision;
import de.fau.cs.inf2.cthree.data.CodeChange;
import de.fau.cs.inf2.cthree.data.DataSet;
import de.fau.cs.inf2.cthree.io.DataBind;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CthreeProcessing {
  public static final int MAX_INPUT_SIZE = 20;
  public static final boolean SKIP_ERRORS = true;

  /**
   * Handle group part.
   *
   * @param cthreeFile the cthree file
   * @param tmpDir the tmp dir
   * @param numThreads the num threads
   */
  public static void handleGroupPart(File cthreeFile, File tmpDir, int numThreads) {
    handleGroupPart(cthreeFile,tmpDir,numThreads, -1, -1);
  }
  
  /**
   * Handle group part.
   *
   * @param cthreeFile the cthree file
   * @param tmpDir the tmp dir
   * @param numThreads the num threads
   * @param start number of the group to start the computation
   * @param end number of the last group to compute
   */
  public static void handleGroupPart(File cthreeFile, File tmpDir, int numThreads,
      int start, int end) {
    HashMap<String, CommitPairIdentifier> cpis = new HashMap<>();
    HashMap<CommitPairIdentifier, String> cpisIds = new HashMap<>();
    ArrayList<ChangeGroup> groups = new ArrayList<>();
    ObjectMapper mapper = DataBind.createMapper();
    try {
      DataSet groupFileObject = mapper.readValue(cthreeFile, DataSet.class);
      int counter = 0;
      for (de.fau.cs.inf2.cthree.data.Cluster cluster : groupFileObject.clusters) {
        HashSet<String> members = new HashSet<>();
        for (CodeChange change : cluster.members) {

          CommitPairIdentifier cpi = new CommitPairIdentifier(counter, change.repository,
              change.commitBeforeChange, change.commitAfterChange, change.fileName,
              change.methodNumberBeforeChange, change.methodNumberAfterChange);
          counter++;
          cpisIds.put(cpi, cpi.id);
          cpis.put(cpi.id, cpi);
          members.add(cpi.id);
        }

        ChangeGroup group = new ChangeGroup(members);
        groups.add(group);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }


    HashMap<String, VcsRepository> repositories = new HashMap<>();

    ExecutorService executioner = Executors.newFixedThreadPool(numThreads);
    ExecutorService innerExecutioner = Executors.newFixedThreadPool(numThreads);
    File repoDir = new File(tmpDir, "/repositories/");
    int count = 0;
    if (start == -1) {
      start = 0;
    }
    if (end == -1) {
      end = groups.size();
    }
    for (ChangeGroup group : groups) {
      count++;
      if (count < start) {
        continue;
      }
      if (count > end) {
        break;
      }
      String groupHash = group.getHash();
      System.out.println(new Date() + " Start with change group " 
          + groupHash + "(" + count + "/" + groups.size() + ").");

      File folder = new File(tmpDir.getAbsolutePath() + "/results/" + groupHash + "/");
      if (!folder.exists()) {
        boolean success = folder.mkdirs();
        if (!success) {
          System.err.println("Could not create Folder!");
          System.exit(-1);
        }
      } else if (SKIP_ERRORS) {
        final File outputFile = new File(folder, "/time_create.json");
        if (outputFile.exists()) {
          System.out.println(new Date() + " Finished change group " + groupHash + ".");
          continue;
        }
      }

      long createTime = generalizeGroup(group, executioner, innerExecutioner, folder, cpis,
          repositories, repoDir);
      if (createTime == -1) {
        System.out.println(new Date() + " Finished change group " + groupHash + ".");
        continue;
      }
      executeRecall(group, executioner, innerExecutioner, folder, cpis, repositories, repoDir,
          numThreads, createTime);
      System.out.println(new Date() + " Finished change group " + groupHash + ".");
    }
    executioner.shutdown();
    innerExecutioner.shutdown();
  }

  /**
   * Generalize group.
   *
   * @param group the group
   * @param executioner the executioner
   * @param innerExecutioner the inner executioner
   * @param workDir the work dir
   * @param cpis the cpis
   * @param repositories the repositories
   * @param repoDir the repo dir
   * @return the long
   */
  public static long generalizeGroup(ChangeGroup group, ExecutorService executioner,
      ExecutorService innerExecutioner, File workDir, HashMap<String, CommitPairIdentifier> cpis,
      HashMap<String, VcsRepository> repositories, File repoDir) {
    long completeTime = -1;
    File before = new File(workDir.getAbsolutePath() + "/Pattern_before.java");
    File after = new File(workDir.getAbsolutePath() + "/Pattern_after.java");
    File outputTimeFile = new File(workDir, "/time_create.json");
    try {
      if (before.exists() && after.exists() && outputTimeFile.exists()) {
        ObjectMapper mapper = AresMapper.createJsonMapper();
        AresCreatePatternTime aresCreatePatternTime =
            mapper.readValue(outputTimeFile, AresCreatePatternTime.class);
        return aresCreatePatternTime.completeCreation;
      }


      if (group.getMembers().size() < MAX_INPUT_SIZE) {

        BastProgram[] progsBefore = new BastProgram[group.getMembers().size()];
        BastProgram[] progsAfter = new BastProgram[group.getMembers().size()];
        createProgramsFromGroup(group, cpis, repositories, repoDir, progsBefore, progsAfter);
        AresCreatePatternTime aresCreatePatternTime = new AresCreatePatternTime(group.getHash());
        aresCreatePatternTime.iteration = 0;
        aresCreatePatternTime.numberOfInputs = group.getMembers().size();
        AresMeasurement measurement = new AresMeasurement();
        long timeComplete = System.nanoTime();
        GeneralizeGroup generalizeGroup = new GeneralizeGroup(workDir.getAbsolutePath(),
            progsBefore, progsAfter, true, measurement);
        generalizeGroup.writeReference = ReferenceConfiguration.WRITE_REFERENCE;
        generalizeGroup.run(executioner, innerExecutioner, false);
        timeComplete = System.nanoTime() - timeComplete;
        if (aresCreatePatternTime != null) {
          aresCreatePatternTime.completeCreation = timeComplete;
          for (long time : measurement.timeOrderCreation) {
            aresCreatePatternTime.determineOrder += time;
          }
          for (long time : measurement.timeTreeDifferencing) {
            aresCreatePatternTime.treeDifferencing += time;
          }

          try {
            ObjectMapper mapper = AresMapper.createJsonMapper();
            File outputFile = new File(workDir, "/time_create.json");
            mapper.writeValue(outputFile, aresCreatePatternTime);
            aresCreatePatternTime = null;
            aresCreatePatternTime = mapper.readValue(outputFile, AresCreatePatternTime.class);
            completeTime = aresCreatePatternTime.completeCreation;
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      } else {
        GeneralizeGroup.writeOvergeneralization(new File(workDir.getAbsolutePath() + "/"),
            new GeneralizationException("Group to large!"), ReferenceConfiguration.WRITE_REFERENCE);
      }
    } catch (Throwable e) {
      e.printStackTrace();
      return -1;
    }
    return completeTime;
  }

  /**
   * Creates the programs from group.
   *
   * @param group the group
   * @param cpis the cpis
   * @param repositories the repositories
   * @param repoDir the repo dir
   * @param progsBeforeGlobal the progs before global
   * @param progsAfterGlobal the progs after global
   * @throws NoSuchCommitException the no such commit exception
   */
  public static void createProgramsFromGroup(ChangeGroup group,
      HashMap<String, CommitPairIdentifier> cpis, HashMap<String, VcsRepository> repositories,
      File repoDir, BastProgram[] progsBeforeGlobal, BastProgram[] progsAfterGlobal)
      throws NoSuchCommitException {
    int counter = 0;
    ArrayList<String> members = new ArrayList<String>(group.getMembers());
    Collections.sort(members);
    for (String member : members) {
      CommitPairIdentifier cpi = cpis.get(member);
      ExtractScript script = ExtractUtilities.getScript(repositories, repoDir, cpi);
      BastProgram[] pair =
          ExtractUtilities.extractScriptPair(repositories, null, repoDir, script, counter);
      progsBeforeGlobal[counter] = pair[0];
      progsAfterGlobal[counter] = pair[1];
      counter++;
    }
  }

  /**
   * Gets the script.
   *
   * @param repositories the repositories
   * @param repoDir the repo dir
   * @param cpi the cpi
   * @return the script
   */
  public static ExtractScript getScript(HashMap<String, VcsRepository> repositories, File repoDir,
      CommitPairIdentifier cpi) {
    if (cpi == null) {
      return null;
    }
    BastProgram oldProgram = null;
    BastProgram newProgram = null;
    byte[] oldFileData;
    byte[] newFileData;
    String oldFileN = cpi.fileName.split("~~")[0];
    String newFileN = cpi.fileName.split("~~")[0];
    String repositoryName = cpi.repository.endsWith("gi") ? cpi.repository + "t" : cpi.repository;
    try {
      File folder = new File(repoDir.getAbsolutePath() + "/" + repositoryName.hashCode());
      VcsRepository repository;
      repository = repositories.get(repositoryName);
      boolean cloneFromRemoteRepo = false;
      if (!folder.exists()) {
        FileUtils.checkAndCreateDirectory(folder);
        cloneFromRemoteRepo = true;
      }
      if (repository == null) {
        if (cloneFromRemoteRepo) {
          repository = FileUtils.cloneRepository(repositoryName, folder);
          repositories.put(repositoryName, repository);
        } else {
          repository = FileUtils.openRepository(folder);
          repositories.put(repositoryName, repository);

        }
      }

      GitRevision oldRev = (GitRevision) repository.searchCommit(cpi.commitId1);
      GitRevision newRev = (GitRevision) repository.searchCommit(cpi.commitId2);

      oldFileData = ((GitRepository) repository).getFileContents(oldRev, oldFileN);
      newFileData = ((GitRepository) repository).getFileContents(newRev, newFileN);
    } catch (NoSuchCommitException e) {
      e.printStackTrace();
      System.exit(-1);
      throw new RuntimeException();
    }

    assert (oldFileData != null);
    assert (newFileData != null);

    try {
      oldProgram = ParserFactory.getParserInstance(AresExtension.NO_EXTENSIONS).parse(oldFileData);
      newProgram = ParserFactory.getParserInstance(AresExtension.NO_EXTENSIONS).parse(newFileData);

      if (oldProgram == null || newProgram == null) {
        throw new RuntimeException("parse");
      }
    } catch (SyntaxError e) {
      System.err.println("SyntaxError: " + e.msg);
      return null;
    } catch (Throwable t) {
      t.printStackTrace();
      return null;
    }
    GetMethodsVisitor gmvOld = new GetMethodsVisitor();
    GetMethodsVisitor gmvNew = new GetMethodsVisitor();
    oldProgram.accept(gmvOld);
    newProgram.accept(gmvNew);
    BastFunction methodOriginal = null;
    BastFunction methodModified = null;
    methodOriginal = gmvOld.functionIdMap.get(cpi.methodNumber1);
    methodModified = gmvNew.functionIdMap.get(cpi.methodNumber2);
    GetInnerClassesVisitor gicOld = new GetInnerClassesVisitor();
    GetInnerClassesVisitor gicNew = new GetInnerClassesVisitor();
    oldProgram.accept(gicOld);
    newProgram.accept(gicNew);

    if (methodOriginal == null || methodModified == null) {
      return null;
    }
    final String innerClassesBefore = gicOld.getClassString(methodOriginal);
    final String innerClassesAfter = gicNew.getClassString(methodModified);
    assert (methodOriginal != null);
    assert (methodOriginal != null);
    assert (cpi != null);
    final BastIdentDeclarator declOriginal = (BastIdentDeclarator) methodOriginal.decl;
    assert (declOriginal != null);
    final BastIdentDeclarator declModified = (BastIdentDeclarator) methodModified.decl;
    assert (declModified != null);
    final BastFunctionParameterDeclarator funcDeclOriginal =
        (BastFunctionParameterDeclarator) declOriginal.declarator;
    assert (funcDeclOriginal != null);
    final BastFunctionParameterDeclarator funcDeclModified =
        (BastFunctionParameterDeclarator) declModified.declarator;
    assert (funcDeclModified != null);
    assert ((BastParameterList) funcDeclOriginal.parameters != null);
    assert ((BastParameterList) funcDeclModified.parameters != null);
    ExtractScript script = new ExtractScript(repositoryName, cpi.commitId1, cpi.commitId2, oldFileN,
        newFileN, innerClassesBefore, innerClassesAfter, methodOriginal.name, methodModified.name,
        (BastParameterList) funcDeclOriginal.parameters,
        (BastParameterList) funcDeclModified.parameters, true);
    return script;
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

  /**
   * Execute recall.
   *
   * @param group the group
   * @param executioner the executioner
   * @param innerExecutioner the inner executioner
   * @param workDir the work dir
   * @param cpis the cpis
   * @param repositories the repositories
   * @param repoDir the repo dir
   * @param numThreads the num threads
   * @param createTime the create time
   */
  public static void executeRecall(ChangeGroup group, ExecutorService executioner,
      ExecutorService innerExecutioner, File workDir, HashMap<String, CommitPairIdentifier> cpis,
      HashMap<String, VcsRepository> repositories, File repoDir, int numThreads, long createTime) {
    final File outputFile = new File(workDir, "/recall_result.json");
    File outputSearch = new File(workDir, "/time_search.json");
    File tmpDir = null;
    if (outputFile.exists() && outputSearch.exists()) {
      return;
    }
    try {
      if (group.getMembers().size() < MAX_INPUT_SIZE) {
        final File genBeforeFile = new File(workDir, "Pattern_original.java");

        String before = new String(FileUtils.getFileContents(genBeforeFile), "UTF-8");
        if (before.startsWith("OVERGENERALIZATION")) {
          return;
        }

        String oldestId = null;
        Date oldest = new Date();
        String repositoryName = null;

        for (String id : group.getMembers()) {
          CommitPairIdentifier cpi = cpis.get(id);
          GitRepository repository = (GitRepository) repositories.get(cpi.repository);
          Date tmp = repository.getCommitDate(cpi.commitId1);
          if (tmp != null) {
            if (oldest.compareTo(tmp) > 0) {
              oldest = tmp;
              oldestId = cpi.commitId1;
              repositoryName = cpi.repository;
            }
          }
        }

        GitRepository repository = (GitRepository) repositories.get(repositoryName);
        GitRevision revisionBefore = (GitRevision) repository.searchCommit(oldestId);
        tmpDir = Files.createTempDirectory(null).toFile();
        final File revDir = createRevisionFiles(tmpDir, repository, revisionBefore);

        final AresMeasurement measurement = new AresMeasurement();
        ArrayList<ReadableEncodedScript> inputs = new ArrayList<>(2);
        for (String scriptId : group.getMembers()) {
          ReadableEncodedScript newScript = createReadableEncodedScript(cpis, repository, scriptId);
          inputs.add(newScript);
        }



        HashMap<ReadableEncodedScript, String> methodSignatureMap = new HashMap<>();
        HashMap<ReadableEncodedScript, AbstractBastNode> methodBlockMap = new HashMap<>();
        computeMethodBlocks(repositories, repositoryName, inputs, methodSignatureMap,
            methodBlockMap);


        if (oldestId == null) {
          return;
        }
        List<String> files = repository.getallFiles_cmdline(oldestId);
        String[] filesArray = files.toArray(new String[files.size()]);
        final long timeComplete = System.nanoTime();


        SearchForCodeLocationsParameter parameterObject = new SearchForCodeLocationsParameter(
            oldestId, repositoryName, revDir, executioner, measurement, inputs, inputs,
            methodSignatureMap, methodBlockMap, filesArray, createTime, group, workDir);
        searchForCodeLocations(parameterObject);
        if (parameterObject.searchTime != null) {
          parameterObject.searchTime.completeSearch.set(System.nanoTime() - timeComplete);
        }
        String groupHash = parameterObject.group.getHash();

        RecommendationResult result = SharedMethods.computeRecommendationResult(parameterObject,
            groupHash, parameterObject.inputs, parameterObject.workDir);
        ObjectMapper aresMapper = AresMapper.createJsonMapper();
        aresMapper.writeValue(outputFile, result);
        result = aresMapper.readValue(outputFile, RecommendationResult.class);
        aresMapper.writeValue(outputSearch, parameterObject.searchTime);
        parameterObject.searchTime = null;
        parameterObject.searchTime = aresMapper.readValue(outputSearch, AresSearchTime.class);
        assert (parameterObject.searchTime != null);
      }
    } catch (Throwable e) {
      return;
    } finally {
      if (tmpDir != null && tmpDir.exists()) {
        FileUtils.deleteTmpDirectory(tmpDir.getAbsolutePath());
      }
    }
    return;
  }

  private static void searchForCodeLocations(SearchForCodeLocationsParameter parameterObject) {
    final File genBeforeFile = new File(parameterObject.workDir, "Pattern_original.java");

    final File genAfterFile = new File(parameterObject.workDir, "Pattern_modified.java");
    byte[] genBeforeBytes = null;
    byte[] genAfterBytes = null;
    try {
      genBeforeBytes = FileUtils.getFileContents(genBeforeFile);

      genAfterBytes = FileUtils.getFileContents(genAfterFile);
    } catch (IOException e1) {
      e1.printStackTrace();
      return;
    }
    BastProgram seqProg = AnalysePath.analyse(genBeforeFile, ParserType.JAVA_PARSER);
    BastProgram parProg = AnalysePath.analyse(genAfterFile, ParserType.JAVA_PARSER);
    ExtendedAresPattern outerTemplate = ExtendedTemplateExtractor.extractTemplate(seqProg, parProg,
        parameterObject.executor, parameterObject.measurement);
    outerTemplate.resolveWildcards();

    for (int i = 0; i < parameterObject.filesArrays.length; i++) {
      try {
        String file = parameterObject.filesArrays[i];

        String extension = "";

        int index = file.lastIndexOf('.');
        if (index > 0) {
          extension = file.substring(index + 1);
        }
        if (!extension.equals("java")) {
          continue;
        }
        if (outerTemplate.originalAst.block.statements == null
            || outerTemplate.originalAst.block.statements.size() == 0) {
          continue;
        }
        if (parameterObject.searchTime != null) {
          parameterObject.searchTime.numberOfJavaFiles.incrementAndGet();
        }
        long parsingTime = System.nanoTime();
        byte[] testBytes = FileUtils.getFileContents(new File(parameterObject.revDir, file));


        BastProgram testProgTmp = null;
        try {
          testProgTmp = AnalysePath.analyse(testBytes, ParserType.JAVA_PARSER, false,
              AresExtension.NO_EXTENSIONS);
        } catch (SyntaxError e) {
          e.printStackTrace();
        }
        if (testProgTmp == null) {
          continue;
        }
        if (parameterObject.searchTime != null) {
          parameterObject.searchTime.parsing.addAndGet(System.nanoTime() - parsingTime);
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
        ArrayList<ArrayList<Recommendation>> testProg =
            RecommendationCreator.createTransformedProgram(file, genBeforeBytes, genAfterBytes,
                testBytes, false, parameterObject.executor, parameterObject.measurement, null);
        SharedMethods.evaluateRecommendation(parameterObject, file, testProg);
      } catch (SyntaxError ex) {
        ex.printStackTrace();
      } catch (Throwable ex) {
        ex.printStackTrace();
      }
    }
  }



  private static void computeMethodBlocks(HashMap<String, VcsRepository> repositories,
      String repositoryName, ArrayList<ReadableEncodedScript> inputs,
      HashMap<ReadableEncodedScript, String> methodSignatureMap,
      HashMap<ReadableEncodedScript, AbstractBastNode> methodBlockMap) {
    for (ReadableEncodedScript script : inputs) {

      methodSignatureMap.put(script, script.methodOriginal);
      GitRepository repositoryTmp = (GitRepository) repositories.get(repositoryName);
      GitRevision revisionAfter =
          (GitRevision) repositoryTmp.searchCommit(script.script.getPair().commitId2);
      byte[] afterBytes = ((GitRepository) repositoryTmp).getFileContents(revisionAfter,
          script.script.getPair().fileName);
      BastProgram afterTmp = RecommendationCreator.getAst(afterBytes, ParserType.JAVA_PARSER,
          AresExtension.NO_EXTENSIONS);
      if (afterTmp == null) {
        assert (false);
        continue;
      }
      GetMethodsVisitor methodsVisitor = new GetMethodsVisitor();
      afterTmp.accept(methodsVisitor);

      BastFunction method = methodsVisitor.functionIdMap.get(script.script.getPair().methodNumber2);
      FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(BastBlock.TAG);
      method.accept(fnft);
      methodBlockMap.put(script, fnft.nodes.get(0));
    }
  }

  private static ReadableEncodedScript createReadableEncodedScript(
      HashMap<String, CommitPairIdentifier> cpis, GitRepository repository, String scriptId) {
    CommitPairIdentifier cpi = cpis.get(scriptId);
    return getEncodedScriptToIdentifier(repository, cpi);
  }

  /**
   * Gets the encoded script to identifier.
   *
   * @param repository the repository
   * @param cpi the cpi
   * @return the encoded script to identifier
   */
  public static ReadableEncodedScript getEncodedScriptToIdentifier(GitRepository repository,
      CommitPairIdentifier cpi) {
    EncodedScript encodedScript = new EncodedScript(cpi, null, null, -1, -1);
    final CommitPairIdentifier commitPair = encodedScript.getPair();

    final StringBuilder diffLines = CreateDiffPatterns.getDiffLines(commitPair, repository);

    GitRepository gitRepo = (GitRepository) repository;
    byte[] oldFileData;
    byte[] newFileData;
    GitRevision oldRev = (GitRevision) gitRepo.searchCommit(encodedScript.getPair().commitId1);
    GitRevision newRev = (GitRevision) gitRepo.searchCommit(encodedScript.getPair().commitId2);
    oldFileData = gitRepo.getFileContents(oldRev, encodedScript.getPair().fileName);
    newFileData = gitRepo.getFileContents(newRev, encodedScript.getPair().fileName);
    BastProgram oldProgram =
        ParserFactory.getParserInstance(AresExtension.NO_EXTENSIONS).parse(oldFileData);
    BastProgram newProgram =
        ParserFactory.getParserInstance(AresExtension.NO_EXTENSIONS).parse(newFileData);
    GetMethodsVisitor oldVisitor = new GetMethodsVisitor();
    oldProgram.accept(oldVisitor);
    GetMethodsVisitor newVisitor = new GetMethodsVisitor();
    newProgram.accept(newVisitor);
    final BastFunction oldFunction =
        oldVisitor.functionIdMap.get(encodedScript.getPair().methodNumber1);
    final BastFunction newFunction =
        newVisitor.functionIdMap.get(encodedScript.getPair().methodNumber2);
    SignaturePrinter printer = new SignaturePrinter();
    oldFunction.accept(printer);
    final String methodBefore = printer.buffer.toString();
    printer.buffer.setLength(0);
    if (newFunction != null) {
      newFunction.accept(printer);
    }
    String methodAfter = printer.buffer.toString();
    String diff = diffLines.toString();
    String commitMsg = repository.getCommitMsg(encodedScript.getPair().commitId2);
    ReadableEncodedScript newScript =
        new ReadableEncodedScript(methodBefore, methodAfter, diff, commitMsg, encodedScript);
    return newScript;
  }
}


