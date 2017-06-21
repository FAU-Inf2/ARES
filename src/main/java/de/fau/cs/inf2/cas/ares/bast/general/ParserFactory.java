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
