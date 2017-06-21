package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.BastInfo;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * todo.
 *
 * <p>Basis class for Bast nodes, responsible to store source code position
 * 
 */
public abstract class AbstractBastNode {
  private static AtomicInteger idCounter = new AtomicInteger();
  public final int nodeId;
  
  protected int line = -1;
  protected int column = -1;

  public BastInfo info = null;

  public ConcurrentHashMap<BastFieldConstants, BastField> fieldMap = new ConcurrentHashMap<>();

  AbstractBastNode(TokenAndHistory[] tokens) {
    nodeId = idCounter.getAndIncrement();
    this.info = new BastInfo(tokens);
  }
  
  /**
   * Gets the node id.
   *
   * @return the node id
   */
  public int getNodeId() {
    return nodeId;
  }
  
  /**
   * Gets the tag.
   *
   * @return the tag
   */
  public abstract int getTag();

  /**
   * Hash code.
   *
   * @return the int
   */
  public int hashCode() {
    return nodeId;
  }
  
  /**
   * Accept.
   *
   * @param visitor the visitor
   */
  public abstract void accept(IBastVisitor visitor);

  /**
   * Gets the field.
   *
   * @param fieldId the field id
   * @return the field
   */
  public BastField getField(BastFieldConstants fieldId) {
    BastField field = fieldMap.get(fieldId);
    return field;
  }

  /**
   * Gets the type.
   *
   * @return the type
   */
  public int getType() {
    return getTag();
  }

  
  /**
   * Replace field.
   *
   * @param field the field
   * @param fieldValue the field value
   */
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    System.err.println("Replace field of " + this.getClass() + " missing!");
    assert (false);
  }

  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    if (info != null) {
      return info.toString();
    } else {
      return this.getClass().toString();
    }
  }
  
  private int depth = -1;
  
  public void setDepth(int currentDepth) {
    this.depth = currentDepth;
  }

  
  public int getDepth() {
    return depth;
  }

}
