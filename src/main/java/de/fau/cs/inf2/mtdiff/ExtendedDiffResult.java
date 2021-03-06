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

package de.fau.cs.inf2.mtdiff;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.cas.common.util.ComparePair;

import de.fau.cs.inf2.mtdiff.editscript.operations.AlignOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.DeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.MoveOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.UpdateOperation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ExtendedDiffResult {
  public String filename;
  public List<BastEditOperation> editScript;
  public final Map<AbstractBastNode, AbstractBastNode> secondToFirstMap;
  public final Map<AbstractBastNode, AbstractBastNode> firstToSecondMap;
  public Map<AbstractBastNode, BastEditOperation> editMapOld =
      new HashMap<AbstractBastNode, BastEditOperation>();
  public Map<AbstractBastNode, BastEditOperation> editMapNew =
      new HashMap<AbstractBastNode, BastEditOperation>();

  /**
   * Instantiates a new extended diff result.
   *
   * @param editScript the edit script
   * @param secondToFirstMap the second to first map
   * @param firstToSecondMap the first to second map
   */
  public ExtendedDiffResult(String filename, List<BastEditOperation> editScript,
      Map<AbstractBastNode, AbstractBastNode> secondToFirstMap,
      Map<AbstractBastNode, AbstractBastNode> firstToSecondMap) {
    super();
    this.filename = filename;
    this.editScript = editScript;
    this.secondToFirstMap = secondToFirstMap;
    this.firstToSecondMap = firstToSecondMap;
    for (BastEditOperation ep : editScript) {
      editMapOld.put(ep.getOldOrInsertedNode(), ep);
    }
    for (BastEditOperation ep : editScript) {
      editMapNew.put(ep.getNewOrChangedNode(), ep);
    }
  }

  /**
   * Scans a modified edit script. Also adjusts the matching generated
   * by the instance of this class to
   * reflect the changes made to the edit script. This method does not consider advanced edit
   * operations. The maps generated by calls to {@see #getFirstToSecondMap()} and
   * {@see #getSecondToFirstMap()} are not updated automatically!
   *
   * @param editScript the edit script
   */
  public void update(final List<BastEditOperation> editScript) {
    final HashSet<AbstractBastNode> insertedNodeSet = new HashSet<>();
    final HashSet<AbstractBastNode> deletedNodeSet = new HashSet<>();

    for (final BastEditOperation op : editScript) {
      switch (op.getType()) {
        case INSERT:
          insertedNodeSet.add(((InsertOperation) op).getOldOrInsertedNode());
          break;

        case DELETE:
          deletedNodeSet.add(((DeleteOperation) op).getOldOrInsertedNode());
          break;

        case ALIGN:

          assert (((AlignOperation) op).getNewOrChangedNode() != null);
          assert (((AlignOperation) op).getOldOrInsertedNode() != null);
          if (((AlignOperation) op).getNewOrChangedNode().getTag() == ((AlignOperation) op)
              .getOldOrInsertedNode().getTag()) {
            secondToFirstMap.put((AbstractBastNode) ((AlignOperation) op).getNewOrChangedNode(),
                (AbstractBastNode) ((AlignOperation) op).getOldOrInsertedNode());

            firstToSecondMap.put((AbstractBastNode) ((AlignOperation) op).getOldOrInsertedNode(),
                (AbstractBastNode) ((AlignOperation) op).getNewOrChangedNode());
          }
          break;

        case MOVE:
          assert (((MoveOperation) op).getNewOrChangedNode() != null);
          assert (((MoveOperation) op).getOldOrInsertedNode() != null);

          secondToFirstMap.put((AbstractBastNode) ((MoveOperation) op).getNewOrChangedNode(),
              (AbstractBastNode) ((MoveOperation) op).getOldOrInsertedNode());

          firstToSecondMap.put((AbstractBastNode) ((MoveOperation) op).getOldOrInsertedNode(),
              (AbstractBastNode) ((MoveOperation) op).getNewOrChangedNode());
          break;

        case UPDATE:
          assert (((UpdateOperation) op).getNewOrChangedNode() != null);
          assert (((UpdateOperation) op).getOldOrInsertedNode() != null);
          if (((UpdateOperation) op).getNewOrChangedNode().getTag() == ((UpdateOperation) op)
              .getOldOrInsertedNode().getTag()) {

            secondToFirstMap.put((AbstractBastNode) ((UpdateOperation) op).getNewOrChangedNode(),
                (AbstractBastNode) ((UpdateOperation) op).getOldOrInsertedNode());

            firstToSecondMap.put((AbstractBastNode) ((UpdateOperation) op).getOldOrInsertedNode(),
                (AbstractBastNode) ((UpdateOperation) op).getNewOrChangedNode());
          }
          break;
        default:
          break;
      }
    }
    for (AbstractBastNode node : deletedNodeSet) {
      AbstractBastNode newNode = firstToSecondMap.get(node);
      if (newNode != null) {
        firstToSecondMap.remove(node);
        secondToFirstMap.remove(newNode);
      }
    }
    for (AbstractBastNode node : insertedNodeSet) {
      AbstractBastNode oldNode = secondToFirstMap.get(node);
      if (oldNode != null) {
        firstToSecondMap.remove(oldNode);
        secondToFirstMap.remove(node);
      }
    }
  }

  /**
   * Gets the matched nodes.
   *
   * @return the matched nodes
   */
  public Set<ComparePair<AbstractBastNode>> getMatchedNodes() {
    HashSet<ComparePair<AbstractBastNode>> matchedNodes = new HashSet<>();
    for (Entry<AbstractBastNode, AbstractBastNode> e : firstToSecondMap.entrySet()) {
      matchedNodes.add(new ComparePair<AbstractBastNode>(e.getKey(), e.getValue()));
    }
    return matchedNodes;
  }

  /**
   * Update edit script.
   *
   * @param editScript the edit script
   */
  public void updateEditScript(List<BastEditOperation> editScript) {
    this.editScript = editScript;
    editMapOld = new HashMap<AbstractBastNode, BastEditOperation>();
    editMapNew = new HashMap<AbstractBastNode, BastEditOperation>();

    for (BastEditOperation ep : editScript) {
      editMapOld.put(ep.getOldOrInsertedNode(), ep);
    }
    for (BastEditOperation ep : editScript) {
      editMapNew.put(ep.getNewOrChangedNode(), ep);
    }
  }
}
