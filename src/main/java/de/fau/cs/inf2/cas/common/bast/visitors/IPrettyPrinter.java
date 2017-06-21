package de.fau.cs.inf2.cas.common.bast.visitors;

import java.io.File;

/**
 * Interface for all the pretty printer visitors.
 * 

 */
public interface IPrettyPrinter extends IBastVisitor {

  /**
   * todo.
   * Write visited Ast to file
   * 
   * @param file the file
   */
  public void print(File file);

  /**
   * todo.
   * Write visited Ast to StringBuffer
   * 
   * @return the buffer
   */
  public StringBuffer getBuffer();
}
