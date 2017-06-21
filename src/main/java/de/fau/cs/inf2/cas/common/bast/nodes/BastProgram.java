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
