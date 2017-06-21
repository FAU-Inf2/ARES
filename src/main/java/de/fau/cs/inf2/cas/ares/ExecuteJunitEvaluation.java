package de.fau.cs.inf2.cas.ares;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.fau.cs.inf2.cas.ares.io.AresMapper;
import de.fau.cs.inf2.cas.ares.io.RecommendationFile;
import de.fau.cs.inf2.cas.ares.io.RecommendationResult;
import de.fau.cs.inf2.cas.ares.pipeline.CthreeProcessing;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class ExecuteJunitEvaluation {
  public static final int NUM_THREADS = 8;
  public static final String INPUT_DEFINITION = "data/junit_evaluation/input.json";

  /**
   * The main method.
   *
   * @param args the arguments
   */
  public static void main(String[] args) {
    File cthreeFile = new File(INPUT_DEFINITION);
    if (!cthreeFile.exists()) {
      System.out.format("Could not find input file.");
      System.exit(-1);
    }
    File tmpDir = new File("/tmp/ARES");
    if (args.length > 0 && args[0] != null) {
      tmpDir = new File(args[0]);
    }
    if (tmpDir.exists() && !tmpDir.isDirectory()) {
      System.out.format("Temporary path (%s) is no directory.", args[0]);
      System.exit(-1);
    } else if (!tmpDir.exists()) {
      boolean createSuccessful = tmpDir.mkdirs();
      if (!createSuccessful) {
        System.out.format("Could not create temporary directory (%s).", args[0]);
        System.exit(-1);
      }
    }
    
    CthreeProcessing.handleGroupPart(cthreeFile, tmpDir, NUM_THREADS);
    writeJson(tmpDir);
  }

  private static void writeJson(File tmpDir) {
    String[] files = new File(tmpDir, "/results/").list();
    ObjectMapper aresMapper = AresMapper.createJsonMapper();
    List<RecommendationResult> aresResults = new LinkedList<>();
    final File completeAresFile = new File(tmpDir, "/results/ares_junit_results.json");

    if (files != null) {
      for (String folderName : files) {
        File folder = new File(tmpDir, "/results/" + folderName);
        if (folder.isDirectory()) {
          File jsonFile = new File(folder, "/" + "recall_result.json");
          if (jsonFile.exists()) {
            RecommendationResult result = null;
            try {
              result = aresMapper.readValue(jsonFile, RecommendationResult.class);
              if (result != null) {
                aresResults.add(result);
              }
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        }
      }
      RecommendationFile aresFile = new RecommendationFile(aresResults);
      try {
        aresMapper.writeValue(completeAresFile, aresFile);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
  
  
}

