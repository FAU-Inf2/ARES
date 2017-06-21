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

package de.fau.cs.inf2.cas.ares.pcreation;

import de.fau.cs.inf2.cas.ares.bast.general.ParserFactory;
import de.fau.cs.inf2.cas.ares.io.AresMeasurement;
import de.fau.cs.inf2.cas.ares.pcreation.exception.GeneralizationException;
import de.fau.cs.inf2.cas.ares.recommendation.extension.ExtendedTemplateExtractor;

import de.fau.cs.inf2.cas.common.bast.nodes.AbstractBastNode;
import de.fau.cs.inf2.cas.common.bast.nodes.BastProgram;

import de.fau.cs.inf2.cas.common.bast.visitors.IPrettyPrinter;

import de.fau.cs.inf2.cas.common.parser.AresExtension;

import de.fau.cs.inf2.mtdiff.ExtendedDiffResult;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The test compares the output of a pretty printer with the actual file.
 * 
 * 
 */
public class GeneralizeGroup {

  private static final int NUM_THREADS = 8;

  private static final String EDIT_SCRIPT_EXTENSION = ".java";

  public static final boolean DEBUG = false;
  public static String patternFilename = "Pattern";
  public static String changeFilename = "Change";
  public static String originalPartName = "original";
  public static String modifiedPartName = "modified";


  @SuppressWarnings("unused")
  private String path = null;
  private String outputPath = null;
  private String filteredPath = null;

  public String generalizedPatternOriginalRef;
  public String generalizedPatternModifiedRef;
  public String generalizedPatternOriginalRes;
  public String generalizedPatternModifiedRes;
  private boolean reevaluateOrder = false;

  public boolean failed = false;
  public ReferenceConfiguration writeReference = ReferenceConfiguration.KEEP_REFERENCE;
  private final BastProgram[] progsOriginal;
  private final BastProgram[] progsModified;
  private AresMeasurement measurement;

  public enum ReferenceConfiguration {
    KEEP_REFERENCE, WRITE_REFERENCE
  }

  /**
   * Instantiates a new generalize group.
   *
   * @param path the path
   * @param reevaluateOrder the reevaluate order
   */
  public GeneralizeGroup(String path, BastProgram[] progsOriginal, BastProgram[] progsModified,
      boolean reevaluateOrder, AresMeasurement measurement, ReferenceConfiguration writeReference) {
    this(path, path, progsOriginal, progsModified, reevaluateOrder, false, measurement,
        writeReference);
  }

  /**
   * Instantiates a new generalize group.
   *
   * @param path the path
   * @param reevaluateOrder the reevaluate order
   */
  public GeneralizeGroup(String path, BastProgram[] progsOriginal, BastProgram[] progsModified,
      boolean reevaluateOrder, AresMeasurement measurement) {
    this(path, path, progsOriginal, progsModified, reevaluateOrder, false, measurement);
  }

  public GeneralizeGroup(String path, BastProgram[] progsOriginal, BastProgram[] progsModified,
      boolean reevaluateOrder, boolean filteredPath, AresMeasurement measurement) {
    this(path, path, progsOriginal, progsModified, reevaluateOrder, filteredPath,
        ReferenceConfiguration.KEEP_REFERENCE, measurement);
  }

  GeneralizeGroup(String path, String outputPath, BastProgram[] progsOriginal,
      BastProgram[] progsModified, boolean reevaluateOrder, boolean filteredPath,
      AresMeasurement measurement) {
    this(path, outputPath, progsOriginal, progsModified, reevaluateOrder, filteredPath,
        ReferenceConfiguration.KEEP_REFERENCE, measurement);
  }

  GeneralizeGroup(String path, String outputPath, BastProgram[] progsOriginal,
      BastProgram[] progsModified, boolean reevaluateOrder, boolean filteredPath,
      AresMeasurement measurement, ReferenceConfiguration writeReference) {
    this(path, outputPath, progsOriginal, progsModified, reevaluateOrder, filteredPath,
        writeReference, measurement);
  }

  public GeneralizeGroup(String path, BastProgram[] progsOriginal, BastProgram[] progsModified,
      boolean reevaluateOrder, boolean filteredPath, ReferenceConfiguration writeReference,
      AresMeasurement measurement) {
    this(path, path, progsOriginal, progsModified, reevaluateOrder, filteredPath, writeReference,
        measurement);
  }

  GeneralizeGroup(String path, String outputPath, BastProgram[] progsOriginal,
      BastProgram[] progsModified, boolean reevaluateOrder, boolean filteredPath,
      ReferenceConfiguration writeReference, AresMeasurement measurement) {
    this.path = path;
    this.outputPath = outputPath;
    this.progsOriginal = progsOriginal;
    this.progsModified = progsModified;
    this.reevaluateOrder = reevaluateOrder;
    if (filteredPath) {
      this.filteredPath = path + "/filtered/";
    }
    this.writeReference = writeReference;
    this.measurement = measurement;
  }

  /**
   * Instantiates a new generalize group.
   *
   * @param path the path
   * @param patternsToUse the patterns to use
   */
  public GeneralizeGroup(String path, int[] patternsToUse) {
    this(path, path, false, patternsToUse, false, null);
  }

  /**
   * Instantiates a new generalize group.
   *
   * @param path the path
   * @param patternsToUse the patterns to use
   */
  public GeneralizeGroup(String path, String outputPath, int[] patternsToUse,
      AresMeasurement measurement) {
    this(path, outputPath, false, patternsToUse, false, measurement);
  }

  /**
   * Instantiates a new generalize group.
   *
   * @param path the path
   * @param patternsToUse the patterns to use
   */
  public GeneralizeGroup(String path, String outputPath, int[] patternsToUse,
      AresMeasurement measurement, ReferenceConfiguration writeReference) {
    this(path, outputPath, false, patternsToUse, false, measurement, writeReference);
  }



  /**
   * Instantiates a new generalize group.
   *
   * @param path the path
   * @param filteredPath the filtered path
   * @param patternsToUse the patterns to use
   * @param reevaluateOrder the reevaluate order
   */
  public GeneralizeGroup(String path, boolean filteredPath, int[] patternsToUse,
      boolean reevaluateOrder) {
    this(path, path, filteredPath, patternsToUse, reevaluateOrder, null);
  }

  /**
   * Instantiates a new generalize group.
   *
   * @param path the path
   * @param filteredPath the filtered path
   * @param patternsToUse the patterns to use
   * @param reevaluateOrder the reevaluate order
   */
  private GeneralizeGroup(String path, String outputPath, boolean filteredPath, int[] patternsToUse,
      boolean reevaluateOrder, AresMeasurement measurement) {
    this(path, outputPath, filteredPath, patternsToUse, reevaluateOrder,
        ReferenceConfiguration.KEEP_REFERENCE, measurement);
  }

  /**
   * Instantiates a new generalize group.
   *
   * @param path the path
   * @param filteredPath the filtered path
   * @param patternsToUse the patterns to use
   * @param reevaluateOrder the reevaluate order
   */
  private GeneralizeGroup(String path, String outputPath, boolean filteredPath, int[] patternsToUse,
      boolean reevaluateOrder, AresMeasurement measurement, ReferenceConfiguration writeReference) {
    this(path, outputPath, filteredPath, patternsToUse, reevaluateOrder, writeReference,
        measurement);
  }

  private GeneralizeGroup(String path, String outputPath, boolean filteredPath, int[] patternsToUse,
      boolean reevaluateOrder, ReferenceConfiguration writeReference, AresMeasurement measurement) {
    this.path = path;
    this.outputPath = outputPath;
    this.reevaluateOrder = reevaluateOrder;
    if (filteredPath) {
      this.filteredPath = path + "/filtered/";
    }
    this.writeReference = writeReference;
    File dir = new File(path);
    File[] filesOriginal = new File[patternsToUse.length];
    File[] filesModified = new File[patternsToUse.length];
    for (int i = 0; i < patternsToUse.length; i++) {
      filesOriginal[i] = new File(dir.getAbsoluteFile() + "/" + changeFilename + "_"
          + patternsToUse[i] + "_" + originalPartName + EDIT_SCRIPT_EXTENSION);
      filesModified[i] = new File(dir.getAbsoluteFile() + "/" + changeFilename + "_"
          + patternsToUse[i] + "_" + modifiedPartName + EDIT_SCRIPT_EXTENSION);
    }
    progsOriginal = new BastProgram[filesOriginal.length];
    progsModified = new BastProgram[filesOriginal.length];
    for (int i = 0; i < filesOriginal.length; i++) {
      progsOriginal[i] = ParserFactory.getParserInstance(AresExtension.WITH_ARES_EXTENSIONS)
          .parse(filesOriginal[i]);
      progsModified[i] = ParserFactory.getParserInstance(AresExtension.WITH_ARES_EXTENSIONS)
          .parse(filesModified[i]);
    }
    this.measurement = measurement;
  }

  /**
   * Instantiates a new generalize group.
   *
   * @param path the path
   * @param filteredPath the filtered path
   * @param reevaluateOrder the reevaluate order
   */
  public GeneralizeGroup(String path, boolean filteredPath, boolean reevaluateOrder) {
    this(path, path, filteredPath, reevaluateOrder);
  }

  /**
   * Instantiates a new generalize group.
   *
   * @param path the path
   * @param filteredPath the filtered path
   * @param reevaluateOrder the reevaluate order
   */
  private GeneralizeGroup(String path, String outputPath, boolean filteredPath,
      boolean reevaluateOrder) {
    this.path = path;
    this.outputPath = outputPath;

    if (filteredPath) {
      this.filteredPath = path + "/filtered/";
    }
    this.reevaluateOrder = reevaluateOrder;
    int counter = 0;
    File dir = new File(path);
    while (true) {
      if (new File(dir.getAbsoluteFile() + "/" + patternFilename + "_" + counter + "_"
          + originalPartName + EDIT_SCRIPT_EXTENSION).exists()) {
        counter++;
      } else {
        break;
      }
    }
    File reorder = new File(this.filteredPath + "/ORDER");
    int[] patternsToUse;
    if (reorder.exists()) {
      try {
        List<String> content = Files.readAllLines(reorder.toPath(), StandardCharsets.UTF_8);
        String line = content.get(0);
        String[] values = line.split(",");

        patternsToUse = new int[values.length];
        for (int j = 0; j < values.length; j++) {
          patternsToUse[j] = Integer.valueOf(values[j].trim());
        }
      } catch (IOException e) {
        e.printStackTrace();
        patternsToUse = null;
      }
    } else {
      patternsToUse = new int[counter];
      for (int j = 0; j < counter; j++) {
        patternsToUse[j] = j;
      }
    }
    if (patternsToUse != null) {
      File[] filesOriginal = new File[patternsToUse.length];
      File[] filesModified = new File[patternsToUse.length];
      for (int j = 0; j < patternsToUse.length; j++) {
        filesOriginal[j] = new File(dir.getAbsoluteFile() + "/" + changeFilename + "_"
            + patternsToUse[j] + "_" + originalPartName + EDIT_SCRIPT_EXTENSION);
        filesModified[j] = new File(dir.getAbsoluteFile() + "/" + changeFilename + "_"
            + patternsToUse[j] + "_" + modifiedPartName + EDIT_SCRIPT_EXTENSION);
      }
      progsOriginal = new BastProgram[filesOriginal.length];
      progsModified = new BastProgram[filesOriginal.length];
      for (int j = 0; j < filesOriginal.length; j++) {
        progsOriginal[j] = ParserFactory.getParserInstance(AresExtension.WITH_ARES_EXTENSIONS)
            .parse(filesOriginal[j]);
        progsModified[j] = ParserFactory.getParserInstance(AresExtension.WITH_ARES_EXTENSIONS)
            .parse(filesModified[j]);
      }
    } else {
      progsOriginal = null;
      progsModified = null;
    }
  }

  /**
   * Instantiates a new generalize group.
   *
   * @param path the path
   * @param reevaluateOrder the reevaluate order
   */
  public GeneralizeGroup(String path, boolean reevaluateOrder) {
    this(path, path, reevaluateOrder);
  }

  /**
   * Instantiates a new generalize group.
   *
   * @param path the path
   * @param reevaluateOrder the reevaluate order
   */
  public GeneralizeGroup(String path, String outputPath, boolean reevaluateOrder) {
    this.path = path;
    this.outputPath = outputPath;

    this.reevaluateOrder = reevaluateOrder;
    int index = 0;
    File dir = new File(path);
    while (true) {
      if (new File(dir.getAbsoluteFile() + "/" + changeFilename + "_" + index + "_"
          + originalPartName + "" + EDIT_SCRIPT_EXTENSION).exists()) {
        index++;
      } else {
        break;
      }
    }
    int[] patternsToUse = new int[index];
    for (int j = 0; j < index; j++) {
      patternsToUse[j] = j;
    }
    File[] filesOriginal = new File[patternsToUse.length];
    File[] filesModified = new File[patternsToUse.length];
    for (int j = 0; j < patternsToUse.length; j++) {
      filesOriginal[j] = new File(dir.getAbsoluteFile() + "/" + changeFilename + "_"
          + patternsToUse[j] + "_" + originalPartName + "" + EDIT_SCRIPT_EXTENSION);
      filesModified[j] = new File(dir.getAbsoluteFile() + "/" + changeFilename + "_"
          + patternsToUse[j] + "_" + modifiedPartName + "" + EDIT_SCRIPT_EXTENSION);
    }
    progsOriginal = new BastProgram[filesOriginal.length];
    progsModified = new BastProgram[filesOriginal.length];
    for (int j = 0; j < filesOriginal.length; j++) {
      progsOriginal[j] = ParserFactory.getParserInstance(AresExtension.WITH_ARES_EXTENSIONS)
          .parse(filesOriginal[j]);
      progsModified[j] = ParserFactory.getParserInstance(AresExtension.WITH_ARES_EXTENSIONS)
          .parse(filesModified[j]);
    }
  }

  /**
   * Next index.
   *
   * @param used the used
   * @param executioner the executioner
   * @param innerExecutioner the inner executioner
   * @return the int
   */
  public static int nextIndex(LinkedList<BastProgram> used, BastProgram[] progsOriginal,
      BastProgram[] progsModified, BastProgram patternOriginal, BastProgram patternModified,
      ExecutorService executioner, ExecutorService innerExecutioner) {
    int minIndex = -1;
    int minValue = Integer.MAX_VALUE;

    for (int i = 0; i < progsOriginal.length; i++) {
      if (!used.contains(progsOriginal[i])) {
        if (patternModified == null || patternOriginal == null) {
          return i;
        }
        ExtendedDiffResult extendedDiffResultOriginal =
            ExtendedTemplateExtractor.pipeline(patternOriginal, progsOriginal[i], executioner);
        ExtendedDiffResult extendedDiffResultModified =
            ExtendedTemplateExtractor.pipeline(patternModified, progsModified[i], executioner);
        if (extendedDiffResultOriginal.editScript.size()
            + extendedDiffResultModified.editScript.size() < minValue) {
          minValue = extendedDiffResultOriginal.editScript.size()
              + extendedDiffResultModified.editScript.size();
          minIndex = i;
        } else if (extendedDiffResultOriginal.editScript.size()
            + extendedDiffResultModified.editScript.size() == minValue) {
          if (minIndex == -1) {
            minIndex = i;
          }
        }
      }
    }
    return minIndex;
  }

  /**
   * Generate initial pair.
   *
   * @param executioner the executioner
   * @return the int[]
   */
  public static int[] generateInitialPair(BastProgram[] progsOriginal, BastProgram[] progsModified,
      ExecutorService executioner) {
    HashMap<BastProgram, Integer> int2DiffSize = new HashMap<>();
    HashMap<BastProgram, HashMap<BastProgram, Integer>> file2fileSize = new HashMap<>();

    for (int i = 0; i < progsOriginal.length; i++) {
      int2DiffSize.put(progsOriginal[i], 0);
      file2fileSize.put(progsOriginal[i], new HashMap<BastProgram, Integer>());
    }

    for (int i = 0; i < progsOriginal.length; i++) {
      for (int j = i + 1; j < progsOriginal.length; j++) {
        ExtendedDiffResult extendedDiffResultOriginal =
            ExtendedTemplateExtractor.pipeline(progsOriginal[i], progsOriginal[j], executioner);
        ExtendedDiffResult extendedDiffResultModified =
            ExtendedTemplateExtractor.pipeline(progsModified[i], progsModified[j], executioner);
        int2DiffSize.put(progsOriginal[i],
            int2DiffSize.get(progsOriginal[i]) + extendedDiffResultOriginal.editScript.size());
        int2DiffSize.put(progsOriginal[j],
            int2DiffSize.get(progsOriginal[j]) + extendedDiffResultOriginal.editScript.size());
        int2DiffSize.put(progsOriginal[i],
            int2DiffSize.get(progsOriginal[i]) + extendedDiffResultModified.editScript.size());
        int2DiffSize.put(progsOriginal[j],
            int2DiffSize.get(progsOriginal[j]) + extendedDiffResultModified.editScript.size());
        file2fileSize.get(progsOriginal[i]).put(progsOriginal[j],
            extendedDiffResultOriginal.editScript.size()
                + extendedDiffResultModified.editScript.size());
        file2fileSize.get(progsOriginal[j]).put(progsOriginal[i],
            extendedDiffResultOriginal.editScript.size()
                + extendedDiffResultModified.editScript.size());
      }
    }
    int[] result = new int[2];
    int minValueBase = Integer.MAX_VALUE;
    BastProgram minIndexBase = null;

    for (int i = 0; i < progsOriginal.length; i++) {
      if (minValueBase > int2DiffSize.get(progsOriginal[i])) {
        minValueBase = int2DiffSize.get(progsOriginal[i]);
        minIndexBase = progsOriginal[i];
      } else if (minValueBase == int2DiffSize.get(progsOriginal[i])) {
        if (minIndexBase == null) {
          minIndexBase = progsOriginal[i];
        }
      }
    }

    int minValueNext = Integer.MAX_VALUE;
    BastProgram minIndexNext = null;
    for (int i = 0; i < progsOriginal.length; i++) {
      if (progsOriginal[i] != minIndexBase) {
        if (minValueNext > file2fileSize.get(minIndexBase).get(progsOriginal[i])) {
          minValueNext = file2fileSize.get(minIndexBase).get(progsOriginal[i]);
          minIndexNext = progsOriginal[i];
        } else if (minValueNext == file2fileSize.get(minIndexBase).get(progsOriginal[i])) {
          if (minIndexNext == null) {
            minIndexNext = progsOriginal[i];
          }
        }
      }
    }
    assert (minIndexBase != null);
    assert (minIndexNext != null);
    assert (minIndexBase != minIndexNext);
    for (int i = 0; i < progsOriginal.length; i++) {
      if (progsOriginal[i] == minIndexBase) {
        result[0] = i;
      } else if (progsOriginal[i] == minIndexNext) {
        result[1] = i;
      }
    }
    return result;
  }

  /**
   * Run.
   */
  public void run() {
    ExecutorService executioner = Executors.newFixedThreadPool(NUM_THREADS);
    ExecutorService innerExecutioner = Executors.newFixedThreadPool(NUM_THREADS);

    run(executioner, innerExecutioner, false);
    executioner.shutdown();
  }

  /**
   * Run.
   *
   * @param executioner the executioner
   * @param innerExecutioner the inner executioner
   * @param evaluateFiltered the evaluate filtered
   */
  public void run(ExecutorService executioner, ExecutorService innerExecutioner,
      boolean evaluateFiltered) {
    File dirY = null;
    if (filteredPath != null) {
      dirY = new File(filteredPath);
      if (!evaluateFiltered) {
        byte[] buffer = null;
        BufferedInputStream istream = null;

        try {
          buffer = new byte[(int) (new File(dirY.getAbsoluteFile() + "/" + patternFilename + "_"
              + modifiedPartName + EDIT_SCRIPT_EXTENSION).length())];

          istream = new BufferedInputStream(new FileInputStream(dirY.getAbsoluteFile() + "/"
              + patternFilename + "_" + originalPartName + EDIT_SCRIPT_EXTENSION));
          istream.read(buffer);
          String str = new String(buffer, "UTF-8");
          if (str.startsWith("UNNECESSARY") || str.startsWith("OVERGENERALIZATION")) {
            return;
          }

        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          if (istream != null) {
            try {
              istream.close();
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
          if (new File(dirY.getAbsoluteFile() + "/MANUAL").exists()) {
            return;
          }
        }
      }
    } else {
      dirY = new File(outputPath);
    }

    IPrettyPrinter printer = null;
    printer = ParserFactory.getAresPrettyPrinter();

    AbstractBastNode initOriginal = null;
    AbstractBastNode initModified = null;
    LinkedList<BastProgram> used = null;
    LinkedList<Integer> usedIndex = null;

    try {
      if (!reevaluateOrder) {
        initOriginal = progsOriginal[0];
        initModified = progsModified[0];
        for (int i = 1; i < progsOriginal.length; i++) {
          AbstractBastNode[] tmp =
              PatternGenerator.generatePattern(progsOriginal[i], initOriginal,
                  progsModified[i], initModified, executioner, innerExecutioner, true, measurement);
          if (tmp == null) {
            failed = true;
            return;
          }
          initOriginal = tmp[0];
          initModified = tmp[1];
        }
      } else {
        long timeOrder = System.nanoTime();
        int[] initialOrder = generateInitialPair(progsOriginal, progsModified, executioner);
        if (measurement != null) {
          measurement.timeOrderCreation.add(System.nanoTime() - timeOrder);
        }
        initOriginal = progsOriginal[initialOrder[0]];
        initModified = progsModified[initialOrder[0]];
        if (DEBUG) {
          System.err.println("REORDER Base: " + initialOrder[0]);
          System.err.println("REORDER Next 1: " + initialOrder[1]);
        }
        used = new LinkedList<>();
        usedIndex = new LinkedList<>();
        used.add(progsOriginal[initialOrder[0]]);
        used.add(progsOriginal[initialOrder[1]]);
        usedIndex.add(initialOrder[0]);
        usedIndex.add(initialOrder[1]);
        AbstractBastNode[] tmp = PatternGenerator.generatePattern(
            progsOriginal[initialOrder[1]], initOriginal, progsModified[initialOrder[1]],
            initModified, executioner, innerExecutioner, true, measurement);
        if (tmp == null) {
          failed = true;
          return;
        }
        initOriginal = tmp[0];
        initModified = tmp[1];
        while (used.size() < progsOriginal.length) {
          timeOrder = System.nanoTime();
          int next = nextIndex(used, progsOriginal, progsModified, (BastProgram) initOriginal,
              (BastProgram) initModified, executioner, innerExecutioner);
          if (measurement != null) {
            measurement.timeOrderCreation.add(System.nanoTime() - timeOrder);
          }
          assert (next != -1);
          if (DEBUG) {

            System.err.println("REORDER Next :" + used.size() + " " + next);
          }

          used.add(progsOriginal[next]);
          usedIndex.add(next);
          tmp = PatternGenerator.generatePattern(progsOriginal[next], initOriginal,
              progsModified[next], initModified, executioner, innerExecutioner, true, measurement);
          if (tmp == null) {
            failed = true;
            return;
          }
          initOriginal = tmp[0];
          initModified = tmp[1];
        }

      }
      initOriginal.accept(printer);
      generalizedPatternOriginalRes = printer.getBuffer().toString();
      printer.print(new File(dirY.getAbsoluteFile() + "/" + patternFilename + "_" + originalPartName
          + EDIT_SCRIPT_EXTENSION + ".pretty"));
      if (writeReference == ReferenceConfiguration.WRITE_REFERENCE) {
        if (generalizedPatternOriginalRes.length() != 0) {
          printer.print(new File(dirY.getAbsoluteFile() + "/" + patternFilename + "_"
              + originalPartName + EDIT_SCRIPT_EXTENSION));
        }

      }
      printer = ParserFactory.getAresPrettyPrinter();
      initModified.accept(printer);
      generalizedPatternModifiedRes = printer.getBuffer().toString();
      printer.print(new File(dirY.getAbsoluteFile() + "/" + patternFilename + "_" + modifiedPartName
          + "" + EDIT_SCRIPT_EXTENSION + ".pretty"));
      if (writeReference == ReferenceConfiguration.WRITE_REFERENCE) {
        if (generalizedPatternModifiedRes.length() != 0) {
          printer.print(new File(dirY.getAbsoluteFile() + "/" + patternFilename + "_"
              + modifiedPartName + "" + EDIT_SCRIPT_EXTENSION));
        }

      }
    } catch (GeneralizationException ex) {
      String constant = "OVERGENERALIZATION! " + ex.getMessage();
      generalizedPatternOriginalRes = constant;
      generalizedPatternModifiedRes = constant;
      writeOvergeneralization(dirY, ex, writeReference);
    }
    byte[] buffer = null;
    BufferedInputStream istream = null;
    File gb = new File(dirY.getAbsoluteFile() + "/" + patternFilename + "_" + originalPartName
        + EDIT_SCRIPT_EXTENSION);
    if (!gb.exists()) {
      try {
        gb.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    try {
      buffer = new byte[(int) (new File(dirY.getAbsoluteFile() + "/" + patternFilename + "_"
          + originalPartName + EDIT_SCRIPT_EXTENSION).length())];

      istream = new BufferedInputStream(new FileInputStream(dirY.getAbsoluteFile() + "/"
          + patternFilename + "_" + originalPartName + EDIT_SCRIPT_EXTENSION));
      istream.read(buffer);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (istream != null) {
        try {
          istream.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    if (buffer == null) {
      return;
    }
    generalizedPatternOriginalRef = new String(buffer);
    File ga = new File(dirY.getAbsoluteFile() + "/" + patternFilename + "_" + modifiedPartName + ""
        + EDIT_SCRIPT_EXTENSION);
    if (!ga.exists()) {
      try {
        ga.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    try {
      buffer = new byte[(int) (new File(dirY.getAbsoluteFile() + "/" + patternFilename + "_"
          + modifiedPartName + "" + EDIT_SCRIPT_EXTENSION).length())];
      istream = new BufferedInputStream(new FileInputStream(dirY.getAbsoluteFile() + "/"
          + patternFilename + "_" + modifiedPartName + "" + EDIT_SCRIPT_EXTENSION));
      istream.read(buffer);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (istream != null) {
        try {
          istream.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    generalizedPatternModifiedRef = new String(buffer);
    if (reevaluateOrder) {
      if (DEBUG) {
        System.out.print("\nNew Order:");
        for (int i = 0; i < used.size(); i++) {
          System.out.print(usedIndex.get(i) + ", ");
        }
      }

      return;
    }
    return;
  }

  /**
   * Write overgeneralization.
   *
   * @param dirY the dir y
   * @param ex the ex
   * @param writeReference the write reference
   */
  public static void writeOvergeneralization(File dirY, GeneralizationException ex,
      ReferenceConfiguration writeReference) {
    try {
      String constant = "OVERGENERALIZATION! " + (ex != null ? ex.getMessage() : "");
      Writer output =
          new BufferedWriter(new FileWriter(new File(dirY.getAbsoluteFile() + "/" + patternFilename
              + "_" + originalPartName + "" + EDIT_SCRIPT_EXTENSION + ".pretty"), false));
      Writer output2 = null;
      if (writeReference == ReferenceConfiguration.WRITE_REFERENCE) {
        output2 = new BufferedWriter(new FileWriter(new File(dirY.getAbsoluteFile() + "/"
            + patternFilename + "_" + originalPartName + "" + EDIT_SCRIPT_EXTENSION), false));
      }
      try {
        output.write(constant);
        if (writeReference == ReferenceConfiguration.WRITE_REFERENCE) {
          output2.write(constant);
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {

        output.close();
        if (writeReference == ReferenceConfiguration.WRITE_REFERENCE) {
          output2.close();
        }
      }
      output =
          new BufferedWriter(new FileWriter(new File(dirY.getAbsoluteFile() + "/" + patternFilename
              + "_" + modifiedPartName + "" + EDIT_SCRIPT_EXTENSION + ".pretty"), false));
      if (writeReference == ReferenceConfiguration.WRITE_REFERENCE) {
        output2 = new BufferedWriter(new FileWriter(new File(dirY.getAbsoluteFile() + "/"
            + patternFilename + "_" + modifiedPartName + "" + EDIT_SCRIPT_EXTENSION), false));
      }
      try {
        output.write(constant);
        if (writeReference == ReferenceConfiguration.WRITE_REFERENCE) {
          output2.write(constant);
        }
      } catch (Exception e) {
        e.printStackTrace();
      } finally {

        output.close();
        if (writeReference == ReferenceConfiguration.WRITE_REFERENCE) {
          output2.close();
        }
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  /**
   * Execute generalization.
   *
   * @param executioner the executioner
   * @param innerExecutioner the inner executioner
   * @param subDir the sub dir
   * @param secondProgramOriginal the second program original
   * @param secondProgramModified the second program modified
   * @param aresMeasurement the ares measurement
   * @param writeConfiguration the write configuration
   */
  public static void executeGeneralization(ExecutorService executioner,
      ExecutorService innerExecutioner, AbstractBastNode initOriginal,
      AbstractBastNode initModified, File subDir, BastProgram secondProgramOriginal,
      BastProgram secondProgramModified, AresMeasurement aresMeasurement,
      ReferenceConfiguration writeConfiguration) {
    try {
      IPrettyPrinter printer = ParserFactory.getAresPrettyPrinter();
      AbstractBastNode[] initialResult = PatternGenerator.generatePattern(
          secondProgramOriginal, initOriginal, secondProgramModified, initModified, executioner,
          innerExecutioner, true, aresMeasurement);
      initOriginal = initialResult[0];
      initModified = initialResult[1];
      initOriginal.accept(printer);
      printer.print(new File(subDir.getAbsoluteFile() + "/" + patternFilename + "_"
          + originalPartName + ".java" + ".pretty"));
      if (writeConfiguration == ReferenceConfiguration.WRITE_REFERENCE) {
        printer.print(new File(
            subDir.getAbsoluteFile() + "/" + patternFilename + "_" + originalPartName + ".java"));
      }
      printer = ParserFactory.getAresPrettyPrinter();
      initModified.accept(printer);
      printer.print(new File(subDir.getAbsoluteFile() + "/" + patternFilename + "_"
          + modifiedPartName + ".java" + ".pretty"));
      if (writeConfiguration == ReferenceConfiguration.WRITE_REFERENCE) {
        printer.print(new File(
            subDir.getAbsoluteFile() + "/" + patternFilename + "_" + modifiedPartName + ".java"));
      }
    } catch (GeneralizationException e) {
      e.printStackTrace();
      GeneralizeGroup.writeOvergeneralization(subDir, e, ReferenceConfiguration.KEEP_REFERENCE);
    }
  }
}
