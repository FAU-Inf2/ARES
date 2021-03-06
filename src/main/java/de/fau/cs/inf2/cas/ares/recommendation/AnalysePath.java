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

package de.fau.cs.inf2.cas.ares.recommendation;

import de.fau.cs.inf2.cas.ares.bast.general.ParserFactory;

import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.visitors.IPrettyPrinter;

import de.fau.cs.inf2.cas.common.parser.AresExtension;
import de.fau.cs.inf2.cas.common.parser.ParserType;

import java.io.File;

/**
 * todo.
 *
 * <p>Calls the language dependent parsers - Dummy implementation
 * 
 */
public class AnalysePath {

  private static final boolean DEBUG = false;

  /**
   * Analyse.
   *
   * @param file the file
   * @param type the type
   * @return the bast program
   */
  public static BastProgram analyse(File file, ParserType type) {
    return analyse(file, type, AresExtension.WITH_ARES_EXTENSIONS);
  }
  
  private static BastProgram analyse(File file, ParserType type, AresExtension extension) {
    return analyse(file, type, false, extension);
  }

  static BastProgram analyse(File file, 
      ParserType type, boolean print, AresExtension extension) {
    assert type == ParserType.C_PARSER || type == ParserType.JAVA_PARSER;
    try {
      BastProgram program = ParserFactory.getParserInstance(extension).parse(file);
      IPrettyPrinter pretty = ParserFactory.getAresPrettyPrinter();
      program.accept(pretty);

      if (DEBUG) {
        if (print) {
          pretty.print(new File(file.getAbsolutePath() + ".pretty"));
        }
      }
      return program;
    } catch (Exception e) {
      if (DEBUG) {
        System.err.println("filename: " + file.getAbsolutePath());
      }
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Analyse.
   *
   * @param file the file
   * @param type the type
   * @param print the print
   * @return the bast program
   */
  public static BastProgram analyse(byte[] file, ParserType type, boolean print) {
    return analyse(file, type, print, AresExtension.WITH_ARES_EXTENSIONS);
  }

  /**
   * Analyse.
   *
   * @param file the file
   * @param type the type
   * @param print the print
   * @param extension the use ARES token
   * @return the bast program
   */
  public static BastProgram analyse(byte[] file, ParserType type, boolean print,
      AresExtension extension) {
    assert type == ParserType.C_PARSER || type == ParserType.JAVA_PARSER;
    try {
      BastProgram program = ParserFactory.getParserInstance(extension).parse(file);
      IPrettyPrinter pretty = ParserFactory.getAresPrettyPrinter();
      program.accept(pretty);
      return program;
    } catch (Exception e) {
      // do nothing
    }
    return null;
  }



}
