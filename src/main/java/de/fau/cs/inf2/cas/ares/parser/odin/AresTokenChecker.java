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

package de.fau.cs.inf2.cas.ares.parser.odin;

import de.fau.cs.inf2.cas.common.parser.SyntaxError;
import de.fau.cs.inf2.cas.common.parser.odin.BasicJavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.JavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

public class AresTokenChecker {

  static void expectAresToken(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.ARES_TOKEN) {
      throw new SyntaxError("//# expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectChoice(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.CHOICE) {
      throw new SyntaxError("CHOICE expected.", ((JavaToken) nextToken.token));
    }
  }

  static void expectMatch(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.MATCH) {
      throw new SyntaxError("MATCH expected.", ((JavaToken) nextToken.token));
    }
  }
  
  static void expectOriginalOrModified(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.ORIGINAL
        && ((JavaToken) nextToken.token).type != BasicJavaToken.MODIFIED) {
      throw new SyntaxError("Original or modified expected.", ((JavaToken) nextToken.token));
    }
  }
  
  static void expectUse(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.USE) {
      throw new SyntaxError("USE expected.", ((JavaToken) nextToken.token));
    }
  }
  
  static void expectWildcard(TokenAndHistory nextToken) {
    if (((JavaToken) nextToken.token).type != BasicJavaToken.WILDCARD) {
      throw new SyntaxError("WILDCARD expected.", ((JavaToken) nextToken.token));
    }
  }
  
  static boolean isNotPattern(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type != BasicJavaToken.PATTERN;
  }

  static boolean isNotPlugin(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type != BasicJavaToken.PLUGIN;
  }

  static boolean isPattern(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.PATTERN;
  }

  static boolean isPlugin(TokenAndHistory nextToken) {
    return ((JavaToken) nextToken.token).type == BasicJavaToken.PLUGIN;
  }
  
}
