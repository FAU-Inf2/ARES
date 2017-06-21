package de.fau.cs.inf2.cas.common.io.extern;

import de.fau.cs.inf2.cas.common.io.CommitPairIdentifier;

public class LaseEvalOutput {
  public LaseOutput laseOutput;
  public LaseEvalOutputTypeEnum type;
  public CommitPairIdentifier foundChange;
  public double tokenSim;
  public double stringSim;
    
  /**
   * Instantiates a new lase output.
   *
   * @param laseOutput the laseOutput
   * @param type the type
   * @param tokenSim the method tokenSim
   * @param stringSim the stringSim
   */
  public LaseEvalOutput(LaseOutput laseOutput, LaseEvalOutputTypeEnum type,
      CommitPairIdentifier foundChange, double tokenSim, double stringSim) {
    this.laseOutput = laseOutput;
    this.type = type;
    this.foundChange = foundChange;
    this.tokenSim = tokenSim;
    this.stringSim = stringSim;
  }
}
