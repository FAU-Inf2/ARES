package de.fau.cs.inf2.cas.common.io.extern;

public enum LaseEvalOutputTypeEnum {
  INPUT("INPUT"), IDENTICAL_FOUND("IDENTICAL_FOUND"),
  FOUND("FOUND"), NOT_FOUND("NOT_FOUND"), UNKNOWN("UNKNOWN");
  
  public final String type;
  
  private LaseEvalOutputTypeEnum(String type) {
    this.type = type;
  }
}
