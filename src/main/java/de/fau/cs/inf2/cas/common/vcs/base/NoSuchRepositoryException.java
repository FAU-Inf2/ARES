package de.fau.cs.inf2.cas.common.vcs.base;

/**
 * Thrown when attempting to access a nonexistant repository.
 *
 * @author Marius Kamp
 */
public class NoSuchRepositoryException extends Exception {
  /**
   * todo.
   */
  private static final long serialVersionUID = -2746086683058446150L;

  /**
   * Instantiates a new no such repository exception.
   *
   * @param repoUri the repo uri
   */
  public NoSuchRepositoryException(final String repoUri) {
    super(String.format("Repository at '%s' does not exist!", repoUri));
  }
}
