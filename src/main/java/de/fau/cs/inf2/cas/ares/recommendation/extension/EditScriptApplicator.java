/*
 * Copyright (c) 2017 Programming Systems Group, CS Department, FAU
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package de.fau.cs.inf2.cas.ares.recommendation.extension;

import de.fau.cs.inf2.cas.ares.bast.general.AresWrapper;
import de.fau.cs.inf2.cas.ares.bast.general.ParentHierarchyHandler;
import de.fau.cs.inf2.cas.ares.bast.general.ParserFactory;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresCaseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.pcreation.PatternGenerator;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.recommendation.ExtendedAresPattern;
import de.fau.cs.inf2.cas.ares.recommendation.Recommendation;
import de.fau.cs.inf2.cas.ares.recommendation.WildcardInstance;
import de.fau.cs.inf2.cas.ares.recommendation.visitors.ApplyWhitespaceVisitor;
import de.fau.cs.inf2.cas.ares.recommendation.visitors.FindParentMethodVisitor;
import de.fau.cs.inf2.cas.ares.recommendation.visitors.ReplaceNameVisitor;
import de.fau.cs.inf2.cas.ares.recommendation.visitors.ReplaceValueVisitor;
import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.BastInfo;
import de.fau.cs.inf2.cas.common.bast.general.CreateJavaNodeHelper;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformation;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAccess;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAsgnExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBoolConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCmp;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEmptyStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastForStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastLabelStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastListInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNew;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameter;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameterList;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.nodes.BastRealConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStringConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStructDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStructMember;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitch;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitchCaseGroup;
import de.fau.cs.inf2.cas.common.bast.nodes.BastUnaryExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastWhileStatement;
import de.fau.cs.inf2.cas.common.bast.type.BastBasicType;
import de.fau.cs.inf2.cas.common.bast.visitors.FindNodesFromTagVisitor;
import de.fau.cs.inf2.cas.common.bast.visitors.GetMethodsVisitor;
import de.fau.cs.inf2.cas.common.bast.visitors.IPrettyPrinter;

import de.fau.cs.inf2.cas.common.parser.AresExtension;
import de.fau.cs.inf2.cas.common.parser.odin.BasicJavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.JavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.ListToken;
import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.DeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.UpdateOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.AdvancedEditOperation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

@SuppressWarnings({ "PMD.ExcessiveClassLength", "PMD.TooManyMethods" })
public class EditScriptApplicator {

  private static final boolean DEBUG_APPLICATION = false;

  /**
   * Find left java token.
   *
   * @param node the node
   * @return the java token
   */
  private static JavaToken findRightJavaToken(AbstractBastNode node) {
    boolean continueLoop = true;
    while (continueLoop) {
      if (node == null) {
        break;
      }
      if (node.info.tokensBefore != null && node.info.tokensBefore.size() > 0) {
        break;
      }
      switch (node.getTag()) {
        case BastBlock.TAG:
        case AresChoiceStmt.TAG:
        case AresCaseStmt.TAG:
        case AresBlock.TAG:
        case BastSwitch.TAG:
          continueLoop = false;
          break;
        default:
          assert (false);
          continueLoop = false;
          break;
      }
    }
    if (node != null && node.info != null) {
      if (node.info.tokensAfter != null && node.info.tokensAfter.size() > 0
          && node.info.tokensAfter.getLast().token != null) {
        return (JavaToken) node.info.tokensBefore.getLast().token;

      } else if (node.info.tokens != null && node.info.tokens.length > 0) {
        for (int i = node.info.tokens.length - 1; i >= 0; i++) {
          if (node.info.tokens[i] != null) {
            return (JavaToken) node.info.tokens[0].token;
          }
        }
      }
    }
    return null;
  }


  /**
   * Apply edit operations.
   *
   * @param program the program
   * @param template the template
   * @param start the start
   * @return the abstract bast node
   * @throws NotFoundException the not found exception
   */
  @SuppressWarnings("unchecked")
  public static LinkedList<Recommendation> applyEditOperations(AbstractBastNode program,
      ExtendedAresPattern template, AbstractBastNode start) throws NotFoundException {
    IPrettyPrinter printer = ParserFactory.getAresPrettyPrinter();
    program.accept(printer);
    FindParentMethodVisitor fpmv = new FindParentMethodVisitor(start);
    program.accept(fpmv);
    GetMethodsVisitor gmv = new GetMethodsVisitor();
    program.accept(gmv);
    final int methodNumber = gmv.idMap2Function.get(fpmv.funct);

    final Map<AbstractBastNode, NodeParentInformationHierarchy> templateParents =
        ParentHierarchyHandler.getParentHierarchy(template.modifiedAst);
    Map<AbstractBastNode, NodeParentInformationHierarchy> templateOriginalParents =
        ParentHierarchyHandler.getParentHierarchy(template.originalAst);
    LinkedList<BastEditOperation> editOperations = new LinkedList<>();
    for (BastEditOperation editOp : template.getDiffResult().editScript) {
      if (editOp instanceof AdvancedEditOperation) {
        editOperations.add(((AdvancedEditOperation) editOp).getBasicOperation());
      } else {
        editOperations.add(editOp);
      }
    }
    LinkedList<BastEditOperation> updates = new LinkedList<>();
    LinkedList<BastEditOperation> deletes = new LinkedList<>();
    ArrayList<BastEditOperation> inserts = new ArrayList<>();
    final RecommendationDeleteComparator deleteComparator =
        new RecommendationDeleteComparator(program, template.assignmentMap, template);
    final InsertComparator newInsertComparator = new InsertComparator(template.modifiedAst);
    orderEditOperations(template, templateParents, templateOriginalParents, editOperations, updates,
        deletes, inserts);

    LinkedList<AbstractBastNode> foundPatternList = null;
    if (template.patternStart.getTag() == BastBlock.TAG) {

      foundPatternList = (LinkedList<AbstractBastNode>) ((BastBlock) template.patternStart)
          .getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
    } else {
      foundPatternList = new LinkedList<>();
      foundPatternList.add(template.patternStart);
    }
    LinkedList<AbstractBastStatement> relevantNodes = new LinkedList<>();
    HashSet<AbstractBastNode> values = new HashSet<>();
    values.addAll(template.assignmentMap.values());
    ArrayList<AbstractBastNode> arrayList = new ArrayList<>(foundPatternList);
    for (int i = template.patternStartListIdOld; i < foundPatternList.size(); i++) {
      if (i >= 0 && i < arrayList.size() && values.contains(arrayList.get(i))) {
        relevantNodes.add((AbstractBastStatement) arrayList.get(i));
      }
    }
    LinkedList<AbstractBastNode> wildcardNodes = new LinkedList<>();
    for (WildcardInstance instance : template.wildcardInstances) {
      for (AbstractBastNode node : instance.headNodes) {
        if (node instanceof AbstractBastStatement) {
          wildcardNodes.add((AbstractBastStatement) node);
        }
      }
    }
    foundPatternList.removeAll(relevantNodes);
    foundPatternList.removeAll(wildcardNodes);
    if (template.patternStartFieldId.isList) {
      template.patternStart.replaceField(template.patternStartFieldId,
          new BastField(foundPatternList));
    }
    BastBlock block = null;
    if (template.patternStart.info != null && template.patternStart.info.tokens != null
        && template.patternStart.info.tokens.length > 0) {
      block = CreateJavaNodeHelper.createBlock(template.patternStart.info.tokens[0], relevantNodes);
    } else {
      block = CreateJavaNodeHelper.createBlock(null, relevantNodes);
    }
    final Map<AbstractBastNode, NodeParentInformationHierarchy> programParents =
        ParentHierarchyHandler.getParentHierarchy(block);
    final AbstractBastNode shadowBlock = block;

    block =
        applyAllSortedEditOperations(program, template, printer, templateParents, updates, deletes,
            inserts, deleteComparator, newInsertComparator, block, programParents, shadowBlock);
    HashMap<String, String> nameReplaceMap = createRenameMap(template, updates, block);

    postProcessRecommendation(program, template, foundPatternList, block, shadowBlock,
        nameReplaceMap);
    return generateRecommendations(program, fpmv, methodNumber);
  }


  private static void postProcessRecommendation(AbstractBastNode program,
      ExtendedAresPattern template, LinkedList<AbstractBastNode> foundPatternList, BastBlock block,
      final AbstractBastNode shadowBlock, HashMap<String, String> nameReplaceMap) {
    block = handleUses(template, block, shadowBlock, nameReplaceMap);
    printAst(block);

    applyWhiteSpaceToMatchBlock(foundPatternList, program, template, block);
    if (template.patternStartListIdOld > foundPatternList.size()
        || template.patternStartListIdOld < 0) {
      foundPatternList.addAll(foundPatternList.size(), block.statements);
    } else {
      foundPatternList.addAll(template.patternStartListIdOld, block.statements);
    }
    if (template.patternStartFieldId.isList) {
      template.patternStart.replaceField(template.patternStartFieldId,
          new BastField(foundPatternList));
    } else {
      if (foundPatternList.size() == 1) {
        // template.patternStart.replaceField(template.patternStartFieldId,
        // new BastField(foundPatternList.get(0)));
      } else {
        LinkedList<AbstractBastStatement> stmts = new LinkedList<>();
        for (AbstractBastNode node : foundPatternList) {
          stmts.add((AbstractBastStatement) node);
        }
        BastBlock replacementBlock =
            CreateJavaNodeHelper.createBlock(template.patternStart.info.tokens[0], stmts);
        template.patternStart.replaceField(template.patternStartFieldId,
            new BastField(replacementBlock));
      }
    }
    IPrettyPrinter printer;
    printer = ParserFactory.getAresPrettyPrinter();
    program.accept(printer);
    printAst(program);
  }


  private static BastBlock applyAllSortedEditOperations(AbstractBastNode program,
      ExtendedAresPattern template, IPrettyPrinter printer,
      final Map<AbstractBastNode, NodeParentInformationHierarchy> templateParents,
      LinkedList<BastEditOperation> updates, LinkedList<BastEditOperation> deletes,
      ArrayList<BastEditOperation> inserts, final RecommendationDeleteComparator deleteComparator,
      final InsertComparator newInsertComparator, BastBlock block,
      final Map<AbstractBastNode, NodeParentInformationHierarchy> programParents,
      final AbstractBastNode shadowBlock) {
    printAst(block);
    applyUpdate(template, printer, updates);
    printAst(block);

    Collections.sort(deletes, deleteComparator);
    Collections.reverse(deletes);
    printAst(template.patternStart);
    block = applyDeletes(template, deletes, block, programParents);
    printAst(block);
    printAst(shadowBlock);



    LinkedList<BastEditOperation> newDeletes =
        identifyAdditionalDeletes(template, block, programParents);
    block = applyDeletes(template, newDeletes, block, programParents);
    printAst(block);

    Collections.sort(inserts, newInsertComparator);
    block = applyInserts(template, templateParents, inserts, block, shadowBlock, true);
    printAst(block);
    printer = ParserFactory.getAresPrettyPrinter();
    program.accept(printer);
    printAst(block);
    return block;
  }


  private static LinkedList<BastEditOperation> identifyAdditionalDeletes(
      ExtendedAresPattern template, BastBlock block,
      final Map<AbstractBastNode, NodeParentInformationHierarchy> programParents) {
    LinkedList<BastEditOperation> newDeletes = new LinkedList<>();
    FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(AresWildcard.TAG);
    template.originalAst.accept(fnft);
    for (int i = fnft.nodes.size() - 1; i >= 0; i--) {
      for (WildcardInstance instance : template.wildcardInstances) {
        if (fnft.nodes.get(i) == instance.wildcard) {
          if (!WildcardAccessHelper.isExprWildcard(instance.wildcard)) {
            for (int j = instance.headNodes.size() - 1; j >= 0; j--) {
              AbstractBastNode headNode = instance.headNodes.get(j);
              NodeParentInformationHierarchy npi = programParents.get(headNode);
              if (npi != null) {
                if (npi.list.get(0).parent != block) {
                  DeleteOperation delOp = new DeleteOperation(npi.list.get(0).parent, headNode,
                      new NodeIndex(npi.list.get(0).fieldConstant, npi.list.get(0).listId));
                  newDeletes.add(delOp);
                }
              }
            }
          }
        }
      }
    }
    return newDeletes;
  }


  private static void orderEditOperations(ExtendedAresPattern template,
      final Map<AbstractBastNode, NodeParentInformationHierarchy> templateParents,
      Map<AbstractBastNode, NodeParentInformationHierarchy> templateOriginalParents,
      LinkedList<BastEditOperation> editOperations, LinkedList<BastEditOperation> updates,
      LinkedList<BastEditOperation> deletes, ArrayList<BastEditOperation> inserts) {
    outer: for (BastEditOperation editOp : editOperations) {
      boolean skip = false;
      if (editOp.getOldOrInsertedNode().getTag() == BastEmptyStmt.TAG
          || editOp.getNewOrChangedNode().nodeId == BastEmptyStmt.TAG) {
        continue;
      }
      NodeParentInformationHierarchy npi =
          templateOriginalParents.get(editOp.getOldOrInsertedNode());
      skip = skipNodeInChoice(npi);

      npi = templateParents.get(editOp.getNewOrChangedNode());
      boolean inChoice = false;
      if (npi != null) {
        for (int i = 0; i < npi.list.size(); i++) {
          if (npi.list.get(i).parent.getTag() == AresChoiceStmt.TAG) {
            if (editOp.getType() == EditOperationType.ALIGN
                || editOp.getType() == EditOperationType.MOVE) {
              inChoice = true;
              break;
            }
            skip = true;
            break;
          }
        }
      }
      if (skip) {
        continue outer;
      }
      final BastFieldConstants childrenListNumber =
          editOp.getOldOrChangedIndex().childrenListNumber;
      switch (editOp.getType()) {
        case ALIGN: {
          orderAlign(templateOriginalParents, deletes, inserts, editOp, inChoice,
              childrenListNumber);
          break;
        }
        case DELETE: {
          orderDelete(template, templateOriginalParents, deletes, editOp, childrenListNumber);
          break;
        }
        case INSERT:
          inserts.add(editOp);
          break;
        case MOVE: {
          orderMove(template, templateOriginalParents, deletes, inserts, editOp, npi, inChoice,
              childrenListNumber);
          break;
        }
        case UPDATE:
          updates.add(editOp);
          break;
        default:
          break;

      }
    }
  }


  private static void orderMove(ExtendedAresPattern template,
      Map<AbstractBastNode, NodeParentInformationHierarchy> templateOriginalParents,
      LinkedList<BastEditOperation> deletes, ArrayList<BastEditOperation> inserts,
      BastEditOperation editOp, NodeParentInformationHierarchy npi, boolean inChoice,
      final BastFieldConstants childrenListNumber) {
    if (!inChoice) {
      inserts.add(editOp);
    }
    if (operationChangesAresNode(editOp)) {
      return;
    }
    if (childrenListNumber == BastFieldConstants.ARES_BLOCK_IDENTIFIERS) {
      return;
    }
    NodeParentInformationHierarchy npiInner =
        templateOriginalParents.get(editOp.getOldOrInsertedNode());
    boolean skip = false;
    skip = skipWildcard(npi, skip, npiInner);
    if (skip) {
      return;
    }
    if (template.assignmentMap.get(editOp.getOldOrInsertedNode()) == null) {
      if (!(editOp.getOldOrInsertedNode().getTag() == BastBlock.TAG
          && editOp.getUnchangedOrOldParentNode().getTag() == AresBlock.TAG)) {
        return;
      }
    }
    deletes.add(editOp);
  }


  private static void orderDelete(ExtendedAresPattern template,
      Map<AbstractBastNode, NodeParentInformationHierarchy> templateOriginalParents,
      LinkedList<BastEditOperation> deletes, BastEditOperation editOp,
      final BastFieldConstants childrenListNumber) {
    boolean skip = false;
    if (operationChangesAresNode(editOp)) {
      return;
    }
    NodeParentInformationHierarchy npiInner =
        templateOriginalParents.get(editOp.getOldOrInsertedNode());
    skip = skipWildcard(npiInner, skip, npiInner);
    if (skip) {
      return;
    }
    if (childrenListNumber == BastFieldConstants.ARES_BLOCK_IDENTIFIERS) {
      return;
    }
    if (template.assignmentMap.get(editOp.getOldOrInsertedNode()) == null) {
      if (!(editOp.getOldOrInsertedNode().getTag() == BastBlock.TAG
          && editOp.getUnchangedOrOldParentNode().getTag() == AresBlock.TAG)) {
        return;
      }
    }
    deletes.add(editOp);
  }


  private static void orderAlign(
      Map<AbstractBastNode, NodeParentInformationHierarchy> templateOriginalParents,
      LinkedList<BastEditOperation> deletes, ArrayList<BastEditOperation> inserts,
      BastEditOperation editOp, boolean inChoice, final BastFieldConstants childrenListNumber) {
    if (!inChoice) {
      inserts.add(editOp);
    }
    if (operationChangesAresNode(editOp)) {
      return;
    }
    if (childrenListNumber == BastFieldConstants.ARES_BLOCK_IDENTIFIERS) {
      return;
    }
    boolean skip = false;

    NodeParentInformationHierarchy npiInner =
        templateOriginalParents.get(editOp.getOldOrInsertedNode());
    skip = skipWildcard(npiInner, skip, npiInner);
    if (skip) {
      return;
    }
    deletes.add(editOp);
  }


  private static boolean skipNodeInChoice(NodeParentInformationHierarchy npi) {
    boolean skip = false;
    if (npi != null) {
      for (int i = 0; i < npi.list.size(); i++) {
        if (npi.list.get(i).parent.getTag() == AresChoiceStmt.TAG) {
          skip = true;
          break;
        }
      }
    }
    return skip;
  }


  private static boolean skipWildcard(NodeParentInformationHierarchy npi, boolean skip,
      NodeParentInformationHierarchy npiInner) {
    if (npi != null) {
      for (int i = 0; i < npiInner.list.size(); i++) {
        if (npiInner.list.get(i).parent.getTag() == AresWildcard.TAG) {
          skip = true;
          break;
        }
      }
    }
    return skip;
  }


  private static boolean operationChangesAresNode(BastEditOperation editOp) {
    return editOp.getOldOrInsertedNode().getTag() == AresWildcard.TAG
        || editOp.getOldOrInsertedNode().getTag() == AresChoiceStmt.TAG
        || editOp.getOldOrInsertedNode().getTag() == AresBlock.TAG;
  }

  private static void applyWhiteSpaceToMatchBlock(LinkedList<AbstractBastNode> foundPatternList,
      AbstractBastNode program, ExtendedAresPattern template, BastBlock block) {
    final Map<AbstractBastNode, NodeParentInformationHierarchy> newProgramParents =
        ParentHierarchyHandler.getParentHierarchy(program);
    if (foundPatternList.size() > 0) {
      String tmp = extractIndentationFromList(foundPatternList);
      if (tmp != null) {
        NodeParentInformationHierarchy npi = newProgramParents.get(template.patternStart);
        String diff = extractWhitespaceDifference(tmp, npi);
        printAst(block);
        if (diff != null) {
          ApplyWhitespaceVisitor awv = new ApplyWhitespaceVisitor(tmp, diff, -1);
          block.accept(awv);
        }
        printAst(block);
      }

    }
  }

  private static String extractIndentationFromList(
      LinkedList<? extends AbstractBastNode> foundPatternList) {
    HashMap<String, Integer> indentationMap = new HashMap<>();
    for (int i = 0; i < foundPatternList.size(); i++) {
      JavaToken token = CreateJavaNodeHelper.findLeftJavaToken(foundPatternList.get(i));
      String tmp = token.whiteSpace.toString();
      Integer count = indentationMap.get(tmp);
      if (count == null) {
        indentationMap.put(tmp, 1);
      } else {
        indentationMap.put(tmp, count + 1);
      }
    }
    int max = Integer.MIN_VALUE;
    String tmp = null;
    for (Entry<String, Integer> entry : indentationMap.entrySet()) {
      if (entry.getValue() > max) {
        max = entry.getValue();
        tmp = entry.getKey();
      }
    }
    return tmp;
  }

  private static String extractWhitespaceDifference(String tmp,
      NodeParentInformationHierarchy npi) {
    AbstractBastNode parentIdentNode = null;
    String diff = null;
    if (npi != null) {
      for (int i = 1; i < npi.list.size(); i++) {
        if (npi.list.get(i).parent.getTag() == BastBlock.TAG) {
          parentIdentNode = npi.list.get(i - 1).parent;
          break;
        }
      }
    }
    if (parentIdentNode != null) {
      JavaToken parentToken = CreateJavaNodeHelper.findLeftJavaToken(parentIdentNode);
      if (parentToken != null) {
        String parentWhitespace = parentToken.whiteSpace.toString();
        String[] parentParts = parentWhitespace.split("\n");
        String[] identationParts = tmp.split("\n");
        String parentLast = parentParts[parentParts.length - 1];
        String identationLast = identationParts[identationParts.length - 1];
        diff = identationLast.replaceFirst(parentLast, "");
        return diff;
      }
    }
    return null;
  }

  private static HashMap<String, String> createRenameMap(ExtendedAresPattern template,
      LinkedList<BastEditOperation> updates, BastBlock block) {
    HashMap<String, LinkedList<String>> renameMap = new HashMap<>();
    LinkedList<String> renamesToIgnore = new LinkedList<>();
    LinkedList<String> oldNodesToIgnore = new LinkedList<>();

    if (template.originalAst.identifiers != null) {
      for (AbstractBastNode name : template.originalAst.identifiers) {
        if (name.getTag() == BastNameIdent.TAG) {
          renamesToIgnore.add(((BastNameIdent) name).name);
        }
      }
    }
    FindNodesFromTagVisitor findNames = new FindNodesFromTagVisitor(BastNameIdent.TAG);
    block.accept(findNames);
    for (AbstractBastNode node : findNames.nodes) {
      oldNodesToIgnore.add(((BastNameIdent) node).name);
    }
    outer: for (BastEditOperation update : updates) {
      if (update.getOldOrInsertedNode().getTag() == BastNameIdent.TAG
          && update.getNewOrChangedNode().getTag() == BastNameIdent.TAG) {
        BastNameIdent oldIdent = (BastNameIdent) update.getOldOrInsertedNode();
        BastNameIdent newIdent = (BastNameIdent) update.getNewOrChangedNode();
        if (oldIdent.name.equals(newIdent.name)) {
          continue;
        }
        for (String name : oldNodesToIgnore) {
          if (oldIdent.name.equals(name)) {
            continue outer;
          }
        }
        for (String name : renamesToIgnore) {
          if (oldIdent.name.equals(name) || newIdent.name.equals(name)) {
            continue outer;
          }
        }
        LinkedList<String> tmp = renameMap.get(oldIdent.name);
        if (tmp == null) {
          tmp = new LinkedList<>();
          renameMap.put(oldIdent.name, tmp);
        }
        tmp.add(newIdent.name);
      }
    }
    HashMap<String, String> nameReplaceMap = new HashMap<>();
    for (Entry<String, LinkedList<String>> entry : renameMap.entrySet()) {
      HashMap<String, Integer> countMap = new HashMap<>(entry.getValue().size());
      for (String replace : entry.getValue()) {
        Integer value = countMap.get(replace);
        if (value == null) {
          countMap.put(replace, 1);
        } else {
          countMap.put(replace, value + 1);
        }
      }
      boolean duplicate = false;
      int max = Integer.MIN_VALUE;
      String partner = null;
      for (Entry<String, Integer> innerEntry : countMap.entrySet()) {
        if (innerEntry.getValue() > max) {
          duplicate = false;
          max = innerEntry.getValue();
          partner = innerEntry.getKey();
        } else if (innerEntry.getValue() == max) {
          duplicate = true;
        }
      }
      if (!duplicate && partner != null) {
        nameReplaceMap.put(entry.getKey(), partner);
      }
    }
    return nameReplaceMap;
  }

  private static LinkedList<Recommendation> generateRecommendations(AbstractBastNode program,
      FindParentMethodVisitor fpmv, int methodNumber) {
    LinkedList<Recommendation> results = new LinkedList<>();

    Recommendation result = new Recommendation(program, fpmv.funct, methodNumber);
    results.add(result);
    boolean changed = true;
    while (changed) {
      changed = false;
      LinkedList<Recommendation> toAdd = new LinkedList<>();
      LinkedList<Recommendation> toRemove = new LinkedList<>();
      for (Recommendation rec : results) {
        FindNodesFromTagVisitor fnftChoice = new FindNodesFromTagVisitor(AresChoiceStmt.TAG);
        rec.resultAst.accept(fnftChoice);
        if (fnftChoice.nodes.size() > 0) {
          toRemove.add(rec);
          changed = true;
          int count = ((AresChoiceStmt) fnftChoice.nodes.get(0)).choiceBlock.statements.size() + 1;
          for (int i = 0; i < count; i++) {
            IPrettyPrinter clonePrinter = ParserFactory.getAresPrettyPrinter();
            rec.resultAst.accept(clonePrinter);
            BastProgram clone = ParserFactory.getParserInstance(AresExtension.WITH_ARES_EXTENSIONS)
                .parse(clonePrinter.getBuffer().toString().getBytes());
            assert (clone != null);
            FindNodesFromTagVisitor fnftFunctions = new FindNodesFromTagVisitor(BastFunction.TAG);
            clone.accept(fnftFunctions);
            AbstractBastNode function = null;
            for (AbstractBastNode funct : fnftFunctions.nodes) {
              if (WildcardAccessHelper.isEqual(rec.method, funct)) {
                function = funct;
                break;
              }
            }
            assert (function != null);
            FindNodesFromTagVisitor fnftInnerChoice =
                new FindNodesFromTagVisitor(AresChoiceStmt.TAG);
            clone.accept(fnftInnerChoice);
            final Map<AbstractBastNode, NodeParentInformationHierarchy> cloneParents =
                ParentHierarchyHandler.getParentHierarchy(clone);
            AresChoiceStmt choice = (AresChoiceStmt) fnftInnerChoice.nodes.get(0);
            NodeParentInformationHierarchy npi = cloneParents.get(choice);
            if (npi.list.size() > 0) {
              if (npi.list.get(0).fieldConstant.isList) {
                LinkedList<? extends AbstractBastNode> nodes = getFirstListField(npi);
                LinkedList<AbstractBastNode> newNodes = new LinkedList<>(nodes);
                newNodes.remove(choice);
                if (i != 0) {
                  newNodes.addAll(npi.list.get(0).listId,
                      ((AresCaseStmt) choice.choiceBlock.statements.get(i - 1)).block.statements);
                }
                getFirstParent(npi).replaceField(npi.list.get(0).fieldConstant,
                    new BastField(newNodes));
                toAdd.add(new Recommendation(clone, function, methodNumber));
              } else {
                assert (false);
              }
            }
          }
        }
      }
      results.removeAll(toRemove);
      results.addAll(toAdd);
    }
    return results;
  }

  private static void printAst(AbstractBastNode node) {
    try {
      if (DEBUG_APPLICATION) {
        IPrettyPrinter debugprinter = ParserFactory.getAresPrettyPrinter();
        node.accept(debugprinter);
        System.err.println("############################################");
        System.err.println(debugprinter.getBuffer().toString());
        System.err.println("############################################");
      }
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
  }

  private static BastBlock handleUses(ExtendedAresPattern template, BastBlock block,
      AbstractBastNode shadowBlock, HashMap<String, String> replacements) {
    FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(AresUseStmt.TAG);
    block.accept(fnft);
    ReplaceNameVisitor rnv = new ReplaceNameVisitor(replacements);

    for (AbstractBastNode node : fnft.nodes) {
      for (WildcardInstance instance : template.wildcardInstances) {
        final Map<AbstractBastNode, NodeParentInformationHierarchy> newHierarchy =
            ParentHierarchyHandler.getParentHierarchy(block);
        NodeParentInformationHierarchy npi = newHierarchy.get(node);
        if (npi != null) {

          if (WildcardAccessHelper.isNameEqual(instance.wildcard, node)) {
            if (WildcardAccessHelper.isExprWildcard(node)) {
              block = handlExprUse(template, block, shadowBlock, node, instance, newHierarchy, npi);
            } else {
              block = handleStmtUse(template, block, shadowBlock, rnv, node, instance, newHierarchy,
                  npi);
            }
          }
        }
      }
    }
    return block;
  }


  private static BastBlock handleStmtUse(ExtendedAresPattern template, BastBlock block,
      AbstractBastNode shadowBlock, ReplaceNameVisitor rnv, AbstractBastNode node,
      WildcardInstance instance,
      final Map<AbstractBastNode, NodeParentInformationHierarchy> newHierarchy,
      NodeParentInformationHierarchy npi) {
    LinkedList<? extends AbstractBastNode> fieldNodes = getFirstListField(npi);
    int pos = -1;
    ApplyWhitespaceVisitor awv = null;
    if (fieldNodes != null) {
      for (int i = 0; i < fieldNodes.size(); i++) {
        if (fieldNodes.get(i) == node) {
          pos = i;
          break;
        }
      }
      String indentation = extractIndentationFromList(fieldNodes);

      if (indentation != null) {
        NodeParentInformationHierarchy whiteSpaceNpi = newHierarchy.get(npi.list.get(0).parent);
        String diff = extractWhitespaceDifference(indentation, whiteSpaceNpi);
        if (diff != null && diff.length() > 0) {
          JavaToken wildcardToken = CreateJavaNodeHelper.findLeftJavaToken(instance.wildcard);
          JavaToken useToken = CreateJavaNodeHelper.findLeftJavaToken(node);
          if (!wildcardToken.whiteSpace.toString().equals(useToken.whiteSpace.toString())) {
            awv = new ApplyWhitespaceVisitor(indentation, diff, 0);
          }

        }
      }
      DeleteOperation delOp = new DeleteOperation(getFirstParent(npi), node,
          new NodeIndex(npi.list.get(0).fieldConstant, pos));
      block = applyDelete(template, delOp, block, newHierarchy);
      printAst(block);
    }
    for (int i = 0; i < instance.headNodes.size(); i++) {
      instance.headNodes.get(i).accept(rnv);
      if (awv != null) {
        instance.headNodes.get(i).accept(awv);
      }
      if (getFirstParent(npi).getTag() == BastBlock.TAG
          && instance.headNodes.get(i).getTag() == BastBlock.TAG) {
        @SuppressWarnings("unchecked")
        LinkedList<AbstractBastNode> tmpStmts = (LinkedList<AbstractBastNode>) instance.headNodes
            .get(i).getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
        for (int j = 0; j < tmpStmts.size(); j++) {
          InsertOperation insOp = new InsertOperation(getFirstParent(npi), tmpStmts.get(j),
              new NodeIndex(npi.list.get(0).fieldConstant, npi.list.get(0).listId + i + j));
          block = applyInsert(template, insOp, block, shadowBlock, tmpStmts.get(j),
              getFirstParent(npi), getFirstParent(npi), npi.list.get(0).fieldConstant, npi, false);
          printAst(block);
        }
      } else {
        InsertOperation insOp = new InsertOperation(getFirstParent(npi), instance.headNodes.get(i),
            new NodeIndex(npi.list.get(0).fieldConstant, npi.list.get(0).listId + i));
        block = applyInsert(template, insOp, block, shadowBlock, instance.headNodes.get(i),
            getFirstParent(npi), getFirstParent(npi), npi.list.get(0).fieldConstant, npi, false);
        printAst(block);
      }
    }
    return block;
  }


  private static BastBlock handlExprUse(ExtendedAresPattern template, BastBlock block,
      AbstractBastNode shadowBlock, AbstractBastNode node, WildcardInstance instance,
      final Map<AbstractBastNode, NodeParentInformationHierarchy> newHierarchy,
      NodeParentInformationHierarchy npi) {
    if (instance.headNodes.size() == 1) {
      block =
          replaceSingleExprNode(template, block, shadowBlock, node, instance, newHierarchy, npi);
    } else if (instance.headNodes.size() == 0) {
      DeleteOperation delOp = new DeleteOperation(getFirstParent(npi), node,
          new NodeIndex(npi.list.get(0).fieldConstant, npi.list.get(0).listId));
      block = applyDelete(template, delOp, block, newHierarchy);
    } else {
      @SuppressWarnings("unchecked")
      LinkedList<AbstractBastNode> parentNodes =
          (LinkedList<AbstractBastNode>) getFirstListField(npi);
      int pos = -1;
      for (int i = 0; i < parentNodes.size(); i++) {
        if (parentNodes.get(i) == node) {
          pos = i;
          break;
        }
      }
      for (int i = pos + 1; i < parentNodes.size(); i++) {
        if (parentNodes.get(i).getTag() != AresUseStmt.TAG) {
          pos = i;
          break;
        }
      }
      AbstractBastNode pattern = WildcardAccessHelper.getExpr(node);
      AbstractBastNode associatedStatement = parentNodes.get(pos);
      FindNodesFromTagVisitor findOccurence = new FindNodesFromTagVisitor(pattern.getTag());
      associatedStatement.accept(findOccurence);
      LinkedList<AbstractBastNode> partner = new LinkedList<>();
      for (AbstractBastNode tmpOccurence : findOccurence.nodes) {
        if (WildcardAccessHelper.isEqual(tmpOccurence, pattern)) {
          partner.add(tmpOccurence);
        }
      }
      long occurence = WildcardAccessHelper.getOccurence(node).value - 1;
      if (occurence < partner.size()) {
        AbstractBastNode foundNode = partner.get((int) occurence);
        NodeParentInformationHierarchy npiFound = newHierarchy.get(foundNode);
        NodeParentInformationHierarchy npiDel = newHierarchy.get(instance.headNodes.get(0));
        assert (npiDel != null);
        for (int i = 0; i < instance.headNodes.size(); i++) {
          @SuppressWarnings("unchecked")
          LinkedList<AbstractBastNode> items =
              (LinkedList<AbstractBastNode>) getFirstParent(npiFound)
                  .getField(npiFound.list.get(0).fieldConstant).getListField();
          if (items != null
              && items.size() > npiFound.list.get(0).listId - (instance.headNodes.size() - 1) + i
              && npiFound.list.get(0).listId - (instance.headNodes.size() - 1) + i >= 0) {
            if (WildcardAccessHelper.isEqual(instance.headNodes.get(i),
                items.get(npiFound.list.get(0).listId - (instance.headNodes.size() - 1) + i))) {
              continue;
            }
          }
          InsertOperation insOp = new InsertOperation(getFirstParent(npiFound),
              instance.headNodes.get(i),
              new NodeIndex(npiFound.list.get(0).fieldConstant, npiFound.list.get(0).listId + i));
          block = applyInsert(template, insOp, block, shadowBlock, instance.headNodes.get(i),
              getFirstParent(npiFound), getFirstParent(npiFound),
              npiFound.list.get(0).fieldConstant, npiFound, false);
        }
      }
      DeleteOperation delOp = new DeleteOperation(getFirstParent(npi), node,
          new NodeIndex(npi.list.get(0).fieldConstant, npi.list.get(0).listId));
      block = applyDelete(template, delOp, block, newHierarchy);
    }
    return block;
  }


  private static BastBlock replaceSingleExprNode(ExtendedAresPattern template, BastBlock block,
      AbstractBastNode shadowBlock, AbstractBastNode node, WildcardInstance instance,
      final Map<AbstractBastNode, NodeParentInformationHierarchy> newHierarchy,
      NodeParentInformationHierarchy npi) {
    @SuppressWarnings("unchecked")
    LinkedList<AbstractBastNode> parentNodes =
        (LinkedList<AbstractBastNode>) getFirstListField(npi);
    int pos = -1;
    for (int i = 0; i < parentNodes.size(); i++) {
      if (parentNodes.get(i) == node) {
        pos = i;
        break;
      }
    }
    for (int i = pos + 1; i < parentNodes.size(); i++) {
      if (parentNodes.get(i).getTag() != AresUseStmt.TAG) {
        pos = i;
        break;
      }
    }
    AbstractBastNode pattern = WildcardAccessHelper.getExpr(node);
    AbstractBastNode associatedStatement = parentNodes.get(pos);
    FindNodesFromTagVisitor findOccurence = new FindNodesFromTagVisitor(pattern.getTag());
    associatedStatement.accept(findOccurence);
    LinkedList<AbstractBastNode> partner = new LinkedList<>();
    for (AbstractBastNode tmpOccurence : findOccurence.nodes) {
      if (WildcardAccessHelper.isEqual(tmpOccurence, pattern)) {
        partner.add(tmpOccurence);
      }
    }
    long occurence = WildcardAccessHelper.getOccurence(node).value - 1;
    if (occurence < partner.size()) {
      AbstractBastNode foundNode = partner.get((int) occurence);
      NodeParentInformationHierarchy npiFound = newHierarchy.get(foundNode);
      NodeParentInformationHierarchy npiDel = newHierarchy.get(instance.headNodes.get(0));
      assert (npiDel != null);
      InsertOperation insOp =
          new InsertOperation(getFirstParent(npiFound), instance.headNodes.get(0),
              new NodeIndex(npiFound.list.get(0).fieldConstant, npiFound.list.get(0).listId));
      block = applyInsert(template, insOp, block, shadowBlock, instance.headNodes.get(0),
          getFirstParent(npiFound), getFirstParent(npiFound), npiFound.list.get(0).fieldConstant,
          npiFound, false);
    }
    DeleteOperation delOp = new DeleteOperation(getFirstParent(npi), node,
        new NodeIndex(npi.list.get(0).fieldConstant, npi.list.get(0).listId));
    block = applyDelete(template, delOp, block, newHierarchy);
    return block;
  }

  private static void applyUpdate(ExtendedAresPattern template, IPrettyPrinter printer,
      LinkedList<BastEditOperation> updates) {
    for (BastEditOperation editOp : updates) {
      UpdateOperation uop = (UpdateOperation) editOp;
      AbstractBastNode oldNode = (AbstractBastNode) uop.getOldOrInsertedNode();
      AbstractBastNode oldFoundNode = template.assignmentMap.get(oldNode);
      int oldTag = -1;
      if (oldFoundNode != null) {
        oldTag = oldFoundNode.getTag();
      } else {
        oldTag = editOp.getNewOrChangedNode().getTag();
        oldFoundNode = oldNode;
      }
      switch (oldTag) {
        case BastNameIdent.TAG:
          applyUpdateToIdentifier(uop, oldFoundNode);
          break;
        case BastCmp.TAG:
          applyUpdateToCompare(uop, oldFoundNode);
          break;
        case BastStringConst.TAG:
          applyUpdateToStringLiteral(uop, oldFoundNode);
          break;
        case BastIntConst.TAG:
          applyUpdateToIntLiteral(uop, oldFoundNode);
          break;
        case BastRealConst.TAG:
          applyUpdateToRealLiteral(uop, oldFoundNode);
          break;
        case BastBoolConst.TAG:
          applyUpdateToBool(uop, oldFoundNode);
          break;
        case BastListInitializer.TAG:
          applyUpdateToListInitializer(uop, oldFoundNode);
          break;
        case BastUnaryExpr.TAG:
          applyUpdateToUnaryExpr(uop, oldFoundNode);
          break;
        case BastBasicType.TAG:
          applyUpdateToBasicType(uop, oldFoundNode);
          break;
        case BastAsgnExpr.TAG:
          applyUpdateToAsgnExpr(uop, oldFoundNode);
          break;
        case BastForStmt.TAG:
          applyUpdateToFor(uop, oldFoundNode);
          break;
        default:
          assert (false);
      }
    }
  }

  private static void applyUpdateToFor(UpdateOperation uop, AbstractBastNode oldFoundNode) {
    // TODO
    assert (false);
  }

  private static void applyUpdateToAsgnExpr(UpdateOperation uop, AbstractBastNode oldFoundNode) {
    BasicJavaToken javaToken;
    TokenAndHistory oldTokenCmp;
    ((BastAsgnExpr) oldFoundNode).operation = ((BastAsgnExpr) uop.getNewOrChangedNode()).operation;
    oldTokenCmp = ((BastAsgnExpr) oldFoundNode).info.tokens[0];
    javaToken = null;
    switch (((BastAsgnExpr) oldFoundNode).operation) {
      case BastAsgnExpr.EXPR_TYPE_EQUAL:
        javaToken = BasicJavaToken.EQUAL;
        break;
      case BastAsgnExpr.EXPR_TYPE_MULTIPLY_EQUAL:
        javaToken = BasicJavaToken.MULTIPLY_EQUAL;
        break;
      case BastAsgnExpr.EXPR_TYPE_DIVIDE_EQUAL:
        javaToken = BasicJavaToken.DIVIDE_EQUAL;
        break;
      case BastAsgnExpr.EXPR_TYPE_REMAINDER_EQUAL:
        javaToken = BasicJavaToken.REMAINDER_EQUAL;
        break;
      case BastAsgnExpr.EXPR_TYPE_PLUS_EQUAL:
        javaToken = BasicJavaToken.PLUS_EQUAL;
        break;
      case BastAsgnExpr.EXPR_TYPE_MINUS_EQUAL:
        javaToken = BasicJavaToken.MINUS_EQUAL;
        break;
      case BastAsgnExpr.EXPR_TYPE_SLL_EQUAL:
        javaToken = BasicJavaToken.SLL_EQUAL;
        break;
      case BastAsgnExpr.EXPR_TYPE_SLR_EQUAL:
        javaToken = BasicJavaToken.SLR_EQUAL;
        break;
      case BastAsgnExpr.EXPR_TYPE_AND_EQUAL:
        javaToken = BasicJavaToken.AND_EQUAL;
        break;
      case BastAsgnExpr.EXPR_TYPE_XOR_EQUAL:
        javaToken = BasicJavaToken.XOR_EQUAL;
        break;
      case BastAsgnExpr.EXPR_TYPE_OR_EQUAL:
        javaToken = BasicJavaToken.OR_EQUAL;
        break;
      case BastAsgnExpr.EXPR_TYPE_SAR_EQUAL:
        javaToken = BasicJavaToken.SAR_EQUAL;
        break;
      default:
        assert (false);
        return;
    }
    ((BastAsgnExpr) oldFoundNode).info.tokens[0] = CreateJavaNodeHelper.createTokenHistory(
        ((JavaToken) ((BastAsgnExpr) oldFoundNode).info.tokens[0].token).whiteSpace.toString(),
        javaToken);
    ((BastAsgnExpr) oldFoundNode).info.tokens[0].prevTokens.addAll(oldTokenCmp.prevTokens);
  }


  private static void applyUpdateToBasicType(UpdateOperation uop, AbstractBastNode oldFoundNode) {
    BasicJavaToken javaToken;
    TokenAndHistory oldTokenCmp;
    javaToken = null;
    ((BastBasicType) oldFoundNode).typeTag = ((BastBasicType) uop.getNewOrChangedNode()).typeTag;
    oldTokenCmp = ((BastBasicType) oldFoundNode).info.tokens[0];
    switch (((BastBasicType) oldFoundNode).typeTag) {

      case TagConstants.TYPE_BYTE:
        javaToken = BasicJavaToken.BYTE;
        break;
      case TagConstants.TYPE_SHORT:
        javaToken = BasicJavaToken.SHORT;
        break;
      case TagConstants.TYPE_CHAR:
        javaToken = BasicJavaToken.CHAR;
        break;
      case TagConstants.TYPE_INT:
        javaToken = BasicJavaToken.INT;
        break;
      case TagConstants.TYPE_LONG:
        javaToken = BasicJavaToken.LONG;
        break;
      case TagConstants.TYPE_FLOAT:
        javaToken = BasicJavaToken.FLOAT;
        break;
      case TagConstants.TYPE_DOUBLE:
        javaToken = BasicJavaToken.DOUBLE;
        break;
      case TagConstants.TYPE_BOOL:
        javaToken = BasicJavaToken.BOOLEAN;
        break;
      default:
        assert (false);
        return;
    }
    ((BastBasicType) oldFoundNode).info.tokens[0] = CreateJavaNodeHelper.createTokenHistory(
        ((JavaToken) ((BastBasicType) oldFoundNode).info.tokens[0].token).whiteSpace.toString(),
        javaToken);
    ((BastBasicType) oldFoundNode).info.tokens[0].prevTokens.addAll(oldTokenCmp.prevTokens);
  }


  private static void applyUpdateToUnaryExpr(UpdateOperation uop, AbstractBastNode oldFoundNode) {
    BasicJavaToken javaToken;
    TokenAndHistory oldTokenCmp;
    javaToken = null;
    ((BastUnaryExpr) oldFoundNode).type = ((BastUnaryExpr) uop.getNewOrChangedNode()).type;
    oldTokenCmp = ((BastUnaryExpr) oldFoundNode).info.tokens[0];
    switch (((BastUnaryExpr) oldFoundNode).type) {
      case BastUnaryExpr.TYPE_INCR:
        javaToken = BasicJavaToken.PLUS_PLUS;
        break;
      case BastUnaryExpr.TYPE_DECR:
        javaToken = BasicJavaToken.MINUS_MINUS;
        break;
      case BastUnaryExpr.TYPE_NOT:
        javaToken = BasicJavaToken.NOT;
        break;
      case BastUnaryExpr.TYPE_INVERSE:
        javaToken = BasicJavaToken.TWIDDLE;
        break;
      case BastUnaryExpr.TYPE_PLUS:
        javaToken = BasicJavaToken.PLUS;
        break;
      case BastUnaryExpr.TYPE_NEG:
        javaToken = BasicJavaToken.MINUS;
        break;
      default:
        assert (false);
        return;
    }
    ((BastUnaryExpr) oldFoundNode).info.tokens[0] = CreateJavaNodeHelper.createTokenHistory(
        ((JavaToken) ((BastUnaryExpr) oldFoundNode).info.tokens[0].token).whiteSpace.toString(),
        javaToken);
    ((BastUnaryExpr) oldFoundNode).info.tokens[0].prevTokens.addAll(oldTokenCmp.prevTokens);
  }


  private static void applyUpdateToListInitializer(UpdateOperation uop,
      AbstractBastNode oldFoundNode) {
    ((BastListInitializer) oldFoundNode).open =
        ((BastListInitializer) uop.getNewOrChangedNode()).open;
    if (((BastListInitializer) oldFoundNode).open) {
      oldFoundNode.info.tokens[2] = CreateJavaNodeHelper.createTokenHistory(BasicJavaToken.COMMA);
    } else {
      oldFoundNode.info.tokens[2] = null;
    }
  }


  private static void applyUpdateToBool(UpdateOperation uop, AbstractBastNode oldFoundNode) {
    ((JavaToken) oldFoundNode.info.tokens[0].token).data.setLength(0);
    ((JavaToken) oldFoundNode.info.tokens[0].token).data
        .append(((JavaToken) uop.getNewOrChangedNode().info.tokens[0].token).data.toString());
    ((BastBoolConst) oldFoundNode).value = ((BastBoolConst) uop.getNewOrChangedNode()).value;
  }


  private static void applyUpdateToRealLiteral(UpdateOperation uop, AbstractBastNode oldFoundNode) {
    ((JavaToken) oldFoundNode.info.tokens[0].token).data.setLength(0);
    ((JavaToken) oldFoundNode.info.tokens[0].token).data
        .append(((JavaToken) uop.getNewOrChangedNode().info.tokens[0].token).data.toString());
    ((BastRealConst) oldFoundNode).value = ((BastRealConst) uop.getNewOrChangedNode()).value;
  }


  private static void applyUpdateToIntLiteral(UpdateOperation uop, AbstractBastNode oldFoundNode) {
    ((JavaToken) oldFoundNode.info.tokens[0].token).data.setLength(0);
    ((JavaToken) oldFoundNode.info.tokens[0].token).data
        .append(((JavaToken) uop.getNewOrChangedNode().info.tokens[0].token).data.toString());
    ((BastIntConst) oldFoundNode).value = ((BastIntConst) uop.getNewOrChangedNode()).value;
  }


  private static void applyUpdateToStringLiteral(UpdateOperation uop,
      AbstractBastNode oldFoundNode) {
    ((JavaToken) oldFoundNode.info.tokens[0].token).data.setLength(0);
    ((JavaToken) oldFoundNode.info.tokens[0].token).data
        .append(((JavaToken) uop.getNewOrChangedNode().info.tokens[0].token).data.toString());
    ((BastStringConst) oldFoundNode).value = ((BastStringConst) uop.getNewOrChangedNode()).value;
  }


  private static void applyUpdateToCompare(UpdateOperation uop, AbstractBastNode oldFoundNode) {
    ((BastCmp) oldFoundNode).operation = ((BastCmp) uop.getNewOrChangedNode()).operation;
    TokenAndHistory oldTokenCmp = ((BastCmp) oldFoundNode).info.tokens[0];
    BasicJavaToken javaToken = null;
    switch (((BastCmp) oldFoundNode).operation) {
      case BastCmp.EQUAL:
        javaToken = BasicJavaToken.EQUAL_EQUAL;
        break;
      case BastCmp.NOT_EQUAL:
        javaToken = BasicJavaToken.NOT_EQUAL;
        break;
      case BastCmp.LESS:
        javaToken = BasicJavaToken.LESS;
        break;
      case BastCmp.GREATER:
        javaToken = BasicJavaToken.GREATER;
        break;
      case BastCmp.LESS_EQUAL:
        javaToken = BasicJavaToken.LESS_EQUAL;
        break;
      case BastCmp.GREATER_EQUAL:
        javaToken = BasicJavaToken.GREATER_EQUAL;
        break;
      default:
        assert (false);
        return;
    }
    ((BastCmp) oldFoundNode).info.tokens[0] = CreateJavaNodeHelper.createTokenHistory(
        ((JavaToken) ((BastCmp) oldFoundNode).info.tokens[0].token).whiteSpace.toString(),
        javaToken);
    ((BastCmp) oldFoundNode).info.tokens[0].prevTokens.addAll(oldTokenCmp.prevTokens);
  }


  private static void applyUpdateToIdentifier(UpdateOperation uop, AbstractBastNode oldFoundNode) {
    ((BastNameIdent) oldFoundNode).name = ((BastNameIdent) uop.getNewOrChangedNode()).name;
    TokenAndHistory oldToken = ((BastNameIdent) oldFoundNode).info.tokens[0];
    ((BastNameIdent) oldFoundNode).info.tokens[0] =
        new TokenAndHistory(new JavaToken(BasicJavaToken.STRING_LITERAL,
            ((BastNameIdent) uop.getNewOrChangedNode()).name));
    ((JavaToken) ((BastNameIdent) oldFoundNode).info.tokens[0].token).whiteSpace =
        ((JavaToken) oldToken.token).whiteSpace;
    ((BastNameIdent) oldFoundNode).info.tokens[0].prevTokens.addAll(oldToken.prevTokens);
  }

  private static BastBlock applyDeletes(ExtendedAresPattern template,
      LinkedList<BastEditOperation> deletes, BastBlock block,
      Map<AbstractBastNode, NodeParentInformationHierarchy> programParents) {
    for (BastEditOperation editOp : deletes) {
      block = applyDelete(template, editOp, block, programParents);
    }
    return block;
  }

  private static BastBlock applyDelete(ExtendedAresPattern template, BastEditOperation editOp,
      BastBlock block, Map<AbstractBastNode, NodeParentInformationHierarchy> programParents) {
    if (editOp.getUnchangedOrOldParentNode().getTag() == AresBlock.TAG && editOp
        .getOldOrChangedIndex().childrenListNumber == BastFieldConstants.ARES_BLOCK_BLOCK) {
      return block;
    }
    AbstractBastNode foundNode = template.assignmentMap.get(editOp.getOldOrInsertedNode());
    NodeParentInformationHierarchy npi = programParents.get(foundNode);
    if (foundNode == null) {
      foundNode = editOp.getOldOrInsertedNode();
      npi = programParents.get(foundNode);
    }

    if (npi == null) {
      return block;
    }
    NodeParentInformation np = npi.list.getFirst();
    ListToken listToken = null;
    switch (np.fieldConstant) {

      case DIRECT_CALL_ARGUMENTS:
        listToken = ((ListToken) ((BastCall) np.parent).info.tokens[1].token);
        if (listToken.tokenList.size() > 0) {
          listToken.tokenList.remove(listToken.tokenList.size() - 1);
          LinkedList<? extends AbstractBastNode> list =
              np.parent.getField(np.fieldConstant).getListField();
          if (np.listId == 0 && list.size() > np.listId + 1) {
            JavaToken token = CreateJavaNodeHelper.findLeftJavaToken(list.get(np.listId + 1));
            if (token != null) {
              token.whiteSpace.setLength(0);
            }
          }
        }

        break;
      default:
        break;
    }
    if (np.fieldConstant.isList) {
      LinkedList<? extends AbstractBastNode> list =
          np.parent.getField(np.fieldConstant).getListField();
      if (np.listId < list.size()) {
        list.remove(np.listId);
        np.parent.replaceField(np.fieldConstant, new BastField(list));
      }
    } else {
      np.parent.replaceField(np.fieldConstant, new BastField((AbstractBastNode) null));
    }

    return block;
  }

  private static BastBlock applyInserts(ExtendedAresPattern template,
      Map<AbstractBastNode, NodeParentInformationHierarchy> templateParents,
      ArrayList<BastEditOperation> inserts, BastBlock block, AbstractBastNode shadowBlock,
      boolean addUseOffset) {
    HashMap<Integer, HashMap<String, String>> nameMap = createNameMapForInsert(template);
    for (int i = 0; i < inserts.size(); i++) {
      printAst(block);
      BastEditOperation editOp = inserts.get(i);
      AbstractBastNode foundNode = null;
      if (editOp.getType() == EditOperationType.MOVE
          && editOp.getUnchangedOrOldParentNode().getTag() == AresBlock.TAG) {
        foundNode = shadowBlock;
      } else if (editOp.getType() == EditOperationType.INSERT) {
        foundNode = editOp.getNewOrChangedNode();
      } else {
        foundNode = template.assignmentMap.get(editOp.getOldOrInsertedNode());
      }
      NodeParentInformationHierarchy npiTemplate = null;
      npiTemplate = templateParents.get(editOp.getNewOrChangedNode());
      if (npiTemplate == null) {
        continue;
      }
      NodeParentInformation npTemplate = npiTemplate.list.getFirst();
      AbstractBastNode templateParent = npTemplate.parent;
      AbstractBastNode parent = template.getDiffResult().secondToFirstMap.get(templateParent);
      final BastFieldConstants fieldConstant = npTemplate.fieldConstant;
      AbstractBastNode foundParent = null;

      if (parent != null && editOp.getType() == EditOperationType.MOVE
          && editOp.getUnchangedOrOldParentNode().getTag() == AresBlock.TAG) {
        parent = editOp.getUnchangedOrNewParentNode();
      }
      if (parent != null) {
        foundParent = template.assignmentMap.get(parent);
      }
      if (parent == null && editOp.getUnchangedOrNewParentNode() == block) {
        foundParent = block;
      }
      final BastFieldConstants childrenListNumber =
          editOp.getOldOrChangedIndex().childrenListNumber;
      if (childrenListNumber == BastFieldConstants.ARES_BLOCK_IDENTIFIERS
          || childrenListNumber == BastFieldConstants.ARES_PLUGIN_CLAUSE_IDENT
          || childrenListNumber == BastFieldConstants.ARES_PATTERN_CLAUSE_IDENT) {
        foundParent = editOp.getUnchangedOrNewParentNode();
        foundNode = editOp.getNewOrChangedNode();
        if (template.getDiffResult().secondToFirstMap.get(foundParent) != null) {
          AbstractBastNode tmp = template.getDiffResult().secondToFirstMap.get(foundParent);
          if (template.getDiffResult().editMapOld.get(tmp) != null) {
            if (template.getDiffResult().editMapOld.get(tmp).getType() == EditOperationType.MOVE) {
              foundParent = template.assignmentMap.get(tmp);
            }
          } else {
            foundParent = template.assignmentMap.get(tmp);
          }
        }
      }
      final BastFieldConstants newChildrenListNumber =
          editOp.getNewOrChangedIndex().childrenListNumber;
      if (newChildrenListNumber == BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS) {
        if (foundParent == null) {
          foundParent = applySwitchWildcard(template, templateParents, editOp);
        } else {
          if (((BastSwitchCaseGroup) editOp.getUnchangedOrNewParentNode()).labels
              .size() != ((BastSwitchCaseGroup) foundParent).labels.size()) {
            foundParent = applySwitchWildcard(template, templateParents, editOp);
          }
        }
      }
      renameIdentifierInInsert(nameMap, foundNode, npiTemplate);
      block = applyInsert(template, editOp, block, shadowBlock, foundNode, parent, foundParent,
          fieldConstant, npiTemplate, addUseOffset);



    }
    return block;
  }


  private static HashMap<Integer, HashMap<String, String>> createNameMapForInsert(
      ExtendedAresPattern template) {
    HashMap<Integer, HashMap<String, String>> tagMap = new HashMap<>();
    HashMap<Integer, HashMap<String, String>> reverseTagMap = new HashMap<>();
    HashMap<Integer, HashSet<String>> toRemoveMap = new HashMap<>();
    for (Entry<AbstractBastNode, AbstractBastNode> entry : template.assignmentMap.entrySet()) {

      HashMap<String, String> valueMap = tagMap.get(entry.getKey().getTag());
      if (valueMap == null) {
        valueMap = new HashMap<>();
        tagMap.put(entry.getKey().getTag(), valueMap);
      }
      HashMap<String, String> reverseValueMap = reverseTagMap.get(entry.getKey().getTag());
      if (reverseValueMap == null) {
        reverseValueMap = new HashMap<>();
        reverseTagMap.put(entry.getKey().getTag(), reverseValueMap);
      }

      String keyName = AresWrapper.staticGetValue(entry.getKey());
      String valueName = AresWrapper.staticGetValue(entry.getValue());
      if ((keyName != null && !keyName.equals(""))
          || (valueName != null && !valueName.equals(""))) {
        if (!keyName.equals(valueName)) {
          HashSet<String> toRemoveSet = toRemoveMap.get(entry.getKey().getTag());
          if (toRemoveSet == null) {
            toRemoveSet = new HashSet<>();
            toRemoveMap.put(entry.getKey().getTag(), toRemoveSet);
          }
          if (valueMap.containsKey(keyName) && !valueMap.get(keyName).equals(valueName)) {
            toRemoveSet.add(keyName);
            toRemoveSet.add(valueName);
            toRemoveSet.add(valueMap.get(keyName));
          } else if (reverseValueMap.containsKey(valueName)
              && !reverseValueMap.get(valueName).equals(keyName)) {
            toRemoveSet.add(keyName);
            toRemoveSet.add(valueName);
            toRemoveSet.add(reverseValueMap.get(valueName));
          } else {
            valueMap.put(keyName, valueName);
            reverseValueMap.put(valueName, keyName);
          }
        }
      }
    }
    for (Entry<Integer, HashSet<String>> entry : toRemoveMap.entrySet()) {
      final HashMap<String, String> valueMap = tagMap.get(entry.getKey());
      if (valueMap != null) {
        for (String name : entry.getValue()) {
          valueMap.remove(name);
        }
      }
    }
    return tagMap;
  }


  private static void renameIdentifierInInsert(HashMap<Integer, HashMap<String, String>> nameMap,
      AbstractBastNode foundNode, NodeParentInformationHierarchy npiTemplate) {
    if (foundNode != null && npiTemplate != null && foundNode.getTag() != AresChoiceStmt.TAG) {
      boolean insideChoice = false;
      for (NodeParentInformation npi : npiTemplate.list) {
        if (npi.parent.getTag() == AresChoiceStmt.TAG) {
          insideChoice = true;
        }
      }
      if (!insideChoice) {
        ReplaceValueVisitor rnv = new ReplaceValueVisitor(nameMap);
        foundNode.accept(rnv);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static AbstractBastNode applySwitchWildcard(ExtendedAresPattern template,
      Map<AbstractBastNode, NodeParentInformationHierarchy> templateParents,
      BastEditOperation editOp) {
    NodeParentInformationHierarchy npi = templateParents.get(editOp.getNewOrChangedNode());
    if (npi != null && npi.list.size() > 1) {
      AbstractBastNode grandParent = npi.list.get(1).parent;
      AbstractBastNode grandParentPartner =
          template.getDiffResult().secondToFirstMap.get(grandParent);
      if (grandParentPartner != null) {
        AbstractBastNode foundGrandParent = template.assignmentMap.get(grandParentPartner);
        if (foundGrandParent != null) {
          LinkedList<AbstractBastNode> switchCaseGroups =
              (LinkedList<AbstractBastNode>) foundGrandParent
                  .getField(BastFieldConstants.SWITCH_CASE_GROUPS).getListField();
          LinkedList<AbstractBastNode> parentLabels =
              (LinkedList<AbstractBastNode>) editOp.getUnchangedOrNewParentNode()
                  .getField(BastFieldConstants.SWITCH_CASE_GROUP_LABELS).getListField();
          int pos = -1;
          LinkedList<AbstractBastNode> labelsToMove = new LinkedList<>();
          AbstractBastNode groupToSplit = null;
          for (int i = 0; i < switchCaseGroups.size(); i++) {
            AbstractBastNode group = switchCaseGroups.get(i);
            LinkedList<AbstractBastNode> labels = (LinkedList<AbstractBastNode>) group
                .getField(BastFieldConstants.SWITCH_CASE_GROUP_LABELS).getListField();
            if (parentLabels.size() <= labels.size()) {
              boolean found = true;
              for (int j = 0; j < parentLabels.size(); j++) {
                if (!WildcardAccessHelper.isEqual(parentLabels.get(j), labels.get(j))) {
                  found = false;
                  labelsToMove.clear();
                  break;
                } else {
                  labelsToMove.add(labels.get(j));
                }
              }
              if (found) {
                pos = i;
                groupToSplit = group;
                break;
              }
              if (WildcardAccessHelper.isEqual(parentLabels.get(parentLabels.size() - 1),
                  labels.get(labels.size() - 1))) {
                return group;
              }
            }

          }
          if (pos != -1) {
            BastSwitchCaseGroup group = CreateJavaNodeHelper.createBastSwitchCaseGroup();
            group.replaceField(BastFieldConstants.SWITCH_CASE_GROUP_LABELS,
                new BastField(labelsToMove));
            LinkedList<AbstractBastNode> labelsToSplit = (LinkedList<AbstractBastNode>) groupToSplit
                .getField(BastFieldConstants.SWITCH_CASE_GROUP_LABELS).getListField();
            labelsToSplit.removeAll(labelsToMove);
            group.replaceField(BastFieldConstants.SWITCH_CASE_GROUP_LABELS,
                new BastField(labelsToMove));
            switchCaseGroups.add(pos, group);
            foundGrandParent.replaceField(BastFieldConstants.SWITCH_CASE_GROUPS,
                new BastField(switchCaseGroups));
            return group;
          }
        }
      }
    }
    return null;
  }

  private static BastBlock applyInsert(ExtendedAresPattern template, BastEditOperation editOp,
      BastBlock block, AbstractBastNode shadowBlock, AbstractBastNode foundNode,
      AbstractBastNode parent, AbstractBastNode foundParent, BastFieldConstants fieldConstant,
      NodeParentInformationHierarchy npiTemplate, boolean addUseOffset) {
    final BastFieldConstants childrenListNumber = editOp.getNewOrChangedIndex().childrenListNumber;
    if (foundParent != null) {
      block = applyInsertWithFoundParent(template, editOp, block, foundNode, foundParent,
          fieldConstant, npiTemplate, childrenListNumber);
    } else {
      block = applyInsertWithoutFoundParent(template, editOp, block, shadowBlock, foundNode, parent,
          fieldConstant, childrenListNumber);
    }
    return block;
  }


  private static BastBlock applyInsertWithFoundParent(ExtendedAresPattern template,
      BastEditOperation editOp, BastBlock block, AbstractBastNode foundNode,
      AbstractBastNode foundParent, BastFieldConstants fieldConstant,
      NodeParentInformationHierarchy npiTemplate, final BastFieldConstants childrenListNumber) {
    if (foundNode != null && foundNode.info != null && foundNode.info.tokensBefore != null) {
      foundNode.info.tokensBefore.clear();
    }
    if (foundNode != null && foundNode.info != null && foundNode.info.tokensAfter != null) {
      foundNode.info.tokensAfter.clear();
    }
    if (fieldConstant.isList) {
      block = applyInsertWithParentInList(editOp, block, foundNode, foundParent, fieldConstant,
          npiTemplate, childrenListNumber);
    } else {

      applyInsertWithParentWithoutList(template, editOp, foundNode, foundParent, npiTemplate,
          childrenListNumber);
    }
    return block;
  }


  private static void applyInsertWithParentWithoutList(ExtendedAresPattern template,
      BastEditOperation editOp, AbstractBastNode foundNode, AbstractBastNode foundParent,
      NodeParentInformationHierarchy npiTemplate, final BastFieldConstants childrenListNumber) {
    JavaToken token = CreateJavaNodeHelper.findLeftJavaToken(foundParent);
    AbstractBastNode partner = template.getDiffResult().secondToFirstMap.get(foundParent);
    if (partner != null) {
      findJavaTokenForInsert(template, foundNode, childrenListNumber, partner);
    }

    switch (childrenListNumber) {
      case EXPR_INITIALIZER_INIT:
        break;
      case FOR_STMT_STATEMENT:
      case IF_IF_PART:
        applyIfPartOrForInsert(foundNode, foundParent);
        break;
      case IF_ELSE_PART:

        applyElsePartInsert(foundNode, foundParent);
        break;
      case ACCESS_MEMBER:
        token = CreateJavaNodeHelper.findLeftJavaToken(foundNode);
        if (token != null) {
          token.whiteSpace.setLength(0);
        }
        foundNode.info.tokensAfter.clear();
        break;
      case ACCESS_TARGET:
      case IF_CONDITION:
      case INSTANCE_OF_EXPR:
      case CAST_EXPR_TYPE:
        break;
      case ADDITIVE_EXPR_RIGHT:
      case ASGN_EXPR_RIGHT:
      case INSTANCE_OF_TYPE:
      case CAST_EXPR_OPERAND:
      case MULTI_EXPR_RIGHT:
      case CMP_EXPR_RIGHT:
      case IDENT_DECLARATOR_IDENTIFIER:
        applyRightHandInsert(editOp, foundNode, foundParent, childrenListNumber, partner);
        break;
      case CLASS_TYPE_NAME:
      case DIRECT_CALL_FUNCTION:
        BastFieldConstants constant = null;
        if (npiTemplate.list.size() > 1) {
          NodeParentInformation npTmp = npiTemplate.list.get(1);
          constant = npTmp.fieldConstant;
        }
        if (constant != null) {
          switch (constant) {
            case ASGN_EXPR_RIGHT:
            case INSTANCE_OF_TYPE:
            case NEW_CLASS_TYPE:
              token = CreateJavaNodeHelper.findLeftJavaToken(foundNode);
              if (token != null) {
                token.whiteSpace.setLength(0);
                token.whiteSpace.append(" ");
              }
              break;
            default:
              break;
          }
        }
        break;
      case IDENT_DECLARATOR_EXPRESSION:
        if (foundParent.info.tokens == null || foundParent.info.tokens[0] == null) {
          TokenAndHistory[] tokens = new TokenAndHistory[] {
              CreateJavaNodeHelper.createTokenHistory(" ", BasicJavaToken.EQUAL) };
          foundParent.info = new BastInfo(tokens);
        }
        break;
      default:
        break;
    }
    if (foundNode != foundParent) {
      foundParent.replaceField(childrenListNumber, new BastField(foundNode));
      applyLeftWhitespaceInInsert(foundParent, npiTemplate, childrenListNumber);
    }
  }


  private static void applyLeftWhitespaceInInsert(AbstractBastNode foundParent,
      NodeParentInformationHierarchy npiTemplate, final BastFieldConstants childrenListNumber) {
    switch (childrenListNumber) {
      case ADDITIVE_EXPR_LEFT:
        break;
      case DECR_EXPR_OPERAND:
      case INCR_EXPR_OPERAND:
      case DIRECT_CALL_FUNCTION:
      case ASGN_EXPR_LEFT:
      case EXPR_INITIALIZER_INIT:
        if (npiTemplate.list.size() > 1) {
          NodeParentInformation npTmp = npiTemplate.list.get(1);
          if (npTmp.parent.getTag() == BastBlock.TAG) {
            @SuppressWarnings("unchecked")
            LinkedList<AbstractBastNode> list = (LinkedList<AbstractBastNode>) npTmp.parent
                .getField(npTmp.fieldConstant).getListField();
            if (list.size() > 0) {
              applyWhitespace(npiTemplate, (AbstractBastNode) list.getFirst(), foundParent);


            }
          }
        }
        break;
      default:
        break;
    }
  }


  private static void applyElsePartInsert(AbstractBastNode foundNode,
      AbstractBastNode foundParent) {
    JavaToken token;
    foundParent.info.tokens[3] = CreateJavaNodeHelper.createTokenHistory(" ", BasicJavaToken.ELSE);

    if (foundNode.getTag() == BastIf.TAG) {
      foundNode.info.tokens[0].prevTokens.clear();
      token = CreateJavaNodeHelper.findLeftJavaToken(foundNode);
      if (token != null) {

        token.whiteSpace.setLength(0);
        token.whiteSpace.append(" ");
      }
    }
  }


  private static void applyIfPartOrForInsert(AbstractBastNode foundNode,
      AbstractBastNode foundParent) {
    switch (foundNode.getTag()) {
      case BastBlock.TAG:
        JavaToken rbrace = (JavaToken) foundNode.info.tokens[2].token;
        rbrace.whiteSpace.setLength(0);
        String tmpWhitespace = ((JavaToken) foundParent.info.tokens[0].token).whiteSpace.toString();
        String[] parts = tmpWhitespace.split("\n");
        rbrace.whiteSpace.append("\n");
        rbrace.whiteSpace.append(parts[parts.length - 1]);
        break;
      case BastForStmt.TAG:
      case BastWhileStatement.TAG:
        break;
      default:
        foundNode.info.tokensAfter
            .add(CreateJavaNodeHelper.createTokenHistory(BasicJavaToken.SEMICOLON));
    }
  }


  private static void applyRightHandInsert(BastEditOperation editOp, AbstractBastNode foundNode,
      AbstractBastNode foundParent, final BastFieldConstants childrenListNumber,
      AbstractBastNode partner) {
    if (partner == null) {
      AbstractBastNode node = foundParent.getField(childrenListNumber).getField();
      if (node != null && editOp.getType() == EditOperationType.INSERT) {
        JavaToken originalToken = CreateJavaNodeHelper.findLeftJavaToken(node);
        if (originalToken != null && node != foundNode) {
          JavaToken foundToken = CreateJavaNodeHelper.findLeftJavaToken(foundNode);
          foundToken.whiteSpace.setLength(0);
          foundToken.whiteSpace.append(originalToken.whiteSpace);
        }
      } else {
        JavaToken foundToken = CreateJavaNodeHelper.findLeftJavaToken(foundNode);
        if (foundToken != null) {
          JavaToken editToken =
              CreateJavaNodeHelper.findLeftJavaToken(editOp.getNewOrChangedNode());
          if (foundToken != editToken) {
            if (editToken.whiteSpace.toString().length() > 0) {
              foundToken.whiteSpace.setLength(0);
              foundToken.whiteSpace.append(editToken.whiteSpace);
            } else {
              switch (childrenListNumber) {
                case CAST_EXPR_OPERAND:
                  break;
                default:
                  foundToken.whiteSpace.setLength(0);
                  foundToken.whiteSpace.append(" ");
              }
            }
          }
        }
      }
    }
  }


  private static void findJavaTokenForInsert(ExtendedAresPattern template,
      AbstractBastNode foundNode, final BastFieldConstants childrenListNumber,
      AbstractBastNode partner) {
    LinkedList<BastEditOperation> operations = template.parentMap.get(partner);
    if (operations != null) {
      for (BastEditOperation op : operations) {
        if (op.getNewOrChangedIndex().childrenListNumber == childrenListNumber
            && op.getType() != EditOperationType.MOVE) {
          JavaToken oldToken = CreateJavaNodeHelper.findLeftJavaToken(op.getOldOrInsertedNode());
          JavaToken newToken = CreateJavaNodeHelper.findLeftJavaToken(foundNode);
          if (oldToken != null && newToken != null) {
            newToken.whiteSpace.setLength(0);
            newToken.whiteSpace.append(oldToken.whiteSpace);
          }
        } else {
          JavaToken newToken = CreateJavaNodeHelper.findLeftJavaToken(foundNode);
          if (newToken != null) {
            newToken.whiteSpace.setLength(0);
          }
        }
      }
    }
  }


  @SuppressWarnings("unchecked")
  private static BastBlock applyInsertWithParentInList(BastEditOperation editOp, BastBlock block,
      AbstractBastNode foundNode, AbstractBastNode foundParent, BastFieldConstants fieldConstant,
      NodeParentInformationHierarchy npiTemplate, final BastFieldConstants childrenListNumber) {
    BastField field = foundParent.getField(fieldConstant);
    LinkedList<AbstractBastNode> list = null;
    if (field != null) {
      list = (LinkedList<AbstractBastNode>) field.getListField();

    }
    if (list == null) {
      list = new LinkedList<>();
    }
    if (list != null && list.size() > 0) {
      if (list.size() > editOp.getNewOrChangedIndex().childrenListIndex) {
        if (WildcardAccessHelper.isEqual(list.get(editOp.getNewOrChangedIndex().childrenListIndex),
            editOp.getNewOrChangedNode())) {
          return block;
        }
      }
      switch (childrenListNumber) {

        case BLOCK_STATEMENT:
          applyWhitespaceBlockInsert(foundNode, npiTemplate, list);
          break;
        case DIRECT_CALL_ARGUMENTS:
        case NEW_CLASS_PARAMETERS:
        case LIST_INITIALIZER_INIT:
          applyWhitespaceExprListInsert(editOp, block, foundNode, foundParent, list);
          break;
        case PARAMETER_TYPE_LIST_PARAMETERS:
          applyWhitespaceParameterListInsert(editOp, foundNode, foundParent, list);
          break;
        case SWITCH_CASE_GROUP_STATEMENTS:
          applyWhitespaceSwitchInsert(foundNode);
          break;
        default:
          applyWhitespace(npiTemplate, (AbstractBastNode) list.getFirst(), foundNode);
          break;
      }
    } else {

      JavaToken token = CreateJavaNodeHelper.findLeftJavaToken(foundParent);
      if (foundNode.info.tokensBefore != null) {
        foundNode.info.tokensBefore.clear();
      }
      if (foundNode.info.tokensAfter != null) {
        foundNode.info.tokensAfter.clear();
      }

      switch (childrenListNumber) {
        case BLOCK_STATEMENT:
          if (foundNode.info.tokensAfter == null) {
            foundNode.info.tokensAfter = new LinkedList<>();
          }

          foundNode.info.tokensAfter
              .add(CreateJavaNodeHelper.createTokenHistory(BasicJavaToken.SEMICOLON));
          break;
        case DIRECT_CALL_ARGUMENTS:
          if (editOp.getNewOrChangedIndex().childrenListIndex == 0) {
            JavaToken foundNodeToken = CreateJavaNodeHelper.findLeftJavaToken(foundNode);
            if (foundNodeToken != null) {
              foundNodeToken.whiteSpace.setLength(0);
            }
          }
          foundNode.info.tokensAfter.clear();
          break;
        case DECLARATION_MODIFIERS:
          if (token != null) {
            token.whiteSpace.setLength(0);
            token.whiteSpace.append(" ");
          }
          break;
        case SWITCH_CASE_GROUP_STATEMENTS:
          applyWhitespaceSwitchInsert(foundNode);
          break;
        default:
          break;
      }

    }
    int offset = 0;
    if (childrenListNumber == BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS && list.size() == 1
        && list.getFirst().getTag() == BastBlock.TAG) {
      LinkedList<AbstractBastNode> stmts = (LinkedList<AbstractBastNode>) list.getFirst()
          .getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
      stmts.add(editOp.getNewOrChangedIndex().childrenListIndex + offset, foundNode);
      list.getFirst().replaceField(BastFieldConstants.BLOCK_STATEMENT, new BastField(stmts));
    } else {
      if (editOp.getNewOrChangedIndex().childrenListIndex + offset >= list.size()) {
        list.add(foundNode);
      } else {
        list.add(editOp.getNewOrChangedIndex().childrenListIndex + offset, foundNode);
      }
      foundParent.replaceField(childrenListNumber, new BastField(list));
    }
    return block;
  }


  private static void applyWhitespaceBlockInsert(AbstractBastNode foundNode,
      NodeParentInformationHierarchy npiTemplate, LinkedList<AbstractBastNode> list) {
    if (foundNode != null) {
      JavaToken insertToken = CreateJavaNodeHelper.findLeftJavaToken(foundNode);
      String[] parts = null;
      if (insertToken != null) {
        parts = insertToken.whiteSpace.toString().split("\n");
      }
      if (insertToken == null) {
        applyWhitespace(npiTemplate, (AbstractBastNode) list.getFirst(), foundNode);
      } else if (parts.length <= 2) {
        if (insertToken.type != BasicJavaToken.LINE_COMMENT
            && insertToken.type != BasicJavaToken.BLOCK_COMMENT) {
          applyWhitespace(npiTemplate, (AbstractBastNode) list.getFirst(), foundNode);
        }

      }
      if (foundNode.getTag() != BastWhileStatement.TAG) {
        foundNode.info.tokensAfter
            .add(CreateJavaNodeHelper.createTokenHistory(BasicJavaToken.SEMICOLON));
      }
    }
  }


  private static void applyWhitespaceSwitchInsert(AbstractBastNode foundNode) {
    switch (foundNode.getTag()) {
      case BastBlock.TAG:
      case BastForStmt.TAG:
      case BastWhileStatement.TAG:
        break;
      default:
        foundNode.info.tokensAfter
            .add(CreateJavaNodeHelper.createTokenHistory(BasicJavaToken.SEMICOLON));
    }
  }


  private static void applyWhitespaceParameterListInsert(BastEditOperation editOp,
      AbstractBastNode foundNode, AbstractBastNode foundParent, LinkedList<AbstractBastNode> list) {
    ListToken listToken;
    listToken = ((ListToken) ((BastParameterList) foundParent).info.tokens[1].token);
    listToken.tokenList.add(CreateJavaNodeHelper.createTokenHistory(BasicJavaToken.COMMA));
    if (editOp.getNewOrChangedIndex().childrenListIndex < list.size()) {
      JavaToken token = CreateJavaNodeHelper
          .findLeftJavaToken(list.get(editOp.getNewOrChangedIndex().childrenListIndex));
      if (token != null) {
        token.whiteSpace.setLength(0);
        token.whiteSpace.append(" ");
      }
    } else {
      JavaToken token = CreateJavaNodeHelper.findLeftJavaToken(foundNode);
      if (token != null) {
        token.whiteSpace.setLength(0);
        token.whiteSpace.append(" ");
      }
    }
  }


  private static BastBlock applyWhitespaceExprListInsert(BastEditOperation editOp, BastBlock block,
      AbstractBastNode foundNode, AbstractBastNode foundParent, LinkedList<AbstractBastNode> list) {
    ListToken listToken;
    if (foundNode == editOp.getNewOrChangedNode()
        && foundParent == editOp.getUnchangedOrNewParentNode()) {
      if (list.size() > editOp.getNewOrChangedIndex().childrenListIndex) {
        if (WildcardAccessHelper.isEqual(foundNode,
            list.get(editOp.getNewOrChangedIndex().childrenListIndex))) {
          return block;
        }
      }
    }
    listToken = getListToken(foundParent);
    if (listToken != null && listToken.tokenList != null) {
      listToken.tokenList.add(CreateJavaNodeHelper.createTokenHistory(BasicJavaToken.COMMA));
    } else {
      if (listToken != null) {
        listToken.tokenList = new LinkedList<>();
        if (list.size() > 0) {
          listToken.tokenList.add(CreateJavaNodeHelper.createTokenHistory(BasicJavaToken.COMMA));
        }
      }
    }
    if (editOp.getNewOrChangedIndex().childrenListIndex < list.size()) {
      JavaToken token = CreateJavaNodeHelper
          .findLeftJavaToken(list.get(editOp.getNewOrChangedIndex().childrenListIndex));
      if (token != null) {
        token.whiteSpace.setLength(0);
        token.whiteSpace.append(" ");
      }
      if (editOp.getNewOrChangedIndex().childrenListIndex == 0) {
        token = CreateJavaNodeHelper.findLeftJavaToken(foundNode);
        if (token != null) {
          token.whiteSpace.setLength(0);
        }
      }
    } else {
      JavaToken token = CreateJavaNodeHelper.findLeftJavaToken(foundNode);
      if (token != null) {
        token.whiteSpace.setLength(0);
        token.whiteSpace.append(" ");
      }
    }
    if (foundNode != null && foundNode.info != null && foundNode.info.tokensAfter != null) {
      foundNode.info.tokensAfter.clear();
    }
    return block;
  }


  @SuppressWarnings("unchecked")
  private static BastBlock applyInsertWithoutFoundParent(ExtendedAresPattern template,
      BastEditOperation editOp, BastBlock block, AbstractBastNode shadowBlock,
      AbstractBastNode foundNode, AbstractBastNode parent, BastFieldConstants fieldConstant,
      final BastFieldConstants childrenListNumber) {
    if (parent == template.originalAst.block) {
      LinkedList<AbstractBastNode> list = null;
      AbstractBastNode target = null;
      if (shadowBlock == null) {
        target = block;

      } else {
        target = shadowBlock;
      }
      list = (LinkedList<AbstractBastNode>) target.getField(fieldConstant).getListField();
      switch (childrenListNumber) {
        case BLOCK_STATEMENT:
          switch (foundNode.getTag()) {
            case BastCall.TAG:
            case BastAccess.TAG:
            case BastDeclaration.TAG:
            case BastAsgnExpr.TAG:
              if (foundNode.info != null) {
                if (foundNode.info.tokensBefore != null) {
                  foundNode.info.tokensBefore.clear();
                }
                if (foundNode.info.tokensBefore != null) {
                  foundNode.info.tokensAfter.clear();
                }
                if (foundNode.info.tokensAfter == null || foundNode.info.tokensAfter.isEmpty()) {
                  foundNode.info.tokensAfter = new LinkedList<>();
                  foundNode.info.tokensAfter
                      .add(CreateJavaNodeHelper.createTokenHistory(BasicJavaToken.SEMICOLON));
                }
              }

              break;
            default:
              break;
          }

          break;
        default:
          break;
      }
      if (editOp.getNewOrChangedIndex().childrenListIndex > list.size()) {
        return block;
      }
      list.add(editOp.getNewOrChangedIndex().childrenListIndex, foundNode);
      target.replaceField(childrenListNumber, new BastField(list));
    } else if (parent == template.originalAst) {
      if (editOp.getType() == EditOperationType.INSERT) {
        foundNode = editOp.getNewOrChangedNode();
        foundNode.replaceField(BastFieldConstants.BLOCK_STATEMENT,
            new BastField(new LinkedList<>()));
      } else {
        foundNode = template.assignmentMap.get(editOp.getOldOrInsertedNode());
      }
      block = (BastBlock) foundNode;
    }
    return block;
  }

  private static LinkedList<? extends AbstractBastNode> getFirstListField(
      NodeParentInformationHierarchy npi) {
    if (npi == null || npi.list == null || npi.list.size() == 0) {
      return null;
    }
    if (!npi.list.get(0).fieldConstant.isList) {
      return null;
    }
    return getFirstParent(npi).getField(npi.list.get(0).fieldConstant).getListField();
  }

  private static AbstractBastNode getFirstParent(NodeParentInformationHierarchy npi) {
    if (npi == null || npi.list == null || npi.list.size() == 0) {
      return null;
    }
    return npi.list.get(0).parent;
  }

  private static ListToken getListToken(AbstractBastNode foundParent) {
    switch (foundParent.getTag()) {
      case BastCall.TAG:
        return ((ListToken) (foundParent).info.tokens[1].token);
      case BastNew.TAG:
        return ((ListToken) (foundParent).info.tokens[2].token);
      case BastListInitializer.TAG:
        return ((ListToken) (foundParent).info.tokens[1].token);
      default:
        return null;
    }


  }

  /**
   * Apply whitespace right.
   *
   * @param node the node
   * @param indendation the indendation
   * @param diff the diff
   * @param depth the depth
   */
  public static void applyWhitespaceRight(AbstractBastNode node, String indendation, String diff,
      int depth) {
    JavaToken right = findRightJavaToken(node);
    if (right != null) {
      right.whiteSpace.setLength(0);
      right.whiteSpace.append(indendation);
      for (int i = 0; i < depth; i++) {
        right.whiteSpace.append(diff);
      }
    }
  }

  /**
   * Apply whitespace left.
   *
   * @param node the node
   * @param indendation the indendation
   * @param diff the diff
   * @param depth the depth
   */
  public static void applyWhitespaceLeft(AbstractBastNode node, String indendation, String diff,
      int depth) {
    JavaToken token = CreateJavaNodeHelper.findLeftJavaToken(node);
    if (token != null) {
      token.whiteSpace.setLength(0);
      token.whiteSpace.append(indendation);
      for (int i = 0; i < depth; i++) {
        token.whiteSpace.append(diff);
      }
    }
  }

  private static void applyWhitespace(NodeParentInformationHierarchy npiTemplate,
      AbstractBastNode previousNode, AbstractBastNode newNode) {
    StringBuffer bufferTmp = PatternGenerator.extractIndentation(previousNode);
    JavaToken token = CreateJavaNodeHelper.findLeftJavaToken(newNode);
    String diff = null;
    if (token != null && bufferTmp.toString().length() > 0) {
      AbstractBastNode parent = getFirstParent(npiTemplate);
      if (parent.getTag() == BastBlock.TAG && npiTemplate.list.size() > 4) {
        StringBuffer bufferTmpParent =
            PatternGenerator.extractIndentation(npiTemplate.list.get(1).parent);
        StringBuffer bufferTmpGrandparent =
            PatternGenerator.extractIndentation(npiTemplate.list.get(3).parent);
        if (bufferTmpParent != null && bufferTmpGrandparent != null) {
          diff = bufferTmpParent.toString().replace(bufferTmpGrandparent.toString(), "");
        }
      } else if (parent.getTag() == BastBlock.TAG && npiTemplate.list.size() > 1) {
        StringBuffer bufferTmpParent =
            PatternGenerator.extractIndentation(npiTemplate.list.get(1).parent);
        if (bufferTmpParent != null) {
          diff = bufferTmpParent.toString();
        }
      }
      if (bufferTmp.toString().equals(" ") && parent.getTag() == BastBlock.TAG) {
        @SuppressWarnings("unchecked")
        LinkedList<AbstractBastNode> nodes = (LinkedList<AbstractBastNode>) parent
            .getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
        if (nodes.size() > 0) {
          bufferTmp = PatternGenerator.extractIndentation(nodes.get(0));
        }
      }
      String[] parts = bufferTmp.toString().split("\n");
      if (parts.length > 2) {
        bufferTmp.setLength(0);
        bufferTmp.append("\n");
        bufferTmp.append(parts[parts.length - 1]);
      }
      token.whiteSpace.setLength(0);
      token.whiteSpace.append(bufferTmp.toString());
    } else {
      if (token != null && npiTemplate.list.size() > 0) {
        AbstractBastNode parent = getFirstParent(npiTemplate);
        if (parent.getTag() == BastBlock.TAG && npiTemplate.list.size() > 4) {
          StringBuffer bufferTmpParent =
              PatternGenerator.extractIndentation(npiTemplate.list.get(1).parent);
          StringBuffer bufferTmpGrandparent =
              PatternGenerator.extractIndentation(npiTemplate.list.get(3).parent);
          if (bufferTmpParent != null && bufferTmpGrandparent != null) {
            diff = bufferTmpParent.toString().replace(bufferTmpGrandparent.toString(), "");
            if (diff.length() > 0) {
              token.whiteSpace.setLength(0);
              token.whiteSpace.append(bufferTmpParent);
              token.whiteSpace.append(diff);
            }
          }
        } else if (parent.getTag() == BastBlock.TAG && npiTemplate.list.size() > 1) {
          StringBuffer bufferTmpParent =
              PatternGenerator.extractIndentation(npiTemplate.list.get(1).parent);
          if (bufferTmpParent != null) {
            diff = bufferTmpParent.toString();
          }
        }
      }
    }
  }
}
