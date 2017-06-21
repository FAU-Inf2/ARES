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

import de.fau.cs.inf2.cas.common.parser.AresExtension;
import de.fau.cs.inf2.cas.common.parser.SyntaxError;

public class JavaLexer {
  private class ScanEscapeResult {
    private final CharElement character;
    private final JavaToken token;

    private ScanEscapeResult(CharElement character, JavaToken token) {
      this.character = character;
      this.token = token;
    }
  }
  
  private static final boolean ENABLE_UNICODE = true;

  private final AresExtension extension;

  /**
   * Instantiates a new java lexer.
   */
  public JavaLexer() {
    this(AresExtension.WITH_ARES_EXTENSIONS, true);
  }

  /**
   * Instantiates a new java lexer.
   *
   */
  public JavaLexer(AresExtension extension) {
    this(extension, true);
  }

  JavaLexer(AresExtension extension, final boolean addComments) {
    this.extension = extension;
  }

  private TokenAndHistory addStructureTokenToHistory(TokenAndHistory previousToken,
      TokenAndHistory newToken) {
    if (previousToken == null || previousToken.token == null || previousToken.flushed) {
      return newToken;
    }
    switch (((JavaToken) previousToken.token).type) {
      case LINE_COMMENT:
      case BLOCK_COMMENT:
      case LBRACE:
      case RPAREN:
      case RBRACE:
      case SEMICOLON:
      case LPAREN:
      case LBRACKET:
      case RBRACKET:
      case COMMA:
      case POINT:
      case ARES_TOKEN:

        newToken.prevTokens.addAll(previousToken.prevTokens);
        newToken.prevTokens.add(previousToken.token);
        break;
      default:
        break;
    }
    return newToken;
  }

  private CharElement getNextChar(byte[] fileData, int pos, boolean ignoreUnicode) {
    if (pos >= fileData.length) {
      return null;
    }
    if (ENABLE_UNICODE && !ignoreUnicode) {

      if (fileData[pos] == '\\') {
        String tmpString = String.valueOf('\\');
        if (fileData[pos + 1] == 'u') {
          int tmpPos = 2 + pos;
          tmpString += 'u';
          while (fileData[tmpPos] == 'u') {
            tmpPos++;
            tmpString += 'u';
          }
          char[] charArray = new char[4];
          charArray[0] = (char) getNumberFromHex(fileData[tmpPos]);
          charArray[1] = (char) getNumberFromHex(fileData[tmpPos + 1]);
          charArray[2] = (char) getNumberFromHex(fileData[tmpPos + 2]);
          charArray[3] = (char) getNumberFromHex(fileData[tmpPos + 3]);
          tmpString += String.valueOf((char) fileData[tmpPos]) + (char) fileData[tmpPos + 1]
              + (char) fileData[tmpPos + 2] + (char) fileData[tmpPos + 3];
          if (charArray[0] <= 15 && charArray[1] 
              <= 15 && charArray[2] <= 15 && charArray[3] <= 15) {
            return new CharElement(tmpPos + 3,
                (char) (((charArray[0] * 16 + charArray[1]) 
                    * 16 + charArray[2]) * 16 + charArray[3]), tmpString);
          } else {
            return new CharElement(pos, (char) fileData[pos], String.valueOf((char) fileData[pos]));
          }
        }
      }
      return new CharElement(pos, (char) fileData[pos], String.valueOf((char) fileData[pos]));
    } else {
      return new CharElement(pos, (char) fileData[pos], String.valueOf((char) fileData[pos]));
    }
  }

  private byte getNumberFromHex(byte hexCode) {
    switch (hexCode) {
      case '0':
        return 0;
      case '1':
        return 1;
      case '2':
        return 2;
      case '3':
        return 3;
      case '4':
        return 4;
      case '5':
        return 5;
      case '6':
        return 6;
      case '7':
        return 7;
      case '8':
        return 8;
      case '9':
        return 9;
      case 'a':
      case 'A':
        return 10;
      case 'b':
      case 'B':
        return 11;
      case 'c':
      case 'C':
        return 12;
      case 'd':
      case 'D':
        return 13;
      case 'e':
      case 'E':
        return 14;
      case 'f':
      case 'F':
        return 15;
      default:
        return 127;
    }

  }

  /**
   * Next token.
   *
   * @param fileData the file data
   * @param previousToken the previous token
   * @return the token and history
   */
  @SuppressWarnings({ "checkstyle:AvoidEscapedUnicodeCharacters", "PMD.ExcessiveMethodLength" })
  public TokenAndHistory nextToken(FileData fileData, TokenAndHistory previousToken) {
    int position = 0;
    int endLine = 0;
    if (previousToken != null) {
      position = ((JavaToken) previousToken.token).endPos + 1;
      endLine = ((JavaToken) previousToken.token).endLine;
    }
    JavaToken tmpToken = new JavaToken();
    tmpToken.endLine = endLine;
    TokenAndHistory tmpTokenAndHistory = new TokenAndHistory(tmpToken);

    if (position >= fileData.data.length) {
      tmpToken.type = BasicJavaToken.EOF;

      return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
    }
    CharElement previousChar = null;
    CharElement character = getNextChar(fileData.data, position, false);
    if (previousToken != null && ((JavaToken) previousToken.token).insideComment) {
      if (character.character == ' ' || character.character == '\t'
          || character.character == '\f') {
        // do nothing
      }

      while (character.character == ' ' || character.character == '\t'
          || character.character == '\f') {
        tmpToken.whiteSpace.append(character);
        previousChar = character;
        character = getNextChar(fileData.data, character.endPos + 1, false);
      }
    } else {
      if (position == 0 && character.character == '\uFFEF') {
        tmpToken.whiteSpace.append(character);
        previousChar = character;
        if (character.endPos + 1 >= fileData.data.length) {
          tmpToken.type = BasicJavaToken.EOF;

          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        character = getNextChar(fileData.data, character.endPos + 1, false);
      }
      if (position == 0 && character.character == '\uFFBB') {
        tmpToken.whiteSpace.append(character);
        previousChar = character;
        if (character.endPos + 1 >= fileData.data.length) {
          tmpToken.type = BasicJavaToken.EOF;

          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        character = getNextChar(fileData.data, character.endPos + 1, false);
      }
      if (position == 0 && character.character == '\uFFBF') {
        tmpToken.whiteSpace.append(character);
        previousChar = character;
        if (character.endPos + 1 >= fileData.data.length) {
          tmpToken.type = BasicJavaToken.EOF;

          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        character = getNextChar(fileData.data, character.endPos + 1, false);
      }
      if (character.character == ' ' || character.character == '\t' || character.character == '\f'
          || character.character == '\r' || character.character == '\n') {
        // do nothing
      }

      while (character.character == ' ' || character.character == '\t'
          || character.character == '\f' || character.character == '\r'
          || character.character == '\n') {
        tmpToken.whiteSpace.append(character);
        if (character.character == '\n') {
          tmpToken.endLine += 1;
          endLine++;
        }
        if (character.endPos + 1 >= fileData.data.length) {
          tmpToken.type = BasicJavaToken.EOF;

          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        previousChar = character;
        character = getNextChar(fileData.data, character.endPos + 1, false);
      }
    }

    switch (character.character) {
      case '=':
        CharElement tmpCharacter = getNextChar(fileData.data, character.endPos + 1, false);
        switch (tmpCharacter.character) {
          case '=':
            character = getNextChar(fileData.data, character.endPos + 1, false);
            tmpToken.type = BasicJavaToken.EQUAL_EQUAL;
            tmpToken.data.append("==");
            tmpToken.endPos = tmpCharacter.endPos;
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
          default:
            tmpToken.endPos = character.endPos;
            tmpToken.data.append('=');
            tmpToken.type = BasicJavaToken.EQUAL;
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
      case '?':
        tmpToken.endPos = character.endPos;
        tmpToken.data.append('?');
        tmpToken.type = BasicJavaToken.QUESTION;
        return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
      case '|':
        tmpCharacter = getNextChar(fileData.data, character.endPos + 1, false);
        switch (tmpCharacter.character) {
          case '|':
            character = getNextChar(fileData.data, character.endPos, false);
            tmpToken.type = BasicJavaToken.OR_OR;
            tmpToken.data.append("||");
            tmpToken.endPos = tmpCharacter.endPos;
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
          case '=':
            character = getNextChar(fileData.data, character.endPos + 1, false);
            tmpToken.type = BasicJavaToken.OR_EQUAL;
            tmpToken.data.append("|=");
            tmpToken.endPos = tmpCharacter.endPos;
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
          default:
            tmpToken.endPos = character.endPos;
            tmpToken.data.append('|');
            tmpToken.type = BasicJavaToken.OR;
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }

      case '(':
        tmpToken.endPos = character.endPos;
        tmpToken.data.append('(');
        tmpToken.type = BasicJavaToken.LPAREN;
        return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
      case ')':
        tmpToken.endPos = character.endPos;
        tmpToken.data.append(')');
        tmpToken.type = BasicJavaToken.RPAREN;
        return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
      case '{':
        tmpToken.type = BasicJavaToken.LBRACE;
        tmpToken.endPos = character.endPos;
        tmpToken.data.append('{');
        return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
      case '}':
        tmpToken.endPos = character.endPos;
        tmpToken.data.append('}');
        tmpToken.type = BasicJavaToken.RBRACE;
        return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
      case '[':
        tmpToken.endPos = character.endPos;
        tmpToken.type = BasicJavaToken.LBRACKET;
        tmpToken.data.append('[');
        return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
      case ']':
        tmpToken.endPos = character.endPos;
        tmpToken.type = BasicJavaToken.RBRACKET;
        tmpToken.data.append(']');
        return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
      case ';':
        tmpToken.type = BasicJavaToken.SEMICOLON;
        tmpToken.endPos = character.endPos;
        tmpToken.data.append(';');
        return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
      case ',':
        tmpToken.endPos = character.endPos;
        tmpToken.type = BasicJavaToken.COMMA;
        tmpToken.data.append(',');
        return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
      case ':':
        tmpToken.endPos = character.endPos;
        tmpToken.type = BasicJavaToken.COLON;
        tmpToken.data.append(':');
        return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
      case '<':
        tmpCharacter = getNextChar(fileData.data, character.endPos + 1, false);
        switch (tmpCharacter.character) {
          case '<':
            CharElement tmpCharacter2 = getNextChar(fileData.data, tmpCharacter.endPos + 1, false);
            switch (tmpCharacter2.character) {
              case '=':
                tmpToken.endPos = tmpCharacter2.endPos;
                tmpToken.type = BasicJavaToken.SLL_EQUAL;
                tmpToken.data.append("<<=");
                return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
              default:
                tmpToken.endPos = character.endPos;
                tmpToken.type = BasicJavaToken.LESS;
                tmpToken.data.append('<');
                return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
            }
          case '=':
            character = tmpCharacter;
            tmpToken.type = BasicJavaToken.LESS_EQUAL;
            tmpToken.data.append("<=");
            tmpToken.endPos = tmpCharacter.endPos;
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
          default:
            tmpToken.endPos = character.endPos;
            tmpToken.type = BasicJavaToken.LESS;
            tmpToken.data.append('<');
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
      case '>':
        tmpCharacter = getNextChar(fileData.data, character.endPos + 1, false);
        if (tmpCharacter != null) {
          switch (tmpCharacter.character) {
            case '>':
              CharElement tmpCharacter2 =
                  getNextChar(fileData.data, tmpCharacter.endPos + 1, false);
              if (tmpCharacter2 != null) {
                switch (tmpCharacter2.character) {
                  case '>':
                    CharElement tmpCharacter3 =
                        getNextChar(fileData.data, tmpCharacter2.endPos + 1, false);
                    if (tmpCharacter3.character == '=') {
                      tmpToken.endPos = tmpCharacter3.endPos;
                      tmpToken.type = BasicJavaToken.SAR_EQUAL;
                      tmpToken.data.append(">>>=");
                      return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
                    } else {
                      tmpToken.endPos = character.endPos;
                      tmpToken.type = BasicJavaToken.GREATER;
                      tmpToken.data.append('>');
                      return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
                    }
                  case '=':
                    tmpToken.endPos = tmpCharacter2.endPos;
                    tmpToken.type = BasicJavaToken.SLR_EQUAL;
                    tmpToken.data.append(">>=");
                    return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
                  default:
                    tmpToken.endPos = character.endPos;
                    tmpToken.type = BasicJavaToken.GREATER;
                    tmpToken.data.append('>');
                    return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
                }
              } else {
                tmpToken.endPos = character.endPos;
                tmpToken.type = BasicJavaToken.GREATER;
                tmpToken.data.append('>');
                return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
              }
            case '=':
              character = tmpCharacter;
              tmpToken.type = BasicJavaToken.GREATER_EQUAL;
              tmpToken.data.append(">=");
              tmpToken.endPos = tmpCharacter.endPos;
              return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
            default:
              tmpToken.endPos = character.endPos;
              tmpToken.type = BasicJavaToken.GREATER;
              tmpToken.data.append('>');
              return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
          }
        } else {
          tmpToken.endPos = character.endPos;
          tmpToken.type = BasicJavaToken.GREATER;
          tmpToken.data.append('>');
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
      case '~':
        tmpToken.endPos = character.endPos;
        tmpToken.type = BasicJavaToken.TWIDDLE;
        tmpToken.data.append('~');
        return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
      case '&':
        tmpCharacter = getNextChar(fileData.data, character.endPos + 1, false);
        switch (tmpCharacter.character) {
          case '&':
            character = tmpCharacter;
            tmpToken.type = BasicJavaToken.AND_AND;
            tmpToken.data.append("&&");
            tmpToken.endPos = tmpCharacter.endPos;
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
          case '=':
            character = tmpCharacter;
            tmpToken.type = BasicJavaToken.AND_EQUAL;
            tmpToken.data.append("&=");
            tmpToken.endPos = tmpCharacter.endPos;
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
          default:
            tmpToken.endPos = character.endPos;
            tmpToken.type = BasicJavaToken.AND;
            tmpToken.data.append('&');
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }

      case '*':
        tmpCharacter = getNextChar(fileData.data, character.endPos + 1, false);
        switch (tmpCharacter.character) {
          case '=':
            character = tmpCharacter;
            tmpToken.type = BasicJavaToken.MULTIPLY_EQUAL;
            tmpToken.data.append("*=");
            tmpToken.endPos = tmpCharacter.endPos;
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
          default:
            tmpToken.endPos = character.endPos;
            tmpToken.type = BasicJavaToken.MULTIPLY;
            tmpToken.data.append('*');
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }

      case '@':
        tmpToken.endPos = character.endPos;
        tmpToken.type = BasicJavaToken.AT;
        tmpToken.data.append('@');
        return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
      case '.':
        tmpCharacter = getNextChar(fileData.data, character.endPos + 1, false);
        if (tmpCharacter.character == '.'
            && getNextChar(fileData.data, tmpCharacter.endPos + 1, false).character == '.') {
          character = getNextChar(fileData.data, tmpCharacter.endPos + 1, false);
          tmpToken.type = BasicJavaToken.ELLIPSIS;
          tmpToken.endPos = character.endPos;
          tmpToken.data.append("...");
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        switch (tmpCharacter.character) {
          case '0':
          case '1':
          case '2':
          case '3':
          case '4':
          case '5':
          case '6':
          case '7':
          case '8':
          case '9':

            tmpToken = scanNumber(tmpToken, fileData.data,
                (previousChar == null) ? position - 1 : previousChar.endPos);
            if (tmpToken.type != null) {
              tmpTokenAndHistory = new TokenAndHistory(tmpToken);
              return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
            }
            break;
          default:
            tmpToken.endPos = character.endPos;
            tmpToken.type = BasicJavaToken.POINT;
            tmpToken.data.append('.');
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        break;
      case '+':
        tmpCharacter = getNextChar(fileData.data, character.endPos + 1, false);
        switch (tmpCharacter.character) {
          case '+':
            tmpToken.type = BasicJavaToken.PLUS_PLUS;
            tmpToken.data.append("++");
            tmpToken.endPos = tmpCharacter.endPos;
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
          case '=':
            tmpToken.type = BasicJavaToken.PLUS_EQUAL;
            tmpToken.data.append("+=");
            tmpToken.endPos = tmpCharacter.endPos;
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
          default:
            tmpToken.type = BasicJavaToken.PLUS;
            tmpToken.endPos = character.endPos;
            tmpToken.data.append("+");
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
      case '-':
        tmpCharacter = getNextChar(fileData.data, character.endPos + 1, false);
        switch (tmpCharacter.character) {
          case '-':
            tmpToken.type = BasicJavaToken.MINUS_MINUS;
            tmpToken.data.append("--");
            tmpToken.endPos = tmpCharacter.endPos;
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
          case '=':
            tmpToken.type = BasicJavaToken.MINUS_EQUAL;
            tmpToken.data.append("-=");
            tmpToken.endPos = tmpCharacter.endPos;
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
          default:
            tmpToken.type = BasicJavaToken.MINUS;
            tmpToken.endPos = character.endPos;
            tmpToken.data.append("-");
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
      case '!':
        tmpCharacter = getNextChar(fileData.data, character.endPos + 1, false);
        switch (tmpCharacter.character) {
          case '=':
            tmpToken.type = BasicJavaToken.NOT_EQUAL;
            tmpToken.data.append("!=");
            tmpToken.endPos = tmpCharacter.endPos;
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
          default:
            tmpToken.type = BasicJavaToken.NOT;
            tmpToken.endPos = character.endPos;
            tmpToken.data.append("!");
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
      case '^':
        tmpCharacter = getNextChar(fileData.data, character.endPos + 1, false);
        switch (tmpCharacter.character) {
          case '=':
            tmpToken.type = BasicJavaToken.XOR_EQUAL;
            tmpToken.data.append("^=");
            tmpToken.endPos = tmpCharacter.endPos;
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
          default:
            tmpToken.type = BasicJavaToken.XOR;
            tmpToken.endPos = character.endPos;
            tmpToken.data.append("^");
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
      case '%':
        tmpCharacter = getNextChar(fileData.data, character.endPos + 1, false);
        switch (tmpCharacter.character) {
          case '=':
            tmpToken.type = BasicJavaToken.REMAINDER_EQUAL;
            tmpToken.data.append("%=");
            tmpToken.endPos = tmpCharacter.endPos;
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
          default:
            tmpToken.type = BasicJavaToken.REMAINDER;
            tmpToken.endPos = character.endPos;
            tmpToken.data.append("%");
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
      case '/':
        tmpCharacter = getNextChar(fileData.data, character.endPos + 1, false);
        switch (tmpCharacter.character) {
          case '/':
            CharElement tmptmpCharacter = getNextChar(fileData.data, character.endPos + 2, false);
            if (extension == AresExtension.WITH_ARES_EXTENSIONS) {
              switch (tmptmpCharacter.character) {
                case '#':
                  tmpToken.type = BasicJavaToken.ARES_TOKEN;
                  tmpToken.data.append("//#");
                  tmpToken.endPos = tmptmpCharacter.endPos;
                  return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
                default:
                  tmpToken.data.append(character);
                  character = tmpCharacter;
                  while (character.endPos + 1 < fileData.data.length && character.character != '\r'
                      && character.character != '\n') {
                    tmpToken.data.append(character);
                    tmpToken.endPos = character.endPos;
                    character = getNextChar(fileData.data, character.endPos + 1, true);
                  }
                  if (character.endPos + 1 == fileData.data.length && character.character != '\n') {
                    tmpToken.data.append(character);
                    tmpToken.endPos = character.endPos;
                  }
                  tmpToken.type = BasicJavaToken.LINE_COMMENT;
                  return nextToken(fileData,
                      addStructureTokenToHistory(previousToken, tmpTokenAndHistory));
              }
            } else {
              tmpToken.data.append(character);
              character = tmpCharacter;
              while (character.endPos + 1 < fileData.data.length && character.character != '\r'
                  && character.character != '\n') {
                tmpToken.data.append(character);
                tmpToken.endPos = character.endPos;
                character = getNextChar(fileData.data, character.endPos + 1, true);
              }
              if (character.endPos + 1 == fileData.data.length && character.character != '\n') {
                tmpToken.data.append(character);
                tmpToken.endPos = character.endPos;
              }
              tmpToken.type = BasicJavaToken.LINE_COMMENT;
              return nextToken(fileData,
                  addStructureTokenToHistory(previousToken, tmpTokenAndHistory));
            }

          case '*':
            tmpToken.data.append("/*");
            character = getNextChar(fileData.data, tmpCharacter.endPos + 1, true);
            while (character.endPos + 1 < fileData.data.length && (character.character != '*'
                || getNextChar(fileData.data, character.endPos + 1, true).character != '/')) {
              tmpToken.data.append(character);
              if (character.character == '\n') {
                tmpToken.endLine += 1;
                endLine++;
              }
              character = getNextChar(fileData.data, character.endPos + 1, true);
            }

            if (character.endPos + 1 < fileData.data.length) {
              tmpToken.data.append("*/");
              character = getNextChar(fileData.data, character.endPos + 1, true);
            } else {
              tmpToken.data.append(character);
            }
            tmpToken.type = BasicJavaToken.BLOCK_COMMENT;
            tmpToken.endPos = character.endPos;
            return nextToken(fileData,
                addStructureTokenToHistory(previousToken, tmpTokenAndHistory));
          case '=':
            tmpToken.endPos = tmpCharacter.endPos;
            tmpToken.data.append("/=");
            tmpToken.type = BasicJavaToken.DIVIDE_EQUAL;
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
          default:
            tmpToken.type = BasicJavaToken.DIV;
            tmpToken.endPos = character.endPos;
            tmpToken.data.append("/");
            return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
      case '8':
      case '9':
        tmpToken = scanNumber(tmpToken, fileData.data,
            (previousChar == null) ? position - 1 : previousChar.endPos);

        if (tmpToken.type != null) {
          tmpTokenAndHistory = new TokenAndHistory(tmpToken);
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        break;
      case '"':
        tmpToken = scanString(tmpToken, fileData.data,
            (previousChar == null) ? position - 1 : previousChar.endPos);
        if (tmpToken != null) {
          tmpTokenAndHistory = new TokenAndHistory(tmpToken);
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        } else {
          throw new SyntaxError("Bad string format!", (JavaToken) null);

        }
      case '\'':
        tmpToken = scanChar(tmpToken, fileData.data,
            (previousChar == null) ? position - 1 : previousChar.endPos);
        if (tmpToken != null) {
          tmpTokenAndHistory = new TokenAndHistory(tmpToken);
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        break;
      default:
        return scanIdentifier(character, previousToken, fileData.data, character.endPos, endLine,
            tmpToken.whiteSpace.toString());
    }

    return null;
  }

  private JavaToken scanBinaryNumber(final JavaToken tmpToken, final byte[] fileData,
      final int position) {
    JavaToken token = new JavaToken();
    token.insideComment = tmpToken.insideComment;
    token.whiteSpace.append(tmpToken.whiteSpace);
    token.endLine = tmpToken.endLine;
    token.type = BasicJavaToken.INTEGER_LITERAL;
    CharElement previousChar = null;
    CharElement nextChar = getNextChar(fileData, position + 1, false);
    token.data.append(nextChar);
    previousChar = nextChar;
    nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
    token.data.append(nextChar);
    previousChar = nextChar;
    nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
    while ("01_".indexOf(nextChar.character) != -1) {
      token.data.append(nextChar);
      previousChar = nextChar;
      nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
    }
    if ("23456789".indexOf(nextChar.character) != -1) {
      throw new SyntaxError("Bad binary number format!", (JavaToken) null);
    }
    if (token.data.toString().endsWith("_")) {
      throw new SyntaxError("Bad binary number format!", (JavaToken) null);
    }
    token.endPos = previousChar.endPos;
    if (nextChar.character == 'L' || nextChar.character == 'l') {
      token.data.append(nextChar);
      token.endPos = nextChar.endPos;
    }
    return token;
  }

  private JavaToken scanChar(JavaToken tmpToken, byte[] fileData, int position) {
    JavaToken token = new JavaToken();
    token.insideComment = tmpToken.insideComment;
    token.whiteSpace.append(tmpToken.whiteSpace);
    token.type = tmpToken.type;
    token.endLine = tmpToken.endLine;
    CharElement previousChar = null;
    CharElement nextChar = getNextChar(fileData, position + 1, false);
    token.data.append(nextChar);
    previousChar = nextChar;
    nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
    if (nextChar.character == '\\') {
      ScanEscapeResult result = scanEscape(token, fileData, previousChar.endPos);
      if (result == null) {
        assert (false);
      }
      nextChar = result.character;
      token = result.token;
    } else {
      token.data.append(nextChar);
    }
    nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
    if (nextChar.character == '\'') {
      token.data.append(nextChar);
      token.type = BasicJavaToken.CHAR_LITERAL;
      token.endPos = nextChar.endPos;
      return token;
    } else {
      throw new SyntaxError("Empty char literal!", (JavaToken) null);
    }
  }

  private JavaToken scanDigitalNumber(final JavaToken tmpToken, final byte[] fileData,
      final int position) {
    JavaToken token = new JavaToken();
    token.insideComment = tmpToken.insideComment;
    token.whiteSpace.append(tmpToken.whiteSpace);
    token.type = BasicJavaToken.INTEGER_LITERAL;
    token.endLine = tmpToken.endLine;
    CharElement previousChar = null;
    CharElement nextChar = getNextChar(fileData, position + 1, false);
    if (nextChar.character != '.') {
      token.data.append(nextChar.character);
      previousChar = nextChar;
      nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
      while (nextChar != null && "0123456789_".indexOf(nextChar.character) != -1) {
        token.data.append(nextChar);
        previousChar = nextChar;
        nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
      }
    }
    if (nextChar != null && nextChar.character == '.') {
      token.type = BasicJavaToken.REAL_LITERAL;
      token.data.append(nextChar);
      previousChar = nextChar;
      nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
      while (nextChar != null && "0123456789".indexOf(nextChar.character) != -1) {
        token.data.append(nextChar);
        previousChar = nextChar;
        nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
      }
    }
    if (nextChar != null && nextChar.character == 'e'
        || nextChar != null && nextChar.character == 'E') {
      token.type = BasicJavaToken.REAL_LITERAL;
      token.data.append(nextChar);
      previousChar = nextChar;
      nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
      if (nextChar.character == '+' || nextChar.character == '-') {
        token.data.append(nextChar);
        previousChar = nextChar;
        nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
      }
      if ("0123456789".indexOf(nextChar.character) == -1) {
        throw new SyntaxError("Bad number format!", (JavaToken) null);
      }
      while ("0123456789".indexOf(nextChar.character) != -1) {
        token.data.append(nextChar);
        previousChar = nextChar;
        nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
      }
    }
    if (token.data.toString().endsWith("_")) {
      throw new SyntaxError("Bad number format!", (JavaToken) null);
    }
    if (nextChar != null && nextChar.character == 'f'
        || nextChar != null && nextChar.character == 'd'
        || nextChar != null && nextChar.character == 'F'
        || nextChar != null && nextChar.character == 'D') {
      token.data.append(nextChar);
      token.endPos = nextChar.endPos;
      token.type = BasicJavaToken.REAL_LITERAL;
    } else if (nextChar != null && nextChar.character == 'L'
        || nextChar != null && nextChar.character == 'l') {
      token.data.append(nextChar);
      token.endPos = nextChar.endPos;
    } else {
      token.endPos = previousChar.endPos;
    }

    return token;
  }

  private ScanEscapeResult scanEscape(final JavaToken tmpToken, byte[] fileData, int position) {
    JavaToken token = new JavaToken();
    token.insideComment = tmpToken.insideComment;
    token.whiteSpace.append(tmpToken.whiteSpace);
    token.data.append(tmpToken.data);
    token.type = tmpToken.type;
    token.endLine = tmpToken.endLine;
    CharElement nextChar = getNextChar(fileData, position + 1, false);
    token.data.append(nextChar);
    nextChar = getNextChar(fileData, nextChar.endPos + 1, true);
    token.data.append(nextChar);
    switch (nextChar.character) {
      case 'u':
        nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
        while (nextChar.character == 'u') {
          token.data.append(nextChar.character);
          nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
        }
        token.data.append(nextChar);
        nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
        token.data.append(nextChar);
        nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
        token.data.append(nextChar);
        nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
        token.data.append(nextChar);
        return new ScanEscapeResult(nextChar, token);
      case 'b':
      case 't':
      case 'n':
      case 'f':
      case 'r':
      case '"':
      case '\'':
      case '\\':
        return new ScanEscapeResult(nextChar, token);
      case '0':
      case '1':
      case '2':
      case '3':
      case '4':
      case '5':
      case '6':
      case '7':
        CharElement nextnextChar = getNextChar(fileData, nextChar.endPos + 1, false);

        if ("01234567".indexOf(nextnextChar.character) != -1) {
          token.data.append(nextnextChar);
          nextnextChar = getNextChar(fileData, nextnextChar.endPos + 1, false);
          if ("0123".indexOf(nextChar.character) != -1
              && "01234567".indexOf(nextnextChar.character) != -1) {
            token.data.append(nextnextChar);
            return new ScanEscapeResult(nextnextChar, token);
          }
          return new ScanEscapeResult(nextnextChar, token);
        }
        return new ScanEscapeResult(nextChar, token);
      default:
        return null;
    }
  }

  private JavaToken scanHexNumber(JavaToken tmpToken, byte[] fileData, final int position) {
    JavaToken token = new JavaToken();
    token.insideComment = tmpToken.insideComment;
    token.whiteSpace.append(tmpToken.whiteSpace);
    token.type = BasicJavaToken.INTEGER_LITERAL;
    token.endLine = tmpToken.endLine;
    CharElement nextChar = getNextChar(fileData, position + 1, false);
    token.data.append(nextChar);
    CharElement previousChar = nextChar;
    nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
    token.data.append(nextChar);
    previousChar = nextChar;
    nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
    if ("0123456789abcdefABCDEF.".indexOf(nextChar.character) == -1) {
      throw new SyntaxError("Bad hex format!", (JavaToken) null);
    }
    while ("0123456789abcdefABCDEF_".indexOf(nextChar.character) != -1) {
      token.data.append(nextChar);
      previousChar = nextChar;
      nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
    }
    if (token.data.toString().endsWith("_")) {
      assert (false);
    }
    if (nextChar.character == '.') {
      token.type = BasicJavaToken.REAL_LITERAL;
      token.data.append(nextChar);
      previousChar = nextChar;
      nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
      while ("0123456789abcdefABCDEF_".indexOf(nextChar.character) != -1) {
        token.data.append(nextChar);

        nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
      }
    }
    if (nextChar.character == 'p' || nextChar.character == 'P') {
      token.type = BasicJavaToken.REAL_LITERAL;
      token.data.append(nextChar);
      previousChar = nextChar;
      nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
      if (nextChar.character == '+' || nextChar.character == '-') {
        token.data.append(nextChar);
        previousChar = nextChar;
        nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
      }
      if ("0123456789".indexOf(nextChar.character) == -1) {
        assert (false);
      }

      while ("0123456789".indexOf(nextChar.character) != -1) {
        token.data.append(nextChar);
        previousChar = nextChar;
        nextChar = getNextChar(fileData, nextChar.endPos + 1, false);

      }
    }
    if (token.data.toString().endsWith("_")) {
      assert (false);
    }
    if (nextChar.character == 'f' || nextChar.character == 'd' || nextChar.character == 'F'
        || nextChar.character == 'D') {
      token.data.append(nextChar);
      token.endPos = nextChar.endPos;
      token.type = BasicJavaToken.REAL_LITERAL;
    } else if (nextChar.character == 'L' || nextChar.character == 'l') {
      token.data.append(nextChar);
      token.endPos = nextChar.endPos;
    } else {
      token.endPos = previousChar.endPos;
    }
    return token;
  }

  @SuppressWarnings("PMD.ExcessiveMethodLength")
  private TokenAndHistory scanIdentifier(CharElement character, TokenAndHistory previousToken,
      byte[] fileData, int position, int endLine, String whitespace) {
    if (!Character.isJavaIdentifierStart(character.character)) {
      throw new SyntaxError("Bad identifier start!", (JavaToken) null);
    }
    JavaToken tmpToken = new JavaToken();
    tmpToken.endLine = endLine;

    StringBuilder charBuilder = new StringBuilder();
    TokenAndHistory tmpTokenAndHistory = new TokenAndHistory(tmpToken);
    if (whitespace != null) {
      ((JavaToken) tmpTokenAndHistory.token).whiteSpace.append(whitespace);
    }
    charBuilder.append(character.character);
    tmpToken.data.append(character);
    CharElement previousChar = character;
    character = getNextChar(fileData, character.endPos + 1, false);
    while (character != null && Character.isJavaIdentifierPart(character.character)) {
      charBuilder.append(character.character);
      tmpToken.data.append(character);
      previousChar = character;
      character = getNextChar(fileData, character.endPos + 1, false);
    }
    switch (charBuilder.length()) {
      case 2:
        if (charBuilder.toString().equals("if")) {
          tmpToken.type = BasicJavaToken.IF;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("do")) {
          tmpToken.type = BasicJavaToken.DO;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        break;
      case 3:
        if (charBuilder.toString().equals("int")) {
          tmpToken.type = BasicJavaToken.INT;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("new")) {
          tmpToken.type = BasicJavaToken.NEW;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("for")) {
          tmpToken.type = BasicJavaToken.FOR;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("try")) {
          tmpToken.type = BasicJavaToken.TRY;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        break;
      case 4:
        if (charBuilder.toString().equals("this")) {
          tmpToken.type = BasicJavaToken.THIS;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("void")) {
          tmpToken.type = BasicJavaToken.VOID;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("case")) {
          tmpToken.type = BasicJavaToken.CASE;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("true")) {
          tmpToken.type = BasicJavaToken.TRUE;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("enum")) {
          tmpToken.type = BasicJavaToken.ENUM;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("else")) {
          tmpToken.type = BasicJavaToken.ELSE;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("byte")) {
          tmpToken.type = BasicJavaToken.BYTE;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("long")) {
          tmpToken.type = BasicJavaToken.LONG;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("char")) {
          tmpToken.type = BasicJavaToken.CHAR;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("null")) {
          tmpToken.type = BasicJavaToken.NULL;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        break;
      case 5:
        if (charBuilder.toString().equals("class")) {
          tmpToken.type = BasicJavaToken.CLASS;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("false")) {
          tmpToken.type = BasicJavaToken.FALSE;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("catch")) {
          tmpToken.type = BasicJavaToken.CATCH;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("break")) {
          tmpToken.type = BasicJavaToken.BREAK;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("final")) {
          tmpToken.type = BasicJavaToken.FINAL;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("short")) {
          tmpToken.type = BasicJavaToken.SHORT;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("while")) {
          tmpToken.type = BasicJavaToken.WHILE;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("throw")) {
          tmpToken.type = BasicJavaToken.THROW;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("super")) {
          tmpToken.type = BasicJavaToken.SUPER;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        break;
      case 6:
        if (charBuilder.toString().equals("public")) {
          tmpToken.type = BasicJavaToken.PUBLIC;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }

        if (charBuilder.toString().equals("static")) {
          tmpToken.type = BasicJavaToken.STATIC;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("switch")) {
          tmpToken.type = BasicJavaToken.SWITCH;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("return")) {
          tmpToken.type = BasicJavaToken.RETURN;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("assert")) {
          tmpToken.type = BasicJavaToken.ASSERT;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("import")) {
          tmpToken.type = BasicJavaToken.IMPORT;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("double")) {
          tmpToken.type = BasicJavaToken.DOUBLE;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("throws")) {
          tmpToken.type = BasicJavaToken.THROWS;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("native")) {
          tmpToken.type = BasicJavaToken.NATIVE;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        break;
      case 7:
        if (charBuilder.toString().equals("default")) {
          tmpToken.type = BasicJavaToken.DEFAULT;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("finally")) {
          tmpToken.type = BasicJavaToken.FINALLY;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("package")) {
          tmpToken.type = BasicJavaToken.PACKAGE;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("extends")) {
          tmpToken.type = BasicJavaToken.EXTENDS;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("private")) {
          tmpToken.type = BasicJavaToken.PRIVATE;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("boolean")) {
          tmpToken.type = BasicJavaToken.BOOLEAN;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        break;
      case 8:
        if (charBuilder.toString().equals("abstract")) {
          tmpToken.type = BasicJavaToken.ABSTRACT;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("continue")) {
          tmpToken.type = BasicJavaToken.CONTINUE;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("volatile")) {
          tmpToken.type = BasicJavaToken.VOLATILE;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("strictfp")) {
          tmpToken.type = BasicJavaToken.STRIcTFP;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        break;
      case 9:
        if (charBuilder.toString().equals("interface")) {
          tmpToken.type = BasicJavaToken.INTERFACE;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("protected")) {
          tmpToken.type = BasicJavaToken.PROTECTED;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("transient")) {
          tmpToken.type = BasicJavaToken.TRANSIENT;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        break;
      case 10:
        if (charBuilder.toString().equals("implements")) {
          tmpToken.type = BasicJavaToken.IMPLEMENTS;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        if (charBuilder.toString().equals("instanceof")) {
          tmpToken.type = BasicJavaToken.INSTANCEOF;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        break;
      case 12:
        if (charBuilder.toString().equals("synchronized")) {
          tmpToken.type = BasicJavaToken.SYNCHRONIZED;
          tmpToken.endPos = previousChar.endPos;
          return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
        }
        break;
      default:
        break;
    }
    tmpToken.type = BasicJavaToken.IDENTIFIER;
    tmpToken.endPos = previousChar.endPos;
    return addStructureTokenToHistory(previousToken, tmpTokenAndHistory);
  }

  private JavaToken scanNumber(JavaToken tmpToken, byte[] fileData, int position) {
    CharElement character = getNextChar(fileData, position + 1, false);
    if (character.character == '0') {
      CharElement nextChar = getNextChar(fileData, character.endPos + 1, false);
      if (nextChar != null) {
        switch (nextChar.character) {
          case 'x':
          case 'X':
            return scanHexNumber(tmpToken, fileData, position);
          case 'b':
          case 'B':
            return scanBinaryNumber(tmpToken, fileData, position);
          case '0':
          case '1':
          case '2':
          case '3':
          case '4':
          case '5':
          case '6':
          case '7':
          case '8':
          case '9':
            return scanDigitalNumber(tmpToken, fileData, position);
          default:
            break;
        }
      } else {
        return scanDigitalNumber(tmpToken, fileData, position);
      }
    }
    return scanDigitalNumber(tmpToken, fileData, position);
  }

  private JavaToken scanString(JavaToken tmpToken, byte[] fileData, int position) {
    JavaToken token = new JavaToken();
    token.endLine = tmpToken.endLine;
    token.insideComment = tmpToken.insideComment;
    token.whiteSpace.append(tmpToken.whiteSpace);
    token.type = tmpToken.type;
    CharElement previousChar = null;
    CharElement nextChar = getNextChar(fileData, position + 1, false);
    token.data.append(nextChar);
    previousChar = nextChar;
    nextChar = getNextChar(fileData, nextChar.endPos + 1, false);
    while (nextChar.character != '"' && nextChar.character != '\r' && nextChar.character != '\n') {
      if (nextChar.character == '\\') {
        ScanEscapeResult result = scanEscape(token, fileData, previousChar.endPos);
        if (result == null) {
          throw new SyntaxError("Bad character!", (JavaToken) null);
        }
        nextChar = result.character;
        token = result.token;

      } else {
        token.data.append(nextChar);
      }
      previousChar = nextChar;
      nextChar = getNextChar(fileData, nextChar.endPos + 1, false);

    }
    if (nextChar.character == '"') {
      token.data.append(nextChar);
      token.type = BasicJavaToken.STRING_LITERAL;
      token.endPos = nextChar.endPos;
      return token;
    }
    return null;
  }

}
