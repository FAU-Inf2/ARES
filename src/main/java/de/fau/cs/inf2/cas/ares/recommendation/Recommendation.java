package de.fau.cs.inf2.cas.ares.recommendation;

import de.fau.cs.inf2.cas.ares.bast.general.ParserFactory;

import de.fau.cs.inf2.cas.common.bast.general.CreateJavaNodeHelper;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.visitors.IPrettyPrinter;

import de.fau.cs.inf2.cas.common.parser.AresExtension;
import de.fau.cs.inf2.cas.common.parser.IParser;

import java.nio.charset.Charset;

public class Recommendation implements Cloneable {
  public final AbstractBastNode resultAst;
  public final AbstractBastNode method;
  public final int methodNumber;
  
  /**
   * Instantiates a new recommendation result.
   *
   * @param resultAst the result ast
   * @param method the method
   * @param methodNumber the method number
   */
  public Recommendation(AbstractBastNode resultAst, AbstractBastNode method,
      int methodNumber) {
    this.resultAst = resultAst;
    this.method = method;
    this.methodNumber = methodNumber;
  }

  @Override
  protected Recommendation clone() {
    IPrettyPrinter printer = ParserFactory.getAresPrettyPrinter();
    resultAst.accept(printer);
    byte[] ast = printer.getBuffer().toString().getBytes(Charset.forName("UTF-8"));
    IParser parser = ParserFactory.getParserInstance(AresExtension.WITH_ARES_EXTENSIONS);
    AbstractBastNode resultAstClone = parser.parse(ast);
    AbstractBastNode resultMethod = CreateJavaNodeHelper.cloneTree(method);
    Recommendation result = new Recommendation(resultAstClone, resultMethod,
        methodNumber);
    return result;

  }

}
