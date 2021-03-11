package edu.kpi.testcourse.bigtable;


// Please, pay attention, that you should not use any 3rd party persistence library (e.g. data
// base, implementation of key-value storage, etc)

import java.io.IOException;
import java.nio.file.Path;

/**
 * BigTable is a json storage.
 */
public interface BigTable {

  /**
   * Stores provided data within separated json file.
   *
   * @param name filename
   * @param value to store
   */
  void store(String name, String value) throws IOException;

  /**
   * Reads and deserializes file if found.
   *
   * @param name filename
   * @return json string
   */
  String read(String name) throws IOException;

  /**
   * Deletes file with provided filename.
   *
   * @param name filename
   */
  void delete(String name) throws IOException;

  /**
   * Returns Path of storage directory.
   */
  Path getDir();
}
