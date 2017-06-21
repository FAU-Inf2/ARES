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
