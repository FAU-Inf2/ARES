package de.fau.cs.inf2.cas.common.vcs.base;

public class RepositoryClosedException extends RuntimeException {
  /**
   * todo.
   */
  private static final long serialVersionUID = 7535429372202773077L;


  /**
   * Instantiates a new repository closed exception.
   *
   * @param repo the repo
   */
  public RepositoryClosedException(final VcsRepository repo) {
    super(String.format("Repository in '%s' is already closed!", repo.getWorkDirectory()));
  }
}
