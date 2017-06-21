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

package de.fau.cs.inf2.cas.ares.bast.nodes;

import de.fau.cs.inf2.cas.ares.bast.visitors.IAresBastVisitor;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractAresStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;

/**
 * todo.
 *
 * <p>To generalize ARES patterns
 * 
 */
public class AresWildcard extends AbstractAresStatement {

  public static final int TAG = TagConstants.ARES_WILDCARD;

  public AresPatternClause pattern = null;
  public AresPluginClause plugin = null;
  public LinkedList<AbstractBastStatement> statements = null;

  public LinkedList<AbstractBastNode> associatedStatements = null;
  public LinkedList<AbstractBastNode> fixedNodes = null;
  
  /**
   * Instantiates a new ARES wildcard.
   *
   * @param tokens the tokens
   * @param plugin the plugin
   * @param statements the statements
   */
  public AresWildcard(TokenAndHistory[] tokens, AresPluginClause plugin,
      LinkedList<AbstractBastStatement> statements) {
    super(tokens);
    this.plugin = plugin;
    fieldMap.put(BastFieldConstants.ARES_WILDCARD_PLUGIN, new BastField(plugin));
    this.pattern = plugin.pattern;
    fieldMap.put(BastFieldConstants.ARES_WILDCARD_PATTERN, new BastField(plugin.pattern));
    this.statements = statements;
    fieldMap.put(BastFieldConstants.ARES_WILDCARD_STATEMENTS, new BastField(statements));
  }

  
  /**
   * Accept.
   *
   * @param visitor the visitor
   */
  public void accept(IAresBastVisitor visitor) {
    visitor.visit(this);

  }

  
  /**
   * Gets the tag.
   *
   * @return the tag
   */
  @Override
  public int getTag() {
    return TAG;
  }

  
  /**
   * Replace field.
   *
   * @param field the field
   * @param fieldValue the field value
   */
  @SuppressWarnings("unchecked")
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case ARES_WILDCARD_PATTERN:
        this.pattern = (AresPatternClause) fieldValue.getField();
        break;
      case ARES_WILDCARD_PLUGIN:
        this.plugin = (AresPluginClause) fieldValue.getField();
        break;
      case ARES_WILDCARD_STATEMENTS:
        this.statements = (LinkedList<AbstractBastStatement>) fieldValue.getListField();
        break;
      default:
        assert (false);

    }
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return "//# wildcard (" + plugin + ")" + statements;
  }

}
