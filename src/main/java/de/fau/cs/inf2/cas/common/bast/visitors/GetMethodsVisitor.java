package de.fau.cs.inf2.cas.common.bast.visitors;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;

import java.util.ArrayList;
import java.util.HashMap;

public class GetMethodsVisitor extends DefaultFieldVisitor {

  public HashMap<String, ArrayList<BastFunction>> functionMap =
      new HashMap<String, ArrayList<BastFunction>>();
  public HashMap<Integer, BastFunction> functionIdMap = new HashMap<Integer, BastFunction>();
  public HashMap<BastFunction, Integer> idMap2Function = new HashMap<BastFunction, Integer>();

  private int id = 0;

  /**
   * Instantiates a new gets the methods visitor.
   */
  public GetMethodsVisitor() {

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
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastFunction node) {
    functionIdMap.put(id, node);
    idMap2Function.put(node, id);
    id++;
    if (((BastFunction) node).name == null) {
      return;
    }
    ArrayList<BastFunction> functions = functionMap.get(((BastFunction) node).name);
    if (functions == null) {
      functions = new ArrayList<BastFunction>();
      functionMap.put(((BastFunction) node).name, functions);
    }
    functions.add(node);
    super.visit(node);
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
