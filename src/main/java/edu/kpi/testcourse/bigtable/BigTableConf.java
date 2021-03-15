package edu.kpi.testcourse.bigtable;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration object for BigTable.
 * Contains path for storage directory.
 */
public final class BigTableConf {

  // Actual path to storage
  public Path path;

  /**
   * Simple constructor.
   * Converts String path to Path object.
   *
   * @param path to storage
   */
  public BigTableConf(String path) {
    this.path = Paths.get(path);
  }

}
