package de.fau.cs.inf2.cas.common.parser.odin;

class CharElement {
  public final int endPos;
  final char character;
  private final String tokenText;

  CharElement(int endPos, char character, String tokenText) {
    this.endPos = endPos;
    this.character = character;
    this.tokenText = tokenText;
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    return tokenText;
  }
}
