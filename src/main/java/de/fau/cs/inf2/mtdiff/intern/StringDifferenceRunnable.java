package de.fau.cs.inf2.mtdiff.intern;

import de.fau.cs.inf2.cas.common.bast.nodes.INode;

import de.fau.cs.inf2.cas.common.util.string.NGramCalculator;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class StringDifferenceRunnable implements Runnable {
  private boolean verbose;
  private static final int HASH_MAP_SIZE = 50000;

  /**
   * Instantiates a new string difference runnable.
   *
   * @param stringSimCache the string sim cache
   * @param subLeaves1 the sub leaves 1
   * @param subLeaves2 the sub leaves 2
   * @param stringCount the string count
   * @param start the start
   * @param end the end
   * @param counter the counter
   * @param verbose the verbose
   */
  public StringDifferenceRunnable(ConcurrentHashMap<String, Float> stringSimCache,
      INode[] subLeaves1, ArrayList<INode> subLeaves2, long stringCount, int start, int end,
      AtomicInteger counter, boolean verbose) {
    super();
    this.stringSimCache = stringSimCache;
    this.subLeaves1 = subLeaves1;
    this.subLeaves2 = subLeaves2;
    this.stringCount = stringCount;
    this.start = start;
    this.end = end;
    this.counter = counter;
    counter.incrementAndGet();
    this.verbose = verbose;
  }

  private ConcurrentHashMap<String, Float> stringSimCache;
  private INode[] subLeaves1;
  private ArrayList<INode> subLeaves2;
  private NGramCalculator stringSim = new NGramCalculator(2);
  private long stringCount;
  private int start;
  private int end;
  private AtomicInteger counter = null;


  /**
   * Run.
   */
  @Override
  public void run() {
    try {
      for (int i = start; i < end; i++) {
        computeStringSimilarities(stringSimCache, subLeaves2, stringSim, stringCount, subLeaves1[i],
            verbose);
      }
      stringSim.clear();
      counter.decrementAndGet();
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  private static void computeStringSimilarities(ConcurrentHashMap<String, Float> stringSimCache,
      ArrayList<INode> subLeaves2, NGramCalculator stringSim, long stringCount, final INode node1,
      boolean verbose) {
    String s1 = (node1.getLabel());
    for (final INode node2 : subLeaves2) {
      String s2 = (node2.getLabel());
      Float sim1 = stringSimCache.get(s1 + "@@" + s2);
      Float sim2 = stringSimCache.get(s2 + "@@" + s1);
      float sim = Float.MIN_VALUE;
      if (sim1 == null && sim2 == null) {
        if (s1.equals(s2)) {
          sim = 1.0f;
        } else {
          if (stringSimCache.size() < HASH_MAP_SIZE) {
            sim = stringSim.similarity(s1, s2);
            stringSimCache.putIfAbsent(s1 + "@@" + s2, sim);
          }
        }
      }
      stringCount++;
    }
  }
}
