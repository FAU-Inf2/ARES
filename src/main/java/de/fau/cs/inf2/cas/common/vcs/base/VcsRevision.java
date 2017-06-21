package de.fau.cs.inf2.cas.common.vcs.base;

/**
 * A single named commit.
 *
 * @author Marius Kamp
 */
public abstract class VcsRevision {
  private final String rawName;
  private final VcsRepository repository;

  /**
   * todo.
   *
   *<p>Create a new revision object.
   */
  protected VcsRevision(final VcsRepository repository, final String rawName) {
    this.repository = repository;
    this.rawName = rawName;
  }

  /**
   * todo.
   * 
   * <p>Returns a raw representation of the name of this revision. It is primally used with the
   * underlying Vcs api.
   *
   * @return the raw name
   */
  public String getRawName() {
    return rawName;
  }

  /**
   * todo.
   * 
   * <p>Returns a user friendly representation of the name of
   *  this revision (usually a branch or tag).
   *
   * @return the name
   */
  public abstract String getName();

  /**
   * todo.
   * 
   * <p>Returns the message associated with this revision.
   *
   * @return the message
   */
  public abstract String getMessage();

  /**
   * todo.
   * 
   * <p>Returns the repository associated with this revision.
   *
   * @return the repository
   */
  public VcsRepository getRepository() {
    return repository;
  }
}
