package de.fau.cs.inf2.cas.common.bast.general;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import java.util.List;

public interface INodeWrapper extends INode {

  /**
   * Gets the node.
   *
   * @return the node
   */
  public AbstractBastNode getNode();

  
  /**
   * Gets the id.
   *
   * @return the id
   */
  public int getId();

  
  /**
   * Gets the type wrapped.
   *
   * @return the type wrapped
   */
  public int getTypeWrapped();

  
  /**
   * Gets the children wrapped.
   *
   * @return the children wrapped
   */
  public List<INode> getChildrenWrapped();

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString();

  
  /**
   * Gets the label.
   *
   * @return the label
   */
  public String getLabel();

  /**
   * Gets the value.
   *
   * @param node the node
   * @return the value
   */
  public String getValue(AbstractBastNode node);

  
  /**
   * Checks if is leaf.
   *
   * @return true, if is leaf
   */
  public boolean isLeaf();
  
  
}
