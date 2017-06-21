package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAsgnExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeSpecifier;
import de.fau.cs.inf2.cas.common.bast.type.BastBasicType;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.DeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TransformDeclarationChanges extends AbstractFilterRule {

  public TransformDeclarationChanges() {
    super(FilterRuleType.TRANSFORM_DECLARATION_CHANGES);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    worklist = standardDeclarationChanges(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);
    return declarationDeletes(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);

  }

  private static List<BastEditOperation> standardDeclarationChanges(
      List<BastEditOperation> editList, ExtendedDiffResult extDiff,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond

  ) {
    ArrayList<BastEditOperation> insertedDeclaration = new ArrayList<>();
    ArrayList<AbstractBastNode> remainingDeclaration = new ArrayList<>();
    for (BastEditOperation ep : extDiff.editScript) {
      switch (ep.getType()) {
        case INSERT:
          if (ep.getOldOrInsertedNode().getTag() == BastDeclaration.TAG) {
            insertedDeclaration.add(ep);
          }
          break;
        case DELETE:
          if (ep.getOldOrInsertedNode().getTag() == BastDeclaration.TAG) {
            insertedDeclaration.add(ep);
          }
          break;
        case MOVE:
        case ALIGN:
        case STATEMENT_REORDERING:
          if (ep.getNewOrChangedNode().getTag() == BastDeclaration.TAG) {
            if (ep.getOldOrInsertedNode().getTag() == BastDeclaration.TAG) {
              insertedDeclaration.add(ep);
            }
          }
          break;
        default:
          break;
      }

    }
    for (BastEditOperation ep : editList) {
      switch (ep.getType()) {
        case INSERT:
          if (ep.getOldOrInsertedNode().getTag() == BastAsgnExpr.TAG) {
            remainingDeclaration.add(ep.getOldOrInsertedNode());
          }
          break;
        default:
          break;
      }
    }
    ArrayList<BastEditOperation> toRemove = new ArrayList<>();
    ArrayList<BastEditOperation> toAdd = new ArrayList<>();
    ArrayList<BastEditOperation> workList = new ArrayList<>();

    for (BastEditOperation declarationOp : insertedDeclaration) {
      boolean name = false;
      boolean type = false;
      boolean expression = false;
      BastNameIdent ident = null;
      BastEditOperation epName = null;
      BastEditOperation epType = null;
      BastEditOperation epExpression = null;
      BastEditOperation epDecl = null;
      BastEditOperation epMod = null;
      BastEditOperation epIdent = null;
      for (BastEditOperation ep : editList) {
        if (ep.getType() == EditOperationType.INSERT) {
          if (declarationOp.getNewOrChangedNode().getField(BastFieldConstants.DECLARATION_SPECIFIER)
              .getListField().contains(ep.getOldOrInsertedNode())) {
            type = true;
            epType = ep;
          } else if (declarationOp.getNewOrChangedNode()
              .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField().get(0)
              .getTag() == BastIdentDeclarator.TAG) {
            if (((BastIdentDeclarator) declarationOp.getNewOrChangedNode()
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0)).identifier == ep.getOldOrInsertedNode()) {
              name = true;
              ident = (BastNameIdent) ep.getOldOrInsertedNode();
              epName = ep;
            } else if (((BastIdentDeclarator) declarationOp.getNewOrChangedNode()
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0)).expression == ep.getOldOrInsertedNode()) {
              expression = true;
              epExpression = ep;
            } else if (((BastIdentDeclarator) declarationOp.getNewOrChangedNode()
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0)).expression != null
                && ((BastIdentDeclarator) declarationOp.getNewOrChangedNode()
                    .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                    .get(0)).expression.getTag() == BastExprInitializer.TAG) {
              if (((BastExprInitializer) ((BastIdentDeclarator) declarationOp.getNewOrChangedNode()
                  .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                  .get(0)).expression).init == ep.getOldOrInsertedNode()) {
                expression = true;
                epExpression = ep;
              }
            }
          } else if (declarationOp.getNewOrChangedNode()
              .getField(BastFieldConstants.DECLARATION_MODIFIERS).getListField()
              .contains(ep.getOldOrInsertedNode())) {
            epMod = ep;
          }
        } else if (ep.getType() == EditOperationType.DELETE) {
          if (declarationOp.getOldOrInsertedNode()
              .getField(BastFieldConstants.DECLARATION_SPECIFIER).getListField()
              .contains(ep.getOldOrInsertedNode())) {
            type = true;
            epType = ep;
          } else if (declarationOp.getOldOrInsertedNode()
              .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField().get(0)
              .getTag() == BastIdentDeclarator.TAG) {
            if (((BastIdentDeclarator) declarationOp.getOldOrInsertedNode()
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0)).identifier == ep.getOldOrInsertedNode()) {
              name = true;
              ident = (BastNameIdent) ep.getOldOrInsertedNode();
              epName = ep;
            } else if (((BastIdentDeclarator) declarationOp.getOldOrInsertedNode()
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0)).expression == ep.getOldOrInsertedNode()) {
              expression = true;
              epExpression = ep;
            } else if (((BastIdentDeclarator) declarationOp.getOldOrInsertedNode()
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0)).expression != null
                && ((BastIdentDeclarator) declarationOp.getOldOrInsertedNode()
                    .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                    .get(0)).expression.getTag() == BastExprInitializer.TAG) {
              if (((BastExprInitializer) ((BastIdentDeclarator) declarationOp.getOldOrInsertedNode()
                  .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                  .get(0)).expression).init == ep.getOldOrInsertedNode()) {
                expression = true;
                epExpression = ep;
              }
            }
          } else if (declarationOp.getOldOrInsertedNode()
              .getField(BastFieldConstants.DECLARATION_MODIFIERS).getListField()
              .contains(ep.getOldOrInsertedNode())) {
            epMod = ep;
          }
        } else if (ep.getType() == EditOperationType.FINAL_INSERT) {
          if (declarationOp.getOldOrInsertedNode()
              .getField(BastFieldConstants.DECLARATION_MODIFIERS) != null
              && declarationOp.getOldOrInsertedNode()
                  .getField(BastFieldConstants.DECLARATION_MODIFIERS).getListField() != null
              && declarationOp.getOldOrInsertedNode()
                  .getField(BastFieldConstants.DECLARATION_MODIFIERS).getListField()
                  .contains(ep.getOldOrInsertedNode())) {
            epMod = ep;
          }
        }
        if (ep.getType() == EditOperationType.MOVE) {

          if (declarationOp.getOldOrInsertedNode()
              .getField(BastFieldConstants.DECLARATION_SPECIFIER).getListField()
              .contains(ep.getOldOrInsertedNode())) {
            type = true;
            epType = ep;
          } else if (declarationOp.getOldOrInsertedNode()
              .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField().get(0)
              .getTag() == BastIdentDeclarator.TAG) {
            if (((BastIdentDeclarator) declarationOp.getOldOrInsertedNode()
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0)).identifier == ep.getOldOrInsertedNode()) {
              name = true;
              ident = (BastNameIdent) ep.getOldOrInsertedNode();
              epName = ep;
            } else if (((BastIdentDeclarator) declarationOp.getOldOrInsertedNode()
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0)).expression == ep.getOldOrInsertedNode()) {
              expression = true;
              epExpression = ep;
            } else if (((BastIdentDeclarator) declarationOp.getOldOrInsertedNode()
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0)).expression != null
                && ((BastIdentDeclarator) declarationOp.getOldOrInsertedNode()
                    .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                    .get(0)).expression.getTag() == BastExprInitializer.TAG
                && ((BastExprInitializer) ((BastIdentDeclarator) declarationOp
                    .getOldOrInsertedNode().getField(BastFieldConstants.DECLARATION_DECLARATORS)
                    .getListField().get(0)).expression).init == ep.getOldOrInsertedNode()) {
              expression = true;
              epExpression = ep;
            }
          } else if (declarationOp.getOldOrInsertedNode()
              .getField(BastFieldConstants.DECLARATION_MODIFIERS).getListField()
              .contains(ep.getNewOrChangedNode())) {
            epMod = ep;
          }
          if (declarationOp.getNewOrChangedNode().getField(BastFieldConstants.DECLARATION_SPECIFIER)
              .getListField().contains(ep.getNewOrChangedNode())) {
            type = true;
            epType = ep;
          } else if (declarationOp.getNewOrChangedNode()
              .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField().get(0)
              .getTag() == BastIdentDeclarator.TAG) {
            if (((BastIdentDeclarator) declarationOp.getNewOrChangedNode()
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField().get(0)) == ep
                    .getNewOrChangedNode()) {

              name = true;
              ident = (BastNameIdent) ((BastIdentDeclarator) ep.getOldOrInsertedNode()).identifier;
              expression = true;
              epIdent = ep;
            } else if (((BastIdentDeclarator) declarationOp.getNewOrChangedNode()
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0)).identifier == ep.getNewOrChangedNode()) {
              name = true;
              ident = (BastNameIdent) ep.getOldOrInsertedNode();
              epName = ep;
            } else if (((BastIdentDeclarator) declarationOp.getNewOrChangedNode()
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0)).expression == ep.getNewOrChangedNode()) {
              expression = true;
              epExpression = ep;
            } else if (((BastIdentDeclarator) declarationOp.getNewOrChangedNode()
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0)).expression != null
                && ((BastIdentDeclarator) declarationOp.getNewOrChangedNode()
                    .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                    .get(0)).expression.getTag() == BastExprInitializer.TAG
                && ((BastExprInitializer) ((BastIdentDeclarator) declarationOp.getNewOrChangedNode()
                    .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                    .get(0)).expression).init == ep.getNewOrChangedNode()) {
              expression = true;
              epExpression = ep;
            }
          } else if (declarationOp.getNewOrChangedNode()
              .getField(BastFieldConstants.DECLARATION_MODIFIERS).getListField()
              .contains(ep.getNewOrChangedNode())) {
            epMod = ep;
          }
        }
      }
      if (WildcardAccessHelper.getNodeToIndex(declarationOp.getNewOrChangedNode(),
          BastFieldConstants.DECLARATION_DECLARATORS, 0) != null
          && WildcardAccessHelper
              .getNodeToIndex(declarationOp.getNewOrChangedNode(),
                  BastFieldConstants.DECLARATION_DECLARATORS, 0)
              .getTag() == BastIdentDeclarator.TAG) {
        if (((BastIdentDeclarator) WildcardAccessHelper.getNodeToIndex(
            declarationOp.getNewOrChangedNode(), BastFieldConstants.DECLARATION_DECLARATORS,
            0)).expression == null) {
          expression = true;
        }
      }
      if (declarationOp.getType() == EditOperationType.MOVE) {
        BastDeclaration oldDeclaration = (BastDeclaration) declarationOp.getOldOrInsertedNode();
        BastDeclaration newDeclaration = (BastDeclaration) declarationOp.getNewOrChangedNode();
        if (oldDeclaration.getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
            .get(0).getTag() == BastIdentDeclarator.TAG
            && newDeclaration.getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0).getTag() == BastIdentDeclarator.TAG) {
          BastIdentDeclarator oldDeclarator = (BastIdentDeclarator) oldDeclaration
              .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField().get(0);
          BastIdentDeclarator newDeclarator = (BastIdentDeclarator) newDeclaration
              .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField().get(0);
          if (!((BastNameIdent) oldDeclarator.identifier).name
              .equals(((BastNameIdent) newDeclarator.identifier).name)) {
            name = true;
            if (oldDeclaration.getField(BastFieldConstants.DECLARATION_SPECIFIER) != null
                && oldDeclaration.getField(BastFieldConstants.DECLARATION_SPECIFIER)
                    .getListField() != null
                && oldDeclaration.getField(BastFieldConstants.DECLARATION_SPECIFIER).getListField()
                    .get(0).getTag() == BastTypeSpecifier.TAG
                && newDeclaration.getField(BastFieldConstants.DECLARATION_SPECIFIER) != null
                && newDeclaration.getField(BastFieldConstants.DECLARATION_SPECIFIER)
                    .getListField() != null
                && newDeclaration.getField(BastFieldConstants.DECLARATION_SPECIFIER).getListField()
                    .get(0).getTag() == BastTypeSpecifier.TAG) {
              BastTypeSpecifier oldTypeSpec = (BastTypeSpecifier) oldDeclaration
                  .getField(BastFieldConstants.DECLARATION_SPECIFIER).getListField().get(0);
              BastTypeSpecifier newTypeSpec = (BastTypeSpecifier) newDeclaration
                  .getField(BastFieldConstants.DECLARATION_SPECIFIER).getListField().get(0);
              if (oldTypeSpec.type.getTag() != newTypeSpec.type.getTag()
                  || oldTypeSpec.type.getTag() == BastBasicType.TAG) {
                type = true;
              }
            }
          }


        }

      }
      if (type && expression && !name) {
        for (BastEditOperation ep : extDiff.editScript) {
          if (ep.getType() == EditOperationType.MOVE) {
            if (ep.getNewOrChangedNode() == ((BastIdentDeclarator) declarationOp
                .getNewOrChangedNode().getField(BastFieldConstants.DECLARATION_DECLARATORS)
                .getListField().get(0)).identifier) {
              name = true;
              break;
            }
          }
        }
        for (BastEditOperation ep : insertedDeclaration) {
          if (ep.getOldOrInsertedNode() == declarationOp.getNewOrChangedNode()) {
            name = true;
          }
        }
      }
      if (name && type && expression || name && expression && epExpression == null) {
        if (extDiff.secondToFirstMap.get(ident) == null
            && hierarchySecond.get(declarationOp.getNewOrChangedNode()) != null
            && hierarchySecond.get(declarationOp.getNewOrChangedNode()).list.size() > 0) {
          AbstractBastNode parentNode =
              hierarchySecond.get(declarationOp.getNewOrChangedNode()).list.get(0).parent;
          NodeIndex index =
              new NodeIndex(
                  hierarchySecond.get(declarationOp.getNewOrChangedNode()).list
                      .get(0).fieldConstant,
                  hierarchySecond.get(declarationOp.getNewOrChangedNode()).list.get(0).listId);
          InsertOperation ip =
              new InsertOperation(parentNode, declarationOp.getNewOrChangedNode(), index);
          toRemove.add(epType);
          toRemove.add(epName);
          toRemove.add(epExpression);
          toRemove.add(epIdent);
          if (epDecl == null) {
            toAdd.add(ip);
          }
        }
      }
      if (remainingDeclaration.contains(declarationOp.getNewOrChangedNode())) {
        if (epType != null) {
          toRemove.add(epType);
        }
        if (epName != null) {
          toRemove.add(epName);
        }
        if (epExpression != null) {
          toRemove.add(epExpression);
        }
        if (epMod != null) {
          toRemove.add(epMod);
        }
      }
    }
    workList.addAll(editList);
    workList.removeAll(toRemove);
    workList.addAll(toAdd);

    return workList;
  }

  private static List<BastEditOperation> declarationDeletes(List<BastEditOperation> editList,
      ExtendedDiffResult extDiff,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond

  ) {
    ArrayList<BastEditOperation> insertedDeclaration = new ArrayList<>();
    ArrayList<AbstractBastNode> insertedDeclarationNodes = new ArrayList<>();
    ArrayList<AbstractBastNode> remainingDeclaration = new ArrayList<>();
    for (BastEditOperation ep : extDiff.editScript) {
      switch (ep.getType()) {
        case VARIABLE_DELETE:
          insertedDeclaration.add(ep);
          break;
        default:
          break;
      }

    }
    for (BastEditOperation ep : editList) {
      switch (ep.getType()) {
        case DELETE:
          if (ep.getOldOrInsertedNode().getTag() == BastAsgnExpr.TAG) {
            remainingDeclaration.add(ep.getOldOrInsertedNode());
          }
          break;
        default:
          break;
      }
    }
    ArrayList<BastEditOperation> toRemove = new ArrayList<>();
    ArrayList<BastEditOperation> toAdd = new ArrayList<>();
    ArrayList<BastEditOperation> workList = new ArrayList<>();

    for (AbstractBastNode node : insertedDeclarationNodes) {
      boolean name = false;
      boolean type = false;
      boolean expression = false;
      BastEditOperation epName = null;
      BastEditOperation epType = null;
      BastEditOperation epExpression = null;
      BastEditOperation epDecl = null;
      BastEditOperation epMod = null;

      for (BastEditOperation ep : editList) {
        if (ep.getType() == EditOperationType.DELETE) {
          if (node.getField(BastFieldConstants.DECLARATION_SPECIFIER).getListField()
              .contains(ep.getOldOrInsertedNode())) {
            type = true;
            epType = ep;
          } else if (node.getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField().get(0)
              .getTag() == BastIdentDeclarator.TAG) {
            if (((BastIdentDeclarator) node.getField(BastFieldConstants.DECLARATION_DECLARATORS)
                .getListField().get(0)).identifier == ep.getOldOrInsertedNode()) {
              name = true;
              epName = ep;
            } else if (((BastIdentDeclarator) node
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0)).expression == ep.getOldOrInsertedNode()) {
              expression = true;
              epExpression = ep;
            } else if (((BastIdentDeclarator) node
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0)).expression != null
                && ((BastIdentDeclarator) node.getField(BastFieldConstants.DECLARATION_DECLARATORS)
                    .getListField().get(0)).expression.getTag() == BastExprInitializer.TAG) {
              if (((BastExprInitializer) ((BastIdentDeclarator) node
                  .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                  .get(0)).expression).init == ep.getOldOrInsertedNode()) {
                expression = true;
                epExpression = ep;
              }
            }
          } else if (node.getField(BastFieldConstants.DECLARATION_MODIFIERS).getListField()
              .contains(ep.getOldOrInsertedNode())) {
            epMod = ep;
          }
        } else if (ep.getType() == EditOperationType.FINAL_DELETE) {
          if (node.getField(BastFieldConstants.DECLARATION_MODIFIERS) != null
              && node.getField(BastFieldConstants.DECLARATION_MODIFIERS).getListField() != null
              && node.getField(BastFieldConstants.DECLARATION_MODIFIERS).getListField()
                  .contains(ep.getOldOrInsertedNode())) {
            epMod = ep;
          }
        }
        if (ep.getType() == EditOperationType.MOVE) {
          if (node.getField(BastFieldConstants.DECLARATION_SPECIFIER).getListField()
              .contains(ep.getOldOrInsertedNode())) {
            type = true;
            epType = ep;
          } else if (node.getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField().get(0)
              .getTag() == BastIdentDeclarator.TAG) {
            if (((BastIdentDeclarator) node.getField(BastFieldConstants.DECLARATION_DECLARATORS)
                .getListField().get(0)).identifier == ep.getOldOrInsertedNode()) {
              name = true;
              epName = ep;
            } else if (((BastIdentDeclarator) node
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0)).expression == ep.getOldOrInsertedNode()) {
              expression = true;
              epExpression = ep;
            } else if (((BastIdentDeclarator) node
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0)).expression != null
                && ((BastIdentDeclarator) node.getField(BastFieldConstants.DECLARATION_DECLARATORS)
                    .getListField().get(0)).expression.getTag() == BastExprInitializer.TAG
                && ((BastExprInitializer) ((BastIdentDeclarator) node
                    .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                    .get(0)).expression).init == ep.getOldOrInsertedNode()) {
              expression = true;
              epExpression = ep;
            }
          } else if (node.getField(BastFieldConstants.DECLARATION_MODIFIERS).getListField()
              .contains(ep.getOldOrInsertedNode())) {
            epMod = ep;
          }
        }

        if (ep.getOldOrInsertedNode() == node) {
          epDecl = ep;
        }
      }
      if (type && expression && !name) {
        for (BastEditOperation ep : extDiff.editScript) {
          if (ep.getType() == EditOperationType.MOVE) {
            if (ep.getOldOrInsertedNode() == ((BastIdentDeclarator) node
                .getField(BastFieldConstants.DECLARATION_DECLARATORS).getListField()
                .get(0)).identifier) {
              name = true;
              break;
            }
          }
        }
      }
      if (name && type && expression) {
        AbstractBastNode parentNode = hierarchyFirst.get(node).list.get(0).parent;
        NodeIndex index = new NodeIndex(hierarchyFirst.get(node).list.get(0).fieldConstant,
            hierarchyFirst.get(node).list.get(0).listId);
        DeleteOperation ip = new DeleteOperation(parentNode, node, index);
        toRemove.add(epType);
        toRemove.add(epName);
        toRemove.add(epExpression);
        if (epDecl == null) {
          toAdd.add(ip);
        }
      }
      if (remainingDeclaration.contains(node)) {
        if (epType != null) {
          toRemove.add(epType);
        }
        if (epName != null) {
          toRemove.add(epName);
        }
        if (epExpression != null) {
          toRemove.add(epExpression);
        }
        if (epMod != null) {
          toRemove.add(epMod);
        }
      }
    }
    workList.addAll(editList);
    workList.removeAll(toRemove);
    workList.addAll(toAdd);

    return workList;
  }


  public static FilterRule getInstance() {
    return new TransformDeclarationChanges();
  }
}
