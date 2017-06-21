package de.fau.cs.inf2.cas.common.parser.odin;

import java.util.List;

public class ParseResult<T> {
  public final T value;
  public final TokenAndHistory currentTokenAndHistory;
  public final List<TokenAndHistory> additionalTokens;

  /**
   * Instantiates a new parses the result.
   *
   * @param value the value
   * @param currentTokenAndHistory the current token and history
   */
  public ParseResult(T value, TokenAndHistory currentTokenAndHistory) {
    this.value = value;
    this.currentTokenAndHistory = currentTokenAndHistory;
    this.additionalTokens = null;
  }

  ParseResult(T value, TokenAndHistory currentTokenAndHistory,
      List<TokenAndHistory> additionalTokens) {
    this.value = value;
    this.currentTokenAndHistory = currentTokenAndHistory;
    this.additionalTokens = additionalTokens;
  }
}
