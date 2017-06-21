package de.fau.cs.inf2.cas.common.io.extern;

import java.util.List;

public class LaseOutputFile {
  public List<LaseOutput> recommendations;
  
  public LaseOutputFile(List<LaseOutput> recommendations) {
    this.recommendations = recommendations;
  }
}
