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
