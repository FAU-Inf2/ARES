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
