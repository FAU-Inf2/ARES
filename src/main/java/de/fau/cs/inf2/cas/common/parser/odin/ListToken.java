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
