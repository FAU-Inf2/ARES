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

package de.fau.cs.inf2.cas.common.vcs.cache;

import de.fau.cs.inf2.cas.common.util.ComparePair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

public class FileCacheMap {
  String repositoryKey;
  ArrayList<FileCacheEntry> fileList;
  
  /**
   * Instantiates a new date cache.
   *
   * @param repositoryMap the repository map
   */
  FileCacheMap(String key, HashMap<String, ArrayList<ComparePair<String>>> repositoryMap) {
    this.repositoryKey = key;
    fileList = new ArrayList<>(repositoryMap.entrySet().size());
    for (Entry<String, ArrayList<ComparePair<String>>> entry : repositoryMap.entrySet()) {
      ArrayList<FileCachePair> list = new ArrayList<>(entry.getValue().size());
      for (ComparePair<String> pair : entry.getValue()) {
        list.add(new FileCachePair(pair.getOldElement(), pair.getOldElement()));
      }
      fileList.add(new FileCacheEntry(entry.getKey(), list));
    }
  }
  
  public FileCacheMap(String repositoryKey, ArrayList<FileCacheEntry> repositoryList) {
    this.repositoryKey = repositoryKey;
    this.fileList = repositoryList;
  }
  
}
