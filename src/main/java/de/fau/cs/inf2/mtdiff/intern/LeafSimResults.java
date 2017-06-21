package de.fau.cs.inf2.mtdiff.intern;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;

public class LeafSimResults<T> {
  public final ConcurrentHashMap<T, ArrayList<MatchingCandidate<T>>> subleafCandidateMap;
  public final ConcurrentSkipListSet<MatchingCandidate<T>> submatchedLeaves;

  /**
   * Instantiates a new leaf sim results.
   *
   * @param subleafCandidateMap the subleaf candidate map
   * @param submatchedLeaves the submatched leaves
   */
  LeafSimResults(ConcurrentHashMap<T, ArrayList<MatchingCandidate<T>>> subleafCandidateMap,
      ConcurrentSkipListSet<MatchingCandidate<T>> submatchedLeaves) {
    super();
    this.subleafCandidateMap = subleafCandidateMap;
    this.submatchedLeaves = submatchedLeaves;
  }

}
