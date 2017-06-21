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

package de.fau.cs.inf2.cas.common.util;

import de.fau.cs.inf2.cas.common.vcs.base.InvalidWorkDirectoryException;
import de.fau.cs.inf2.cas.common.vcs.base.NoSuchRepositoryException;
import de.fau.cs.inf2.cas.common.vcs.base.VcsRepository;
import de.fau.cs.inf2.cas.common.vcs.git.GitLink;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

public class FileUtils {
  /**
   * Check and create directory.
   *
   * @param dir the dir
   */
  public static void checkAndCreateDirectory(final File dir) {
    if (dir.exists()) {
      if (!dir.isDirectory()) {
        System.err.println(String.format("Error: \"%s\" is not a directory!", dir.toString()));
        System.exit(-1);
      }
    } else if (!dir.mkdirs()) {
      System.err.println(String.format("Error: Cannot create directory \"%s\"!", dir.toString()));
    }
  }
  
  /**
   * Clone repository.
   *
   * @param repoUri the repo uri
   * @param workDir the work dir
   * @return the vcs repository
   */
  public static VcsRepository cloneRepository(final String repoUri, final File workDir) {

    try {
      return GitLink.cloneRepository(workDir, repoUri);
    } catch (NoSuchRepositoryException e) {
      System.err.println("Error: " + e.getMessage());
      System.exit(-1);
    } catch (InvalidWorkDirectoryException e) {
      System.err.println("Internal Error:");
      e.printStackTrace();
      System.exit(-1);
    }
    return null;
  }
  
  /**
   * Open repository.
   *
   * @param workDir the work dir
   * @return the vcs repository
   */
  public static VcsRepository openRepository(final File workDir) {
    try {
      return GitLink.openRepository(workDir);
    } catch (NoSuchRepositoryException e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
      System.exit(-1);
    }

    return null;
  }
  
  /**
   * Write exception.
   *
   * @param filenamePrefix the filename prefix
   * @param throwable the t
   * @param args the args
   * @return the string
   */
  public static String writeException(String filenamePrefix, Throwable throwable,
       String... args) {
    StringBuilder sb = new StringBuilder();
    for (String a : args) {
      sb.append(a + "\n");
    }
    sb.append("\n");
    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    throwable.printStackTrace(pw);
    sb.append(sw.toString());
    sb.append("\n\n");

    int num = 0;
    String filename;
    do {
      filename = filenamePrefix + "_" + System.currentTimeMillis() + "_" + num + ".txt";
    }
    while (new File(filename).exists());

    FileUtils.append(filename, sb.toString());
    return filename;
  }
  
  /**
   * Append.
   *
   * @param filename the filename
   * @param text the text
   */
  public static void append(String filename, String text) {
    BufferedWriter bw = null;
    try {
      bw = new BufferedWriter(new FileWriter(filename, true));
      bw.write(text);
      bw.newLine();
      bw.flush();
    } catch (IOException ioe) {
      ioe.printStackTrace();
    } finally {
      if (bw != null) {
        try {
          bw.close();
        } catch (IOException ioe2) {
          // do nothing
        }
      }
    }
  }
  
  /**
   * Gets the file contents.
   *
   * @param file the file
   * @return the file contents
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static byte[] getFileContents(final File file) throws IOException {
    final InputStream inStream = new BufferedInputStream(new FileInputStream(file));

    final ByteArrayOutputStream outStream = new ByteArrayOutputStream();

    int rvalue = 0;
    byte[] transfer = new byte[8192];
    while ((rvalue = inStream.read(transfer)) > 0) {
      outStream.write(transfer, 0, rvalue);
    }
    inStream.close();
    return outStream.toByteArray();
  }
  
  /**
   * Gets the file data.
   *
   * @param path the path
   * @return the file data
   */
  public static String getFileData(String path) {
    byte[] buffer = null;
    BufferedInputStream istream = null;
    File gb = new File(path);
    if (!gb.exists()) {
      try {
        gb.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    try {
      buffer = new byte[(int) (new File(path).length())];

      istream = new BufferedInputStream(new FileInputStream(path));
      istream.read(buffer);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (istream != null) {
        try {
          istream.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    if (buffer == null) {
      return null;
    }
    return new String(buffer);
  }
  
  /**
   * Write string to file.
   *
   * @param file the file
   * @param string the string
   */
  public static void writeStringToFile(File file, String string) {
    try {
      Writer output = new BufferedWriter(new FileWriter(file));
      try {
        output.write(string);
      } catch (Exception e) {
        System.err.println("filename: " + file.getAbsolutePath());
        e.printStackTrace();
      } finally {
        output.close();
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
