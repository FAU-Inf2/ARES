package de.fau.cs.inf2.cas.common.vcs;

import de.fau.cs.inf2.cas.common.bast.nodes.BastParameterList;

public class ExtractScript {

  public final String repUri;
  public final String commitOriginal;
  public final String commitModified;
  public final String pathOriginal;
  public final String pathModified;
  public final String methodNameOriginal;
  public final String methodNameModified;
  public final String innerClassOriginal;
  public final String innerClassModified;
  final BastParameterList parametersOriginal;
  final BastParameterList parametersModified;
  final String rawParametersOriginal;
  final String rawParametersModified;
  final boolean inGroup;

  /**
   * Instantiates a new extract script.
   *
   */
  @SuppressWarnings("PMD.ExcessiveParameterList")
  public ExtractScript(String repUri, String commitOriginal, String commitModified,
      String pathOriginal, String pathModified, String innerClassOriginal,
      String innerClassModified, String methodNameOriginal, String methodNameModified,
      BastParameterList parametersOriginal, BastParameterList parametersModified, boolean inGroup) {
    this(repUri, commitOriginal, commitModified, pathOriginal, pathModified, innerClassOriginal,
        innerClassModified, methodNameOriginal, methodNameModified, parametersOriginal,
        parametersModified, inGroup, null, null);
  }

  @SuppressWarnings("PMD.ExcessiveParameterList")
  ExtractScript(String repUri, String commitOriginal, String commitModified, String pathOriginal,
      String pathModified, String innerClassOriginal, String innerClassModified,
      String methodNameOriginal, String methodNameModified, BastParameterList parametersOriginal,
      BastParameterList parametersModified, boolean inGroup, String rawParametersOriginal,
      String rawParametersModified) {
    this.repUri = repUri;
    this.commitOriginal = commitOriginal;
    this.commitModified = commitModified;
    this.pathOriginal = pathOriginal;
    this.pathModified = pathModified;
    this.methodNameOriginal = methodNameOriginal;
    this.methodNameModified = methodNameModified;
    this.parametersOriginal = parametersOriginal;
    this.parametersModified = parametersModified;
    this.inGroup = inGroup;
    this.innerClassOriginal = innerClassOriginal;
    this.innerClassModified = innerClassModified;
    this.rawParametersOriginal = rawParametersOriginal;
    this.rawParametersModified = rawParametersModified;
  }

}
