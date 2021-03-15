package edu.kpi.testcourse.bigtable;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Configuration object for BigTable.
 * Contains path for storage directory.
 */
public final class BigTableConf {

  // Actual path to storage
  private final Path storagePath;

  /**
   * Simple constructor.
   * Converts String path to Path object.
   *
   * @param path to storage
   */
  public BigTableConf(String path) {
    storagePath = Paths.get(path);
  }

  /**
   * Getter for main storage path.
   *
   * @return storage folder
   */
  public Path storage() {
    return storagePath;
  }

  /**
   * Getter for urls storage path.
   *
   * @return links folder
   */
  public Path links() {
    return storagePath.resolve("links");
  }

  /**
   * Getter for users storage path.
   *
   * @return users forlder
   */
  public Path users() {
    return storagePath.resolve("users");
  }
}
