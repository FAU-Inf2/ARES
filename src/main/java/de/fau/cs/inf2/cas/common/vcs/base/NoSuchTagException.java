package de.fau.cs.inf2.cas.common.vcs.base;

/**
 * Thrown when searching for a nonexistant tag.
 *
 * @author Marius Kamp
 */
public class NoSuchTagException extends RuntimeException {

  /**
   * todo.
   */
  private static final long serialVersionUID = 8528964678495463398L;

  /**
   * Instantiates a new no such tag exception.
   *
   * @param tag the tag
   */
  public NoSuchTagException(final String tag) {
    super(String.format("Tag '%s' not available!", tag));
  }
}
