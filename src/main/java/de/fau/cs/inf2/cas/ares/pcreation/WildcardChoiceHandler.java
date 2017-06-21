package de.fau.cs.inf2.cas.ares.pcreation;

import de.fau.cs.inf2.cas.ares.bast.general.ParentHierarchyHandler;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresCaseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.pcreation.exception.GeneralizationException;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.AddUseVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.CollectEmptyUsesVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.CombineChoiceVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.CombineWuvisitor;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.ConnectWildcardAndUseVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.RemoveDuplicateChoiceVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.RemoveEmptyUsesInChoiceVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.RemoveEnclosedChoices;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.RemoveLostWildcardNamesVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.RemoveObsoleteUseVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.RemoveStatementsfromWildcardsVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.RemoveSubUseVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.RestoreChoicePartsVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.UpdateWildcardNameVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.CreateJavaNodeHelper;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.modification.ModificationInformation;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEmptyStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastForStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitch;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitchCaseGroup;
import de.fau.cs.inf2.cas.common.bast.nodes.BastWhileStatement;
import de.fau.cs.inf2.cas.common.bast.visitors.FindNodesFromTagVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.JavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.StatementInsertOperation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class WildcardChoiceHandler {

  private static class MyAresComparator<T> implements Comparator<AbstractBastNode> {

    @Override
    public int compare(AbstractBastNode o1, AbstractBastNode o2) {
      return Integer.compare(o1.getNodeId(), o2.getNodeId());
    }

  }

  private static class WildcardEntryComparator
      implements Comparator<Entry<AbstractBastNode, AbstractBastNode>> {

    @Override
    public int compare(Entry<AbstractBastNode, AbstractBastNode> o1,
        Entry<AbstractBastNode, AbstractBastNode> o2) {
      if (o2.getValue() == o1.getValue()) {
        return Integer.compare(o1.getKey().nodeId, o2.getKey().nodeId);
      } else {
        return Integer.compare(o1.getValue().nodeId, o2.getValue().nodeId);
      }
    }

  }

  private static class MyListComparator implements Comparator<AbstractBastStatement> {

    @Override
    public int compare(AbstractBastStatement o1, AbstractBastStatement o2) {
      return Integer.compare(o1.nodeId, o2.nodeId);
    }

  }

  public static class NodeComparator implements Comparator<AbstractBastNode> {

    @Override
    public int compare(AbstractBastNode o1, AbstractBastNode o2) {
      return Integer.compare(o1.nodeId, o2.nodeId);
    }

  }

  static void handleWildcards(AbstractBastNode example2Original, AbstractBastNode example2Modified,
      ExtendedDiffResult exDiffBb, ExtendedDiffResult exDiffAa, ExtendedDiffResult exDiffBA1,
      ExtendedDiffResult exDiffBA2, ConnectWildcardAndUseVisitor cwuv,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyA1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyA2,
      HashMap<AbstractBastNode, AbstractBastNode> delInsertMap,
      HashMap<AbstractBastNode, List<RevertModificationInfo>> revertMap, InsertResult originalRes,
      InsertResult modifiedRes, MatchBoundary boundary, ConnectWildcardAndUseVisitor connectKeeper)
      throws GeneralizationException {
    originalRes.replaceMap =
        combineSwitchCases(example2Original, originalRes.replaceMap, AresWildcard.TAG, cwuv);
    PatternGenerator.print(example2Original);
    modifiedRes.replaceMap =
        combineSwitchCases(example2Modified, modifiedRes.replaceMap, AresUseStmt.TAG, cwuv);
    PatternGenerator.print(example2Modified);
    Map<AresWildcard, List<AresUseStmt>> wildcardUseMap =
        createWildcardUseMap(exDiffBA1, exDiffBA2, exDiffBb, exDiffAa, originalRes, modifiedRes,
            cwuv, example2Original, example2Modified, delInsertMap);
    splitUses(example2Modified, wildcardUseMap, exDiffBA1, exDiffBA2, originalRes, modifiedRes,
        cwuv);
    PatternGenerator.print(example2Original);
    PatternGenerator.print(example2Modified);
    RemoveObsoleteUseVisitor rouv = new RemoveObsoleteUseVisitor(cwuv);
    example2Modified.accept(rouv);
    insertWildcardUseNames(example2Original, example2Modified, wildcardUseMap, cwuv, originalRes,
        modifiedRes);
    PatternGenerator.print(example2Original);
    PatternGenerator.print(example2Modified);

    final HashSet<AbstractBastNode> addedUses = splitAdvancedUses(example2Modified, wildcardUseMap,
        exDiffBA1, exDiffBA2, originalRes, modifiedRes, cwuv);
    PatternGenerator.print(example2Modified);

    removeEmptyBorderWildcardsAndUses(example2Original, example2Modified);
    PatternGenerator.print(example2Original);
    PatternGenerator.print(example2Modified);

    identifyChoicesInUses(example2Original, example2Modified, exDiffBA1, exDiffBA2, exDiffAa,
        originalRes, modifiedRes, hierarchyA1, hierarchyA2, addedUses);
    PatternGenerator.print(example2Original);
    PatternGenerator.print(example2Modified);

    combineEmptyExprUses(example2Modified, hierarchyA2, modifiedRes.replaceMap, cwuv, addedUses,
        exDiffAa);
    PatternGenerator.print(example2Modified);

    RemoveSubUseVisitor rsuv = new RemoveSubUseVisitor();
    example2Modified.accept(rsuv);
    PatternGenerator.print(example2Modified);

    insertCompleteMethodChoice(example2Modified, boundary);
    PatternGenerator.print(example2Modified);


    insertChoices(exDiffAa, exDiffBA1, exDiffBA2, example2Modified, hierarchyA2,
        modifiedRes.replaceMap, revertMap, delInsertMap, boundary);
    PatternGenerator.print(example2Modified);

    CombineChoiceVisitor ccv = new CombineChoiceVisitor(hierarchyA1, hierarchyA2);
    example2Modified.accept(ccv);

    removeBorderWildcardsAndUses(example2Original, example2Modified, boundary);
    example2Modified.accept(ccv);

    RemoveLostWildcardNamesVisitor rlwn = new RemoveLostWildcardNamesVisitor();
    example2Original.accept(rlwn);
    example2Modified.accept(rlwn);
    PatternGenerator.print(example2Original);
    PatternGenerator.print(example2Modified);

    RemoveEmptyUsesInChoiceVisitor reuic = new RemoveEmptyUsesInChoiceVisitor();
    example2Modified.accept(reuic);
    PatternGenerator.print(example2Original);
    PatternGenerator.print(example2Modified);
    insertChoices(exDiffAa, exDiffBA1, exDiffBA2, example2Modified, hierarchyA2,
        modifiedRes.replaceMap, revertMap, delInsertMap, boundary);
    PatternGenerator.print(example2Modified);
    ccv = new CombineChoiceVisitor(hierarchyA1, hierarchyA2);
    example2Modified.accept(ccv);
    RemoveDuplicateChoiceVisitor rdcv = new RemoveDuplicateChoiceVisitor(exDiffAa);
    example2Modified.accept(rdcv);
    PatternGenerator.print(example2Original);

    RemoveStatementsfromWildcardsVisitor rsfw = new RemoveStatementsfromWildcardsVisitor(exDiffBA2,
        exDiffBb, exDiffAa, example2Original, example2Modified, originalRes, modifiedRes);
    example2Original.accept(rsfw);
    PatternGenerator.print(example2Original);
    PatternGenerator.print(example2Modified);
    RemoveEnclosedChoices rec = new RemoveEnclosedChoices(exDiffBA2);
    example2Modified.accept(rec);
    PatternGenerator.print(example2Modified);

    UpdateWildcardNameVisitor uwnvNameVisitor = new UpdateWildcardNameVisitor();
    example2Original.accept(uwnvNameVisitor);
    example2Modified.accept(uwnvNameVisitor);
    removeWildcardBlocks(AresWildcard.TAG, example2Original);
    removeWildcardBlocks(AresUseStmt.TAG, example2Modified);
    PatternGenerator.print(example2Original);
    PatternGenerator.print(example2Modified);
  }

  private static void removeWildcardBlocks(int tag, AbstractBastNode example2Original) {
    Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy =
        ParentHierarchyHandler.getParentHierarchy(example2Original);
    FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(tag);
    example2Original.accept(fnft);
    for (AbstractBastNode node : fnft.nodes) {
      NodeParentInformationHierarchy npi = hierarchy.get(node);
      if (npi != null && npi.list.size() > 2 && npi.list.get(0).parent.getTag() == BastBlock.TAG) {
        @SuppressWarnings("unchecked")
        LinkedList<AbstractBastNode> stmts = (LinkedList<AbstractBastNode>) npi.list.get(0).parent
            .getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
        if (stmts.size() == 1) {
          switch (npi.list.get(1).parent.getTag()) {
            case BastForStmt.TAG:
            case BastWhileStatement.TAG:
            case BastIf.TAG:
              BastField field = null;
              if (npi.list.get(1).fieldConstant.isList) {
                LinkedList<AbstractBastNode> newStmts = new LinkedList<>();
                newStmts.add(node);
                field = new BastField(newStmts);
              } else {
                field = new BastField(node);
              }
              npi.list.get(1).parent.replaceField(npi.list.get(1).fieldConstant, field);
              break;
            default:
              break;
          }
        }
      }
    }

  }

  @SuppressWarnings("unchecked")
  private static void insertCompleteMethodChoice(AbstractBastNode example2Modified,
      MatchBoundary boundary) {
    FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(AresBlock.TAG);
    example2Modified.accept(fnft);
    if (fnft.nodes.size() == 1) {
      AresBlock lblock = ((AresBlock) fnft.nodes.get(0));
      if (lblock != null) {
        LinkedList<AbstractBastStatement> blockStmts =
            (LinkedList<AbstractBastStatement>) lblock.block
                .getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
        boolean replace = true;
        boolean useFound = false;
        for (AbstractBastNode node : blockStmts) {
          if (node.getTag() == AresUseStmt.TAG) {
            if (useFound) {
              replace = false;
              break;
            } else {
              if (WildcardAccessHelper.getName(node) == null) {
                useFound = true;
              } else {
                replace = false;
                break;
              }
            }
          } else if (node.getTag() != AresChoiceStmt.TAG) {
            replace = false;
            break;
          }
        }
        if (replace && useFound) {
          final LinkedList<AbstractBastStatement> stmts = new LinkedList<>();
          LinkedList<AbstractBastStatement> oldStmts = null;
          LinkedList<AbstractBastStatement> newStmts = null;

          if (boundary.getCopyNode1().getField(boundary.field1).isList()) {
            oldStmts = (LinkedList<AbstractBastStatement>) boundary.getCopyNode1()
                .getField(boundary.field1).getListField();

          } else {
            assert (false);
          }
          if (boundary.getCopyNode2().getField(boundary.field2).isList()) {
            newStmts = (LinkedList<AbstractBastStatement>) boundary.getCopyNode2()
                .getField(boundary.field2).getListField();
            LinkedList<AbstractBastNode> toRemove = new LinkedList<>();
            for (AbstractBastNode node : newStmts) {
              if (node.getTag() == AresChoiceStmt.TAG) {
                toRemove.add(node);
              }
            }
            newStmts.removeAll(toRemove);
          } else {
            assert (false);
          }
          LinkedList<AbstractBastStatement> choices = new LinkedList<>();
          TokenAndHistory token = null;
          if (blockStmts.size() > 0 && blockStmts.get(0).info != null
              && blockStmts.get(0).info.tokens[0] != null) {
            token = blockStmts.get(0).info.tokens[0];
          }
          AbstractBastStatement caseStmt = CreateJavaNodeHelper.createAresCase("", token, oldStmts);
          choices.add(caseStmt);
          caseStmt = CreateJavaNodeHelper.createAresCase("", token, newStmts);
          choices.add(caseStmt);
          AbstractBastStatement choice = CreateJavaNodeHelper.createAresChoice("", token, choices);
          stmts.add(choice);
          lblock.block.replaceField(BastFieldConstants.BLOCK_STATEMENT, new BastField(stmts));
        }
      }
    }
  }

  private static Map<AresWildcard, List<AresUseStmt>> createWildcardUseMap(
      ExtendedDiffResult exDiffBA1, ExtendedDiffResult exDiffBA2, ExtendedDiffResult exDiffBb,
      ExtendedDiffResult exDiffAa, InsertResult originalRes, InsertResult modifiedRes,
      ConnectWildcardAndUseVisitor cuwv, AbstractBastNode example2Original,
      AbstractBastNode example2Modified, HashMap<AbstractBastNode, AbstractBastNode> delInsertMap) {
    Map<AresWildcard, List<AresUseStmt>> wildcardUseMap =
        new HashMap<AresWildcard, List<AresUseStmt>>();

    ArrayList<Entry<AbstractBastNode, AbstractBastNode>> wildcardList = new ArrayList<>();
    wildcardList.addAll(originalRes.replaceMap.entrySet());
    Collections.sort(wildcardList, new WildcardEntryComparator());
    for (Entry<AbstractBastNode, AbstractBastNode> wildEntry : wildcardList) {
      AbstractBastNode tmp2 = (AbstractBastNode) exDiffBA2.firstToSecondMap.get(wildEntry.getKey());

      if (tmp2 == null) {
        tmp2 = (AbstractBastNode) exDiffBA1.firstToSecondMap.get(wildEntry.getKey());
      }
      AresUseStmt use = null;
      if (tmp2 != null) {
        use = (AresUseStmt) modifiedRes.replaceMap.get(tmp2);
      }

      if (use != null) {
        if (WildcardAccessHelper.getName(use) != null
            && WildcardAccessHelper.getName(wildEntry.getValue()) == null) {
          continue;
        }
        ArrayList<AresUseStmt> list = new ArrayList<AresUseStmt>();
        list.add(use);
        BastEditOperation ep = exDiffAa.editMapOld.get(tmp2);
        BastEditOperation ep2 = exDiffBb.editMapOld.get(wildEntry.getKey());
        if (ep2 != null && ep != null) {
          if (ep2.getType() == EditOperationType.UPDATE
              && ep.getType() != EditOperationType.UPDATE) {
            continue;
          }
        }
        wildcardUseMap.put((AresWildcard) wildEntry.getValue(), list);
      }
    }
    for (Map.Entry<String, AresWildcard> entry : cuwv.wildcards.entrySet()) {
      if (cuwv.useStmts.get(entry.getKey()) != null) {
        if (!wildcardUseMap.containsKey(entry.getValue())) {
          wildcardUseMap.put(entry.getValue(), cuwv.useStmts.get(entry.getKey()));
        } else {
          if (wildcardUseMap.get(entry.getValue()) != cuwv.useStmts.get(entry.getKey())
              && cuwv.useStmts.get(entry.getKey()) != null
              && !cuwv.useStmts.get(entry.getKey()).isEmpty()) {

            wildcardUseMap.put(entry.getValue(), cuwv.useStmts.get(entry.getKey()));
          }
        }
      }
    }
    for (Entry<AbstractBastNode, AbstractBastNode> wildEntry : originalRes.replaceMap.entrySet()) {
      if (!wildcardUseMap.containsKey(wildEntry.getValue())) {

        for (Entry<AbstractBastNode, AbstractBastNode> useEntry : modifiedRes.replaceMap
            .entrySet()) {
          if (true) {
            AbstractBastNode parent = null;
            NodeIndex index = null;
            AbstractBastNode parentInsert = null;
            NodeIndex indexInsert = null;
            BastEditOperation ep = exDiffBA1.editMapOld.get(wildEntry.getKey());
            if (ep != null) {
              if (ep.getType() == EditOperationType.DELETE
                  || ep.getType() == EditOperationType.STATEMENT_DELETE) {
                parent = ep.getUnchangedOrOldParentNode();
                index = ep.getNewOrChangedIndex();
              }
            }
            ep = exDiffBA1.editMapOld.get(useEntry.getKey());
            if (ep != null) {
              if (ep.getType() == EditOperationType.INSERT
                  || ep.getType() == EditOperationType.STATEMENT_INSERT) {
                parentInsert = ep.getUnchangedOrOldParentNode();
                indexInsert = ep.getNewOrChangedIndex();
              }
            }
            if (parent != null && parentInsert != null) {
              if (parentInsert == exDiffBA1.firstToSecondMap.get(parent)) {
                if (index.childrenListNumber == indexInsert.childrenListNumber) {
                  if (index.childrenListIndex == indexInsert.childrenListIndex) {
                    ArrayList<AresUseStmt> list = new ArrayList<AresUseStmt>();
                    list.add((AresUseStmt) useEntry.getValue());
                    wildcardUseMap.put((AresWildcard) wildEntry.getValue(), list);
                    break;

                  } else if (parent.getField(index.childrenListNumber).getListField().size()
                      - 1 == index.childrenListIndex
                      && parentInsert.getField(indexInsert.childrenListNumber).getListField().size()
                          - 1 == indexInsert.childrenListIndex) {
                    ArrayList<AresUseStmt> list = new ArrayList<AresUseStmt>();
                    list.add((AresUseStmt) useEntry.getValue());
                    wildcardUseMap.put((AresWildcard) wildEntry.getValue(), list);
                    break;

                  }
                }
              }
            }
            if (WildcardAccessHelper.isExprWildcard(wildEntry.getValue())
                && WildcardAccessHelper.isExprWildcard(useEntry.getValue())) {
              AbstractBastNode wildExpr =
                  WildcardAccessHelper.getExpressionNode(wildEntry.getValue(), example2Original);
              AbstractBastNode useExpr =
                  WildcardAccessHelper.getExpressionNode(useEntry.getValue(), example2Modified);
              if (wildExpr != null && useExpr != null) {
                if (exDiffBA2.firstToSecondMap.get(wildExpr) == useExpr
                    || delInsertMap.get(wildExpr) == useExpr) {
                  if (WildcardAccessHelper.getExpr(wildEntry.getValue())
                      .getTag() == WildcardAccessHelper.getExpr(useEntry.getValue()).getTag()) {
                    if (WildcardAccessHelper.isEqual(
                        WildcardAccessHelper.getExpr(wildEntry.getValue()),
                        WildcardAccessHelper.getExpr(useEntry.getValue()))) {
                      ArrayList<AresUseStmt> list = new ArrayList<AresUseStmt>();
                      list.add((AresUseStmt) useEntry.getValue());
                      wildcardUseMap.put((AresWildcard) wildEntry.getValue(), list);
                    }
                  }
                }
              }

            }
          }
        }
        if (!wildcardUseMap.containsKey(wildEntry.getValue())) {
          wildcardUseMap.put((AresWildcard) wildEntry.getValue(), null);
        }
      }
    }
    for (Entry<AbstractBastNode, AbstractBastNode> wildEntry : wildcardList) {
      AbstractBastNode tmp2 = (AbstractBastNode) exDiffBA2.firstToSecondMap.get(wildEntry.getKey());

      if (tmp2 == null) {
        tmp2 = (AbstractBastNode) exDiffBA1.firstToSecondMap.get(wildEntry.getKey());
      }
      AresUseStmt use = null;
      if (tmp2 == null) {
        use = (AresUseStmt) modifiedRes.replaceMap.get(tmp2);
      }

      if (wildcardUseMap.get(wildEntry.getValue()) == null) {
        if (use == null) {
          if (WildcardAccessHelper.getName(wildEntry.getValue()) != null) {
            FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(AresUseStmt.TAG);
            example2Modified.accept(fnft);
            ArrayList<AresUseStmt> list = new ArrayList<AresUseStmt>();
            for (AbstractBastNode node : fnft.nodes) {
              if (WildcardAccessHelper.getName(node) != null
                  && WildcardAccessHelper.getName(wildEntry.getValue()).name
                      .equals(WildcardAccessHelper.getName(node).name)) {
                list.add((AresUseStmt) node);
              }
            }
            if (list.size() == 1) {
              wildcardUseMap.put((AresWildcard) wildEntry.getValue(), list);
            }
          }
        }
      }
    }
    ArrayList<Entry<AresWildcard, List<AresUseStmt>>> list =
        new ArrayList<Map.Entry<AresWildcard, List<AresUseStmt>>>(wildcardUseMap.entrySet());
    Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyOriginal =
        ParentHierarchyHandler.getParentHierarchy(example2Original);
    Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyModified =
        ParentHierarchyHandler.getParentHierarchy(example2Modified);

    for (int i = 0; i < list.size(); i++) {
      for (int j = i; j < list.size(); j++) {
        Entry<AresWildcard, List<AresUseStmt>> first = list.get(i);
        Entry<AresWildcard, List<AresUseStmt>> second = list.get(j);
        AresWildcard wildFirst = first.getKey();
        AresWildcard wildSecond = second.getKey();
        if (wildFirst != wildSecond) {
          HashMap<Entry<AresWildcard, List<AresUseStmt>>, AresUseStmt> hashMap =
              new HashMap<Map.Entry<AresWildcard, List<AresUseStmt>>, AresUseStmt>();
          if (first.getValue() != null) {

            for (AresUseStmt firstUse : first.getValue()) {
              if (second.getValue() != null) {
                for (AresUseStmt secondUse : second.getValue()) {
                  if (firstUse == secondUse && !hashMap.containsValue(firstUse)) {
                    LinkedList<AbstractBastNode> wildcardFirstList =
                        new LinkedList<AbstractBastNode>();
                    LinkedList<AbstractBastNode> wildcardSecondList =
                        new LinkedList<AbstractBastNode>();
                    LinkedList<AbstractBastNode> useList = new LinkedList<AbstractBastNode>();
                    for (Entry<AbstractBastNode, AbstractBastNode> b : originalRes.replaceMap
                        .entrySet()) {
                      if (b.getValue() == wildFirst) {
                        wildcardFirstList.add(b.getKey());
                      }
                      if (b.getValue() == wildSecond) {
                        wildcardSecondList.add(b.getKey());
                      }
                    }
                    for (Entry<AbstractBastNode, AbstractBastNode> b : modifiedRes.replaceMap
                        .entrySet()) {
                      if (b.getValue() == firstUse) {
                        useList.add(b.getKey());
                      }
                    }
                    int firstCount = 0;
                    int secondCount = 0;
                    for (AbstractBastNode node : useList) {
                      for (AbstractBastNode firstNode : wildcardFirstList) {
                        if (WildcardAccessHelper.isEqual(node, firstNode)) {
                          firstCount++;
                          break;
                        }
                      }
                      for (AbstractBastNode secondNode : wildcardSecondList) {
                        if (WildcardAccessHelper.isEqual(node, secondNode)) {
                          secondCount++;
                        }
                      }
                    }
                    if (firstCount < secondCount) {
                      if (secondCount == 1
                          && WildcardAccessHelper.isNameEqual(wildFirst, firstUse)) {
                        hashMap.put(second, secondUse);
                      } else {
                        hashMap.put(first, firstUse);
                      }

                    } else if (firstCount > secondCount) {
                      if (firstCount == 1
                          && WildcardAccessHelper.isNameEqual(wildSecond, secondUse)) {
                        hashMap.put(first, firstUse);
                      } else {
                        hashMap.put(second, secondUse);
                      }

                    } else {
                      NodeParentInformationHierarchy npiFirst = hierarchyOriginal.get(wildFirst);
                      NodeParentInformationHierarchy npiSecond = hierarchyOriginal.get(wildSecond);
                      NodeParentInformationHierarchy npiUse = hierarchyModified.get(firstUse);
                      if (npiFirst != null && npiSecond != null && npiUse != null) {
                        int firstPos = npiFirst.list.get(0).listId;
                        int secondPos = npiSecond.list.get(0).listId;
                        int usePos = npiUse.list.get(0).listId;
                        if (Math.abs(firstPos - usePos) > Math.abs(secondPos - usePos)) {
                          hashMap.put(first, firstUse);
                        } else if (Math.abs(firstPos - usePos) < Math.abs(secondPos - usePos)) {
                          hashMap.put(second, secondUse);
                        } else {
                          if (Math.abs(wildFirst.nodeId - firstUse.nodeId) > Math
                              .abs(wildSecond.nodeId - firstUse.nodeId)) {
                            hashMap.put(first, firstUse);
                          } else {
                            hashMap.put(second, secondUse);
                          }
                        }
                      }
                    }

                  }
                }
              }
            }
          }
          for (Entry<Entry<AresWildcard, List<AresUseStmt>>, AresUseStmt> entry : hashMap
              .entrySet()) {
            entry.getKey().getValue().remove(entry.getValue());
          }
        }

      }
    }
    return wildcardUseMap;
  }

  private static ReplaceMap combineSwitchCases(AbstractBastNode example, ReplaceMap replaceMap,
      int tag, ConnectWildcardAndUseVisitor cwuv) {
    FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(BastSwitch.TAG);
    example.accept(fnft);
    for (AbstractBastNode node : fnft.nodes) {
      BastSwitch examineSwitch = ((BastSwitch) node);
      @SuppressWarnings("unchecked")
      LinkedList<AbstractBastNode> groups = (LinkedList<AbstractBastNode>) examineSwitch.fieldMap
          .get(BastFieldConstants.SWITCH_CASE_GROUPS).getListField();
      boolean changed = true;
      while (changed) {
        changed = false;
        for (int i = 0; i < groups.size(); i++) {
          if (groups.get(i).getTag() == BastSwitchCaseGroup.TAG) {
            BastSwitchCaseGroup group = (BastSwitchCaseGroup) groups.get(i);
            if (group.stmts == null || group.stmts.size() == 0) {
              if (group.labels != null && group.labels.size() > 0
                  && WildcardAccessHelper.isWildcard(group.labels.getLast())) {
                if (i < groups.size() - 1) {
                  if (WildcardAccessHelper.isWildcard(groups.get(i + 1))) {
                    replaceMap = CombineWuvisitor.updateMaps(cwuv, tag, replaceMap,
                        group.labels.getLast(), groups.get(i + 1));
                    group.labels.removeLast();
                    group.replaceField(BastFieldConstants.SWITCH_CASE_GROUP_LABELS,
                        new BastField(group.labels));
                    changed = true;
                    break;
                  }
                }
              }
            } else if (group.stmts != null && group.stmts.size() > 0
                && WildcardAccessHelper.isWildcard(group.stmts.getLast())) {
              if (i < groups.size() - 1) {
                if (WildcardAccessHelper.isWildcard(groups.get(i + 1))) {
                  replaceMap = CombineWuvisitor.updateMaps(cwuv, tag, replaceMap,
                      group.stmts.getLast(), groups.get(i + 1));
                  group.stmts.removeLast();
                  group.replaceField(BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS,
                      new BastField(group.stmts));
                  changed = true;
                  break;
                }
              }
            }
          }
        }
      }
    }
    return replaceMap;
  }

  private static void insertWildcardUseNames(AbstractBastNode example2Original,
      AbstractBastNode example2Modified, Map<AresWildcard, List<AresUseStmt>> wildcardUseMap,
      ConnectWildcardAndUseVisitor cwuv, InsertResult originalRes, InsertResult modifiedRes) {
    VariableNameGenerator vng = new VariableNameGenerator();

    ArrayList<AresWildcard> keyList = new ArrayList<>(wildcardUseMap.keySet());
    Comparator<AbstractBastNode> mycomp = new MyAresComparator<>();
    Collections.sort(keyList, mycomp);
    LinkedList<AresUseStmt> assignedNames = new LinkedList<AresUseStmt>();
    FindNodesFromTagVisitor fnfv = new FindNodesFromTagVisitor(AresWildcard.TAG);
    example2Original.accept(fnfv);
    HashSet<String> names = new HashSet<>();
    for (AbstractBastNode node : fnfv.nodes) {
      AresWildcard wildcard = (AresWildcard) node;
      if (wildcard.pattern != null && wildcard.pattern.ident != null) {
        names.add(wildcard.pattern.ident.name);
      }
      if (wildcard.plugin != null && wildcard.plugin.exprList != null
          && wildcard.plugin.exprList.size() > 0 && wildcard.plugin.exprList.getFirst() != null
          && wildcard.plugin.exprList.getFirst().getTag() == AresPatternClause.TAG
          && ((AresPatternClause) wildcard.plugin.exprList.getFirst()).ident != null) {
        names.add(((AresPatternClause) wildcard.plugin.exprList.getFirst()).ident.name);
      }
    }
    fnfv = new FindNodesFromTagVisitor(AresUseStmt.TAG);
    example2Modified.accept(fnfv);
    for (AbstractBastNode node : fnfv.nodes) {
      AresUseStmt use = (AresUseStmt) node;
      if (WildcardAccessHelper.getName(use) != null) {
        names.add(WildcardAccessHelper.getName(use).name);
      }
    }
    fnfv = new FindNodesFromTagVisitor(AresWildcard.TAG);
    example2Modified.accept(fnfv);
    for (AresWildcard key : keyList) {
      assert (key.plugin != null);
      LinkedList<AbstractBastExpr> list = key.plugin.exprList;
      AresPatternClause pattern = null;
      if (list == null) {
        list = new LinkedList<>();
      } else {
        for (AbstractBastExpr expr : list) {
          if (expr.getTag() == AresPatternClause.TAG) {
            pattern = (AresPatternClause) expr;
          }
        }
      }
      String name = null;
      while (name == null) {
        name = vng.getName();
        if (cwuv.names.contains(name)) {
          name = null;
        }
        if (names.contains(name)) {
          name = null;
        }
      }
      BastNameIdent nameW = CreateJavaNodeHelper.createBastNameIdent(name);

      if (pattern == null) {
        list.add(CreateJavaNodeHelper.createAresPatternClause(null, null, null, nameW));
        key.plugin.replaceField(BastFieldConstants.ARES_PLUGIN_CLAUSE_EXPR_LIST,
            new BastField(list));
      } else {
        if (pattern.ident != null && pattern.ident.name != null) {
          nameW = CreateJavaNodeHelper.createBastNameIdent(pattern.ident.name);
        }
        pattern.replaceField(BastFieldConstants.ARES_PATTERN_CLAUSE_IDENT, new BastField(nameW));
      }

      if (wildcardUseMap.get(key) != null) {

        for (AresUseStmt useEntry : wildcardUseMap.get(key)) {
          if (useEntry != null) {
            if (WildcardAccessHelper.getName(useEntry) != null && !fnfv.nodes.contains(key)) {
              continue;
            }
            assignedNames.add(useEntry);
            BastNameIdent nameU = null;
            if (pattern == null) {
              nameU = CreateJavaNodeHelper.createBastNameIdent(name);
            } else {
              nameU = CreateJavaNodeHelper.createBastNameIdent(pattern.ident.name);

            }
            AresPatternClause clause = (AresPatternClause) useEntry
                .getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField();
            if (clause == null) {
              clause = CreateJavaNodeHelper.createAresPatternClause(null, null, null, nameU);
              CreateJavaNodeHelper.addWhiteSpace(clause, " ");
              useEntry.replaceField(BastFieldConstants.ARES_USE_STMT_PATTERN,
                  new BastField(clause));
            } else {
              clause.replaceField(BastFieldConstants.ARES_PATTERN_CLAUSE_IDENT,
                  new BastField(nameU));
            }
          }
        }
      }
    }
  }

  private static void removeEmptyBorderWildcardsAndUses(AbstractBastNode example2Original,
      AbstractBastNode example2Modified) {
    FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(AresBlock.TAG);
    example2Original.accept(fnft);
    if (fnft.nodes.isEmpty()) {
      return;
    }
    AresBlock lblock = (AresBlock) fnft.nodes.get(0);
    assert (lblock != null);
    fnft = new FindNodesFromTagVisitor(AresBlock.TAG);
    example2Modified.accept(fnft);
    @SuppressWarnings("unchecked")
    LinkedList<AbstractBastStatement> stmts =
        (LinkedList<AbstractBastStatement>) lblock.getField(BastFieldConstants.ARES_BLOCK_BLOCK)
            .getField().getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
    if (fnft.nodes.size() == 0) {
      return;
    }
    @SuppressWarnings("unchecked")
    LinkedList<AbstractBastStatement> stmtsUse =
        (LinkedList<AbstractBastStatement>) ((AresBlock) fnft.nodes.get(0))
            .getField(BastFieldConstants.ARES_BLOCK_BLOCK).getField()
            .getField(BastFieldConstants.BLOCK_STATEMENT).getListField();

    AbstractBastStatement[] scannedWStmts = new AbstractBastStatement[stmts.size()];
    for (int i = 0; i < stmts.size(); i++) {
      if (stmts.get(i).getTag() == AresWildcard.TAG) {
        AresWildcard wildcard = (AresWildcard) stmts.get(i);
        if (wildcard.plugin.ident.name.equals("stmt")) {
          scannedWStmts[i] = wildcard;

        }
      } else if (stmts.get(i).getTag() == BastEmptyStmt.TAG) {
        scannedWStmts[i] = stmts.get(i);
      }
    }
    if (stmtsUse == null) {
      return;
    }
    AbstractBastStatement[] scannedUStmts = new AbstractBastStatement[stmtsUse.size()];
    for (int i = 0; i < stmtsUse.size(); i++) {
      if (stmtsUse.get(i).getTag() == AresUseStmt.TAG) {
        AresUseStmt use = (AresUseStmt) stmtsUse.get(i);
        scannedUStmts[i] = use;
      } else if (stmtsUse.get(i).getTag() == BastEmptyStmt.TAG) {
        scannedUStmts[i] = stmtsUse.get(i);
      }
    }
    for (int i = 0; i < scannedWStmts.length; i++) {
      if (scannedWStmts[i] == null) {
        break;
      }
      if (scannedWStmts[i].getTag() == BastEmptyStmt.TAG) {
        continue;
      }
      AresWildcard wildcard = (AresWildcard) scannedWStmts[i];
      if (wildcard.plugin.pattern == null || wildcard.plugin.pattern.ident == null) {
        if (i < scannedUStmts.length && scannedUStmts[i] != null) {
          if (WildcardAccessHelper.getName(((AresUseStmt) scannedUStmts[i])) == null) {
            stmtsUse.remove(i);
            stmts.remove(i);
          } else if (wildcard.plugin.exprList != null) {
            if (wildcard.plugin.exprList.get(0) != null
                && wildcard.plugin.exprList.get(0).getTag() == AresPatternClause.TAG
                && ((AresPatternClause) wildcard.plugin.exprList.get(0)).ident.name
                    .equals(WildcardAccessHelper.getName(((AresUseStmt) scannedUStmts[i])).name)) {
              stmtsUse.remove(i);
              stmts.remove(i);
            }
          }

        }
      }
    }
    int offset = 0;
    for (int i = 1; i < scannedWStmts.length; i++) {
      if (scannedWStmts[scannedWStmts.length - i] == null) {
        break;
      }
      if (scannedWStmts[scannedWStmts.length - i].getTag() == BastEmptyStmt.TAG) {
        offset++;
        continue;
      }
      AresWildcard wildcard = (AresWildcard) scannedWStmts[scannedWStmts.length - i];
      if (wildcard.plugin.pattern == null || wildcard.plugin.pattern.ident == null) {
        if (scannedUStmts.length - i + offset >= 0
            && scannedUStmts.length - i + offset < stmtsUse.size()
            && scannedUStmts[scannedUStmts.length - i + offset] != null) {
          if (WildcardAccessHelper
              .getName(((AresUseStmt) scannedUStmts[scannedUStmts.length - i + offset])) == null) {
            stmtsUse.remove(scannedUStmts.length - i + offset);
            stmts.remove(scannedWStmts.length - i);
          }
        }
      }
    }
    lblock.block.replaceField(BastFieldConstants.BLOCK_STATEMENT, new BastField(stmts));
    fnft = new FindNodesFromTagVisitor(AresBlock.TAG);
    example2Modified.accept(fnft);
    lblock = (AresBlock) fnft.nodes.get(0);
    assert (lblock != null);

    lblock.block.replaceField(BastFieldConstants.BLOCK_STATEMENT, new BastField(stmtsUse));
  }

  private static void identifyChoicesInUses(AbstractBastNode example2Original,
      AbstractBastNode example2Modified, ExtendedDiffResult extDiffBA1,
      ExtendedDiffResult extDiffBA2, ExtendedDiffResult extDiffaa, InsertResult originalRes,
      InsertResult modifiedRes, Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyA1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyA2,
      HashSet<AbstractBastNode> addedUses) {
    FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(AresWildcard.TAG);
    example2Original.accept(fnft);
    fnft = new FindNodesFromTagVisitor(AresUseStmt.TAG);
    example2Modified.accept(fnft);
    ArrayList<AbstractBastNode> uses = fnft.nodes;
    FindNodesFromTagVisitor fnft2 = new FindNodesFromTagVisitor(AresBlock.TAG);
    example2Modified.accept(fnft2);
    HashMap<AbstractBastNode, ModificationInformation> modifiedNodesBA2 =
        PatternGenerator.getModifiedNodes(extDiffBA2.editScript, extDiffBA2, false, false);
    for (AbstractBastNode node : uses) {
      AresUseStmt stmt = (AresUseStmt) node;
      LinkedList<AbstractBastNode> withPartner = new LinkedList<>();
      LinkedList<AbstractBastNode> withoutPartner = new LinkedList<>();
      boolean hasDelete = false;
      for (Entry<AbstractBastNode, AbstractBastNode> pair : modifiedRes.replaceMap.entrySet()) {
        if (pair.getValue() == stmt) {
          if (modifiedNodesBA2.get(pair.getKey()) != null) {
            withoutPartner.add(pair.getKey());
            if (extDiffBA2.editMapNew.get(pair.getKey()) != null) {
              if (extDiffBA2.editMapNew.get(pair.getKey()).getType() == EditOperationType.ALIGN) {
                hasDelete = true;
                break;
              }
            }
          } else {
            if (WildcardAccessHelper.isEqual(pair.getKey(),
                extDiffBA2.secondToFirstMap.get(pair.getKey()))) {
              extDiffBA2.secondToFirstMap.get(pair.getKey());
              withPartner.add(pair.getKey());
            } else {
              if (extDiffaa.secondToFirstMap.get(pair.getKey()) != null) {
                AbstractBastNode modifiedPartner = extDiffaa.secondToFirstMap.get(pair.getKey());
                if (extDiffBA1.editMapNew.get(modifiedPartner) != null && extDiffBA1.editMapNew
                    .get(modifiedPartner).getType() == EditOperationType.INSERT) {
                  withoutPartner.add(pair.getKey());
                  continue;
                }
              }
              withPartner.add(pair.getKey());
            }
          }
        }
      }
      if (hasDelete) {
        AbstractBastNode pattern =
            stmt.getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField();
        if (pattern != null) {
          pattern.replaceField(BastFieldConstants.ARES_PATTERN_CLAUSE_IDENT,
              new BastField((AbstractBastNode) null));
        }
        continue;
      }
      if (withoutPartner.size() > 0) {
        int withLarger = 0;
        int withSmaller = 0;
        for (AbstractBastNode withNode : withPartner) {
          for (AbstractBastNode withoutNode : withoutPartner) {
            NodeParentInformationHierarchy npiWith = hierarchyA1.get(withNode);
            if (npiWith != null) {
              NodeParentInformationHierarchy npiWithout = hierarchyA1.get(withoutNode);
              if (npiWithout != null) {
                if (npiWith.list.size() > 0 && npiWithout.list.size() > 0) {
                  if (npiWith.list.get(0).listId > npiWithout.list.get(0).listId) {
                    withLarger++;
                  } else {
                    withSmaller++;
                  }
                }
              }
            } else {
              npiWith = hierarchyA2.get(withNode);
              if (npiWith != null) {
                NodeParentInformationHierarchy npiWithout = hierarchyA2.get(withoutNode);
                if (npiWithout != null) {
                  if (npiWith.list.size() > 0 && npiWithout.list.size() > 0) {
                    if (npiWith.list.get(0).listId > npiWithout.list.get(0).listId) {
                      withLarger++;
                    } else {
                      withSmaller++;
                    }
                  }
                }
              }
            }
          }
        }
        if (withLarger > 0 && withSmaller == 0) {
          StringBuffer bufferTmp = PatternGenerator.extractIndentation(stmt);
          AresUseStmt newUse =
              CreateJavaNodeHelper.createAresUse(bufferTmp.toString(), null, null, null, null);
          AddUseVisitor auv = new AddUseVisitor(stmt, newUse, true);
          example2Modified.accept(auv);
          for (AbstractBastNode tmp : withoutPartner) {
            modifiedRes.replaceMap.put(tmp, newUse, false);
          }
          addedUses.add(newUse);
        } else if (withSmaller > 0 && withLarger == 0) {
          StringBuffer bufferTmp = PatternGenerator.extractIndentation(stmt);
          AresUseStmt newUse =
              CreateJavaNodeHelper.createAresUse(bufferTmp.toString(), null, null, null, null);
          AddUseVisitor auv = new AddUseVisitor(stmt, newUse, false);
          example2Modified.accept(auv);
          for (AbstractBastNode tmp : withoutPartner) {
            modifiedRes.replaceMap.put(tmp, newUse, false);
          }
          addedUses.add(newUse);
        }
      }
    }

  }

  private static AbstractBastNode combineEmptyExprUses(AbstractBastNode example2Modified,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyA2, ReplaceMap replaceMap,
      ConnectWildcardAndUseVisitor cwuv, HashSet<AbstractBastNode> addedUses,
      ExtendedDiffResult extDiff) {
    CombineWuvisitor useVisitor =
        new CombineWuvisitor(replaceMap, null, cwuv, true, AresUseStmt.TAG, true, null, extDiff);
    useVisitor.usesToIgnore(addedUses);
    example2Modified.accept(useVisitor);
    return example2Modified;
  }

  @SuppressWarnings("unchecked")
  private static void removeBorderWildcardsAndUses(AbstractBastNode example2Original,
      AbstractBastNode example2Modified, MatchBoundary boundary) throws GeneralizationException {
    HashSet<String> names = new HashSet<>();
    FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(AresBlock.TAG);
    example2Original.accept(fnft);
    if (fnft.nodes.isEmpty()) {
      return;
    }
    AresBlock lblock = (AresBlock) fnft.nodes.get(0);
    assert (lblock != null);
    LinkedList<AbstractBastStatement> stmts =
        (LinkedList<AbstractBastStatement>) lblock.getField(BastFieldConstants.ARES_BLOCK_BLOCK)
            .getField().getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
    AresWildcard[] scannedWStmts = new AresWildcard[stmts.size()];
    for (int i = 0; i < stmts.size(); i++) {
      if (stmts.get(i).getTag() == AresWildcard.TAG) {
        AresWildcard wildcard = (AresWildcard) stmts.get(i);
        if (wildcard.plugin.ident.name.equals("stmt")) {
          scannedWStmts[i] = wildcard;

        } else if (i == stmts.size() - 1) {
          scannedWStmts[i] = wildcard;
        }
      }
    }
    for (int i = 0; i < scannedWStmts.length; i++) {
      if (scannedWStmts[i] == null) {
        break;
      }
      stmts.remove(scannedWStmts[i]);
      if (scannedWStmts[i].plugin.pattern != null) {
        names.add(scannedWStmts[i].plugin.pattern.ident.name);
      } else {
        if (scannedWStmts[i].plugin.exprList != null) {
          names.add(((AresPatternClause) (scannedWStmts[i].plugin.exprList.get(0))).ident.name);
        }
      }
    }
    for (int i = scannedWStmts.length - 1; i >= 0; i--) {
      if (scannedWStmts[i] == null) {
        break;
      }
      stmts.remove(scannedWStmts[i]);
      if (scannedWStmts[i].plugin.pattern != null) {
        names.add(scannedWStmts[i].plugin.pattern.ident.name);
      } else {
        if (scannedWStmts[i].plugin.exprList != null) {
          names.add(((AresPatternClause) (scannedWStmts[i].plugin.exprList.get(0))).ident.name);
        }
      }
    }
    lblock.block.replaceField(BastFieldConstants.BLOCK_STATEMENT, new BastField(stmts));
    fnft = new FindNodesFromTagVisitor(AresBlock.TAG);
    example2Modified.accept(fnft);
    if (fnft.nodes.size() == 0) {
      throw new GeneralizationException("No block found.");
    }
    lblock = (AresBlock) fnft.nodes.get(0);
    assert (lblock != null);
    stmts = (LinkedList<AbstractBastStatement>) lblock.getField(BastFieldConstants.ARES_BLOCK_BLOCK)
        .getField().getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
    if (stmts == null) {
      return;
    }
    AresUseStmt[] scannedUStmts = new AresUseStmt[stmts.size()];
    for (int i = 0; i < stmts.size(); i++) {
      if (stmts.get(i).getTag() == AresUseStmt.TAG) {
        AresUseStmt use = (AresUseStmt) stmts.get(i);
        if (WildcardAccessHelper.getName(use) != null
            && names.contains(WildcardAccessHelper.getName(use).name)) {
          scannedUStmts[i] = use;

        }
      }
    }
    for (int i = 0; i < scannedUStmts.length; i++) {
      if (scannedUStmts[i] == null) {
        continue;
      }
      boolean remove = true;
      for (int j = i; j < scannedUStmts.length; j++) {
        if (scannedUStmts[j] == null) {
          remove = false;
        }
      }
      if (remove) {
        stmts.remove(scannedUStmts[i]);
      } else {
        AbstractBastNode pattern =
            scannedUStmts[i].getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField();
        if (pattern != null) {
          pattern.replaceField(BastFieldConstants.ARES_PATTERN_CLAUSE_IDENT,
              new BastField((AbstractBastNode) null));
        }
      }
    }

    lblock.block.replaceField(BastFieldConstants.BLOCK_STATEMENT, new BastField(stmts));
  }

  private static AbstractBastNode insertChoices(ExtendedDiffResult extDiffAa,
      ExtendedDiffResult exDiffBA1, ExtendedDiffResult exDiffBA2, AbstractBastNode example2Modified,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyB2, ReplaceMap replaceMap,
      HashMap<AbstractBastNode, List<RevertModificationInfo>> revertMap,
      HashMap<AbstractBastNode, AbstractBastNode> delInsertMap, MatchBoundary boundary) {
    FindNodesFromTagVisitor findAresBlockV = new FindNodesFromTagVisitor(AresBlock.TAG);
    boundary.getNode2().accept(findAresBlockV);

    CollectEmptyUsesVisitor emptyUsesV = new CollectEmptyUsesVisitor();
    example2Modified.accept(emptyUsesV);
    ArrayList<AresUseStmt> emptyUseStmts = new ArrayList<AresUseStmt>();
    emptyUseStmts.addAll(emptyUsesV.uses);
    LinkedList<AresChoiceStmt> insertedChoices = new LinkedList<AresChoiceStmt>();
    for (AresUseStmt useStmt : emptyUseStmts) {
      PatternGenerator.print(example2Modified);
      emptyUsesV = new CollectEmptyUsesVisitor();
      example2Modified.accept(emptyUsesV);
      AbstractBastNode parent = emptyUsesV.useParents.get(useStmt);
      NodeIndex useIndex = emptyUsesV.usePosition.get(useStmt);
      if (useIndex == null) {
        continue;
      }
      int searchIndex = useIndex.childrenListIndex;
      AbstractBastNode otherParent = extDiffAa.secondToFirstMap.get(parent);
      BastFieldConstants otherField = null;

      if (parent.getTag() == AresBlock.TAG) {
        for (BastEditOperation ep : extDiffAa.editScript) {
          if (ep.getType() == EditOperationType.MOVE
              && ep.getUnchangedOrNewParentNode() == parent) {
            otherParent = ep.getUnchangedOrOldParentNode();
            otherField = ep.getOldOrChangedIndex().childrenListNumber;
            break;
          }
        }
      }

      ArrayList<BastEditOperation> eops = new ArrayList<>();
      ArrayList<BastEditOperation> moveOps = new ArrayList<>();

      for (BastEditOperation ep : extDiffAa.editScript) {
        if (ep.getType() != EditOperationType.UPDATE
            && ep.getType() != EditOperationType.METHOD_RENAME
            && ep.getType() != EditOperationType.STATEMENT_UPDATE) {
          if (ep.getUnchangedOrOldParentNode() == parent) {
            if (ep.getOldOrChangedIndex().childrenListIndex == useIndex.childrenListIndex
                && ep.getOldOrChangedIndex().childrenListNumber == useIndex.childrenListNumber) {
              if (ep.getOldOrInsertedNode().getTag() != AresUseStmt.TAG
                  && ep.getOldOrInsertedNode().getTag() != AresChoiceStmt.TAG) {
                eops.add(ep);
              }
            }
          }
        }
      }
      ArrayList<BastEditOperation> eopsOther = new ArrayList<>();
      if (otherParent == null || otherField == null || otherParent.getField(otherField) == null
          || !otherParent.getField(otherField).isList()) {
        // do nothing
      } else {
        @SuppressWarnings("unchecked")
        LinkedList<AbstractBastNode> fieldStmts =
            (LinkedList<AbstractBastNode>) otherParent.getField(otherField).getListField();
        int otherIndex =
            getOtherIndex(fieldStmts, parent, otherParent, otherField, useIndex, extDiffAa);
        if (eops.isEmpty()) {
          for (BastEditOperation ep : extDiffAa.editScript) {
            if (ep.getType() != EditOperationType.UPDATE) {
              if (ep.getUnchangedOrOldParentNode() == otherParent) {
                if (ep.getOldOrChangedIndex().childrenListIndex == otherIndex
                    && otherIndex != useIndex.childrenListIndex
                    && ep.getOldOrChangedIndex().childrenListNumber == otherField) {
                  eopsOther.add(ep);

                }
              }
            }
          }
        } else {
          for (BastEditOperation ep : extDiffAa.editScript) {
            if (ep.getType() != EditOperationType.UPDATE
                && ep.getType() != EditOperationType.METHOD_RENAME) {
              if (ep.getUnchangedOrOldParentNode() == otherParent) {
                if (ep.getOldOrChangedIndex().childrenListIndex == otherIndex
                    && otherIndex != useIndex.childrenListIndex
                    && ep.getOldOrChangedIndex().childrenListNumber == otherField) {
                  eopsOther.add(ep);

                }
              }
            }
          }
        }
      }
      LinkedList<AbstractBastStatement> choices = new LinkedList<>();
      LinkedList<AbstractBastStatement> toRemove = new LinkedList<>();
      AresCaseStmt caseStmt = null;
      LinkedList<AbstractBastStatement> stmts = null;
      LinkedList<AbstractBastStatement> delStmt = new LinkedList<>();
      LinkedList<AbstractBastNode> nodesToSkipAtEndOfFile = new LinkedList<>();
      AresPatternClause patternClause =
          (AresPatternClause) useStmt.getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField();
      if (patternClause != null && patternClause.expr != null) {
        eops.clear();
        eopsOther.clear();
      }
      if (eops.size() == 0) {
        if (moveOps.size() > 0 && patternClause == null) {
          stmts = new LinkedList<>();
          AbstractBastNode partner =
              extDiffAa.firstToSecondMap.get(moveOps.get(0).getUnchangedOrOldParentNode());
          boolean skip = false;
          if (partner != null) {
            AbstractBastNode partnerNode = WildcardAccessHelper.getNodeToIndex(partner,
                (moveOps.get(0).getOldOrChangedIndex()));
            if (partnerNode != null) {
              FindNodesFromTagVisitor fnft =
                  new FindNodesFromTagVisitor(moveOps.get(0).getNewOrChangedNode().getTag());
              partnerNode.accept(fnft);
              if (fnft.nodes.contains(moveOps.get(0).getNewOrChangedNode())) {
                skip = true;
              }
            }
          }

          if (!skip) {
            if ((AbstractBastStatement) moveOps.get(0).getOldOrInsertedNode() != null) {
              stmts.add((AbstractBastStatement) moveOps.get(0).getOldOrInsertedNode());
            }
            caseStmt = CreateJavaNodeHelper.createAresCase("", useStmt.info.tokens[0], stmts);
            choices.add(caseStmt);
          }
        } else {
          boolean sort = true;
          stmts = new LinkedList<>();
          if (parent.getField(useIndex.childrenListNumber).isList() && searchIndex + 1 < parent
              .getField(useIndex.childrenListNumber).getListField().size()) {
            if (patternClause != null && patternClause.expr != null) {
              AbstractBastStatement st = (AbstractBastStatement) parent
                  .getField(useIndex.childrenListNumber).getListField().get(searchIndex + 1);
              List<RevertModificationInfo> modInfoList = revertMap.get(st);
              if (modInfoList != null) {
                for (RevertModificationInfo modInfo : modInfoList) {
                  modInfo.revert();
                }
              }
              if (st != null) {
                stmts.add(st);
              }
              LinkedList<AbstractBastNode> exprNodes = new LinkedList<AbstractBastNode>();
              AbstractBastNode oldSt = st;
              for (int i = searchIndex - 1; i >= 0; i--) {
                if (parent.getField(useIndex.childrenListNumber).getListField().get(i)
                    .getTag() == AresUseStmt.TAG) {
                  AresUseStmt oldUse = (AresUseStmt) parent.getField(useIndex.childrenListNumber)
                      .getListField().get(i);
                  if (WildcardAccessHelper.getName(oldUse) != null && oldUse
                      .getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField() != null) {
                    if (WildcardAccessHelper.isPart(st, WildcardAccessHelper.getExpr(oldUse))) {
                      exprNodes.addFirst(oldUse);
                      toRemove.add(oldUse);
                    }
                  } else {
                    break;
                  }
                } else {
                  break;
                }
              }
              for (int i = exprNodes.size() - 1; i >= 0; i--) {
                AbstractBastStatement myUse =
                    (AbstractBastStatement) CreateJavaNodeHelper.cloneTree(exprNodes.get(i));
                ((JavaToken) myUse.info.tokens[0].token).whiteSpace
                    .append(((JavaToken) useStmt.info.tokens[0].token).whiteSpace);
                stmts.addFirst(myUse);
              }
              caseStmt = CreateJavaNodeHelper.createAresCase("", useStmt.info.tokens[0], stmts);
              choices.add(caseStmt);
              LinkedList<AbstractBastStatement> oldStmts = stmts;
              stmts = new LinkedList<>();
              if (extDiffAa.secondToFirstMap.get(st) == null) {
                if (delInsertMap.get(st) != null) {
                  int indexInsert = -1;
                  int indexDelete = -1;
                  BastEditOperation epIns = extDiffAa.editMapOld.get(st);

                  if (epIns != null) {
                    indexInsert = epIns.getOldOrChangedIndex().childrenListIndex;
                  }
                  BastEditOperation epDel = extDiffAa.editMapOld.get(delInsertMap.get(st));

                  if (epDel != null) {
                    indexDelete = epDel.getOldOrChangedIndex().childrenListIndex;
                  }
                  int diff = 0;
                  if (epIns != null) {
                    for (int i = epIns.getOldOrChangedIndex().childrenListIndex - 1; i >= 0; i--) {
                      if (epIns.getUnchangedOrOldParentNode()
                          .getField(epIns.getOldOrChangedIndex().childrenListNumber).getListField()
                          .size() > i
                          && WildcardAccessHelper.isWildcard(epIns.getUnchangedOrOldParentNode()
                              .getField(epIns.getOldOrChangedIndex().childrenListNumber)
                              .getListField().get(i))) {
                        diff++;
                      } else {
                        continue;
                      }
                    }
                  }
                  if (indexInsert == indexDelete || indexInsert + diff == indexDelete) {
                    st = (AbstractBastStatement) delInsertMap.get(st);
                  }

                } else {
                  if (extDiffAa.firstToSecondMap.get(st) != null) {
                    st = (AbstractBastStatement) extDiffAa.firstToSecondMap.get(st);
                  } else {
                    st = null;
                  }
                }
              } else {
                st = (AbstractBastStatement) extDiffAa.secondToFirstMap.get(st);
              }
              if (st != null) {
                if (WildcardAccessHelper.isEqual(oldSt, st)) {
                  BastField field = parent.getField(useIndex.childrenListNumber);
                  @SuppressWarnings("unchecked")
                  LinkedList<AbstractBastStatement> list =
                      (LinkedList<AbstractBastStatement>) field.getListField();
                  list.remove(useStmt);
                  parent.replaceField(useIndex.childrenListNumber, new BastField(list));
                  continue;
                }
                if (extDiffAa.editMapOld.get(st) == null
                    || extDiffAa.editMapOld.get(st).getType() != EditOperationType.MOVE
                    || extDiffAa.editMapOld.get(st)
                        .getUnchangedOrOldParentNode() == extDiffAa.secondToFirstMap
                            .get(extDiffAa.editMapOld.get(st).getUnchangedOrNewParentNode())
                    || (extDiffAa.editMapOld.get(st).getUnchangedOrOldParentNode() == boundary
                        .getNode1()
                        || extDiffAa.editMapOld.get(st).getUnchangedOrNewParentNode() == boundary
                            .getNode2())) {
                  stmts.add(st);
                }
                for (int i = exprNodes.size() - 1; i >= 0; i--) {
                  AbstractBastStatement myUse =
                      (AbstractBastStatement) CreateJavaNodeHelper.cloneTree(exprNodes.get(i));
                  ((JavaToken) myUse.info.tokens[0].token).whiteSpace
                      .append(((JavaToken) useStmt.info.tokens[0].token).whiteSpace);
                  if (WildcardAccessHelper.isPart(st, WildcardAccessHelper.getExpr(myUse))) {
                    stmts.addFirst(myUse);
                  }
                }
                sort = false;

              } else {
                StatementInsertOperation stiOp = null;
                for (BastEditOperation editOperation : extDiffAa.editScript) {
                  if (editOperation.getType() == EditOperationType.STATEMENT_INSERT
                      && editOperation.getOldOrInsertedNode() == (AbstractBastStatement) parent
                          .getField(useIndex.childrenListNumber).getListField()
                          .get(searchIndex + 1)) {
                    stiOp = (StatementInsertOperation) editOperation;
                    break;
                  }
                }
                if (stiOp != null) {
                  AbstractBastNode parentPartner =
                      extDiffAa.secondToFirstMap.get(stiOp.getUnchangedOrOldParentNode());
                  if (parentPartner != null) {
                    for (BastEditOperation editOperation : extDiffAa.editScript) {
                      if (editOperation.getType() == EditOperationType.STATEMENT_DELETE
                          && editOperation.getUnchangedOrOldParentNode() == parentPartner
                          && editOperation.getOldOrInsertedNode().getTag() == stiOp
                              .getOldOrInsertedNode().getTag()) {
                        st = (AbstractBastStatement) editOperation.getOldOrInsertedNode();
                        stmts.add(st);
                        break;
                      }

                    }
                  }
                }
                if (stmts.size() == 0) {
                  stmts = oldStmts;
                  choices.clear();
                }
              }
            } else {
              if (eopsOther.size() == 0) {
                AbstractBastStatement potentialPartner = null;
                for (Entry<AbstractBastNode, AbstractBastNode> node : replaceMap.entrySet()) {
                  if (node.getValue() == useStmt && !replaceMap.belongsToExpr(node.getKey())) {
                    if (node.getKey() != null) {
                      boolean found = false;
                      BastEditOperation editOperation = extDiffAa.editMapOld.get(node.getKey());
                      if (editOperation != null) {
                        if ((editOperation.getType() == EditOperationType.STATEMENT_DELETE
                            || editOperation.getType() == EditOperationType.DELETE)) {
                          delStmt.add((AbstractBastStatement) node.getKey());
                          found = true;

                        } else if ((editOperation.getType() == EditOperationType.MOVE)) {

                          delStmt.add((AbstractBastStatement) node.getKey());
                          found = true;

                        } else if ((editOperation.getType() == EditOperationType.ALIGN)) {

                          if (delStmt.size() > 0) {
                            delStmt.add((AbstractBastStatement) node.getKey());
                          }
                          int count = 0;
                          for (Entry<AbstractBastNode, AbstractBastNode> etmp : replaceMap
                              .entrySet()) {
                            if (etmp.getValue() == useStmt
                                && replaceMap.belongsToExpr(etmp.getKey())) {
                              count++;
                            }

                          }
                          if (count > 0) {
                            if (editOperation.getUnchangedOrNewParentNode()
                                .getTag() == BastBlock.TAG) {
                              delStmt.add((AbstractBastStatement) node.getKey());
                              if (editOperation.getUnchangedOrOldParentNode()
                                  .getTag() == BastBlock.TAG) {
                                AbstractBastNode replacedNode = WildcardAccessHelper.getNodeToIndex(
                                    editOperation.getUnchangedOrOldParentNode(),
                                    editOperation.getOldOrChangedIndex().childrenListNumber,
                                    editOperation.getNewOrChangedIndex().childrenListIndex);
                                if (replacedNode != null) {
                                  delStmt.add((AbstractBastStatement) replacedNode);
                                }
                              }
                            }
                          }
                          if (editOperation.getUnchangedOrNewParentNode()
                              .getTag() == BastBlock.TAG) {
                            stmts.add((AbstractBastStatement) editOperation.getNewOrChangedNode());

                          }
                          found = true;
                        }
                      } else {
                        editOperation = extDiffAa.editMapNew.get(node.getKey());
                        if (editOperation != null) {
                          if ((editOperation.getType() == EditOperationType.MOVE)) {
                            stmts.add((AbstractBastStatement) editOperation.getOldOrInsertedNode());
                            found = true;
                          }
                        }
                      }
                      if (!found) {
                        stmts.add((AbstractBastStatement) node.getKey());
                        if (extDiffAa.secondToFirstMap.get(node.getKey()) != null) {
                          if (extDiffAa.secondToFirstMap
                              .get(node.getKey()) instanceof AbstractBastStatement) {
                            if (delStmt.size() == 0) {
                              potentialPartner = (AbstractBastStatement) extDiffAa.secondToFirstMap
                                  .get(node.getKey());
                            }
                          }
                        }
                      }
                    }
                  }
                }
                if (delStmt.size() == 0 && potentialPartner != null) {
                  delStmt.add(potentialPartner);
                }
              } else {
                stmts.add((AbstractBastStatement) parent.getField(useIndex.childrenListNumber)
                    .getListField().get(searchIndex + 1));
              }
            }
          } else {
            if (eopsOther.size() == 0) {
              LinkedList<Entry<AbstractBastNode, AbstractBastNode>> list =
                  new LinkedList<>(replaceMap.entrySet());
              Collections.sort(list, new WildcardEntryComparator());
              for (Entry<AbstractBastNode, AbstractBastNode> node : list) {
                if (node.getValue() == useStmt && !replaceMap.belongsToExpr(node.getKey())) {
                  if (node.getKey() != null) {
                    boolean found = false;
                    BastEditOperation editOperation = extDiffAa.editMapOld.get(node.getKey());
                    if (editOperation != null) {
                      if ((editOperation.getType() == EditOperationType.STATEMENT_DELETE
                          || editOperation.getType() == EditOperationType.DELETE)) {
                        delStmt.add((AbstractBastStatement) node.getKey());
                        found = true;
                      } else if ((editOperation.getType() == EditOperationType.MOVE)) {
                        for (AbstractBastNode deltmp : delStmt) {
                          if (WildcardAccessHelper.isEqual(deltmp, node.getKey())) {
                            found = true;
                          }
                        }
                        if (!found) {
                          delStmt.add((AbstractBastStatement) node.getKey());
                          if (editOperation.getUnchangedOrNewParentNode()
                              .getTag() == BastBlock.TAG) {
                            stmts.add((AbstractBastStatement) editOperation.getNewOrChangedNode());
                          }
                          found = true;
                        }

                      }
                    }
                    if (!found) {
                      if (findAresBlockV.nodes.size() != 1) {
                        continue;
                      }
                      assert (findAresBlockV.nodes.size() == 1);
                      AresBlock aresBlock = (AresBlock) findAresBlockV.nodes.get(0);
                      if (aresBlock.block == parent) {
                        if (aresBlock.block.statements.getLast() == useStmt) {
                          AbstractBastNode partnerNode =
                              exDiffBA1.secondToFirstMap.get(node.getKey());
                          if (partnerNode == null) {
                            partnerNode = exDiffBA2.secondToFirstMap.get(node.getKey());
                          }
                          if (partnerNode != null) {
                            if (WildcardAccessHelper.isEqual(node.getKey(), partnerNode)) {
                              nodesToSkipAtEndOfFile.add(node.getKey());
                            }
                          }
                        }
                      }
                      if (extDiffAa.editMapNew.get(node.getKey()) != null
                          || extDiffAa.secondToFirstMap.get(node.getKey()) != null) {
                        stmts.add((AbstractBastStatement) node.getKey());
                        if (extDiffAa.secondToFirstMap.get(node.getKey()) != null) {
                          if (extDiffAa.secondToFirstMap
                              .get(node.getKey()) instanceof AbstractBastStatement) {
                            delStmt.add((AbstractBastStatement) extDiffAa.secondToFirstMap
                                .get(node.getKey()));
                          }
                        }
                      } else {
                        delStmt.add((AbstractBastStatement) node.getKey());
                        if (extDiffAa.firstToSecondMap.get(node.getKey()) != null) {
                          if (extDiffAa.firstToSecondMap
                              .get(node.getKey()) instanceof AbstractBastStatement) {
                            stmts.add((AbstractBastStatement) extDiffAa.firstToSecondMap
                                .get(node.getKey()));
                          }
                        }
                      }
                    }
                  }
                }
              }

            } else {
              assert (false);
            }

          }
          if (stmts.size() == 0) {
            for (BastEditOperation ep : extDiffAa.editScript) {
              if (ep.getType() == EditOperationType.MOVE
                  || ep.getType() == EditOperationType.ALIGN) {
                for (AbstractBastNode node : delStmt) {
                  if (WildcardAccessHelper.isPart(node, ep.getOldOrInsertedNode())) {
                    if (ep.getUnchangedOrNewParentNode() == parent
                        || boundary.getNode2() == ep.getUnchangedOrNewParentNode()) {
                      stmts.add((AbstractBastStatement) ep.getNewOrChangedNode());
                    }
                  }
                }
              }
            }
          }
          for (int i = 0; i < stmts.size(); i++) {
            RestoreChoicePartsVisitor rcv = new RestoreChoicePartsVisitor(extDiffAa, replaceMap);
            if (stmts.get(i) != null) {
              stmts.get(i).accept(rcv);
            }
          }
          if (parent.getField(useIndex.childrenListNumber).isList()) {
            @SuppressWarnings("unchecked")
            LinkedList<AbstractBastNode> nodes = (LinkedList<AbstractBastNode>) parent
                .getField(useIndex.childrenListNumber).getListField();
            if (searchIndex + 1 < nodes.size() && stmts.size() > 0) {
              if (WildcardAccessHelper.isExprWildcard(nodes.get(searchIndex))) {
                nodes.remove(searchIndex + 1);
              }
            }
          }
          if (stmts.size() > 0) {
            if (sort) {
              Collections.sort(stmts, new NodeComparator());
            }
            LinkedList<AbstractBastStatement> reducedStmts = new LinkedList<>();
            for (int i = 0; i < stmts.size(); i++) {
              if (nodesToSkipAtEndOfFile.contains(stmts.get(i))) {
                break;
              } else {
                reducedStmts.add(stmts.get(i));
              }
            }
            stmts = reducedStmts;
            LinkedList<AbstractBastNode> duplicates = new LinkedList<AbstractBastNode>();
            BastField field = parent.getField(useIndex.childrenListNumber);
            @SuppressWarnings("unchecked")
            LinkedList<AbstractBastStatement> list =
                (LinkedList<AbstractBastStatement>) field.getListField();
            for (AbstractBastNode st : stmts) {
              for (int i = useIndex.childrenListIndex; i < list.size(); i++) {
                if (WildcardAccessHelper.isEqual(st, list.get(i))) {
                  toRemove.add(list.get(i));
                }
              }
            }
            stmts.removeAll(duplicates);
            if (stmts.size() > 0) {
              caseStmt = CreateJavaNodeHelper.createAresCase("", useStmt.info.tokens[0], stmts);
              choices.add(caseStmt);
            }
          }
          if (delStmt.size() > 0) {
            Collections.sort(delStmt, new NodeComparator());

            LinkedList<AbstractBastNode> duplicates = new LinkedList<AbstractBastNode>();
            BastField field = parent.getField(useIndex.childrenListNumber);
            if (field.isList()) {
              @SuppressWarnings("unchecked")
              LinkedList<AbstractBastStatement> list =
                  (LinkedList<AbstractBastStatement>) field.getListField();
              for (AbstractBastNode st : delStmt) {
                for (int i = useIndex.childrenListIndex; i < list.size(); i++) {
                  if (WildcardAccessHelper.isEqual(st, list.get(i))) {
                    toRemove.add(list.get(i));
                  }
                }
              }
            }
            delStmt.removeAll(duplicates);
            if (delStmt.size() > 0) {
              caseStmt = CreateJavaNodeHelper.createAresCase("", useStmt.info.tokens[0], delStmt);
              choices.add(caseStmt);
            }
          }
        }
      } else {
        LinkedList<AbstractBastStatement> stmtsDelete = new LinkedList<>();
        switch (eops.get(0).getType()) {
          case INSERT:
          case STATEMENT_INSERT:

            stmts = new LinkedList<>();

            stmts.add((AbstractBastStatement) eops.get(0).getOldOrInsertedNode());
            final AbstractBastNode unchangedParent = eops.get(0).getUnchangedOrOldParentNode();
            LinkedList<Entry<AbstractBastNode, AbstractBastNode>> list =
                new LinkedList<>(replaceMap.entrySet());
            Collections.sort(list, new WildcardEntryComparator());
            for (Entry<AbstractBastNode, AbstractBastNode> pair : list) {
              if (pair.getValue() == useStmt) {
                if (!stmts.contains((AbstractBastStatement) pair.getKey())) {
                  boolean found = false;
                  for (BastEditOperation other : eopsOther) {
                    if (other.getOldOrInsertedNode() == pair.getKey()) {
                      found = true;
                    }
                  }
                  if (!found) {
                    boolean deleteMove = false;

                    BastEditOperation ep = extDiffAa.editMapOld.get(pair.getKey());
                    if (ep != null && (ep.getType() == EditOperationType.DELETE
                        || ep.getType() == EditOperationType.STATEMENT_DELETE)) {
                      deleteMove = true;
                    }
                    if (ep != null && ep.getType() == EditOperationType.MOVE) {
                      deleteMove = true;
                    }

                    if (!deleteMove) {
                      stmts.add((AbstractBastStatement) pair.getKey());
                    } else {
                      stmtsDelete.add((AbstractBastStatement) pair.getKey());
                    }
                  }
                }
              }
            }
            LinkedList<AbstractBastNode> duplicates = new LinkedList<AbstractBastNode>();
            BastField field = parent.getField(useIndex.childrenListNumber);
            @SuppressWarnings("unchecked")
            LinkedList<AbstractBastStatement> list2 =
                (LinkedList<AbstractBastStatement>) field.getListField();
            for (AbstractBastNode st : stmts) {
              for (int i = useIndex.childrenListIndex; i < list2.size(); i++) {
                if (WildcardAccessHelper.isEqual(st, list2.get(i))) {
                  toRemove.add(list2.get(i));
                }
              }
            }
            stmts.removeAll(duplicates);
            LinkedList<AbstractBastNode> toRemoveExpr = new LinkedList<>();
            for (int i = 0; i < stmts.size(); i++) {
              for (int j = 0; j < stmts.size(); j++) {
                if (i == j) {
                  continue;
                }
                FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(stmts.get(j).getTag());
                stmts.get(i).accept(fnft);
                if (fnft.nodes.contains(stmts.get(j)) && !toRemoveExpr.contains(stmts.get(j))) {
                  toRemoveExpr.add(stmts.get(j));
                }
              }
            }
            stmts.removeAll(toRemoveExpr);
            if (unchangedParent.getTag() == AresBlock.TAG) {
              Collections.sort(stmts, new MyListComparator());
            } else if (unchangedParent.getTag() == BastBlock.TAG) {
              Collections.sort(stmts, new MyListComparator());
            } else if (unchangedParent.getTag() == BastSwitchCaseGroup.TAG) {
              Collections.sort(stmts, new MyListComparator());
            } else {
              return example2Modified;
            }
            if (stmts.size() > 0) {
              caseStmt = CreateJavaNodeHelper.createAresCase("", useStmt.info.tokens[0], stmts);
              choices.add(caseStmt);
            }
            emptyOpsOther(extDiffAa, delInsertMap, boundary, useStmt, useIndex, eopsOther, choices,
                stmts, delStmt, stmtsDelete);
            stmts = new LinkedList<AbstractBastStatement>();
            break;
          default:
            break;
        }
      }
      if (eopsOther.size() > 0) {
        switch (eopsOther.get(0).getType()) {
          case DELETE:
          case STATEMENT_DELETE:
          case MOVE:
            stmts = new LinkedList<>();
            for (int i = 0; i < eopsOther.size(); i++) {
              AbstractBastNode tmp = eopsOther.get(i).getOldOrInsertedNode();

              stmts.add((AbstractBastStatement) tmp);
            }
            if (stmts.size() > 0) {
              caseStmt = CreateJavaNodeHelper.createAresCase("", useStmt.info.tokens[0], stmts);
              choices.add(caseStmt);
            }
            break;
          default:
            break;
        }
      } else {
        if (choices.size() < 2) {
          emptyOpsOther(extDiffAa, delInsertMap, boundary, useStmt, useIndex, eopsOther, choices,
              stmts, delStmt, null);
        }
      }
      AresChoiceStmt choice = null;
      if (useIndex.childrenListNumber.isList) {

        BastField field = parent.getField(useIndex.childrenListNumber);
        @SuppressWarnings("unchecked")
        LinkedList<AbstractBastStatement> list =
            (LinkedList<AbstractBastStatement>) field.getListField();
        if (choices.size() > 0) {
          choice = CreateJavaNodeHelper.createAresChoice("", useStmt.info.tokens[0], choices);
        }
        if (choice != null && useIndex.childrenListIndex >= list.size()) {
          list.add(choice);
        } else {
          if (useIndex.childrenListIndex < list.size()) {
            if (choice != null && choice.choiceBlock != null
                && choice.choiceBlock.statements != null
                && choice.choiceBlock.statements.size() == 1
                && list.get(useIndex.childrenListIndex) == useStmt
                && useIndex.childrenListIndex + 1 < list.size()
                && list.get(useIndex.childrenListIndex + 1).getTag() != AresUseStmt.TAG
                && WildcardAccessHelper.isExprWildcard(useStmt)) {
              list.remove(useIndex.childrenListIndex + 1);
            }
            list.remove(useIndex.childrenListIndex);

          }
          if (list.size() > useIndex.childrenListIndex
              && list.get(useIndex.childrenListIndex).getTag() == AresChoiceStmt.TAG) {
            if (!insertedChoices.contains(list.get(useIndex.childrenListIndex))) {
              LinkedList<AbstractBastStatement> oldStmts =
                  ((AresChoiceStmt) list.get(useIndex.childrenListIndex)).choiceBlock.statements;
              LinkedList<AbstractBastStatement> newStmts = new LinkedList<>();
              newStmts.addAll(choices);
              newStmts.addAll(oldStmts);
              ((AresChoiceStmt) list.get(useIndex.childrenListIndex)).choiceBlock
                  .replaceField(BastFieldConstants.BLOCK_STATEMENT, new BastField(newStmts));
              insertedChoices.add((AresChoiceStmt) list.get(useIndex.childrenListIndex));
            } else {
              if (choice != null) {
                list.add(useIndex.childrenListIndex, choice);
              }
            }
          } else {
            if (choice != null) {
              list.add(useIndex.childrenListIndex, choice);
            }
          }
        }
        if (choice != null) {
          insertedChoices.add(choice);
        }
        list.removeAll(toRemove);
        parent.replaceField(useIndex.childrenListNumber, new BastField(list));
      }
    }
    return example2Modified;
  }

  private static void emptyOpsOther(ExtendedDiffResult extDiffAa,
      HashMap<AbstractBastNode, AbstractBastNode> delInsertMap, MatchBoundary boundary,
      AresUseStmt useStmt, NodeIndex useIndex, ArrayList<BastEditOperation> eopsOther,
      LinkedList<AbstractBastStatement> choices, LinkedList<AbstractBastStatement> stmts,
      LinkedList<AbstractBastStatement> delStmt, LinkedList<AbstractBastStatement> stmtsDelete) {
    AresCaseStmt caseStmt;
    LinkedList<AbstractBastNode> toRemoveExpr;
    if (stmtsDelete == null) {
      stmtsDelete = new LinkedList<>();
    }
    if (eopsOther.size() == 0) {
      LinkedList<AbstractBastStatement> newStmts = new LinkedList<>();
      for (BastEditOperation ep : extDiffAa.editScript) {
        if (ep.getType() == EditOperationType.DELETE
            || ep.getType() == EditOperationType.STATEMENT_DELETE) {
          if (stmts.contains(ep.getOldOrInsertedNode())
              || stmtsDelete.contains(ep.getOldOrInsertedNode())) {
            newStmts.add((AbstractBastStatement) ep.getOldOrInsertedNode());
            continue;
          }
        }
        if (ep.getType() == EditOperationType.MOVE) {
          if (stmts.contains(ep.getOldOrInsertedNode())
              || stmtsDelete.contains(ep.getOldOrInsertedNode())) {
            if (ep.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG) {
              newStmts.add((AbstractBastStatement) ep.getOldOrInsertedNode());
              continue;
            }
          }
        }
        if (delInsertMap.containsKey(ep.getOldOrInsertedNode())
            && stmts.contains(ep.getOldOrInsertedNode())) {
          newStmts.add((AbstractBastStatement) delInsertMap.get(ep.getOldOrInsertedNode()));
          continue;
        }
        if (delInsertMap.containsKey(ep.getNewOrChangedNode())
            && stmts.contains(ep.getNewOrChangedNode())) {
          newStmts.add((AbstractBastStatement) delInsertMap.get(ep.getNewOrChangedNode()));
          continue;
        }
      }
      if (newStmts.size() > 0) {

        stmts = newStmts;
        toRemoveExpr = new LinkedList<>();
        for (int i = 0; i < stmts.size(); i++) {
          for (int j = 0; j < stmts.size(); j++) {
            if (i == j) {
              continue;
            }
            FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(stmts.get(j).getTag());
            stmts.get(i).accept(fnft);
            if (fnft.nodes.contains(stmts.get(j)) && !toRemoveExpr.contains(stmts.get(j))) {
              toRemoveExpr.add(stmts.get(j));
            }
          }
        }
        Collections.sort(stmts, new MyListComparator());

        if (stmts.size() > 0) {
          caseStmt = CreateJavaNodeHelper.createAresCase("", useStmt.info.tokens[0], stmts);
          choices.add(caseStmt);
        }

      }

    }
  }

  private static HashSet<AbstractBastNode> splitAdvancedUses(AbstractBastNode example2Modified,
      Map<AresWildcard, List<AresUseStmt>> wildcardUseMap, ExtendedDiffResult exDiffBA1,
      ExtendedDiffResult exDiffBA2, InsertResult originalRes, InsertResult modifiedRes,
      ConnectWildcardAndUseVisitor cwuv) {

    HashSet<AbstractBastNode> addedUses = new HashSet<AbstractBastNode>();
    boolean splitNecessary = true;
    while (splitNecessary) {
      splitNecessary = false;
      HashMap<String, String> wildcardRenames = new HashMap<String, String>();
      for (Entry<AbstractBastNode, AbstractBastNode> entry : cwuv.nodeReplacements.entrySet()) {
        if (entry.getKey().getTag() == AresWildcard.TAG) {
          if (WildcardAccessHelper.getName(entry.getKey()) != null
              && WildcardAccessHelper.getName(entry.getValue()) != null) {
            wildcardRenames.put(WildcardAccessHelper.getName(entry.getKey()).name,
                WildcardAccessHelper.getName(entry.getValue()).name);
          }
        }
      }
      AresUseStmt useStmt = null;
      String name = null;
      AresUseStmt keyUseStmt = null;
      for (Entry<AbstractBastNode, AbstractBastNode> entry : cwuv.nodeReplacements.entrySet()) {
        if (entry.getKey().getTag() == AresUseStmt.TAG) {
          if (WildcardAccessHelper.getName(entry.getKey()) != null
              && WildcardAccessHelper.getName(entry.getValue()) != null) {
            if ((wildcardRenames.get(WildcardAccessHelper.getName(entry.getKey()).name)) != null) {
              if (!(wildcardRenames.get(WildcardAccessHelper.getName(entry.getKey()).name))
                  .equals(WildcardAccessHelper.getName(entry.getValue()).name)) {
                splitNecessary = true;
                useStmt = (AresUseStmt) entry.getValue();
                name = wildcardRenames.get(WildcardAccessHelper.getName(entry.getKey()).name);
                keyUseStmt = (AresUseStmt) entry.getKey();
              }
            }
          }

        }
      }

      if (splitNecessary) {
        if (useStmt == null) {
          return addedUses;
        }
        StringBuffer bufferTmp = PatternGenerator.extractIndentation(useStmt);
        AresUseStmt newUse = CreateJavaNodeHelper.createAresUse(bufferTmp.toString(), null,
            CreateJavaNodeHelper.createBastNameIdent(name), null, null);
        AddUseVisitor auv = new AddUseVisitor(useStmt, newUse, true);
        example2Modified.accept(auv);
        ArrayList<AresUseStmt> list = new ArrayList<AresUseStmt>();
        list.add(newUse);
        cwuv.nodeReplacements.put(keyUseStmt, newUse);
        addedUses.add(newUse);
      }

    }
    return addedUses;
  }

  private static void splitUses(AbstractBastNode example2Modified,
      Map<AresWildcard, List<AresUseStmt>> wildcardUseMap, ExtendedDiffResult exDiffBA1,
      ExtendedDiffResult exDiffBA2, InsertResult originalRes, InsertResult modifiedRes,
      ConnectWildcardAndUseVisitor cwuv) {
    boolean splitNecessary = true;
    while (splitNecessary) {
      splitNecessary = false;
      HashMap<AresUseStmt, AresWildcard> reversedMap = new HashMap<>();
      AresUseStmt useStmt = null;
      AresWildcard wildcard1 = null;

      for (Entry<AresWildcard, List<AresUseStmt>> entry : wildcardUseMap.entrySet()) {
        if (entry.getValue() != null) {
          for (AresUseStmt useS : entry.getValue()) {
            if (reversedMap.get(useS) != null) {
              useStmt = useS;
              wildcard1 = reversedMap.get(useS).nodeId < entry.getKey().nodeId
                  ? reversedMap.get(useS) : entry.getKey();
              splitNecessary = true;
              break;
            } else {
              reversedMap.put(useS, entry.getKey());
            }
          }
        }
      }
      if (splitNecessary) {
        if (useStmt == null) {
          return;
        }
        StringBuffer bufferTmp = PatternGenerator.extractIndentation(useStmt);
        AresUseStmt newUse =
            CreateJavaNodeHelper.createAresUse(bufferTmp.toString(), null, null, null, null);
        AddUseVisitor auv = new AddUseVisitor(useStmt, newUse, true);
        example2Modified.accept(auv);
        ArrayList<AresUseStmt> list = new ArrayList<AresUseStmt>();
        list.add(newUse);
        wildcardUseMap.put(wildcard1, list);
      }
    }
  }

  private static int getOtherIndex(LinkedList<AbstractBastNode> fieldStmts, AbstractBastNode parent,
      AbstractBastNode otherParent, BastFieldConstants otherField, NodeIndex useIndex,
      ExtendedDiffResult extDiffAa) {
    if (useIndex.childrenListIndex > 0) {
      AbstractBastNode tmp = (AbstractBastNode) parent.fieldMap.get(useIndex.childrenListNumber)
          .getListField().get(useIndex.childrenListIndex - 1);
      AbstractBastNode tmpPartner = extDiffAa.secondToFirstMap.get(tmp);
      if (tmpPartner != null) {
        return fieldStmts.indexOf(tmpPartner) + 1;
      }

    }
    int count = 0;

    for (int i = 0; i < fieldStmts.size(); i++) {

      boolean deleted = false;
      for (BastEditOperation ep : extDiffAa.editScript) {
        if (ep.getType() == EditOperationType.STATEMENT_DELETE) {
          if (ep.getUnchangedOrOldParentNode() == otherParent) {
            if (ep.getOldOrChangedIndex().childrenListNumber == otherField) {
              if (ep.getOldOrChangedIndex().childrenListIndex == i) {
                deleted = true;
                break;
              }
            }
          }
        }
      }
      if (!deleted) {
        count++;
      }
      if (count == useIndex.childrenListIndex) {
        return i - 1;
      }
    }

    return 0;
  }
}
