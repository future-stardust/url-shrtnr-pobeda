package edu.kpi.testcourse.bigtable;


// Please, pay attention, that you should not use any 3rd party persistence library (e.g. data
// base, implementation of key-value storage, etc)

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
  void store(String name, String value);

  /**
   * Reads and deserializes file if found.
   *
   * @param name filename
   * @return json string
   */
  String read(String name);

  /**
   * Deletes file with provided filename.
   *
   * @param name filename
   */
  void delete(String name);
}
