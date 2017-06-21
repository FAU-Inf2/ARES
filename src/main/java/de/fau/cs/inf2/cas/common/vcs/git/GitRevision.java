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

import de.fau.cs.inf2.cas.common.vcs.base.VcsRevision;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.revwalk.RevWalk;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class GitRevision extends VcsRevision {
  private static final Pattern BRANCH_PATTERN = Pattern.compile("/([^/]*)$");

  private final GitRevisionType revType;
  private final ObjectId id;
  private final int time;

  protected GitRevision(final GitRepository repository, final String revName, final ObjectId id,
      final GitRevisionType revType, final int time) {
    super(repository, revName);
    this.revType = revType;
    this.id = id;
    this.time = time;
  }

  
  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    switch (revType) {
      case GIT_REVISION_UNKNOWN:
      case GIT_REVISION_FROM_COMMIT:
        return getRawName();
      case GIT_REVISION_FROM_BRANCH:
        final Matcher matcher = BRANCH_PATTERN.matcher(getRawName());

        if (matcher.find()) {
          return matcher.group(1);
        } else {
          return getRawName();
        }
      case GIT_REVISION_FROM_TAG:
        return getRawName();
      default:
        return null;
    }
  }

  
  /**
   * Gets the message.
   *
   * @return the message
   */
  public String getMessage() {
    try {
      final RevWalk rw = ((GitRepository) getRepository()).getRevWalk();

      return rw.parseCommit(id).getFullMessage();
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * todo.
   *
   *<p>Package local method to access the ObjectId of this commit.
   */
  ObjectId getId() {
    return id;
  }

  /**
   * Gets the time.
   *
   * @return the time
   */
  public int getTime() {
    return time;
  }

}
