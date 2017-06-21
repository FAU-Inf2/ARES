package de.fau.cs.inf2.cas.ares;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.fau.cs.inf2.cas.ares.io.AresMapper;
import de.fau.cs.inf2.cas.ares.io.RecommendationFile;
import de.fau.cs.inf2.cas.ares.pipeline.SharedMethods;

import java.io.File;
import java.io.IOException;


public class ReadEvaluationResults {
  public static final String LASE_DATASET_LASE_RESULTS =
      "data/lase_evaluation/lase_result.json";
  public static final String LASE_DATASET_ARES_TWO_INPUTS_RESULTS =
      "data/lase_evaluation/ares_two_inputs_result.json";
  public static final String LASE_DATASET_ARES_ALL_INPUTS_RESULTS =
      "data/lase_evaluation/ares_all_inputs_result.json";
  public static final String JUNIT_DATASET_LASE_ALL_INPUTS_RESULTS =
      "data/junit_evaluation/lase_junit_results.json";
  public static final String JUNIT_DATASET_ARES_ALL_INPUTS_RESULTS =
      "data/junit_evaluation/ares_junit_results.json";
  public static final String JUNIT_INPUT_DEFINITION =
      "data/junit_evaluation/input.json";

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
    final ObjectMapper aresMapper = AresMapper.createJsonMapper();
    RecommendationFile laseData = null;
    RecommendationFile aresTwoInputsData = null;
    RecommendationFile aresAllInputsData = null;

    try {
      laseData = aresMapper.readValue(laseDataFile, RecommendationFile.class);
      aresTwoInputsData = aresMapper.readValue(aresTwoInputsDataFile, RecommendationFile.class);
      aresAllInputsData = aresMapper.readValue(aresAllInputsDataFile, RecommendationFile.class);
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

  }



}
