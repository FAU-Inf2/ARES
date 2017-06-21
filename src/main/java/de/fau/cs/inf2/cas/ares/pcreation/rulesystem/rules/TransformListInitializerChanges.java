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

package de.fau.cs.inf2.cas.ares.pcreation.rulesystem.rules;

import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.AbstractFilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRule;
import de.fau.cs.inf2.cas.ares.pcreation.rulesystem.FilterRuleType;

import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformation;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastListInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitchCaseGroup;

import de.fau.cs.inf2.cas.common.util.NodeIndex;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.DeleteOperation;
import de.fau.cs.inf2.mtdiff.editscript.operations.EditOperationType;
import de.fau.cs.inf2.mtdiff.editscript.operations.InsertOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TransformListInitializerChanges extends AbstractFilterRule {

  public TransformListInitializerChanges() {
    super(FilterRuleType.TRANSFORM_LIST_INITIALIZER_CHANGES);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    worklist = handleListInitializer(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);
    return handleSpecialCases(worklist, exDiffCurrent, hierarchyFirst, hierarchySecond);

  }

  private static List<BastEditOperation> handleListInitializer(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    List<BastListInitializer> listInitializers = new ArrayList<>();
    List<BastListInitializer> doneInitializers = new ArrayList<>();

    List<BastEditOperation> tmp3 = new ArrayList<>();

    HashMap<AbstractBastNode, LinkedList<BastEditOperation>> listInitMap = new HashMap<>();

    for (BastEditOperation ep : exDiffAa.editScript) {
      if (ep.getOldOrInsertedNode().getTag() == BastListInitializer.TAG) {
        if (ep.getType() == EditOperationType.MOVE) {
          listInitializers.add((BastListInitializer) ep.getNewOrChangedNode());
        }
        listInitializers.add((BastListInitializer) ep.getOldOrInsertedNode());
      }
    }
    for (BastEditOperation ep : workList) {
      if (ep != null && ep.getType() != EditOperationType.UPDATE
          && ep.getUnchangedOrOldParentNode() != null
          && ep.getUnchangedOrOldParentNode().getTag() == BastListInitializer.TAG) {
        LinkedList<BastEditOperation> list = listInitMap.get(ep.getUnchangedOrOldParentNode());
        if (list == null) {
          list = new LinkedList<>();
          listInitMap.put(ep.getUnchangedOrOldParentNode(), list);
        }
        list.add(ep);
      }
      if (ep != null && ep.getType() != EditOperationType.UPDATE
          && ep.getUnchangedOrNewParentNode() != null
          && ep.getUnchangedOrNewParentNode().getTag() == BastListInitializer.TAG) {
        LinkedList<BastEditOperation> list = listInitMap.get(ep.getUnchangedOrNewParentNode());
        if (list == null) {
          list = new LinkedList<>();
          listInitMap.put(ep.getUnchangedOrNewParentNode(), list);
        }
        list.add(ep);
      }

      if (ep != null && ep.getOldOrInsertedNode().getTag() == BastDeclaration.TAG) {
        tmp3.add(ep);
      }
    }
    for (Entry<AbstractBastNode, AbstractBastNode> entry : exDiffAa.firstToSecondMap.entrySet()) {
      if (entry.getKey().getTag() == BastListInitializer.TAG) {
        listInitializers.add((BastListInitializer) entry.getKey());
        listInitializers.add((BastListInitializer) entry.getValue());

      }
    }
    for (int i = 0; i < listInitializers.size(); i++) {
      if (doneInitializers.contains(listInitializers.get(i))) {
        continue;
      }
      NodeParentInformationHierarchy outerNpi = hierarchyFirst.get(listInitializers.get(i));
      if (outerNpi == null) {
        outerNpi = hierarchySecond.get(listInitializers.get(i));
      }
      if (outerNpi != null) {
        BastDeclaration outerDecl = null;
        for (NodeParentInformation np : outerNpi.list) {
          if (np.parent.getTag() == BastDeclaration.TAG) {
            outerDecl = (BastDeclaration) np.parent;
            break;
          }
        }
        if (outerDecl != null) {
          BastDeclaration outerPartner =
              (BastDeclaration) exDiffAa.firstToSecondMap.get(outerDecl);
          if (outerPartner == null) {
            outerPartner = (BastDeclaration) exDiffAa.secondToFirstMap.get(outerDecl);
          }
          if (outerPartner != null) {
            for (int j = 1; j < listInitializers.size(); j++) {
              if (doneInitializers.contains(listInitializers.get(j))) {
                continue;
              }
              NodeParentInformationHierarchy innerNpi =
                  hierarchyFirst.get(listInitializers.get(j));
              boolean useInsert = false;
              if (innerNpi == null) {
                innerNpi = hierarchySecond.get(listInitializers.get(j));
                useInsert = true;
              }
              if (innerNpi != null) {
                BastDeclaration innerDecl = null;
                for (NodeParentInformation np : innerNpi.list) {
                  if (np.parent.getTag() == BastDeclaration.TAG) {
                    innerDecl = (BastDeclaration) np.parent;
                    break;
                  }
                }
                if (innerDecl != null && innerDecl == outerPartner) {
                  Math.max(listInitializers.get(i).list.size(),
                      listInitializers.get(j).list.size());
                  List<BastEditOperation> outerList = listInitMap.get(listInitializers.get(i));
                  List<BastEditOperation> innerList = listInitMap.get(listInitializers.get(j));
                  int outerSize = outerList == null ? 0 : outerList.size();
                  int innerSize = innerList == null ? 0 : innerList.size();

                  int changeSize = Math.max(outerSize, innerSize);
                  if (changeSize > 4) {
                    AbstractBastNode parentNode = null;
                    NodeIndex index = null;
                    if (hierarchySecond.get(innerDecl) != null) {
                      parentNode = hierarchySecond.get(innerDecl).list.get(0).parent;

                      index =
                          new NodeIndex(hierarchySecond.get(innerDecl).list.get(0).fieldConstant,
                              hierarchySecond.get(innerDecl).list.get(0).listId);

                    } else {
                      parentNode = hierarchySecond.get(outerDecl).list.get(0).parent;
                      index =
                          new NodeIndex(hierarchySecond.get(outerDecl).list.get(0).fieldConstant,
                              hierarchySecond.get(outerDecl).list.get(0).listId);

                    }
                    exDiffAa.firstToSecondMap.get(innerDecl);
                    LinkedList<BastEditOperation> toRemove = new LinkedList<>();
                    for (BastEditOperation ep : workList) {
                      NodeParentInformationHierarchy npi =
                          hierarchyFirst.get(ep.getOldOrInsertedNode());
                      if (npi == null) {
                        npi = hierarchySecond.get(ep.getOldOrInsertedNode());
                      }
                      if (npi != null) {
                        for (NodeParentInformation np : npi.list) {
                          if (np.parent == outerDecl || np.parent == innerDecl) {
                            toRemove.add(ep);
                            break;
                          }
                        }
                      }
                    }
                    for (BastEditOperation ep : workList) {
                      NodeParentInformationHierarchy npi =
                          hierarchyFirst.get(ep.getNewOrChangedNode());
                      if (npi == null) {
                        npi = hierarchySecond.get(ep.getNewOrChangedNode());
                      }
                      if (npi != null) {
                        for (NodeParentInformation np : npi.list) {
                          if (np.parent == outerDecl || np.parent == innerDecl) {
                            toRemove.add(ep);
                            break;
                          }
                        }
                      }
                    }
                    workList.removeAll(toRemove);
                    if (useInsert) {
                      final BastEditOperation ip =
                          new InsertOperation(parentNode, innerDecl, index);
                      workList.add(ip);
                    } else {
                      final BastEditOperation ip =
                          new InsertOperation(parentNode, outerDecl, index);
                      workList.add(ip);
                    }



                    doneInitializers.add(listInitializers.get(i));
                    doneInitializers.add(listInitializers.get(j));
                    break;

                  }
                }
              }
            }
          }
        }
      }
    }

    return workList;
  }

  private static List<BastEditOperation> handleSpecialCases(List<BastEditOperation> workList,
      ExtendedDiffResult exDiffAa,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchyFirst,
      Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchySecond) {
    List<BastEditOperation> toRemove = new LinkedList<>();
    List<BastEditOperation> toAdd = new LinkedList<>();
    for (BastEditOperation ep : workList) {
      if (ep.getType() == EditOperationType.DELETE || ep.getType() == EditOperationType.ALIGN
          || ep.getType() == EditOperationType.MOVE) {
        boolean containsListInit = false;
        if (ep.getUnchangedOrOldParentNode().getTag() != BastBlock.TAG && WildcardAccessHelper
            .hasChildWithTag(ep.getOldOrInsertedNode(), BastListInitializer.TAG)) {
          containsListInit = true;
        }
        NodeParentInformationHierarchy npi = hierarchyFirst.get(ep.getOldOrInsertedNode());
        if (npi != null) {

          for (int i = 0; i < npi.list.size(); i++) {
            if (npi.list.get(i).parent.getTag() == BastListInitializer.TAG) {
              containsListInit = true;
            }
            if (!containsListInit && npi.list.get(i).parent.getTag() == BastBlock.TAG) {
              break;
            }
            if (containsListInit && npi.list.get(i).parent.getTag() == BastSwitchCaseGroup.TAG
                && i > 0) {
              DeleteOperation delOp =
                  new DeleteOperation(npi.list.get(i).parent, npi.list.get(i - 1).parent,
                      new NodeIndex(npi.list.get(i).fieldConstant, npi.list.get(i).listId));
              toAdd.add(delOp);
              toRemove.remove(ep);
              break;
            }
            if (containsListInit && npi.list.get(i).parent.getTag() == BastBlock.TAG && i > 0) {
              DeleteOperation delOp =
                  new DeleteOperation(npi.list.get(i).parent, npi.list.get(i - 1).parent,
                      new NodeIndex(npi.list.get(i).fieldConstant, npi.list.get(i).listId));
              toAdd.add(delOp);
              toRemove.remove(ep);
              break;
            }
          }
        }
      }
    }
    for (BastEditOperation ep : workList) {
      if (ep.getType() == EditOperationType.INSERT || ep.getType() == EditOperationType.ALIGN
          || ep.getType() == EditOperationType.MOVE) {
        boolean containsListInit = false;
        if (ep.getUnchangedOrNewParentNode().getTag() != BastBlock.TAG && WildcardAccessHelper
            .hasChildWithTag(ep.getNewOrChangedNode(), BastListInitializer.TAG)) {
          containsListInit = true;
        }
        NodeParentInformationHierarchy npi = hierarchySecond.get(ep.getNewOrChangedNode());
        if (npi != null) {

          for (int i = 0; i < npi.list.size(); i++) {
            if (npi.list.get(i).parent.getTag() == BastListInitializer.TAG) {
              containsListInit = true;
            }
            if (containsListInit && npi.list.get(i).parent.getTag() == BastBlock.TAG && i > 0) {
              InsertOperation insOp =
                  new InsertOperation(npi.list.get(i).parent, npi.list.get(i - 1).parent,
                      new NodeIndex(npi.list.get(i).fieldConstant, npi.list.get(i).listId));
              toAdd.add(insOp);
              toRemove.remove(ep);
              break;
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
    return new TransformListInitializerChanges();
  }
}