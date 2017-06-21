package de.fau.cs.inf2.cas.common.parser.odin;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileData {
  final byte[] data;

  /**
   * Instantiates a new file data.
   *
   * @param data the data
   */
  public FileData(byte[] data) {
    this.data = data;
    try {
      MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
      digest.update(data);
    } catch (NoSuchAlgorithmException e) {
      throw new RuntimeException(e);
    }
  }

  FileData(File file) {
    try {
      FileInputStream in = new FileInputStream(file);
      int length = (int) file.length();

      if (length > Integer.MAX_VALUE) {
        assert (false);
      }

      data = new byte[length];

      int readData = in.read(data, 0, length);

      if (readData < data.length) {
        in.close();
        throw new IOException("Could not completely read file " + file.getName());
      }

      in.close();
      MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
      digest.update(data);
    } catch (NoSuchAlgorithmException ex) {
      throw new RuntimeException(ex);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
