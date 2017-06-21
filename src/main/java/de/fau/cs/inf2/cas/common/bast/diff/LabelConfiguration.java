package de.fau.cs.inf2.cas.common.bast.diff;

import java.util.HashSet;

public class LabelConfiguration {
  public final int identifierLabel;
  public final int rootLabel;
  public final int classLabel;
  public final int basicTypeLabel;
  public final int qualifierLabel;

  public HashSet<Integer> labelsForValueCompare;
  public HashSet<Integer> labelsForRealCompare;
  public HashSet<Integer> labelsForIntCompare;
  public HashSet<Integer> labelsForStringCompare;
  public HashSet<Integer> labelsForBoolCompare;

  /**
   * Instantiates a new label configuration.
   *
   * @param labelsForValueCompare the labels for value compare
   */
  public LabelConfiguration(HashSet<Integer> labelsForValueCompare) {
    this(-1, -1, -1, -1, -1, labelsForValueCompare, new HashSet<Integer>(), new HashSet<Integer>(),
        new HashSet<Integer>(), new HashSet<Integer>());

  }

  /**
   * Instantiates a new label configuration.
   *
   * @param identifierLabel the identifier label
   * @param rootLabel the root label
   * @param classLabel the class label
   * @param basicTypeLabel the basic type label
   * @param qualifierLabel the qualifier label
   * @param labelsForValueCompare the labels for value compare
   * @param labelsForRealCompare the labels for real compare
   * @param labelsForIntCompare the labels for int compare
   * @param labelsForStringCompare the labels for string compare
   * @param labelsForBoolCompare the labels for bool compare
   */
  public LabelConfiguration(int identifierLabel, int rootLabel, int classLabel, int basicTypeLabel,
      int qualifierLabel, HashSet<Integer> labelsForValueCompare,
      HashSet<Integer> labelsForRealCompare, HashSet<Integer> labelsForIntCompare,
      HashSet<Integer> labelsForStringCompare, HashSet<Integer> labelsForBoolCompare) {
    this.identifierLabel = identifierLabel;
    this.rootLabel = rootLabel;
    this.classLabel = classLabel;
    this.labelsForValueCompare = labelsForValueCompare;
    this.labelsForBoolCompare = labelsForBoolCompare;
    this.labelsForRealCompare = labelsForRealCompare;
    this.labelsForIntCompare = labelsForIntCompare;
    this.labelsForStringCompare = labelsForStringCompare;
    this.labelsForBoolCompare = labelsForBoolCompare;
    this.basicTypeLabel = basicTypeLabel;
    this.qualifierLabel = qualifierLabel;
  }
}
