package de.fau.cs.inf2.cas.common.bast.general;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import java.util.LinkedList;

public class BastField {
  private LinkedList<? extends AbstractBastNode> list = null;
  private AbstractBastNode field = null;
  private final boolean isList;

  /**
   * Instantiates a new bast field.
   *
   * @param field the field
   */
  public BastField(AbstractBastNode field) {
    this.field = field;
    isList = false;
  }

  /**
   * Instantiates a new bast field.
   *
   * @param list the list
   */
  public BastField(LinkedList<? extends AbstractBastNode> list) {
    this.list = list;
    isList = true;
  }

  /**
   * Gets the field.
   *
   * @return the field
   */
  public AbstractBastNode getField() {
    if (list != null) {
      assert (false);
    }
    return field;
  }

  /**
   * Gets the list field.
   *
   * @return the list field
   */
  public LinkedList<? extends AbstractBastNode> getListField() {
    assert (field == null);
    return list;
  }

  /**
   * Checks if is list.
   *
   * @return true, if is list
   */
  public boolean isList() {
    return isList;
  }

}
