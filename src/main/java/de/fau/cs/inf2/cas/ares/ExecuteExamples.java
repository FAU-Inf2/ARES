package de.fau.cs.inf2.cas.ares;

import de.fau.cs.inf2.cas.ares.pcreation.GeneralizeGroup;
import de.fau.cs.inf2.cas.ares.pipeline.SharedMethods;

import de.fau.cs.inf2.cas.common.util.FileUtils;

import java.io.File;

public class ExecuteExamples {
  public static final String EXAMPLE_ONE = "data/examples/one/";
  public static final String EXAMPLE_TWO = "data/examples/two/";

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    executeExample(EXAMPLE_ONE);
    System.out.println("\n###########################################\n");
    executeExample(EXAMPLE_TWO);

  }

  private static void executeExample(String path) {
    GeneralizeGroup group = new GeneralizeGroup(path, false);
    group.run();
    File original = new File(path, "Pattern_original.java.pretty");
    File modified = new File(path, "Pattern_modified.java.pretty");
    File test = new File(path, "Test.java");
    File result = new File(path, "Expected_result.java");
    SharedMethods.runSimpleRecommendation(original, modified, test, result, false);
    String resBefore = FileUtils.getFileData(path + "/Expected_result.java.pretty");
    System.out.println(resBefore);
  }
}
