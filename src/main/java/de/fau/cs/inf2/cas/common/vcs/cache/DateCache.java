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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;

public class DateCache {
  private ArrayList<DateCacheEntry> repositoryList;
  
  /**
   * Instantiates a new date cache.
   *
   * @param repositoryMap the repository map
   */
  public DateCache(HashMap<String, HashMap<String,Date>> repositoryMap) {
    repositoryList = new ArrayList<>(repositoryMap.entrySet().size());
    for (Entry<String, HashMap<String,Date>> entry : repositoryMap.entrySet()) {
      repositoryList.add(new DateCacheEntry(entry.getKey(), entry.getValue()));
    }
  }
  
  public DateCache(ArrayList<DateCacheEntry> repositoryList) {
    this.repositoryList = repositoryList;
  }
  
  /**
   * Gets the repository map.
   *
   * @return the repository map
   */
  public HashMap<String, HashMap<String,Date>> getRepositoryMap() {
    HashMap<String, HashMap<String,Date>> repositoryMap = new HashMap<>();
    for (DateCacheEntry entry : repositoryList) {
      repositoryMap.put(entry.repository, entry.map);
    }
    return repositoryMap;
  }
}
