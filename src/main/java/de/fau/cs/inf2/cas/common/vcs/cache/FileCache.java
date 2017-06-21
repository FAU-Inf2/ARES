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
