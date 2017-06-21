package de.fau.cs.inf2.cas.common.vcs.base;

import java.io.File;

/**
 * Thrown when an invalid working directory is given
 * to {@see VcsRepository.setWorkDirectory}.
 *
 * @author Marius Kamp
 */
public final class InvalidWorkDirectoryException extends Exception {
  /**
   * todo.
   */
  private static final long serialVersionUID = -6212415478931811157L;
  private final File workDirectory;

  /**
   * Instantiates a new invalid work directory exception.
   *
   * @param workDirectory the work directory
   */
  public InvalidWorkDirectoryException(final File workDirectory) {
    super(String.format("'%s' is not a valid working directory!", workDirectory.getAbsolutePath()));

    this.workDirectory = workDirectory;
  }

  /**
   * Gets the work directory.
   *
   * @return the work directory
   */
  public File getWorkDirectory() {
    return workDirectory;
  }
}
