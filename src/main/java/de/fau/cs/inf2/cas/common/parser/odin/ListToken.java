package de.fau.cs.inf2.cas.common.parser.odin;

import de.fau.cs.inf2.cas.common.bast.general.TagConstants;

import de.fau.cs.inf2.cas.common.parser.IGeneralToken;
import de.fau.cs.inf2.cas.common.parser.TokenPosition;

import java.util.ArrayList;
import java.util.List;

public class ListToken implements IGeneralToken {
  public static final int TAG = TagConstants.LIST_TOKEN;
  public List<TokenAndHistory> tokenList = new ArrayList<>();

  /**
   * Instantiates a new list token.
   */
  public ListToken() {

  }

  /**
   * Instantiates a new list token.
   *
   * @param tokenList the token list
   */
  public ListToken(List<TokenAndHistory> tokenList) {
    this.tokenList = tokenList;
  }

  
  /**
   * Clone.
   *
   * @return the i general token
   */
  public IGeneralToken clone() {
    if (tokenList == null) {
      return new ListToken(null);
    }
    List<TokenAndHistory> cloneList = new ArrayList<TokenAndHistory>();
    for (TokenAndHistory token : tokenList) {
      TokenAndHistory clone = token.clone();
      cloneList.add(clone);
    }
    return new ListToken(cloneList);
  }

  
  /**
   * Gets the code id.
   *
   * @return the code id
   */
  @Override
  public int getCodeId() {
    assert (false);
    return 0;
  }

  
  /**
   * Gets the pos.
   *
   * @return the pos
   */
  @Override
  public TokenPosition getPos() {
    assert (false);
    return null;
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
   * Gets the text.
   *
   * @return the text
   */
  @Override
  public String getText() {
    assert (false);
    return null;
  }

}
