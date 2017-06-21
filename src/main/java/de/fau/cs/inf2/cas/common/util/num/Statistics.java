package de.fau.cs.inf2.cas.common.util.num;

import java.util.List;

/**
 * The Class Statistics.
 */
public class Statistics {
  public static final Double NANO = 1000000000d;

  /**
   * Creates the upper quartile.
   *
   * @param values the values
   * @return the double
   */
  public static Double getUpperQuartile(List<Long> values) {
    Double upperQuartile;
    if (values.size() % 2 == 0) {
      upperQuartile = (double) values.size() / 2 == 0
          ? ((double) values.get(values.size() / 2 / 2 + values.size() / 2)
              + values.get(values.size() / 2 / 2 + 1 + values.size() / 2)) / 2
          : (double) values.get(values.size() / 2 / 2 + 1 + values.size() / 2);
    } else {
      upperQuartile = (double) (values.size() - 1) / 2 == 0
          ? ((double) values.get((values.size() - 1) / 2 / 2 + values.size() / 2 + 1)
              + values.get((values.size() - 1) / 2 / 2 + 1 + values.size() / 2 + 1)) / 2
          : (double) values.get((values.size() - 1) / 2 / 2 + 1 + values.size() / 2 + 1);
    }
    return upperQuartile;
  }

  /**
   * Creates the lower quartile.
   *
   * @param values the values
   * @return the double
   */
  public static Double getLowerQuartile(List<Long> values) {
    Double lowerQuartile;
    if (values.size() % 2 == 0) {
      lowerQuartile = (double) values.size() / 2 == 0
          ? ((double) values.get(values.size() / 2 / 2) + values.get(values.size() / 2 / 2 + 1)) / 2
          : (double) values.get(values.size() / 2 / 2 + 1);

    } else {
      lowerQuartile = (double) (values.size() - 1) / 2 == 0
          ? ((double) values.get((values.size() - 1) / 2 / 2)
              + values.get((values.size() - 1) / 2 / 2 + 1)) / 2
          : (double) values.get((values.size() - 1) / 2 / 2 + 1);

    }
    return lowerQuartile;
  }

  /**
   * Creates the median.
   *
   * @param values the values
   * @return the double
   */
  public static Double getMedian(List<Long> values) {
    Double median = values.size() % 2 == 0
        ? ((double) values.get(values.size() / 2) + values.get(values.size() / 2 + 1)) / 2
        : (double) values.get(values.size() / 2 + 1);
    return median;
  }
}
