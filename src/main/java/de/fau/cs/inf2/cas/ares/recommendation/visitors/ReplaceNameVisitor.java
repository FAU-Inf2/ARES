package de.fau.cs.inf2.cas.ares.recommendation.visitors;

import de.fau.cs.inf2.cas.ares.bast.visitors.AresDefaultFieldVisitor;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;

import de.fau.cs.inf2.cas.common.parser.odin.JavaToken;

import java.util.HashMap;

public class ReplaceNameVisitor extends AresDefaultFieldVisitor {

  private HashMap<String,String> replacements = null;



  /**
   * Instantiates a new replace name visitor.
   *
   * @param replacements the replacements
   */
  public ReplaceNameVisitor(HashMap<String,String> replacements) {
    this.replacements = replacements;
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
    String replace = replacements.get(node.name);
    if (replace != null) {
      node.name = replace;
      ((JavaToken)node.info.tokens[0].token).data.setLength(0);
      ((JavaToken)node.info.tokens[0].token).data.append(replace);
    }
  }

}
