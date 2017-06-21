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

package de.fau.cs.inf2.cas.ares.recommendation;

import de.fau.cs.inf2.cas.ares.bast.general.ParentHierarchyHandler;
import de.fau.cs.inf2.cas.ares.bast.general.ParserFactory;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresPatternClause;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresWildcard;
import de.fau.cs.inf2.cas.ares.bast.visitors.NodeStreamVisitor;
import de.fau.cs.inf2.cas.ares.parser.odin.AresJavaParser;
import de.fau.cs.inf2.cas.ares.pcreation.WildcardAccessHelper;
import de.fau.cs.inf2.cas.ares.recommendation.visitors.CollectWildcardVisitor;
import de.fau.cs.inf2.cas.ares.recommendation.visitors.FindPatternOccurrence;
import de.fau.cs.inf2.cas.ares.recommendation.visitors.FindSpecialPatternStartsVisitor;
import de.fau.cs.inf2.cas.ares.recommendation.visitors.IsomorphLinearizationVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.CreateJavaNodeHelper;
import de.fau.cs.inf2.cas.common.bast.general.NodeParentInformationHierarchy;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAsgnExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.visitors.FindNodesFromTagVisitor;
import de.fau.cs.inf2.cas.common.bast.visitors.IPrettyPrinter;

import de.fau.cs.inf2.cas.common.parser.AresExtension;
import de.fau.cs.inf2.cas.common.parser.odin.FileData;
import de.fau.cs.inf2.cas.common.parser.odin.JavaLexer;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

public class ExtendedAresPattern implements Cloneable {
  
  
  /**
   * Read external.
   *
   * @param in the in
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ClassNotFoundException the class not found exception
   */
  public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
  }

  
  /**
   * Write external.
   *
   * @param out the out
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void writeExternal(ObjectOutput out) throws IOException {

  }

  public AresBlock originalAst = null;
  public AbstractBastNode patternStart = null;
  public int patternStartListIdOld = -1;
  private ExtendedDiffResult diffResult = null;
  public HashMap<AbstractBastNode, LinkedList<BastEditOperation>> parentMap = null;
  public AresBlock modifiedAst = null;
  private HashSet<String> identifierNamesToIgnore;
  public HashMap<AbstractBastNode, AbstractBastNode> assignmentMap = new HashMap<>();
  public HashMap<AbstractBastNode, HashMap<BastFieldConstants, Integer>> minExpr = new HashMap<>();

  public HashMap<WildcardInstance, Integer> wildCardPenalties =
      new HashMap<WildcardInstance, Integer>();

  /**
   * Instantiates a new extended ARES template.
   *
   * @param originalAst the pattern
   * @param enclosingFunction the enclosing function
   * @param modifiedAst the changed pattern
   */
  public ExtendedAresPattern(AresBlock originalAst, BastFunction enclosingFunction,
      AresBlock modifiedAst) {
    this.originalAst = originalAst;
    this.modifiedAst = modifiedAst;
  }

  
  /**
   * Clone.
   *
   * @return the extended ARES template
   */
  public ExtendedAresPattern clone() {
    ExtendedAresPattern clone = new ExtendedAresPattern(originalAst, null, modifiedAst);
    clone.wildcardInstances = this.wildcardInstances;
    clone.occuringWildcards = this.occuringWildcards;
    return clone;
  }

  private HashMap<Integer, LinkedList<AbstractBastNode>> wildcards = null;

  /**
   * Gets the wildcards.
   *
   * @return the wildcards
   */
  public HashMap<Integer, LinkedList<AbstractBastNode>> getWildcards() {
    return wildcards;
  }

  public LinkedList<WildcardInstance> wildcardInstances = null;

  /**
   * Sets the wildcards.
   *
   * @param wildcards the wildcards
   */
  public void setWildcards(HashMap<Integer, WildcardInstance> wildcards) {
    this.wildcards = new HashMap<Integer, LinkedList<AbstractBastNode>>();
    wildcardInstances = new LinkedList<WildcardInstance>();
    wildcardInstances.addAll(wildcards.values());
    for (Integer wildIdent : wildcards.keySet()) {
      ArrayList<AbstractBastNode> wildcardSet = wildcards.get(wildIdent).getNodes();
      LinkedList<AbstractBastNode> newStmts = new LinkedList<AbstractBastNode>();
      for (AbstractBastNode node : wildcardSet) {
        newStmts.add(node);
      }
      this.wildcards.put(wildIdent, newStmts);
    }

  }

  private LinkedList<AresWildcard> occuringWildcards = null;

  private LinkedList<AbstractBastStatement> getWildcardStmts(
      LinkedList<AbstractBastStatement> wildcardStmts) {
    LinkedList<AbstractBastStatement> extractedStmts = new LinkedList<>();
    for (AbstractBastStatement stmt : wildcardStmts) {
      if (stmt.getTag() == AresWildcard.TAG) {
        extractedStmts.addAll(getWildcardStmts(((AresWildcard) stmt).statements));
      } else {
        extractedStmts.add(stmt);
      }
    }
    return extractedStmts;
  }

  /**
   * Resolve wildcards.
   */
  public void resolveWildcards() {
    CollectWildcardVisitor visitor = new CollectWildcardVisitor();
    originalAst.accept(visitor);
    occuringWildcards = visitor.wildcards;
    for (AresWildcard w : occuringWildcards) {
      if (w.statements == null) {
        if (WildcardAccessHelper.isExprWildcard(w)) {
          Map<AbstractBastNode, NodeParentInformationHierarchy> hierarchy =
              ParentHierarchyHandler.getParentHierarchy(originalAst);
          NodeParentInformationHierarchy npi = hierarchy.get(w);
          @SuppressWarnings("unchecked")
          LinkedList<AbstractBastStatement> blockStmt =
              (LinkedList<AbstractBastStatement>) npi.list.get(0).parent
                  .getField(npi.list.get(0).fieldConstant).getListField();
          w.statements = new LinkedList<>();
          w.statements.add(blockStmt.get(blockStmt.indexOf(w) + 1));
        }
      }
    }
    outer : for (AresWildcard w : occuringWildcards) {
      w.associatedStatements = new LinkedList<>();
      assert (w.plugin != null);
      if (w.plugin.exprList != null && w.plugin.exprList.peek() != null) {
        if (w.plugin.exprList.peek().getTag() == AresPatternClause.TAG) {
          AresPatternClause lpc = ((AresPatternClause) w.plugin.exprList.peek());
          if (lpc.expr != null) {
            FindSpecialPatternStartsVisitor fps = null;
            AbstractBastNode expression = null;
            if (w.statements != null) {
              LinkedList<AbstractBastStatement> statements = getWildcardStmts(w.statements);
             
              if (statements.getFirst().getTag() == BastDeclaration.TAG) {
                if (lpc.expr.getTag() == BastAsgnExpr.TAG) {
                  expression = handleAsgnExpr(w, lpc);
                  continue outer;
                } else if (lpc.expr.getTag() == BastIntConst.TAG) {
                  FindNodesFromTagVisitor fnft = 
                      new FindNodesFromTagVisitor(BastIdentDeclarator.TAG);
                  statements.getFirst().accept(fnft);
                  int count = 0;
                  for (AbstractBastNode node : fnft.nodes) {
                    BastIdentDeclarator ident = (BastIdentDeclarator)node;
                    BastExprInitializer expr = (BastExprInitializer) ident.expression;
                    if (WildcardAccessHelper.isEqual(expr.init, lpc.expr)) {
                      count++;
                    }
                    if (count == WildcardAccessHelper.getOccurence(w).value) {                     
                      expression = handleIntConst(w, lpc, statements, ident);
                      continue outer;
                    }
                  }
                } else {
                  expression = lpc.expr;
                }
              } else {
                expression = lpc.expr;
              }
              if (expression != null) {
                fps = new FindSpecialPatternStartsVisitor(expression, null);
                for (AbstractBastStatement stmt : statements) {
                  stmt.accept(fps);
                }
              }
            } else {
              if (WildcardAccessHelper.isExprWildcard(w)) {

                assert (false);
              }
            }
            if (expression != null && lpc.occurrence != null) {
              FindPatternOccurrence fwcV =
                  new FindPatternOccurrence(expression, (int) lpc.occurrence.value);
              if (fps != null) {
                for (AbstractBastNode stmt : fps.starts) {
                  stmt.accept(fwcV);
                }
                w.associatedStatements.addAll(fwcV.matches);
              }
            }
          }
        } else {
          assert (false);
        }
      } else {
        IsomorphLinearizationVisitor iv = new IsomorphLinearizationVisitor();
        if (w.statements != null) {
          for (AbstractBastStatement stmt : w.statements) {
            stmt.accept(iv);
            w.associatedStatements.addAll(iv.list);
            iv.list.clear();
          }
        }
      }

    }
  }


  private AbstractBastNode handleIntConst(AresWildcard wildcard,
      AresPatternClause lpc,
      LinkedList<AbstractBastStatement> statements, BastIdentDeclarator ident) {
    AbstractBastNode expression;
    expression = ident;
    expression = CreateJavaNodeHelper.cloneTree(expression);
    NodeStreamVisitor nsv = new NodeStreamVisitor(expression);
    expression.accept(nsv);                      
    wildcard.associatedStatements.addAll(nsv.nodes);
    lpc.replaceField(BastFieldConstants
        .ARES_PATTERN_CLAUSE_EXPR, new BastField(expression));
    lpc.replaceField(BastFieldConstants
        .ARES_PATTERN_CLAUSE_OCCURENCE, new BastField(CreateJavaNodeHelper
            .createBastIntConst(1)));
    wildcard.fixedNodes = new LinkedList<>();
    wildcard.fixedNodes.add(ident.identifier);
    HashMap<BastFieldConstants, Integer> tmp = minExpr.get(statements.getFirst());
    if (tmp == null) {
      tmp = new HashMap<>();
      minExpr.put(statements.getFirst(), tmp);
    }
    Integer value = tmp.get(BastFieldConstants.DECLARATION_DECLARATORS);
    if (value == null) {
      tmp.put(BastFieldConstants.DECLARATION_DECLARATORS, 1);
    } else {
      tmp.put(BastFieldConstants.DECLARATION_DECLARATORS, value + 1);
    }
    return expression;
  }


  private AbstractBastNode handleAsgnExpr(AresWildcard wildcard, AresPatternClause lpc) {
    AbstractBastNode expression;
    IPrettyPrinter print = ParserFactory.getAresPrettyPrinter();
    lpc.expr.accept(print);
    String tmp = print.getBuffer().toString();
    byte[] bytes = tmp.toString().getBytes();
    FileData data = new FileData(bytes);
    AresJavaParser parser = (AresJavaParser) 
        ParserFactory.getParserInstance(AresExtension.WITH_ARES_EXTENSIONS);
    JavaLexer lexer = new JavaLexer(AresExtension.WITH_ARES_EXTENSIONS);
    expression = CreateJavaNodeHelper.parseIdentDeclarator(data, parser, lexer);
    lpc.replaceField(BastFieldConstants
        .ARES_PATTERN_CLAUSE_EXPR, new BastField(expression));
    NodeStreamVisitor nsv = new NodeStreamVisitor(expression);
    expression.accept(nsv);                      
    wildcard.associatedStatements.addAll(nsv.nodes);
    return expression;
  }

  /**
   * Gets the identifier names to ignore.
   *
   * @return the identifier names to ignore
   */
  public HashSet<String> getIdentifierNamesToIgnore() {
    return identifierNamesToIgnore;
  }

  /**
   * Sets the identifier names to ignore.
   *
   * @param identifierNamesToIgnore the new identifier names to ignore
   */
  public void setIdentifierNamesToIgnore(HashSet<String> identifierNamesToIgnore) {
    this.identifierNamesToIgnore = identifierNamesToIgnore;
  }

  boolean ignoreIdentifier(String name) {
    return identifierNamesToIgnore.contains(name);
  }
  
  /**
   * Sets the diff result.
   *
   * @param diffResult the new diff result
   */
  public void setDiffResult(ExtendedDiffResult diffResult) {
    this.diffResult = diffResult;
    HashMap<AbstractBastNode, LinkedList<BastEditOperation>> parentMap = new HashMap<>();
    for (BastEditOperation op : diffResult.editScript) {
      
      LinkedList<BastEditOperation> operations = parentMap.get(op.getUnchangedOrOldParentNode());
      if (operations == null) {
        operations = new LinkedList<>();
        parentMap.put(op.getUnchangedOrOldParentNode(), operations);
      }
      operations.add(op);
      if (op.getUnchangedOrOldParentNode() != op.getUnchangedOrNewParentNode()) {
        operations = parentMap.get(op.getUnchangedOrNewParentNode());
        if (operations == null) {
          operations = new LinkedList<>();
          parentMap.put(op.getUnchangedOrNewParentNode(), operations);
        }
        operations.add(op);

      }
    }
    this.parentMap = parentMap;
  }
  
  /**
   * Gets the diff result.
   *
   * @return the diff result
   */
  public ExtendedDiffResult getDiffResult() {
    return diffResult;
  }

}
