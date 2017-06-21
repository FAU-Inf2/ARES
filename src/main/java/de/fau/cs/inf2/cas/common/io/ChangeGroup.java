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

package de.fau.cs.inf2.cas.common.io;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChangeGroup {
  private final HashSet<String> members;

  /**
   * Instantiates a new im group.
   */
  public ChangeGroup() {
    members = new HashSet<String>();
  }

  /**
   * Instantiates a new im group.
   *
   * @param members the members
   */
  public ChangeGroup(HashSet<String> members) {
    this.members = members;
  }

  /**
   * Gets the members.
   *
   * @return the members
   */
  public Set<String> getMembers() {
    return members;
  }

  private static String getGroupHash(AbstractCollection<String> collectionMembers) {
    StringBuilder builder = new StringBuilder();
    boolean first = true;
    ArrayList<String> list = new ArrayList<String>(collectionMembers);
    Collections.sort(list);
    for (String member : list) {
      if (first) {
        first = false;
      } else {
        builder.append("_");
      }
      builder.append(member);
    }
    byte[] bytesOfMessage;
    StringBuffer sb = new StringBuffer();
    try {
      bytesOfMessage = builder.toString().getBytes("UTF-8");
      MessageDigest md = MessageDigest.getInstance("MD5");
      byte[] thedigest = md.digest(bytesOfMessage);

      for (int i = 0; i < thedigest.length; ++i) {
        sb.append(Integer.toHexString((thedigest[i] & 0xFF) | 0x100).substring(1, 3));
      }

    } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
      e.printStackTrace();
    }
    return sb.toString();
  }

  /**
   * Gets the hash.
   *
   * @return the hash
   */
  public String getHash() {
    return getGroupHash(members);
  }

  /**
   * Adds the member.
   *
   * @param pair the pair
   */
  public synchronized void addMember(String pair) {
    members.add(pair);
  }

  /**
   * Adds the members.
   *
   * @param members the members
   */
  synchronized void addMembers(HashSet<String> members) {
    this.members.addAll(members);
  }

}
