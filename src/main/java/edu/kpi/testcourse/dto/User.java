package edu.kpi.testcourse.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;

/**
 * Dto for User.
 */
@Introspected
public record User(@JsonProperty("email") String email,
                   @JsonProperty("password") String password) {
  /**
   * Check if user data is valid.
   *
   * @return true if data is valid, otherwise false
   */
  @JsonIgnore
  public boolean isValid() {
    return
      email != null && !email.isEmpty()
        && password != null && !password.isEmpty();
  }
}
