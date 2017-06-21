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
