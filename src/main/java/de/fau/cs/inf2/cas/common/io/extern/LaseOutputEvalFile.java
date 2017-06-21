package de.fau.cs.inf2.cas.common.io.extern;

import de.fau.cs.inf2.cas.common.io.CommitPairIdentifier;

import java.util.List;

public class LaseOutputEvalFile {
  public List<CommitPairIdentifier> cpis;
  public List<LaseEvalOutput> outputEval;
  public int inputsFound;
  public int identicalChangesFound;
  
  /**
   * Instantiates a new lase output eval file.
   *
   * @param cpis the cpis
   * @param outputEval the output eval
   * @param inputsFound the inputs found
   * @param identicalChangesFound the identical changes found
   */
  public LaseOutputEvalFile(List<CommitPairIdentifier> cpis,
      List<LaseEvalOutput> outputEval, int inputsFound, int identicalChangesFound) {
    this.cpis = cpis;
    this.outputEval = outputEval;
    this.inputsFound = inputsFound;
    this.identicalChangesFound = identicalChangesFound;
  }
}
