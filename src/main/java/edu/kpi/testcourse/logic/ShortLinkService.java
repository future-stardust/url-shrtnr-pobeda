package edu.kpi.testcourse.logic;

import edu.kpi.testcourse.dto.ShortLink;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Business-logic class to provide read-write access to short links.
 */
public interface ShortLinkService {
  Optional<String> getDestinationByShortLink(String shortLink);

  String generateAlias();

  boolean isUrlValid(String destination);

  boolean deleteLinkIfBelongsToUser(String email, String destination);

  ShortLink saveLink(String userEmail, String destination);

  ShortLink saveLink(String userEmail, String destination, String alias);

  ArrayList<ShortLink> getLinksByUserEmail(String userEmail);
}
