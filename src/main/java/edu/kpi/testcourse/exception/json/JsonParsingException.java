package edu.kpi.testcourse.exception.json;

/**
 * JSON parsing error.
 */
public class JsonParsingException extends IllegalArgumentException {
  public JsonParsingException(Throwable e) {
    super("Error during JSON parsing", e);
  }
}
