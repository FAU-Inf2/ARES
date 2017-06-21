/*
 * Copyright (c) 2017 Programming Systems Group, CS Department, FAU
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */

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
