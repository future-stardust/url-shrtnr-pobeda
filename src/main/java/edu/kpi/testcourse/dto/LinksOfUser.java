package edu.kpi.testcourse.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.core.annotation.Introspected;
import java.util.ArrayList;

/**
 * DTO for urls of user in GET /urls.
 */
@Introspected
public record LinksOfUser(@JsonProperty("urls") ArrayList<ShortLink> urls) {}
