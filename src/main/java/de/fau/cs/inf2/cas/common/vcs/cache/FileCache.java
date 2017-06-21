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

public class FileCache {
  private ArrayList<FileCacheMap> repositoryList;
  
  /**
   * Instantiates a new date cache.
   *
   * @param repositoryMap the repository map
   */
  public FileCache(HashMap<String, HashMap<String, ArrayList<ComparePair<String>>>> repositoryMap) {
    
    repositoryList = new ArrayList<>(repositoryMap.entrySet().size());
    for (Entry<String, HashMap<String, ArrayList<ComparePair<String>>>> entry 
        : repositoryMap.entrySet()) {
      repositoryList.add(new FileCacheMap(entry.getKey(), entry.getValue()));
    }
  }
  
  public FileCache(ArrayList<FileCacheMap> repositoryList) {
    this.repositoryList = repositoryList;
  }
  
  /**
   * Gets the repository map.
   *
   * @return the repository map
   */
  public HashMap<String, HashMap<String, ArrayList<ComparePair<String>>>> getRepositoryMap() {
    HashMap<String, HashMap<String, ArrayList<ComparePair<String>>>> 
        repositoryMap = new HashMap<>();
    for (FileCacheMap entry : repositoryList) {
      HashMap<String, ArrayList<ComparePair<String>>> fileMap = new HashMap<>();
      for (FileCacheEntry fileEntry : entry.fileList) {
        ArrayList<ComparePair<String>> list = new ArrayList<>(fileEntry.list.size());
        for (FileCachePair pair : fileEntry.list) {
          list.add(new ComparePair<String>(pair.first, pair.second));
        }
        fileMap.put(fileEntry.filename, list);
      }
      repositoryMap.put(entry.repositoryKey, fileMap);
    }
    return repositoryMap;
  }
}
