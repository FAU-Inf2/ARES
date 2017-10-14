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

package de.fau.cs.inf2.cas.ares.recommendation;

import de.fau.cs.inf2.cas.ares.bast.general.ParentHierarchyHandler;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresCaseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.bast.visitors.NodeStreamVisitor;
import de.fau.cs.inf2.cas.ares.io.AresMeasurement;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.recommendation.extension.EditScriptApplicator;
import de.fau.cs.inf2.cas.ares.recommendation.extension.ExtendedTemplateExtractor;
import de.fau.cs.inf2.cas.ares.recommendation.visitors.FindInitialPatternStartsVisitor;
import de.fau.cs.inf2.cas.ares.recommendation.visitors.FindSpecialPatternStartsVisitor;
import de.fau.cs.inf2.cas.ares.recommendation.visitors.GetChildrenMapVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformation;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEmptyStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitchCaseGroup;
import de.fau.cs.inf2.cas.common.bast.visitors.FindNodesFromTagVisitor;
import de.fau.cs.inf2.cas.common.bast.visitors.NormalFormNodeStreamVisitor;

import de.fau.cs.inf2.cas.common.parser.AresExtension;
import de.fau.cs.inf2.cas.common.parser.ParserType;

import de.fau.cs.inf2.cas.common.tokenmatching.LevenshteinTokenDistance;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.matching.LeavesSimilarityCalculator;
import de.fau.cs.inf2.mtdiff.matching.SimilarityAdapter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.concurrent.ExecutorService;

@SuppressWarnings("PMD.ExcessiveClassLength")
public class RecommendationCreator {
  public static final boolean DEBUG_SEARCH = false;

  private static CompareNodesResult compareNodes(
      Map<AbstractBastNode, NodeParentInformationHierarchy> programParents,
      Map<AbstractBastNode, NodeParentInformationHierarchy> templateParents,
      ExtendedAresPattern template, ArrayList<AbstractBastNode> programStream,
      ArrayList<AbstractBastNode> templateStream, int positionTemplate, int positionProgram,
      AbstractBastNode templateNode, AbstractBastNode programNode) {
    if (programNode.getTag() == templateNode.getTag()) {
      if (programNode.getTag() == BastNameIdent.TAG) {

        if (!((BastNameIdent) programNode).name.equals(((BastNameIdent) templateNode).name)) {
          if (!template.ignoreIdentifier(((BastNameIdent) templateNode).name)) {
            return new CompareNodesResult(positionTemplate, positionProgram,
                CompareNodesResult.FAILED, CompareNodesResult.NO_WILDCARD);
          }
        }

      }

      template.assignmentMap.put(templateNode, programNode);


      if (templateNode.getTag() == BastCall.TAG) {
        BastCall call = (BastCall) templateNode;
        switch (call.function.getTag()) {
          case BastNameIdent.TAG:
            break;
          default:
        }
      }
      return new CompareNodesResult(positionTemplate + 1, positionProgram + 1,
          CompareNodesResult.SUCCESS, CompareNodesResult.NO_WILDCARD);

    } else {
      if (programNode.getTag() == BastBlock.TAG || templateNode.getTag() == BastBlock.TAG) {
        NodeParentInformationHierarchy npiProgram = programParents.get(programNode);
        NodeParentInformationHierarchy npiTemplate = templateParents.get(templateNode);
        if (npiProgram != null && npiTemplate != null) {
          if (npiProgram.list.size() > 0 && npiTemplate.list.size() > 0) {
            NodeParentInformation npProgram = npiProgram.list.get(0);
            NodeParentInformation npTemplate = npiTemplate.list.get(0);
            if (npProgram.parent.getTag() == BastIf.TAG
                && npTemplate.parent.getTag() == BastIf.TAG) {
              if (npProgram.fieldConstant == BastFieldConstants.IF_IF_PART
                  && npTemplate.fieldConstant == BastFieldConstants.IF_IF_PART) {
                if (programNode.getTag() == BastBlock.TAG) {
                  return new CompareNodesResult(positionTemplate, positionProgram + 1,
                      CompareNodesResult.SUCCESS, CompareNodesResult.NO_WILDCARD);
                } else {
                  return new CompareNodesResult(positionTemplate + 1, positionProgram,
                      CompareNodesResult.SUCCESS, CompareNodesResult.NO_WILDCARD);
                }
              }
            } else if (npProgram.parent.getTag() == BastSwitchCaseGroup.TAG
                && npTemplate.parent.getTag() == BastSwitchCaseGroup.TAG) {
              if (npProgram.fieldConstant == BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS
                  && npTemplate.fieldConstant == BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS) {
                if (programNode.getTag() == BastBlock.TAG) {
                  return new CompareNodesResult(positionTemplate, positionProgram + 1,
                      CompareNodesResult.SUCCESS, CompareNodesResult.NO_WILDCARD);
                } else {
                  return new CompareNodesResult(positionTemplate + 1, positionProgram,
                      CompareNodesResult.SUCCESS, CompareNodesResult.NO_WILDCARD);
                }
              }
            }
          }
        }
      }
    }
    return new CompareNodesResult(positionTemplate, positionProgram, CompareNodesResult.FAILED,
        CompareNodesResult.NO_WILDCARD);
  }

  /**
   * Creates the transformed program.
   *
   */
  public static ArrayList<ArrayList<Recommendation>> createTransformedProgram(String testA,
      File original, File modified, byte[] testBytes, ExecutorService executor,
      AresMeasurement recommendationMeasurement, ExtendedAresPattern template) {
    Path pathOriginal = original.toPath();
    byte[] dataOriginal;
    try {
      dataOriginal = Files.readAllBytes(pathOriginal);
      Path pathModified = modified.toPath();
      byte[] dataModified = Files.readAllBytes(pathModified);
      return createTransformedProgram(testA, dataOriginal, dataModified, testBytes, executor,
          recommendationMeasurement, template);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Creates the transformed program.
   *
   */
  public static ArrayList<ArrayList<Recommendation>> createTransformedProgram(String testA,
      byte[] original, byte[] modified, byte[] testBytes, ExecutorService executor,
      AresMeasurement recommendationMeasurement, ExtendedAresPattern template) {
    return createTransformedProgram(testA, original, modified, testBytes, false, executor,
        recommendationMeasurement, template);
  }

  /**
   * Creates the transformed program.
   *
   */
  public static ArrayList<ArrayList<Recommendation>> createTransformedProgram(String testA,
      byte[] original, byte[] modified, byte[] testBytes, boolean verbose, ExecutorService executor,
      AresMeasurement recommendationMeasurement, ExtendedAresPattern template) {

    ArrayList<ArrayList<Recommendation>> transformedPrograms =
        new ArrayList<ArrayList<Recommendation>>();
    BastProgram originalProg = null;

    BastProgram modifiedProg = null;
    if (template == null) {
      originalProg = getAst(original, ParserType.JAVA_PARSER);
      modifiedProg = getAst(modified, ParserType.JAVA_PARSER);
      template = extractTemplate(originalProg, modifiedProg, executor, recommendationMeasurement);
      template.resolveWildcards();
    }

    BastProgram testProg =
        AnalysePath.analyse(testBytes, ParserType.JAVA_PARSER, false, AresExtension.NO_EXTENSIONS);
    de.fau.cs.inf2.cas.ares.recommendation.visitors.CountNodesVisitor countVisitor =
        new de.fau.cs.inf2.cas.ares.recommendation.visitors.CountNodesVisitor();
    testProg.accept(countVisitor);
    if (template.originalAst.block.statements == null
        || template.originalAst.block.statements.size() == 0) {
      return null;
    }
    FindInitialPatternStartsVisitor starts =
        new FindInitialPatternStartsVisitor(template.originalAst.block.statements, template);
    testProg.accept(starts);
    GetChildrenMapVisitor gcmvProg = new GetChildrenMapVisitor();
    testProg.accept(gcmvProg);
    GetChildrenMapVisitor gcmvTemplate = new GetChildrenMapVisitor();
    template.originalAst.accept(gcmvTemplate);
    NormalFormNodeStreamVisitor templateTokenVisitor =
        new NormalFormNodeStreamVisitor(template.originalAst);
    template.originalAst.accept(templateTokenVisitor);
    Map<AbstractBastNode, NodeParentInformationHierarchy> programParents =
        ParentHierarchyHandler.getParentHierarchy(testProg);
    Map<AbstractBastNode, NodeParentInformationHierarchy> templateParents =
        ParentHierarchyHandler.getParentHierarchy(template.originalAst);
    int counter = 0;

    while (counter < starts.starts.size()) {
      NormalFormNodeStreamVisitor programTokenVisitor =
          new NormalFormNodeStreamVisitor(starts.starts.get(counter));
      testProg.accept(programTokenVisitor);

      int found = matchPattern(template, starts.starts.get(counter), testProg,
          gcmvProg.getChildrenMap(), gcmvTemplate.getChildrenMap(), programTokenVisitor.nodes,
          templateTokenVisitor.nodes, programParents, templateParents);

      if (found == CompareNodesResult.SUCCESS) {
        try {
          long timeCreateRecommendation = System.nanoTime();
          LinkedList<Recommendation> results = EditScriptApplicator.applyEditOperations(testProg,
              template, starts.starts.get(counter));
          if (recommendationMeasurement != null) {
            recommendationMeasurement.timeRecommendationCreation
                .add(System.nanoTime() - timeCreateRecommendation);
          }

          transformedPrograms.add(new ArrayList<>(results));

        } catch (Throwable e) {
          e.printStackTrace();
        }
        testProg = null;
        template = null;
        starts = null;
        countVisitor = null;
        gcmvProg = null;
        gcmvTemplate = null;
        templateTokenVisitor = null;
        programParents = null;
        templateParents = null;
        programTokenVisitor = null;

        originalProg = getAst(original, ParserType.JAVA_PARSER);

        modifiedProg = getAst(modified, ParserType.JAVA_PARSER);

        template = extractTemplate(originalProg, modifiedProg, executor, recommendationMeasurement);
        template.resolveWildcards();
        gcmvTemplate = new GetChildrenMapVisitor();
        template.originalAst.accept(gcmvTemplate);
        templateTokenVisitor = new NormalFormNodeStreamVisitor(template.originalAst);
        template.originalAst.accept(templateTokenVisitor);
        testProg = AnalysePath.analyse(testBytes, ParserType.JAVA_PARSER, false,
            AresExtension.NO_EXTENSIONS);
        countVisitor = new de.fau.cs.inf2.cas.ares.recommendation.visitors.CountNodesVisitor();
        testProg.accept(countVisitor);
        starts =
            new FindInitialPatternStartsVisitor(template.originalAst.block.statements, template);
        testProg.accept(starts);
        gcmvProg = new GetChildrenMapVisitor();
        testProg.accept(gcmvProg);
        programTokenVisitor = new NormalFormNodeStreamVisitor(starts.starts.get(counter));
        testProg.accept(programTokenVisitor);
        programParents = ParentHierarchyHandler.getParentHierarchy(testProg);
        templateParents = ParentHierarchyHandler.getParentHierarchy(template.originalAst);
      } else {
        template.assignmentMap.clear();
        if (template.wildcardInstances != null) {
          template.wildcardInstances.clear();
        }
      }
      counter++;
    }
    return transformedPrograms;
  }

  /**
   * Extract template.
   *
   */
  public static ExtendedAresPattern extractTemplate(BastProgram originalProg,
      BastProgram modifiedProg, ExecutorService executor, AresMeasurement aresMeasurement) {

    ExtendedAresPattern template = ExtendedTemplateExtractor.extractTemplate(originalProg,
        modifiedProg, executor, aresMeasurement);
    template.resolveWildcards();
    return template;
  }

  private static AbstractBastNode getAssociatedNodes(AresWildcard wildcard,
      ArrayList<AbstractBastNode> templateNodes,
      HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> templateChildrenMap) {
    if (wildcard.plugin == null) {
      return null;
    }
    if (wildcard.plugin.exprList == null || wildcard.plugin.exprList.size() == 0) {
      return null;
    }
    long occurrence = 1;
    if (((AresPatternClause) wildcard.plugin.exprList.get(0)).occurrence != null) {
      occurrence = ((AresPatternClause) wildcard.plugin.exprList.get(0)).occurrence.value;
    }

    final ArrayList<AbstractBastNode> templateChildrenNodes = new ArrayList<>();
    ArrayList<AbstractBastNode> templatePatternNodes = new ArrayList<>();

    ArrayList<AbstractBastNode> children =
        templateChildrenMap.get(((AresPatternClause) wildcard.plugin.exprList.get(0)).expr);
    templatePatternNodes.addAll(children);
    templatePatternNodes.add(((AresPatternClause) wildcard.plugin.exprList.get(0)).expr);
    AbstractBastNode wild = wildcard;
    while (wild.getTag() == AresWildcard.TAG && ((AresWildcard) wild).statements != null) {
      wild = ((AresWildcard) wild).statements.get(0);
    }
    templateChildrenNodes.add(wild);
    children = templateChildrenMap.get(wild);
    templateChildrenNodes.addAll(children);
    int index = -1;
    for (int i = 0; i < templateChildrenNodes.size(); i++) {
      boolean found = true;
      if (i + templatePatternNodes.size() > templateChildrenNodes.size()) {

        break;
      }
      for (int j = 0; j < templatePatternNodes.size(); j++) {
        if (templateChildrenNodes.get(i + j).getTag() != templatePatternNodes.get(j).getTag()) {
          found = false;
          break;
        } else {
          if (templateChildrenNodes.get(i + j).getTag() == BastNameIdent.TAG) {
            if (!((BastNameIdent) templateChildrenNodes.get(i + j)).name
                .equals(((BastNameIdent) templatePatternNodes.get(j)).name)) {
              found = false;
              break;
            }
          }
        }
      }
      if (found) {
        occurrence--;
        if (occurrence == 0) {
          index = i + templatePatternNodes.size() - 1;
          break;
        }
      }
    }
    assert (index != -1);
    if (index < 0 || index > templateChildrenNodes.size()) {
      return null;
    }
    return templateChildrenNodes.get(index);
  }

  /**
   * Gets the ast.
   *
   */
  public static BastProgram getAst(File original, ParserType parserType, AresExtension extension) {
    BastProgram ast = AnalysePath.analyse(original, parserType, false, extension);
    return ast;
  }

  /**
   * Gets the ast.
   *
   */
  public static BastProgram getAst(File original, ParserType parserType) {
    BastProgram ast = AnalysePath.analyse(original, parserType);
    return ast;
  }

  /**
   * Gets the ast.
   *
   */
  public static BastProgram getAst(byte[] original, ParserType parserType,
      AresExtension extension) {
    BastProgram ast = AnalysePath.analyse(original, parserType, false, extension);
    return ast;
  }

  /**
   * Gets the ast.
   *
   */
  public static BastProgram getAst(byte[] original, ParserType parserType) {
    BastProgram originalProg = AnalysePath.analyse(original, parserType, false);
    return originalProg;
  }

  private static int innerMatchPattern(ExtendedAresPattern template, AbstractBastNode start,
      AbstractBastNode testProgram,
      HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> programChildrenMap,
      HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> templateChildrenMap,
      ArrayList<AbstractBastNode> programStream, ArrayList<AbstractBastNode> templateStream,
      Map<AbstractBastNode, NodeParentInformationHierarchy> programParents,
      Map<AbstractBastNode, NodeParentInformationHierarchy> templateParents) {

    LinkedList<CompareNodesResult> wildcardResets = new LinkedList<>();
    HashMap<CompareNodesResult, WildcardInstance> resetTowildcardMap = new HashMap<>();
    HashMap<AresWildcard, WildcardInstance> wildcardMap = new HashMap<>();
    HashMap<AbstractBastNode, WildcardInstance> nodesToAssociatedWildcard = new HashMap<>();
    HashMap<AresWildcard, LinkedList<AbstractBastNode>> requiredNodesMap = new HashMap<>();

    FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(AresWildcard.TAG);
    template.originalAst.accept(fnft);
    for (AbstractBastNode node : fnft.nodes) {
      if (WildcardAccessHelper.isExprWildcard(node)) {
        continue;
      }
      NodeParentInformationHierarchy npiTemplate = templateParents.get(node);
      if (npiTemplate != null && npiTemplate.list != null && npiTemplate.list.size() > 0) {
        NodeParentInformation np = npiTemplate.list.get(0);
        if (np.fieldConstant.isList) {
          @SuppressWarnings("unchecked")
          LinkedList<AbstractBastNode> nodes =
              (LinkedList<AbstractBastNode>) np.parent.getField(np.fieldConstant).getListField();
          LinkedList<AbstractBastNode> requiredNodes = new LinkedList<>();
          for (int i = np.listId + 1; i < nodes.size(); i++) {
            if (WildcardAccessHelper.isWildcard(nodes.get(i))
                || nodes.get(i).getTag() == BastEmptyStmt.TAG) {
              continue;
            }
            requiredNodes.add(nodes.get(i));
          }
          requiredNodesMap.put((AresWildcard) node, requiredNodes);
        }
      }
    }
    int positionTemplate = 0;
    int positionProgram = 0;
    NodeParentInformationHierarchy startHierarchy = programParents.get(start);
    template.patternStart = startHierarchy.list.get(0).parent;
    template.patternStartFieldId = startHierarchy.list.get(0).fieldConstant;
    template.patternStartListIdOld = startHierarchy.list.get(0).listId;
    HashSet<WildcardInstance> allowReset = new HashSet<>();
    boolean done = false;
    while (!done) {
      CompareNodesResult result = null;
      while (positionTemplate < templateStream.size() && positionProgram < programStream.size()) {
        AbstractBastNode templateToken = templateStream.get(positionTemplate);
        AbstractBastNode programToken = programStream.get(positionProgram);
        if (DEBUG_SEARCH) {
          System.err.println(templateToken.getClass().getName() + "(" + positionTemplate + ")"
              + " <-> " + programToken.getClass().getName() + "(" + positionProgram + ") "
              + templateToken.toString().replace("\n", "") + " <-> "
              + programToken.toString().replace("\n", ""));
        }
        if (templateToken.getTag() == AresWildcard.TAG) {
          WildcardInstance instance = wildcardMap.get(templateToken);

          if (instance == null) {
            if (programToken.getTag() == BastBlock.TAG) {
              NodeParentInformationHierarchy npiProgram = programParents.get(programToken);
              NodeParentInformationHierarchy npiTemplate = templateParents.get(templateToken);
              if (npiProgram != null && npiTemplate != null) {
                if (npiProgram.list.size() > 0 && npiTemplate.list.size() > 0) {
                  NodeParentInformation npProgram = npiProgram.list.get(0);
                  NodeParentInformation npTemplate = npiTemplate.list.get(0);
                  if (npProgram.parent.getTag() == BastSwitchCaseGroup.TAG
                      && npTemplate.parent.getTag() == BastSwitchCaseGroup.TAG) {
                    BastFieldConstants fieldConstant = npTemplate.fieldConstant;
                    if (npProgram.fieldConstant == BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS
                        && fieldConstant == BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS) {
                      positionProgram++;
                      continue;
                    }
                  }
                }
              }
            }
          }
        }
        if (templateToken.getTag() == AresBlock.TAG) {
          AresBlock block = (AresBlock) templateToken;
          AbstractBastNode firstInterestingToken = block.block.statements.getFirst();
          positionTemplate = templateStream.indexOf(firstInterestingToken);
          continue;
        }
        if (templateToken.getTag() == AresWildcard.TAG) {
          AresWildcard wildcard = (AresWildcard) templateToken;
          if (wildcard.associatedStatements != null && wildcard.associatedStatements.size() > 0) {
            WildcardInstance instance = wildcardMap.get(templateToken);
            if (instance == null) {
              instance = new WildcardInstance(wildcard, (BastProgram) testProgram);
              AbstractBastNode startAssociationNode =
                  getAssociatedNodes(wildcard, templateStream, templateChildrenMap);
              nodesToAssociatedWildcard.put(startAssociationNode, instance);
              wildcardMap.put(wildcard, instance);
              if (instance.acceptEmpty()) {
                ArrayList<AbstractBastNode> associatedChildren =
                    templateChildrenMap.get(startAssociationNode);
                int index = templateStream.indexOf(startAssociationNode);
                if (positionProgram == 0 && associatedChildren != null
                    && index + associatedChildren.size() + 1 < templateStream.size()
                    && templateStream.get(index + associatedChildren.size() + 1)
                        .getTag() == AresWildcard.TAG) {
                  // do nothing
                } else {
                  if (instance.wildcard.fixedNodes == null
                      || instance.wildcard.fixedNodes.size() == 0) {
                    CompareNodesResult reset = new CompareNodesResult(
                        index + associatedChildren.size() + 1, positionProgram,
                        CompareNodesResult.NEUTRAL, CompareNodesResult.WILDCARD_ACCEPT);
                    wildcardResets.add(reset);
                    resetTowildcardMap.put(reset, instance);
                  }
                }
              }
              int index = templateStream.indexOf(wildcard.statements.getFirst());

              positionTemplate = index;
            } else {
              if (instance.nodes.size() == 0) {
                wildcardMap.remove(templateToken);
                continue;
              }
              return CompareNodesResult.FAILED;
            }
          } else {
            WildcardInstance instance = wildcardMap.get(templateToken);

            if (instance == null) {
              instance = new WildcardInstance(wildcard, (BastProgram) testProgram);
              wildcardMap.put(wildcard, instance);
              ArrayList<AbstractBastNode> childlist = templateChildrenMap.get(wildcard);
              if (instance.acceptEmpty()) {
                if (positionProgram == 0 && templateStream
                    .get(positionTemplate + childlist.size() + 1).getTag() == AresWildcard.TAG) {
                  // do nothing
                } else {
                  CompareNodesResult reset = new CompareNodesResult(
                      positionTemplate + childlist.size() + 1, positionProgram,
                      CompareNodesResult.NEUTRAL, CompareNodesResult.WILDCARD_ACCEPT);
                  wildcardResets.add(reset);
                  resetTowildcardMap.put(reset, instance);
                }
              }
            } else if (allowReset.contains(instance)) {
              if (instance.acceptEmpty()) {
                ArrayList<AbstractBastNode> childlist = templateChildrenMap.get(wildcard);
                if (positionProgram == 0 && templateStream
                    .get(positionTemplate + childlist.size() + 1).getTag() == AresWildcard.TAG) {
                  // do nothing
                } else {

                  CompareNodesResult reset = new CompareNodesResult(
                      positionTemplate + childlist.size() + 1, positionProgram,
                      CompareNodesResult.NEUTRAL, CompareNodesResult.WILDCARD_ACCEPT);
                  wildcardResets.add(reset);
                  resetTowildcardMap.put(reset, instance);
                }
              }
              allowReset.remove(instance);
            }
            int wildCardTest = instance.acceptNode(programToken, templateToken, programChildrenMap,
                programParents, templateParents, template.assignmentMap);
            CompareNodesResult reset = null;
            ArrayList<AbstractBastNode> childlist = null;
            switch (wildCardTest) {
              case CompareNodesResult.WILDCARD_ACCEPT:
                int offset = 1;
                childlist = templateChildrenMap.get(wildcard);
                LinkedList<AbstractBastNode> requiredNodes = requiredNodesMap.get(wildcard);
                childlist = createReset(templateChildrenMap, wildcardResets, resetTowildcardMap,
                    positionTemplate, positionProgram, wildcard, instance, offset, programStream,
                    templateStream, requiredNodes, programParents);
                if (positionTemplate + childlist.size() + offset < templateStream.size()
                    && templateStream.get(positionTemplate + childlist.size() + offset)
                        .getTag() == BastSwitchCaseGroup.TAG) {
                  childlist = createReset(templateChildrenMap, wildcardResets, resetTowildcardMap,
                      positionTemplate, positionProgram, wildcard, instance, offset + 1,
                      programStream, templateStream, requiredNodes, programParents);
                  childlist = createReset(templateChildrenMap, wildcardResets, resetTowildcardMap,
                      positionTemplate, positionProgram - instance.nodes.size(), wildcard, instance,
                      offset + 1, programStream, templateStream, requiredNodes, programParents);
                }
                positionProgram++;

                continue;

              case CompareNodesResult.WILDCARD_NO_MATCH:
                childlist = templateChildrenMap.get(wildcard);
                positionTemplate = positionTemplate + childlist.size() + 1;
                continue;
              case CompareNodesResult.WILDCARD_UNFINISHED:
                positionProgram++;
                continue;
              case CompareNodesResult.WILDCARD_FAIL:
                if (wildcardResets.isEmpty()) {
                  return CompareNodesResult.FAILED;
                } else {
                  reset = wildcardResets.removeLast();
                  instance.triedResetNodes.add(reset);
                  for (WildcardInstance tmpInstance : wildcardMap.values()) {
                    for (int i = positionProgram; i >= reset.positionProgram; i--) {
                      tmpInstance.removeNode(programStream.get(i));
                    }
                  }
                  positionProgram = reset.positionProgram;
                  positionTemplate = reset.positionTemplate;

                }
                break;
              default:
                break;
            }
          }
        } else {
          if (nodesToAssociatedWildcard.containsKey(templateToken)) {
            NodeParentInformationHierarchy npi = programParents.get(programToken);
            WildcardInstance instance = nodesToAssociatedWildcard.get(templateToken);
            int wildCardTest = instance.acceptNode(programToken, templateToken, programChildrenMap,
                programParents, templateParents, template.assignmentMap);
            CompareNodesResult reset = null;
            ArrayList<AbstractBastNode> associatedChildren = null;
            int index = -1;
            if (npi != null && npi.list != null) {
              if (npi.list.get(0).parent.getTag() == BastBlock.TAG
                  || (npi.list.get(0).parent.getTag() == BastIf.TAG
                      && npi.list.get(0).fieldConstant == BastFieldConstants.IF_IF_PART)) {
                AbstractBastNode startAssociationNode =
                    getAssociatedNodes(instance.wildcard, templateStream, templateChildrenMap);
                associatedChildren = templateChildrenMap.get(startAssociationNode);
                if (instance.wildcard.fixedNodes == null
                    || instance.wildcard.fixedNodes.size() == 0) {
                  index = templateStream.indexOf(startAssociationNode);
                  positionTemplate = index + associatedChildren.size() + 1;
                  instance.removeNode(programToken);
                  continue;
                } else {
                  wildCardTest = CompareNodesResult.WILDCARD_FAIL;
                }
              }
            }

            switch (wildCardTest) {
              case CompareNodesResult.WILDCARD_ACCEPT:
                associatedChildren = templateChildrenMap.get(templateToken);
                int programPos = 0;
                if (associatedChildren.size() > 0) {
                  if (instance.nodes.contains(programToken)) {
                    programPos++;
                  }

                }
                reset = new CompareNodesResult(positionTemplate + associatedChildren.size() + 1,
                    positionProgram + programPos, CompareNodesResult.NEUTRAL,
                    CompareNodesResult.WILDCARD_ACCEPT);
                boolean found = false;
                for (CompareNodesResult res : instance.triedResetNodes) {
                  if (res.positionProgram == positionProgram
                      && res.positionTemplate == positionTemplate + 1) {
                    found = true;
                  }
                }
                if (!found) {
                  wildcardResets.add(reset);
                  resetTowildcardMap.put(reset, instance);
                }
                positionProgram++;
                break;

              case CompareNodesResult.WILDCARD_NO_MATCH:
                AbstractBastNode startAssociationNode =
                    getAssociatedNodes(instance.wildcard, templateStream, templateChildrenMap);
                associatedChildren = templateChildrenMap.get(startAssociationNode);
                index = templateStream.indexOf(startAssociationNode);
                positionTemplate = index + associatedChildren.size() + 1;
                continue;
              case CompareNodesResult.WILDCARD_UNFINISHED:
                positionProgram++;
                continue;
              case CompareNodesResult.WILDCARD_FAIL:
                if (wildcardResets.isEmpty()) {
                  return CompareNodesResult.FAILED;
                } else {
                  reset = wildcardResets.removeLast();
                  instance.triedResetNodes.add(reset);
                  for (int i = positionProgram; i >= reset.positionProgram; i--) {
                    instance.removeNode(programStream.get(i));
                  }
                  positionProgram = reset.positionProgram;
                  positionTemplate = reset.positionTemplate;

                }
                break;
              default:
                break;
            }
          } else {
            result = compareNodes(programParents, templateParents, template, programStream,
                templateStream, positionTemplate, positionProgram, templateToken, programToken);
            if (result.status == CompareNodesResult.FAILED) {
              result = compareNodes(programParents, templateParents, template, programStream,
                  templateStream, positionTemplate, positionProgram, templateToken, programToken);
              if (wildcardResets.isEmpty()) {
                return CompareNodesResult.FAILED;
              } else {
                CompareNodesResult reset = wildcardResets.removeLast();
                while (reset != null && reset.positionTemplate > positionTemplate) {
                  if (wildcardResets.size() > 0) {
                    reset = wildcardResets.removeLast();
                  } else {
                    return CompareNodesResult.FAILED;
                  }
                }
                if (reset == null) {
                  return CompareNodesResult.FAILED;
                }
                WildcardInstance instance = resetTowildcardMap.get(reset);
                instance.triedResetNodes.add(reset);
                for (int i = positionProgram; i >= reset.positionProgram; i--) {
                  instance.removeNode(programStream.get(i));
                }
                for (WildcardInstance tmpInstance : resetTowildcardMap.values()) {
                  for (int i = positionProgram; i >= reset.positionProgram; i--) {
                    tmpInstance.removeNode(programStream.get(i));
                  }
                }
                positionProgram = reset.positionProgram;
                positionTemplate = reset.positionTemplate;
                if (positionTemplate == templateStream.size()) {
                  return CompareNodesResult.FAILED;
                }

              }
            } else {
              assert (result.status == CompareNodesResult.SUCCESS);
              positionTemplate = result.positionTemplate;
              positionProgram = result.positionProgram;
            }
          }
        }

      }
      if (positionTemplate == templateStream.size()) {
        done = true;
      } else if (positionTemplate < templateStream.size()
          && templateStream.get(positionTemplate).getTag() == AresWildcard.TAG) {
        AresWildcard wildcard = (AresWildcard) templateStream.get(positionTemplate);
        WildcardInstance instance = wildcardMap.get(wildcard);
        int listSize = 0;
        if (instance == null) {
          instance = new WildcardInstance(wildcard, (BastProgram) testProgram);
          wildcardMap.put(wildcard, instance);
          ArrayList<AbstractBastNode> childlist = templateChildrenMap.get(wildcard);
          listSize = childlist.size();
          if (instance.acceptEmpty()) {
            CompareNodesResult reset =
                new CompareNodesResult(positionTemplate + childlist.size() + 1, positionProgram,
                    CompareNodesResult.NEUTRAL, CompareNodesResult.WILDCARD_ACCEPT);
            wildcardResets.add(reset);
            resetTowildcardMap.put(reset, instance);
          }
        }

        if (instance.acceptEmpty()) {
          positionTemplate += listSize + 1;
          continue;
        }
      } else if (positionTemplate < templateStream.size()
          && nodesToAssociatedWildcard.containsKey(templateStream.get(positionTemplate))) {
        AresWildcard wildcard = (AresWildcard) nodesToAssociatedWildcard
            .get(templateStream.get(positionTemplate)).wildcard;
        wildcardMap.get(wildcard);
        ArrayList<AbstractBastNode> associatedChildren =
            templateChildrenMap.get(templateStream.get(positionTemplate));
        positionTemplate += associatedChildren.size() + 1;
        continue;

      }
      if (!done) {
        if (wildcardResets.isEmpty()) {
          return CompareNodesResult.FAILED;
        } else {
          CompareNodesResult reset = wildcardResets.removeLast();
          WildcardInstance instance = resetTowildcardMap.get(reset);
          boolean found = false;

          for (CompareNodesResult cnr : wildcardResets) {
            if (resetTowildcardMap.get(cnr).wildcard == instance.wildcard) {
              found = true;
            }
          }
          if (!found) {
            allowReset.add(instance);
          }
          instance.triedResetNodes.add(reset);
          positionProgram--;
          for (int i = positionProgram; i >= reset.positionProgram; i--) {
            instance.removeNode(programStream.get(i));
          }
          positionProgram = reset.positionProgram;
          positionTemplate = reset.positionTemplate;

        }
      }

    }
    template.wildcardInstances = new LinkedList<>();
    template.wildcardInstances.addAll(wildcardMap.values());
    return CompareNodesResult.SUCCESS;

  }

  private static ArrayList<AbstractBastNode> createReset(
      HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> templateChildrenMap,
      LinkedList<CompareNodesResult> wildcardResets,
      HashMap<CompareNodesResult, WildcardInstance> resetTowildcardMap, int positionTemplate,
      int positionProgram, AresWildcard wildcard, WildcardInstance instance, int offset,
      ArrayList<AbstractBastNode> programStream, ArrayList<AbstractBastNode> templateStream,
      LinkedList<AbstractBastNode> requiredNodes,
      Map<AbstractBastNode, NodeParentInformationHierarchy> programParents) {
    CompareNodesResult reset;
    ArrayList<AbstractBastNode> childlist;
    childlist = templateChildrenMap.get(wildcard);
    reset = new CompareNodesResult(positionTemplate + childlist.size() + offset,
        positionProgram + 1, CompareNodesResult.NEUTRAL, CompareNodesResult.WILDCARD_ACCEPT);

    boolean found = false;
    for (CompareNodesResult res : instance.triedResetNodes) {
      if (res.positionProgram == positionProgram && res.positionTemplate == positionTemplate + 1) {
        found = true;
      }
    }
    if (!found) {
      if (reset.positionTemplate < templateStream.size()) {
        if (templateStream.get(reset.positionTemplate).getTag() != AresWildcard.TAG) {
          if (reset.positionProgram < programStream.size()) {
            if (templateStream.get(reset.positionTemplate).getTag() != programStream
                .get(reset.positionProgram).getTag()) {
              return childlist;
            }
          }
        }
      }
      if (requiredNodes != null && requiredNodes.size() > 0) {
        if (instance.headNodes.size() > 0) {
          AbstractBastNode matchedProgramNode =
              instance.headNodes.get(instance.headNodes.size() - 1);
          if (matchedProgramNode != null) {
            NodeParentInformationHierarchy npi = programParents.get(matchedProgramNode);
            if (npi != null && npi.list != null && npi.list.size() > 0) {
              NodeParentInformation np = npi.list.get(0);
              if (np.parent.getField(np.fieldConstant).isList()) {
                @SuppressWarnings("unchecked")
                LinkedList<AbstractBastNode> nodes = (LinkedList<AbstractBastNode>) np.parent
                    .getField(np.fieldConstant).getListField();
                int remainingSize = nodes.size() - np.listId;
                if (remainingSize < requiredNodes.size()) {
                  return childlist;
                }
                boolean possibleLocationFound = false;
                int locationPosition = np.listId + 1;
                for (int i = 0; i < requiredNodes.size(); i++) {
                  AbstractBastNode nextNodeToFind = requiredNodes.get(i);
                  boolean foundRequiredNode = false;
                  for (int j = locationPosition; j < nodes.size(); j++) {
                    if (nextNodeToFind.getTag() == nodes.get(j).getTag()) {
                      locationPosition = j + 1;
                      foundRequiredNode = true;
                      break;
                    }
                  }
                  if (!foundRequiredNode) {
                    return childlist;
                  }
                  if (i == requiredNodes.size() - 1) {
                    possibleLocationFound = true;
                  }
                }
                if (!possibleLocationFound) {
                  return childlist;
                }
              }
            }
          }
        }

      }
      wildcardResets.add(reset);
      resetTowildcardMap.put(reset, instance);
    }
    return childlist;
  }

  private static int matchPattern(ExtendedAresPattern template, AbstractBastNode start,
      AbstractBastNode testProgram,
      HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> programChildrenMap,
      HashMap<AbstractBastNode, ArrayList<AbstractBastNode>> templateChildrenMap,
      ArrayList<AbstractBastNode> programStream, ArrayList<AbstractBastNode> templateStream,
      Map<AbstractBastNode, NodeParentInformationHierarchy> programParents,
      Map<AbstractBastNode, NodeParentInformationHierarchy> templateParents) {
    int found = innerMatchPattern(template, start, testProgram, programChildrenMap,
        templateChildrenMap, programStream, templateStream, programParents, templateParents);
    if (found == CompareNodesResult.SUCCESS) {
      boolean nonChoiceEdits = false;
      Map<AbstractBastNode, NodeParentInformationHierarchy> templateChangedParents =
          ParentHierarchyHandler.getParentHierarchy(template.modifiedAst);

      for (BastEditOperation ep : template.getDiffResult().editScript) {
        NodeParentInformationHierarchy npi = templateParents.get(ep.getOldOrInsertedNode());
        if (npi == null) {
          npi = templateChangedParents.get(ep.getOldOrInsertedNode());
        }
        if (npi != null) {
          boolean relevant = true;
          for (NodeParentInformation np : npi.list) {
            if (np.parent.getTag() == AresWildcard.TAG
                || np.parent.getTag() == AresChoiceStmt.TAG) {
              relevant = false;
              break;
            }
          }
          if (!relevant) {
            continue;
          }
        }
        switch (ep.getType()) {
          case INSERT:
            if (ep.getOldOrInsertedNode().getTag() == AresUseStmt.TAG
                || ep.getOldOrInsertedNode().getTag() == AresChoiceStmt.TAG) {
              continue;
            }
            break;
          case DELETE:
            BastFieldConstants childrenListNumber = ep.getOldOrChangedIndex().childrenListNumber;
            if (childrenListNumber == BastFieldConstants.ARES_BLOCK_IDENTIFIERS) {
              continue;
            }
            if (ep.getOldOrInsertedNode().getTag() == AresWildcard.TAG) {
              continue;
            }
            break;
          default:

            break;
        }
        nonChoiceEdits = true;
        break;
      }
      if (!nonChoiceEdits) {
        FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(AresChoiceStmt.TAG);
        template.modifiedAst.accept(fnft);
        if (fnft.nodes != null) {
          boolean foundAllChoices = true;
          for (AbstractBastNode node : fnft.nodes) {
            AresChoiceStmt choice = (AresChoiceStmt) node;
            boolean foundChoice = false;
            for (AbstractBastNode cstmt : choice.choiceBlock.statements) {
              AresCaseStmt caseStmt = (AresCaseStmt) cstmt;
              LinkedList<AbstractBastNode> caseNodes = new LinkedList<AbstractBastNode>();
              for (AbstractBastNode stmt : caseStmt.block.statements) {
                NodeStreamVisitor nsv = new NodeStreamVisitor(stmt);
                stmt.accept(nsv);
                caseNodes.addAll(nsv.nodes);
              }
              LinkedList<Integer> starts = new LinkedList<Integer>();
              NodeParentInformationHierarchy npi = programParents.get(start);
              BastFunction startFunction = null;
              for (NodeParentInformation np : npi.list) {
                if (np.parent.getTag() == BastFunction.TAG) {
                  startFunction = (BastFunction) np.parent;
                  break;
                }
              }
              for (int i = programStream.indexOf(start); i < programStream.size(); i++) {
                if (programStream.get(i).getTag() == caseNodes.get(0).getTag()) {
                  NodeParentInformationHierarchy localnpi =
                      programParents.get(programStream.get(i));
                  BastFunction tmpFunction = null;

                  for (NodeParentInformation np : localnpi.list) {
                    if (np.parent.getTag() == BastFunction.TAG) {
                      tmpFunction = (BastFunction) np.parent;
                      break;
                    }
                  }
                  if (tmpFunction == startFunction) {
                    starts.add(i);
                  }
                }
              }
              new SimilarityAdapter(new LeavesSimilarityCalculator());
              for (Integer caseStmtStart : starts) {
                AbstractBastNode[] arrayA =
                    caseNodes.toArray(new AbstractBastNode[caseNodes.size()]);
                int sizeB = caseStmtStart + caseNodes.size() > programStream.size()
                    ? programStream.size() - caseStmtStart : caseNodes.size();
                AbstractBastNode[] arrayB = new AbstractBastNode[sizeB];
                for (int i = 0; i < sizeB; i++) {
                  arrayB[i] = programStream.get(caseStmtStart + i);
                }
                if (DEBUG_SEARCH) {
                  double sim = LevenshteinTokenDistance.matchArrays(arrayA, arrayB);
                  System.err.println("\n" + sim + " " + caseNodes.size() + "\n");
                }
              }
              if (foundChoice) {
                break;
              }
            }
            if (!foundChoice) {
              foundAllChoices = false;
              break;
            }
          }
        }
      }
    }
    return found;
  }
}
