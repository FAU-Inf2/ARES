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

package de.fau.cs.inf2.cas.ares.pipeline;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
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
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CthreeForkedProcessing {
  public static final int MAX_INPUT_SIZE = 20;

  public static void main(String[] args) {
    ObjectMapper aresMapper = AresMapper.createJsonMapper();

    try {
      HashMap<String, CommitPairIdentifier> cpis = new HashMap<>();
      File cpiData = new File(args[0]);
      CommitPairIdentifier[] readArray =
          aresMapper.readValue(cpiData, CommitPairIdentifier[].class);
      assert (readArray.length > 0);
      ChangeGroup group = new ChangeGroup();
      for (CommitPairIdentifier cpi : readArray) {
        group.addMember(cpi.id);
        cpis.put(cpi.id, cpi);
      }
      File tmpDir = new File(args[1]);
      int numThreads = Integer.parseInt(args[2]);
      File repoDir = new File(args[3]);
      boolean skipErrors = Boolean.parseBoolean(args[4]);
      executeSingleGroup(group, tmpDir, numThreads, cpis, repoDir, skipErrors);

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }


  }

  /**
   * Handle group part.
   *
   * @param cthreeFile the cthree file
   * @param tmpDir the tmp dir
   * @param numThreads the num threads
   */
  public static void handleGroupPart(File cthreeFile, File tmpDir, int numThreads,
      boolean skipErrors) {
    handleGroupPart(cthreeFile, tmpDir, numThreads, -1, -1, skipErrors);
  }

  /**
   * Handle group part.
   *
   * @param cthreeFile the cthree file
   * @param tmpDir the tmp dir
   * @param numThreads the num threads
   */
  public static void handleGroupPart(File cthreeFile, File tmpDir, int numThreads, int start,
      int end, boolean skipErrors) {
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
      if (skipErrors) {
        File folder = new File(tmpDir.getAbsolutePath() + "/results/" + group.getHash() + "/");
        if (folder.exists()) {
          continue;
        }
      }
      System.out.println(new Date() + " Start with change group " + group.getHash() + "(" + count
          + "/" + groups.size() + ").");
      CommitPairIdentifier[] cpiArray = new CommitPairIdentifier[group.getMembers().size()];
      int pos = 0;
      for (String member : group.getMembers()) {
        cpiArray[pos] = cpis.get(member);
        pos++;
      }
      File folder = new File(tmpDir.getAbsolutePath() + "/results/" + group.getHash() + "/");
      if (!folder.exists()) {
        boolean success = folder.mkdirs();
        if (!success) {
          System.err.println("Could not create Folder!");
          continue;
        }
      }
      File cpiData = new File(folder, "cpidata.json");
      ObjectMapper aresMapper = AresMapper.createJsonMapper();
      try {
        aresMapper.writeValue(cpiData, cpiArray);
      } catch (JsonProcessingException e) {
        e.printStackTrace();
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      String[] args = new String[5];

        args[0] = cpiData.getAbsolutePath();
        args[1] = tmpDir.getAbsolutePath();
        args[2] = String.valueOf(numThreads);
        args[3] = repoDir.getAbsolutePath();
        args[4] = String.valueOf(skipErrors);
        executeWithProcess(args);
        //main(args);

    }
  }

  private static final String[] appendAdditionalArgs(final String[] additionalArgs,
      final String... args) {
    final String[] newArgs = new String[additionalArgs.length + args.length];

    System.arraycopy(args, 0, newArgs, 0, args.length);
    System.arraycopy(additionalArgs, 0, newArgs, args.length, additionalArgs.length);

    return newArgs;
  }

  /**
   * Execute.
   *
   * @param entryClass the entry class
   * @param additionalArgs the additional args
   * @return the process
   */
  private static final Process execute(final Class<?> entryClass, final String... additionalArgs) {
    // see https://stackoverflow.com/questions/636367
    final String javaHome = System.getProperty("java.home");
    final String javaBinary = javaHome + File.separator + "bin" + File.separator + "java";

    final String classPath = System.getProperty("java.class.path");
    final String className = entryClass.getName();

    final String[] args =
        appendAdditionalArgs(additionalArgs, javaBinary, "-ea", "-cp", classPath, className);

    final ProcessBuilder processBuilder = new ProcessBuilder(args);
    processBuilder.inheritIO();

    try {
      final Process process = processBuilder.start();

      return process;
    } catch (final Throwable throwable) {
      System.err.println("[!] unable to execute program: " + throwable.getMessage());
      return null;
    }
  }

  public static void executeWithProcess(String[] args) {
    final Process process = execute(CthreeForkedProcessing.class, args);

    try {
      process.waitFor(20, TimeUnit.MINUTES);
    } catch (final Throwable throwable) {
      System.err.println("Unable to wait for child process: " + throwable.getMessage());
      return;
    }
    if (process.isAlive()) {
      process.destroyForcibly();
      try {
        process.waitFor();
      } catch (InterruptedException e) {
        System.err.println("Unable to wait for child process: " + e.getMessage());
        return;
      }
    }
    int exitValue = process.exitValue();
    if (exitValue != 0) {
      System.out.println("Timeout.");
    }
  }


  public static void executeSingleGroup(ChangeGroup group, File tmpDir, int numThreads,
      HashMap<String, CommitPairIdentifier> cpis, File repoDir, boolean skipErrors) {
    HashMap<String, VcsRepository> repositories = new HashMap<>();
    String groupHash = group.getHash();
    ExecutorService executioner = Executors.newFixedThreadPool(numThreads);
    ExecutorService innerExecutioner = Executors.newFixedThreadPool(numThreads);
    try {
      File folder = new File(tmpDir.getAbsolutePath() + "/results/" + groupHash + "/");
      if (!folder.exists()) {
          System.err.println("Folder does not exist!");
          return;
      }
      long createTime = CthreeProcessing.generalizeGroup(group, executioner, innerExecutioner,
          folder, cpis, repositories, repoDir);
      if (createTime == -1) {
        System.out.println(new Date() + " Finished change group " + groupHash + ".");
        return;
      }
      CthreeProcessing.executeRecall(group, executioner, innerExecutioner, folder, cpis,
          repositories, repoDir, numThreads, createTime);
      System.out.println(new Date() + " Finished change group " + groupHash + ".");
    } catch (Throwable e) {
      e.printStackTrace();
    } finally {
      executioner.shutdownNow();
      innerExecutioner.shutdownNow();
    }
  }
}


