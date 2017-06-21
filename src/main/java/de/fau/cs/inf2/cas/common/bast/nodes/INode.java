package de.fau.cs.inf2.cas.common.bast.nodes;

import java.util.List;

public interface INode {
  
  /**
   * Gets the type wrapped.
   *
   * @return the type wrapped
   */
  public abstract int getTypeWrapped();

  /**
   * Gets the children wrapped.
   *
   * @return the children wrapped
   */
  public abstract List<INode> getChildrenWrapped();

  /**
   * Gets the label.
   *
   * @return the label
   */
  public abstract String getLabel();

  /**
   * Gets the id.
   *
   * @return the id
   */
  public abstract int getId();

  /**
   * Checks if is leaf.
   *
   * @return true, if is leaf
   */
  public abstract boolean isLeaf();
}
