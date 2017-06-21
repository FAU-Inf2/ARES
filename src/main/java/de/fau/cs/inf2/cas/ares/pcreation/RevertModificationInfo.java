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

package de.fau.cs.inf2.cas.ares.pcreation;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.cas.common.parser.IGeneralToken;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

class RevertModificationInfo {
  public BastFieldConstants fieldConstant;
  public BastField field = null;
  public AbstractBastNode parent = null;
  private HashMap<AbstractBastNode, LinkedList<IGeneralToken>> updateTokens = new HashMap<>();

  /**
   * Instantiates a new revert modification info.
   */
  public RevertModificationInfo() {}

  /**
   * Revert.
   */
  public void revert() {
    if (parent != null && field != null && fieldConstant != null) {
      parent.replaceField(fieldConstant, field);
      for (Entry<AbstractBastNode, LinkedList<IGeneralToken>> tokenEntry : updateTokens
          .entrySet()) {
        tokenEntry.getKey().info.tokens[0].prevTokens.clear();
        tokenEntry.getKey().info.tokens[0].prevTokens.addAll(tokenEntry.getValue());
      }
    }
  }

  /**
   * Update tokens.
   *
   * @param abstractBastExpr the abstract bast expr
   * @param prevTokens the prev tokens
   */
  public void updateTokens(AbstractBastExpr abstractBastExpr,
      LinkedList<IGeneralToken> prevTokens) {
    LinkedList<IGeneralToken> list = new LinkedList<IGeneralToken>();
    list.addAll(prevTokens);
    updateTokens.put(abstractBastExpr, list);

  }
}
