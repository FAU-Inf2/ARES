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

package de.fau.cs.inf2.cas.ares.recommendation.plugin;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;

public class PluginTester {

  public static final int PLUGIN_ACCEPT_ALL_STMT = 0;
  public static final int PLUGIN_ACCEPT_ALL_EXPR = 1;
  private static final int PLUGIN_PRIMARY_EXPR = 2;
  public static final int PLUGIN_IS_SHARED = 3;
  private static final int PLUGIN_IS_PARALLEL = 4;
  public static final int PLUGIN_ACCEPT_ALL_CONSTANT = 5;
  public static final int PLUGIN_ACCEPT_ALL_WITHOUT_CHANGE_TO = 6;

  /**
   * Gets the plugin id.
   *
   * @param name the name
   * @return the plugin id
   */
  public static int getPluginId(String name) {
    if (name.equals("acceptAllStmt") || name.equals("stmt")) {
      return PLUGIN_ACCEPT_ALL_STMT;
    } else if (name.equals("acceptAllExpr") || name.equals("expr")) {
      return PLUGIN_ACCEPT_ALL_EXPR;
    } else if (name.equals("primaryExpr")) {
      return PLUGIN_PRIMARY_EXPR;
    } else if (name.equals("isShared")) {
      return PLUGIN_IS_SHARED;
    } else if (name.equals("isParallel")) {
      return PLUGIN_IS_PARALLEL;
    } else if (name.equals("acceptAllConstant")) {
      return PLUGIN_ACCEPT_ALL_CONSTANT;
    } else if (name.equals("acceptAllWithoutChangeTo")) {
      return PLUGIN_ACCEPT_ALL_WITHOUT_CHANGE_TO;
    } else {
      System.err.println("Plugin not found: " + name);
      assert (false);
    }
    return -1;
  }

  /**
   * Gets the plugin penalty.
   *
   * @param program the program
   * @param node the node
   * @param name the name
   * @return the plugin penalty
   */
  public static int getPluginPenalty(BastProgram program, AbstractBastNode node, String name) {
    assert (false);
    return 1;
  }
}
