package edu.kpi.testcourse.logic;

import java.net.URL;

record ShortLinkMock(
    String shortLink,
    String userEmail,
    URL destination
) {}
