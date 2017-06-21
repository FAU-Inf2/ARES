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

import de.fau.cs.inf2.cas.ares.io.AresSearchTime;

import de.fau.cs.inf2.cas.common.io.ReadableEncodedGroup;
import de.fau.cs.inf2.cas.common.vcs.base.VcsRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class LaseEvaluationGroupParameters {
  public ReadableEncodedGroup group;
  public HashMap<String, HashMap<String, Date>> dateMap;
  public HashMap<String, VcsRepository> repoMap;
  public File tmpDir;
  public boolean allPatterns;
  public AresSearchTime searchTime;
  public ArrayList<String> filteredFiles;
  
  /**
   * Instantiates a new lase evaluation group parameters.
   *
   * @param group the group
   * @param dateMap the date map
   * @param repoMap the repo map
   * @param tmpDir the tmp dir
   * @param allPatterns the all patterns
   * @param searchTime the search time
   */
  public LaseEvaluationGroupParameters(ReadableEncodedGroup group,
      HashMap<String, HashMap<String, Date>> dateMap, HashMap<String, VcsRepository> repoMap,
      File tmpDir, boolean allPatterns, AresSearchTime searchTime) {
    this.group = group;
    this.dateMap = dateMap;
    this.repoMap = repoMap;
    this.tmpDir = tmpDir;
    this.allPatterns = allPatterns;
    this.searchTime = searchTime;
  }
  
  public void setFilteredFiles(ArrayList<String> filteredFiles) {
    this.filteredFiles = filteredFiles;
  }
}