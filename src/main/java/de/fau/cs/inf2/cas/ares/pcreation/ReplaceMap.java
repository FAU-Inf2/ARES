package de.fau.cs.inf2.cas.ares.pcreation;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.HashMap;
import java.util.Set;

public class ReplaceMap {
  private HashMap<AbstractBastNode, AbstractBastNode> map = new HashMap<>();
  private HashMap<AbstractBastNode, Boolean> exprMap = new HashMap<>();

  /**
   * Gets the.
   *
   * @param node the node
   * @return the abstract bast node
   */
  public AbstractBastNode get(AbstractBastNode node) {
    return map.get(node);
  }

  /**
   * Put.
   *
   * @param key the key
   * @param value the value
   * @param isExprWildcard the is expr wildcard
   */
  public void put(AbstractBastNode key, AbstractBastNode value, boolean isExprWildcard) {
    map.put(key, value);
    exprMap.put(key, isExprWildcard);
  }

  /**
   * Belongs to expr.
   *
   * @param key the key
   * @return the boolean
   */
  public Boolean belongsToExpr(AbstractBastNode key) {
    return exprMap.get(key);
  }

  /**
   * Entry set.
   *
   * @return the sets the
   */
  public Set<java.util.Map.Entry<AbstractBastNode, AbstractBastNode>> entrySet() {
    return map.entrySet();
  }
}
