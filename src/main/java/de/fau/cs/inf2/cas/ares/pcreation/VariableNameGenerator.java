package de.fau.cs.inf2.cas.ares.pcreation;

class VariableNameGenerator {
  private int number = -1;
  private final String prefixString = "ARES";

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    number++;
    return prefixString + number;
  }

}
