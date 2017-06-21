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

package de.fau.cs.inf2.cas.ares.bast.general;

import de.fau.cs.inf2.cas.ares.bast.visitors.AresEnlightenedJavaPrinter;
import de.fau.cs.inf2.cas.ares.parser.odin.AresJavaParser;

import de.fau.cs.inf2.cas.common.bast.visitors.EnlightenedJavaPrinter;
import de.fau.cs.inf2.cas.common.bast.visitors.IPrettyPrinter;

import de.fau.cs.inf2.cas.common.parser.AresExtension;
import de.fau.cs.inf2.cas.common.parser.IParser;
import de.fau.cs.inf2.cas.common.parser.ParserType;
import de.fau.cs.inf2.cas.common.parser.odin.JavaParser;

public class ParserFactory {

  /**
   * Gets the pretty printer.
   *
   * @return the pretty printer
   */
  public static IPrettyPrinter getPrettyPrinter(ParserType type) {
    switch (type) {
      case JAVA_PARSER:
        return new EnlightenedJavaPrinter();
      default:
        assert false;
        return null;
    }
  }
  
  /**
   * Gets the pretty printer.
   *
   * @return the pretty printer
   */
  public static IPrettyPrinter getPrettyPrinter() {
    return getPrettyPrinter(ParserType.JAVA_PARSER);
  }
  
  /**
   * Gets the ares pretty printer.
   *
   * @return the ares pretty printer
   */
  public static IPrettyPrinter getAresPrettyPrinter(ParserType type) {
    switch (type) {
      case JAVA_PARSER:
        return new AresEnlightenedJavaPrinter();
      default:
        assert false;
        return null;
    }
  }
  
  /**
   * Gets the ares pretty printer.
   *
   * @return the ares pretty printer
   */
  public static IPrettyPrinter getAresPrettyPrinter() {
    return getAresPrettyPrinter(ParserType.JAVA_PARSER);
  }
  
  /**
   * Gets the parser instance.
   *
   * @return the parser instance
   */
  public static IParser getParserInstance(AresExtension extension) {
    return getParserInstance(ParserType.JAVA_PARSER, extension);
  }
  
  /**
   * Gets the parser instance.
   *
   * @return the parser instance
   */
  public static IParser getParserInstance(ParserType type, AresExtension extension) {
    switch (type) {
      case JAVA_PARSER:
        if (extension == AresExtension.WITH_ARES_EXTENSIONS) {
          return AresJavaParser.getInstance();
        } else {
          return JavaParser.getInstance();
        }
      default:
        assert false;
        return null;
    }
  }
}
