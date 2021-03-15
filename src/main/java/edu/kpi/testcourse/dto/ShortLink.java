package edu.kpi.testcourse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import java.net.URL;

/**
 * Entity for short link record in the data storage.
 */
@Introspected
public record ShortLink(@JsonProperty("alias") String alias,
                        @JsonProperty("email") String email,
                        @JsonProperty("url") String url) {

}
