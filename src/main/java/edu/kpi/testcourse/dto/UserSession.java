package edu.kpi.testcourse.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import java.util.UUID;

/**
 * Dto for User.
 */
@Introspected
public record UserSession(@JsonProperty("id") UUID id,
                          @JsonProperty("userEmail") String userEmail,
                          @JsonProperty("token") String token) {
  /**
   * Check if users session data is valid.
   *
   * @return true if data is valid, otherwise false
   */
  @JsonIgnore
  public boolean isValid() {
    return
      userEmail != null && !userEmail.isEmpty() && id != null
        && token != null && !token.isEmpty();
  }
}
