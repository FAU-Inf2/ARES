package de.fau.cs.inf2.cas.ares.recommendation.extension;

import de.fau.cs.inf2.cas.ares.bast.diff.AresTreeIterator;
import de.fau.cs.inf2.cas.common.bast.diff.IterationOrder;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.mtdiff.editscript.operations.BastEditOperation;

import java.util.Comparator;
import java.util.HashMap;

public class InsertComparator implements Comparator<BastEditOperation> {

  HashMap<AbstractBastNode, Integer> map = new HashMap<>();

  /**
   * Instantiates a new insert comparator.
   *
   * @param root the root
   */
  public InsertComparator(AbstractBastNode root) {
    AresTreeIterator iterator = new AresTreeIterator(IterationOrder.BREADTH_FIRST, root);
    int pos = 0;
    while (iterator.hasNext()) {
      AbstractBastNode node = iterator.next();
      map.put(node, pos);
      pos++;
    }
  }

  @Override
  public int compare(BastEditOperation arg0, BastEditOperation arg1) {
    if (map.get(arg0.getNewOrChangedNode()) == null) {
      assert (false);
    }
    int posArg0 = map.get(arg0.getNewOrChangedNode());
    int posArg1 = map.get(arg1.getNewOrChangedNode());
    return Integer.compare(posArg0, posArg1);
  }

}