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

package de.fau.cs.inf2.cas.ares.pcreation.visitors;

import de.fau.cs.inf2.cas.ares.bast.general.ParentHierarchyHandler;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.pcreation.InsertResult;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.visitors.DefaultFieldVisitor;
import de.fau.cs.inf2.cas.common.bast.visitors.FindNodesFromTagVisitor;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

public class RemoveStatementsfromWildcardsVisitor extends DefaultFieldVisitor {
  private ExtendedDiffResult extDiffBa;
  private ExtendedDiffResult extDiffBb;
  private Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy1;
  private Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy2;
  private Map<String, AbstractBastNode> wildcardMap = new HashMap<String, AbstractBastNode>();
  private Map<String, AbstractBastNode> useMap = new HashMap<String, AbstractBastNode>();
  private Map<AbstractBastNode, LinkedList<AbstractBastNode>> stmtMapWildcard =
      new HashMap<AbstractBastNode, LinkedList<AbstractBastNode>>();

  /**
   * Instantiates a new removes the statementsfrom wildcards visitor.
   *
   * @param extDiffBa the ext diff ba
   * @param extDiffBb the ext diff bb
   * @param extDiffAa the ext diff aa
   */
  public RemoveStatementsfromWildcardsVisitor(ExtendedDiffResult extDiffBa,
      ExtendedDiffResult extDiffBb, ExtendedDiffResult extDiffAa, AbstractBastNode example2Original,
      AbstractBastNode example2Modified, InsertResult originalRes, InsertResult modifiedRes) {
    this.extDiffBa = extDiffBa;
    this.extDiffBb = extDiffBb;
    this.hierarchy1 = ParentHierarchyHandler.getParentHierarchy(example2Original);
    this.hierarchy2 = ParentHierarchyHandler.getParentHierarchy(example2Modified);
    FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(AresWildcard.TAG);
    example2Original.accept(fnft);
    for (AbstractBastNode node : fnft.nodes) {
      if (WildcardAccessHelper.getName(node) != null) {
        wildcardMap.put(WildcardAccessHelper.getName(node).name, (AresWildcard) node);
      }
    }
    fnft = new FindNodesFromTagVisitor(AresUseStmt.TAG);
    example2Modified.accept(fnft);
    for (AbstractBastNode node : fnft.nodes) {
      if (WildcardAccessHelper.getName(node) != null) {
        useMap.put(WildcardAccessHelper.getName(node).name, (AresUseStmt) node);
      }
    }
    for (Entry<AbstractBastNode, AbstractBastNode> entry : originalRes.replaceMap.entrySet()) {
      LinkedList<AbstractBastNode> nodes = stmtMapWildcard.get(entry.getValue());
      if (nodes == null) {
        nodes = new LinkedList<>();
        stmtMapWildcard.put(entry.getValue(), nodes);
      }
      if (!originalRes.replaceMap.belongsToExpr(entry.getKey())) {

        nodes.add(entry.getKey());
      }
    }
    for (Entry<AbstractBastNode, AbstractBastNode> entry : modifiedRes.replaceMap.entrySet()) {
      LinkedList<AbstractBastNode> nodes = stmtMapWildcard.get(entry.getValue());
      if (nodes == null) {
        nodes = new LinkedList<>();
        stmtMapWildcard.put(entry.getValue(), nodes);
      }
      if (!modifiedRes.replaceMap.belongsToExpr(entry.getKey())) {

        nodes.add(entry.getKey());
      }
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
              field = removeStatements(node.fieldMap.get(constant).getListField());
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

  private BastField removeStatements(LinkedList<? extends AbstractBastNode> listField) {
    int containsWildcards = 0;
    boolean changed = false;
    LinkedList<AbstractBastNode> wildcards = new LinkedList<>();
    LinkedList<AbstractBastNode> newStmts = new LinkedList<>();
    newStmts.addAll(listField);
    for (AbstractBastNode expr : listField) {
      if (WildcardAccessHelper.isWildcard(expr)) {
        containsWildcards++;
        wildcards.add(expr);
      }
    }
    if (containsWildcards > 0) {
      for (AbstractBastNode w : wildcards) {
        AbstractBastNode partner = null;
        NodeParentInformationHierarchy npi = null;
        NodeParentInformationHierarchy npiPartner = null;

        if (WildcardAccessHelper.getName(w) == null) {
          continue;
        }

        if (w.getTag() == AresWildcard.TAG) {
          partner = useMap.get(WildcardAccessHelper.getName(w).name);
          npi = hierarchy1.get(w);
          npiPartner = hierarchy2.get(partner);

        } else {
          if (WildcardAccessHelper.getName(w) == null) {
            continue;
          }
          partner = wildcardMap.get(WildcardAccessHelper.getName(w).name);
          npi = hierarchy2.get(w);
          npiPartner = hierarchy1.get(partner);

        }
        if (npiPartner == null) {
          continue;
        }
        final AbstractBastNode parent = npi.list.get(0).parent;
        final AbstractBastNode partnerParent = npiPartner.list.get(0).parent;
        final LinkedList<AbstractBastNode> replacedNodes = stmtMapWildcard.get(w);
        if (replacedNodes != null) {
          for (AbstractBastNode node : replacedNodes) {
            if (!parent.getField(npi.list.get(0).fieldConstant).getListField().contains(node)) {
              AbstractBastNode nodePartner = null;
              if (w.getTag() == AresWildcard.TAG) {
                nodePartner = extDiffBa.firstToSecondMap.get(node);
                if (nodePartner == null) {
                  node = extDiffBb.firstToSecondMap.get(node);
                }
                nodePartner = extDiffBa.firstToSecondMap.get(node);
              } else {
                nodePartner = extDiffBa.secondToFirstMap.get(node);
                if (nodePartner == null) {
                  node = extDiffBb.firstToSecondMap.get(node);
                }
                nodePartner = extDiffBa.secondToFirstMap.get(node);
              }
              if (nodePartner != null) {
                if (partnerParent.getField(npiPartner.list.get(0).fieldConstant).isList()
                    && partnerParent.getField(npi.list.get(0).fieldConstant).getListField()
                        .contains(nodePartner)) {
                  int listId =
                      parent.getField(npi.list.get(0).fieldConstant).getListField().indexOf(w);
                  if (listId != -1) {
                    if ((listId == 0 || !WildcardAccessHelper.isWildcard(parent
                        .getField(npi.list.get(0).fieldConstant).getListField().get(listId - 1)))
                        && (listId == parent.getField(npi.list.get(0).fieldConstant).getListField()
                            .size() - 1
                            || !WildcardAccessHelper
                                .isWildcard(parent.getField(npi.list.get(0).fieldConstant)
                                    .getListField().get(listId + 1)))) {
                      if (listId == 0 || parent.getField(npi.list.get(0).fieldConstant)
                          .getListField().get(listId - 1).nodeId < node.nodeId) {
                        if (listId == parent.getField(npi.list.get(0).fieldConstant).getListField()
                            .size() - 1
                            || parent.getField(npi.list.get(0).fieldConstant).getListField()
                                .get(listId + 1).nodeId > node.nodeId) {
                          newStmts.add(listId, node);
                          changed = true;
                          break;
                        }
                      }
                    }

                  }
                }
              }
            }
          }
        }
      }
    }

    if (!changed) {
      return null;
    } else {
      return new BastField(newStmts);
    }
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
}
