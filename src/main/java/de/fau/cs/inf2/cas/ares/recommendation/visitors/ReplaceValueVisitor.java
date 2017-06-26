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

import de.fau.cs.inf2.cas.ares.bast.general.AresWrapper;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCharConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastLabelStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameter;
import de.fau.cs.inf2.cas.common.bast.nodes.BastRealConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStringConst;
import de.fau.cs.inf2.cas.common.parser.odin.JavaToken;

import java.math.BigInteger;
import java.util.HashMap;

public class ReplaceValueVisitor extends AresDefaultFieldVisitor {

  private HashMap<Integer, HashMap<String, String>> replacementTagMap = null;



  /**
   * Instantiates a new replace name visitor.
   *
   */
  public ReplaceValueVisitor(HashMap<Integer, HashMap<String, String>> replacementTagMap) {
    this.replacementTagMap = replacementTagMap;
  }


  @Override
  public void beginVisit(AbstractBastNode node) {


  }


  @Override
  public void endVisit(AbstractBastNode node) {

  }

  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastNameIdent node) {
    super.visit(node);
    HashMap<String, String> replacements = replacementTagMap.get(node.getTag());
    if (replacements != null) {
      String replace = replacements.get(node.name);
      if (replace != null) {
        node.name = replace;
        replaceTokenValue(node, replace);
      }
    }
  }

  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastIntConst node) {
    super.visit(node);
    HashMap<String, String> replacements = replacementTagMap.get(node.getTag());
    if (replacements != null) {
      String replace = replacements.get(AresWrapper.staticGetValue(node));
      if (replace != null) {
        node.fitsInLong = false;
        node.bigValue = new BigInteger(replace);
        replaceTokenValue(node, replace);
      }
    }
  }
  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCharConst node) {
    super.visit(node);
    HashMap<String, String> replacements = replacementTagMap.get(node.getTag());
    if (replacements != null) {
      String replace = replacements.get(AresWrapper.staticGetValue(node));
      if (replace != null) {
        node.value = replace.charAt(0);
        replaceTokenValue(node, replace);
      }
    }
  }
  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastRealConst node) {
    super.visit(node);
    HashMap<String, String> replacements = replacementTagMap.get(node.getTag());
    if (replacements != null) {
      String replace = replacements.get(AresWrapper.staticGetValue(node));
      if (replace != null) {
        node.fitsInDouble = false;
        node.actualValue = replace;
        replaceTokenValue(node, replace);
      }
    }
  }
  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastParameter node) {
    super.visit(node);
    HashMap<String, String> replacements = replacementTagMap.get(node.getTag());
    if (replacements != null) {
      String replace = replacements.get(AresWrapper.staticGetValue(node));
      if (replace != null) {
        node.name = replace;
        replaceTokenValue(node, replace);
      }
    }
  }
  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastStringConst node) {
    super.visit(node);
    HashMap<String, String> replacements = replacementTagMap.get(node.getTag());
    if (replacements != null) {
      String replace = replacements.get(AresWrapper.staticGetValue(node));
      if (replace != null) {
        node.value = replace;
        replaceTokenValue(node, replace);
      }
    }
  }
  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastLabelStmt node) {
    super.visit(node);
    HashMap<String, String> replacements = replacementTagMap.get(node.getTag());
    if (replacements != null) {
      String replace = replacements.get(AresWrapper.staticGetValue(node));
      if (replace != null) {
        node.name = replace;
        replaceTokenValue(node, replace);
      }
    }
  }

  private void replaceTokenValue(AbstractBastNode node, String replace) {
    if (node.info == null) {
      return;
    }
    if (node.info.tokens == null) {
      return;
    }
    if (node.info.tokens.length == 0) {
      return;
    }
    if (node.info.tokens[0] == null) {
      return;
    }
    ((JavaToken) node.info.tokens[0].token).data.setLength(0);
    ((JavaToken) node.info.tokens[0].token).data.append(replace);
  }
  
  

}
