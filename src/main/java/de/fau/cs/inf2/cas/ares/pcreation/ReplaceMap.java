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
