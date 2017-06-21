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

package de.fau.cs.inf2.cas.ares.pcreation.rulesystem;

import de.fau.cs.inf2.cas.ares.pcreation.GeneralizationParameter;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Interface Rule.
 */
public interface FilterRule {

  /**
   * Execute rule.
   *
   * @return the list
   */
  public List<BastEditOperation> executeRule(List<BastEditOperation> worklist);

  /**
   * Gets the rule name.
   *
   * @return the rule name
   */
  public String getRuleName();

  public FilterRule initRule(HashMap<AbstractBastNode, AbstractBastNode> delInsertMap,
      ArrayList<BastEditOperation> alignList, HashMap<String, String> variableMap,
      GeneralizationParameter parameter);

}
