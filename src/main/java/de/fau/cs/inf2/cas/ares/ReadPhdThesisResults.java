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

package de.fau.cs.inf2.cas.ares;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.fau.cs.inf2.cas.ares.io.AresMapper;
import de.fau.cs.inf2.cas.ares.io.AresOnCthreeResult;
import de.fau.cs.inf2.cas.ares.io.AresOnCthreeResultFile;
import de.fau.cs.inf2.cas.ares.io.AresRecommendation;
import de.fau.cs.inf2.cas.ares.io.AresRecommendationSet;
import de.fau.cs.inf2.cas.ares.pipeline.SharedMethods;

import de.fau.cs.inf2.cthree.data.Algorithm;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ReadPhdThesisResults {

  public static final int LH_INDEX = 0;
  public static final int LD_INDEX = 1;
  public static final int TH_INDEX = 2;
  public static final int TD_INDEX = 3;
  public static final String DATA_PATH = "data/cthree_evaluation_phdthesis";

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    File inputDir = new File(DATA_PATH);
    print(inputDir);
  }


  private static void print(File inputDir) {
    final ObjectMapper aresMapper = AresMapper.createJsonMapper();
    int[][] usefulGroups = new int[4][10];

    final int unchangedIndex = 0;
    final int minAccuracyIndex = 1;
    final int maxAccuracyIndex = 2;
    int[] sumGroups = new int[4];
    int[] sumGroupMembers = new int[4];
    int[] sumType1CloneGroups = new int[4];
    int[] sumType1CloneMembers = new int[4];
    int[] sumType2CloneGroups = new int[4];
    int[] sumType2CloneMembers = new int[4];
    int[] sumOtherGroups = new int[4];
    int[] sumOtherMembers = new int[4];
    int[] sumCloneGroups = new int[4];
    int[] sumCloneMembers = new int[4];
    int[] sumUsefulGroups = new int[4];
    int[] sumUsefulMembers = new int[4];
    int[] sumUsefulGroupsSize2 = new int[4];
    int[] sumUsefulMembersSize2 = new int[4];
    String[] accuracyNames = new String[] { "UNCHANGED", "ARES (MIN)", "ARES (MAX)" };
    int[][] accuracy = new int[3][11];
    @SuppressWarnings("unchecked")
    ArrayList<Integer>[] memberSizes = new ArrayList[4];
    for (int i = 0; i < memberSizes.length; i++) {
      memberSizes[i] = new ArrayList<>();
    }
    HashMap<String, ArrayList<Double>> accuracyMap = new HashMap<>();
    for (int i = 0; i < accuracyNames.length; i++) {
      accuracyMap.put(accuracyNames[i], new ArrayList<>());
    }
    File[] files = inputDir.listFiles();
    if (files == null) {
      System.exit(-1);
    }
    for (File file : files) {
      if (file.exists() && file.getAbsolutePath().endsWith(".json")) {
        AresOnCthreeResultFile aresOnCthreeResultFile = null;
        try {
          aresOnCthreeResultFile = aresMapper.readValue(file, AresOnCthreeResultFile.class);
        } catch (JsonParseException e) {
          e.printStackTrace();
        } catch (JsonMappingException e) {
          e.printStackTrace();
        } catch (IOException e) {
          e.printStackTrace();
        }
        if (aresOnCthreeResultFile == null) {
          System.exit(-1);
        }
        for (AresOnCthreeResult res : aresOnCthreeResultFile.results) {
          switch (res.cloneType) {
            case OTHER:
              for (Algorithm alg : res.cluster.detectedBy) {
                int algorithmIndex = getAlgorithmIndex(alg);
                sumGroups[algorithmIndex]++;
                sumGroupMembers[algorithmIndex] += res.cluster.members.length;
                sumCloneGroups[algorithmIndex]++;
                sumCloneMembers[algorithmIndex] += res.cluster.members.length;
                memberSizes[algorithmIndex].add(res.cluster.members.length);
                sumOtherGroups[algorithmIndex]++;
                sumOtherMembers[algorithmIndex] += res.cluster.members.length;
              }
              if (res.recommendationResult != null) {
                countAresGroups(usefulGroups, unchangedIndex, minAccuracyIndex, maxAccuracyIndex,
                    sumUsefulGroups, sumUsefulMembers, sumUsefulGroupsSize2, sumUsefulMembersSize2,
                    accuracyNames, accuracy, accuracyMap, res);
              }
              break;
            case TYPE_ONE:
              for (Algorithm alg : res.cluster.detectedBy) {
                int algorithmIndex = getAlgorithmIndex(alg);
                sumGroups[algorithmIndex]++;
                sumGroupMembers[algorithmIndex] += res.cluster.members.length;
                sumCloneGroups[algorithmIndex]++;
                sumCloneMembers[algorithmIndex] += res.cluster.members.length;
                memberSizes[algorithmIndex].add(res.cluster.members.length);
                sumType1CloneGroups[algorithmIndex]++;
                sumType1CloneMembers[algorithmIndex] += res.cluster.members.length;
              }
              break;
            case TYPE_TWO:
              for (Algorithm alg : res.cluster.detectedBy) {
                int algorithmIndex = getAlgorithmIndex(alg);
                sumGroups[algorithmIndex]++;
                sumGroupMembers[algorithmIndex] += res.cluster.members.length;
                sumCloneGroups[algorithmIndex]++;
                sumCloneMembers[algorithmIndex] += res.cluster.members.length;
                memberSizes[algorithmIndex].add(res.cluster.members.length);
                sumType2CloneGroups[algorithmIndex]++;
                sumType2CloneMembers[algorithmIndex] += res.cluster.members.length;
              }
              break;
            default:
              break;
          }
        }
      }
    }

    final String[] configNames = new String[] { "LH", "LD", "TH", "TD" };
    printTable(sumGroups, sumGroupMembers, sumType1CloneGroups, sumType1CloneMembers,
        sumType2CloneGroups, sumType2CloneMembers, sumOtherGroups, sumOtherMembers, sumUsefulGroups,
        sumUsefulMembers, sumUsefulGroupsSize2, sumUsefulMembersSize2);

    printGroupDistribution(usefulGroups, configNames);
    printAccuracyDistribution(accuracyNames, accuracy);
    printAccuracyStatistics(accuracyNames, accuracyMap);
  }

  private static void printTable(int[] sumGroups, int[] sumGroupMembers, int[] sumType1CloneGroups,
      int[] sumType1CloneMembers, int[] sumType2CloneGroups, int[] sumType2CloneMembers,
      int[] sumOtherGroups, int[] sumOtherMembers, int[] sumUsefulGroups, int[] sumUsefulMembers,
      int[] sumUsefulGroupsSize2, int[] sumUsefulMembersSize2) {
    System.out.format(" %22s |", "");
    System.out.format("%7s |", "LH");
    System.out.format("%7s |", "LD");
    System.out.format("%7s |", "TH");
    System.out.format("%7s |", "TD");
    System.out.println();
    printRow(sumGroups, "Groups");
    printRow(sumGroupMembers, "...Code Changes");
    printRowPercent(sumGroupMembers, "...Code Changes (%)");
    printMean(sumGroups, sumGroupMembers, "Mean Number of Members");

    printRow(sumType1CloneGroups, "Type-1 Clone Groups");
    printRow(sumType1CloneMembers, "...Code Changes");
    printRowPercent(sumType1CloneMembers, "...Code Changes (%)");
    printRow(sumType2CloneGroups, "Type-2 Clone Groups");
    printRow(sumType2CloneMembers, "...Code Changes");
    printRowPercent(sumType2CloneMembers, "...Code Changes (%)");
    printRow(sumOtherGroups, "Other Groups");
    printRow(sumOtherMembers, "...Code Changes");
    printRowPercent(sumOtherMembers, "...Code Changes (%)");
    printRow(sumUsefulGroups, "Useful Groups");
    printRow(sumUsefulMembers, "...Code Changes");
    printRowPercent(sumUsefulMembers, "...Code Changes (%)");
    printRow(sumUsefulGroupsSize2, "Useful Groups (Size 2)");
    printRow(sumUsefulMembersSize2, "...Code Changes");
    printRowPercent(sumUsefulMembersSize2, "...Code Changes (%)");
  }

  private static void printGroupDistribution(int[][] usefulGroups, String[] configNames) {
    System.out.println("Distribution of useful groups:");
    for (int i = 0; i < usefulGroups.length; i++) {
      System.out.println(configNames[i] + ": ");
      for (int j = 0; j < usefulGroups[i].length - 1; j++) {
        System.out.println("Size " + (j + 2) + ": " + usefulGroups[i][j]);
      }
      System.out.println();
    }
  }

  private static void printAccuracyDistribution(String[] accuracyNames, int[][] accuracy) {
    System.out.println("Accuracy distribution:");
    for (int i = 0; i < accuracy.length; i++) {
      System.out.println(accuracyNames[i] + ": ");
      for (int j = 0; j < accuracy[i].length; j++) {
        System.out
            .println("Interval [" + (j * 10) + "; " + ((j + 1) * 10) + "[: " + accuracy[i][j]);
      }
      System.out.println();
    }
  }

  private static void printAccuracyStatistics(String[] accuracyNames,
      HashMap<String, ArrayList<Double>> accuracyMap) {
    HashMap<String, Double> medianMap = new HashMap<>();
    HashMap<String, Double> upperQuartileMap = new HashMap<>();
    HashMap<String, Double> lowerQuartileMap = new HashMap<>();
    HashMap<String, Double> upperWhiskerMap = new HashMap<>();
    HashMap<String, Double> lowerWhiskerMap = new HashMap<>();
    HashMap<String, Double> avgMap = new HashMap<>();

    for (String step : accuracyNames) {
      medianMap.put(step, -1d);
      upperQuartileMap.put(step, -1d);
      lowerQuartileMap.put(step, -1d);
      upperWhiskerMap.put(step, -1d);
      lowerWhiskerMap.put(step, -1d);
      avgMap.put(step, -1d);
    }
    for (String type : accuracyNames) {
      ArrayList<Double> values = accuracyMap.get(type);
      System.out.println(type + ":");
      SharedMethods.printBoxPlotValuesDouble(values);

    }
  }

  private static void printRow(int[] sumGroups, String rowType) {
    System.out.format(" %22s | ", rowType);
    for (int i = 0; i < 4; i++) {
      System.out.format("%7s |", sumGroups[i]);
    }
    System.out.println();
  }

  private static void printRowPercent(int[] sumGroups, String rowType) {
    DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
    otherSymbols.setDecimalSeparator('.');
    otherSymbols.setGroupingSeparator(',');
    DecimalFormat myFormat = new DecimalFormat("00", otherSymbols);
    System.out.format(" %22s | ", rowType);
    for (int i = 0; i < 4; i++) {
      System.out.format("%7s |", myFormat.format((double) sumGroups[i] / 311267 * 100));
    }
    System.out.println();
  }

  private static void printMean(int[] sumGroups, int[] sumMembers, String rowType) {
    DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
    otherSymbols.setDecimalSeparator('.');
    otherSymbols.setGroupingSeparator(',');
    DecimalFormat myFormat = new DecimalFormat("##.#", otherSymbols);
    System.out.format(" %22s | ", rowType);
    for (int i = 0; i < 4; i++) {
      final double mean = (double) sumMembers[i] / sumGroups[i];
      System.out.format("%7s |", myFormat.format(mean));
    }
    System.out.println();
  }

  private static void countAresGroups(int[][] usefulGroups, final int unchangedIndex,
      final int minAccuracyIndex, final int maxAccuracyIndex, int[] sumUsefulGroups,
      int[] sumUsefulMembers, int[] sumUsefulGroupsSize2, int[] sumUsefulMembersSize2,
      String[] accuracyNames, int[][] accuracy, HashMap<String, ArrayList<Double>> accuracyMap,
      AresOnCthreeResult res) {
    int sizeIndex = -1;
    switch (res.recommendationResult.allMembers.size()) {
      case 1:
        assert (false);
        break;
      case 2:
        sizeIndex = res.recommendationResult.allMembers.size() - 2;
        for (Algorithm alg : res.cluster.detectedBy) {
          int algorithmIndex = getAlgorithmIndex(alg);
          sumUsefulGroupsSize2[algorithmIndex]++;
          sumUsefulMembersSize2[algorithmIndex] += res.cluster.members.length;
        }
        break;
      case 3:
      case 4:
      case 5:
      case 6:
      case 7:
      case 8:
      case 9:
        sizeIndex = res.recommendationResult.allMembers.size() - 2;
        break;
      default:
        sizeIndex = 8;
    }

    for (Algorithm alg : res.cluster.detectedBy) {
      int algorithmIndex = getAlgorithmIndex(alg);
      usefulGroups[algorithmIndex][sizeIndex]++;
      sumUsefulGroups[algorithmIndex]++;
      sumUsefulMembers[algorithmIndex] += res.cluster.members.length;
    }

    for (AresRecommendationSet set : res.recommendationResult.recommendationSets) {
      if (set.memberId == null) {
        continue;
      }
      double min = Double.MAX_VALUE;
      double max = Double.MIN_VALUE;
      for (AresRecommendation recommendation : set.recommendations) {
        assert (recommendation.accuracyTokens > 0);
        min = Math.min(min, recommendation.accuracyTokens);
        max = Math.max(max, recommendation.accuracyTokens);

      }
      accuracyMap.get(accuracyNames[minAccuracyIndex]).add(min);
      accuracyMap.get(accuracyNames[maxAccuracyIndex]).add(max);
      int minIndex = ((int) (min * 10));
      int maxIndex = ((int) (max * 10));
      accuracy[minAccuracyIndex][minIndex]++;
      accuracy[maxAccuracyIndex][maxIndex]++;
    }
    accuracyMap.get(accuracyNames[unchangedIndex]).addAll(res.unchangedAccuracyTokens);
    for (double d : res.unchangedAccuracyTokens) {
      int index = ((int) (d * 10));
      accuracy[unchangedIndex][index]++;
    }
  }

  private static int getAlgorithmIndex(Algorithm alg) {
    int algorithmIndex = -1;
    switch (alg) {
      case AST_DBSCAN:
        algorithmIndex = TD_INDEX;
        break;
      case AST_HIERARCHICAL:
        algorithmIndex = TH_INDEX;
        break;
      case DIFF_DBSCAN:
        algorithmIndex = LD_INDEX;
        break;
      case DIFF_HIERARCHICAL:
        algorithmIndex = LH_INDEX;
        break;
      default:
        break;

    }
    return algorithmIndex;
  }

}
