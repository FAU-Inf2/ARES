package de.fau.cs.inf2.cas.ares.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import de.fau.cs.inf2.cas.ares.bast.general.ParserFactory;
import de.fau.cs.inf2.cas.ares.bast.nodes.AresBlock;
import de.fau.cs.inf2.cas.ares.recommendation.AnalysePath;
import de.fau.cs.inf2.cas.ares.recommendation.ExtendedAresPattern;
import de.fau.cs.inf2.cas.ares.recommendation.Recommendation;
import de.fau.cs.inf2.cas.ares.recommendation.RecommendationCreator;
import de.fau.cs.inf2.cas.ares.recommendation.extension.ExtendedTemplateExtractor;

import de.fau.cs.inf2.cas.common.bast.general.BastField;
import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;
import de.fau.cs.inf2.cas.common.bast.general.CreateJavaNodeHelper;
import de.fau.cs.inf2.cas.common.bast.general.TagConstants;
import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastStatement;
import de.fau.cs.inf2.cas.common.bast.nodes.BastBlock;
import de.fau.cs.inf2.cas.common.bast.nodes.BastClassDecl;
import de.fau.cs.inf2.cas.common.bast.nodes.BastFunction;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;
import de.fau.cs.inf2.cas.common.bast.visitors.FindNodesFromTagVisitor;
import de.fau.cs.inf2.cas.common.bast.visitors.IPrettyPrinter;

import de.fau.cs.inf2.cas.common.parser.AresExtension;
import de.fau.cs.inf2.cas.common.parser.IParser;
import de.fau.cs.inf2.cas.common.parser.ParserType;
import de.fau.cs.inf2.cas.common.parser.odin.TokenAndHistory;

import de.fau.cs.inf2.cas.common.util.string.LevenshteinDistance;

import de.fau.cs.inf2.cas.common.tokenmatching.LevenshteinTokenDistance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.LinkedList;

public class AresSimpleDefinition {

  /**
   * Run simple recommendation.
   *
   * @param filename the filename
   * @return the double[]
   */
  public static double[] runSimpleRecommendation(String filename) {
    File inputDirectory = new File("tests/ares/recommendation/");
    File seq = new File(inputDirectory + "/before/" + filename
        + ".java");
    File par = new File(inputDirectory + "/after/" + filename
        + ".java");
    File test = new File(inputDirectory + "/before/" + filename
        + ".java");
    return runSimpleRecommendation(seq, par, test);
  }

  public static double[] runSimpleRecommendation(File seq, File par, File test) {
    return runSimpleRecommendation(seq, par, test, par, false);
  }

  
  public static double[] runSimpleRecommendation(File seq, File par, File test, File result) {
    return runSimpleRecommendation(seq, par, test, result, false);
  }

  /**
   * Run simple recommendation.
   *
   * @param seq the seq
   * @param par the par
   * @param test the test
   * @param verbose the verbose
   * @return the double[]
   */
  public static double[] runSimpleRecommendation(File seq, File par, File test,
      File result, boolean verbose) {


    BastProgram beforeProg = RecommendationCreator.getAst(seq, ParserType.JAVA_PARSER);

    BastProgram afterProg = RecommendationCreator.getAst(par, ParserType.JAVA_PARSER);



    BastBlock blockBefore = (BastBlock) ((BastFunction) ((BastClassDecl) beforeProg.functionBlocks
        .getFirst()).declarations.getFirst()).statements.getFirst();
    BastBlock blockAfter = (BastBlock) ((BastFunction) ((BastClassDecl) afterProg.functionBlocks
        .getFirst()).declarations.getFirst()).statements.getFirst();


    TokenAndHistory tokenBefore = null;
    if (blockBefore.statements != null && blockBefore.statements.size() > 0
        && blockBefore.statements.getFirst().info != null
        && blockBefore.statements.getFirst().info.tokens != null
        && blockBefore.statements.getFirst().info.tokens[0].token
            .getTag() == TagConstants.JAVA_TOKEN) {
      tokenBefore = blockBefore.statements.getFirst().info.tokens[0];
    }
    TokenAndHistory tokenAfter = null;
    if (blockAfter.statements != null && blockAfter.statements.size() > 0
        && blockAfter.statements.getFirst().info != null
        && blockAfter.statements.getFirst().info.tokens != null
        && blockAfter.statements.getFirst().info.tokens[0].token
            .getTag() == TagConstants.JAVA_TOKEN) {
      tokenAfter = blockAfter.statements.getFirst().info.tokens[0];
    }
    FindNodesFromTagVisitor fnft = new FindNodesFromTagVisitor(AresBlock.TAG);
    blockBefore.accept(fnft);
    if (fnft.nodes.size() == 0) {
      AresBlock aresBlockBefore =
          CreateJavaNodeHelper.createAresBlock(tokenBefore, 0, true, blockBefore.statements, null);
      AresBlock aresBlockAfter =
          CreateJavaNodeHelper.createAresBlock(tokenAfter, 0, false, blockAfter.statements, null);
      LinkedList<AbstractBastStatement> listBefore = new LinkedList<>();
      listBefore.add(aresBlockBefore);
      LinkedList<AbstractBastStatement> listAfter = new LinkedList<>();
      listAfter.add(aresBlockAfter);
      blockBefore.replaceField(BastFieldConstants.BLOCK_STATEMENT, new BastField(listBefore));
      blockAfter.replaceField(BastFieldConstants.BLOCK_STATEMENT, new BastField(listAfter));
    }

    BastProgram testP = AnalysePath.analyse(test, ParserType.JAVA_PARSER);
    IPrettyPrinter printer = ParserFactory.getPrettyPrinter();
    testP.accept(printer);
    final byte[] testBytes = (printer.getBuffer().toString().getBytes());

    IPrettyPrinter debugPrinter = ParserFactory.getAresPrettyPrinter();
    beforeProg.accept(debugPrinter);
    final byte[] seqBytes = (debugPrinter.getBuffer().toString().getBytes());
    if (verbose) {
      System.out.println(debugPrinter.getBuffer().toString());
    }
    debugPrinter.getBuffer().setLength(0);
    afterProg.accept(debugPrinter);
    if (verbose) {
      System.out.println(debugPrinter.getBuffer().toString());
    }
    byte[] parBytes = (debugPrinter.getBuffer().toString().getBytes());
    
    BastProgram seqProg =  AnalysePath.analyse(seq, ParserType.JAVA_PARSER);
    BastProgram parProg =  AnalysePath.analyse(par, ParserType.JAVA_PARSER);
    ExtendedAresPattern template =
        ExtendedTemplateExtractor.extractTemplate(seqProg, parProg, null, null);
    template.resolveWildcards();

    ArrayList<ArrayList<Recommendation>> testProgs 
        = RecommendationCreator.createTransformedProgram(
        test.toString(), seqBytes, parBytes, testBytes, verbose, null, null,
        template);
    if (testProgs == null) {
      return new double[] { 1.0, 1.0 };
    }
    IPrettyPrinter print = ParserFactory.getPrettyPrinter();
    double simToken = 0;
    double simString = 0;
    if (result == par) {
      afterProg = RecommendationCreator.getAst(result, ParserType.JAVA_PARSER);
    } else {
      afterProg = null;
    }
    debugPrinter.getBuffer().setLength(0);
    BastBlock afterBlock = null;
    if (afterProg != null && afterProg.functionBlocks != null) {
      afterBlock = (BastBlock) ((BastFunction) ((BastClassDecl) afterProg.functionBlocks
          .getFirst()).declarations.getFirst()).statements.getFirst();
      afterBlock.accept(debugPrinter);
    }

    int counter = 0;
    StringBuffer resultBuffer = new StringBuffer();
    for (ArrayList<Recommendation> testProg : testProgs) {
      for (Recommendation res : testProg) {
        resultBuffer.append("RECOMMENDATION " + counter + "\n\n");
        print = ParserFactory.getAresPrettyPrinter();
        // res.resultAst.accept(print);
        BastBlock resultBlock =
            (BastBlock) ((BastFunction) ((BastClassDecl) 
                ((BastProgram) res.resultAst).functionBlocks
                .getFirst()).declarations.getFirst()).statements.getFirst();
        resultBlock.accept(print);
        IPrettyPrinter testPrinter = ParserFactory.getAresPrettyPrinter();
        res.resultAst.accept(testPrinter);
        String tmp = testPrinter.getBuffer().toString();
        if (verbose) {
          System.out.println(tmp);
        }
        if (verbose) {
          assertEquals(debugPrinter.getBuffer().toString(), print.getBuffer().toString());
        }
        IParser parser = ParserFactory.getParserInstance(AresExtension.WITH_ARES_EXTENSIONS);
        //BastProgram program = parser.parse(tmp.getBytes());
        //assertNotNull(program);
        if (afterProg != null && afterProg.functionBlocks != null) {
          double tmpToken = LevenshteinTokenDistance.match(afterBlock, resultBlock);
          double tmpString = LevenshteinDistance.levenshteinMatch(afterBlock, resultBlock);
          simToken = Math.max(simToken, tmpToken);
          simString = Math.max(simString, tmpString);
        }
        counter++;
        IPrettyPrinter resultPrinter = ParserFactory.getAresPrettyPrinter();
        res.resultAst.accept(resultPrinter);
        resultBuffer.append(resultPrinter.getBuffer());
        resultBuffer.append("\n\n");
      }
    }
    if (result == null) {
      System.out.println(resultBuffer.toString());
    } else {
      if (!par.getAbsolutePath().equals(result.getAbsolutePath())) {
        print(new File(result.getAbsoluteFile() 
            + ".pretty"), resultBuffer.toString());
      }
    }
    // System.out.println("Token Distance: " + simToken);
    // System.out.println("String Distance: " + simString);
    double[] sims = new double[] { simToken, simString };
    return sims;
  }
  
  private static void print(File file, String string) {
    try {
      Writer output = new BufferedWriter(new FileWriter(file));
      // Writer output2 = new BufferedWriter(new FileWriter(typeResult));
      try {
        // FileWriter always assumes default encoding is OK!
        output.write(string);
        // output2.write( t.buffer.toString() );
      } catch (Exception e) {
        System.err.println("filename: " + file.getAbsolutePath());
        e.printStackTrace();
      } finally {

        output.close();
        // output2.close();
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }
}
