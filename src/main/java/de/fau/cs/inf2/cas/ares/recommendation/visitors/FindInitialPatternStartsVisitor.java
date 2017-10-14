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

package de.fau.cs.inf2.cas.ares.recommendation.visitors;

import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.recommendation.ExtendedAresPattern;
import de.fau.cs.inf2.cas.ares.recommendation.plugin.PluginTester;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEmptyStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeSpecifier;
import de.fau.cs.inf2.cas.common.bast.type.BastBasicType;
import de.fau.cs.inf2.cas.common.bast.visitors.FindPatternStartsVisitor;

import java.util.LinkedList;
import java.util.Map.Entry;

public class FindInitialPatternStartsVisitor extends AresDefaultFieldVisitor {

  ExtendedAresPattern template;
  public LinkedList<AbstractBastNode> starts = new LinkedList<AbstractBastNode>();
  public boolean exactName = false;
  protected LinkedList<AbstractBastStatement> statementsToFind = null;
  int nodesToSkip = 0;
  private boolean visit = true;


  /**
   * Instantiates a new find special pattern starts visitor.
   *
   */
  public FindInitialPatternStartsVisitor(LinkedList<AbstractBastStatement> statementsToFind,
      ExtendedAresPattern template) {
    this.template = template;
    this.statementsToFind = statementsToFind;
    for (AbstractBastStatement stmt : statementsToFind) {
      if (WildcardAccessHelper.isWildcard(stmt)) {
        nodesToSkip++;
      } else if (stmt.getTag() == BastEmptyStmt.TAG) {
        nodesToSkip++;
      }
    }
  }

  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    if (statementsToFind == null || statementsToFind.size() == 0) {
      return;
    }
    int index = 0;
    AbstractBastStatement nodeToFind = statementsToFind.get(0);
    while (index < statementsToFind.size()) {
      if (WildcardAccessHelper.isWildcard(statementsToFind.get(index))) {
        index++;
        continue;
      } else {
        nodeToFind = statementsToFind.get(index);

        break;
      }
    }
    if (index == statementsToFind.size()) {
      return;
    }
    boolean valid = isValidNode(node, nodeToFind);
    if (!valid) {
      return;
    }
    if (statementsToFind.size() == 1) {
      starts.add(node);
      return;
    }
    if (!fieldId.isList) {
      return;
    }
    @SuppressWarnings("unchecked")
    LinkedList<AbstractBastNode> nodes =
        (LinkedList<AbstractBastNode>) globalParent.fieldMap.get(fieldId).getListField();
    int remainingSize = nodes.size() - listId;
    int patternSize = statementsToFind.size() - nodesToSkip;
    if (remainingSize < patternSize) {
      return;
    }
    int locationPosition = listId + 1;
    boolean possibleLocationFound = false;
    for (int i = index + 1; i < statementsToFind.size(); i++) {
      if (WildcardAccessHelper.isWildcard(statementsToFind.get(i))
          || statementsToFind.get(i).getTag() == BastEmptyStmt.TAG) {
        if (i == statementsToFind.size() - 1) {
          possibleLocationFound = true;
        }
        continue;
      }
      AbstractBastNode nextNodeToFind = statementsToFind.get(i);
      boolean found = false;
      for (int j = locationPosition; j < nodes.size(); j++) {
        valid = isValidNode(nodes.get(j), nextNodeToFind);
        if (valid) {
          locationPosition = j + 1;
          found = true;
          break;
        }
      }
      if (!found) {
        return;
      }
      if (i == statementsToFind.size() - 1) {
        possibleLocationFound = true;
      }
    }
    if (possibleLocationFound) {
      starts.add(node);
    }
  }

  private boolean isValidNode(AbstractBastNode node, AbstractBastNode nodeToFind) {
    if (node.getTag() == nodeToFind.getTag()) {
      if (node.getTag() == BastBasicType.TAG) {
        if (((BastBasicType) node).getTypeTag2() == ((BastBasicType) node).getTypeTag2()) {
          return true;
        }
      } else if (node.getTag() == BastNameIdent.TAG) {
        if (exactName) {
          if (((BastNameIdent) nodeToFind).name.equals(((BastNameIdent) node).name)) {
            return true;
          }
        } else {
          return true;
        }
      } else {
        if (template != null && template.minExpr.get(nodeToFind) != null) {
          for (Entry<BastFieldConstants, Integer> tmpMap : template.minExpr.get(nodeToFind)
              .entrySet()) {
            if (tmpMap.getKey().isList) {
              LinkedList<? extends AbstractBastNode> tmp =
                  node.getField(tmpMap.getKey()).getListField();
              if (tmp.size() >= tmpMap.getValue()) {
                return true;
              }
            }
          }
        } else {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Standard visit.
   *
   * @param constant the constant
   * @param node the node
   */
  public void standardVisit(BastFieldConstants constant, AbstractBastNode node) {
    if (visit == false) {
      return;
    }
    if (node.fieldMap.get(constant) != null) {
      if (node.fieldMap.get(constant).isList()) {
        int counter = 0;
        if (node.fieldMap.get(constant).getListField() != null) {
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

  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {
    visit = true;

  }
}
