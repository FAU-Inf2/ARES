package de.fau.cs.inf2.cas.common.parser;

public enum ParserType {
  C_PARSER, JAVA_PARSER, FORTRAN_PARSER;

  /**
   * Extension_to_type.
   *
   * @param string the string
   * @return the parser type
   */
  public static ParserType extension_to_type(String string) {
    if (string.endsWith(".c")) {
      return C_PARSER;
    }
    if (string.endsWith(".java")) {
      return JAVA_PARSER;
    }
    if (string.endsWith(".f")) {
      return FORTRAN_PARSER;
    }
    return null;
  }
}
