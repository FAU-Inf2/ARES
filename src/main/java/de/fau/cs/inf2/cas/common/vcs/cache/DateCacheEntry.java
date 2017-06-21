package de.fau.cs.inf2.cas.common.vcs.cache;

import java.util.Date;
import java.util.HashMap;

public class DateCacheEntry {
  String repository;
  HashMap<String, Date> map;
  
  DateCacheEntry(String repository, HashMap<String, Date> map) {
    this.repository = repository;
    this.map = map;
  }
}
