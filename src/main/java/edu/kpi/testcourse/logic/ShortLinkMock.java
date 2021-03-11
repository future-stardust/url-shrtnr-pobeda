package edu.kpi.testcourse.logic;

import io.micronaut.core.annotation.Introspected;
import java.net.URL;

/**
 * Mock data for short link record in the data storage.
 */
@Introspected
public record ShortLinkMock(
    String shortLink,
    String userEmail,
    URL destination
) {}
