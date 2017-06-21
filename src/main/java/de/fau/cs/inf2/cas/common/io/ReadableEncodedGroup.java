package de.fau.cs.inf2.cas.common.io;

import java.util.List;

public class ReadableEncodedGroup {
  public String name;
  public List<ReadableEncodedScript> scripts;
  
  public ReadableEncodedGroup(String name, List<ReadableEncodedScript> scripts) {
    this.name = name;
    this.scripts = scripts;
  }
}
