package edu.kpi.testcourse.logic;

import java.net.URL;

/**
 * Mock data for short link record in the data storage.
 */
public record ShortLinkMock(
    String shortLink,
    String userEmail,
    URL destination
) {}
