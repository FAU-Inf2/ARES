package de.fau.cs.inf2.cas.common.io.extern;

public class LaseOutput {
  public String classname;
  public String relativePath;
  public String methodSignature;
  public int startPosition;
  public int length;
  public String newMethodString;
  /**
   * Instantiates a new lase output.
   *
   * @param classname the classname
   * @param relativePath the relativePath
   * @param methodSignature the method signature
   * @param startPosition the start position
   * @param length the length
   * @param newMethodString the new method string
   */
  
  public LaseOutput(String classname, String relativePath, String methodSignature,
      int startPosition, int length, String newMethodString) {
    this.classname = classname;
    this.relativePath = relativePath;
    this.methodSignature = methodSignature;
    this.startPosition = startPosition;
    this.length = length;
    this.newMethodString = newMethodString;
  }

}
