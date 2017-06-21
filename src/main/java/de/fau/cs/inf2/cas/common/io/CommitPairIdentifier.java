package de.fau.cs.inf2.cas.common.io;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Represents the changes in one file from one commit to another.
 */
public class CommitPairIdentifier {
  public final String id;
  public int idx;

  public String repository;
  public String commitId1;
  public String commitId2;

  public String fileName;
  public int methodNumber1 = -1;
  public int methodNumber2 = -1;

  
  /**
   * Instantiates a new commit pair identifier.
   *
   * @param idx the idx
   * @param repository the repository
   * @param commitId1 the commit id1
   * @param commitId2 the commit id2
   * @param fileName the file name
   * @param methodNumber1 the method number1
   * @param methodNumber2 the method number2
   */
  public CommitPairIdentifier(int idx, String repository, String commitId1, String commitId2,
      String fileName, int methodNumber1, int methodNumber2) {
    this.id = getIdHash(repository, commitId1, commitId2, fileName, methodNumber1, methodNumber2);
    this.idx = idx;

    this.repository = repository;
    this.commitId1 = commitId1;
    this.commitId2 = commitId2;
    this.fileName = fileName;

    this.methodNumber1 = methodNumber1;
    this.methodNumber2 = methodNumber2;
  }

  
  /**
   * Instantiates a new commit pair identifier.
   *
   * @param id the id
   * @param idx the idx
   * @param repository the repository
   * @param commitId1 the commit id1
   * @param commitId2 the commit id2
   * @param fileName the file name
   * @param methodNumber1 the method number1
   * @param methodNumber2 the method number2
   */
  public CommitPairIdentifier(String id, int idx, String repository, String commitId1,
      String commitId2, String fileName, int methodNumber1, int methodNumber2) {
    assert (id.equals(
        getIdHash(repository, commitId1, commitId2, fileName, methodNumber1, methodNumber2)));
    this.id = id;
    this.idx = idx;

    this.repository = repository;
    this.commitId1 = commitId1;
    this.commitId2 = commitId2;
    this.fileName = fileName;

    this.methodNumber1 = methodNumber1;
    this.methodNumber2 = methodNumber2;
  }

  /**
   * Gets the id hash.
   *
   * @param identifier the identifier
   * @return the id hash
   */
  static String getIdHash(CommitPairIdentifier identifier) {
    return getIdHash(identifier.repository, identifier.commitId1, identifier.commitId2,
        identifier.fileName, identifier.methodNumber1, identifier.methodNumber2);
  }

  /**
   * Gets the id hash.
   *
   * @param repository the repository
   * @param commitId1 the commit id1
   * @param commitId2 the commit id2
   * @param fileName the file name
   * @param methodNumber1 the method number1
   * @param methodNumber2 the method number2
   * @return the id hash
   */
  public static final String getIdHash(final String repository, final String commitId1,
      final String commitId2, final String fileName, final int methodNumber1,
      final int methodNumber2) {
    final StringBuilder builder = new StringBuilder();
    builder.append(repository);
    builder.append("_");
    builder.append(commitId1);
    builder.append("_");
    builder.append(commitId2);
    builder.append("_");
    builder.append(fileName);
    builder.append("_");
    builder.append(methodNumber1);
    builder.append("_");
    builder.append(methodNumber2);

    final byte[] bytesOfMessage;
    final StringBuffer sb = new StringBuffer();

    try {
      bytesOfMessage = builder.toString().getBytes("UTF-8");
      MessageDigest md = MessageDigest.getInstance("MD5");
      final byte[] thedigest = md.digest(bytesOfMessage);

      for (int i = 0; i < thedigest.length; ++i) {
        sb.append(Integer.toHexString((thedigest[i] & 0xFF) | 0x100).substring(1, 3));
      }

    } catch (UnsupportedEncodingException | NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return sb.toString();
  }


  /**
   * Gets the short name.
   *
   * @return the short name
   */
  public final String getShortName() {
    String name = commitId1.substring(0, 6) + " - " + commitId2.substring(0, 6) + " - " + fileName;
    return name;
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return String.format("{Id: %s, index: %d)", this.id, this.idx);
  }

  /**
   * Compare to.
   *
   * @param other the other
   * @return the int
   */
  public int compareTo(CommitPairIdentifier other) {
    if (other == null) {
      return 1;
    }

    return this.id.compareTo(other.id);
  }

  
  /**
   * Hash code.
   *
   * @return the int
   */
  @Override
  public int hashCode() {
    return this.id.hashCode();
  }

  
  /**
   * Equals.
   *
   * @param other the other
   * @return true, if successful
   */
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof CommitPairIdentifier)) {
      return false;
    }

    CommitPairIdentifier otherCommitPair = (CommitPairIdentifier) other;

    return this.id.equals(otherCommitPair.id);
  }

  /**
   * Gets the stripped file name.
   *
   * @return the stripped file name
   */
  public final String getStrippedFileName() {
    final int indexTilde = this.fileName.indexOf("~");

    if (indexTilde >= 0) {
      return this.fileName.substring(0, indexTilde);
    } else {
      return this.fileName;
    }
  }

  /**
   * Gets the file string.
   *
   * @return the file string
   */
  public String getFileString() {
    return createFileString(repository, commitId1, commitId2, fileName.split("~~")[0]);
  }

  static String createFileString(String repository, String commit1, String commit2,
      String filename) {
    return repository + "_" + commit1 + "_" + commit2 + "_" + filename;
  }

}
