/*
 * Copyright (c) 2017 Programming Systems Group, CS Department, FAU
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 */

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
