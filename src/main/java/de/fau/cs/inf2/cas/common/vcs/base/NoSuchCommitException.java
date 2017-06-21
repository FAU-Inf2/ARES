package de.fau.cs.inf2.cas.common.vcs.base;

/**
 * Thrown when searching for a nonexistant commit.
 *
 * @author Marius Kamp
 */
public class NoSuchCommitException extends RuntimeException {
  /**
   * todo.
   */
  private static final long serialVersionUID = 2425933517211852801L;

  /**
   * Instantiates a new no such commit exception.
   *
   * @param commitId the commit id
   */
  public NoSuchCommitException(final String commitId) {
    super(String.format("No commit '%s' available!", commitId));
  }
}
