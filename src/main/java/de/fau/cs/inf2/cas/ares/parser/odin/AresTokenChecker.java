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
