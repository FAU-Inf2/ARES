package de.fau.cs.inf2.cas.ares.pcreation.visitors;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresCaseStmt;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class RemoveDuplicateChoiceVisitor extends AresDefaultFieldVisitor {
  private ExtendedDiffResult extDiff;

  /**
   * Instantiates a new removes the duplicate choice visitor.
   *
   * @param extDiff the ext diff
   */
  public RemoveDuplicateChoiceVisitor(ExtendedDiffResult extDiff) {
    this.extDiff = extDiff;
  }

  
  /**
   * Standard visit.
   *
   * @param constant the constant
   * @param node the node
   */
  @Override
  public void standardVisit(BastFieldConstants constant, AbstractBastNode node) {
    if (node.fieldMap.get(constant) != null) {
      if (node.fieldMap.get(constant).isList()) {
        int counter = 0;
        if (node.fieldMap.get(constant).getListField() != null) {
          if (node.fieldMap.get(constant).getListField().size() > 1) {
            globalParent = node;
            fieldId = constant;
            boolean changed = true;
            while (changed) {
              changed = false;
              BastField field = null;
              field = removeUses(node.fieldMap.get(constant).getListField());
              if (field != null) {
                if (field.getListField().size() != node.fieldMap.get(constant).getListField()
                    .size()) {
                  changed = true;
                }
                node.replaceField(constant, field);
                continue;
              }

            }

          }
          for (AbstractBastNode expr : node.fieldMap.get(constant).getListField()) {
            setVariables(constant, node, counter++);
            expr.accept(this);
          }
        }
      } else if (node.fieldMap.get(constant).getField() != null) {
        globalParent = node;
        listId = -1;
        fieldId = constant;
        node.fieldMap.get(constant).getField().accept(this);
      }
    }
  }

  private BastField removeUses(LinkedList<? extends AbstractBastNode> listField) {
    int containsCases = 0;
    for (AbstractBastNode expr : listField) {
      if (expr.getTag() == AresCaseStmt.TAG) {
        containsCases++;
      }
    }
    if (containsCases > 0) {
      LinkedList<AbstractBastNode> toRemove = new LinkedList<AbstractBastNode>();
      HashMap<AresCaseStmt, AresCaseStmt> replaceCaseMap =
          new HashMap<AresCaseStmt, AresCaseStmt>();
      for (int i = 0; i < listField.size(); i++) {
        for (int j = i + 1; j < listField.size(); j++) {
          if (listField.get(i).getTag() == AresCaseStmt.TAG
              && listField.get(j).getTag() == AresCaseStmt.TAG) {
            AresCaseStmt case1 = (AresCaseStmt) listField.get(i);
            AresCaseStmt case2 = (AresCaseStmt) listField.get(j);
            if (toRemove.contains(case2)) {
              continue;
            }
            ArrayList<AbstractBastNode> firstList = new ArrayList<>();
            ArrayList<AbstractBastNode> secondList = new ArrayList<>();
            firstList.addAll(case1.block.statements);
            secondList.addAll(case2.block.statements);

            if (WildcardAccessHelper.isEqual(firstList, secondList)) {
              toRemove.add(case2);
              replaceCaseMap.put(case2, case1);
            }

          }
        }
      }
      for (int i = 0; i < listField.size(); i++) {
        for (int j = 0; j < toRemove.size(); j++) {
          if (listField.get(i).getTag() == AresCaseStmt.TAG
              && listField.get(j).getTag() == AresCaseStmt.TAG) {
            AresCaseStmt case1 = (AresCaseStmt) listField.get(i);
            AresCaseStmt case2 = (AresCaseStmt) toRemove.get(j);
            LinkedList<AbstractBastNode> firstList = new LinkedList<>();
            ArrayList<AbstractBastNode> secondList = new ArrayList<>();
            firstList.addAll(case1.block.statements);
            secondList.addAll(case2.block.statements);

            if (!WildcardAccessHelper.isEqual(firstList, secondList)) {
              AbstractBastNode lastStmt1 = firstList.get(firstList.size() - 1);
              AbstractBastNode lastStmt2 = secondList.get(secondList.size() - 1);
              if (extDiff.editMapOld.get(lastStmt1) != null
                  && (extDiff.editMapOld.get(lastStmt1).getType() == EditOperationType.DELETE
                      || extDiff.editMapOld.get(lastStmt1)
                          .getType() == EditOperationType.STATEMENT_DELETE)) {
                if (extDiff.editMapOld.get(lastStmt2) != null
                    && (extDiff.editMapOld.get(lastStmt2).getType() == EditOperationType.INSERT
                        || extDiff.editMapOld.get(lastStmt2)
                            .getType() == EditOperationType.STATEMENT_INSERT)) {
                  boolean found = false;
                  for (int k = 0; k < firstList.size(); k++) {
                    if (WildcardAccessHelper.isEqual(firstList.get(k), lastStmt2)) {
                      found = true;
                    }
                  }
                  if (!found) {
                    firstList.addAll(secondList);
                    case1.block.replaceField(BastFieldConstants.BLOCK_STATEMENT,
                        new BastField(firstList));
                  }

                } else if (extDiff.editMapOld.get(lastStmt2) != null
                    && (extDiff.editMapOld.get(lastStmt2).getType() == EditOperationType.DELETE
                        || extDiff.editMapOld.get(lastStmt2)
                            .getType() == EditOperationType.STATEMENT_DELETE)) {
                  boolean found = false;
                  for (int k = 0; k < firstList.size(); k++) {
                    if (WildcardAccessHelper.isEqual(firstList.get(k), lastStmt2)) {
                      found = true;
                    }
                  }
                  if (!found) {
                    firstList.addAll(secondList);
                    case1.block.replaceField(BastFieldConstants.BLOCK_STATEMENT,
                        new BastField(firstList));
                  }

                }
              }

            }

          }
        }

      }
      if (toRemove.size() == 0) {
        return null;
      } else {
        LinkedList<AbstractBastNode> newNodes = new LinkedList<>();
        newNodes.addAll(listField);
        newNodes.removeAll(toRemove);
        return new BastField(newNodes);
      }
    }
    return null;
  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {

  }

  
  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {

  }
}
