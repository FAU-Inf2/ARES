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

import de.fau.cs.inf2.cas.common.util.Token.TokenType;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;

public final class MiniLexer implements Iterable<Token> {
  private final BufferedReader reader;

  /**
   * Instantiates a new mini lexer.
   *
   * @param reader the reader
   */
  public MiniLexer(final BufferedReader reader) {
    this.reader = reader;
  }

  private final class TokenIterator implements Iterator<Token> {
    private final BufferedReader reader;

    public TokenIterator(final BufferedReader reader) {
      this.reader = reader;
    }

    public final boolean hasNext() {
      try {
        return (this.reader != null) && (this.reader.ready());
      } catch (final IOException excecption) {
        return false;
      }
    }

    private final int read() {
      try {
        return this.reader.read();
      } catch (final IOException exception) {
        return -2;
      }
    }

    private final void mark() {
      try {
        this.reader.mark(2);
      } catch (final IOException exception) {
        /* intentionally left blank */
      }
    }

    private final void reset() {
      try {
        this.reader.reset();
      } catch (final IOException exception) {
        /* intentionally left blank */
      }
    }

    public final Token next() {
      while (true) {
        int read = read();
        {
          if (read == -1) {
            return new Token(Token.TokenType.TOKEN_EOF, "");
          }
          if (read == -2) {
            return new Token(Token.TokenType.TOKEN_UNKNOWN, "");
          }
        }

        char character = (char) (read);

        if (character == '.') {
          return new Token(Token.TokenType.TOKEN_DOT, ".");
        } else if (character == ';') {
          return new Token(Token.TokenType.TOKEN_SEMIcOLON, ";");
        } else if (Character.isJavaIdentifierStart(character)) {
          final StringBuilder builder = new StringBuilder();
          builder.append(character);

          mark();

          while (true) {
            read = read();

            if (read < 0 || !Character.isJavaIdentifierPart((char) read)) {
              reset();
              break;
            }

            builder.append((char) read);
            mark();
          }

          final String string = builder.toString();
          final Token.TokenType tokenType;
          {
            if (string.equals("package")) {
              tokenType = Token.TokenType.TOKEN_PACKAGE;
            } else {
              tokenType = Token.TokenType.TOKEN_IDENTIFIER;
            }
          }

          return new Token(tokenType, string);
        } else if (Character.isWhitespace(character)) {
          mark();

          while (true) {
            read = read();

            if (read < 0 || !Character.isWhitespace((char) read)) {
              reset();
              break;
            }

            mark();
          }
        } else if (character == '/') {
          read = read();

          if (read < 0) {
            return new Token(Token.TokenType.TOKEN_UNKNOWN, character + "");
          }

          mark();

          char secondCharacter = (char) read;

          if (secondCharacter == '/') {
            mark();

            while (true) {
              read = read();

              if (read < 0 || ((char) read) == '\n') {
                reset();
                break;
              }

              mark();
            }
          } else if (secondCharacter == '*') {
            mark();

            boolean foundClosingStar = false;

            while (true) {
              read = read();

              if (foundClosingStar) {
                if (read < 0 || ((char) read) == '/') {
                  break;
                }

                foundClosingStar = false;
              } else {
                if (read < 0) {
                  break;
                }

                if ((char) read == '*') {
                  foundClosingStar = true;
                }
              }

              mark();
            }
          } else {
            reset();
            return new Token(Token.TokenType.TOKEN_UNKNOWN, character + "");
          }
        } else {
          return new Token(Token.TokenType.TOKEN_UNKNOWN, character + "");
        }
      }
    }

    public final void remove() {
      throw new UnsupportedOperationException();
    }
  }

  
  /**
   * Iterator.
   *
   * @return the iterator
   */
  public final Iterator<Token> iterator() {
    return new TokenIterator(this.reader);
  }
}
