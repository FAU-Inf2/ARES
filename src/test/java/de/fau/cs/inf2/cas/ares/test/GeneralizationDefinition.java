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

package de.fau.cs.inf2.cas.ares.test;

import static org.junit.Assert.assertTrue;

import de.fau.cs.inf2.cas.ares.io.AresMeasurement;
import de.fau.cs.inf2.cas.ares.pcreation.GeneralizeGroup;

public class GeneralizationDefinition {
  private GeneralizeGroup group = null;

  /**
   * Instantiates a new generalization definition.
   *
   * @param path the path
   * @param patternsToUse the patterns to use
   */
  public GeneralizationDefinition(String path, String outputPath, int[] patternsToUse) {
    group = new GeneralizeGroup(path, outputPath, patternsToUse, null);

  }
  
  /**
   * Instantiates a new generalization definition.
   *
   * @param path the path
   * @param patternsToUse the patterns to use
   */
  public GeneralizationDefinition(String path, String outputPath, int[] patternsToUse,
      AresMeasurement measurement) {
    group = new GeneralizeGroup(path, outputPath, patternsToUse, measurement);

  }
  
  /**
   * Instantiates a new generalization definition.
   *
   * @param path the path
   * @param patternsToUse the patterns to use
   */
  public GeneralizationDefinition(String path, int[] patternsToUse) {
    group = new GeneralizeGroup(path, patternsToUse);

  }

  /**
   * Instantiates a new generalization definition.
   *
   * @param path the path
   * @param patternsToUse the patterns to use
   * @param reevaluateOrder the reevaluate order
   */
  public GeneralizationDefinition(String path, int[] patternsToUse, boolean reevaluateOrder) {
    this(path, false, patternsToUse, reevaluateOrder);
  }

  /**
   * Instantiates a new generalization definition.
   *
   * @param path the path
   * @param filteredPath the filtered path
   * @param patternsToUse the patterns to use
   * @param reevaluateOrder the reevaluate order
   */
  public GeneralizationDefinition(String path, boolean filteredPath, int[] patternsToUse,
      boolean reevaluateOrder) {
    group = new GeneralizeGroup(path, filteredPath, patternsToUse, reevaluateOrder);

  }

  /**
   * Instantiates a new generalization definition.
   *
   * @param path the path
   * @param filteredPath the filtered path
   * @param reevaluateOrder the reevaluate order
   */
  public GeneralizationDefinition(String path, boolean filteredPath, boolean reevaluateOrder) {
    group = new GeneralizeGroup(path, filteredPath, reevaluateOrder);
  }

  /**
   * Instantiates a new generalization definition.
   *
   * @param path the path
   * @param reevaluateOrder the reevaluate order
   */
  public GeneralizationDefinition(String path, boolean reevaluateOrder) {
    group = new GeneralizeGroup(path, reevaluateOrder);
  }
  
  /**
   * Instantiates a new generalization definition.
   *
   * @param path the path
   * @param reevaluateOrder the reevaluate order
   */
  public GeneralizationDefinition(String path, String outputPath, boolean reevaluateOrder) {
    group = new GeneralizeGroup(path, outputPath, reevaluateOrder);
  }

  /**
   * Run.
   */
  public void run() {
    //EmgIterationSettings settings = EmgIterationSettings.getInstance();
    //settings.setLeafThreshold(0.6f);
    group.run();
    if (group.failed) {
      assertTrue(false);
    }
  }

  /**
   * Generalized pattern before ref.
   *
   * @return the string
   */
  public String generalizedPatternOriginalRef() {
    return group.generalizedPatternOriginalRef;
  }

  /**
   * Generalized pattern before res.
   *
   * @return the string
   */
  public String generalizedPatternOriginalRes() {
    return group.generalizedPatternOriginalRes;
  }

  /**
   * Generalized pattern after ref.
   *
   * @return the string
   */
  public String generalizedPatternModifiedRef() {
    return group.generalizedPatternModifiedRef;
  }

  /**
   * Generalized pattern after res.
   *
   * @return the string
   */
  public String generalizedPatternModifiedRes() {
    return group.generalizedPatternModifiedRes;
  }
}
