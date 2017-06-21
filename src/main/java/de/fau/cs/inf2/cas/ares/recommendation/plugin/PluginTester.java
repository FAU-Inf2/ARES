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
