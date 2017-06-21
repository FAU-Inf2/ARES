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

import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.ExecutionRunType;

import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;

import java.util.List;
import java.util.Map;

public class GeneralizationParameter {
  public ExtendedDiffResult exDiffOo = null;

  ExtendedDiffResult exDiffOoReversed = null;
  public ExtendedDiffResult exDiffMm = null;
  ExtendedDiffResult exDiffMmReversed = null;
  public ExtendedDiffResult exDiffOM1 = null;
  public ExtendedDiffResult exDiffOM2 = null;
  public List<BastEditOperation> original1original2 = null;
  public List<BastEditOperation> modified1modified2 = null;
  List<BastEditOperation> original1modified1 = null;

  public Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyM1 = null;
  public Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyM2 = null;
  public Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyO1 = null;
  public Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyO2 = null;
  boolean[] insideMethodOM1 = null;
  boolean[] insideMethodOM2 = null;

  public boolean[] insideMethodOo = null;
  public boolean[] insideMethodMm = null;

  public boolean[] ignoreOpOo = null;
  public boolean[] ignoreOpMm = null;
  public NodeParentInformationHierarchy parentOM1 = null;
  public NodeParentInformationHierarchy parentOM2 = null;
  NodeParentInformationHierarchy parentOo = null;
  NodeParentInformationHierarchy parentMm = null;
  public NodeParentInformationHierarchy parentMO1 = null;
  public NodeParentInformationHierarchy parentMO2 = null;

  @SuppressWarnings("ucd")
  public MatchBoundary matchBoundaryOo = null;
  public MatchBoundary matchBoundaryMm = null;
  public ExecutionRunType runType = ExecutionRunType.ORIGINAL_RUN;
}
