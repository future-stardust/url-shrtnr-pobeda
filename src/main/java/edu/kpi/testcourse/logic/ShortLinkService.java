package edu.kpi.testcourse.logic;

import edu.kpi.testcourse.dto.ShortLink;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Business-logic class to provide read-write access to short links.
 */
public interface ShortLinkService {
  /**
   * Retrieve "long version" of link by short link.
   *
   * @param shortLink short link
   * @return Optional - "long link" if it had been found in the storage
   */
  Optional<String> getDestinationByShortLink(String shortLink);

  /**
   * Randomly generate alias for long link.
   *
   * @return alias - 8-digits alphanumeric sequence consisting of characters [0-9a-zA-Z]
   */
  String generateAlias();

  /**
   * Check validity of URL.
   *
   * @param destination URL that should be validated
   * @return is a 'destination' parameter a valid URL
   */
  boolean isUrlValid(String destination);

  /**
   * Delete link entity by alias if belongs to a user with given email.
   *
   * @param email user's email
   * @param shortLink link's alias
   * @return if a link has been returned
   */
  boolean deleteLinkIfBelongsToUser(String email, String shortLink);

  /**
   * Create and save user link without custom alias provided.
   * Generates alias automatically.
   *
   * @param userEmail email of user link belongs to
   * @param destination "long" link alias must be provided for
   * @return a link that has been created
   */
  ShortLink saveLink(String userEmail, String destination);

  /**
   * Create and save user link with custom alias provided.
   *
   * @param userEmail email of user link belongs to
   * @param destination "long" link alias must be provided for
   * @param alias custom user alias for a "long" link
   * @return a link that has been created
   */
  ShortLink saveLink(String userEmail, String destination, String alias);

  /**
   * Get links created by a user.
   *
   * @param email email of user
   * @return list of user's links
   */
  ArrayList<ShortLink> getLinksByUserEmail(String email);
}
