package de.fau.cs.inf2.cas.ares.pcreation;

import de.fau.cs.inf2.cas.ares.bast.general.ParentHierarchyHandler;
import de.fau.cs.inf2.cas.ares.bast.general.ParserFactory;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPluginClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.bast.visitors.RetrieveParentInformationMapVisitor;
import de.fau.cs.inf2.cas.ares.io.AresMeasurement;
import de.fau.cs.inf2.cas.ares.pcreation.exception.GeneralizationException;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.ExecutionRunType;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.AffectedNodesOutsideMethodBlock;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules.RemoveMovesObsoleteDueToWildcards;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.CollectMatchedNodesInBoundaryVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.CombineWuvisitor;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.ConnectWildcardAndUseVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.CorrectIndentationVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.FindNodesFromTagWithoutChoiceVisitor;
import de.fau.cs.inf2.cas.ares.recommendation.extension.DeleteComparator;
import de.fau.cs.inf2.cas.ares.recommendation.extension.ExtendedTemplateExtractor;
import de.fau.cs.inf2.cas.ares.recommendation.extension.InsertComparator;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.CreateJavaNodeHelper;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformation;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.modification.ModificationInformation;
import de.fau.cs.inf2.cas.common.bast.modification.visitor.FindModScopeVisitor;
import de.fau.cs.inf2.cas.common.bast.modification.visitor.FindModificationsVisitor;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAsgnExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.nodes.BastReturn;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTryStmt;
import de.fau.cs.inf2.cas.common.bast.visitors.DefaultFieldVisitor;
import de.fau.cs.inf2.cas.common.bast.visitors.FindNodesFromTagVisitor;
import de.fau.cs.inf2.cas.common.bast.visitors.IPrettyPrinter;

import de.fau.cs.inf2.cas.common.parser.AresExtension;
import de.fau.cs.inf2.cas.common.parser.IGeneralToken;
import de.fau.cs.inf2.cas.common.parser.IParser;
import de.fau.cs.inf2.cas.common.parser.odin.BasicJavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.JavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.ListToken;
import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.DeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.MoveOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.UpdateOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.AdvancedEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.MethodRenamingOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.ParameterRenamingOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.advanced.VariableRenamingOperation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;

@SuppressWarnings({ "PMD.ExcessiveClassLength", "PMD.TooManyMethods" })
public class PatternGenerator {

  private static final boolean DEBUG = false;

  private static void updateModInfo(HashMap<AbstractBastNode, ModificationInformation> modInfoMap,
      AbstractBastNode node, BastFieldConstants field, int bound, boolean modified) {
    if (node != null) {
      ModificationInformation modInfo = modInfoMap.get(node);
      if (modInfo == null) {
        modInfo = new ModificationInformation();
        modInfoMap.put(node, modInfo);
        modInfo.fieldLowerBorder.put(field, bound);
        modInfo.fieldUpperBorder.put(field, bound);
      } else {
        Integer lower = modInfo.fieldLowerBorder.get(field);
        if (lower == null) {
          modInfo.fieldLowerBorder.put(field, bound);
          modInfo.fieldUpperBorder.put(field, bound);
        } else {
          modInfo.fieldLowerBorder.put(field, Math.min(lower, bound));
          Integer upper = modInfo.fieldUpperBorder.get(field);
          modInfo.fieldUpperBorder.put(field, Math.max(upper, bound));
        }
      }
      modInfo.nodeModified = modified;
    }
  }

  private static void updateModInfo(HashMap<AbstractBastNode, ModificationInformation> modInfoMap,
      AbstractBastNode node, boolean modified) {
    if (node != null) {
      ModificationInformation modInfo = modInfoMap.get(node);
      if (modInfo == null) {
        modInfo = new ModificationInformation();
        modInfoMap.put(node, modInfo);
        modInfo.nodeModified = true;
      } else {
        modInfo.nodeModified = true;
      }
    }
  }

  private static List<BastEditOperation> filterCallRenames(List<BastEditOperation> parameter,
      ExtendedDiffResult extDiff, HashMap<String, String> variableMap) {
    ArrayList<InsertOperation> insertList = new ArrayList<>();
    ArrayList<DeleteOperation> deleteList = new ArrayList<>();
    HashMap<InsertOperation, DeleteOperation> mapInsertDelete = new HashMap<>();
    for (BastEditOperation ep : parameter) {
      switch (ep.getType()) {
        case INSERT:
          InsertOperation iop = (InsertOperation) ep;
          if (iop.getOldOrInsertedNode().getTag() == BastCall.TAG) {
            insertList.add(iop);
          }
          break;
        case DELETE:
          DeleteOperation dop = (DeleteOperation) ep;
          if (dop.getOldOrInsertedNode().getTag() == BastCall.TAG) {
            deleteList.add(dop);
          }
          break;
        default:
          break;
      }

    }
    for (InsertOperation ip : insertList) {
      for (DeleteOperation ep : deleteList) {
        if ((extDiff.secondToFirstMap.get(ip.getUnchangedOrOldParentNode()) == ep
            .getUnchangedOrOldParentNode())) {
          if (ip.getOldOrChangedIndex().childrenListNumber == ep
              .getOldOrChangedIndex().childrenListNumber) {
            if (ip.getOldOrChangedIndex().childrenListIndex == ep
                .getOldOrChangedIndex().childrenListIndex) {
              BastCall insNode = (BastCall) ip.getOldOrInsertedNode();
              BastCall delNode = (BastCall) ep.getOldOrInsertedNode();
              if ((insNode.arguments == null || insNode.arguments.size() == 0)
                  && (delNode.arguments == null || delNode.arguments.size() == 0)) {
                if (insNode.function.getTag() == BastNameIdent.TAG
                    && delNode.function.getTag() == BastNameIdent.TAG) {
                  BastNameIdent insDec = (BastNameIdent) insNode.function;
                  BastNameIdent delDec = (BastNameIdent) delNode.function;
                  if (variableMap.get(insDec.name) == null) {
                    if (variableMap.get(delDec.name) == null) {
                      variableMap.put(delDec.name, insDec.name);
                      mapInsertDelete.put(ip, ep);
                    } else {
                      if (variableMap.get(delDec.name).equals(insDec.name)) {
                        mapInsertDelete.put(ip, ep);
                      }
                    }
                  } else if (variableMap.get(insDec.name).equals(delDec.name)) {
                    mapInsertDelete.put(ip, ep);
                    break;
                  }
                }
              }
            }
          }
        }
      }
    }
    ArrayList<AbstractBastNode> nodeWorkList = new ArrayList<>();
    for (Entry<InsertOperation, DeleteOperation> entry : mapInsertDelete.entrySet()) {
      nodeWorkList.add((AbstractBastNode) entry.getKey().getOldOrInsertedNode());
      nodeWorkList.add((AbstractBastNode) entry.getValue().getOldOrInsertedNode());
      parameter.remove(entry.getKey());
      parameter.remove(entry.getValue());
    }
    boolean changed = true;
    while (changed) {
      changed = false;
      Iterator<BastEditOperation> it = parameter.iterator();
      while (it.hasNext()) {
        BastEditOperation ep = it.next();
        switch (ep.getType()) {
          case INSERT:
            InsertOperation iop = (InsertOperation) ep;
            if (nodeWorkList.contains(iop.getUnchangedOrOldParentNode())) {
              changed = true;
              it.remove();
              nodeWorkList.add((AbstractBastNode) iop.getOldOrInsertedNode());
            }
            break;
          case DELETE:
            DeleteOperation dop = (DeleteOperation) ep;
            if (nodeWorkList.contains(dop.getUnchangedOrOldParentNode())) {
              changed = true;
              it.remove();
              nodeWorkList.add((AbstractBastNode) dop.getOldOrInsertedNode());
            }
            break;
          case MOVE:
            MoveOperation mop = (MoveOperation) ep;
            if (nodeWorkList.contains(mop.getUnchangedOrOldParentNode())) {
              changed = true;
              it.remove();
            }
            break;
          default:
            break;
        }
      }
    }
    return parameter;
  }

  static HashMap<AbstractBastNode, ModificationInformation> getModifiedNodes(
      List<BastEditOperation> editOperations, ExtendedDiffResult extDiff, boolean considerVarRename,
      boolean useAlignment) {
    HashMap<AbstractBastNode, ModificationInformation> modInfoMap = new HashMap<>();
    AbstractBastNode parent = null;
    AbstractBastNode oldParent = null;
    ModificationInformation modInfo = null;
    HashMap<String, String> variableMap = new HashMap<>();
    boolean[] ignoreOpBb = findConsistentVarRename(extDiff.editScript, variableMap, extDiff);
    int index = 0;
    List<BastEditOperation> workList = new ArrayList<>();
    workList.addAll(extDiff.editScript);
    workList =
        AffectedNodesOutsideMethodBlock.parameterRenames(workList,
            extDiff, variableMap, null);
    workList = filterCallRenames(workList, extDiff, variableMap);
    List<BastEditOperation> alignList = new ArrayList<BastEditOperation>();
    workList = RemoveMovesObsoleteDueToWildcards.alignments(workList,
        extDiff, null, null, null,
        alignList, null, ExecutionRunType.ORIGINAL_RUN);
    for (BastEditOperation ep : workList) {
      switch (ep.getType()) {
        case CLASS_RENAME:
          updateModInfo(modInfoMap, ep.getOldOrInsertedNode(), true);
          break;
        case METHOD_RENAME:
        case VARIABLE_RENAME:
        case PARAMETER_RENAME:
          if (!considerVarRename || !ignoreOpBb[index]) {
            updateModInfo(modInfoMap, ep.getOldOrInsertedNode(), true);
          }
          break;
        case INSERT:
        case STATEMENT_INSERT:
        case METHOD_INSERT:
        case VARIABLE_INSERT:
        case FINAL_INSERT:
          parent = ep.getUnchangedOrNewParentNode();
          oldParent = extDiff.secondToFirstMap.get(parent);
          updateModInfo(modInfoMap, oldParent, true);
          updateModInfo(modInfoMap, parent, true);
          updateModInfo(modInfoMap, oldParent, getOldListNumber(ep),
              ep.getOldOrChangedIndex().childrenListIndex, true);
          updateModInfo(modInfoMap, parent, getNewListNumber(ep),
              ep.getNewOrChangedIndex().childrenListIndex, true);
          break;

        case METHOD_DELETE:
          parent = ep.getUnchangedOrNewParentNode();
          oldParent = extDiff.secondToFirstMap.get(parent);
          updateModInfo(modInfoMap, oldParent, true);
          updateModInfo(modInfoMap, parent, true);
          break;
        case STATEMENT_DELETE:
        case VARIABLE_DELETE:

          parent = ep.getUnchangedOrNewParentNode();

          oldParent = extDiff.firstToSecondMap.get(parent);
          updateModInfo(modInfoMap, oldParent, true);
          updateModInfo(modInfoMap, parent, true);
          updateModInfo(modInfoMap, oldParent, getOldListNumber(ep),
              ep.getOldOrChangedIndex().childrenListIndex, true);
          updateModInfo(modInfoMap, parent, getNewListNumber(ep),
              ep.getNewOrChangedIndex().childrenListIndex, true);
          break;
        case ALIGN:
        case STATEMENT_REORDERING:
          if (useAlignment) {
            oldParent = ep.getUnchangedOrOldParentNode();
            parent = extDiff.firstToSecondMap.get(oldParent);
            updateModInfo(modInfoMap, oldParent, true);
            updateModInfo(modInfoMap, parent, true);
          }
          break;
        case MOVE:
        case STATEMENT_PARENT_CHANGE:
          oldParent = ep.getUnchangedOrOldParentNode();
          parent = ep.getUnchangedOrNewParentNode();
          updateModInfo(modInfoMap, oldParent, true);
          updateModInfo(modInfoMap, parent, true);
          updateModInfo(modInfoMap, oldParent, getOldListNumber(ep),
              ep.getOldOrChangedIndex().childrenListIndex, true);
          updateModInfo(modInfoMap, parent, getNewListNumber(ep),
              ep.getNewOrChangedIndex().childrenListIndex, true);
          break;
        case UPDATE:
        case STATEMENT_UPDATE:
        case VARIABLE_TYPE_UPDATE:
          if (!considerVarRename || !ignoreOpBb[index]) {
            updateModInfo(modInfoMap, ep.getOldOrInsertedNode(), true);
            updateModInfo(modInfoMap, ep.getNewOrChangedNode(), true);
          }
          break;
        case DELETE:
        case FINAL_DELETE:
        case FIELD_DELETE:

          modInfo = modInfoMap.get(ep.getOldOrInsertedNode());
          if (modInfo == null) {
            modInfo = new ModificationInformation();
            modInfoMap.put(ep.getOldOrInsertedNode(), modInfo);
          }
          modInfo.nodeModified = true;
          oldParent = (AbstractBastNode) ep.getUnchangedOrOldParentNode();
          parent = extDiff.firstToSecondMap.get(parent);
          updateModInfo(modInfoMap, oldParent, true);
          updateModInfo(modInfoMap, parent, true);
          break;
        case INCREASING_ACCESS:
        case DECREASING_ACCESS:
        case PARENT_CLASS_INSERT:
        case PARENT_CLASS_DELETE:
        case PARAMETER_DELETE:
        case PARAMETER_INSERT:
        case PARAMETER_REORDERING:
        case PARAMETER_TYPE_UPDATE:
        case FIELD_INSERT:
        case FIELD_RENAME:
        case FIELD_TYPE_UPDATE:
        case RETURN_TYPE_UPDATE:

          break;

        default:
          if (DEBUG) {
            System.err.println(ep.getClass().toString());
          }
          assert (false);
          break;
      }
      index++;
    }
    return modInfoMap;
  }

  private static MatchBoundary createMatchBoundaries(boolean modifiedVersion,
      ExtendedDiffResult original1modified1, ExtendedDiffResult original2modified2,
      ExtendedDiffResult original1original2, ExtendedDiffResult original2original1,
      AbstractBastNode original1Root, AbstractBastNode original2Root,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyB1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyB2, MatchBoundary boundaryBb,
      ExtendedDiffResult modified1tomodified2, int subChangeDepth) {
    HashMap<AbstractBastNode, ModificationInformation> modifiedNodesBA1 =
        getModifiedNodes(original1modified1.editScript, original1modified1, false, true);
    HashMap<AbstractBastNode, ModificationInformation> modifiedNodesBA2 =
        getModifiedNodes(original2modified2.editScript, original2modified2, false, true);
    if (modifiedVersion) {
      CollectMatchedNodesInBoundaryVisitor cmv = new CollectMatchedNodesInBoundaryVisitor(
          boundaryBb, original1original2, original1modified1, original2modified2);
      boundaryBb.getNode1().accept(cmv);
      boundaryBb.getNode2().accept(cmv);
      modifiedNodesBA1.putAll(cmv.modMap);
      modifiedNodesBA2.putAll(cmv.modMap);
    }
    FindModificationsVisitor fmsv = new FindModificationsVisitor(modifiedNodesBA1);
    original1Root.accept(fmsv);
    HashMap<AbstractBastNode, ModificationInformation> modMapOD1 = null;
    if (modifiedVersion) {
      modMapOD1 = fmsv.modMap;
    } else {
      modMapOD1 = modifiedNodesBA1;
    }
    FindModScopeVisitor msv1 = new FindModScopeVisitor(modMapOD1);
    original1Root.accept(msv1);
    fmsv = new FindModificationsVisitor(modifiedNodesBA2);
    original2Root.accept(fmsv);
    HashMap<AbstractBastNode, ModificationInformation> modMapOD2 = null;
    if (modifiedVersion) {
      modMapOD2 = fmsv.modMap;
    } else {
      modMapOD2 = modifiedNodesBA2;
    }
    FindModScopeVisitor msv2 = new FindModScopeVisitor(modMapOD2);
    original2Root.accept(msv2);
    AbstractBastNode originalRootNode = null;
    BastFieldConstants field1 = null;
    BastFieldConstants field2 = null;
    originalRootNode = msv1.topNode;
    AbstractBastNode original2RootNode = null;
    if (originalRootNode != null && (originalRootNode.getTag() == BastClassDecl.TAG
        || originalRootNode.getTag() == BastProgram.TAG)) {
      FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(BastFunction.TAG);
      original1Root.accept(fnft);
      if (!fnft.nodes.isEmpty()) {
        originalRootNode = fnft.nodes.get(0);
        field1 = BastFieldConstants.FUNCTION_BLOCK_STATEMENTS;
      }
    } else {
      HashMap<AbstractBastNode, ModificationInformation> modifiedNodesBb =
          getModifiedNodes(original1original2.editScript, original1original2, true, true);

      fmsv = new FindModificationsVisitor(modifiedNodesBb);
      original1Root.accept(fmsv);
      FindModScopeVisitor msvBb = new FindModScopeVisitor(modifiedNodesBb);
      original1Root.accept(msvBb);

      if (msvBb.topNode != null && msvBb.topNode.equals(msv1.topNode)) {
        if (msvBb.field == msv1.field) {
          field1 = msvBb.field;
          originalRootNode = msvBb.topNode;
        } else {
          assert (false);
        }
      } else {
        originalRootNode = null;
        NodeParentInformationHierarchy npi = hierarchyB1.get(msvBb.topNode);
        if (npi != null) {
          for (int i = 0; i < npi.list.size(); i++) {
            if (npi.list.get(i).parent.equals(msv1.topNode)) {
              originalRootNode = msv1.topNode;
              field1 = npi.list.get(i).fieldConstant;
              break;

            }
          }
        }

      }
      if (originalRootNode == null) {
        FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(BastFunction.TAG);
        original1Root.accept(fnft);
        if (!fnft.nodes.isEmpty()) {
          originalRootNode = fnft.nodes.get(0);
          field1 = BastFieldConstants.FUNCTION_BLOCK_STATEMENTS;
        }
      }

      HashMap<AbstractBastNode, ModificationInformation> modifiedNodesBbReversed =
          getModifiedNodes(original2original1.editScript, original2original1, true, true);

      fmsv = new FindModificationsVisitor(modifiedNodesBbReversed);
      original2Root.accept(fmsv);
      FindModScopeVisitor msvBbReversed = new FindModScopeVisitor(modifiedNodesBbReversed);
      original2Root.accept(msvBbReversed);
      if (msvBbReversed.topNode != null && msvBbReversed.topNode.equals(msv2.topNode)) {
        if (msvBbReversed.field == msv2.field) {
          field2 = msvBbReversed.field;
          original2RootNode = msvBbReversed.topNode;

        } else {
          assert (false);
        }
      } else {
        NodeParentInformationHierarchy npi = hierarchyB2.get(msvBbReversed.topNode);
        if (npi != null) {
          for (int i = 0; i < npi.list.size(); i++) {
            if (npi.list.get(i).parent.equals(msv2.topNode)) {
              original2RootNode = msv2.topNode;
              field2 = npi.list.get(i).fieldConstant;
              break;

            }
          }
        }

      }
      if (original2RootNode == null) {
        original2RootNode = msv2.topNode;
        if (original2RootNode == null) {
          FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(BastFunction.TAG);
          original2Root.accept(fnft);
          if (!fnft.nodes.isEmpty()) {
            original2RootNode = fnft.nodes.get(0);
            field2 = BastFieldConstants.FUNCTION_BLOCK_STATEMENTS;

          }
        } else {
          field2 = msv2.field;
        }
      }
      if (original2RootNode == null) {
        assert (false);
      } else if (original2RootNode.getTag() != BastBlock.TAG) {
        NodeParentInformationHierarchy npi = hierarchyB2.get(original2RootNode);
        for (NodeParentInformation np : npi.list) {
          if (np.parent.getTag() == BastBlock.TAG) {
            original2RootNode = np.parent;
            field2 = BastFieldConstants.BLOCK_STATEMENT;
            break;
          } else if (np.parent.getTag() == BastFunction.TAG) {
            original2RootNode = np.parent;
            field2 = BastFieldConstants.FUNCTION_BLOCK_STATEMENTS;
            break;
          }
        }
        if (original2RootNode.getTag() != BastBlock.TAG
            && original2RootNode.getTag() != BastFunction.TAG) {
          assert (false);
        }
      }
    }

    assert (field1 != null);
    assert (originalRootNode != null);
    assert (field2 != null);
    assert (original2RootNode != null);
    boolean update = false;
    if (subChangeDepth > 0) {
      AbstractBastNode node = originalRootNode;
      node = getSubRoot(subChangeDepth, modMapOD1, node);
      boolean done = false;
      if (node != null) {
        if (node != originalRootNode) {
          AbstractBastNode partner = original1original2.firstToSecondMap.get(node);
          if (partner != null) {
            LinkedList<? extends AbstractBastNode> partnerList =
                partner.fieldMap.get(BastFieldConstants.BLOCK_STATEMENT).getListField();
            LinkedList<? extends AbstractBastNode> nodeList =
                node.fieldMap.get(BastFieldConstants.BLOCK_STATEMENT).getListField();
            if (partnerList.size() != nodeList.size()) {
              boolean found = false;
              for (AbstractBastNode n : nodeList) {
                if (original1original2.firstToSecondMap.get(n) != null
                    && partnerList.contains(original1original2.firstToSecondMap.get(n))) {
                  if (WildcardAccessHelper.isEqual(n, original1original2.firstToSecondMap.get(n))) {
                    found = true;
                    break;
                  }
                }
              }
              if (!found) {
                FindNodesFromTagVisitor visitor = new FindNodesFromTagVisitor(BastBlock.TAG);
                partner.accept(visitor);
                for (AbstractBastNode viNode : visitor.nodes) {
                  LinkedList<? extends AbstractBastNode> localList =
                      viNode.fieldMap.get(BastFieldConstants.BLOCK_STATEMENT).getListField();
                  for (AbstractBastNode n : nodeList) {
                    if (original1original2.firstToSecondMap.get(n) != null
                        && localList.contains(original1original2.firstToSecondMap.get(n))) {
                      if (WildcardAccessHelper.isEqual(n,
                          original1original2.firstToSecondMap.get(n))) {
                        found = true;
                        partner = viNode;
                        break;
                      }
                    }
                  }
                  if (found) {
                    break;
                  }
                }
              }
            }
            original2RootNode = partner;
            originalRootNode = node;
            field1 = BastFieldConstants.BLOCK_STATEMENT;
            field2 = BastFieldConstants.BLOCK_STATEMENT;
            done = true;
          }
        }
      }
      if (!done) {
        node = original2RootNode;
        node = getSubRoot(subChangeDepth, modMapOD2, node);
        if (node != null) {
          original2RootNode = node;
          field2 = BastFieldConstants.BLOCK_STATEMENT;
        }
      }
    } else {
      if (isBlock(original2RootNode) && ((BastBlock) original2RootNode).statements.size() == 1
          && ((BastBlock) original2RootNode).statements.getFirst().getTag() == AresBlock.TAG) {
        AbstractBastNode tmp =
            ((AresBlock) ((BastBlock) original2RootNode).statements.getFirst()).block;
        if (original1original2.secondToFirstMap.get(tmp) != null) {
          if (originalRootNode != original1original2.secondToFirstMap.get(tmp)) {
            HashMap<AbstractBastNode, Integer> map = new HashMap<>();
            for (AbstractBastNode stmt : ((BastBlock) tmp).statements) {
              AbstractBastNode partner = original1original2.secondToFirstMap.get(stmt);
              if (partner != null) {
                NodeParentInformationHierarchy npi = hierarchyB1.get(partner);
                if (npi != null && npi.list.size() > 0) {
                  AbstractBastNode partnerParent = npi.list.get(0).parent;
                  Integer value = map.get(partnerParent);
                  if (value == null) {
                    map.put(partnerParent, 1);
                  } else {
                    map.put(partnerParent, value + 1);
                  }
                }
              }
            }
            int max = Integer.MIN_VALUE;
            boolean duplicate = false;
            AbstractBastNode possiblePartner = null;
            for (Entry<AbstractBastNode, Integer> entry : map.entrySet()) {
              if (entry.getValue() > max) {
                max = entry.getValue();
                possiblePartner = entry.getKey();
                duplicate = false;
              } else if (entry.getValue() == max) {
                duplicate = true;
              }
            }
            original2RootNode = tmp;
            if (!duplicate && possiblePartner != null && isBlock(possiblePartner)
                && !modifiedVersion) {
              originalRootNode = possiblePartner;
            } else {
              originalRootNode = original1original2.secondToFirstMap.get(tmp);
            }
            field1 = BastFieldConstants.BLOCK_STATEMENT;
            field2 = BastFieldConstants.BLOCK_STATEMENT;
          } else if (((BastBlock) originalRootNode).statements.size() < ((BastBlock) tmp).statements
              .size() && !modifiedVersion) {

            HashMap<AbstractBastNode, Integer> map = new HashMap<>();
            for (AbstractBastNode stmt : ((BastBlock) tmp).statements) {
              AbstractBastNode partner = original1original2.secondToFirstMap.get(stmt);
              if (partner != null) {
                NodeParentInformationHierarchy npi = hierarchyB1.get(partner);
                if (npi != null && npi.list.size() > 0) {
                  AbstractBastNode partnerParent = npi.list.get(0).parent;
                  Integer value = map.get(partnerParent);
                  if (value == null) {
                    map.put(partnerParent, 1);
                  } else {
                    map.put(partnerParent, value + 1);
                  }
                }
              }
            }
            int max = Integer.MIN_VALUE;
            boolean duplicate = false;
            AbstractBastNode possiblePartner = null;
            for (Entry<AbstractBastNode, Integer> entry : map.entrySet()) {
              if (entry.getValue() > max) {
                max = entry.getValue();
                possiblePartner = entry.getKey();
                duplicate = false;
              } else if (entry.getValue() == max) {
                duplicate = true;
              }
            }
            if (!duplicate && possiblePartner != null && possiblePartner != originalRootNode
                && isBlock(possiblePartner)
                && (map.get(originalRootNode) == null || map.get(originalRootNode) == 0)) {
              original2RootNode = tmp;
              originalRootNode = possiblePartner;
              field1 = BastFieldConstants.BLOCK_STATEMENT;
              field2 = BastFieldConstants.BLOCK_STATEMENT;
            }
          }
        }
      } else {
        FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(AresBlock.TAG);
        original2Root.accept(fnft);
        FindNodesFromTagVisitor fnft2 = new FindNodesFromTagVisitor(AresUseStmt.TAG);
        original2Root.accept(fnft2);
        FindNodesFromTagVisitor fnft3 = new FindNodesFromTagVisitor(AresWildcard.TAG);
        original2Root.accept(fnft3);

        if (fnft.nodes.size() == 0 && fnft2.nodes.size() == 0 && fnft3.nodes.size() == 0
            && isBlock(originalRootNode)) {
          LinkedList<? extends AbstractBastNode> partnerList1 =
              originalRootNode.fieldMap.get(BastFieldConstants.BLOCK_STATEMENT).getListField();
          HashMap<AbstractBastNode, Integer> matchMap = new HashMap<>();
          for (AbstractBastNode node : partnerList1) {
            AbstractBastNode possiblePartner = original1original2.firstToSecondMap.get(node);
            if (possiblePartner != null) {
              NodeParentInformationHierarchy npi = hierarchyB2.get(possiblePartner);
              if (npi.list != null && npi.list.size() > 0) {
                possiblePartner = npi.list.get(0).parent;
              }
              if (possiblePartner != null) {
                Integer value = matchMap.get(possiblePartner);
                if (value == null) {
                  matchMap.put(possiblePartner, 1);
                } else {
                  matchMap.put(possiblePartner, value + 1);
                }
              }
            }
          }
          int max = Integer.MIN_VALUE;
          boolean duplicate = false;
          AbstractBastNode partner = null;
          for (Entry<AbstractBastNode, Integer> entry : matchMap.entrySet()) {
            if (entry.getValue() > max) {
              max = entry.getValue();
              duplicate = false;
              partner = entry.getKey();
            } else if (entry.getValue() == max) {
              duplicate = true;
            }
          }
          if (!duplicate && partner != null) {
            if (partner != original2RootNode) {
              AbstractBastNode subRoot = getSubRoot(2, modMapOD2, original2Root);
              if (subRoot == partner) {
                original2RootNode = subRoot;
                update = true;
              }
            }
          }
        }
      }
    }
    if (isBlock(original2RootNode) && ((BastBlock) original2RootNode).statements.size() == 1
        && ((BastBlock) original2RootNode).statements.getFirst().getTag() == AresBlock.TAG) {
      original2RootNode = ((AresBlock) ((BastBlock) original2RootNode).statements.getFirst()).block;
      field2 = BastFieldConstants.BLOCK_STATEMENT;
    }
    return new MatchBoundary(originalRootNode, original2RootNode, field1, field2, update);
  }

  private static AbstractBastNode getSubRoot(int subChangeDepth,
      HashMap<AbstractBastNode, ModificationInformation> modMapOD1, AbstractBastNode node) {
    boolean done = false;
    int currentDepth = 0;
    while (!done) {
      ModificationInformation modInfo = modMapOD1.get(node);
      if (!modInfo.nodeModified && modInfo.fieldLowerBorder.size() == 1) {
        Entry<BastFieldConstants, Integer> lowerEntry =
            modInfo.fieldLowerBorder.entrySet().iterator().next();
        Entry<BastFieldConstants, Integer> upperEntry =
            modInfo.fieldUpperBorder.entrySet().iterator().next();
        if (Math.abs(lowerEntry.getValue() - upperEntry.getValue()) == 1) {
          node = node.fieldMap.get(lowerEntry.getKey()).getListField().get(lowerEntry.getValue());
        } else if (lowerEntry.getValue() == 0 && upperEntry.getValue() == 0) {
          node = node.fieldMap.get(lowerEntry.getKey()).getField();
        } else {
          node = null;
        }
        if (node != null) {
          if (isBlock(node)) {
            if (currentDepth + 1 == subChangeDepth) {
              done = true;
            } else {
              currentDepth++;
            }
          }
        } else {
          done = true;
        }
      } else {
        node = null;
        done = true;
      }
    }
    return node;
  }

  private static ExtendedDiffResult combineDiff(ExtendedDiffResult extDiffComp,
      ExtendedDiffResult extDiffSmall) {
    HashMap<AbstractBastNode, AbstractBastNode> secondToFirstMap = new HashMap<>();
    HashMap<AbstractBastNode, AbstractBastNode> firstToSecondMap = new HashMap<>();
    secondToFirstMap.putAll(extDiffComp.secondToFirstMap);
    firstToSecondMap.putAll(extDiffComp.firstToSecondMap);
    for (Entry<AbstractBastNode, AbstractBastNode> nodeEntry : extDiffSmall.secondToFirstMap
        .entrySet()) {
      if (nodeEntry.getKey().getTag() != BastProgram.TAG
          && nodeEntry.getValue().getTag() != BastProgram.TAG) {
        secondToFirstMap.put(nodeEntry.getKey(), nodeEntry.getValue());
      }
    }
    for (Entry<AbstractBastNode, AbstractBastNode> nodeEntry : extDiffSmall.firstToSecondMap
        .entrySet()) {
      if (nodeEntry.getKey().getTag() != BastProgram.TAG
          && nodeEntry.getValue().getTag() != BastProgram.TAG) {
        firstToSecondMap.put(nodeEntry.getKey(), nodeEntry.getValue());
      }
    }

    ExtendedDiffResult extDiff =
        new ExtendedDiffResult(null, extDiffSmall.editScript, secondToFirstMap, firstToSecondMap);
    return extDiff;
  }

  /**
   * Prints the.
   *
   * @param node the node
   */
  public static void print(AbstractBastNode node) {
    if (DEBUG) {
      IPrettyPrinter printer = ParserFactory.getAresPrettyPrinter();
      if (node != null) {
        node.accept(printer);
      }
      System.out.println(printer.getBuffer().toString());
    }
  }

  /**
   * Clone.
   *
   * @param node the node
   * @return the abstract bast node
   */
  private static AbstractBastNode clone(AbstractBastNode node, AresExtension extension) {
    IPrettyPrinter printer = ParserFactory.getAresPrettyPrinter();
    node.accept(printer);
    IParser parser = ParserFactory.getParserInstance(extension);
    AbstractBastNode clone = null;
    try {
      clone = parser.parse(printer.getBuffer().toString().getBytes());
    } catch (Throwable e) {
      e.printStackTrace();
      clone = parser.parse(printer.getBuffer().toString().getBytes());
    }
    return clone;

  }

  /**
   * Generate example.
   *
   * @param example1Original the example1 original
   * @param example2Original the example2 original
   * @param example1Modified the example1 modified
   * @param example2Modified the example2 modified
   * @param executioner the executioner
   * @param innerExecutioner the inner executioner
   * @return the abstract bast node[]
   * @throws GeneralizationException the generalization exception
   */
  public static AbstractBastNode[] generatePattern(AbstractBastNode example1Original,
      AbstractBastNode example2Original, AbstractBastNode example1Modified,
      AbstractBastNode example2Modified, ExecutorService executioner,
      ExecutorService innerExecutioner, boolean allowSubChanges, AresMeasurement aresMeasurement)
      throws GeneralizationException {
    int depth = 0;
    AbstractBastNode[] tmp = null;
    while (depth < 3) {
      try {
        AbstractBastNode example1OriginalClone =
            clone(example1Original, AresExtension.WITH_ARES_EXTENSIONS);
        AbstractBastNode example2OriginalClone =
            clone(example2Original, AresExtension.WITH_ARES_EXTENSIONS);
        AbstractBastNode example1ModifiedClone =
            clone(example1Modified, AresExtension.WITH_ARES_EXTENSIONS);
        AbstractBastNode example2ModifiedClone =
            clone(example2Modified, AresExtension.WITH_ARES_EXTENSIONS);
        tmp = innerGenerateExample(example1OriginalClone, example2OriginalClone,
            example1ModifiedClone, example2ModifiedClone, executioner, depth, aresMeasurement);
        return tmp;
      } catch (GeneralizationException ex) {
        if (!allowSubChanges || depth >= 2) {
          throw ex;
        }
      }
      depth++;
    }
    return tmp;
  }



  private static AbstractBastNode[] innerGenerateExample(AbstractBastNode example1Original,
      AbstractBastNode example2Original, AbstractBastNode example1Modified,
      AbstractBastNode example2Modified, ExecutorService executioner, int subChangeDepth,
      AresMeasurement aresMeasurement) throws GeneralizationException {
    print(example1Original);
    print(example2Original);
    print(example1Modified);
    print(example2Modified);

    checkForMissingBodyChange(example1Original, example1Modified, executioner);
    checkForMissingBodyChange(example2Original, example2Modified, executioner);
    ConnectWildcardAndUseVisitor cwuv = new ConnectWildcardAndUseVisitor();

    GeneralizationParameter parameter =
        createParameter(example1Original, example2Original, example1Modified, example2Modified,
            cwuv, null, executioner, subChangeDepth, aresMeasurement);
    FindNodesFromTagVisitor fnftTmp = new FindNodesFromTagVisitor(BastFunction.TAG);
    example2Original.accept(fnftTmp);
    if (parameter.matchBoundaryOo.getNode2().getTag() == BastBlock.TAG
        && ((BastBlock) parameter.matchBoundaryOo.getNode2()).statements.size() > 0
        && (((BastBlock) parameter.matchBoundaryOo.getNode2()).statements.get(0)
            .getTag() == AresBlock.TAG)
        && parameter.hierarchyO1.get(parameter.parentOM1.getNode()) != null
        && parameter.hierarchyO1.get(parameter.parentOM1.getNode()).list.get(0).parent
            .getTag() != BastFunction.TAG) {

      AbstractBastNode tmpParent = parameter.parentOM1.getNode();
      NodeParentInformationHierarchy npi = null;
      boolean function = false;
      if (tmpParent.getTag() != BastBlock.TAG) {
        npi = parameter.hierarchyO1.get(tmpParent);
        for (int i = 0; i < npi.list.size(); i++) {
          tmpParent = npi.list.get(i).parent;
          if (isBlock(tmpParent)) {
            if (i + 1 < npi.list.size()) {
              if (npi.list.get(i + 1).parent.getTag() == BastFunction.TAG) {
                function = true;
              }
            }
            break;
          }
        }
      }

      AbstractBastNode partner = parameter.exDiffOM1.firstToSecondMap.get(tmpParent);

      if (!function && partner != null && isBlock(partner)
          && parameter.hierarchyM1.get(partner).list.get(1).parent.getTag() != BastFunction.TAG
          && parameter.exDiffOo.firstToSecondMap
              .get(parameter.hierarchyO1.get(tmpParent).list.get(0).parent) == null) {
        long timeTreeDiff = System.nanoTime();
        ExtendedDiffResult exDiffBbTmp =
            ExtendedTemplateExtractor.pipeline(null, tmpParent, example2Original, executioner);
        ExtendedDiffResult exDiffAaTmp =
            ExtendedTemplateExtractor.pipeline(null, partner, example2Modified, executioner);
        if (aresMeasurement != null) {
          aresMeasurement.timeTreeDifferencing.add(System.nanoTime() - timeTreeDiff);
        }
        if (exDiffBbTmp != null && exDiffAaTmp != null) {
          cwuv = new ConnectWildcardAndUseVisitor();

          parameter = createParameter(example1Original, example2Original, example1Modified,
              example2Modified, cwuv, parameter, executioner, subChangeDepth, aresMeasurement);
        }
      }
    } else if (parameter.matchBoundaryOo.update) {
      return handleSubChange(example1Original, example2Original, example1Modified, example2Modified,
          executioner, parameter, aresMeasurement);
    } else if (subChangeDepth == 0 && parameter.matchBoundaryOo.getNode2().getTag() == BastBlock.TAG
        && ((BastBlock) parameter.matchBoundaryOo.getNode2()).statements.size() > 0
        && (parameter.hierarchyO2.get(((BastBlock) parameter.matchBoundaryOo.getNode2())).list
            .get(0).parent.getTag() == AresBlock.TAG)
        && parameter.hierarchyM1.get(
            parameter.exDiffOM1.firstToSecondMap.get(parameter.matchBoundaryOo.getNode1())) != null
        && parameter.hierarchyO1.get(parameter.matchBoundaryOo.getNode1()) != null
        && (parameter.hierarchyO1.get(parameter.matchBoundaryOo.getNode1()).list.get(0).parent
            .getTag() != BastFunction.TAG
            || (parameter.exDiffOM1.firstToSecondMap
                .get(parameter.matchBoundaryOo.getNode1()) != parameter.matchBoundaryMm.getNode1()

                && parameter.hierarchyM1.get(parameter.exDiffOM1.firstToSecondMap
                    .get(parameter.matchBoundaryOo.getNode1())).list.size() > 0
                && parameter.hierarchyM1.get(parameter.exDiffOM1.firstToSecondMap
                    .get(parameter.matchBoundaryOo.getNode1())).list.get(0).parent
                        .getTag() != BastFunction.TAG))
        && parameter.hierarchyO1.get(parameter.matchBoundaryOo.getNode1()).list.size() > 1
        && parameter.hierarchyO1.get(parameter.matchBoundaryOo.getNode1()).list.get(1).parent
            .getTag() != BastFunction.TAG) {
      return handleAresChange(example1Original, example2Original, example1Modified,
          example2Modified, executioner, parameter, aresMeasurement);
    } else if (parameter.matchBoundaryOo.getNode2().getTag() == BastBlock.TAG
        && ((BastBlock) parameter.matchBoundaryOo.getNode2()).statements.size() > 0
        && subChangeDepth != Integer.MIN_VALUE
        && parameter.hierarchyO2.get(((BastBlock) parameter.matchBoundaryOo.getNode2())).list
            .get(0).parent.getTag() == BastFunction.TAG
        && parameter.hierarchyO1.get(parameter.parentOM1.getNode()) != null
        && parameter.hierarchyO1.get(parameter.parentOM1.getNode()).list.get(0).parent
            .getTag() != BastFunction.TAG
        && parameter.hierarchyO1.get(parameter.parentOM1.getNode()).list.size() > 2
        && parameter.hierarchyO1.get(parameter.parentOM1.getNode()).list.get(2).parent
            .getTag() != BastFunction.TAG) {

      AbstractBastNode tmpParentB1 =
          parameter.hierarchyO1.get(parameter.parentOM1.getNode()).list.get(0).parent;
      parameter.parentOM2 = findParentHierarchy(parameter.exDiffOM2, parameter.hierarchyO2,
          parameter.hierarchyM2, parameter.insideMethodOM2, example1Original);
      if (parameter.parentOM2 == null) {
        IPrettyPrinter printer = ParserFactory.getPrettyPrinter();
        if (example1Modified != null) {
          example1Modified.accept(printer);
        }
      }
      if (parameter.parentOM2 != null && parameter.parentOM2.list != null
          && parameter.parentOM2.list.size() > 0) {
        if (parameter.parentOM2.list.get(0).parent.getTag() == tmpParentB1.getTag()) {
          NodeParentInformationHierarchy npiB1 = parameter.hierarchyO1.get(tmpParentB1);
          NodeParentInformationHierarchy npiB2 =
              parameter.hierarchyO2.get(parameter.parentOM2.list.get(0).parent);
          AbstractBastNode replaceParentB1 = npiB1.list.get(0).parent;
          AbstractBastNode replaceParentB2 = npiB2.list.get(0).parent;
          if (replaceParentB1.getTag() != replaceParentB2.getTag()) {
            if (npiB1.list.size() > 1
                && npiB1.list.get(1).parent.getTag() == replaceParentB2.getTag()) {
              replaceParentB1 = npiB1.list.get(1).parent;
            }
          }
          if (replaceParentB1.getTag() != BastFunction.TAG
              && replaceParentB2.getTag() != BastFunction.TAG) {

            BastFunction functionB1 = null;
            BastFunction functionB2 = null;
            for (NodeParentInformation np : npiB1.list) {
              if (np.parent.getTag() == BastFunction.TAG) {
                functionB1 = (BastFunction) np.parent;
                break;
              }
            }
            for (NodeParentInformation np : npiB2.list) {
              if (np.parent.getTag() == BastFunction.TAG) {
                functionB2 = (BastFunction) np.parent;
                break;
              }
            }
            if (replaceParentB1.getTag() != BastBlock.TAG) {
              for (NodeParentInformation np : npiB1.list) {
                switch (np.fieldConstant) {
                  case BLOCK_STATEMENT:
                  case CATCH_CLAUSE_BLOCK:
                  case FOR_STMT_STATEMENT:
                  case IF_ELSE_PART:
                  case IF_IF_PART:
                  case SWITCH_CASE_GROUP_STATEMENTS:
                  case SYNCHRONIZED_BLOCK_BLOCK:
                  case TRY_BLOCK:
                  case WHILE_STATEMENT:
                    if (np.fieldConstant.isList) {
                      replaceParentB1 =
                          np.parent.getField(np.fieldConstant).getListField().get(np.listId);
                    } else {
                      replaceParentB1 = np.parent.getField(np.fieldConstant).getField();
                    }
                    break;
                  default:
                    break;
                }
              }
            }
            if (replaceParentB2.getTag() != BastBlock.TAG) {

              for (NodeParentInformation np : npiB2.list) {
                switch (np.fieldConstant) {
                  case BLOCK_STATEMENT:
                  case CATCH_CLAUSE_BLOCK:
                  case FOR_STMT_STATEMENT:
                  case IF_ELSE_PART:
                  case IF_IF_PART:
                  case SWITCH_CASE_GROUP_STATEMENTS:
                  case SYNCHRONIZED_BLOCK_BLOCK:
                  case TRY_BLOCK:
                  case WHILE_STATEMENT:
                    if (np.fieldConstant.isList) {
                      replaceParentB2 =
                          np.parent.getField(np.fieldConstant).getListField().get(np.listId);
                    } else {
                      replaceParentB2 = np.parent.getField(np.fieldConstant).getField();
                    }
                    break;
                  default:
                    break;
                }
              }
            }
            boolean skipB1 = false;
            if (functionB1 != null && functionB1.statements.getFirst() != null
                && functionB1.statements.getFirst().getTag() == BastBlock.TAG) {
              BastBlock block = ((BastBlock) functionB1.statements.getFirst());
              if (block.statements != null && block.statements.size() == 1) {
                if (block.statements.getFirst() == replaceParentB1) {
                  skipB1 = true;
                }
              }
            }
            boolean skipB2 = false;
            if (functionB2 != null && functionB2.statements.getFirst() != null
                && functionB2.statements.getFirst().getTag() == BastBlock.TAG) {
              BastBlock block = ((BastBlock) functionB2.statements.getFirst());
              if (block.statements != null && block.statements.size() == 1) {
                if (block.statements.getFirst() == replaceParentB2) {
                  skipB2 = true;
                }
              }
              if (replaceParentB2 == block) {
                skipB2 = true;
              }
            }
            if (!(skipB1 && skipB2)) {
              AbstractBastNode replaceParentA1 =
                  parameter.exDiffOM1.firstToSecondMap.get(replaceParentB1);
              AbstractBastNode replaceParentA2 =
                  parameter.exDiffOM2.firstToSecondMap.get(replaceParentB2);
              if (replaceParentA1 != null && replaceParentA2 != null) {
                NodeParentInformationHierarchy npiA1 = parameter.hierarchyM1.get(replaceParentA1);
                NodeParentInformationHierarchy npiA2 = parameter.hierarchyM2.get(replaceParentA2);
                BastFunction functionA1 = null;
                BastFunction functionA2 = null;
                for (NodeParentInformation np : npiA1.list) {
                  if (np.parent.getTag() == BastFunction.TAG) {
                    functionA1 = (BastFunction) np.parent;
                    break;
                  }
                }
                for (NodeParentInformation np : npiA2.list) {
                  if (np.parent.getTag() == BastFunction.TAG) {
                    functionA2 = (BastFunction) np.parent;
                    break;
                  }
                }

                LinkedList<AbstractBastStatement> stmts = new LinkedList<>();
                stmts.add((AbstractBastStatement) replaceParentB1);
                if (replaceParentB1.info != null && replaceParentB1.info.tokens != null) {
                  if (replaceParentB1.info.tokens[replaceParentB1.info.tokens.length - 1] == null) {
                    replaceParentB1.info.tokens[replaceParentB1.info.tokens.length - 1] =
                        CreateJavaNodeHelper.createTokenHistory(BasicJavaToken.SEMICOLON);
                  }
                  if (replaceParentB1.info.tokens.length > 0
                      && replaceParentB1.info.tokens[0] != null
                      && replaceParentB1.info.tokens[0].prevTokens != null) {
                    replaceParentB1.info.tokens[0].prevTokens.clear();
                  }
                }
                if (replaceParentB1.getTag() != BastBlock.TAG) {
                  BastBlock block = CreateJavaNodeHelper.createBlock(null, stmts);
                  stmts = new LinkedList<AbstractBastStatement>();
                  stmts.add(block);
                }
                functionB1.replaceField(BastFieldConstants.FUNCTION_BLOCK_STATEMENTS,
                    new BastField(stmts));
                stmts = new LinkedList<>();
                stmts.add((AbstractBastStatement) replaceParentB2);
                if (replaceParentB2.info != null && replaceParentB2.info.tokens != null) {
                  if (replaceParentB2.info.tokens[replaceParentB2.info.tokens.length - 1] == null) {
                    replaceParentB2.info.tokens[replaceParentB2.info.tokens.length - 1] =
                        CreateJavaNodeHelper.createTokenHistory(BasicJavaToken.SEMICOLON);
                  }
                  if (replaceParentB2.info.tokens.length > 0
                      && replaceParentB2.info.tokens[0] != null
                      && replaceParentB2.info.tokens[0].prevTokens != null) {
                    replaceParentB2.info.tokens[0].prevTokens.clear();
                  }
                }
                if (replaceParentB2.getTag() != BastBlock.TAG) {
                  BastBlock block = CreateJavaNodeHelper.createBlock(null, stmts);
                  stmts = new LinkedList<AbstractBastStatement>();
                  stmts.add(block);
                }
                functionB2.replaceField(BastFieldConstants.FUNCTION_BLOCK_STATEMENTS,
                    new BastField(stmts));
                stmts = new LinkedList<>();
                stmts.add((AbstractBastStatement) replaceParentA1);
                if (replaceParentA1.info != null && replaceParentA1.info.tokens != null) {
                  if (replaceParentA1.info.tokens[replaceParentA1.info.tokens.length - 1] == null) {
                    replaceParentA1.info.tokens[replaceParentA1.info.tokens.length - 1] =
                        CreateJavaNodeHelper.createTokenHistory(BasicJavaToken.SEMICOLON);
                  }
                  if (replaceParentA1.info.tokens.length > 0
                      && replaceParentA1.info.tokens[0] != null
                      && replaceParentA1.info.tokens[0].prevTokens != null) {
                    replaceParentA1.info.tokens[0].prevTokens.clear();
                  }
                }
                if (replaceParentA1.getTag() != BastBlock.TAG) {
                  BastBlock block = CreateJavaNodeHelper.createBlock(null, stmts);
                  stmts = new LinkedList<AbstractBastStatement>();
                  stmts.add(block);
                }
                if (functionA1 != null) {
                  functionA1.replaceField(BastFieldConstants.FUNCTION_BLOCK_STATEMENTS,
                      new BastField(stmts));
                }
                stmts = new LinkedList<>();
                stmts.add((AbstractBastStatement) replaceParentA2);
                if (replaceParentA2.info != null && replaceParentA2.info.tokens != null) {
                  if (replaceParentA2.info.tokens[replaceParentA2.info.tokens.length - 1] == null) {
                    replaceParentA2.info.tokens[replaceParentA2.info.tokens.length - 1] =
                        CreateJavaNodeHelper.createTokenHistory(BasicJavaToken.SEMICOLON);
                  }
                  if (replaceParentA2.info.tokens.length > 0
                      && replaceParentA2.info.tokens[0] != null
                      && replaceParentA2.info.tokens[0].prevTokens != null) {
                    replaceParentA2.info.tokens[0].prevTokens.clear();
                  }
                }
                if (replaceParentA2.getTag() != BastBlock.TAG) {
                  BastBlock block = CreateJavaNodeHelper.createBlock(null, stmts);
                  stmts = new LinkedList<AbstractBastStatement>();
                  stmts.add(block);
                }
                if (functionA2 != null) {
                  functionA2.replaceField(BastFieldConstants.FUNCTION_BLOCK_STATEMENTS,
                      new BastField(stmts));
                }
                return innerGenerateExample(example1Original, example2Original, example1Modified,
                    example2Modified, executioner, Integer.MIN_VALUE, aresMeasurement);
              }
            }
          }
        }
      }
    } else if (subChangeDepth > 0 && parameter.matchBoundaryOo.getNode2().getTag() == BastBlock.TAG
        && ((BastBlock) parameter.matchBoundaryOo.getNode2()).statements.size() > 0
        && parameter.hierarchyO2.get(((BastBlock) parameter.matchBoundaryOo.getNode2())).list
            .get(0).parent.getTag() != BastFunction.TAG
        && parameter.matchBoundaryOo.getNode1().getTag() == BastBlock.TAG
        && ((BastBlock) parameter.matchBoundaryOo.getNode1()).statements.size() > 0
        && parameter.hierarchyO1.get(((BastBlock) parameter.matchBoundaryOo.getNode1())).list
            .get(0).parent.getTag() != BastFunction.TAG) {
      return handleSubChange(example1Original, example2Original, example1Modified, example2Modified,
          executioner, parameter, aresMeasurement);
    }
    print(example2Original);
    print(example2Modified);

    new MatchBoundary(parameter.matchBoundaryOo);
    new MatchBoundary(parameter.matchBoundaryMm);
    FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(AresBlock.TAG);
    example2Original.accept(fnft);
    boolean containsAresBlock = false;
    ConnectWildcardAndUseVisitor connectKeeper = new ConnectWildcardAndUseVisitor();
    example2Original.accept(connectKeeper);
    example2Modified.accept(connectKeeper);
    if (!fnft.nodes.isEmpty()) {
      parameter.parentOM2 = parameter.hierarchyO2.get(fnft.nodes.get(0));
      containsAresBlock = true;
    } else {
      parameter.parentOM2 = findParentHierarchy(parameter.exDiffOM2, parameter.hierarchyO2,
          parameter.hierarchyM2, parameter.insideMethodOM2, example2Original);
    }
    parameter.parentOo = findParentHierarchy(parameter.exDiffOo, parameter.hierarchyO1,
        parameter.hierarchyO2, parameter.insideMethodOo, example1Original);

    parameter.parentMm = findParentHierarchy(parameter.exDiffMm, parameter.hierarchyM1,
        parameter.hierarchyM2, parameter.insideMethodMm, example1Modified);

    assert (parameter.parentOM1 != null);

    assert (parameter.parentOo != null);
    assert (parameter.parentOM2 != null);
    HashMap<String, String> identifierMapBb = new HashMap<>();
    HashMap<String, String> identifierMapAa = new HashMap<>();
    parameter.ignoreOpOo =
        findConsistentVarRename(parameter.original1original2, identifierMapBb, parameter.exDiffOo);
    parameter.ignoreOpMm =
        findConsistentVarRename(parameter.modified1modified2, identifierMapAa, parameter.exDiffMm);
    HashMap<AbstractBastNode, AbstractBastNode> delInsertMap = new HashMap<>();
    ArrayList<AbstractBastNode> additionalWildcards = checkAresBlockMoves(parameter.exDiffOo);
    ArrayList<AbstractBastNode> additionalUses = null;
    if (additionalWildcards.size() > 0) {
      additionalUses = checkAresBlockMoves(parameter.exDiffMm);
    }
    print(example2Modified);
    List<BastEditOperation> workList =
        Filter.filterEditScript2(parameter, identifierMapBb, delInsertMap, null);
    HashMap<AbstractBastNode, List<RevertModificationInfo>> revertMap = new HashMap<>();
    HashMap<AbstractBastNode, BlockChanges> changeMap = new HashMap<>();
    final InsertResult originalRes = insertAndCombine(example1Original, example2Original, workList,
        parameter.exDiffOo, parameter.hierarchyO1, parameter.hierarchyO2, parameter.matchBoundaryOo,
        cwuv, delInsertMap, additionalWildcards, false, 0, revertMap, containsAresBlock, changeMap);
    HashMap<BastNameIdent, BastNameIdent> identMap = new HashMap<BastNameIdent, BastNameIdent>();


    for (Entry<AbstractBastNode, AbstractBastNode> entry : delInsertMap.entrySet()) {
      boolean addKey = true;
      for (BastEditOperation ep : workList) {
        if (ep.getType() == EditOperationType.DELETE) {
          NodeParentInformationHierarchy npi = parameter.hierarchyO1.get(entry.getKey());
          if (npi != null) {
            for (NodeParentInformation np : npi.list) {
              if (np.parent == ep.getOldOrInsertedNode()) {
                addKey = false;
              }
            }
          }
        }
      }
      if (!addKey) {
        continue;
      }
      if (entry != null && entry.getKey() != null && entry.getValue() != null) {
        if (entry.getKey().getTag() == BastNameIdent.TAG
            && entry.getValue().getTag() == BastNameIdent.TAG) {
          if (!((BastNameIdent) entry.getKey()).name
              .equals(((BastNameIdent) entry.getValue()).name)) {
            identMap.put((BastNameIdent) entry.getKey(), (BastNameIdent) entry.getValue());
          }
        } else if (entry.getKey().getTag() == BastIdentDeclarator.TAG
            && entry.getValue().getTag() == BastIdentDeclarator.TAG
            && ((BastIdentDeclarator) entry.getKey()).identifier.getTag() == BastNameIdent.TAG
            && ((BastIdentDeclarator) entry.getValue()).identifier.getTag() == BastNameIdent.TAG) {
          if (!((BastNameIdent) ((BastIdentDeclarator) entry.getKey()).identifier).name
              .equals(((BastNameIdent) ((BastIdentDeclarator) entry.getValue()).identifier).name)) {
            identMap.put((BastNameIdent) ((BastIdentDeclarator) entry.getKey()).identifier,
                ((BastNameIdent) ((BastIdentDeclarator) entry.getValue()).identifier));
          }
        }
      }
    }

    print(example2Original);

    AbstractBastNode tmp =
        (AbstractBastNode) parameter.exDiffOM2.firstToSecondMap.get(parameter.parentOM2.getNode());
    if (tmp == null) {
      FindNodesFromTagVisitor fnft2 = new FindNodesFromTagVisitor(BastFunction.TAG);
      example2Modified.accept(fnft2);
      if (!fnft2.nodes.isEmpty()) {
        tmp = (BastBlock) fnft2.nodes.get(0).getField(BastFieldConstants.FUNCTION_BLOCK_STATEMENTS)
            .getListField().get(0);
      }
    }
    final NodeParentInformationHierarchy parentAB2 = parameter.hierarchyM2.get(tmp);
    tmp =
        (AbstractBastNode) parameter.exDiffOM1.firstToSecondMap.get(parameter.parentOM1.getNode());
    if (tmp == null) {
      NodeParentInformationHierarchy parent =
          parameter.hierarchyO1.get(parameter.parentOM1.getNode());
      tmp = (AbstractBastNode) parameter.exDiffOM1.firstToSecondMap
          .get(parent.list.getFirst().parent);
      parameter.exDiffOM1.secondToFirstMap.entrySet().toArray();
      if (tmp == null) {
        if (parameter.parentOM1.list.get(0).parent.getTag() == BastFunction.TAG) {
          FindNodesFromTagVisitor fnft2 = new FindNodesFromTagVisitor(BastFunction.TAG);
          example1Modified.accept(fnft2);
          if (!fnft2.nodes.isEmpty()) {
            tmp = (BastBlock) fnft2.nodes.get(0)
                .getField(BastFieldConstants.FUNCTION_BLOCK_STATEMENTS).getListField().get(0);
          }
        } else {
          assert (false);
        }
      }
    }

    NodeParentInformationHierarchy parentAB1 = parameter.hierarchyM1.get(tmp);
    assert (parentAB1 != null);
    assert (parentAB2 != null);
    parameter.parentMO1 = parentAB1;
    parameter.parentMO2 = parentAB2;
    parameter.runType = ExecutionRunType.MODIFIED_RUN;
    List<BastEditOperation> deletedStmtsOutsideBoundary = new ArrayList<>();
    List<BastEditOperation> workListModified = Filter.filterEditScript2(parameter, identifierMapAa,
        delInsertMap, deletedStmtsOutsideBoundary);

    final InsertResult modifiedRes = insertAndCombine(example1Modified, example2Modified,
        workListModified, parameter.exDiffMm, parameter.hierarchyM1, parameter.hierarchyM2,
        parameter.matchBoundaryMm, cwuv, delInsertMap, additionalUses, true,
        deletedStmtsOutsideBoundary.size(), revertMap, containsAresBlock, changeMap);
    print(example2Modified);
    if (fnft.nodes.isEmpty()) {
      example2Original = insertAresBlock(parameter.matchBoundaryOo,
          parameter.hierarchyO1.get(parameter.matchBoundaryOo.getNode1()), example2Original,
          parameter.parentOM2, true);
      example2Modified = insertAresBlock(parameter.matchBoundaryMm,
          parameter.hierarchyM1.get(parameter.matchBoundaryMm.getNode2()), example2Modified,
          parentAB2, false);
    } else {
      print(example2Original);
      print(example2Modified);
      print(example2Original);
      print(example2Modified);

    }

    print(example2Original);
    print(example2Modified);

    WildcardChoiceHandler.handleWildcards(example2Original, example2Modified, parameter.exDiffOo,
        parameter.exDiffMm, parameter.exDiffOM1, parameter.exDiffOM2, cwuv, parameter.hierarchyM1,
        parameter.hierarchyM2, delInsertMap, revertMap, originalRes, modifiedRes,
        parameter.matchBoundaryMm, connectKeeper);
    updateIdentifierMap(example2Original, identMap);
    AbstractBastNode[] generalizedPattern = new AbstractBastNode[2];
    generalizedPattern[0] = example2Original;
    generalizedPattern[1] = example2Modified;
    checkForOvergeneralization(example2Original, example2Modified, parameter.exDiffOM1,
        parameter.exDiffOM2, parameter, executioner);

    return generalizedPattern;
  }

  private static AbstractBastNode[] handleAresChange(AbstractBastNode example1Original,
      AbstractBastNode example2Original, AbstractBastNode example1Modified,
      AbstractBastNode example2Modified, ExecutorService executioner,
      GeneralizationParameter parameter, AresMeasurement aresMeasurement)
      throws GeneralizationException {
    BastFunction functionB1 = null;
    NodeParentInformationHierarchy npiB1 =
        parameter.hierarchyO1.get(parameter.matchBoundaryOo.getNode1());
    for (NodeParentInformation np : npiB1.list) {
      if (np.parent.getTag() == BastFunction.TAG) {
        functionB1 = (BastFunction) np.parent;
        break;
      }
    }
    AbstractBastNode partnerA1 =
        parameter.exDiffOM1.firstToSecondMap.get(parameter.matchBoundaryOo.getNode1());
    if (partnerA1 != null) {
      BastFunction functionA1 = null;
      NodeParentInformationHierarchy npiA1 = parameter.hierarchyM1.get(partnerA1);
      for (NodeParentInformation np : npiA1.list) {
        if (np.parent.getTag() == BastFunction.TAG) {
          functionA1 = (BastFunction) np.parent;
          break;
        }
      }

      if (functionB1 != null && functionA1 != null) {
        replaceWithSubChange(parameter.matchBoundaryOo.getNode1(), functionB1);
        replaceWithSubChange(partnerA1, functionA1);
        return innerGenerateExample(example1Original, example2Original, example1Modified,
            example2Modified, executioner, 0, aresMeasurement);
      }
    }
    return null;
  }

  private static AbstractBastNode[] handleSubChange(AbstractBastNode example1Original,
      AbstractBastNode example2Original, AbstractBastNode example1Modified,
      AbstractBastNode example2Modified, ExecutorService executioner,
      GeneralizationParameter parameter, AresMeasurement aresMeasurement)
      throws GeneralizationException {
    BastFunction functionB1 = null;
    BastFunction functionB2 = null;
    NodeParentInformationHierarchy npiB1 =
        parameter.hierarchyO1.get(parameter.matchBoundaryOo.getNode1());
    NodeParentInformationHierarchy npiB2 =
        parameter.hierarchyO2.get(parameter.matchBoundaryOo.getNode2());
    NodeParentInformationHierarchy npiTmpA1 =
        parameter.hierarchyM1.get(parameter.matchBoundaryMm.getNode1());
    NodeParentInformationHierarchy npiTmpA2 =
        parameter.hierarchyM2.get(parameter.matchBoundaryMm.getNode2());
    for (NodeParentInformation np : npiB1.list) {
      if (np.parent.getTag() == BastFunction.TAG) {
        functionB1 = (BastFunction) np.parent;
        break;
      }
    }
    for (NodeParentInformation np : npiB2.list) {
      if (np.parent.getTag() == BastFunction.TAG) {
        functionB2 = (BastFunction) np.parent;
        break;
      }
    }
    AbstractBastNode partnerA1 =
        parameter.exDiffOM1.firstToSecondMap.get(parameter.matchBoundaryOo.getNode1());
    AbstractBastNode partnerA2 =
        parameter.exDiffOM2.firstToSecondMap.get(parameter.matchBoundaryOo.getNode2());

    if (npiTmpA1 != null && npiTmpA2 != null) {
      if (npiB1.list.get(0).parent.getTag() != npiTmpA1.list.get(0).parent.getTag()) {
        AbstractBastNode newPartnerParent =
            parameter.exDiffOM1.firstToSecondMap.get(npiB1.list.get(0).parent);
        if (newPartnerParent != null) {
          AbstractBastNode newPartner = WildcardAccessHelper.getNodeToIndex(newPartnerParent,
              npiB1.list.get(0).fieldConstant, npiB1.list.get(0).listId);
          if (newPartner != null) {
            partnerA1 = newPartner;
          }
        }
      }
      if (npiB2.list.get(0).parent.getTag() != npiTmpA2.list.get(0).parent.getTag()) {
        AbstractBastNode newPartnerParent =
            parameter.exDiffOM2.firstToSecondMap.get(npiB2.list.get(0).parent);
        if (newPartnerParent != null) {
          AbstractBastNode newPartner = WildcardAccessHelper.getNodeToIndex(newPartnerParent,
              npiB2.list.get(0).fieldConstant, npiB2.list.get(0).listId);
          if (newPartner != null) {
            partnerA2 = newPartner;
          }
        }
      }
    }

    if (partnerA1 != null && partnerA2 != null) {
      BastFunction functionA1 = null;
      BastFunction functionA2 = null;
      NodeParentInformationHierarchy npiA1 = parameter.hierarchyM1.get(partnerA1);
      NodeParentInformationHierarchy npiA2 = parameter.hierarchyM2.get(partnerA2);
      for (NodeParentInformation np : npiA1.list) {
        if (np.parent.getTag() == BastFunction.TAG) {
          functionA1 = (BastFunction) np.parent;
          break;
        }
      }
      for (NodeParentInformation np : npiA2.list) {
        if (np.parent.getTag() == BastFunction.TAG) {
          functionA2 = (BastFunction) np.parent;
          break;
        }
      }

      if (functionB1 != null && functionB2 != null && functionA1 != null && functionA2 != null) {
        replaceWithSubChange(parameter.matchBoundaryOo.getNode1(), functionB1);
        replaceWithSubChange(parameter.matchBoundaryOo.getNode2(), functionB2);
        replaceWithSubChange(partnerA1, functionA1);
        replaceWithSubChange(partnerA2, functionA2);
        return innerGenerateExample(example1Original, example2Original, example1Modified,
            example2Modified, executioner, 0, aresMeasurement);
      }
    }
    return null;
  }

  private static void replaceWithSubChange(AbstractBastNode subChange, BastFunction functionB1) {
    @SuppressWarnings("unchecked")
    LinkedList<AbstractBastStatement> stmtBorder = (LinkedList<AbstractBastStatement>) subChange
        .getField(BastFieldConstants.BLOCK_STATEMENT).getListField();
    StringBuffer bufferTmp = extractIndentation(stmtBorder, stmtBorder.size() - 1);
    @SuppressWarnings("unchecked")
    LinkedList<AbstractBastStatement> previousFunctionBlock =
        (LinkedList<AbstractBastStatement>) functionB1
            .getField(BastFieldConstants.FUNCTION_BLOCK_STATEMENTS).getListField();
    @SuppressWarnings("unchecked")
    LinkedList<AbstractBastStatement> stmtFunctionBlock =
        (LinkedList<AbstractBastStatement>) previousFunctionBlock.getFirst()
            .getField(BastFieldConstants.BLOCK_STATEMENT).getListField();

    StringBuffer bufferTmp2 = extractIndentation(stmtFunctionBlock, stmtFunctionBlock.size() - 1);
    int returnIndex = bufferTmp.toString().lastIndexOf('\n');
    if (returnIndex != -1) {
      String innerIndentation = bufferTmp.substring(returnIndex);
      int returnIndex2 = bufferTmp2.toString().lastIndexOf('\n');
      String outerIndentation = bufferTmp2.substring(returnIndex2);
      innerIndentation = innerIndentation.replace(outerIndentation, "");
      int lastIndex = bufferTmp.lastIndexOf(innerIndentation);
      String result = bufferTmp.substring(lastIndex);
      CorrectIndentationVisitor civ = new CorrectIndentationVisitor(result);
      functionB1.accept(civ);
      LinkedList<AbstractBastStatement> stmts = new LinkedList<AbstractBastStatement>();
      stmts.add((AbstractBastStatement) subChange);
      functionB1.replaceField(BastFieldConstants.FUNCTION_BLOCK_STATEMENTS, new BastField(stmts));
    }
  }

  private static void updateIdentifierMap(AbstractBastNode example2Original,
      HashMap<BastNameIdent, BastNameIdent> identMap) {
    FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(AresBlock.TAG);
    example2Original.accept(fnft);
    if (fnft.nodes.size() != 1) {
      return;
    }
    AresBlock block = (AresBlock) fnft.nodes.get(0);
    if (identMap.size() == 0 && block.identifiers == null) {
      return;
    }
    HashSet<String> possibleStrings = new HashSet<>();
    if (block.identifiers != null) {
      for (AbstractBastExpr expr : block.identifiers) {
        if (expr.getTag() == BastNameIdent.TAG) {
          possibleStrings.add(((BastNameIdent) expr).name);
        }
      }
    }
    for (Entry<BastNameIdent, BastNameIdent> entry : identMap.entrySet()) {
      possibleStrings.add(((BastNameIdent) entry.getValue()).name);
    }
    fnft = new FindNodesFromTagVisitor(BastNameIdent.TAG);
    block.block.accept(fnft);
    Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy =
        ParentHierarchyHandler.getParentHierarchy(block.block);
    LinkedList<AbstractBastExpr> identifiers = new LinkedList<AbstractBastExpr>();
    outer: for (AbstractBastNode node : fnft.nodes) {
      BastNameIdent ident = (BastNameIdent) node;
      if (possibleStrings.contains(ident.name)) {
        NodeParentInformationHierarchy npi = hierarchy.get(ident);
        for (NodeParentInformation np : npi.list) {
          if (np.parent.getTag() == AresWildcard.TAG) {
            continue outer;
          }
          if (np.fieldConstant.isList) {
            if (np.parent.getTag() == BastBlock.TAG) {
              if (np.listId > 0) {
                AbstractBastNode nodeWithIdent =
                    WildcardAccessHelper.getNodeToIndex(np.parent, np.fieldConstant, np.listId);
                if (nodeWithIdent != null) {
                  for (int i = np.listId - 1; i >= 0; i--) {
                    AbstractBastNode possibleWildcard =
                        WildcardAccessHelper.getNodeToIndex(np.parent, np.fieldConstant, i);
                    AbstractBastNode expr = WildcardAccessHelper.getExpr(possibleWildcard);
                    if (expr != null) {
                      if (WildcardAccessHelper.isEqual(expr, node)) {
                        continue outer;
                      }
                    }
                  }
                }
              }
            }
          }
        }
        BastNameIdent clone = null;
        clone = CreateJavaNodeHelper.createBastNameIdent(ident.name);
        identifiers.add(clone);
        possibleStrings.remove(ident.name);
      }

    }
    if (!identifiers.isEmpty()) {
      ArrayList<TokenAndHistory> additionalTokens = new ArrayList<>();
      for (int i = 0; i < identifiers.size() - 1; i++) {
        additionalTokens.add(CreateJavaNodeHelper.createTokenHistory(BasicJavaToken.COMMA));
      }
      if (block.identifiers == null || block.identifiers.isEmpty()) {
        block.info.tokens[5] = CreateJavaNodeHelper.createTokenHistory(BasicJavaToken.COMMA);
        block.info.tokens[6] = CreateJavaNodeHelper.createTokenHistory(" ", BasicJavaToken.LPAREN);
        block.info.tokens[8] = CreateJavaNodeHelper.createTokenHistory(BasicJavaToken.RPAREN);

      }
      block.info.tokens[7] = new TokenAndHistory(new ListToken(additionalTokens));
      block.replaceField(BastFieldConstants.ARES_BLOCK_IDENTIFIERS, new BastField(identifiers));
      print(example2Original);
    } else if (identifiers.isEmpty() && !possibleStrings.isEmpty()) {
      block.info.tokens[5] = null;
      block.info.tokens[6] = null;
      block.info.tokens[7] = null;
      block.info.tokens[8] = null;
      block.replaceField(BastFieldConstants.ARES_BLOCK_IDENTIFIERS, new BastField(identifiers));
      print(example2Original);
    }

  }

  private static GeneralizationParameter createParameter(AbstractBastNode example1Original,
      AbstractBastNode example2Original, AbstractBastNode example1Modified,
      AbstractBastNode example2Modified, ConnectWildcardAndUseVisitor cwuv,
      GeneralizationParameter oldParameter, ExecutorService executioner, int subChangeDepth,
      AresMeasurement aresMeasurement) throws GeneralizationException {
    GeneralizationParameter parameter = new GeneralizationParameter();
    AbstractBastNode tmpParent = null;
    AbstractBastNode partner = null;
    if (oldParameter != null
        && oldParameter.exDiffOM1.firstToSecondMap.get(oldParameter.parentOM1.getNode()) == null) {
      oldParameter = null;
    }
    if (oldParameter != null) {
      tmpParent = oldParameter.parentOM1.getNode();
      partner = oldParameter.exDiffOM1.firstToSecondMap.get(tmpParent);
      long timeTreeDiff = System.nanoTime();
      ExtendedDiffResult exDiffBbTmp =
          ExtendedTemplateExtractor.pipeline(null, tmpParent, example2Original, executioner);

      ExtendedDiffResult exDiffAaTmp =
          ExtendedTemplateExtractor.pipeline(null, partner, example2Modified, executioner);
      parameter.exDiffOo = combineDiff(oldParameter.exDiffOo, exDiffBbTmp);
      ExtendedDiffResult exDiffBbReversedTmp =
          ExtendedTemplateExtractor.pipeline(null, example2Original, tmpParent, executioner);
      parameter.exDiffOoReversed = combineDiff(oldParameter.exDiffOoReversed, exDiffBbReversedTmp);
      parameter.exDiffMm = combineDiff(oldParameter.exDiffMm, exDiffAaTmp);

      ExtendedDiffResult exDiffAaReversedTmp =
          ExtendedTemplateExtractor.pipeline(null, example2Modified, partner, executioner);
      parameter.exDiffMmReversed = combineDiff(oldParameter.exDiffMmReversed, exDiffAaReversedTmp);
      if (aresMeasurement != null) {
        aresMeasurement.timeTreeDifferencing.add(System.nanoTime() - timeTreeDiff);
      }
    } else {
      long timeTreeDiff = System.nanoTime();
      parameter.exDiffOo =
          ExtendedTemplateExtractor.pipeline(example1Original, example2Original, executioner);

      parameter.exDiffOoReversed =
          ExtendedTemplateExtractor.pipeline(example2Original, example1Original, executioner);
      parameter.exDiffMm =
          ExtendedTemplateExtractor.pipeline(example1Modified, example2Modified, executioner);
      parameter.exDiffMmReversed =
          ExtendedTemplateExtractor.pipeline(example2Modified, example1Modified, executioner);
      if (aresMeasurement != null) {
        aresMeasurement.timeTreeDifferencing.add(System.nanoTime() - timeTreeDiff);
      }
    }
    long timeTreeDiff = System.nanoTime();
    parameter.exDiffOM1 =
        ExtendedTemplateExtractor.pipeline(example1Original, example1Modified, executioner);
    parameter.exDiffOM2 =
        ExtendedTemplateExtractor.pipeline(example2Original, example2Modified, executioner);
    if (aresMeasurement != null) {
      aresMeasurement.timeTreeDifferencing.add(System.nanoTime() - timeTreeDiff);
    }
    checkForImpossiblePatterns(parameter.exDiffOM1, parameter.exDiffOM2);
    example2Original.accept(cwuv);
    example2Modified.accept(cwuv);
    parameter.original1original2 = parameter.exDiffOo.editScript;
    parameter.modified1modified2 = parameter.exDiffMm.editScript;
    parameter.original1modified1 = parameter.exDiffOM1.editScript;

    RetrieveParentInformationMapVisitor rpimv = new RetrieveParentInformationMapVisitor();
    example1Original.accept(rpimv);

    parameter.hierarchyM1 = ParentHierarchyHandler.getParentHierarchy(example1Modified);
    parameter.hierarchyM2 = ParentHierarchyHandler.getParentHierarchy(example2Modified);
    parameter.hierarchyO1 = ParentHierarchyHandler.getParentHierarchy(example1Original);
    parameter.hierarchyO2 = ParentHierarchyHandler.getParentHierarchy(example2Original);

    parameter.insideMethodOM1 = new boolean[parameter.original1modified1.size()];
    parameter.insideMethodOM2 = new boolean[parameter.original1modified1.size()];

    parameter.insideMethodOo = new boolean[parameter.original1original2.size()];
    parameter.insideMethodMm = new boolean[parameter.original1original2.size()];

    parameter.ignoreOpOo = new boolean[parameter.original1original2.size()];
    parameter.ignoreOpMm = new boolean[parameter.original1original2.size()];

    parameter.parentOM1 = null;
    parameter.parentOM2 = null;
    parameter.parentOo = null;
    parameter.parentMm = null;

    parameter.insideMethodOM1 = identifyEditScriptsInMethods(parameter.exDiffOM1,
        parameter.hierarchyO1, parameter.hierarchyM1, false);

    parameter.insideMethodOM2 = identifyEditScriptsInMethods(parameter.exDiffOM2,
        parameter.hierarchyO2, parameter.hierarchyM2, false);

    parameter.insideMethodOo = identifyEditScriptsInMethods(parameter.exDiffOo,
        parameter.hierarchyO1, parameter.hierarchyO2, true);

    parameter.insideMethodMm = identifyEditScriptsInMethods(parameter.exDiffMm,
        parameter.hierarchyM1, parameter.hierarchyM2, true);

    parameter.parentOM1 = findParentHierarchy(parameter.exDiffOM1, parameter.hierarchyO1,
        parameter.hierarchyM1, parameter.insideMethodOM1, example1Original);
    parameter.matchBoundaryOo = createMatchBoundaries(false, parameter.exDiffOM1,
        parameter.exDiffOM2, parameter.exDiffOo, parameter.exDiffOoReversed, example1Original,
        example2Original, parameter.hierarchyO1, parameter.hierarchyO2, null, null, subChangeDepth);
    parameter.matchBoundaryMm =
        createMatchBoundaries(true, parameter.exDiffOM1, parameter.exDiffOM2, parameter.exDiffOo,
            parameter.exDiffMmReversed, example1Modified, example2Modified, parameter.hierarchyM1,
            parameter.hierarchyM2, parameter.matchBoundaryOo, parameter.exDiffMm, subChangeDepth);
    if (oldParameter != null) {
      NodeParentInformation np = null;
      if (oldParameter.parentOM1.getNode().getTag() != BastBlock.TAG) {

        for (int i = 0; i < oldParameter.parentOM1.list.size(); i++) {
          np = oldParameter.parentOM1.list.get(i);
          if (np.parent.getTag() == BastBlock.TAG) {
            break;
          }
        }

        parameter.matchBoundaryOo.setNode1(np.parent);
        parameter.matchBoundaryOo.field1 = np.fieldConstant;

      } else {
        parameter.matchBoundaryOo.setNode1(oldParameter.parentOM1.getNode());
        parameter.matchBoundaryOo.field1 = BastFieldConstants.BLOCK_STATEMENT;
      }
      NodeParentInformationHierarchy npi = parameter.hierarchyM1.get(partner);
      np = null;
      if (npi.getNode().getTag() != BastBlock.TAG) {
        for (int i = 0; i < npi.list.size(); i++) {
          np = npi.list.get(i);
          if (np.parent.getTag() == BastBlock.TAG) {
            break;
          }
        }
        parameter.matchBoundaryMm.setNode1(np.parent);
        parameter.matchBoundaryMm.field1 = np.fieldConstant;

      } else {
        parameter.matchBoundaryMm.setNode1(npi.getNode());
        parameter.matchBoundaryMm.field1 = BastFieldConstants.BLOCK_STATEMENT;
      }
    }
    return parameter;
  }

  private static void checkForMissingBodyChange(AbstractBastNode example2Original,
      AbstractBastNode example2Modified, ExecutorService executioner)
      throws GeneralizationException {
    FindNodesFromTagVisitor fnftB = new FindNodesFromTagVisitor(BastFunction.TAG);
    example2Original.accept(fnftB);
    FindNodesFromTagVisitor fnftA = new FindNodesFromTagVisitor(BastFunction.TAG);
    example2Modified.accept(fnftA);
    if (fnftB.nodes.size() == 0 || ((BastFunction) fnftB.nodes.get(0)).statements == null
        || ((BastFunction) fnftB.nodes.get(0)).statements.size() == 0) {

      throw new GeneralizationException("No body change in example!");
    }
    if (((BastFunction) fnftB.nodes.get(0)).statements.size() == 1
        && ((BastFunction) fnftB.nodes.get(0)).statements.get(0).getTag() == BastBlock.TAG
        && (((BastBlock) ((BastFunction) fnftB.nodes.get(0)).statements.get(0)).statements == null
            || ((BastBlock) ((BastFunction) fnftB.nodes.get(0)).statements.get(0)).statements
                .size() == 0)) {

      throw new GeneralizationException("Empty body in example!");
    }
    if (fnftA.nodes == null || fnftA.nodes.size() == 0
        || ((BastFunction) fnftA.nodes.get(0)).statements == null
        || ((BastFunction) fnftA.nodes.get(0)).statements.size() == 0) {
      throw new GeneralizationException("Empty body in example!");
    }
    ExtendedDiffResult exDiffGen =
        ExtendedTemplateExtractor.pipeline(((BastFunction) fnftB.nodes.get(0)).statements.get(0),
            ((BastFunction) fnftA.nodes.get(0)).statements.get(0), executioner);
    if (exDiffGen != null && exDiffGen.editScript.size() <= 0) {
      throw new GeneralizationException("No body change in example!");
    }
  }

  private static void checkForImpossiblePatterns(ExtendedDiffResult exDiffBA1,
      ExtendedDiffResult exDiffBA2) throws GeneralizationException {
    boolean insertDelete = false;
    for (int i = 0; i < exDiffBA1.editScript.size(); i++) {
      switch (exDiffBA1.editScript.get(i).getType()) {
        case ALIGN:
        case STATEMENT_PARENT_CHANGE:
        case STATEMENT_REORDERING:
        case CLASS_RENAME:
        case MOVE:
        case UPDATE:
        case METHOD_DELETE:
        case METHOD_INSERT:
        case METHOD_RENAME:
        case VARIABLE_RENAME:
        case FINAL_INSERT:
        case FINAL_DELETE:
        case PARENT_CLASS_INSERT:
        case DECREASING_ACCESS:
        case INCREASING_ACCESS:
        case STATEMENT_UPDATE:
          continue;
        default:
          break;
      }
      boolean found = false;
      for (int j = 0; j < exDiffBA2.editScript.size(); j++) {
        insertDelete = true;
        switch (exDiffBA1.editScript.get(i).getType()) {
          case INSERT:
            if (exDiffBA2.editScript.get(j).getType() == EditOperationType.DELETE) {
              if (WildcardAccessHelper.isEqual(exDiffBA1.editScript.get(i).getOldOrInsertedNode(),
                  exDiffBA2.editScript.get(j).getOldOrInsertedNode())) {
                found = true;
                break;
              }
            }
            break;
          case VARIABLE_INSERT:
            if (exDiffBA2.editScript.get(j).getType() == EditOperationType.VARIABLE_DELETE) {
              if (WildcardAccessHelper.isEqual(exDiffBA1.editScript.get(i).getOldOrInsertedNode(),
                  exDiffBA2.editScript.get(j).getOldOrInsertedNode())) {
                found = true;
                break;
              }
            }
            break;
          case VARIABLE_DELETE:
            if (exDiffBA2.editScript.get(j).getType() == EditOperationType.VARIABLE_INSERT) {
              if (WildcardAccessHelper.isEqual(exDiffBA1.editScript.get(i).getOldOrInsertedNode(),
                  exDiffBA2.editScript.get(j).getOldOrInsertedNode())) {
                found = true;
                break;
              }
            }
            break;
          case STATEMENT_INSERT:
            if (exDiffBA2.editScript.get(j).getType() == EditOperationType.STATEMENT_DELETE) {
              if (WildcardAccessHelper.isEqual(exDiffBA1.editScript.get(i).getOldOrInsertedNode(),
                  exDiffBA2.editScript.get(j).getOldOrInsertedNode())) {
                found = true;
                break;
              }
            }
            break;
          case STATEMENT_DELETE:
            if (exDiffBA2.editScript.get(j).getType() == EditOperationType.STATEMENT_INSERT) {
              if (WildcardAccessHelper.isEqual(exDiffBA1.editScript.get(i).getOldOrInsertedNode(),
                  exDiffBA2.editScript.get(j).getOldOrInsertedNode())) {
                found = true;
                break;
              }
            }
            break;
          case DELETE:
            if (exDiffBA2.editScript.get(j).getType() == EditOperationType.INSERT) {
              if (WildcardAccessHelper.isEqual(exDiffBA1.editScript.get(i).getOldOrInsertedNode(),
                  exDiffBA2.editScript.get(j).getOldOrInsertedNode())) {
                found = true;
                break;
              }
            }
            break;
          default:
            if (DEBUG) {
              System.err.println(exDiffBA1.editScript.get(i).getType().niceName);
            }
            assert (false);
        }
        if (found) {
          break;
        }
      }
      if (!found && insertDelete) {
        return;
      }
    }
    if (insertDelete) {
      throw new GeneralizationException("Impossible change!");
    }

  }

  private static void checkForOvergeneralization(AbstractBastNode example2Original,
      AbstractBastNode example2Modified, ExtendedDiffResult exDiffBA1, ExtendedDiffResult exDiffBA2,
      GeneralizationParameter parameter, ExecutorService executioner)
      throws GeneralizationException {
    FindNodesFromTagVisitor fnftB = new FindNodesFromTagVisitor(AresBlock.TAG);
    example2Original.accept(fnftB);
    FindNodesFromTagVisitor fnftA = new FindNodesFromTagVisitor(AresBlock.TAG);
    example2Modified.accept(fnftA);
    if (fnftB.nodes.isEmpty()) {
      throw new GeneralizationException("No match block!");
    } else {
      for (AbstractBastNode node : fnftB.nodes) {
        AresBlock blockNode = (AresBlock) node;
        if (blockNode.block.statements.isEmpty()) {
          throw new GeneralizationException("Empty match block!");
        }
        boolean allWildcards = true;
        for (AbstractBastNode stmt : blockNode.block.statements) {
          if (stmt.getTag() != AresWildcard.TAG) {
            allWildcards = false;
            break;
          }
        }
        if (allWildcards) {
          throw new GeneralizationException("Only wildcards!");
        }
      }
      for (AbstractBastNode node : fnftA.nodes) {
        AresBlock blockNode = (AresBlock) node;
        boolean allEmptyUses = true;
        if (blockNode.block != null && blockNode.block.statements != null) {
          for (AbstractBastNode stmt : blockNode.block.statements) {
            if (stmt.getTag() != AresUseStmt.TAG) {
              allEmptyUses = false;
              break;
            } else {
              if (WildcardAccessHelper.getName(((AresUseStmt) stmt)) != null) {
                allEmptyUses = false;
                break;
              }
            }
          }

          if (allEmptyUses) {
            if (blockNode.block.statements.size() > 0) {
              throw new GeneralizationException("Only empty uses!");
            }
          }
        }
      }
      HashMap<AbstractBastNode, ModificationInformation> modifiedNodesBA2 =
          getModifiedNodes(exDiffBA2.editScript, exDiffBA2, true, true);
      HashMap<AbstractBastNode, ModificationInformation> modifiedNodesBA1 =
          getModifiedNodes(exDiffBA1.editScript, exDiffBA1, true, true);

      FindModificationsVisitor fmsv = new FindModificationsVisitor(modifiedNodesBA1);
      example2Original.accept(fmsv);

      boolean noRelevantChange = true;

      for (AbstractBastNode node : fnftB.nodes) {
        AresBlock blockNode = (AresBlock) node;
        for (AbstractBastNode stmt : blockNode.block.statements) {
          if (stmt.getTag() != AresWildcard.TAG) {
            if (modifiedNodesBA2.get(stmt) != null
                || (fmsv.modMap.get(stmt) != null && fmsv.modMap.get(stmt).childModified)) {
              noRelevantChange = false;
              break;
            }
          }
        }

      }
      fmsv = new FindModificationsVisitor(modifiedNodesBA2);
      example2Modified.accept(fmsv);
      for (AbstractBastNode node : fnftA.nodes) {
        AresBlock blockNode = (AresBlock) node;
        if (blockNode.block != null && blockNode.block.statements != null) {
          for (AbstractBastNode stmt : blockNode.block.statements) {
            if (stmt.getTag() != AresUseStmt.TAG) {
              if (modifiedNodesBA2.get(stmt) != null || stmt.getTag() == AresChoiceStmt.TAG
                  || (fmsv.modMap.get(stmt) != null && fmsv.modMap.get(stmt).childModified)) {
                noRelevantChange = false;
                break;
              }
            }
          }
        }

      }

      if (noRelevantChange) {
        throw new GeneralizationException("Change not covered!");
      }
    }
    try {
      ExtendedDiffResult exDiffGen =
          ExtendedTemplateExtractor.pipeline(fnftB.nodes.get(0), fnftA.nodes.get(0), executioner);
      if (exDiffGen.editScript.size() <= 0) {
        throw new GeneralizationException("No change remains!");
      }
      boolean relevantChange = false;
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyB =
          ParentHierarchyHandler.getParentHierarchy(fnftB.nodes.get(0));
      for (BastEditOperation ep : exDiffGen.editScript) {
        if (ep instanceof AdvancedEditOperation) {
          ep = ((AdvancedEditOperation) ep).getBasicOperation();
        }
        switch (ep.getType()) {
          case MOVE:
            if (ep.getUnchangedOrNewParentNode().getTag() == AresUseStmt.TAG
                && (ep.getOldOrInsertedNode().getTag() == BastNameIdent.TAG
                    || ep.getOldOrInsertedNode().getTag() == AresPatternClause.TAG)) {
              continue;
            } else {
              if (ep.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG
                  && ep.getUnchangedOrNewParentNode().getTag() == BastBlock.TAG
                  && ep.getOldOrInsertedNode().getTag() == BastTryStmt.TAG) {
                continue;
              }
              if (ep.getOldOrInsertedNode().getTag() == AresBlock.TAG) {
                continue;
              }
              relevantChange = true;
            }
            break;
          case INSERT:
            if (ep.getOldOrInsertedNode().getTag() == AresUseStmt.TAG) {
              continue;
            } else {
              relevantChange = true;
            }
            break;
          case DELETE:
            if (ep.getOldOrInsertedNode().getTag() == AresWildcard.TAG) {
              continue;
            } else if (ep.getOldOrInsertedNode().getTag() == AresPluginClause.TAG) {
              continue;
            } else if (ep.getUnchangedOrOldParentNode().getTag() == AresPluginClause.TAG) {
              continue;
            } else if (ep.getUnchangedOrOldParentNode().getTag() == AresPatternClause.TAG) {
              continue;
            } else {
              relevantChange = true;
            }
            break;
          case UPDATE:
            if (ep.getUnchangedOrOldParentNode().getTag() == AresBlock.TAG) {
              continue;
            }
            NodeParentInformationHierarchy npi = hierarchyB.get(ep.getOldOrInsertedNode());
            if (npi != null) {
              NodeParentInformation np = null;
              for (int i = 0; i < npi.list.size(); i++) {
                if (npi.list.get(i).parent.getTag() == BastBlock.TAG) {
                  np = npi.list.get(i);
                  break;
                }
              }
              if (np != null) {
                LinkedList<AbstractBastNode> exprWildcards = new LinkedList<>();
                AbstractBastNode stmt = null;
                stmt = np.parent.getField(np.fieldConstant).getListField().get(np.listId);
                if (stmt != null) {
                  if (stmt.getTag() == AresWildcard.TAG) {
                    break;
                  }

                  for (int i = np.listId - 1; i >= 0; i--) {
                    if (WildcardAccessHelper
                        .isWildcard(np.parent.getField(np.fieldConstant).getListField().get(i))) {
                      exprWildcards.add(np.parent.getField(np.fieldConstant).getListField().get(i));
                    } else {
                      break;
                    }
                  }
                  if (exprWildcards.size() > 0) {
                    boolean noChange = false;
                    for (AbstractBastNode node : exprWildcards) {
                      if (WildcardAccessHelper.isWildcardPart(node, ep.getOldOrInsertedNode())) {
                        noChange = true;
                        break;
                      }
                    }
                    if (noChange) {
                      break;
                    }
                  }
                }

              }
            }
            relevantChange = true;
            break;
          default:
            relevantChange = true;

        }
        if (relevantChange) {
          break;
        }
      }
      if (!relevantChange) {
        throw new GeneralizationException("No change remains!");
      }
      if (((AresBlock) fnftB.nodes.get(0)).block.statements.size() == 2) {
        if (WildcardAccessHelper
            .isExprWildcard(((AresBlock) fnftB.nodes.get(0)).block.statements.get(0))) {

          if (((AresBlock) fnftB.nodes.get(0)).block.statements.get(1).getTag() == BastIf.TAG) {
            BastIf ifStmt = (BastIf) ((AresBlock) fnftB.nodes.get(0)).block.statements.get(1);
            if (WildcardAccessHelper.isEqual((ifStmt.condition), WildcardAccessHelper
                .getExpr(((AresBlock) fnftB.nodes.get(0)).block.statements.get(0)))) {
              if (ifStmt.elsePart == null) {
                if (ifStmt.ifPart.getTag() == BastBlock.TAG) {
                  BastBlock ifBlock = (BastBlock) ifStmt.ifPart;
                  if (ifBlock.statements == null || ifBlock.statements.size() == 0) {
                    throw new GeneralizationException("Too small original part!");
                  } else if (ifBlock.statements.size() == 1
                      && WildcardAccessHelper.isWildcard(ifBlock.statements.get(0))) {
                    throw new GeneralizationException("Too small original part!");
                  }
                } else if (WildcardAccessHelper.isWildcard(ifStmt.ifPart)) {
                  throw new GeneralizationException("Too small original part!");
                }
              }
            }
          } else if (((AresBlock) fnftB.nodes.get(0)).block.statements.get(1)
              .getTag() == BastReturn.TAG) {
            BastReturn returnStmt =
                (BastReturn) ((AresBlock) fnftB.nodes.get(0)).block.statements.get(1);
            if (WildcardAccessHelper.isEqual((returnStmt.returnValue), WildcardAccessHelper
                .getExpr(((AresBlock) fnftB.nodes.get(0)).block.statements.get(0)))) {
              throw new GeneralizationException("Too small original part!");
            }
          }
        }
      }
      if (((AresBlock) fnftB.nodes.get(0)).block.statements.size() == 2) {
        if (WildcardAccessHelper
            .isExprWildcard(((AresBlock) fnftB.nodes.get(0)).block.statements.get(0))) {
          if (((AresBlock) fnftB.nodes.get(0)).block.statements.get(1)
              .getTag() == BastDeclaration.TAG) {
            BastDeclaration decl =
                (BastDeclaration) ((AresBlock) fnftB.nodes.get(0)).block.statements.get(1);
            if (decl.declaratorList.get(0).getTag() == BastIdentDeclarator.TAG) {
              BastIdentDeclarator identDecl = (BastIdentDeclarator) decl.declaratorList.get(0);
              if (WildcardAccessHelper.isEqual(((BastExprInitializer) identDecl.expression).init,
                  WildcardAccessHelper
                      .getExpr(((AresBlock) fnftB.nodes.get(0)).block.statements.get(0)))) {
                throw new GeneralizationException("Too small original part!");
              }
            }
          } else if (((AresBlock) fnftB.nodes.get(0)).block.statements.get(1)
              .getTag() == BastReturn.TAG) {
            BastReturn returnStmt =
                (BastReturn) ((AresBlock) fnftB.nodes.get(0)).block.statements.get(1);
            if (WildcardAccessHelper.isEqual((returnStmt.returnValue), WildcardAccessHelper
                .getExpr(((AresBlock) fnftB.nodes.get(0)).block.statements.get(0)))) {
              throw new GeneralizationException("Too small original part!");
            }
          }
        }
      } else if (((AresBlock) fnftB.nodes.get(0)).block.statements.size() == 1) {
        if (((AresBlock) fnftB.nodes.get(0)).block.statements.get(0)
            .getTag() == BastDeclaration.TAG) {
          BastDeclaration decl =
              (BastDeclaration) ((AresBlock) fnftB.nodes.get(0)).block.statements.get(0);
          if (decl.declaratorList.get(0).getTag() == BastIdentDeclarator.TAG) {
            BastIdentDeclarator identDecl = (BastIdentDeclarator) decl.declaratorList.get(0);
            if (identDecl.expression == null) {
              throw new GeneralizationException("Too small original part!");
            }
          }
        }

      } else if (((AresBlock) fnftB.nodes.get(0)).block.statements.size() == 1) {
        if (((AresBlock) fnftB.nodes.get(0)).block.statements.get(0).getTag() == BastReturn.TAG) {
          BastReturn decl = (BastReturn) ((AresBlock) fnftB.nodes.get(0)).block.statements.get(0);
          if (decl.returnValue.getTag() == BastIntConst.TAG) {
            throw new GeneralizationException("Too small original part!");
          }
        }
      }
      if (((AresBlock) fnftB.nodes.get(0)).block.statements.size() > 2) {
        LinkedList<AbstractBastNode> expressions = new LinkedList<>();
        for (AbstractBastNode node : ((AresBlock) fnftB.nodes.get(0)).block.statements) {
          if (WildcardAccessHelper.isExprWildcard(node)) {
            expressions.add(WildcardAccessHelper.getExpr(node));
          }
        }
        if (expressions.size() + 1 == ((AresBlock) fnftB.nodes.get(0)).block.statements.size()) {
          AbstractBastNode stmt = (((AresBlock) fnftB.nodes.get(0)).block.statements
              .get(((AresBlock) fnftB.nodes.get(0)).block.statements.size() - 1));
          if (stmt.getTag() == BastReturn.TAG) {
            BastReturn returnStmt = (BastReturn) stmt;
            if (returnStmt.returnValue.getTag() == BastCall.TAG) {
              BastCall call = (BastCall) returnStmt.returnValue;
              boolean[] arguments = new boolean[call.arguments.size()];
              boolean function = false;
              for (AbstractBastNode node : expressions) {
                for (int i = 0; i < arguments.length; i++) {
                  if (WildcardAccessHelper.isEqual(call.arguments.get(i), node)) {
                    arguments[i] = true;
                  }
                }
                if (WildcardAccessHelper.isEqual(call.function, node)) {
                  function = true;
                }
              }
              boolean test = function;
              for (int i = 0; i < arguments.length; i++) {
                test &= arguments[i];
              }
              if (test) {
                throw new GeneralizationException("Too small original part!");
              }
            } else {
              for (AbstractBastNode node : expressions) {
                if (WildcardAccessHelper.isEqual((returnStmt.returnValue),
                    WildcardAccessHelper.getExpr(node))) {
                  throw new GeneralizationException("Too small original part!");
                }
              }
            }
          } else if (stmt.getTag() == BastAsgnExpr.TAG) {
            BastAsgnExpr asgnStmt = (BastAsgnExpr) stmt;

            boolean left = false;
            boolean right = false;
            for (AbstractBastNode node : expressions) {
              if (WildcardAccessHelper.isEqual(asgnStmt.left, node)) {
                left = true;
              }
              if (WildcardAccessHelper.isEqual(asgnStmt.right, node)) {
                right = true;
              }
            }

            if (left && right) {
              throw new GeneralizationException("Too small original part!");
            }
          }
        }
      }

    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    FindNodesFromTagWithoutChoiceVisitor findBlocks =
        new FindNodesFromTagWithoutChoiceVisitor(BastBlock.TAG);
    example2Modified.accept(findBlocks);
    int choices = 0;
    int otherNodes = 0;
    for (AbstractBastNode node : findBlocks.nodes) {
      BastBlock block = (BastBlock) node;
      for (AbstractBastNode blockNode : block.statements) {
        if (blockNode.getTag() == AresChoiceStmt.TAG) {
          choices++;
        } else {
          if (!(blockNode.getTag() == AresUseStmt.TAG || blockNode.getTag() == AresWildcard.TAG
              || blockNode.getTag() == AresBlock.TAG)) {
            otherNodes++;
          }
        }
      }
    }
    findBlocks = new FindNodesFromTagWithoutChoiceVisitor(BastBlock.TAG);
    example2Original.accept(findBlocks);
    int otherNodesOriginal = 0;
    for (AbstractBastNode node : findBlocks.nodes) {
      BastBlock block = (BastBlock) node;
      for (AbstractBastNode blockNode : block.statements) {
        if (!(blockNode.getTag() == AresUseStmt.TAG || blockNode.getTag() == AresWildcard.TAG
            || blockNode.getTag() == AresBlock.TAG)) {
          otherNodesOriginal++;
        }
      }
    }
    if (otherNodes == 1 && choices > 1 && otherNodesOriginal != 1) {
      throw new GeneralizationException("Too many choices!");
    }
  }

  private static ArrayList<AbstractBastNode> checkAresBlockMoves(ExtendedDiffResult exDiffBb) {
    boolean moves = false;
    for (BastEditOperation ep : exDiffBb.editScript) {
      if (ep.getType() == EditOperationType.MOVE) {
        if (ep.getUnchangedOrNewParentNode().getTag() == AresBlock.TAG) {

          moves = true;
          break;
        }
      }
    }
    AbstractBastNode oldParentNode = null;
    int maxValue = Integer.MIN_VALUE;
    HashMap<Integer, Integer> target2Source = new HashMap<>();
    ArrayList<AbstractBastNode> wildcardPos = new ArrayList<>();
    AbstractBastNode aresBlock = null;
    if (moves) {
      for (BastEditOperation ep : exDiffBb.editScript) {
        if (ep.getType() == EditOperationType.MOVE) {
          aresBlock = ep.getUnchangedOrNewParentNode();
          if (getNewListNumber(ep) == BastFieldConstants.ARES_BLOCK_BLOCK) {
            if (oldParentNode == null) {
              oldParentNode = ep.getUnchangedOrOldParentNode();
            } else {
              if (oldParentNode != ep.getUnchangedOrOldParentNode()) {
                continue;
              }
            }
            maxValue = Math.max(maxValue, ep.getOldOrChangedIndex().childrenListIndex);
            target2Source.put(ep.getNewOrChangedIndex().childrenListIndex,
                ep.getOldOrChangedIndex().childrenListIndex);
          }
        }
      }
    }
    ArrayList<Integer> targetList = new ArrayList<>();
    targetList.addAll(target2Source.keySet());
    Collections.sort(targetList);

    for (int i = 0; i < targetList.size(); i++) {
      int target = targetList.get(i);
      if (i == 0) {
        if (target != 0 && target2Source.get(target) != 0) {
          wildcardPos.add(aresBlock.getField(BastFieldConstants.ARES_BLOCK_BLOCK).getField()
              .getField(BastFieldConstants.BLOCK_STATEMENT).getListField().get(target));
        }
      } else {
        if (Math.abs(target2Source.get(targetList.get(i - 1)) - target2Source.get(target)) > 1) {
          wildcardPos.add(aresBlock.getField(BastFieldConstants.ARES_BLOCK_BLOCK).getField()
              .getField(BastFieldConstants.BLOCK_STATEMENT).getListField().get(target));
        }
      }
    }
    return wildcardPos;
  }

  private static HashMap<DeleteOperation, InsertOperation> removeDuplicateInsertDeleteActions(
      List<BastEditOperation> editScript, ExtendedDiffResult exDiff) {
    List<BastEditOperation> workList = new ArrayList<>();
    workList.addAll(editScript);
    List<DeleteOperation> delList = new ArrayList<>();
    HashMap<DeleteOperation, InsertOperation> map = new HashMap<>();
    for (BastEditOperation ep : editScript) {
      switch (ep.getType()) {
        case DELETE:
          delList.add((DeleteOperation) ep);
          break;
        default:
          break;
      }
    }
    for (BastEditOperation ep : editScript) {
      switch (ep.getType()) {
        case INSERT:
          for (DeleteOperation del : delList) {
            InsertOperation ins = (InsertOperation) ep;
            if (del.getUnchangedOrOldParentNode() == exDiff.secondToFirstMap
                .get(ins.getUnchangedOrOldParentNode())) {
              if (ins.getOldOrChangedIndex().childrenListNumber == del
                  .getOldOrChangedIndex().childrenListNumber) {
                if (ins.getOldOrChangedIndex().childrenListIndex == del
                    .getOldOrChangedIndex().childrenListIndex) {
                  map.put(del, ins);
                }
              }
            }
          }

          break;
        default:
          break;
      }
    }
    return map;
  }

  /**
   * Extract indentation.
   *
   * @param astNode the ast node
   * @return the string buffer
   */
  public static StringBuffer extractIndentation(AbstractBastNode astNode) {
    StringBuffer bufferTmp = new StringBuffer();
    IPrettyPrinter enlP = ParserFactory.getAresPrettyPrinter();
    astNode.accept(enlP);

    int loopI = 0;
    byte[] tmpString = enlP.getBuffer().toString().getBytes();
    while (loopI < tmpString.length) {
      if (tmpString[loopI] == '/' && tmpString.length > loopI + 1 && tmpString[loopI + 1] == '/') {
        while (loopI + 1 < tmpString.length && tmpString[loopI] != '\n') {
          loopI++;
        }
      }
      if (tmpString[loopI] == ' ' || tmpString[loopI] == '\n' || tmpString[loopI] == '\t') {
        bufferTmp.append((char) tmpString[loopI]);
        loopI++;
      } else {
        break;
      }

    }
    return bufferTmp;
  }

  static StringBuffer extractIndentation(LinkedList<AbstractBastStatement> stmt, int index) {
    if (index < stmt.size()) {
      return extractIndentation(stmt.get(index));
    } else {
      return new StringBuffer();
    }
  }

  @SuppressWarnings("unchecked")
  private static AbstractBastNode insertAresBlock(MatchBoundary boundary,
      NodeParentInformationHierarchy boundaryNpi, AbstractBastNode example2Original,
      NodeParentInformationHierarchy parentBA2, boolean original) throws GeneralizationException {
    if (boundary != null) {
      if (boundary.field2 == BastFieldConstants.FUNCTION_BLOCK_STATEMENTS) {
        LinkedList<AbstractBastStatement> statements = new LinkedList<>();
        BastBlock block =
            (BastBlock) boundary.getNode2().getField(boundary.field2).getListField().get(0);
        if (block.statements == null || block.statements.size() == 0) {
          throw new GeneralizationException("Empty match block!");
        }
        statements.addAll(block.statements);
        TokenAndHistory structureTokens = null;
        AresBlock leaBlock =
            CreateJavaNodeHelper.createAresBlock(structureTokens, 1, original, statements, null);
        AbstractBastNode tmpNode = statements.getFirst();
        StringBuffer bufferTmp = extractIndentation(tmpNode);

        ((JavaToken) (leaBlock.info.tokens[0].token)).whiteSpace.append(bufferTmp.toString());
        if (leaBlock.info.tokens[7] != null && leaBlock.info.tokens[7].token != null
            && ((JavaToken) (leaBlock.info.tokens[7].token)).whiteSpace != null
            && bufferTmp != null) {
          ((JavaToken) (leaBlock.info.tokens[7].token)).whiteSpace.append(bufferTmp.toString());
        }
        LinkedList<AbstractBastStatement> originalStatements = new LinkedList<>();
        originalStatements.add(leaBlock);
        BastField newField = new BastField(originalStatements);
        block.replaceField(BastFieldConstants.BLOCK_STATEMENT, newField);
      } else if (boundary.getNode2().getField(boundary.field2) != null
          && boundary.getNode2().getField(boundary.field2).isList()) {
        LinkedList<AbstractBastStatement> statements = new LinkedList<>();
        statements.addAll((Collection<? extends AbstractBastStatement>) boundary.getNode2()
            .getField(boundary.field2).getListField());
        TokenAndHistory structureTokens = null;
        if (!statements.isEmpty() && statements.get(0).info.tokens != null) {
          for (TokenAndHistory t : statements.get(0).info.tokens) {
            if (t != null) {
              break;
            }
          }

        }
        AresBlock leaBlock =
            CreateJavaNodeHelper.createAresBlock(structureTokens, 1, original, statements, null);
        StringBuffer bufferTmp = new StringBuffer();

        AbstractBastNode tmpNode = null;
        if (statements.size() > 0) {
          tmpNode = statements.getFirst();
          bufferTmp = extractIndentation(tmpNode);
        }

        ((JavaToken) (leaBlock.info.tokens[0].token)).whiteSpace.append(bufferTmp.toString());
        ((JavaToken) (leaBlock.block.info.tokens[2].token)).whiteSpace.append(bufferTmp.toString());
        LinkedList<AbstractBastStatement> originalStatements = new LinkedList<>();
        originalStatements.add(leaBlock);
        BastField newField = new BastField(originalStatements);
        boundary.getNode2().replaceField(boundary.field2, newField);
      } else {
        assert (false);
      }

    } else {
      NodeParentInformation npi = parentBA2.list.peek();
      if (npi.fieldConstant == BastFieldConstants.FUNCTION_BLOCK_STATEMENTS) {
        LinkedList<AbstractBastStatement> statements = new LinkedList<>();
        assert (parentBA2.getNode().getTag() == BastBlock.TAG);
        BastBlock bastBlock = (BastBlock) ((AbstractBastStatement) parentBA2.getNode());
        BastField field = bastBlock.getField(BastFieldConstants.BLOCK_STATEMENT);
        LinkedList<? extends AbstractBastNode> listField = field.getListField();
        statements.addAll((Collection<? extends AbstractBastStatement>) listField);
        TokenAndHistory structureTokens = null;
        if (parentBA2.getNode().info != null && parentBA2.getNode().info.tokens != null) {
          structureTokens = parentBA2.getNode().info.tokens[0];
        } else if (((AbstractBastNode) parentBA2.getNode()).getTag() == BastBlock.TAG) {
          structureTokens = ((BastBlock) ((AbstractBastNode) parentBA2.getNode()))
              .getField(BastFieldConstants.BLOCK_STATEMENT).getListField()
              .getFirst().info.tokens[0];
        }
        AresBlock leaBlock =
            CreateJavaNodeHelper.createAresBlock(structureTokens, 1, original, statements, null);
        AbstractBastNode tmpNode = statements.getFirst();
        StringBuffer bufferTmp = extractIndentation(tmpNode);

        ((JavaToken) (leaBlock.info.tokens[0].token)).whiteSpace.append(bufferTmp.toString());
        ((JavaToken) (leaBlock.info.tokens[7].token)).whiteSpace.append(bufferTmp.toString());
        LinkedList<AbstractBastStatement> originalStatements = new LinkedList<>();
        originalStatements.add(leaBlock);

        BastField newField = new BastField(originalStatements);
        parentBA2.getNode().replaceField(BastFieldConstants.BLOCK_STATEMENT, newField);
      } else {
        LinkedList<AbstractBastStatement> statements = new LinkedList<>();
        statements.add((AbstractBastStatement) parentBA2.getNode());
        TokenAndHistory structureTokens = null;
        if (parentBA2.getNode().info != null && parentBA2.getNode().info.tokens != null) {
          structureTokens = parentBA2.getNode().info.tokens[0];
        } else if (((AbstractBastNode) parentBA2.getNode()).getTag() == BastBlock.TAG) {
          structureTokens = ((BastBlock) ((AbstractBastNode) parentBA2.getNode()))
              .getField(BastFieldConstants.BLOCK_STATEMENT).getListField()
              .getFirst().info.tokens[0];
        }
        AresBlock leaBlock =
            CreateJavaNodeHelper.createAresBlock(structureTokens, 1, original, statements, null);

        LinkedList<AbstractBastStatement> originalStatements = new LinkedList<>();
        LinkedList<AbstractBastStatement> stmts = null;
        if ((npi.parent.fieldMap.get(npi.fieldConstant)).isList()) {
          stmts = (LinkedList<AbstractBastStatement>) (npi.parent.fieldMap.get(npi.fieldConstant))
              .getListField();
        }
        TokenAndHistory[] endTokens = null;
        if (stmts != null) {
          for (int i = 0; i < stmts.size(); i++) {
            if (i != npi.listId) {
              if (i == npi.listId + 1) {
                endTokens = stmts.get(i).info.tokens;
              }
              originalStatements.add(stmts.get(i));

            } else {

              originalStatements.add(leaBlock);
            }
          }
          if (endTokens == null
              && (((AbstractBastNode) parentBA2.getNode()).getTag() == BastBlock.TAG)) {
            endTokens = ((BastBlock) ((AbstractBastNode) parentBA2.getNode()))
                .getField(BastFieldConstants.BLOCK_STATEMENT).getListField().getLast().info.tokens;
          }

          LinkedList<IGeneralToken> tokenList = new LinkedList<>();
          if (endTokens != null) {
            tokenList.addAll(endTokens[0].prevTokens);
          }
          if (endTokens != null) {
            ((JavaToken) leaBlock.info.tokens[7].token).whiteSpace
                .append(((JavaToken) (endTokens[0].token)).whiteSpace.toString());
            endTokens[0].prevTokens.clear();
          }

          BastField newField = new BastField(originalStatements);
          npi.parent.replaceField(npi.fieldConstant, newField);
        } else {
          originalStatements.add(leaBlock);
          if (endTokens == null
              && (((AbstractBastNode) parentBA2.getNode()).getTag() == BastBlock.TAG)) {
            endTokens = ((BastBlock) ((AbstractBastNode) parentBA2.getNode()))
                .getField(BastFieldConstants.BLOCK_STATEMENT).getListField().getLast().info.tokens;
          }
          BastBlock block = CreateJavaNodeHelper.createBlock(null, originalStatements);
          LinkedList<IGeneralToken> tokenList = new LinkedList<>();
          if (endTokens != null) {
            tokenList.addAll(endTokens[0].prevTokens);
          }
          if (endTokens != null) {
            ((JavaToken) leaBlock.info.tokens[7].token).whiteSpace
                .append(((JavaToken) (endTokens[0].token)).whiteSpace.toString());
            endTokens[0].prevTokens.clear();
          }

          BastField newField = new BastField(block);
          npi.parent.replaceField(npi.fieldConstant, newField);
        }
      }
    }
    return example2Original;

  }

  private static InsertResult insertAndCombine(AbstractBastNode example1Original,
      AbstractBastNode example2Original, List<BastEditOperation> workList,
      ExtendedDiffResult exDiff, Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy2, MatchBoundary boundary,
      ConnectWildcardAndUseVisitor cwuv,
      HashMap<AbstractBastNode, AbstractBastNode> deleteInsertNodeMap,
      ArrayList<AbstractBastNode> additionalAnnotations, boolean useVersion,
      int deleteOutsideBoundaryOffset,
      HashMap<AbstractBastNode, List<RevertModificationInfo>> revertMap, boolean containsAresBlock,
      HashMap<AbstractBastNode, BlockChanges> changeMap) {
    HashMap<DeleteOperation, InsertOperation> delInsMap =
        removeDuplicateInsertDeleteActions(workList, exDiff);
    ArrayList<BastEditOperation> oldList = new ArrayList<>();
    oldList.addAll(workList);
    Collections.sort(workList, new EnforceEditOpOrderComparator());
    ReplaceMap replaceMap = new ReplaceMap();
    if (DEBUG) {
      if (useVersion) {
        System.err.println("modified");
      } else {
        System.err.println("original");

      }
    }
    ArrayList<BastEditOperation> updates = new ArrayList<>();
    ArrayList<BastEditOperation> deletes = new ArrayList<>();
    ArrayList<BastEditOperation> inserts = new ArrayList<>();
    ArrayList<BastEditOperation> deletesBlock = new ArrayList<>();
    ArrayList<BastEditOperation> insertsBlock = new ArrayList<>();
    for (BastEditOperation editOp : workList) {
      BastFieldConstants newListNumber = getNewListNumber(editOp);
      BastFieldConstants oldListNumber = getOldListNumber(editOp);
      AbstractBastNode unchangedOrNewParentNode = editOp.getUnchangedOrNewParentNode();
      AbstractBastNode unchangedOrOldParentNode = editOp.getUnchangedOrOldParentNode();
      switch (editOp.getType()) {
        case ALIGN:
          if (isBlock(unchangedOrNewParentNode)
              || isSwitchCaseGroupStatementsField(newListNumber)) {
            insertsBlock.add(editOp);
          } else {
            inserts.add(editOp);
          }
          if (isBlock(unchangedOrOldParentNode)
              || isSwitchCaseGroupStatementsField(oldListNumber)) {
            deletesBlock.add(editOp);
          } else {
            deletes.add(editOp);
          }
          break;
        case MOVE:
          if (isBlock(unchangedOrOldParentNode)
              || isSwitchCaseGroupStatementsField(oldListNumber)) {
            deletesBlock.add(editOp);
          } else {
            deletes.add(editOp);
          }
          if (isBlock(unchangedOrNewParentNode)
              || isSwitchCaseGroupStatementsField(newListNumber)) {
            insertsBlock.add(editOp);
          } else {
            inserts.add(editOp);
          }
          break;
        case DELETE:
          if (isBlock(unchangedOrOldParentNode)
              || isSwitchCaseGroupStatementsField(oldListNumber)) {
            deletesBlock.add(editOp);
          } else {
            deletes.add(editOp);
          }
          break;
        case UPDATE:
          updates.add(editOp);
          break;
        case INSERT:
          if (isBlock(unchangedOrNewParentNode)) {
            insertsBlock.add(editOp);
          } else {
            inserts.add(editOp);
          }
          break;
        default:
          assert (false);
      }
    }
    final InsertComparator newInsertComparator = new InsertComparator(example2Original);
    final DeleteComparator newDeleteComparator = new DeleteComparator(example1Original);

    Collections.sort(inserts, newInsertComparator);
    Collections.sort(insertsBlock, newInsertComparator);
    Collections.sort(deletes, newDeleteComparator);
    Collections.sort(deletesBlock, newDeleteComparator);

    for (int i = 0; i < insertsBlock.size(); i++) {
      if (DEBUG) {
        print(example2Original);
      }
      BastEditOperation editOp = insertsBlock.get(i);
      switch (editOp.getType()) {
        case ALIGN:
          CombinationHelper.handleReorderingStmt(useVersion, editOp, replaceMap, exDiff, changeMap,
              hierarchy1, hierarchy2, boundary, deleteInsertNodeMap,
              (ArrayList<BastEditOperation>) workList);
          break;
        case INSERT:
          CombinationHelper.handleInsert(useVersion, editOp, replaceMap, exDiff, delInsMap,
              hierarchy1, hierarchy2, boundary, changeMap, revertMap, deleteInsertNodeMap,
              (ArrayList<BastEditOperation>) workList);
          break;
        case MOVE:
          CombinationHelper.handleMoveInsertStep(useVersion, exDiff, (MoveOperation) editOp,
              replaceMap, hierarchy1, hierarchy2, boundary, changeMap, deleteInsertNodeMap,
              (ArrayList<BastEditOperation>) workList);
          break;
        default:
          assert (false);

      }
    }

    for (int i = 0; i < deletesBlock.size(); i++) {
      if (DEBUG) {
        print(example2Original);
        if (i == 9) {
          exDiff.secondToFirstMap.get(workList.get(i).getUnchangedOrNewParentNode());
        }
      }
      BastEditOperation editOp = deletesBlock.get(i);
      switch (editOp.getType()) {
        case DELETE:
          CombinationHelper.handleDelete(useVersion, editOp, replaceMap, exDiff, hierarchy1,
              hierarchy2, delInsMap, boundary, revertMap, changeMap, deleteInsertNodeMap,
              (ArrayList<BastEditOperation>) workList);
          break;
        case MOVE:
        case ALIGN:
          CombinationHelper.handleMoveDeleteStep(useVersion, exDiff, editOp, replaceMap, hierarchy1,
              hierarchy2, boundary, changeMap, deleteInsertNodeMap,
              (ArrayList<BastEditOperation>) workList);
          break;
        default:
          assert (false);

      }
    }



    for (int i = 0; i < deletes.size(); i++) {
      if (DEBUG) {
        print(example2Original);
      }
      BastEditOperation editOp = deletes.get(i);
      switch (editOp.getType()) {
        case DELETE:
          CombinationHelper.handleDelete(useVersion, editOp, replaceMap, exDiff, hierarchy1,
              hierarchy2, delInsMap, boundary, revertMap, changeMap, deleteInsertNodeMap,
              (ArrayList<BastEditOperation>) workList);
          break;
        case MOVE:
          CombinationHelper.handleExprMove(useVersion, exDiff, replaceMap, hierarchy1, hierarchy2,
              boundary, changeMap, revertMap, delInsMap, deleteInsertNodeMap,
              (ArrayList<BastEditOperation>) workList, (MoveOperation) editOp, false);
          break;
        case ALIGN:
          break;
        default:
          assert (false);

      }
    }

    for (int i = 0; i < inserts.size(); i++) {
      if (DEBUG) {
        print(example2Original);

      }
      BastEditOperation editOp = inserts.get(i);
      switch (editOp.getType()) {
        case ALIGN:
          CombinationHelper.handleReorderingStmt(useVersion, editOp, replaceMap, exDiff, changeMap,
              hierarchy1, hierarchy2, boundary, deleteInsertNodeMap,
              (ArrayList<BastEditOperation>) workList);
          break;
        case INSERT:
          CombinationHelper.handleInsert(useVersion, editOp, replaceMap, exDiff, delInsMap,
              hierarchy1, hierarchy2, boundary, changeMap, revertMap, deleteInsertNodeMap,
              (ArrayList<BastEditOperation>) workList);
          break;
        case MOVE:
          CombinationHelper.handleExprMove(useVersion, exDiff, replaceMap, hierarchy1, hierarchy2,
              boundary, changeMap, revertMap, delInsMap, deleteInsertNodeMap,
              (ArrayList<BastEditOperation>) workList, (MoveOperation) editOp, true);
          break;
        default:
          assert (false);

      }
    }

    for (int i = 0; i < updates.size(); i++) {
      if (DEBUG) {
        print(example2Original);
      }
      BastEditOperation editOp = updates.get(i);
      CombinationHelper.handleUpdate(useVersion, exDiff, editOp, deleteInsertNodeMap);
    }

    print(example2Original);
    DefaultFieldVisitor cv = null;
    ReplaceMap visitorMap = null;
    if (useVersion) {
      cv = new CombineWuvisitor(replaceMap, boundary, cwuv, false, AresUseStmt.TAG,
          containsAresBlock, changeMap, exDiff);
      example2Original.accept(cv);
      visitorMap = ((CombineWuvisitor) cv).replaceMap;
    } else {

      cv = new CombineWuvisitor(replaceMap, boundary, cwuv, false, AresWildcard.TAG,
          containsAresBlock, changeMap, exDiff);
      example2Original.accept(cv);
      visitorMap = ((CombineWuvisitor) cv).replaceMap;
    }
    print(example2Original);
    return new InsertResult(example2Original, visitorMap);
  }

  private static boolean isBlock(AbstractBastNode unchangedOrNewParentNode) {
    return unchangedOrNewParentNode.getTag() == BastBlock.TAG;
  }

  private static boolean isSwitchCaseGroupStatementsField(BastFieldConstants newListNumber) {
    return newListNumber == BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS;
  }

  private static BastFieldConstants getOldListNumber(BastEditOperation editOp) {
    return editOp.getOldOrChangedIndex().childrenListNumber;
  }

  private static BastFieldConstants getNewListNumber(BastEditOperation editOp) {
    return editOp.getNewOrChangedIndex().childrenListNumber;
  }

  private static boolean[] findConsistentVarRename(final List<BastEditOperation> editScript,
      HashMap<String, String> identifierMap, ExtendedDiffResult exDiff) {
    boolean[] ignoreRename = new boolean[editScript.size()];
    for (int i = 0; i < editScript.size(); i++) {
      switch (editScript.get(i).getType()) {
        case INSERT:
          InsertOperation iop = (InsertOperation) editScript.get(i);
          if (iop.getNewOrChangedNode().getTag() == BastNameIdent.TAG) {

            AbstractBastNode oldParent =
                exDiff.firstToSecondMap.get(iop.getUnchangedOrNewParentNode());
            AbstractBastNode oldParent2 =
                exDiff.firstToSecondMap.get(iop.getUnchangedOrNewParentNode());
          }
          break;
        case UPDATE:
          UpdateOperation uop = (UpdateOperation) editScript.get(i);
          if (uop.getOldOrInsertedNode().getTag() == BastNameIdent.TAG
              && uop.getNewOrChangedNode().getTag() == BastNameIdent.TAG) {
            String name =
                identifierMap.get(((BastNameIdent) uop.getOldOrInsertedNode()).toString());
            if (name == null) {
              identifierMap.put(((BastNameIdent) uop.getOldOrInsertedNode()).toString(),
                  ((BastNameIdent) uop.getNewOrChangedNode()).toString());
              ignoreRename[i] = true;
            } else {
              if (name.equals(((BastNameIdent) uop.getNewOrChangedNode()).toString())) {
                ignoreRename[i] = true;
                continue;
              }
            }
          }
          break;
        case VARIABLE_RENAME:
          VariableRenamingOperation vrop = (VariableRenamingOperation) editScript.get(i);
          if (vrop.getOldOrInsertedNode().getTag() == BastNameIdent.TAG
              && vrop.getNewOrChangedNode().getTag() == BastNameIdent.TAG) {
            String name =
                identifierMap.get(((BastNameIdent) vrop.getOldOrInsertedNode()).toString());
            if (name == null) {
              identifierMap.put(((BastNameIdent) vrop.getOldOrInsertedNode()).toString(),
                  ((BastNameIdent) vrop.getNewOrChangedNode()).toString());
              ignoreRename[i] = true;
            } else {
              if (name.equals(((BastNameIdent) vrop.getNewOrChangedNode()).toString())) {
                ignoreRename[i] = true;
                continue;
              }
            }
          }
          break;
        case METHOD_RENAME:
          MethodRenamingOperation mrop = (MethodRenamingOperation) editScript.get(i);
          if (mrop.getOldOrInsertedNode().getTag() == BastNameIdent.TAG
              && mrop.getNewOrChangedNode().getTag() == BastNameIdent.TAG) {
            String name =
                identifierMap.get(((BastNameIdent) mrop.getOldOrInsertedNode()).toString());
            if (name == null) {
              identifierMap.put(((BastNameIdent) mrop.getOldOrInsertedNode()).toString(),
                  ((BastNameIdent) mrop.getNewOrChangedNode()).toString());
              ignoreRename[i] = true;
            } else {
              if (name.equals(((BastNameIdent) mrop.getNewOrChangedNode()).toString())) {
                ignoreRename[i] = true;
                continue;
              }
            }
          }
          break;
        case PARAMETER_RENAME:
          ParameterRenamingOperation prop = (ParameterRenamingOperation) editScript.get(i);
          if (prop.getOldOrInsertedNode().getTag() == BastNameIdent.TAG
              && prop.getNewOrChangedNode().getTag() == BastNameIdent.TAG) {
            String name =
                identifierMap.get(((BastNameIdent) prop.getOldOrInsertedNode()).toString());
            if (name == null) {
              identifierMap.put(((BastNameIdent) prop.getOldOrInsertedNode()).toString(),
                  ((BastNameIdent) prop.getNewOrChangedNode()).toString());
              ignoreRename[i] = true;
            } else {
              if (name.equals(((BastNameIdent) prop.getNewOrChangedNode()).toString())) {
                ignoreRename[i] = true;
                continue;
              }
            }
          }
          break;
        default:
          break;
      }
    }
    return ignoreRename;
  }

  private static NodeParentInformationHierarchy findParentHierarchy(ExtendedDiffResult exDiff,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyB1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyA1, boolean[] insideMethodBA1,
      AbstractBastNode rootFirst) throws GeneralizationException {
    NodeParentInformationHierarchy parentBA1 = null;
    for (int i = 0; i < exDiff.editScript.size(); i++) {
      if (insideMethodBA1[i]) {
        parentBA1 = NodeParentInformationHierarchy.sharedParentHierarchy(parentBA1,
            PatternGenerator.getParentHierarchy(exDiff.editScript.get(i), hierarchyB1, hierarchyA1,
                exDiff.secondToFirstMap),
            hierarchyB1);
      }
    }

    if (parentBA1 == null || parentBA1.getNode().getTag() == BastClassDecl.TAG) {
      FindNodesFromTagVisitor fnft2 = new FindNodesFromTagVisitor(BastFunction.TAG);
      rootFirst.accept(fnft2);
      if (fnft2.nodes.get(0).getField(BastFieldConstants.FUNCTION_BLOCK_STATEMENTS).getListField()
          .size() == 0) {
        throw new GeneralizationException("Empty body!");
      }
      parentBA1 = hierarchyB1.get((BastBlock) fnft2.nodes.get(0)
          .getField(BastFieldConstants.FUNCTION_BLOCK_STATEMENTS).getListField().get(0));

    }
    return parentBA1;
  }

  private static boolean[] identifyEditScriptsInMethods(ExtendedDiffResult exDiff,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyOld,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyNew, boolean defaultValue

  ) {
    boolean[] insideMethodBA1 = new boolean[exDiff.editScript.size()];
    for (int i = 0; i < exDiff.editScript.size(); i++) {
      insideMethodBA1[i] = defaultValue;
      NodeParentInformationHierarchy tmpParent = getParentHierarchy(exDiff.editScript.get(i),
          hierarchyOld, hierarchyNew, exDiff.secondToFirstMap);
      NodeParentInformationHierarchy tmpParentNew = null;
      if (exDiff.editScript.get(i).getType() == EditOperationType.INSERT
          || exDiff.editScript.get(i).getType() == EditOperationType.FINAL_INSERT) {
        tmpParentNew = getNewParentHierarchy(exDiff.editScript.get(i), hierarchyNew);
      }
      if (exDiff.editScript.get(i).getType() == EditOperationType.MOVE) {
        tmpParentNew = getNewParentHierarchy(exDiff.editScript.get(i), hierarchyNew);
      }
      if (tmpParent != null) {
        insideMethodBA1[i] = tmpParent.insideMethod();
      }
      if (tmpParentNew != null) {
        insideMethodBA1[i] = tmpParentNew.insideMethod();
      }

    }
    return insideMethodBA1;
  }

  /**
   * Gets the new parent hierarchy.
   *
   * @param editOp the edit op
   * @param hierarchyMap the hierarchy map
   * @return the new parent hierarchy
   */
  public static NodeParentInformationHierarchy getNewParentHierarchy(BastEditOperation editOp,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyMap) {
    AbstractBastNode node = null;
    switch (editOp.getType()) {
      case DELETE:
      case STATEMENT_DELETE:
      case DECREASING_ACCESS:
      case FIELD_DELETE:
      case FINAL_DELETE:
      case METHOD_DELETE:
      case PARAMETER_DELETE:
      case PARENT_CLASS_DELETE:
      case VARIABLE_DELETE:
      case INCREASING_ACCESS:
        node = null;
        break;
      case MOVE:
      case STATEMENT_PARENT_CHANGE:
        node = ((AbstractBastNode) (editOp).getUnchangedOrNewParentNode());
        break;
      case METHOD_INSERT:
      case STATEMENT_REORDERING:
      case VARIABLE_INSERT:
      case STATEMENT_INSERT:
      case INSERT:
      case ALIGN:
      case FIELD_INSERT:
      case PARAMETER_INSERT:
      case PARAMETER_REORDERING:
        node = ((AbstractBastNode) (editOp).getUnchangedOrOldParentNode());
        break;
      case FINAL_INSERT:
      case PARENT_CLASS_INSERT:
      case METHOD_RENAME:
      case UPDATE:
      case CLASS_RENAME:
      case FIELD_RENAME:
      case FIELD_TYPE_UPDATE:
      case PARAMETER_RENAME:
      case PARAMETER_TYPE_UPDATE:
      case RETURN_TYPE_UPDATE:
      case STATEMENT_UPDATE:
      case VARIABLE_RENAME:
      case VARIABLE_TYPE_UPDATE:
        node = editOp.getNewOrChangedNode();
        break;

      default:
        if (DEBUG) {
          System.err.println("Type missing: " + editOp.getType().niceName);
        }
        assert (false);

    }
    return hierarchyMap.get(node);
  }

  /**
   * Gets the parent hierarchy.
   *
   * @param editOp the edit op
   * @param hierarchyMap1 the hierarchy map 1
   * @param hierarchyMap2 the hierarchy map 2
   * @param treeMap the tree map
   * @return the parent hierarchy
   */
  public static NodeParentInformationHierarchy getParentHierarchy(BastEditOperation editOp,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyMap1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyMap2,
      Map<AbstractBastNode, AbstractBastNode> treeMap) {
    AbstractBastNode node = null;
    switch (editOp.getType()) {
      case INSERT:
        node = ((AbstractBastNode) ((InsertOperation) editOp).getUnchangedOrOldParentNode());
        if ((AbstractBastNode) treeMap.get(node) == null) {
          NodeParentInformationHierarchy nodeHierarchy = hierarchyMap2.get(node);
          if (nodeHierarchy != null) {
            for (NodeParentInformation npi : nodeHierarchy.list) {
              if (treeMap.get(npi.parent) != null) {
                node = (AbstractBastNode) treeMap.get(npi.parent);
                break;
              }
            }
          }
        } else {
          node = (AbstractBastNode) treeMap.get(node);
        }

        assert (node != null);
        break;
      case VARIABLE_INSERT:
      case VARIABLE_DELETE:
      case DECREASING_ACCESS:
      case INCREASING_ACCESS:
      case STATEMENT_PARENT_CHANGE:
      case PARAMETER_REORDERING:
        node = editOp.getUnchangedOrOldParentNode();
        break;
      case DELETE:
      case MOVE:
      case ALIGN:
      case UPDATE:
      case CLASS_RENAME:
      case METHOD_RENAME:
      case FINAL_INSERT:
      case FINAL_DELETE:
      case STATEMENT_REORDERING:
      case PARENT_CLASS_DELETE:
      case PARENT_CLASS_INSERT:
      case STATEMENT_DELETE:
      case STATEMENT_INSERT:
      case METHOD_DELETE:
      case METHOD_INSERT:
      case VARIABLE_RENAME:
      case FIELD_DELETE:
      case FIELD_INSERT:
      case FIELD_RENAME:
      case FIELD_TYPE_UPDATE:
      case PARAMETER_DELETE:
      case PARAMETER_INSERT:
      case PARAMETER_RENAME:
      case PARAMETER_TYPE_UPDATE:
      case RETURN_TYPE_UPDATE:
      case STATEMENT_UPDATE:
      case VARIABLE_TYPE_UPDATE:
        node = editOp.getOldOrInsertedNode();
        break;

      default:
        if (DEBUG) {
          System.err.println("Type missing: " + editOp.getType().niceName);
        }
        assert (false);

    }
    return hierarchyMap1.get(node);
  }
}
