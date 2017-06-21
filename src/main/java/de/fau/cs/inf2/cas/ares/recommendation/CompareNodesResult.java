package de.fau.cs.inf2.cas.ares.recommendation;

class CompareNodesResult {
  final int positionTemplate;
  static final int NEUTRAL = 0;
  static final int FAILED = 1;
  static final int SUCCESS = 2;
  static final int NO_WILDCARD = 3;
  static final int WILDCARD_ACCEPT = 4;
  static final int WILDCARD_FAIL = 5;
  static final int WILDCARD_UNFINISHED = 6;
  static final int WILDCARD_NO_MATCH = 7;

  CompareNodesResult(int positionTemplate, int positionProgram, int status, int wildcard) {
    this.positionTemplate = positionTemplate;
    this.positionProgram = positionProgram;
    this.status = status;
  }

  final int positionProgram;
  public final int status;

}
