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

package de.fau.cs.inf2.mtdiff.editscript.operations.inode;

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.ComparePair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class INodeExtendedDiffResult {
  private List<INodeEditOperation> editScript;
  @SuppressWarnings("unused")
  private final Map<INode, INode> secondToFirstMap;
  private final Map<INode, INode> firstToSecondMap;
  private Map<INode, INodeEditOperation> editMapOld = new HashMap<INode, INodeEditOperation>();
  private Map<INode, INodeEditOperation> editMapNew = new HashMap<INode, INodeEditOperation>();


  /**
   * Instantiates a new i node extended diff result.
   *
   * @param editScript the edit script
   * @param secondToFirstMap the second to first map
   * @param firstToSecondMap the first to second map
   */
  public INodeExtendedDiffResult(List<INodeEditOperation> editScript,
      Map<INode, INode> secondToFirstMap, Map<INode, INode> firstToSecondMap) {
    super();
    this.setEditScript(editScript);
    this.secondToFirstMap = secondToFirstMap;
    this.firstToSecondMap = firstToSecondMap;
    for (INodeEditOperation ep : editScript) {
      editMapOld.put(ep.getOldOrInsertedNode(), ep);
    }
    for (INodeEditOperation ep : editScript) {
      editMapNew.put(ep.getNewOrChangedNode(), ep);
    }
  }

  /**
   * Gets the matched nodes.
   *
   * @return the matched nodes
   */
  public Set<ComparePair<INode>> getMatchedNodes() {
    HashSet<ComparePair<INode>> matchedNodes = new HashSet<>();
    for (Entry<INode, INode> e : getFirstToSecondMap().entrySet()) {
      matchedNodes.add(new ComparePair<INode>(e.getKey(), e.getValue()));
    }
    return matchedNodes;
  }

  public Map<INode, INode> getFirstToSecondMap() {
    return firstToSecondMap;
  }

  public List<INodeEditOperation> getEditScript() {
    return editScript;
  }

  private void setEditScript(List<INodeEditOperation> editScript) {
    this.editScript = editScript;
  }

}
