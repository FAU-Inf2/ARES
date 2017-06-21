package de.fau.cs.inf2.cas.ares.recommendation.extension;

import de.fau.cs.inf2.cas.ares.bast.diff.AresTreeIterator;
import de.fau.cs.inf2.cas.common.bast.diff.IterationOrder;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;

import java.util.Comparator;
import java.util.HashMap;

public class DeleteComparator implements Comparator<BastEditOperation> {

  HashMap<AbstractBastNode, Integer> map = new HashMap<>();

  /**
   * Instantiates a new delete comparator.
   *
   * @param root the root
   */
  public DeleteComparator(AbstractBastNode root) {
    AresTreeIterator iterator = new AresTreeIterator(IterationOrder.DEPTH_FIRST_POST_ORDER, root);
    int pos = 0;
    while (iterator.hasNext()) {
      AbstractBastNode node = iterator.next();
      map.put(node, pos);
      pos++;
    }
  }

  @Override
  public int compare(BastEditOperation arg0, BastEditOperation arg1) {
    int posArg0 = map.get(arg0.getOldOrInsertedNode());
    int posArg1 = map.get(arg1.getOldOrInsertedNode());
    return Integer.compare(posArg0, posArg1);
  }

}