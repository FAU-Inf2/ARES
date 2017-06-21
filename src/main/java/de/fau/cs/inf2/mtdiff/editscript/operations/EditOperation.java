package de.fau.cs.inf2.mtdiff.editscript.operations;

import java.util.concurrent.atomic.AtomicInteger;

public class EditOperation {
  
  private static AtomicInteger idCounter = new AtomicInteger(-1);
  public final int opId;
  
  private final EditOperationType type;

  protected EditOperation(final EditOperationType type) {
    this.type = type;
    opId = idCounter.incrementAndGet();
  }

  /**
   * Gets the type.
   *
   * @return the type
   */
  public final EditOperationType getType() {
    return type;
  }
}
