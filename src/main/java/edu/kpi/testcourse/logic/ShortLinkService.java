package edu.kpi.testcourse.logic;

import java.net.URL;
import java.util.Optional;

/**
 * Business-logic class to provide read-write access to short links.
 */
public interface ShortLinkService {
  Optional<URL> getDestinationByShortLink(String shortLink);

  String generateAlias();

  Optional<URL> safelyCreateUrl(String destination);

  ShortLinkMock saveLink(String userEmail, String destination);

  ShortLinkMock saveLink(String userEmail, String destination, String alias);
}
