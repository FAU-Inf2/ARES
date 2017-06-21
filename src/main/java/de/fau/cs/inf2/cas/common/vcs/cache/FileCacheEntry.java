package de.fau.cs.inf2.cas.common.vcs.cache;

import java.util.ArrayList;


public class FileCacheEntry {
  String filename;
  public ArrayList<FileCachePair> list;
  
  FileCacheEntry(String filename, ArrayList<FileCachePair> list) {
    this.filename = filename;
    this.list = list;
  }
}
