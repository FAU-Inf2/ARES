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
