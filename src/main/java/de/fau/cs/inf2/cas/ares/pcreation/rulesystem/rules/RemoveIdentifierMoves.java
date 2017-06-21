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

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAccess;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAdditiveExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastArrayRef;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAsgnExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCase;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCmp;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitch;
import de.fau.cs.inf2.cas.common.bast.type.BastClassType;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;

import java.util.ArrayList;
import java.util.List;

public class RemoveIdentifierMoves extends AbstractFilterRule {

  public RemoveIdentifierMoves() {
    super(FilterRuleType.REMOVE_IDENTIFIER_MOVES);
  }



  @Override
  public List<BastEditOperation> ruleImplementation(List<BastEditOperation> worklist) {
    return handleNameChanges(worklist, exDiffCurrent);
  }

  private static List<BastEditOperation> handleNameChanges(List<BastEditOperation> editList,
      ExtendedDiffResult extDiff) {
    ArrayList<BastEditOperation> workList = new ArrayList<>();

    for (BastEditOperation ep : editList) {
      BastFieldConstants childrenListNumber = ep.getOldOrChangedIndex().childrenListNumber;
      switch (ep.getType()) {
        case MOVE:
          if (ep.getUnchangedOrOldParentNode().getTag() == BastCall.TAG
              || ep.getUnchangedOrOldParentNode().getTag() == BastAccess.TAG
              || ep.getUnchangedOrOldParentNode().getTag() == BastIdentDeclarator.TAG
              || ep.getUnchangedOrOldParentNode().getTag() == BastCmp.TAG) {
            if (ep.getOldOrInsertedNode().getTag() == BastNameIdent.TAG) {
              if (((BastNameIdent) ep.getOldOrInsertedNode()).name
                  .equals(((BastNameIdent) ep.getNewOrChangedNode()).name)) {
                continue;
              } else {
                AbstractBastNode partner =
                    extDiff.secondToFirstMap.get(ep.getUnchangedOrNewParentNode());
                if (partner != null) {
                  if (partner.getField(ep.getNewOrChangedIndex().childrenListNumber).isList()) {
                    if (partner.getField(ep.getNewOrChangedIndex().childrenListNumber)
                        .getListField() != null
                        && partner.getField(ep.getNewOrChangedIndex().childrenListNumber)
                            .getListField()
                            .size() > ep.getNewOrChangedIndex().childrenListIndex) {
                      if (WildcardAccessHelper.isEqual(ep.getNewOrChangedNode(),
                          partner.getField(ep.getNewOrChangedIndex().childrenListNumber)
                              .getListField().get(ep.getNewOrChangedIndex().childrenListIndex))) {
                        continue;
                      }
                    }
                  } else {
                    if (partner.getField(ep.getNewOrChangedIndex().childrenListNumber)
                        .getField() != null) {
                      if (WildcardAccessHelper.isEqual(ep.getNewOrChangedNode(), partner
                          .getField(ep.getNewOrChangedIndex().childrenListNumber).getField())) {
                        continue;
                      }
                    }
                  }
                }
              }
            }
          }
          workList.add(ep);
          break;
        case INSERT:
          if (ep.getUnchangedOrOldParentNode().getTag() == BastAccess.TAG) {
            if (ep.getOldOrInsertedNode().getTag() == BastNameIdent.TAG) {
              continue;
            }
          } else if (ep.getUnchangedOrOldParentNode().getTag() == BastCall.TAG
              && childrenListNumber == BastFieldConstants.DIRECT_CALL_FUNCTION) {
            if (ep.getOldOrInsertedNode().getTag() == BastNameIdent.TAG) {
              // do nothing;
            }
          } else if (ep.getUnchangedOrOldParentNode().getTag() == BastSwitch.TAG
              && childrenListNumber == BastFieldConstants.SWITCH_CONDITION) {
            if (ep.getOldOrInsertedNode().getTag() == BastNameIdent.TAG) {
              continue;
            }

          } else if (ep.getUnchangedOrOldParentNode().getTag() == BastClassType.TAG
              && childrenListNumber == BastFieldConstants.CLASS_TYPE_NAME) {
            if (ep.getOldOrInsertedNode().getTag() == BastNameIdent.TAG) {
              continue;
            }
          } else if (ep.getUnchangedOrOldParentNode().getTag() == BastCase.TAG
              && childrenListNumber == BastFieldConstants.CASE_CONSTANT) {
            if (ep.getOldOrInsertedNode().getTag() == BastNameIdent.TAG) {
              continue;
            }
          } else if (ep.getUnchangedOrOldParentNode().getTag() == BastAsgnExpr.TAG
              && childrenListNumber == BastFieldConstants.ASGN_EXPR_LEFT) {
            if (ep.getOldOrInsertedNode().getTag() == BastNameIdent.TAG) {
              continue;
            }
          } else if (ep.getUnchangedOrOldParentNode().getTag() == BastAdditiveExpr.TAG
              && childrenListNumber == BastFieldConstants.ASGN_EXPR_RIGHT) {
            if (ep.getOldOrInsertedNode().getTag() == BastNameIdent.TAG) {
              continue;
            }
          }

          workList.add(ep);
          break;
        case DELETE:
          if (ep.getUnchangedOrOldParentNode().getTag() == BastAccess.TAG) {
            if (ep.getOldOrInsertedNode().getTag() == BastNameIdent.TAG) {
              continue;
            }
          } else if (ep.getUnchangedOrOldParentNode().getTag() == BastArrayRef.TAG) {
            if (ep.getOldOrInsertedNode().getTag() == BastNameIdent.TAG) {
              // do nothing;
            }
          } else if (ep.getUnchangedOrOldParentNode().getTag() == BastCase.TAG
              && childrenListNumber == BastFieldConstants.CASE_CONSTANT) {
            if (ep.getOldOrInsertedNode().getTag() == BastNameIdent.TAG) {
              continue;
            }
          }
          workList.add(ep);
          break;
        default:
          workList.add(ep);
      }
    }

    return workList;
  }



  public static FilterRule getInstance() {
    return new RemoveIdentifierMoves();
  }
}