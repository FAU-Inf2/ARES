package de.fau.cs.inf2.cas.common.vcs.visitors;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.CreateJavaNodeHelper;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.visitors.DefaultFieldVisitor;

import java.util.LinkedList;

public class RenameClassVisitor extends DefaultFieldVisitor {

  private String oldName;
  private String newName;

  /**
   * Instantiates a new rename class visitor.
   *
   * @param oldName the old name
   * @param newName the new name
   */
  public RenameClassVisitor(String oldName, String newName) {
    this.oldName = oldName;
    this.newName = newName;
  }

  
  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastClassDecl node) {
    if (((BastNameIdent) node.getField(BastFieldConstants.CLASS_DECL_NAME).getField()).name
        .equals(oldName)) {
      BastNameIdent newNameIdent = CreateJavaNodeHelper.createBastNameIdent(" " + newName);
      node.replaceField(BastFieldConstants.CLASS_DECL_NAME, new BastField(newNameIdent));
      node.replaceField(BastFieldConstants.CLASS_DECL_MODIFIERS,
          new BastField(new LinkedList<AbstractBastNode>()));
      node.replaceField(BastFieldConstants.CLASS_DECL_TYPE_PARAMETERS,
          new BastField(new LinkedList<AbstractBastNode>()));
      node.replaceField(BastFieldConstants.CLASS_DECL_EXTENDED_CLASS,
          new BastField((AbstractBastNode) null));
      node.replaceField(BastFieldConstants.CLASS_DECL_INTERFACES,
          new BastField(new LinkedList<AbstractBastNode>()));
      for (int i = 0; i < node.info.tokens.length; i++) {
        if (i == 1 || i == 2) {
          node.info.tokens[i] = null;
        }
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
