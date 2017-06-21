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
import java.util.List;
import java.util.ListIterator;

/**
 * A class representing a state of the repository at a specific time.
 *
 * @author Marius Kamp
 */
public class VcsState {
  private final VcsRevision revision;
  private final List<File> fileList;
  private final File workDir;

  /**
   * todo.
   * 
   * <p>Creates a new VcsState object.
   * 
   * @param revision The associated revision object
   *
   * @param workDir The work directory of the associated repository
   * @param fileList A list of file names associated with the specified revision. All file names
   *        should be absolute
   */
  public VcsState(final VcsRevision revision, final File workDir, final List<File> fileList) {

    this.revision = revision;
    this.workDir = workDir;
    this.fileList = fileList;
  }

  /**
   * todo.
   * 
   * <p>Returns a reference to the revision object which corresponds to this state.
   *
   * @return the revision
   */
  public VcsRevision getRevision() {
    return revision;
  }

  /**
   * todo.
   *
   *<p>Returns a list of all associated files.
   *
   * @param absolutePaths Determines whether the returned File Objects should contain absolute
   *        paths. If set to false, the paths are relative to the work directory.
   */
  private List<File> getFileList(final boolean absolutePaths) {
    if (absolutePaths) {
      return fileList;
    } else {
      final ArrayList<File> relList = new ArrayList<File>(fileList);
      final ListIterator<File> it = relList.listIterator();

      while (it.hasNext()) {
        final File f = it.next();
        it.set(getRelativeFile(f, workDir));
      }

      return relList;
    }
  }
  
  /**
   * todo.
   * 
   * <p>Returns a list of all associated files.
   *
   * @return the file list
   */
  public List<File> getFileList() {
    return getFileList(true);
  }

  /**
   * todo.
   * 
   * <p>Returns a File which is relative to a given File.
   * 
   * @param file The File which relative path is to be created
   *
   * @param ref The reference File for the relative path
   * @return Relative path of file
   */
  public static File getRelativeFile(final File file, final File ref) {
    return ref.toPath().relativize(file.toPath()).toFile();
  }
  


  /**
   * todo.
   * 
   * <p>Returns the associated work directory.
   *
   * @return the work directory
   */
  public File getWorkDirectory() {
    return workDir;
  }

  /**
   * todo.
   * 
   * <p>Returns a list of files which have been changed between
   *  two revisions. Files which only appear
   * in either class are not included.
   * 
   * @param otherState Another VcsState object representing another revision
   *
   * @return A list of files which have been changed
   */
  public List<File> getChangedFiles(final VcsState otherState) {

    final List<File> changedFiles = new ArrayList<>();
    final List<String> changedPaths = this.revision.getRepository()
        .getChangedFiles(this.revision.getName(), otherState.revision.getName());
    for (String changedPath : changedPaths) {
      changedFiles.add(new File(changedPath));
    }

    /*
     * final List<File> relFiles = getFileList(false); final List<File> otherFiles =
     * otherState.getFileList(false);
     * 
     * final List<File> resultList = ListUtils.intersect(relFiles, otherFiles); final
     * ListIterator<File> it = resultList.listIterator();
     * 
     * while (it.hasNext()) { final String fileName = it.next().toString(); final File f1 = new
     * File(workDir, fileName); final File f2 = new File(otherState.getWorkDirectory(), fileName);
     * 
     * if (!FileUtils.filesDiffer(f1, f2)) { it.remove(); } }
     * 
     * assert (resultList.size() == changedFiles.size()); for(File file : resultList) {
     * assert (changedFiles.contains(file)); }
     */

    return changedFiles;
  }

  /**
   * todo.
   * 
   * <p>Returns a list of files which have been added between
   *  two revisions. The returned list contains
   * all files which exist in this state but not in the specified one.
   * 
   * @param otherState Another VcsState object representing the earlier revision.
   *
   * @return A list of files which have been added between otherState and this state.
   */
  public List<File> getAddedFiles(final VcsState otherState) {
    final List<File> relFiles = getFileList(false);
    final List<File> otherFiles = otherState.getFileList(false);

    relFiles.removeAll(otherFiles);

    return relFiles;
  }
}
