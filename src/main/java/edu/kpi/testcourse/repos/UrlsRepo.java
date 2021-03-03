package edu.kpi.testcourse.repos;

import edu.kpi.testcourse.except.UrlsException;
import java.util.Optional;

/**
 * Interface for abstraction of storage and managing Urls and Aliases.
 */
public interface UrlsRepo {

  /**
   * Persists provided data for url using BigTable.
   * Also adds alias to collection of ones of specified user.
   *
   * @param longUrl destination
   * @param shortUrl alias
   * @param userEmail assigned user
   * @throws UrlsException if specified alias already exists in the system
   */
  void storeUrl(String shortUrl, String longUrl, String userEmail) throws UrlsException;

  /**
   * Goes through stored urls and returns long url for specified shortened.
   *
   * @param shortUrl alias
   * @return destination
   */
  Optional<String> getDestination(String shortUrl);

  /**
   * Finds email of user associated with provided alias.
   *
   * @param shortUrl alias
   * @return users email
   */
  Optional<String> getUserEmail(String shortUrl);

}
