package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;
import de.fau.cs.inf2.cas.ares.pcreation.visitors.CollectCorrespondingNodesVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformation;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastForStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunctionParameterDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameter;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitchCaseGroup;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeSpecifier;
import de.fau.cs.inf2.cas.common.bast.type.BastBasicType;
import de.fau.cs.inf2.cas.common.bast.type.BastClassType;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.DeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.MoveOperation;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class RemoveStructureNodeChanges extends AbstractFilterRule {

  public RemoveStructureNodeChanges() {
    super(FilterRuleType.REMOVE_STRUCTURE_NODE_CHANGES);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    worklist = handleStandardCases(worklist);
    worklist = handleDeclaratorInsert(worklist, exDiffCurrent, hierarchySecond, delInsertMap);
    worklist = handleSwitch(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);
    worklist = handleParameter(worklist);
    worklist = handleClassType(worklist);
    worklist = handleFunction(worklist);
    return handleIdentDeclarator(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);






  }

  private static List<BastEditOperation> handleStandardCases(List<BastEditOperation> workList) {
    ArrayList<BastEditOperation> toRemove = new ArrayList<>();
    for (int i = 0; i < workList.size(); i++) {
      switch (workList.get(i).getType()) {
        case INSERT:
          break;
        case DELETE:
          DeleteOperation delOp = (DeleteOperation) workList.get(i);
          if (delOp.getOldOrInsertedNode().getTag() == BastFunctionParameterDeclarator.TAG) {
            toRemove.add(workList.get(i));
          }
          break;
        case MOVE:
          break;
        case ALIGN:
          break;
        case UPDATE:
          break;
        default:
      }
    }
    for (BastEditOperation toR : toRemove) {
      workList.remove(toR);
    }
    return workList;
  }

  private static List<BastEditOperation> handleDeclaratorInsert(List<BastEditOperation> editScript,
      ExtendedDiffResult extDiffBb,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond,
      Map<AbstractBastNode, AbstractBastNode> delInsMap) {
    ArrayList<InsertOperation> inserts = new ArrayList<>();
    ArrayList<InsertOperation> otherInserts = new ArrayList<>();
    for (BastEditOperation ep : editScript) {
      switch (ep.getType()) {
        case INSERT:
          if (ep.getOldOrInsertedNode().getTag() == BastIdentDeclarator.TAG) {
            NodeParentInformationHierarchy npi =
                hierarchySecond.get(ep.getUnchangedOrOldParentNode());

            if (npi.list.get(0).parent.getTag() == BastForStmt.TAG) {
              otherInserts.add((InsertOperation) ep);
            } else {
              inserts.add((InsertOperation) ep);
              delInsMap.put(((BastIdentDeclarator) ep.getOldOrInsertedNode()).identifier,
                  ((BastIdentDeclarator) ep.getOldOrInsertedNode()).identifier);
            }
          }

          break;
        default:
          break;
      }
    }
    editScript.removeAll(inserts);
    ArrayList<BastEditOperation> toRemove = new ArrayList<>();
    for (InsertOperation viop : inserts) {
      if (viop.getUnchangedOrOldParentNode().getTag() != BastDeclaration.TAG) {
        continue;
      }
      BastDeclaration decl = (BastDeclaration) viop.getUnchangedOrOldParentNode();
      NodeParentInformationHierarchy npi = hierarchySecond.get(decl);
      assert (npi != null);
      NodeParentInformation np = npi.list.get(0);
      CollectCorrespondingNodesVisitor ccn =
          new CollectCorrespondingNodesVisitor(extDiffBb.secondToFirstMap);
      if (np.parent.getField(np.fieldConstant).isList()) {
        @SuppressWarnings("unchecked")
        LinkedList<AbstractBastStatement> stmts = (LinkedList<AbstractBastStatement>) np.parent
            .getField(np.fieldConstant).getListField();
        AbstractBastNode stmt = stmts.get(np.listId);
        stmt.accept(ccn);
        for (BastEditOperation ep : editScript) {
          switch (ep.getType()) {
            case DELETE:
              DeleteOperation del = (DeleteOperation) ep;
              if (ccn.nodes.contains(del.getOldOrInsertedNode())
                  || ccn.nodes.contains(del.getUnchangedOrOldParentNode())) {
                toRemove.add(ep);
              }

              break;
            default:
              break;
          }
        }
      }
    }
    for (BastEditOperation epInsert : otherInserts) {
      for (BastEditOperation ep : editScript) {
        switch (ep.getType()) {
          case INSERT:
            if (ep.getUnchangedOrOldParentNode() == epInsert.getOldOrInsertedNode()) {
              toRemove.add(ep);
            }
            break;
          default:
            continue;
        }
      }
    }
    editScript.removeAll(toRemove);

    return editScript;
  }
  

  private static List<BastEditOperation> handleSwitch(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    List<BastEditOperation> toRemove = new LinkedList<>();
    List<BastEditOperation> toAdd = new LinkedList<>();

    for (BastEditOperation editOp : workList) {
      if (editOp.getType() == EditOperationType.MOVE
          || editOp.getType() == EditOperationType.DELETE) {
        if (editOp.getUnchangedOrOldParentNode().getTag() == BastBlock.TAG) {
          NodeParentInformationHierarchy npi =
              hierarchyFirst.get(editOp.getUnchangedOrOldParentNode());
          if (npi != null && npi.list.size() > 0) {
            if (npi.list.get(0).parent.getTag() == BastSwitchCaseGroup.TAG) {
              BastEditOperation newOp = null;
              if (editOp.getType() == EditOperationType.MOVE) {
                newOp = new MoveOperation(npi.list.get(0).parent,
                    editOp.getUnchangedOrNewParentNode(), editOp.getOldOrInsertedNode(),
                    editOp.getNewOrChangedNode(),
                    new NodeIndex(BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS,
                        editOp.getOldOrChangedIndex().childrenListIndex),
                    new NodeIndex(BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS,
                        editOp.getNewOrChangedIndex().childrenListIndex));
              } else {
                newOp = new DeleteOperation(npi.list.get(0).parent, editOp.getOldOrInsertedNode(),
                    new NodeIndex(BastFieldConstants.SWITCH_CASE_GROUP_STATEMENTS,
                        editOp.getOldOrChangedIndex().childrenListIndex));
              }

              toAdd.add(newOp);
              toRemove.add(newOp);
            }
          }
        }
      }

    }
    workList.removeAll(toRemove);
    workList.addAll(toAdd);
    return workList;
  }

  private static List<BastEditOperation> handleParameter(List<BastEditOperation> editScript) {
    ArrayList<BastEditOperation> workList = new ArrayList<>();

    for (BastEditOperation ep : editScript) {
      switch (ep.getType()) {
        case DELETE:
          if (ep.getOldOrInsertedNode().getTag() == BastParameter.TAG) {
            continue;
          }
          workList.add(ep);
          break;
        case MOVE:
          if (ep.getUnchangedOrOldParentNode().getTag() == BastParameter.TAG
              || ep.getUnchangedOrNewParentNode().getTag() == BastParameter.TAG) {
            continue;
          }
          workList.add(ep);
          break;
        default:
          workList.add(ep);
      }
    }

    return workList;
  }

  private static List<BastEditOperation> handleClassType(List<BastEditOperation> editList) {
    ArrayList<BastEditOperation> workList = new ArrayList<>();

    for (BastEditOperation ep : editList) {
      switch (ep.getType()) {
        case INSERT:
          if (ep.getOldOrInsertedNode().getTag() == BastClassType.TAG && ep
              .getOldOrChangedIndex().childrenListNumber == BastFieldConstants.NEW_CLASS_TYPE) {
            continue;
          }
          workList.add(ep);
          break;
        case DELETE:
          if (ep.getOldOrInsertedNode().getTag() == BastClassType.TAG && ep
              .getOldOrChangedIndex().childrenListNumber == BastFieldConstants.NEW_CLASS_TYPE) {
            continue;
          }
          workList.add(ep);
          break;
        default:
          workList.add(ep);
      }
    }

    return workList;
  }

  private static List<BastEditOperation> handleFunction(List<BastEditOperation> editList) {
    ArrayList<BastEditOperation> workList = new ArrayList<>();

    for (BastEditOperation ep : editList) {
      switch (ep.getType()) {
        case INSERT:
          if (ep.getOldOrInsertedNode().getTag() == BastFunction.TAG) {
            continue;
          }
          workList.add(ep);
          break;
        case DELETE:
          if (ep.getOldOrInsertedNode().getTag() == BastFunction.TAG) {
            continue;
          }
          workList.add(ep);
          break;
        default:
          workList.add(ep);
      }
    }

    return workList;
  }

  private static List<BastEditOperation> handleIdentDeclarator(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    LinkedList<BastEditOperation> toAdd = new LinkedList<>();
    LinkedList<BastEditOperation> toRemove = new LinkedList<>();

    for (BastEditOperation ep : workList) {
      if (ep.getType() != EditOperationType.UPDATE) {
        if (ep.getOldOrInsertedNode().getTag() == BastIdentDeclarator.TAG) {
          if (ep.getUnchangedOrOldParentNode().getTag() == BastDeclaration.TAG) {
            BastDeclaration decl = (BastDeclaration) ep.getUnchangedOrOldParentNode();
            if (decl.specifierList.size() == 1 && decl.declaratorList.size() == 1) {
              if (((BastTypeSpecifier) decl.specifierList.get(0)).type
                  .getTag() == BastBasicType.TAG) {
                if (ep.getType() == EditOperationType.MOVE) {
                  if (!WildcardAccessHelper.isEqual(ep.getOldOrInsertedNode(),
                      ep.getNewOrChangedNode())) {
                    NodeParentInformationHierarchy npiOld =
                        hierarchyFirst.get(ep.getUnchangedOrOldParentNode());
                    NodeParentInformationHierarchy npiNew =
                        hierarchySecond.get(ep.getUnchangedOrNewParentNode());
                    if (npiOld != null && npiNew != null && npiOld.list.size() > 0
                        && npiNew.list.size() > 0) {
                      MoveOperation mop =
                          new MoveOperation(npiOld.list.get(0).parent, npiNew.list.get(0).parent,
                              ep.getUnchangedOrOldParentNode(), ep.getUnchangedOrNewParentNode(),
                              new NodeIndex(npiOld.list.get(0).fieldConstant,
                                  npiOld.list.get(0).listId),
                              new NodeIndex(npiNew.list.get(0).fieldConstant,
                                  npiNew.list.get(0).listId));
                      toAdd.add(mop);
                      toRemove.add(ep);
                    }
                  }
                } else {
                  toRemove.add(ep);
                  AbstractBastNode parentNode = null;
                  NodeIndex index = null;
                  if (hierarchySecond.get(decl) != null) {
                    parentNode = hierarchySecond.get(decl).list.get(0).parent;

                    index = new NodeIndex(hierarchySecond.get(decl).list.get(0).fieldConstant,
                        hierarchySecond.get(decl).list.get(0).listId);

                  } else {
                    parentNode = hierarchyFirst.get(decl).list.get(0).parent;
                    index = new NodeIndex(hierarchyFirst.get(decl).list.get(0).fieldConstant,
                        hierarchyFirst.get(decl).list.get(0).listId);

                  }
                  DeleteOperation newDelOp = new DeleteOperation(parentNode, decl, index);
                  toAdd.add(newDelOp);
                }

              }
            }
          }
        }
      }
    }
    workList.removeAll(toRemove);
    workList.addAll(toAdd);
    return workList;
  }

  
  public static FilterRule getInstance() {
    return new RemoveStructureNodeChanges();
  }
}