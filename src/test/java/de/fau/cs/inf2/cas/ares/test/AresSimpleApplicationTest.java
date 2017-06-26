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

package de.fau.cs.inf2.cas.ares.test;

import static org.junit.Assert.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import de.fau.cs.inf2.cas.ares.io.AresMapper;
import de.fau.cs.inf2.cas.common.io.ReadableEncodedGroupFile;

import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;



@RunWith(DataProviderRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AresSimpleApplicationTest {

  public static final String SOURCE = "data/lase_evaluation/input.json";
  private static ReadableEncodedGroupFile groupFile;


  /**
   * One time set up.
   */
  @BeforeClass
  public static void oneTimeSetUp() {
    ObjectMapper mapper = AresMapper.createJsonMapper();
    try {
      groupFile = mapper.readValue(new File(SOURCE), ReadableEncodedGroupFile.class);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  
  /**
   * Returns the test data.
   *
   * @return the test data.
   */
  @DataProvider(format = "%i: %p[0]")
  public static Object[][] data() {
    return new Object[][] {
        { "data/examples/one" },
        { "data/examples/two" },
    };
  }
  

  @Test
  @UseDataProvider("data")
  public void updateTest(String folder) {
    File original = new File(folder, "Pattern_original.java");
    File modified = new File(folder, "Pattern_modified.java");
    File test = new File(folder, "Test.java");
    File result = new File(folder, "Expected_result.java");
    double[] res = AresSimpleDefinition.runSimpleRecommendation(original, modified, test, result);
    evaluateFiles(folder);
  }
  
  private static void evaluateFiles(String folder) {
    StringBuilder refBuilder = new StringBuilder();
    StringBuilder resBuilder = new StringBuilder();
    String refBefore = getFileData(folder + "/Expected_result.java");
    refBuilder.append(refBefore);
    String resBefore = getFileData(folder + "/Expected_result.java.pretty");
    resBuilder.append(resBefore);
    assertEquals( refBuilder.toString(),
        resBuilder.toString());
  }
  
  private static String getFileData(String path) {
    byte[] buffer = null;
    BufferedInputStream istream = null;
    File gb = new File(path);
    if (!gb.exists()) {
      try {
        gb.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    try {
      buffer = new byte[(int) (new File(path).length())];

      istream = new BufferedInputStream(new FileInputStream(path));
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
      return null;
    }
    return new String(buffer);
  }

}
