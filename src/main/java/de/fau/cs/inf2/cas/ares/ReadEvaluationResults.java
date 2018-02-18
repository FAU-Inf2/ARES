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

import com.fasterxml.jackson.databind.ObjectMapper;

import de.fau.cs.inf2.cas.ares.io.AresMapper;
import de.fau.cs.inf2.cas.ares.io.RecommendationFile;
import de.fau.cs.inf2.cas.ares.pipeline.SharedMethods;

import java.io.File;
import java.io.IOException;


public class ReadEvaluationResults {
  public static final String LASE_DATASET_LASE_RESULTS = "data/lase_evaluation/lase_result.json";
  public static final String LASE_DATASET_ARES_TWO_INPUTS_RESULTS =
      "data/lase_evaluation/ares_two_inputs_result.json";
  public static final String LASE_DATASET_ARES_ALL_INPUTS_RESULTS =
      "data/lase_evaluation/ares_all_inputs_result.json";
  public static final String LASE_DATASET_ARES_TWO_INPUTS_RESULTS_PHDTHESIS =
      "data/lase_evaluation_phdthesis/ares_two_inputs_result.json";
  public static final String LASE_DATASET_ARES_ALL_INPUTS_RESULTS_PHDTHESIS =
      "data/lase_evaluation_phdthesis/ares_all_inputs_result.json";
  public static final String JUNIT_DATASET_LASE_ALL_INPUTS_RESULTS =
      "data/junit_evaluation/lase_junit_results.json";
  public static final String JUNIT_DATASET_ARES_ALL_INPUTS_RESULTS =
      "data/junit_evaluation/ares_junit_results.json";
  public static final String JUNIT_INPUT_DEFINITION = "data/junit_evaluation/input.json";

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    printLaseDatasetResults();
    SharedMethods.printJunitResults(JUNIT_INPUT_DEFINITION, JUNIT_DATASET_ARES_ALL_INPUTS_RESULTS,
        JUNIT_DATASET_LASE_ALL_INPUTS_RESULTS);
  }

  private static void printLaseDatasetResults() {
    File laseDataFile = new File(LASE_DATASET_LASE_RESULTS);
    File aresTwoInputsDataFile = new File(LASE_DATASET_ARES_TWO_INPUTS_RESULTS);
    File aresAllInputsDataFile = new File(LASE_DATASET_ARES_ALL_INPUTS_RESULTS);
    File aresTwoInputsDataFilePhdThesis = new File(LASE_DATASET_ARES_TWO_INPUTS_RESULTS_PHDTHESIS);
    File aresAllInputsDataFilePhdThesis = new File(LASE_DATASET_ARES_ALL_INPUTS_RESULTS_PHDTHESIS);
    final ObjectMapper aresMapper = AresMapper.createJsonMapper();
    RecommendationFile laseData = null;
    RecommendationFile aresTwoInputsData = null;
    RecommendationFile aresAllInputsData = null;
    RecommendationFile aresTwoInputsDataPhdThesis = null;
    RecommendationFile aresAllInputsDataPhdThesis = null;
    try {
      laseData = aresMapper.readValue(laseDataFile, RecommendationFile.class);
      aresTwoInputsData = aresMapper.readValue(aresTwoInputsDataFile, RecommendationFile.class);
      aresAllInputsData = aresMapper.readValue(aresAllInputsDataFile, RecommendationFile.class);
      aresTwoInputsDataPhdThesis =
          aresMapper.readValue(aresTwoInputsDataFilePhdThesis, RecommendationFile.class);
      aresAllInputsDataPhdThesis =
          aresMapper.readValue(aresAllInputsDataFilePhdThesis, RecommendationFile.class);
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
    SharedMethods.printTable(laseData, aresTwoInputsData, aresAllInputsData);
    System.out.println("\n############################################\n");
    System.out.println("Time LASE: ");
    SharedMethods.printTimeData(laseData.results);
    System.out.println();
    System.out.println("Time ARES - Two Input Changes: ");
    SharedMethods.printTimeData(aresTwoInputsData.results);
    System.out.println("\n############################################\n");
    System.out.println("Phd Thesis Results:");
    SharedMethods.printTable(laseData, aresTwoInputsDataPhdThesis, aresAllInputsDataPhdThesis);
    System.out.println("\n############################################\n");
    System.out.println("Time LASE: ");
    SharedMethods.printTimeData(laseData.results);
    System.out.println();
    System.out.println("Time ARES - Two Input Changes: ");
    SharedMethods.printTimeData(aresTwoInputsDataPhdThesis.results);
    System.out.println();
    System.out.println("Time ARES - All Input Changes: ");
    SharedMethods.printTimeData(aresAllInputsDataPhdThesis.results);
    System.out.println("\n############################################\n");
  }



}
