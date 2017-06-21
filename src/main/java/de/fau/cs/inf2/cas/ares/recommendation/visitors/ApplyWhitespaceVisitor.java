package de.fau.cs.inf2.cas.ares.recommendation.visitors;

import de.fau.cs.inf2.cas.ares.bast.nodes.AresCaseStmt;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresChoiceStmt;
import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;
import de.fau.cs.inf2.cas.ares.recommendation.extension.EditScriptApplicator;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;

import de.fau.cs.inf2.cas.common.parser.odin.JavaToken;

;

public class ApplyWhitespaceVisitor extends AresDefaultFieldVisitor {

  private String indentation = null;
  private String diff = null;
  private int depth;


  /**
   * Instantiates a new apply whitespace visitor.
   *
   * @param indentation the indentation
   * @param diff the diff
   * @param depth the depth
   */
  public ApplyWhitespaceVisitor(String indentation, String diff, int depth) {
    this.indentation = indentation;
    this.diff = diff;
    this.depth = depth;
  }


  @Override
  public void beginVisit(AbstractBastNode node) {
    if (globalParent != null) {
      switch (globalParent.getTag()) {
        case BastBlock.TAG:
          EditScriptApplicator.applyWhitespaceLeft(node, indentation, diff, depth);
          break;
        default:
          break;
          
      }
    }
    
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
  public void visit(AresChoiceStmt node) {
    beginVisit(node);
    int localDepth = depth;
    depth --;
    standardVisit(BastFieldConstants.ARES_CHOICE_STMT_BLOCK, node);
    depth = localDepth;
    endVisit(node);
  }
  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AresCaseStmt node) {
    beginVisit(node);
    int localDepth = depth;
    depth --;
    standardVisit(BastFieldConstants.ARES_CASE_STMT_BLOCK, node);
    depth = localDepth;
    endVisit(node);
  }
  
  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastBlock node) {
    int localDepth = depth;
    depth++;
    standardVisit(BastFieldConstants.BLOCK_STATEMENT, node);
    depth = localDepth;
    if (node.info != null && node.info.tokens[2] != null) {
      if (node.info.tokens[2].token != null) {
        JavaToken token = (JavaToken) node.info.tokens[2].token;
        token.whiteSpace.setLength(0);
        token.whiteSpace.append(indentation);
        for (int i = 0; i < depth; i++) {
          token.whiteSpace.append(diff);
        }
      }
      if (node.info.tokens[2].prevTokens != null) {
        if (node.info.tokens[2].prevTokens.size() == 1) {
          JavaToken token = (JavaToken) node.info.tokens[2].prevTokens.getFirst();
          token.whiteSpace.setLength(0);
          token.whiteSpace.append(indentation);
          for (int i = 0; i < depth; i++) {
            token.whiteSpace.append(diff);
          }
        }
      }
      endVisit(node);
    }
  }

}
