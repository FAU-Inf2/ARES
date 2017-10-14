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

package de.fau.cs.inf2.cas.ares.recommendation.extension;

import de.fau.cs.inf2.cas.ares.bast.general.AresConfiguration;
import de.fau.cs.inf2.cas.ares.io.AresMeasurement;
import de.fau.cs.inf2.cas.ares.recommendation.ExtendedAresPattern;
import de.fau.cs.inf2.cas.ares.recommendation.visitors.TemplateExtractorVisitor;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastNameIdent;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;
import de.fau.cs.inf2.mtdiff.TreeDifferencer;

import java.util.HashSet;
import java.util.concurrent.ExecutorService;

public class ExtendedTemplateExtractor {
  private static final int NUM_THREADS = 8;
  private static final double INNER_NODE_THRESHOLD = 0.099f;
  private static final double WEIGHT_INNER_CHILDREN_SIM = 0.25f;
  private static final double WEIGHT_INNER_VALUE_SIM = 0.40f;
  private static final double WEIGHT_POSITION = 0.0024f;
  private static final double WEIGHT_SIMILARITY = 0.37f;
  private static final float LEAF_THRESHOLD = (float) 0.88f;
  private static final double INNER_NODE_NON_EQUAL_SIMILARITY = 0.2f;

  /**
   * Pipeline.
   *
   * @param root1 the root1
   * @param root2 the root2
   * @param executor the executor
   * @return the extended diff result
   */
  public static ExtendedDiffResult pipeline(String filename, final AbstractBastNode root1,
      final AbstractBastNode root2, ExecutorService executor) {
    return pipeline(root1, root2,
        executor);
  }

  /**
   * Pipeline.
   *
   * @param root1 the root1
   * @param root2 the root2
   * @param executor the executor
   * @return the extended diff result
   */
  public static ExtendedDiffResult pipeline(final AbstractBastNode root1,
      final AbstractBastNode root2, ExecutorService executor) {

    TreeDifferencer treeDif = null;

    if (executor != null) {
      treeDif = new TreeDifferencer(executor, LEAF_THRESHOLD);
    } else {
      treeDif = new TreeDifferencer(LEAF_THRESHOLD, NUM_THREADS);
    }

    ExtendedDiffResult exDiff = null;
    try {
      exDiff = treeDif.computeDifference(null,
          new AresConfiguration(root1, root2, LEAF_THRESHOLD,
              WEIGHT_SIMILARITY,
              WEIGHT_POSITION,
              WEIGHT_INNER_VALUE_SIM,
              WEIGHT_INNER_CHILDREN_SIM,
              INNER_NODE_THRESHOLD,
              INNER_NODE_NON_EQUAL_SIMILARITY,
              NUM_THREADS));
      if (executor == null) {
        treeDif.shutdown();
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      if (executor == null) {
        treeDif.shutdown();
      }
    }
    return exDiff;
  }

  /**
   * Extract template.
   *
   * @param oldAst the old ast
   * @param newAst the new ast
   * @return the extended ARES template
   */
  public static ExtendedAresPattern extractTemplate(AbstractBastNode oldAst,
      AbstractBastNode newAst, ExecutorService executor,
      AresMeasurement recommendationMeasurement) {
    TemplateExtractorVisitor extractorVisitor1 = new TemplateExtractorVisitor();
    oldAst.accept(extractorVisitor1);
    TemplateExtractorVisitor extractorVisitor2 = new TemplateExtractorVisitor();

    newAst.accept(extractorVisitor2);

    ExtendedAresPattern modifiedTemplate = extractorVisitor2.currentTemplate;
    ExtendedAresPattern originalTempl = extractorVisitor1.currentTemplate;
    assert (modifiedTemplate != null);
    assert (modifiedTemplate.originalAst != null);
    assert (originalTempl != null);
    assert (originalTempl.originalAst != null);
    assert (modifiedTemplate.originalAst.number == originalTempl.originalAst.number);
    long time = System.nanoTime();
    ExtendedDiffResult diffResult =
        pipeline(originalTempl.originalAst, modifiedTemplate.originalAst, executor);
    if (recommendationMeasurement != null) {
      recommendationMeasurement.timeTreeDifferencing.add(System.nanoTime() - time);
    }
    originalTempl.modifiedAst = modifiedTemplate.originalAst;
    originalTempl.setDiffResult(diffResult);
    HashSet<String> identifierNamesToIgnore = new HashSet<>();
    if (originalTempl.originalAst.identifiers != null) {
      for (AbstractBastNode expr : originalTempl.originalAst.identifiers) {
        assert (expr.getTag() == BastNameIdent.TAG);
        identifierNamesToIgnore.add(((BastNameIdent) expr).name);
      }
    }
    originalTempl.setIdentifierNamesToIgnore(identifierNamesToIgnore);
    return originalTempl;
  }
}
