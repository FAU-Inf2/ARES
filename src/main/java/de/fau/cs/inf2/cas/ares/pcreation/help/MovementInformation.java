package de.fau.cs.inf2.cas.ares.pcreation.help;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;

import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;

import java.util.ArrayList;

public class MovementInformation {
  private int[] oldMap;
  public ArrayList<BastEditOperation> delList = new ArrayList<>();
  public ArrayList<BastEditOperation> insList = new ArrayList<>();

  /**
   * Instantiates a new movement information.
   *
   * @param oldNode the old node
   * @param oldSize the old size
   * @param newNode the new node
   * @param newSize the new size
   */
  public MovementInformation(AbstractBastNode oldNode, int oldSize, AbstractBastNode newNode,
      int newSize) {
    oldMap = new int[oldSize];
    for (int i = 0; i < oldSize; i++) {
      oldMap[i] = i;
    }

  }

  /**
   * Update map.
   *
   * @param begin the begin
   * @param end the end
   * @param diff the diff
   */
  public void updateMap(int begin, int end, int diff) {
    int[] tmp = oldMap;

    int tmpBegin = (begin == -1) ? 0 : begin;

    int tmpEnd = (end == -1) ? tmp.length : end;
    for (int i = tmpBegin; i < tmpEnd; i++) {
      tmp[i] = tmp[i] + diff;
    }

  }

}
