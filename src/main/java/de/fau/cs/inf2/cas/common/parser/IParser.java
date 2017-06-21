package de.fau.cs.inf2.cas.common.parser;

import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;

import java.io.File;

public interface IParser {
  
  /**
   * Parses the.
   *
   * @param data the data
   * @return the bast program
   */
  public BastProgram parse(byte[] data);

  /**
   * Parses the.
   *
   * @param file the file
   * @return the bast program
   */
  public BastProgram parse(File file);
}
