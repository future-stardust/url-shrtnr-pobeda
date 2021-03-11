package edu.kpi.testcourse.exception.bigtable;

import java.io.IOException;

/**
 * Top level exception for all of IOExceptions from BigTable and BigTableManager.
 */
public class BigTableException extends RuntimeException {
  public BigTableException(IOException exception) {
    super(exception.getMessage(), exception);
  }
}
