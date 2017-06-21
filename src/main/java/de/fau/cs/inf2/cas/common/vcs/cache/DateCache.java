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
