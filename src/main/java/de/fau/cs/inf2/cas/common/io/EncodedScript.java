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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Arrays;

/**
 * Holds the encoded patterns for one file and one commit pair.
 */
public class EncodedScript {

  private final CommitPairIdentifier pair;
  private final short[] pattern;
  public EditOperationCounts operations = null;
  
  public final int treeSize;

  /**
   * Instantiates a new encoded script.
   *
   * @param pair the pair
   * @param pattern the pattern
   * @param operations the operations
   * @param patternLength the pattern length
   * @param treeSize the tree size
   */
  public EncodedScript(CommitPairIdentifier pair, short[] pattern, EditOperationCounts operations,
      int patternLength, int treeSize) {
    this.pair = pair;
    this.pattern = pattern;
    this.operations = operations;
    this.treeSize = treeSize;
  }

  EncodedScript(CommitPairIdentifier pair, short[] pattern, EditOperationCounts operations,
      int treeSize) {
    this.pair = pair;
    this.pattern = pattern;
    this.operations = operations;
    this.treeSize = treeSize;
  }

  /**
   * Instantiates a new encoded script.
   *
   * @param pair the pair
   * @param pattern the pattern
   * @param treeSize the tree size
   */
  public EncodedScript(CommitPairIdentifier pair, short[] pattern, int treeSize) {
    this.pair = pair;
    this.pattern = pattern;
    this.treeSize = treeSize;
  }

  /**
   * Gets the pair.
   *
   * @return the pair
   */
  public CommitPairIdentifier getPair() {
    return pair;
  }

  /**
   * Gets the pattern.
   *
   * @return the pattern
   */
  public short[] getPattern() {
    return pattern;
  }

  @SuppressWarnings("unused")
  private static String getString(byte[] input) throws IOException {
    ByteArrayInputStream bais = new ByteArrayInputStream(input);
    DataInputStream dis = new DataInputStream(bais);
    int len = dis.readInt();
    StringBuilder sb = new StringBuilder(len);
    for (int i = 0; i < len; i++) {
      int vvalue = dis.readInt();
      sb.append((char) vvalue);
    }
    return sb.toString();
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    return CommitPairIdentifier.getIdHash(this.getPair()) + "\n" + Arrays.toString(pattern) + "\n";
  }
}
