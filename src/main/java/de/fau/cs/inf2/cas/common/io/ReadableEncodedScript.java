package de.fau.cs.inf2.cas.common.io;

public class ReadableEncodedScript {
  public String methodOriginal;
  public String methodModified; 
  public String diff;
  public String commitMsg;
  public EncodedScript script;
  
  /**
   * Instantiates a new readable encoded script.
   *
   * @param diff the diff
   * @param commitMsg the commit msg
   * @param script the script
   */
  public ReadableEncodedScript(String methodOriginal, String methodModified, String diff,
      String commitMsg, EncodedScript script) {
    this.methodOriginal = methodOriginal;
    this.methodModified = methodModified;
    this.diff = diff;
    this.commitMsg = commitMsg;
    this.script = script;
  }
}
