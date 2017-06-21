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

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(DataProviderRunner.class)
public class AresSimpleGeneralizationTest {


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

  /**
   * Returns the test data.
   *
   * @return the test data.
   */
  @DataProvider(format = "after - %i: %p[0]")
  public static Object[][] dataAfter() {
    return new Object[][] { 
        { "data/examples/one" },
        { "data/examples/two" },
    };
  }

  @Test
  @UseDataProvider("dataAfter")
  public void testModified(String folder) {
    GeneralizationDefinition test = new GeneralizationDefinition(folder,
        folder, new int[]{0,1});
    test.run();
    assertEquals(test.generalizedPatternModifiedRef(), test.generalizedPatternModifiedRes());
  }
  
  @Test
  @UseDataProvider("data")
  public void testOriginal(String folder) {
    GeneralizationDefinition test = new GeneralizationDefinition(folder,
        folder, new int[]{0,1});
    test.run();
    assertEquals(test.generalizedPatternOriginalRef(), test.generalizedPatternOriginalRes());
  }

}
