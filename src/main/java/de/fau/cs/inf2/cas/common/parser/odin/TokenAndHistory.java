package de.fau.cs.inf2.cas.common.parser.odin;

import de.fau.cs.inf2.cas.common.parser.IGeneralToken;

import java.util.LinkedList;

public class TokenAndHistory implements Cloneable {
  public final IGeneralToken token;
  public final LinkedList<IGeneralToken> prevTokens = new LinkedList<IGeneralToken>();
  public final LinkedList<IGeneralToken> followingTokens = new LinkedList<IGeneralToken>();
  final boolean flushed;

  /**
   * Instantiates a new token and history.
   *
   * @param token the token
   */
  public TokenAndHistory(IGeneralToken token) {
    this.token = token;
    this.flushed = false;
  }

  private TokenAndHistory(IGeneralToken token, boolean flushed) {
    this.token = token;
    this.flushed = flushed;
  }

  /**
   * Clone.
   *
   * @return the token and history
   */
  public TokenAndHistory clone() {
    TokenAndHistory tah = null;
    if (this.token != null) {
      tah = new TokenAndHistory(this.token.clone());
    } else {
      tah = new TokenAndHistory(null);
    }

    for (IGeneralToken t : prevTokens) {
      tah.prevTokens.add(t.clone());
    }
    for (IGeneralToken t : followingTokens) {
      tah.followingTokens.add(t.clone());
    }
    return tah;
  }

  
  /**
   * Sets the flushed.
   *
   * @return the token and history
   */
  public TokenAndHistory setFlushed() {
    TokenAndHistory tah = null;
    if (this.token != null) {
      tah = new TokenAndHistory(this.token.clone(), true);
    } else {
      tah = new TokenAndHistory(null, true);
    }

    for (IGeneralToken t : prevTokens) {
      tah.prevTokens.add(t.clone());
    }
    for (IGeneralToken t : followingTokens) {
      tah.followingTokens.add(t.clone());
    }
    return tah;
  }

  
  /**
   * To string.
   *
   * @return the string
   */
  public String toString() {
    String tmp = "";
    for (IGeneralToken t : prevTokens) {
      if (t != null) {
        tmp += t.getText();
      }
    }
    if (token != null) {
      if (token.getTag() != ListToken.TAG) {
        tmp += token.getText();
      }
    }
    for (IGeneralToken t : followingTokens) {
      if (t != null) {
        tmp += t.getText();
      }
    }
    return tmp;
  }

}
