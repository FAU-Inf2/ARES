package de.fau.cs.inf2.cas.common.bast.general;

import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.LinkedList;

public class BastInfo {

  public TokenAndHistory[] tokens;
  public LinkedList<TokenAndHistory> tokensBefore = new LinkedList<>();
  public LinkedList<TokenAndHistory> tokensAfter = new LinkedList<>();

  /**
   * Instantiates a new bast info.
   *
   * @param tokens the tokens
   */
  public BastInfo(TokenAndHistory[] tokens) {
    this.tokens = tokens;
  }

  /**
   * Gets the token clone.
   *
   * @return the token clone
   */
  public TokenAndHistory[] getTokenClone() {
    if (tokens == null) {
      return null;
    }
    TokenAndHistory[] clonedTokens = new TokenAndHistory[tokens.length];
    for (int i = 0; i < tokens.length; i++) {
      if (tokens[i] != null) {
        clonedTokens[i] = tokens[i].clone();
      } else {
        clonedTokens[i] = null;
      }
    }
    return clonedTokens;
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    String tmp = "";
    if (tokens != null) {
      for (int i = 0; i < tokens.length; i++) {
        if (tokens[i] != null) {
          tmp += tokens[i].toString();
        }
      }
    }
    return tmp;
  }
}
