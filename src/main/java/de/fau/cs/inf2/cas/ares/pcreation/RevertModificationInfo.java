package de.fau.cs.inf2.cas.ares.pcreation;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.cas.common.parser.IGeneralToken;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

class RevertModificationInfo {
  public BastFieldConstants fieldConstant;
  public BastField field = null;
  public AbstractBastNode parent = null;
  private HashMap<AbstractBastNode, LinkedList<IGeneralToken>> updateTokens = new HashMap<>();

  /**
   * Instantiates a new revert modification info.
   */
  public RevertModificationInfo() {}

  /**
   * Revert.
   */
  public void revert() {
    if (parent != null && field != null && fieldConstant != null) {
      parent.replaceField(fieldConstant, field);
      for (Entry<AbstractBastNode, LinkedList<IGeneralToken>> tokenEntry : updateTokens
          .entrySet()) {
        tokenEntry.getKey().info.tokens[0].prevTokens.clear();
        tokenEntry.getKey().info.tokens[0].prevTokens.addAll(tokenEntry.getValue());
      }
    }
  }

  /**
   * Update tokens.
   *
   * @param abstractBastExpr the abstract bast expr
   * @param prevTokens the prev tokens
   */
  public void updateTokens(AbstractBastExpr abstractBastExpr,
      LinkedList<IGeneralToken> prevTokens) {
    LinkedList<IGeneralToken> list = new LinkedList<IGeneralToken>();
    list.addAll(prevTokens);
    updateTokens.put(abstractBastExpr, list);

  }
}
