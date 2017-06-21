package de.fau.cs.inf2.cas.ares.pcreation.visitors;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresCaseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class CombineChoiceVisitor extends AresDefaultFieldVisitor {

  private Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyA1 = null;
  private Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyA2 = null;

  /**
   * Instantiates a new combine choice visitor.
   */
  public CombineChoiceVisitor(Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyA1,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyA2) {
    this.hierarchyA1 = hierarchyA1;
    this.hierarchyA2 = hierarchyA2;
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

  private BastField combineChoices(LinkedList<? extends AbstractBastNode> nodes) {
    int containChoices = 0;
    for (AbstractBastNode expr : nodes) {
      if (expr.getTag() == AresChoiceStmt.TAG) {
        containChoices++;
      }
    }
    if (containChoices > 1) {
      BastField field = combineIdenticalChoices(nodes);
      if (field != null) {
        return field;
      }
      return combineNormalChoices(nodes);
    }
    return null;
  }


  @SuppressWarnings("unchecked")
  private BastField combineNormalChoices(LinkedList<? extends AbstractBastNode> nodes) {
    LinkedList<AbstractBastNode> newNodes = new LinkedList<>();
    newNodes.addAll(nodes);
    boolean globalChange = false;
    boolean changed = true;
    int index = -1;
    while (changed || index < newNodes.size()) {
      changed = false;
      index++;
      boolean useSelected = false;
      for (int i = index; i < newNodes.size(); i++) {
        if (newNodes.get(i).getTag() == AresChoiceStmt.TAG) {
          index = i;
          useSelected = true;
          break;
        }
      }
      if (!useSelected) {
        break;
      }
      boolean remove = false;
      int jvar = index + 1;
      if (jvar < newNodes.size() && newNodes.get(jvar).getTag() == AresChoiceStmt.TAG) {
        AresChoiceStmt firstChoice = ((AresChoiceStmt) newNodes.get(index));
        AresChoiceStmt secondChoice = ((AresChoiceStmt) newNodes.get(jvar));

        LinkedList<AresCaseStmt> firstCases = new LinkedList<>();
        LinkedList<AresCaseStmt> secondCases = new LinkedList<>();

        for (AbstractBastNode stmt : firstChoice.choiceBlock.statements) {
          if (stmt.getTag() == AresCaseStmt.TAG) {
            AresCaseStmt caseStmt = ((AresCaseStmt) stmt);
            firstCases.add(caseStmt);
          }
        }
        for (AbstractBastNode stmt : secondChoice.choiceBlock.statements) {
          if (stmt.getTag() == AresCaseStmt.TAG) {
            AresCaseStmt caseStmt = ((AresCaseStmt) stmt);
            secondCases.add(caseStmt);
          }
        }
        if (firstCases.size() == 1 && secondCases.size() == 1) {
          return null;
        }
        HashMap<AresCaseStmt, AresCaseStmt> caseMap = new HashMap<>();
        for (AresCaseStmt firstCase : firstCases) {
          for (AresCaseStmt secondCase : secondCases) {
            for (AbstractBastNode firstNode : firstCase.block.statements) {
              boolean identicalPart = true;
              for (AbstractBastNode secondNode : secondCase.block.statements) {
                if (!hierarchyA1.containsKey(firstNode)
                    && !hierarchyA2.containsKey(firstNode)) {
                  identicalPart = false;
                  break;
                }
                if (!hierarchyA1.containsKey(secondNode)
                    && !hierarchyA2.containsKey(secondNode)) {
                  identicalPart = false;
                  break;
                }
                if (hierarchyA1.containsKey(firstNode)
                    && hierarchyA2.containsKey(secondNode)) {
                  identicalPart = false;
                  break;
                } else if (hierarchyA2.containsKey(firstNode)
                    && hierarchyA1.containsKey(secondNode)) {
                  identicalPart = false;
                  break;
                }
              }
              if (identicalPart) {
                if (!caseMap.containsKey(firstCase) && !caseMap.containsValue(secondCase)) {
                  caseMap.put(firstCase, secondCase);
                }
              }
            }
          }
        }

        for (Entry<AresCaseStmt, AresCaseStmt> entry : caseMap.entrySet()) {
          LinkedList<AbstractBastStatement> firstCaseStmts =
              (LinkedList<AbstractBastStatement>) entry.getKey().block.fieldMap
                  .get(BastFieldConstants.BLOCK_STATEMENT).getListField();
          LinkedList<AbstractBastStatement> secondCaseStmts =
              (LinkedList<AbstractBastStatement>) entry.getValue().block.fieldMap
                  .get(BastFieldConstants.BLOCK_STATEMENT).getListField();
          secondCaseStmts.addAll(0, firstCaseStmts);
          entry.getValue().block.replaceField(BastFieldConstants.BLOCK_STATEMENT,
              new BastField(secondCaseStmts));
        }

        LinkedList<AbstractBastStatement> stmts =
            (LinkedList<AbstractBastStatement>) firstChoice.choiceBlock.fieldMap
                .get(BastFieldConstants.BLOCK_STATEMENT).getListField();
        stmts.removeAll(caseMap.keySet());
        firstChoice.choiceBlock.replaceField(BastFieldConstants.BLOCK_STATEMENT,
            new BastField(stmts));
        if (stmts.isEmpty()) {
          remove = true;
        }
      }
      if (remove) {
        newNodes.remove(index);
        changed = true;
        globalChange = true;
      }

    }
    if (globalChange) {
      return new BastField(newNodes);
    } else {
      return null;
    }
  }

  private BastField combineIdenticalChoices(LinkedList<? extends AbstractBastNode> nodes) {
    LinkedList<AbstractBastNode> newNodes = new LinkedList<>();
    newNodes.addAll(nodes);
    boolean globalChange = false;
    boolean changed = true;
    int index = -1;
    while (changed || index < newNodes.size()) {
      changed = false;
      index++;
      boolean useSelected = false;
      for (int i = index; i < newNodes.size(); i++) {
        if (newNodes.get(i).getTag() == AresChoiceStmt.TAG) {
          index = i;
          useSelected = true;
          break;
        }
      }
      if (!useSelected) {
        break;
      }
      boolean remove = false;
      int jvar = index + 1;
      if (jvar < newNodes.size() && newNodes.get(jvar).getTag() == AresChoiceStmt.TAG) {
        AresChoiceStmt firstChoice = ((AresChoiceStmt) newNodes.get(index));
        AresChoiceStmt secondChoice = ((AresChoiceStmt) newNodes.get(jvar));

        LinkedList<ArrayList<AbstractBastNode>> firstLists = new LinkedList<>();
        LinkedList<AresCaseStmt> firstCases = new LinkedList<>();

        LinkedList<ArrayList<AbstractBastNode>> secondLists = new LinkedList<>();
        boolean replace = false;
        for (AbstractBastNode stmt : firstChoice.choiceBlock.statements) {
          if (stmt.getTag() == AresCaseStmt.TAG) {
            AresCaseStmt caseStmt = ((AresCaseStmt) stmt);
            ArrayList<AbstractBastNode> list = new ArrayList<>();
            list.addAll(caseStmt.block.statements);
            firstLists.add(list);
            firstCases.add(caseStmt);
          }

        }
        for (AbstractBastNode stmt : secondChoice.choiceBlock.statements) {
          if (stmt.getTag() == AresCaseStmt.TAG) {
            AresCaseStmt caseStmt = ((AresCaseStmt) stmt);
            ArrayList<AbstractBastNode> list = new ArrayList<>();

            list.addAll(caseStmt.block.statements);
            secondLists.add(list);
          }

        }
        boolean listChanged = true;
        while (listChanged) {
          listChanged = false;
          for (List<AbstractBastNode> secondList : secondLists) {
            LinkedList<List<AbstractBastNode>> toRemove = new LinkedList<>();
            for (List<AbstractBastNode> firstList : firstLists) {
              if (WildcardAccessHelper.isEqual(firstList, secondList)) {
                int stringIndex = firstLists.indexOf(firstList);
                toRemove.add(firstLists.get(stringIndex));
                if (stringIndex < firstCases.size()) {
                  firstCases.remove(stringIndex);
                }
                replace = true;
                listChanged = true;
              }
            }
            firstLists.removeAll(toRemove);

          }
        }
        if (replace) {
          firstChoice.choiceBlock.replaceField(BastFieldConstants.BLOCK_STATEMENT,
              new BastField(firstCases));
          if (firstCases.size() == 0) {
            remove = true;

          }
          changed = true;
        }
      }
      if (remove) {
        newNodes.remove(index);
        changed = true;
        globalChange = true;
      }

    }
    if (globalChange) {
      return new BastField(newNodes);
    } else {
      return null;
    }
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
              field = combineChoices(node.fieldMap.get(constant).getListField());
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
        setVariables(constant, node, -1);
        node.fieldMap.get(constant).getField().accept(this);
      }
    }

  }

}
