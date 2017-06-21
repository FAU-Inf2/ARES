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

import de.fau.cs.inf2.cas.common.util.FileUtils;

import de.fau.cs.inf2.cas.common.vcs.base.NoSuchBranchException;
import de.fau.cs.inf2.cas.common.vcs.base.NoSuchCommitException;
import de.fau.cs.inf2.cas.common.vcs.base.NoSuchTagException;
import de.fau.cs.inf2.cas.common.vcs.base.RepositoryClosedException;
import de.fau.cs.inf2.cas.common.vcs.base.VcsRepository;
import de.fau.cs.inf2.cas.common.vcs.base.VcsRevision;
import de.fau.cs.inf2.cas.common.vcs.base.VcsState;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.DiffCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.CheckoutConflictException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.AbstractTreeIterator;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Provides access to a Git repository.
 *
 * @author Marius Kamp
 */
@SuppressWarnings({"checkstyle:nofinalizer"})
public final class GitRepository extends VcsRepository {
  private final Repository repository;
  private final RevWalk revWalk;

  protected GitRepository(final File workDir, final Repository repository) {
    super(workDir);
    this.repository = repository;
    this.revWalk = new RevWalk(repository);
    Git git = new Git(repository);
    ListBranchCommand listBranchCmd = git.branchList();
    git.close();
    listBranchCmd.setListMode(ListBranchCommand.ListMode.ALL);
    List<Ref> refList = null;
    try {
      refList = listBranchCmd.call();
    } catch (GitAPIException e) {
      e.printStackTrace();
      FileUtils.writeException("gitapi", e, workDir.toString());
      System.exit(-1);
    }

    for (final Ref r : refList) {
      branchList.add(new GitRevision(this, r.getName(), r.getObjectId(),
          GitRevisionType.GIT_REVISION_FROM_BRANCH, -1));
    }
  }

  /**
   * todo.
   *
   *<p>Runs command-line git.
   * 
   * @param args The arguments to start git with.
   * @return The started process
   * @throws IOException todo
   */
  private Process cmdLineGit(String... args) throws IOException {
    String gitExecutable;

    String osName = System.getProperty("os.name");
    if (osName.contains("Windows")) {
      gitExecutable = "C:\\Program Files (x86)\\Git\\bin\\git.exe";
    } else {
      gitExecutable = "git";
    }

    String[] allArgs = new String[args.length + 1];
    allArgs[0] = gitExecutable;
    for (int i = 0; i < args.length; i++) {
      {
        allArgs[i + 1] = args[i];
      }
    }

    ProcessBuilder pb = new ProcessBuilder(allArgs);
    pb.directory(this.workDirectory.getAbsoluteFile());
    Process process = pb.start();
    try {
      process.waitFor(1, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return process;
  }

  /**
   * todo.
   * 
   * <p>Returns the contents of a file in a revision.
   * 
   * @param rev The revision to search in.
   * @param filename The relative name of the file.
   * @return Contents of the given file.
   */
  public byte[] getFileContents(VcsRevision rev, String filename) {
    String prm = rev.getName() + ":" + filename.replace('\\', '/');
    try {
      Process process = cmdLineGit("--no-pager","show", prm);
      InputStream is = process.getInputStream();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      int characters;
      while ((characters = is.read()) >= 0) {
        baos.write(characters);
      }
      return baos.toByteArray();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
      return null;
    }
  }

  /**
   * Extract to directory.
   *
   * @param path the path
   */
  public void extractToDirectory(String commit, String path) {
    try {
      assert (new File(path).exists());
      Process process = cmdLineGit("archive", commit, "|", "(" + path + " && tar -x)");
      InputStream is = process.getInputStream();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      int characters;
      while ((characters = is.read()) >= 0) {
        baos.write(characters);
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
    }
  }
  
  /**
   * Gets the commit msg.
   *
   * @param commit the commit
   * @return the commit msg
   */
  public String getCommitMsg(String commit) {
    try {
      Process process = cmdLineGit("log", "--format=%B", "-n 1", commit);
      InputStream is = process.getInputStream();
      BufferedReader rreader = new BufferedReader(new InputStreamReader(is));
      StringBuffer text = new StringBuffer();
      String line;
      while ((line = rreader.readLine()) != null) {
        text.append(line);
      }
      return text.toString();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
      return null;
    }
  }

  /**
   * Gets the author name.
   *
   * @param commit the commit
   * @return the author name
   */
  public String getAuthorName(String commit) {
    try {
      Process process = cmdLineGit("log", "--format=%aN", "-n 1", commit);
      InputStream is = process.getInputStream();
      BufferedReader rreader = new BufferedReader(new InputStreamReader(is));
      StringBuffer text = new StringBuffer();
      String line;
      while ((line = rreader.readLine()) != null) {
        text.append(line);
      }
      return text.toString();
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
      return null;
    }
  }

  /**
   * Gets the commit date.
   *
   * @param commit the commit
   * @return the commit date
   */
  public Date getCommitDate(String commit) {
    try {
      Process process = cmdLineGit("log", "--format=%ci", "-n 1", commit);
      InputStream is = process.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      StringBuffer text = new StringBuffer();
      String line;
      while ((line = reader.readLine()) != null) {
        text.append(line);
      }
      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss Z");
      return dateFormat.parse(text.toString());
    } catch (IOException | ParseException e) {
      e.printStackTrace();
      System.exit(-1);
      return null;
    }
  }

 
  /**
   * Gets the all files_cmdline.
   *
   * @param commit1 the commit1
   * @return the all files_cmdline
   */
  public List<String> getallFiles_cmdline(String commit1) {
    try {
      Process process = cmdLineGit("ls-tree", "--name-only", "-r", commit1);
      InputStream is = process.getInputStream();
      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      List<String> changed = new ArrayList<>();
      String line;
      while ((line = reader.readLine()) != null) {
        changed.add(line);
      }
      return changed;
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(-1);
      return null;
    }
  }

  protected void finalize() throws Throwable {
    this.close();
  }

  private VcsRevision findRevision(final List<VcsRevision> list, final String name) {
    for (final VcsRevision rev : list) {
      if (rev.getName().equals(name)) {
        return rev;
      }
    }

    return null;
  }

  
  /**
   * Check out.
   *
   * @param revision the revision
   * @return the vcs state
   */
  public VcsState checkOut(VcsRevision revision) {
    if (isClosed()) {
      throw new RepositoryClosedException(this);
    }
    Git git = new Git(repository);
    CheckoutCommand checkout = git.checkout();
    git.close();
    checkout.setAllPaths(true);
    checkout.setCreateBranch(false);
    checkout.setStartPoint(revision.getRawName());

    try {
      checkout.call();
    } catch (CheckoutConflictException e) {
      e.printStackTrace();
      FileUtils.writeException("git.checkout", e, revision.getName());
    } catch (Exception e) {
      e.printStackTrace();
      FileUtils.writeException("git.checkout", e, revision.getName());
      return null;
    }
    final List<File> fileList = new ArrayList<File>();
    try {
      final RevTree tree = revWalk.parseCommit(repository.resolve(revision.getRawName())).getTree();

      assert tree != null;

      final TreeWalk walk = new TreeWalk(repository);
      walk.addTree(tree);
      walk.setRecursive(true);

      while (walk.next()) {
        fileList.add(new File(getWorkDirectory(), walk.getPathString()));
      }
      walk.close();
    } catch (final Exception e) {
      e.printStackTrace();
    }

    return new VcsState(revision, getWorkDirectory(), fileList);
  }

  
  /**
   * Close.
   */
  public void close() {
    super.close();
    revWalk.close();
    repository.close();
  }

  
  /**
   * Gets the commit list.
   *
   * @return the commit list
   */
  public final synchronized List<VcsRevision>  getCommitList() {
    final Git git = new Git(repository);
    final LogCommand logCommand = git.log();
    final ArrayList<VcsRevision> result = new ArrayList<>();

    try {
      git.checkout().setName(Constants.MASTER).call();
      for (final RevCommit commit : logCommand.call()) {
        result.add(new GitRevision(this, ObjectId.toString(commit.getId()), commit.getId(),
            GitRevisionType.GIT_REVISION_FROM_COMMIT, commit.getCommitTime()));
      }
      git.close();
    } catch (Exception e) {
      e.printStackTrace();
      FileUtils.writeException("git.getCommitList", e);
      return Collections.emptyList();
    }

    System.out.println("GitRepository.getCommitList -> " + result.size());
    return result;
  }

  
  /**
   * Search branch.
   *
   * @param branchName the branch name
   * @return the vcs revision
   * @throws NoSuchBranchException the no such branch exception
   */
  @SuppressWarnings("ucd")
  public VcsRevision searchBranch(final String branchName) {

    if (isClosed()) {
      throw new RepositoryClosedException(this);
    }

    VcsRevision revision = findRevision(getBranchList(), branchName);

    if (revision == null) {
      throw new NoSuchBranchException(branchName);
    }

    return revision;
  }

  
  /**
   * Search commit.
   *
   * @param commitName the commit name
   * @return the vcs revision
   * @throws NoSuchCommitException the no such commit exception
   */
  public VcsRevision searchCommit(final String commitName) throws NoSuchCommitException {

    if (isClosed()) {
      throw new RepositoryClosedException(this);
    }
    if ((commitName.length() != 40) && (!commitName.matches("^[0-9a-fA-F]+$"))) {
      throw new NoSuchCommitException(commitName);
    }

    final ObjectId revObject;
    try {
      revObject = repository.resolve(commitName);
    } catch (Exception e) {
      throw new NoSuchCommitException(commitName);
    }

    if (revObject == null) {
      throw new NoSuchCommitException(commitName);
    }

    final RevCommit revCommit = revWalk.lookupCommit(revObject);

    return new GitRevision(this, ObjectId.toString(revObject), revObject,
        GitRevisionType.GIT_REVISION_FROM_COMMIT, revCommit.getCommitTime());
  }

  
  /**
   * Search tag.
   *
   * @param tagName the tag name
   * @return the vcs revision
   * @throws NoSuchTagException the no such tag exception
   */
  public VcsRevision searchTag(final String tagName) throws NoSuchTagException {

    if (isClosed()) {
      throw new RepositoryClosedException(this);
    }

    VcsRevision revision = findRevision(getTagList(), tagName);

    if (revision == null) {
      throw new NoSuchTagException(tagName);
    }

    return revision;
  }

  
  /**
   * Gets the parents.
   *
   * @param revision the revision
   * @return the parents
   */
  public List<VcsRevision> getParents(final VcsRevision revision) {
    if (revision == null) {
      throw new NullPointerException("revision must not be null!");
    }

    final RevCommit commit;
    try {
      commit = revWalk.parseCommit(((GitRevision) revision).getId());
    } catch (Exception e) {
      e.printStackTrace();
      FileUtils.writeException("git.getParents", e, revision.getName());
      return null;
    }

    final ArrayList<VcsRevision> result = new ArrayList<>(commit.getParentCount());

    for (final RevCommit parent : commit.getParents()) {
      result.add(new GitRevision(this, ObjectId.toString(parent.getId()), parent.getId(),
          GitRevisionType.GIT_REVISION_FROM_COMMIT, parent.getCommitTime()));
    }

    return result;
  }

  private AbstractTreeIterator getTreeIterator(String name) throws IOException {
    final ObjectId id = this.repository.resolve(name);
    if (id == null) {
      throw new IllegalArgumentException(name);
    }
    final CanonicalTreeParser p = new CanonicalTreeParser();
    final ObjectReader or = this.repository.newObjectReader();
    try {
      RevWalk revWalk = new RevWalk(this.repository);
      p.reset(or, revWalk.parseTree(id));
      revWalk.close();
      return p;
    } finally {
      or.close();
    }
  }

  
  /**
   * Gets the diff.
   *
   * @param c1 the c1
   * @param c2 the c2
   * @param filename the filename
   * @param contextLines the context lines
   * @return the diff
   */
  public String getDiff(String c1, String c2, String filename, int contextLines) {
    try {
      OutputStream out = new ByteArrayOutputStream();
      Git git = new Git(repository);
      DiffCommand cmd =
          git.diff().setOutputStream(out).setContextLines(contextLines)
              .setPathFilter(PathFilter.create(filename.replace('\\', '/')))
              .setOldTree(getTreeIterator(c1)).setNewTree(getTreeIterator(c2));
      cmd.call();
      git.close();
      return out.toString();

    } catch (Exception e) {
      System.err.format("Couldn't get diff from %s to %s.\n", c1, c2);
      e.printStackTrace();
      FileUtils.writeException("git.getDiff", e, c1, c2, filename);
    }
    return null;
  }

  
  /**
   * Gets the changed files.
   *
   * @param commit1 the commit1
   * @param commit2 the commit2
   * @return the changed files
   */
  public List<String> getChangedFiles(String commit1, String commit2) {
    try {
      OutputStream out = new ByteArrayOutputStream();
      Git git = new Git(repository);
      DiffCommand cmd = git.diff().setOutputStream(out).setContextLines(0)
          .setOldTree(getTreeIterator(commit1)).setNewTree(getTreeIterator(commit2));
      git.close();
      List<DiffEntry> entries = cmd.call();
      List<String> files = new ArrayList<>();
      for (DiffEntry de : entries) {
        switch (de.getChangeType()) {
          case MODIFY:
            files.add(de.getNewPath());
            break;
          default:
            break;
        }
      }
      return files;

    } catch (Exception e) {
      System.err.format("Couldn't get changed files from %s to %s.\n", commit1, commit2);
      e.printStackTrace();
      FileUtils.writeException("git.getChangedFiles", e, commit1, commit2);
      return new ArrayList<>();
    }
  }

  
  /**
   * Gets the relative file.
   *
   * @param file the file
   * @return the relative file
   */
  public File getRelativeFile(final File file) {
    final String path = Repository.stripWorkDir(getWorkDirectory(), file);

    if (path.equals("")) {
      return null;
    } else {
      return new File(path);
    }
  }

  /**
   * todo.
   *
   *<p>A package local method to retrieve the RevWalk object associated with this repository.
   */
  RevWalk getRevWalk() {
    return revWalk;
  }
}
