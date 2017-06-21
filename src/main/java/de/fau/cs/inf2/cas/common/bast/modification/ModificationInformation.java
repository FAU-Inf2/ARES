package de.fau.cs.inf2.cas.common.bast.modification;

import de.fau.cs.inf2.cas.common.bast.general.BastFieldConstants;

import java.util.HashMap;

public class ModificationInformation {
  public boolean nodeModified = false;
  public boolean childModified = false;
  public HashMap<BastFieldConstants, Integer> fieldLowerBorder = new HashMap<>();
  public HashMap<BastFieldConstants, Integer> fieldUpperBorder = new HashMap<>();
}
