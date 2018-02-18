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
import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.io.AresMapper;
import de.fau.cs.inf2.cas.ares.io.AresRecommendation;
import de.fau.cs.inf2.cas.ares.io.AresRecommendationSet;
import de.fau.cs.inf2.cas.ares.io.AresSearchTime;
import de.fau.cs.inf2.cas.ares.io.RecommendationFile;
import de.fau.cs.inf2.cas.ares.io.RecommendationResult;
import de.fau.cs.inf2.cas.ares.recommendation.AnalysePath;
import de.fau.cs.inf2.cas.ares.recommendation.ExtendedAresPattern;
import de.fau.cs.inf2.cas.ares.recommendation.Recommendation;
import de.fau.cs.inf2.cas.ares.recommendation.RecommendationCreator;
import de.fau.cs.inf2.cas.ares.recommendation.extension.ExtendedTemplateExtractor;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.CreateJavaNodeHelper;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExternalDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.visitors.FindNodesFromTagVisitor;
import de.fau.cs.inf2.cas.common.bast.visitors.IPrettyPrinter;
import de.fau.cs.inf2.cas.common.io.ReadableEncodedScript;

import de.fau.cs.inf2.cas.common.parser.ParserType;
import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import de.fau.cs.inf2.cas.common.tokenmatching.LevenshteinTokenDistance;

import de.fau.cs.inf2.cas.common.util.FileUtils;
import de.fau.cs.inf2.cas.common.util.num.Statistics;
import de.fau.cs.inf2.cas.common.util.string.LevenshteinDistance;

import de.fau.cs.inf2.cas.common.vcs.ExtractUtilities.SignaturePrinter;
import de.fau.cs.inf2.cthree.data.DataSet;
import de.fau.cs.inf2.cthree.io.DataBind;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SharedMethods {

  /**
   * Gets the method text.
   *
   * @param patternFile the pattern file
   * @return the method text
   */
  public static String getMethodText(final File patternFile) {
    BastProgram ast = RecommendationCreator.getAst(patternFile, ParserType.JAVA_PARSER);
    if (ast == null) {
      return null;
    }
    FindNodesFromTagVisitor fnftPattern = new FindNodesFromTagVisitor(AresBlock.TAG);
    ast.accept(fnftPattern);
    IPrettyPrinter printer = ParserFactory.getAresPrettyPrinter();
    fnftPattern.nodes.get(0).accept(printer);
    final String pattern = printer.getBuffer().toString();
    return pattern;
  }

  /**
   * Evaluate recommendation.
   *
   * @param parameterObject the parameter object
   * @param file the file
   * @param testProg the test prog
   */
  public static void evaluateRecommendation(SearchForCodeLocationsParameter parameterObject,
      String file, ArrayList<ArrayList<Recommendation>> testProg) {
    outer: for (ArrayList<Recommendation> rec : testProg) {
      if (rec != null && rec.size() > 0) {
        List<AresRecommendation> recommendations = new LinkedList<>();
        ReadableEncodedScript foundScript = null;
        AbstractBastNode foundMethodBlock = null;

        for (ReadableEncodedScript script : parameterObject.members) {
          if (script.script.getPair().repository.equals(parameterObject.repositoryName)) {
            if (file.equals(script.script.getPair().fileName)) {
              SignaturePrinter sigPrinter = new SignaturePrinter();
              rec.get(0).method.accept(sigPrinter);
              String recSignature = sigPrinter.buffer.toString();
              String scriptSignature = parameterObject.methodSignatureMap.get(script);
              if (recSignature != null && scriptSignature != null) {
                if (recSignature.equals(scriptSignature)) {
                  if (parameterObject.foundInputs.contains(script)) {
                    continue outer;
                  } else {
                    parameterObject.foundInputs.add(script);
                    foundScript = script;
                    foundMethodBlock = parameterObject.methodBlockMap.get(script);
                    break;
                  }
                }
              }
            }
          }
        }
        for (Recommendation recResult : rec) {
          if (recResult != null && recResult.method != null) {
            FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(BastBlock.TAG);
            recResult.method.accept(fnft);
            IPrettyPrinter printer = ParserFactory.getPrettyPrinter();
            fnft.nodes.get(0).accept(printer);
            double accuracyTokens = -1;
            double accuracyCharacters = -1;
            if (foundMethodBlock != null) {
              accuracyTokens = LevenshteinTokenDistance.match(fnft.nodes.get(0), foundMethodBlock);
              accuracyCharacters =
                  LevenshteinDistance.levenshteinMatch(fnft.nodes.get(0), foundMethodBlock);
            }
            AresRecommendation newRec = new AresRecommendation(printer.getBuffer().toString(),
                accuracyTokens, accuracyCharacters);
            recommendations.add(newRec);
          }
        }
        String id = null;
        if (foundScript != null) {
          id = foundScript.script.getPair().id;
        }
        AresRecommendationSet set = new AresRecommendationSet(parameterObject.repositoryName,
            parameterObject.oldestId, file, rec.get(0).methodNumber, recommendations, id);
        parameterObject.recommendationSets.add(set);
      }
    }
  }

  /**
   * Compute recommendation result.
   *
   * @param parameterObject the parameter object
   * @param name the name
   * @param members the members
   * @param workDir the work dir
   * @return the recommendation result
   */
  public static RecommendationResult computeRecommendationResult(
      SearchForCodeLocationsParameter parameterObject, String name,
      List<ReadableEncodedScript> members, File workDir) {
    final double precision =
        ((double) parameterObject.foundInputs.size()) / parameterObject.recommendationSets.size();
    final double recall = ((double) parameterObject.foundInputs.size()) / members.size();
    final LinkedList<Double> tokenAccListMin = new LinkedList<>();
    final LinkedList<Double> charAccListMin = new LinkedList<>();
    final LinkedList<Double> tokenAccListMax = new LinkedList<>();
    final LinkedList<Double> charAccListMax = new LinkedList<>();
    for (AresRecommendationSet set : parameterObject.recommendationSets) {
      if (set.memberId == null) {
        continue;
      }
      double minToken = Double.MAX_VALUE;
      double minChar = Double.MAX_VALUE;
      for (AresRecommendation rec : set.recommendations) {
        minToken = Math.min(minToken, rec.accuracyTokens);
        minChar = Math.min(minChar, rec.accuracyCharacters);
      }
      if (minToken != Double.MAX_VALUE && minChar != Double.MAX_VALUE) {
        tokenAccListMin.add(minToken);
        charAccListMin.add(minChar);
      }
      double maxToken = Double.MIN_VALUE;
      double maxChar = Double.MIN_VALUE;
      for (AresRecommendation rec : set.recommendations) {
        maxToken = Math.max(maxToken, rec.accuracyTokens);
        maxChar = Math.max(maxChar, rec.accuracyCharacters);
      }
      if (maxToken != Double.MIN_VALUE && maxChar != Double.MIN_VALUE) {
        tokenAccListMax.add(maxToken);
        charAccListMax.add(maxChar);
      }
    }
    assert (tokenAccListMin.size() == parameterObject.foundInputs.size());
    assert (charAccListMin.size() == parameterObject.foundInputs.size());
    assert (tokenAccListMax.size() == parameterObject.foundInputs.size());
    assert (charAccListMax.size() == parameterObject.foundInputs.size());
    double accuracyTokensMin = -1;
    double accuracyCharactersMin = -1;
    double accuracyTokensMax = -1;
    double accuracyCharactersMax = -1;
    if (tokenAccListMin.size() > 0) {
      accuracyTokensMin = 0;
      accuracyCharactersMin = 0;
      for (double val : tokenAccListMin) {
        accuracyTokensMin += val;
      }
      for (double val : charAccListMin) {
        accuracyCharactersMin += val;
      }
      accuracyTokensMin = accuracyTokensMin / parameterObject.foundInputs.size();
      accuracyCharactersMin = accuracyCharactersMin / parameterObject.foundInputs.size();
    }
    if (tokenAccListMax.size() > 0) {
      accuracyTokensMax = 0;
      accuracyCharactersMax = 0;
      for (double val : tokenAccListMax) {
        accuracyTokensMax += val;
      }
      for (double val : charAccListMax) {
        accuracyCharactersMax += val;
      }
      accuracyTokensMax = accuracyTokensMax / parameterObject.foundInputs.size();
      accuracyCharactersMax = accuracyCharactersMax / parameterObject.foundInputs.size();
    }
    AresSearchTime searchTime = parameterObject.searchTime;

    if (searchTime != null) {
      for (long time : parameterObject.measurement.timeRecommendationCreation) {
        searchTime.createRecommendation.addAndGet(time);
      }
      for (long time : parameterObject.measurement.timeTreeDifferencing) {
        searchTime.treeDifferencing.addAndGet(time);
      }
    }
    final File genOriginalFile = new File(workDir, "Pattern_original.java");
    final File genModifiedFile = new File(workDir, "Pattern_modified.java");
    final String patternOriginal = SharedMethods.getMethodText(genOriginalFile);

    final String patternModified = SharedMethods.getMethodText(genModifiedFile);

    RecommendationResult result = new RecommendationResult(name, members, parameterObject.inputs,
        parameterObject.recommendationSets, parameterObject.foundInputs.size(),
        parameterObject.recommendationSets.size(), precision, recall, accuracyTokensMin,
        accuracyCharactersMin, accuracyTokensMax, accuracyCharactersMax, patternOriginal,
        patternModified, parameterObject.createTime,
        searchTime == null ? 0 : searchTime.completeSearch.get());
    return result;
  }

  private static void printHeader() {
    System.out.format("%22s || ", "");
    System.out.format("%46s || ", "LASE");
    System.out.format("%46s || ", "ARES - Two Input Changes");
    System.out.format("%46s || ", "ARES - All Changes");
    System.out.println();
    System.out.format("%3s || ", "In");
    System.out.format("%8s || ", "Bug Id");
    System.out.format("%3s || ", "m");
    System.out.format("%3s || ", "F");
    System.out.format("%3s || ", "C");
    System.out.format("%7s || ", "A_T");
    System.out.format("%7s || ", "A_C");
    System.out.format("%3s || ", "P");
    System.out.format("%3s || ", "R");
    System.out.format("%3s || ", "F");
    System.out.format("%3s || ", "C");
    System.out.format("%7s || ", "A_T");
    System.out.format("%7s || ", "A_C");
    System.out.format("%3s || ", "P");
    System.out.format("%3s || ", "R");
    System.out.format("%3s || ", "F");
    System.out.format("%3s || ", "C");
    System.out.format("%7s || ", "A_T");
    System.out.format("%7s || ", "A_C");
    System.out.format("%3s || ", "P");
    System.out.format("%3s || ", "R");
    System.out.println();
  }

  /**
   * Prints the row beginning.
   *
   * @param laseData the lase data
   * @param index the index
   */
  public static void printRowBeginning(RecommendationFile laseData, int index) {
    printIntValue(index + 1);
    System.out.format("%8s || ", laseData.results.get(index).name);
    printIntValue(laseData.results.get(index).allMembers.size());
  }

  /**
   * Prints the data set row.
   *
   * @param laseData the lase data
   * @param index the index
   */
  public static void printDataSetRow(RecommendationFile laseData, int index) {
    printIntValue(laseData.results.get(index).numberOfRecommendationSets);
    printIntValue(laseData.results.get(index).foundMembers);
    printAccuracyValue(laseData.results.get(index).accuracyTokensMin,
        laseData.results.get(index).accuracyTokensMax);
    printAccuracyValue(laseData.results.get(index).accuracyCharactersMin,
        laseData.results.get(index).accuracyCharactersMax);
    printDoubleValue(laseData.results.get(index).precision);
    printDoubleValue(laseData.results.get(index).recall);
  }

  public static void printIntValue(int value) {
    System.out.format("%3d || ", value);
  }

  public static void printDoubleValue(double value) {
    System.out.format("%3d || ", Math.round(value * 100));
  }

  /**
   * Prints the accuracy value.
   *
   * @param valueMin the value min
   * @param valueMax the value max
   */
  public static void printAccuracyValue(double valueMin, double valueMax) {
    if (valueMax == valueMin) {
      System.out.format("    %3d || ", Math.round(valueMax * 100));
    } else {
      System.out.format("%3d/%3d || ", Math.round(valueMin * 100), Math.round(valueMax * 100));
    }
  }

  /**
   * Prints the table.
   *
   * @param laseData the lase data
   * @param aresTwoInputsData the ares two inputs data
   * @param aresAllInputsData the ares all inputs data
   */
  public static void printTable(RecommendationFile laseData, RecommendationFile aresTwoInputsData,
      RecommendationFile aresAllInputsData) {
    printHeader();
    for (int i = 0; i < laseData.results.size(); i++) {
      printRowBeginning(laseData, i);
      printDataSetRow(laseData, i);
      printDataSetRow(aresTwoInputsData, i);
      printDataSetRow(aresAllInputsData, i);
      System.out.println();
    }
    System.out.println();
  }

  /**
   * Prints the junit values.
   *
   * @param aresAccuracyChars the ares accuracy chars
   * @param aresAccuracyTokens the ares accuracy tokens
   * @param prefix the prefix
   */
  public static void printJunitValues(LinkedList<Double> aresAccuracyChars,
      LinkedList<Double> aresAccuracyTokens, String prefix) {
    Collections.sort(aresAccuracyChars);
    Collections.sort(aresAccuracyTokens);
    final double aresAccTokenMedian = aresAccuracyTokens.size() % 2 == 0
        ? (aresAccuracyTokens.get(aresAccuracyTokens.size() / 2)
            + aresAccuracyTokens.get(aresAccuracyTokens.size() / 2 + 1)) / 2
        : aresAccuracyTokens.get(aresAccuracyTokens.size() / 2 + 1);

    double sum = 0;
    for (double d : aresAccuracyTokens) {
      sum += d;
    }
    double aresAccTokenMean = sum / aresAccuracyTokens.size();
    System.out.println(prefix + " A_T (Mean): " + +Math.round(aresAccTokenMean * 100));
    System.out.println(prefix + " A_T (Median): " + Math.round(aresAccTokenMedian * 100));

    final double aresAccCharMedian = aresAccuracyChars.size() % 2 == 0
        ? (aresAccuracyChars.get(aresAccuracyChars.size() / 2)
            + aresAccuracyChars.get(aresAccuracyChars.size() / 2 + 1)) / 2
        : aresAccuracyChars.get(aresAccuracyChars.size() / 2 + 1);

    sum = 0;

    for (double d : aresAccuracyChars) {
      sum += d;
    }

    double aresAccCharMean = sum / aresAccuracyChars.size();
    System.out.println(prefix + " A_C (Mean): " + +Math.round(aresAccCharMean * 100));
    System.out.println(prefix + " A_C (Median): " + +Math.round(aresAccCharMedian * 100));

  }

  /**
   * Gets the recommendation results.
   *
   * @param path the path
   * @return the recommendation results
   */
  public static List<RecommendationResult> readRecommendationResultsFromFile(String path) {
    File resultFile = new File(path);
    if (resultFile.exists()) {
      try {
        ObjectMapper aresMapper = AresMapper.createJsonMapper();
        RecommendationFile aresFile = aresMapper.readValue(resultFile, RecommendationFile.class);
        return aresFile.results;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  /**
   * Prints the time data.
   *
   * @param results the results
   */
  public static void printTimeData(final List<RecommendationResult> results) {
    ArrayList<Long> valuesCreate = new ArrayList<>();
    ArrayList<Long> valuesUse = new ArrayList<>();

    for (RecommendationResult result : results) {
      valuesCreate.add(result.patternCreationTimeInNanoSec);
      valuesUse.add(result.patternUseTimeInNanoSec);
    }
    Collections.sort(valuesUse);
    System.out.println("Create: ");
    printBoxPlotValues(valuesCreate);
    System.out.println("Use: ");
    printBoxPlotValues(valuesUse);
  }

  private static void printBoxPlotValues(ArrayList<Long> valuesCreate) {
    Collections.sort(valuesCreate);
    System.out.format("Max: %f\n",
        ((double) valuesCreate.get(valuesCreate.size() - 1)) / Statistics.NANO);
    System.out.format("Upper quartile: %f\n",
        ((double) Statistics.getUpperQuartile(valuesCreate)) / Statistics.NANO);
    System.out.format("Median: %f\n",
        ((double) Statistics.getMedian(valuesCreate)) / Statistics.NANO);
    System.out.format("Lower quartile: %f\n",
        ((double) Statistics.getLowerQuartile(valuesCreate)) / Statistics.NANO);
    System.out.format("min: %f\n", ((double) valuesCreate.get(0)) / Statistics.NANO);
    System.out.format("mean: %f\n", ((double) Statistics.getMean(valuesCreate)) / Statistics.NANO);
  }

  /**
   * Prints the junit results.
   *
   * @param inputPath the input path
   * @param aresResultPath the ares result path
   * @param laseResultPath the lase result path
   */
  public static void printJunitResults(String inputPath, String aresResultPath,
      String laseResultPath) {
    printJunitInputSize(inputPath);

    HashMap<String, HashMap<String, LinkedList<Double>>> foundInputsMap = new HashMap<>();

    addAresAccuracyValues(foundInputsMap, aresResultPath);
    addLaseAccuracyValues(foundInputsMap, laseResultPath);

    LinkedList<Double> aresAccuracyCharsSharedMin = new LinkedList<>();
    LinkedList<Double> aresAccuracyTokensSharedMin = new LinkedList<>();
    LinkedList<Double> aresAccuracyCharsSharedMax = new LinkedList<>();
    LinkedList<Double> aresAccuracyTokensSharedMax = new LinkedList<>();
    LinkedList<Double> laseAccuracyCharsShared = new LinkedList<>();
    LinkedList<Double> laseAccuracyTokensShared = new LinkedList<>();
    int shared = 0;
    for (HashMap<String, LinkedList<Double>> inputsToGroup : foundInputsMap.values()) {
      for (LinkedList<Double> list : inputsToGroup.values()) {
        if (list.size() == 6) {
          aresAccuracyCharsSharedMax.add(list.get(0));
          aresAccuracyTokensSharedMax.add(list.get(1));
          aresAccuracyCharsSharedMin.add(list.get(2));
          aresAccuracyTokensSharedMin.add(list.get(3));
          laseAccuracyCharsShared.add(list.get(4));
          laseAccuracyTokensShared.add(list.get(5));
          shared++;
        }
      }
    }
    System.out.println("Shared Recommendations: " + shared);


    SharedMethods.printJunitValues(aresAccuracyCharsSharedMax, aresAccuracyTokensSharedMax,
        "Ares Shared Recommendations Max");

    SharedMethods.printJunitValues(aresAccuracyCharsSharedMin, aresAccuracyTokensSharedMin,
        "Ares Shared Recommendations Min");

    SharedMethods.printJunitValues(laseAccuracyCharsShared, laseAccuracyTokensShared,
        "Lase Shared Recommendations");
  }

  private static void addLaseAccuracyValues(
      HashMap<String, HashMap<String, LinkedList<Double>>> foundInputsMap, String laseResultPath) {
    List<RecommendationResult> laseResults =
        SharedMethods.readRecommendationResultsFromFile(laseResultPath);
    for (RecommendationResult result : laseResults) {
      if (result.recommendationSets != null) {
        for (AresRecommendationSet set : result.recommendationSets) {
          double accuracyChar = Double.MIN_VALUE;
          double accuracyToken = Double.MIN_VALUE;
          boolean found = false;
          for (AresRecommendation rec : set.recommendations) {
            if (rec.accuracyCharacters != -1) {
              found = true;
              accuracyChar = Math.max(accuracyChar, rec.accuracyCharacters);
              accuracyToken = Math.max(accuracyToken, rec.accuracyTokens);
            }
          }
          if (found) {
            HashMap<String, LinkedList<Double>> inputsToGroup = foundInputsMap.get(result.name);
            if (inputsToGroup == null) {
              inputsToGroup = new HashMap<>();
              foundInputsMap.put(result.name, inputsToGroup);
            }
            LinkedList<Double> list = inputsToGroup.get(set.memberId);
            if (list == null) {
              list = new LinkedList<>();
              inputsToGroup.put(set.memberId, list);
            }
            if (list.size() == 4) {
              list.add(accuracyChar);
              list.add(accuracyToken);
            }
          }
        }
      }
    }
  }

  private static void addAresAccuracyValues(
      HashMap<String, HashMap<String, LinkedList<Double>>> foundInputsMap, String aresResultPath) {
    List<RecommendationResult> aresResults =
        SharedMethods.readRecommendationResultsFromFile(aresResultPath);

    for (RecommendationResult result : aresResults) {
      for (AresRecommendationSet set : result.recommendationSets) {
        double accuracyCharMax = Double.MIN_VALUE;
        double accuracyTokenMax = Double.MIN_VALUE;
        double accuracyCharMin = Double.MAX_VALUE;
        double accuracyTokenMin = Double.MAX_VALUE;
        boolean found = false;
        for (AresRecommendation rec : set.recommendations) {
          if (rec.accuracyCharacters != -1) {
            found = true;
            accuracyCharMax = Math.max(accuracyCharMax, rec.accuracyCharacters);
            accuracyTokenMax = Math.max(accuracyTokenMax, rec.accuracyTokens);
            accuracyCharMin = Math.min(accuracyCharMin, rec.accuracyCharacters);
            accuracyTokenMin = Math.min(accuracyTokenMin, rec.accuracyTokens);
          }
        }
        if (found) {
          HashMap<String, LinkedList<Double>> inputsToGroup = foundInputsMap.get(result.name);
          if (inputsToGroup == null) {
            inputsToGroup = new HashMap<>();
            foundInputsMap.put(result.name, inputsToGroup);
          }
          LinkedList<Double> list = inputsToGroup.get(set.memberId);
          if (list == null) {
            list = new LinkedList<>();
            inputsToGroup.put(set.memberId, list);
          }
          if (list.size() == 0) {
            list.add(accuracyCharMax);
            list.add(accuracyTokenMax);
            list.add(accuracyCharMin);
            list.add(accuracyTokenMin);
          }
        }
      }
    }
  }

  private static void printJunitInputSize(String inputPath) {
    ObjectMapper dataMapper = DataBind.createMapper();
    try {
      DataSet groupFileObject = dataMapper.readValue(new File(inputPath), DataSet.class);
      System.out.println("Groups of Code Changes: " + groupFileObject.clusters.size());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Run simple recommendation.
   *
   * @param seq the seq
   * @param par the par
   * @param test the test
   * @param verbose the verbose
   */
  public static void runSimpleRecommendation(File seq, File par, File test, File result,
      boolean verbose) {


    BastProgram beforeProg = RecommendationCreator.getAst(seq, ParserType.JAVA_PARSER);

    BastProgram afterProg = RecommendationCreator.getAst(par, ParserType.JAVA_PARSER);



    BastBlock blockOriginal = (BastBlock) ((BastFunction) ((BastClassDecl) beforeProg.functionBlocks
        .getFirst()).declarations.getFirst()).statements.getFirst();
    BastBlock blockModified = (BastBlock) ((BastFunction) ((BastClassDecl) afterProg.functionBlocks
        .getFirst()).declarations.getFirst()).statements.getFirst();


    TokenAndHistory tokenOriginal = getToken(blockOriginal);
    TokenAndHistory tokenModified = getToken(blockModified);
    FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(AresBlock.TAG);
    blockOriginal.accept(fnft);
    if (fnft.nodes.size() == 0) {
      AresBlock aresBlockBefore = CreateJavaNodeHelper.createAresBlock(tokenOriginal, 0, true,
          blockOriginal.statements, null);
      AresBlock aresBlockAfter = CreateJavaNodeHelper.createAresBlock(tokenModified, 0, false,
          blockModified.statements, null);
      LinkedList<AbstractBastStatement> listBefore = new LinkedList<>();
      listBefore.add(aresBlockBefore);
      LinkedList<AbstractBastStatement> listAfter = new LinkedList<>();
      listAfter.add(aresBlockAfter);
      blockOriginal.replaceField(BastFieldConstants.BLOCK_STATEMENT, new BastField(listBefore));
      blockModified.replaceField(BastFieldConstants.BLOCK_STATEMENT, new BastField(listAfter));
    }

    BastProgram testP = AnalysePath.analyse(test, ParserType.JAVA_PARSER);
    IPrettyPrinter printer = ParserFactory.getPrettyPrinter();
    testP.accept(printer);
    final byte[] testBytes = (printer.getBuffer().toString().getBytes());

    IPrettyPrinter debugPrinter = ParserFactory.getAresPrettyPrinter();
    beforeProg.accept(debugPrinter);
    final byte[] seqBytes = (debugPrinter.getBuffer().toString().getBytes());
    if (verbose) {
      System.out.println(debugPrinter.getBuffer().toString());
    }
    debugPrinter.getBuffer().setLength(0);
    afterProg.accept(debugPrinter);
    if (verbose) {
      System.out.println(debugPrinter.getBuffer().toString());
    }
    byte[] parBytes = (debugPrinter.getBuffer().toString().getBytes());

    BastProgram seqProg = AnalysePath.analyse(seq, ParserType.JAVA_PARSER);
    BastProgram parProg = AnalysePath.analyse(par, ParserType.JAVA_PARSER);
    ExtendedAresPattern template =
        ExtendedTemplateExtractor.extractTemplate(seqProg, parProg, null, null);
    template.resolveWildcards();

    ArrayList<ArrayList<Recommendation>> testProgs = RecommendationCreator.createTransformedProgram(
        test.toString(), seqBytes, parBytes, testBytes, verbose, null, null, template);
    if (testProgs == null) {
      return;
    }
    if (result == par) {
      afterProg = RecommendationCreator.getAst(result, ParserType.JAVA_PARSER);
    } else {
      afterProg = null;
    }
    debugPrinter.getBuffer().setLength(0);
    BastBlock afterBlock = null;
    if (afterProg != null && afterProg.functionBlocks != null) {
      afterBlock = (BastBlock) ((BastFunction) ((BastClassDecl) afterProg.functionBlocks
          .getFirst()).declarations.getFirst()).statements.getFirst();
      afterBlock.accept(debugPrinter);
    }

    int counter = 0;
    StringBuffer resultBuffer = new StringBuffer();
    counter = createRecommendationOutput(verbose, debugPrinter, testProgs, counter, resultBuffer);
    if (result == null) {
      System.out.println(resultBuffer.toString());
    } else {
      if (!par.getAbsolutePath().equals(result.getAbsolutePath())) {
        FileUtils.writeStringToFile(new File(result.getAbsoluteFile() + ".pretty"),
            resultBuffer.toString());
      }
    }
  }

  private static TokenAndHistory getToken(BastBlock blockBefore) {
    TokenAndHistory tokenOriginal = null;
    if (blockBefore.statements != null && blockBefore.statements.size() > 0
        && blockBefore.statements.getFirst().info != null
        && blockBefore.statements.getFirst().info.tokens != null
        && blockBefore.statements.getFirst().info.tokens[0].token
            .getTag() == TagConstants.JAVA_TOKEN) {
      tokenOriginal = blockBefore.statements.getFirst().info.tokens[0];
    }
    return tokenOriginal;
  }

  private static int createRecommendationOutput(boolean verbose, IPrettyPrinter debugPrinter,
      ArrayList<ArrayList<Recommendation>> testProgs, int counter, StringBuffer resultBuffer) {
    IPrettyPrinter print;
    for (ArrayList<Recommendation> testProg : testProgs) {
      for (Recommendation res : testProg) {
        resultBuffer.append("RECOMMENDATION " + counter + "\n\n");
        print = ParserFactory.getAresPrettyPrinter();
        final BastProgram resultAst = (BastProgram) res.resultAst;
        final LinkedList<AbstractBastExternalDecl> functionBlocks = resultAst.functionBlocks;
        BastBlock resultBlock =
            (BastBlock) ((BastFunction) ((BastClassDecl) functionBlocks
                .getFirst()).declarations.getFirst()).statements.getFirst();
        resultBlock.accept(print);
        IPrettyPrinter testPrinter = ParserFactory.getAresPrettyPrinter();
        res.resultAst.accept(testPrinter);
        String tmp = testPrinter.getBuffer().toString();
        if (verbose) {
          System.out.println(tmp);
        }
        counter++;
        IPrettyPrinter resultPrinter = ParserFactory.getAresPrettyPrinter();
        res.resultAst.accept(resultPrinter);
        resultBuffer.append(resultPrinter.getBuffer());
        resultBuffer.append("\n\n");
      }
    }
    return counter;
  }

}
