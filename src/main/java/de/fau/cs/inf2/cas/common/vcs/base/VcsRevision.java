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
