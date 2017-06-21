package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RemoveIdentifierChanges extends AbstractFilterRule {

  public RemoveIdentifierChanges() {
    super(FilterRuleType.REMOVE_IDENTIFIER_CHANGES);
  }

  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    worklist = removeConsistentRenames(delInsertMap, opsFirstToSecond,
        exDiffCurrent, ignoreOp,
        worklist);
    return removeIdentifierDeletes(worklist, exDiffCurrent, delInsertMap);

  }

  /**
   * Removes the consistent variable renames.
   *
   * @param delInsertMap the del insert map
   * @param original1original2 the original 1 original 2
   * @param exDiffCurrent the ex diff current
   * @param ignoreOpBb the ignore op bb
   * @param workList the work list
   * @return the list
   */
  public static List<BastEditOperation> removeConsistentRenames(
      HashMap<AbstractBastNode, AbstractBastNode> delInsertMap,
      List<BastEditOperation> original1original2, ExtendedDiffResult exDiffCurrent,
      boolean[] ignoreOpBb, List<BastEditOperation> workList) {
    List<BastEditOperation> itemsToRemove2 =
        RemoveIdentifierChanges.getVarEditsToIgnore(original1original2, ignoreOpBb);
    for (BastEditOperation ep : itemsToRemove2) {
      AbstractBastNode partner = exDiffCurrent.firstToSecondMap.get(ep.getOldOrInsertedNode());
      delInsertMap.put(ep.getOldOrInsertedNode(), partner);
    }
    workList.removeAll(itemsToRemove2);
    return workList;
  }

  static List<BastEditOperation> getVarEditsToIgnore(final List<BastEditOperation> editScript,
      boolean[] ignoreOpBb) {
    List<BastEditOperation> itemsToRemove = new ArrayList<BastEditOperation>();
    for (int i = 0; i < editScript.size(); i++) {
      if (ignoreOpBb[i]) {
        itemsToRemove.add(editScript.get(i));
      }
    }
    return itemsToRemove;
  }
  
  private static List<BastEditOperation> removeIdentifierDeletes(List<BastEditOperation> editList,
      ExtendedDiffResult exDiffBb, HashMap<AbstractBastNode, AbstractBastNode> delInsertMap) {
    ArrayList<BastEditOperation> workList = new ArrayList<>();
    for (BastEditOperation ep : editList) {
      switch (ep.getType()) {
        case DELETE:
          AbstractBastNode parent = ep.getUnchangedOrOldParentNode();
          AbstractBastNode newParent = exDiffBb.firstToSecondMap.get(parent);
          if (ep.getOldOrInsertedNode().getTag() == BastNameIdent.TAG) {
            if (newParent == null) {
              workList.add(ep);
              continue;
            }
            if (newParent != null
                && newParent.getField(ep.getOldOrChangedIndex().childrenListNumber) != null
                && !newParent.getField(ep.getOldOrChangedIndex().childrenListNumber).isList()) {
              AbstractBastNode entry =
                  newParent.getField(ep.getOldOrChangedIndex().childrenListNumber).getField();
              if (entry != null && entry.getTag() == ep.getOldOrInsertedNode().getTag()) {
                if (((BastNameIdent) entry).name
                    .equals(((BastNameIdent) ep.getOldOrInsertedNode()).name)) {
                  continue;
                }
              }
            }
          }
          workList.add(ep);
          break;
        default:
          workList.add(ep);
      }
    }
    return workList;
  }


  public static FilterRule getInstance() {
    return new RemoveIdentifierChanges();
  }
}
