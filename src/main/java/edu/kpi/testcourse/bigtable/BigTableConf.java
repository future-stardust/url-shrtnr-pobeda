package edu.kpi.testcourse.bigtable;

import java.nio.file.Path;

/**
 * Configuration object for BigTable.
 * Contains path for storage directory.
 */
public final class BigTableConf {

  // Actual path to storage
  private final Path storagePath;

  /**
   * Simple constructor.
   *
   * @param path to storage
   */
  public BigTableConf(Path path) {
    storagePath = path;
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

  /**
   * Getter for sessions storage path.
   *
   * @return sessions forlder
   */
  public Path sessions() {
    return storagePath.resolve("sessions");
  }
}
