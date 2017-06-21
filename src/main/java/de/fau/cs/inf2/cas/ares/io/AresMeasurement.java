package de.fau.cs.inf2.cas.ares.io;

import java.util.concurrent.ConcurrentSkipListSet;

public class AresMeasurement {
  public ConcurrentSkipListSet<Long> timeTreeDifferencing = new ConcurrentSkipListSet<>();
  public ConcurrentSkipListSet<Long> timeRecommendationCreation = new ConcurrentSkipListSet<>();
  public ConcurrentSkipListSet<Long> timeOrderCreation = new ConcurrentSkipListSet<>();

  
  public AresMeasurement() {
    
  }
  
}
