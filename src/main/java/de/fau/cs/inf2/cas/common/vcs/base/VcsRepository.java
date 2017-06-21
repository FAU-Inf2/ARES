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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Basic interface to access a source code repository.
 *
 * @author Marius Kamp
 */
public abstract class VcsRepository {
  protected final List<VcsRevision> branchList = new ArrayList<VcsRevision>();
  private final List<VcsRevision> tagList = new ArrayList<VcsRevision>();
  protected final File workDirectory;
  private boolean closed;

  protected VcsRepository(final File workDir) {
    workDirectory = workDir;
  }

  /**
   * todo.
   * 
   * <p>Checkout a given commit.
   * 
   * 
   * 
   *
   * @param revision A VcsRevision object representing the branch/tag to check out
   * @return An object representing the state of the repository at the given revision
   * @throws RepositoryClosedException Thrown if the repository has already been closed
   */
  public abstract VcsState checkOut(VcsRevision revision);

  /**
   * todo.
   *
   *<p>Closes this repository and releases all associated ressources.
   */
  public void close() {
    closed = true;
  }

  /**
   * todo.
   * 
   * <p>Retrieve a list of branches available in this repository.
   *  If no branches are available an empty
   * list is returned.
   * 
   * @return List of revisions which have been identified as branches
   *
   */
  public final List<VcsRevision> getBranchList() {
    return new ArrayList<VcsRevision>(branchList);
  }

  /**
   * todo.
   * 
   * <p>Retrieve a list of tags available in this repository.
   *  If no tags are available an empty list is
   * returned.
   * 
   * @return List of revisions which have been identified as tags
   *
   */
  public final List<VcsRevision> getTagList() {
    return new ArrayList<VcsRevision>(tagList);
  }

  /**
   * todo.
   * 
   * <p>Retrieve a list of tags available in this repository.
   *  If no commits are available an empty list
   * is returned.
   * 
   * @return List of all revisions
   *
   */
  public abstract List<VcsRevision> getCommitList();

  /**
   * todo.
   * 
   * <p>Access the working directory. This is the directory, where the checked out files are stored.
   * 
   * @return An object representing the working directory
   *
   */
  public final File getWorkDirectory() {
    return workDirectory.getAbsoluteFile();
  }

  /**
   * todo.
   * 
   * <p>Determines whether this repository is closed.
   *
   * @return true, if is closed
   */
  public final boolean isClosed() {
    return closed;
  }

  /**
   * todo.
   * 
   * <p>Search for a branch with the specified name.
   * 
   * @param branchName The name of the branch
   *
   * @return Revision object representing the branch
   * @throws NoSuchBranchException Thrown if no branch with the given name is found
   * @throws RepositoryClosedException Thrown if the repository has already been closed
   */
  public abstract VcsRevision searchBranch(final String branchName);

  /**
   * todo.
   * 
   * <p>Search for a commit specified by a given name
   * 
   * @param commitName A name representing a commit
   *
   * @return Revision object representing this commit
   * @throws NoSuchCommitException Thrown if there is no commit with the given name
   * @throws RepositoryClosedException Thrown if the repository has already been closed
   */
  public abstract VcsRevision searchCommit(final String commitName);

  /**
   * todo.
   * 
   * <p>Search for a tag with the specified name.
   * 
   * @param tagName The name of the tag
   *
   * @return Revision object representing the tag
   * @throws NoSuchTagException Thrown if no tag with the given name is found
   * @throws RepositoryClosedException Thrown if the repository has already been closed
   */
  public abstract VcsRevision searchTag(final String tagName);

  /**
   * todo.
   * 
   * <p>Retrieves the parent of the specified revision object.
   *  The parent is the revision the specified
   * revision is directly based on.
   * 
   * @param revision The revision object, whose parent should be returned
   *
   * @return The revision object describing the parent revision, or null, if no such revision
   *         exists.
   * @throws IllegalArgumentException Thrown if the specified revision is invalid
   */
  public abstract List<VcsRevision> getParents(final VcsRevision revision);

  /**
   * todo.
   * 
   * <p>Converts an absolute file name to a file name relative to the working directory of this
   * repository.
   * 
   * @param file The file whose relative file name should be returned
   *
   * @return relative file name of the specified file, or null, if the file is not in the work
   *         directory.
   */
  public abstract File getRelativeFile(final File file);

  /**
   * Gets the diff.
   *
   * @param commit1 the commit1
   * @param commit2 the commit2
   * @param filename the filename
   * @return the diff
   */
  public String getDiff(String commit1, String commit2, String filename) {
    return getDiff(commit1, commit2, filename, 0);
  }

  /**
   * Gets the diff.
   *
   * @param commit1 the commit1
   * @param commit2 the commit2
   * @param filename the filename
   * @param contextLines the context lines
   * @return the diff
   */
  public abstract String getDiff(String commit1, String commit2, String filename, int contextLines);

  /**
   * Gets the changed files.
   *
   * @param commit1 the commit1
   * @param commit2 the commit2
   * @return the changed files
   */
  public abstract List<String> getChangedFiles(String commit1, String commit2);

  public abstract List<String> getallFiles_cmdline(String oldestId);

  public abstract Date getCommitDate(String commitId1);
}
