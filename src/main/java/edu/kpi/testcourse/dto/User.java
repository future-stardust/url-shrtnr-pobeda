package edu.kpi.testcourse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Dto for User.
 */
public record User(@JsonProperty("email") String email, @JsonProperty("password") String password) {

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
