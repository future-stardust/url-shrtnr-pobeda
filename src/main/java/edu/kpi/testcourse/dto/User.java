package edu.kpi.testcourse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Dto for User.
 */
public final class User {

  private String email;
  private String password;

  public User(String email, String password) {
    this.email = email;
    this.password = password;
  }

  public String email() {
    return email;
  }

  public String password() {
    return password;
  }

  /**
   * Check if user data is valid.
   *
   * @return true if data is valid, otherwise false
   */
  public boolean isValid() {
    return
      email != null && !email.isEmpty()
        && password != null && !password.isEmpty();
  }

}
