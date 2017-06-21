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

package de.fau.cs.inf2.cas.common.bast.nodes;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.visitors.IBastVisitor;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;



/**
 * todo.
 *
 * <p>The program node, root of the file
 * 

 */
public class BastProgram extends AbstractBastNode {

  public static final int TAG = TagConstants.BAST_PROGRAM;
  public LinkedList<AbstractBastExternalDecl> functionBlocks = null;
  public LinkedList<AbstractBastComment> comments = null;
  public BastPackage packageName = null;
  public LinkedList<BastImportDeclaration> imports = null;
  public LinkedList<BastAnnotation> annotations = null;


  /**
   * todo.
   *
   *
   * @param functionBlocks functions or classes
   * @param comments todo
   */
  public BastProgram(TokenAndHistory[] tokens,
      LinkedList<AbstractBastExternalDecl> functionBlocks, LinkedList<AbstractBastComment> comments,
      BastPackage packageName, LinkedList<BastImportDeclaration> imports,
      LinkedList<BastAnnotation> annotations) {
    super(tokens);

    this.functionBlocks = functionBlocks;
    fieldMap.put(BastFieldConstants.PROGRAM_FUNCTION_BLOCKS, new BastField(functionBlocks));

    this.comments = comments;
    fieldMap.put(BastFieldConstants.PROGRAM_COMMENTS, new BastField(comments));

    this.packageName = packageName;
    fieldMap.put(BastFieldConstants.PROGRAM_PACKAGE, new BastField(packageName));

    this.imports = imports;
    fieldMap.put(BastFieldConstants.PROGRAM_IMPORTS, new BastField(imports));

    this.annotations = annotations;
    fieldMap.put(BastFieldConstants.PROGRAM_ANNOTATIONS, new BastField(annotations));

  }


  /**
   * todo.
   *
   * @param functionBlocks functions or classes
   * @param comments todo
   */
  public BastProgram(TokenAndHistory[] tokens,
      LinkedList<AbstractBastExternalDecl> functionBlocks, LinkedList<BastDeclaration> symtab,
      LinkedList<AbstractBastComment> comments) {
    super(tokens);

    this.functionBlocks = functionBlocks;
    fieldMap.put(BastFieldConstants.PROGRAM_FUNCTION_BLOCKS, new BastField(functionBlocks));

    this.comments = comments;
    fieldMap.put(BastFieldConstants.PROGRAM_COMMENTS, new BastField(comments));


  }

  
  /**
   * Accept.
   *
   * @param visitor the visitor
   */
  public void accept(IBastVisitor visitor) {
    visitor.visit(this);
  }

  
  /**
   * Gets the tag.
   *
   * @return the tag
   */
  @Override
  public int getTag() {
    return TAG;
  }

  
  /**
   * Replace field.
   *
   * @param field the field
   * @param fieldValue the field value
   */
  @SuppressWarnings("unchecked")
  public void replaceField(BastFieldConstants field, BastField fieldValue) {
    fieldMap.put(field, fieldValue);
    switch (field) {
      case PROGRAM_FUNCTION_BLOCKS:
        this.functionBlocks = (LinkedList<AbstractBastExternalDecl>) fieldValue.getListField();
        break;
      case PROGRAM_COMMENTS:
        this.comments = (LinkedList<AbstractBastComment>) fieldValue.getListField();
        break;
      case PROGRAM_PACKAGE:
        this.packageName = (BastPackage) fieldValue.getField();
        break;
      case PROGRAM_IMPORTS:
        this.imports = (LinkedList<BastImportDeclaration>) fieldValue.getListField();
        break;
      case PROGRAM_ANNOTATIONS:
        this.annotations = (LinkedList<BastAnnotation>) fieldValue.getListField();
        break;
      default:
        assert (false);
    }
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    assert (false);
    return null;
  }

}
