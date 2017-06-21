package de.fau.cs.inf2.cas.common.io;

public class EditOperationCounts {
  public final int all;
  public final int moves;
  public final int inserts;
  public final int deletes;
  public final int updates;
  public final int aligns;

  EditOperationCounts(int all, int moves, int inserts, int deletes, int updates, int aligns) {
    this.all = all;
    this.moves = moves;
    this.inserts = inserts;
    this.deletes = deletes;
    this.updates = updates;
    this.aligns = aligns;
  }
}
