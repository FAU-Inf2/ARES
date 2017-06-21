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

package de.fau.cs.inf2.cas.ares.io;

import java.util.List;

public class AresRecommendationSet {

  public String repository;
  public String commitId;
  public String fileName;
  public int methodNumber = -1;
  public List<AresRecommendation> recommendations;
  public String memberId;
  
  /**
   * Instantiates a new ares recommendation set.
   *
   * @param repository the repository
   * @param commitId the commit id
   * @param fileName the file name
   * @param methodNumber the method number
   * @param recommendations the recommendations
   */
  public AresRecommendationSet(String repository, String commitId, String fileName,
      int methodNumber, List<AresRecommendation> recommendations, String memberId) {
    super();
    this.repository = repository;
    this.commitId = commitId;
    this.fileName = fileName;
    this.methodNumber = methodNumber;
    this.recommendations = recommendations;
    this.memberId = memberId;
  }
  
}
