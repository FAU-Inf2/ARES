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

import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresUseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;

import de.fau.cs.inf2.cas.common.parser.odin.JavaToken;

import java.util.HashMap;

public class UpdateWildcardNameVisitor extends AresDefaultFieldVisitor {

  private HashMap<String, String> nameMapping = new HashMap<>();
  private int count = 0;

  /**
   * Instantiates a new update wildcard name visitor.
   */
  public UpdateWildcardNameVisitor() {}

  private void replaceName(BastNameIdent name) {
    if (name != null) {
      String replaceName = nameMapping.get(name.name);
      if (replaceName == null) {
        replaceName = "ARES" + count;
        nameMapping.put(name.name, replaceName);
        count++;
      }
      name.name = replaceName;
      ((JavaToken) name.info.tokens[0].token).data = new StringBuilder(replaceName);
    }
  }

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    if (node.getTag() == AresWildcard.TAG) {
      AresWildcard wildcard = (AresWildcard) node;
      if (wildcard.plugin != null && wildcard.plugin.exprList != null
          && wildcard.plugin.exprList.size() > 0) {
        AresPatternClause pattern = (AresPatternClause) wildcard.plugin.exprList.get(0);
        BastNameIdent name = pattern.ident;
        replaceName(name);
      }
    } else if (node.getTag() == AresUseStmt.TAG) {
      AresUseStmt use = (AresUseStmt) node;
      if (WildcardAccessHelper.getName(node) != null) {
        replaceName(WildcardAccessHelper.getName(node));
      }
    }
  }

  
  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {}

}
