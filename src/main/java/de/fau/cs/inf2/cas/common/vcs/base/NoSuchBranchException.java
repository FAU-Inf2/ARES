package de.fau.cs.inf2.cas.common.vcs.base;

/**
 * Thrown when searching for a nonexistant branch.
 *
 * @author Marius Kamp
 */
public class NoSuchBranchException extends RuntimeException {

  /**
   * todo.
   */
  private static final long serialVersionUID = -8307270302096311794L;

  /**
   * Instantiates a new no such branch exception.
   *
   * @param branch the branch
   */
  public NoSuchBranchException(final String branch) {
    super(String.format("Branch '%s' not available!", branch));
  }
}
