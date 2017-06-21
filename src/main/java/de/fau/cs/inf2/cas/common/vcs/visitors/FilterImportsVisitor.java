package de.fau.cs.inf2.cas.common.vcs.visitors;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastImportDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastPackage;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.visitors.DefaultFieldVisitor;

import java.util.LinkedList;

public class FilterImportsVisitor extends DefaultFieldVisitor {

  
  /**
   * Visit.
   *
   * @param program the program
   */
  @Override
  public void visit(BastProgram program) {
    program.replaceField(BastFieldConstants.PROGRAM_PACKAGE, new BastField((BastPackage) null));
    program.replaceField(BastFieldConstants.PROGRAM_IMPORTS,
        new BastField(new LinkedList<BastImportDeclaration>()));
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
