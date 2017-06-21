package de.fau.cs.inf2.cas.ares.pcreation;

import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.ExecutionRunType;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.AddDeleteForChangeRootStatements;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.AddInsertForAlignments;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.AddInsertForChangeRootBodyPairs;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.AddInsertForStatementsBetweenUses;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.AffectedNodesOutsideMethodBlock;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.CaseAnnotationChanges;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.ChangeRootMovements;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.ChoiceAnnotationChanges;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.CombineSubchangesOfStatements;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.MatchAnnotationChanges;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.RemoveAccessChanges;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.RemoveBlockChanges;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.RemoveChangesInChoice;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.RemoveChangesOfNodesWithIdenticalType;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.RemoveChangesToWildcardChildren;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.RemoveChangesWithIdenticalPartners;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.RemoveIdentifierChanges;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.RemoveIdentifierMoves;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.RemoveIntegerLiteralChanges;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.RemoveMovesObsoleteDueToWildcards;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.RemoveStructureNodeChanges;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.RemoveSubChanges;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.RemoveWildcardOrUseChanges;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.TransformClassCreatorDelete;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.TransformDeclarationChanges;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.TransformDeclarationChildrenChanges;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.TransformForMovement;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.TransformIfChanges;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.TransformListInitializerChanges;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.TransformOrCondition;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.TransformStatementChanges;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.TransformSwitchLabelChanges;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.visitors.CollectNodesVisitor;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.AdvancedEditOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings({ "PMD.ExcessiveClassLength", "PMD.TooManyMethods" })
public class Filter {
  static List<BastEditOperation> filterEditScript2(GeneralizationParameter parameter,
      HashMap<String, String> identifierMap,
      HashMap<AbstractBastNode, AbstractBastNode> delInsertMap,
      List<BastEditOperation> deletedStmtOutsideBoundary) {
    ;
    List<BastEditOperation> opsFirstToSecond = null;

    if (parameter.runType == ExecutionRunType.MODIFIED_RUN) {
      opsFirstToSecond = parameter.modified1modified2;
    } else {
      opsFirstToSecond = parameter.original1original2;
    }
    final ArrayList<BastEditOperation> alignList = new ArrayList<BastEditOperation>();

    List<BastEditOperation> workList = new ArrayList<BastEditOperation>();
    for (BastEditOperation ep : opsFirstToSecond) {
      if (ep instanceof AdvancedEditOperation) {
        workList.add(((AdvancedEditOperation) ep).getBasicOperation());
      } else {
        workList.add(ep);
      }
    }
    ArrayList<FilterRule> rules = new ArrayList<>();
    rules.add(AffectedNodesOutsideMethodBlock.getInstance());
    rules.add(RemoveIdentifierChanges.getInstance());
    rules.add(MatchAnnotationChanges.getInstance());
    rules.add(ChangeRootMovements.getInstance());
    rules.add(CaseAnnotationChanges.getInstance());
    rules.add(ChoiceAnnotationChanges.getInstance());
    rules.add(RemoveChangesInChoice.getInstance());
    rules.add(RemoveChangesOfNodesWithIdenticalType.getInstance());
    rules.add(RemoveMovesObsoleteDueToWildcards.getInstance());
    rules.add(RemoveChangesOfNodesWithIdenticalType.getInstance());
    rules.add(RemoveBlockChanges.getInstance());
    rules.add(TransformForMovement.getInstance());
    rules.add(RemoveSubChanges.getInstance());
    rules.add(RemoveWildcardOrUseChanges.getInstance());
    rules.add(RemoveStructureNodeChanges.getInstance());
    rules.add(TransformDeclarationChanges.getInstance());
    rules.add(RemoveIdentifierMoves.getInstance());
    rules.add(RemoveIntegerLiteralChanges.getInstance());
    rules.add(TransformIfChanges.getInstance());
    rules.add(CombineSubchangesOfStatements.getInstance());
    rules.add(TransformStatementChanges.getInstance());
    rules.add(TransformSwitchLabelChanges.getInstance());
    rules.add(TransformListInitializerChanges.getInstance());
    rules.add(TransformDeclarationChildrenChanges.getInstance());
    rules.add(RemoveChangesWithIdenticalPartners.getInstance());
    rules.add(RemoveChangesToWildcardChildren.getInstance());
    rules.add(TransformOrCondition.getInstance());
    rules.add(AddInsertForChangeRootBodyPairs.getInstance());
    rules.add(TransformClassCreatorDelete.getInstance());
    rules.add(AddDeleteForChangeRootStatements.getInstance());
    rules.add(RemoveAccessChanges.getInstance());
    rules.add(AddInsertForAlignments.getInstance());
    rules.add(AddInsertForStatementsBetweenUses.getInstance());
    for (FilterRule rule : rules) {
      workList = rule.initRule(delInsertMap, alignList, identifierMap, parameter)
          .executeRule(workList);
    }
    return workList;
  }

  /**
   * Check del insert mapping.
   *
   * @param delInsertMap the del insert map
   * @param examine the examine
   * @param nodeKey the node key
   * @param nodeValue the node value
   * @return true, if successful
   */
  public static boolean checkDelInsertMapping(
      HashMap<AbstractBastNode, AbstractBastNode> delInsertMap, boolean examine,
      AbstractBastNode nodeKey, AbstractBastNode nodeValue) {
    CollectNodesVisitor nodeVisitor = new CollectNodesVisitor();
    nodeKey.accept(nodeVisitor);

    CollectNodesVisitor nodeVisitor2 = new CollectNodesVisitor();
    nodeValue.accept(nodeVisitor2);
    if (nodeVisitor.nodes.size() <= 1 || nodeVisitor.nodes.size() != nodeVisitor2.nodes.size()) {
      examine = false;
    }
    for (int j = 0; j < nodeVisitor.nodes.size(); j++) {
      if (nodeVisitor.nodes.get(j).getTag() != nodeVisitor2.nodes.get(j).getTag()) {
        examine = false;
        break;
      }
      if (nodeVisitor.nodes.get(j).getTag() == BastNameIdent.TAG) {
        if (WildcardAccessHelper.isEqual(nodeVisitor.nodes.get(j), nodeVisitor2.nodes.get(j))) {
          if (!delInsertMap.containsKey(nodeVisitor.nodes.get(j)) || WildcardAccessHelper
              .isEqual(nodeVisitor.nodes.get(j), delInsertMap.get(nodeVisitor.nodes.get(j)))) {
            examine = false;
            break;
          }
        }
      }
    }
    return examine;
  }



}
