package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.pcreation.Filter;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.CreateJavaNodeHelper;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameter;
import de.fau.cs.inf2.cas.common.bast.type.BastClassType;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.DeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.MoveOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

public class AffectedNodesOutsideMethodBlock extends AbstractFilterRule {

  public AffectedNodesOutsideMethodBlock() {
    super(FilterRuleType.AFFECT_NODES_OUTSIDE_METHOD_BLOCK);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    worklist = normalOutOfMethodEdits(opsFirstToSecond, insideMethod, worklist);
    worklist = parameterRenames(worklist, exDiffCurrent, variableMap, delInsertMap);
    return classOrMethodRenames(worklist, exDiffCurrent, variableMap);

  }

  private static List<BastEditOperation> normalOutOfMethodEdits(
      List<BastEditOperation> original1original2, boolean[] insideMethodBb,
      List<BastEditOperation> workList) {
    List<BastEditOperation> toTransform = new LinkedList<BastEditOperation>();
    List<BastEditOperation> itemsToRemove =
        getOutOfMethodEdits(original1original2, insideMethodBb, toTransform);
    workList.removeAll(itemsToRemove);
    for (BastEditOperation ep : toTransform) {
      if (ep.getType() == EditOperationType.MOVE) {
        BastFieldConstants childrenListNumber = ep.getNewOrChangedIndex().childrenListNumber;
        if (childrenListNumber == BastFieldConstants.FUNCTION_BLOCK_STATEMENTS) {
          DeleteOperation del = new DeleteOperation(ep.getUnchangedOrOldParentNode(),
              ep.getOldOrInsertedNode(), ep.getOldOrChangedIndex());
          workList.add(del);
        }
      }
    }
    return workList;
  }

  public static FilterRule getInstance() {
    return new AffectedNodesOutsideMethodBlock();
  }
  
  /**
   * Gets the out of method edits.
   *
   * @param original1original2 the original 1 original 2
   * @param insideMethodBb the inside method bb
   * @param toTransform the to transform
   * @return the out of method edits
   */
  public static List<BastEditOperation> getOutOfMethodEdits(
      final List<BastEditOperation> original1original2, boolean[] insideMethodBb,
      List<BastEditOperation> toTransform) {
    List<BastEditOperation> itemsToRemove = new ArrayList<BastEditOperation>();
    for (int i = 0; i < original1original2.size(); i++) {
      if (!insideMethodBb[i]) {
        BastFieldConstants childrenListNumber =
            original1original2.get(i).getNewOrChangedIndex().childrenListNumber;
        BastFieldConstants childrenListNumber2 =
            original1original2.get(i).getOldOrChangedIndex().childrenListNumber;
        if (original1original2.get(i).getType() == EditOperationType.MOVE
            && childrenListNumber == BastFieldConstants.FUNCTION_BLOCK_STATEMENTS
            && childrenListNumber2 != BastFieldConstants.FUNCTION_BLOCK_STATEMENTS) {
          toTransform.add(original1original2.get(i));
        }
        itemsToRemove.add(original1original2.get(i));

      }
    }
    
    return itemsToRemove;

  }

  /**
   * Parameter renames.
   *
   * @param parameter the parameter
   * @param extDiff the ext diff
   * @param variableMap the variable map
   * @param delInsertMap the del insert map
   * @return the list
   */
  public static List<BastEditOperation> parameterRenames(List<BastEditOperation> parameter,
      ExtendedDiffResult extDiff, HashMap<String, String> variableMap,
      HashMap<AbstractBastNode, AbstractBastNode> delInsertMap) {
    ArrayList<InsertOperation> insertList = new ArrayList<>();
    ArrayList<DeleteOperation> deleteList = new ArrayList<>();
    HashMap<InsertOperation, DeleteOperation> mapInsertDelete = new HashMap<>();
    for (BastEditOperation ep : parameter) {
      switch (ep.getType()) {
        case INSERT:
          InsertOperation iop = (InsertOperation) ep;
          if (iop.getOldOrInsertedNode().getTag() == BastParameter.TAG) {
            insertList.add(iop);
          }
          break;
        case DELETE:
          DeleteOperation dop = (DeleteOperation) ep;
          if (dop.getOldOrInsertedNode().getTag() == BastParameter.TAG) {
            deleteList.add(dop);
          }
          break;
        default:
          break;
        case UPDATE:
          if (delInsertMap != null && ep.getOldOrInsertedNode().getTag() == BastParameter.TAG) {
            delInsertMap.put(
                CreateJavaNodeHelper
                    .createBastNameIdent(((BastParameter) ep.getOldOrInsertedNode()).name),
                CreateJavaNodeHelper
                    .createBastNameIdent(((BastParameter) ep.getNewOrChangedNode()).name));
          }
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
              BastParameter insNode = (BastParameter) ip.getOldOrInsertedNode();
              BastParameter delNode = (BastParameter) ep.getOldOrInsertedNode();
              if (insNode.declarator.getTag() == BastIdentDeclarator.TAG
                  && delNode.declarator.getTag() == BastIdentDeclarator.TAG) {
                BastIdentDeclarator insDec = (BastIdentDeclarator) insNode.declarator;
                BastIdentDeclarator delDec = (BastIdentDeclarator) delNode.declarator;
                if (variableMap.get(insDec.identifier.getName()) == null) {
                  if (variableMap.get(delDec.identifier.getName()) == null) {
                    variableMap.put(delDec.identifier.getName(), insDec.identifier.getName());
                    mapInsertDelete.put(ip, ep);
                  } else {
                    if (variableMap.get(delDec.identifier.getName())
                        .equals(insDec.identifier.getName())) {
                      mapInsertDelete.put(ip, ep);
                    }
                  }
                } else if (variableMap.get(insDec.identifier.getName())
                    .equals(delDec.identifier.getName())) {
                  mapInsertDelete.put(ip, ep);
                  break;
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

  private static List<BastEditOperation> classOrMethodRenames(List<BastEditOperation> classes,
      ExtendedDiffResult extDiff, HashMap<String, String> variableMap) {
    ArrayList<InsertOperation> insertList = new ArrayList<>();
    ArrayList<DeleteOperation> deleteList = new ArrayList<>();
    HashMap<InsertOperation, DeleteOperation> mapInsertDelete = new HashMap<>();
    for (BastEditOperation ep : classes) {
      switch (ep.getType()) {
        case INSERT:
          InsertOperation iop = (InsertOperation) ep;
          if (iop.getOldOrInsertedNode().getTag() == BastClassType.TAG) {
            insertList.add(iop);
          }
          break;
        case DELETE:
          DeleteOperation dop = (DeleteOperation) ep;
          if (dop.getOldOrInsertedNode().getTag() == BastClassType.TAG) {
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
              BastClassType insNode = (BastClassType) ip.getOldOrInsertedNode();
              BastClassType delNode = (BastClassType) ep.getOldOrInsertedNode();
              if (insNode.name.getTag() == BastNameIdent.TAG
                  && delNode.name.getTag() == BastNameIdent.TAG) {
                BastNameIdent insDec = (BastNameIdent) insNode.name;
                BastNameIdent delDec = (BastNameIdent) delNode.name;
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
    ArrayList<AbstractBastNode> nodeWorkList = new ArrayList<>();
    for (Entry<InsertOperation, DeleteOperation> entry : mapInsertDelete.entrySet()) {
      nodeWorkList.add((AbstractBastNode) entry.getKey().getOldOrInsertedNode());
      nodeWorkList.add((AbstractBastNode) entry.getValue().getOldOrInsertedNode());
      classes.remove(entry.getKey());
      classes.remove(entry.getValue());
    }
    boolean changed = true;
    while (changed) {
      changed = false;
      Iterator<BastEditOperation> it = classes.iterator();
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
    ArrayList<BastEditOperation> tmpWorkList = new ArrayList<>();

    for (BastEditOperation ep : classes) {
      switch (ep.getType()) {

        case METHOD_RENAME:
          break;
        default:
          tmpWorkList.add(ep);
      }

    }
    return tmpWorkList;
  }

  
}