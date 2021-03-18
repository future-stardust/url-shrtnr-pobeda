package edu.kpi.testcourse.exception.auth;

/**
 * Exception for invalid user data (email and password).
 */
public class InvalidCredentialsException extends RuntimeException {
  public InvalidCredentialsException(String message) {
    super(message);
  }
}

