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

package de.fau.cs.inf2.cas.common.parser.odin;

import de.fau.cs.inf2.cas.common.parser.ITokenType;

import java.util.HashMap;

public enum BasicJavaToken implements ITokenType {

  TRUE("true", 0),
  EOF("eof", 1),
  LINE_COMMENT("line comment", 2),
  BLOCK_COMMENT("block comment", 3),
  ELSE("else", 4),
  DIV("/", 5),
  LPAREN("(", 6),
  RPAREN(")", 7),
  LBRACE("{", 8),
  RBRACE("}", 9),
  LBRACKET("[", 10),
  RBRACKET("]", 11),
  SEMICOLON(";", 12),
  COMMA(",", 13),
  POINT(".", 14),
  ELLIPSIS("...", 15),
  AT("@", 16),
  QUESTION("?", 17),
  EQUAL("=", 18),
  FALSE("false", 19),
  PLUS("+", 20),
  MINUS("-", 21),
  PLUS_PLUS("++", 22),
  MINUS_MINUS("--", 23),
  TWIDDLE("~", 24),
  NOT("!", 25),
  PLUS_EQUAL("+=", 26),
  MINUS_EQUAL("-=", 27),
  MULTIPLY_EQUAL("*=", 28),
  DIVIDE_EQUAL("/=", 29),
  AND_EQUAL("&=", 30),
  OR_EQUAL("|=", 31),
  XOR_EQUAL("^=", 32),
  REMAINDER_EQUAL("%=", 33),
  SLL_EQUAL("<<=", 34),
  SLR_EQUAL(">>=", 35),
  SAR_EQUAL(">>>=", 36),
  COLON(":", 37),
  REMAINDER("%", 38),
  SAR(">>>", 39),
  SLR(">>", 40),
  SLL("<<", 41),
  LESS_EQUAL("<=", 42),
  GREATER_EQUAL(">=", 43),
  EQUAL_EQUAL("==", 44),
  NOT_EQUAL("!=", 45),
  XOR("^", 46),
  OR("|", 46),
  AND("&", 46),
  AND_AND("&&", 46),
  OR_OR("||&", 46),
  PACKAGE("package", 100),
  CLASS("class", 101),
  PUBLIC("public", 102),
  PROTECTED("protected", 103),
  PRIVATE("private", 104),
  STATIC("static", 105),
  ABSTRACT("abstract", 106),
  FINAL("final", 107),
  NATIVE("native", 108),
  SYNCHRONIZED("synchronized", 109),
  TRANSIENT("transient", 110),
  VOLATILE("volatile", 111),
  STRIcTFP("strictfp", 112),
  ENUM("enum", 113),
  IDENTIFIER("identifier", 114),
  IMPORT("import", 115),
  MULTIPLY("multiply", 116),
  INTERFACE("interface", 117),
  LESS("<", 118),
  GREATER(">", 119),
  EXTENDS("extends", 120),
  // AND("and",121),
  BYTE("byte", 122),
  SHORT("short", 123),
  CHAR("char", 124),
  INT("int", 125),
  LONG("long", 126),
  FLOAT("float", 127),
  DOUBLE("double", 128),
  BOOLEAN("boolean", 129),
  IMPLEMENTS("implements", 130),
  VOID("void", 131),
  THROWS("throws", 132),
  THIS("throws", 133),
  SUPER("throws", 134),
  STRING_LITERAL("string literal", 135),
  INTEGER_LITERAL("integer literal", 136),
  REAL_LITERAL("real literal", 137),
  CHAR_LITERAL("char literal", 138),
  NULL("null", 139),
  NEW("null", 140),
  INSTANCEOF("instanceof", 141),
  SWITCH("switch", 142),
  CASE("case", 143),
  RETURN("return", 144),
  DEFAULT("default", 145),
  FOR("for", 146),
  ASSERT("assert", 147),
  TRY("try", 148),
  CATCH("catch", 149),
  FINALLY("finally", 150),
  BREAK("break", 151),
  IF("if", 152),
  WHILE("while", 153),
  CONTINUE("continue", 154),
  THROW("throw", 155),
  DO("do", 156),
  MATCH("match", 441),
  WILDCARD("wildcard", 442),
  PATTERN("pattern", 443),
  USE("use", 444),
  ORIGINAL("original", 445),
  MODIFIED("modified", 446),
  PLUGIN("plugin", 447),
  ARES_TOKEN("//#", 448),
  INSERT("insert", 449),
  CHOICE("choice", 550);
  public static HashMap<Integer, BasicJavaToken> map = new HashMap<>();
  public final String name;

  final int id;

  
  private BasicJavaToken(String name, int id) {
    this.name = name;
    this.id = id;
  }

  
  /**
   * Gets the id.
   *
   * @return the id
   */
  @SuppressWarnings("ucd")
  public int getId() {
    return id;
  }

  /**
   * Gets the name.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

}
