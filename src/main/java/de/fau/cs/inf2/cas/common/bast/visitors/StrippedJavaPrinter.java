package de.fau.cs.inf2.cas.common.bast.visitors;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractAresStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastExternalDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastInternalDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastSpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAccess;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAdditiveExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAnd;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAnnotation;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAnnotationDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAnnotationElemValue;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAnnotationMethod;
import de.fau.cs.inf2.cas.common.bast.nodes.BastArrayDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastArrayRef;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAsgnExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastAssertStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlockComment;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBoolConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBreak;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCall;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCase;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCastExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCatchClause;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCharConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassKeySpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCmp;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondAnd;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastCondOr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastContinue;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDecrExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDefault;
import de.fau.cs.inf2.cas.common.bast.nodes.BastDummyToken;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEmptyDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEmptyStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEnumDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEnumMember;
import de.fau.cs.inf2.cas.common.bast.nodes.BastEnumSpec;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprList;
import de.fau.cs.inf2.cas.common.bast.nodes.BastExprWater;
import de.fau.cs.inf2.cas.common.bast.nodes.BastForStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunctionIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunctionParameterDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastGoto;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIdentDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastImportDeclaration;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIncludeStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIncrExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastInstanceOf;
import de.fau.cs.inf2.cas.common.bast.nodes.BastIntConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastInterfaceDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastLabelStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastLineComment;
import de.fau.cs.inf2.cas.common.bast.nodes.BastListInitializer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastMultiExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNew;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNullConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastOr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastPackage;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameter;
import de.fau.cs.inf2.cas.common.bast.nodes.BastParameterList;
import de.fau.cs.inf2.cas.common.bast.nodes.BastPointer;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.nodes.BastRealConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastRegularDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastReturn;
import de.fau.cs.inf2.cas.common.bast.nodes.BastShift;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStorageClassSpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStringConst;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStructDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStructDeclarator;
import de.fau.cs.inf2.cas.common.bast.nodes.BastStructMember;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSuper;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitch;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSwitchCaseGroup;
import de.fau.cs.inf2.cas.common.bast.nodes.BastSynchronizedBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTemplateSpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastThis;
import de.fau.cs.inf2.cas.common.bast.nodes.BastThrowStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTryStmt;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeArgument;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeParameter;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeQualifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastTypeSpecifier;
import de.fau.cs.inf2.cas.common.bast.nodes.BastUnaryExpr;
import de.fau.cs.inf2.cas.common.bast.nodes.BastWhileStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastXor;
import de.fau.cs.inf2.cas.common.bast.type.BastArrayType;
import de.fau.cs.inf2.cas.common.bast.type.BastBasicType;
import de.fau.cs.inf2.cas.common.bast.type.BastClassType;
import de.fau.cs.inf2.cas.common.bast.type.BastPointerType;
import de.fau.cs.inf2.cas.common.bast.type.BastStructOrUnionSpecifierType;
import de.fau.cs.inf2.cas.common.bast.type.BastType;
import de.fau.cs.inf2.cas.common.bast.type.BastTypeName;

import de.fau.cs.inf2.cas.common.parser.IGeneralToken;
import de.fau.cs.inf2.cas.common.parser.odin.JavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

@SuppressWarnings("ucd")
public class StrippedJavaPrinter implements IPrettyPrinter, IBastVisitor {
  private StringBuffer buffer = new StringBuffer();

  private int currentLine = 0;

  private HashMap<AbstractBastNode, Integer> endLines;
  private HashMap<AbstractBastNode, Integer> startLines;
  private HashMap<AbstractBastNode, Integer> startPos;

  private HashSet<AbstractBastNode> waitingNodes = new HashSet<>();

  /**
   * Instantiates a new stripped java printer.
   */
  public StrippedJavaPrinter() {

  }


  /**
   * Instantiates a new stripped java printer.
   *
   * @param startLines the start lines
   * @param endLines the end lines
   * @param startPos the start pos
   */
  public StrippedJavaPrinter(HashMap<AbstractBastNode, Integer> startLines,
      HashMap<AbstractBastNode, Integer> endLines, HashMap<AbstractBastNode, Integer> startPos) {
    this.startLines = startLines;
    this.endLines = endLines;
    this.startPos = startPos;
  }

  protected void addTokenData(AbstractBastNode node, int field) {
    if (node.info.tokens == null || node.info.tokens.length <= field
        || node.info.tokens[field] == null) {
      return;
    }
    String tmp = null;
    TokenAndHistory tokenAndHistory = node.info.tokens[field];
    if (tokenAndHistory.prevTokens != null) {
      for (IGeneralToken token : tokenAndHistory.prevTokens) {
        if (printToken((JavaToken) token)) {
          tmp = trimString(((JavaToken) token).whiteSpace.toString());
          int countReturn = 0;
          for (int i = 0; i < tmp.length(); i++) {
            if (tmp.charAt(i) == '\n') {
              countReturn++;
            }
          }
          currentLine += countReturn;
          buffer.append(tmp);
          tmp = trimString(((JavaToken) token).data.toString());
          countReturn = 0;
          for (int i = 0; i < tmp.length(); i++) {
            if (tmp.charAt(i) == '\n') {
              countReturn++;
            }
          }
          currentLine += countReturn;
          buffer.append(tmp);
        } else {
          buffer.append(" ");
        }
      }
    }
    if (printToken((JavaToken) tokenAndHistory.token)) {
      tmp = trimString(trimString(((JavaToken) tokenAndHistory.token).whiteSpace.toString()));
      int countReturn = 0;
      for (int i = 0; i < tmp.length(); i++) {
        if (tmp.charAt(i) == '\n') {
          countReturn++;
        }
      }
      currentLine += countReturn;
      buffer.append(tmp);
    } else {
      buffer.append(" ");
    }
    for (AbstractBastNode wnode : waitingNodes) {
      startLines.put(wnode, currentLine);
      startPos.put(wnode, buffer.length());
    }
    waitingNodes.clear();
    if (printToken((JavaToken) tokenAndHistory.token)) {
      int countReturn = 0;
      tmp = trimString(trimString(((JavaToken) tokenAndHistory.token).data.toString()));
      countReturn = 0;
      for (int i = 0; i < tmp.length(); i++) {
        if (tmp.charAt(i) == '\n') {
          countReturn++;
        }
      }
      currentLine += countReturn;
      buffer.append(tmp);
    } else {
      buffer.append(" ");
    }
    if (tokenAndHistory.followingTokens != null) {
      for (IGeneralToken token : tokenAndHistory.followingTokens) {
        if (printToken((JavaToken) token)) {
          tmp = trimString(((JavaToken) token).whiteSpace.toString());
          int countReturn = 0;
          for (int i = 0; i < tmp.length(); i++) {
            if (tmp.charAt(i) == '\n') {
              countReturn++;
            }
          }
          currentLine += countReturn;
          buffer.append(tmp);
          countReturn = 0;
          for (int i = 0; i < tmp.length(); i++) {
            if (tmp.charAt(i) == '\n') {
              countReturn++;
            }
          }
          currentLine += countReturn;
          buffer.append(tmp);
        } else {
          buffer.append(" ");
        }
      }
    }
  }

  /**
   * Gets the buffer.
   *
   * @return the buffer
   */
  public StringBuffer getBuffer() {
    return null;
  }

  /**
   * Gets the string.
   *
   * @return the string
   */
  public String getString() {
    String tmp = buffer.toString();
    tmp = trimString(tmp);
    if (tmp.startsWith("\n")) {
      tmp = tmp.substring(1);
    }
    return trimString(tmp).trim();
  }

  /**
   * Prints the.
   *
   * @param file the file
   */
  public void print(File file) {
    print(file, false);
  }


  private void print(File file, boolean append) {
    try {
      Writer output = new BufferedWriter(new FileWriter(file, append));
      try {
        output.write(trimString(this.buffer.toString()));
      } catch (Exception e) {
        e.printStackTrace();
      } finally {

        output.close();
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }


  private boolean printToken(JavaToken token) {
    switch (token.type) {
      case ABSTRACT:

      case AND:

      case AND_AND:

      case AND_EQUAL:

      case ASSERT:

      case AT:

      case BOOLEAN:

      case BREAK:

      case BYTE:

      case CASE:

      case CATCH:

      case CHAR:

      case CHAR_LITERAL:

      case CLASS:

      case COLON:

      case COMMA:

      case CONTINUE:

      case DEFAULT:

      case DIV:

      case DIVIDE_EQUAL:

      case DO:

      case DOUBLE:

      case ELLIPSIS:

      case ELSE:

      case ENUM:

      case EOF:

      case EQUAL:

      case EQUAL_EQUAL:

      case ARES_TOKEN:

      case EXTENDS:

      case FALSE:

      case FINAL:

      case FINALLY:

      case FLOAT:

      case FOR:

      case GREATER:

      case GREATER_EQUAL:

      case IDENTIFIER:

      case IF:

      case IMPLEMENTS:

      case IMPORT:

      case INSERT:

      case INSTANCEOF:

      case INT:

      case INTEGER_LITERAL:

      case INTERFACE:

      case LBRACE:

      case LBRACKET:

      case LESS:

      case LESS_EQUAL:

      case LONG:

      case LPAREN:

      case MATCH:

      case MINUS:

      case MINUS_EQUAL:

      case MINUS_MINUS:

      case MULTIPLY:

      case MULTIPLY_EQUAL:

      case NATIVE:

      case NEW:

      case NOT:

      case NOT_EQUAL:

      case NULL:

      case OR:

      case ORIGINAL:

      case OR_EQUAL:

      case OR_OR:

      case PACKAGE:

      case PATTERN:

      case PLUGIN:

      case PLUS:

      case PLUS_EQUAL:

      case PLUS_PLUS:

      case POINT:

      case PRIVATE:

      case PROTECTED:

      case PUBLIC:

      case QUESTION:

      case RBRACE:

      case RBRACKET:

      case REAL_LITERAL:

      case REMAINDER:

      case REMAINDER_EQUAL:

      case RETURN:

      case RPAREN:

      case SAR:

      case SAR_EQUAL:

      case SEMICOLON:

      case SHORT:

      case SLL:

      case SLL_EQUAL:

      case SLR:

      case SLR_EQUAL:

      case STATIC:

      case STRIcTFP:

      case STRING_LITERAL:

      case SUPER:

      case SWITCH:

      case SYNCHRONIZED:

      case THIS:

      case THROW:

      case THROWS:

      case MODIFIED:

      case TRANSIENT:

      case TRUE:

      case TRY:

      case TWIDDLE:

      case USE:

      case VOID:

      case VOLATILE:

      case WHILE:

      case WILDCARD:

      case XOR:

      case XOR_EQUAL:
        return true;
      case LINE_COMMENT:
      case BLOCK_COMMENT:
        return false;
      default:
        return true;

    }
  }

  /**
   * Sets the end line.
   *
   * @param node the new end line
   */
  public void setEndLine(AbstractBastNode node) {
    if (endLines == null) {
      return;
    }
    endLines.put(node, currentLine);
  }

  /**
   * Sets the start line.
   *
   * @param node the new start line
   */
  public void setStartLine(AbstractBastNode node) {
    if (startLines == null) {
      return;
    }
    waitingNodes.add(node);

  }


  private String trimString(String input) {
    input = input.replace("\t", " ");
    String inputOld = input;
    input = input.replace("  ", " ");
    while (!inputOld.equals(input)) {
      inputOld = input;
      input = input.replace("  ", " ");
    }
    input = input.replaceAll("[^\\S\\r\\n]+", " ").replaceAll("(?m)^\\s\\n", "\n")
        .replaceAll("^\n[ ]", "\n").replace("\n\n", "\n").replace("\r\r", "\r")
        .replace("\r\n\r\n", "\n\r");
    input = input.replace("\n ", "\n");
    input = input.replace(" \n", "\n");
    for (char c : "+-!=,*/(){}[];<>.:&|~".toCharArray()) {
      input = input.replace(c + " ", c + "");
      input = input.replace(" " + c, c + "");

    }
    input = input.replace("  ", " ");
    return input;
  }

  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(AbstractAresStatement node) {
    assert false;

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAccess node) {
    setStartLine(node);
    node.target.accept(this);
    switch (node.type) {
      case BastAccess.TYPE_DEFAULT:
        break;
      case BastAccess.TYPE_STRUCT_POINTER:
        buffer.append("->");
        break;
      default:
        break;
    }
    addTokenData(node, 0);
    if (node.member != null) {
      node.member.accept(this);
    }
    addTokenData(node, 1);
    setEndLine(node);

  }

  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAdditiveExpr node) {
    setStartLine(node);

    if (node.left != null && node.left.getTag() == BastAdditiveExpr.TAG) {
      AbstractBastExpr tmp = node;
      Stack<BastAdditiveExpr> stack = new Stack<BastAdditiveExpr>();
      while (tmp.getTag() == BastAdditiveExpr.TAG) {
        stack.push((BastAdditiveExpr) tmp);
        tmp = ((BastAdditiveExpr) tmp).left;
      }
      tmp.accept(this);
      while (!stack.isEmpty()) {
        tmp = stack.pop();
        setStartLine(tmp);

        addTokenData(tmp, 0);
        ((BastAdditiveExpr) tmp).right.accept(this);
        setEndLine(tmp);

      }
    } else {
      if (node.left != null) {
        node.left.accept(this);
      }
      addTokenData(node, 0);
      switch (node.type) {
        case BastAdditiveExpr.TYPE_ADD:
          break;
        case BastAdditiveExpr.TYPE_SUB:
          break;
        default:
          break;
      }
      if (node.right != null) {
        node.right.accept(this);
      }
    }
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnd node) {
    setStartLine(node);

    node.left.accept(this);
    addTokenData(node, 0);
    node.right.accept(this);
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnnotation node) {
    setStartLine(node);

    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.exprList != null) {
      for (AbstractBastExpr elem : node.exprList) {
        elem.accept(this);
      }
    }
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnnotationDecl node) {
    setStartLine(node);

    if (node.modifiers != null) {
      for (AbstractBastSpecifier modifier : node.modifiers) {
        modifier.accept(this);
      }
    }
    addTokenData(node, 0);
    addTokenData(node, 1);
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.declarations != null) {
      for (AbstractBastExternalDecl elem : node.declarations) {
        elem.accept(this);
      }
    }
    addTokenData(node, 2);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnnotationElemValue node) {
    setStartLine(node);

    if (node.qualifiedName != null) {
      node.qualifiedName.accept(this);
    }
    addTokenData(node, 0);
    if (node.initList != null) {
      for (AbstractBastExpr elem : node.initList) {
        elem.accept(this);
      }
    }
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAnnotationMethod node) {
    setStartLine(node);

    if (node.modifiers != null) {
      for (AbstractBastSpecifier modifier : node.modifiers) {
        modifier.accept(this);
      }
    }
    if (node.type != null) {
      node.type.accept(this);
    }
    if (node.declarator != null) {
      node.declarator.accept(this);
    }
    addTokenData(node, 0);
    if (node.defaultValue != null) {
      node.defaultValue.accept(this);
    }
    addTokenData(node, 1);

    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastArrayDeclarator node) {
    setStartLine(node);
    if (node.source != null) {
      node.source.accept(this);
    }
    addTokenData(node, 0);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastArrayRef node) {
    setStartLine(node);

    if (node.arrayRef != null) {
      node.arrayRef.accept(this);
    }
    if (node.indices != null) {
      boolean comma = false;
      for (AbstractBastExpr expr : node.indices) {
        if (comma == false) {
          comma = true;
        }
        if (expr != null) {
          expr.accept(this);
        }
      }
    }
    addTokenData(node, 0);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastArrayType node) {
    setStartLine(node);

    addTokenData(node, 0);
    node.type.accept(this);

    if (node.dimensions != null) {
      for (AbstractBastExpr expr : node.dimensions) {
        expr.accept(this);
      }

    }
    addTokenData(node, 1);

    if (node.initializer != null) {
      node.initializer.accept(this);
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAsgnExpr node) {
    setStartLine(node);

    node.left.accept(this);
    switch (node.operation) {
      case BastAsgnExpr.EXPR_TYPE_PLUS_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_DIVIDE_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_MINUS_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_SLL_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_SLR_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_SAR_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_REMAINDER_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_AND_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_OR_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_XOR_EQUAL:
        addTokenData(node, 0);
        break;
      case BastAsgnExpr.EXPR_TYPE_MULTIPLY_EQUAL:
        addTokenData(node, 0);
        break;
      default:
        assert (false);
    }
    node.right.accept(this);
    addTokenData(node, 1);
    addTokenData(node, 2);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastAssertStmt node) {
    setStartLine(node);

    addTokenData(node, 0);
    if (node.firstAssert != null) {
      node.firstAssert.accept(this);
    }
    if (node.secondAssert != null) {
      addTokenData(node, 1);
      node.secondAssert.accept(this);
    }
    addTokenData(node, 2);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastBasicType node) {
    setStartLine(node);

    addTokenData(node, 0);
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastBlock node) {
    setStartLine(node);

    addTokenData(node, 0);
    addTokenData(node, 1);
    if (node.statements != null) {
      for (AbstractBastStatement stmt : node.statements) {
        if (stmt != null) {
          stmt.accept(this);
        }
      }
    }

    addTokenData(node, 2);
    addTokenData(node, 3);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastBlockComment node) {
    assert (false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastBoolConst node) {
    setStartLine(node);

    addTokenData(node, 0);
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastBreak node) {
    setStartLine(node);

    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCall node) {
    setStartLine(node);
    node.function.accept(this);
    addTokenData(node, 0);
    if (node.arguments != null) {
      boolean comma = false;
      for (AbstractBastExpr expr : node.arguments) {
        if (comma == false) {
          comma = true;
        }
        expr.accept(this);
      }
    }
    addTokenData(node, 1);
    addTokenData(node, 2);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCase node) {
    setStartLine(node);
    addTokenData(node, 0);
    node.caseConstant.accept(this);
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCastExpr node) {
    setStartLine(node);

    if (node.castType != null) {
      node.castType.accept(this);
      node.operand.accept(this);
    } else {
      switch (node.type) {
        case BastCastExpr.RND_CAST:
          buffer.append("CAst_RND(");
          break;
        case BastCastExpr.RND_NEG_CAST:
          buffer.append("CAst_RNDNEG(");
          break;
        case BastCastExpr.RND_POS_CAST:
          buffer.append("CAst_RNDPOS(");
          break;
        case BastCastExpr.TRUNC_CAST:
          buffer.append("CAst_TRUNC(");
          break;
        default:
          assert (false);
      }
      node.operand.accept(this);
    }
    addTokenData(node, 0);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCatchClause node) {
    setStartLine(node);

    addTokenData(node, 0);
    if (node.decl != null) {
      node.decl.accept(this);
    }
    addTokenData(node, node.info.tokens.length - 1);
    if (node.block != null) {
      node.block.accept(this);
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCharConst node) {
    setStartLine(node);
    addTokenData(node, 0);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastClassConst node) {
    setStartLine(node);

    addTokenData(node, 0);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastClassDecl node) {
    setStartLine(node);

    if (node.modifiers != null) {
      for (AbstractBastSpecifier modifier : node.modifiers) {
        modifier.accept(this);
      }
    }
    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.typeParameters != null) {
      for (BastTypeParameter typeParameter : node.typeParameters) {
        typeParameter.accept(this);
      }
    }
    if (node.extendedClass != null) {
      addTokenData(node, 1);
      node.extendedClass.accept(this);
    }
    if (node.interfaces != null) {
      addTokenData(node, 2);
      for (BastType inter : node.interfaces) {
        inter.accept(this);
      }
    }
    addTokenData(node, 3);
    if (node.declarations != null) {
      for (AbstractBastInternalDecl decl : node.declarations) {
        decl.accept(this);
      }
    }
    addTokenData(node, 4);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param bastClassKeySpecifier the bast class key specifier
   */
  @Override
  public void visit(BastClassKeySpecifier bastClassKeySpecifier) {
    assert false; // This node is only used in C
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastClassType node) {
    setStartLine(node);

    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.typeArgumentList != null) {
      for (BastTypeArgument arg : node.typeArgumentList) {
        arg.accept(this);
      }
    }
    if (node.subClass != null) {
      node.subClass.accept(this);
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCmp node) {
    setStartLine(node);
    node.left.accept(this);
    addTokenData(node, 0);
    node.right.accept(this);
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCondAnd node) {
    setStartLine(node);

    node.left.accept(this);
    addTokenData(node, 0);
    node.right.accept(this);
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCondExpr node) {
    setStartLine(node);
    node.condition.accept(this);
    addTokenData(node, 0);
    node.truePart.accept(this);
    addTokenData(node, 1);
    node.falsePart.accept(this);
    addTokenData(node, 2);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastCondOr node) {
    setStartLine(node);

    node.left.accept(this);
    addTokenData(node, 0);
    node.right.accept(this);
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastContinue node) {
    setStartLine(node);
    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastDeclaration node) {
    setStartLine(node);

    if (node.modifiers != null) {
      for (AbstractBastSpecifier spec : node.modifiers) {
        spec.accept(this);
      }
    }

    if (node.specifierList != null) {
      for (AbstractBastSpecifier spec : node.specifierList) {
        if (spec != null) {
          spec.accept(this);
        }
      }
    }
    if (node.declaratorList != null) {
      for (AbstractBastDeclarator decl : node.declaratorList) {
        if (decl != null) {
          decl.accept(this);
        }
      }
    }
    addTokenData(node, 0);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastDecrExpr node) {
    setStartLine(node);

    if (node.operand != null) {
      node.operand.accept(this);
    }
    addTokenData(node, 0);
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastDefault node) {
    setStartLine(node);

    addTokenData(node, 0);
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastDummyToken node) {
    assert false; // This node is only used in C
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastEmptyDeclaration node) {
    setStartLine(node);

    addTokenData(node, 0);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastEmptyStmt node) {
    setStartLine(node);

    addTokenData(node, 0);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastEnumDecl node) {
    setStartLine(node);

    if (node.modifiers != null) {
      for (AbstractBastSpecifier modifier : node.modifiers) {
        modifier.accept(this);
      }
    }
    if (node.enumerator != null) {
      node.enumerator.accept(this);
    }
    addTokenData(node, 0);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastEnumMember node) {
    setStartLine(node);

    if (node.annotations != null) {
      for (BastAnnotation annotation : node.annotations) {
        annotation.accept(this);
      }
    }
    if (node.identifier != null) {
      node.identifier.accept(this);
    }
    if (node.init != null) {
      node.accept(this);
    }
    addTokenData(node, 0);
    if (node.initArguments != null) {
      for (AbstractBastExpr expr : node.initArguments) {
        expr.accept(this);
      }
    }
    addTokenData(node, 1);
    addTokenData(node, 2);
    if (node.classBodies != null) {
      for (AbstractBastInternalDecl decl : node.classBodies) {
        decl.accept(this);
      }
    }
    addTokenData(node, 3);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastEnumSpec node) {
    setStartLine(node);

    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }

    if (node.interfaces != null) {
      addTokenData(node, 1);
      for (BastType type : node.interfaces) {
        type.accept(this);
      }
    }
    addTokenData(node, 2);

    if (node.members != null) {
      for (BastEnumMember member : node.members) {
        member.accept(this);
      }
    }
    if (node.declarations != null) {
      for (AbstractBastInternalDecl decl : node.declarations) {
        decl.accept(this);
      }
    }
    addTokenData(node, 3);

    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastExprInitializer node) {
    setStartLine(node);

    addTokenData(node, 0);
    if (node.init != null) {
      node.init.accept(this);
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastExprList node) {
    setStartLine(node);

    if (node.list != null) {
      int count = node.list.size() - 1;
      for (AbstractBastExpr expr : node.list) {
        expr.accept(this);
      }
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastExprWater node) {
    assert false; // This node is only used in C
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastForStmt node) {
    setStartLine(node);

    addTokenData(node, 0);
    if (node.init != null) {
      node.init.accept(this);
    } else {
      if (node.initDecl != null) {
        node.initDecl.accept(this);
      }
    }
    if (node.listStmt != null) {
      addTokenData(node, 1);
      node.listStmt.accept(this);
    }
    if (node.condition != null) {
      node.condition.accept(this);
    }
    if (node.increment != null) {
      node.increment.accept(this);
    }
    addTokenData(node, 2);
    if (node.statement != null) {
      node.statement.accept(this);
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastFunction node) {
    setStartLine(node);

    if (node.modifiers != null) {
      for (AbstractBastSpecifier spec : node.modifiers) {
        spec.accept(this);
      }
    }

    if (node.typeParameters != null) {
      for (BastTypeParameter param : node.typeParameters) {
        param.accept(this);
      }
    }

    if (node.specifierList != null) {
      for (AbstractBastSpecifier spec : node.specifierList) {
        spec.accept(this);
      }
    }
    if (node.returnType != null) {
      node.returnType.accept(this);
    }

    if (node.decl != null) {
      node.decl.accept(this);
    }
    if (node.exceptions != null) {
      addTokenData(node, 0);
      for (AbstractBastExpr expr : node.exceptions) {
        expr.accept(this);
      }
    }
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastFunctionIdentDeclarator node) {
    setStartLine(node);

    if (node.function != null) {
      node.function.accept(this);
    }
    if (node.parameters != null) {
      for (BastNameIdent par : node.parameters) {
        par.accept(this);
      }
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastFunctionParameterDeclarator node) {
    setStartLine(node);

    if (node.function != null) {
      node.function.accept(this);
    }
    if (node.parameters != null) {
      node.parameters.accept(this);
    }
    if (node.pointer != null) {
      node.pointer.accept(this);
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastGoto node) {
    setStartLine(node);
    addTokenData(node, 0);
    node.label.accept(this);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public synchronized void visit(BastIdentDeclarator node) {
    setStartLine(node);
    if (node.pointer != null) {
      node.pointer.accept(this);
    }
    if (node.identifier != null) {
      node.identifier.accept(this);
    }
    if (node.declarator != null) {
      node.declarator.accept(this);
    }
    if (node.expression != null) {
      if (node.expression.getTag() == BastFunctionParameterDeclarator.TAG
          || node.expression.getTag() == BastFunctionIdentDeclarator.TAG) {
        node.expression.accept(this);
      } else {
        if (node.info.tokens != null) {
          addTokenData(node, node.info.tokens.length - 1);
        }
        node.expression.accept(this);
      }
    }
    setEndLine(node);

  }

  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastIf node) {
    setStartLine(node);
    addTokenData(node, 0);
    addTokenData(node, 1);
    if (node.condition != null) {
      node.condition.accept(this);
    }
    addTokenData(node, 2);
    if (node.ifPart != null && node.ifPart.getTag() == TagConstants.BAST_BLOCK) {
      node.ifPart.accept(this);
    } else {
      if (node.ifPart != null) {
        node.ifPart.accept(this);
      }
    }

    if (node.elsePart != null) {
      addTokenData(node, 3);
      node.elsePart.accept(this);
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastImportDeclaration node) {
    setStartLine(node);
    addTokenData(node, 0);
    addTokenData(node, 1);
    if (node.name != null) {
      node.name.accept(this);
    }
    addTokenData(node, 2);
    addTokenData(node, 3);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastIncludeStmt node) {
    if (node.list != null) {
      for (BastNameIdent name : node.list) {
        name.accept(this);
      }
    }
    assert (false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastIncrExpr node) {
    setStartLine(node);
    if (node.operand != null) {
      node.operand.accept(this);
    }
    addTokenData(node, 0);
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastInstanceOf node) {
    setStartLine(node);

    if (node.expr != null) {
      node.expr.accept(this);
    }
    addTokenData(node, 0);
    if (node.type != null) {
      node.type.accept(this);
    }
    addTokenData(node, 1);
    setEndLine(node);

  }

  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastIntConst node) {
    setStartLine(node);
    addTokenData(node, 0);
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastInterfaceDecl node) {
    setStartLine(node);

    if (node.modifiers != null) {
      for (AbstractBastSpecifier modifier : node.modifiers) {
        modifier.accept(this);
      }
    }
    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.typeParameters != null) {
      for (BastTypeParameter elem : node.typeParameters) {
        elem.accept(this);
      }
    }
    if (node.interfaces != null) {
      addTokenData(node, 1);
      for (BastType elem : node.interfaces) {
        elem.accept(this);
      }
    }
    addTokenData(node, 2);
    if (node.declarations != null) {
      for (AbstractBastInternalDecl elem : node.declarations) {
        elem.accept(this);
      }
    }
    addTokenData(node, 3);

    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastLabelStmt node) {
    setStartLine(node);
    if (node.ident != null) {
      node.ident.accept(this);
    }
    addTokenData(node, 0);
    if (node.stmt != null) {
      node.stmt.accept(this);
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastLineComment node) {
    assert (false);
  }

  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastListInitializer node) {
    setStartLine(node);
    if (node.list != null) {
      for (AbstractBastInitializer init : node.list) {
        init.accept(this);
      }
    }
    addTokenData(node, 0);
    setEndLine(node);

  }

  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastMultiExpr node) {
    setStartLine(node);
    node.left.accept(this);
    addTokenData(node, 0);
    node.right.accept(this);
    addTokenData(node, 1);
    setEndLine(node);

  }

  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastNameIdent node) {
    setStartLine(node);
    addTokenData(node, 0);
    addTokenData(node, 1);
    addTokenData(node, 2);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastNew node) {
    setStartLine(node);
    addTokenData(node, 0);
    if (node.typeArguments != null) {
      int count = 0;
      for (BastType typeArgument : node.typeArguments) {
        count++;
        typeArgument.accept(this);
      }
    }
    if (node.type != null) {
      node.type.accept(this);
    }

    addTokenData(node, 1);

    if (node.parameters != null) {
      int count = 0;
      for (AbstractBastExpr parameter : node.parameters) {
        count++;
        parameter.accept(this);
      }
    }
    addTokenData(node, 2);
    addTokenData(node, 3);
    if (node.declarations != null) {
      for (AbstractBastInternalDecl decl : node.declarations) {
        decl.accept(this);
      }
    }

    addTokenData(node, 4);
    addTokenData(node, 5);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastNullConst node) {
    setStartLine(node);
    addTokenData(node, 0);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastOr node) {
    setStartLine(node);

    node.left.accept(this);
    addTokenData(node, 0);
    node.right.accept(this);
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastPackage node) {
    setStartLine(node);

    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }
    if (node.annotations != null) {
      for (BastAnnotation elem : node.annotations) {
        elem.accept(this);
      }
    }
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastParameter node) {
    setStartLine(node);

    if (node.specifiers != null) {
      int counter = 0;
      for (AbstractBastSpecifier spec : node.specifiers) {
        spec.accept(this);
        counter++;
      }
    }
    addTokenData(node, 0);
    if (node.declarator != null) {
      node.declarator.accept(this);
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastParameterList node) {
    setStartLine(node);

    if (node.parameters != null) {
      int counter = 0;
      for (BastParameter par : node.parameters) {
        par.accept(this);
        counter++;
      }
    }
    addTokenData(node, 0);
    setEndLine(node);

  }
  

  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastPointer node) {
    setStartLine(node);

    if (node.pointer != null) {
      node.pointer.accept(this);
    }
    if (node.list != null) {
      for (AbstractBastSpecifier qual : node.list) {
        qual.accept(this);
      }
    }
    buffer.append("*");
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastPointerType node) {
    assert (false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastProgram node) {
    setStartLine(node);
    if (node.annotations != null) {
      for (BastAnnotation expr : node.annotations) {
        expr.accept(this);
      }
    }
    if (node.packageName != null) {
      node.packageName.accept(this);
    }
    if (node.imports != null) {
      for (BastImportDeclaration expr : node.imports) {
        expr.accept(this);
      }
    }
    if (node.functionBlocks != null) {
      for (AbstractBastExternalDecl expr : node.functionBlocks) {
        expr.accept(this);
      }
    }

    addTokenData(node, 0);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastRealConst node) {
    setStartLine(node);
    addTokenData(node, 0);
    setEndLine(node);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastRegularDeclarator node) {
    setStartLine(node);

    if (node.pointer != null) {
      node.pointer.accept(this);
    }
    if (node.declaration != null) {
      node.declaration.accept(this);
    }
    if (node.expression != null) {
      node.expression.accept(this);
    }
    if (node.init != null) {
      node.init.accept(this);
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastReturn node) {
    setStartLine(node);
    addTokenData(node, 0);
    if (node.returnValue != null) {
      node.returnValue.accept(this);
    }
    addTokenData(node, 1);
    addTokenData(node, 2);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastShift node) {
    setStartLine(node);

    node.left.accept(this);
    addTokenData(node, 0);
    addTokenData(node, 1);
    addTokenData(node, 2);
    node.right.accept(this);
    addTokenData(node, 3);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastStorageClassSpecifier node) {
    assert (false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastStringConst node) {
    setStartLine(node);

    addTokenData(node, 0);
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastStructDecl node) {
    setStartLine(node);

    buffer.append("structdecl ");
    buffer.append(node.name);
    buffer.append(" {");
    if (node.members != null) {
      for (BastStructMember expr : node.members) {
        expr.accept(this);
      }
    }
    buffer.append("};");
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastStructDeclarator node) {
    assert (false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastStructMember node) {
    setStartLine(node);

    buffer.append(node.name);
    buffer.append(" : ");
    buffer.append(node.type);
    buffer.append(" @ ");
    node.position.accept(this);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastStructOrUnionSpecifierType node) {
    assert (false);
  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastSuper node) {
    setStartLine(node);

    addTokenData(node, 0);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastSwitch node) {
    setStartLine(node);

    addTokenData(node, 0);
    addTokenData(node, 1);
    node.condition.accept(this);
    addTokenData(node, 2);
    addTokenData(node, 3);
    if (node.switchGroups != null) {
      for (AbstractBastStatement group : node.switchGroups) {
        group.accept(this);
      }
    }
    addTokenData(node, 4);
    addTokenData(node, 5);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastSwitchCaseGroup node) {
    setStartLine(node);
    if (node.labels != null) {
      for (AbstractBastStatement expr : node.labels) {
        expr.accept(this);
      }
    }
    if (node.stmts != null) {
      for (AbstractBastStatement stmt : node.stmts) {
        stmt.accept(this);
      }
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastSynchronizedBlock node) {
    setStartLine(node);

    addTokenData(node, 0);
    addTokenData(node, 1);
    if (node.expr != null) {
      node.expr.accept(this);
    }
    addTokenData(node, 2);
    if (node.block != null) {
      node.block.accept(this);
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTemplateSpecifier node) {
    setStartLine(node);

    if (node.typeArguments != null) {
      for (BastTypeArgument decl : node.typeArguments) {
        decl.accept(this);
      }
    }

    if (node.target != null) {
      node.target.accept(this);
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastThis node) {
    setStartLine(node);

    addTokenData(node, 0);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastThrowStmt node) {
    setStartLine(node);

    addTokenData(node, 0);
    if (node.exception != null) {
      node.exception.accept(this);
    }
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTryStmt node) {
    setStartLine(node);

    addTokenData(node, 0);
    if (node.resources != null) {
      for (BastDeclaration decl : node.resources) {
        decl.accept(this);
      }
    }
    addTokenData(node, 1);
    if (node.block != null) {
      node.block.accept(this);
    }
    if (node.catches != null) {
      for (BastCatchClause elem : node.catches) {
        elem.accept(this);
      }
    }
    addTokenData(node, 2);
    if (node.finallyBlock != null) {
      node.finallyBlock.accept(this);
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTypeArgument node) {
    setStartLine(node);

    addTokenData(node, 0);
    addTokenData(node, 1);
    addTokenData(node, 2);
    if (node.type != null) {
      node.type.accept(this);
    }
    addTokenData(node, 3);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastTypeName node) {
    setStartLine(node);

    if (node.specifiers != null) {
      for (AbstractBastSpecifier spec : node.specifiers) {
        spec.accept(this);
      }
    }
    if (node.declarator != null) {
      node.declarator.accept(this);
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTypeParameter node) {
    setStartLine(node);

    addTokenData(node, 0);
    if (node.name != null) {
      node.name.accept(this);
    }
    addTokenData(node, 1);
    if (node.list != null) {
      for (BastType decl : node.list) {
        decl.accept(this);
      }
    }
    addTokenData(node, 2);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  public void visit(BastTypeQualifier node) {
    setStartLine(node);

    addTokenData(node, 0);
    switch (node.type) {
      case BastTypeQualifier.CONST_QUALIFIER:
        break;
      case BastTypeQualifier.VOLATILE_QUALIFIER:
        break;
      case BastTypeQualifier.TYPE_PUBLIC:
        break;
      case BastTypeQualifier.TYPE_PROTECTED:
        break;
      case BastTypeQualifier.TYPE_PRIVATE:
        break;
      case BastTypeQualifier.TYPE_STATIC:
        break;
      case BastTypeQualifier.TYPE_ABSTRACT:
        break;
      case BastTypeQualifier.TYPE_FINAL:
        break;
      case BastTypeQualifier.TYPE_NATIVE:
        break;
      case BastTypeQualifier.TYPE_SYNCHRONIZED:
        break;
      case BastTypeQualifier.TYPE_TRANSIENT:
        break;
      case BastTypeQualifier.TYPE_VOLATILE:
        break;
      case BastTypeQualifier.TYPE_STRICTFP:
        break;
      default:
        assert (false);

    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastTypeSpecifier node) {
    setStartLine(node);

    addTokenData(node, 0);
    if (node.type != null) {
      node.type.accept(this);
    }
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastUnaryExpr node) {
    setStartLine(node);

    addTokenData(node, 0);
    node.operand.accept(this);
    addTokenData(node, 1);
    setEndLine(node);

  }


  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastWhileStatement node) {
    setStartLine(node);

    addTokenData(node, 0);
    if (node.type == BastWhileStatement.TYPE_WHILE) {
      addTokenData(node, 1);
      if (node.expression != null) {
        node.expression.accept(this);
      }
      addTokenData(node, 2);
      if (node.statement != null) {
        node.statement.accept(this);
      }
    } else {
      if (node.statement != null) {
        node.statement.accept(this);
      }
      addTokenData(node, 1);
      addTokenData(node, 2);
      if (node.expression != null) {
        node.expression.accept(this);
      }
      addTokenData(node, 3);
    }
    addTokenData(node, 4);
    setEndLine(node);

  }

  /**
   * Visit.
   *
   * @param node the node
   */
  @Override
  public void visit(BastXor node) {
    setStartLine(node);

    node.left.accept(this);
    addTokenData(node, 0);
    node.right.accept(this);
    setEndLine(node);

  }


}
