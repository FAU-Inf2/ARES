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

import de.fau.cs.inf2.cas.ares.bast.general.ParserFactory;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresCaseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPluginClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.FindOccurenceVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.CreateJavaNodeHelper;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformation;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastArrayRef;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCmp;
import de.fau.cs.inf2.cas.common.bast.nodes.BastForStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastListInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameter;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitch;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitchCaseGroup;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTryStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeArgument;
import de.fau.cs.inf2.cas.common.bast.visitors.FindNodesFromTagVisitor;
import de.fau.cs.inf2.cas.common.bast.visitors.IPrettyPrinter;

import de.fau.cs.inf2.cas.common.parser.IGeneralToken;
import de.fau.cs.inf2.cas.common.parser.odin.BasicJavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.JavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.ListToken;
import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import de.fau.cs.inf2.cas.common.util.string.IStringSimilarityMeasure;
import de.fau.cs.inf2.cas.common.util.string.NGramCalculator;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.AlignOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.DeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.MoveOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.FinalModifierInsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.StatementDeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.StatementInsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.StatementReorderingOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.VariableDeclarationInsertOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class CombinationHelper {

  /**
   * Creates the wildcard1.
   *
   * @param bufferTmp the buffer tmp
   * @return the abstract bast statement
   */
  public static AbstractBastStatement createWildcard1(StringBuffer bufferTmp) {
    BastNameIdent ident = CreateJavaNodeHelper.createBastNameIdent("stmt");
    CreateJavaNodeHelper.addWhiteSpace(ident, " ");
    AresPluginClause plugin = CreateJavaNodeHelper.createAresPluginClause(null, ident, null);
    AbstractBastStatement annotation =
        CreateJavaNodeHelper.createAresWildcard(bufferTmp.toString(), null, plugin, null);
    return annotation;
  }

  /**
   * Creates the stmt use.
   *
   * @param bufferTmp the buffer tmp
   * @return the abstract bast statement
   */
  public static AbstractBastStatement createStmtUse(StringBuffer bufferTmp) {
    AbstractBastStatement annotation =
        CreateJavaNodeHelper.createAresUse(bufferTmp.toString(), null, null, null, null);
    return annotation;
  }

  static void handleReorderingStmt(boolean createUse, BastEditOperation editOp,
      ReplaceMap replaceMap, ExtendedDiffResult exDiff,
      HashMap<AbstractBastNode, BlockChanges> changeMap,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy2, MatchBoundary boundary,
      HashMap<AbstractBastNode, AbstractBastNode> delInsNodeMap,
      ArrayList<BastEditOperation> workList) {

    AlignOperation reorder = null;
    if (editOp.getType() == EditOperationType.STATEMENT_REORDERING) {
      reorder = (AlignOperation) ((StatementReorderingOperation) editOp).getBasicOperation();
    } else {
      reorder = (AlignOperation) editOp;
    }
    AbstractBastNode block = null;
    BastField field = null;
    block =
        exDiff.firstToSecondMap.get(((AbstractBastNode) (reorder).getUnchangedOrOldParentNode()));
    assert (block != null);
    field = block.getField(reorder.getNewOrChangedIndex().childrenListNumber);
    if (reorder
        .getNewOrChangedIndex().childrenListNumber == BastFieldConstants.DIRECT_CALL_ARGUMENTS) {
      return;
    }
    if (reorder.getOldOrInsertedNode().getTag() == BastTypeArgument.TAG) {
      return;
    }
    if (reorder.getUnchangedOrOldParentNode().getTag() == BastParameter.TAG) {
      return;
    }
    if (field.isList()) {
      insertWildcard1(createUse, replaceMap, changeMap, reorder, block, field, exDiff, hierarchy1,
          hierarchy2, boundary, false, null, delInsNodeMap, workList);
    }
  }

  @SuppressWarnings("unchecked")
  private static void insertWildcard1(boolean createUse, ReplaceMap replaceMap,
      HashMap<AbstractBastNode, BlockChanges> changeMap, BastEditOperation editOp,
      AbstractBastNode block, BastField field, ExtendedDiffResult exDiff,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy2, MatchBoundary boundary,
      boolean moveOldPart, BastFieldConstants fieldConst,
      HashMap<AbstractBastNode, AbstractBastNode> delInsNodeMap,
      ArrayList<BastEditOperation> workList) {
    if (!createUse && hierarchy1.get(block) != null && hierarchy1.get(block).list != null
        && hierarchy1.get(block).list.size() > 0
        && hierarchy1.get(block).list.get(0).parent.getTag() == BastListInitializer.TAG) {
      return;
    }
    BastFieldConstants wildcardIndex = null;
    boolean newIndex = false;
    switch (editOp.getType()) {
      case ALIGN:
        if (moveOldPart) {
          wildcardIndex = editOp.getOldOrChangedIndex().childrenListNumber;
          newIndex = false;
        } else {
          wildcardIndex = editOp.getNewOrChangedIndex().childrenListNumber;
          newIndex = true;
        }
        break;
      case INSERT:
        wildcardIndex = editOp.getOldOrChangedIndex().childrenListNumber;
        newIndex = false;
        break;
      case DELETE:
        if (fieldConst != null) {
          wildcardIndex = fieldConst;
        } else {
          wildcardIndex = editOp.getOldOrChangedIndex().childrenListNumber;
        }
        newIndex = false;
        break;
      case MOVE:
        if (moveOldPart) {
          wildcardIndex = editOp.getOldOrChangedIndex().childrenListNumber;
          newIndex = false;
        } else {
          wildcardIndex = editOp.getNewOrChangedIndex().childrenListNumber;
          newIndex = true;
        }
        break;
      default:
        assert (false);
    }
    LinkedList<AbstractBastStatement> stmt;
    LinkedList<AbstractBastStatement> newStmts;
    final int maxVal;
    if (field.isList()) {
      stmt = (LinkedList<AbstractBastStatement>) field.getListField();
      if (stmt == null) {
        stmt = new LinkedList<AbstractBastStatement>();
      }
    } else {
      stmt = new LinkedList<AbstractBastStatement>();
      stmt.add((AbstractBastStatement) field.getField());
    }
    newStmts = new LinkedList<>();

    if (stmt.size() > 0 && stmt.get(0).getTag() == AresBlock.TAG) {
      return;
    }
    
    int newPosition = 0;
    if (field.isList()) {
      newPosition = WildcardPlacementComputation.getPosition(editOp, block, wildcardIndex,
        changeMap, newIndex, exDiff, workList, boundary);
    }

    maxVal = Math.max(stmt.size(), newPosition + 1);

    for (int j = 0; j < maxVal; j++) {
      if (j == newPosition) {

        StringBuffer bufferTmp = new StringBuffer();
        boolean exprWildcard = false;
        if (moveOldPart) {
          bufferTmp = PatternGenerator.extractIndentation(stmt, j);
          if (bufferTmp.length() == 0) {
            bufferTmp = PatternGenerator.extractIndentation(editOp.getOldOrInsertedNode());
          }
        } else if (fieldConst != null) {
          if (j < stmt.size()) {
            if (stmt.get(j).getTag() == AresWildcard.TAG
                || stmt.get(j).getTag() == AresUseStmt.TAG) {

              if (stmt.get(j).getTag() == AresWildcard.TAG) {
                if (((AresWildcard) stmt.get(j)).plugin != null
                    && ((AresWildcard) stmt.get(j)).plugin.exprList != null
                    && ((AresPatternClause) ((AresWildcard) stmt.get(j)).plugin.exprList
                        .get(0)).expr != null) {
                  exprWildcard = true;
                }
              } else if (stmt.get(j).getTag() == AresUseStmt.TAG) {
                if (WildcardAccessHelper.isExprWildcard(((AresUseStmt) stmt.get(j)))) {
                  exprWildcard = true;
                }
              }
            }
            bufferTmp = PatternGenerator.extractIndentation(stmt, j);
          } else {
            bufferTmp = PatternGenerator.extractIndentation(editOp.getOldOrInsertedNode());
          }
        } else {
          bufferTmp = PatternGenerator.extractIndentation(stmt, j);
          if (bufferTmp.length() == 0 
              || editOp.getNewOrChangedNode().getTag() == BastSwitchCaseGroup.TAG) {
            bufferTmp = PatternGenerator.extractIndentation(editOp.getOldOrInsertedNode());
          }
        }

        AbstractBastStatement annotation = null;
        if (createUse) {
          annotation = createStmtUse(bufferTmp);
        } else {
          annotation = createWildcard1(bufferTmp);
        }
        if (editOp.getUnchangedOrOldParentNode().getTag() != BastBlock.TAG) {
          replaceMap.put((AbstractBastNode) editOp.getOldOrInsertedNode(), annotation, true);
        } else {
          if (newIndex) {
            replaceMap.put((AbstractBastNode) editOp.getNewOrChangedNode(), annotation, false);
          } else {
            replaceMap.put((AbstractBastNode) editOp.getOldOrInsertedNode(), annotation, false);
          }
        }

        if (editOp.getType() == EditOperationType.MOVE
            || editOp.getType() == EditOperationType.ALIGN) {
          if (moveOldPart) {
            if (editOp.getOldOrChangedIndex().childrenListNumber.isList) {
              LinkedList<AbstractBastNode> tmpList =
                  (LinkedList<AbstractBastNode>) editOp.getUnchangedOrOldParentNode()
                      .getField(editOp.getOldOrChangedIndex().childrenListNumber).getListField();
              if (tmpList != null) {
                for (int i = 0; i < tmpList.size(); i++) {
                  if (stmt.size() < j
                      && WildcardAccessHelper.isEqual(tmpList.get(i), stmt.get(j))) {
                    if (i < editOp.getOldOrChangedIndex().childrenListIndex) {
                      newPosition++;
                      continue;
                    }
                  }
                }
              }
            }
            if (j < stmt.size()
                && stmt.get(j).getTag() == annotation.getTag() 
                && WildcardAccessHelper.getName(stmt.get(j)) != null) {
              if (editOp.getUnchangedOrOldParentNode().getTag() != BastBlock.TAG) {
                replaceMap.put((AbstractBastNode) stmt.get(j), annotation, true);
              } else {
                if (newIndex) {
                  replaceMap.put((AbstractBastNode) stmt.get(j), annotation, false);
                } else {
                  replaceMap.put((AbstractBastNode) stmt.get(j), annotation, false);
                }
              }
              newStmts.add(stmt.get(j));
            } else {
              newStmts.add(annotation);
              if (j >= stmt.size()) {
                WildcardPlacementComputation.additionalWildcards(block, wildcardIndex, annotation,
                    editOp, changeMap, false, boundary, j);
              } else if (j < stmt.size() && exDiff.secondToFirstMap.get(stmt.get(j)) != null
              ) {
                if (exDiff.editMapNew.get(stmt.get(j)) == null
                    || WildcardAccessHelper.isEqual(exDiff.editMapNew.get(stmt.get(j))
                        .getOldOrInsertedNode(),
                        exDiff.editMapNew.get(stmt.get(j)).getNewOrChangedNode())) {
                  newStmts.add(stmt.get(j));
                  WildcardPlacementComputation.additionalWildcards(block, wildcardIndex, annotation,
                      editOp, changeMap, false, boundary, j);
                } else {
                  WildcardPlacementComputation.updateReplacedStatement(block, wildcardIndex,
                      stmt.get(j), annotation, editOp, changeMap, replaceMap);
                }
              } else {
                WildcardPlacementComputation.updateReplacedStatement(block, wildcardIndex,
                    stmt.get(j), annotation, editOp, changeMap, replaceMap);
              }
            }
          } else {
            insertMoveWildcard(replaceMap, changeMap, editOp, block, exDiff, hierarchy1, hierarchy2,
                boundary, wildcardIndex, stmt, newStmts, j, annotation);
          }
        } else if (fieldConst != null) {
          if (j == 0 && stmt.size() > j && stmt.get(j).getTag() == AresChoiceStmt.TAG) {
            insertDeleteWildcard(exDiff, hierarchy1, boundary, changeMap, editOp, block, fieldConst,
                stmt, newStmts, j, exprWildcard, annotation, delInsNodeMap, replaceMap, workList);
            newStmts.add(annotation);
          } else {
            newStmts.add(annotation);
            insertDeleteWildcard(exDiff, hierarchy1, boundary, changeMap, editOp, block, fieldConst,
                stmt, newStmts, j, exprWildcard, annotation, delInsNodeMap, replaceMap, workList);
          }
        } else {
          newStmts.add(annotation);
          if (j >= stmt.size()) {
            WildcardPlacementComputation.additionalWildcards(block, wildcardIndex, annotation,
                editOp, changeMap, false, boundary, j);
          } else if (editOp.getType() == EditOperationType.DELETE && j < stmt.size()
              && exDiff.secondToFirstMap.get(stmt.get(j)) != null && field.isList()) {
            newStmts.add(stmt.get(j));
            WildcardPlacementComputation.additionalWildcards(block, wildcardIndex, annotation,
                editOp, changeMap, false, boundary, j);
          } else {
            WildcardPlacementComputation.updateReplacedStatement(block, wildcardIndex, stmt.get(j),
                annotation, editOp, changeMap, replaceMap);
          }
        }
      } else if (j < stmt.size()) {
        newStmts.add(stmt.get(j));
      }
    }
    if (field.isList()) {
      field = new BastField(newStmts);
    } else {
      JavaToken token = CreateJavaNodeHelper.findLeftJavaToken(newStmts.get(0));
      JavaToken oldToken = CreateJavaNodeHelper.findLeftJavaToken(stmt.get(0));
      if (token != null && oldToken != null) {
        if (!oldToken.whiteSpace.toString().contains("\n")) {
          String newWhitespace = token.whiteSpace.toString().replace("\n", "");
          token.whiteSpace.setLength(0);
          token.whiteSpace.append(newWhitespace);
        }
      }
      field = new BastField(newStmts.get(0));
    }
    block.replaceField(wildcardIndex, field);
  }

  private static void insertMoveWildcard(ReplaceMap replaceMap,
      HashMap<AbstractBastNode, BlockChanges> changeMap, BastEditOperation editOp,
      AbstractBastNode block, ExtendedDiffResult exDiff,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy2, MatchBoundary boundary,
      BastFieldConstants wildcardIndex, LinkedList<AbstractBastStatement> stmt,
      LinkedList<AbstractBastStatement> newStmts, int jpar, AbstractBastStatement annotation) {
    AbstractBastNode oldblock = exDiff.firstToSecondMap.get(editOp.getUnchangedOrOldParentNode());
    oldblock = optimizeBlockMapping(editOp.getUnchangedOrOldParentNode(), oldblock,
        exDiff.firstToSecondMap, exDiff, hierarchy1, hierarchy2);
    if (boundary.getNode2().getTag() == BastBlock.TAG
        && boundary.getNode2().getField(BastFieldConstants
            .BLOCK_STATEMENT).getListField().size() > 0
        && hierarchy2.get(boundary.getNode2()).list.get(0).parent.getTag() == AresBlock.TAG) {
      if (block == boundary.getNode2() && editOp
          .getUnchangedOrOldParentNode() == boundary.getNode1()
          && exDiff.secondToFirstMap.get(boundary.getNode2()) 
          == boundary.getNode1() && jpar < stmt.size()
          && editOp.getNewOrChangedIndex().childrenListIndex == jpar) {
        WildcardPlacementComputation.updateReplacedStatement(block, wildcardIndex, stmt.get(jpar),
            annotation, editOp, changeMap, replaceMap);
        newStmts.add(annotation);
      } else {
        if (exDiff.firstToSecondMap.get(editOp.getUnchangedOrOldParentNode()) == editOp
            .getUnchangedOrNewParentNode()
            || editOp.getUnchangedOrOldParentNode() == boundary.getNode1()
                && editOp.getUnchangedOrNewParentNode() == boundary.getNode2()) {
          if (editOp.getOldOrInsertedNode().getTag() == BastIf.TAG || WildcardAccessHelper
              .isEqual(editOp.getOldOrInsertedNode(), editOp.getNewOrChangedNode())) {
            newStmts.add(annotation);
            WildcardPlacementComputation.updateReplacedStatement(block, wildcardIndex,
                stmt.get(jpar), annotation, editOp, changeMap, replaceMap);
          } else {
            replaceMap.put(editOp.getOldOrInsertedNode(), annotation, false);
            newStmts.add(annotation);
            AbstractBastNode replaceNode = null;
            if (jpar < stmt.size()) {
              replaceNode = stmt.get(jpar);
            }

            WildcardPlacementComputation.updateReplacedStatement(block, wildcardIndex, replaceNode,
                annotation, editOp, changeMap, replaceMap);
          }
        } else {
          replaceMap.put(editOp.getOldOrInsertedNode(), annotation, false);
          newStmts.add(annotation);
          AbstractBastNode replaceNode = null;
          if (jpar < stmt.size()) {
            replaceNode = stmt.get(jpar);
          }

          WildcardPlacementComputation.updateReplacedStatement(block, wildcardIndex, replaceNode,
              annotation, editOp, changeMap, replaceMap);
        }

      }
    } else if (exDiff.firstToSecondMap.get(block) != null) {
      newStmts.add(stmt.get(jpar));
    } else {
      if (editOp.getUnchangedOrOldParentNode().getTag() != BastBlock.TAG) {
        replaceMap.put(editOp.getOldOrInsertedNode(), annotation, true);

      } else {
        replaceMap.put(editOp.getOldOrInsertedNode(), annotation, false);
      }
      newStmts.add(annotation);
      if (jpar < stmt.size()) {
        WildcardPlacementComputation.updateReplacedStatement(block, wildcardIndex, stmt.get(jpar),
            annotation, editOp, changeMap, replaceMap);
      } else {
        WildcardPlacementComputation.updateReplacedStatement(block, wildcardIndex, null, annotation,
            editOp, changeMap, replaceMap);
      }
    }
  }

  static void handleInsert(boolean createUse, BastEditOperation editOp, ReplaceMap replaceMap,
      ExtendedDiffResult exDiffAa, HashMap<DeleteOperation, InsertOperation> delInsMap,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy2, MatchBoundary boundary,
      HashMap<AbstractBastNode, BlockChanges> changeMap,
      HashMap<AbstractBastNode, List<RevertModificationInfo>> revertMap,
      HashMap<AbstractBastNode, AbstractBastNode> delInsNodeMap,
      ArrayList<BastEditOperation> workList) {

    InsertOperation insertOp = null;
    if (editOp.getType() == EditOperationType.STATEMENT_INSERT) {
      insertOp = (InsertOperation) ((StatementInsertOperation) editOp).getBasicOperation();
    } else if (editOp.getType() == EditOperationType.FINAL_INSERT) {
      insertOp = (InsertOperation) ((FinalModifierInsertOperation) editOp).getBasicOperation();
    } else if (editOp.getType() == EditOperationType.VARIABLE_INSERT) {
      insertOp =
          (InsertOperation) ((VariableDeclarationInsertOperation) editOp).getBasicOperation();

      if (hierarchy2.get(insertOp.getUnchangedOrOldParentNode()).list.get(0).parent
          .getTag() != BastForStmt.TAG) {
        return;
      }
    } else {
      insertOp = (InsertOperation) editOp;
    }
    AbstractBastNode block = null;
    BastField field = null;
    if (insertOp.getUnchangedOrNewParentNode().getTag() == BastBlock.TAG
        || (insertOp.getUnchangedOrNewParentNode().getTag() == BastSwitchCaseGroup.TAG && insertOp
            .getNewOrChangedIndex().childrenListNumber 
            == BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS)
        || (insertOp.getUnchangedOrNewParentNode().getTag() == BastTryStmt.TAG
            && insertOp.getNewOrChangedIndex().childrenListNumber == BastFieldConstants.TRY_CATCHES)
        || (insertOp.getUnchangedOrNewParentNode().getTag() == BastSwitch.TAG && insertOp
            .getNewOrChangedIndex().childrenListNumber == BastFieldConstants.SWITCH_CASE_GROUPS)
        || (insertOp.getUnchangedOrNewParentNode().getTag() == BastIf.TAG && insertOp
            .getNewOrChangedIndex().childrenListNumber != BastFieldConstants.IF_CONDITION)) {
      block = (AbstractBastNode) (insertOp).getUnchangedOrOldParentNode();
      assert (block != null);
      field = block.getField(insertOp.getNewOrChangedIndex().childrenListNumber);
      insertWildcard1(createUse, replaceMap, changeMap, insertOp, block, field, exDiffAa,
          hierarchy1, hierarchy2, boundary, false, null, delInsNodeMap, workList);
    } else if (insertOp.getUnchangedOrOldParentNode().getTag() == BastSwitchCaseGroup.TAG) {
      block = (AbstractBastNode) (insertOp).getUnchangedOrOldParentNode();
      assert (block != null);
      field = block.getField(insertOp.getNewOrChangedIndex().childrenListNumber);
      insertWildcard1(createUse, replaceMap, changeMap, insertOp, block, field, exDiffAa,
          hierarchy1, hierarchy2, boundary, false, null, delInsNodeMap, workList);
    } else if (insertOp.getNewOrChangedIndex().childrenListNumber 
        == BastFieldConstants.IF_IF_PART) {
      block = insertOp.getUnchangedOrNewParentNode();
      field = block.getField(insertOp.getNewOrChangedIndex().childrenListNumber);
      insertWildcard1(createUse, replaceMap, changeMap, insertOp, block, field, exDiffAa,
          hierarchy1, hierarchy2, boundary, false, null, delInsNodeMap, workList);
    } else {
      handleExprInsert(createUse, replaceMap, exDiffAa, delInsMap, hierarchy2, boundary, changeMap,
          revertMap, workList, insertOp);
    }
  }

  private static void handleExprInsert(boolean createUse, ReplaceMap replaceMap,
      ExtendedDiffResult exDiff, HashMap<DeleteOperation, InsertOperation> delInsMap,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy2, MatchBoundary boundary,
      HashMap<AbstractBastNode, BlockChanges> changeMap,
      HashMap<AbstractBastNode, List<RevertModificationInfo>> revertMap,
      ArrayList<BastEditOperation> workList, InsertOperation editOp) {
    AbstractBastNode node = editOp.getOldOrInsertedNode();
    NodeParentInformationHierarchy npi = hierarchy2.get(node);
    NodeParentInformation parentInfo = null;
    NodeParentInformation parentBlockInfo = null;
    AbstractBastNode parentNode = null;
    AbstractBastNode insertB2Parent = null;
    int depth = -1;
    for (int j = 0; j < npi.list.size(); j++) {
      insertB2Parent = npi.list.get(j).parent;
      if (insertB2Parent != null) {
        depth = j;
        break;
      }
    }
    if (insertB2Parent == null || insertB2Parent.getTag() == BastProgram.TAG) {
      return;
    }
    for (int j = 0; j < npi.list.size(); j++) {
      if (npi.list.get(j).parent.getTag() == BastBlock.TAG
          || npi.list.get(j).parent.getTag() == BastSwitchCaseGroup.TAG) {
        parentBlockInfo = npi.list.get(j);
        parentNode = npi.list.get(j - 1).parent;
        parentInfo = npi.list.get(0);
        break;
      }
    }
    loop2: for (int j = 0; j < npi.list.size(); j++) {

      if (npi.list.get(j).parent.getField(npi.list.get(j).fieldConstant).isList()) {
        switch (npi.list.get(j).parent.getTag()) {
          case AresBlock.TAG:
          case BastBlock.TAG:
            parentBlockInfo = npi.list.get(j);
            parentInfo = npi.list.get(0);
            break loop2;
          case BastSwitchCaseGroup.TAG:
            parentBlockInfo = npi.list.get(j);
            parentInfo = npi.list.get(0);
            break loop2;
          default:
            continue;
        }
      }
    }
    if (parentInfo == null) {
      return;
    }
    AbstractBastNode parent = null;
    BastFieldConstants fieldConstant = null;
    parent = insertB2Parent;
    fieldConstant = parentInfo.fieldConstant;
    exprWildcardInit(createUse, replaceMap, exDiff, delInsMap, boundary, revertMap, changeMap,
        workList, editOp, node, npi, parentBlockInfo, parentNode, depth, parent, fieldConstant);
  }

  private static AbstractBastStatement createExprUse(StringBuffer bufferTmp,
      BastIntConst occurrence, AbstractBastNode clone) {
    AresPatternClause pattern =
        CreateJavaNodeHelper.createAresPatternClause(null, occurrence, clone, null, true);
    LinkedList<AbstractBastExpr> exprList = new LinkedList<>();
    exprList.add(pattern);
    AbstractBastStatement annotation =
        CreateJavaNodeHelper.createAresUse(bufferTmp.toString(), null, null, pattern, null);
    return annotation;
  }

  /**
   * Optimize block mapping.
   *
   * @param editOpBlock the edit op block
   * @param probableblock the probableblock
   * @param nodeMapping the node mapping
   * @param exDiff the ex diff
   * @param hierarchy1 the hierarchy 1
   * @param hierarchy2 the hierarchy 2
   * @return the abstract bast node
   */
  public static AbstractBastNode optimizeBlockMapping(AbstractBastNode editOpBlock,
      AbstractBastNode probableblock, Map<AbstractBastNode, AbstractBastNode> nodeMapping,
      ExtendedDiffResult exDiff, Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy2) {
    NodeParentInformationHierarchy npi = hierarchy1.get(editOpBlock);
    if (npi == null || npi.list == null || npi.list.isEmpty()) {
      return probableblock;
    }
    if (editOpBlock == null) {
      return probableblock;
    }
    if (probableblock == null) {
      AbstractBastNode parent = npi.list.get(0).parent;
      AbstractBastNode parentPartner = nodeMapping.get(parent);
      if (parentPartner == null) {
        return probableblock;
      }
      BastField parentPartnerField = parentPartner.getField(npi.list.get(0).fieldConstant);
      if (parentPartnerField != null) {
        if (!parentPartnerField.isList()) {
          if (parentPartnerField.getField() == null) {
            return probableblock;
          }
          switch (parentPartnerField.getField().getTag()) {
            case BastBlock.TAG:
              if (editOpBlock.getTag() != BastBlock.TAG) {
                return probableblock;
              }
              int countAlternative = 0;
              for (AbstractBastStatement stmt : ((BastBlock) editOpBlock).statements) {
                AbstractBastNode stmtPartner = nodeMapping.get(stmt);
                if (stmtPartner != null) {
                  if (((BastBlock) parentPartnerField.getField()).statements
                      .contains(stmtPartner)) {
                    countAlternative++;
                  }
                }
              }
              if (countAlternative > ((BastBlock) editOpBlock).statements.size() / 2) {
                return parentPartnerField.getField();
              } else {
                return probableblock;
              }
            default:
              return probableblock;
          }
        } else if (parentPartner.getTag() == BastFunction.TAG) {
          switch (parentPartnerField.getListField().getFirst().getTag()) {
            case BastBlock.TAG:
              if (editOpBlock.getTag() != BastBlock.TAG) {
                return probableblock;
              }
              int countAlternative = 0;
              if (((BastBlock) parentPartnerField.getListField().get(0)).statements.size() == 1
                  && ((BastBlock) parentPartnerField.getListField().get(0)).statements.get(0)
                      .getTag() == AresBlock.TAG) {

                for (AbstractBastStatement stmt : ((BastBlock) editOpBlock).statements) {
                  AbstractBastNode stmtPartner = nodeMapping.get(stmt);
                  if (stmtPartner != null) {
                    if (((AresBlock) ((BastBlock) parentPartnerField.getListField()
                        .get(0)).statements.get(0)).block.statements.contains(stmtPartner)) {
                      countAlternative++;
                    }
                  }
                }
                if (countAlternative > ((BastBlock) editOpBlock).statements.size() / 2) {
                  return ((AresBlock) ((BastBlock) parentPartnerField.getListField()
                      .get(0)).statements.get(0));
                } else {
                  return probableblock;
                }
              }
              if (countAlternative > ((BastBlock) editOpBlock).statements.size() / 2) {
                return parentPartnerField.getField();
              } else {
                return probableblock;
              }
            default:
              assert (false);
          }

        }
      }
    } else {
      AbstractBastNode parent = npi.list.get(0).parent;
      AbstractBastNode parentPartner = nodeMapping.get(parent);
      NodeParentInformationHierarchy npiNew = hierarchy2.get(probableblock);

      AbstractBastNode probableblockParent = npiNew.list.get(0).parent;
      if (parentPartner == null) {
        return probableblock;
      }
      BastField parentPartnerField = parentPartner.getField(npi.list.get(0).fieldConstant);
      if (parentPartnerField != null) {
        if (!parentPartnerField.isList() && parentPartnerField.getField() != null) {
          switch (parentPartnerField.getField().getTag()) {
            case BastBlock.TAG:
              if (editOpBlock.getTag() != BastBlock.TAG
                  || probableblock.getTag() != BastBlock.TAG) {
                return probableblock;
              }
              int countProbableBlock = 0;
              int countAlternative = 0;
              for (AbstractBastStatement stmt : ((BastBlock) editOpBlock).statements) {
                AbstractBastNode stmtPartner = nodeMapping.get(stmt);
                if (stmtPartner != null) {
                  assert (probableblock != null);
                  if (((BastBlock) probableblock).statements != null
                      && ((BastBlock) probableblock).statements.contains(stmtPartner)) {
                    countProbableBlock++;
                  }
                  if (((BastBlock) parentPartnerField.getField()).statements != null
                      && ((BastBlock) parentPartnerField.getField()).statements
                          .contains(stmtPartner)) {
                    countAlternative++;
                  }
                }
              }
              if (countAlternative > countProbableBlock) {
                return parentPartnerField.getField();
              } else {
                if (countAlternative == 0 && countProbableBlock == 0) {
                  if (((BastBlock) probableblock).statements.size() > 0) {
                    if (((BastBlock) probableblock).statements.get(0).getTag() == AresBlock.TAG) {
                      return parentPartnerField.getField();
                    }
                  }
                  
                }
                if (parent.getTag() == parentPartner.getTag()
                    && probableblockParent.getTag() != parent.getTag()) {
                  return parentPartnerField.getField();
                } else {
                  return probableblock;
                }
              }
            case BastIf.TAG:
              return probableblock;
            default:
              return probableblock;
          }
        } else if (parentPartner.getTag() == BastFunction.TAG) {
          switch (parentPartnerField.getListField().getFirst().getTag()) {
            case BastBlock.TAG:
              if (editOpBlock.getTag() != BastBlock.TAG
                  || probableblock.getTag() != BastBlock.TAG) {
                return probableblock;
              }
              int countProbableBlock = 0;
              int countAlternative = 0;
              if (((BastBlock) parentPartnerField.getListField().get(0)).statements.size() == 1
                  && ((BastBlock) parentPartnerField.getListField().get(0)).statements.get(0)
                      .getTag() == AresBlock.TAG) {

                for (AbstractBastStatement stmt : ((BastBlock) editOpBlock).statements) {
                  AbstractBastNode stmtPartner = nodeMapping.get(stmt);
                  if (stmtPartner != null) {
                    if (((BastBlock) probableblock).statements.contains(stmtPartner)) {
                      countProbableBlock++;
                    }
                    if (((AresBlock) ((BastBlock) parentPartnerField.getListField()
                        .get(0)).statements.get(0)).block.statements.contains(stmtPartner)) {
                      countAlternative++;
                    }
                  }
                }
                if (countAlternative > countProbableBlock) {
                  return ((AresBlock) ((BastBlock) parentPartnerField.getListField()
                      .get(0)).statements.get(0));
                } else {
                  return probableblock;
                }
              }
              if (countAlternative > countProbableBlock) {
                return parentPartnerField.getField();
              } else {
                return probableblock;
              }
            default:
              assert (false);
          }

        }
      }

    }
    return probableblock;
  }

  static void handleDelete(boolean createUse, BastEditOperation editOp, ReplaceMap replaceMap,
      ExtendedDiffResult exDiff, Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy2,
      HashMap<DeleteOperation, InsertOperation> delInsMap, MatchBoundary boundary,
      HashMap<AbstractBastNode, List<RevertModificationInfo>> revertMap,
      HashMap<AbstractBastNode, BlockChanges> changeMap,
      HashMap<AbstractBastNode, AbstractBastNode> delInsNodeMap,
      ArrayList<BastEditOperation> workList) {

    DeleteOperation deleteOp = null;
    if (editOp.getType() == EditOperationType.STATEMENT_DELETE) {
      deleteOp = (DeleteOperation) ((StatementDeleteOperation) editOp).getBasicOperation();
    } else {
      deleteOp = (DeleteOperation) editOp;
    }
    if (deleteOp.getUnchangedOrOldParentNode().getTag() == AresBlock.TAG) {
      return;
    }
    AbstractBastNode block = null;
    BastField field = null;
    BastBlock functionBlock = null;
    if (deleteOp.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG) {
      if ((deleteOp).getUnchangedOrOldParentNode().getTag() == BastProgram.TAG) {
        return;
      }
      if (exDiff.firstToSecondMap
          .get(((BastBlock) (deleteOp).getUnchangedOrOldParentNode())) == null
          || exDiff.firstToSecondMap.get(((BastBlock) (deleteOp).getUnchangedOrOldParentNode()))
              .getTag() == BastProgram.TAG) {
        if (hierarchy1.get((deleteOp).getUnchangedOrOldParentNode()).list.get(0).parent
            .getTag() == BastFunction.TAG) {
          if (exDiff.firstToSecondMap
              .get(hierarchy1.get((deleteOp).getUnchangedOrOldParentNode()).list
                  .get(0).parent) != null) {
            functionBlock =
                (BastBlock) exDiff.firstToSecondMap
                    .get(
                        hierarchy1.get((deleteOp).getUnchangedOrOldParentNode()).list.get(0).parent)
                    .getField(BastFieldConstants.FUNCTION_BLOCK_STATEMENTS).getListField().get(0);
            if (functionBlock.statements.get(0).getTag() == AresBlock.TAG) {
              functionBlock = null;
            }
          } else {
            if (boundary.getNode2().getTag() == BastBlock.TAG
                && (boundary.getNode2().getField(BastFieldConstants.BLOCK_STATEMENT))
                    .getListField() != null
                && (boundary.getNode2().getField(BastFieldConstants.BLOCK_STATEMENT)).getListField()
                    .size() > 0) {
              if (boundary.getNode1() == (deleteOp).getUnchangedOrOldParentNode()
                  && boundary.getNode1().getTag() == BastBlock.TAG) {
                functionBlock = (BastBlock) boundary.getNode2();
              } else if (boundary.getNode1().getTag() == BastFunction.TAG
                  && ((BastFunction) boundary.getNode1()).statements.get(0) == (deleteOp)
                      .getUnchangedOrOldParentNode()) {
                return;

              } else {
                return;
              }
            } else {
              return;
            }
          }
        } else if (deleteOp.getUnchangedOrOldParentNode() == boundary.getNode1()) {
          // do nothing
        } else {
          AbstractBastNode partner = null;
          NodeParentInformationHierarchy npi = hierarchy1.get(deleteOp.getOldOrInsertedNode());
          int foundId = -1;
          for (int i = 0; i < npi.list.size(); i++) {
            if (exDiff.firstToSecondMap.get(npi.list.get(i).parent) != null) {
              foundId = i;
              break;
            }
          }

          if (foundId != -1) {
            partner = exDiff.firstToSecondMap.get(npi.list.get(foundId).parent);
            for (int j = foundId; j >= 1; j--) {
              if (partner != null && partner.getField(npi.list.get(j).fieldConstant) != null) {
                if (partner.getField(npi.list.get(j).fieldConstant).isList() 
                    && partner.getField(npi.list.get(j).fieldConstant).getListField() != null) {
                  if (partner.getField(npi.list.get(j).fieldConstant).getListField()
                      .size() > npi.list.get(j).listId) {
                    partner = partner.getField(npi.list.get(j).fieldConstant).getListField()
                        .get(npi.list.get(j).listId);
                  } else {
                    partner = null;
                    break;
                  }
                } else {
                  partner = null;
                }
              } else {
                partner = null;
                break;
              }

            }
          }
          if (partner == null || partner.getTag() != BastBlock.TAG) {
            return;
          } else {
            functionBlock = (BastBlock) partner;
          }
        }
      }
      if (functionBlock != null) {
        block = functionBlock;
      } else {
        block = (BastBlock) (deleteOp).getUnchangedOrOldParentNode();
        block = (BastBlock) exDiff.firstToSecondMap
            .get(((BastBlock) (deleteOp).getUnchangedOrOldParentNode()));
      }
      block = optimizeBlockMapping((deleteOp).getUnchangedOrOldParentNode(), block,
          exDiff.firstToSecondMap, exDiff, hierarchy1, hierarchy2);
      if (block == null) {
        if ((deleteOp).getUnchangedOrOldParentNode() == boundary.getNode1()) {
          block = boundary.getNode2();
        } else {
          return;
        }
      }
      if (block.getTag() == BastBlock.TAG) {
        if (((BastBlock) block).statements != null && ((BastBlock) block).statements.size() == 1
            && ((BastBlock) block).statements.get(0).getTag() == AresBlock.TAG) {
          block = ((AresBlock) ((BastBlock) block).statements.get(0)).block;
        }
      }
      assert (block != null);
      BastFieldConstants fieldConst = null;
      if (block.getTag() == AresBlock.TAG) {
        fieldConst = BastFieldConstants.BLOCK_STATEMENT;
        field = ((AresBlock) block).block.getField(fieldConst);
        block = ((AresBlock) block).block;

      } else if (block.getTag() == BastBlock.TAG) {
        fieldConst = BastFieldConstants.BLOCK_STATEMENT;
        field = block.getField(BastFieldConstants.BLOCK_STATEMENT);
      } else {
        fieldConst = deleteOp.getOldOrChangedIndex().childrenListNumber;
        field = block.getField(deleteOp.getOldOrChangedIndex().childrenListNumber);
      }
      insertWildcard1(createUse, replaceMap, changeMap, deleteOp, block, field, exDiff, hierarchy1,
          hierarchy2, boundary, false, fieldConst, delInsNodeMap, workList);
    } else if (deleteOp.getUnchangedOrOldParentNode().getTag() == BastSwitch.TAG) {
      AbstractBastNode parentBlockX = (AbstractBastNode) (deleteOp).getUnchangedOrOldParentNode();
      parentBlockX = (AbstractBastNode) exDiff.firstToSecondMap
          .get(((AbstractBastNode) (deleteOp).getUnchangedOrOldParentNode()));
      if (parentBlockX == null) {
        return;
      }
      assert (parentBlockX != null);
      field = parentBlockX.getField(deleteOp.getOldOrChangedIndex().childrenListNumber);
      insertWildcard1(createUse, replaceMap, changeMap, deleteOp, parentBlockX, field, exDiff,
          hierarchy1, hierarchy2, boundary, false, null, delInsNodeMap, workList);
    } else if (deleteOp.getUnchangedOrOldParentNode().getTag() == BastSwitchCaseGroup.TAG) {
      AbstractBastNode parentBlockX = (AbstractBastNode) (deleteOp).getUnchangedOrOldParentNode();
      parentBlockX = (AbstractBastNode) exDiff.firstToSecondMap
          .get(((AbstractBastNode) (deleteOp).getUnchangedOrOldParentNode()));
      if (parentBlockX == null) {
        return;
      }
      if (parentBlockX.getTag() == BastSwitchCaseGroup.TAG) {
        if (((BastSwitchCaseGroup) deleteOp.getUnchangedOrOldParentNode()).labels
            .size() != ((BastSwitchCaseGroup) parentBlockX).labels.size()
            && ((BastSwitchCaseGroup) parentBlockX).labels.size() > 0
            && ((BastSwitchCaseGroup) parentBlockX).labels.get(0).getTag() != AresWildcard.TAG
            && ((BastSwitchCaseGroup) parentBlockX).labels.get(0).getTag() != AresUseStmt.TAG) {
          BastSwitchCaseGroup originalGroup =
              ((BastSwitchCaseGroup) deleteOp.getUnchangedOrOldParentNode());
          BastSwitchCaseGroup groupClone =
              (BastSwitchCaseGroup) CreateJavaNodeHelper.cloneTree(parentBlockX);

          int start = -1;
          LinkedList<AbstractBastNode> otherLabels = new LinkedList<>();
          LinkedList<AbstractBastNode> newLabels = new LinkedList<>();
          for (int i = 0; i < ((BastSwitchCaseGroup) parentBlockX).labels.size(); i++) {
            AbstractBastNode labelNode = ((BastSwitchCaseGroup) parentBlockX).labels.get(i);
            boolean found = false;
            for (AbstractBastNode partnerLabel : originalGroup.labels) {
              if (WildcardAccessHelper.isEqual(labelNode, partnerLabel)) {
                found = true;
                if (start == -1) {
                  start = i;
                }
                break;
              }
            }
            if (!found) {
              otherLabels.add(labelNode);
            } else {
              newLabels.add(labelNode);
            }
          }
          groupClone.replaceField(BastFieldConstants.SWITCH_CASE_GROUP_LABELS,
              new BastField(newLabels));
          parentBlockX.replaceField(BastFieldConstants.SWITCH_CASE_GROUP_LABELS,
              new BastField(otherLabels));
          NodeParentInformationHierarchy npi = hierarchy2.get(parentBlockX);
          assert (npi != null);
          if (npi.list != null && npi.list.size() > 0) {
            AbstractBastNode parent = npi.list.get(0).parent;
            @SuppressWarnings("unchecked")
            LinkedList<AbstractBastNode> switchStmts = (LinkedList<AbstractBastNode>) parent
                .getField(npi.list.get(0).fieldConstant).getListField();
            int index = switchStmts.indexOf(parentBlockX);
            if (index != -1) {
              if (start != 0) {
                groupClone.replaceField(BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS,
                    parentBlockX.getField(BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS));
                parentBlockX.replaceField(BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS,
                    new BastField((new LinkedList<AbstractBastStatement>())));
                switchStmts.add(index + 1, groupClone);
              } else {
                groupClone.replaceField(BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS,
                    new BastField((new LinkedList<AbstractBastStatement>())));

                switchStmts.add(index, groupClone);
              }
              parent.replaceField(npi.list.get(0).fieldConstant, new BastField(switchStmts));
              parentBlockX = groupClone;
            }
          }

        }
      }
      assert (parentBlockX != null);
      field = parentBlockX.getField(deleteOp.getOldOrChangedIndex().childrenListNumber);
      insertWildcard1(createUse, replaceMap, changeMap, deleteOp, parentBlockX, field, exDiff,
          hierarchy1, hierarchy2, boundary, false, null, delInsNodeMap, workList);
    } else if (deleteOp.getNewOrChangedIndex().childrenListNumber 
        == BastFieldConstants.IF_IF_PART) {
      AbstractBastNode parent = exDiff.firstToSecondMap.get(deleteOp.getUnchangedOrOldParentNode());
      if (parent != null) {
        field = parent.getField(deleteOp.getOldOrChangedIndex().childrenListNumber);
        insertWildcard1(createUse, replaceMap, changeMap, deleteOp, parent, field, exDiff,
            hierarchy1, hierarchy2, boundary, false, null, delInsNodeMap, workList);
        
      }
      
    } else {
      handleExprDelete(createUse, replaceMap, exDiff, hierarchy1, hierarchy2, delInsMap, boundary,
          revertMap, changeMap, delInsNodeMap, workList, deleteOp);
    }
  }

  private static void handleExprDelete(boolean createUse, ReplaceMap replaceMap,
      ExtendedDiffResult exDiff, Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy2,
      HashMap<DeleteOperation, InsertOperation> delInsMap, MatchBoundary boundary,
      HashMap<AbstractBastNode, List<RevertModificationInfo>> revertMap,
      HashMap<AbstractBastNode, BlockChanges> changeMap,
      HashMap<AbstractBastNode, AbstractBastNode> delInsNodeMap,
      ArrayList<BastEditOperation> workList, DeleteOperation editOp) {
    AbstractBastNode node = editOp.getOldOrInsertedNode();
    NodeParentInformationHierarchy npiOld = hierarchy1.get(node);
    AbstractBastNode deleteB2Parent = null;
    int depth = 0;
    if (npiOld != null && npiOld.list != null) {
      for (int j = 0; j < npiOld.list.size(); j++) {
        deleteB2Parent = exDiff.firstToSecondMap.get(npiOld.list.get(j).parent);
        if (deleteB2Parent == null) {
          if (delInsNodeMap.get(npiOld.list.get(j).parent) != null) {
            deleteB2Parent = delInsNodeMap.get(npiOld.list.get(j).parent);
          }
        }
        if (deleteB2Parent != null) {
          depth = j;
          break;
        }
      }
      if (createUse) {
        if (deleteB2Parent.getTag() == BastSwitch.TAG) {
          return;
        }
      }
    } else {
      return;
    }
    NodeParentInformationHierarchy npiNew = hierarchy2.get(deleteB2Parent);
    NodeParentInformation parentBlockInfo = null;
    AbstractBastNode parentNode = null;
    if (npiNew == null) {
      if (deleteB2Parent.getTag() == BastProgram.TAG) {
        return;
      }
      assert (false) : "No parent found";
    }
    loop: for (int j = 0; j < npiNew.list.size(); j++) {
      if (npiNew.list.get(j).parent.getField(npiNew.list.get(j).fieldConstant).isList()) {
        switch (npiNew.list.get(j).parent.getTag()) {
          case AresBlock.TAG:
          case BastBlock.TAG:
            parentBlockInfo = npiNew.list.get(j);
            if (j == 0) {
              parentNode = npiNew.getNode();
            } else {
              parentNode = npiNew.list.get(j - 1).parent;
            }
            break loop;
          case BastSwitchCaseGroup.TAG:
            parentBlockInfo = npiNew.list.get(j);
            if (j == 0) {
              parentNode = npiNew.getNode();
            } else {
              parentNode = npiNew.list.get(j - 1).parent;
            }
            break loop;
          case BastSwitch.TAG:
            parentBlockInfo = npiNew.list.get(j);
            if (j == 0) {
              parentNode = npiNew.getNode();
            } else {
              parentNode = npiNew.list.get(j - 1).parent;
            }
            break loop;
          default:
            continue;
        }
      }
    }
    if (npiNew.list.get(0).parent.getTag() == BastFunction.TAG) {
      return;
    }
    AbstractBastNode parent = null;
    BastFieldConstants fieldConstant = null;
    parent = deleteB2Parent;
    fieldConstant = editOp.getOldOrChangedIndex().childrenListNumber;
    exprWildcardInit(createUse, replaceMap, exDiff, delInsMap, boundary, revertMap, changeMap,
        workList, editOp, node, npiOld, parentBlockInfo, parentNode, depth, parent, fieldConstant);
  }

  private static void exprWildcardInit(boolean createUse, ReplaceMap replaceMap,
      ExtendedDiffResult exDiff, HashMap<DeleteOperation, InsertOperation> delInsMap,
      MatchBoundary boundary, HashMap<AbstractBastNode, List<RevertModificationInfo>> revertMap,
      HashMap<AbstractBastNode, BlockChanges> changeMap, ArrayList<BastEditOperation> workList,
      BastEditOperation editOp, AbstractBastNode node, NodeParentInformationHierarchy npi,
      NodeParentInformation parentBlockInfo, AbstractBastNode parentNode, int depth,
      AbstractBastNode parent, BastFieldConstants fieldConstant) {
    int listIdupdate = -1;

    ArrayList<AbstractBastNode> replacements = new ArrayList<>();

    RevertModificationInfo modInfo = new RevertModificationInfo();
    switch (fieldConstant) {
      case BLOCK_STATEMENT:
        assert (false);
        break;
      case ARES_BLOCK_BLOCK:
        assert (false);
        break;
      case DIRECT_CALL_ARGUMENTS:
        listIdupdate = handleCallArguments(delInsMap, editOp, node, parent, listIdupdate, modInfo,
            replacements, editOp.getOldOrChangedIndex().childrenListIndex,
            editOp.getOldOrChangedIndex().childrenListNumber, exDiff);
        if (listIdupdate == Integer.MAX_VALUE) {
          return;
        }
        break;
      case IDENT_DECLARATOR_EXPRESSION:
        if (parent != null
            && parent.getField(BastFieldConstants.IDENT_DECLARATOR_EXPRESSION) != null
            && parent.getField(BastFieldConstants.IDENT_DECLARATOR_EXPRESSION).getField() == null) {
          AbstractBastNode copy = CreateJavaNodeHelper
              .cloneTree((AbstractBastNode) editOp.getNewOrChangedNode());
          parent.replaceField(BastFieldConstants.IDENT_DECLARATOR_EXPRESSION, new BastField(copy));
          if (parent.info != null 
              && parent.info.tokens != null
              && parent.info.tokens.length > 0) {
            parent.info.tokens[0] = new TokenAndHistory(new JavaToken(BasicJavaToken.EQUAL, "="));
            ((JavaToken)parent.info.tokens[0].token).whiteSpace.append(" ");
          } else {
            assert (false);
            return;
          }
          if (copy.info.tokens == null) {
            copy.info.tokens = new TokenAndHistory[1];
          }
          JavaToken token = CreateJavaNodeHelper.findLeftJavaToken(copy);
          token.whiteSpace.append(" ");
        }
        break;
      case DECLARATION_DECLARATORS:
        if (parent.fieldMap
                .get(BastFieldConstants.DECLARATION_DECLARATORS) != null) {
          @SuppressWarnings("unchecked")
          LinkedList<AbstractBastDeclarator> declarations =
              (LinkedList<AbstractBastDeclarator>) parent.fieldMap
                  .get(BastFieldConstants.DECLARATION_DECLARATORS).getListField();
          if (declarations != null) {
            if (editOp.getOldOrChangedIndex().childrenListIndex >= declarations.size()) {
              AbstractBastNode copy =
                  CreateJavaNodeHelper.cloneTree((AbstractBastNode) editOp.getOldOrInsertedNode());
              declarations.add((AbstractBastDeclarator) copy);
              parent.replaceField(BastFieldConstants.DECLARATION_DECLARATORS,
                  new BastField(declarations));
              if (parent.info.tokens.length > 0) {
                ListToken token = (ListToken) parent.info.tokens[0].token;
                TokenAndHistory tah = new TokenAndHistory(new JavaToken(BasicJavaToken.COMMA, ","));
                token.tokenList.add(tah);
                JavaToken ltoken = CreateJavaNodeHelper.findLeftJavaToken(copy);
                ltoken.whiteSpace.append(" ");
              }
            }
          }
        }
        break;
      case IDENT_DECLARATOR_IDENTIFIER:
      case PARAMETER_DECLARATOR:
      case PARAMETER_TYPE_LIST_PARAMETERS:
      case FUNCTION_BLOCK_MODIFIERS:
        return;
      case ACCESS_TARGET:
      case ARRAY_REF_REF:
      case ASGN_EXPR_LEFT:
      case ASGN_EXPR_RIGHT:
      case CMP_EXPR_LEFT:
      case CMP_EXPR_RIGHT:
      case DECLARATION_MODIFIERS:
      case DECLARATION_SPECIFIER:
      case DIRECT_CALL_FUNCTION:
      case EXPR_INITIALIZER_INIT:
      case IF_CONDITION:
      case RETURN_VALUE:
      case COND_EXPR_FALSE_PART:
      case COND_EXPR_CONDITION:
      case FOR_STMT_CONDITION:
        break;
      default:
        return;

    }
    if (parentBlockInfo == null) {
      return;
    }
    insertExprWildcard(createUse, replaceMap, boundary, revertMap, changeMap, editOp, npi, parent,
        depth, parentBlockInfo, parentNode, listIdupdate, modInfo, replacements, false, exDiff,
        workList);
  }

  private static void insertDeleteWildcard(ExtendedDiffResult exDiff,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy1, MatchBoundary boundary,
      HashMap<AbstractBastNode, BlockChanges> changeMap, BastEditOperation deleteOp,
      AbstractBastNode block, BastFieldConstants fieldConst, LinkedList<AbstractBastStatement> stmt,
      LinkedList<AbstractBastStatement> newStmts, int jpar, boolean exprWildcard,
      AbstractBastStatement annotation, HashMap<AbstractBastNode, AbstractBastNode> delInsNodeMap,
      ReplaceMap replaceMap, ArrayList<BastEditOperation> workList) {
    boolean additionalWildcard = false;
    if (jpar < stmt.size()) {
      if (exprWildcard) {
        additionalWildcard = true;
        newStmts.add(stmt.get(jpar));
      } else if (stmt.get(jpar).getTag() == AresChoiceStmt.TAG) {
        additionalWildcard = true;
        newStmts.add(stmt.get(jpar));
      } else if (exDiff.secondToFirstMap.get(stmt.get(jpar)) != null) {
        if (hierarchy1.get(exDiff.secondToFirstMap.get(stmt.get(jpar))).list.get(0).parent
            .getTag() == block.getTag()) {
          boolean parentChange = false;
          BastEditOperation ep = exDiff.editMapNew.get(stmt.get(jpar));
          if (ep != null && ep.getType() == EditOperationType.STATEMENT_PARENT_CHANGE) {
            if (jpar != ep.getNewOrChangedIndex().childrenListIndex) {
              if (ep.getUnchangedOrOldParentNode() == boundary.getNode1()
                  && ep.getUnchangedOrNewParentNode() == boundary.getNode2()) {
                // do nothing
              } else {
                parentChange = true;
              }
            }

          } else if (ep != null && ep.getType() == EditOperationType.ALIGN
              && workList.contains(ep)) {
            if (ep.getNewOrChangedIndex().childrenListIndex == deleteOp
                .getOldOrChangedIndex().childrenListIndex
                && jpar == deleteOp.getOldOrChangedIndex().childrenListIndex) {
              parentChange = true;
            }
          }
          if (!parentChange) {
            if (!WildcardAccessHelper.isEqual(deleteOp.getOldOrInsertedNode(),
                stmt.get(jpar))) {
              if (exDiff.firstToSecondMap.get(deleteOp.getOldOrInsertedNode()) != stmt.get(jpar)) {
                newStmts.add(stmt.get(jpar));
                additionalWildcard = true;
              }
            }
          }
        } else {
          if (block.getTag() == AresBlock.TAG
              && jpar <= hierarchy1.get(exDiff.secondToFirstMap.get(stmt.get(jpar))).list
                  .get(0).listId) {
            newStmts.add(stmt.get(jpar));
            additionalWildcard = true;

          }
        }
      } else if (stmt.get(jpar).getTag() == AresWildcard.TAG
          || stmt.get(jpar).getTag() == AresUseStmt.TAG) {
        newStmts.add(stmt.get(jpar));
        additionalWildcard = true;

      } else if (stmt.get(jpar).getTag() == AresChoiceStmt.TAG) {
        AresChoiceStmt choice = (AresChoiceStmt) stmt.get(jpar);
        boolean found = false;
        for (AbstractBastNode caseNode : choice.choiceBlock.statements) {
          AresCaseStmt myCase = (AresCaseStmt) caseNode;
          for (AbstractBastNode caseStmt : myCase.block.statements) {
            BastEditOperation ep = exDiff.editMapNew.get(caseStmt);
            if (ep != null) {
              found = true;
            }
          }
        }
        if (found) {
          newStmts.add(stmt.get(jpar));
          additionalWildcard = true;
        }
      }
    }
    if (additionalWildcard || jpar >= stmt.size()) {
      if (stmt.size() > 1 && !WildcardAccessHelper.isWildcard(stmt.getLast())
          && WildcardAccessHelper.isExprWildcard(newStmts.getLast())) {
        if (exDiff.secondToFirstMap.get(stmt.get(jpar + 1)) != null) {
          NodeParentInformationHierarchy npi =
              hierarchy1.get(exDiff.secondToFirstMap.get(stmt.get(jpar + 1)));
          if (npi.list.get(0).listId != jpar) {
            WildcardPlacementComputation.additionalWildcards(block, fieldConst, annotation,
                deleteOp, changeMap, false, boundary, jpar);
          } else {
            WildcardPlacementComputation.additionalWildcards(block, fieldConst, annotation,
                deleteOp, changeMap, true, boundary, jpar);
          }
        } else {
          if (!WildcardAccessHelper.isWildcard(stmt.get(jpar + 1))) {
            WildcardPlacementComputation.additionalWildcards(block, fieldConst, annotation,
                deleteOp, changeMap, false, boundary, jpar);
          } else {
            WildcardPlacementComputation.additionalWildcards(block, fieldConst, annotation,
                deleteOp, changeMap, false, boundary, jpar);
          }
        }
      } else {
        WildcardPlacementComputation.additionalWildcards(block, fieldConst, annotation, deleteOp,
            changeMap, false, boundary, jpar);
      }
    } else {
      WildcardPlacementComputation.updateReplacedStatement(block, fieldConst, stmt.get(jpar),
          annotation, deleteOp, changeMap, replaceMap);
    }
  }

  @SuppressWarnings("unchecked")
  private static void insertExprWildcard(boolean createUse, ReplaceMap replaceMap,
      MatchBoundary boundary, HashMap<AbstractBastNode, List<RevertModificationInfo>> revertMap,
      HashMap<AbstractBastNode, BlockChanges> changeMap, BastEditOperation editOp,
      NodeParentInformationHierarchy npi, AbstractBastNode btwoParent, int depth,
      NodeParentInformation parentBlockInfo, AbstractBastNode parentNode, int listIdupdate,
      RevertModificationInfo modInfo, List<AbstractBastNode> replacements, boolean addNewIndex,
      ExtendedDiffResult extDiff, ArrayList<BastEditOperation> workList) {
    if (editOp.getOldOrInsertedNode().getTag() == BastBlock.TAG) {
      return;
    }
    LinkedList<AbstractBastStatement> stmt;
    LinkedList<AbstractBastStatement> newStmts;
    final int maxVal;
    BastField field;
    AbstractBastNode parentBlock = parentBlockInfo.parent;
    newStmts = new LinkedList<>();
    switch (parentBlockInfo.fieldConstant) {
      case SWITCH_CASE_GROUP_STATEMENTS:
        field = parentBlock.getField(BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS);
        break;
      case BLOCK_STATEMENT:
        field = parentBlock.getField(BastFieldConstants.BLOCK_STATEMENT);
        break;
      case ARES_BLOCK_BLOCK:
        assert (false);
        field = parentBlock.getField(BastFieldConstants.ARES_BLOCK_BLOCK).getField()
            .getField(BastFieldConstants.BLOCK_STATEMENT);
        break;
      case SWITCH_CASE_GROUPS:
        field = parentBlock.getField(BastFieldConstants.SWITCH_CASE_GROUPS);

        break;
      case SWITCH_CASE_GROUP_LABELS:
        field = parentBlock.getField(BastFieldConstants.SWITCH_CASE_GROUP_LABELS);

        break;
      default:
        field = null;
        assert (false);
        return;
    }
    stmt = (LinkedList<AbstractBastStatement>) field.getListField();
    int listId = -1;
    for (int i = 0; i < stmt.size(); i++) {
      if (stmt.get(i) == parentNode) {
        listId = i;
      }
    }
    if (listId == -1) {
      return;
    }
    int newPosition = WildcardPlacementComputation.getPosition(editOp, parentBlock,
        parentBlockInfo.fieldConstant, changeMap, false, listId, extDiff, workList, boundary);

    maxVal = Math.max(stmt.size(), newPosition + 1);

    for (int j = 0; j < maxVal; j++) {
      if (j == newPosition) {

        StringBuffer bufferTmp = PatternGenerator.extractIndentation(stmt, j);
        AbstractBastNode clonePartner =
            getClonePartner(editOp, npi, btwoParent, depth, listIdupdate, extDiff, addNewIndex);
        if (clonePartner == null && editOp.getType() == EditOperationType.INSERT) {
          clonePartner = editOp.getOldOrInsertedNode();
        } else if (clonePartner == null && editOp.getType() == EditOperationType.MOVE) {
          clonePartner = editOp.getNewOrChangedNode();
        } else if (clonePartner == null) {
          clonePartner = editOp.getOldOrInsertedNode();;
        } else if (clonePartner == editOp.getOldOrInsertedNode()
            && editOp.getType() == EditOperationType.MOVE && addNewIndex) {
          clonePartner = editOp.getNewOrChangedNode();
        }

        AbstractBastNode clone =
            (AbstractBastNode) CreateJavaNodeHelper.cloneTree((AbstractBastNode) clonePartner);
        if (clone == null) {
          assert (false);
        }
        IPrettyPrinter printer = ParserFactory.getAresPrettyPrinter();
        clone.accept(printer);
        String toFind = printer.getBuffer().toString();
        printer.getBuffer().setLength(0);
        if (j >= stmt.size()) {

          newPosition = WildcardPlacementComputation.getPosition(editOp, parentBlock,
              parentBlockInfo.fieldConstant, changeMap, false, listId, extDiff, workList, boundary);
          assert (false);
        }
        stmt.get(j).accept(printer);
        String statementString = printer.getBuffer().toString();
        int occurence = -1;
        try {
          occurence = statementString.split(Pattern.quote(toFind), -1).length - 1;
        } catch (PatternSyntaxException e) {
          occurence = 1;
        }
        BastIntConst occurrence = null;
        if (occurence > 1) {
          FindOccurenceVisitor fov = new FindOccurenceVisitor(clonePartner, clone);
          stmt.get(j).accept(fov);
          occurrence = CreateJavaNodeHelper.createBastIntConst(fov.count);

        } else {
          occurrence = CreateJavaNodeHelper.createBastIntConst(1);

        }
        AbstractBastStatement annotation = null;
        if (createUse) {
          annotation = createExprUse(bufferTmp, occurrence, clone);
          CreateJavaNodeHelper.addWhiteSpace(((AresUseStmt)annotation)
              .getField(BastFieldConstants.ARES_USE_STMT_PATTERN).getField(), " ");
        } else {
          annotation = extractExprWildcard(bufferTmp, clone, occurrence);
        }
        if (clonePartner != editOp.getOldOrInsertedNode()
            && clonePartner != editOp.getNewOrChangedNode()) {
          replaceMap.put((AbstractBastNode) clonePartner, annotation, true);
          replaceMap.put((AbstractBastNode) editOp.getOldOrInsertedNode(), annotation, true);
        } else {
          replaceMap.put((AbstractBastNode) editOp.getOldOrInsertedNode(), annotation, true);
        }
        if (editOp.getType() == EditOperationType.MOVE && addNewIndex) {
          replaceMap.put((AbstractBastNode) editOp.getNewOrChangedNode(), annotation, true);

        }
        for (AbstractBastNode n : replacements) {
          replaceMap.put(n, annotation, true);
        }
        newStmts.add(annotation);
        if (editOp.getType() == EditOperationType.DELETE
            || (editOp.getType() == EditOperationType.MOVE && !addNewIndex)) {
          List<RevertModificationInfo> modInfoTmp = revertMap.get(stmt.get(j));
          if (modInfoTmp == null) {
            modInfoTmp = new ArrayList<RevertModificationInfo>();
            revertMap.put(stmt.get(j), modInfoTmp);
          }
          modInfoTmp.add(modInfo);

        }
        JavaToken token = CreateJavaNodeHelper.findLeftJavaToken(stmt.get(j));

        if (token != null) {
          String whitespace = token.whiteSpace.toString();
          if (j != 0 && !whitespace.contains("{") && !whitespace.contains("}")) {
            whitespace = whitespace.replace("\n\n", "");
          }
          token.whiteSpace.replace(0, token.whiteSpace.length(), whitespace);
        }
        if (editOp.getType() == EditOperationType.DELETE) {
          if (editOp.getOldOrInsertedNode().getTag() == BastNameIdent.TAG) {
            FindNodesFromTagVisitor fnftv =
                new FindNodesFromTagVisitor(editOp.getUnchangedOrOldParentNode().getTag());
            stmt.get(j).accept(fnftv);
            if (fnftv.nodes.size() == 1) {
              AbstractBastNode partner = fnftv.nodes.get(0);
              AbstractBastNode clone2 = (AbstractBastNode) CreateJavaNodeHelper
                  .cloneTree((AbstractBastNode) clonePartner);
              JavaToken token2 = CreateJavaNodeHelper.findLeftJavaToken(clone2);

              if (partner.getTag() != BastCmp.TAG && token2 != null) {
                token2.whiteSpace.append(" ");
              }
              if (!editOp.getNewOrChangedIndex().childrenListNumber.isList) {
                partner.replaceField(editOp.getOldOrChangedIndex().childrenListNumber,
                    new BastField(clone2));
              }
            }
          }
        }
        newStmts.add(stmt.get(j));

        WildcardPlacementComputation.updateExpressionWildcard(parentBlock,
            parentBlockInfo.fieldConstant, stmt.get(j), annotation, changeMap, boundary, j);
      } else if (j < stmt.size()) {
        newStmts.add(stmt.get(j));
      }
    }
    field = new BastField(newStmts);
    parentBlock.replaceField(parentBlockInfo.fieldConstant, field);
  }

  private static AbstractBastNode getClonePartner(BastEditOperation deleteOp,
      NodeParentInformationHierarchy npiOld, AbstractBastNode deleteB2Parent, int depth,
      int listIdupdate, ExtendedDiffResult extDiff, boolean addNewIndex) {
    AbstractBastNode clonePartner = null;
    AbstractBastNode tmpParent = deleteB2Parent;
    BastFieldConstants tmpConstant = null;
    int listIndex = -1;
    if (addNewIndex) {
      tmpConstant = deleteOp.getNewOrChangedIndex().childrenListNumber;
      listIndex = deleteOp.getNewOrChangedIndex().childrenListIndex;
      if (listIdupdate != -1) {
        listIndex = listIdupdate;
      }
    } else {
      tmpConstant = deleteOp.getOldOrChangedIndex().childrenListNumber;
      listIndex = deleteOp.getOldOrChangedIndex().childrenListIndex;
      if (listIdupdate != -1) {
        listIndex = listIdupdate;
      }

    }
    int down = depth;
    while (down != 0) {
      if (tmpParent.getField(npiOld.list.get(down).fieldConstant) != null) {
        if (tmpParent.getField(npiOld.list.get(down).fieldConstant).isList()) {
          if (tmpParent.getField(npiOld.list.get(down).fieldConstant).getListField().size()
              <= npiOld.list.get(down).listId) {
            tmpParent = tmpParent.getField(npiOld.list.get(down).fieldConstant).getListField()
                .get(tmpParent.getField(npiOld.list.get(down)
                    .fieldConstant).getListField().size() - 1);
            tmpConstant = npiOld.list.get(down - 1).fieldConstant;
          } else {
            tmpParent = tmpParent.getField(npiOld.list.get(down).fieldConstant).getListField()
                .get(npiOld.list.get(down).listId);
            tmpConstant = npiOld.list.get(down - 1).fieldConstant;
          }
        } else {
          tmpParent = tmpParent.getField(npiOld.list.get(down).fieldConstant).getField();
          if (tmpParent.getTag() == BastNameIdent.TAG) {
            return tmpParent;
          } else {
            tmpConstant = npiOld.list.get(down - 1).fieldConstant;
          }
        }
      }
      down--;
    }
    if (tmpParent.getField(tmpConstant) == null
        && tmpParent.getField(deleteOp.getOldOrChangedIndex().childrenListNumber) != null) {
      tmpConstant = deleteOp.getOldOrChangedIndex().childrenListNumber;
    }
    if (tmpParent.getField(tmpConstant) == null && deleteOp.getType() == EditOperationType.MOVE) {
      tmpConstant = deleteOp.getNewOrChangedIndex().childrenListNumber;
    }
    if (tmpParent.getField(tmpConstant) == null) {
      if (tmpParent.getTag() == BastCall.TAG
          && deleteOp.getOldOrInsertedNode().getTag() == BastCall.TAG) {
        clonePartner = ((BastCall) tmpParent).function;
      } else if (tmpParent.getTag() == BastNameIdent.TAG) {
        clonePartner = tmpParent;
      } else {
        return null;
      }

    } else if (tmpParent.getField(tmpConstant).isList()
        && tmpParent.getField(tmpConstant).getListField() != null) {
      if (deleteOp.getOldOrChangedIndex().childrenListIndex < tmpParent.getField(tmpConstant)
          .getListField().size()) {
        if (listIndex < tmpParent.getField(tmpConstant).getListField().size()) {
          clonePartner = tmpParent.getField(tmpConstant).getListField().get(listIndex);
        } else {
          clonePartner = deleteOp.getOldOrInsertedNode();
        }
      } else {
        clonePartner = deleteOp.getOldOrInsertedNode();
      }
    } else {
      clonePartner = tmpParent.getField(tmpConstant).getField();

    }
    return clonePartner;
  }

  @SuppressWarnings("unchecked")
  private static int handleCallArguments(HashMap<DeleteOperation, InsertOperation> delInsMap,
      BastEditOperation deleteOp, AbstractBastNode node, AbstractBastNode parent, int listIdupdate,
      RevertModificationInfo modInfo, List<AbstractBastNode> replacements, int index,
      BastFieldConstants fieldConstant, ExtendedDiffResult exDiff) {
    final int maxVal;
    BastField field;
    LinkedList<AbstractBastExpr> newExpr = new LinkedList<>();
    field = parent.getField(fieldConstant);
    if (field == null) {
      return -1;
    }

    LinkedList<AbstractBastExpr> expr = null;
    if (field.isList()) {
      expr = (LinkedList<AbstractBastExpr>) field.getListField();
      if (expr == null) {
        return -1;
      }
    } else {
      expr = new LinkedList<>();
      expr.add((AbstractBastExpr) field.getField());
    }
    LinkedList<AbstractBastExpr> partnerExpr = null;
    boolean singleExpression = false;
    if (deleteOp.getUnchangedOrOldParentNode()
        .getField(deleteOp.getOldOrChangedIndex().childrenListNumber).isList()) {
      partnerExpr = (LinkedList<AbstractBastExpr>) deleteOp.getUnchangedOrOldParentNode()
          .getField(deleteOp.getOldOrChangedIndex().childrenListNumber).getListField();
    } else {
      partnerExpr = new LinkedList<AbstractBastExpr>();
      partnerExpr.add((AbstractBastExpr) deleteOp.getUnchangedOrOldParentNode()
          .getField(deleteOp.getOldOrChangedIndex().childrenListNumber).getField());
      singleExpression = true;
      index = 0;
    }
    maxVal = Math.max(expr.size(), index + 1);
    int addDst = -1;
    AbstractBastExpr absExpr = null;
    boolean changed = false;

    for (int j = 0; j < maxVal; j++) {
      int offset = 0;
      if (j == index || (j == 0 && singleExpression)) {
        absExpr = (AbstractBastExpr) CreateJavaNodeHelper.cloneTree((AbstractBastNode) node);
        ArrayList<String> exprList = new ArrayList<>();
        for (int i = 0; i < expr.size(); i++) {
          if (absExpr.getTag() == BastNameIdent.TAG && expr.get(i).getTag() == BastNameIdent.TAG) {
            exprList.add(((BastNameIdent) expr.get(i)).name);
          } else if (expr.get(i).getTag() == BastArrayRef.TAG
              && ((BastArrayRef) expr.get(i)).arrayRef.getTag() == BastNameIdent.TAG) {
            exprList.add(((BastNameIdent) ((BastArrayRef) expr.get(i)).arrayRef).name);
          }
        }
        if (expr != null && expr.size() > 0 && j < expr.size()
            && absExpr.getTag() == BastNameIdent.TAG && expr.get(j).getTag() == BastNameIdent.TAG) {
          if (((BastNameIdent) absExpr).name.equals(((BastNameIdent) expr.get(j)).name)
              || singleExpression) {

            ArrayList<String> partnerExprList = new ArrayList<>();
            for (int i = 0; i < partnerExpr.size(); i++) {
              if (partnerExpr.get(i).getTag() == BastNameIdent.TAG) {
                partnerExprList.add(((BastNameIdent) partnerExpr.get(i)).name);
              }
            }
            for (int i = 0; i < expr.size(); i++) {
              if (expr.get(i).getTag() == BastNameIdent.TAG) {
                exprList.add(((BastNameIdent) expr.get(i)).name);
              }
            }
            IStringSimilarityMeasure stringSim = new NGramCalculator(2, 10, 10);
            for (int i = index; i < partnerExprList.size(); i++) {
              if (!partnerExprList.get(i).equals(((BastNameIdent) absExpr).name)
                  && !(exprList.contains(partnerExprList.get(i)))) {
                float sim =
                    stringSim.similarity(partnerExprList.get(i), ((BastNameIdent) absExpr).name);
                if (sim > 0.6) {
                  absExpr = (AbstractBastExpr) CreateJavaNodeHelper
                      .cloneTree((AbstractBastNode) partnerExpr.get(i));
                  absExpr.info.tokens[0].prevTokens
                      .addAll(partnerExpr.get(j).info.tokens[0].prevTokens);
                  offset = i;
                  if (deleteOp.getType() == EditOperationType.MOVE) {
                    if (singleExpression) {
                      offset = i == 0 ? deleteOp.getNewOrChangedIndex().childrenListIndex + 1 : i;
                    } else {

                      int simPos = index;
                      for (int k = index; k < exprList.size(); k++) {
                        if (absExpr.getTag() == BastNameIdent.TAG) {
                          sim =
                              stringSim.similarity(exprList.get(k), ((BastNameIdent) absExpr).name);
                        } else {
                          sim = 0;
                        }
                        if (sim < 0.6) {
                          simPos = k;
                          break;
                        }
                      }
                      offset = simPos;
                    }
                  }
                  listIdupdate = offset;
                  changed = true;
                  replacements.add(partnerExpr.get(i));
                  break;
                }
              }
            }
          }

        } else if (expr != null && expr.size() > 0 && j < expr.size()
            && absExpr.getTag() == BastArrayRef.TAG
            && (((BastArrayRef) absExpr).arrayRef).getTag() == BastNameIdent.TAG
            && expr.get(j).getTag() == BastArrayRef.TAG
            && (((BastArrayRef) expr.get(j)).arrayRef).getTag() == BastNameIdent.TAG) {
          if (((BastNameIdent) ((BastArrayRef) absExpr).arrayRef).name.equals(
              ((BastNameIdent) ((BastArrayRef) expr.get(j)).arrayRef).name) || singleExpression) {

            ArrayList<String> partnerExprList = new ArrayList<>();

            for (int i = 0; i < partnerExpr.size(); i++) {
              if (partnerExpr.get(i).getTag() == BastArrayRef.TAG
                  && ((BastArrayRef) partnerExpr.get(i)).arrayRef.getTag() == BastNameIdent.TAG) {
                partnerExprList
                    .add(((BastNameIdent) ((BastArrayRef) partnerExpr.get(i)).arrayRef).name);
              }
            }
            for (int i = 0; i < expr.size(); i++) {
              if (expr.get(i).getTag() == BastArrayRef.TAG
                  && ((BastArrayRef) expr.get(i)).arrayRef.getTag() == BastNameIdent.TAG) {
                exprList.add(((BastNameIdent) ((BastArrayRef) expr.get(i)).arrayRef).name);
              }
            }
            IStringSimilarityMeasure stringSim = new NGramCalculator(2, 10, 10);
            for (int i = index; i < partnerExprList.size(); i++) {
              if (!partnerExprList.get(i)
                  .equals(((BastNameIdent) ((BastArrayRef) absExpr).arrayRef).name)
                  && !(exprList.contains(partnerExprList.get(i)))) {
                float sim = stringSim.similarity(partnerExprList.get(i),
                    ((BastNameIdent) ((BastArrayRef) absExpr).arrayRef).name);
                if (sim > 0.6) {
                  absExpr = (AbstractBastExpr) CreateJavaNodeHelper
                      .cloneTree((AbstractBastNode) partnerExpr.get(i));
                  absExpr.info.tokens[0].prevTokens
                      .addAll(partnerExpr.get(j).info.tokens[0].prevTokens);
                  offset = i;
                  if (deleteOp.getType() == EditOperationType.MOVE) {
                    if (singleExpression) {
                      offset = deleteOp.getNewOrChangedIndex().childrenListIndex;
                    } else {
                      offset = j + 1;
                    }
                  }
                  listIdupdate = offset;
                  changed = true;
                  replacements.add(partnerExpr.get(i));
                  break;
                }
              }
            }
          }

        } else if (j >= expr.size() && absExpr.getTag() == BastArrayRef.TAG
            && (((BastArrayRef) absExpr).arrayRef).getTag() == BastNameIdent.TAG) {
          absExpr = (AbstractBastExpr) CreateJavaNodeHelper
              .cloneTree((AbstractBastNode) deleteOp.getOldOrInsertedNode());
          if (expr.size() > 0) {
            addComma(parent, absExpr);
          }
          newExpr.add(absExpr);
          modInfo.field = field;
          modInfo.fieldConstant = fieldConstant;
          modInfo.parent = parent;
          field = new BastField(newExpr);
          parent.replaceField(fieldConstant, field);
          replacements.add(deleteOp.getOldOrInsertedNode());
          return index;
        }
        if (absExpr.getTag() == BastNameIdent.TAG
            && exprList.contains(((BastNameIdent) absExpr).name)) {
          AbstractBastNode parentPartner = exDiff
              .secondToFirstMap.get(deleteOp.getUnchangedOrOldParentNode());
          if (parentPartner != null && parentPartner.getTag() == BastCall.TAG) {
            LinkedList<? extends AbstractBastNode> partnerList 
                = parentPartner.getField(BastFieldConstants
                .DIRECT_CALL_ARGUMENTS).getListField();
            if (partnerList.size() > j) {
              if (partnerList.get(j).getTag() != BastNameIdent.TAG) {
                newExpr.add(expr.get(j));
                continue;
              }
            }
          }
          return Integer.MAX_VALUE;
        }
        if (j + 1 >= expr.size()) {
          if (changed || deleteOp.getType() == EditOperationType.DELETE) {
            if (j < expr.size() && expr.get(j).getTag() != absExpr.getTag()
                && deleteOp.getType() == EditOperationType.DELETE) {
              newExpr.add(expr.get(j));
            } else {
              if (expr.size() == partnerExpr.size() && j < expr.size()
                  && expr.get(j).getTag() == absExpr.getTag()
                  && deleteOp.getType() == EditOperationType.DELETE) {
                newExpr.add(expr.get(j));
              } else {
                newExpr.add(absExpr);
              }
            }
          }
        } else {
          if (offset == 0) {
            if (changed || deleteOp.getType() == EditOperationType.DELETE) {
              if (j == 0 && (expr.get(j).getTag() != BastNameIdent.TAG
                  || expr.size() == partnerExpr.size())) {
                newExpr.add(expr.get(j));
              }

            }
          }
        }
        if (j < expr.size()) {
          if (!delInsMap.containsKey(deleteOp)) {
            if (offset > 0) {
              newExpr.add(j, expr.get(j));
              if (offset == newExpr.size()) {
                if (changed || deleteOp.getType() == EditOperationType.DELETE) {
                  newExpr.add(absExpr);
                }
              } else {
                addDst = offset - newExpr.size() + index;
              }
              boolean commaFound = false;
              for (IGeneralToken token : absExpr.info.tokens[0].prevTokens) {
                if (token instanceof JavaToken) {
                  if (((JavaToken) token).type == BasicJavaToken.COMMA) {
                    commaFound = true;
                  }
                }
              }
              if (!commaFound) {
                addComma(parent, absExpr);
              }
            } else {
              if (newExpr.size() > j) {
                if (newExpr.get(j) != expr.get(j)) {
                  newExpr.add(expr.get(j));
                }
              } else {
                newExpr.add(expr.get(j));
              }
              if (absExpr.info.tokens != null) {
                if (expr.get(j) != null && expr.get(j).info != null
                    && expr.get(j).info.tokens != null && expr.get(j).info.tokens[0] != null) {
                  absExpr.info.tokens[0].prevTokens.addAll(expr.get(j).info.tokens[0].prevTokens);
                }
              }
              if (changed || deleteOp.getType() == EditOperationType.DELETE) {
                if (expr.get(j) != null && expr.get(j).info != null
                    && expr.get(j).info.tokens != null && expr.get(j).info.tokens[0] != null) {
                  modInfo.updateTokens(expr.get(j), expr.get(j).info.tokens[0].prevTokens);
                  expr.get(j).info.tokens[0].prevTokens.clear();
                }

                if (j > 0 || newExpr.size() > 1) {
                  addComma(parent, expr.get(j));
                }
              }

            }
          }

        } else {
          if (expr.size() > 0) {
            addComma(parent, absExpr);
          }
        }
      } else if (j > index && j < expr.size()) {

        newExpr.add(expr.get(j));
        if (addDst != -1 && j == addDst && absExpr != null) {
          if (changed || deleteOp.getType() == EditOperationType.DELETE) {

            newExpr.add(absExpr);
          }
        }
      } else if (j < index && j < expr.size()) {

        newExpr.add(expr.get(j));
        if (addDst != -1 && j == addDst && absExpr != null) {
          if (changed || deleteOp.getType() == EditOperationType.DELETE) {

            newExpr.add(absExpr);
          }
        }
      }
    }
    modInfo.field = field;
    modInfo.fieldConstant = fieldConstant;
    modInfo.parent = parent;
    field = new BastField(newExpr);
    parent.replaceField(fieldConstant, field);
    return listIdupdate;
  }

  private static void addComma(AbstractBastNode parent, AbstractBastNode node) {
    if (parent.getTag() == BastCall.TAG) {
      ((ListToken) parent.info.tokens[1].token).tokenList
          .add(new TokenAndHistory(new JavaToken(BasicJavaToken.COMMA, ", ")));
    } else {
      assert (false);
    }
  }

  private static AbstractBastStatement extractExprWildcard(StringBuffer bufferTmp,
      AbstractBastNode clone, BastIntConst occurrence) {
    BastNameIdent ident = CreateJavaNodeHelper.createBastNameIdent("expr");
    CreateJavaNodeHelper.addWhiteSpace(ident, " ");
    AresPatternClause pattern =
        CreateJavaNodeHelper.createAresPatternClause(null, occurrence, clone, null);
    LinkedList<AbstractBastExpr> exprList = new LinkedList<>();
    exprList.add(pattern);
    AresPluginClause plugin = CreateJavaNodeHelper.createAresPluginClause(null, ident, exprList);
    AbstractBastStatement wildcard =
        CreateJavaNodeHelper.createAresWildcard(bufferTmp.toString(), null, plugin, null);
    return wildcard;
  }

  static void handleMoveDeleteStep(boolean createUse, ExtendedDiffResult exDiff,
      BastEditOperation moveOp, ReplaceMap replaceMap,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy2, MatchBoundary boundary,
      HashMap<AbstractBastNode, BlockChanges> changeMap,
      HashMap<AbstractBastNode, AbstractBastNode> delInsNodeMap,
      ArrayList<BastEditOperation> workList) {
    LinkedList<AbstractBastStatement> stmt;
    LinkedList<AbstractBastStatement> newStmts;
    BastField field; 
    AbstractBastNode oldParentStmt =
        exDiff.firstToSecondMap.get(moveOp.getUnchangedOrOldParentNode());
    if (moveOp.getUnchangedOrOldParentNode() == boundary.getNode1()
        && moveOp.getUnchangedOrNewParentNode() != boundary.getNode2()) {
      oldParentStmt = boundary.getNode2();
    } else if (moveOp.getUnchangedOrOldParentNode() == boundary.getNode1()
        && moveOp.getUnchangedOrNewParentNode() == boundary.getNode2()) {
      oldParentStmt = boundary.getNode2();
    }
    oldParentStmt = optimizeBlockMapping(moveOp.getUnchangedOrOldParentNode(), oldParentStmt,
        exDiff.firstToSecondMap, exDiff, hierarchy1, hierarchy2);

    if (oldParentStmt != null) {
      field = oldParentStmt.getField(moveOp.getOldOrChangedIndex().childrenListNumber);
      if (field == null) {
        return;
      }
      if (field.isList()) {
        if (moveOp
            .getOldOrChangedIndex().childrenListNumber 
            != BastFieldConstants.DIRECT_CALL_ARGUMENTS) {

          insertWildcard1(createUse, replaceMap, changeMap, moveOp, oldParentStmt, field, exDiff,
              hierarchy1, hierarchy2, boundary, true, null, delInsNodeMap, workList);
        }

      } else {
        if (field != null && field.getField() != null
            && field.getField().getTag() == BastBlock.TAG) {
          stmt = ((BastBlock) (field.getField())).statements;
          newStmts = new LinkedList<>();

          StringBuffer bufferTmp = new StringBuffer();
          bufferTmp = PatternGenerator.extractIndentation(moveOp.getOldOrInsertedNode());

          AbstractBastStatement annotation = null;
          if (createUse) {
            annotation = createStmtUse(bufferTmp);
          } else {
            annotation = createWildcard1(bufferTmp);
          }
          if (field.isList()) {
            CreateJavaNodeHelper.addReturnChar(annotation);
          }
          for (AbstractBastNode node : stmt) {
            replaceMap.put(node, annotation, false);
          }
          newStmts.add(annotation);

          BastField newfield = new BastField(newStmts);
          field.getField().replaceField(BastFieldConstants.BLOCK_STATEMENT, newfield);

        }
      }
    }
  }

  /**
   * Handle move insert step.
   *
   * @param createUse the create use
   * @param exDiff the ex diff
   * @param moveOp the move op
   * @param replaceMap the replace map
   * @param hierarchy1 the hierarchy1
   * @param hierarchy2 the hierarchy2
   * @param boundary the boundary
   * @param changeMap the change map
   * @param delInsNodeMap the del ins node map
   * @param workList the work list
   */
  static void handleMoveInsertStep(boolean createUse, ExtendedDiffResult exDiff,
      MoveOperation moveOp, ReplaceMap replaceMap,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy2, MatchBoundary boundary,
      HashMap<AbstractBastNode, BlockChanges> changeMap,
      HashMap<AbstractBastNode, AbstractBastNode> delInsNodeMap,
      ArrayList<BastEditOperation> workList) {
    LinkedList<AbstractBastStatement> stmt;
    LinkedList<AbstractBastStatement> newStmts;
    AbstractBastNode block;
    BastField field;
    AbstractBastNode parentStmt = moveOp.getUnchangedOrNewParentNode();
    assert (parentStmt != null);
    field = parentStmt.getField(moveOp.getNewOrChangedIndex().childrenListNumber);
    if (field == null) {
      return;
    }
    if (field != null && field.isList()) {
      insertWildcard1(createUse, replaceMap, changeMap, moveOp, parentStmt, field, exDiff,
          hierarchy1, hierarchy2, boundary, false, null, delInsNodeMap, workList);
    } else {
      if (field.getField().getTag() == BastBlock.TAG) {
        stmt = ((BastBlock) (field.getField())).statements;
        block = field.getField();
        newStmts = new LinkedList<>();
        StringBuffer bufferTmp = new StringBuffer();
        bufferTmp = PatternGenerator.extractIndentation(stmt, 0);

        AbstractBastStatement annotation = null;
        if (createUse) {
          annotation = createStmtUse(bufferTmp);
        } else {
          annotation = createWildcard1(bufferTmp);
        }
        AbstractBastNode oldParentStmt =
            exDiff.firstToSecondMap.get(moveOp.getUnchangedOrOldParentNode());
        oldParentStmt = optimizeBlockMapping(moveOp.getUnchangedOrOldParentNode(), oldParentStmt,
            exDiff.firstToSecondMap, exDiff, hierarchy1, hierarchy2);

        for (AbstractBastNode node : stmt) {
          replaceMap.put(node, annotation, false);
          WildcardPlacementComputation.updateReplacedStatement(block,
              BastFieldConstants.BLOCK_STATEMENT, node, annotation, moveOp, changeMap, replaceMap);

        }
        newStmts.add(annotation);

        BastField newfield = new BastField(newStmts);
        field.getField().replaceField(BastFieldConstants.BLOCK_STATEMENT, newfield);
      }
    }
  }

  /**
   * Handle expr move.
   *
   * @param createUse the create use
   * @param exDiff the ex diff
   * @param replaceMap the replace map
   * @param hierarchy1 the hierarchy1
   * @param hierarchy2 the hierarchy2
   * @param boundary the boundary
   * @param changeMap the change map
   * @param revertMap the revert map
   * @param delInsMap the del ins map
   * @param delInsNodeMap the del ins node map
   * @param workList the work list
   * @param moveOp the move op
   */
  static void handleExprMove(boolean createUse, ExtendedDiffResult exDiff,
      ReplaceMap replaceMap, Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy2, MatchBoundary boundary,
      HashMap<AbstractBastNode, BlockChanges> changeMap,
      HashMap<AbstractBastNode, List<RevertModificationInfo>> revertMap,
      HashMap<DeleteOperation, InsertOperation> delInsMap,
      HashMap<AbstractBastNode, AbstractBastNode> delInsNodeMap,
      ArrayList<BastEditOperation> workList, MoveOperation moveOp, boolean insertStmt) {
    BastField field;
    AbstractBastNode node = moveOp.getNewOrChangedNode();
    NodeParentInformationHierarchy npi = hierarchy2.get(moveOp.getNewOrChangedNode());
    NodeParentInformation parentBlockInfo = null;
    AbstractBastNode parentNode = null;
    AbstractBastNode insertB2Parent = null;
    int depth = 0;
    int listIdupdate = -1;
    RevertModificationInfo modInfo = new RevertModificationInfo();
    ArrayList<AbstractBastNode> replacements = new ArrayList<>();
    boolean addNewIndex = true;
    if (moveOp.getUnchangedOrNewParentNode().getTag() == AresPatternClause.TAG) {

      insertB2Parent = exDiff.firstToSecondMap.get(moveOp.getUnchangedOrOldParentNode());
      if (insertB2Parent == null) {
        return;
      }
      npi = hierarchy2.get(insertB2Parent);
      for (int j = 0; j < npi.list.size(); j++) {
        if (npi.list.get(j).parent.getTag() == BastBlock.TAG) {
          parentBlockInfo = npi.list.get(j);
          if (j == 0) {
            parentNode = insertB2Parent;
          } else {
            parentNode = npi.list.get(j - 1).parent;
          }
          break;
        }
      }
      if (moveOp.getOldOrChangedIndex().childrenListNumber.isList) {
        listIdupdate = handleCallArguments(delInsMap, moveOp, node, insertB2Parent, listIdupdate,
            modInfo, replacements, moveOp.getOldOrChangedIndex().childrenListIndex,
            moveOp.getOldOrChangedIndex().childrenListNumber, exDiff);
      }
      addNewIndex = false;
    } else {
      insertB2Parent = moveOp.getUnchangedOrNewParentNode();
      for (int j = 0; j < npi.list.size(); j++) {
        if (npi.list.get(j).parent.getTag() == BastBlock.TAG
            || npi.list.get(j).parent.getTag() == BastSwitchCaseGroup.TAG) {
          parentBlockInfo = npi.list.get(j);
          if (j == 0) {
            parentNode = insertB2Parent;
          } else {
            parentNode = npi.list.get(j - 1).parent;
          }
          break;
        }
      }
      if (moveOp
          .getNewOrChangedIndex().childrenListNumber == BastFieldConstants.DIRECT_CALL_ARGUMENTS) {
        listIdupdate = handleCallArguments(delInsMap, moveOp, node, insertB2Parent, listIdupdate,
            modInfo, replacements, moveOp.getNewOrChangedIndex().childrenListIndex,
            moveOp.getNewOrChangedIndex().childrenListNumber, exDiff);
      }
      addNewIndex = true;
    }
    if (parentBlockInfo == null) {
      assert (false);
      return;
    }
    @SuppressWarnings("unchecked")
    LinkedList<AbstractBastStatement> stmtTest =
        (LinkedList<AbstractBastStatement>) parentBlockInfo.parent
            .getField(parentBlockInfo.fieldConstant).getListField();
    int listId = -1;
    for (int i = 0; i < stmtTest.size(); i++) {
      if (stmtTest.get(i) == parentNode) {
        listId = i;
      }
    }
    if (listId != -1) {
      if (insertStmt 
          && (moveOp.getNewOrChangedIndex().childrenListNumber == BastFieldConstants.IF_IF_PART
          || moveOp.getNewOrChangedIndex().childrenListNumber == BastFieldConstants.IF_ELSE_PART
          || moveOp.getNewOrChangedIndex().childrenListNumber 
          == BastFieldConstants.CATCH_CLAUSE_BLOCK)) {
        if (moveOp.getNewOrChangedNode().getTag() != BastBlock.TAG) {
          field = moveOp.getUnchangedOrNewParentNode()
              .getField(moveOp.getNewOrChangedIndex().childrenListNumber);


          insertWildcard1(createUse, replaceMap, changeMap, moveOp,
              moveOp.getUnchangedOrNewParentNode(), field, exDiff, hierarchy1, hierarchy2, boundary,
              false, null, delInsNodeMap, workList);
        }
      } else if (moveOp.getOldOrChangedIndex().childrenListNumber == BastFieldConstants.IF_IF_PART
          || moveOp.getOldOrChangedIndex().childrenListNumber == BastFieldConstants.IF_ELSE_PART
          || moveOp.getOldOrChangedIndex().childrenListNumber 
          == BastFieldConstants.CATCH_CLAUSE_BLOCK) {
        field = moveOp.getUnchangedOrOldParentNode()
            .getField(moveOp.getOldOrChangedIndex().childrenListNumber);
        AbstractBastNode partner =
            exDiff.firstToSecondMap.get(moveOp.getUnchangedOrOldParentNode());
        if (partner != null) {
          insertWildcard1(createUse, replaceMap, changeMap, moveOp,
              partner, field, exDiff, hierarchy1, hierarchy2, boundary,
              true, null, delInsNodeMap, workList);
        }

      } else {
        insertExprWildcard(createUse, replaceMap, boundary, revertMap, changeMap, moveOp, npi,
            insertB2Parent, depth, parentBlockInfo, parentNode, listIdupdate, modInfo, replacements,
            addNewIndex, exDiff, workList);
      }
    }
  }

  static void handleUpdate(boolean useVersion, ExtendedDiffResult exDiff, BastEditOperation editOp,
      HashMap<AbstractBastNode, AbstractBastNode> deleteInsertNodeMap) {
    if (useVersion) {
      return;
    }
    if (editOp.getOldOrInsertedNode().getTag() == BastNameIdent.TAG) {
      AbstractBastNode partner = exDiff.firstToSecondMap.get(editOp.getOldOrInsertedNode());
      deleteInsertNodeMap.put(editOp.getOldOrInsertedNode(), partner);
    }

  }

}
