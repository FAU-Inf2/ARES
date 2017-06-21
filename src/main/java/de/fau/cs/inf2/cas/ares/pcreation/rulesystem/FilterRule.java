package de.fau.cs.inf2.cas.ares.pcreation.rulesystem;

import de.fau.cs.inf2.cas.ares.pcreation.GeneralizationParameter;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The Interface Rule.
 */
public interface FilterRule {

  /**
   * Execute rule.
   *
   * @return the list
   */
  public List<BastEditOperation> executeRule(List<BastEditOperation> worklist);

  /**
   * Gets the rule name.
   *
   * @return the rule name
   */
  public String getRuleName();

  public FilterRule initRule(HashMap<AbstractBastNode, AbstractBastNode> delInsertMap,
      ArrayList<BastEditOperation> alignList, HashMap<String, String> variableMap,
      GeneralizationParameter parameter);

}
