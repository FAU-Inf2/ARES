package de.fau.cs.inf2.cas.common.vcs.visitors;

import de.fau.cs.inf2.cas.common.bast.general.BastInfo;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.visitors.DefaultFieldVisitor;

import de.fau.cs.inf2.cas.common.parser.IGeneralToken;
import de.fau.cs.inf2.cas.common.parser.odin.BasicJavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.JavaToken;
import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import java.util.Iterator;

public class RemoveCommentsVisitor extends DefaultFieldVisitor {

  
  /**
   * Begin visit.
   *
   * @param node the node
   */
  @Override
  public void beginVisit(AbstractBastNode node) {
    BastInfo info = node.info;
    TokenAndHistory[] tokens = info.tokens;
    if (tokens != null) {
      for (int i = 0; i < tokens.length; i++) {
        if (tokens[i] != null) {
          Iterator<IGeneralToken> it = tokens[i].prevTokens.iterator();
          while (it.hasNext()) {
            JavaToken token = (JavaToken) it.next();
            if (token.type == BasicJavaToken.BLOCK_COMMENT) {
              token.data = new StringBuilder();
            }
          }
        }
      }
    }

  }

  
  /**
   * End visit.
   *
   * @param node the node
   */
  @Override
  public void endVisit(AbstractBastNode node) {

  }
}
