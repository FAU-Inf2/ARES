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

package de.fau.cs.inf2.cas.common.vcs.git;

import de.fau.cs.inf2.cas.common.vcs.base.InvalidWorkDirectoryException;
import de.fau.cs.inf2.cas.common.vcs.base.NoSuchRepositoryException;
import de.fau.cs.inf2.cas.common.vcs.base.VcsRepository;

import org.eclipse.jgit.api.CloneCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.JGitInternalException;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class providing access to a Git repository.
 * 
 * <p>@author Marius Kamp
 */
public final class GitLink {
  /**
   * Clone repository.
   *
   * @param workDir the work dir
   * @param repoUri the repo uri
   * @return the vcs repository
   * @throws InvalidWorkDirectoryException the invalid work directory exception
   * @throws NoSuchRepositoryException the no such repository exception
   */
  public static VcsRepository cloneRepository(File workDir, String repoUri)
      throws InvalidWorkDirectoryException, NoSuchRepositoryException {
    if (!validateWorkDirectory(workDir)) {
      throw new InvalidWorkDirectoryException(workDir);
    }

    try {
      CloneCommand cloneCmd = Git.cloneRepository();
      cloneCmd.setURI(repoUri);
      cloneCmd.setDirectory(workDir.getAbsoluteFile());
      cloneCmd.setCloneAllBranches(false);
      cloneCmd.setCloneSubmodules(false);

      Git git = null;
      try {
        git = cloneCmd.call();
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(-1);
      }

      return new GitRepository(workDir, git.getRepository());
    } catch (JGitInternalException e) {
      throw new NoSuchRepositoryException(repoUri);
    }
  }

  /**
   * Clone repository.
   *
   * @param workDir the work dir
   * @param repoUri the repo uri
   * @return the vcs repository
   * @throws InvalidWorkDirectoryException the invalid work directory exception
   * @throws NoSuchRepositoryException the no such repository exception
   */
  public static VcsRepository cloneRepository(String workDir, String repoUri)
      throws InvalidWorkDirectoryException, NoSuchRepositoryException {
    return cloneRepository(new File(workDir), repoUri);
  }

  /**
   * Creates the repository string based on the threadId and the repository url.
   *
   * @param workDir the work dir
   * @param threadId the thread id
   * @param url the url
   * @return the repository path
   * @throws UnsupportedEncodingException the unsupported encoding exception
   * @throws NoSuchAlgorithmException the no such algorithm exception
   */
  private static File getRepositoryPath(final File workDir, int threadId, String url)
      throws UnsupportedEncodingException, NoSuchAlgorithmException {
    byte[] bytesOfMessage = url.getBytes("UTF-8");
    MessageDigest md = MessageDigest.getInstance("MD5");
    byte[] thedigest = md.digest(bytesOfMessage);
    StringBuffer buffer = new StringBuffer();
    for (int i = 0; i < thedigest.length; ++i) {
      buffer.append(Integer.toHexString((thedigest[i] & 0xFF) | 0x100).substring(1, 3));
    }
    File path = new File(workDir + "/" + threadId + "/" + buffer.toString());
    return path;
  }
  
  /**
   * Open repository.
   *
   * @param workDir the work dir
   * @return the vcs repository
   * @throws NoSuchRepositoryException the no such repository exception
   */
  public static VcsRepository openRepository(final File workDir) throws NoSuchRepositoryException {
    try {
      final Git git = Git.open(workDir);

      return new GitRepository(workDir, git.getRepository());
    } catch (Exception e) {
      throw new NoSuchRepositoryException(workDir.toURI().toString());
    }
  }
  
  /**
   * Open repository for different threads and urls.
   *
   * @param workDir the work dir
   * @param threadId the thread id
   * @param url the url
   * @return the vcs repository
   * @throws NoSuchRepositoryException the no such repository exception
   */
  public static VcsRepository openRepository(
      final File workDir, int threadId, String url) throws NoSuchRepositoryException {
    try {
      File path = getRepositoryPath(workDir, threadId, url);
      if (!path.exists()) {
        path.mkdirs();
        cloneRepository(path, url);
      }
      
      final Git git = Git.open(path);

      return new GitRepository(path, git.getRepository());
    } catch (Exception e) {
      throw new NoSuchRepositoryException(workDir.toURI().toString());
    }
  }

  private static boolean validateWorkDirectory(final File workDir) {
    if (workDir.exists()) {
      if (workDir.isDirectory()) {
        File[] files = workDir.listFiles();
        if (files == null) {
          return false;
        }
        return (files.length == 0) && workDir.canRead() && workDir.canWrite();
      }
    } else {
      File parent = workDir.getAbsoluteFile().getParentFile();

      if (parent == null) {
        return true;
      }

      return parent.exists() && parent.isDirectory() && parent.canRead() && parent.canWrite();
    }

    return false;
  }

  private GitLink() {}
}
