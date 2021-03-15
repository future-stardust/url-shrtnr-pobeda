package edu.kpi.testcourse.logic;

import edu.kpi.testcourse.dto.ShortLink;
import java.net.URL;
import java.util.Optional;

/**
 * Business-logic class to provide read-write access to short links.
 */
public interface ShortLinkService {
  Optional<String> getDestinationByShortLink(String shortLink);

  String generateAlias();

  boolean isUrlValid(String destination);

  ShortLink saveLink(String userEmail, String destination);

  ShortLink saveLink(String userEmail, String destination, String alias);
}
