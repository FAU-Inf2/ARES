/*
 * Copyright (c) 2017 Programming Systems Group, CS Department, FAU
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */

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
