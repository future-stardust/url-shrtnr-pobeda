package edu.kpi.testcourse.exception.url;

/**
 * Throw this exception if user provides a wrong URL to shorten.
 */
public class InvalidUrlException extends RuntimeException {
  public InvalidUrlException(String m) {
    super(m);
  }
}
